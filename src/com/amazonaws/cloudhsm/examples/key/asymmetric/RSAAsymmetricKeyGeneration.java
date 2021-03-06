package com.amazonaws.cloudhsm.examples.key.asymmetric;

import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import com.amazonaws.cloudhsm.examples.operations.LoginLogoutExample;
import com.cavium.cfm2.CFM2Exception;
import com.cavium.cfm2.Util;
import com.cavium.key.CaviumKey;
import com.cavium.key.CaviumRSAPrivateKey;
import com.cavium.key.CaviumRSAPublicKey;
import com.cavium.key.parameter.CaviumRSAKeyGenParameterSpec;

public class RSAAsymmetricKeyGeneration {

	public static void main(String[] z) {
		System.out.println("I Rule!");
		LoginLogoutExample.loginWithExplicitCredentials();
		new RSAAsymmetricKeyGeneration().generateRSAKeyPair(2048, new BigInteger("65537"), true);

		new RSAAsymmetricKeyGeneration().generateRSAKeyPair(2048, new BigInteger("65537"), "publicKeyLabel-1" , "privateKeyLabel-1" , false, true);
		LoginLogoutExample.logout();
	}

	public KeyPair generateRSAKeyPair(int keySize,  BigInteger exponent, boolean isPersistant) {
		KeyPairGenerator keyPairGen;
		try {
			keyPairGen = KeyPairGenerator.getInstance("RSA", "Cavium");
			keyPairGen.initialize(new CaviumRSAKeyGenParameterSpec(keySize, exponent));
			KeyPair kp = keyPairGen.generateKeyPair();

			if (kp == null) {
				System.out.println("Failed to generate keypair");
			}
			RSAPrivateKey privKey = (RSAPrivateKey) kp.getPrivate();
			RSAPublicKey pubKey = (RSAPublicKey) kp.getPublic();
			System.out.println("Generated RSA Key Pair!");
			if (privKey instanceof CaviumRSAPrivateKey) {
				CaviumRSAPrivateKey cavRSAPrivateKey = (CaviumRSAPrivateKey) privKey;
				//Save Private Key Handle. You'll need this to perform encrypt/decrypt operation in future.
				System.out.println("Private Key Handle = " + cavRSAPrivateKey.getHandle());
				//Get Private Key Label. This Label is generated by SDK for this key
				System.out.println("Private Key Label = " + cavRSAPrivateKey.getLabel());
				//Get Extractable property of this Private key
				System.out.println("Is Private Key Extractalbe = " + cavRSAPrivateKey.isExtractable());
				//Get Persistent property of this Private key
				System.out.println("Is Private Key Persistent = " + cavRSAPrivateKey.isPersistent());
				//By default Keys are not persistent. Set them Persistent here
				if(isPersistant) {
					System.out.println("Setting Private Key as Persistent:");
					makeKeyPersistant(cavRSAPrivateKey);
					System.out.println("Added RSA Private Key to HSM");
				}
				System.out.println("Is Private Key Persistent = " + cavRSAPrivateKey.isPersistent());
				
				//Verify Key type and Size
				System.out.println("Key Algo : " + cavRSAPrivateKey.getAlgorithm());
				System.out.println("Key Size : " + cavRSAPrivateKey.getSize());
			}
			
			if(pubKey instanceof CaviumRSAPublicKey) {
				CaviumRSAPublicKey cavRSAPublicKey = (CaviumRSAPublicKey) pubKey;
				//Save Public Key Handle. You'll need this to perform encrypt/decrypt operation in future.
				System.out.println("Public Key Handle = " + cavRSAPublicKey.getHandle());
				//Get Public Key Label. This Label is generated by SDK for this key
				System.out.println("Public Key Label = " + cavRSAPublicKey.getLabel());
				//Get Extractable property of this Public key
				System.out.println("Is Public Key Extractable = " +cavRSAPublicKey.isExtractable());
				//Get Persistent property of this Public key
				System.out.println("Is Public Key Persistent = " + cavRSAPublicKey.isPersistent());
				//By default Keys are not persistent. Set them Persistent here
				if(isPersistant) {
					System.out.println("Setting Public Key as Persistent:");
					makeKeyPersistant(cavRSAPublicKey);
					System.out.println("Added RSA Public Key to HSM");
				}
				System.out.println("Is Public Key Persistent = " + cavRSAPublicKey.isPersistent());
				
				//Verify Key type and Size
				System.out.println("Public Key Algo : " + cavRSAPublicKey.getAlgorithm());
				System.out.println("Public Key Size : " + cavRSAPublicKey.getSize());
			}
			return kp;
		} catch (NoSuchAlgorithmException | NoSuchProviderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidAlgorithmParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	//If invoking this method, you can specify public key label, private key label, if private key can be extracted and if key pair is Persistent
	public KeyPair generateRSAKeyPair(int keySize, BigInteger exponent, String publicKeyLabel, String privateKeyLabel, boolean isExtractable, boolean isPersistent) {
		KeyPairGenerator keyPairGen;
		try {
			keyPairGen = KeyPairGenerator.getInstance("RSA", "Cavium");
			CaviumRSAKeyGenParameterSpec rsaKeyGenSpec= new CaviumRSAKeyGenParameterSpec(keySize,exponent, publicKeyLabel, privateKeyLabel, isExtractable, isPersistent);
			keyPairGen.initialize(rsaKeyGenSpec);
			KeyPair kp = keyPairGen.generateKeyPair();

			if (kp == null) {
				System.out.println("Failed to generate keypair");
			}
			RSAPrivateKey privKey = (RSAPrivateKey) kp.getPrivate();
			RSAPublicKey pubKey = (RSAPublicKey) kp.getPublic();
			System.out.println("Generated RSA Key Pair!");
			if (privKey instanceof CaviumRSAPrivateKey) {
				CaviumRSAPrivateKey cavRSAPrivateKey = (CaviumRSAPrivateKey) privKey;
				//Save Private Key Handle. You'll need this to perform encrypt/decrypt operation in future.
				System.out.println("Private Key Handle = " + cavRSAPrivateKey.getHandle());
				//Get Private Key Label. 
				System.out.println("Private Key Label = " + cavRSAPrivateKey.getLabel());
				//Get Extractable property of this Private key
				System.out.println("Is Private Key Extractalbe = " + cavRSAPrivateKey.isExtractable());
				//Get Persistent property of this Private key
				System.out.println("Is Private Key Persistent = " + cavRSAPrivateKey.isPersistent());

				//Verify Key type and Size
				System.out.println("Private Key Algo : " + cavRSAPrivateKey.getAlgorithm());
				System.out.println("Private Key Size : " + cavRSAPrivateKey.getSize());
			}

			if(pubKey instanceof CaviumRSAPublicKey) {
				CaviumRSAPublicKey cavRSAPublicKey = (CaviumRSAPublicKey) pubKey;
				//Save Public Key Handle. You'll need this to perform encrypt/decrypt operation in future.
				System.out.println("Public Key Handle = " + cavRSAPublicKey.getHandle());
				//Get Public Key Label. 
				System.out.println("Public Key Label = " + cavRSAPublicKey.getLabel());
				//Get Extractable property of this Public key
				System.out.println("Is Public Key Extractalbe = " +cavRSAPublicKey.isExtractable());
				//Get Persistent property of this Public key
				System.out.println("Is Public Key Persistent = " + cavRSAPublicKey.isPersistent());
				
				//Verify Key type and Size
				System.out.println("Public Key Algo : " + cavRSAPublicKey.getAlgorithm());
				System.out.println("Public Key Size : " + cavRSAPublicKey.getSize());
			}
			return kp;
		} catch (NoSuchAlgorithmException | NoSuchProviderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidAlgorithmParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}	

	protected void makeKeyPersistant(CaviumKey key) {
		CaviumKey rsaKey = (CaviumKey) key;
		try {
			Util.persistKey(rsaKey);
		} catch (CFM2Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}