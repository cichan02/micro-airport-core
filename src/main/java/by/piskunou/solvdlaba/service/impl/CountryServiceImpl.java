package by.piskunou.solvdlaba.service.impl;

import by.piskunou.solvdlaba.domain.Country;
import by.piskunou.solvdlaba.service.CountryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class CountryServiceImpl implements CountryService {

    @Override
    public Country findById(long id) {
        Mono<Country> countryMono = WebClient.create("http://localhost:8765/countries")
                .get()
                .uri("/" + id)
                .retrieve()
                .bodyToMono(Country.class);

        return countryMono.block();
    }

    @Override
    public boolean isExists(long id) {
        Mono<Boolean> booleanMono = WebClient.create("http://localhost:8765/countries/exists")
                .get()
                .uri("/" + id)
                .retrieve()
                .bodyToMono(Boolean.class);

        return booleanMono.block();
    }

}
