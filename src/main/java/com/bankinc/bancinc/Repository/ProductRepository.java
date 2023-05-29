package com.bankinc.bancinc.Repository;

import org.springframework.stereotype.Repository;

import com.bankinc.bancinc.Model.Product;

import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    
}
