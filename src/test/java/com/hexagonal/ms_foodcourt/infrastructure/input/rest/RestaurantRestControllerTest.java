package com.hexagonal.ms_foodcourt.infrastructure.input.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.hexagonal.ms_foodcourt.application.dto.response.PaginatedResponse;
import com.hexagonal.ms_foodcourt.application.dto.request.RestaurantRequest;
import com.hexagonal.ms_foodcourt.application.dto.response.RestaurantResponse;
import com.hexagonal.ms_foodcourt.application.handler.IRestaurantHandler;
import com.hexagonal.ms_foodcourt.domain.exception.RestaurantAlreadyExistsException;
import com.hexagonal.ms_foodcourt.infrastructure.exceptionhandler.ControllerAdvisor;
import com.hexagonal.ms_foodcourt.util.TestDataRestaurantFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class RestaurantRestControllerTest {

    private MockMvc mockMvc;
    private IRestaurantHandler restaurantHandler;

    private JacksonTester<RestaurantRequest> jacksonTester;

    @BeforeEach
    void setUp() {
        restaurantHandler = mock(IRestaurantHandler.class);
        RestaurantRestController controller = new RestaurantRestController(restaurantHandler);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        JacksonTester.initFields(this, objectMapper);

        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new ControllerAdvisor()).build();

        var auth = new TestingAuthenticationToken("admin", "password", "ROLE_ADMIN");
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    @Test
    void saveUserSuccessTest() throws Exception {
        RestaurantRequest restaurantRequest = TestDataRestaurantFactory.mockRestaurantRequest();

        mockMvc.perform(post("/api/v1/restaurant/admin")
                        .with(authentication(SecurityContextHolder.getContext().getAuthentication()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jacksonTester.write(restaurantRequest).getJson()))
                .andExpect(status().isCreated());

        verify(restaurantHandler).saveRestaurant(any(RestaurantRequest.class));
    }

    @Test
    void saveUserAlreadyExistsTest() throws Exception {
        RestaurantRequest restaurantRequest = TestDataRestaurantFactory.mockRestaurantRequest();

        doThrow(new RestaurantAlreadyExistsException())
                .when(restaurantHandler).saveRestaurant(any(RestaurantRequest.class));

        mockMvc.perform(post("/api/v1/restaurant/admin")
                        .with(authentication(SecurityContextHolder.getContext().getAuthentication()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jacksonTester.write(restaurantRequest).getJson()))
                .andExpect(status().isConflict());
    }

    @Test
    void listRestaurantsSuccessTest() throws Exception {
        RestaurantResponse r1 = new RestaurantResponse();
        r1.setName("A la 1 a la 2 a la 3");
        r1.setUrlLogo("https://example.com/logo1");

        RestaurantResponse r2 = new RestaurantResponse();
        r2.setName("Zaza ya cuza ya cuza");
        r2.setUrlLogo("https://example.com/logo2");

        List<RestaurantResponse> responses = List.of(r1, r2);

        PaginatedResponse<RestaurantResponse> paginatedResponse = new PaginatedResponse<>(
                responses,
                1,
                2L,
                true
        );

        when(restaurantHandler.getListRestaurants(0, 10)).thenReturn(paginatedResponse);

        var auth = new TestingAuthenticationToken("cliente", "password", "ROLE_CLIENTE");
        SecurityContextHolder.getContext().setAuthentication(auth);

        mockMvc.perform(get("/api/v1/restaurant/all")
                        .param("page", "0")
                        .param("size", "10")
                        .with(authentication(auth)))
                .andExpect(status().isOk());
        verify(restaurantHandler).getListRestaurants(0, 10);

        SecurityContextHolder.clearContext();
    }
}