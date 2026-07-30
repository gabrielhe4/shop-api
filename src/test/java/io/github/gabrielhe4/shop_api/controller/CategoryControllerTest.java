package io.github.gabrielhe4.shop_api.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.gabrielhe4.shop_api.config.AppConstants;
import io.github.gabrielhe4.shop_api.dto.CategoryRequest;
import io.github.gabrielhe4.shop_api.dto.PaginatedCategoryResponse;
import io.github.gabrielhe4.shop_api.security.AuthTokenFilter;
import io.github.gabrielhe4.shop_api.security.JwtUtils;
import io.github.gabrielhe4.shop_api.service.CategoryService;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;



@WebMvcTest(CategoryController.class)
@AutoConfigureMockMvc(addFilters = false)
public class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CategoryService categoryService;

    @MockitoBean
    JwtUtils jwtUtils;

    @MockitoBean
    AuthTokenFilter authTokenFilter;

    private static final String ADMIN_CATEGORY_PATH = "/api/admin/category";
    private static final String ADMIN_CATEGORY_ID_PATH = "/api/admin/categories/{id}";
    private static final String PUBLIC_CATEGORIES_PATH = "/api/public/categories";

    @Test
    //@WithMockUser(username = "gabriel", roles = {"ADMIN"})
    void createCategory_ShouldReturnCreatedStatus_WhenRequestIsValid() throws Exception {
        CategoryRequest request = new CategoryRequest("Electronics", "Electronic devices");
        String body = objectMapper.writeValueAsString(request);

        mockMvc.perform(post(ADMIN_CATEGORY_PATH)
                //.with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isCreated());

    }

    @Test
    void createCategory_ShouldReturnBadRequest_WhenRequiredFieldIsMissing() throws Exception {
        CategoryRequest request = new CategoryRequest(null, "some description");
        String body = objectMapper.writeValueAsString(request);

        mockMvc.perform(post(ADMIN_CATEGORY_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createCategory_ShouldReturnBadRequest_WhenAllFieldsAreEmpty() throws Exception {
        CategoryRequest request = new CategoryRequest("", "");
        String body = objectMapper.writeValueAsString(request);

        mockMvc.perform(post(ADMIN_CATEGORY_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isBadRequest());
    }


    void createCategory_ShouldReturnUnauthorized_WhenNotAuthenticated() throws Exception {
        CategoryRequest request = new CategoryRequest("Electronics", "Electronic devices");
        String body = objectMapper.writeValueAsString(request);

        mockMvc.perform(post(ADMIN_CATEGORY_PATH)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isUnauthorized());
    }

    void updateCategory_ShouldReturnUnauthorized_WhenNotAuthenticated() throws Exception {
        CategoryRequest request = new CategoryRequest("Books", "Book store");
        String body = objectMapper.writeValueAsString(request);

        mockMvc.perform(put(ADMIN_CATEGORY_ID_PATH, "1")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void updateCategory_ShouldReturnBadRequest_WhenIdIsNullOrEmpty() throws Exception {
        CategoryRequest request = new CategoryRequest("Books", "Book store");
        String body = objectMapper.writeValueAsString(request);

        when(categoryService.updateCategory(anyLong(), any(CategoryRequest.class)))
            .thenThrow(new IllegalArgumentException("Invalid ID"));

        mockMvc.perform(put(ADMIN_CATEGORY_ID_PATH, "e")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAllCategories_ShouldReturnSuccess_WhenDefaultParametersAreUsed() throws Exception {
        PaginatedCategoryResponse expectedResponse = createMockPaginatedResponse();

        when(categoryService.getAllCategories(anyInt(),anyInt(), anyString(), anyString()))
            .thenReturn(expectedResponse);
        
        mockMvc.perform(get(PUBLIC_CATEGORIES_PATH))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.pageNumber").value(0))
            .andExpect(jsonPath("$.pageSize").value(15));

        verify(categoryService).getAllCategories(
            Integer.parseInt(AppConstants.PAGE_NUMBER), 
            Integer.parseInt(AppConstants.PAGE_SIZE), 
            AppConstants.SORT_CUSTOMERS_BY, 
            AppConstants.SORT_DIR);

    }

    @Test
    void deleteCategory_ShouldReturnNoContent_WhenRequestIsValid() throws Exception {
        mockMvc.perform(delete(ADMIN_CATEGORY_ID_PATH, "123"))
            .andExpect(status().isNoContent());

        verify(categoryService).deleteCategory(123L);
    }



    private PaginatedCategoryResponse createMockPaginatedResponse() {
        return PaginatedCategoryResponse.builder()
                .categories(List.of())
                .pageNumber(0)
                .pageSize(15)
                .totalElements(0L)
                .totalPages(0)
                .lastPage(true)
                .build();
    }






}
