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

                return "03000301" + ScriptAssembler.setCoinType(0x90) + ScriptAssembler.copyString("5354580012000022")
                                + ScriptAssembler.copyArgument(argFlags) + ScriptAssembler.copyString("24")
                                + ScriptAssembler.copyArgument(argSequence) + ScriptAssembler.copyString("2E")
                                + ScriptAssembler.copyArgument(argTag) + ScriptAssembler.copyString("201B")
                                + ScriptAssembler.copyArgument(argLastLedgerSequence)
                                + ScriptAssembler.copyString("6140") + ScriptAssembler.copyArgument(argAmount)
                                + ScriptAssembler.copyString("6840") + ScriptAssembler.copyArgument(argFee)
                                + ScriptAssembler.copyString("7321") + ScriptAssembler.copyArgument(argPublicKey)
                                + ScriptAssembler.copyString("8114") + ScriptAssembler.copyArgument(argAccount)
                                + ScriptAssembler.copyString("8314") + ScriptAssembler.copyArgument(argDest)
                                + ScriptAssembler.showMessage("XRP") + ScriptAssembler.copyString("00", Buffer.FREE)
                                + ScriptAssembler.copyArgument(argDest, Buffer.FREE)
                                + ScriptAssembler.hash(ScriptData.getDataBufferAll(Buffer.FREE), Buffer.FREE,
                                                ScriptAssembler.DoubleSHA256)
                                + ScriptAssembler.copyString(HexUtil.toHexString(
                                                "rpshnaf39wBUDNEGHJKLM4PQRST7VWXYZ2bcdeCg65jkm8oFqi1tuvAxyz"),
                                                Buffer.EXTENDED)
                                + ScriptAssembler.baseConvert(ScriptData.getBuffer(Buffer.FREE, 0, 25), Buffer.FREE, 45,
                                                ScriptAssembler.extendedCharset, ScriptAssembler.zeroInherit)
                                + ScriptAssembler.showAddress(ScriptData.getDataBufferAll(Buffer.FREE, 53))
                                + ScriptAssembler.showAmount(argAmount, 6) + ScriptAssembler.showPressButton();
        }

}
