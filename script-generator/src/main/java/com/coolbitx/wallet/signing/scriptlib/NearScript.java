/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.coolbitx.wallet.signing.scriptlib;

import com.coolbitx.wallet.signing.utils.ScriptArgumentComposer;
import com.coolbitx.wallet.signing.utils.ScriptAssembler;
import com.coolbitx.wallet.signing.utils.ScriptData;
import com.coolbitx.wallet.signing.utils.ScriptAssembler.HashType;
import com.coolbitx.wallet.signing.utils.ScriptAssembler.SignType;

public class NearScript {

    public static void listAll() {
        System.out.println("Near: \n" + getNearScript() + "\n");
    }

    public static String getNearScript() {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptData argAmount = sac.getArgumentRightJustified(10);
        ScriptData argPayload = sac.getArgumentAll();

        String script = new ScriptAssembler()
            .setCoinType(0x018d)
            .copyArgument(argPayload)
            .showMessage("NEAR")
            .showAmount(argAmount, 18)
            .showPressButton()
            .setHeader(HashType.SHA256, SignType.EDDSA)
            .getScript();

        return script;
    }
}
