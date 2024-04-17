/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.coolbitx.wallet.signing.scriptlib;

import com.coolbitx.wallet.signing.utils.ScriptArgumentComposer;
import com.coolbitx.wallet.signing.utils.ScriptAssembler;
import com.coolbitx.wallet.signing.utils.ScriptData;
import com.coolbitx.wallet.signing.utils.ScriptData.Buffer;
import com.google.common.base.Strings;

public class DogeScript {

    public static void main(String[] args) {
        listAll();
    }

    public static void listAll() {
        System.out.println("DOGE transfer: \n" + getDOGEScript() + "\n");
    }

    public static String getAddressScript(ScriptData argOutputScriptType, ScriptData argOutputDest) {
        return new ScriptAssembler()
                .switchString(argOutputScriptType, Buffer.CACHE2, "1e,16") // D,A or 9
                .copyArgument(argOutputDest, Buffer.CACHE2)
                .hash(ScriptData.getDataBufferAll(Buffer.CACHE2), Buffer.CACHE2, ScriptAssembler.HashType.DoubleSHA256)
                .baseConvert(ScriptData.getBuffer(Buffer.CACHE2, 0, 25), Buffer.CACHE1, 0, ScriptAssembler.base58Charset, ScriptAssembler.zeroInherit)
                .showAddress(ScriptData.getDataBufferAll(Buffer.CACHE1))
                .getScript();
    }

    public static String getDOGEScript() {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptData argReverseVersion = sac.getArgument(4);
        ScriptData argZeroPadding = sac.getArgument(4);
        // Depending on destination address
        // P2PKH  = 00 start with D
        // P2SH   = 01 start with A or 9
        ScriptData argOutputScriptType = sac.getArgument(1);
        ScriptData argOutputAmount = sac.getArgument(8);
        ScriptData argOutputDest = sac.getArgument(20);
        ScriptData argHaveChange = sac.getArgument(1);
        ScriptData argChangeAmount = sac.getArgument(8);
        ScriptData argChangePath = sac.getArgument(21);
        ScriptData argReverseSequence = sac.getArgument(4);
        ScriptData argReverseLockTime = sac.getArgument(4);

        String addressScript = getAddressScript(argOutputScriptType, argOutputDest);

        String script = new ScriptAssembler()
                .setCoinType(0x03)
                .copyArgument(argReverseVersion)
                .copyString("01")
                .utxoDataPlaceholder(argZeroPadding)
                .copyArgument(argReverseSequence)
                .ifEqual(argHaveChange, "00",
                        new ScriptAssembler().copyString("01").getScript(),
                        new ScriptAssembler().copyString("02").getScript()
                )
                .baseConvert(argOutputAmount, Buffer.TRANSACTION, 8, ScriptAssembler.binaryCharset, ScriptAssembler.littleEndian)
                // switch redeemScript P2PKH=00,P2SH=01
                .switchString(argOutputScriptType, Buffer.TRANSACTION, "1976A914,17A914")
                .copyArgument(argOutputDest)
                .switchString(argOutputScriptType, Buffer.TRANSACTION, "88AC,87")
                // if haveChange
                .ifEqual(argHaveChange, "01",
                        new ScriptAssembler()
                                .baseConvert(argChangeAmount, Buffer.TRANSACTION, 8, ScriptAssembler.binaryCharset, ScriptAssembler.littleEndian)
                                .derivePublicKey(argChangePath, Buffer.CACHE1)
                                .copyString("1976A914")
                                .hash(ScriptData.getDataBufferAll(Buffer.CACHE1), Buffer.TRANSACTION, ScriptAssembler.HashType.SHA256RipeMD160)
                                .copyString("88AC")
                                .getScript(), "")
                .copyArgument(argReverseLockTime)
                .copyString("81000000")
                .clearBuffer(Buffer.CACHE1)
                .showMessage("DOGE")
                .insertString(addressScript)
                .showAmount(argOutputAmount, 8)
                .showPressButton()
                .setHeader(ScriptAssembler.HashType.DoubleSHA256, ScriptAssembler.SignType.ECDSA).getScript();
        return script;
    }

    public static String DOGEcriptSignature = Strings.padStart("304402205ca345d645aa68218ace302785706b40801ce8255add3c161ca646e88bc365e402207c7d72789867c4356c66c87c4ca8e89fce31445c5c886eaf5487fac3c1cd3412", 144, '0');
//    public static String DOGEScriptSignature = Strings.padEnd("FA", 144, '0');

}
