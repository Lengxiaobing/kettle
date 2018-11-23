package org.kettle.sxdata.util.task;

import java.math.BigInteger;

/**
 * @description: kettle加密
 * @author: ZX
 * @date: 2018/11/20 14:28
 */
public class KettleEncr {
    private static final int RADIX = 16;
    private static final String SEED = "0933910847463829827159347601486730416058";

    /**
     * 解码 解码成可阅读的字符串
     *
     * @param encryptedFromKettle
     * @return
     */
    public static final String decryptPasswd(String encryptedFromKettle) {
        return decryptPassword(encryptedFromKettle.replace("Encrypted ", ""));
    }

    /**
     * 编码
     *
     * @param password
     * @return
     */
    public static final String encryptPassword(String password) {
        if (password == null || password.length() == 0) {
            return "";
        }

        BigInteger bi_passwd = new BigInteger(password.getBytes());

        BigInteger bi_r0 = new BigInteger(SEED);
        BigInteger bi_r1 = bi_r0.xor(bi_passwd);

        return bi_r1.toString(RADIX);
    }

    private static final String decryptPassword(String encrypted) {
        if (encrypted == null || encrypted.length() == 0) {
            return "";
        }

        BigInteger bi_confuse = new BigInteger(SEED);

        try {
            BigInteger bi_r1 = new BigInteger(encrypted, RADIX);
            BigInteger bi_r0 = bi_r1.xor(bi_confuse);

            return new String(bi_r0.toByteArray());
        } catch (Exception e) {
            return "";
        }
    }

}
