/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import utils.ScriptBuffer.BufferType;

/**
 *
 * @author Hank Liu <hankliu@coolbitx.com>
 */
public class ScriptAssembler {
    
    public static final String binaryCharset = "binaryCharset";
    public static final String hexadecimalCharset = "hexadecimalCharset";
    public static final String bcdCharset = "bcdCharset";
    public static final String decimalCharset = "decimalCharset";
    public static final String binary32Charset = "binary32Charset";
    public static final String base32BitcoinCashCharset = "base32BitcoinCashCharset";
    public static final String base58Charset = "base58Charset";
    public static final String extendedCharset = "extentetCharset";

    public static final int leftJustify = 0x01;
    public static final int littleEndian = 0x02;
    public static final int zeroInherit = 0x04;
    public static final int bitLeftJustify8to5 = 0x08;
    public static final int inLittleEndian = 0x10;

    public static final int SHA1 = 0x01;
    public static final int SHA256 = 0x02;
    public static final int SHA512 = 0x03;
    public static final int SHA3256 = 0x04;
    public static final int SHA3512 = 0x05;
    public static final int Keccak256 = 0x06;
    public static final int Keccak512 = 0x07;
    public static final int RipeMD160 = 0x08;
    public static final int SHA256RipeMD160 = 0x09;
    public static final int DoubleSHA256 = 0x0D;
    public static final int CRC16 = 0x0A;
    public static final int Blake2b256 = 0x0E;
    public static final int Blake2b512 = 0x0F;

    private static String firstParameter, secondParameter;
    public static final String throwSEError = "FF00";

    private static int argumentOffset = 0;

    public static String compose(String command, ScriptBuffer dataBuf, BufferType destBuf, int arg0, int arg1) {
        clearParameter();
        if (dataBuf == null) {
            firstParameter += "0";
        } else {
            switch (dataBuf.bufferType) {
                case ARGUMENT:
                    firstParameter += "A";
                    break;
                case TRANSACTION:
                    firstParameter += "7";
                    break;
                case EXTENDED:
                    firstParameter += "E";
                    break;
                case FREE:
                    firstParameter += "F";
                    break;
                default:
                // Throw some exceptions here.
            }
            addIntParameter(dataBuf.offset);
            addIntParameter(dataBuf.length);
        }
        if (null == destBuf) {
            firstParameter += "7";  // TODO (should it be 0?)
        } else {
            switch (destBuf) {
                case TRANSACTION:
                    firstParameter += "7";
                    break;
                case EXTENDED:
                    firstParameter += "E";
                    break;
                case FREE:
                    firstParameter += "F";
                    break;
                default:
                // Throw some exceptions here.
            }
        }

        addIntParameter(arg0);
        addIntParameter(arg1);
        return command + firstParameter + secondParameter;
    }

    private static void clearParameter() {
        firstParameter = secondParameter = "";
    }

    private static void addIntParameter(int i) {
        switch (i) {
            case 0:
                firstParameter += "0";
                break;
            case 1:
                firstParameter += "1";
                break;
            case 20:
                firstParameter += "2";
                break;
            case 32:
                firstParameter += "5";
                break;
            case 64:
                firstParameter += "6";
                break;
            default:
                if (i > 600) {
                    if (i < 1500) {
                        firstParameter += "B";
                    } else {
                        firstParameter += "9";
                    }
                } else if (i < 0 || i >= 256) {
                    if (i < 0) {
                        i = 0x10000 + i;
                    }
                    firstParameter += "D";
                    secondParameter += HexUtil.toHexString(i / 256, 1);
                    secondParameter += HexUtil.toHexString(i % 256, 1);
                } else {
                    firstParameter += "C";
                    secondParameter += HexUtil.toHexString(i, 1);
                }
                break;
        }
    }

    public static String setCoinType(int coinType) {
        String hexCoinType = HexUtil.toHexString(coinType, 4);
        return compose("C7", null, null, 0, 0) + hexCoinType;
    }

    public static String copyArgument(ScriptBuffer data) {
        return copyArgument(data, BufferType.TRANSACTION);
    }

    public static String copyArgument(ScriptBuffer data, BufferType dest) {
        return compose("CA", data, dest, 0, 0);
    }

    public static String copyString(String data) {
        return copyString(data, BufferType.TRANSACTION);
    }

    public static String copyString(String data, BufferType dest) {
        return compose("CC", null, dest, data.length() / 2, 0) + data;
    }

    public static String switchString(ScriptBuffer conditionData, BufferType dest, String str) {
        String[] strList = str.split(",");
        String ret = compose("C1", conditionData, dest, strList.length, 0);

        for (int i = 0; i < strList.length; i++) {
            if (strList[i].equals("[]")) {
                ret += "00";
            } else {
                ret += HexUtil.toHexString(strList[i].length() / 2, 1);
                ret += strList[i];
            }
        }
        return ret;
    }

    public static String btcScript(ScriptBuffer scriptTypeData, int supportType, String content) {
        switch (supportType) {
            case 2:
                return switchString(scriptTypeData, BufferType.TRANSACTION, "1976A914,17A914")
                        + content
                        + switchString(scriptTypeData, BufferType.TRANSACTION, "88AC,87");
            case 3:
                return switchString(scriptTypeData, BufferType.TRANSACTION, "1976A914,17A914,160014")
                        + content
                        + switchString(scriptTypeData, BufferType.TRANSACTION, "88AC,87,[]");
            case 4:
                return switchString(scriptTypeData, BufferType.TRANSACTION, "1976A914,17A914,160014,220020")
                        + // switch redeemScript P2PKH=00,P2SH=01,P2WPKH=02,P2WSH=03
                        content
                        + switchString(scriptTypeData, BufferType.TRANSACTION, "88AC,87,[],[]") // switch redeemScript end
                        ;
            case 79:
                return switchString(scriptTypeData, BufferType.TRANSACTION, "3F76A914,3DA914")
                        + content
                        + switchString(scriptTypeData, BufferType.TRANSACTION, "88AC,87");
            default:
                return "XX";
        }
    }

    public static String rlpString(ScriptBuffer data) {
        return rlpString(data, BufferType.TRANSACTION);
    }

    public static String rlpString(ScriptBuffer data, BufferType dest) {
        return compose("C2", data, dest, 0, 0);
    }

    public static String rlpList(int preserveLength) {
        return rlpList(preserveLength, BufferType.TRANSACTION);
    }

    public static String rlpList(int preserveLength, BufferType dest) {
        return compose("C3", ScriptBuffer.getDataBufferAll(dest), dest, preserveLength, 0);
    }

    public static String checkRegularString(ScriptBuffer data) {
        return compose("29", data, null, 0, 0);
    }

    public static String copyRegularString(ScriptBuffer data) {
        return copyRegularString(data, BufferType.TRANSACTION);
    }

    public static String copyRegularString(ScriptBuffer data, BufferType dest) {
        return checkRegularString(data)
                + copyArgument(data, dest);
    }

    public static String baseConvert(ScriptBuffer data, BufferType dest, int outputLimit, String charset, int baseConvertArg) {
        if (outputLimit == 0) {
            outputLimit = 64;
        }

        String charsetIndex = "0";
        if (charset.equals(binaryCharset)) {
            charsetIndex = "F";
        } else if (charset.equals(hexadecimalCharset)) {
            charsetIndex = "E";
        } else if (charset.equals(bcdCharset)) {
            charsetIndex = "B";
        } else if (charset.equals(decimalCharset)) {
            charsetIndex = "D";
        } else if (charset.equals(binary32Charset)) {
            charsetIndex = "5";
        } else if (charset.equals(base32BitcoinCashCharset)) {
            charsetIndex = "C";
        } else if (charset.equals(base58Charset)) {
            charsetIndex = "8";
        } else if (charset.equals(extendedCharset)) {
            charsetIndex = "1";
        } else {
            return "XX";
        }
        return compose("BA", data, dest, outputLimit, HexUtil.toInt(charsetIndex)) + HexUtil.toHexString(baseConvertArg, 1);
    }

    public static String hash(ScriptBuffer data, BufferType dest, int hashType) {
        return compose("5A", data, dest, hashType, 0);
    }

    public static String derivePublicKey(ScriptBuffer pathData, BufferType dest) {
        return compose("6C", pathData, dest, 0, 0);
    }

    public static String bech32Polymod(ScriptBuffer data, BufferType dest) {
        return compose("5A", data, dest, 0xB, 0);
    }

    public static String bchPolymod(ScriptBuffer data, BufferType dest) {
        return compose("5A", data, dest, 0xC, 0);
    }

    public static String setBufferInt(ScriptBuffer data, int min, int max) {
        String setB = compose("B5", data, null, 0, 0);
        return ifRange(data, HexUtil.toHexString(min, 1), HexUtil.toHexString(max, 1), "", throwSEError) + setB;
    }

    public static String setBufferIntToDataLength(ScriptBuffer data) {
        return compose("B1", data, null, 0, 0);
    }

    public static String putBufferInt(BufferType dest) {
        return compose("B9", null, dest, 0, 0);
    }

    public static String paddingZero(BufferType dest, int base) {
        return compose("C6", null, dest, base, 0);
    }

//    public static String paddingZero(String data, String dest, int base) {
//        return setBufferIntToDataLength(data) + ScriptCommand.compose("C6", null, dest, base, 0);
//    }

    public static String skip(String skipee) {
        return compose("15", null, null, skipee.length() / 2, 0);
    }

    public static String ifEqual(ScriptBuffer argData, String expect, String trueStatement, String falseStatement) {
        if (!falseStatement.equals("")) {
            trueStatement += skip(falseStatement);
        }
        return compose("1A", argData, null, trueStatement.length() / 2, 0)
                + HexUtil.rightJustify(expect, argData.length)
                + trueStatement + falseStatement;
    }

    public static String ifRange(ScriptBuffer argData, String min, String max, String trueStatement, String falseStatement) {
        if (!falseStatement.equals("")) {
            trueStatement += skip(falseStatement);
        }
        return compose("12", argData, null, trueStatement.length() / 2, 0)
                + HexUtil.rightJustify(min, argData.length)
                + HexUtil.rightJustify(max, argData.length)
                + trueStatement + falseStatement;
    }

    public static String ifSigned(ScriptBuffer argData, ScriptBuffer signData, String trueStatement, String falseStatement) {
        if (!falseStatement.equals("")) {
            trueStatement += skip(falseStatement);
        }
        return compose("11", argData, null, trueStatement.length() / 2, signData.offset)
                + trueStatement + falseStatement;
    }

    public static String resetDest(BufferType dest) {
        return compose("25", null, dest, 0, 0);
    }

    public static String showMessage(String data) {
        return compose("DC", null, null, data.length(), 0) + HexUtil.toHexString(data);
    }

    public static String showMessage(ScriptBuffer data) {
        return compose("DE", data, null, 0, 0);
    }

    public static String showWrap(String data0, String data1) {
        /*if(isDataBuf(data)){
            clearParameter();
            addDataParameter(data);
            ret+="0";
            return "DE"+ret+secondParameter;
        } else {*/
        return compose("D2", null, null, data0.length(), data1.length()) + HexUtil.toHexString(data0) + HexUtil.toHexString(data1);
        //}
    }

    public static String showAddress(ScriptBuffer data) {
        return compose("DD", data, null, 0, 0);
    }

    public static String showAmount(ScriptBuffer data, int decimal) {
        return compose("DA", data, null, decimal, 0);
    }

    public static String showPressButton() {
        return showWrap("PRESS", "BUTToN");
    }

    public static String protobuf(ScriptBuffer data, int wireType) {
        return protobuf(data, BufferType.TRANSACTION, wireType);
    }

    public static String protobuf(ScriptBuffer data, BufferType dest, int wireType) {
        return compose("BF", data, dest, wireType, 0);
    }

    public static String arrayPointer() {
        return compose("A0", null, null, 0, 0);
    }

    public static String arrayEnd() {
        return compose("BE", null, null, 0, 0);
    }

    public static String scaleEncode(ScriptBuffer data, BufferType dest) {
        return compose("A2", data, dest, 0, 0);
    }

    public static String scaleDecode(ScriptBuffer data, BufferType dest) {
        return compose("A3", data, dest, 0, 0);
    }
}
