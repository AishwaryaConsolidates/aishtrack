package com.aishtek.aishtrack.services;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;
import java.util.Random;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import com.amazonaws.services.secretsmanager.AWSSecretsManager;
import com.amazonaws.services.secretsmanager.AWSSecretsManagerClientBuilder;
import com.amazonaws.services.secretsmanager.model.DecryptionFailureException;
import com.amazonaws.services.secretsmanager.model.GetSecretValueRequest;
import com.amazonaws.services.secretsmanager.model.GetSecretValueResult;
import com.amazonaws.services.secretsmanager.model.InternalServiceErrorException;
import com.amazonaws.services.secretsmanager.model.InvalidParameterException;
import com.amazonaws.services.secretsmanager.model.InvalidRequestException;
import com.amazonaws.services.secretsmanager.model.ResourceNotFoundException;

public class EncryptionService {

  private static final String ALGO = "AES";
  private static final String secretName = "encryptBankAccount";
  private static final String region = "ap-south-1";

  private String getSecret() {
    // Create a Secrets Manager client
    AWSSecretsManager client = AWSSecretsManagerClientBuilder.standard().withRegion(region).build();

    String secret = null, decodedBinarySecret = null;
    GetSecretValueRequest getSecretValueRequest =
        new GetSecretValueRequest().withSecretId(secretName);
    GetSecretValueResult getSecretValueResult = null;

    try {
      getSecretValueResult = client.getSecretValue(getSecretValueRequest);
    } catch (DecryptionFailureException e) {
      // Secrets Manager can't decrypt the protected secret text using the provided KMS key.
      // Deal with the exception here, and/or rethrow at your discretion.
      throw e;
    } catch (InternalServiceErrorException e) {
      // An error occurred on the server side.
      // Deal with the exception here, and/or rethrow at your discretion.
      throw e;
    } catch (InvalidParameterException e) {
      // You provided an invalid value for a parameter.
      // Deal with the exception here, and/or rethrow at your discretion.
      throw e;
    } catch (InvalidRequestException e) {
      // You provided a parameter value that is not valid for the current state of the resource.
      // Deal with the exception here, and/or rethrow at your discretion.
      throw e;
    } catch (ResourceNotFoundException e) {
      // We can't find the resource that you asked for.
      // Deal with the exception here, and/or rethrow at your discretion.
      throw e;
    }

    // Decrypts secret using the associated KMS CMK.
    // Depending on whether the secret is a string or binary, one of these fields will be populated.
    if (getSecretValueResult.getSecretString() != null) {
      secret = getSecretValueResult.getSecretString();
    } else {
      decodedBinarySecret =
          new String(Base64.getDecoder().decode(getSecretValueResult.getSecretBinary()).array());
    }

    return secret;
  }

  public String encrypt(String strToEncrypt, int seed) throws Exception {
    try {
      byte[] iv = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
      IvParameterSpec ivspec = new IvParameterSpec(iv);

      Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
      cipher.init(Cipher.ENCRYPT_MODE, getSecretKey(seed), ivspec);
      return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes("UTF-8")));
    } catch (Exception e) {
      throw e;
    }
  }

  private SecretKeySpec getSecretKey(int seed)
      throws NoSuchAlgorithmException, InvalidKeySpecException {
    SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
    KeySpec spec = new PBEKeySpec(getSecret().toCharArray(), getSalt(seed).getBytes(), 65536, 256);
    SecretKey tmp = factory.generateSecret(spec);
    SecretKeySpec secretKey = new SecretKeySpec(tmp.getEncoded(), "AES");
    return secretKey;
  }

  public String decrypt(String strToDecrypt, int seed) {
    try {
      byte[] iv = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
      IvParameterSpec ivspec = new IvParameterSpec(iv);

      Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
      cipher.init(Cipher.DECRYPT_MODE, getSecretKey(seed), ivspec);
      return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
    } catch (Exception e) {
      System.out.println("Error while decrypting: " + e.toString());
    }
    return null;
}

  private String getSalt(int seed) {
    Random random = new Random(seed);
    return "" + random.nextInt();
  }

  public String obfuscate(String aString) {
    return "*****" + aString.substring(aString.length() - 4);
  }
}
