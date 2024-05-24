package com.example.diplomchick.repository;

import com.example.diplomchick.model.MyUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<MyUser, Integer> {
    long deleteByEmail(String email);
    List<MyUser> findByPassword(String password);
    @Transactional
    @Modifying
    @Query("update MyUser m set m.isBlocked = ?1 where m.email = ?2")
    int updateIsBlockedByEmail(boolean isBlocked, String email);
    MyUser findByEmail(String email);
}
