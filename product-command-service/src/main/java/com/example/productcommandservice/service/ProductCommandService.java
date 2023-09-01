package com.example.productcommandservice.service;

import com.example.productcommandservice.entity.Product;
import com.example.productcommandservice.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductCommandService {

    private final ProductRepository productRepository;

    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    @Transactional
    public Product updateProduct(String sku, Product product) {
        Product existingProduct = productRepository.findBySku(sku)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with sku: " + sku));

        existingProduct.setName(product.getName());
        existingProduct.setDescription(product.getDescription());
        existingProduct.setPrice(product.getPrice());

        return existingProduct;
    }

    @Transactional
    public void deleteProduct(String sku) {
        Product existingProduct = productRepository.findBySku(sku)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with sku: " + sku));
        productRepository.delete(existingProduct);
    }
}
