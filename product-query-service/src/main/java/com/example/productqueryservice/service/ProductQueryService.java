package com.example.productqueryservice.service;

import com.example.productqueryservice.entity.Product;
import com.example.productqueryservice.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductQueryService {

    private final ProductRepository productRepository;

    public Product getProductById(Long id){
        return productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + id));
    }

    public Product getProductBySku(String sku){
        return productRepository.findBySku(sku)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with sku: " + sku));
    }

    public List<Product> getProductByName(String name){
        return productRepository.findByNameContaining(name);
    }

    public List<Product> getAllProducts(){
        return productRepository.findAll();
    }
}
