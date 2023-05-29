package com.bankinc.bancinc.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bankinc.bancinc.Model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    
}
