package com.chandan.pos.mapper;

import com.chandan.pos.modal.Product;
import com.chandan.pos.modal.Store;
import com.chandan.pos.payload.dto.ProductDTO;

public class ProductMapper {

    public static ProductDTO toDTO(Product product){
        if(product == null) return null;

        ProductDTO dto = new ProductDTO();

        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setSku(product.getSku());
        dto.setDescription(product.getDescription());
        dto.setMrp(product.getMrp());
        dto.setSellingPrice(product.getSellingPrice());
        dto.setBrand(product.getBrand());
        dto.setImage(product.getImage());

        // Store mapping
        if(product.getStore() != null){
            dto.setStoreId(product.getStore().getId());
        }

        // Category not implemented yet
        // dto.setCategoryId(...);

        dto.setCreatedAt(product.getCreatedAt());
        dto.setUpdatedAt(product.getUpdatedAt());

        return dto;
    }
    

    public static Product toEntity(ProductDTO productDTO, Store store){
        if(productDTO == null) return null;

        Product product = new Product();

        product.setName(productDTO.getName());
        product.setSku(productDTO.getSku());
        product.setDescription(productDTO.getDescription());
        product.setMrp(productDTO.getMrp());
        product.setSellingPrice(productDTO.getSellingPrice());
        product.setBrand(productDTO.getBrand());

        return product;
    }
}