package com.example.auctionserver.service;

import com.example.auctionserver.model.BidMessage;
import com.example.auctionserver.model.Product;
import com.example.auctionserver.model.ProductSearchParams;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class ProductService {
    private List<Product> products;

    @PostConstruct
    public void init() {
        ObjectMapper mapper = new ObjectMapper();
        TypeReference<List<Product>> typeReference = new TypeReference<List<Product>>() {
        };
        InputStream inputStream = TypeReference.class.getResourceAsStream("/data/products.json");
        try {
            products = mapper.readValue(inputStream, typeReference);
            System.out.println("Products Saved!");
        } catch (IOException e) {
            System.out.println("Unable to save products: " + e.getMessage());
        }
    }

    //getProducts
    public List<Product> getProducts(@Nullable ProductSearchParams productSearchParams) {
        return filter(productSearchParams);
    }

    //getProductsById
    public Product getProductById(int id) {
        return products.stream().filter(product -> product.getId() == id).findFirst().get();
    }

    //getDistinctCategories
    public List<String> getCategories() {
        return products.stream().map(product -> product.getCategories()).flatMap(List::stream).distinct().collect(Collectors.toList());
    }

    //getProductsByCategory
    public List<Product> getProductsByCategory(String category) {
        return products.stream().filter(product -> product.getCategories().contains(category)).collect(Collectors.toList());
    }

    //updateProduct
    public void updateProductBidAmount(BidMessage bidMessage) {
        Optional<Product> productToUpdate = products.stream().filter(product -> product.getId() == bidMessage.getProductId()).findFirst();
        if (productToUpdate.isPresent()) {
            productToUpdate.get().setPrice(bidMessage.getPrice());
        }
    }

    private List<Product> filter(@Nullable ProductSearchParams productSearchParams) {
        if (productSearchParams != null) {
            return products.stream()
                    .filter(product -> productSearchParams.getMaxPrice() == null || product.getPrice() < Integer.valueOf(productSearchParams.getMaxPrice()))
                    .filter(product -> productSearchParams.getMinPrice() == null || product.getPrice() > Integer.valueOf(productSearchParams.getMinPrice()))
                    .filter(product -> productSearchParams.getTitle() == null || product.getTitle().toLowerCase().contains(productSearchParams.getTitle()))
                    .collect(Collectors.toList());

        } else {
            return products;
        }
    }
}
