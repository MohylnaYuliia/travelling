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
import travelling.exception.NotEnoughSpotsException;
import travelling.exception.RouteNotExistsException;
import travelling.exception.SpotsSoldOutException;
import travelling.exception.UserNotExistsException;
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
    void testThrowExceptionWhenSpotsSoldOut() {
        routeRepository.save(RouteEntity.builder().id(1).name("Munich-Berlin").spots(0).build());
        userRepository.save(UserEntity.builder().id(1).name("John").build());

        SpotsSoldOutException exception = Assertions.assertThrows(SpotsSoldOutException.class, () -> {
            service.bookSpots(1, 1, 1);
        });

        Assertions.assertEquals("All spots are sold out", exception.getMessage());

        Optional<UserRouteEntity> userRouteEntity = userRouteRepository.findById(UserRouteId.builder().routeId(1).userId(1).build());
        Assertions.assertFalse(userRouteEntity.isPresent());
    }

    @Test
    void testThrowExceptionWhenNotEnoughSpots() {
        routeRepository.save(RouteEntity.builder().id(1).name("Munich-Berlin").spots(1).build());
        userRepository.save(UserEntity.builder().id(1).name("John").build());

        NotEnoughSpotsException exception = Assertions.assertThrows(NotEnoughSpotsException.class, () -> {
            service.bookSpots(1, 1, 2);
        });

        Assertions.assertEquals("Not enough spots", exception.getMessage());

        Optional<UserRouteEntity> userRouteEntity = userRouteRepository.findById(UserRouteId.builder().routeId(1).userId(1).build());
        Assertions.assertFalse(userRouteEntity.isPresent());
    }

    @Test
    void testThrowExceptionWhenUserNotExists() {
        UserNotExistsException exception = Assertions.assertThrows(UserNotExistsException.class, () -> {
            service.bookSpots(1, 1, 2);
        });

        Assertions.assertEquals("User not exists", exception.getMessage());

        Optional<UserRouteEntity> userRouteEntity = userRouteRepository.findById(UserRouteId.builder().routeId(1).userId(1).build());
        Assertions.assertFalse(userRouteEntity.isPresent());
    }

    @Test
    void testThrowExceptionWhenRouteNotExists() {
        userRepository.save(UserEntity.builder().id(1).name("John").build());
        RouteNotExistsException exception = Assertions.assertThrows(RouteNotExistsException.class, () -> {
            service.bookSpots(1, 1, 2);
        });

        Assertions.assertEquals("Route not exists", exception.getMessage());

        Optional<UserRouteEntity> userRouteEntity = userRouteRepository.findById(UserRouteId.builder().routeId(1).userId(1).build());
        Assertions.assertFalse(userRouteEntity.isPresent());
    }

    @Test
    void testWhenUserBooksTwoTimes() {
        routeRepository.save(RouteEntity.builder().id(1).name("Munich-Berlin").spots(10).build());
        userRepository.save(UserEntity.builder().id(1).name("John").build());

        service.bookSpots(1, 1, 1);

        Optional<UserRouteEntity> userRouteEntity = userRouteRepository.findById(UserRouteId.builder().routeId(1).userId(1).build());

        Assertions.assertTrue(userRouteEntity.isPresent());
        Assertions.assertEquals(1, userRouteEntity.get().getSpotCount());
        Assertions.assertEquals(9, userRouteEntity.get().getRoute().getSpots());
        Assertions.assertEquals("Munich-Berlin", userRouteEntity.get().getRoute().getName());
        Assertions.assertEquals("John", userRouteEntity.get().getUser().getName());

        service.bookSpots(1, 1, 2);

        userRouteEntity = userRouteRepository.findById(UserRouteId.builder().routeId(1).userId(1).build());

        Assertions.assertTrue(userRouteEntity.isPresent());
        Assertions.assertEquals(3, userRouteEntity.get().getSpotCount());
        Assertions.assertEquals(7, userRouteEntity.get().getRoute().getSpots());
        Assertions.assertEquals("Munich-Berlin", userRouteEntity.get().getRoute().getName());
        Assertions.assertEquals("John", userRouteEntity.get().getUser().getName());
    }

    @Test
    void testWhenTwoUsersBookSpots() {
        routeRepository.save(RouteEntity.builder().id(1).name("Munich-Berlin").spots(10).build());
        userRepository.save(UserEntity.builder().id(1).name("John").build());
        userRepository.save(UserEntity.builder().id(2).name("Eddi").build());

        service.bookSpots(1, 1, 1);
        service.bookSpots(2, 1, 2);
        List<UserRouteEntity> all = (List<UserRouteEntity>) userRouteRepository.findAll();
        Assertions.assertEquals(2, all.size());

        UserRouteEntity userRouteFirst = all.get(0);

        Assertions.assertEquals(1, userRouteFirst.getSpotCount());
        Assertions.assertEquals(7, userRouteFirst.getRoute().getSpots());
        Assertions.assertEquals("Munich-Berlin", userRouteFirst.getRoute().getName());
        Assertions.assertEquals("John", userRouteFirst.getUser().getName());

        UserRouteEntity userRouteSecond = all.get(1);

        Assertions.assertEquals(2, userRouteSecond.getSpotCount());
        Assertions.assertEquals(7, userRouteSecond.getRoute().getSpots());
        Assertions.assertEquals("Munich-Berlin", userRouteSecond.getRoute().getName());
        Assertions.assertEquals("Eddi", userRouteSecond.getUser().getName());
    }
}