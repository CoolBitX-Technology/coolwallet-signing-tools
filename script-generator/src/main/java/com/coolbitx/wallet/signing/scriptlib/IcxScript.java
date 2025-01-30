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
import com.google.common.base.Strings;

public class IcxScript {
    public static void main(String[] args) {
        listAll();
    }

    public static void listAll() {
        System.out.println("Icx: \n" + getICXScript() + "\n");
    }

    public static String getICXScript() {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptData argFrom = sac.getArgument(20);
        ScriptData argTo = sac.getArgument(20);
        ScriptData argValue = sac.getArgument(10);
        ScriptData argTime = sac.getArgument(10);
        ScriptData argNetworkId = sac.getArgument(2);

        String script = new ScriptAssembler()
                // set coinType to 4A
                .setCoinType(0x4A)
                .copyString(HexUtil.toHexString("icx_sendTransaction.from.hx"))
                .baseConvert(argFrom, Buffer.TRANSACTION, 0, ScriptAssembler.hexadecimalCharset, ScriptAssembler.zeroInherit)
                .copyString(HexUtil.toHexString(".nid.0x"))
                .baseConvert(argNetworkId, Buffer.TRANSACTION, 0, ScriptAssembler.hexadecimalCharset, ScriptAssembler.leftJustify)
                .copyString(HexUtil.toHexString(".stepLimit.0x186a0.timestamp.0x"))
                .baseConvert(argTime, Buffer.TRANSACTION, 0, ScriptAssembler.hexadecimalCharset, ScriptAssembler.leftJustify)
                .copyString(HexUtil.toHexString(".to.hx"), Buffer.CACHE2)
                .baseConvert(argTo, Buffer.CACHE2, 0, ScriptAssembler.hexadecimalCharset, ScriptAssembler.zeroInherit)
                .copyArgument(ScriptData.getDataBufferAll(Buffer.CACHE2))
                .copyString(HexUtil.toHexString(".value.0x"))
                .baseConvert(argValue, Buffer.TRANSACTION, 0, ScriptAssembler.hexadecimalCharset, ScriptAssembler.leftJustify)
                .copyString(HexUtil.toHexString(".version.0x3"))
                .showMessage("ICX")
                .showAddress(ScriptData.getDataBufferAll(Buffer.CACHE2, 4))
                .showAmount(argValue, 18)
                .showPressButton()
                //version=00 ScriptAssembler.hash=04=sha3256 sign=01=ECDSA
                .setHeader(HashType.SHA3256, SignType.ECDSA)
                .getScript();
        return script;
    }

    public static String getICXScriptSignature() {
        return Strings.padStart(
                "304502204b1911ba2d52c9fb0c87552fc47b31f25e9ae13ab4887d14d467bd3eb84baa26022100dc36343d426045e3f5f0d1223e4ed0d0eab3e6ab33a5d7bbb9de09870f478604",
                144,
                '0');
    }
}

