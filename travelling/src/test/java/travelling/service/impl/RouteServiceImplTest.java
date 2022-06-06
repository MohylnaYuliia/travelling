package travelling.service.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import travelling.entity.RouteEntity;
import travelling.entity.UserEntity;
import travelling.entity.UserRouteEntity;
import travelling.entity.UserRouteId;
import travelling.exception.CancellationSpotsMoreThanBooked;
import travelling.exception.NoReservationExists;
import travelling.exception.NotEnoughSpotsException;
import travelling.exception.RouteNotExistsException;
import travelling.exception.SpotsSoldOutException;
import travelling.exception.UserNotExistsException;
import travelling.repository.RouteRepository;
import travelling.repository.UserRepository;
import travelling.repository.UserRouteRepository;

import java.util.List;
import java.util.Optional;

import static travelling.constant.Constants.ALL_SPOTS_SOLD_OUT;
import static travelling.constant.Constants.NOT_ENOUGH_SPOTS;
import static travelling.constant.Constants.NO_RESERVATION_EXISTS;
import static travelling.constant.Constants.ROUTE_NOT_EXISTS;
import static travelling.constant.Constants.USER_NOT_EXISTS;
import static travelling.constant.Constants.TRY_TO_CANCEL_MORE_THAN_RESERVED;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class RouteServiceImplTest {

    private static final String ROUTE_NAME = "Munich-Berlin";
    private static final String USER_NAME = "John";
    private static final int SPOTS = 10;

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
    @Transactional
    @Rollback
    void testGetAllBooks() {
        routeRepository.save(RouteEntity.builder().id(1).name(ROUTE_NAME).spots(SPOTS).build());
        Assertions.assertEquals(1, ((List<RouteEntity>) routeRepository.findAll()).size());

        Assertions.assertEquals(1, service.getAllRouts().size());
        Assertions.assertEquals(ROUTE_NAME, ((List<RouteEntity>) routeRepository.findAll()).get(0).getName());
    }

    @Test
    @Transactional
    @Rollback
    void testBookSpots() {
        routeRepository.save(RouteEntity.builder().id(1).name(ROUTE_NAME).spots(SPOTS).build());
        userRepository.save(UserEntity.builder().id(1).name(USER_NAME).build());

        service.bookSpots(1, 1, 1);
        Optional<UserRouteEntity> userRouteEntity = userRouteRepository.findById(UserRouteId.builder().routeId(1).userId(1).build());

        Assertions.assertTrue(userRouteEntity.isPresent());
        Assertions.assertEquals(1, userRouteEntity.get().getSpotCount());
        Assertions.assertEquals(9, userRouteEntity.get().getRoute().getSpots());
        Assertions.assertEquals(ROUTE_NAME, userRouteEntity.get().getRoute().getName());
        Assertions.assertEquals(USER_NAME, userRouteEntity.get().getUser().getName());
    }

    @Test
    @Transactional
    @Rollback
    void testThrowExceptionWhenSpotsSoldOut() {
        routeRepository.save(RouteEntity.builder().id(1).name(ROUTE_NAME).spots(0).build());
        userRepository.save(UserEntity.builder().id(1).name(USER_NAME).build());

        SpotsSoldOutException exception = Assertions.assertThrows(SpotsSoldOutException.class, () -> {
            service.bookSpots(1, 1, 1);
        });

        Assertions.assertEquals(ALL_SPOTS_SOLD_OUT, exception.getMessage());

        Optional<UserRouteEntity> userRouteEntity = userRouteRepository.findById(UserRouteId.builder().routeId(1).userId(1).build());
        Assertions.assertFalse(userRouteEntity.isPresent());
    }

    @Test
    @Transactional
    @Rollback
    void testThrowExceptionWhenNotEnoughSpots() {
        routeRepository.save(RouteEntity.builder().id(1).name(ROUTE_NAME).spots(1).build());
        userRepository.save(UserEntity.builder().id(1).name(USER_NAME).build());

        NotEnoughSpotsException exception = Assertions.assertThrows(NotEnoughSpotsException.class, () -> {
            service.bookSpots(1, 1, 2);
        });

        Assertions.assertEquals(NOT_ENOUGH_SPOTS, exception.getMessage());

        Optional<UserRouteEntity> userRouteEntity = userRouteRepository.findById(UserRouteId.builder().routeId(1).userId(1).build());
        Assertions.assertFalse(userRouteEntity.isPresent());
    }

    @Test
    void testThrowExceptionWhenUserNotExists() {
        UserNotExistsException exception = Assertions.assertThrows(UserNotExistsException.class, () -> {
            service.bookSpots(1, 1, 2);
        });

        Assertions.assertEquals(USER_NOT_EXISTS, exception.getMessage());

        Optional<UserRouteEntity> userRouteEntity = userRouteRepository.findById(UserRouteId.builder().routeId(1).userId(1).build());
        Assertions.assertFalse(userRouteEntity.isPresent());
    }

    @Test
    @Transactional
    @Rollback
    void testThrowExceptionWhenRouteNotExists() {
        userRepository.save(UserEntity.builder().id(1).name(USER_NAME).build());
        RouteNotExistsException exception = Assertions.assertThrows(RouteNotExistsException.class, () -> {
            service.bookSpots(1, 1, 2);
        });

        Assertions.assertEquals(ROUTE_NOT_EXISTS, exception.getMessage());

        Optional<UserRouteEntity> userRouteEntity = userRouteRepository.findById(UserRouteId.builder().routeId(1).userId(1).build());
        Assertions.assertFalse(userRouteEntity.isPresent());
    }

    @Test
    @Transactional
    @Rollback
    void testWhenUserBooksTwoTimes() {
        routeRepository.save(RouteEntity.builder().id(1).name(ROUTE_NAME).spots(SPOTS).build());
        userRepository.save(UserEntity.builder().id(1).name(USER_NAME).build());

        service.bookSpots(1, 1, 1);

        Optional<UserRouteEntity> userRouteEntity = userRouteRepository.findById(UserRouteId.builder().routeId(1).userId(1).build());

        Assertions.assertTrue(userRouteEntity.isPresent());
        Assertions.assertEquals(1, userRouteEntity.get().getSpotCount());
        Assertions.assertEquals(9, userRouteEntity.get().getRoute().getSpots());
        Assertions.assertEquals(ROUTE_NAME, userRouteEntity.get().getRoute().getName());
        Assertions.assertEquals(USER_NAME, userRouteEntity.get().getUser().getName());

        service.bookSpots(1, 1, 2);

        userRouteEntity = userRouteRepository.findById(UserRouteId.builder().routeId(1).userId(1).build());

        Assertions.assertTrue(userRouteEntity.isPresent());
        Assertions.assertEquals(3, userRouteEntity.get().getSpotCount());
        Assertions.assertEquals(7, userRouteEntity.get().getRoute().getSpots());
        Assertions.assertEquals(ROUTE_NAME, userRouteEntity.get().getRoute().getName());
        Assertions.assertEquals(USER_NAME, userRouteEntity.get().getUser().getName());
    }

    @Test
    @Transactional
    @Rollback
    void testWhenTwoUsersBookSpots() {
        routeRepository.save(RouteEntity.builder().id(1).name(ROUTE_NAME).spots(SPOTS).build());
        userRepository.save(UserEntity.builder().id(1).name(USER_NAME).build());
        userRepository.save(UserEntity.builder().id(2).name("Eddi").build());

        service.bookSpots(1, 1, 1);
        service.bookSpots(2, 1, 2);
        List<UserRouteEntity> all = (List<UserRouteEntity>) userRouteRepository.findAll();
        Assertions.assertEquals(2, all.size());

        UserRouteEntity userRouteFirst = all.get(0);

        Assertions.assertEquals(1, userRouteFirst.getSpotCount());
        Assertions.assertEquals(7, userRouteFirst.getRoute().getSpots());
        Assertions.assertEquals(ROUTE_NAME, userRouteFirst.getRoute().getName());
        Assertions.assertEquals(USER_NAME, userRouteFirst.getUser().getName());

        UserRouteEntity userRouteSecond = all.get(1);

        Assertions.assertEquals(2, userRouteSecond.getSpotCount());
        Assertions.assertEquals(7, userRouteSecond.getRoute().getSpots());
        Assertions.assertEquals(ROUTE_NAME, userRouteSecond.getRoute().getName());
        Assertions.assertEquals("Eddi", userRouteSecond.getUser().getName());
    }

    @Test
    @Transactional
    @Rollback
    public void testCancelSpots() {
        RouteEntity routeEntity = RouteEntity.builder().id(1).name(ROUTE_NAME).spots(SPOTS).build();
        routeRepository.save(routeEntity);
        UserEntity userEntity = UserEntity.builder().id(1).name(USER_NAME).build();
        userRepository.save(userEntity);

        userRouteRepository.save(UserRouteEntity.builder()
                .id(UserRouteId.builder().userId(userEntity.getId()).routeId(routeEntity.getId()).build())
                .route(routeEntity)
                .user(userEntity)
                .spotCount(2)
                .build());

        service.cancelReservation(1, 1, 0);

        Assertions.assertFalse(userRouteRepository.existsById(UserRouteId.builder().userId(userEntity.getId()).routeId(routeEntity.getId()).build()));
        RouteEntity updatedRouteEntity = routeRepository.findById(1).get();
        Assertions.assertEquals(12, updatedRouteEntity.getSpots());
    }

    @Test
    public void testWhenReservationNotExists() {
        NoReservationExists exception = Assertions.assertThrows(NoReservationExists.class, () -> {
            service.cancelReservation(1, 1, 0);
        });
        Assertions.assertEquals(NO_RESERVATION_EXISTS, exception.getMessage());
    }

    @Test
    @Transactional
    @Rollback
    public void testFlexibleCancellation() {
        RouteEntity routeEntity = RouteEntity.builder().id(1).name(ROUTE_NAME).spots(SPOTS).build();
        routeRepository.save(routeEntity);
        UserEntity userEntity = UserEntity.builder().id(1).name(USER_NAME).build();
        userRepository.save(userEntity);

        userRouteRepository.save(UserRouteEntity.builder()
                .id(UserRouteId.builder().userId(userEntity.getId()).routeId(routeEntity.getId()).build())
                .route(routeEntity)
                .user(userEntity)
                .spotCount(2)
                .build());

        service.cancelReservation(1, 1, 1);

        Assertions.assertTrue(userRouteRepository.existsById(UserRouteId.builder().userId(userEntity.getId()).routeId(routeEntity.getId()).build()));
        UserRouteEntity userRouteEntity = userRouteRepository.findById(UserRouteId.builder().userId(userEntity.getId()).routeId(routeEntity.getId()).build()).get();
        Assertions.assertEquals(1, userRouteEntity.getSpotCount());

        RouteEntity updatedRouteEntity = routeRepository.findById(1).get();
        Assertions.assertEquals(11, updatedRouteEntity.getSpots());
    }

    @Test
    @Transactional
    @Rollback
    public void testWhenFlexibleCancellationSpotsMoreThenOrdered() {
        RouteEntity routeEntity = RouteEntity.builder().id(1).name(ROUTE_NAME).spots(SPOTS).build();
        routeRepository.save(routeEntity);
        UserEntity userEntity = UserEntity.builder().id(1).name(USER_NAME).build();
        userRepository.save(userEntity);

        userRouteRepository.save(UserRouteEntity.builder()
                .id(UserRouteId.builder().userId(userEntity.getId()).routeId(routeEntity.getId()).build())
                .route(routeEntity)
                .user(userEntity)
                .spotCount(2)
                .build());

        CancellationSpotsMoreThanBooked exception = Assertions.assertThrows(CancellationSpotsMoreThanBooked.class, () -> {
            service.cancelReservation(1, 1, 3);
        });

        Assertions.assertEquals(TRY_TO_CANCEL_MORE_THAN_RESERVED, exception.getMessage());


        Assertions.assertTrue(userRouteRepository.existsById(UserRouteId.builder().userId(userEntity.getId()).routeId(routeEntity.getId()).build()));
        UserRouteEntity userRouteEntity = userRouteRepository.findById(UserRouteId.builder().userId(userEntity.getId()).routeId(routeEntity.getId()).build()).get();
        Assertions.assertEquals(2, userRouteEntity.getSpotCount());

        RouteEntity updatedRouteEntity = routeRepository.findById(1).get();
        Assertions.assertEquals(SPOTS, updatedRouteEntity.getSpots());
    }
}