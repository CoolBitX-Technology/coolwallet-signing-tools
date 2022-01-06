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

        String script = 
                // set coinType to 4A
                ScriptAssembler.setCoinType(0x4A)
                + ScriptAssembler.copyString(HexUtil.toHexString("icx_sendTransaction.from.hx"))
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

        //version=00 ScriptAssembler.hash=04=sha3256 sign=01=ECDSA
        return ScriptAssembler.setHeader(HashType.SHA3256, SignType.ECDSA) + script;
    }

    public static String getICXScriptSignature = "30460221009398B4AA5BF223EA1472164BC4314B925044F15A45273B7E095C7257C68A6180022100E21357341DFB230F8F394FF43427D91F31AFF2A1A4BD776585B86CC74E7962FC";
}
