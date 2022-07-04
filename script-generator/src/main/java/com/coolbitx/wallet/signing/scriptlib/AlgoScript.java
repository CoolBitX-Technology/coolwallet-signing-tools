package com.coolbitx.wallet.signing.scriptlib;

import com.coolbitx.wallet.signing.utils.*;

import static com.coolbitx.wallet.signing.utils.ScriptAssembler.TYPE_MESSAGE_PACK_ARRAY;
import static com.coolbitx.wallet.signing.utils.ScriptAssembler.TYPE_MESSAGE_PACK_MAP;

public class AlgoScript {
    public static void listAll() {
        System.out.println("COIN-ALGO SCRIPTS");
        System.out.println("==================================================");
        System.out.println("Payment Transaction: \n" + getPaymentTransaction() + "\n");
        System.out.println("Payment Transaction rlp: \n" + getPaymentTransactionRlp() + "\n");
        System.out.println("Key Registration Transaction: \n" + getKeyRegistrationTransaction() + "\n");
        System.out.println("Key Registration Transaction rlp: \n" + getKeyRegistrationTransactionRlp() + "\n");
        System.out.println("Asset Config Transaction: \n" + getAssetConfigTransaction() + "\n");
        System.out.println("Asset Config Transaction rlp: \n" + getAssetConfigTransactionRlp() + "\n");
        System.out.println("Asset Transfer Transaction: \n" + getAssetTransferTransaction() + "\n");
        System.out.println("Asset Transfer Transaction rlp: \n" + getAssetTransferTransactionRlp() + "\n");
        System.out.println("Asset Freeze Transaction: \n" + getAssetFreezeTransaction() + "\n");
        System.out.println("Asset Freeze Transaction rlp: \n" + getAssetFreezeTransactionRlp() + "\n");
        System.out.println("Application Call Transaction rlp:\n" + getApplicationCallTransactionRlp() + "\n");
        System.out.println("Application Call Transaction 1: [NoOp - creation]\n" + getApplicationCallTransaction() + "\n");
        System.out.println("Application Call Transaction 2 [UpdateApplication]: \n" + getApplicationCallTransaction2() + "\n");
        System.out.println("Application Call Transaction 3: [NoOp - general, OptIn, CloseOut, DeleteApplication, ClearState]\n" + getApplicationCallTransaction3() + "\n");
        System.out.println("Application Call Transaction 3: [NoOp - general, OptIn, CloseOut, DeleteApplication, ClearState]\n" + getApplicationCallTransaction4() + "\n");
        System.out.println("==================================================");
    }

    public static String getPaymentTransaction() {
        String TX = HexUtil.toHexString("TX".getBytes());
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        // Amount
        ScriptData amtPresent = sac.getArgument(1);
        ScriptData amtValue = sac.getArgumentRightJustified(8);
        // Close
        ScriptData closePresent = sac.getArgument(1);
        ScriptData closeValue = sac.getArgumentRightJustified(32);
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
        // Receiver
        ScriptData rcvPresent = sac.getArgument(1);
        ScriptData rcvValue = sac.getArgumentRightJustified(32);
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
                .ifEqual(amtPresent, "00",
                        "", new ScriptAssembler().messagePack(Hex.encode("amt".getBytes()), ScriptData.Buffer.TRANSACTION)
                                .messagePack(ScriptAssembler.typeInt, amtValue, ScriptData.Buffer.TRANSACTION).getScript())
                .ifEqual(closePresent, "00",
                        "", new ScriptAssembler().messagePack(Hex.encode("close".getBytes()), ScriptData.Buffer.TRANSACTION)
                                .messagePack(ScriptAssembler.typeBinary, closeValue, ScriptData.Buffer.TRANSACTION).getScript())
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
                .ifEqual(rcvPresent, "00",
                        "", new ScriptAssembler().messagePack(Hex.encode("rcv".getBytes()), ScriptData.Buffer.TRANSACTION)
                                .messagePack(ScriptAssembler.typeBinary, rcvValue, ScriptData.Buffer.TRANSACTION).getScript())
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
                .showMessage("pay")
                .showAmount(amtValue, 6)
                .ifEqual(rcvPresent, "00",
                        "", new ScriptAssembler().hash(rcvValue, ScriptData.Buffer.CACHE1, ScriptAssembler.HashType.SHA512256)
                                .copyArgument(rcvValue, ScriptData.Buffer.CACHE2)
                                .copyArgument(ScriptData.getBuffer(ScriptData.Buffer.CACHE1, 28, 4), ScriptData.Buffer.CACHE2)
                                .clearBuffer(ScriptData.Buffer.CACHE1)
                                .copyString(HexUtil.toHexString("ABCDEFGHIJKLMNOPQRSTUVWXYZ234567"), ScriptData.Buffer.CACHE1)
                                .baseConvert(ScriptData.getBuffer(ScriptData.Buffer.CACHE2, 0, 36), ScriptData.Buffer.CACHE2, 58, ScriptAssembler.cache1Charset, ScriptAssembler.bitLeftJustify8to5)
                                .showAddress(ScriptData.getDataBufferAll(ScriptData.Buffer.CACHE2, 36))
                                .clearBuffer(ScriptData.Buffer.CACHE1)
                                .clearBuffer(ScriptData.Buffer.CACHE2).getScript())
                .showPressButton()
                .setHeader(ScriptAssembler.HashType.NONE, ScriptAssembler.SignType.EDDSA)
                .getScript();
        return script;
    }

    public static String getPaymentTransactionRlp() {
        String TX = HexUtil.toHexString("TX".getBytes());
        ScriptRlpArgumentComposer sac = new ScriptRlpArgumentComposer();
        // Amount
        ScriptRlpData amtValue = sac.getArgumentRlpItem();
        // Close
        ScriptRlpData closeValue = sac.getArgumentRlpItem();
        // Fee
        ScriptRlpData feeValue = sac.getArgumentRlpItem();
        // First Valid
        ScriptRlpData fvValue = sac.getArgumentRlpItem();
        // GenesisID
        ScriptRlpData genValue = sac.getArgumentRlpItem();
        // Group
        ScriptRlpData grpValue = sac.getArgumentRlpItem();
        // Genesis Hash
        ScriptRlpData ghValue = sac.getArgumentRlpItem();
        // Last Valid
        ScriptRlpData lvValue = sac.getArgumentRlpItem();
        // Lease
        ScriptRlpData lxValue = sac.getArgumentRlpItem();
        // Note
        ScriptRlpData noteValue = sac.getArgumentRlpItem();
        // Receiver
        ScriptRlpData rcvValue = sac.getArgumentRlpItem();
        // Rekey To
        ScriptRlpData rekeyValue = sac.getArgumentRlpItem();
        // Sender
        ScriptRlpData sndValue = sac.getArgumentRlpItem();
        // Type
        ScriptRlpData typeValue = sac.getArgumentRlpItem();

        ScriptAssembler scriptAsb = new ScriptAssembler();
        String script = scriptAsb.setCoinType(0x11B)
                .copyString(TX)
                .arrayPointer()
                .isEmpty(amtValue,
                        "", new ScriptAssembler().messagePack(Hex.encode("amt".getBytes()), ScriptData.Buffer.TRANSACTION)
                                .messagePack(ScriptAssembler.typeInt, amtValue, ScriptData.Buffer.TRANSACTION).getScript())
                .isEmpty(closeValue,
                        "", new ScriptAssembler().messagePack(Hex.encode("close".getBytes()), ScriptData.Buffer.TRANSACTION)
                                .messagePack(ScriptAssembler.typeBinary, closeValue, ScriptData.Buffer.TRANSACTION).getScript())
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
                .isEmpty(rcvValue,
                        "", new ScriptAssembler().messagePack(Hex.encode("rcv".getBytes()), ScriptData.Buffer.TRANSACTION)
                                .messagePack(ScriptAssembler.typeBinary, rcvValue, ScriptData.Buffer.TRANSACTION).getScript())
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
                .showMessage("pay")
                .showAmount(amtValue, 6)
                .isEmpty(rcvValue,
                        "", new ScriptAssembler().hash(rcvValue, ScriptData.Buffer.CACHE1, ScriptAssembler.HashType.SHA512256)
                                .copyArgument(rcvValue, ScriptData.Buffer.CACHE2)
                                .copyArgument(ScriptData.getBuffer(ScriptData.Buffer.CACHE1, 28, 4), ScriptData.Buffer.CACHE2)
                                .clearBuffer(ScriptData.Buffer.CACHE1)
                                .copyString(HexUtil.toHexString("ABCDEFGHIJKLMNOPQRSTUVWXYZ234567"), ScriptData.Buffer.CACHE1)
                                .baseConvert(ScriptData.getBuffer(ScriptData.Buffer.CACHE2, 0, 36), ScriptData.Buffer.CACHE2, 58, ScriptAssembler.cache1Charset, ScriptAssembler.bitLeftJustify8to5)
                                .showAddress(ScriptData.getDataBufferAll(ScriptData.Buffer.CACHE2, 36))
                                .clearBuffer(ScriptData.Buffer.CACHE1)
                                .clearBuffer(ScriptData.Buffer.CACHE2).getScript())
                .showPressButton()
                .setHeader(ScriptAssembler.HashType.NONE, ScriptAssembler.SignType.EDDSA)
                .getScript();
        return script;
    }

    public static String getKeyRegistrationTransaction() {
        String TX = HexUtil.toHexString("TX".getBytes());
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
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
        // Non Participation
        ScriptData nonpartPresent = sac.getArgument(1);
        ScriptData nonpartValue = sac.getArgument(1);
        // Note
        ScriptData notePresent = sac.getArgument(1);
        ScriptData noteValue = sac.getArgumentRightJustified(1024);
        // Rekey To
        ScriptData rekeyPresent = sac.getArgument(1);
        ScriptData rekeyValue = sac.getArgumentRightJustified(32);
        // Selection PK
        ScriptData selkeyPresent = sac.getArgument(1);
        ScriptData selkeyValue = sac.getArgumentRightJustified(32);
        // State Proof PK
        ScriptData sprfkeyPresent = sac.getArgument(1);
        ScriptData sprfkeyValue = sac.getArgumentRightJustified(64);
        // Sender
        ScriptData sndPresent = sac.getArgument(1);
        ScriptData sndValue = sac.getArgumentRightJustified(32);
        // Type
        ScriptData typePresent = sac.getArgument(1);
        ScriptData typeValue = sac.getArgumentRightJustified(8);
        // Vote First
        ScriptData votefstPresent = sac.getArgument(1);
        ScriptData votefstValue = sac.getArgumentRightJustified(8);
        // Vote Key Dilution
        ScriptData votekdPresent = sac.getArgument(1);
        ScriptData votekdValue = sac.getArgumentRightJustified(8);
        // Vote PK
        ScriptData votekeyPresent = sac.getArgument(1);
        ScriptData votekeyValue = sac.getArgumentRightJustified(32);
        // Vote Last
        ScriptData votelstPresent = sac.getArgument(1);
        ScriptData votelstValue = sac.getArgumentRightJustified(8);

        ScriptAssembler scriptAsb = new ScriptAssembler();
        String script = scriptAsb.setCoinType(0x11B)
                .arrayPointer()
                .copyString(TX)
                .arrayPointer()
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
                .ifEqual(nonpartPresent, "00",
                        "", new ScriptAssembler().messagePack(Hex.encode("nonpart".getBytes()), ScriptData.Buffer.TRANSACTION)
                                .messagePack(ScriptAssembler.typeBoolean, nonpartValue, ScriptData.Buffer.TRANSACTION).getScript())
                .ifEqual(lxPresent, "00",
                        "", new ScriptAssembler().messagePack(Hex.encode("lx".getBytes()), ScriptData.Buffer.TRANSACTION)
                                .messagePack(ScriptAssembler.typeBinary, lxValue, ScriptData.Buffer.TRANSACTION).getScript())
                .ifEqual(notePresent, "00",
                        "", new ScriptAssembler().messagePack(Hex.encode("note".getBytes()), ScriptData.Buffer.TRANSACTION)
                                .messagePack(ScriptAssembler.typeBinary, noteValue, ScriptData.Buffer.TRANSACTION).getScript())
                .ifEqual(rekeyPresent, "00",
                        "", new ScriptAssembler().messagePack(Hex.encode("rekey".getBytes()), ScriptData.Buffer.TRANSACTION)
                                .messagePack(ScriptAssembler.typeBinary, rekeyValue, ScriptData.Buffer.TRANSACTION).getScript())
                .ifEqual(selkeyPresent, "00",
                        "", new ScriptAssembler().messagePack(Hex.encode("selkey".getBytes()), ScriptData.Buffer.TRANSACTION)
                                .messagePack(ScriptAssembler.typeBinary, selkeyValue, ScriptData.Buffer.TRANSACTION).getScript())
                .ifEqual(sprfkeyPresent, "00",
                        "", new ScriptAssembler().messagePack(Hex.encode("sprfkey".getBytes()), ScriptData.Buffer.TRANSACTION)
                                .messagePack(ScriptAssembler.typeBinary, sprfkeyValue, ScriptData.Buffer.TRANSACTION).getScript())
                .ifEqual(sndPresent, "00",
                        "", new ScriptAssembler().messagePack(Hex.encode("snd".getBytes()), ScriptData.Buffer.TRANSACTION)
                                .messagePack(ScriptAssembler.typeBinary, sndValue, ScriptData.Buffer.TRANSACTION).getScript())
                .ifEqual(typePresent, "00",
                        "", new ScriptAssembler().messagePack(Hex.encode("type".getBytes()), ScriptData.Buffer.TRANSACTION)
                                .messagePack(ScriptAssembler.typeString, typeValue, ScriptData.Buffer.TRANSACTION).getScript())
                .ifEqual(votefstPresent, "00",
                        "", new ScriptAssembler().messagePack(Hex.encode("votefst".getBytes()), ScriptData.Buffer.TRANSACTION)
                                .messagePack(ScriptAssembler.typeInt, votefstValue, ScriptData.Buffer.TRANSACTION).getScript())
                .ifEqual(votekdPresent, "00",
                        "", new ScriptAssembler().messagePack(Hex.encode("votekd".getBytes()), ScriptData.Buffer.TRANSACTION)
                                .messagePack(ScriptAssembler.typeInt, votekdValue, ScriptData.Buffer.TRANSACTION).getScript())
                .ifEqual(votekeyPresent, "00",
                        "", new ScriptAssembler().messagePack(Hex.encode("votekey".getBytes()), ScriptData.Buffer.TRANSACTION)
                                .messagePack(ScriptAssembler.typeBinary, votekeyValue, ScriptData.Buffer.TRANSACTION).getScript())
                .ifEqual(votelstPresent, "00",
                        "", new ScriptAssembler().messagePack(Hex.encode("votelst".getBytes()), ScriptData.Buffer.TRANSACTION)
                                .messagePack(ScriptAssembler.typeInt, votelstValue, ScriptData.Buffer.TRANSACTION).getScript())
                .arrayEnd(TYPE_MESSAGE_PACK_MAP)
                .showMessage("ALGO")
                .showMessage("keyreg")
                .showPressButton()
                .setHeader(ScriptAssembler.HashType.NONE, ScriptAssembler.SignType.EDDSA)
                .getScript();
        return script;
    }

    public static String getKeyRegistrationTransactionRlp() {
        String TX = HexUtil.toHexString("TX".getBytes());
        ScriptRlpArgumentComposer sac = new ScriptRlpArgumentComposer();
        // Fee
        ScriptRlpData feeValue = sac.getArgumentRlpItem();
        // First Valid
        ScriptRlpData fvValue = sac.getArgumentRlpItem();
        // GenesisID
        ScriptRlpData genValue = sac.getArgumentRlpItem();
        // Group
        ScriptRlpData grpValue = sac.getArgumentRlpItem();
        // Genesis Hash
        ScriptRlpData ghValue = sac.getArgumentRlpItem();
        // Last Valid
        ScriptRlpData lvValue = sac.getArgumentRlpItem();
        // Lease
        ScriptRlpData lxValue = sac.getArgumentRlpItem();
        // Non Participation
        ScriptRlpData nonpartValue = sac.getArgumentRlpItem();
        // Note
        ScriptRlpData noteValue = sac.getArgumentRlpItem();
        // Rekey To
        ScriptRlpData rekeyValue = sac.getArgumentRlpItem();
        // Selection PK
        ScriptRlpData selkeyValue = sac.getArgumentRlpItem();
        // State Proof PK
        ScriptRlpData sprfkeyValue = sac.getArgumentRlpItem();
        // Sender
        ScriptRlpData sndValue = sac.getArgumentRlpItem();
        // Type
        ScriptRlpData typeValue = sac.getArgumentRlpItem();
        // Vote First
        ScriptRlpData votefstValue = sac.getArgumentRlpItem();
        // Vote Key Dilution
        ScriptRlpData votekdValue = sac.getArgumentRlpItem();
        // Vote PK
        ScriptRlpData votekeyValue = sac.getArgumentRlpItem();
        // Vote Last
        ScriptRlpData votelstValue = sac.getArgumentRlpItem();

        ScriptAssembler scriptAsb = new ScriptAssembler();
        String script = scriptAsb.setCoinType(0x11B)
                .copyString(TX)
                .arrayPointer()
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
                .isEmpty(nonpartValue,
                        "", new ScriptAssembler().messagePack(Hex.encode("nonpart".getBytes()), ScriptData.Buffer.TRANSACTION)
                                .messagePack(ScriptAssembler.typeBoolean, nonpartValue, ScriptData.Buffer.TRANSACTION).getScript())
                .isEmpty(lxValue,
                        "", new ScriptAssembler().messagePack(Hex.encode("lx".getBytes()), ScriptData.Buffer.TRANSACTION)
                                .messagePack(ScriptAssembler.typeBinary, lxValue, ScriptData.Buffer.TRANSACTION).getScript())
                .isEmpty(noteValue,
                        "", new ScriptAssembler().messagePack(Hex.encode("note".getBytes()), ScriptData.Buffer.TRANSACTION)
                                .messagePack(ScriptAssembler.typeBinary, noteValue, ScriptData.Buffer.TRANSACTION).getScript())
                .isEmpty(rekeyValue,
                        "", new ScriptAssembler().messagePack(Hex.encode("rekey".getBytes()), ScriptData.Buffer.TRANSACTION)
                                .messagePack(ScriptAssembler.typeBinary, rekeyValue, ScriptData.Buffer.TRANSACTION).getScript())
                .isEmpty(selkeyValue,
                        "", new ScriptAssembler().messagePack(Hex.encode("selkey".getBytes()), ScriptData.Buffer.TRANSACTION)
                                .messagePack(ScriptAssembler.typeBinary, selkeyValue, ScriptData.Buffer.TRANSACTION).getScript())
                .isEmpty(sprfkeyValue,
                        "", new ScriptAssembler().messagePack(Hex.encode("sprfkey".getBytes()), ScriptData.Buffer.TRANSACTION)
                                .messagePack(ScriptAssembler.typeBinary, sprfkeyValue, ScriptData.Buffer.TRANSACTION).getScript())
                .isEmpty(sndValue,
                        "", new ScriptAssembler().messagePack(Hex.encode("snd".getBytes()), ScriptData.Buffer.TRANSACTION)
                                .messagePack(ScriptAssembler.typeBinary, sndValue, ScriptData.Buffer.TRANSACTION).getScript())
                .isEmpty(typeValue,
                        "", new ScriptAssembler().messagePack(Hex.encode("type".getBytes()), ScriptData.Buffer.TRANSACTION)
                                .messagePack(ScriptAssembler.typeString, typeValue, ScriptData.Buffer.TRANSACTION).getScript())
                .isEmpty(votefstValue,
                        "", new ScriptAssembler().messagePack(Hex.encode("votefst".getBytes()), ScriptData.Buffer.TRANSACTION)
                                .messagePack(ScriptAssembler.typeInt, votefstValue, ScriptData.Buffer.TRANSACTION).getScript())
                .isEmpty(votekdValue,
                        "", new ScriptAssembler().messagePack(Hex.encode("votekd".getBytes()), ScriptData.Buffer.TRANSACTION)
                                .messagePack(ScriptAssembler.typeInt, votekdValue, ScriptData.Buffer.TRANSACTION).getScript())
                .isEmpty(votekeyValue,
                        "", new ScriptAssembler().messagePack(Hex.encode("votekey".getBytes()), ScriptData.Buffer.TRANSACTION)
                                .messagePack(ScriptAssembler.typeBinary, votekeyValue, ScriptData.Buffer.TRANSACTION).getScript())
                .isEmpty(votelstValue,
                        "", new ScriptAssembler().messagePack(Hex.encode("votelst".getBytes()), ScriptData.Buffer.TRANSACTION)
                                .messagePack(ScriptAssembler.typeInt, votelstValue, ScriptData.Buffer.TRANSACTION).getScript())
                .arrayEnd(TYPE_MESSAGE_PACK_MAP)
                .showMessage("ALGO")
                .showMessage("keyreg")
                .showPressButton()
                .setHeader(ScriptAssembler.HashType.NONE, ScriptAssembler.SignType.EDDSA)
                .getScript();
        return script;
    }

    public static String getAssetConfigTransaction() {
        String TX = HexUtil.toHexString("TX".getBytes());
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        // Asset Params
        ScriptData aparPresent = sac.getArgument(1);
        // MetaDataHash
        ScriptData amPresent = sac.getArgument(1);
        ScriptData amValue = sac.getArgumentRightJustified(32);
        // Asset Name
        ScriptData anPresent = sac.getArgument(1);
        ScriptData anValue = sac.getArgumentRightJustified(32);
        // URL
        ScriptData auPresent = sac.getArgument(1);
        ScriptData auValue = sac.getArgumentRightJustified(96);
        // Clawback Address
        ScriptData cPresent = sac.getArgument(1);
        ScriptData cValue = sac.getArgumentRightJustified(32);
        // Decimals
        ScriptData dcPresent = sac.getArgument(1);
        ScriptData dcValue = sac.getArgumentRightJustified(8);
        // Default Frozen
        ScriptData dfPresent = sac.getArgument(1);
        ScriptData dfValue = sac.getArgument(1);
        // Freeze Address
        ScriptData fPresent = sac.getArgument(1);
        ScriptData fValue = sac.getArgumentRightJustified(32);
        // Manager Address
        ScriptData mPresent = sac.getArgument(1);
        ScriptData mValue = sac.getArgumentRightJustified(32);
        // Reserve Address
        ScriptData rPresent = sac.getArgument(1);
        ScriptData rValue = sac.getArgumentRightJustified(32);
        // Total
        ScriptData tPresent = sac.getArgument(1);
        ScriptData tValue = sac.getArgumentRightJustified(8);
        // Unit Name
        ScriptData unPresent = sac.getArgument(1);
        ScriptData unValue = sac.getArgumentRightJustified(8);
        // Config Asset
        ScriptData caidPresent = sac.getArgument(1);
        ScriptData caidValue = sac.getArgumentRightJustified(8);
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
                .ifEqual(aparPresent, "00",
                        "", new ScriptAssembler()
                                .messagePack(Hex.encode("apar".getBytes()), ScriptData.Buffer.TRANSACTION)
                                .arrayPointer()
                                .ifEqual(amPresent, "00",
                                        "", new ScriptAssembler().messagePack(Hex.encode("am".getBytes()), ScriptData.Buffer.TRANSACTION)
                                                .messagePack(ScriptAssembler.typeBinary, amValue, ScriptData.Buffer.TRANSACTION).getScript())
                                .ifEqual(anPresent, "00",
                                        "", new ScriptAssembler().messagePack(Hex.encode("an".getBytes()), ScriptData.Buffer.TRANSACTION)
                                                .messagePack(ScriptAssembler.typeString, anValue, ScriptData.Buffer.TRANSACTION).getScript())
                                .ifEqual(auPresent, "00",
                                        "", new ScriptAssembler().messagePack(Hex.encode("au".getBytes()), ScriptData.Buffer.TRANSACTION)
                                                .messagePack(ScriptAssembler.typeString, auValue, ScriptData.Buffer.TRANSACTION).getScript())
                                .ifEqual(cPresent, "00",
                                        "", new ScriptAssembler().messagePack(Hex.encode("c".getBytes()), ScriptData.Buffer.TRANSACTION)
                                                .messagePack(ScriptAssembler.typeBinary, cValue, ScriptData.Buffer.TRANSACTION).getScript())
                                .ifEqual(dcPresent, "00",
                                        "", new ScriptAssembler().messagePack(Hex.encode("dc".getBytes()), ScriptData.Buffer.TRANSACTION)
                                                .messagePack(ScriptAssembler.typeInt, dcValue, ScriptData.Buffer.TRANSACTION).getScript())
                                .ifEqual(dfPresent, "00",
                                        "", new ScriptAssembler().messagePack(Hex.encode("df".getBytes()), ScriptData.Buffer.TRANSACTION)
                                                .messagePack(ScriptAssembler.typeBoolean, dfValue, ScriptData.Buffer.TRANSACTION).getScript())
                                .ifEqual(fPresent, "00",
                                        "", new ScriptAssembler().messagePack(Hex.encode("f".getBytes()), ScriptData.Buffer.TRANSACTION)
                                                .messagePack(ScriptAssembler.typeBinary, fValue, ScriptData.Buffer.TRANSACTION).getScript())
                                .ifEqual(mPresent, "00",
                                        "", new ScriptAssembler().messagePack(Hex.encode("m".getBytes()), ScriptData.Buffer.TRANSACTION)
                                                .messagePack(ScriptAssembler.typeBinary, mValue, ScriptData.Buffer.TRANSACTION).getScript())
                                .ifEqual(rPresent, "00",
                                        "", new ScriptAssembler().messagePack(Hex.encode("r".getBytes()), ScriptData.Buffer.TRANSACTION)
                                                .messagePack(ScriptAssembler.typeBinary, rValue, ScriptData.Buffer.TRANSACTION).getScript())
                                .ifEqual(tPresent, "00",
                                        "", new ScriptAssembler().messagePack(Hex.encode("t".getBytes()), ScriptData.Buffer.TRANSACTION)
                                                .messagePack(ScriptAssembler.typeInt, tValue, ScriptData.Buffer.TRANSACTION).getScript())
                                .ifEqual(unPresent, "00",
                                        "", new ScriptAssembler().messagePack(Hex.encode("un".getBytes()), ScriptData.Buffer.TRANSACTION)
                                                .messagePack(ScriptAssembler.typeString, unValue, ScriptData.Buffer.TRANSACTION).getScript())
                                .arrayEnd(TYPE_MESSAGE_PACK_MAP)
                                .getScript())
                .ifEqual(caidPresent, "00",
                        "", new ScriptAssembler().messagePack(Hex.encode("caid".getBytes()), ScriptData.Buffer.TRANSACTION)
                                .messagePack(ScriptAssembler.typeInt, caidValue, ScriptData.Buffer.TRANSACTION).getScript())
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
                .showMessage("acfg")
                .showPressButton()
                .setHeader(ScriptAssembler.HashType.NONE, ScriptAssembler.SignType.EDDSA)
                .getScript();
        return script;
    }

    public static String getAssetConfigTransactionRlp() {
        String TX = HexUtil.toHexString("TX".getBytes());
        ScriptRlpArgumentComposer sac = new ScriptRlpArgumentComposer();
        // Asset Params
        ScriptRlpData aparPresent = sac.getArgumentRlpItem();
        // MetaDataHash
        ScriptRlpData amValue = sac.getArgumentRlpItem();
        // Asset Name
        ScriptRlpData anValue = sac.getArgumentRlpItem();
        // URL
        ScriptRlpData auValue = sac.getArgumentRlpItem();
        // Clawback Address
        ScriptRlpData cValue = sac.getArgumentRlpItem();
        // Decimals
        ScriptRlpData dcValue = sac.getArgumentRlpItem();
        // Default Frozen
        ScriptRlpData dfValue = sac.getArgumentRlpItem();
        // Freeze Address
        ScriptRlpData fValue = sac.getArgumentRlpItem();
        // Manager Address
        ScriptRlpData mValue = sac.getArgumentRlpItem();
        // Reserve Address
        ScriptRlpData rValue = sac.getArgumentRlpItem();
        // Total
        ScriptRlpData tValue = sac.getArgumentRlpItem();
        // Unit Name
        ScriptRlpData unValue = sac.getArgumentRlpItem();
        // Config Asset
        ScriptRlpData caidValue = sac.getArgumentRlpItem();
        // Fee
        ScriptRlpData feeValue = sac.getArgumentRlpItem();
        // First Valid
        ScriptRlpData fvValue = sac.getArgumentRlpItem();
        // GenesisID
        ScriptRlpData genValue = sac.getArgumentRlpItem();
        // Group
        ScriptRlpData grpValue = sac.getArgumentRlpItem();
        // Genesis Hash
        ScriptRlpData ghValue = sac.getArgumentRlpItem();
        // Last Valid
        ScriptRlpData lvValue = sac.getArgumentRlpItem();
        // Lease
        ScriptRlpData lxValue = sac.getArgumentRlpItem();
        // Note
        ScriptRlpData noteValue = sac.getArgumentRlpItem();
        // Rekey To
        ScriptRlpData rekeyValue = sac.getArgumentRlpItem();
        // Sender
        ScriptRlpData sndValue = sac.getArgumentRlpItem();
        // Type
        ScriptRlpData typeValue = sac.getArgumentRlpItem();

        ScriptAssembler scriptAsb = new ScriptAssembler();
        String script = scriptAsb.setCoinType(0x11B)
                .copyString(TX)
                .arrayPointer()
                .isEmpty(aparPresent,
                        "", new ScriptAssembler()
                                .messagePack(Hex.encode("apar".getBytes()), ScriptData.Buffer.TRANSACTION)
                                .arrayPointer()
                                .isEmpty(amValue,
                                        "", new ScriptAssembler().messagePack(Hex.encode("am".getBytes()), ScriptData.Buffer.TRANSACTION)
                                                .messagePack(ScriptAssembler.typeBinary, amValue, ScriptData.Buffer.TRANSACTION).getScript())
                                .isEmpty(anValue,
                                        "", new ScriptAssembler().messagePack(Hex.encode("an".getBytes()), ScriptData.Buffer.TRANSACTION)
                                                .messagePack(ScriptAssembler.typeString, anValue, ScriptData.Buffer.TRANSACTION).getScript())
                                .isEmpty(auValue,
                                        "", new ScriptAssembler().messagePack(Hex.encode("au".getBytes()), ScriptData.Buffer.TRANSACTION)
                                                .messagePack(ScriptAssembler.typeString, auValue, ScriptData.Buffer.TRANSACTION).getScript())
                                .isEmpty(cValue,
                                        "", new ScriptAssembler().messagePack(Hex.encode("c".getBytes()), ScriptData.Buffer.TRANSACTION)
                                                .messagePack(ScriptAssembler.typeBinary, cValue, ScriptData.Buffer.TRANSACTION).getScript())
                                .isEmpty(dcValue,
                                        "", new ScriptAssembler().messagePack(Hex.encode("dc".getBytes()), ScriptData.Buffer.TRANSACTION)
                                                .messagePack(ScriptAssembler.typeInt, dcValue, ScriptData.Buffer.TRANSACTION).getScript())
                                .isEmpty(dfValue,
                                        "", new ScriptAssembler().messagePack(Hex.encode("df".getBytes()), ScriptData.Buffer.TRANSACTION)
                                                .messagePack(ScriptAssembler.typeBoolean, dfValue, ScriptData.Buffer.TRANSACTION).getScript())
                                .isEmpty(fValue,
                                        "", new ScriptAssembler().messagePack(Hex.encode("f".getBytes()), ScriptData.Buffer.TRANSACTION)
                                                .messagePack(ScriptAssembler.typeBinary, fValue, ScriptData.Buffer.TRANSACTION).getScript())
                                .isEmpty(mValue,
                                        "", new ScriptAssembler().messagePack(Hex.encode("m".getBytes()), ScriptData.Buffer.TRANSACTION)
                                                .messagePack(ScriptAssembler.typeBinary, mValue, ScriptData.Buffer.TRANSACTION).getScript())
                                .isEmpty(rValue,
                                        "", new ScriptAssembler().messagePack(Hex.encode("r".getBytes()), ScriptData.Buffer.TRANSACTION)
                                                .messagePack(ScriptAssembler.typeBinary, rValue, ScriptData.Buffer.TRANSACTION).getScript())
                                .isEmpty(tValue,
                                        "", new ScriptAssembler().messagePack(Hex.encode("t".getBytes()), ScriptData.Buffer.TRANSACTION)
                                                .messagePack(ScriptAssembler.typeInt, tValue, ScriptData.Buffer.TRANSACTION).getScript())
                                .isEmpty(unValue,
                                        "", new ScriptAssembler().messagePack(Hex.encode("un".getBytes()), ScriptData.Buffer.TRANSACTION)
                                                .messagePack(ScriptAssembler.typeString, unValue, ScriptData.Buffer.TRANSACTION).getScript())
                                .arrayEnd(TYPE_MESSAGE_PACK_MAP)
                                .getScript())
                .isEmpty(caidValue,
                        "", new ScriptAssembler().messagePack(Hex.encode("caid".getBytes()), ScriptData.Buffer.TRANSACTION)
                                .messagePack(ScriptAssembler.typeInt, caidValue, ScriptData.Buffer.TRANSACTION).getScript())
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
                .showMessage("acfg")
                .showPressButton()
                .setHeader(ScriptAssembler.HashType.NONE, ScriptAssembler.SignType.EDDSA)
                .getScript();
        return script;
    }

    public static String getAssetTransferTransaction() {
        String TX = HexUtil.toHexString("TX".getBytes());
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        // Asset Amount
        ScriptData aamtPresent = sac.getArgument(1);
        ScriptData aamtValue = sac.getArgumentRightJustified(8);
        // Asset Close To
        ScriptData aclosePresent = sac.getArgument(1);
        ScriptData acloseValue = sac.getArgumentRightJustified(32);
        // Asset Receiver
        ScriptData arcvPresent = sac.getArgument(1);
        ScriptData arcvValue = sac.getArgumentRightJustified(32);
        // Asset Sender
        ScriptData asndPresent = sac.getArgument(1);
        ScriptData asndValue = sac.getArgumentRightJustified(32);
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
        // Xfer Asset
        ScriptData xaidPresent = sac.getArgument(1);
        ScriptData xaidValue = sac.getArgumentRightJustified(8);

        ScriptAssembler scriptAsb = new ScriptAssembler();
        String script = scriptAsb.setCoinType(0x11B)
                .arrayPointer()
                .copyString(TX)
                .arrayPointer()
                .ifEqual(aamtPresent, "00",
                        "", new ScriptAssembler().messagePack(Hex.encode("aamt".getBytes()), ScriptData.Buffer.TRANSACTION)
                                .messagePack(ScriptAssembler.typeInt, aamtValue, ScriptData.Buffer.TRANSACTION).getScript())
                .ifEqual(aclosePresent, "00",
                        "", new ScriptAssembler().messagePack(Hex.encode("aclose".getBytes()), ScriptData.Buffer.TRANSACTION)
                                .messagePack(ScriptAssembler.typeBinary, acloseValue, ScriptData.Buffer.TRANSACTION).getScript())
                .ifEqual(arcvPresent, "00",
                        "", new ScriptAssembler().messagePack(Hex.encode("arcv".getBytes()), ScriptData.Buffer.TRANSACTION)
                                .messagePack(ScriptAssembler.typeBinary, arcvValue, ScriptData.Buffer.TRANSACTION).getScript())
                .ifEqual(asndPresent, "00",
                        "", new ScriptAssembler().messagePack(Hex.encode("asnd".getBytes()), ScriptData.Buffer.TRANSACTION)
                                .messagePack(ScriptAssembler.typeBinary, asndValue, ScriptData.Buffer.TRANSACTION).getScript())
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
                .ifEqual(xaidPresent, "00",
                        "", new ScriptAssembler().messagePack(Hex.encode("xaid".getBytes()), ScriptData.Buffer.TRANSACTION)
                                .messagePack(ScriptAssembler.typeInt, xaidValue, ScriptData.Buffer.TRANSACTION).getScript())
                .arrayEnd(TYPE_MESSAGE_PACK_MAP)
                .showMessage("ALGO")
                .showMessage("axfer")
                .ifEqual(xaidPresent, "00",
                        "", new ScriptAssembler().showMessage("assetID").baseConvert(xaidValue, ScriptData.Buffer.CACHE1, 16, ScriptAssembler.decimalCharset, ScriptAssembler.zeroInherit)
                                .showMessage(ScriptData.getDataBufferAll(ScriptData.Buffer.CACHE1)).clearBuffer(ScriptData.Buffer.CACHE1).getScript())
                .ifEqual(arcvPresent, "00",
                        "", new ScriptAssembler().hash(arcvValue, ScriptData.Buffer.CACHE1, ScriptAssembler.HashType.SHA512256)
                                .copyArgument(arcvValue, ScriptData.Buffer.CACHE2)
                                .copyArgument(ScriptData.getBuffer(ScriptData.Buffer.CACHE1, 28, 4), ScriptData.Buffer.CACHE2)
                                .clearBuffer(ScriptData.Buffer.CACHE1)
                                .copyString(HexUtil.toHexString("ABCDEFGHIJKLMNOPQRSTUVWXYZ234567"), ScriptData.Buffer.CACHE1)
                                .baseConvert(ScriptData.getBuffer(ScriptData.Buffer.CACHE2, 0, 36), ScriptData.Buffer.CACHE2, 58, ScriptAssembler.cache1Charset, ScriptAssembler.bitLeftJustify8to5)
                                .showAddress(ScriptData.getDataBufferAll(ScriptData.Buffer.CACHE2, 36))
                                .clearBuffer(ScriptData.Buffer.CACHE1)
                                .clearBuffer(ScriptData.Buffer.CACHE2).getScript())
                .showPressButton()
                .setHeader(ScriptAssembler.HashType.NONE, ScriptAssembler.SignType.EDDSA)
                .getScript();
        return script;
    }

    public static String getAssetTransferTransactionRlp() {
        String TX = HexUtil.toHexString("TX".getBytes());
        ScriptRlpArgumentComposer sac = new ScriptRlpArgumentComposer();
        // Asset Amount
        ScriptRlpData aamtValue = sac.getArgumentRlpItem();
        // Asset Close To
        ScriptRlpData acloseValue = sac.getArgumentRlpItem();
        // Asset Receiver
        ScriptRlpData arcvValue = sac.getArgumentRlpItem();
        // Asset Sender
        ScriptRlpData asndValue = sac.getArgumentRlpItem();
        // Fee
        ScriptRlpData feeValue = sac.getArgumentRlpItem();
        // First Valid
        ScriptRlpData fvValue = sac.getArgumentRlpItem();
        // GenesisID
        ScriptRlpData genValue = sac.getArgumentRlpItem();
        // Group
        ScriptRlpData grpValue = sac.getArgumentRlpItem();
        // Genesis Hash
        ScriptRlpData ghValue = sac.getArgumentRlpItem();
        // Last Valid
        ScriptRlpData lvValue = sac.getArgumentRlpItem();
        // Lease
        ScriptRlpData lxValue = sac.getArgumentRlpItem();
        // Note
        ScriptRlpData noteValue = sac.getArgumentRlpItem();
        // Rekey To
        ScriptRlpData rekeyValue = sac.getArgumentRlpItem();
        // Sender
        ScriptRlpData sndValue = sac.getArgumentRlpItem();
        // Type
        ScriptRlpData typeValue = sac.getArgumentRlpItem();
        // Xfer Asset
        ScriptRlpData xaidValue = sac.getArgumentRlpItem();

        ScriptAssembler scriptAsb = new ScriptAssembler();
        String script = scriptAsb.setCoinType(0x11B)
                .copyString(TX)
                .arrayPointer()
                .isEmpty(aamtValue,
                        "", new ScriptAssembler().messagePack(Hex.encode("aamt".getBytes()), ScriptData.Buffer.TRANSACTION)
                                .messagePack(ScriptAssembler.typeInt, aamtValue, ScriptData.Buffer.TRANSACTION).getScript())
                .isEmpty(acloseValue,
                        "", new ScriptAssembler().messagePack(Hex.encode("aclose".getBytes()), ScriptData.Buffer.TRANSACTION)
                                .messagePack(ScriptAssembler.typeBinary, acloseValue, ScriptData.Buffer.TRANSACTION).getScript())
                .isEmpty(arcvValue,
                        "", new ScriptAssembler().messagePack(Hex.encode("arcv".getBytes()), ScriptData.Buffer.TRANSACTION)
                                .messagePack(ScriptAssembler.typeBinary, arcvValue, ScriptData.Buffer.TRANSACTION).getScript())
                .isEmpty(asndValue,
                        "", new ScriptAssembler().messagePack(Hex.encode("asnd".getBytes()), ScriptData.Buffer.TRANSACTION)
                                .messagePack(ScriptAssembler.typeBinary, asndValue, ScriptData.Buffer.TRANSACTION).getScript())
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
                .isEmpty(xaidValue,
                        "", new ScriptAssembler().messagePack(Hex.encode("xaid".getBytes()), ScriptData.Buffer.TRANSACTION)
                                .messagePack(ScriptAssembler.typeInt, xaidValue, ScriptData.Buffer.TRANSACTION).getScript())
                .arrayEnd(TYPE_MESSAGE_PACK_MAP)
                .showMessage("ALGO")
                .showMessage("axfer")
                .isEmpty(xaidValue,
                        "", new ScriptAssembler().showMessage("assetID").baseConvert(xaidValue, ScriptData.Buffer.CACHE1, 16, ScriptAssembler.decimalCharset, ScriptAssembler.zeroInherit)
                                .showMessage(ScriptData.getDataBufferAll(ScriptData.Buffer.CACHE1)).clearBuffer(ScriptData.Buffer.CACHE1).getScript())
                .isEmpty(arcvValue,
                        "", new ScriptAssembler().hash(arcvValue, ScriptData.Buffer.CACHE1, ScriptAssembler.HashType.SHA512256)
                                .copyArgument(arcvValue, ScriptData.Buffer.CACHE2)
                                .copyArgument(ScriptData.getBuffer(ScriptData.Buffer.CACHE1, 28, 4), ScriptData.Buffer.CACHE2)
                                .clearBuffer(ScriptData.Buffer.CACHE1)
                                .copyString(HexUtil.toHexString("ABCDEFGHIJKLMNOPQRSTUVWXYZ234567"), ScriptData.Buffer.CACHE1)
                                .baseConvert(ScriptData.getBuffer(ScriptData.Buffer.CACHE2, 0, 36), ScriptData.Buffer.CACHE2, 58, ScriptAssembler.cache1Charset, ScriptAssembler.bitLeftJustify8to5)
                                .showAddress(ScriptData.getDataBufferAll(ScriptData.Buffer.CACHE2, 36))
                                .clearBuffer(ScriptData.Buffer.CACHE1)
                                .clearBuffer(ScriptData.Buffer.CACHE2).getScript())
                .showPressButton()
                .setHeader(ScriptAssembler.HashType.NONE, ScriptAssembler.SignType.EDDSA)
                .getScript();
        return script;
    }

    public static String getAssetFreezeTransaction() {
        String TX = HexUtil.toHexString("TX".getBytes());
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        // Asset Frozen
        ScriptData afrzPresent = sac.getArgument(1);
        ScriptData afrzValue = sac.getArgument(1);
        // Freeze Account
        ScriptData faddPresent = sac.getArgument(1);
        ScriptData faddValue = sac.getArgumentRightJustified(32);
        // Freeze Asset
        ScriptData faidPresent = sac.getArgument(1);
        ScriptData faidValue = sac.getArgumentRightJustified(8);
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
                .ifEqual(afrzPresent, "00",
                        "", new ScriptAssembler().messagePack(Hex.encode("afrz".getBytes()), ScriptData.Buffer.TRANSACTION)
                                .messagePack(ScriptAssembler.typeBoolean, afrzValue, ScriptData.Buffer.TRANSACTION).getScript())
                .ifEqual(faddPresent, "00",
                        "", new ScriptAssembler().messagePack(Hex.encode("fadd".getBytes()), ScriptData.Buffer.TRANSACTION)
                                .messagePack(ScriptAssembler.typeBinary, faddValue, ScriptData.Buffer.TRANSACTION).getScript())
                .ifEqual(faidPresent, "00",
                        "", new ScriptAssembler().messagePack(Hex.encode("faid".getBytes()), ScriptData.Buffer.TRANSACTION)
                                .messagePack(ScriptAssembler.typeInt, faidValue, ScriptData.Buffer.TRANSACTION).getScript())
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
                .showMessage("afrz")
                .showPressButton()
                .setHeader(ScriptAssembler.HashType.NONE, ScriptAssembler.SignType.EDDSA)
                .getScript();
        return script;
    }
    public static String getAssetFreezeTransactionRlp() {
        String TX = HexUtil.toHexString("TX".getBytes());
        ScriptRlpArgumentComposer sac = new ScriptRlpArgumentComposer();
        // Asset Frozen
        ScriptRlpData afrzValue = sac.getArgumentRlpItem();
        // Freeze Account
        ScriptRlpData faddValue = sac.getArgumentRlpItem();
        // Freeze Asset
        ScriptRlpData faidValue = sac.getArgumentRlpItem();
        // Fee
        ScriptRlpData feeValue = sac.getArgumentRlpItem();
        // First Valid
        ScriptRlpData fvValue = sac.getArgumentRlpItem();
        // GenesisID
        ScriptRlpData genValue = sac.getArgumentRlpItem();
        // Group
        ScriptRlpData grpValue = sac.getArgumentRlpItem();
        // Genesis Hash
        ScriptRlpData ghValue = sac.getArgumentRlpItem();
        // Last Valid
        ScriptRlpData lvValue = sac.getArgumentRlpItem();
        // Lease
        ScriptRlpData lxValue = sac.getArgumentRlpItem();
        // Note
        ScriptRlpData noteValue = sac.getArgumentRlpItem();
        // Rekey To
        ScriptRlpData rekeyValue = sac.getArgumentRlpItem();
        // Sender
        ScriptRlpData sndValue = sac.getArgumentRlpItem();
        // Type
        ScriptRlpData typeValue = sac.getArgumentRlpItem();

        ScriptAssembler scriptAsb = new ScriptAssembler();
        String script = scriptAsb.setCoinType(0x11B)
                .copyString(TX)
                .arrayPointer()
                .isEmpty(afrzValue,
                        "", new ScriptAssembler().messagePack(Hex.encode("afrz".getBytes()), ScriptData.Buffer.TRANSACTION)
                                .messagePack(ScriptAssembler.typeBoolean, afrzValue, ScriptData.Buffer.TRANSACTION).getScript())
                .isEmpty(faddValue,
                        "", new ScriptAssembler().messagePack(Hex.encode("fadd".getBytes()), ScriptData.Buffer.TRANSACTION)
                                .messagePack(ScriptAssembler.typeBinary, faddValue, ScriptData.Buffer.TRANSACTION).getScript())
                .isEmpty(faidValue,
                        "", new ScriptAssembler().messagePack(Hex.encode("faid".getBytes()), ScriptData.Buffer.TRANSACTION)
                                .messagePack(ScriptAssembler.typeInt, faidValue, ScriptData.Buffer.TRANSACTION).getScript())
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
                .showMessage("afrz")
                .showPressButton()
                .setHeader(ScriptAssembler.HashType.NONE, ScriptAssembler.SignType.EDDSA)
                .getScript();
        return script;
    }

    public static String getApplicationCallTransaction() {
        String TX = HexUtil.toHexString("TX".getBytes());
        ScriptArgumentComposer sac = new ScriptArgumentComposer();

        // App Arguments
        ScriptData apaaPresent = sac.getArgument(1);
        // App Argument 1
        ScriptData apaaValue1Present = sac.getArgument(1);
        ScriptData apaaValue1 = sac.getArgumentRightJustified(2048);
        // App Argument 2
        ScriptData apaaValue2Present = sac.getArgument(1);
        ScriptData apaaValue2 = sac.getArgumentRightJustified(2048);
        // App Argument 3
        ScriptData apaaValue3Present = sac.getArgument(1);
        ScriptData apaaValue3 = sac.getArgumentRightJustified(2048);
        // App Argument 4
        ScriptData apaaValue4Present = sac.getArgument(1);
        ScriptData apaaValue4 = sac.getArgumentRightJustified(2048);
        // App Argument 5
        ScriptData apaaValue5Present = sac.getArgument(1);
        ScriptData apaaValue5 = sac.getArgumentRightJustified(2048);
        // App Argument 6
        ScriptData apaaValue6Present = sac.getArgument(1);
        ScriptData apaaValue6 = sac.getArgumentRightJustified(2048);
        // App Argument 7
        ScriptData apaaValue7Present = sac.getArgument(1);
        ScriptData apaaValue7 = sac.getArgumentRightJustified(2048);
        // App Argument 8
        ScriptData apaaValue8Present = sac.getArgument(1);
        ScriptData apaaValue8 = sac.getArgumentRightJustified(2048);

        // On Complete
        ScriptData apanPresent = sac.getArgument(1);
        ScriptData apanValue = sac.getArgumentRightJustified(8);
        // Aproval Program
        ScriptData apapPresent = sac.getArgument(1);
        ScriptData apapValue = sac.getArgumentRightJustified(8192);

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
        // Foreign Asset 7
        ScriptData apasValue7Present = sac.getArgument(1);
        ScriptData apasValue7 = sac.getArgumentRightJustified(8);
        // Foreign Asset 8
        ScriptData apasValue8Present = sac.getArgument(1);
        ScriptData apasValue8 = sac.getArgumentRightJustified(8);

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
        // Accounts 7
        ScriptData apatValue7Present = sac.getArgument(1);
        ScriptData apatValue7 = sac.getArgumentRightJustified(32);
        // Accounts 8
        ScriptData apatValue8Present = sac.getArgument(1);
        ScriptData apatValue8 = sac.getArgumentRightJustified(32);

        // Extra Program Pages
        ScriptData apepPresent = sac.getArgument(1);
        ScriptData apepValue = sac.getArgumentRightJustified(8);

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
        // Foreign App 7
        ScriptData apfaValue7Present = sac.getArgument(1);
        ScriptData apfaValue7 = sac.getArgumentRightJustified(8);
        // Foreign App 8
        ScriptData apfaValue8Present = sac.getArgument(1);
        ScriptData apfaValue8 = sac.getArgumentRightJustified(8);

        // Local State Schema
        ScriptData aplsPresent = sac.getArgument(1);
        // Number Byte Slices
        ScriptData lnbsPresent = sac.getArgument(1);
        ScriptData lnbsValue = sac.getArgumentRightJustified(8);
        // Number Ints
        ScriptData lnuiPresent = sac.getArgument(1);
        ScriptData lnuiValue = sac.getArgumentRightJustified(8);

        // Global State Schema
        ScriptData apgsPresent = sac.getArgument(1);
        // Number Byte Slices
        ScriptData gnbsPresent = sac.getArgument(1);
        ScriptData gnbsValue = sac.getArgumentRightJustified(8);
        // Number Ints
        ScriptData gnuiPresent = sac.getArgument(1);
        ScriptData gnuiValue = sac.getArgumentRightJustified(8);

        // Clear State Program
        ScriptData apsuPresent = sac.getArgument(1);
        ScriptData apsuValue = sac.getArgumentRightJustified(8192);

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
                                .ifEqual(apaaValue7Present, "00",
                                        "", new ScriptAssembler().messagePack(ScriptAssembler.typeBinary, apaaValue7, ScriptData.Buffer.TRANSACTION).getScript())
                                .ifEqual(apaaValue8Present, "00",
                                        "", new ScriptAssembler().messagePack(ScriptAssembler.typeBinary, apaaValue8, ScriptData.Buffer.TRANSACTION).getScript())
                                .arrayEnd(TYPE_MESSAGE_PACK_ARRAY)
                                .getScript())
                .ifEqual(apanPresent, "00",
                        "", new ScriptAssembler().messagePack(Hex.encode("apan".getBytes()), ScriptData.Buffer.TRANSACTION)
                                .messagePack(ScriptAssembler.typeInt, apanValue, ScriptData.Buffer.TRANSACTION).getScript())
                .ifEqual(apapPresent, "00",
                        "", new ScriptAssembler().messagePack(Hex.encode("apap".getBytes()), ScriptData.Buffer.TRANSACTION)
                                .messagePack(ScriptAssembler.typeBinary, apapValue, ScriptData.Buffer.TRANSACTION).getScript())
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
                                .ifEqual(apasValue7Present, "00",
                                        "", new ScriptAssembler().messagePack(ScriptAssembler.typeInt, apasValue7, ScriptData.Buffer.TRANSACTION).getScript())
                                .ifEqual(apasValue8Present, "00",
                                        "", new ScriptAssembler().messagePack(ScriptAssembler.typeInt, apasValue8, ScriptData.Buffer.TRANSACTION).getScript())
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
                                .ifEqual(apatValue7Present, "00",
                                        "", new ScriptAssembler().messagePack(ScriptAssembler.typeBinary, apatValue7, ScriptData.Buffer.TRANSACTION).getScript())
                                .ifEqual(apatValue8Present, "00",
                                        "", new ScriptAssembler().messagePack(ScriptAssembler.typeBinary, apatValue8, ScriptData.Buffer.TRANSACTION).getScript())
                                .arrayEnd(TYPE_MESSAGE_PACK_ARRAY)
                                .getScript())
                .ifEqual(apepPresent, "00",
                        "", new ScriptAssembler().messagePack(Hex.encode("apep".getBytes()), ScriptData.Buffer.TRANSACTION)
                                .messagePack(ScriptAssembler.typeInt, apepValue, ScriptData.Buffer.TRANSACTION).getScript())
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
                                .ifEqual(apfaValue7Present, "00",
                                        "", new ScriptAssembler().messagePack(ScriptAssembler.typeInt, apfaValue7, ScriptData.Buffer.TRANSACTION).getScript())
                                .ifEqual(apfaValue8Present, "00",
                                        "", new ScriptAssembler().messagePack(ScriptAssembler.typeInt, apfaValue8, ScriptData.Buffer.TRANSACTION).getScript())
                                .arrayEnd(TYPE_MESSAGE_PACK_ARRAY)
                                .getScript())
                .ifEqual(aplsPresent, "00",
                        "", new ScriptAssembler()
                                .messagePack(Hex.encode("apls".getBytes()), ScriptData.Buffer.TRANSACTION)
                                .arrayPointer()
                                .ifEqual(lnbsPresent, "00",
                                        "", new ScriptAssembler().messagePack(Hex.encode("nbs".getBytes()), ScriptData.Buffer.TRANSACTION)
                                                .messagePack(ScriptAssembler.typeInt, lnbsValue, ScriptData.Buffer.TRANSACTION).getScript())
                                .ifEqual(lnuiPresent, "00",
                                        "", new ScriptAssembler().messagePack(Hex.encode("nui".getBytes()), ScriptData.Buffer.TRANSACTION)
                                                .messagePack(ScriptAssembler.typeInt, lnuiValue, ScriptData.Buffer.TRANSACTION).getScript())
                                .arrayEnd(TYPE_MESSAGE_PACK_MAP)
                                .getScript())
                .ifEqual(apgsPresent, "00",
                        "", new ScriptAssembler()
                                .messagePack(Hex.encode("apgs".getBytes()), ScriptData.Buffer.TRANSACTION)
                                .arrayPointer()
                                .ifEqual(gnbsPresent, "00",
                                        "", new ScriptAssembler().messagePack(Hex.encode("nbs".getBytes()), ScriptData.Buffer.TRANSACTION)
                                                .messagePack(ScriptAssembler.typeInt, gnbsValue, ScriptData.Buffer.TRANSACTION).getScript())
                                .ifEqual(gnuiPresent, "00",
                                        "", new ScriptAssembler().messagePack(Hex.encode("nui".getBytes()), ScriptData.Buffer.TRANSACTION)
                                                .messagePack(ScriptAssembler.typeInt, gnuiValue, ScriptData.Buffer.TRANSACTION).getScript())
                                .arrayEnd(TYPE_MESSAGE_PACK_MAP)
                                .getScript())
                .ifEqual(apsuPresent, "00",
                        "", new ScriptAssembler().messagePack(Hex.encode("apsu".getBytes()), ScriptData.Buffer.TRANSACTION)
                                .messagePack(ScriptAssembler.typeBinary, apsuValue, ScriptData.Buffer.TRANSACTION).getScript())
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
                .showPressButton()
                .setHeader(ScriptAssembler.HashType.NONE, ScriptAssembler.SignType.EDDSA)
                .getScript();
        return script;
    }
    public static String getApplicationCallTransactionRlp() {
        String TX = HexUtil.toHexString("TX".getBytes());
        ScriptRlpArgumentComposer sac = new ScriptRlpArgumentComposer();

        // App Arguments
        ScriptRlpData apaaPresent = sac.getArgumentRlpItem();
        // App Argument 1
        ScriptRlpData apaaValue1 = sac.getArgumentRlpItem();
        // App Argument 2
        ScriptRlpData apaaValue2 = sac.getArgumentRlpItem();
        // App Argument 3
        ScriptRlpData apaaValue3 = sac.getArgumentRlpItem();
        // App Argument 4
        ScriptRlpData apaaValue4 = sac.getArgumentRlpItem();
        // App Argument 5
        ScriptRlpData apaaValue5 = sac.getArgumentRlpItem();
        // App Argument 6
        ScriptRlpData apaaValue6 = sac.getArgumentRlpItem();
        // App Argument 7
        ScriptRlpData apaaValue7 = sac.getArgumentRlpItem();
        // App Argument 8
        ScriptRlpData apaaValue8 = sac.getArgumentRlpItem();

        // On Complete
        ScriptRlpData apanValue = sac.getArgumentRlpItem();
        // Aproval Program
        ScriptRlpData apapValue = sac.getArgumentRlpItem();

        // Foreign Asset
        ScriptRlpData apasPresent = sac.getArgumentRlpItem();
        // Foreign Asset 1
        ScriptRlpData apasValue1 = sac.getArgumentRlpItem();
        // Foreign Asset 2
        ScriptRlpData apasValue2 = sac.getArgumentRlpItem();
        // Foreign Asset 3
        ScriptRlpData apasValue3 = sac.getArgumentRlpItem();
        // Foreign Asset 4
        ScriptRlpData apasValue4 = sac.getArgumentRlpItem();
        // Foreign Asset 5
        ScriptRlpData apasValue5 = sac.getArgumentRlpItem();
        // Foreign Asset 6
        ScriptRlpData apasValue6 = sac.getArgumentRlpItem();
        // Foreign Asset 7
        ScriptRlpData apasValue7 = sac.getArgumentRlpItem();
        // Foreign Asset 8
        ScriptRlpData apasValue8 = sac.getArgumentRlpItem();

        // Accounts
        ScriptRlpData apatPresent = sac.getArgumentRlpItem();
        // Accounts 1
        ScriptRlpData apatValue1 = sac.getArgumentRlpItem();
        // Accounts 2
        ScriptRlpData apatValue2 = sac.getArgumentRlpItem();
        // Accounts 3
        ScriptRlpData apatValue3 = sac.getArgumentRlpItem();
        // Accounts 4
        ScriptRlpData apatValue4 = sac.getArgumentRlpItem();
        // Accounts 5
        ScriptRlpData apatValue5 = sac.getArgumentRlpItem();
        // Accounts 6
        ScriptRlpData apatValue6 = sac.getArgumentRlpItem();
        // Accounts 7
        ScriptRlpData apatValue7 = sac.getArgumentRlpItem();
        // Accounts 8
        ScriptRlpData apatValue8 = sac.getArgumentRlpItem();

        // Extra Program Pages
        ScriptRlpData apepValue = sac.getArgumentRlpItem();

        // Foreign App
        ScriptRlpData apfaPresent = sac.getArgumentRlpItem();
        // Foreign App 1
        ScriptRlpData apfaValue1 = sac.getArgumentRlpItem();
        // Foreign App 2
        ScriptRlpData apfaValue2 = sac.getArgumentRlpItem();
        // Foreign App 3
        ScriptRlpData apfaValue3 = sac.getArgumentRlpItem();
        // Foreign App 4
        ScriptRlpData apfaValue4 = sac.getArgumentRlpItem();
        // Foreign App 5
        ScriptRlpData apfaValue5 = sac.getArgumentRlpItem();
        // Foreign App 6
        ScriptRlpData apfaValue6 = sac.getArgumentRlpItem();
        // Foreign App 7
        ScriptRlpData apfaValue7 = sac.getArgumentRlpItem();
        // Foreign App 8
        ScriptRlpData apfaValue8 = sac.getArgumentRlpItem();

        // Application ID
        ScriptRlpData apidValue = sac.getArgumentRlpItem();

        // Local State Schema
        ScriptRlpData aplsPresent = sac.getArgumentRlpItem();
        // Number Byte Slices
        ScriptRlpData lnbsValue = sac.getArgumentRlpItem();
        // Number Ints
        ScriptRlpData lnuiValue = sac.getArgumentRlpItem();

        // Global State Schema
        ScriptRlpData apgsPresent = sac.getArgumentRlpItem();
        // Number Byte Slices
        ScriptRlpData gnbsValue = sac.getArgumentRlpItem();
        // Number Ints
        ScriptRlpData gnuiValue = sac.getArgumentRlpItem();

        // Clear State Program
        ScriptRlpData apsuValue = sac.getArgumentRlpItem();
        // Fee
        ScriptRlpData feeValue = sac.getArgumentRlpItem();
        // First Valid
        ScriptRlpData fvValue = sac.getArgumentRlpItem();
        // GenesisID
        ScriptRlpData genValue = sac.getArgumentRlpItem();
        // Group
        ScriptRlpData grpValue = sac.getArgumentRlpItem();
        // Genesis Hash
        ScriptRlpData ghValue = sac.getArgumentRlpItem();
        // Last Valid
        ScriptRlpData lvValue = sac.getArgumentRlpItem();
        // Lease
        ScriptRlpData lxValue = sac.getArgumentRlpItem();
        // Note
        ScriptRlpData noteValue = sac.getArgumentRlpItem();
        // Rekey To
        ScriptRlpData rekeyValue = sac.getArgumentRlpItem();
        // Sender
        ScriptRlpData sndValue = sac.getArgumentRlpItem();
        // Type
        ScriptRlpData typeValue = sac.getArgumentRlpItem();

        ScriptAssembler scriptAsb = new ScriptAssembler();
        String script = scriptAsb.setCoinType(0x11B)
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
                                .isEmpty(apaaValue7,
                                        "", new ScriptAssembler().messagePack(ScriptAssembler.typeBinary, apaaValue7, ScriptData.Buffer.TRANSACTION).getScript())
                                .isEmpty(apaaValue8,
                                        "", new ScriptAssembler().messagePack(ScriptAssembler.typeBinary, apaaValue8, ScriptData.Buffer.TRANSACTION).getScript())
                                .arrayEnd(TYPE_MESSAGE_PACK_ARRAY)
                                .getScript())
                .isEmpty(apanValue,
                        "", new ScriptAssembler().messagePack(Hex.encode("apan".getBytes()), ScriptData.Buffer.TRANSACTION)
                                .messagePack(ScriptAssembler.typeInt, apanValue, ScriptData.Buffer.TRANSACTION).getScript())
                .isEmpty(apapValue,
                        "", new ScriptAssembler().messagePack(Hex.encode("apap".getBytes()), ScriptData.Buffer.TRANSACTION)
                                .messagePack(ScriptAssembler.typeBinary, apapValue, ScriptData.Buffer.TRANSACTION).getScript())
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
                                .isEmpty(apasValue7,
                                        "", new ScriptAssembler().messagePack(ScriptAssembler.typeInt, apasValue7, ScriptData.Buffer.TRANSACTION).getScript())
                                .isEmpty(apasValue8,
                                        "", new ScriptAssembler().messagePack(ScriptAssembler.typeInt, apasValue8, ScriptData.Buffer.TRANSACTION).getScript())
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
                                .isEmpty(apatValue7,
                                        "", new ScriptAssembler().messagePack(ScriptAssembler.typeBinary, apatValue7, ScriptData.Buffer.TRANSACTION).getScript())
                                .isEmpty(apatValue8,
                                        "", new ScriptAssembler().messagePack(ScriptAssembler.typeBinary, apatValue8, ScriptData.Buffer.TRANSACTION).getScript())
                                .arrayEnd(TYPE_MESSAGE_PACK_ARRAY)
                                .getScript())
                .isEmpty(apepValue,
                        "", new ScriptAssembler().messagePack(Hex.encode("apep".getBytes()), ScriptData.Buffer.TRANSACTION)
                                .messagePack(ScriptAssembler.typeInt, apepValue, ScriptData.Buffer.TRANSACTION).getScript())
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
                                .isEmpty(apfaValue7,
                                        "", new ScriptAssembler().messagePack(ScriptAssembler.typeInt, apfaValue7, ScriptData.Buffer.TRANSACTION).getScript())
                                .isEmpty(apfaValue8,
                                        "", new ScriptAssembler().messagePack(ScriptAssembler.typeInt, apfaValue8, ScriptData.Buffer.TRANSACTION).getScript())
                                .arrayEnd(TYPE_MESSAGE_PACK_ARRAY)
                                .getScript())
                .isEmpty(aplsPresent,
                        "", new ScriptAssembler()
                                .messagePack(Hex.encode("apls".getBytes()), ScriptData.Buffer.TRANSACTION)
                                .arrayPointer()
                                .isEmpty(lnbsValue,
                                        "", new ScriptAssembler().messagePack(Hex.encode("nbs".getBytes()), ScriptData.Buffer.TRANSACTION)
                                                .messagePack(ScriptAssembler.typeInt, lnbsValue, ScriptData.Buffer.TRANSACTION).getScript())
                                .isEmpty(lnuiValue,
                                        "", new ScriptAssembler().messagePack(Hex.encode("nui".getBytes()), ScriptData.Buffer.TRANSACTION)
                                                .messagePack(ScriptAssembler.typeInt, lnuiValue, ScriptData.Buffer.TRANSACTION).getScript())
                                .arrayEnd(TYPE_MESSAGE_PACK_MAP)
                                .getScript())
                .isEmpty(apgsPresent,
                        "", new ScriptAssembler()
                                .messagePack(Hex.encode("apgs".getBytes()), ScriptData.Buffer.TRANSACTION)
                                .arrayPointer()
                                .isEmpty(gnbsValue,
                                        "", new ScriptAssembler().messagePack(Hex.encode("nbs".getBytes()), ScriptData.Buffer.TRANSACTION)
                                                .messagePack(ScriptAssembler.typeInt, gnbsValue, ScriptData.Buffer.TRANSACTION).getScript())
                                .isEmpty(gnuiValue,
                                        "", new ScriptAssembler().messagePack(Hex.encode("nui".getBytes()), ScriptData.Buffer.TRANSACTION)
                                                .messagePack(ScriptAssembler.typeInt, gnuiValue, ScriptData.Buffer.TRANSACTION).getScript())
                                .arrayEnd(TYPE_MESSAGE_PACK_MAP)
                                .getScript())
                .isEmpty(apidValue,
                        "", new ScriptAssembler().messagePack(Hex.encode("apid".getBytes()), ScriptData.Buffer.TRANSACTION)
                                .messagePack(ScriptAssembler.typeInt, apidValue, ScriptData.Buffer.TRANSACTION).getScript())
                .isEmpty(apsuValue,
                        "", new ScriptAssembler().messagePack(Hex.encode("apsu".getBytes()), ScriptData.Buffer.TRANSACTION)
                                .messagePack(ScriptAssembler.typeBinary, apsuValue, ScriptData.Buffer.TRANSACTION).getScript())
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

    public static String getApplicationCallTransaction2() {
        String TX = HexUtil.toHexString("TX".getBytes());
        ScriptArgumentComposer sac = new ScriptArgumentComposer();

        // App Arguments
        ScriptData apaaPresent = sac.getArgument(1);
        // App Argument 1
        ScriptData apaaValue1Present = sac.getArgument(1);
        ScriptData apaaValue1 = sac.getArgumentRightJustified(2048);
        // App Argument 2
        ScriptData apaaValue2Present = sac.getArgument(1);
        ScriptData apaaValue2 = sac.getArgumentRightJustified(2048);
        // App Argument 3
        ScriptData apaaValue3Present = sac.getArgument(1);
        ScriptData apaaValue3 = sac.getArgumentRightJustified(2048);
        // App Argument 4
        ScriptData apaaValue4Present = sac.getArgument(1);
        ScriptData apaaValue4 = sac.getArgumentRightJustified(2048);
        // App Argument 5
        ScriptData apaaValue5Present = sac.getArgument(1);
        ScriptData apaaValue5 = sac.getArgumentRightJustified(2048);
        // App Argument 6
        ScriptData apaaValue6Present = sac.getArgument(1);
        ScriptData apaaValue6 = sac.getArgumentRightJustified(2048);
        // App Argument 7
        ScriptData apaaValue7Present = sac.getArgument(1);
        ScriptData apaaValue7 = sac.getArgumentRightJustified(2048);
        // App Argument 8
        ScriptData apaaValue8Present = sac.getArgument(1);
        ScriptData apaaValue8 = sac.getArgumentRightJustified(2048);

        // On Complete
        ScriptData apanPresent = sac.getArgument(1);
        ScriptData apanValue = sac.getArgumentRightJustified(8);
        // Aproval Program
        ScriptData apapPresent = sac.getArgument(1);
        ScriptData apapValue = sac.getArgumentRightJustified(8192);

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
        // Foreign Asset 7
        ScriptData apasValue7Present = sac.getArgument(1);
        ScriptData apasValue7 = sac.getArgumentRightJustified(8);
        // Foreign Asset 8
        ScriptData apasValue8Present = sac.getArgument(1);
        ScriptData apasValue8 = sac.getArgumentRightJustified(8);

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
        // Accounts 7
        ScriptData apatValue7Present = sac.getArgument(1);
        ScriptData apatValue7 = sac.getArgumentRightJustified(32);
        // Accounts 8
        ScriptData apatValue8Present = sac.getArgument(1);
        ScriptData apatValue8 = sac.getArgumentRightJustified(32);

        // Extra Program Pages
        ScriptData apepPresent = sac.getArgument(1);
        ScriptData apepValue = sac.getArgumentRightJustified(8);

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
        // Foreign App 7
        ScriptData apfaValue7Present = sac.getArgument(1);
        ScriptData apfaValue7 = sac.getArgumentRightJustified(8);
        // Foreign App 8
        ScriptData apfaValue8Present = sac.getArgument(1);
        ScriptData apfaValue8 = sac.getArgumentRightJustified(8);

        // Application ID
        ScriptData apidPresent = sac.getArgument(1);
        ScriptData apidValue = sac.getArgumentRightJustified(8);

        // Clear State Program
        ScriptData apsuPresent = sac.getArgument(1);
        ScriptData apsuValue = sac.getArgumentRightJustified(8192);

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
                                .ifEqual(apaaValue7Present, "00",
                                        "", new ScriptAssembler().messagePack(ScriptAssembler.typeBinary, apaaValue7, ScriptData.Buffer.TRANSACTION).getScript())
                                .ifEqual(apaaValue8Present, "00",
                                        "", new ScriptAssembler().messagePack(ScriptAssembler.typeBinary, apaaValue8, ScriptData.Buffer.TRANSACTION).getScript())
                                .arrayEnd(TYPE_MESSAGE_PACK_ARRAY)
                                .getScript())
                .ifEqual(apanPresent, "00",
                        "", new ScriptAssembler().messagePack(Hex.encode("apan".getBytes()), ScriptData.Buffer.TRANSACTION)
                                .messagePack(ScriptAssembler.typeInt, apanValue, ScriptData.Buffer.TRANSACTION).getScript())
                .ifEqual(apapPresent, "00",
                        "", new ScriptAssembler().messagePack(Hex.encode("apap".getBytes()), ScriptData.Buffer.TRANSACTION)
                                .messagePack(ScriptAssembler.typeBinary, apapValue, ScriptData.Buffer.TRANSACTION).getScript())
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
                                .ifEqual(apasValue7Present, "00",
                                        "", new ScriptAssembler().messagePack(ScriptAssembler.typeInt, apasValue7, ScriptData.Buffer.TRANSACTION).getScript())
                                .ifEqual(apasValue8Present, "00",
                                        "", new ScriptAssembler().messagePack(ScriptAssembler.typeInt, apasValue8, ScriptData.Buffer.TRANSACTION).getScript())
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
                                .ifEqual(apatValue7Present, "00",
                                        "", new ScriptAssembler().messagePack(ScriptAssembler.typeBinary, apatValue7, ScriptData.Buffer.TRANSACTION).getScript())
                                .ifEqual(apatValue8Present, "00",
                                        "", new ScriptAssembler().messagePack(ScriptAssembler.typeBinary, apatValue8, ScriptData.Buffer.TRANSACTION).getScript())
                                .arrayEnd(TYPE_MESSAGE_PACK_ARRAY)
                                .getScript())
                .ifEqual(apepPresent, "00",
                        "", new ScriptAssembler().messagePack(Hex.encode("apep".getBytes()), ScriptData.Buffer.TRANSACTION)
                                .messagePack(ScriptAssembler.typeInt, apepValue, ScriptData.Buffer.TRANSACTION).getScript())
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
                                .ifEqual(apfaValue7Present, "00",
                                        "", new ScriptAssembler().messagePack(ScriptAssembler.typeInt, apfaValue7, ScriptData.Buffer.TRANSACTION).getScript())
                                .ifEqual(apfaValue8Present, "00",
                                        "", new ScriptAssembler().messagePack(ScriptAssembler.typeInt, apfaValue8, ScriptData.Buffer.TRANSACTION).getScript())
                                .arrayEnd(TYPE_MESSAGE_PACK_ARRAY)
                                .getScript())
                .ifEqual(apidPresent, "00",
                        "", new ScriptAssembler().messagePack(Hex.encode("apid".getBytes()), ScriptData.Buffer.TRANSACTION)
                                .messagePack(ScriptAssembler.typeInt, apidValue, ScriptData.Buffer.TRANSACTION).getScript())
                .ifEqual(apsuPresent, "00",
                        "", new ScriptAssembler().messagePack(Hex.encode("apsu".getBytes()), ScriptData.Buffer.TRANSACTION)
                                .messagePack(ScriptAssembler.typeBinary, apsuValue, ScriptData.Buffer.TRANSACTION).getScript())
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
                .setHeader(ScriptAssembler.HashType.NONE, ScriptAssembler.SignType.EDDSA)
                .getScript();
        return script;
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
                .setHeader(ScriptAssembler.HashType.NONE, ScriptAssembler.SignType.EDDSA)
                .getScript();
        return script;
    }

    public static String getApplicationCallTransaction4() {
        String TX = HexUtil.toHexString("TX".getBytes());
        ScriptArgumentComposer sac = new ScriptArgumentComposer();

        // App Arguments
        ScriptData apaaPresent = sac.getArgument(1);
        // App Argument 1
        ScriptData apaaValue1Present = sac.getArgument(1);
        ScriptData apaaValue1IsUint = sac.getArgument(1);
        ScriptData apaaValue1 = sac.getArgumentRightJustified(1024);

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
                                .arrayEnd(TYPE_MESSAGE_PACK_ARRAY)
                                .getScript())
                .arrayEnd(TYPE_MESSAGE_PACK_MAP)
                .showMessage("ALGO")
                .showMessage("appl")
                .showPressButton()
                .setHeader(ScriptAssembler.HashType.NONE, ScriptAssembler.SignType.EDDSA)
                .getScript();
        return script;
    }
}
