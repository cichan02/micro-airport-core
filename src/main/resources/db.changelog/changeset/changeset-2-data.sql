--liquibase formatted sql

--changeset cichan:insertion-into-airlplanes
insert into airplanes(model, seats_in_row, row_no) values ('Boeing Next-Genetation 737', 6, 30);
insert into airplanes(model, seats_in_row, row_no) values ('Boeing 737 MAX', 10, 50);
insert into airplanes(model, seats_in_row, row_no) values ('Boeing 747-8', 4, 30);
insert into airplanes(model, seats_in_row, row_no) values ('Boeing 767', 6, 30);
insert into airplanes(model, seats_in_row, row_no) values ('Boeing 777', 6, 30);
insert into airplanes(model, seats_in_row, row_no) values ('Boeing 777X', 6, 35);
insert into airplanes(model, seats_in_row, row_no) values ('Boeing 787', 10, 40);
insert into airplanes(model, seats_in_row, row_no) values ('A220', 4, 25);
insert into airplanes(model, seats_in_row, row_no) values ('A320', 6, 32);
insert into airplanes(model, seats_in_row, row_no) values ('A330', 6, 31);
insert into airplanes(model, seats_in_row, row_no) values ('A350', 6, 31);
insert into airplanes(model, seats_in_row, row_no) values ('A380', 10, 40);

--changeset cichan:insertion-into-airlines
insert into airlines(name, iata, icao, callsign) values ('Belavia Belarusian Airlines', 'B2', 'BRU', 'BELAVIA');
insert into airlines(name, iata, icao, callsign) values ('PJSC Aeroflot – Russian Airlines', 'SU', 'AFL', 'AEROFLOT');
insert into airlines(name, iata, icao, callsign) values ('Aurora', 'HZ', 'SHU', 'AURORA');
insert into airlines(name, iata, icao, callsign) values ('JSC Siberia Airlines', 'S7', 'SBI', 'SIBERIAN');
insert into airlines(name, iata, icao, callsign) values ('Ukraine International Airlines PJSC', 'PS', 'AUI', 'UKRAINE INTERNATIONAL');
insert into airlines(name, iata, icao, callsign) values ('Polskie Linie Lotnicze LOT S.A.', 'LO', 'LOT', 'LOT');
insert into airlines(name, iata, icao, callsign) values ('Eurowings GmbH', 'EW', 'EWG', 'EUROWINGS');
insert into airlines(name, iata, icao, callsign) values ('Deutsche Lufthansa AG', 'LH', 'DLH', 'LUFTHANSA');
insert into airlines(name, iata, icao, callsign) values ('TUI fly Deutschland', 'X3', 'TUI', 'TUI JET');
insert into airlines(name, iata, icao, callsign) values ('Air Arabia', 'G9', 'ABY', 'ARABIA');
insert into airlines(name, iata, icao, callsign) values ('Air Arabia Abu Dhabi', '3L', 'ADY', 'NAWRAS');
insert into airlines(name, iata, icao, callsign) values ('Emirates', 'EK', 'UAE', 'EMIRATES');
insert into airlines(name, iata, icao, callsign) values ('Etihad Airways', 'EY', 'ETD', 'ETIHAD');
insert into airlines(name, iata, icao, callsign) values ('Flydubai', 'FZ', 'FDB', 'SKYDUBAI');
insert into airlines(name, iata, icao, callsign) values ('Wizz Air Abu Dhabi LLC', '5W', 'WAZ', 'WIZZ SKY');
