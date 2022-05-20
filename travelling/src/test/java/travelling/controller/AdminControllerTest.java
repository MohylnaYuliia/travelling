package travelling.controller;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import travelling.entity.RouteEntity;
import travelling.entity.UserEntity;
import travelling.entity.UserRouteEntity;
import travelling.entity.UserRouteId;
import travelling.service.impl.AdminServiceImpl;

import java.util.Arrays;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AdminServiceImpl service;

    @Test
    void getAllRoutes() throws Exception {
        RouteEntity routeFirst = RouteEntity.builder().id(1).name("Berlin-Paris").spots(10).build();
        UserEntity userEntity = UserEntity.builder().id(1).name("John").build();
        UserRouteEntity userRouteEntity = UserRouteEntity.builder()
                .id(UserRouteId.builder().userId(1).routeId(1).build())
                .spotCount(1)
                .user(userEntity)
                .route(routeFirst).build();

        when(service.getAllInformation()).thenReturn(Arrays.asList(userRouteEntity));
        mockMvc.perform(MockMvcRequestBuilders
                .get("/admin")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", Matchers.hasSize(1)))
                .andExpect(jsonPath("$[0].*", Matchers.hasSize(4)))
                .andExpect(jsonPath("$[0].spotsCount").value(1));
    }

}