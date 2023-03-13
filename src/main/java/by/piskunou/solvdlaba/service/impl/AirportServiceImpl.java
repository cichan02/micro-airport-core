package by.piskunou.solvdlaba.service.impl;

import by.piskunou.solvdlaba.domain.Airport;
import by.piskunou.solvdlaba.service.AirportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class AirportServiceImpl implements AirportService {

    @Override
    public Airport findById(long id) {
        Mono<Airport> airportMono = WebClient.create("http://localhost:8765/airports")
                .get()
                .uri("/" + id)
                .retrieve()
                .bodyToMono(Airport.class);

        return airportMono.block();
    }

    @Override
    public boolean isExists(long id) {
        Mono<Boolean> booleanMono = WebClient.create("http://localhost:8765/airports/exists")
                .get()
                .uri("/" + id)
                .retrieve()
                .bodyToMono(Boolean.class);

        return booleanMono.block();
    }

}
