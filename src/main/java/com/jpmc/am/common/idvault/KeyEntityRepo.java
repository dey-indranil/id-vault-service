package com.jpmc.am.common.idvault;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface KeyEntityRepo extends JpaRepository<KeyEntity, Long> {
	
		Collection<KeyEntity> findById(@Param("id") Long id);
}
