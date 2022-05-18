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
    public void returnBook(@PathVariable Integer routeId, @PathVariable Integer userId, Integer spotsNumber) {

    }
}
