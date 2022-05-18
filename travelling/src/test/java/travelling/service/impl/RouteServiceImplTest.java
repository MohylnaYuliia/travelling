package travelling.service.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import travelling.entity.RouteEntity;
import travelling.entity.UserEntity;
import travelling.entity.UserRouteEntity;
import travelling.entity.UserRouteId;
import travelling.exception.SpotsSoldOutException;
import travelling.repository.RouteRepository;
import travelling.repository.UserRepository;
import travelling.repository.UserRouteRepository;

import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class RouteServiceImplTest {

    @Autowired
    private RouteServiceImpl service;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RouteRepository routeRepository;

    @Autowired
    private UserRouteRepository userRouteRepository;

    @BeforeEach
    public void setup() {
        userRepository.deleteAll();
        routeRepository.deleteAll();
    }

    @Test
    void testGetBooks() {
        Assertions.assertEquals(0, ((List<RouteEntity>) routeRepository.findAll()).size());

        Assertions.assertEquals(0, service.getAllRouts().size());
    }

    @Test
    void testGetAllBooks() {
        routeRepository.save(RouteEntity.builder().id(1).name("Munich-Berlin").spots(10).build());
        Assertions.assertEquals(1, ((List<RouteEntity>) routeRepository.findAll()).size());

        Assertions.assertEquals(1, service.getAllRouts().size());
        Assertions.assertEquals("Munich-Berlin", ((List<RouteEntity>) routeRepository.findAll()).get(0).getName());
    }

    @Test
    void testBookSpots() {
        routeRepository.save(RouteEntity.builder().id(1).name("Munich-Berlin").spots(10).build());
        userRepository.save(UserEntity.builder().id(1).name("John").build());

        service.bookSpots(1, 1, 1);
        Optional<UserRouteEntity> userRouteEntity = userRouteRepository.findById(UserRouteId.builder().routeId(1).userId(1).build());

        Assertions.assertTrue(userRouteEntity.isPresent());
        Assertions.assertEquals(1, userRouteEntity.get().getSpotCount());
        Assertions.assertEquals(9, userRouteEntity.get().getRoute().getSpots());
        Assertions.assertEquals("Munich-Berlin", userRouteEntity.get().getRoute().getName());
        Assertions.assertEquals("John", userRouteEntity.get().getUser().getName());
    }

    @Test
    void testThrowExceptionWhenNotEnoughSpotsSoldOut() {
        routeRepository.save(RouteEntity.builder().id(1).name("Munich-Berlin").spots(0).build());
        userRepository.save(UserEntity.builder().id(1).name("John").build());

        SpotsSoldOutException exception = Assertions.assertThrows(SpotsSoldOutException.class, () -> {
            service.bookSpots(1, 1, 1);
        });

        Assertions.assertEquals("All spots are sold out", exception.getMessage());

        Optional<UserRouteEntity> userRouteEntity = userRouteRepository.findById(UserRouteId.builder().routeId(1).userId(1).build());

        Assertions.assertFalse(userRouteEntity.isPresent());
    }

}