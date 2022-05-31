package com.kkkzoz.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.util.Arrays;

@Slf4j
@Component
public class AESCryptoUtil {
    public static void main(String[] args) {
        String content = "美好的世界,美好的中国,美好的未来！！！";

        byte[] encryptResult = encrypt(content);
        byte[] decryptResult = decrypt(encryptResult);


        String encryptStr = parseByte2HexStr(encryptResult);
        printMsg("========\n加密前:%s\n加密后:%s\n解密后：%s", content, encryptStr, new String(decryptResult));

        //解密时，不能new String(encryptResult,"utf-8").getBytes("utf-8")
        decryptResult = decrypt(parseHexStr2Byte(encryptStr));
        printMsg("========\n加密前:%s\n加密后:%s\n解密后：%s", content, encryptStr,new String(decryptResult));


        //采用AES/ECB/NoPadding时，要求密钥长度16、24、32中的一个、待加密内容的byte[]长度必须是16的倍数
        encryptResult = encrypt2(content);
        decryptResult = decrypt2(encryptResult);
        encryptStr = parseByte2HexStr(encryptResult);
        printMsg("========\n加密前:%s\n加密后:%s\n解密后：%s", content, encryptStr, new String(decryptResult));

    }

    private static void printMsg(String template, Object... args) {
        System.out.println(String.format(template, args));
    }

    private static String password = "科技兴国@！##";
    private static SecretKeySpec key;

    static {
        init();
    }

    private static void init() {
        try {
            KeyGenerator kgen = KeyGenerator.getInstance("AES");
            kgen.init(256, new SecureRandom(password.getBytes()));
            SecretKey secretKey = kgen.generateKey();
            byte[] enCodeFormat = secretKey.getEncoded();
            key = new SecretKeySpec(enCodeFormat, "AES");
        } catch (Exception e) {
            log.error("初始化AES算法失败" + e.getMessage(), e);
        }
    }

    /**
     * 加密
     *
     * @param content 需要加密的内容
     * @return
     */
    public static byte[] encrypt(String content) {
        try {
            Cipher cipher = Cipher.getInstance("AES");
            byte[] byteContent = content.getBytes("utf-8");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] result = cipher.doFinal(byteContent);
            return result;
        } catch (Exception e) {
            log.error("AES加密失败" + e.getMessage(), e);
        }
        return null;
    }

    public static byte[] decrypt(byte[] content) {
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] result = cipher.doFinal(content);
            return result;
        } catch (Exception e) {
            log.error("AES解密失败" + e.getMessage(), e);
        }
        return null;
    }

    /**
     * 加密
     *
     * @param content 需要加密的内容
     * @return
     */
    public static byte[] encrypt2(String content) {
        try {
            byte[] bytePassword = password.getBytes();
            bytePassword = checkByteLength(bytePassword);
            SecretKeySpec key = new SecretKeySpec(bytePassword, "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");
            byte[] byteContent = content.getBytes("utf-8");
            byteContent = checkByteLength(byteContent);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] result = cipher.doFinal(byteContent);
            return result;
        } catch (Exception e) {
            log.error("AES加密失败" + e.getMessage(), e);
        }
        return null;
    }

    public static byte[] decrypt2(byte[] content) {
        try {
            byte[] bytePassword = password.getBytes();
            bytePassword = checkByteLength(bytePassword);
            SecretKeySpec key = new SecretKeySpec(bytePassword, "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] result = cipher.doFinal(content);
            return result;
        } catch (Exception e) {
            log.error("AES解密失败" + e.getMessage(), e);
        }
        return null;
    }

    private static byte[] checkByteLength(byte[] byteContent) {
        int length = byteContent.length;
        int remainder = length % 16;
        if(remainder == 0){
            return byteContent;
        }else{
            return Arrays.copyOf(byteContent,length+(16-remainder));
        }
    }

    /**将二进制转换成16进制
     * @param buf
     * @return
     */
    public static String parseByte2HexStr(byte buf[]) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < buf.length; i++) {
            String hex = Integer.toHexString(buf[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }

    public static byte[] parseHexStr2Byte(String hexStr) {
        if (hexStr.length() < 1) {
            return null;
        }
        byte[] result = new byte[hexStr.length()/2];
        for (int i = 0;i< hexStr.length()/2; i++) {
            int high = Integer.parseInt(hexStr.substring(i*2, i*2+1), 16);
            int low = Integer.parseInt(hexStr.substring(i*2+1, i*2+2), 16);
            result[i] = (byte) (high * 16 + low);
        }
        return result;
    }
}
