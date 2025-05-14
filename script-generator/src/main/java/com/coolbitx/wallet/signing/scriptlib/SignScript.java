/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.coolbitx.wallet.signing.scriptlib;

import com.coolbitx.wallet.signing.utils.ScriptArgumentComposer;
import com.coolbitx.wallet.signing.utils.ScriptAssembler;
import com.coolbitx.wallet.signing.utils.ScriptData;
import com.google.common.base.Strings;

/**
 *
 * @author tom.lin
 */
public class SignScript {
    
    public static void main(String[] args) {
        listAll();
    }

    public static void listAll() {
        System.out.println("SignECDSA: \n" + getSignECDSAScript() + "\n");
    }
 
    public static String getSignECDSAScript() {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptData hexData = sac.getArgumentAll();

        String script
                = new ScriptAssembler()
                        .copyArgument(hexData)
                        .setHeader(ScriptAssembler.HashType.NONE, ScriptAssembler.SignType.ECDSA)
                        .getScript();
        return script;
    }

    public static String SignECDSAScriptSignature
            = Strings.padEnd(
                    "FA",
                    144,
                    '0');
}
