drop schema airport cascade;
create schema if not exists microairport;

set schema 'airport';

select * from countries;

select countries.id, countries.name, cities.id, cities.name
from countries join cities on countries.id = cities.fk_country_id
where countries.id = ?;

select cities.id, cities.name, airports.id, airports.name
from cities join airports on cities.id = airports.fk_city_id
where cities.id in (?);

select countries.id, countries.name, cities.id, cities.name, airports.id, airports.name
from countries join cities on countries.id = cities.fk_country_id
             join airports on cities.id = airports.fk_city_id
where countries.id = ?;

select cities.id, cities.name, airports.id, airports.name
from cities join airports on cities.id = airports.fk_city_id;


select flights.id as flight_id,

       airport_from.id as airport_from_id,
       airport_from.name as airport_from_name,

       airport_to.id as airport_to_id,
       airport_to.name as airport_to_name,

       airplanes.id as airplane_id,
       airplanes.model as airplane_model,
       airplanes.seats_in_row as airplane_seats_in_row,
       airplanes.row_no as airplane_row_no,

       airlines.id as airline_id,
       airlines.name as airline_name,

       flights.departure_time as flight_departure_time,
       flights.arrival_time as flight_arrival_time,
       flights.price as flight_price

from flights inner join airports airport_from on flights.fk_airport_from_id = airport_from.id
     inner join airports airport_to on flights.fk_airport_to_id = airport_to.id
     inner join airlines on flights.fk_airline_id = airlines.id
     inner join airplanes on flights.fk_airplane_id = airplanes.id

where flights.id = 9;

select flights.id as flight_id,
       flights.departure_time as flight_departure_time,
       flights.arrival_time as flight_arrival_time,
       flights.price as flight_price,

       airport_from.id as airport_from_id,
       airport_from.name as airport_from_id,

       airport_to.id as airport_to_id,
       airport_to.name as airport_to_id,

       airlines.id as airline_id,
       airlines.name as airline_name

from flights inner join airports airport_from on flights.fk_airport_from_id = airport_from.id
            inner join airports airport_to on flights.fk_airport_to_id = airport_to.id
            inner join airlines on flights.fk_airline_id = airlines.id

where airport_from.id = 1 and airport_to.id = 4 and
      flights.departure_time between ? and ?;

select flights.id, free_seats from flights where flights.id = 9;

select jsonb_path_query_array(flights.free_seats, free_seats->'number') as free_seats
from flights where flights.id = 9;

select jsonb_path_query_array(flights.free_seats, '$[*] ? (@.free == true)') as free_seats
from flights where flights.id = 9;

update flights f_1 set free_seats = tmp.json_array
from (
    select f.id, jsonb_agg(
        case
            when e ->> 'number' !~  then jsonb_set(e, '{free}', 'true')
            else e
        end
    ) as json_array
    from flights f
    cross join jsonb_array_elements(free_seats) e
    group by 1
) as tmp
where f_1.id = 18;

select flights.id, free_seats from flights;

update flights set free_seats = jsonb_set(flights.free_seats, '{2, free}', 'true');

select users.id as user_id,
       users.username as user_name,

       tickets.id as ticket_id,
       tickets.seat_no as ticket_seat_no,

       passengers.id as passenger_id,
       passengers.firstname as passenger_firstname,
       passengers.surname as passenger_surname,

       flights.id as flight_id,
       flights.departure_time as flight_departure_time,

       airport_from.id as airport_from_id,
       airport_from.name as airport_from_id,

       airport_to.id as airport_to_id,
       airport_to.name as airport_to_id,

       airlines.id as airline_id,
       airlines.name airline_name

from users inner join tickets on users.id = tickets.fk_owner_id
          inner join passengers on tickets.fk_passenger_id = passengers.id
          inner join flights on tickets.fk_flight_id = flights.id
          inner join airports airport_from on flights.fk_airport_from_id = airport_from.id
          inner join airports airport_to on flights.fk_airport_to_id = airport_to.id
          inner join airlines on flights.fk_airline_id = airlines.id
where users.id = ?;

select users.id as user_id,
       users.username as user_name,

       tickets.id as ticket_id,
       tickets.seat_no as ticket_seat_no,

       passengers.id as passenger_id,
       passengers.firstname as passenger_firstname,
       passengers.surname as passenger_surname,

       flights.id as flight_id,
       flights.departure_time as flight_departure_time,

       airport_from.id as airport_from_id,
       airport_from.name as airport_from_name,

       airport_to.id as airport_to_id,
       airport_to.name as airport_to_name,

       airlines.id as airline_id,
       airlines.name airline_name

from users left join tickets on users.id = tickets.fk_owner_id
            left join passengers on tickets.fk_passenger_id = passengers.id
            left join flights on tickets.fk_flight_id = flights.id
            left join airports airport_from on flights.fk_airport_from_id = airport_from.id
            left join airports airport_to on flights.fk_airport_to_id = airport_to.id
            left join airlines on flights.fk_airline_id = airlines.id
where users.id = 1;

select users.id as user_id,
       users.username as user_name,

       tickets.id as ticket_id,
       tickets.seat_no as ticket_seat_no,

       passengers.id as passenger_id,
       passengers.firstname as passenger_firstname,
       passengers.surname as passenger_surname

from users left join tickets on users.id = tickets.fk_owner_id
            left join passengers on tickets.fk_passenger_id = passengers.id

where users.id = 1;

select jsonb_path_query(flights.free_seats, '$[*] ? (@.free == true)') as free_seats
from flights
where flights.id = 1;

-- update flights set jsonb_set((flights.free_seats, '$[*] ? (@.number = "A1")', 'true'));

select (free_seats::jsonb).size() from flights where id = 1;

SELECT '{"foo": [true, "bar"], "tags": {"a": 1, "b": null}}'::jsonb;
SELECT '{"bar": "baz", "balance": 7.77, "active":false}'::jsonb;
SELECT '"foo"'::jsonb @> '"foo"'::jsonb;
SELECT '[1, 2, [1, 3]]'::jsonb @> '[[1, 3]]'::jsonb;
SELECT '{"foo": "bar"}'::jsonb ? 'foo';
SELECT '"foo"'::jsonb ? 'foo';
SELECT '["foo", "bar", "baz"]'::jsonb ? 'bar';
SELECT ('{"a": 1}'::jsonb)['a'];
SELECT ('[1, "2", null]'::jsonb)[1];
select '["a", "b", "c"]'::jsonb ? 'b';
select '{"a":[1,2,3,4,5]}'::jsonb @? '$.a[*] ? (@ > 2)';
select json_array_elements('[1,true, [2,false]]') as value;
select json_array_elements('[1,true, [2,false]]');
select * from json_each('{"a":"foo", "b":"bar"}');
select json_each('{"a":"foo", "b":"bar"}');
select '{"a":[1,2,3,4,5]}'::jsonb @@ '$.a[*] > 2';
select json_extract_path('{"f2":{"f3":1},"f4":{"f5":99,"f6":"foo"}}', 'f2', 'f3');
select '[{"a":"foo"},{"b":"bar"},{"c":"baz"}]'::json->>2;
select json_array_length('[1,2,3,{"f1":1,"f2":[5,6]},4]');
select jsonb_object_keys('{"f1":"abc","f2":{"f3":"a", "f4":"b"}}');

select free_seats::jsonb @> '[{"free": true, "number": "13A"}]' as is_free from flights where id = 1;
select free_seats::jsonb @> '[{"free": true, "number": "13A"}]'::jsonb from flights where id = 1;
select free_seats::jsonb->>'place'='"1A"' from flights where id = 1;
select (free_seats::jsonb)[0]['place'] from flights where id = 1;
select (free_seats::jsonb)[1]['place'] from flights where id = 1;
select free_seats::jsonb #> '{0, number}' from flights where id = 1;
select free_seats::jsonb @@ '$[*].number == "1A"' from flights where id = 1;
select jsonb_array_elements(free_seats::jsonb) from flights where id = 1;
select jsonb_array_elements(jsonb_path_query_array(free_seats, '$[*] ? (@.free == true)'))
    as free_seats from flights where id =1;
select jsonb_array_length(jsonb_path_query_array(free_seats, '$[*] ? (@.free == true)'))
           as size from flights where id = 1;
select id from flights where
        jsonb_array_length(jsonb_path_query_array(free_seats, '$[*] ? (@.free == true)')) > 500;
select jsonb_array_elements(free_seats::jsonb) from flights where id = 1;
select jsonb_path_query(free_seats, '$[*] ? (@.free == true)')
    as lala from flights where id = 1;
select jsonb_object_keys((select (free_seats::jsonb)[0] from flights where id = 1));


update flights set free_seats[0]['free'] = 'false' where id = 1;

select free_seats::jsonb from flights where id = 1;

select jsonb_path_query('{
  "track": {
    "segments": [
      {
        "location":   [ 47.763, 13.4034 ],
        "start time": "2018-10-14 10:05:14",
        "HR": 73
      },
      {
        "location":   [ 47.706, 13.2635 ],
        "start time": "2018-10-14 10:39:21",
        "HR": 135
      }
    ]
  }
}', '($.track.segments ? (@[*].HR > 70)).size()');
select jsonb_array_length(free_seats::jsonb) from flights where id = 1;
select jsonb_set((select free_seats::jsonb from flights where id = 1), '$[*] ? (@.number == "1A")', 'true');
select jsonb_path_query_array('{"x": [2,3,4]}', '+ $.x');
select row_to_json(row(1,'foo'));
select json_build_object('number', ?, 'free', false);

select * from airlines;

select * from airlines where iata = 'BRU';

select * from airplanes where seats_in_row * row_no > 0 and model like '%%';

select cities.id as city_id,
       cities.name as city_name,

       countries.id as country_id,
       countries.name as country_name,

       case when ? then (select airports.id) end as airport_id,
       case when ? then (select airports.name) end as airport_name

from cities inner join countries on cities.fk_country_id = countries.id
            left join airports on cities.id = airports.fk_city_id
where cities.id = ?;

select * from airlines;
select exists (select from airlines where name = 'Air France' and id != 0);

delete from countries where name = '';

update countries set name = (case when ? is not null then ? else name end) where id = ?;
select exists (select from cities where name = 'Leipzig' and id != 0);

select * from users;
update users set role = 'ADMIN' where id = 6;
select * from passengers;
select * from airlines;
select * from passports;
select * from tickets;

select tickets.id as ticket_id,
       jsonb_build_object('number', tickets.seat_no)::jsonb as ticket_seat_no,

       passengers.id as passenger_id,
       passengers.firstname as passenger_firstname,
       passengers.surname as passenger_surname,

       flights.id as flight_id,
       flights.departure_time as flight_departure_time,

       airport_from.id as airport_from_id,
       airport_from.name as airport_from_name,
       airport_from.iata as airport_from_iata_code,
       airport_from.icao as airport_from_icao_code,

       airport_to.id as airport_to_id,
       airport_to.name as airport_to_name,
       airport_to.iata as airport_to_iata_code,
       airport_to.icao as airport_to_icao_code,

       airlines.id as airline_id,
       airlines.name as airline_name,
       airlines.iata as airline_iata_code,
       airlines.icao as airline_icao_code,
       airlines.callsign as airline_callsign

from tickets inner join passengers on tickets.fk_passenger_id = passengers.id
             inner join flights on tickets.fk_flight_id = flights.id
             inner join airports airport_from on flights.fk_airport_from_id = airport_from.id
             inner join airports airport_to on flights.fk_airport_to_id = airport_to.id
             inner join airlines on flights.fk_airline_id = airlines.id
where tickets.id = 2 and fk_owner_id = 1;

select jsonb_array_elements(free_seats::jsonb) from flights where id = 1;

select jsonb_build_object('number', seat_no)::jsonb
from tickets where id = 2;

select * from airplanes;
select * from passports;
select * from flights;
select * from airports;
select * from countries;
select * from users;

select exists (select * from airports where id != (case when ? is not null then ? else name end));
select exists (select from airports where name = 'Frankfurt Airport' and id != (case when ? is null then 0 else ? end));
select exists (select from airports where name = ? and (case when ? is not null then ? else 0 end) != id);
select exists (select from airports where name = ? and id != ?);

select exists (select from users where id = ? and password = '$2a$10$wObMjBpvmYpGh9hzE01qn.WJP0FCao4CLfHjWEAj68HTwrHsktGse');

