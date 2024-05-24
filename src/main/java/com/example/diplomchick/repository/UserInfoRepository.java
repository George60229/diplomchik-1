package com.example.diplomchick.repository;

import com.example.diplomchick.model.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserInfoRepository extends JpaRepository<UserInfo,Long> {
    List<UserInfo> findByCountryAndEmail(String country, String email);
    List<UserInfo> findByCountry(String country);
}
