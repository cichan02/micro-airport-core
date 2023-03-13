--liquibase formatted sql

--changeset cichan:create-users-table
create table if not exists users(
    id bigserial primary key,
    username varchar(50) not null unique,
    email varchar(100) not null unique,
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
    fk_airport_from_id bigint not null,
    fk_airport_to_id bigint not null,
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
    fk_country_id bigint not null,
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
