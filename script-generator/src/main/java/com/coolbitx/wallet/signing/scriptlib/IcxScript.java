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
import com.coolbitx.wallet.signing.utils.ScriptData.Buffer;

public class IcxScript {

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

        return "03000401"
                + //version=00 ScriptAssembler.hash=04=sha3256 sign=01=ECDSA
                ScriptAssembler.setCoinType(0x4A)
                + // set coinType to 4A
                ScriptAssembler.copyString(HexUtil.toHexString("icx_sendTransaction.from.hx"))
                + ScriptAssembler.baseConvert(argFrom, Buffer.TRANSACTION, 0, ScriptAssembler.hexadecimalCharset, ScriptAssembler.leftJustify)
                + ScriptAssembler.copyString(HexUtil.toHexString(".nid.0x"))
                + ScriptAssembler.baseConvert(argNetworkId, Buffer.TRANSACTION, 0, ScriptAssembler.hexadecimalCharset, ScriptAssembler.leftJustify)
                + ScriptAssembler.copyString(HexUtil.toHexString(".stepLimit.0x186a0.timestamp.0x"))
                + ScriptAssembler.baseConvert(argTime, Buffer.TRANSACTION, 0, ScriptAssembler.hexadecimalCharset, ScriptAssembler.leftJustify)
                + ScriptAssembler.copyString(HexUtil.toHexString(".to.hx"), Buffer.CACHE2)
                + ScriptAssembler.baseConvert(argTo, Buffer.CACHE2, 0, ScriptAssembler.hexadecimalCharset, ScriptAssembler.leftJustify)
                + ScriptAssembler.copyArgument(ScriptData.getDataBufferAll(Buffer.CACHE2))
                + ScriptAssembler.copyString(HexUtil.toHexString(".value.0x"))
                + ScriptAssembler.baseConvert(argValue, Buffer.TRANSACTION, 0, ScriptAssembler.hexadecimalCharset, ScriptAssembler.leftJustify)
                + ScriptAssembler.copyString(HexUtil.toHexString(".version.0x3"))
                + ScriptAssembler.showMessage("ICX")
                + ScriptAssembler.showAddress(ScriptData.getDataBufferAll(Buffer.CACHE2, 4))
                + ScriptAssembler.showAmount(argValue, 18)
                + ScriptAssembler.showPressButton();
    }

}
