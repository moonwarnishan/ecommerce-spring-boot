package com.brainstation23.ecommerce.ecommerce.model.dto.product;

import com.brainstation23.ecommerce.ecommerce.model.domain.Category;

import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;

public class ProductResponse {
    private UUID id;
    private String name;
    private BigDecimal unitPrice;
    private String description;
    private String imageUrl;
    private Set<Category> categories;
    public String categoriesStr(){
        return (this.categories == null || categories.isEmpty()) ?
                "No Category Found" : categories
                .iterator().next()
                .getCategoryName();
    }
}
