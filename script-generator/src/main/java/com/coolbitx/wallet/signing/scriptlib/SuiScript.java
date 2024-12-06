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
        ScriptData rawData = sac.getArgumentAll();
        ScriptAssembler scriptAssembler = new ScriptAssembler()
                // set coinType to 310
                .setCoinType(coinType)
                .arrayPointer()
                .clearBuffer(ScriptData.Buffer.TRANSACTION)
                .copyArgument(rawData)
                .showMessage("SUI")
                .showWrap("SMART", "")
                .showPressButton()
                .setHeader(HashType.NONE, SignType.EDDSA);
        return scriptAssembler.getScript();
    }

    public static String SmartContractScriptSignature = Strings.padStart(
            "30450221008bd26f2ae80c316b678433e8e9fa4a1e04d6d87dd1a7be33f7f68867df0c30dd0220708aa67f90928e100ec03c4d350e5c71f96430493d7c86c04171cc105b91811c",
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
                .copyArgument(rawData)
                .showMessage("SUI")
                .clearBuffer(ScriptData.Buffer.TRANSACTION)
                .clearBuffer(ScriptData.Buffer.CACHE1)
                .setBufferInt(toAddressIndex, 0, 255)
                .showAddress(ScriptData.getBuffer(ScriptData.Buffer.TRANSACTION, ScriptData.bufInt, 64))
                .copyString(HexUtil.toHexString("0x"), ScriptData.Buffer.CACHE1)
                .setBufferInt(sentAmountIndex, 0, 255)
                .baseConvert(
                        ScriptData.getBuffer(ScriptData.Buffer.TRANSACTION, ScriptData.bufInt, 16),
                        ScriptData.Buffer.CACHE1,
                        8,
                        ScriptAssembler.binaryCharset,
                        ScriptAssembler.inLittleEndian)
                .showAmount(ScriptData.getDataBufferAll(ScriptData.Buffer.CACHE1), 9)
                .clearBuffer(ScriptData.Buffer.CACHE1)
                .showPressButton()
                .setHeader(HashType.NONE, SignType.EDDSA);
        return scriptAssembler.getScript();
    }

    public static String CoinTransferScriptSignature = Strings.padStart(
            "30450220139187cbe56bd0338fe28b8afeea6880fe0e4a2df26436be074eb527c95f85170221008373a3ee87178a4bce4caedeee955fcfead319d5e951552580f0ef486a0d8671",
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

    public static String TokenTransferScriptSignature = Strings.padStart(
            "3045022100c36a14ef60bea98590119a621c84fc5962f0e232c5c675a66667617be6403cb2022056cdb47788be550ec25f05474672cdaf115ceffba2a13fb31ab35bd8d7c14c44",
            144,
            '0');
}
