--Порядок - вставка, выборка, модификация, удаление.



--Вставка:

--Вставка - однотабличная:



-- Добавляем новую услугу
INSERT INTO services (name, cost_our, cost_foreign)
VALUES ('Новая услуга', 1000, 1500);

-- Добавляем нового мастера
INSERT INTO masters (name)
VALUES ('Иван Иванов');

-- Добавляем новый автомобиль
INSERT INTO cars (num, color, mark, is_foreign)
VALUES ('A123BC', 'Красный', 'Toyota', TRUE);

-- Добавляем новую работу
INSERT INTO works (date_work, master_id, car_id, service_id)
VALUES (current_date, 1, 1, 1);



--Вставка - многотабличная:

BEGIN;

-- Добавляем новый автомобиль
INSERT INTO cars (num, color, mark, is_foreign)
VALUES ('B234CD', 'Синий', 'Ford', FALSE);

-- Добавляем новую услугу
INSERT INTO services (name, cost_our, cost_foreign)
VALUES ('Еще одна новая услуга', 2000, 2500);

-- Добавляем нового мастера
INSERT INTO masters (name)
VALUES ('Петр Петров');

-- Добавляем новую работу
INSERT INTO works (date_work, master_id, car_id, service_id)
VALUES (current_date, 2, 2, 2);

COMMIT;



--Выборка:


--Однотабличная выборка:

--Вычислить общее число услуг и общую сумму стоимости для отечественных
--и импортных автомобилей:
 SELECT 
    cars.is_foreign,
    COUNT(*) AS total_services,
    SUM(CASE 
        WHEN cars.is_foreign THEN services.cost_foreign
        ELSE services.cost_our
    END) AS total_cost
FROM works
JOIN cars ON works.car_id = cars.id
JOIN services ON works.service_id = services.id
GROUP BY cars.is_foreign;

--Вывести все работы за последний месяц:
SELECT * FROM works
WHERE date_work >= current_date - INTERVAL '1 month';



--Соединение таблиц (join)

--Вывести стоимость обслуживания каждого автомобиля за последний год,
--включая автомобили, которые не обслуживались, упорядочив по убыванию стоимости
SELECT 
    cars.id,
    cars.num,
    SUM(CASE 
        WHEN cars.is_foreign THEN services.cost_foreign
        ELSE services.cost_our
    END) AS total_cost_last_year
FROM cars
LEFT JOIN works ON cars.id = works.car_id AND works.date_work >= current_date - INTERVAL '1 year'
LEFT JOIN services ON works.service_id = services.id
GROUP BY cars.id
ORDER BY total_cost_last_year DESC;


--Для реализации проекта
--Вычислить общую стоимость обслуживания отечественных
--и импортных автомобилей за все время существования сервиса
SELECT 
    cars.is_foreign,
    SUM(CASE 
        WHEN cars.is_foreign THEN services.cost_foreign
        ELSE services.cost_our
    END) AS total_cost_all_time
FROM works
JOIN cars ON works.car_id = cars.id
JOIN services ON works.service_id = services.id
GROUP BY cars.is_foreign;



--Модификация данных


--Модификация по фильтру
--Увеличить стоимость всех услуг на 15%
UPDATE services
SET cost_our = cost_our * 1.15,
    cost_foreign = cost_foreign * 1.15;



--Модификация в рамках транзакции
--В рамках транзакции в таблице услуг увеличить цену услуги,
--оказанной последней, на 10.00
BEGIN;

UPDATE services
SET cost_our = cost_our + 10, cost_foreign = cost_foreign + 10
WHERE id = (SELECT service_id FROM works ORDER BY date_work DESC LIMIT 1);

COMMIT;


--То же, что и п1, но транзакцию откатить
BEGIN;

UPDATE services
SET cost_our = cost_our + 10, cost_foreign = cost_foreign + 10
WHERE id = (SELECT service_id FROM works ORDER BY date_work DESC LIMIT 1);

ROLLBACK;



--Удаление по фильтру и удаление из связанных таблиц
--Удалить статью автомобиль и все работы по нему

-- Удаление всех работ, связанных с заданным автомобилем 'Toyota'
DELETE FROM works WHERE car_id = (SELECT id FROM cars WHERE mark = 'Toyota');

-- Удаление самого автомобиля 'Toyota'
DELETE FROM cars WHERE mark = 'Toyota';




--Удаление в рамках транзакции
--Удалить в рамках транзакции услуги, которые оказывались только заданным мастером. Удалить работы по таким услугам этого мастера.
BEGIN;

-- Удаление всех работ, оказанных мастером 'Иван Иванов' по определенным услугам
DELETE FROM works WHERE master_id = (SELECT id FROM masters WHERE name = 'Иван Иванов');

-- Удаление услуг, которые оказывались только мастером 'Иван Иванов'
DELETE FROM services WHERE id IN (SELECT service_id FROM works WHERE master_id = (SELECT id FROM masters WHERE name = 'Иван Иванов'));

COMMIT;




--То же, что и п1, но еще удалить мастера и транзакцию откатить
BEGIN;

-- Удаление всех работ, оказанных мастером 'Иван Иванов' по определенным услугам
DELETE FROM works WHERE master_id = (SELECT id FROM masters WHERE name = 'Иван Иванов');

-- Удаление услуг, которые оказывались только мастером 'Иван Иванов'
DELETE FROM services WHERE id IN (SELECT service_id FROM works WHERE master_id = (SELECT id FROM masters WHERE name = 'Иван Иванов'));

-- Удаление самого мастера 'Иван Иванов'
DELETE FROM masters WHERE name = 'Иван Иванов';

ROLLBACK;

