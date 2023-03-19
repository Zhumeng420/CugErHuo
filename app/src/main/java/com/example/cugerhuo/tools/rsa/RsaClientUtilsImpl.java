package com.example.cugerhuo.tools.rsa;

import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

public class RsaClientUtilsImpl implements RsaClientUtils {

    private String publicKey = null;
    private String privateKey = null;

    @Override
    public void generateKey() {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048, new SecureRandom());
            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            this.publicKey =  Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded());
            this.privateKey =  Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getPublicKey() {
        return this.publicKey;
    }

    @Override
    public String getPrivateKey() {
        return this.privateKey;
    }

    @Override
    public String decrypt(byte[] cipherText, String privateKey) {
        String plainText = null;
        try {
            PrivateKey rsaPrivateKey = KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKey)));
            byte[] bytes =  Base64.getDecoder().decode(privateKey);
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, rsaPrivateKey);
            plainText = new String(cipher.doFinal(cipherText), StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return plainText;
    }
}
