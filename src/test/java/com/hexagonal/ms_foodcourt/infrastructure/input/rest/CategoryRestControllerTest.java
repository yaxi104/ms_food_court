package com.hexagonal.ms_foodcourt.infrastructure.input.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.hexagonal.ms_foodcourt.application.dto.request.CategoryRequest;
import com.hexagonal.ms_foodcourt.application.dto.request.PaginatedResponse;
import com.hexagonal.ms_foodcourt.application.dto.response.CategoryResponse;
import com.hexagonal.ms_foodcourt.application.handler.ICategoryHandler;
import com.hexagonal.ms_foodcourt.infrastructure.exceptionhandler.ControllerAdvisor;
import com.hexagonal.ms_foodcourt.util.TestDataCategoryFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class CategoryRestControllerTest {

    private MockMvc mockMvc;
    private ICategoryHandler categoryHandler;

    private JacksonTester<CategoryRequest> categoryRequestTester;

    @BeforeEach
    void setUp() {
        categoryHandler = mock(ICategoryHandler.class);
        CategoryRestController controller = new CategoryRestController(categoryHandler);

        ObjectMapper objectMapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .registerModule(new ParameterNamesModule())
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        JacksonTester.initFields(this, objectMapper);

        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new ControllerAdvisor())
                .build();

        var auth = new TestingAuthenticationToken("owner", "password", "ROLE_PROPIETARIO");
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    @Test
    void saveCategorySuccess() throws Exception {
        CategoryRequest request = TestDataCategoryFactory.mockCategoryRequest();

        mockMvc.perform(post("/api/v1/category/owner")
                        .with(authentication(SecurityContextHolder.getContext().getAuthentication()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(categoryRequestTester.write(request).getJson()))
                .andExpect(status().isCreated());

        verify(categoryHandler).saveCategory(any(CategoryRequest.class));
    }

    @Test
    void saveCategoryBadRequest() throws Exception {
        CategoryRequest invalidRequest = new CategoryRequest();
        invalidRequest.setName(null);

        mockMvc.perform(post("/api/v1/category/owner")
                        .with(authentication(SecurityContextHolder.getContext().getAuthentication()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(categoryRequestTester.write(invalidRequest).getJson()))
                .andExpect(status().isBadRequest());

        verify(categoryHandler, never()).saveCategory(any());
    }

    @Test
    void listAllCategoriesSuccess() throws Exception {
        List<CategoryResponse> categories = List.of(TestDataCategoryFactory.mockCategoryResponse());

        PaginatedResponse<CategoryResponse> paginated = new PaginatedResponse<>(
                categories,
                1,
                2L,
                true
        );

        when(categoryHandler.findAll(null, null)).thenReturn(paginated);

        mockMvc.perform(get("/api/v1/category/all")
                        .with(authentication(SecurityContextHolder.getContext().getAuthentication())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());

        verify(categoryHandler, times(1)).findAll(null, null);
    }
}
