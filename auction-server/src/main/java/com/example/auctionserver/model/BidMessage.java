package com.example.auctionserver.model;

import lombok.Data;

@Data
public class BidMessage {
    private int productId;
    private int price;
}
