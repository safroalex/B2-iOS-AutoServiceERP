sudo -i -u postgres

psql

CREATE DATABASE lab1;

CREATE USER safr WITH ENCRYPTED PASSWORD 'password';

GRANT ALL PRIVILEGES ON DATABASE lab1 TO safr;

exit

exit

sudo nano /etc/postgresql/14/main/pg_hba.conf

# Измените эту строку
local   all             all                                     peer
# на
local   all             all                                     md5
# или
local   all             all                                     trust

cd ~

systemctl restart postgresql

sudo -i -u postgres

psql -U safr -d lab1


CREATE TABLE cars (
  id SERIAL PRIMARY KEY,
  num VARCHAR(50) NOT NULL,
  color VARCHAR(50) NOT NULL,
  mark VARCHAR(50) NOT NULL,
  is_foreign BOOLEAN NOT NULL
);

CREATE TABLE masters (
  id SERIAL PRIMARY KEY,
  name VARCHAR(50) NOT NULL
);

CREATE TABLE services (
  id SERIAL PRIMARY KEY,
  name VARCHAR(50) NOT NULL,
  cost_our INT NOT NULL,
  cost_foreign INT NOT NULL
);

CREATE TABLE works (
  id SERIAL PRIMARY KEY,
  date_work DATE NOT NULL,
  master_id INT REFERENCES masters(id),
  car_id INT REFERENCES cars(id),
  service_id INT REFERENCES services(id)
);


ALTER TABLE works
ADD CONSTRAINT fk_works_masters FOREIGN KEY (master_id) REFERENCES masters(id) ON DELETE CASCADE;

ALTER TABLE works
ADD CONSTRAINT fk_works_cars FOREIGN KEY (car_id) REFERENCES cars(id) ON DELETE CASCADE;

ALTER TABLE works
ADD CONSTRAINT fk_works_services FOREIGN KEY (service_id) REFERENCES services(id) ON DELETE CASCADE;



---------------------

pg_dump -U safr -d lab1 -f ~/lab1_backup.sql

sudo -i -u postgres

dropdb lab1

sudo -i -u postgres createdb new_lab1

sudo -i -u postgres psql

GRANT ALL PRIVILEGES ON DATABASE new_lab1 TO safr;

psql -U safr -d new_lab1 -f ~/lab1_backup.sql



---------------------


Пример без автозаполнения:

CREATE TABLE cars (
  id INTEGER PRIMARY KEY,
  num VARCHAR(50) NOT NULL,
  color VARCHAR(50) NOT NULL,
  mark VARCHAR(50) NOT NULL,
  is_foreign BOOLEAN NOT NULL
);



Нужно будет указывать явно значение для столбца id

INSERT INTO cars (id, num, color, mark, is_foreign) VALUES (1, 'A123BC', 'Red', 'Toyota', TRUE);
INSERT INTO cars (id, num, color, mark, is_foreign) VALUES (2, 'B321CB', 'Blue', 'Ford', FALSE);
-- и так далее

