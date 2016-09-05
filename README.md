client

gets url qualifier from props or overridden envvar
gets server public key making a rest call
creates a keypair
creates cipher text by encrypting upload payload with own private key and then server public key
upload payload looks like
	
 -  ciphertext
	app-signature: <app sig>
	usr-domain: <which is a key against which a user/pass has been stored in the vault>
sends request to server with following params
 -  own public key
 
 
 server
 
on request, extracts ciphertext, client public key
decrypts ciphertext using own private key and then client public key into plaintext
extracts app-signature, domain pair and corresponding mapped stored plaintext
creates response payload which may look like
	app-signature: <app signature>
	usr-domain: <param passed earlier in req>
	stored-secret: <stored secret>
	
encrypts it using own private key and client public key
sends to client
