package com.chandan.pos.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.chandan.pos.payload.dto.CategoryDTO;
import com.chandan.pos.payload.response.ApiResponse;
import com.chandan.pos.service.CategoryService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/categories")
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<CategoryDTO> createCategory(
        @RequestBody CategoryDTO categoryDTO
    ) throws Exception{
        return ResponseEntity.ok(
            categoryService.createCategory(categoryDTO)
        );
    }

    @GetMapping("/store/{storeId}")
    public ResponseEntity<List<CategoryDTO>> getCategoriesByStoreId(
        @PathVariable Long storeId
    ) throws Exception{
        return ResponseEntity.ok(
            categoryService.getCategoriesByStore(storeId)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryDTO> updateCategory(
        @RequestBody CategoryDTO categoryDTO,
        @PathVariable Long id
    ) throws Exception{
        return ResponseEntity.ok(
            categoryService.updateCategory(id, categoryDTO)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteCategory(
        @PathVariable Long id
    ) throws Exception{
        categoryService.deleteCategory(id);
        ApiResponse apiResponse=new ApiResponse();
        apiResponse.setMessage("category delete successfully");
        return ResponseEntity.ok(
            apiResponse
        );
    }
}
