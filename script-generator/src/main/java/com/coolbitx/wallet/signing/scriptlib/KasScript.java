/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.coolbitx.wallet.signing.scriptlib;

import com.coolbitx.wallet.signing.utils.Hex;
import com.coolbitx.wallet.signing.utils.HexUtil;
import com.coolbitx.wallet.signing.utils.ScriptArgumentComposer;
import com.coolbitx.wallet.signing.utils.ScriptAssembler;
import com.coolbitx.wallet.signing.utils.ScriptData;
import com.coolbitx.wallet.signing.utils.ScriptData.Buffer;
import com.google.common.base.Strings;

public class KasScript {

    public static void main(String[] args) {
        listAll();
    }

    public static void listAll() {
        System.out.println("Kas transfer: \n" + getTransferScript(false) + "\n");
    }

    public static String getAddressScript(ScriptData argOutputXOnlyPublicKey) {
        String hrp = "kaspa";
        String hrpExpand = "";
        for (int i = 0; i < hrp.length(); i++) {
            hrpExpand += HexUtil.toHexString(hrp.charAt(i) & 31, 1);
        }
        hrpExpand += "00";
        String bech32AddressScript = new ScriptAssembler()
                .clearBuffer(Buffer.CACHE1)
                .clearBuffer(Buffer.CACHE2)
                .copyString(hrpExpand, Buffer.CACHE2)
                // version + outputXOnlyPublicKey -> cache1
                .copyString("00", Buffer.CACHE1)
                .copyArgument(argOutputXOnlyPublicKey, Buffer.CACHE1)
                // 33 * 8 / 5 = 52.8 ≈ 53
                // convert8To5Bits(version + outputXOnlyPublicKey) -> cache2
                .baseConvert(ScriptData.getDataBufferAll(Buffer.CACHE1), Buffer.CACHE2, 53,
                        ScriptAssembler.binary32Charset, ScriptAssembler.bitLeftJustify8to5)
                .copyString("0000000000000000", Buffer.CACHE2)
                // calculate checksum -> cache1
                .bchPolymod(ScriptData.getDataBufferAll(Buffer.CACHE2), Buffer.CACHE1)
                .clearBuffer(Buffer.CACHE2)
                // (33 + 5) * 8 / 5 = 60.8 ≈ 61
                // bech32(convert8To5Bits(version + outputXOnlyPublicKey)) -> cache2
                .baseConvert(ScriptData.getBuffer(Buffer.CACHE1, 0, 33), Buffer.CACHE2, 53, ScriptAssembler.base32BitcoinCashCharset, ScriptAssembler.bitLeftJustify8to5)   
                // bech32(convert8To5Bits(checksum)) -> cache2
                .baseConvert(ScriptData.getDataBufferAll(Buffer.CACHE1, 33), Buffer.CACHE2, 8, ScriptAssembler.base32BitcoinCashCharset, ScriptAssembler.bitLeftJustify8to5)    
                .showAddress(ScriptData.getDataBufferAll(Buffer.CACHE2))
                .getScript();
        return bech32AddressScript;
    }

//     0000
//     b9040cd6c2cc517e2684744b61b9defaeb670312759d8a778007ab72c5d06e02
//     0f99135614633e507969d12522c80f967cff6ebc0436863e02ee42b2b66556fc
//     8523b0471bcbea04575ccaa635eef9f9114f2890bda54367e5ff8caa3878bf82
//     e803000000000000
//     0000
//     2200000000000000
//     200a3da6e8c7a8795440e60e4662686bc17cd965780095604c85d8d137e0f48079ac
//     01
//     22c8668c00000000
//     328000002c8001b207800000000000000000000000
//     0000000000000000
//     0000000000000000000000000000000000000000
//     0000000000000000
//     0000000000000000000000000000000000000000000000000000000000000000
//     01
    public static String getTransferScript(boolean isTestnet) {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptData argReverseVersion = sac.getArgument(2);
        ScriptData argHashPrevouts = sac.getArgument(32);
        ScriptData argHashSequences = sac.getArgument(32);
        ScriptData argHashSigOpCount = sac.getArgument(32);
        ScriptData argZeroPadding = sac.getArgument(4); // workaround for utxoDataPlaceholder
        ScriptData argHashOutLength = sac.getArgument(2);
        ScriptData argReverseOutputAmount = sac.getArgument(8);
        ScriptData argReverseOutputScriptionVersion = sac.getArgument(2);
        ScriptData argReverseOutputScriptPublicKeyLength = sac.getArgument(8);
        ScriptData argOutputXOnlyPublicKey = sac.getArgumentUnion(1, 32);
        ScriptData argOutputScriptPublicKey = sac.getArgument(34);
        ScriptData argHaveChange = sac.getArgument(1);
        ScriptData argReverseChangeAmount = sac.getArgument(8);
        ScriptData argHashKeyLength = sac.getArgument(2);
        ScriptData argHashKey = sac.getArgumentVariableLength(22);
        ScriptData argChangePath = sac.getArgument(21);
        ScriptData argReverseLockTime = sac.getArgument(8);
        ScriptData argSubNetwokId = sac.getArgument(20);
        ScriptData argReverseGas = sac.getArgument(8);
        ScriptData argPayload = sac.getArgument(32);
        ScriptData argHashType = sac.getArgument(1);
        String addressScript = getAddressScript(argOutputXOnlyPublicKey);

        
        String script = new ScriptAssembler()
                .setCoinType(0x1b207)
                // AllHash
                .copyArgument(argReverseVersion)
                .copyArgument(argHashPrevouts)
                .copyArgument(argHashSequences)
                .copyArgument(argHashSigOpCount)
                .utxoDataPlaceholder(argZeroPadding)
                // Output
                .copyArgument(argHashType, Buffer.CACHE1)
                .copyArgument(argHashOutLength, Buffer.CACHE1)
                .copyArgument(argReverseOutputAmount, Buffer.CACHE1)
                .copyArgument(argReverseOutputScriptionVersion, Buffer.CACHE1)
                .copyArgument(argReverseOutputScriptPublicKeyLength, Buffer.CACHE1)
                .copyArgument(argOutputScriptPublicKey, Buffer.CACHE1)
                // if haveChange derive change address
                .ifEqual(argHaveChange, "01",
                        new ScriptAssembler()
                                .copyArgument(argReverseChangeAmount, Buffer.CACHE1)
                                .copyArgument(argReverseOutputScriptionVersion, Buffer.CACHE1)
                                // Change script publicKey length
                                .copyString("2200000000000000", Buffer.CACHE1)
                                // Script publicKey prefix
                                .copyString("20", Buffer.CACHE1)
                                .derivePublicKey(argChangePath, Buffer.CACHE2)
                                // To x only public key
                                .copyArgument(ScriptData.getBuffer(Buffer.CACHE2, 1,
                                        32), Buffer.CACHE1)
                                .clearBuffer(Buffer.CACHE2)
                                // Script publicKey end
                                .copyString("ac", Buffer.CACHE1)
                                .getScript(),
                        "")
                // blake2b hash all outputs
                .copyArgument(argHashKeyLength, Buffer.CACHE1)
                .copyArgument(argHashKey, Buffer.CACHE1)
                .newHash(ScriptData.getDataBufferAll(Buffer.CACHE1), Buffer.TRANSACTION,
                        ScriptAssembler.HashType.Blake2b256Mac)
                .copyArgument(argReverseLockTime)
                .copyArgument(argSubNetwokId)
                .copyArgument(argReverseGas)
                .copyArgument(argPayload)
                .copyArgument(argHashType)
                .showMessage("KAS")
                .insertString(addressScript)
                .clearBuffer(Buffer.CACHE1)
                .baseConvert(argReverseOutputAmount, Buffer.CACHE1, 8, ScriptAssembler.binaryCharset,
                        ScriptAssembler.littleEndian)
                .showAmount(ScriptData.getDataBufferAll(Buffer.CACHE1), 8)
                .clearBuffer(Buffer.CACHE1)
                .showPressButton()
                .setHeader(ScriptAssembler.HashType.Blake2b256Mac, ScriptAssembler.SignType.SCHNORR)
                .getScript();
        return script;
    }

//    public static String KASScriptSignature = Strings.padStart(
//            "",
//            144, '0');
     public static String KASScriptSignature = Strings.padEnd("FA", 144, '0');
}
