package com.user.main.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.user.main.entity.StateMasterEntity;

public interface StatesMasterRepository extends JpaRepository<StateMasterEntity, Integer> {

	public List<StateMasterEntity> findByCountryId(Integer countryId);

}
