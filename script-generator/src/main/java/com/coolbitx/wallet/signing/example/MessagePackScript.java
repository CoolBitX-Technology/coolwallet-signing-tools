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
    
    public static String getApplicationCallTransaction3() {
        String TX = HexUtil.toHexString("TX".getBytes());
        ScriptArgumentComposer sac = new ScriptArgumentComposer();

        // App Arguments
        ScriptData apaaPresent = sac.getArgument(1);
        // App Argument 1
        ScriptData apaaValue1Present = sac.getArgument(1);
        ScriptData apaaValue1 = sac.getArgumentRightJustified(1024);
        // App Argument 2
        ScriptData apaaValue2Present = sac.getArgument(1);
        ScriptData apaaValue2 = sac.getArgumentRightJustified(1024);
        // App Argument 3
        ScriptData apaaValue3Present = sac.getArgument(1);
        ScriptData apaaValue3 = sac.getArgumentRightJustified(1024);
        // App Argument 4
        ScriptData apaaValue4Present = sac.getArgument(1);
        ScriptData apaaValue4 = sac.getArgumentRightJustified(1024);
        // App Argument 5
        ScriptData apaaValue5Present = sac.getArgument(1);
        ScriptData apaaValue5 = sac.getArgumentRightJustified(1024);
        // App Argument 6
        ScriptData apaaValue6Present = sac.getArgument(1);
        ScriptData apaaValue6 = sac.getArgumentRightJustified(1024);

        // On Complete
        ScriptData apanPresent = sac.getArgument(1);
        ScriptData apanValue = sac.getArgumentRightJustified(8);

        // Foreign Asset
        ScriptData apasPresent = sac.getArgument(1);
        // Foreign Asset 1
        ScriptData apasValue1Present = sac.getArgument(1);
        ScriptData apasValue1 = sac.getArgumentRightJustified(8);
        // Foreign Asset 2
        ScriptData apasValue2Present = sac.getArgument(1);
        ScriptData apasValue2 = sac.getArgumentRightJustified(8);
        // Foreign Asset 3
        ScriptData apasValue3Present = sac.getArgument(1);
        ScriptData apasValue3 = sac.getArgumentRightJustified(8);
        // Foreign Asset 4
        ScriptData apasValue4Present = sac.getArgument(1);
        ScriptData apasValue4 = sac.getArgumentRightJustified(8);
        // Foreign Asset 5
        ScriptData apasValue5Present = sac.getArgument(1);
        ScriptData apasValue5 = sac.getArgumentRightJustified(8);
        // Foreign Asset 6
        ScriptData apasValue6Present = sac.getArgument(1);
        ScriptData apasValue6 = sac.getArgumentRightJustified(8);

        // Accounts
        ScriptData apatPresent = sac.getArgument(1);
        // Accounts 1
        ScriptData apatValue1Present = sac.getArgument(1);
        ScriptData apatValue1 = sac.getArgumentRightJustified(32);
        // Accounts 2
        ScriptData apatValue2Present = sac.getArgument(1);
        ScriptData apatValue2 = sac.getArgumentRightJustified(32);
        // Accounts 3
        ScriptData apatValue3Present = sac.getArgument(1);
        ScriptData apatValue3 = sac.getArgumentRightJustified(32);
        // Accounts 4
        ScriptData apatValue4Present = sac.getArgument(1);
        ScriptData apatValue4 = sac.getArgumentRightJustified(32);
        // Accounts 5
        ScriptData apatValue5Present = sac.getArgument(1);
        ScriptData apatValue5 = sac.getArgumentRightJustified(32);
        // Accounts 6
        ScriptData apatValue6Present = sac.getArgument(1);
        ScriptData apatValue6 = sac.getArgumentRightJustified(32);

        // Foreign App
        ScriptData apfaPresent = sac.getArgument(1);
        // Foreign App 1
        ScriptData apfaValue1Present = sac.getArgument(1);
        ScriptData apfaValue1 = sac.getArgumentRightJustified(8);
        // Foreign App 2
        ScriptData apfaValue2Present = sac.getArgument(1);
        ScriptData apfaValue2 = sac.getArgumentRightJustified(8);
        // Foreign App 3
        ScriptData apfaValue3Present = sac.getArgument(1);
        ScriptData apfaValue3 = sac.getArgumentRightJustified(8);
        // Foreign App 4
        ScriptData apfaValue4Present = sac.getArgument(1);
        ScriptData apfaValue4 = sac.getArgumentRightJustified(8);
        // Foreign App 5
        ScriptData apfaValue5Present = sac.getArgument(1);
        ScriptData apfaValue5 = sac.getArgumentRightJustified(8);
        // Foreign App 6
        ScriptData apfaValue6Present = sac.getArgument(1);
        ScriptData apfaValue6 = sac.getArgumentRightJustified(8);

        // Application ID
        ScriptData apidPresent = sac.getArgument(1);
        ScriptData apidValue = sac.getArgumentRightJustified(8);

        // Fee
        ScriptData feePresent = sac.getArgument(1);
        ScriptData feeValue = sac.getArgumentRightJustified(8);
        // First Valid
        ScriptData fvPresent = sac.getArgument(1);
        ScriptData fvValue = sac.getArgumentRightJustified(8);
        // GenesisID
        ScriptData genPresent = sac.getArgument(1);
        ScriptData genValue = sac.getArgumentRightJustified(32);
        // Group
        ScriptData grpPresent = sac.getArgument(1);
        ScriptData grpValue = sac.getArgumentRightJustified(32);
        // Genesis Hash
        ScriptData ghPresent = sac.getArgument(1);
        ScriptData ghValue = sac.getArgumentRightJustified(32);
        // Last Valid
        ScriptData lvPresent = sac.getArgument(1);
        ScriptData lvValue = sac.getArgumentRightJustified(8);
        // Lease
        ScriptData lxPresent = sac.getArgument(1);
        ScriptData lxValue = sac.getArgumentRightJustified(32);
        // Note
        ScriptData notePresent = sac.getArgument(1);
        ScriptData noteValue = sac.getArgumentRightJustified(1024);
        // Rekey To
        ScriptData rekeyPresent = sac.getArgument(1);
        ScriptData rekeyValue = sac.getArgumentRightJustified(32);
        // Sender
        ScriptData sndPresent = sac.getArgument(1);
        ScriptData sndValue = sac.getArgumentRightJustified(32);
        // Type
        ScriptData typePresent = sac.getArgument(1);
        ScriptData typeValue = sac.getArgumentRightJustified(8);

        ScriptAssembler scriptAsb = new ScriptAssembler();
        String script = scriptAsb.setCoinType(0x11B)
                .arrayPointer()
                .copyString(TX)
                .arrayPointer()
                .ifEqual(apaaPresent, "00",
                        "", new ScriptAssembler()
                                .messagePack(Hex.encode("apaa".getBytes()), ScriptData.Buffer.TRANSACTION)
                                .arrayPointer()
                                .ifEqual(apaaValue1Present, "00",
                                        "", new ScriptAssembler().messagePack(ScriptAssembler.typeBinary, apaaValue1, ScriptData.Buffer.TRANSACTION).getScript())
                                .ifEqual(apaaValue2Present, "00",
                                        "", new ScriptAssembler().messagePack(ScriptAssembler.typeBinary, apaaValue2, ScriptData.Buffer.TRANSACTION).getScript())
                                .ifEqual(apaaValue3Present, "00",
                                        "", new ScriptAssembler().messagePack(ScriptAssembler.typeBinary, apaaValue3, ScriptData.Buffer.TRANSACTION).getScript())
                                .ifEqual(apaaValue4Present, "00",
                                        "", new ScriptAssembler().messagePack(ScriptAssembler.typeBinary, apaaValue4, ScriptData.Buffer.TRANSACTION).getScript())
                                .ifEqual(apaaValue5Present, "00",
                                        "", new ScriptAssembler().messagePack(ScriptAssembler.typeBinary, apaaValue5, ScriptData.Buffer.TRANSACTION).getScript())
                                .ifEqual(apaaValue6Present, "00",
                                        "", new ScriptAssembler().messagePack(ScriptAssembler.typeBinary, apaaValue6, ScriptData.Buffer.TRANSACTION).getScript())
                                .arrayEnd(TYPE_MESSAGE_PACK_ARRAY)
                                .getScript())
                .ifEqual(apanPresent, "00",
                        "", new ScriptAssembler().messagePack(Hex.encode("apan".getBytes()), ScriptData.Buffer.TRANSACTION)
                                .messagePack(ScriptAssembler.typeInt, apanValue, ScriptData.Buffer.TRANSACTION).getScript())
                .ifEqual(apasPresent, "00",
                        "", new ScriptAssembler()
                                .messagePack(Hex.encode("apas".getBytes()), ScriptData.Buffer.TRANSACTION)
                                .arrayPointer()
                                .ifEqual(apasValue1Present, "00",
                                        "", new ScriptAssembler().messagePack(ScriptAssembler.typeInt, apasValue1, ScriptData.Buffer.TRANSACTION).getScript())
                                .ifEqual(apasValue2Present, "00",
                                        "", new ScriptAssembler().messagePack(ScriptAssembler.typeInt, apasValue2, ScriptData.Buffer.TRANSACTION).getScript())
                                .ifEqual(apasValue3Present, "00",
                                        "", new ScriptAssembler().messagePack(ScriptAssembler.typeInt, apasValue3, ScriptData.Buffer.TRANSACTION).getScript())
                                .ifEqual(apasValue4Present, "00",
                                        "", new ScriptAssembler().messagePack(ScriptAssembler.typeInt, apasValue4, ScriptData.Buffer.TRANSACTION).getScript())
                                .ifEqual(apasValue5Present, "00",
                                        "", new ScriptAssembler().messagePack(ScriptAssembler.typeInt, apasValue5, ScriptData.Buffer.TRANSACTION).getScript())
                                .ifEqual(apasValue6Present, "00",
                                        "", new ScriptAssembler().messagePack(ScriptAssembler.typeInt, apasValue6, ScriptData.Buffer.TRANSACTION).getScript())
                                .arrayEnd(TYPE_MESSAGE_PACK_ARRAY)
                                .getScript())
                .ifEqual(apatPresent, "00",
                        "", new ScriptAssembler()
                                .messagePack(Hex.encode("apat".getBytes()), ScriptData.Buffer.TRANSACTION)
                                .arrayPointer()
                                .ifEqual(apatValue1Present, "00",
                                        "", new ScriptAssembler().messagePack(ScriptAssembler.typeBinary, apatValue1, ScriptData.Buffer.TRANSACTION).getScript())
                                .ifEqual(apatValue2Present, "00",
                                        "", new ScriptAssembler().messagePack(ScriptAssembler.typeBinary, apatValue2, ScriptData.Buffer.TRANSACTION).getScript())
                                .ifEqual(apatValue3Present, "00",
                                        "", new ScriptAssembler().messagePack(ScriptAssembler.typeBinary, apatValue3, ScriptData.Buffer.TRANSACTION).getScript())
                                .ifEqual(apatValue4Present, "00",
                                        "", new ScriptAssembler().messagePack(ScriptAssembler.typeBinary, apatValue4, ScriptData.Buffer.TRANSACTION).getScript())
                                .ifEqual(apatValue5Present, "00",
                                        "", new ScriptAssembler().messagePack(ScriptAssembler.typeBinary, apatValue5, ScriptData.Buffer.TRANSACTION).getScript())
                                .ifEqual(apatValue6Present, "00",
                                        "", new ScriptAssembler().messagePack(ScriptAssembler.typeBinary, apatValue6, ScriptData.Buffer.TRANSACTION).getScript())
                                .arrayEnd(TYPE_MESSAGE_PACK_ARRAY)
                                .getScript())
                .ifEqual(apfaPresent, "00",
                        "", new ScriptAssembler()
                                .messagePack(Hex.encode("apfa".getBytes()), ScriptData.Buffer.TRANSACTION)
                                .arrayPointer()
                                .ifEqual(apfaValue1Present, "00",
                                        "", new ScriptAssembler().messagePack(ScriptAssembler.typeInt, apfaValue1, ScriptData.Buffer.TRANSACTION).getScript())
                                .ifEqual(apfaValue2Present, "00",
                                        "", new ScriptAssembler().messagePack(ScriptAssembler.typeInt, apfaValue2, ScriptData.Buffer.TRANSACTION).getScript())
                                .ifEqual(apfaValue3Present, "00",
                                        "", new ScriptAssembler().messagePack(ScriptAssembler.typeInt, apfaValue3, ScriptData.Buffer.TRANSACTION).getScript())
                                .ifEqual(apfaValue4Present, "00",
                                        "", new ScriptAssembler().messagePack(ScriptAssembler.typeInt, apfaValue4, ScriptData.Buffer.TRANSACTION).getScript())
                                .ifEqual(apfaValue5Present, "00",
                                        "", new ScriptAssembler().messagePack(ScriptAssembler.typeInt, apfaValue5, ScriptData.Buffer.TRANSACTION).getScript())
                                .ifEqual(apfaValue6Present, "00",
                                        "", new ScriptAssembler().messagePack(ScriptAssembler.typeInt, apfaValue6, ScriptData.Buffer.TRANSACTION).getScript())
                                .arrayEnd(TYPE_MESSAGE_PACK_ARRAY)
                                .getScript())
                .ifEqual(apidPresent, "00",
                        "", new ScriptAssembler().messagePack(Hex.encode("apid".getBytes()), ScriptData.Buffer.TRANSACTION)
                                .messagePack(ScriptAssembler.typeInt, apidValue, ScriptData.Buffer.TRANSACTION).getScript())
                .ifEqual(feePresent, "00",
                        "", new ScriptAssembler().messagePack(Hex.encode("fee".getBytes()), ScriptData.Buffer.TRANSACTION)
                                .messagePack(ScriptAssembler.typeInt, feeValue, ScriptData.Buffer.TRANSACTION).getScript())
                .ifEqual(fvPresent, "00",
                        "", new ScriptAssembler().messagePack(Hex.encode("fv".getBytes()), ScriptData.Buffer.TRANSACTION)
                                .messagePack(ScriptAssembler.typeInt, fvValue, ScriptData.Buffer.TRANSACTION).getScript())
                .ifEqual(genPresent, "00",
                        "", new ScriptAssembler().messagePack(Hex.encode("gen".getBytes()), ScriptData.Buffer.TRANSACTION)
                                .messagePack(ScriptAssembler.typeString, genValue, ScriptData.Buffer.TRANSACTION).getScript())
                .ifEqual(grpPresent, "00",
                        "", new ScriptAssembler().messagePack(Hex.encode("grp".getBytes()), ScriptData.Buffer.TRANSACTION)
                                .messagePack(ScriptAssembler.typeBinary, grpValue, ScriptData.Buffer.TRANSACTION).getScript())
                .ifEqual(ghPresent, "00",
                        "", new ScriptAssembler().messagePack(Hex.encode("gh".getBytes()), ScriptData.Buffer.TRANSACTION)
                                .messagePack(ScriptAssembler.typeBinary, ghValue, ScriptData.Buffer.TRANSACTION).getScript())
                .ifEqual(lvPresent, "00",
                        "", new ScriptAssembler().messagePack(Hex.encode("lv".getBytes()), ScriptData.Buffer.TRANSACTION)
                                .messagePack(ScriptAssembler.typeInt, lvValue, ScriptData.Buffer.TRANSACTION).getScript())
                .ifEqual(lxPresent, "00",
                        "", new ScriptAssembler().messagePack(Hex.encode("lx".getBytes()), ScriptData.Buffer.TRANSACTION)
                                .messagePack(ScriptAssembler.typeBinary, lxValue, ScriptData.Buffer.TRANSACTION).getScript())
                .ifEqual(notePresent, "00",
                        "", new ScriptAssembler().messagePack(Hex.encode("note".getBytes()), ScriptData.Buffer.TRANSACTION)
                                .messagePack(ScriptAssembler.typeBinary, noteValue, ScriptData.Buffer.TRANSACTION).getScript())
                .ifEqual(rekeyPresent, "00",
                        "", new ScriptAssembler().messagePack(Hex.encode("rekey".getBytes()), ScriptData.Buffer.TRANSACTION)
                                .messagePack(ScriptAssembler.typeBinary, rekeyValue, ScriptData.Buffer.TRANSACTION).getScript())
                .ifEqual(sndPresent, "00",
                        "", new ScriptAssembler().messagePack(Hex.encode("snd".getBytes()), ScriptData.Buffer.TRANSACTION)
                                .messagePack(ScriptAssembler.typeBinary, sndValue, ScriptData.Buffer.TRANSACTION).getScript())
                .ifEqual(typePresent, "00",
                        "", new ScriptAssembler().messagePack(Hex.encode("type".getBytes()), ScriptData.Buffer.TRANSACTION)
                                .messagePack(ScriptAssembler.typeString, typeValue, ScriptData.Buffer.TRANSACTION).getScript())
                .arrayEnd(TYPE_MESSAGE_PACK_MAP)
                .showMessage("ALGO")
                .showMessage("appl")
                .ifEqual(apidPresent, "00",
                        "", new ScriptAssembler().showMessage("appID").baseConvert(apidValue, ScriptData.Buffer.CACHE1, 16, ScriptAssembler.decimalCharset, ScriptAssembler.zeroInherit)
                                .showMessage(ScriptData.getDataBufferAll(ScriptData.Buffer.CACHE1)).clearBuffer(ScriptData.Buffer.CACHE1).getScript())
                .showPressButton()
                .setHeader(ScriptAssembler.HashType.SHA512256, ScriptAssembler.SignType.ECDSA)
                .getScript();
        return script;
    }

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
                .setHeader(ScriptAssembler.HashType.SHA512256, ScriptAssembler.SignType.ECDSA)
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
                .setHeader(HashType.SHA512256, SignType.ECDSA)
                .getScript();
        return script;
    }

    public static String Signature = Strings.padEnd("FA", 144, '0');

}
