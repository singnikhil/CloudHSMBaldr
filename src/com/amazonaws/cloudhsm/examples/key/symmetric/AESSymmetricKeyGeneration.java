package com.amazonaws.cloudhsm.examples.key.symmetric;

import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import com.amazonaws.cloudhsm.examples.operations.LoginLogoutExample;
import com.cavium.cfm2.CFM2Exception;
import com.cavium.cfm2.Util;
import com.cavium.key.CaviumAESKey;
import com.cavium.key.parameter.CaviumAESKeyGenParameterSpec;
import com.cavium.provider.CaviumProvider;

public class AESSymmetricKeyGeneration {

	public static void main(String[] z) {
		LoginLogoutExample.loginWithExplicitCredentials();
			new AESSymmetricKeyGeneration().generateAESKey(256, true);
			new AESSymmetricKeyGeneration().generateAESKey(256, "AESKeyCustomLabel", false, true);
		LoginLogoutExample.logout();
	}

	public Key generateAESKey(int keySize, boolean isPersistent) {
		KeyGenerator keyGen;
		try {
			//CaviumProvider cp = new CaviumProvider();
			//Security.addProvider(new CaviumProvider());
			keyGen = KeyGenerator.getInstance("AES","Cavium");
			keyGen.init(keySize);
			SecretKey aesKey = keyGen.generateKey();
			System.out.println("Key Generated!");
			if(aesKey instanceof CaviumAESKey) {
				System.out.println("Key is of type CaviumAESKey");
				CaviumAESKey caviumAESKey = (CaviumAESKey) aesKey;
				//Save Key Handle. You'll need this to perform encrypt/decrypt operation in future.
				System.out.println("Key Handle = " + caviumAESKey.getHandle());
				//Get Key Label. This Label is generated by SDK for this key
				System.out.println("Key Label = " + caviumAESKey.getLabel());
				//Get Extractable property of this AES key
				System.out.println("Is Key Extractable? : " + caviumAESKey.isExtractable());
				//Get Persistent property of this AES key
				System.out.println("Is Key Persistent? : " + caviumAESKey.isPersistent());
				//By default Keys are not persistent. Set them Persistent here
				if(isPersistent){
					System.out.println("Setting Key as Persistent:");
					makeKeyPersistant(caviumAESKey);
					System.out.println("Key is Persistent!");
				}
				System.out.println("Is Key Persistent? : " + caviumAESKey.isPersistent());

				//Verify Key type and Size
				System.out.println("Key Algo : " + caviumAESKey.getAlgorithm());
				System.out.println("Key Size : " + caviumAESKey.getSize());
			}
			return aesKey;
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchProviderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return null;
	}

	//If invoking this method, you can specify public key label, private key label, if private key can be extracted and if key pair is Persistent
	public Key generateAESKey(int keySize,String keyLabel, boolean isExtractable, boolean isPersistent) {
		KeyGenerator keyGen;
		try {
			keyGen = KeyGenerator.getInstance("AES","Cavium");
			CaviumAESKeyGenParameterSpec aesSpec = new CaviumAESKeyGenParameterSpec(keySize, keyLabel, isExtractable, isPersistent);
			keyGen.init(aesSpec);
			SecretKey aesKey = keyGen.generateKey();
			System.out.println("Key Generated!");
			if(aesKey instanceof CaviumAESKey) {
				System.out.println("Key is of type CaviumAESKey");
				CaviumAESKey caviumAESKey = (CaviumAESKey) aesKey;
				//Save Key Handle. You'll need this to perform encrypt/decrypt operation in future.
				System.out.println("Key Handle = " + caviumAESKey.getHandle());
				//Get Key Label. 
				System.out.println("Key Label = " + caviumAESKey.getLabel());
				//Get Extractable property of this AES key
				System.out.println("Is Key Extractable? : " + caviumAESKey.isExtractable());
				//Get Persistent property of this AES key
				System.out.println("Is Key Persistent? : " + caviumAESKey.isPersistent());

				//Verify Key type and Size
				System.out.println("Key Algo : " + caviumAESKey.getAlgorithm());
				System.out.println("Key Size : " + caviumAESKey.getSize());
			}
			return aesKey;
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchProviderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return null;
	}

	public static void makeKeyPersistant(Key key) {
		CaviumAESKey caviumAESKey = (CaviumAESKey) key;
		try {
			Util.persistKey(caviumAESKey);
			System.out.println("Added Key to HSM");
		} catch (CFM2Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}