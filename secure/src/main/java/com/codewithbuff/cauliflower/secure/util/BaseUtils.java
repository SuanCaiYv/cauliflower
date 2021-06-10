package com.codewithbuff.cauliflower.secure.util;

import com.usthe.sureness.util.Md5Util;

import java.util.Random;
import java.util.UUID;

/**
 * @author 十三月之夜
 */
public class BaseUtils {

    /**
     * 生成盐值
     */
    public static String getSalt() {

        int val = new Random().nextInt() % 999999;
        val = Math.abs(val);
        String salt = String.format("%06d", val);
        salt += UUID.randomUUID().toString().replace("-", "").substring(0, 12).toUpperCase();
        return salt;
    }

    public static String MD5(String plainText, String secret) {
        return Md5Util.md5(plainText + secret);
    }

    public static String generateUsername() {
        return UUID.randomUUID().toString().toUpperCase();
    }
}
