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

public class MinaScript {

    public static void listAll() {
        //method getDelegationScript
        //method getPaymentScript
        System.out.println("Mina Payment: \n" + getMinaPaymentScript(false) + "\n");
        System.out.println("Mina Delegate: \n" + getMinaDelegateScript(false) + "\n");
    }


    public static String getMinaPaymentScript(boolean isTestnet) {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptData argFrom = sac.getArgumentRightJustified(55);
        ScriptData argTo = sac.getArgumentRightJustified(55);
        ScriptData argAmount = sac.getArgument(8);
        ScriptData argAccountNumber = sac.getArgument(8);

        ScriptData argSequence = sac.getArgument(8);
        ScriptData argSource = sac.getArgument(8);
        ScriptData argMemo = sac.getArgument(32);
        ScriptData argNonce = sac.getArgument(4);
        ScriptData argFee = sac.getArgument(8);
        ScriptData argValidUntil = sac.getArgument(4);


        ScriptAssembler scriptAsb = new ScriptAssembler();
        String script = scriptAsb
                .setCoinType(0x312A)
                .copyString(HexUtil.toHexString("{\"account_number\":\""))
                .baseConvert(argAccountNumber, Buffer.TRANSACTION, 0, ScriptAssembler.decimalCharset, ScriptAssembler.leftJustify)
                .getScript();

        script = (!isTestnet ? scriptAsb.copyString(HexUtil.toHexString("\",\"chain_id\":\"Binance-Chain-Tigris\",\"data\":null,\"memo\":\""))
                : scriptAsb.copyString(HexUtil.toHexString("\",\"chain_id\":\"Binance-Chain-Nile\",\"data\":null,\"memo\":\"")))
                .copyRegularString(argMemo)
                .copyString(HexUtil.toHexString("\",\"msgs\":[{\"inputs\":[{\"address\":\""))
                .copyRegularString(argFrom)
                .copyString(HexUtil.toHexString("\",\"coins\":[{\"amount\":"))
                .baseConvert(argAmount, Buffer.TRANSACTION, 0, ScriptAssembler.decimalCharset, ScriptAssembler.leftJustify)
                .copyString(HexUtil.toHexString(",\"denom\":\"MINA\"}]}],\"outputs\":[{\"address\":\""))
                .copyRegularString(argTo)
                .copyString(HexUtil.toHexString("\",\"coins\":[{\"amount\":"))
                .baseConvert(argAmount, Buffer.TRANSACTION, 0, ScriptAssembler.decimalCharset, ScriptAssembler.leftJustify)
                .copyString(HexUtil.toHexString(",\"denom\":\"MINA\"}]}]}],\"sequence\":\""))
                .baseConvert(argSequence, Buffer.TRANSACTION, 0, ScriptAssembler.decimalCharset, ScriptAssembler.leftJustify)
                .copyString(HexUtil.toHexString("\",\"source\":\""))
                .baseConvert(argSource, Buffer.TRANSACTION, 0, ScriptAssembler.decimalCharset, ScriptAssembler.leftJustify)
                .copyString(HexUtil.toHexString("\"}"))
                .getScript();
        script = (!isTestnet ? scriptAsb.showMessage("MINA")
                : scriptAsb.showWrap("MINA", "TESTNET"))
                .showAddress(argTo)
                .showAmount(argAmount, 8)
                .showPressButton()
                //version=00 ScriptAssembler.hash=02=sha256 sign=01=ECDSA
                .setHeader(HashType.SHA256, SignType.ECDSA)//NEED TO BE REPLACE TO SCHNORR and POSEIDON hash
                .getScript();
        return script;
    }


    public static String getMinaDelegateScript(boolean isTestnet) {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptData argFrom = sac.getArgumentRightJustified(64);
        ScriptData argTo = sac.getArgumentRightJustified(64);
        ScriptData argValue = sac.getArgument(8);
        ScriptData argAccountNumber = sac.getArgument(8);

        ScriptData argSequence = sac.getArgument(8);
        ScriptData argSource = sac.getArgument(8);
        ScriptData argMemo = sac.getArgumentAll();


        ScriptAssembler scriptAsb = new ScriptAssembler();
        String script = scriptAsb
                .setCoinType(0x02CA)
                // set coinType to 02CA
                .copyString(HexUtil.toHexString("{\"account_number\":\""))
                .baseConvert(argAccountNumber, Buffer.TRANSACTION, 0, ScriptAssembler.decimalCharset, ScriptAssembler.leftJustify)
                .getScript();
        script = (!isTestnet ? scriptAsb.copyString(HexUtil.toHexString("\",\"chain_id\":\"Binance-Chain-Tigris\",\"data\":null,\"memo\":\""))
                : scriptAsb.copyString(HexUtil.toHexString("\",\"chain_id\":\"Binance-Chain-Nile\",\"data\":null,\"memo\":\"")))
                .copyRegularString(argMemo)
                .copyString(HexUtil.toHexString("\",\"msgs\":[{\"inputs\":[{\"address\":\""))
                .copyRegularString(argFrom)
                .copyString(HexUtil.toHexString("\",\"coins\":[{\"amount\":"))
                .baseConvert(argValue, Buffer.TRANSACTION, 0, ScriptAssembler.decimalCharset, ScriptAssembler.leftJustify)
                .copyString(HexUtil.toHexString(",\"denom\":\"MINA\"}]}],\"outputs\":[{\"address\":\""))
                .copyRegularString(argTo)
                .copyString(HexUtil.toHexString("\",\"coins\":[{\"amount\":"))
                .baseConvert(argValue, Buffer.TRANSACTION, 0, ScriptAssembler.decimalCharset, ScriptAssembler.leftJustify)
                .copyString(HexUtil.toHexString(",\"denom\":\"MINA\"}]}]}],\"sequence\":\""))
                .baseConvert(argSequence, Buffer.TRANSACTION, 0, ScriptAssembler.decimalCharset, ScriptAssembler.leftJustify)
                .copyString(HexUtil.toHexString("\",\"source\":\""))
                .baseConvert(argSource, Buffer.TRANSACTION, 0, ScriptAssembler.decimalCharset, ScriptAssembler.leftJustify)
                .copyString(HexUtil.toHexString("\"}"))
                .getScript();
        script = (!isTestnet ? scriptAsb.showMessage("MINA")
                : scriptAsb.showWrap("MINA", "TESTNET"))
                .showAddress(argTo)
                .showAmount(argValue, 8)
                .showPressButton()
                //version=00 ScriptAssembler.hash=02=sha256 sign=01=ECDSA
                .setHeader(HashType.SHA256, SignType.ECDSA)//NEED TO BE REPLACE TO SCHNORR and POSEIDON hash
                .getScript();
        return script;
    }

    public static String getMinaPaymentScriptSignature = "";
    public static String getMinaDelegateScriptSignature = "";
}