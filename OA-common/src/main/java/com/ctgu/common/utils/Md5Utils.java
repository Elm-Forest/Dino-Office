package com.ctgu.common.utils;

import cn.hutool.core.util.RandomUtil;
import com.ctgu.common.bo.RsaBO;
import org.apache.shiro.crypto.hash.Md5Hash;

import static com.ctgu.common.constants.ShiroConst.HASH_ITERATIONS;

/**
 * @author Zhang Jinming
 * @create 21/6/2022 下午9:09
 */
public class Md5Utils {
    public static RsaBO passwordMd5(String password) {
        String salt = byteArrayToHexString(RandomUtil.randomBytes(10));
        Md5Hash md5 = new Md5Hash(password,
                salt,
                HASH_ITERATIONS);
        return RsaBO.builder().password(md5.toHex()).salt(salt).build();
    }

    public static String byteArrayToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte aByte : bytes) {
            String hex = Integer.toHexString(0xFF & aByte);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }
}
