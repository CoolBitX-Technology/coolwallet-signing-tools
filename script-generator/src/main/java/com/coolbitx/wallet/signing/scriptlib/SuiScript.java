/*
 * To change this license header, choose License Headers in Project Properties. To change this
 * template file, choose Tools | Templates and open the template in the editor.
 */
package com.coolbitx.wallet.signing.scriptlib;

import com.coolbitx.wallet.signing.utils.HexUtil;
import com.coolbitx.wallet.signing.utils.ScriptArgumentComposer;
import com.coolbitx.wallet.signing.utils.ScriptAssembler;
import com.coolbitx.wallet.signing.utils.ScriptAssembler.HashType;
import com.coolbitx.wallet.signing.utils.ScriptAssembler.SignType;
import com.coolbitx.wallet.signing.utils.ScriptData;
import com.google.common.base.Strings;

public class SuiScript {
    public static void main(String[] args) {
        listAll();
    }

    public static void listAll() {
        System.out.println("Sui Smart Contract: \n" + getSmartContractScript(0x310) + "\n");
        System.out.println("Sui Coin Transfer: \n" + getCoinTransferScript(0x310) + "\n");
        System.out.println("Sui Token Transfer: \n" + getTokenTransferScript(0x310) + "\n");
    }

    public static String getSmartContractScript(int coinType) {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        // ScriptData path = sac.getArgument(21);
        ScriptData rawData = sac.getArgumentAll();
        ScriptAssembler scriptAssembler = new ScriptAssembler()
                // set coinType to 310
                .setCoinType(coinType)
                .arrayPointer()
                .clearBuffer(ScriptData.Buffer.TRANSACTION)
                .clearBuffer(ScriptData.Buffer.CACHE1)
                .copyArgument(rawData, ScriptData.Buffer.TRANSACTION)
                .showMessage("SUI")
                .showWrap("SMART", "")
                .showPressButton()
                .setHeader(HashType.Blake2b256, SignType.EDDSA);

        return scriptAssembler.getScript();
    }

    public static String SmartContractScriptSignature = Strings.padStart(
            "304502201beb0945e33d5371a304d8a9a504c0bf27c59a6edbd1373291be48b8f8c3aced022100be56eb062e4e8770f6bfd55d7c828084c4bb5bf028eac15190bb95ec12438ad7",
            144,
            '0');

    /*
     * E7 2A 85 09C74AFE1F 82 5208 94 A3255ECFE3F6727A62D938F4C29B2F73C361B26C 83
     * 989680 80 03
     * 80 80
     */
    public static String getCoinTransferScript(int coinType) {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptData toAddressIndex = sac.getArgument(2);
        ScriptData sentAmountIndex = sac.getArgument(2);
        ScriptData rawData = sac.getArgumentAll();

        ScriptAssembler scriptAssembler = new ScriptAssembler()
                // set coinType to 310
                .setCoinType(coinType)
                .arrayPointer()
                .clearBuffer(ScriptData.Buffer.TRANSACTION)
                .clearBuffer(ScriptData.Buffer.CACHE1)
                .copyArgument(rawData)
                .showMessage("SUI")

                // show Addreess
                .setBufferInt(toAddressIndex, 0, 32767) // 2^16/2 - 1 for 2 bytes
                .copyString(HexUtil.toHexString("0x"), ScriptData.Buffer.CACHE1)
                .baseConvert(
                        ScriptData.getBuffer(ScriptData.Buffer.TRANSACTION, ScriptData.bufInt, 32),
                        ScriptData.Buffer.CACHE1,
                        0,
                        ScriptAssembler.hexadecimalCharset,
                        ScriptAssembler.zeroInherit)
                .showAddress(ScriptData.getDataBufferAll(ScriptData.Buffer.CACHE1))

                // show Amount
                .clearBuffer(ScriptData.Buffer.CACHE1)
                .setBufferInt(sentAmountIndex, 0, 32767) // 2^16/2 - 1 for 2 bytes
                .baseConvert(
                        ScriptData.getBuffer(ScriptData.Buffer.TRANSACTION, ScriptData.bufInt, 8),
                        ScriptData.Buffer.CACHE1,
                        8,
                        ScriptAssembler.binaryCharset,
                        ScriptAssembler.inLittleEndian)
                .showAmount(ScriptData.getDataBufferAll(ScriptData.Buffer.CACHE1), 9)

                // show Press Button
                .showPressButton()
                .setHeader(HashType.Blake2b256, SignType.EDDSA);
        return scriptAssembler.getScript();
    }

    public static String CoinTransferScriptSignature = Strings.padStart(
            "3044022033641e50266c41c57f3378c12ce225c2836c314a524104a183f54653967ebd7d022057cb7efaaa5d6486943f926bb16c9db0c2ffd5e1a1bbdc3796b8a4891f6cd788",
            144,
            '0');

    /*
     * E7 2A 85 09C74AFE1F 82 5208 94 A3255ECFE3F6727A62D938F4C29B2F73C361B26C 83
     * 989680 80 03
     * 80 80
     */
    public static String getTokenTransferScript(int coinType) {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptData toAddressIndex = sac.getArgument(2);
        ScriptData sentAmountIndex = sac.getArgument(2);

        ScriptData tokenInfo = sac.getArgumentUnion(0, 9);
        ScriptData tokenDecimals = sac.getArgument(1);
        ScriptData tokenNameLength = sac.getArgument(1);
        ScriptData tokenName = sac.getArgumentVariableLength(7);

        ScriptData rawData = sac.getArgumentAll();

        ScriptAssembler scriptAssembler = new ScriptAssembler()
                // set coinType to 310
                .setCoinType(coinType)
                .arrayPointer()
                .clearBuffer(ScriptData.Buffer.TRANSACTION)
                .clearBuffer(ScriptData.Buffer.CACHE1)
                .copyArgument(rawData)

                // show main coin symbol
                .showMessage("SUI")

                // show Token symbol
                .copyString(HexUtil.toHexString("@"), ScriptData.Buffer.CACHE1)
                .setBufferInt(tokenNameLength, 1, 7)
                .copyArgument(tokenName, ScriptData.Buffer.CACHE1)
                .showMessage(ScriptData.getDataBufferAll(ScriptData.Buffer.CACHE1))

                // show to address
                .clearBuffer(ScriptData.Buffer.CACHE1)
                .setBufferInt(toAddressIndex, 0, 32767) // 2^16/2 - 1 for 2 bytes
                .copyString(HexUtil.toHexString("0x"), ScriptData.Buffer.CACHE1)
                .baseConvert(
                        ScriptData.getBuffer(ScriptData.Buffer.TRANSACTION, ScriptData.bufInt, 32),
                        ScriptData.Buffer.CACHE1,
                        0,
                        ScriptAssembler.hexadecimalCharset,
                        ScriptAssembler.zeroInherit)
                .showAddress(ScriptData.getDataBufferAll(ScriptData.Buffer.CACHE1))

                // show Amount
                .clearBuffer(ScriptData.Buffer.CACHE1)
                .setBufferInt(sentAmountIndex, 0, 32767) // 2^16/2 - 1 for 2 bytes
                .baseConvert(
                        ScriptData.getBuffer(ScriptData.Buffer.TRANSACTION, ScriptData.bufInt, 8),
                        ScriptData.Buffer.CACHE1,
                        8,
                        ScriptAssembler.binaryCharset,
                        ScriptAssembler.inLittleEndian)
                .setBufferInt(tokenDecimals, 0, 20)
                .showAmount(ScriptData.getDataBufferAll(ScriptData.Buffer.CACHE1), ScriptData.bufInt)

                // show Press Button
                .showPressButton()
                .setHeader(HashType.Blake2b256, SignType.EDDSA);
        return scriptAssembler.getScript();
    }

    public static String TokenTransferScriptSignature = Strings.padStart(
            "3045022100c50dcec26f5e09554621e2a9a02ff3594b109489c5e9fa9de68b901b10fe235b02206ec0227b57574a03e3b4e14a4a86a7ac88fe3099d5c6a500113e3b1cf900adab",
            144,
            '0');
}
