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

public class EtcScript {

    public static void listAll() {
        System.out.println("Etc: \n" + getCROScript() + "\n");
    }

    /*
    E7
    2A
    85 09C74AFE1F
    82 5208
    94 A3255ECFE3F6727A62D938F4C29B2F73C361B26C
    83 989680
    80
    03
    80
    80
     */
    public static String getCROScript() {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptData argTo = sac.getArgument(20);
        ScriptData argValue = sac.getArgumentRightJustified(10);
        ScriptData argGasPrice = sac.getArgumentRightJustified(10);
        ScriptData argGasLimit = sac.getArgumentRightJustified(10);
        ScriptData argNonce = sac.getArgumentRightJustified(8);

        String script = new ScriptAssembler()
                // set coinType to 3C
                .setCoinType(0x3D)
                .arrayPointer()
                // nonce
                .rlpString(argNonce)
                // gasPrice
                .rlpString(argGasPrice)
                // gasLimit
                .rlpString(argGasLimit)
                // toAddress
                .copyString("94").copyArgument(argTo)
                // value
                .rlpString(argValue)
                // data
                .copyString("80")
                // chainId v
                .copyString("3D", Buffer.CACHE1)
                .rlpString(ScriptData.getDataBufferAll(Buffer.CACHE1))
                // r,s
                .copyString("8080")
                .arrayEnd(1)
                .showMessage("ETC")
                .copyString(HexUtil.toHexString("0x"), Buffer.CACHE2)
                .baseConvert(argTo, Buffer.CACHE2, 0, ScriptAssembler.hexadecimalCharset,
                        ScriptAssembler.zeroInherit)
                .showAddress(ScriptData.getDataBufferAll(Buffer.CACHE2))
                .showAmount(argValue, 18)
                .showPressButton()
                // version=00 ScriptAssembler.hash=06=ScriptAssembler.Keccak256 sign=01=ECDSA
                .setHeader(HashType.Keccak256, SignType.ECDSA)
                .getScript();
        return script;
    }

    public static String ETCScriptSignature = "03040601C707000000003DA00700C2ACD70032FFF8C2ACD7001EFFF6C2ACD70028FFF6CC071094CAA02700C2A2D700FFF6CC071080CC0E103DC2E09700CC07C0028080BE0710DC07C003455443CC0FC0023078BAA02F6C0E04DDF09700DAA2D7C0FFF612D207CC05065052455353425554546F4E";

}