package by.piskunou.solvdlaba.service;

import by.piskunou.solvdlaba.domain.Airport;
import by.piskunou.solvdlaba.domain.City;
import by.piskunou.solvdlaba.domain.Country;
import by.piskunou.solvdlaba.domain.exception.ResourceAlreadyExistsException;
import by.piskunou.solvdlaba.domain.exception.ResourceNotExistsException;
import by.piskunou.solvdlaba.persistence.CityRepository;
import by.piskunou.solvdlaba.service.impl.CityServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CityServiceTest {

    @Mock
    private CountryService countryService;

    @Mock
    private CityRepository repository;

    @InjectMocks
    private CityServiceImpl cityService;

    @Test
    void verifyFindAllTest() {
        cityService.findAll();
        verify(repository).findAll();
    }

    @Test
    void verifyFindByIdTest() {
        City city = City.builder()
                        .id(1L)
                        .name("Warsaw")
                        .country(new Country(1L, "Poland"))
                        .build();
        when(repository.findById(1)).thenReturn(Optional.of(city));

        assertEquals(city, cityService.findById(1, false));
        verify(repository).findById(1);
    }

    @Test
    void verifyFindByIdWithAirportsTest() {
        List<Airport> airports = new LinkedList<>();
        airports.add(new Airport(1L, "Warsaw Chopin Airport", "IATA", "EPWA"));
        City city = City.builder()
                        .id(1L)
                        .name("Warsaw")
                        .country(new Country(1L, "Poland"))
                        .airports(airports)
                        .build();
        when(repository.findByIdWithAirports(1)).thenReturn(Optional.of(city));

        assertEquals(city, cityService.findById(1, true));
        verify(repository).findByIdWithAirports(1);
    }

    @Test
    void verifyFindByIdFailedTest() {
        assertThrows(ResourceNotExistsException.class, () -> cityService.findById(Long.MAX_VALUE, false));
        verify(repository).findById(Long.MAX_VALUE);
    }

    @Test
    void verifySearchTest() {
        cityService.search("a");
        verify(repository).search("%a%");
    }

    @Test
    void verifyCreateTest() {
        City city = City.builder()
                        .name("Warsaw")
                        .build();
        when(countryService.isExists(4)).thenReturn(true);
        doAnswer(new Answer() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                City city = invocation.getArgument(1);
                city.setId(1L);
                return null;
            }
        }).when(repository).create(4, city);

        assertEquals(city, cityService.create(4, city));
        verify(countryService).isExists(4);
        verify(repository).isExistsByName(null, "Warsaw");
        verify(repository).create(4, city);
        assertEquals(1, city.getId());
    }

    @Test
    void verifyCreateOccupiedNameTest() {
        City city = City.builder()
                        .name("Warsaw")
                        .build();
        when(countryService.isExists(4)).thenReturn(true);
        when(cityService.isExists(null, "Warsaw")).thenReturn(true);

        assertThrows(ResourceAlreadyExistsException.class, () -> cityService.create(4, city));
        verify(countryService).isExists(4);
        verify(repository).isExistsByName(null, "Warsaw");
    }

    @Test
    void verifyCreateNonexistentCountryIdTest() {
        assertThrows(ResourceNotExistsException.class, () -> cityService.create(Long.MAX_VALUE, any()));
        verify(countryService).isExists(Long.MAX_VALUE);
    }

    @Test
    void verifyUpdateByIdTest() {
        City city = City.builder()
                        .name("Warsaw")
                        .build();
        when(cityService.isExists(1)).thenReturn(true);
        when(countryService.isExists(4)).thenReturn(true);

        assertEquals(city, cityService.updateById(1, 4, city));
        verify(repository).isExistsById(1);
        assertEquals(1, city.getId());
        verify(repository).update(4, city);
    }

    @Test
    void verifyUpdateByNonexistentIdTest() {
        City city = City.builder()
                        .name("Warsaw")
                        .build();
        when(countryService.isExists(4)).thenReturn(true);

        assertEquals(city, cityService.updateById(1, 4, city));
        verify(repository).isExistsById(1);
    }

    @Test
    void verifyRemoveByIdTest() {
        cityService.removeById(1);
        verify(repository).removeById(1);
    }

    @Test
    void verifyIsExistsTest() {
        assertFalse(cityService.isExists(1));
        verify(repository).isExistsById(1);
    }

    @Test
    void verifyIsExistsByNameTest() {
        assertFalse(cityService.isExists(0L, "Poland"));
        verify(repository).isExistsByName(0L, "Poland");
    }
    
}