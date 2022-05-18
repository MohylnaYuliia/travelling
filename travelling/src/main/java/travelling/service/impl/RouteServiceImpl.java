package travelling.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import travelling.entity.RouteEntity;
import travelling.entity.UserEntity;
import travelling.entity.UserRouteEntity;
import travelling.entity.UserRouteId;
import travelling.repository.RouteRepository;
import travelling.repository.UserRepository;
import travelling.repository.UserRouteRepository;
import travelling.service.RouteService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
        Optional<UserEntity> userEntity = userRepository.findById(userId);
        Optional<RouteEntity> routeEntity = routeRepository.findById(routId);

        RouteEntity route = routeEntity.get();
        route.setSpots(route.getSpots() - spotNumber);

        userRouteRepository.save(UserRouteEntity.builder()
                .id(UserRouteId.builder().userId(userId).routeId(routId).build())
                .route(route).user(userEntity.get()).spotCount(spotNumber).build());
    }
}
