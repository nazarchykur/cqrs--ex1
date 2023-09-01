package com.example.productcommandservice.service;

import com.example.productcommandservice.dto.ProductEvent;
import com.example.productcommandservice.dto.ProductEventType;
import com.example.productcommandservice.entity.Product;
import com.example.productcommandservice.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductCommandService {

    private final ProductRepository productRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public Product createProduct(Product product) {
        Product savedProduct = productRepository.save(product);
        kafkaTemplate.send("product-event-topic", new ProductEvent(ProductEventType.CREATED, savedProduct));
        return savedProduct;
    }

    @Transactional
    public Product updateProduct(String sku, Product product) {
        Product existingProduct = productRepository.findBySku(sku)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with sku: " + sku));

        existingProduct.setName(product.getName());
        existingProduct.setDescription(product.getDescription());
        existingProduct.setPrice(product.getPrice());
        Product updatedProduct = productRepository.save(existingProduct);
        kafkaTemplate.send("product-event-topic", new ProductEvent(ProductEventType.UPDATED, updatedProduct));

        return existingProduct;
    }

    @Transactional
    public void deleteProduct(String sku) {
        Product existingProduct = productRepository.findBySku(sku)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with sku: " + sku));
        productRepository.delete(existingProduct);
        kafkaTemplate.send("product-event-topic", new ProductEvent(ProductEventType.DELETED, existingProduct));
    }
}
