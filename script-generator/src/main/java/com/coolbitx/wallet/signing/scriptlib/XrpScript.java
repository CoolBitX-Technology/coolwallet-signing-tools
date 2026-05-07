/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.coolbitx.wallet.signing.scriptlib;

import com.coolbitx.wallet.signing.utils.HexUtil;
import com.coolbitx.wallet.signing.utils.ScriptArgumentComposer;
import com.coolbitx.wallet.signing.utils.ScriptAssembler;
import com.coolbitx.wallet.signing.utils.ScriptAssembler.HashType;
import com.coolbitx.wallet.signing.utils.ScriptAssembler.SignType;
import com.coolbitx.wallet.signing.utils.ScriptData;
import com.coolbitx.wallet.signing.utils.ScriptData.Buffer;
import com.coolbitx.wallet.signing.utils.ScriptRlpArray;
import com.coolbitx.wallet.signing.utils.ScriptRlpData;
import com.google.common.base.Strings;

public class XrpScript {

    public static void main(String[] args) {
        listAll();
    }

    public static void listAll() {
        System.out.println("XRP script: \n" + getXRPScript() + "\n");
        System.out.println("XRP new script: \n" + getXRPNewScript() + "\n");
        System.out.println("XRP sign message script: \n" + getXRPMessageScript() + "\n");
        System.out.println("XRP IOU RLUSD trust set script: \n" + getXRPRLUSDTrustSetScript() + "\n");
        System.out.println("XRP IOU trust set script: \n" + getXRPTrustSetScript() + "\n");
    }

    public static String getXRPScript() {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptData argAccount = sac.getArgument(20);
        ScriptData argPublicKey = sac.getArgument(33);
        ScriptData argDest = sac.getArgument(20);
        ScriptData argPadding1 = sac.getArgument(1);
        ScriptData argAmount = sac.getArgument(7);
        ScriptData argPadding2 = sac.getArgument(1);
        ScriptData argFee = sac.getArgument(7);
        ScriptData argSequence = sac.getArgument(4);
        ScriptData argLastLedgerSequence = sac.getArgument(4);
        ScriptData argTag = sac.getArgument(4);
        ScriptData argFlags = sac.getArgument(4);

        String script = new ScriptAssembler().setCoinType(0x90)
            .copyString("5354580012000022")
            .copyArgument(argFlags)
            .copyString("24")
            .copyArgument(argSequence)
            .copyString("2E")
            .copyArgument(argTag)
            .copyString("201B")
            .copyArgument(argLastLedgerSequence)
            .copyString("6140")
            .copyArgument(argAmount)
            .copyString("6840")
            .copyArgument(argFee)
            .copyString("7321")
            .copyArgument(argPublicKey)
            .copyString("8114")
            .copyArgument(argAccount)
            .copyString("8314")
            .copyArgument(argDest)
            .showMessage("XRP")
            .copyString("00", Buffer.CACHE2)
            .copyArgument(argDest, Buffer.CACHE2)
            .hash(ScriptData.getDataBufferAll(Buffer.CACHE2), Buffer.CACHE2, HashType.DoubleSHA256)
            .copyString(HexUtil.toHexString("rpshnaf39wBUDNEGHJKLM4PQRST7VWXYZ2bcdeCg65jkm8oFqi1tuvAxyz"),
                Buffer.CACHE1)
            .baseConvert(ScriptData.getBuffer(Buffer.CACHE2, 0, 25), Buffer.CACHE2, 45, ScriptAssembler.cache1Charset,
                ScriptAssembler.zeroInherit)
            .showAddress(ScriptData.getDataBufferAll(Buffer.CACHE2, 53))
            .showAmount(argAmount, 6)
            .showPressButton()
            .setHeader(HashType.SHA512, SignType.ECDSA)
            .getScript();
        return script;
    }

    public static String XRPScriptSignature = Strings.padStart(
        "304402206B2A707864EB98033BF83A80E8FDD7FCF903CC059ABC0E4FBB317040B6E9AD1D02203DCD2BDC4480B88DB0D9DC74948BAF6BD62203E90AE39990978999ABEAEABA63",
        144, '0');

    public static String getXRPNewScript() {
        ScriptRlpArray array = new ScriptRlpArray();
        ScriptRlpData argFlags = array.getRlpItemArgument(); // 4 bytes or null
        ScriptRlpData argSequence = array.getRlpItemArgument(); // 4 bytes
        ScriptRlpData argDestinationTag = array.getRlpItemArgument(); // 4 bytes or null
        ScriptRlpData argLastLedgerSequence = array.getRlpItemArgument(); // 4 bytes
        ScriptRlpData argAmount = array.getRlpItemArgument(); // 7 bytes
        ScriptRlpData argFee = array.getRlpItemArgument(); // 7 bytes
        ScriptRlpData argPublicKey = array.getRlpItemArgument(); // 33 bytes
        ScriptRlpData argAccount = array.getRlpItemArgument(); // 20 bytes
        ScriptRlpData argDest = array.getRlpItemArgument(); // 20 bytes
        ScriptRlpArray argMemos = array.getRlpArrayArgument(); // variable array of bytes or null
        ScriptRlpData argMemoType = argMemos.getRlpItemArgument(); // variable byte or null
        ScriptRlpData argMemoData = argMemos.getRlpItemArgument(); // variable bytes or null
        ScriptRlpData argMemoFormat = argMemos.getRlpItemArgument(); // variable byte or null

        String script = new ScriptAssembler().setCoinType(0x90)
            .copyString("53545800")
            .copyString("12") // TransactionType
            .copyString("0000")
            .isEmpty(argFlags, "", new ScriptAssembler().copyString("22").copyArgument(argFlags).getScript()) // Flags
            .copyString("24") // Sequence
            .copyArgument(argSequence)
            .isEmpty(argDestinationTag, "",
                new ScriptAssembler().copyString("2E").copyArgument(argDestinationTag).getScript()) // DestinationTag
            .copyString("201B") // LastLedgerSequence, although optional, is strongly recommended
            .copyArgument(argLastLedgerSequence)
            .copyString("6140") // Amount
            .copyArgument(argAmount)
            .copyString("6840") // Fee
            .copyArgument(argFee)
            .copyString("7321") // SigningPubKey
            .copyArgument(argPublicKey)
            .copyString("8114") // Account
            .copyArgument(argAccount)
            .copyString("8314") // Destination
            .copyArgument(argDest)
            .isEmpty(argMemos, "", new ScriptAssembler().copyString("F9")
                .copyString("EA")
                .isEmpty(argMemoType, "", new ScriptAssembler().copyString("7C").copyArgument(argMemoType).getScript())
                .isEmpty(argMemoData, "", new ScriptAssembler().copyString("7D").copyArgument(argMemoData).getScript())
                .isEmpty(argMemoFormat, "",
                    new ScriptAssembler().copyString("7E").copyArgument(argMemoFormat).getScript())
                .copyString("E1")
                .copyString("F1")
                .getScript())
            .showMessage("XRP")
            .copyString("00", Buffer.CACHE2)
            .copyArgument(argDest, Buffer.CACHE2)
            .hash(ScriptData.getDataBufferAll(Buffer.CACHE2), Buffer.CACHE2, HashType.DoubleSHA256)
            .copyString(HexUtil.toHexString("rpshnaf39wBUDNEGHJKLM4PQRST7VWXYZ2bcdeCg65jkm8oFqi1tuvAxyz"),
                Buffer.CACHE1)
            .baseConvert(ScriptData.getBuffer(Buffer.CACHE2, 0, 25), Buffer.CACHE2, 45, ScriptAssembler.cache1Charset,
                ScriptAssembler.zeroInherit)
            .showAddress(ScriptData.getDataBufferAll(Buffer.CACHE2, 53))
            .showAmount(argAmount, 6)
            .showPressButton()
            .setHeader(HashType.SHA512, SignType.ECDSA)
            .getScript();
        return script;
    }

    public static String XRPNewScriptSignature = Strings.padStart(
        "3044022011a087b28a0011597df3e6b495d7601a972022ad7233fe3e1e98d10b00d0b3f9022020e3b9b6c2f2b44decc1b33388dc44b5dedf095a73e43914c4a1959f5a055ecb",
        144, '0');

    public static String getXRPMessageScript() {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptData argMessage = sac.getArgumentAll();

        String script = new ScriptAssembler().setCoinType(0x90)
            .copyString("16585250205369676E6564204D6573736167653A0A") // \x16XRP Signed Message:\n
            .copyArgument(argMessage)
            .showMessage("XRP")
            .showWrap("MESSAGE", "")
            .showPressButton()
            .setHeader(HashType.SHA512, SignType.ECDSA)
            .getScript();
        return script;
    }

    public static String XRPMessageScriptSignature = Strings.padStart(
        "3045022100c506a4230844357bfb3ca1fe11c811302da05a7dc56de044dd149ce4f5471aa90220670efa881aaf65da22517348ca4ce4f1f5cbf3b793da377a050293b6ec13d1d4",
        144, '0');

    public static String getXRPRLUSDTrustSetScript() {
        ScriptRlpArray array = new ScriptRlpArray();
        ScriptRlpData argFlags = array.getRlpItemArgument(); // 4 bytes or null
        ScriptRlpData argSequence = array.getRlpItemArgument(); // 4 bytes
        ScriptRlpData argDestinationTag = array.getRlpItemArgument(); // 4 bytes or null
        ScriptRlpData argLastLedgerSequence = array.getRlpItemArgument(); // 4 bytes
        ScriptRlpData argLimitAmount = array.getRlpItemArgument(); // 8 bytes
        ScriptRlpData argFee = array.getRlpItemArgument(); // 7 bytes
        ScriptRlpData argPublicKey = array.getRlpItemArgument(); // 33 bytes
        ScriptRlpData argAccount = array.getRlpItemArgument(); // 20 bytes

        String script = new ScriptAssembler().setCoinType(0x90)
            .copyString("53545800")
            .copyString("12") // TransactionType
            .copyString("0014")
            .isEmpty(argFlags, "", new ScriptAssembler().copyString("22").copyArgument(argFlags).getScript()) // Flags
            .copyString("24") // Sequence
            .copyArgument(argSequence)
            .isEmpty(argDestinationTag, "", // DestinationTag
                new ScriptAssembler().copyString("2E").copyArgument(argDestinationTag).getScript())
            .copyString("201B") // LastLedgerSequence, although optional, is strongly recommended
            .copyArgument(argLastLedgerSequence)
            .copyString("63")
            .copyArgument(argLimitAmount)
            .copyString("524C555344000000000000000000000000000000")
            .copyString("E5E961C6A025C9404AA7B662DD1DF975BE75D13E")
            .copyString("6840") // Fee
            .copyArgument(argFee)
            .copyString("7321") // SigningPubKey
            .copyArgument(argPublicKey)
            .copyString("8114") // Account
            .copyArgument(argAccount)
            .showMessage("XRP")
            .showMessage("TRUST")
            .showMessage("RLUSD")
            .copyString("724D78434B62454477717237365175686553554D64454766344239784A386D354465", Buffer.CACHE1)
            .showAddress(ScriptData.getDataBufferAll(Buffer.CACHE1))
            .showPressButton()
            .setHeader(HashType.SHA512, SignType.ECDSA)
            .getScript();
        return script;
    }

    public static String XRPRLUSDTrustSetScriptSignature = Strings.padStart(
        "304502200751b793863ee64da47c7352cabda8a7be3ae63d10e75cb543d16c40bce60480022100d631f5d53d233516b86dae5c4a523973894ca28655af391e1b3d03f0279b31df",
        144, '0');

    public static String getXRPTrustSetScript() {
        ScriptRlpArray array = new ScriptRlpArray();
        ScriptRlpData argFlags = array.getRlpItemArgument(); // 4 bytes or null
        ScriptRlpData argSequence = array.getRlpItemArgument(); // 4 bytes
        ScriptRlpData argDestinationTag = array.getRlpItemArgument(); // 4 bytes or null
        ScriptRlpData argLastLedgerSequence = array.getRlpItemArgument(); // 4 bytes
        ScriptRlpData argLimitAmount = array.getRlpItemArgument(); // 8 bytes
        ScriptRlpData argFee = array.getRlpItemArgument(); // 7 bytes
        ScriptRlpData argPublicKey = array.getRlpItemArgument(); // 33 bytes
        ScriptRlpData argAccount = array.getRlpItemArgument(); // 20 bytes
        // IOU Info
        ScriptRlpData argTokenInfo = array.getRlpItemArgument(); // 48 bytes[nameLength(1B)][name(padding zero)(7B)][tokenCode(20B)][issuerAccount(20B)]

        String script = new ScriptAssembler().setCoinType(0x90)
            .copyString("53545800")
            .copyString("12") // TransactionType
            .copyString("0014")
            .isEmpty(argFlags, "", new ScriptAssembler().copyString("22").copyArgument(argFlags).getScript()) // Flags
            .copyString("24") // Sequence
            .copyArgument(argSequence)
            .isEmpty(argDestinationTag, "", // DestinationTag
                new ScriptAssembler().copyString("2E").copyArgument(argDestinationTag).getScript())
            .copyString("201B") // LastLedgerSequence, although optional, is strongly recommended
            .copyArgument(argLastLedgerSequence)
            .copyArgument(argTokenInfo, Buffer.CACHE1)
            .copyString("63")
            .copyArgument(argLimitAmount)
            .copyArgument(ScriptData.getBuffer(Buffer.CACHE1, 8, 40))
            .copyString("6840") // Fee
            .copyArgument(argFee)
            .copyString("7321") // SigningPubKey
            .copyArgument(argPublicKey)
            .copyString("8114") // Account
            .copyArgument(argAccount)
            .showMessage("XRP")
            .showMessage("TRUST")
            .copyString(HexUtil.toHexString("@"), Buffer.CACHE1)
            .setBufferInt(ScriptData.getBuffer(Buffer.CACHE1, 0, 1), 1, 7)
            .copyArgument(ScriptData.getBuffer(Buffer.CACHE1, 1, ScriptData.bufInt), Buffer.CACHE1)
            .showMessage(ScriptData.getDataBufferAll(Buffer.CACHE1, 48))
            .copyString("00", Buffer.CACHE2)
            .copyArgument(ScriptData.getBuffer(Buffer.CACHE1, 28, 20), Buffer.CACHE2)
            .hash(ScriptData.getDataBufferAll(Buffer.CACHE2), Buffer.CACHE2, HashType.DoubleSHA256)
            .clearBuffer(Buffer.CACHE1)
            .copyString(HexUtil.toHexString("rpshnaf39wBUDNEGHJKLM4PQRST7VWXYZ2bcdeCg65jkm8oFqi1tuvAxyz"),
                Buffer.CACHE1)

            .baseConvert(ScriptData.getBuffer(Buffer.CACHE2, 0, 25), Buffer.CACHE2, 45, ScriptAssembler.cache1Charset,
                ScriptAssembler.zeroInherit)
            .showAddress(ScriptData.getDataBufferAll(Buffer.CACHE2, 53))
            .showPressButton()
            .setHeader(HashType.SHA512, SignType.ECDSA)
            .getScript();
        return script;
    }

    public static String XRPTrustSetScriptSignature = Strings.padStart(
        "3046022100a13acf6e4e0be56b54a11ff62988c51cec49b8fa2fcf27fd81ab705f0f26855c022100e5f04deaaa0e431447f4defe11b15f24df7a95266efb55956029b07491d2da71",
        144, '0');

    // public static String getXRPTrustSetScript() {
    //     ScriptArgumentComposer sac = new ScriptArgumentComposer();
    //     ScriptData argSequence = sac.getArgument(4);
    //     ScriptData argLastLedgerSequence = sac.getArgument(4);
    //     ScriptData argLimitAmount = sac.getArgument(8);
    //     ScriptData argPadding = sac.getArgument(1);
    //     ScriptData argFee = sac.getArgument(7);
    //     ScriptData argPublicKey = sac.getArgument(33);
    //     ScriptData argAccount = sac.getArgument(20);
    //     // IOU Info
    //     ScriptData argTokenInfo = sac.getArgumentUnion(0, 41);
    //     ScriptData argNameLength = sac.getArgument(1);
    //     ScriptData argName = sac.getArgumentVariableLength(20);
    //     ScriptData argIssuerAccount = sac.getArgument(20);
    //     ScriptData argTokenSign = sac.getArgument(72);

    //     String script = new ScriptAssembler().setCoinType(0x90)
    //         .copyString("53545800")
    //         .copyString("120014")
    //         .copyString("24")
    //         .copyArgument(argSequence)
    //         .copyString("201B")
    //         .copyArgument(argLastLedgerSequence)
    //         .ifSigned(argTokenInfo, argTokenSign, "", ScriptAssembler.throwSEError)
    //         .copyString("63")// [value][currency][issuer AccountID]
    //         .copyArgument(argLimitAmount)
    //         .setBufferIntFromDataLength(argIssuerAccount)
    //         .copyArgument(argName)
    //         .copyArgument(argIssuerAccount)
    //         .copyString("6840")
    //         .copyArgument(argFee)
    //         .copyString("7321")
    //         .copyArgument(argPublicKey)
    //         .copyString("8114")
    //         .copyArgument(argAccount)
    //         .showMessage("XRP")
    //         .showMessage("TRUST")
    //         .setBufferInt(argNameLength, 1, 7)
    //         .copyArgument(argName, Buffer.CACHE1)
    //         .showMessage(ScriptData.getDataBufferAll(Buffer.CACHE1))
    //         .clearBuffer(Buffer.CACHE1)
    //         .copyString("00", Buffer.CACHE2)
    //         .copyArgument(argIssuerAccount, Buffer.CACHE2)
    //         .hash(ScriptData.getDataBufferAll(Buffer.CACHE2), Buffer.CACHE2, HashType.DoubleSHA256)
    //         .copyString(HexUtil.toHexString("rpshnaf39wBUDNEGHJKLM4PQRST7VWXYZ2bcdeCg65jkm8oFqi1tuvAxyz"),
    //             Buffer.CACHE1)
    //         .baseConvert(ScriptData.getBuffer(Buffer.CACHE2, 0, 25), Buffer.CACHE2, 45, ScriptAssembler.cache1Charset,
    //             ScriptAssembler.zeroInherit)
    //         .showAddress(ScriptData.getDataBufferAll(Buffer.CACHE2, 53))
    //         .showPressButton()
    //         .setHeader(HashType.SHA512, SignType.ECDSA)
    //         .getScript();
    //     return script;
    // }

}
