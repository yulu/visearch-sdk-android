package com.visenze.visearch.android.util;

import com.visenze.visearch.android.VisenzeError;

import org.apache.commons.codec.binary.Hex;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * generate auth key
 * Created by visenze on 10/17/14.
 */
public class AuthGenerator {

    /**
     * Generate a map of parameters for signature security
     *
     * @param accessKey - the authorized access key
     * @param secretKey - the authorized secret key
     *
     * @return map of parameters
     */
    public static Map<String,String> getAuthParam(String accessKey, String secretKey) throws VisenzeError {
        Map<String,String> parameterMap = new HashMap<String, String>();
        String nonce = generateNonce();
        long date = System.currentTimeMillis() / 1000L;
        StringBuilder sigStr = new StringBuilder();
        sigStr.append(secretKey);
        sigStr.append(nonce);
        sigStr.append(date);

        parameterMap.put("access_key", accessKey);
        parameterMap.put("nonce", nonce);
        parameterMap.put("date", date+"");
        try {
            parameterMap.put("sig", hmacEncode(sigStr.toString(), secretKey));
        } catch (Exception e) {
            throw new VisenzeError("Exception in getAuthParam: " + e.toString(), VisenzeError.Code.ERROR);
        }
        return parameterMap;
    }

    private static String generateNonce() {
        SecureRandom sr = new SecureRandom();
        byte[] bytes = new byte[16];
        sr.nextBytes(bytes);
        return new String(Hex.encodeHex(bytes));
    }


    /**
     * encode base string with HmacSHA1
     *
     * @param baseString base string
     * @param key key
     * @return generated code
     * @throws java.security.NoSuchAlgorithmException
     * @throws java.security.InvalidKeyException
     * @throws IllegalStateException
     * @throws java.io.UnsupportedEncodingException
     */
    private static String hmacEncode(String baseString, String key) throws NoSuchAlgorithmException, InvalidKeyException, IllegalStateException,
            UnsupportedEncodingException {
        Mac mac = Mac.getInstance("HmacSHA1");
        SecretKeySpec secret = new SecretKeySpec(key.getBytes(), mac.getAlgorithm());
        mac.init(secret);
        byte[] digest = mac.doFinal(baseString.getBytes());
        return new String(Hex.encodeHex(digest));
    }
}