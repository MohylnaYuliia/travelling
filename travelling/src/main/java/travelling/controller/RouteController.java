package travelling.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import travelling.entity.RouteEntity;
import travelling.service.RouteService;

import java.util.List;

@RestController
@RequestMapping("/route")
public class RouteController {

    @Autowired
    private RouteService routeService;

    @GetMapping
    public List<RouteEntity> getAllBooks() {
        return routeService.getAllRouts();
    }

    @PostMapping("/{routeId}/users/{userId}/spots/{spotsNumber}")
    public void bookRoute(@PathVariable Integer routeId, @PathVariable Integer userId, @PathVariable Integer spotsNumber) {
        routeService.bookSpots(userId, routeId, spotsNumber);
    }

    @DeleteMapping("/{routeId}/users/{userId}")
    public void cancelReservation(@PathVariable Integer routeId, @PathVariable Integer userId) {
        routeService.cancelReservation(userId, routeId, 0);
    }

    @DeleteMapping("/{routeId}/users/{userId}/spots/{spotsNumber}")
    public void cancelSpots(@PathVariable Integer routeId, @PathVariable Integer userId, @PathVariable Integer spotsNumber) {
        routeService.cancelReservation(userId, routeId, spotsNumber);
    }
}
