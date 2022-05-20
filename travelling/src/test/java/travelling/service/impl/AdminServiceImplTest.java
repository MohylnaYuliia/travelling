package travelling.service.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import travelling.entity.RouteEntity;
import travelling.entity.UserEntity;
import travelling.entity.UserRouteEntity;
import travelling.entity.UserRouteId;
import travelling.repository.RouteRepository;
import travelling.repository.UserRepository;
import travelling.repository.UserRouteRepository;

import java.util.List;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class AdminServiceImplTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RouteRepository routeRepository;

    @Autowired
    private UserRouteRepository userRouteRepository;

    @Autowired
    private AdminServiceImpl adminService;

    @Test
    public void getAllInformation() {
        RouteEntity routeFirst = RouteEntity.builder().id(1).name("Berlin-Paris").spots(10).build();
        routeRepository.save(routeFirst);
        UserEntity userEntity = UserEntity.builder().id(1).name("John").build();
        userRepository.save(userEntity);
        userRouteRepository.save(UserRouteEntity.builder()
                .id(UserRouteId.builder().userId(1).routeId(1).build())
                .spotCount(1)
                .user(userEntity)
                .route(routeFirst)
                .build());
        Assertions.assertEquals(1, ((List<UserRouteEntity>) adminService.getAllInformation()).size());
    }

}