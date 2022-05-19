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
import travelling.service.impl.RouteServiceImpl;

import java.util.Arrays;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class RouteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RouteServiceImpl service;

    @Test
    void getAllBooks() throws Exception {
        when(service.getAllRouts()).thenReturn(Arrays.asList(RouteEntity.builder().id(1).name("Munich-Berlin").spots(10).build()));
        mockMvc.perform(MockMvcRequestBuilders
                .get("/route")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", Matchers.hasSize(1)))
                .andExpect(jsonPath("$[0].*", Matchers.hasSize(3)))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Munich-Berlin"));
    }

    @Test
    public void testBookSpots() throws Exception {
        doNothing().when(service).bookSpots(1, 1, 1);

        mockMvc.perform(MockMvcRequestBuilders
                .post("/route/{routeId}/users/{userId}/spots/{spotsNumber}", 1, 1, 1)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void testCancelReservation() throws Exception {
        doNothing().when(service).cancelReservation(1, 1, 0);

        mockMvc.perform(MockMvcRequestBuilders
                .delete("/route/{routeId}/users/{userId}", 1, 1)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

}