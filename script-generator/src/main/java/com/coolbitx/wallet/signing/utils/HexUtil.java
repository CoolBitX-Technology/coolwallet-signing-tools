/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.math.BigInteger;
import org.spongycastle.util.encoders.Hex;

/**
 *
 * @author derek
 */
public class HexUtil {

    public static String getOriginalStringFromHexString(String str) {
        return new String(Hex.decode(str));
    }

    public static String toHexString(String stringData) {
        return toHexString(stringData.getBytes(), stringData.length());
    }

    public static String toHexStringLeftJustify(String stringData, int byteLength) {
        return leftJustify(toHexString(stringData), byteLength);
    }

    public static String toHexStringRightJustify(String stringData, int byteLength) {
        return rightJustify(toHexString(stringData), byteLength);
    }

    public static String toHexString(byte[] data) {
        return Hex.toHexString(data);
    }

    public static String toHexString(byte[] data, int byteLength) {
        if (byteLength == -1) {
            return Hex.toHexString(data);
        } else {
            return rightJustify(Hex.toHexString(data), byteLength);
        }
    }

    public static String toHexString(int i, int byteLength) {
        int hexLength = byteLength * 2;
        if (byteLength == -2) {
            hexLength = 1;
        }
        return String.format("%0" + hexLength + "X", i);
    }

    public static String toHexString(BigInteger i, int byteLength) {
        return rightJustify(i.toString(16), byteLength);
    }

    public static byte[] toByteArray(String data) {
        return Hex.decode(data);
    }

    public static int toInt(String hexData) {
        return new BigInteger(hexData, 16).intValue();
    }

    public static int toInt(BigInteger i) {
        return i.intValue();
    }

    public static String leftJustify(String str, int byteLength) {
        if (str.length() > byteLength * 2) {
            throw new java.lang.Error("str length too long");
        }
        while (str.length() < byteLength * 2) {
            str = str + "0";
        }
        return str;
    }

    public static String rightJustify(String str, int byteLength) {
        if (str.length() > byteLength * 2) {
            throw new java.lang.Error("str length too long");
        }
        while (str.length() < byteLength * 2) {
            str = "0" + str;
        }
        return str;
    }

    public static String addZeroForNum(String str, int strLength) {
        if (str.length() > strLength) {
            System.out.println("addZero:\"" + str + "\" " + strLength);
            throw new java.lang.Error("str length too long");
        }
        while (str.length() < strLength) {
            str = "0" + str;
        }
        return str;
    }
}
