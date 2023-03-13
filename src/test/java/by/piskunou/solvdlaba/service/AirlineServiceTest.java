package by.piskunou.solvdlaba.service;

import by.piskunou.solvdlaba.domain.Airline;
import by.piskunou.solvdlaba.domain.exception.ResourceAlreadyExistsException;
import by.piskunou.solvdlaba.domain.exception.ResourceNotExistsException;
import by.piskunou.solvdlaba.persistence.AirlineRepository;
import by.piskunou.solvdlaba.service.impl.AirlineServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.anyLong;

@ExtendWith(MockitoExtension.class)
class AirlineServiceTest {

    @Mock
    private AirlineRepository repository;

    @InjectMocks
    private AirlineServiceImpl service;

    @Test
    void verifyFindAllTest() {
        service.findAll();
        verify(repository).findAll();
    }

    @Test
    void verifyFindByIdTest() {
        Airline airline = Airline.builder()
                                 .id(1L)
                                 .name("Belavia Belarusian Airlines")
                                 .iata("B2")
                                 .icao("BRU")
                                 .callsign("BELAVIA")
                                 .build();
        when(repository.findById(1)).thenReturn(Optional.of(airline));
        assertEquals(airline, service.findById(1));
        verify(repository).findById(1);
    }

    @Test
    void verifyFindByIdFailedTest() {
        assertThrows(ResourceNotExistsException.class, () -> service.findById(Long.MAX_VALUE));
        verify(repository).findById(Long.MAX_VALUE);
    }

    @Test
    void verifySearchTest() {
        Airline airline = new Airline("Mi", "E", "E", null);
        assertNotNull(service.search(airline));

        assertEquals("%Mi%", airline.getName());
        assertEquals("%E%", airline.getIata());
        assertEquals("%E%", airline.getIcao());
        assertEquals("%%", airline.getCallsign());

        verify(repository).search(airline);
    }

    @Test
    void verifyCreateTest() {
        Airline airline = Airline.builder()
                                  .name("Royal Air Maroc")
                                  .iata("AT")
                                  .icao("RAM")
                                  .callsign("ROYAL AIR MAROC")
                                  .build();
        doAnswer(invocation -> {
            Airline airline1 = invocation.getArgument(0);
            airline1.setId(1L);
            return null;
        }).when(repository).create(airline);

        assertEquals(airline, service.create(airline));
        verify(repository).isExistsByName(null, "Royal Air Maroc");
        verify(repository).isExistsByIata(null, "AT");
        verify(repository).isExistsByIcao(null, "RAM");
        verify(repository).isExistsByCallsign(null, "ROYAL AIR MAROC");
        verify(repository).create(airline);
        assertEquals(1, airline.getId());
    }


    @Test
    void verifyCreateNameFailedTest() {
        Airline airline = Airline.builder()
                                 .name("Belavia Belarusian Airlines")
                                 .build();

        when(repository.isExistsByName(null, "Belavia Belarusian Airlines")).thenReturn(true);

        assertThrows(ResourceAlreadyExistsException.class, () -> service.create(airline));
        verify(repository).isExistsByName(null, "Belavia Belarusian Airlines");
    }

    @Test
    void verifyCreateIataFailedTest() {
        Airline airline = Airline.builder()
                                 .name("Belavia Belarusian Airlines")
                                 .iata("B2")
                                 .build();
        when(repository.isExistsByIata(null, "B2")).thenReturn(true);


        assertThrows(ResourceAlreadyExistsException.class, () -> service.create(airline));
        verify(repository).isExistsByName(null, "Belavia Belarusian Airlines");
        verify(repository).isExistsByIata(null, "B2");
    }

    @Test
    void verifyCreateIcaoFailedTest() {
        Airline airline = Airline.builder()
                                 .name("Belavia Belarusian Airlines")
                                 .iata("B2")
                                 .icao("BRU")
                                 .build();
        when(repository.isExistsByIcao(null, "BRU")).thenReturn(true);

        assertThrows(ResourceAlreadyExistsException.class, () -> service.create(airline));
        verify(repository).isExistsByName(null, "Belavia Belarusian Airlines");
        verify(repository).isExistsByIata(null, "B2");
        verify(repository).isExistsByIcao(null, "BRU");
    }

    @Test
    void verifyCreateCallsignFailedTest() {
        Airline airline = Airline.builder()
                                 .name("Belavia Belarusian Airlines")
                                 .iata("B2")
                                 .icao("BRU")
                                 .callsign("BELAVIA")
                                 .build();
        when(repository.isExistsByCallsign(null, "BELAVIA")).thenReturn(true);

        assertThrows(ResourceAlreadyExistsException.class, () -> service.create(airline));
        verify(repository).isExistsByName(null, "Belavia Belarusian Airlines");
        verify(repository).isExistsByIata(null, "B2");
        verify(repository).isExistsByIcao(null, "BRU");
        verify(repository).isExistsByCallsign(null, "BELAVIA");
    }


    @Test
    void verifyUpdateByIdTest() {
        Airline airline = Airline.builder()
                                 .name("Belavia Belarusian Airlines")
                                 .iata("B3")
                                 .icao("BRT")
                                 .callsign("BELAVIA")
                                 .build();
        when(repository.isExistsById(1)).thenReturn(true);

        assertEquals(airline, service.updateById(1, airline));
        verify(repository).isExistsById(1);
        assertEquals(1, airline.getId());
        verify(repository).update(airline);
    }

    @Test
    void verifyUpdateByNonexistentIdTest() {
        Airline airline = Airline.builder()
                                 .name("Belavia Belarusian Airlines")
                                 .iata("B3")
                                 .icao("BRT")
                                 .callsign("BELAVIA")
                                 .build();
        assertEquals(airline, service.updateById(Long.MAX_VALUE, airline));
        verify(repository).isExistsById(Long.MAX_VALUE);
    }

    @Test
    void verifyRemoveByIdTest() {
        service.removeById(anyLong());
        verify(repository).removeById(anyLong());
    }

    @Test
    void verifyIsExistsTest() {
        when(repository.isExistsById(1)).thenReturn(true);
        assertTrue(service.isExists(1L));
        verify(repository).isExistsById(1);
    }

    @Test
    void verifyIsExistsFailedTest() {
        assertFalse(service.isExists(0L));
        verify(repository).isExistsById(0);
    }

    @Test
    void verifyIsExistsByNameTest() {
        when(repository.isExistsByName(1L,"Belavia Belarusian Airlines" )).thenReturn(true);
        assertTrue(service.isExistsByName(1L, "Belavia Belarusian Airlines"));
        verify(repository).isExistsByName(1L, "Belavia Belarusian Airlines");
    }

    @Test
    void verifyIsExistsByIataTest() {
        when(repository.isExistsByIata(1L, "B2")).thenReturn(true);
        assertTrue(service.isExistsByIata(1L, "B2"));
        verify(repository).isExistsByIata(1L, "B2");
    }

    @Test
    void verifyIsExistsByIcaoTest() {
        when(repository.isExistsByIcao(0L, "BRU")).thenReturn(true);
        assertTrue(service.isExistsByIcao(0L, "BRU"));
        verify(repository).isExistsByIcao(0L, "BRU");
    }

    @Test
    void verifyIsExistsByCallsignTest() {
        when(repository.isExistsByCallsign(1L, "BELAVIA")).thenReturn(true);
        assertTrue(service.isExistsByCallsign(1L, "BELAVIA"));
        verify(repository).isExistsByCallsign(1L, "BELAVIA");
    }


}