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

        public static String getAddressScript(
                        ScriptData argOutputScriptType,
                        ScriptData argOutputXOnlyPublicKeyOrScriptHash,
                        ScriptData argOutputPublicKey) {
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
                                .switchString(argOutputScriptType, Buffer.CACHE1, "01,02,08")
                                .ifEqual(argOutputScriptType, "01",
                                                // addressVersion + outputPublicKey -> cache1
                                                new ScriptAssembler().copyArgument(
                                                                argOutputPublicKey,
                                                                Buffer.CACHE1).getScript(),
                                                // addressVersion + outputXOnlyPublicKeyOrScriptHash -> cache1
                                                new ScriptAssembler().copyArgument(
                                                                argOutputXOnlyPublicKeyOrScriptHash,
                                                                Buffer.CACHE1).getScript())
                                .ifEqual(argOutputScriptType, "01",
                                                // 34 * 8 / 5 = 54.4 ≈ 55
                                                // convert8To5Bits(version + outputPublicKey) -> cache2
                                                new ScriptAssembler().baseConvert(
                                                                ScriptData.getDataBufferAll(Buffer.CACHE1),
                                                                Buffer.CACHE2,
                                                                55,
                                                                ScriptAssembler.binary32Charset,
                                                                ScriptAssembler.bitLeftJustify8to5).getScript(),
                                                // 33 * 8 / 5 = 52.8 ≈ 53
                                                // convert8To5Bits(addressVersion + outputXOnlyPublicKeyOrScriptHash) ->
                                                // cache2
                                                new ScriptAssembler().baseConvert(
                                                                ScriptData.getDataBufferAll(Buffer.CACHE1),
                                                                Buffer.CACHE2,
                                                                53,
                                                                ScriptAssembler.binary32Charset,
                                                                ScriptAssembler.bitLeftJustify8to5).getScript())
                                .copyString("0000000000000000", Buffer.CACHE2)
                                // calculate checksum -> cache1
                                .bchPolymod(ScriptData.getDataBufferAll(Buffer.CACHE2), Buffer.CACHE1)
                                .clearBuffer(Buffer.CACHE2)
                                .ifEqual(argOutputScriptType, "01",
                                                // bech32(convert8To5Bits(addressVersion + outputPublicKey)) -> cache2
                                                new ScriptAssembler().baseConvert(
                                                                ScriptData.getBuffer(Buffer.CACHE1, 0, 34),
                                                                Buffer.CACHE2,
                                                                55,
                                                                ScriptAssembler.base32BitcoinCashCharset,
                                                                ScriptAssembler.bitLeftJustify8to5).getScript(),
                                                // bech32(convert8To5Bits(addressVersion +
                                                // outputXOnlyPublicKeyOrScriptHash)) -> cache2
                                                new ScriptAssembler().baseConvert(
                                                                ScriptData.getBuffer(Buffer.CACHE1, 0, 33),
                                                                Buffer.CACHE2,
                                                                53,
                                                                ScriptAssembler.base32BitcoinCashCharset,
                                                                ScriptAssembler.bitLeftJustify8to5).getScript())
                                // bech32(convert8To5Bits(checksum)) -> cache2
                                .ifEqual(argOutputScriptType, "01",
                                                new ScriptAssembler().baseConvert(
                                                                ScriptData.getDataBufferAll(Buffer.CACHE1, 34),
                                                                Buffer.CACHE2,
                                                                8,
                                                                ScriptAssembler.base32BitcoinCashCharset,
                                                                ScriptAssembler.bitLeftJustify8to5).getScript(),
                                                new ScriptAssembler().baseConvert(
                                                                ScriptData.getDataBufferAll(Buffer.CACHE1, 33),
                                                                Buffer.CACHE2,
                                                                8,
                                                                ScriptAssembler.base32BitcoinCashCharset,
                                                                ScriptAssembler.bitLeftJustify8to5).getScript())
                                .showAddress(ScriptData.getDataBufferAll(Buffer.CACHE2))
                                .getScript();
                return bech32AddressScript;
        }

        /**
         * 0000 // transaction version
         * b9040cd6c2cc517e2684744b61b9defaeb670312759d8a778007ab72c5d06e02 //
         * previousOutputsHash
         * 0f99135614633e507969d12522c80f967cff6ebc0436863e02ee42b2b66556fc //
         * sequencesHash
         * 8523b0471bcbea04575ccaa635eef9f9114f2890bda54367e5ff8caa3878bf82 //
         * sigOpCountsHash
         * 00000000 // zeroPadding
         * 13 // hashType
         * 0068 // totalOutputsLength
         * 00/01/02 // outputScriptType
         * 000a3da6e8c7a8795440e60e4662686bc17cd965780095604c85d8d137e0f48079 //
         * outputPublicKeyOrScriptHash
         * e803000000000000 // outputReverseAmount
         * 0000 // outputReverseScriptVersion
         * 2200000000000000 // outputScriptPublicKeyReverseLength
         * 01 // haveChange
         * 22c8668c00000000 // changeReverseAmount
         * 165472616e73616374696f6e5369676e696e6748617368 // hashKey:
         * TransactionSigningHash
         * 328000002c8001b207800000000000000000000000 // sePath
         * 0000000000000000 // reverseLockTime
         * 0000000000000000000000000000000000000000 // subNetworkId
         * 0000000000000000 // reverseGas
         * 0000000000000000000000000000000000000000000000000000000000000000 // payload
         * 01 // signType
         **/
        public static String getTransferScript(boolean isTestnet) {
                ScriptArgumentComposer sac = new ScriptArgumentComposer();
                ScriptData argReverseVersion = sac.getArgument(2);
                ScriptData argHashPrevouts = sac.getArgument(32);
                ScriptData argHashSequences = sac.getArgument(32);
                ScriptData argHashSigOpCount = sac.getArgument(32);
                ScriptData argZeroPadding = sac.getArgument(4); // workaround for utxoDataPlaceholder
                ScriptData argNewHashType = sac.getArgument(1);
                ScriptData argHashOutLength = sac.getArgument(2);

                ScriptData argOutputScriptType = sac.getArgument(1);
                ScriptData argOutputXOnlyPublicKeyOrScriptHash = sac.getArgumentUnion(1, 32);
                ScriptData argOutputPublicKey = sac.getArgument(33);
                String addressScript = getAddressScript(argOutputScriptType, argOutputXOnlyPublicKeyOrScriptHash,
                                argOutputPublicKey);

                ScriptData argReverseOutputAmount = sac.getArgument(8);
                ScriptData argReverseOutputScriptionVersion = sac.getArgument(2);
                ScriptData argReverseOutputScriptPublicKeyLength = sac.getArgument(8);

                ScriptData argHaveChange = sac.getArgument(1);
                ScriptData argReverseChangeAmount = sac.getArgument(8);
                ScriptData argHashKeyLength = sac.getArgument(2);
                ScriptData argHashKey = sac.getArgument(22);
                ScriptData argChangePath = sac.getArgument(21);
                ScriptData argReverseLockTime = sac.getArgument(8);
                ScriptData argSubNetwokId = sac.getArgument(20);
                ScriptData argReverseGas = sac.getArgument(8);
                ScriptData argPayload = sac.getArgument(32);
                ScriptData argHashType = sac.getArgument(1);

                String script = new ScriptAssembler()
                                .setCoinType(0x1b207)
                                // AllHash
                                .copyArgument(argReverseVersion)
                                .copyArgument(argHashPrevouts)
                                .copyArgument(argHashSequences)
                                .copyArgument(argHashSigOpCount)
                                .utxoDataPlaceholder(argZeroPadding)
                                // Output
                                .copyArgument(argNewHashType, Buffer.CACHE1)
                                .copyArgument(argHashOutLength, Buffer.CACHE1)
                                .copyArgument(argReverseOutputAmount, Buffer.CACHE1)
                                .copyArgument(argReverseOutputScriptionVersion, Buffer.CACHE1)
                                .copyArgument(argReverseOutputScriptPublicKeyLength, Buffer.CACHE1)
                                // ScriptPublicKey Rules:
                                // 1. Output script types:
                                // - 00: 20 + X-only Public key + AC
                                // - 01: 21 + public key + AB
                                // - 02: AA20 + Script hash + 87
                                // 2. Copy the corresponding public key or script hash to CACHE1 buffer
                                .switchString(argOutputScriptType, Buffer.CACHE1, "20,21,AA20")
                                .ifEqual(argOutputScriptType, "01",
                                                new ScriptAssembler().copyArgument(
                                                                argOutputPublicKey,
                                                                Buffer.CACHE1).getScript(),
                                                new ScriptAssembler().copyArgument(
                                                                argOutputXOnlyPublicKeyOrScriptHash,
                                                                Buffer.CACHE1).getScript())
                                .switchString(argOutputScriptType, Buffer.CACHE1, "AC,AB,87")
                                // if haveChange derive change address
                                .ifEqual(argHaveChange, "01",
                                                new ScriptAssembler()
                                                                .copyArgument(argReverseChangeAmount, Buffer.CACHE1)
                                                                .copyArgument(argReverseOutputScriptionVersion,
                                                                                Buffer.CACHE1)
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

        public static String KASScriptSignature = Strings.padStart(
                        "3045022100907440fb94c1888ccefc93e50b4f916de123900ab75245f6cc8c5fa42ceef80f02207a2404f273ac008f1eb51dc96e3cda695dca3b3e5b746716feecb22e1021775e",
                        144, '0');
}
