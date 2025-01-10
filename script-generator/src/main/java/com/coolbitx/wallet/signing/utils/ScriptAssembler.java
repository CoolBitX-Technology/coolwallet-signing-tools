/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.coolbitx.wallet.signing.utils;

import com.coolbitx.wallet.signing.utils.ScriptData.Buffer;
import static com.coolbitx.wallet.signing.utils.ScriptData.Buffer.ARGUMENT;
import static com.coolbitx.wallet.signing.utils.ScriptData.Buffer.CACHE1;
import static com.coolbitx.wallet.signing.utils.ScriptData.Buffer.CACHE2;
import static com.coolbitx.wallet.signing.utils.ScriptData.Buffer.TRANSACTION;

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
    public static final String cache1Charset = "cache1Charset";

    public static final int leftJustify = 0x01;
    public static final int littleEndian = 0x02;
    public static final int zeroInherit = 0x04;
    public static final int bitLeftJustify8to5 = 0x08;
    public static final int inLittleEndian = 0x10;
    public static final String throwSEError = "FF00";

    private versionType version;
    private String script;
    private String firstParameter, secondParameter;
    private String argType = "00";

    public ScriptAssembler() {
        this.version = versionType.version00;
        this.script = "";
        argType = "00";
        clearParameter();
    }

    public String getScript() {
        return script;
    }

    public enum HashType {
        NONE("00"),
        SHA1("01"),
        SHA256("02"),
        SHA512("03"),
        SHA3256("04"),
        SHA3512("05"),
        Keccak256("06"),
        Keccak512("07"),
        RipeMD160("08"),
        SHA256RipeMD160("09"),
        CRC16("0A"),
        DoubleSHA256("0D"),
        Blake2b256("0E"),
        Blake2b512("0F"),
        SHA512256("10"),
        Blake3256("11"),
        Blake2b256Mac("13"),
        Blake2b512Mac("14");
        private final String hashLabel;

        private HashType(String hashLabel) {
            this.hashLabel = hashLabel;
        }

        @Override
        public String toString() {
            return hashLabel;
        }

        public int toInt() {
            return Integer.parseInt(hashLabel, 16);
        }

        public static HashType fromInt(int type) {
            String typeString = String.format("%02x", type);
            for (HashType hashType : HashType.values()) {
                if (hashType.hashLabel.equals(typeString)) {
                    return hashType;
                }
            }
            return NONE;
        }
    }

    public enum SignType {
        NONE("00"),
        ECDSA("01"),
        EDDSA("02"),
        BIP32EDDSA("03"),
        CURVE25519("04"),
        SCHNORR("05");
        private final String signLabel;

        SignType(String signLabel) {
            this.signLabel = signLabel;
        }

        public String toString() {
            return signLabel;
        }

        public static SignType fromInt(int type) {
            String typeString = String.format("%02x", type);
            for (SignType signType : SignType.values()) {
                if (signType.signLabel.equals(typeString)) {
                    return signType;
                }
            }
            return NONE;
        }
    }

    public static enum versionType {
        version00(0, "00"),
        version02(2, "02"),
        version03(3, "03"),
        version04(4, "04"),
        version05(5, "05"),
        version06(6, "06"),
        version07(7, "07"),
        version08(8, "08"),
        version09(9, "09");
        private final int versionNum;
        private final String versionLabel;

        private versionType(int versionNum, String versionLabel) {
            this.versionNum = versionNum;
            this.versionLabel = versionLabel;
        }

        public int getVersionNum() {
            return versionNum;
        }

        public String toString() {
            return versionLabel;
        }
    }

    public ScriptAssembler setHeader(HashType hash, SignType sign) {
        if (!argType.equals("01")) {
            script = "03" + version.versionLabel + hash + sign + script;
        } else {
            script = "05" + version.versionLabel + hash + sign + "00" + argType + script;
        }
        return this;
    }

    private String compose(String command, ScriptInterface dataBuf, Buffer destBuf, int arg0, int arg1) {
        clearParameter();
        String argVar = "";
        System.out.println("0");
        if (dataBuf == null) {
            System.out.println("1");
            firstParameter += "0";
        } else if (dataBuf instanceof ScriptData) {
            System.out.println("2");
            ScriptData dataBuf_ = (ScriptData) dataBuf;
            switch (dataBuf_.bufferType) {
                case ARGUMENT:
                    firstParameter += "A";
                    break;
                case TRANSACTION:
                    firstParameter += "7";
                    break;
                case CACHE1:
                    firstParameter += "E";
                    break;
                case CACHE2:
                    firstParameter += "F";
                    break;
                default:
                // Throw some exceptions here.
            }
            addIntParameter(dataBuf.getBufferParameter1());
            addIntParameter(dataBuf.getBufferParameter2());
        } else if (dataBuf instanceof ScriptRlpItem || dataBuf instanceof ScriptRlpArray) {
            System.out.println("3");
            firstParameter += "A"; // RLP should from ARGUMENT
            argType = "01";
            byte[] path = ((ScriptRlpData) dataBuf).getPath();
            argVar = String.format("%02d", path.length);
            System.out.println("argVar: " + argVar);
            argVar += Hex.encode(path);
            //            ScriptRlpItem dataBuf_ = (ScriptRlpItem) dataBuf;
            //            firstParameter += "B";
            //            addIntParameter(dataBuf_.getBufferParameter0());
            //            firstParameter += "A";
        } else {
            System.out.println("4");
            // Throw some exceptions here.
        }

        if (null == destBuf) {
            firstParameter += "7";  // TODO (should it be 0?)
        } else {
            switch (destBuf) {
                case TRANSACTION:
                    firstParameter += "7";
                    break;
                case CACHE1:
                    firstParameter += "E";
                    break;
                case CACHE2:
                    firstParameter += "F";
                    break;
                default:
                // Throw some exceptions here.
            }
        }

        addIntParameter(arg0);
        addIntParameter(arg1);
        return command + firstParameter + secondParameter + argVar;
    }

    private void clearParameter() {
        firstParameter = secondParameter = "";
    }

    private void addIntParameter(int i) {
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
            case ScriptData.bufInt:
                firstParameter += "B";
                break;
            case ScriptData.max:
                firstParameter += "9";
                break;
            default:
                if (i < 0 || i >= 256) {
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
    public ScriptAssembler setCoinType(int coinType) {
        String hexCoinType = HexUtil.toHexString(coinType, 4);
        script += compose("C7", null, null, 0, 0) + hexCoinType;
        return this;
    }

    /**
     * Copy argument to transaction buffer.
     *
     * @param data
     * @return
     */
    public ScriptAssembler copyArgument(ScriptObjectAbstract data) {
        return copyArgument(data, Buffer.TRANSACTION);
    }

    /**
     * Copy argument to destination buffer.
     *
     * @param data
     * @param destinationBuf The destination buffer.
     * @return
     */
    public ScriptAssembler copyArgument(ScriptObjectAbstract data, Buffer destinationBuf) {
        script += compose("CA", data, destinationBuf, 0, 0);
        return this;
    }

    /**
     * Testing function, implement later
     *
     * @param data
     * @param destinationBuf
     * @return
     */
    public ScriptAssembler forloop(ScriptArrayAbstract data, Buffer destinationBuf) {
        script += compose("AA", data, destinationBuf, 0, 0);
        return this;
    }

    /**
     * Copy string to transaction buffer.
     *
     * @param data
     * @return
     */
    public ScriptAssembler copyString(String data) {
        return copyString(data, Buffer.TRANSACTION);
    }

    /**
     * Copy string to destination buffer.
     *
     * @param data
     * @param destinationBuf The destination buffer.
     * @return
     */
    public ScriptAssembler copyString(String data, Buffer destinationBuf) {
        script += compose("CC", null, destinationBuf, data.length() / 2, 0) + data;
        return this;
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
    public ScriptAssembler switchString(ScriptObjectAbstract conditionData, Buffer destinationBuf, String stringArray) {
        String[] strList = stringArray.split(",");
        script += compose("C1", conditionData, destinationBuf, strList.length, 0);

        for (int i = 0; i < strList.length; i++) {
            if (strList[i].equals("[]")) {
                script += "00";
            } else {
                script += HexUtil.toHexString(strList[i].length() / 2, 1);
                script += strList[i];
            }
        }
        return this;
    }

    /**
     * Compose BTC-like coin redeem script.
     *
     * @param scriptTypeData
     * @param supportType
     * @param content
     * @return
     */
    //@Deprecated
    public ScriptAssembler btcScript(ScriptObjectAbstract scriptTypeData, int supportType, String content) {
        switch (supportType) {
            case 2:
                return switchString(scriptTypeData, Buffer.TRANSACTION, "1976A914,17A914")
                        .insertString(content)
                        .switchString(scriptTypeData, Buffer.TRANSACTION, "88AC,87");
            case 3:
                return switchString(scriptTypeData, Buffer.TRANSACTION, "1976A914,17A914,160014")
                        .insertString(content)
                        .switchString(scriptTypeData, Buffer.TRANSACTION, "88AC,87,[]");
            case 4:
                return switchString(scriptTypeData, Buffer.TRANSACTION, "1976A914,17A914,160014,220020")
                        // switch redeemScript P2PKH=00,P2SH=01,P2WPKH=02,P2WSH=03
                        .insertString(content)
                        .switchString(scriptTypeData, Buffer.TRANSACTION, "88AC,87,[],[]"); // switch redeemScript end
            case 79:
                return switchString(scriptTypeData, Buffer.TRANSACTION, "3F76A914,3DA914")
                        .insertString(content)
                        .switchString(scriptTypeData, Buffer.TRANSACTION, "88AC,87");
            default:
                return insertString("XX");
        }
    }

    /**
     * rlp encode string and put the output to transaction buffer.
     *
     * @param data
     * @return
     */
    public ScriptAssembler rlpString(ScriptObjectAbstract data) {
        return rlpString(data, Buffer.TRANSACTION);
    }

    /**
     * rlp encode string and put the output to destination buffer.
     *
     * @param data
     * @param destinationBuf The destination buffer.
     * @return
     */
    public ScriptAssembler rlpString(ScriptObjectAbstract data, Buffer destinationBuf) {
        script += compose("C2", data, destinationBuf, 0, 0);
        return this;
    }

    /**
     * rlp list encode and put the output to transaction buffer.
     *
     * @param preserveLength
     * @return
     */
    @Deprecated
    public ScriptAssembler rlpList(int preserveLength) {
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
    public ScriptAssembler rlpList(int preserveLength, Buffer destinationBuf) {
        script += compose("C3", ScriptData.getDataBufferAll(destinationBuf), destinationBuf, preserveLength, 0);
        return this;
    }

    /**
     * Send protobuf data place holder length to transaction buffer.
     *
     * @param data
     * @type type: 0 rlp, type: 1 protobuf
     * @return
     */
    public ScriptAssembler protobufDataPlaceholder(ScriptObjectAbstract data) {
        if (version.getVersionNum() < 5) {
            version = versionType.version05;
        }
        script += compose("C4", data, Buffer.TRANSACTION, 1, 0);
        return this;
    }

    /**
     * Send rlp data place holder length to transaction buffer.
     *
     * @param data
     * @return
     */
    public ScriptAssembler rlpDataPlaceholder(ScriptObjectAbstract data) {
        if (version.getVersionNum() < 5) {
            version = versionType.version05;
        }
        script += compose("C4", data, Buffer.TRANSACTION, 0, 0);
        return this;
    }

    /**
     * Send rlp data place holder length.
     *
     * @param data
     * @param destinationBuf
     * @return
     */
    public ScriptAssembler rlpDataPlaceholder(ScriptObjectAbstract data, Buffer destinationBuf) {
        if (version.getVersionNum() < 5) {
            version = versionType.version05;
        }
        script += compose("C4", data, destinationBuf, 0, 0);
        return this;
    }

    public ScriptAssembler dataPlaceholder(ScriptObjectAbstract data, Buffer destinationBuf) {
        if (version.getVersionNum() < 5) {
            version = versionType.version05;
        }
        script += compose("C4", data, destinationBuf, 3, 0);
        return this;
    }

    /**
     * Send utxo data place holder length.
     *
     * @param data
     * @return
     */
    public ScriptAssembler utxoDataPlaceholder(ScriptObjectAbstract data) {
        if (version.getVersionNum() < 7) {
            version = versionType.version07;
        }
        script += compose("C4", data, Buffer.TRANSACTION, 2, 0);
        return this;
    }

    /**
     * Check whether the data is in range of asc-ii code encode (0x20~0x7e,
     * except 0x23(")) or not.
     *
     * @param data
     * @return
     */
    public ScriptAssembler checkRegularString(ScriptObjectAbstract data) {
        script += compose("29", data, null, 0, 0);
        return this;
    }

    /**
     * Copy buffer data and put the output to transaction buffer. At the same
     * time will check whether the data is in range of asc-ii code encode
     * (0x20~0x7e, except 0x23(")) or not.
     *
     * @param data
     * @return
     */
    public ScriptAssembler copyRegularString(ScriptObjectAbstract data) {
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
    public ScriptAssembler copyRegularString(ScriptObjectAbstract data, Buffer destinationBuf) {
        return checkRegularString(data).copyArgument(data, destinationBuf);
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
    public ScriptAssembler baseConvert(ScriptObjectAbstract data, Buffer destinationBuf, int outputLimit, String charset, int baseConvertArg) {
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
        } else if (charset.equals(cache1Charset)) {
            charsetIndex = "1";
        } else {
            script += "XX";
            return this;
        }
        script += compose("BA", data, destinationBuf, outputLimit, HexUtil.toInt(charsetIndex)) + HexUtil.toHexString(baseConvertArg, 1);
        return this;
    }

    /**
     * Hash data and put the output to destination buffer.
     *
     * @param data The input data.
     * @param destinationBuf The destination buffer.
     * @param hashType The parameter is defined in enumeration class HashType
     * @return
     */
    public ScriptAssembler hash(ScriptObjectAbstract data, Buffer destinationBuf, HashType hashType) {
        int hashIndex = hashType.toInt();
        script += compose("5A", data, destinationBuf, hashIndex & 0xf, hashIndex >>> 4);
        return this;
    }

    /**
     * Hash data and put the output to destination buffer. NOTE: todo implement
     * with script rlp data
     *
     * @param data The input data.
     * @param destinationBuf The destination buffer.
     * @param hashType The parameter is defined in enumeration class HashType
     * @return
     */
    public ScriptAssembler newHash(ScriptObjectAbstract data, Buffer destinationBuf, HashType hashType) {
        if (version.getVersionNum() < 9) {
            version = versionType.version09;
        }
        script += compose("5B", data, destinationBuf, 0, 0);
        return this;
    }

    /**
     * Derive public key by derive path and put the output to destination
     * buffer.
     *
     * @param pathData Derive path.
     * @param destinationBuf The destination buffer.
     * @return
     */
    public ScriptAssembler derivePublicKey(ScriptObjectAbstract pathData, Buffer destinationBuf) {
        script += compose("6C", pathData, destinationBuf, 0, 0);
        return this;
    }

    /**
     * Compute Bech32 ploymod checksum and put the output to destination buffer.
     *
     * @param data The input data.
     * @param destinationBuf The destination buffer.
     * @return
     */
    public ScriptAssembler bech32Polymod(ScriptObjectAbstract data, Buffer destinationBuf) {
        if (version.getVersionNum() < 4) {
            version = versionType.version04;
        }
        script += compose("5A", data, destinationBuf, 0xB, 0);
        return this;
    }

    /**
     * Compute Bech3m2 ploymod checksum and put the output to destination
     * buffer.
     *
     * @param data The input data.
     * @param destinationBuf The destination buffer.
     * @return
     */
    public ScriptAssembler bech32mPolymod(ScriptObjectAbstract data, Buffer destinationBuf) {
        if (version.getVersionNum() < 4) {
            version = versionType.version04;
        }
        script += compose("5A", data, destinationBuf, 0x2, 0x1);
        return this;
    }

    /**
     * Compute BCH ploymod checksum and put the output to destination buffer.
     *
     * @param data The input data.
     * @param destinationBuf The destination buffer.
     * @return
     */
    public ScriptAssembler bchPolymod(ScriptObjectAbstract data, Buffer destinationBuf) {
        script += compose("5A", data, destinationBuf, 0xC, 0);
        return this;
    }

    /**
     * Set bufferInt from data length and check range.
     *
     * @param data
     * @param min
     * @param max
     * @return
     */
    public ScriptAssembler setBufferInt(ScriptObjectAbstract data, int min, int max) {
        String setB = compose("B5", data, null, 0, 0);
        script += new ScriptAssembler().ifRange(data, HexUtil.toHexString(min, 1), HexUtil.toHexString(max, 1), "", throwSEError).getScript() + setB;
        return this;
    }

    /**
     * Set bufferInt from data length without range checks.
     *
     * @param data
     * @return
     */
    public ScriptAssembler setBufferIntUnsafe(ScriptObjectAbstract data) {
        script += compose("B5", data, null, 0, 0);
        return this;
    }

    /**
     * Set bufferInt from buffer length.
     *
     * @param data
     * @return
     */
    public ScriptAssembler setBufferIntFromDataLength(ScriptObjectAbstract data) {
        script += compose("B1", data, null, 0, 0);
        return this;
    }

    /**
     * Put bufferInt into destination buffer(cast short to 2 bytes).
     *
     * @param destinationBuf
     * @return
     */
    public ScriptAssembler putBufferInt(Buffer destinationBuf) {
        script += compose("B9", null, destinationBuf, 0, 0);
        return this;
    }

    /**
     * Padding zero to the destination buffer.
     *
     * @param destinationBuf Destination buffer.
     * @param base The number of padding zero is the base minus the remainder of
     * bufferInt divided by base(base - (bufferInt % base)).
     * @return
     */
    public ScriptAssembler paddingZero(Buffer destinationBuf, int base) {
        script += compose("C6", null, destinationBuf, base, 0);
        return this;
    }

    /**
     * Skip part script
     *
     * @param script The script want to skip.
     * @return
     */
    public String skip(String script) {
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
    public ScriptAssembler ifEqual(ScriptObjectAbstract argData, String expect, String trueStatement, String falseStatement) {
        boolean restore = false;
        if (!falseStatement.equals("")) {
            trueStatement += skip(falseStatement);
        }
        int argDataLength = argData.getBufferParameter2();
        if (argData instanceof ScriptRlpItem) {
            argData.setBufferParameter2(expect.length() / 2);
            restore = true;
        } else if (argData instanceof ScriptData) {
            if (argDataLength == ScriptData.bufInt || argDataLength < 0) {
                argData.setBufferParameter2(expect.length() / 2);
                restore = true;
            }
        }
        script += compose("1A", argData, null, trueStatement.length() / 2, 0)
                + HexUtil.rightJustify(expect, Math.abs(argData.getBufferParameter2()))
                + trueStatement + falseStatement;
        if (restore) {
            argData.setBufferParameter2(argDataLength);
        }
        return this;
    }

    public ScriptAssembler isEmpty(ScriptObjectAbstract argData, String trueStatement, String falseStatement) {
        if (version.getVersionNum() < 6) {
            version = versionType.version06;
        }
        if (!falseStatement.equals("")) {
            trueStatement += skip(falseStatement);
        }
        script += compose("1C", argData, null, trueStatement.length() / 2, 0)
                + trueStatement + falseStatement;
        return this;
    }

    /**
     * If the value of argData lies between min and max(min ≤ argData ≤ max)
     * then run trueStatement; if not, please run falseStatement.
     *
     * @param argData Requirement.
     * @param min The min value of the range.
     * @param max The max value of the range.
     * @param trueStatement The script wanna execute when the status is true.
     * @param falseStatement The script wanna execute when the status is false.
     * @return
     */
    public ScriptAssembler ifRange(ScriptObjectAbstract argData, String min, String max, String trueStatement, String falseStatement) {
        boolean restore = false;
        if (!falseStatement.equals("")) {
            trueStatement += skip(falseStatement);
        }
        int argDataLength = argData.getBufferParameter2();
        if (argDataLength == ScriptData.bufInt) {
            argData.setBufferParameter2(min.length() / 2);
            restore = true;
        }
        script += compose("12", argData, null, trueStatement.length() / 2, 0)
                + HexUtil.rightJustify(min, argDataLength)
                + HexUtil.rightJustify(max, argDataLength)
                + trueStatement + falseStatement;
        if (restore) {
            argData.setBufferParameter2(ScriptData.bufInt);
        }
        return this;
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
    public ScriptAssembler ifSigned(ScriptObjectAbstract argData, ScriptData signData, String trueStatement, String falseStatement) {
        if (!falseStatement.equals("")) {
            trueStatement += skip(falseStatement);
        }
        script += compose("11", argData, null, trueStatement.length() / 2, signData.getBufferParameter1())
                + trueStatement + falseStatement;
        return this;
    }

    /**
     * Reset the destination buffer.
     *
     * @param destinationBuf Target buffer.
     * @return
     */
    public ScriptAssembler clearBuffer(Buffer destinationBuf) {
        script += compose("25", null, destinationBuf, 0, 0);
        return this;
    }

    /**
     * Show word on card.
     *
     * @param data The word would show on card.
     * @return
     */
    public ScriptAssembler showMessage(String data) {
        script += compose("DC", null, null, data.length(), 0) + HexUtil.toHexString(data);
        return this;
    }

    /**
     * Show word on card from data.
     *
     * @param data The word wanted to show on card.
     * @return
     */
    public ScriptAssembler showMessage(ScriptObjectAbstract data) {
        script += compose("DE", data, null, 0, 0);
        return this;
    }

    /**
     * Show word on card with two line.
     *
     * @param data0 The word in line one.
     * @param data1 The word in line two.
     * @return
     */
    public ScriptAssembler showWrap(String data0, String data1) {
        script += compose("D2", null, null, data0.length(), data1.length()) + HexUtil.toHexString(data0) + HexUtil.toHexString(data1);
        return this;
        //}
    }

    /**
     * Show transaction address on card.
     *
     * @param data The transaction address data.
     * @return
     */
    public ScriptAssembler showAddress(ScriptObjectAbstract data) {
        script += compose("DD", data, null, 0, 0);
        return this;
    }

    /**
     * Show transaction amount on card.
     *
     * @param data The transaction amount data.
     * @param decimal The decimal in this transaction.
     * @return
     */
    public ScriptAssembler showAmount(ScriptObjectAbstract data, int decimal) {
        script += compose("DA", data, null, decimal, 0);
        return this;
    }

    /**
     * Show "PRESS BOTTON" on card.
     *
     * @return
     */
    public ScriptAssembler showPressButton() {
        return showWrap("PRESS", "BUTToN");
    }

    /**
     * Protobuf decode data put the output to transaction buffer.
     *
     * @param data The input data.
     * @param wireType
     * @return
     */
    public ScriptAssembler protobuf(ScriptObjectAbstract data, int wireType) {
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
    public ScriptAssembler protobuf(ScriptObjectAbstract data, Buffer destinationBuf, int wireType) {
        if (version.getVersionNum() < 3) {
            version = versionType.version03;
        }
        script += compose("BF", data, destinationBuf, wireType, 0);
        return this;
    }

    /**
     * Encode uint to varint
     *
     * @return
     */
    public ScriptAssembler varint(ScriptObjectAbstract data, Buffer destinationBuf) {
        script += compose("A1", data, destinationBuf, 0, 0);
        return this;
    }

    /**
     * Point the array start position.
     *
     * @return
     */
    public ScriptAssembler arrayPointer() {
        script += compose("A0", null, null, 0, 0);
        return this;
    }

    /**
     * Encode data from the last position point in arrayPointer function with
     * protobuf encoding.
     *
     * @return
     */
    public ScriptAssembler arrayEnd() {
        return arrayEnd(TYPE_PROTOBUF);
    }

    public static final int TYPE_PROTOBUF = 0;
    public static final int TYPE_RLP = 1;
    public static final int TYPE_MESSAGE_PACK_MAP = 2;
    public static final int TYPE_MESSAGE_PACK_ARRAY = 3;

    /**
     * Encode data from the last position point in arrayPointer function with
     * specified encoding.
     *
     * @param type 0: protobuf, 1: rlp, 2: message pack map, 3: message pack
     * array
     * @return
     */
    public ScriptAssembler arrayEnd(int type) {
        if (version.getVersionNum() < 3) {
            version = versionType.version04;
        }
        script += compose("BE", null, null, type, 0);
        return this;
    }

    /**
     * Scale decode data and put the output to destination buffer.
     *
     * @param data The input data.
     * @param destinationBuf The destination buffer.
     * @return
     */
    public ScriptAssembler scaleEncode(ScriptObjectAbstract data, Buffer destinationBuf) {
        if (version.getVersionNum() < 2) {
            version = versionType.version02;
        }
        script += compose("A2", data, destinationBuf, 0, 0);
        return this;
    }

    /**
     * Scale encode data and put the output to destination buffer.
     *
     * @param data The input data.
     * @param destinationBuf The destination buffer.
     * @return
     */
    public ScriptAssembler scaleDecode(ScriptObjectAbstract data, Buffer destinationBuf) {
        script += compose("A3", data, destinationBuf, 0, 0);
        return this;
    }

    public static final byte typeInt = 0;
    public static final byte typeString = 1;
    public static final byte typeBoolean = 2;
    public static final byte typeBinary = 3;

    /**
     * Message pack encode data and put the output to destination buffer.
     *
     * @param type 0: Int, 1: String, 2: Boolean
     * @param data The input data. When type equals to Int, data is Hexadecimal.
     * Type equals to String, data is ascii code staing. Type equals to Boolean,
     * 0x00 means false, 0x01 means true.
     * @param destinationBuf The destination buffer.
     * @return
     */
    public ScriptAssembler messagePack(int type, ScriptInterface data, Buffer destinationBuf) {
        if (version.getVersionNum() < 6) {
            version = versionType.version06;
        }
        script += compose("C5", data, destinationBuf, type, 0);
        return this;
    }

    /**
     * Message pack string type encode data and put the output to destination
     * buffer.
     *
     * @param data The input data. Only support String, data is ascii code
     * staing.
     * @param destinationBuf The destination buffer.
     * @return
     */
    public ScriptAssembler messagePack(String data, Buffer destinationBuf) {
        if (version.getVersionNum() < 6) {
            version = versionType.version06;
        }
        script += compose("C8", null, destinationBuf, data.length() / 2, 0) + data;
        return this;
    }

    public enum Tag {
        CHALLENGE("00"),
        AUX("01"),
        NONCE("02"),
        TAP_TWEAK("03"),
        TAP_SIGHASH("04");

        private final String tagName;

        Tag(String tagName) {
            this.tagName = tagName;
        }

        @Override
        public String toString() {
            return tagName;
        }

        public int toInt() {
            return Integer.parseInt(tagName, 16);
        }
    }

    /**
     * Tagged hash data to destination buffer.
     *
     * @param data
     * @param destinationBuf The destination buffer.
     * @param tag
     * @return
     */
    public ScriptAssembler taggedHash(ScriptObjectAbstract data, Buffer destinationBuf, Tag tag) {
        script += compose("AE", data, destinationBuf, tag.toInt(), 0);
        return this;
    }

    public ScriptAssembler insertString(String data) {
        script += data;
        return this;
    }

    /**
     * Convert the argument from a bit array to a byte array and store it in the
     * destination buffer.
     *
     * @param data
     * @return
     */
    public ScriptAssembler bitToByte(ScriptObjectAbstract data) {
        return bitToByte(data, Buffer.TRANSACTION);
    }

    /**
     * Convert the argument from a bit array to a byte array and store it in the
     * destination buffer.
     *
     * @param data
     * @param destinationBuf The destination buffer.
     * @return
     */
    public ScriptAssembler bitToByte(ScriptObjectAbstract data, Buffer destinationBuf) {
        if (version.getVersionNum() < 8) {
            version = versionType.version08;
        }
        script += compose("BB", data, destinationBuf, 0, 0);
        return this;
    }
}
