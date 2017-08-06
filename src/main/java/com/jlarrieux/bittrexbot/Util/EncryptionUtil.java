package com.jlarrieux.bittrexbot.Util;



import org.apache.commons.codec.binary.Hex;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;



public class EncryptionUtil {

    public static String calculateHash(String secret, String url) throws NoSuchAlgorithmException, InvalidKeyException{
        byte[] hash = getHash(secret,url);
        return Hex.encodeHexString(hash);
    }

    public static String getSignDigest(String secret, String url) throws InvalidKeyException, NoSuchAlgorithmException {
        return new String(getHash(secret,url));
    }

    public static String generateNonce() throws NoSuchAlgorithmException,UnsupportedEncodingException{
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
        random.setSeed(System.currentTimeMillis());
        byte[] nonceBuytes = new byte[16];
        random.nextBytes(nonceBuytes);
        return new String(Base64.getEncoder().encode(nonceBuytes), "UTF-8");

    }

    private static byte[] getHash(String secret, String url) throws NoSuchAlgorithmException, InvalidKeyException {

        Mac shaHmac = Mac.getInstance(Constants.ENCRYPTION_ALGORITHM);
        SecretKeySpec secretKeySpec = new SecretKeySpec(secret.getBytes(), Constants.ENCRYPTION_ALGORITHM);
        shaHmac.init(secretKeySpec);
        return  shaHmac.doFinal(url.getBytes());
    }


}
