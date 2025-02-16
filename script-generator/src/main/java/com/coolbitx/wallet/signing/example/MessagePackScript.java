/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.coolbitx.wallet.signing.example;

import com.coolbitx.wallet.signing.utils.Hex;
import com.coolbitx.wallet.signing.utils.HexUtil;
import com.coolbitx.wallet.signing.utils.ScriptArgumentComposer;
import com.coolbitx.wallet.signing.utils.ScriptAssembler;
import static com.coolbitx.wallet.signing.utils.ScriptAssembler.TYPE_MESSAGE_PACK_ARRAY;
import static com.coolbitx.wallet.signing.utils.ScriptAssembler.TYPE_MESSAGE_PACK_MAP;
import com.coolbitx.wallet.signing.utils.ScriptData.Buffer;
import com.google.common.base.Strings;
import com.coolbitx.wallet.signing.utils.ScriptData;
import com.coolbitx.wallet.signing.utils.ScriptRlpArray;
import com.coolbitx.wallet.signing.utils.ScriptRlpData;

public class MessagePackScript {

    public static void main(String[] args) throws Exception {
        getApplicationCallTransaction3();
        System.out.println("Algorand: \n" + getApplicationCallTransaction3() + "\n");
    }

    public static void listAll() {
        System.out.println("Algorand: \n" + getApplicationCallTransaction3() + "\n");
    }

    public static String getApplicationCallTransaction3() {
        String TX = HexUtil.toHexString("TX".getBytes());
        ScriptRlpArray array = new ScriptRlpArray();
        ScriptRlpData item0 = array.getRlpItemArgument();
        ScriptRlpData item1 = array.getRlpItemArgument();
        ScriptRlpData item2 = array.getRlpItemArgument();
        ScriptRlpData item3 = array.getRlpItemArgument();
        ScriptRlpData item4 = array.getRlpItemArgument();
        ScriptRlpData item5 = array.getRlpItemArgument();
        ScriptRlpData item6 = array.getRlpItemArgument();
        ScriptRlpArray array7 = array.getRlpArrayArgument();
        ScriptRlpData item7_0 = array7.getRlpItemArgument();
        ScriptRlpData item7_1 = array7.getRlpItemArgument();
        ScriptRlpArray array7_2 = array7.getRlpArrayArgument();
        ScriptRlpData item7_2_0 = array7_2.getRlpItemArgument();
        ScriptRlpData item7_2_1 = array7_2.getRlpItemArgument();
        ScriptRlpData item7_2_2 = array7_2.getRlpItemArgument();
        ScriptRlpData item7_3 = array7.getRlpItemArgument();
        ScriptRlpData item7_4 = array7.getRlpItemArgument();
        ScriptRlpData item7_5 = array7.getRlpItemArgument();
        ScriptRlpData item8 = array.getRlpItemArgument();
        ScriptRlpData item9 = array.getRlpItemArgument();
        ScriptRlpData item10 = array.getRlpItemArgument();
        ScriptRlpData item11 = array.getRlpItemArgument();

        System.out.println("array: " + array.toString());
        System.out.println("item0: " + item0.toString());
        System.out.println("item1: " + item1.toString());
        System.out.println("item6: " + item6.toString());
        System.out.println("array7: " + array7.toString());
        System.out.println("item7_0: " + item7_0.toString());
        System.out.println("item7_1: " + item7_1.toString());
        System.out.println("array7_2: " + array7_2.toString());
        System.out.println("item7_2_0: " + item7_2_0.toString());
        System.out.println("item7_2_2: " + item7_2_2.toString());
        System.out.println("item7_5: " + item7_5.toString());
        System.out.println("item8: " + item8.toString());
        System.out.println("item11: " + item11.toString());

        String script = new ScriptAssembler().setCoinType(0x11B)
                .copyArgument(item0)
//                .copyArgument(array7)
                .forloop(array, Buffer.TRANSACTION)
                .setHeader(ScriptAssembler.HashType.NONE, ScriptAssembler.SignType.EDDSA)
                .getScript();
        return script;
    }

    public static String ApplicationIDScriptSignature = Strings.padEnd("FA", 144, '0');

    public static String getAddressScript() {
        String TX = HexUtil.toHexString("TX".getBytes());
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptData rcvPresent = sac.getArgument(1);
        ScriptData rcvKey = sac.getArgumentRightJustified(8);
        ScriptData rcvValue = sac.getArgumentRightJustified(32);
        ScriptData ID = sac.getArgumentRightJustified(8);

        ScriptAssembler scriptAsb = new ScriptAssembler();
        String script = scriptAsb.setCoinType(0x11B)
                .arrayPointer()
                .copyString(TX)
                .arrayPointer()
                .ifEqual(rcvPresent, "00",
                        "", new ScriptAssembler().messagePack(ScriptAssembler.typeString, rcvKey, ScriptData.Buffer.TRANSACTION)
                                .messagePack(ScriptAssembler.typeBinary, rcvValue, ScriptData.Buffer.TRANSACTION).getScript())
                .arrayEnd(TYPE_MESSAGE_PACK_MAP)
                .showMessage("ALGO")
                .hash(rcvValue, ScriptData.Buffer.CACHE1, ScriptAssembler.HashType.SHA512256)
                .copyArgument(rcvValue, ScriptData.Buffer.CACHE2)
                .copyArgument(ScriptData.getBuffer(ScriptData.Buffer.CACHE1, 28, 4), ScriptData.Buffer.CACHE2)
                .clearBuffer(ScriptData.Buffer.CACHE1)
                .copyString(HexUtil.toHexString("ABCDEFGHIJKLMNOPQRSTUVWXYZ234567"), Buffer.CACHE1)
                .baseConvert(ScriptData.getBuffer(ScriptData.Buffer.CACHE2, 0, 36), ScriptData.Buffer.CACHE2, 58, ScriptAssembler.cache1Charset, ScriptAssembler.bitLeftJustify8to5)
                .showAddress(ScriptData.getDataBufferAll(Buffer.CACHE2, 36))
                .clearBuffer(ScriptData.Buffer.CACHE1)
                .clearBuffer(ScriptData.Buffer.CACHE2)
                .baseConvert(ID, ScriptData.Buffer.CACHE1, 16, ScriptAssembler.decimalCharset, ScriptAssembler.zeroInherit)
                .showMessage(ScriptData.getDataBufferAll(Buffer.CACHE1))
                .showPressButton()
                .setHeader(ScriptAssembler.HashType.NONE, ScriptAssembler.SignType.EDDSA)
                .getScript();
        return script;
    }

// JSON format wanted:
// {
//   "1294a54f44fc00ae692ead9a1235c4dfc41afcfe":ture,
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
//            + "01" // value1
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
        ScriptData value1 = sac.getArgument(1);

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
                .setCoinType(0x11b)
                //                .copyString("24")
                .arrayPointer()
                .messagePack(ScriptAssembler.typeString, key1, Buffer.TRANSACTION)
                .messagePack(ScriptAssembler.typeBoolean, value1, Buffer.TRANSACTION)
                .messagePack(ScriptAssembler.typeString, key2, Buffer.TRANSACTION)
                .messagePack(ScriptAssembler.typeInt, value2, Buffer.TRANSACTION)
                //                .messagePack(ScriptAssembler.typeString, key3, Buffer.TRANSACTION)
                .messagePack(Hex.encode("result".getBytes()), Buffer.TRANSACTION)
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
                .showMessage("ALGO")
                //                .showAddress(ScriptData.getDataBufferAll(Buffer.CACHE2, 53))
                //                .showAmount(argAmount, 6)
                .showPressButton()
                .setHeader(ScriptAssembler.HashType.NONE, ScriptAssembler.SignType.EDDSA)
                .getScript();
        return script;
    }

    public static String TransferScriptSignature = Strings.padEnd("FA", 144, '0');

}
