package travelling.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
import travelling.service.RouteService;

import java.util.ArrayList;
import java.util.List;

@Service
public class RouteServiceImpl implements RouteService {

    @Autowired
    private RouteRepository routeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserRouteRepository userRouteRepository;

    @Override
    public List<RouteEntity> getAllRouts() {
        List<RouteEntity> result = new ArrayList<>();
        routeRepository.findAll().forEach(result::add);
        return result;
    }

    @Override
    @Transactional
    public void bookSpots(Integer userId, Integer routId, int spotNumber) {
        UserEntity userEntity = userRepository.findById(userId).orElseThrow(() -> new UserNotExistsException("User not exists"));
        RouteEntity routeEntity = routeRepository.findById(routId).orElseThrow(() -> new RouteNotExistsException("Route not exists"));

        if (routeEntity.getSpots() == 0) {
            throw new SpotsSoldOutException("All spots are sold out");
        }
        if (routeEntity.getSpots() < spotNumber) {
            throw new NotEnoughSpotsException("Not enough spots");
        }
        routeEntity.setSpots(routeEntity.getSpots() - spotNumber);

        userRouteRepository.save(UserRouteEntity.builder()
                .id(UserRouteId.builder().userId(userId).routeId(routId).build())
                .route(routeEntity).user(userEntity).spotCount(spotNumber).build());
    }
}
