package com.jpmc.am.common.idvault;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface SecretEntityRepo extends JpaRepository<SecretEntity, Long> {
	
		Collection<SecretEntity> findByAppSignature(@Param("appSignature") String appSignature);
		Collection<SecretEntity> findByUsrDomain(@Param("usrDomain") String usrDomain);
		Collection<SecretEntity> findByAppSignatureAndUsrDomain(@Param("appSignature") String appSignature, 
				@Param("usrDomain") String usrDomain);
}
