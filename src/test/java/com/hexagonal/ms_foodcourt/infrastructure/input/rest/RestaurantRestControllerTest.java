package com.hexagonal.ms_foodcourt.infrastructure.input.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.hexagonal.ms_foodcourt.application.dto.request.RestaurantRequest;
import com.hexagonal.ms_foodcourt.application.handler.IRestaurantHandler;
import com.hexagonal.ms_foodcourt.domain.exception.RestaurantAlreadyExistsException;
import com.hexagonal.ms_foodcourt.infrastructure.exceptionhandler.ControllerAdvisor;
import com.hexagonal.ms_foodcourt.util.TestDataFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class RestaurantRestControllerTest {

    private MockMvc mockMvc;
    private IRestaurantHandler restaurantHandler;

    private JacksonTester<RestaurantRequest>  jacksonTester;

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
        RestaurantRequest restaurantRequest = TestDataFactory.mockRestaurantRequest();

        mockMvc.perform(post("/api/v1/restaurant/admin")
                        .with(authentication(SecurityContextHolder.getContext().getAuthentication()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jacksonTester.write(restaurantRequest).getJson()))
                .andExpect(status().isCreated());

        verify(restaurantHandler).saveRestaurant(any(RestaurantRequest.class));
    }

    @Test
    void saveUserAlreadyExistsTest() throws Exception {
        RestaurantRequest restaurantRequest = TestDataFactory.mockRestaurantRequest();

        doThrow(new RestaurantAlreadyExistsException())
                .when(restaurantHandler).saveRestaurant(any(RestaurantRequest.class));

        mockMvc.perform(post("/api/v1/restaurant/admin")
                        .with(authentication(SecurityContextHolder.getContext().getAuthentication()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jacksonTester.write(restaurantRequest).getJson()))
                .andExpect(status().isConflict());
    }

}