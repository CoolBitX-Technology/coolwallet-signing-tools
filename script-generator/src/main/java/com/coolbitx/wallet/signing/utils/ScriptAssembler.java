/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.coolbitx.wallet.signing.utils;

import com.coolbitx.wallet.signing.utils.ScriptData.Buffer;

/**
 * This ScriptAssembler class provide all the function to generator single
 * script, you can concatenate all the script to become a signing script for
 * CoolWallet Pro that can sign any cryptocurrency with any supported algorithm.
 *
 * @author Hank Liu (hankliu@coolbitx.com)
 */
public class ScriptAssembler {

    public static final String binaryCharset = "binaryCharset";
    public static final String hexadecimalCharset = "hexadecimalCharset";
    public static final String bcdCharset = "bcdCharset";
    public static final String decimalCharset = "decimalCharset";
    public static final String binary32Charset = "binary32Charset";
    public static final String base32BitcoinCashCharset = "base32BitcoinCashCharset";
    public static final String base58Charset = "base58Charset";
    public static final String extendedCharset = "extendedCharset";

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

    private static String compose(String command, ScriptData dataBuf, Buffer destBuf, int arg0, int arg1) {
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

    /**
     * Set coin type, should use in the begin of script.
     *
     * @param coinType
     * @return
     */
    public static String setCoinType(int coinType) {
        String hexCoinType = HexUtil.toHexString(coinType, 4);
        return compose("C7", null, null, 0, 0) + hexCoinType;
    }

    /**
     * Copy argument to transaction buffer.
     *
     * @param data
     * @return
     */
    public static String copyArgument(ScriptData data) {
        return copyArgument(data, Buffer.TRANSACTION);
    }

    /**
     * Copy argument to destination buffer.
     *
     * @param data
     * @param destinationBuf The destination buffer.
     * @return
     */
    public static String copyArgument(ScriptData data, Buffer destinationBuf) {
        return compose("CA", data, destinationBuf, 0, 0);
    }

    /**
     * Copy string to transaction buffer.
     *
     * @param data
     * @return
     */
    public static String copyString(String data) {
        return copyString(data, Buffer.TRANSACTION);
    }

    /**
     * Copy string to destination buffer.
     *
     * @param data
     * @param destinationBuf The destination buffer.
     * @return
     */
    public static String copyString(String data, Buffer destinationBuf) {
        return compose("CC", null, destinationBuf, data.length() / 2, 0) + data;
    }

    /**
     * Copy string to destination buffer with switch condition.
     *
     * @param conditionData One byte condition data, number only(
     * "00","01","02"...). For example, if the condition data is "01", will copy
     * the second(start from zero) string in the stringArray to destination
     * buffer. If conditionData is greater the the size of stringArray or less
     * zero will throw 0x6A0D error.
     * @param destinationBuf The destination buffer.
     * @param stringArray The string array concatenate with comma(ex.
     * "A,B,C,D").
     * @return
     */
    public static String switchString(ScriptData conditionData, Buffer destinationBuf, String stringArray) {
        String[] strList = stringArray.split(",");
        String ret = compose("C1", conditionData, destinationBuf, strList.length, 0);

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

    /**
     * Compose BTC-like coin redeem script.
     *
     * @param scriptTypeData
     * @param supportType
     * @param content
     * @return
     */
    @Deprecated
    public static String btcScript(ScriptData scriptTypeData, int supportType, String content) {
        switch (supportType) {
            case 2:
                return switchString(scriptTypeData, Buffer.TRANSACTION, "1976A914,17A914")
                        + content
                        + switchString(scriptTypeData, Buffer.TRANSACTION, "88AC,87");
            case 3:
                return switchString(scriptTypeData, Buffer.TRANSACTION, "1976A914,17A914,160014")
                        + content
                        + switchString(scriptTypeData, Buffer.TRANSACTION, "88AC,87,[]");
            case 4:
                return switchString(scriptTypeData, Buffer.TRANSACTION, "1976A914,17A914,160014,220020")
                        + // switch redeemScript P2PKH=00,P2SH=01,P2WPKH=02,P2WSH=03
                        content
                        + switchString(scriptTypeData, Buffer.TRANSACTION, "88AC,87,[],[]") // switch redeemScript end
                        ;
            case 79:
                return switchString(scriptTypeData, Buffer.TRANSACTION, "3F76A914,3DA914")
                        + content
                        + switchString(scriptTypeData, Buffer.TRANSACTION, "88AC,87");
            default:
                return "XX";
        }
    }

    /**
     * rlp encode string and put the output to transaction buffer.
     *
     * @param data
     * @return
     */
    public static String rlpString(ScriptData data) {
        return rlpString(data, Buffer.TRANSACTION);
    }

    /**
     * rlp encode string and put the output to destination buffer.
     *
     * @param data
     * @param destinationBuf The destination buffer.
     * @return
     */
    public static String rlpString(ScriptData data, Buffer destinationBuf) {
        return compose("C2", data, destinationBuf, 0, 0);
    }

    /**
     * rlp list encode and put the output to transaction buffer.
     *
     * @param preserveLength
     * @return
     */
    @Deprecated
    public static String rlpList(int preserveLength) {
        return rlpList(preserveLength, Buffer.TRANSACTION);
    }

    /**
     * rlp list encode and put the output to destination buffer.
     *
     * @param preserveLength
     * @param destinationBuf The destination buffer.
     * @return
     */
    @Deprecated
    public static String rlpList(int preserveLength, Buffer destinationBuf) {
        return compose("C3", ScriptData.getDataBufferAll(destinationBuf), destinationBuf, preserveLength, 0);
    }

    /**
     * Check whether the data is in range of asc-ii code encode (0x20~0x7e,
     * except 0x23(")) or not.
     *
     * @param data
     * @return
     */
    public static String checkRegularString(ScriptData data) {
        return compose("29", data, null, 0, 0);
    }

    /**
     * Copy buffer data and put the output to transaction buffer. At the same
     * time will check whether the data is in range of asc-ii code encode
     * (0x20~0x7e, except 0x23(")) or not.
     *
     * @param data
     * @return
     */
    public static String copyRegularString(ScriptData data) {
        return copyRegularString(data, Buffer.TRANSACTION);
    }

    /**
     * Copy buffer data and put the output to destination buffer. At the same
     * time will check whether the data is in range of asc-ii code encode
     * (0x20~0x7e, except 0x23(")) or not.
     *
     * @param data
     * @param destinationBuf The destination buffer.
     * @return
     */
    public static String copyRegularString(ScriptData data, Buffer destinationBuf) {
        return checkRegularString(data)
                + copyArgument(data, destinationBuf);
    }

    /**
     * Convert the data encode and put the output to destination buffer.
     *
     * @param data The data should to encode.
     * @param destinationBuf The destination of the encoded data.
     * @param outputLimit The limit length of encoded result.
     * @param charset The name of the charset requested: "binaryCharset",
     * "hexadecimalCharset", "bcdCharset", "decimalCharset", "binary32Charset",
     * "base32BitcoinCashCharset", "base58Charset", "extentetCharset".
     * @param baseConvertArg The number of the base-cenoding requested:
     * leftJustify = 0x01, littleEndian = 0x02, zeroInherit = 0x04,
     * bitLeftJustify8to5 = 0x08, inLittleEndian = 0x10.
     * @return
     */
    public static String baseConvert(ScriptData data, Buffer destinationBuf, int outputLimit, String charset, int baseConvertArg) {
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
        return compose("BA", data, destinationBuf, outputLimit, HexUtil.toInt(charsetIndex)) + HexUtil.toHexString(baseConvertArg, 1);
    }

    /**
     * Bech32 hash data and put the output to destination buffer.
     *
     * @param data The input data.
     * @param destinationBuf The destination buffer.
     * @param hashType SHA1 = 0x01, SHA256 = 0x02, SHA512 = 0x03, SHA3256 =
     * 0x04, SHA3512 = 0x05, Keccak256 = 0x06, Keccak512 = 0x07, RipeMD160 =
     * 0x08, SHA256RipeMD160 = 0x09, DoubleSHA256 = 0x0D, CRC16 = 0x0A,
     * Blake2b512 = 0x0F;
     * @return
     */
    public static String hash(ScriptData data, Buffer destinationBuf, int hashType) {
        return compose("5A", data, destinationBuf, hashType, 0);
    }

    /**
     * Derive public key by derive path and put the output to destination
     * buffer.
     *
     * @param pathData Derive path.
     * @param destinationBuf The destination buffer.
     * @return
     */
    public static String derivePublicKey(ScriptData pathData, Buffer destinationBuf) {
        return compose("6C", pathData, destinationBuf, 0, 0);
    }

    /**
     * Compute Bech32 ploymod checksum and put the output to destination buffer.
     *
     * @param data The input data.
     * @param destinationBuf The destination buffer.
     * @return
     */
    public static String bech32Polymod(ScriptData data, Buffer destinationBuf) {
        return compose("5A", data, destinationBuf, 0xB, 0);
    }

    /**
     * Compute BCH ploymod checksum and put the output to destination buffer.
     *
     * @param data The input data.
     * @param destinationBuf The destination buffer.
     * @return
     */
    public static String bchPolymod(ScriptData data, Buffer destinationBuf) {
        return compose("5A", data, destinationBuf, 0xC, 0);
    }

    /**
     * Set bufferInt from data length and check range.
     *
     * @param data
     * @param min
     * @param max
     * @return
     */
    public static String setBufferInt(ScriptData data, int min, int max) {
        String setB = compose("B5", data, null, 0, 0);
        return ifRange(data, HexUtil.toHexString(min, 1), HexUtil.toHexString(max, 1), "", throwSEError) + setB;
    }

    /**
     * Set bufferInt from buffer length.
     *
     * @param data
     * @return
     */
    public static String setBufferIntFromDataLength(ScriptData data) {
        return compose("B1", data, null, 0, 0);
    }

    /**
     * Put bufferInt into destination buffer(cast short to 2 bytes).
     *
     * @param destinationBuf
     * @return
     */
    public static String putBufferInt(Buffer destinationBuf) {
        return compose("B9", null, destinationBuf, 0, 0);
    }

    /**
     * Padding zero to the destination buffer.
     *
     * @param destinationBuf Destination buffer.
     * @param base The number of padding zero is the base minus the remainder of
     * bufferInt divided by base(base - (bufferInt % base)).
     * @return
     */
    public static String paddingZero(Buffer destinationBuf, int base) {
        return compose("C6", null, destinationBuf, base, 0);
    }

    /**
     * Skip part script
     *
     * @param script The script want to skip.
     * @return
     */
    public static String skip(String script) {
        return compose("15", null, null, script.length() / 2, 0);
    }

    /**
     * If argData equals to expect, the the card execute the trueStatement,
     * otherwise execute the falseStatement.
     *
     * @param argData Requirement.
     * @param expect Analyzing conditions.
     * @param trueStatement The script wanna execute when the status is true.
     * @param falseStatement The script wanna execute when the status is false.
     * @return
     */
    public static String ifEqual(ScriptData argData, String expect, String trueStatement, String falseStatement) {
        if (!falseStatement.equals("")) {
            trueStatement += skip(falseStatement);
        }
        return compose("1A", argData, null, trueStatement.length() / 2, 0)
                + HexUtil.rightJustify(expect, argData.length)
                + trueStatement + falseStatement;
    }

    /**
     * If the value of argData lies between min and max(min ≤ argData ≤ max)
     * then run trueStatement; if not, please run falseStatement.
     *
     * @param argData Requirement.
     * @param min The min value of the range.
     * @param max The min value of the range.
     * @param trueStatement The script wanna execute when the status is true.
     * @param falseStatement The script wanna execute when the status is false.
     * @return
     */
    public static String ifRange(ScriptData argData, String min, String max, String trueStatement, String falseStatement) {
        if (!falseStatement.equals("")) {
            trueStatement += skip(falseStatement);
        }
        return compose("12", argData, null, trueStatement.length() / 2, 0)
                + HexUtil.rightJustify(min, argData.length)
                + HexUtil.rightJustify(max, argData.length)
                + trueStatement + falseStatement;
    }

    /**
     * Use CoolBitX public key to verify that the signature is valid or not. If
     * the result is true, the the card execute the trueStatement, otherwise
     * execute the falseStatement.
     *
     * @param argData Requirement.
     * @param signData The encoded ECDSA(CBKey) signature. Signing:
     * SHA256(argData).
     * @param trueStatement The script wanna execute when the status is true.
     * @param falseStatement The script wanna execute when the status is false.
     * @return
     */
    public static String ifSigned(ScriptData argData, ScriptData signData, String trueStatement, String falseStatement) {
        if (!falseStatement.equals("")) {
            trueStatement += skip(falseStatement);
        }
        return compose("11", argData, null, trueStatement.length() / 2, signData.offset)
                + trueStatement + falseStatement;
    }

    /**
     * Reset the destination buffer.
     *
     * @param destinationBuf Target buffer.
     * @return
     */
    public static String clearBuffer(Buffer destinationBuf) {
        return compose("25", null, destinationBuf, 0, 0);
    }

    /**
     * Show word on card.
     *
     * @param data The word would show on card.
     * @return
     */
    public static String showMessage(String data) {
        return compose("DC", null, null, data.length(), 0) + HexUtil.toHexString(data);
    }

    /**
     * Show word on card from data.
     *
     * @param data The word wanted to show on card.
     * @return
     */
    public static String showMessage(ScriptData data) {
        return compose("DE", data, null, 0, 0);
    }

    /**
     * Show word on card with two line.
     *
     * @param data0 The word in line one.
     * @param data1 The word in line two.
     * @return
     */
    public static String showWrap(String data0, String data1) {
        return compose("D2", null, null, data0.length(), data1.length()) + HexUtil.toHexString(data0) + HexUtil.toHexString(data1);
        //}
    }

    /**
     * Show transaction address on card.
     *
     * @param data The transaction address data.
     * @return
     */
    public static String showAddress(ScriptData data) {
        return compose("DD", data, null, 0, 0);
    }

    /**
     * Show transaction amount on card.
     *
     * @param data The transaction amount data.
     * @param decimal The decimal in this transaction.
     * @return
     */
    public static String showAmount(ScriptData data, int decimal) {
        return compose("DA", data, null, decimal, 0);
    }

    /**
     * Show "PRESS BOTTON" on card.
     *
     * @return
     */
    public static String showPressButton() {
        return showWrap("PRESS", "BUTToN");
    }

    /**
     * Protobuf decode data put the output to transaction buffer.
     *
     * @param data The input data.
     * @param wireType
     * @return
     */
    public static String protobuf(ScriptData data, int wireType) {
        return protobuf(data, Buffer.TRANSACTION, wireType);
    }

    /**
     * Protobuf decode data put the output to destination buffer.
     *
     * @param data The input data.
     * @param destinationBuf The destination buffer.
     * @param wireType
     * @return
     */
    public static String protobuf(ScriptData data, Buffer destinationBuf, int wireType) {
        return compose("BF", data, destinationBuf, wireType, 0);
    }

    /**
     * Point the array start position.
     *
     * @return
     */
    public static String arrayPointer() {
        return compose("A0", null, null, 0, 0);
    }

    /**
     * Encode data from the last position point in arrayPointer function with
     * protobuf encoding.
     *
     * @return
     */
    public static String arrayEnd() {
        return arrayEnd(0);
    }

    /**
     * Encode data from the last position point in arrayPointer function with
     * specified encoding.
     *
     * @param type 0: protobuf, 1: rlp
     * @return
     */
    public static String arrayEnd(int type) {
        return compose("BE", null, null, type, 0);
    }

    /**
     * Scale decode data and put the output to destination buffer.
     *
     * @param data The input data.
     * @param destinationBuf The destination buffer.
     * @return
     */
    public static String scaleEncode(ScriptData data, Buffer destinationBuf) {
        return compose("A2", data, destinationBuf, 0, 0);
    }

    /**
     * Scale encode data and put the output to destination buffer.
     *
     * @param data The input data.
     * @param destinationBuf The destination buffer.
     * @return
     */
    public static String scaleDecode(ScriptData data, Buffer destinationBuf) {
        return compose("A3", data, destinationBuf, 0, 0);
    }
}
