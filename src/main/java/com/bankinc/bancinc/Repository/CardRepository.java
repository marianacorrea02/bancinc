package com.bankinc.bancinc.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bankinc.bancinc.Model.Card;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {

}
