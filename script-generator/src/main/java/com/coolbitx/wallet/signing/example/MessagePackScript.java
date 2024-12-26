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
import com.coolbitx.wallet.signing.utils.ScriptRlpArgumentComposer;
import com.coolbitx.wallet.signing.utils.ScriptRlpData;
import com.coolbitx.wallet.signing.utils.ScriptRlpData.MutableScriptData;

public class MessagePackScript {

    public static void main(String[] args) throws Exception {
        getApplicationCallTransaction3();
//        System.out.println("Algorand: \n" + getApplicationCallTransaction3() + "\n");
    }

    public static void listAll() {
        System.out.println("Algorand: \n" + getApplicationCallTransaction3() + "\n");
    }

    public static String getApplicationCallTransaction3() {
        String TX = HexUtil.toHexString("TX".getBytes());
        ScriptRlpArgumentComposer sac = new ScriptRlpArgumentComposer();
        ScriptRlpData apaaPresent = sac.getRlpItemArgument();
        ScriptRlpData apaaValue1 = sac.getRlpItemArgument();
        ScriptRlpData apaaValue2 = sac.getRlpItemArgument();
        ScriptRlpData apaaValue3 = sac.getRlpItemArgument();
        ScriptRlpData apaaValue4 = sac.getRlpItemArgument();
        ScriptRlpData apaaValue5 = sac.getRlpItemArgument();
        ScriptRlpData apaaValue6 = sac.getRlpItemArgument();
        MutableScriptData rlpArray = sac.getRlpArrayArgument();
        ScriptRlpData apbbValue1 = rlpArray.getRlpItemArgument();
        ScriptRlpData apbbValue2 = rlpArray.getRlpItemArgument();
        ScriptRlpData apbbValue3 = rlpArray.getRlpItemArgument();
        ScriptRlpData apbbValue4 = rlpArray.getRlpItemArgument();
        ScriptRlpData apbbValue5 = rlpArray.getRlpItemArgument();
        ScriptRlpData apbbValue6 = rlpArray.getRlpItemArgument();

        ScriptRlpData apanValue = sac.getRlpItemArgument();
        ScriptRlpData apasPresent = sac.getRlpItemArgument();
        ScriptRlpData apasValue1 = sac.getRlpItemArgument();
        ScriptRlpData apasValue2 = sac.getRlpItemArgument();
        ScriptRlpData apasValue3 = sac.getRlpItemArgument();
        ScriptRlpData apasValue4 = sac.getRlpItemArgument();
        ScriptRlpData apasValue5 = sac.getRlpItemArgument();
        ScriptRlpData apasValue6 = sac.getRlpItemArgument();
        ScriptRlpData apatPresent = sac.getRlpItemArgument();
        ScriptRlpData apatValue1 = sac.getRlpItemArgument();
        ScriptRlpData apatValue2 = sac.getRlpItemArgument();
        ScriptRlpData apatValue3 = sac.getRlpItemArgument();
        ScriptRlpData apatValue4 = sac.getRlpItemArgument();
        ScriptRlpData apatValue5 = sac.getRlpItemArgument();
        ScriptRlpData apatValue6 = sac.getRlpItemArgument();
        ScriptRlpData apfaPresent = sac.getRlpItemArgument();
        ScriptRlpData apfaValue1 = sac.getRlpItemArgument();
        ScriptRlpData apfaValue2 = sac.getRlpItemArgument();
        ScriptRlpData apfaValue3 = sac.getRlpItemArgument();
        ScriptRlpData apfaValue4 = sac.getRlpItemArgument();
        ScriptRlpData apfaValue5 = sac.getRlpItemArgument();
        ScriptRlpData apfaValue6 = sac.getRlpItemArgument();
        ScriptRlpData apidValue = sac.getRlpItemArgument();
        ScriptRlpData feeValue = sac.getRlpItemArgument();
        ScriptRlpData fvValue = sac.getRlpItemArgument();
        // GenesisID
        ScriptRlpData genValue = sac.getRlpItemArgument();
        // Group
        ScriptRlpData grpValue = sac.getRlpItemArgument();
        // Genesis Hash
        ScriptRlpData ghValue = sac.getRlpItemArgument();
        // Last Valid
        ScriptRlpData lvValue = sac.getRlpItemArgument();
        // Lease
        ScriptRlpData lxValue = sac.getRlpItemArgument();
        // Note
        ScriptRlpData noteValue = sac.getRlpItemArgument();
        // Rekey To
        ScriptRlpData rekeyValue = sac.getRlpItemArgument();
        // Sender
        ScriptRlpData sndValue = sac.getRlpItemArgument();
        // Type
        ScriptRlpData typeValue = sac.getRlpItemArgument();

        System.out.println("apaaPresent: " + apaaPresent.toString());
        System.out.println("apaaValue1: " + apaaValue1.toString());
        System.out.println("apaaValue6: " + apaaValue6.toString());
        System.out.println("apbbValue1: " + apbbValue1.toString());
        System.out.println("apbbValue6: " + apbbValue6.toString());
        System.out.println("typeValue: " + typeValue.toString());

        String script = new ScriptAssembler().setCoinType(0x11B)
                .arrayPointer()
                .copyString(TX)
                .arrayPointer()
                .isEmpty(apaaPresent,
                        "", new ScriptAssembler()
                                .messagePack(Hex.encode("apaa".getBytes()), ScriptData.Buffer.TRANSACTION)
                                .arrayPointer()
                                .isEmpty(apaaValue1,
                                        "", new ScriptAssembler().messagePack(ScriptAssembler.typeBinary, apaaValue1, ScriptData.Buffer.TRANSACTION).getScript())
                                .isEmpty(apaaValue2,
                                        "", new ScriptAssembler().messagePack(ScriptAssembler.typeBinary, apaaValue2, ScriptData.Buffer.TRANSACTION).getScript())
                                .isEmpty(apaaValue3,
                                        "", new ScriptAssembler().messagePack(ScriptAssembler.typeBinary, apaaValue3, ScriptData.Buffer.TRANSACTION).getScript())
                                .isEmpty(apaaValue4,
                                        "", new ScriptAssembler().messagePack(ScriptAssembler.typeBinary, apaaValue4, ScriptData.Buffer.TRANSACTION).getScript())
                                .isEmpty(apaaValue5,
                                        "", new ScriptAssembler().messagePack(ScriptAssembler.typeBinary, apaaValue5, ScriptData.Buffer.TRANSACTION).getScript())
                                .isEmpty(apaaValue6,
                                        "", new ScriptAssembler().messagePack(ScriptAssembler.typeBinary, apaaValue6, ScriptData.Buffer.TRANSACTION).getScript())
                                .arrayEnd(TYPE_MESSAGE_PACK_ARRAY)
                                .getScript())
                .isEmpty(apanValue,
                        "", new ScriptAssembler().messagePack(Hex.encode("apan".getBytes()), ScriptData.Buffer.TRANSACTION)
                                .messagePack(ScriptAssembler.typeInt, apanValue, ScriptData.Buffer.TRANSACTION).getScript())
                .isEmpty(apasPresent,
                        "", new ScriptAssembler()
                                .messagePack(Hex.encode("apas".getBytes()), ScriptData.Buffer.TRANSACTION)
                                .arrayPointer()
                                .isEmpty(apasValue1,
                                        "", new ScriptAssembler().messagePack(ScriptAssembler.typeInt, apasValue1, ScriptData.Buffer.TRANSACTION).getScript())
                                .isEmpty(apasValue2,
                                        "", new ScriptAssembler().messagePack(ScriptAssembler.typeInt, apasValue2, ScriptData.Buffer.TRANSACTION).getScript())
                                .isEmpty(apasValue3,
                                        "", new ScriptAssembler().messagePack(ScriptAssembler.typeInt, apasValue3, ScriptData.Buffer.TRANSACTION).getScript())
                                .isEmpty(apasValue4,
                                        "", new ScriptAssembler().messagePack(ScriptAssembler.typeInt, apasValue4, ScriptData.Buffer.TRANSACTION).getScript())
                                .isEmpty(apasValue5,
                                        "", new ScriptAssembler().messagePack(ScriptAssembler.typeInt, apasValue5, ScriptData.Buffer.TRANSACTION).getScript())
                                .isEmpty(apasValue6,
                                        "", new ScriptAssembler().messagePack(ScriptAssembler.typeInt, apasValue6, ScriptData.Buffer.TRANSACTION).getScript())
                                .arrayEnd(TYPE_MESSAGE_PACK_ARRAY)
                                .getScript())
                .isEmpty(apatPresent,
                        "", new ScriptAssembler()
                                .messagePack(Hex.encode("apat".getBytes()), ScriptData.Buffer.TRANSACTION)
                                .arrayPointer()
                                .isEmpty(apatValue1,
                                        "", new ScriptAssembler().messagePack(ScriptAssembler.typeBinary, apatValue1, ScriptData.Buffer.TRANSACTION).getScript())
                                .isEmpty(apatValue2,
                                        "", new ScriptAssembler().messagePack(ScriptAssembler.typeBinary, apatValue2, ScriptData.Buffer.TRANSACTION).getScript())
                                .isEmpty(apatValue3,
                                        "", new ScriptAssembler().messagePack(ScriptAssembler.typeBinary, apatValue3, ScriptData.Buffer.TRANSACTION).getScript())
                                .isEmpty(apatValue4,
                                        "", new ScriptAssembler().messagePack(ScriptAssembler.typeBinary, apatValue4, ScriptData.Buffer.TRANSACTION).getScript())
                                .isEmpty(apatValue5,
                                        "", new ScriptAssembler().messagePack(ScriptAssembler.typeBinary, apatValue5, ScriptData.Buffer.TRANSACTION).getScript())
                                .isEmpty(apatValue6,
                                        "", new ScriptAssembler().messagePack(ScriptAssembler.typeBinary, apatValue6, ScriptData.Buffer.TRANSACTION).getScript())
                                .arrayEnd(TYPE_MESSAGE_PACK_ARRAY)
                                .getScript())
                .isEmpty(apfaPresent,
                        "", new ScriptAssembler()
                                .messagePack(Hex.encode("apfa".getBytes()), ScriptData.Buffer.TRANSACTION)
                                .arrayPointer()
                                .isEmpty(apfaValue1,
                                        "", new ScriptAssembler().messagePack(ScriptAssembler.typeInt, apfaValue1, ScriptData.Buffer.TRANSACTION).getScript())
                                .isEmpty(apfaValue2,
                                        "", new ScriptAssembler().messagePack(ScriptAssembler.typeInt, apfaValue2, ScriptData.Buffer.TRANSACTION).getScript())
                                .isEmpty(apfaValue3,
                                        "", new ScriptAssembler().messagePack(ScriptAssembler.typeInt, apfaValue3, ScriptData.Buffer.TRANSACTION).getScript())
                                .isEmpty(apfaValue4,
                                        "", new ScriptAssembler().messagePack(ScriptAssembler.typeInt, apfaValue4, ScriptData.Buffer.TRANSACTION).getScript())
                                .isEmpty(apfaValue5,
                                        "", new ScriptAssembler().messagePack(ScriptAssembler.typeInt, apfaValue5, ScriptData.Buffer.TRANSACTION).getScript())
                                .isEmpty(apfaValue6,
                                        "", new ScriptAssembler().messagePack(ScriptAssembler.typeInt, apfaValue6, ScriptData.Buffer.TRANSACTION).getScript())
                                .arrayEnd(TYPE_MESSAGE_PACK_ARRAY)
                                .getScript())
                .isEmpty(apidValue,
                        "", new ScriptAssembler().messagePack(Hex.encode("apid".getBytes()), ScriptData.Buffer.TRANSACTION)
                                .messagePack(ScriptAssembler.typeInt, apidValue, ScriptData.Buffer.TRANSACTION).getScript())
                .isEmpty(feeValue,
                        "", new ScriptAssembler().messagePack(Hex.encode("fee".getBytes()), ScriptData.Buffer.TRANSACTION)
                                .messagePack(ScriptAssembler.typeInt, feeValue, ScriptData.Buffer.TRANSACTION).getScript())
                .isEmpty(fvValue,
                        "", new ScriptAssembler().messagePack(Hex.encode("fv".getBytes()), ScriptData.Buffer.TRANSACTION)
                                .messagePack(ScriptAssembler.typeInt, fvValue, ScriptData.Buffer.TRANSACTION).getScript())
                .isEmpty(genValue,
                        "", new ScriptAssembler().messagePack(Hex.encode("gen".getBytes()), ScriptData.Buffer.TRANSACTION)
                                .messagePack(ScriptAssembler.typeString, genValue, ScriptData.Buffer.TRANSACTION).getScript())
                .isEmpty(grpValue,
                        "", new ScriptAssembler().messagePack(Hex.encode("grp".getBytes()), ScriptData.Buffer.TRANSACTION)
                                .messagePack(ScriptAssembler.typeBinary, grpValue, ScriptData.Buffer.TRANSACTION).getScript())
                .isEmpty(ghValue,
                        "", new ScriptAssembler().messagePack(Hex.encode("gh".getBytes()), ScriptData.Buffer.TRANSACTION)
                                .messagePack(ScriptAssembler.typeBinary, ghValue, ScriptData.Buffer.TRANSACTION).getScript())
                .isEmpty(lvValue,
                        "", new ScriptAssembler().messagePack(Hex.encode("lv".getBytes()), ScriptData.Buffer.TRANSACTION)
                                .messagePack(ScriptAssembler.typeInt, lvValue, ScriptData.Buffer.TRANSACTION).getScript())
                .isEmpty(lxValue,
                        "", new ScriptAssembler().messagePack(Hex.encode("lx".getBytes()), ScriptData.Buffer.TRANSACTION)
                                .messagePack(ScriptAssembler.typeBinary, lxValue, ScriptData.Buffer.TRANSACTION).getScript())
                .isEmpty(noteValue,
                        "", new ScriptAssembler().messagePack(Hex.encode("note".getBytes()), ScriptData.Buffer.TRANSACTION)
                                .messagePack(ScriptAssembler.typeBinary, noteValue, ScriptData.Buffer.TRANSACTION).getScript())
                .isEmpty(rekeyValue,
                        "", new ScriptAssembler().messagePack(Hex.encode("rekey".getBytes()), ScriptData.Buffer.TRANSACTION)
                                .messagePack(ScriptAssembler.typeBinary, rekeyValue, ScriptData.Buffer.TRANSACTION).getScript())
                .isEmpty(sndValue,
                        "", new ScriptAssembler().messagePack(Hex.encode("snd".getBytes()), ScriptData.Buffer.TRANSACTION)
                                .messagePack(ScriptAssembler.typeBinary, sndValue, ScriptData.Buffer.TRANSACTION).getScript())
                .isEmpty(typeValue,
                        "", new ScriptAssembler().messagePack(Hex.encode("type".getBytes()), ScriptData.Buffer.TRANSACTION)
                                .messagePack(ScriptAssembler.typeString, typeValue, ScriptData.Buffer.TRANSACTION).getScript())
                .arrayEnd(TYPE_MESSAGE_PACK_MAP)
                .showMessage("ALGO")
                .showMessage("appl")
                .isEmpty(apidValue,
                        "", new ScriptAssembler().showMessage("appID").baseConvert(apidValue, ScriptData.Buffer.CACHE1, 16, ScriptAssembler.decimalCharset, ScriptAssembler.zeroInherit)
                                .showMessage(ScriptData.getDataBufferAll(ScriptData.Buffer.CACHE1)).clearBuffer(ScriptData.Buffer.CACHE1).getScript())
                .showPressButton()
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
