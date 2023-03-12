package by.piskunou.solvdlaba.service;

import by.piskunou.solvdlaba.domain.Airport;
import by.piskunou.solvdlaba.domain.exception.ResourceAlreadyExistsException;
import by.piskunou.solvdlaba.domain.exception.ResourceNotExistsException;
import by.piskunou.solvdlaba.persistence.AirportRepository;
import by.piskunou.solvdlaba.service.impl.AirportServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AirportServiceTest {

    @Mock
    private CityService cityService;

    @Mock
    private AirportRepository repository;

    @InjectMocks
    private AirportServiceImpl airportService;

    @Test
    void verifyFindAllTest() {
        airportService.findAll();
        verify(repository).findAll();
    }

    @Test
    void verifyFindByIdTest() {
        Airport airport = Airport.builder()
                                 .id(1L)
                                 .name("Minsk National Airport")
                                 .iata("MSQ")
                                 .icao("UMMS")
                                 .build();
        when(repository.findById(1)).thenReturn(Optional.of(airport));
        assertEquals(airport, airportService.findById(1));
        verify(repository).findById(1);
    }

    @Test
    void verifyFindByIdFailedTest() {
        assertThrows(ResourceNotExistsException.class, () -> airportService.findById(Long.MAX_VALUE));
        verify(repository).findById(Long.MAX_VALUE);
    }

    @Test
    void verifySearchTest() {
        Airport airport = Airport.builder()
                                 .name("Mi")
                                 .iata("S")
                                 .build();

        airportService.search(airport);
        assertEquals("%Mi%", airport.getName());
        assertEquals("%S%", airport.getIata());
        assertEquals("%%", airport.getIcao());
        verify(repository).search(airport);
    }

    @Test
    void verifyCreateTest() {
        Airport airport = Airport.builder()
                                 .name("Agadir – Al Massira Airport")
                                 .iata("AGA")
                                 .icao("GMAD")
                                 .build();
        when(cityService.isExists(85)).thenReturn(true);
        doAnswer(new Answer() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                Airport airport = invocation.getArgument(1);
                airport.setId(1L);
                return null;
            }
        }).when(repository).create(85, airport);

        assertEquals(airport, airportService.create(85, airport));
        verify(cityService).isExists(85);
        verify(repository).isExistsByName(null, "Agadir – Al Massira Airport");
        verify(repository).isExistsByIata(null, "AGA");
        verify(repository).isExistsByIcao(null, "GMAD");
        verify(repository).create(85, airport);
        assertEquals(1, airport.getId());
    }

    @Test
    void verifyCreateIncorrectCityIdTest() {
        assertThrows(ResourceNotExistsException.class, () -> airportService.create(65, any()));
        verify(cityService).isExists(65);
    }

    @Test
    void verifyCreateExistedNameTest() {
        Airport airport = Airport.builder()
                                 .name("Minsk National Airport")
                                 .build();
        when(cityService.isExists(85)).thenReturn(true);
        when(airportService.isExistsByName(null, "Minsk National Airport")).thenReturn(true);

        assertThrows(ResourceAlreadyExistsException.class, () -> airportService.create(85, airport));
        verify(cityService).isExists(85);
        verify(repository).isExistsByName(null, "Minsk National Airport");
    }

    @Test
    void verifyCreateExistedIataTest() {
        Airport airport = Airport.builder()
                                 .name("Agadir – Al Massira Airport")
                                 .iata("MSQ")
                                 .build();
        when(cityService.isExists(85)).thenReturn(true);
        when(airportService.isExistsByIata(null, "MSQ")).thenReturn(true);

        assertThrows(ResourceAlreadyExistsException.class, () -> airportService.create(85, airport));
        verify(cityService).isExists(85);
        verify(repository).isExistsByName(null, "Agadir – Al Massira Airport");
        verify(repository).isExistsByIata(null, "MSQ");
    }

    @Test
    void verifyCreateExistedIcaoTest() {
        Airport airport = Airport.builder()
                                 .name("Agadir – Al Massira Airport")
                                 .iata("AGA")
                                 .icao("UMMS")
                                 .build();
        when(cityService.isExists(85)).thenReturn(true);
        when(airportService.isExistsByIcao(null, "UMMS")).thenReturn(true);

        assertThrows(ResourceAlreadyExistsException.class, () -> airportService.create(85, airport));
        verify(cityService).isExists(85);
        verify(repository).isExistsByName(null, "Agadir – Al Massira Airport");
        verify(repository).isExistsByIata(null, "AGA");
        verify(repository).isExistsByIcao(null, "UMMS");
    }

    @Test
    void verifyUpdateByIdTest() {
        Airport airport = Airport.builder()
                                 .name("Agadir – Al Massira Airport")
                                 .iata("AGA")
                                 .icao("GMAD")
                                 .build();
        when(airportService.isExists(43)).thenReturn(true);
        when(cityService.isExists(85)).thenReturn(true);

        assertEquals(airport, airportService.updateById(43, 85, airport));
        verify(repository).isExistsById(43);
        assertEquals(43, airport.getId());
        verify(repository).update(85, airport);
    }

    @Test
    void verifyUpdateByNonexistentIdTest() {
        Airport airport = Airport.builder()
                                 .name("Agadir – Al Massira Airport")
                                 .iata("AGA")
                                 .icao("GMAD")
                                 .build();
        when(cityService.isExists(85)).thenReturn(true);

        assertEquals(airport, airportService.updateById(Long.MAX_VALUE, 85, airport));
        verify(repository).isExistsById(Long.MAX_VALUE);
    }

    @Test
    void verifyRemoveByIdTest() {
        airportService.removeById(1);
        verify(repository).removeById(1);
    }

    @Test
    void verifyIsExistsTest() {
        assertFalse(airportService.isExists(1));
        verify(repository).isExistsById(1);
    }

    @Test
    void verifyIsExistsByNameTest() {
        assertFalse(airportService.isExistsByName(0L, "Minsk National Airport"));
        verify(repository).isExistsByName(0L, "Minsk National Airport");
    }

    @Test
    void verifyIsExistsByIataTest() {
        assertFalse(airportService.isExistsByIata(0L, "MSQ"));
        verify(repository).isExistsByIata(0L, "MSQ");
    }

    @Test
    void verifyIsExistsByIcaoTest() {
        assertFalse(airportService.isExistsByIcao(0L, "UMMS"));
        verify(repository).isExistsByIcao(0L, "UMMS");
    }

}