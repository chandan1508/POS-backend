package com.chandan.pos.mapper;

import com.chandan.pos.modal.Category;
import com.chandan.pos.payload.dto.CategoryDTO;

public class CategoryMapper {
    public static CategoryDTO toDTO(Category category){
        return CategoryDTO.builder()
                  .id(category.getId())
                  .name(category.getName())
                  .storeId(category.getStore()!=null?category.getStore().getId():null)
                  .build();
    }
}
