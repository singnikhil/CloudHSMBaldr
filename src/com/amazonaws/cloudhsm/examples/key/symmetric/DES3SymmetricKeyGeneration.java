package com.amazonaws.cloudhsm.examples.key.symmetric;

import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import com.amazonaws.cloudhsm.examples.operations.LoginLogoutExample;
import com.cavium.cfm2.CFM2Exception;
import com.cavium.cfm2.Util;
import com.cavium.key.CaviumDES3Key;
import com.cavium.key.parameter.CaviumDESKeyGenParameterSpec;

public class DES3SymmetricKeyGeneration {

	//Keysize can be either 168 or 192 bits.
	public static void main(String[] z) {
		LoginLogoutExample.loginWithExplicitCredentials();
		new DES3SymmetricKeyGeneration().generate3DesKey(168, true);
		new DES3SymmetricKeyGeneration().generate3DesKey(168, "DESKey-1", false, true);
		LoginLogoutExample.logout();
	}

	public Key generate3DesKey(int keySize, boolean isPersistent) {
		KeyGenerator keyGen;
		try {
			keyGen = KeyGenerator.getInstance("DESede","Cavium");
			keyGen.init(keySize); 
			SecretKey des3Key = keyGen.generateKey();
			System.out.println("Key Generated!");
			if(des3Key instanceof CaviumDES3Key) {
				System.out.println("Key is of type CaviumDES3Key");
				CaviumDES3Key ck = (CaviumDES3Key) des3Key;
				//Save Key Handle. You'll need this to perform encrypt/decrypt operation in future.
				System.out.println("Key Handle = " + ck.getHandle());
				//Get Key Label. This Label is generated by SDK for this key
				System.out.println("Key Label = " + ck.getLabel());
				//Get Extractable property of this key
				System.out.println("Is Key Extractable? : " + ck.isExtractable());
				//By default Keys are not persistent. Set them Persistent here
				System.out.println("Is Key Persistent? : " + ck.isPersistent());
				
				if(isPersistent){
					System.out.println("Setting Key as Persistent:");
					makeKeyPersistant(ck);
					System.out.println("Key is Persistent!");
				}
				System.out.println("Is Key Persistant? : " + ck.isPersistent());
				
				//Verify Key type and Size
				System.out.println("Key Algo : " + ck.getAlgorithm());
				System.out.println("Key Size : " + ck.getSize());
			}
			return des3Key;
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
	public Key generate3DesKey(int keySize, String keyLabel, boolean isExtractable, boolean isPersistent) {
		KeyGenerator keyGen;
		try {
			keyGen = KeyGenerator.getInstance("DESede","Cavium");
			CaviumDESKeyGenParameterSpec desKeyGenSpec = new CaviumDESKeyGenParameterSpec(keySize, keyLabel, isExtractable, isPersistent); 
			keyGen.init(desKeyGenSpec); 
			SecretKey des3Key = keyGen.generateKey();
			System.out.println("Key Generated!");
			if(des3Key instanceof CaviumDES3Key) {
				System.out.println("Key is of type CaviumDES3Key");
				CaviumDES3Key ck = (CaviumDES3Key) des3Key;
				//Save Key Handle. You'll need this to perform encrypt/decrypt operation in future.
				System.out.println("Key Handle = " + ck.getHandle());
				//Get Key Label. 
				System.out.println("Key Label = " + ck.getLabel());
				//Get Extractable property of this key
				System.out.println("Is Key Extractable? : " + ck.isExtractable());
				//By default Keys are not persistent. Set them Persistent here
				System.out.println("Is Key Persistent? : " + ck.isPersistent());
				
				//Verify Key type and Size
				System.out.println("Key Algo : " + ck.getAlgorithm());
				System.out.println("Key Size : " + ck.getSize());
			}
			return des3Key;
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
		CaviumDES3Key caviumDES3Key = (CaviumDES3Key) key;
		try {
			Util.persistKey(caviumDES3Key);
			System.out.println("Added Key to HSM");
		} catch (CFM2Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}