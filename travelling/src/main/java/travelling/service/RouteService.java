package travelling.service;

import travelling.entity.RouteEntity;

import java.util.List;

public interface RouteService {

    List<RouteEntity> getAllRouts();

    void bookSpots(Integer userId, Integer routId, Integer spotNumber);

    void cancelReservation(Integer userId, Integer routId);
}
