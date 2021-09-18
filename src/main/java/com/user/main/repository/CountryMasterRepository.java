package com.user.main.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.user.main.entity.CountryMasterEntity;

public interface CountryMasterRepository extends JpaRepository<CountryMasterEntity, Integer>{

}