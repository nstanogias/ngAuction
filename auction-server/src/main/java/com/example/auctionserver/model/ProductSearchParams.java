package com.example.auctionserver.model;

import lombok.Data;

@Data
public class ProductSearchParams {
    private String title;
    private String minPrice;
    private String maxPrice;
}
