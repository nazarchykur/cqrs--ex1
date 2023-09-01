package com.example.productcommandservice.controller;

import com.example.productcommandservice.entity.Product;
import com.example.productcommandservice.service.ProductCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductCommandController {

    private final ProductCommandService productCommandService;


    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        Product savedProduct = productCommandService.createProduct(product);
        URI uri = URI.create("/api/v1/products/" + savedProduct.getId());
        return ResponseEntity.created(uri).body(savedProduct);
    }

    @PutMapping("/{sku}")
    public ResponseEntity<Product> updateProduct(@PathVariable String sku, @RequestBody Product product) {
        return ResponseEntity.ok(productCommandService.updateProduct(sku, product));
    }

    @DeleteMapping("/{sku}")
    public ResponseEntity<Void> deleteProduct(@PathVariable String sku) {
        productCommandService.deleteProduct(sku);
        return ResponseEntity.noContent().build();
    }
}
