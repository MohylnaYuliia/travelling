package travelling.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import travelling.entity.RouteEntity;
import travelling.entity.UserEntity;
import travelling.repository.RouteRepository;
import travelling.service.RouteService;

import java.util.ArrayList;
import java.util.List;

@Service
public class RouteServiceImpl implements RouteService {

    @Autowired
    private RouteRepository routeRepository;

    @Override
    public List<RouteEntity> getAllRouts() {
        List<RouteEntity> result = new ArrayList<>();
        routeRepository.findAll().forEach(result::add);
        return result;
    }

    @Override
    public void bookSpots(Integer userId, Integer routId, int spotNumber) {

    }
}
