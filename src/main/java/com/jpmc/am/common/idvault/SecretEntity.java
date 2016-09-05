package com.jpmc.am.common.idvault;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class SecretEntity {

		@Id
		@GeneratedValue
		private Long id;  // id

		private String appSignature; 
		private String usrDomain;
		@JsonIgnore
		private String secret;

		public Long getId() {
			return id;
		}

		public String getAppSignature() {
			return appSignature;
		}

		public void setAppSignature(String appSignature) {
			this.appSignature = appSignature;
		}

		public String getUsrDomain() {
			return usrDomain;
		}

		public void setUsrDomain(String usrDomain) {
			this.usrDomain = usrDomain;
		}

		public String getSecret() {
			return secret;
		}

		public void setSecret(String secret) {
			this.secret = secret;
		}

		public void setId(Long id) {
			this.id = id;
		}

		@Override
		public String toString() {
			return "SecretEntity [id=" + id + ", appSignature=" + appSignature
					+ ", usrDomain=" + usrDomain + "]";
		}

		public SecretEntity() {
		}
		
}
