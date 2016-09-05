package com.jpmc.am.common.idvault;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;



@SpringBootApplication
public class IdVaultServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(IdVaultServiceApplication.class, args);
	}
}

@Component
class NexonHealthIndicator implements HealthIndicator {

	@Override
	public Health health() {
		return Health.status("I do <3 Nexon!!").build();
	}
}

@RestController
@RefreshScope
class IdVaultServiceRestController {

	private final SecretEntityRepo secretEntityRepository;
	private final KeyEntityRepo keyEntityRepository;
	
	@Autowired
	public IdVaultServiceRestController(SecretEntityRepo secretEntityRepository,KeyEntityRepo keyEntityRepository) {
		this.secretEntityRepository = secretEntityRepository;
		this.keyEntityRepository = keyEntityRepository;
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/secretByAppSignature")
	public Collection<SecretEntity> secretByAppSignature(@RequestParam("appSignature") String appSignature) {
		return this.secretEntityRepository.findByAppSignature(appSignature);
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/secretByAppSignatureAndUsrDomain")
	public Collection<SecretEntity> secretByAppSignatureAndUsrDomain(@RequestParam("appSignature") String appSignature, 
			@RequestParam("usrDomain") String usrDomain) {
		return this.secretEntityRepository.findByAppSignatureAndUsrDomain(appSignature, usrDomain);
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/keyEntityById")
	public Collection<KeyEntity> keyEntityById(@RequestParam("id") String id) {
		return this.keyEntityRepository.findById(Long.valueOf(id));
	}
	
	@RequestMapping(method=RequestMethod.POST, value="/getSecret")
    public void getSecret(RequestPayload requestPayload) throws ClassNotFoundException, IOException {
        Collection<KeyEntity> keyEntityList = this.keyEntityRepository.findById(Long.valueOf("1"));
        KeyEntity keyEntity = (KeyEntity) new ArrayList<>(keyEntityList).get(0);
        PrivateKey privateKey = keyEntity.getPrivateKey();
        String decryptedByPrivKey = EncryptionUtil.decrypt(requestPayload.getCipherText().getBytes(), privateKey);
        
        PublicKey publicKey = (PublicKey) new ObjectInputStream
        		(new ByteArrayInputStream(requestPayload.getPublicKey().getBytes())).readObject();
        String decryptedByPubKey = EncryptionUtil.decrypt(decryptedByPrivKey.getBytes(), publicKey);
        System.out.println(decryptedByPubKey);
    }

}

@Component
class SampleRecordsCLR implements CommandLineRunner {

	private final SecretEntityRepo secretEntityRepository;

	@Autowired
	public SampleRecordsCLR(SecretEntityRepo secretEntityRepository) {
		this.secretEntityRepository = secretEntityRepository;
	}
/*
	@Override
	public void run(String... args) throws Exception {
		Stream.of("Gosh", "Jungryeol", "Nosung", "Hyobeom",
				"Soeun", "Seunghue", "Peter", "Jooyong")
				.forEach(name -> secretEntityRepository.save(new Reservation(name)));

		secretEntityRepository.findAll().forEach(System.out::println);
	}*/

	@Override
	public void run(String... args) throws Exception {
		SecretEntity s1 = new SecretEntity();
		s1.setAppSignature("a1");
		s1.setUsrDomain("ud1");
		s1.setSecret("secret1");
		
		SecretEntity s2 = new SecretEntity();
		s2.setAppSignature("a2");
		s2.setUsrDomain("ud2");
		
		secretEntityRepository.save(s1);
		secretEntityRepository.save(s2);
		s2.setSecret("secret2");
		
		secretEntityRepository.findAll().forEach(System.out::println);
	}
	
	@Component
	class KeyGenerationCLR implements CommandLineRunner {

		public static final String ALGORITHM = "RSA";
		private final KeyEntityRepo keyEntityRepository;
		
		@Autowired
		public KeyGenerationCLR(KeyEntityRepo keyEntityRepository) {
			this.keyEntityRepository = keyEntityRepository;
		}
	
		@Override
		public void run(String... args) throws Exception {
			generateKey();
		}
		
		public void generateKey() {
		    try {
		      final KeyPairGenerator keyGen = KeyPairGenerator.getInstance(ALGORITHM);
		      keyGen.initialize(1024);
		      final KeyPair key = keyGen.generateKeyPair();
		      KeyEntity keyEntity = new KeyEntity();
		      keyEntity.setPrivateKey(key.getPrivate());
		      keyEntity.setPublicKey(key.getPublic());
		      keyEntityRepository.save(keyEntity);
		      
		      keyEntityRepository.findAll().forEach(System.out::println);
		      
		    } catch (Exception e) {
		      e.printStackTrace();
		    }

		  }
	}
}


