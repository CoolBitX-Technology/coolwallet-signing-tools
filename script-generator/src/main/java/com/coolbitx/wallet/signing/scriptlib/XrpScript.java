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
import com.coolbitx.wallet.signing.utils.ScriptAssembler.HashType;
import com.coolbitx.wallet.signing.utils.ScriptAssembler.SignType;
import com.coolbitx.wallet.signing.utils.ScriptData.Buffer;

public class XrpScript {

    public static void listAll() {
        System.out.println("Xrp: \n" + getXRPScript() + "\n");
    }

    public static String getXRPScript() {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptData argAccount = sac.getArgument(20);
        ScriptData argPublicKey = sac.getArgument(33);
        ScriptData argDest = sac.getArgument(20);
        ScriptData argPadding1 = sac.getArgument(1);
        ScriptData argAmount = sac.getArgument(7);
        ScriptData argPadding2 = sac.getArgument(1);
        ScriptData argFee = sac.getArgument(7);
        ScriptData argSequence = sac.getArgument(4);
        ScriptData argLastLedgerSequence = sac.getArgument(4);
        ScriptData argTag = sac.getArgument(4);
        ScriptData argFlags = sac.getArgument(4);

        String script = new ScriptAssembler()
                .setCoinType(0x90)
                .copyString("5354580012000022")
                .copyArgument(argFlags)
                .copyString("24")
                .copyArgument(argSequence)
                .copyString("2E")
                .copyArgument(argTag)
                .copyString("201B")
                .copyArgument(argLastLedgerSequence)
                .copyString("6140")
                .copyArgument(argAmount)
                .copyString("6840")
                .copyArgument(argFee)
                .copyString("7321")
                .copyArgument(argPublicKey)
                .copyString("8114")
                .copyArgument(argAccount)
                .copyString("8314")
                .copyArgument(argDest)
                .showMessage("XRP")
                .copyString("00", Buffer.CACHE2)
                .copyArgument(argDest, Buffer.CACHE2)
                .hash(ScriptData.getDataBufferAll(Buffer.CACHE2), Buffer.CACHE2, HashType.DoubleSHA256)
                .copyString(HexUtil.toHexString("rpshnaf39wBUDNEGHJKLM4PQRST7VWXYZ2bcdeCg65jkm8oFqi1tuvAxyz"), Buffer.CACHE1)
                .baseConvert(ScriptData.getBuffer(Buffer.CACHE2, 0, 25), Buffer.CACHE2, 45, ScriptAssembler.cache1Charset, ScriptAssembler.zeroInherit)
                .showAddress(ScriptData.getDataBufferAll(Buffer.CACHE2, 53))
                .showAmount(argAmount, 6)
                .showPressButton()
                .setHeader(HashType.SHA512, SignType.ECDSA)
                .getScript();
        return script;
    }

    public static String XRPScriptSignature = "0000304402206B2A707864EB98033BF83A80E8FDD7FCF903CC059ABC0E4FBB317040B6E9AD1D02203DCD2BDC4480B88DB0D9DC74948BAF6BD62203E90AE39990978999ABEAEABA63";

}
