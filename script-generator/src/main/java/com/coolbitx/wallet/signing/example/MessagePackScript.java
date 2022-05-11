/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.coolbitx.wallet.signing.example;

import com.coolbitx.wallet.signing.utils.ScriptArgumentComposer;
import com.coolbitx.wallet.signing.utils.ScriptAssembler;
import com.coolbitx.wallet.signing.utils.ScriptData;
import com.coolbitx.wallet.signing.utils.ScriptAssembler.HashType;
import com.coolbitx.wallet.signing.utils.ScriptAssembler.SignType;
import static com.coolbitx.wallet.signing.utils.ScriptAssembler.TYPE_MESSAGE_PACK_ARRAY;
import static com.coolbitx.wallet.signing.utils.ScriptAssembler.TYPE_MESSAGE_PACK_MAP;
import com.coolbitx.wallet.signing.utils.ScriptData.Buffer;
import com.google.common.base.Strings;

public class MessagePackScript {

    public static void listAll() {
        System.out.println("Xrp: \n" + getTransferScript() + "\n");
    }
// JSON format wanted:
// {
//   "1294a54f44fc00ae692ead9a1235c4dfc41afcfe":136,
//   "id":0,
//   "result":{
//     "baseFee":"0",
//     "compact":[
//       "a",
//       "b",
//       "c",
//       "d",
//       "e"
//     ]
//   }
// }
    
//    Argument:
//            Hex.toHexString("1294a54f44fc00ae692ead9a1235c4dfc41afcfe".getBytes()) // key1
//            + "00000088" // value1
//            + Strings.padStart(Hex.toHexString("id".getBytes()), 20, '0') // key2
//            + "00000000000000000000" // value2
//            + Strings.padStart(Hex.toHexString("result".getBytes()), 20, '0') // key3
//            + Strings.padStart(Hex.toHexString("baseFee".getBytes()), 20, '0') // key4
//            + Strings.padStart(Hex.toHexString("0".getBytes()), 20, '0') // value4
//            + Strings.padStart(Hex.toHexString("compact".getBytes()), 20, '0') // key5
//            + Strings.padStart(Hex.toHexString("a".getBytes()), 8, '0') // value5a
//            + Strings.padStart(Hex.toHexString("b".getBytes()), 8, '0') // value5b
//            + Strings.padStart(Hex.toHexString("c".getBytes()), 8, '0') // value5c
//            + Strings.padStart(Hex.toHexString("d".getBytes()), 8, '0') // value5d
//            + Strings.padStart(Hex.toHexString("e".getBytes()), 8, '0'); // value5e
    public static String getTransferScript() {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptData key1 = sac.getArgument(40);
        ScriptData value1 = sac.getArgumentRightJustified(4);

        ScriptData key2 = sac.getArgumentRightJustified(10);
        ScriptData value2 = sac.getArgumentRightJustified(10);

        ScriptData key3 = sac.getArgumentRightJustified(10);
        ScriptData key4 = sac.getArgumentRightJustified(10);
        ScriptData value4 = sac.getArgumentRightJustified(10);

        ScriptData key5 = sac.getArgumentRightJustified(10);

        ScriptData value5a = sac.getArgumentRightJustified(4);
        ScriptData value5b = sac.getArgumentRightJustified(4);
        ScriptData value5c = sac.getArgumentRightJustified(4);
        ScriptData value5d = sac.getArgumentRightJustified(4);
        ScriptData value5e = sac.getArgumentRightJustified(4);

        String script = new ScriptAssembler()
                .setCoinType(0x90)
                //                .copyString("24")
                .arrayPointer()
                .messagePack(ScriptAssembler.typeString, key1, Buffer.TRANSACTION)
                .messagePack(ScriptAssembler.typeInt, value1, Buffer.TRANSACTION)
                .messagePack(ScriptAssembler.typeString, key2, Buffer.TRANSACTION)
                .messagePack(ScriptAssembler.typeInt, value2, Buffer.TRANSACTION)
                .messagePack(ScriptAssembler.typeString, key3, Buffer.TRANSACTION)
                .arrayPointer()
                .messagePack(ScriptAssembler.typeString, key4, Buffer.TRANSACTION)
                .messagePack(ScriptAssembler.typeString, value4, Buffer.TRANSACTION)
                .messagePack(ScriptAssembler.typeString, key5, Buffer.TRANSACTION)
                .arrayPointer()
                .messagePack(ScriptAssembler.typeString, value5a, Buffer.TRANSACTION)
                .messagePack(ScriptAssembler.typeString, value5b, Buffer.TRANSACTION)
                .messagePack(ScriptAssembler.typeString, value5c, Buffer.TRANSACTION)
                .messagePack(ScriptAssembler.typeString, value5d, Buffer.TRANSACTION)
                .messagePack(ScriptAssembler.typeString, value5e, Buffer.TRANSACTION)
                .arrayEnd(TYPE_MESSAGE_PACK_ARRAY)
                .arrayEnd(TYPE_MESSAGE_PACK_MAP)
                .arrayEnd(TYPE_MESSAGE_PACK_MAP)
                //                .showAddress(ScriptData.getDataBufferAll(Buffer.CACHE2, 53))
                //                .showAmount(argAmount, 6)
                .showPressButton()
                .setHeader(HashType.SHA512, SignType.ECDSA)
                .getScript();
        return script;
    }

    public static String TransferScriptSignature = Strings.padEnd("FA", 144, '0');

}
