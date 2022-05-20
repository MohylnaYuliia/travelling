package travelling.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import travelling.entity.UserRouteEntity;
import travelling.service.AdminService;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService userService;

    @GetMapping
    public List<UserRouteEntity> getInformation() {
        return null;
    }
}
