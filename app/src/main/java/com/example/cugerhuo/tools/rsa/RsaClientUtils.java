package com.example.cugerhuo.tools.rsa;

/**
 * 对称加密接口类
 * @author 施立豪
 */
public interface RsaClientUtils {

    /**
     * 生成RSA算法的公钥和私钥
     */
    void generateKey();

    /**
     * 获取RSA算法公钥
     *
     * @return 公钥
     */
    String getPublicKey();

    /**
     * 获取RSA算法私钥
     *
     * @return 私钥
     */
    String getPrivateKey();

    /**
     * 利用RSA算法将密文解密
     *
     * @param cipherText 密文(byte数组)
     * @param privateKey 私钥
     * @return 明文(字符串)
     */
    String decrypt(byte[] cipherText, String privateKey);

}
