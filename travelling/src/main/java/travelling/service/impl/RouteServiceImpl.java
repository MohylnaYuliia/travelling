package travelling.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
import travelling.service.RouteService;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static travelling.constant.Constants.ALL_SPOTS_SOLD_OUT;
import static travelling.constant.Constants.DEFAULT_NUMBER_TO_CANCEL_ALL_SPOTS;
import static travelling.constant.Constants.NOT_ENOUGH_SPOTS;
import static travelling.constant.Constants.NO_RESERVATION_EXISTS;
import static travelling.constant.Constants.ROUTE_NOT_EXISTS;
import static travelling.constant.Constants.TRY_TO_CANCEL_MORE_THAN_RESERVED;
import static travelling.constant.Constants.USER_NOT_EXISTS;

@Service
public class RouteServiceImpl implements RouteService {

    @Autowired
    private RouteRepository routeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserRouteRepository userRouteRepository;

    @Override
    @Transactional(readOnly = true)
    public List<RouteEntity> getAllRouts() {
        List<RouteEntity> result = new ArrayList<>();
        routeRepository.findAll().forEach(result::add);
        return result;
    }

    @Override
    @Transactional
    public void bookSpots(Integer userId, Integer routId, Integer spotNumber) {
        UserEntity userEntity = userRepository.findById(userId).orElseThrow(() -> new UserNotExistsException(USER_NOT_EXISTS));
        RouteEntity routeEntity = routeRepository.findById(routId).orElseThrow(() -> new RouteNotExistsException(ROUTE_NOT_EXISTS));

        if (routeEntity.getSpots() != 0 && routeEntity.getSpots() < spotNumber) {
            throw new NotEnoughSpotsException(NOT_ENOUGH_SPOTS);
        }
        if (routeEntity.getSpots() == 0) {
            throw new SpotsSoldOutException(ALL_SPOTS_SOLD_OUT);
        }
        Optional<UserRouteEntity> userRouteEntity = userRouteRepository.findById(UserRouteId.builder().routeId(routId).userId(userId).build());
        UserRouteEntity userRouteExisted = null;
        if (userRouteEntity.isPresent()) {
            userRouteExisted = userRouteEntity.get();
            userRouteExisted.setSpotCount(userRouteExisted.getSpotCount() + spotNumber);
            userRouteRepository.save(userRouteExisted);
            routeEntity.setSpots(routeEntity.getSpots() - spotNumber);
            routeRepository.save(routeEntity);
            return;
        }

        routeEntity.setSpots(routeEntity.getSpots() - spotNumber);

        userRouteRepository.save(UserRouteEntity.builder()
                .id(UserRouteId.builder().userId(userId).routeId(routId).build())
                .route(routeEntity).user(userEntity)
                .spotCount(Objects.isNull(userRouteExisted) ? spotNumber : spotNumber + userRouteExisted.getSpotCount())
                .build());
    }

    @Override
    @Transactional
    public void cancelReservation(Integer userId, Integer routId, Integer spots) {
        UserRouteEntity reservation = userRouteRepository.findById(UserRouteId.builder().routeId(routId).userId(userId).build())
                .orElseThrow(() -> new NoReservationExists(NO_RESERVATION_EXISTS));

        RouteEntity route = reservation.getRoute();
        if (spots > reservation.getSpotCount()) {
            throw new CancellationSpotsMoreThanBooked(TRY_TO_CANCEL_MORE_THAN_RESERVED);
        }
        if (spots == DEFAULT_NUMBER_TO_CANCEL_ALL_SPOTS) {
            route.setSpots(route.getSpots() + reservation.getSpotCount());
            userRouteRepository.delete(reservation);
            return;
        }
        route.setSpots(route.getSpots() + spots);
        reservation.setSpotCount(reservation.getSpotCount() - spots);
        routeRepository.save(route);
    }
}
