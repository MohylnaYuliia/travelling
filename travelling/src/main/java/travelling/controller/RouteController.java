package travelling.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import travelling.entity.RouteEntity;
import travelling.exception.WrongNumberOfSpotsException;
import travelling.service.RouteService;

import java.util.List;

import static travelling.constant.Constants.DEFAULT_NUMBER_TO_CANCEL_ALL_SPOTS;
import static travelling.constant.Constants.WRONG_NUMBER_OF_SPOTS;

@RestController
@RequestMapping("/route")
public class RouteController {

    @Autowired
    private RouteService routeService;

    @GetMapping
    public List<RouteEntity> getAllRouts() {
        return routeService.getAllRouts();
    }

    @PostMapping("/{routeId}/users/{userId}/spots/{spotsNumber}")
    public void bookRoute(@PathVariable Integer routeId, @PathVariable Integer userId, @PathVariable Integer spotsNumber) {
        if (spotsNumber <= 0) {
            throw new WrongNumberOfSpotsException(WRONG_NUMBER_OF_SPOTS);
        }
        routeService.bookSpots(userId, routeId, spotsNumber);
    }

    @DeleteMapping("/{routeId}/users/{userId}")
    public void cancelReservation(@PathVariable Integer routeId, @PathVariable Integer userId) {
        routeService.cancelReservation(userId, routeId, DEFAULT_NUMBER_TO_CANCEL_ALL_SPOTS);
    }

    @DeleteMapping("/{routeId}/users/{userId}/spots/{spotsNumber}")
    public void cancelSpots(@PathVariable Integer routeId, @PathVariable Integer userId, @PathVariable Integer spotsNumber) {
        if (spotsNumber <= 0) {
            throw new WrongNumberOfSpotsException(WRONG_NUMBER_OF_SPOTS);
        }
        routeService.cancelReservation(userId, routeId, spotsNumber);
    }
}
