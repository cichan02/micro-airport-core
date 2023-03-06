package by.piskunou.solvdlaba.service;

import by.piskunou.solvdlaba.domain.airplane.Airplane;
import by.piskunou.solvdlaba.domain.airplane.AirplaneRequest;
import by.piskunou.solvdlaba.domain.exception.ResourceAlreadyExistsException;
import by.piskunou.solvdlaba.domain.exception.ResourceNotExistsException;
import by.piskunou.solvdlaba.persistence.AirplaneRepository;
import by.piskunou.solvdlaba.service.impl.AirplaneServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AirplaneServiceTest {

    @Mock
    private AirplaneRepository repository;

    @InjectMocks
    private AirplaneServiceImpl service;

    @Test
    void verifyFindAllTest() {
        service.findAll();
        verify(repository).findAll();
    }

    @Test
    void verifyFindByIdTest() {
        Airplane airplane = Airplane.builder()
                                    .id(7L)
                                    .model("Boeing 787")
                                    .seatsInRow((byte) 10)
                                    .rowNo((short) 40)
                                    .build();
        when(repository.findById(7)).thenReturn(Optional.of(airplane));
        assertEquals(airplane, service.findById(7));
        verify(repository).findById(7);
    }

    @Test
    void verifyFindByIdFailedTest() {
        assertThrows(ResourceNotExistsException.class, () -> service.findById(Long.MAX_VALUE));
        verify(repository).findById(Long.MAX_VALUE);
    }

    @Test
    void search() {
        AirplaneRequest request = AirplaneRequest.builder()
                                                 .modelInquiry("Boe")
                                                 .build();
        service.search(request);
        assertEquals("%Boe%", request.getModelInquiry());
        verify(repository).search(request);
    }

    @Test
    void verifyCreateTest() {
        Airplane airplane = Airplane.builder()
                                    .model("A321P2F")
                                    .seatsInRow((byte) 6)
                                    .rowNo((short) 40)
                                    .build();
        doAnswer(new Answer() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                Airplane airplane = invocation.getArgument(0);
                airplane.setId(1L);
                return null;
            }
        }).when(repository).create(airplane);

        assertEquals(airplane, service.create(airplane));
        verify(repository).isExistsByModel(null, "A321P2F");
        verify(repository).create(airplane);
        assertEquals(1, airplane.getId());
    }

    @Test
    void verifyCreateFailedTest() {
        Airplane airplane = Airplane.builder()
                .model("Boeing Next-Genetation 737")
                .seatsInRow((byte) 6)
                .rowNo((short) 40)
                .build();
        when(repository.isExistsByModel(null, "Boeing Next-Genetation 737")).thenReturn(true);

        assertThrows(ResourceAlreadyExistsException.class, () -> service.create(airplane));
        verify(repository).isExistsByModel(null, "Boeing Next-Genetation 737");
        verify(repository, times(0)).create(airplane);
    }

    @Test
    void verifyUpdateByIdTest() {
        Airplane airplane = Airplane.builder()
                                    .model("A321P2F")
                                    .seatsInRow((byte) 6)
                                    .rowNo((short) 40)
                                    .build();
        when(repository.isExistsById(1)).thenReturn(true);

        assertEquals(airplane, service.updateById(1, airplane));
        verify(repository).isExistsById(1);
        assertEquals(1, airplane.getId());
        verify(repository).update(airplane);
    }

    @Test
    void verifyUpdateByNonexistentIdTest() {
        Airplane airplane = Airplane.builder()
                                    .model("A321P2F")
                                    .seatsInRow((byte) 6)
                                    .rowNo((short) 40)
                                    .build();
        assertEquals(airplane, service.updateById(Long.MAX_VALUE, airplane));
        verify(repository).isExistsById(Long.MAX_VALUE);
    }

    @Test
    void verifyRemoveByIdTest() {
        service.removeById(1);
        verify(repository).removeById(1);
    }

    @Test
    void verifyIsExistTest() {
        assertFalse(service.isExists(1));
        verify(repository).isExistsById(1);
    }

    @Test
    void verifyIsExistsByModelTest() {
        assertFalse(service.isExists(1L, "Boeing Next-Genetation 737"));
        verify(repository).isExistsByModel(1L, "Boeing Next-Genetation 737");
    }
    
}