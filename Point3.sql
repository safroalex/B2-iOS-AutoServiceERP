--DROP PROCEDURE IF EXISTS get_avg_service_cost_per_car;
--DROP TRIGGER IF EXISTS trigger_check_work_date_change ON works;


--Услуги, по которым сумма превысила некоторую границу (например, 1500) --
CREATE VIEW HighCostServices AS
SELECT 
    services.id,
    services.name,
    SUM(CASE WHEN cars.is_foreign THEN services.cost_foreign ELSE services.cost_our END) AS total_cost
FROM 
    services
JOIN 
    works ON services.id = works.service_id
JOIN 
    cars ON works.car_id = cars.id
GROUP BY 
    services.id, services.name
HAVING 
    SUM(CASE WHEN cars.is_foreign THEN services.cost_foreign ELSE services.cost_our END) > 1500;

SELECT * FROM HighCostServices;
SELECT pg_get_viewdef('HighCostServices', true);


--Общий доход мастеров за последний год
CREATE VIEW MastersIncomeLastYear AS
SELECT 
    masters.id,
    masters.name,
    COALESCE(SUM(CASE WHEN cars.is_foreign THEN services.cost_foreign ELSE services.cost_our END), 0) AS total_income
FROM 
    masters
LEFT JOIN 
    works ON masters.id = works.master_id
LEFT JOIN 
    services ON works.service_id = services.id
LEFT JOIN 
    cars ON works.car_id = cars.id
WHERE 
    works.date_work >= current_date - INTERVAL '1 year' OR works.date_work IS NULL
GROUP BY 
    masters.id, masters.name;

SELECT * FROM MastersIncomeLastYear;
SELECT pg_get_viewdef('MastersIncomeLastYear', true);
SELECT table_name FROM information_schema.views WHERE table_schema = 'public';



-- без параметров
CREATE OR REPLACE FUNCTION get_avg_service_cost_for_all_cars()
RETURNS TABLE (car_id INT, car_num VARCHAR, avg_cost NUMERIC) AS $$
BEGIN
  RETURN QUERY
    SELECT 
        cars.id AS car_id, 
        cars.num AS car_num,
        COALESCE(AVG(CASE WHEN cars.is_foreign THEN services.cost_foreign ELSE services.cost_our END), 0) AS avg_cost
    FROM 
        cars
    LEFT JOIN 
        works ON cars.id = works.car_id
    LEFT JOIN 
        services ON works.service_id = services.id
    GROUP BY 
        cars.id, cars.num;
END;
$$ LANGUAGE plpgsql;

-- Проверка
SELECT * FROM get_avg_service_cost_for_all_cars();

-- с входными параметрами
CREATE OR REPLACE FUNCTION calculate_service_cost_for_car(p_service_id INT, p_car_id INT)
RETURNS TABLE (total_cost NUMERIC, total_works INT) AS $$
BEGIN
    RETURN QUERY
    SELECT CAST(SUM(s.cost_our) AS NUMERIC) AS total_cost, COUNT(*)::INT AS total_works
    FROM works w
    JOIN services s ON w.service_id = s.id
    WHERE w.service_id = p_service_id AND w.car_id = p_car_id;
END;
$$ LANGUAGE plpgsql;

-- Проверка
SELECT * FROM calculate_service_cost_for_car(1, 1);  -- Для услуги с ID=1 и машины с ID=1

-- с выходными параметрами
CREATE OR REPLACE FUNCTION get_common_services_count(p_master1_id INT, p_master2_id INT, OUT common_services_count INT)
LANGUAGE plpgsql
AS $$
BEGIN
    SELECT COUNT(DISTINCT w1.service_id)
    INTO common_services_count
    FROM works w1
    JOIN works w2 ON w1.service_id = w2.service_id
    WHERE w1.master_id = p_master1_id AND w2.master_id = p_master2_id;
END;
$$;

-- Проверка
DO $$ 
DECLARE 
    common_count INT;
BEGIN
    SELECT * INTO common_count FROM get_common_services_count(1, 2);
    RAISE NOTICE 'Common services count: %', common_count;
END $$;




-- Функция
CREATE OR REPLACE FUNCTION check_unique_car_num()
RETURNS TRIGGER AS $$
BEGIN
  IF EXISTS (SELECT 1 FROM cars WHERE num = NEW.num) THEN
    RAISE EXCEPTION 'Автомобиль с таким номером уже существует';
  END IF;
  RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Триггер
CREATE TRIGGER trigger_check_unique_car_num
BEFORE INSERT ON cars
FOR EACH ROW EXECUTE FUNCTION check_unique_car_num();

-- Проверка
INSERT INTO CARS (num, color, mark, is_foreign) VALUES ('A123BC', 'Зеленый', 'Nissan', 't');


-- Функция
CREATE OR REPLACE FUNCTION check_work_date_change()
RETURNS TRIGGER AS $$
BEGIN
  IF ABS(EXTRACT(EPOCH FROM (NEW.date_work::timestamp - OLD.date_work::timestamp))) > 86400 THEN
    RAISE EXCEPTION 'Нельзя изменить дату работы более чем на один день';
  END IF;
  RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Триггер
CREATE TRIGGER trigger_check_work_date_change
BEFORE UPDATE OF date_work ON works
FOR EACH ROW EXECUTE FUNCTION check_work_date_change();

-- Проверка
UPDATE WORKS SET date_work = '2023-10-22' WHERE id = 1;


-- Функция
CREATE OR REPLACE FUNCTION prevent_car_deletion_if_works_exist()
RETURNS TRIGGER AS $$
BEGIN
  IF EXISTS (SELECT 1 FROM works WHERE car_id = OLD.id) THEN
    RAISE EXCEPTION 'Нельзя удалить автомобиль, по которому есть работы';
  END IF;
  RETURN OLD;
END;
$$ LANGUAGE plpgsql;

-- Триггер
CREATE TRIGGER trigger_prevent_car_deletion_if_works_exist
BEFORE DELETE ON cars
FOR EACH ROW EXECUTE FUNCTION prevent_car_deletion_if_works_exist();

-- Проверка
DELETE FROM cars WHERE id = 3;  -- Успешно, так как по этому автомобилю нет работ
DELETE FROM cars WHERE id = 1;  -- Ошибка, так как по этому автомобилю есть работы




-- Курсоры
CREATE OR REPLACE FUNCTION calculate_total_salary(p_master_id INT, base_salary NUMERIC)
RETURNS NUMERIC AS $$
DECLARE
    total_salary NUMERIC;
    bonus NUMERIC;
    current_month INT;
BEGIN
    -- Получение текущего месяца
    SELECT EXTRACT(MONTH FROM CURRENT_DATE) INTO current_month;

    -- Расчет премии как 10% от стоимости всех выполненных работ за текущий месяц
    SELECT SUM(s.cost_our) * 0.1 INTO bonus
    FROM works w
    JOIN services s ON w.service_id = s.id
    WHERE w.master_id = p_master_id AND EXTRACT(MONTH FROM w.date_work) = current_month;

    -- Расчет общей зарплаты
    total_salary := base_salary + COALESCE(bonus, 0);

    RETURN total_salary;
END;
$$ LANGUAGE plpgsql;


-- Проверка
SELECT calculate_total_salary(1, 50000);  -- Для мастера с ID=1 и базовой зарплатой 50000
