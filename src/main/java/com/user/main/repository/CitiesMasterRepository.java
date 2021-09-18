package com.user.main.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.user.main.entity.CitiesMasterEntity;

public interface CitiesMasterRepository extends JpaRepository<CitiesMasterEntity, Integer>{

	public List<CitiesMasterEntity> findByStateId(Integer stateid);
}
