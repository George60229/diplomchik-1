package com.example.diplomchick.repository;

import com.example.diplomchick.model.SecretKey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SecretKeyRepository extends JpaRepository<SecretKey, Integer> {
    long deleteByUsername(String username);
    SecretKey findByUsername(String username);

}
