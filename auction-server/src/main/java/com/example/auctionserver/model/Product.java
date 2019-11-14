package com.example.auctionserver.model;


import lombok.Data;

import java.util.List;

@Data
public class Product {
    private int id;
    private String description;
    private String imageUrl;
    private int price;
    private String title;
    private List<String> categories;
}
