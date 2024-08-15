/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.coolbitx.wallet.signing.scriptlib;

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

        public static String getAddressScript(ScriptData argOutputScriptPublicKey) {
                String hrp = "kaspa";
                String hrpExpand = "";
                for (int i = 0; i < hrp.length(); i++) {
                        hrpExpand += HexUtil.toHexString((hrp.charAt(i) >> 5) & 7, 1);
                }
                hrpExpand += "00";
                for (int i = 0; i < hrp.length(); i++) {
                        hrpExpand += HexUtil.toHexString(hrp.charAt(i) & 31, 1);
                }
                String bech32AddressScript = new ScriptAssembler()
                                .copyString(hrpExpand + "00", Buffer.CACHE2)
                                .baseConvert(argOutputScriptPublicKey, Buffer.CACHE2, 55,
                                                ScriptAssembler.binary32Charset, ScriptAssembler.bitLeftJustify8to5) // 34
                                                                                                                     // *
                                                                                                                     // 8
                                                                                                                     // /
                                                                                                                     // 5
                                                                                                                     // =
                                                                                                                     // 55
                                .copyString("000000000000", Buffer.CACHE2)
                                .bech32Polymod(ScriptData.getDataBufferAll(Buffer.CACHE2), Buffer.CACHE1)
                                .clearBuffer(Buffer.CACHE2)
                                .baseConvert(argOutputScriptPublicKey, Buffer.CACHE2, 55,
                                                ScriptAssembler.base32BitcoinCashCharset,
                                                ScriptAssembler.bitLeftJustify8to5)
                                .baseConvert(ScriptData.getDataBufferAll(Buffer.CACHE1), Buffer.CACHE2, 8,
                                                ScriptAssembler.base32BitcoinCashCharset, 0)
                                .showAddress(ScriptData.getDataBufferAll(Buffer.CACHE2))
                                .getScript();
                return bech32AddressScript;
        }

        // 0000
        // b9040cd6c2cc517e2684744b61b9defaeb670312759d8a778007ab72c5d06e02
        // 0f99135614633e507969d12522c80f967cff6ebc0436863e02ee42b2b66556fc
        // 8523b0471bcbea04575ccaa635eef9f9114f2890bda54367e5ff8caa3878bf82
        // e803000000000000
        // 0000
        // 2200000000000000
        // 200a3da6e8c7a8795440e60e4662686bc17cd965780095604c85d8d137e0f48079ac
        // 01
        // 22c8668c00000000
        // 328000002c8001b207800000000000000000000000
        // 0000000000000000
        // 0000000000000000000000000000000000000000
        // 0000000000000000
        // 0000000000000000000000000000000000000000000000000000000000000000
        // 01
        public static String getTransferScript(boolean isTestnet) {
                ScriptArgumentComposer sac = new ScriptArgumentComposer();
                ScriptData argReverseVersion = sac.getArgument(2);
                ScriptData argHashPrevouts = sac.getArgument(32);
                ScriptData argHashSequences = sac.getArgument(32);
                ScriptData argHashSigOpCount = sac.getArgument(32);
                ScriptData argReverseOutputAmount = sac.getArgument(8);
                ScriptData argReverseOutputScriptionVersion = sac.getArgument(2);
                ScriptData argReverseOutputScriptPublicKeyLength = sac.getArgument(8);
                ScriptData argOutputScriptPublicKey = sac.getArgument(34);
                ScriptData argHaveChange = sac.getArgument(1);
                ScriptData argChangeAmount = sac.getArgument(8);
                ScriptData argChangePath = sac.getArgument(21);
                ScriptData argReverseLockTime = sac.getArgument(8);
                ScriptData argSubNetwokId = sac.getArgument(20);
                ScriptData argReverseGas = sac.getArgument(8);
                ScriptData argPayload = sac.getArgument(32);
                ScriptData argReverseHashType = sac.getArgument(1);

                String addressScript = getAddressScript(argOutputScriptPublicKey);

                ScriptData argZeroPadding = sac.getArgument(4); // workaround for utxoDataPlaceholder
                String script = new ScriptAssembler()
                                .setCoinType(0x1b207)
                                // AllHash
                                .copyArgument(argReverseVersion)
                                .copyArgument(argHashPrevouts)
                                .copyArgument(argHashSequences)
                                .copyArgument(argHashSigOpCount)
                                .utxoDataPlaceholder(argZeroPadding)
                                // Output
                                .copyArgument(argReverseOutputAmount, Buffer.CACHE1)
                                .copyArgument(argReverseOutputScriptionVersion, Buffer.CACHE1)
                                .copyArgument(argReverseOutputScriptPublicKeyLength, Buffer.CACHE1)
                                .copyArgument(argOutputScriptPublicKey, Buffer.CACHE1)
                                // if haveChange derive change address
                                .ifEqual(argHaveChange, "01",
                                                new ScriptAssembler()
                                                                .copyArgument(argChangeAmount, Buffer.CACHE1)
                                                                .copyArgument(argReverseOutputScriptionVersion, Buffer.CACHE1)
                                                                .derivePublicKey(argChangePath, Buffer.CACHE2)
                                                                // Change script publicKey length
                                                                .copyString("22", Buffer.CACHE1)
                                                                // Script publicKey prefix
                                                                .copyString("20", Buffer.CACHE1)
                                                                .copyArgument(ScriptData.getBuffer(Buffer.CACHE2, 1,
                                                                                32), Buffer.CACHE1)
                                                                .clearBuffer(Buffer.CACHE2)
                                                                // Script publicKey end
                                                                .copyString("ac", Buffer.CACHE1)
                                                                .getScript(),
                                                "")
                                // blake2b hash all outputs            
                                .hash(ScriptData.getDataBufferAll(Buffer.CACHE1), Buffer.TRANSACTION,
                                                ScriptAssembler.HashType.Blake2b256)
                                .copyArgument(argReverseLockTime)
                                .copyArgument(argSubNetwokId)
                                .copyArgument(argReverseGas)
                                .copyArgument(argPayload)
                                .copyArgument(argReverseHashType)
                                .clearBuffer(Buffer.CACHE1)
                                .clearBuffer(Buffer.CACHE2)
                                .showMessage("KAS")
                                .insertString(addressScript)
                                .baseConvert(argReverseOutputAmount, Buffer.CACHE1, 8, ScriptAssembler.binaryCharset,
                                                ScriptAssembler.littleEndian)
                                .showAmount(ScriptData.getDataBufferAll(Buffer.CACHE1), 8)
                                .clearBuffer(Buffer.CACHE1)
                                .showPressButton()
                                .setHeader(ScriptAssembler.HashType.Blake2b256, ScriptAssembler.SignType.SCHNORR)
                                .getScript();
                return script;
        }

        public static String KASScriptSignature = Strings.padStart(
                        "304502200f255c7e238f586478e20a97161433cc32cd8cc916d0638a3081e5dc329f2ca1022100de05b467c312effef03a810ec390b8907bb64a8f61a49ff90f9f1228b8bd134c",
                        144, '0');
        // public static String KASScriptSignature = Strings.padEnd("FA", 144, '0');
}
