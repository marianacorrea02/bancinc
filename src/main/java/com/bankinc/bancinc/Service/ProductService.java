package com.bankinc.bancinc.Service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bankinc.bancinc.Model.Product;
import com.bankinc.bancinc.Repository.ProductRepository;

import jakarta.annotation.PostConstruct;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @PostConstruct
    public void initData() {

        if (productRepository.count() == 0) {
            if (productRepository.count() == 0) {
                List<Product> products = new ArrayList<>();
                products.add(new Product(102030L, "Debit"));
                products.add(new Product(302010L, "Credit"));
                productRepository.saveAll(products);
            }
        }
    }

}
