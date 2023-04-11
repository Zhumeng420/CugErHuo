package com.example.cugerhuo.tools.rsa;

import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import javax.crypto.Cipher;

/**
 * 利用RSA算法将明文加密
 * @author 施立豪
 */
public class RsaServerUtilsImpl implements RsaServerUtils {
    @Override
    public byte[] encrypt(String plainText, String publicKey) {
        byte[] cipherText = null;
        try {
            byte[] plainTextBytes = plainText.getBytes(StandardCharsets.UTF_8);
            PublicKey rsaPublicKey = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec( Base64.getDecoder().decode(publicKey)));
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, rsaPublicKey);
            cipherText = cipher.doFinal(plainTextBytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cipherText;
    }
}
