package travelling.service.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import travelling.entity.RouteEntity;
import travelling.repository.RouteRepository;
import travelling.repository.UserRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class RouteServiceImplTest {

    @Autowired
    private RouteServiceImpl service;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RouteRepository routeRepository;

    @BeforeEach
    public void setup() {
        userRepository.deleteAll();
        routeRepository.deleteAll();
    }

    @Test
    void testGetBooks() {
        Assertions.assertEquals(0, ((List<RouteEntity>)routeRepository.findAll()).size());

        Assertions.assertEquals(0, service.getAllRouts().size());
    }

    @Test
    void testGetAllBooks() {
        routeRepository.save(RouteEntity.builder().id(1).name("Munich-Berlin").spots(10).build());
        Assertions.assertEquals(1, ((List<RouteEntity>)routeRepository.findAll()).size());

        Assertions.assertEquals(1, service.getAllRouts().size());
        Assertions.assertEquals("Munich-Berlin", ((List<RouteEntity>)routeRepository.findAll()).get(0).getName());
    }

}