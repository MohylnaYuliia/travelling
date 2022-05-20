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
import travelling.dto.UserRouteDto;
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
        UserRouteDto dto = UserRouteDto.builder().routeName("Berlin-Paris").routeId(1).userId(1).userName("John").routeSpotsNumber(10).reservedSpots(1).build();

        when(service.getAllInformation()).thenReturn(Arrays.asList(dto));
        mockMvc.perform(MockMvcRequestBuilders
                .get("/admin")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", Matchers.hasSize(1)))
                .andExpect(jsonPath("$[0].*", Matchers.hasSize(6)))
                .andExpect(jsonPath("$[0].reservedSpots").value(1));
    }

}