/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.coolbitx.wallet.signing.scriptlib;

import com.coolbitx.wallet.signing.utils.ScriptAssembler;
import com.coolbitx.wallet.signing.utils.ScriptData;
import com.coolbitx.wallet.signing.utils.ScriptData.Buffer;
import com.coolbitx.wallet.signing.utils.ScriptObjectAbstract;
import com.coolbitx.wallet.signing.utils.ScriptRlpArray;
import com.coolbitx.wallet.signing.utils.ScriptRlpData;
import com.google.common.base.Strings;

public class ZcashScript {

    public static void main(String[] args) {
        listAll();
    }

    public static void listAll() {
        System.out.println("Zcash transparent transaction: \n" + getZECScript(false) + "\n");
    }

    public static String getAddressScript(boolean isTestnet, ScriptObjectAbstract argOutputScriptType,
        ScriptObjectAbstract argOutputDest) {
        String addressScript = new ScriptAssembler()
            .switchString(argOutputScriptType, Buffer.CACHE2, !isTestnet ? "1CB8,1CBD" : "1D25,1CBA") // t1,t3:tm,t2
            .copyArgument(argOutputDest, Buffer.CACHE2)
            .hash(ScriptData.getDataBufferAll(Buffer.CACHE2), Buffer.CACHE2, ScriptAssembler.HashType.DoubleSHA256)
            .baseConvert(ScriptData.getBuffer(Buffer.CACHE2, 0, 26), Buffer.CACHE1, 0, ScriptAssembler.base58Charset,
                ScriptAssembler.zeroInherit)
            .showAddress(ScriptData.getDataBufferAll(Buffer.CACHE1))
            .getScript();
        return addressScript;
    }

    public static String getZECScript(boolean isTestnet) {
        ScriptRlpArray array = new ScriptRlpArray();
        ScriptRlpData argReverseVersion = array.getRlpItemArgument();
        ScriptRlpData argReverseGroupId = array.getRlpItemArgument();
        ScriptRlpData argHashPrevouts = array.getRlpItemArgument();
        ScriptRlpData argHashSequences = array.getRlpItemArgument();
        ScriptRlpData argOutputAmount = array.getRlpItemArgument();
        ScriptRlpData argOutputScriptType = array.getRlpItemArgument();
        ScriptRlpData argOutputDest = array.getRlpItemArgument();

        ScriptRlpData argHaveChange = array.getRlpItemArgument();
        ScriptRlpData argChangeAmount = array.getRlpItemArgument();
        ScriptRlpData argChangeScriptType = array.getRlpItemArgument();
        ScriptRlpData argChangePath = array.getRlpItemArgument();
        ScriptRlpData argOutputHashPersonal = array.getRlpItemArgument();

        ScriptRlpData argLockTime = array.getRlpItemArgument();
        ScriptRlpData argExpiryHeight = array.getRlpItemArgument();
        ScriptRlpData argReverseHashType = array.getRlpItemArgument();

        String addressScript = getAddressScript(isTestnet, argOutputScriptType, argOutputDest);

        String script = new ScriptAssembler().setCoinType(0x85)
            .copyArgument(argReverseVersion)
            .copyArgument(argReverseGroupId)
            .copyArgument(argHashPrevouts)
            .copyArgument(argHashSequences)
            .baseConvert(argOutputAmount, Buffer.CACHE1, 8, ScriptAssembler.binaryCharset, ScriptAssembler.littleEndian)
            // switch redeemScript P2PKH=00,P2SH=01
            .switchString(argOutputScriptType, Buffer.CACHE1, "1976A914,17A914")
            .copyArgument(argOutputDest, Buffer.CACHE1)
            .switchString(argOutputScriptType, Buffer.CACHE1, "88AC,87")
            // if haveChange
            .ifEqual(argHaveChange, "01",
                new ScriptAssembler()
                    .baseConvert(argChangeAmount, Buffer.CACHE1, 8, ScriptAssembler.binaryCharset,
                        ScriptAssembler.littleEndian)
                    .derivePublicKey(argChangePath, Buffer.CACHE2)
                    .copyString("1976A914", Buffer.CACHE1)
                    // if P2PKH
                    .ifEqual(argChangeScriptType, "00",
                        new ScriptAssembler()
                            .hash(ScriptData.getDataBufferAll(Buffer.CACHE2), Buffer.CACHE1,
                                ScriptAssembler.HashType.SHA256RipeMD160)
                            .getScript(),
                        "")
                    .copyString("88AC", Buffer.CACHE1)
                    .getScript(),
                "")
            .advancedHash(ScriptData.getDataBufferAll(Buffer.CACHE1), argOutputHashPersonal, Buffer.TRANSACTION,
                ScriptAssembler.AdvancedHashType.Blake2b256Personal)
            .copyString("0000000000000000000000000000000000000000000000000000000000000000") // Hash JoinSplits
            .copyString("0000000000000000000000000000000000000000000000000000000000000000") // Hash ShieldedSpends
            .copyString("0000000000000000000000000000000000000000000000000000000000000000") // Hash ShieldedOutputs
            .copyArgument(argLockTime)
            .copyArgument(argExpiryHeight)
            .copyString("0000000000000000") // Value Balance
            .copyArgument(argReverseHashType)
            .utxoDataPlaceholder()
            .clearBuffer(Buffer.CACHE1)
            .clearBuffer(Buffer.CACHE2)
            .showMessage("ZEC")
            .insertString(addressScript)
            .showAmount(argOutputAmount, 8)
            .showPressButton()
            .setHeader(ScriptAssembler.AdvancedHashType.Blake2b256Personal, ScriptAssembler.SignType.ECDSA)
            .getScript();
        return script;
    }

    public static String ZECScriptSignature = Strings.padStart("3046022100eebe7b79978d11b265ceb3977a683df311d7d3a4844fc513a611d8ea20fb6d42022100ea45cb25c9aa448d693ec06334ee47a90f5dfdf84e868198cf957754c4547213", 144, '0');

}
