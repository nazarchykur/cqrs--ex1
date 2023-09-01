package com.example.productqueryservice.dto;

import com.example.productqueryservice.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductEvent {
    private ProductEventType productEventType;
    private Product product;
}
