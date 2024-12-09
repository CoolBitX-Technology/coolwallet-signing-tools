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
        System.out.println("Sui: \n" + getCoinTransferScript(0x310) + "\n");
        System.out.println("Sui token: \n" + getTokenTransferScript(0x310) + "\n");
    }
    
    public static String getSmartContractScript(int coinType) {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptData path = sac.getArgument(21);
        ScriptData rawData = sac.getArgumentAll();
        ScriptAssembler scriptAssembler = new ScriptAssembler()
                // set coinType to 310
                .setCoinType(coinType)
                .arrayPointer()
                .clearBuffer(ScriptData.Buffer.TRANSACTION)
                .clearBuffer(ScriptData.Buffer.CACHE1)
//                .copyArgument(path)
//                .copyString("d532ec7786285eacc5aee62f5fcf74d8542e6a557f29b2794cc6269e96d6188c", ScriptData.Buffer.TRANSACTION)
                .derivePublicKey(path, ScriptData.Buffer.TRANSACTION)
//                .showAddress(ScriptData.getDataBufferAll(ScriptData.Buffer.CACHE1))
                .showMessage("SUI")
                .showWrap("SMART", "")
                .showPressButton()
                .setHeader(HashType.NONE, SignType.EDDSA);
        return scriptAssembler.getScript();
    }

    public static String SmartContractScriptSignature = Strings.padEnd(
            "FA",
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
//                .setBufferInt(toAddressIndex, 0, 255)
//                .copyString(HexUtil.toHexString("0x"), ScriptData.Buffer.CACHE1)
//                .copyArgument(ScriptData.getBuffer(ScriptData.Buffer.TRANSACTION, ScriptData.bufInt, 64), ScriptData.Buffer.CACHE1)
//                .showAddress(ScriptData.getDataBufferAll(ScriptData.Buffer.CACHE1))
//                .clearBuffer(ScriptData.Buffer.TRANSACTION)
//                .copyArgument(ScriptData.getDataBufferAll(ScriptData.Buffer.CACHE1))
//                .clearBuffer(ScriptData.Buffer.CACHE1)
//                .setBufferInt(sentAmountIndex, 0, 255)
//                .baseConvert(
//                        ScriptData.getBuffer(ScriptData.Buffer.TRANSACTION, ScriptData.bufInt, 16),
//                        ScriptData.Buffer.CACHE1,
//                        8,
//                        ScriptAssembler.binaryCharset,
//                        ScriptAssembler.inLittleEndian)
//                .showAmount(ScriptData.getDataBufferAll(ScriptData.Buffer.CACHE1), 9)
                .showPressButton()
                .setHeader(HashType.NONE, SignType.EDDSA);
        return scriptAssembler.getScript();
    }

    public static String CoinTransferScriptSignature = Strings.padEnd(
            "FA",
            144,
            '0');

    /*
     * E7 2A 85 09C74AFE1F 82 5208 94 A3255ECFE3F6727A62D938F4C29B2F73C361B26C 83
     * 989680 80 03
     * 80 80
     */
    public static String getTokenTransferScript(int coinType) {
        // TODO implement
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptData argData = sac.getArgumentAll();
        ScriptAssembler scriptAssembler = new ScriptAssembler()
                // set coinType to 310
                .setCoinType(coinType)
                .arrayPointer()
                .copyArgument(argData)
                .showMessage("SUI")
                .showWrap("SMART", "")
                .showPressButton()
                .setHeader(HashType.SHA512, SignType.EDDSA);
        return scriptAssembler.getScript();
    }

    public static String TokenTransferScriptSignature = Strings.padEnd(
            "FA",
            144,
            '0');
}
