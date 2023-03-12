package by.piskunou.solvdlaba.service;

import by.piskunou.solvdlaba.domain.Country;
import by.piskunou.solvdlaba.domain.exception.ResourceAlreadyExistsException;
import by.piskunou.solvdlaba.domain.exception.ResourceNotExistsException;
import by.piskunou.solvdlaba.persistence.CountryRepository;
import by.piskunou.solvdlaba.service.impl.CountryServiceImpl;
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
class CountryServiceTest {

    @Mock
    private CountryRepository repository;

    @InjectMocks
    private CountryServiceImpl service;

    @Test
    void verifyFindAllTest() {
        service.findAll();
        verify(repository).findAll();
    }

    @Test
    void verifyFindByIdTest() {
        Country country = Country.builder()
                                 .id(1L)
                                 .name("Poland")
                                 .build();
        when(repository.findById(1)).thenReturn(Optional.of(country));

        assertEquals(country, service.findById(1));
        verify(repository).findById(1);
    }

    @Test
    void verifyFindByIdFailedTest() {
        assertThrows(ResourceNotExistsException.class, () -> service.findById(0));
        verify(repository).findById(0);
    }

    @Test
    void verifyCreateTest() {
        Country country = Country.builder()
                                 .name("Poland")
                                 .build();
        doAnswer(new Answer() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                Country country = invocation.getArgument(0);
                country.setId(1L);
                return null;
            }
        }).when(repository).create(country);

        assertEquals(country, service.create(country));
        verify(repository).isExistsByName(null, "Poland");
        verify(repository).create(country);
        assertEquals(1, country.getId());
    }

    @Test
    void verifyCreateFailedTest() {
        Country country = Country.builder()
                                 .name("Poland")
                                 .build();
        when(service.isExists(null, "Poland")).thenReturn(true);
        assertThrows(ResourceAlreadyExistsException.class, () -> service.create(country));
        verify(repository).isExistsByName(null, "Poland");
    }


    @Test
    void verifyUpdateByIdTest() {
        Country country = Country.builder()
                                 .name("Poland")
                                 .build();
        when(repository.isExistsById(1)).thenReturn(true);

        assertEquals(country, service.updateById(1, country));
        verify(repository).isExistsById(1);
        assertEquals(1, country.getId());
        verify(repository).update(country);
    }

    @Test
    void verifyUpdateByNonexistentIdTest() {
        Country country = Country.builder()
                                .name("Poland")
                                .build();
        assertEquals(country, service.updateById(Long.MAX_VALUE, country));
        verify(repository).isExistsById(Long.MAX_VALUE);
    }

    @Test
    void verifyRemoveByIdTest() {
        service.removeById(1);
        verify(repository).removeById(1);
    }

    @Test
    void verifyIsExistsTest() {
        assertFalse(service.isExists(0));
        verify(repository).isExistsById(0);
    }

    @Test
    void verifyIsExistsByNameTest() {
        assertFalse(service.isExists(0L,"Poland"));
        verify(repository).isExistsByName(0L, "Poland");
    }

}