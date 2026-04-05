package com.chandan.pos.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.chandan.pos.mapper.ProductMapper;
import com.chandan.pos.modal.Product;
import com.chandan.pos.modal.Store;
import com.chandan.pos.modal.User;
import com.chandan.pos.payload.dto.ProductDTO;
import com.chandan.pos.repository.ProductRepository;
import com.chandan.pos.repository.StoreRepository;
import com.chandan.pos.service.ProductService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final StoreRepository storeRepository;

    @Override
    public ProductDTO createProduct(ProductDTO productDTO, User user) throws Exception {
        Store store = storeRepository.findById(productDTO.getStoreId()).orElseThrow(
            ()->new Exception("Store not found")
        );

        Product product=ProductMapper.toEntity(productDTO, store);
        Product savedProduct=productRepository.save(product);
        return ProductMapper.toDTO(savedProduct);
    }

    @Override
    public ProductDTO updateProduct(Long id, ProductDTO productDTO, User user) throws Exception {
        Product product = productRepository.findById(id).orElseThrow(
            ()-> new Exception("product not found")
        );

        product.setSku(product.getSku());
        product.setName(product.getName());
        product.setDescription(product.getDescription());
        product.setMrp(product.getMrp());
        product.setSellingPrice(product.getSellingPrice());
        product.setBrand(product.getBrand());
        product.setImage(product.getImage());
        product.setUpdatedAt(product.getUpdatedAt());

        Product savedProduct = productRepository.save(product);
        return ProductMapper.toDTO(savedProduct);
    }

    @Override
    public void deleteProduct(Long id, User user) throws Exception {
        Product product = productRepository.findById(id).orElseThrow(
            ()-> new Exception("product not found")
        );

        productRepository.delete(product);
    }

    @Override
    public List<ProductDTO> getProductsByStoreId(Long storeId) {
        List<Product> products = productRepository.findByStoreId(storeId);
        return products.stream()
                  .map(ProductMapper::toDTO)
                  .collect(Collectors.toList());
    }

    @Override
    public List<ProductDTO> searchByKeyword(Long storeId, String keyword) {
        List<Product> products = productRepository.searchByKeyword(storeId, keyword);
        return products.stream()
                  .map(ProductMapper::toDTO)
                  .collect(Collectors.toList());
    }
    
}
