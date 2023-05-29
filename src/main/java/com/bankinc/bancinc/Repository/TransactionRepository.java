package com.bankinc.bancinc.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bankinc.bancinc.Model.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

}
