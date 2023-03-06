--liquibase formatted sql

--changeset cichan:create-users-table
create table if not exists users(
    id bigserial primary key,
    username varchar(50) not null unique,
    password varchar(300) not null unique,
    role varchar(10) not null
);
--rollback drop table users;

--changeset cichan:create-airplanes-table
create table if not exists airplanes(
    id bigserial primary key,
    model varchar(300) not null unique,
    seats_in_row smallint not null,
    row_no smallint not null
);
--rollback drop table airplanes;

--changeset cichan:create-countries-table
create table if not exists countries(
    id bigserial primary key,
    "name" varchar(50) not null unique
);
--rollback drop table countries;

--changeset cichan:create-cities-table
create table if not exists cities(
    id bigserial primary key,
    fk_country_id bigint not null references countries(id) on delete cascade on update no action,
    "name" varchar(50) not null unique
);
--rollback drop table cities;

--changeset cichan:create-airports-table
create table if not exists airports(
    id bigserial primary key,
    fk_city_id bigint not null references cities(id) on delete cascade on update no action,
    "name" varchar(200) not null unique,
    iata varchar check (length(iata) = 3) unique,
    icao varchar check (length(icao) = 4) unique
);
--rollback drop table airports;

--changeset cichan:create-airlines-table
create table if not exists airlines(
    id bigserial primary key,
    "name" varchar(50) not null unique,
    iata varchar check (length(iata) = 2) unique,
    icao varchar check (length(icao) = 3) unique,
    callsign varchar(30) not null unique
);
--rollback drop table airlines;

--changeset cichan:create-flights-table
create table if not exists flights(
    id bigserial primary key,
    fk_airport_from_id bigint not null references airports(id) on delete restrict on update no action,
    fk_airport_to_id bigint not null references airports(id) on delete restrict on update no action,
    fk_airline_id bigint not null references airlines(id) on delete restrict on update no action,
    fk_airplane_id bigint not null references airplanes(id) on delete restrict on update no action,
    departure_time timestamp not null,
    arrival_time timestamp not null,
    price decimal(8, 2) not null,
    free_seats jsonb not null
);
--rollback drop table flights;

--changeset cichan:create-passengers-table
create table if not exists passengers(
    id bigserial primary key,
    fk_country_id bigint references countries(id) on delete set null on update no action,
    firstname varchar(50) not null,
    surname varchar(50) not null,
    date_of_birth date not null,
    age varchar(10) not null,
    gender varchar(6) not null
);
--rollback drop table passengers;

--changeset cichan:create-passports-table
create table if not exists passports(
    fk_passenger_id bigint primary key references passengers(id) on delete cascade on update no action,
    "number" varchar(20) not null unique,
    identification_no varchar(20) not null unique
);
--rollback drop table passports

--changeset cichan:create-tickets-table
create table if not exists tickets(
    id bigserial primary key,
    fk_owner_id bigint references users(id) on delete set null on update no action,
    fk_flight_id bigint not null references flights(id) on delete cascade on update no action,
    fk_passenger_id bigint not null references passengers(id) on delete cascade on update no action,
    seat_no int not null
);
--rollback drop table tickets

--changeset cichan:add-email-to-users
alter table users add column email varchar(100) not null unique;
--rollback alter table users drop column if exists email
