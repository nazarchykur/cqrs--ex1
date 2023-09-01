package com.example.productqueryservice.service;

import com.example.productqueryservice.dto.ProductEvent;
import com.example.productqueryservice.dto.ProductEventType;
import com.example.productqueryservice.entity.Product;
import com.example.productqueryservice.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
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

    @KafkaListener(topics = "product-event-topic", groupId = "product-event-group")
    public void processProductEvent(ProductEvent productEvent) {
        Product product = productEvent.getProduct();
        if (productEvent.getProductEventType().equals(ProductEventType.CREATED)) {
            productRepository.save(product);
        } else if (productEvent.getProductEventType().equals(ProductEventType.UPDATED)) {
            String sku = product.getSku();
            Product existingProduct = productRepository.findBySku(sku)
                    .orElseThrow(() -> new EntityNotFoundException("Product not found with sku: " + sku));

            existingProduct.setName(product.getName());
            existingProduct.setDescription(product.getDescription());
            existingProduct.setPrice(product.getPrice());

            productRepository.save(existingProduct);
        } else if (productEvent.getProductEventType().equals(ProductEventType.DELETED)) {
            String sku = product.getSku();
            Product existingProduct = productRepository.findBySku(sku)
                    .orElseThrow(() -> new EntityNotFoundException("Product not found with sku: " + sku));
            productRepository.delete(existingProduct);
        }
    }
}
