package com.user.main.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.user.main.entity.UserAccountEntity;



@Repository
public interface UserAccountsRepository extends JpaRepository<UserAccountEntity, Integer>{

	public UserAccountEntity findByEmailAndPazzword(String email, String pazzword);
}