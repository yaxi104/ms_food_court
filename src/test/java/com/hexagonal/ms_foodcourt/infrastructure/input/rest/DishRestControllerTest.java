package com.hexagonal.ms_foodcourt.infrastructure.input.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.hexagonal.ms_foodcourt.application.dto.request.DishRequest;
import com.hexagonal.ms_foodcourt.application.dto.request.DishToggleStatusRequest;
import com.hexagonal.ms_foodcourt.application.dto.request.DishUpdateRequest;
import com.hexagonal.ms_foodcourt.application.dto.response.DishResponse;
import com.hexagonal.ms_foodcourt.application.handler.IDishHandler;
import com.hexagonal.ms_foodcourt.infrastructure.exceptionhandler.ControllerAdvisor;
import com.hexagonal.ms_foodcourt.util.TestDataDishFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(MockitoExtension.class)
class DishRestControllerTest {

    private MockMvc mockMvc;
    private IDishHandler dishHandler;

    private JacksonTester<DishRequest> dishRequestTester;
    private JacksonTester<DishUpdateRequest> dishUpdateRequestTester;
    private JacksonTester<DishToggleStatusRequest> dishToggleStatusRequestTester;

    @BeforeEach
    void setUp() {
        dishHandler = mock(IDishHandler.class);
        DishRestController controller = new DishRestController(dishHandler);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        JacksonTester.initFields(this, objectMapper);

        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new ControllerAdvisor())
                .build();

        var auth = new TestingAuthenticationToken("owner", "password", "ROLE_PROPIETARIO");
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    @Test
    void saveDishSuccess() throws Exception {
        DishRequest request = TestDataDishFactory.mockDishRequest();

        mockMvc.perform(post("/api/v1/dish/owner")
                        .with(authentication(SecurityContextHolder.getContext().getAuthentication()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(dishRequestTester.write(request).getJson()))
                .andExpect(status().isCreated());

        verify(dishHandler).saveDish(any(DishRequest.class));
    }

    @Test
    void updateDishSuccess() throws Exception {
        DishUpdateRequest request = TestDataDishFactory.mockDishUpdateRequest();

        mockMvc.perform(patch("/api/v1/dish/owner")
                        .with(authentication(SecurityContextHolder.getContext().getAuthentication()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(dishUpdateRequestTester.write(request).getJson()))
                .andExpect(status().isNoContent());

        verify(dishHandler).updateDish(any(DishUpdateRequest.class));
    }

    @Test
    void toggleDishStatusSuccess() throws Exception {
        DishToggleStatusRequest request = TestDataDishFactory.mockDishToggleStatusRequest();

        mockMvc.perform(patch("/api/v1/dish/toggle/status")
                        .with(authentication(SecurityContextHolder.getContext().getAuthentication()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(dishToggleStatusRequestTester.write(request).getJson()))
                .andExpect(status().isNoContent());

        verify(dishHandler).toggleDishStatus(any(DishToggleStatusRequest.class));
    }

    @Test
    void listDishesWithCategoryIdTest() throws Exception {
        Long restaurantId = 1L;
        Long categoryId = 2L;
        int page = 0;
        int size = 10;

        DishResponse d1 = new DishResponse();
        d1.setName("Bandeja paisa");
        d1.setPrice(25000);
        d1.setDescription("Completa");
        d1.setImageUrl("https://img.com/1");
        DishResponse d2 = new DishResponse();
        d2.setName("Arepa");
        d2.setPrice(5000);
        d2.setDescription("Con queso");
        d2.setImageUrl("https://img.com/2");
        Page<DishResponse> pageResult = new PageImpl<>(List.of(d1, d2), PageRequest.of(page, size), 2);

        when(dishHandler.getListRestaurants(restaurantId, categoryId, page, size)).thenReturn(pageResult);

        mockMvc.perform(get("/api/v1/dish/all/{restaurantId}", restaurantId)
                        .param("categoryId", categoryId.toString())
                        .param("page", String.valueOf(page))
                        .param("size", String.valueOf(size))
                        .with(user("cliente").roles("CLIENTE")))
                .andExpect(status().isOk());
        verify(dishHandler).getListRestaurants(restaurantId, categoryId, page, size);
    }

}