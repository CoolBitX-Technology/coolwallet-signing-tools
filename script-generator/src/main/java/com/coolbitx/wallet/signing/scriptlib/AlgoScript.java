package com.coolbitx.wallet.signing.scriptlib;

import com.coolbitx.wallet.signing.utils.HexUtil;
import com.coolbitx.wallet.signing.utils.ScriptArgumentComposer;
import com.coolbitx.wallet.signing.utils.ScriptAssembler;
import com.coolbitx.wallet.signing.utils.ScriptData;

import java.util.HashMap;
import java.util.Map;

import static com.coolbitx.wallet.signing.utils.ScriptAssembler.TYPE_MESSAGE_PACK_ARRAY;
import static com.coolbitx.wallet.signing.utils.ScriptAssembler.TYPE_MESSAGE_PACK_MAP;

public class AlgoScript {
    public static void listAll() {
        System.out.println("COIN-ALGO SCRIPTS");
        System.out.println("==================================================");
        System.out.println("Address Script: \n" + getAddressScript() + "\n");
        System.out.println("Payment Transaction: \n" + getPaymentTransaction() + "\n");
        System.out.println("Key Registration Transaction: \n" + getKeyRegistrationTransaction() + "\n");
        System.out.println("Asset Config Transaction: \n" + getAssetConfigTransaction() + "\n");
        System.out.println("Asset Transfer Transaction: \n" + getAssetTransferTransaction() + "\n");
        System.out.println("Asset Freeze Transaction: \n" + getAssetFreezeTransaction() + "\n");
        System.out.println("Application Call Transaction: \n" + getApplicationCallTransaction() + "\n");
        System.out.println("==================================================");
    }

    public static String getPaymentTransaction() {
        String TX = HexUtil.toHexString("TX".getBytes());
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        // Amount
        ScriptData amtPresent = sac.getArgument(1);
        ScriptData amtKey = sac.getArgumentRightJustified(8);
        ScriptData amtValue = sac.getArgumentRightJustified(8);
        // Close
        ScriptData closePresent = sac.getArgument(1);
        ScriptData closeKey = sac.getArgumentRightJustified(8);
        ScriptData closeValue = sac.getArgumentRightJustified(32);
        // Fee
        ScriptData feePresent = sac.getArgument(1);
        ScriptData feeKey = sac.getArgumentRightJustified(8);
        ScriptData feeValue = sac.getArgumentRightJustified(8);
        // First Valid
        ScriptData fvPresent = sac.getArgument(1);
        ScriptData fvKey = sac.getArgumentRightJustified(8);
        ScriptData fvValue = sac.getArgumentRightJustified(8);
        // GenesisID
        ScriptData genPresent = sac.getArgument(1);
        ScriptData genKey = sac.getArgumentRightJustified(8);
        ScriptData genValue = sac.getArgumentRightJustified(32);
        // Group
        ScriptData grpPresent = sac.getArgument(1);
        ScriptData grpKey = sac.getArgumentRightJustified(8);
        ScriptData grpValue = sac.getArgumentRightJustified(32);
        // Genesis Hash
        ScriptData ghPresent = sac.getArgument(1);
        ScriptData ghKey = sac.getArgumentRightJustified(8);
        ScriptData ghValue = sac.getArgumentRightJustified(32);
        // Last Valid
        ScriptData lvPresent = sac.getArgument(1);
        ScriptData lvKey = sac.getArgumentRightJustified(8);
        ScriptData lvValue = sac.getArgumentRightJustified(8);
        // Lease
        ScriptData lxPresent = sac.getArgument(1);
        ScriptData lxKey = sac.getArgumentRightJustified(8);
        ScriptData lxValue = sac.getArgumentRightJustified(32);
        // Note
        ScriptData notePresent = sac.getArgument(1);
        ScriptData noteKey = sac.getArgumentRightJustified(8);
        ScriptData noteValue = sac.getArgumentRightJustified(1024);
        // Receiver
        ScriptData rcvPresent = sac.getArgument(1);
        ScriptData rcvKey = sac.getArgumentRightJustified(8);
        ScriptData rcvValue = sac.getArgumentRightJustified(32);
        // Rekey To
        ScriptData rekeyPresent = sac.getArgument(1);
        ScriptData rekeyKey = sac.getArgumentRightJustified(8);
        ScriptData rekeyValue = sac.getArgumentRightJustified(32);
        // Sender
        ScriptData sndPresent = sac.getArgument(1);
        ScriptData sndKey = sac.getArgumentRightJustified(8);
        ScriptData sndValue = sac.getArgumentRightJustified(32);
        // Type
        ScriptData typePresent = sac.getArgument(1);
        ScriptData typeKey = sac.getArgumentRightJustified(8);
        ScriptData typeValue = sac.getArgumentRightJustified(8);

        ScriptAssembler scriptAsb = new ScriptAssembler();
        String script = scriptAsb.setCoinType(0x11B)
                .arrayPointer()
                .copyString(TX)
                .arrayPointer()
                .ifEqual(amtPresent, "00",
                        "", new ScriptAssembler().messagePack(ScriptAssembler.typeString, amtKey, ScriptData.Buffer.TRANSACTION)
                                .messagePack(ScriptAssembler.typeInt, amtValue, ScriptData.Buffer.TRANSACTION).getScript())
                .ifEqual(closePresent, "00",
                        "", new ScriptAssembler().messagePack(ScriptAssembler.typeString, closeKey, ScriptData.Buffer.TRANSACTION)
                                .messagePack(ScriptAssembler.typeBinary, closeValue, ScriptData.Buffer.TRANSACTION).getScript())
                .ifEqual(feePresent, "00",
                        "", new ScriptAssembler().messagePack(ScriptAssembler.typeString, feeKey, ScriptData.Buffer.TRANSACTION)
                                .messagePack(ScriptAssembler.typeInt, feeValue, ScriptData.Buffer.TRANSACTION).getScript())
                .ifEqual(fvPresent, "00",
                        "", new ScriptAssembler().messagePack(ScriptAssembler.typeString, fvKey, ScriptData.Buffer.TRANSACTION)
                                .messagePack(ScriptAssembler.typeInt, fvValue, ScriptData.Buffer.TRANSACTION).getScript())
                .ifEqual(genPresent, "00",
                        "", new ScriptAssembler().messagePack(ScriptAssembler.typeString, genKey, ScriptData.Buffer.TRANSACTION)
                                .messagePack(ScriptAssembler.typeString, genValue, ScriptData.Buffer.TRANSACTION).getScript())
                .ifEqual(grpPresent, "00",
                        "", new ScriptAssembler().messagePack(ScriptAssembler.typeString, grpKey, ScriptData.Buffer.TRANSACTION)
                                .messagePack(ScriptAssembler.typeBinary, grpValue, ScriptData.Buffer.TRANSACTION).getScript())
                .ifEqual(ghPresent, "00",
                        "", new ScriptAssembler().messagePack(ScriptAssembler.typeString, ghKey, ScriptData.Buffer.TRANSACTION)
                                .messagePack(ScriptAssembler.typeBinary, ghValue, ScriptData.Buffer.TRANSACTION).getScript())
                .ifEqual(lvPresent, "00",
                        "", new ScriptAssembler().messagePack(ScriptAssembler.typeString, lvKey, ScriptData.Buffer.TRANSACTION)
                                .messagePack(ScriptAssembler.typeInt, lvValue, ScriptData.Buffer.TRANSACTION).getScript())
                .ifEqual(lxPresent, "00",
                        "", new ScriptAssembler().messagePack(ScriptAssembler.typeString, lxKey, ScriptData.Buffer.TRANSACTION)
                                .messagePack(ScriptAssembler.typeBinary, lxValue, ScriptData.Buffer.TRANSACTION).getScript())
                .ifEqual(notePresent, "00",
                        "", new ScriptAssembler().messagePack(ScriptAssembler.typeString, noteKey, ScriptData.Buffer.TRANSACTION)
                                .messagePack(ScriptAssembler.typeBinary, noteValue, ScriptData.Buffer.TRANSACTION).getScript())
                .ifEqual(rcvPresent, "00",
                        "", new ScriptAssembler().messagePack(ScriptAssembler.typeString, rcvKey, ScriptData.Buffer.TRANSACTION)
                                .messagePack(ScriptAssembler.typeBinary, rcvValue, ScriptData.Buffer.TRANSACTION).getScript())
                .ifEqual(rekeyPresent, "00",
                        "", new ScriptAssembler().messagePack(ScriptAssembler.typeString, rekeyKey, ScriptData.Buffer.TRANSACTION)
                                .messagePack(ScriptAssembler.typeBinary, rekeyValue, ScriptData.Buffer.TRANSACTION).getScript())
                .ifEqual(sndPresent, "00",
                        "", new ScriptAssembler().messagePack(ScriptAssembler.typeString, sndKey, ScriptData.Buffer.TRANSACTION)
                                .messagePack(ScriptAssembler.typeBinary, sndValue, ScriptData.Buffer.TRANSACTION).getScript())
                .ifEqual(typePresent, "00",
                        "", new ScriptAssembler().messagePack(ScriptAssembler.typeString, typeKey, ScriptData.Buffer.TRANSACTION)
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
                                .baseConvert(ScriptData.getBuffer(ScriptData.Buffer.CACHE2, 0, 36), ScriptData.Buffer.CACHE2, 58, ScriptAssembler.extendedCharset, ScriptAssembler.bitLeftJustify8to5)
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
        ScriptData feeKey = sac.getArgumentRightJustified(8);
        ScriptData feeValue = sac.getArgumentRightJustified(8);
        // First Valid
        ScriptData fvPresent = sac.getArgument(1);
        ScriptData fvKey = sac.getArgumentRightJustified(8);
        ScriptData fvValue = sac.getArgumentRightJustified(8);
        // GenesisID
        ScriptData genPresent = sac.getArgument(1);
        ScriptData genKey = sac.getArgumentRightJustified(8);
        ScriptData genValue = sac.getArgumentRightJustified(32);
        // Group
        ScriptData grpPresent = sac.getArgument(1);
        ScriptData grpKey = sac.getArgumentRightJustified(8);
        ScriptData grpValue = sac.getArgumentRightJustified(32);
        // Genesis Hash
        ScriptData ghPresent = sac.getArgument(1);
        ScriptData ghKey = sac.getArgumentRightJustified(8);
        ScriptData ghValue = sac.getArgumentRightJustified(32);
        // Last Valid
        ScriptData lvPresent = sac.getArgument(1);
        ScriptData lvKey = sac.getArgumentRightJustified(8);
        ScriptData lvValue = sac.getArgumentRightJustified(8);
        // Lease
        ScriptData lxPresent = sac.getArgument(1);
        ScriptData lxKey = sac.getArgumentRightJustified(8);
        ScriptData lxValue = sac.getArgumentRightJustified(32);
        // Non Participation
        ScriptData nonpartPresent = sac.getArgument(1);
        ScriptData nonpartKey = sac.getArgumentRightJustified(8);
        ScriptData nonpartValue = sac.getArgument(1);
        // Note
        ScriptData notePresent = sac.getArgument(1);
        ScriptData noteKey = sac.getArgumentRightJustified(8);
        ScriptData noteValue = sac.getArgumentRightJustified(1024);
        // Rekey To
        ScriptData rekeyPresent = sac.getArgument(1);
        ScriptData rekeyKey = sac.getArgumentRightJustified(8);
        ScriptData rekeyValue = sac.getArgumentRightJustified(32);
        // Selection PK
        ScriptData selkeyPresent = sac.getArgument(1);
        ScriptData selkeyKey = sac.getArgumentRightJustified(8);
        ScriptData selkeyValue = sac.getArgumentRightJustified(32);
        // State Proof PK
        ScriptData sprfkeyPresent = sac.getArgument(1);
        ScriptData sprfkeyKey = sac.getArgumentRightJustified(8);
        ScriptData sprfkeyValue = sac.getArgumentRightJustified(64);
        // Sender
        ScriptData sndPresent = sac.getArgument(1);
        ScriptData sndKey = sac.getArgumentRightJustified(8);
        ScriptData sndValue = sac.getArgumentRightJustified(32);
        // Type
        ScriptData typePresent = sac.getArgument(1);
        ScriptData typeKey = sac.getArgumentRightJustified(8);
        ScriptData typeValue = sac.getArgumentRightJustified(8);
        // Vote First
        ScriptData votefstPresent = sac.getArgument(1);
        ScriptData votefstKey = sac.getArgumentRightJustified(8);
        ScriptData votefstValue = sac.getArgumentRightJustified(8);
        // Vote Key Dilution
        ScriptData votekdPresent = sac.getArgument(1);
        ScriptData votekdKey = sac.getArgumentRightJustified(8);
        ScriptData votekdValue = sac.getArgumentRightJustified(8);
        // Vote PK
        ScriptData votekeyPresent = sac.getArgument(1);
        ScriptData votekeyKey = sac.getArgumentRightJustified(8);
        ScriptData votekeyValue = sac.getArgumentRightJustified(32);
        // Vote Last
        ScriptData votelstPresent = sac.getArgument(1);
        ScriptData votelstKey = sac.getArgumentRightJustified(8);
        ScriptData votelstValue = sac.getArgumentRightJustified(8);

        ScriptAssembler scriptAsb = new ScriptAssembler();
        String script = scriptAsb.setCoinType(0x11B)
                .arrayPointer()
                .copyString(TX)
                .arrayPointer()
                .ifEqual(feePresent, "00",
                        "", new ScriptAssembler().messagePack(ScriptAssembler.typeString, feeKey, ScriptData.Buffer.TRANSACTION)
                                .messagePack(ScriptAssembler.typeInt, feeValue, ScriptData.Buffer.TRANSACTION).getScript())
                .ifEqual(fvPresent, "00",
                        "", new ScriptAssembler().messagePack(ScriptAssembler.typeString, fvKey, ScriptData.Buffer.TRANSACTION)
                                .messagePack(ScriptAssembler.typeInt, fvValue, ScriptData.Buffer.TRANSACTION).getScript())
                .ifEqual(genPresent, "00",
                        "", new ScriptAssembler().messagePack(ScriptAssembler.typeString, genKey, ScriptData.Buffer.TRANSACTION)
                                .messagePack(ScriptAssembler.typeString, genValue, ScriptData.Buffer.TRANSACTION).getScript())
                .ifEqual(grpPresent, "00",
                        "", new ScriptAssembler().messagePack(ScriptAssembler.typeString, grpKey, ScriptData.Buffer.TRANSACTION)
                                .messagePack(ScriptAssembler.typeBinary, grpValue, ScriptData.Buffer.TRANSACTION).getScript())
                .ifEqual(ghPresent, "00",
                        "", new ScriptAssembler().messagePack(ScriptAssembler.typeString, ghKey, ScriptData.Buffer.TRANSACTION)
                                .messagePack(ScriptAssembler.typeBinary, ghValue, ScriptData.Buffer.TRANSACTION).getScript())
                .ifEqual(lvPresent, "00",
                        "", new ScriptAssembler().messagePack(ScriptAssembler.typeString, lvKey, ScriptData.Buffer.TRANSACTION)
                                .messagePack(ScriptAssembler.typeInt, lvValue, ScriptData.Buffer.TRANSACTION).getScript())
                .ifEqual(nonpartPresent, "00",
                        "", new ScriptAssembler().messagePack(ScriptAssembler.typeString, nonpartKey, ScriptData.Buffer.TRANSACTION)
                                .messagePack(ScriptAssembler.typeBoolean, nonpartValue, ScriptData.Buffer.TRANSACTION).getScript())
                .ifEqual(lxPresent, "00",
                        "", new ScriptAssembler().messagePack(ScriptAssembler.typeString, lxKey, ScriptData.Buffer.TRANSACTION)
                                .messagePack(ScriptAssembler.typeBinary, lxValue, ScriptData.Buffer.TRANSACTION).getScript())
                .ifEqual(notePresent, "00",
                        "", new ScriptAssembler().messagePack(ScriptAssembler.typeString, noteKey, ScriptData.Buffer.TRANSACTION)
                                .messagePack(ScriptAssembler.typeBinary, noteValue, ScriptData.Buffer.TRANSACTION).getScript())
                .ifEqual(rekeyPresent, "00",
                        "", new ScriptAssembler().messagePack(ScriptAssembler.typeString, rekeyKey, ScriptData.Buffer.TRANSACTION)
                                .messagePack(ScriptAssembler.typeBinary, rekeyValue, ScriptData.Buffer.TRANSACTION).getScript())
                .ifEqual(selkeyPresent, "00",
                        "", new ScriptAssembler().messagePack(ScriptAssembler.typeString, selkeyKey, ScriptData.Buffer.TRANSACTION)
                                .messagePack(ScriptAssembler.typeBinary, selkeyValue, ScriptData.Buffer.TRANSACTION).getScript())
                .ifEqual(sprfkeyPresent, "00",
                        "", new ScriptAssembler().messagePack(ScriptAssembler.typeString, sprfkeyKey, ScriptData.Buffer.TRANSACTION)
                                .messagePack(ScriptAssembler.typeBinary, sprfkeyValue, ScriptData.Buffer.TRANSACTION).getScript())
                .ifEqual(sndPresent, "00",
                        "", new ScriptAssembler().messagePack(ScriptAssembler.typeString, sndKey, ScriptData.Buffer.TRANSACTION)
                                .messagePack(ScriptAssembler.typeBinary, sndValue, ScriptData.Buffer.TRANSACTION).getScript())
                .ifEqual(typePresent, "00",
                        "", new ScriptAssembler().messagePack(ScriptAssembler.typeString, typeKey, ScriptData.Buffer.TRANSACTION)
                                .messagePack(ScriptAssembler.typeString, typeValue, ScriptData.Buffer.TRANSACTION).getScript())
                .ifEqual(votefstPresent, "00",
                        "", new ScriptAssembler().messagePack(ScriptAssembler.typeString, votefstKey, ScriptData.Buffer.TRANSACTION)
                                .messagePack(ScriptAssembler.typeInt, votefstValue, ScriptData.Buffer.TRANSACTION).getScript())
                .ifEqual(votekdPresent, "00",
                        "", new ScriptAssembler().messagePack(ScriptAssembler.typeString, votekdKey, ScriptData.Buffer.TRANSACTION)
                                .messagePack(ScriptAssembler.typeInt, votekdValue, ScriptData.Buffer.TRANSACTION).getScript())
                .ifEqual(votekeyPresent, "00",
                        "", new ScriptAssembler().messagePack(ScriptAssembler.typeString, votekeyKey, ScriptData.Buffer.TRANSACTION)
                                .messagePack(ScriptAssembler.typeBinary, votekeyValue, ScriptData.Buffer.TRANSACTION).getScript())
                .ifEqual(votelstPresent, "00",
                        "", new ScriptAssembler().messagePack(ScriptAssembler.typeString, votelstKey, ScriptData.Buffer.TRANSACTION)
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
        ScriptData aparKey = sac.getArgumentRightJustified(8);
        // MetaDataHash
        ScriptData amPresent = sac.getArgument(1);
        ScriptData amKey = sac.getArgumentRightJustified(8);
        ScriptData amValue = sac.getArgumentRightJustified(32);
        // Asset Name
        ScriptData anPresent = sac.getArgument(1);
        ScriptData anKey = sac.getArgumentRightJustified(8);
        ScriptData anValue = sac.getArgumentRightJustified(32);
        // URL
        ScriptData auPresent = sac.getArgument(1);
        ScriptData auKey = sac.getArgumentRightJustified(8);
        ScriptData auValue = sac.getArgumentRightJustified(96);
        // Clawback Address
        ScriptData cPresent = sac.getArgument(1);
        ScriptData cKey = sac.getArgumentRightJustified(8);
        ScriptData cValue = sac.getArgumentRightJustified(32);
        // Decimals
        ScriptData dcPresent = sac.getArgument(1);
        ScriptData dcKey = sac.getArgumentRightJustified(8);
        ScriptData dcValue = sac.getArgumentRightJustified(8);
        // Default Frozen
        ScriptData dfPresent = sac.getArgument(1);
        ScriptData dfKey = sac.getArgumentRightJustified(8);
        ScriptData dfValue = sac.getArgument(1);
        // Freeze Address
        ScriptData fPresent = sac.getArgument(1);
        ScriptData fKey = sac.getArgumentRightJustified(8);
        ScriptData fValue = sac.getArgumentRightJustified(32);
        // Manager Address
        ScriptData mPresent = sac.getArgument(1);
        ScriptData mKey = sac.getArgumentRightJustified(8);
        ScriptData mValue = sac.getArgumentRightJustified(32);
        // Reserve Address
        ScriptData rPresent = sac.getArgument(1);
        ScriptData rKey = sac.getArgumentRightJustified(8);
        ScriptData rValue = sac.getArgumentRightJustified(32);
        // Total
        ScriptData tPresent = sac.getArgument(1);
        ScriptData tKey = sac.getArgumentRightJustified(8);
        ScriptData tValue = sac.getArgumentRightJustified(8);
        // Unit Name
        ScriptData unPresent = sac.getArgument(1);
        ScriptData unKey = sac.getArgumentRightJustified(8);
        ScriptData unValue = sac.getArgumentRightJustified(8);
        // Config Asset
        ScriptData caidPresent = sac.getArgument(1);
        ScriptData caidKey = sac.getArgumentRightJustified(8);
        ScriptData caidValue = sac.getArgumentRightJustified(8);
        // Fee
        ScriptData feePresent = sac.getArgument(1);
        ScriptData feeKey = sac.getArgumentRightJustified(8);
        ScriptData feeValue = sac.getArgumentRightJustified(8);
        // First Valid
        ScriptData fvPresent = sac.getArgument(1);
        ScriptData fvKey = sac.getArgumentRightJustified(8);
        ScriptData fvValue = sac.getArgumentRightJustified(8);
        // GenesisID
        ScriptData genPresent = sac.getArgument(1);
        ScriptData genKey = sac.getArgumentRightJustified(8);
        ScriptData genValue = sac.getArgumentRightJustified(32);
        // Group
        ScriptData grpPresent = sac.getArgument(1);
        ScriptData grpKey = sac.getArgumentRightJustified(8);
        ScriptData grpValue = sac.getArgumentRightJustified(32);
        // Genesis Hash
        ScriptData ghPresent = sac.getArgument(1);
        ScriptData ghKey = sac.getArgumentRightJustified(8);
        ScriptData ghValue = sac.getArgumentRightJustified(32);
        // Last Valid
        ScriptData lvPresent = sac.getArgument(1);
        ScriptData lvKey = sac.getArgumentRightJustified(8);
        ScriptData lvValue = sac.getArgumentRightJustified(8);
        // Lease
        ScriptData lxPresent = sac.getArgument(1);
        ScriptData lxKey = sac.getArgumentRightJustified(8);
        ScriptData lxValue = sac.getArgumentRightJustified(32);
        // Note
        ScriptData notePresent = sac.getArgument(1);
        ScriptData noteKey = sac.getArgumentRightJustified(8);
        ScriptData noteValue = sac.getArgumentRightJustified(1024);
        // Rekey To
        ScriptData rekeyPresent = sac.getArgument(1);
        ScriptData rekeyKey = sac.getArgumentRightJustified(8);
        ScriptData rekeyValue = sac.getArgumentRightJustified(32);
        // Sender
        ScriptData sndPresent = sac.getArgument(1);
        ScriptData sndKey = sac.getArgumentRightJustified(8);
        ScriptData sndValue = sac.getArgumentRightJustified(32);
        // Type
        ScriptData typePresent = sac.getArgument(1);
        ScriptData typeKey = sac.getArgumentRightJustified(8);
        ScriptData typeValue = sac.getArgumentRightJustified(8);

        ScriptAssembler scriptAsb = new ScriptAssembler();
        String script = scriptAsb.setCoinType(0x11B)
                .arrayPointer()
                .copyString(TX)
                .arrayPointer()
                .ifEqual(aparPresent, "00",
                        "", new ScriptAssembler()
                                .messagePack(ScriptAssembler.typeString, aparKey, ScriptData.Buffer.TRANSACTION)
                                .arrayPointer()
                                .ifEqual(amPresent, "00",
                                        "", new ScriptAssembler().messagePack(ScriptAssembler.typeString, amKey, ScriptData.Buffer.TRANSACTION)
                                                .messagePack(ScriptAssembler.typeBinary, amValue, ScriptData.Buffer.TRANSACTION).getScript())
                                .ifEqual(anPresent, "00",
                                        "", new ScriptAssembler().messagePack(ScriptAssembler.typeString, anKey, ScriptData.Buffer.TRANSACTION)
                                                .messagePack(ScriptAssembler.typeString, anValue, ScriptData.Buffer.TRANSACTION).getScript())
                                .ifEqual(auPresent, "00",
                                        "", new ScriptAssembler().messagePack(ScriptAssembler.typeString, auKey, ScriptData.Buffer.TRANSACTION)
                                                .messagePack(ScriptAssembler.typeString, auValue, ScriptData.Buffer.TRANSACTION).getScript())
                                .ifEqual(cPresent, "00",
                                        "", new ScriptAssembler().messagePack(ScriptAssembler.typeString, cKey, ScriptData.Buffer.TRANSACTION)
                                                .messagePack(ScriptAssembler.typeBinary, cValue, ScriptData.Buffer.TRANSACTION).getScript())
                                .ifEqual(dcPresent, "00",
                                        "", new ScriptAssembler().messagePack(ScriptAssembler.typeString, dcKey, ScriptData.Buffer.TRANSACTION)
                                                .messagePack(ScriptAssembler.typeInt, dcValue, ScriptData.Buffer.TRANSACTION).getScript())
                                .ifEqual(dfPresent, "00",
                                        "", new ScriptAssembler().messagePack(ScriptAssembler.typeString, dfKey, ScriptData.Buffer.TRANSACTION)
                                                .messagePack(ScriptAssembler.typeBoolean, dfValue, ScriptData.Buffer.TRANSACTION).getScript())
                                .ifEqual(fPresent, "00",
                                        "", new ScriptAssembler().messagePack(ScriptAssembler.typeString, fKey, ScriptData.Buffer.TRANSACTION)
                                                .messagePack(ScriptAssembler.typeBinary, fValue, ScriptData.Buffer.TRANSACTION).getScript())
                                .ifEqual(mPresent, "00",
                                        "", new ScriptAssembler().messagePack(ScriptAssembler.typeString, mKey, ScriptData.Buffer.TRANSACTION)
                                                .messagePack(ScriptAssembler.typeBinary, mValue, ScriptData.Buffer.TRANSACTION).getScript())
                                .ifEqual(rPresent, "00",
                                        "", new ScriptAssembler().messagePack(ScriptAssembler.typeString, rKey, ScriptData.Buffer.TRANSACTION)
                                                .messagePack(ScriptAssembler.typeBinary, rValue, ScriptData.Buffer.TRANSACTION).getScript())
                                .ifEqual(tPresent, "00",
                                        "", new ScriptAssembler().messagePack(ScriptAssembler.typeString, tKey, ScriptData.Buffer.TRANSACTION)
                                                .messagePack(ScriptAssembler.typeInt, tValue, ScriptData.Buffer.TRANSACTION).getScript())
                                .ifEqual(unPresent, "00",
                                        "", new ScriptAssembler().messagePack(ScriptAssembler.typeString, unKey, ScriptData.Buffer.TRANSACTION)
                                                .messagePack(ScriptAssembler.typeString, unValue, ScriptData.Buffer.TRANSACTION).getScript())
                                .arrayEnd(TYPE_MESSAGE_PACK_MAP)
                                .getScript())
                .ifEqual(caidPresent, "00",
                        "", new ScriptAssembler().messagePack(ScriptAssembler.typeString, caidKey, ScriptData.Buffer.TRANSACTION)
                                .messagePack(ScriptAssembler.typeInt, caidValue, ScriptData.Buffer.TRANSACTION).getScript())
                .ifEqual(feePresent, "00",
                        "", new ScriptAssembler().messagePack(ScriptAssembler.typeString, feeKey, ScriptData.Buffer.TRANSACTION)
                                .messagePack(ScriptAssembler.typeInt, feeValue, ScriptData.Buffer.TRANSACTION).getScript())
                .ifEqual(fvPresent, "00",
                        "", new ScriptAssembler().messagePack(ScriptAssembler.typeString, fvKey, ScriptData.Buffer.TRANSACTION)
                                .messagePack(ScriptAssembler.typeInt, fvValue, ScriptData.Buffer.TRANSACTION).getScript())
                .ifEqual(genPresent, "00",
                        "", new ScriptAssembler().messagePack(ScriptAssembler.typeString, genKey, ScriptData.Buffer.TRANSACTION)
                                .messagePack(ScriptAssembler.typeString, genValue, ScriptData.Buffer.TRANSACTION).getScript())
                .ifEqual(grpPresent, "00",
                        "", new ScriptAssembler().messagePack(ScriptAssembler.typeBinary, grpKey, ScriptData.Buffer.TRANSACTION)
                                .messagePack(ScriptAssembler.typeBinary, grpValue, ScriptData.Buffer.TRANSACTION).getScript())
                .ifEqual(ghPresent, "00",
                        "", new ScriptAssembler().messagePack(ScriptAssembler.typeBinary, ghKey, ScriptData.Buffer.TRANSACTION)
                                .messagePack(ScriptAssembler.typeBinary, ghValue, ScriptData.Buffer.TRANSACTION).getScript())
                .ifEqual(lvPresent, "00",
                        "", new ScriptAssembler().messagePack(ScriptAssembler.typeString, lvKey, ScriptData.Buffer.TRANSACTION)
                                .messagePack(ScriptAssembler.typeInt, lvValue, ScriptData.Buffer.TRANSACTION).getScript())
                .ifEqual(lxPresent, "00",
                        "", new ScriptAssembler().messagePack(ScriptAssembler.typeString, lxKey, ScriptData.Buffer.TRANSACTION)
                                .messagePack(ScriptAssembler.typeBinary, lxValue, ScriptData.Buffer.TRANSACTION).getScript())
                .ifEqual(notePresent, "00",
                        "", new ScriptAssembler().messagePack(ScriptAssembler.typeBinary, noteKey, ScriptData.Buffer.TRANSACTION)
                                .messagePack(ScriptAssembler.typeBinary, noteValue, ScriptData.Buffer.TRANSACTION).getScript())
                .ifEqual(rekeyPresent, "00",
                        "", new ScriptAssembler().messagePack(ScriptAssembler.typeBinary, rekeyKey, ScriptData.Buffer.TRANSACTION)
                                .messagePack(ScriptAssembler.typeBinary, rekeyValue, ScriptData.Buffer.TRANSACTION).getScript())
                .ifEqual(sndPresent, "00",
                        "", new ScriptAssembler().messagePack(ScriptAssembler.typeBinary, sndKey, ScriptData.Buffer.TRANSACTION)
                                .messagePack(ScriptAssembler.typeBinary, sndValue, ScriptData.Buffer.TRANSACTION).getScript())
                .ifEqual(typePresent, "00",
                        "", new ScriptAssembler().messagePack(ScriptAssembler.typeString, typeKey, ScriptData.Buffer.TRANSACTION)
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
        ScriptData aamtKey = sac.getArgumentRightJustified(8);
        ScriptData aamtValue = sac.getArgumentRightJustified(8);
        // Asset Close To
        ScriptData aclosePresent = sac.getArgument(1);
        ScriptData acloseKey = sac.getArgumentRightJustified(8);
        ScriptData acloseValue = sac.getArgumentRightJustified(32);
        // Asset Receiver
        ScriptData arcvPresent = sac.getArgument(1);
        ScriptData arcvKey = sac.getArgumentRightJustified(8);
        ScriptData arcvValue = sac.getArgumentRightJustified(32);
        // Asset Sender
        ScriptData asndPresent = sac.getArgument(1);
        ScriptData asndKey = sac.getArgumentRightJustified(8);
        ScriptData asndValue = sac.getArgumentRightJustified(32);
        // Fee
        ScriptData feePresent = sac.getArgument(1);
        ScriptData feeKey = sac.getArgumentRightJustified(8);
        ScriptData feeValue = sac.getArgumentRightJustified(8);
        // First Valid
        ScriptData fvPresent = sac.getArgument(1);
        ScriptData fvKey = sac.getArgumentRightJustified(8);
        ScriptData fvValue = sac.getArgumentRightJustified(8);
        // GenesisID
        ScriptData genPresent = sac.getArgument(1);
        ScriptData genKey = sac.getArgumentRightJustified(8);
        ScriptData genValue = sac.getArgumentRightJustified(32);
        // Group
        ScriptData grpPresent = sac.getArgument(1);
        ScriptData grpKey = sac.getArgumentRightJustified(8);
        ScriptData grpValue = sac.getArgumentRightJustified(32);
        // Genesis Hash
        ScriptData ghPresent = sac.getArgument(1);
        ScriptData ghKey = sac.getArgumentRightJustified(8);
        ScriptData ghValue = sac.getArgumentRightJustified(32);
        // Last Valid
        ScriptData lvPresent = sac.getArgument(1);
        ScriptData lvKey = sac.getArgumentRightJustified(8);
        ScriptData lvValue = sac.getArgumentRightJustified(8);
        // Lease
        ScriptData lxPresent = sac.getArgument(1);
        ScriptData lxKey = sac.getArgumentRightJustified(8);
        ScriptData lxValue = sac.getArgumentRightJustified(32);
        // Note
        ScriptData notePresent = sac.getArgument(1);
        ScriptData noteKey = sac.getArgumentRightJustified(8);
        ScriptData noteValue = sac.getArgumentRightJustified(1024);
        // Rekey To
        ScriptData rekeyPresent = sac.getArgument(1);
        ScriptData rekeyKey = sac.getArgumentRightJustified(8);
        ScriptData rekeyValue = sac.getArgumentRightJustified(32);
        // Sender
        ScriptData sndPresent = sac.getArgument(1);
        ScriptData sndKey = sac.getArgumentRightJustified(8);
        ScriptData sndValue = sac.getArgumentRightJustified(32);
        // Type
        ScriptData typePresent = sac.getArgument(1);
        ScriptData typeKey = sac.getArgumentRightJustified(8);
        ScriptData typeValue = sac.getArgumentRightJustified(8);
        // Xfer Asset
        ScriptData xaidPresent = sac.getArgument(1);
        ScriptData xaidKey = sac.getArgumentRightJustified(8);
        ScriptData xaidValue = sac.getArgumentRightJustified(8);

        ScriptAssembler scriptAsb = new ScriptAssembler();
        String script = scriptAsb.setCoinType(0x11B)
                .arrayPointer()
                .copyString(TX)
                .arrayPointer()
                .ifEqual(aamtPresent, "00",
                        "", new ScriptAssembler().messagePack(ScriptAssembler.typeString, aamtKey, ScriptData.Buffer.TRANSACTION)
                                .messagePack(ScriptAssembler.typeInt, aamtValue, ScriptData.Buffer.TRANSACTION).getScript())
                .ifEqual(aclosePresent, "00",
                        "", new ScriptAssembler().messagePack(ScriptAssembler.typeString, acloseKey, ScriptData.Buffer.TRANSACTION)
                                .messagePack(ScriptAssembler.typeBinary, acloseValue, ScriptData.Buffer.TRANSACTION).getScript())
                .ifEqual(arcvPresent, "00",
                        "", new ScriptAssembler().messagePack(ScriptAssembler.typeString, arcvKey, ScriptData.Buffer.TRANSACTION)
                                .messagePack(ScriptAssembler.typeBinary, arcvValue, ScriptData.Buffer.TRANSACTION).getScript())
                .ifEqual(asndPresent, "00",
                        "", new ScriptAssembler().messagePack(ScriptAssembler.typeString, asndKey, ScriptData.Buffer.TRANSACTION)
                                .messagePack(ScriptAssembler.typeBinary, asndValue, ScriptData.Buffer.TRANSACTION).getScript())
                .ifEqual(feePresent, "00",
                        "", new ScriptAssembler().messagePack(ScriptAssembler.typeString, feeKey, ScriptData.Buffer.TRANSACTION)
                                .messagePack(ScriptAssembler.typeInt, feeValue, ScriptData.Buffer.TRANSACTION).getScript())
                .ifEqual(fvPresent, "00",
                        "", new ScriptAssembler().messagePack(ScriptAssembler.typeString, fvKey, ScriptData.Buffer.TRANSACTION)
                                .messagePack(ScriptAssembler.typeInt, fvValue, ScriptData.Buffer.TRANSACTION).getScript())
                .ifEqual(genPresent, "00",
                        "", new ScriptAssembler().messagePack(ScriptAssembler.typeString, genKey, ScriptData.Buffer.TRANSACTION)
                                .messagePack(ScriptAssembler.typeString, genValue, ScriptData.Buffer.TRANSACTION).getScript())
                .ifEqual(grpPresent, "00",
                        "", new ScriptAssembler().messagePack(ScriptAssembler.typeString, grpKey, ScriptData.Buffer.TRANSACTION)
                                .messagePack(ScriptAssembler.typeBinary, grpValue, ScriptData.Buffer.TRANSACTION).getScript())
                .ifEqual(ghPresent, "00",
                        "", new ScriptAssembler().messagePack(ScriptAssembler.typeString, ghKey, ScriptData.Buffer.TRANSACTION)
                                .messagePack(ScriptAssembler.typeBinary, ghValue, ScriptData.Buffer.TRANSACTION).getScript())
                .ifEqual(lvPresent, "00",
                        "", new ScriptAssembler().messagePack(ScriptAssembler.typeString, lvKey, ScriptData.Buffer.TRANSACTION)
                                .messagePack(ScriptAssembler.typeInt, lvValue, ScriptData.Buffer.TRANSACTION).getScript())
                .ifEqual(lxPresent, "00",
                        "", new ScriptAssembler().messagePack(ScriptAssembler.typeString, lxKey, ScriptData.Buffer.TRANSACTION)
                                .messagePack(ScriptAssembler.typeBinary, lxValue, ScriptData.Buffer.TRANSACTION).getScript())
                .ifEqual(notePresent, "00",
                        "", new ScriptAssembler().messagePack(ScriptAssembler.typeString, noteKey, ScriptData.Buffer.TRANSACTION)
                                .messagePack(ScriptAssembler.typeBinary, noteValue, ScriptData.Buffer.TRANSACTION).getScript())
                .ifEqual(rekeyPresent, "00",
                        "", new ScriptAssembler().messagePack(ScriptAssembler.typeString, rekeyKey, ScriptData.Buffer.TRANSACTION)
                                .messagePack(ScriptAssembler.typeBinary, rekeyValue, ScriptData.Buffer.TRANSACTION).getScript())
                .ifEqual(sndPresent, "00",
                        "", new ScriptAssembler().messagePack(ScriptAssembler.typeString, sndKey, ScriptData.Buffer.TRANSACTION)
                                .messagePack(ScriptAssembler.typeBinary, sndValue, ScriptData.Buffer.TRANSACTION).getScript())
                .ifEqual(typePresent, "00",
                        "", new ScriptAssembler().messagePack(ScriptAssembler.typeString, typeKey, ScriptData.Buffer.TRANSACTION)
                                .messagePack(ScriptAssembler.typeString, typeValue, ScriptData.Buffer.TRANSACTION).getScript())
                .ifEqual(xaidPresent, "00",
                        "", new ScriptAssembler().messagePack(ScriptAssembler.typeString, xaidKey, ScriptData.Buffer.TRANSACTION)
                                .messagePack(ScriptAssembler.typeInt, xaidValue, ScriptData.Buffer.TRANSACTION).getScript())
                .arrayEnd(TYPE_MESSAGE_PACK_MAP)
                .showMessage("ALGO")
                .showMessage("axfer")
                .ifEqual(xaidPresent, "00",
                        "", new ScriptAssembler().showMessage("assetID").showAmount(xaidValue, 0).getScript())
                .ifEqual(arcvPresent, "00",
                        "", new ScriptAssembler().hash(arcvValue, ScriptData.Buffer.CACHE1, ScriptAssembler.HashType.SHA512256)
                                .copyArgument(arcvValue, ScriptData.Buffer.CACHE2)
                                .copyArgument(ScriptData.getBuffer(ScriptData.Buffer.CACHE1, 28, 4), ScriptData.Buffer.CACHE2)
                                .clearBuffer(ScriptData.Buffer.CACHE1)
                                .copyString(HexUtil.toHexString("ABCDEFGHIJKLMNOPQRSTUVWXYZ234567"), ScriptData.Buffer.CACHE1)
                                .baseConvert(ScriptData.getBuffer(ScriptData.Buffer.CACHE2, 0, 36), ScriptData.Buffer.CACHE2, 58, ScriptAssembler.extendedCharset, ScriptAssembler.bitLeftJustify8to5)
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
        ScriptData afrzKey = sac.getArgumentRightJustified(8);
        ScriptData afrzValue = sac.getArgument(1);
        // Freeze Account
        ScriptData faddPresent = sac.getArgument(1);
        ScriptData faddKey = sac.getArgumentRightJustified(8);
        ScriptData faddValue = sac.getArgumentRightJustified(32);
        // Freeze Asset
        ScriptData faidPresent = sac.getArgument(1);
        ScriptData faidKey = sac.getArgumentRightJustified(8);
        ScriptData faidValue = sac.getArgumentRightJustified(8);
        // Fee
        ScriptData feePresent = sac.getArgument(1);
        ScriptData feeKey = sac.getArgumentRightJustified(8);
        ScriptData feeValue = sac.getArgumentRightJustified(8);
        // First Valid
        ScriptData fvPresent = sac.getArgument(1);
        ScriptData fvKey = sac.getArgumentRightJustified(8);
        ScriptData fvValue = sac.getArgumentRightJustified(8);
        // GenesisID
        ScriptData genPresent = sac.getArgument(1);
        ScriptData genKey = sac.getArgumentRightJustified(8);
        ScriptData genValue = sac.getArgumentRightJustified(32);
        // Group
        ScriptData grpPresent = sac.getArgument(1);
        ScriptData grpKey = sac.getArgumentRightJustified(8);
        ScriptData grpValue = sac.getArgumentRightJustified(32);
        // Genesis Hash
        ScriptData ghPresent = sac.getArgument(1);
        ScriptData ghKey = sac.getArgumentRightJustified(8);
        ScriptData ghValue = sac.getArgumentRightJustified(32);
        // Last Valid
        ScriptData lvPresent = sac.getArgument(1);
        ScriptData lvKey = sac.getArgumentRightJustified(8);
        ScriptData lvValue = sac.getArgumentRightJustified(8);
        // Lease
        ScriptData lxPresent = sac.getArgument(1);
        ScriptData lxKey = sac.getArgumentRightJustified(8);
        ScriptData lxValue = sac.getArgumentRightJustified(32);
        // Note
        ScriptData notePresent = sac.getArgument(1);
        ScriptData noteKey = sac.getArgumentRightJustified(8);
        ScriptData noteValue = sac.getArgumentRightJustified(1024);
        // Rekey To
        ScriptData rekeyPresent = sac.getArgument(1);
        ScriptData rekeyKey = sac.getArgumentRightJustified(8);
        ScriptData rekeyValue = sac.getArgumentRightJustified(32);
        // Sender
        ScriptData sndPresent = sac.getArgument(1);
        ScriptData sndKey = sac.getArgumentRightJustified(8);
        ScriptData sndValue = sac.getArgumentRightJustified(32);
        // Type
        ScriptData typePresent = sac.getArgument(1);
        ScriptData typeKey = sac.getArgumentRightJustified(8);
        ScriptData typeValue = sac.getArgumentRightJustified(8);

        ScriptAssembler scriptAsb = new ScriptAssembler();
        String script = scriptAsb.setCoinType(0x11B)
                .arrayPointer()
                .copyString(TX)
                .arrayPointer()
                .ifEqual(afrzPresent, "00",
                        "", new ScriptAssembler().messagePack(ScriptAssembler.typeString, afrzKey, ScriptData.Buffer.TRANSACTION)
                                .messagePack(ScriptAssembler.typeBoolean, afrzValue, ScriptData.Buffer.TRANSACTION).getScript())
                .ifEqual(faddPresent, "00",
                        "", new ScriptAssembler().messagePack(ScriptAssembler.typeString, faddKey, ScriptData.Buffer.TRANSACTION)
                                .messagePack(ScriptAssembler.typeBinary, faddValue, ScriptData.Buffer.TRANSACTION).getScript())
                .ifEqual(faidPresent, "00",
                        "", new ScriptAssembler().messagePack(ScriptAssembler.typeString, faidKey, ScriptData.Buffer.TRANSACTION)
                                .messagePack(ScriptAssembler.typeInt, faidValue, ScriptData.Buffer.TRANSACTION).getScript())
                .ifEqual(feePresent, "00",
                        "", new ScriptAssembler().messagePack(ScriptAssembler.typeString, feeKey, ScriptData.Buffer.TRANSACTION)
                                .messagePack(ScriptAssembler.typeInt, feeValue, ScriptData.Buffer.TRANSACTION).getScript())
                .ifEqual(fvPresent, "00",
                        "", new ScriptAssembler().messagePack(ScriptAssembler.typeString, fvKey, ScriptData.Buffer.TRANSACTION)
                                .messagePack(ScriptAssembler.typeInt, fvValue, ScriptData.Buffer.TRANSACTION).getScript())
                .ifEqual(genPresent, "00",
                        "", new ScriptAssembler().messagePack(ScriptAssembler.typeString, genKey, ScriptData.Buffer.TRANSACTION)
                                .messagePack(ScriptAssembler.typeString, genValue, ScriptData.Buffer.TRANSACTION).getScript())
                .ifEqual(grpPresent, "00",
                        "", new ScriptAssembler().messagePack(ScriptAssembler.typeString, grpKey, ScriptData.Buffer.TRANSACTION)
                                .messagePack(ScriptAssembler.typeBinary, grpValue, ScriptData.Buffer.TRANSACTION).getScript())
                .ifEqual(ghPresent, "00",
                        "", new ScriptAssembler().messagePack(ScriptAssembler.typeString, ghKey, ScriptData.Buffer.TRANSACTION)
                                .messagePack(ScriptAssembler.typeBinary, ghValue, ScriptData.Buffer.TRANSACTION).getScript())
                .ifEqual(lvPresent, "00",
                        "", new ScriptAssembler().messagePack(ScriptAssembler.typeString, lvKey, ScriptData.Buffer.TRANSACTION)
                                .messagePack(ScriptAssembler.typeInt, lvValue, ScriptData.Buffer.TRANSACTION).getScript())
                .ifEqual(lxPresent, "00",
                        "", new ScriptAssembler().messagePack(ScriptAssembler.typeString, lxKey, ScriptData.Buffer.TRANSACTION)
                                .messagePack(ScriptAssembler.typeBinary, lxValue, ScriptData.Buffer.TRANSACTION).getScript())
                .ifEqual(notePresent, "00",
                        "", new ScriptAssembler().messagePack(ScriptAssembler.typeString, noteKey, ScriptData.Buffer.TRANSACTION)
                                .messagePack(ScriptAssembler.typeBinary, noteValue, ScriptData.Buffer.TRANSACTION).getScript())
                .ifEqual(rekeyPresent, "00",
                        "", new ScriptAssembler().messagePack(ScriptAssembler.typeString, rekeyKey, ScriptData.Buffer.TRANSACTION)
                                .messagePack(ScriptAssembler.typeBinary, rekeyValue, ScriptData.Buffer.TRANSACTION).getScript())
                .ifEqual(sndPresent, "00",
                        "", new ScriptAssembler().messagePack(ScriptAssembler.typeString, sndKey, ScriptData.Buffer.TRANSACTION)
                                .messagePack(ScriptAssembler.typeBinary, sndValue, ScriptData.Buffer.TRANSACTION).getScript())
                .ifEqual(typePresent, "00",
                        "", new ScriptAssembler().messagePack(ScriptAssembler.typeString, typeKey, ScriptData.Buffer.TRANSACTION)
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
        ScriptData apaaKey = sac.getArgumentRightJustified(8);
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
        ScriptData apanKey = sac.getArgumentRightJustified(8);
        ScriptData apanValue = sac.getArgumentRightJustified(32);
        // Aproval Program
        ScriptData apapPresent = sac.getArgument(1);
        ScriptData apapKey = sac.getArgumentRightJustified(8);
        ScriptData apapValue = sac.getArgumentRightJustified(8192);

        // Foreign Asset
        ScriptData apasPresent = sac.getArgument(1);
        ScriptData apasKey = sac.getArgumentRightJustified(8);
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
        ScriptData apatKey = sac.getArgumentRightJustified(8);
        // Accounts 1
        ScriptData apatValue1Present = sac.getArgument(1);
        ScriptData apatValue1 = sac.getArgumentRightJustified(64);
        // Accounts 2
        ScriptData apatValue2Present = sac.getArgument(1);
        ScriptData apatValue2 = sac.getArgumentRightJustified(64);
        // Accounts 3
        ScriptData apatValue3Present = sac.getArgument(1);
        ScriptData apatValue3 = sac.getArgumentRightJustified(64);
        // Accounts 4
        ScriptData apatValue4Present = sac.getArgument(1);
        ScriptData apatValue4 = sac.getArgumentRightJustified(64);
        // Accounts 5
        ScriptData apatValue5Present = sac.getArgument(1);
        ScriptData apatValue5 = sac.getArgumentRightJustified(64);
        // Accounts 6
        ScriptData apatValue6Present = sac.getArgument(1);
        ScriptData apatValue6 = sac.getArgumentRightJustified(64);
        // Accounts 7
        ScriptData apatValue7Present = sac.getArgument(1);
        ScriptData apatValue7 = sac.getArgumentRightJustified(64);
        // Accounts 8
        ScriptData apatValue8Present = sac.getArgument(1);
        ScriptData apatValue8 = sac.getArgumentRightJustified(64);

        // Extra Program Pages
        ScriptData apepPresent = sac.getArgument(1);
        ScriptData apepKey = sac.getArgumentRightJustified(8);
        ScriptData apepValue = sac.getArgumentRightJustified(8);

        // Foreign App
        ScriptData apfaPresent = sac.getArgument(1);
        ScriptData apfaKey = sac.getArgumentRightJustified(8);
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
        ScriptData apidKey = sac.getArgumentRightJustified(8);
        ScriptData apidValue = sac.getArgumentRightJustified(8);

        // Local State Schema
        ScriptData aplsPresent = sac.getArgument(1);
        ScriptData aplsKey = sac.getArgumentRightJustified(8);
        // Number Byte Slices
        ScriptData lnbsPresent = sac.getArgument(1);
        ScriptData lnbsKey = sac.getArgumentRightJustified(8);
        ScriptData lnbsValue = sac.getArgumentRightJustified(8);
        // Number Ints
        ScriptData lnuiPresent = sac.getArgument(1);
        ScriptData lnuiKey = sac.getArgumentRightJustified(8);
        ScriptData lnuiValue = sac.getArgumentRightJustified(8);

        // Global State Schema
        ScriptData apgsPresent = sac.getArgument(1);
        ScriptData apgsKey = sac.getArgumentRightJustified(8);
        // Number Byte Slices
        ScriptData gnbsPresent = sac.getArgument(1);
        ScriptData gnbsKey = sac.getArgumentRightJustified(8);
        ScriptData gnbsValue = sac.getArgumentRightJustified(8);
        // Number Ints
        ScriptData gnuiPresent = sac.getArgument(1);
        ScriptData gnuiKey = sac.getArgumentRightJustified(8);
        ScriptData gnuiValue = sac.getArgumentRightJustified(8);

        // Clear State Program
        ScriptData apsuPresent = sac.getArgument(1);
        ScriptData apsuKey = sac.getArgumentRightJustified(8);
        ScriptData apsuValue = sac.getArgumentRightJustified(8192);

        // Fee
        ScriptData feePresent = sac.getArgument(1);
        ScriptData feeKey = sac.getArgumentRightJustified(8);
        ScriptData feeValue = sac.getArgumentRightJustified(8);
        // First Valid
        ScriptData fvPresent = sac.getArgument(1);
        ScriptData fvKey = sac.getArgumentRightJustified(8);
        ScriptData fvValue = sac.getArgumentRightJustified(8);
        // GenesisID
        ScriptData genPresent = sac.getArgument(1);
        ScriptData genKey = sac.getArgumentRightJustified(8);
        ScriptData genValue = sac.getArgumentRightJustified(32);
        // Group
        ScriptData grpPresent = sac.getArgument(1);
        ScriptData grpKey = sac.getArgumentRightJustified(8);
        ScriptData grpValue = sac.getArgumentRightJustified(32);
        // Genesis Hash
        ScriptData ghPresent = sac.getArgument(1);
        ScriptData ghKey = sac.getArgumentRightJustified(8);
        ScriptData ghValue = sac.getArgumentRightJustified(32);
        // Last Valid
        ScriptData lvPresent = sac.getArgument(1);
        ScriptData lvKey = sac.getArgumentRightJustified(8);
        ScriptData lvValue = sac.getArgumentRightJustified(8);
        // Lease
        ScriptData lxPresent = sac.getArgument(1);
        ScriptData lxKey = sac.getArgumentRightJustified(8);
        ScriptData lxValue = sac.getArgumentRightJustified(32);
        // Note
        ScriptData notePresent = sac.getArgument(1);
        ScriptData noteKey = sac.getArgumentRightJustified(8);
        ScriptData noteValue = sac.getArgumentRightJustified(1024);
        // Rekey To
        ScriptData rekeyPresent = sac.getArgument(1);
        ScriptData rekeyKey = sac.getArgumentRightJustified(8);
        ScriptData rekeyValue = sac.getArgumentRightJustified(32);
        // Sender
        ScriptData sndPresent = sac.getArgument(1);
        ScriptData sndKey = sac.getArgumentRightJustified(8);
        ScriptData sndValue = sac.getArgumentRightJustified(32);
        // Type
        ScriptData typePresent = sac.getArgument(1);
        ScriptData typeKey = sac.getArgumentRightJustified(8);
        ScriptData typeValue = sac.getArgumentRightJustified(8);

        ScriptAssembler scriptAsb = new ScriptAssembler();
        String script = scriptAsb.setCoinType(0x11B)
                .arrayPointer()
                .copyString(TX)
                .arrayPointer()
                .ifEqual(apaaPresent, "00",
                        "", new ScriptAssembler()
                                .messagePack(ScriptAssembler.typeString, apaaKey, ScriptData.Buffer.TRANSACTION)
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
                        "", new ScriptAssembler().messagePack(ScriptAssembler.typeString, apanKey, ScriptData.Buffer.TRANSACTION)
                                .messagePack(ScriptAssembler.typeInt, apanValue, ScriptData.Buffer.TRANSACTION).getScript())
                .ifEqual(apapPresent, "00",
                        "", new ScriptAssembler().messagePack(ScriptAssembler.typeString, apapKey, ScriptData.Buffer.TRANSACTION)
                                .messagePack(ScriptAssembler.typeBinary, apapValue, ScriptData.Buffer.TRANSACTION).getScript())
                .ifEqual(apasPresent, "00",
                        "", new ScriptAssembler()
                                .messagePack(ScriptAssembler.typeString, apasKey, ScriptData.Buffer.TRANSACTION)
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
                                .messagePack(ScriptAssembler.typeString, apatKey, ScriptData.Buffer.TRANSACTION)
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
                        "", new ScriptAssembler().messagePack(ScriptAssembler.typeString, apepKey, ScriptData.Buffer.TRANSACTION)
                                .messagePack(ScriptAssembler.typeInt, apepValue, ScriptData.Buffer.TRANSACTION).getScript())
                .ifEqual(apfaPresent, "00",
                        "", new ScriptAssembler()
                                .messagePack(ScriptAssembler.typeString, apfaKey, ScriptData.Buffer.TRANSACTION)
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
                        "", new ScriptAssembler().messagePack(ScriptAssembler.typeString, apidKey, ScriptData.Buffer.TRANSACTION)
                                .messagePack(ScriptAssembler.typeInt, apidValue, ScriptData.Buffer.TRANSACTION).getScript())
                .ifEqual(aplsPresent, "00",
                        "", new ScriptAssembler()
                                .messagePack(ScriptAssembler.typeString, aplsKey, ScriptData.Buffer.TRANSACTION)
                                .arrayPointer()
                                .ifEqual(lnbsPresent, "00",
                                        "", new ScriptAssembler().messagePack(ScriptAssembler.typeString, lnbsKey, ScriptData.Buffer.TRANSACTION)
                                                .messagePack(ScriptAssembler.typeInt, lnbsValue, ScriptData.Buffer.TRANSACTION).getScript())
                                .ifEqual(lnuiPresent, "00",
                                        "", new ScriptAssembler().messagePack(ScriptAssembler.typeString, lnuiKey, ScriptData.Buffer.TRANSACTION)
                                                .messagePack(ScriptAssembler.typeInt, lnuiValue, ScriptData.Buffer.TRANSACTION).getScript())
                                .arrayEnd(TYPE_MESSAGE_PACK_MAP)
                                .getScript())
                .ifEqual(apgsPresent, "00",
                        "", new ScriptAssembler()
                                .messagePack(ScriptAssembler.typeString, apgsKey, ScriptData.Buffer.TRANSACTION)
                                .arrayPointer()
                                .ifEqual(gnbsPresent, "00",
                                        "", new ScriptAssembler().messagePack(ScriptAssembler.typeString, gnbsKey, ScriptData.Buffer.TRANSACTION)
                                                .messagePack(ScriptAssembler.typeInt, gnbsValue, ScriptData.Buffer.TRANSACTION).getScript())
                                .ifEqual(gnuiPresent, "00",
                                        "", new ScriptAssembler().messagePack(ScriptAssembler.typeString, gnuiKey, ScriptData.Buffer.TRANSACTION)
                                                .messagePack(ScriptAssembler.typeInt, gnuiValue, ScriptData.Buffer.TRANSACTION).getScript())
                                .arrayEnd(TYPE_MESSAGE_PACK_MAP)
                                .getScript())
                .ifEqual(apsuPresent, "00",
                        "", new ScriptAssembler().messagePack(ScriptAssembler.typeString, apsuKey, ScriptData.Buffer.TRANSACTION)
                                .messagePack(ScriptAssembler.typeBinary, apsuValue, ScriptData.Buffer.TRANSACTION).getScript())
                .ifEqual(feePresent, "00",
                        "", new ScriptAssembler().messagePack(ScriptAssembler.typeString, feeKey, ScriptData.Buffer.TRANSACTION)
                                .messagePack(ScriptAssembler.typeInt, feeValue, ScriptData.Buffer.TRANSACTION).getScript())
                .ifEqual(fvPresent, "00",
                        "", new ScriptAssembler().messagePack(ScriptAssembler.typeString, fvKey, ScriptData.Buffer.TRANSACTION)
                                .messagePack(ScriptAssembler.typeInt, fvValue, ScriptData.Buffer.TRANSACTION).getScript())
                .ifEqual(genPresent, "00",
                        "", new ScriptAssembler().messagePack(ScriptAssembler.typeString, genKey, ScriptData.Buffer.TRANSACTION)
                                .messagePack(ScriptAssembler.typeString, genValue, ScriptData.Buffer.TRANSACTION).getScript())
                .ifEqual(grpPresent, "00",
                        "", new ScriptAssembler().messagePack(ScriptAssembler.typeString, grpKey, ScriptData.Buffer.TRANSACTION)
                                .messagePack(ScriptAssembler.typeBinary, grpValue, ScriptData.Buffer.TRANSACTION).getScript())
                .ifEqual(ghPresent, "00",
                        "", new ScriptAssembler().messagePack(ScriptAssembler.typeString, ghKey, ScriptData.Buffer.TRANSACTION)
                                .messagePack(ScriptAssembler.typeBinary, ghValue, ScriptData.Buffer.TRANSACTION).getScript())
                .ifEqual(lvPresent, "00",
                        "", new ScriptAssembler().messagePack(ScriptAssembler.typeString, lvKey, ScriptData.Buffer.TRANSACTION)
                                .messagePack(ScriptAssembler.typeInt, lvValue, ScriptData.Buffer.TRANSACTION).getScript())
                .ifEqual(lxPresent, "00",
                        "", new ScriptAssembler().messagePack(ScriptAssembler.typeString, lxKey, ScriptData.Buffer.TRANSACTION)
                                .messagePack(ScriptAssembler.typeBinary, lxValue, ScriptData.Buffer.TRANSACTION).getScript())
                .ifEqual(notePresent, "00",
                        "", new ScriptAssembler().messagePack(ScriptAssembler.typeString, noteKey, ScriptData.Buffer.TRANSACTION)
                                .messagePack(ScriptAssembler.typeBinary, noteValue, ScriptData.Buffer.TRANSACTION).getScript())
                .ifEqual(rekeyPresent, "00",
                        "", new ScriptAssembler().messagePack(ScriptAssembler.typeString, rekeyKey, ScriptData.Buffer.TRANSACTION)
                                .messagePack(ScriptAssembler.typeBinary, rekeyValue, ScriptData.Buffer.TRANSACTION).getScript())
                .ifEqual(sndPresent, "00",
                        "", new ScriptAssembler().messagePack(ScriptAssembler.typeString, sndKey, ScriptData.Buffer.TRANSACTION)
                                .messagePack(ScriptAssembler.typeBinary, sndValue, ScriptData.Buffer.TRANSACTION).getScript())
                .ifEqual(typePresent, "00",
                        "", new ScriptAssembler().messagePack(ScriptAssembler.typeString, typeKey, ScriptData.Buffer.TRANSACTION)
                                .messagePack(ScriptAssembler.typeString, typeValue, ScriptData.Buffer.TRANSACTION).getScript())
                .arrayEnd(TYPE_MESSAGE_PACK_MAP)
                .showMessage("ALGO")
                .showMessage("appl")
                .ifEqual(apidPresent, "00",
                        "", new ScriptAssembler().showMessage("appID").showAmount(apidValue, 0).getScript())
                .showPressButton()
                .setHeader(ScriptAssembler.HashType.NONE, ScriptAssembler.SignType.EDDSA)
                .getScript();
        return script;
    }

    public static String getAddressScript() {
        String TX = HexUtil.toHexString("TX".getBytes());
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptData rcvPresent = sac.getArgument(1);
        ScriptData rcvKey = sac.getArgumentRightJustified(8);
        ScriptData rcvValue = sac.getArgumentRightJustified(32);

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
                .ifEqual(rcvPresent, "00",
                        "", new ScriptAssembler().hash(rcvValue, ScriptData.Buffer.CACHE1, ScriptAssembler.HashType.SHA512256)
                                .copyArgument(rcvValue, ScriptData.Buffer.CACHE2)
                                .copyArgument(ScriptData.getBuffer(ScriptData.Buffer.CACHE1, 28, 4), ScriptData.Buffer.CACHE2)
                                .clearBuffer(ScriptData.Buffer.CACHE1)
                                .copyString(HexUtil.toHexString("ABCDEFGHIJKLMNOPQRSTUVWXYZ234567"), ScriptData.Buffer.CACHE1)
                                .baseConvert(ScriptData.getBuffer(ScriptData.Buffer.CACHE2, 0, 36), ScriptData.Buffer.CACHE2, 58, ScriptAssembler.extendedCharset, ScriptAssembler.bitLeftJustify8to5)
                                .showAddress(ScriptData.getDataBufferAll(ScriptData.Buffer.CACHE2, 36))
                                .clearBuffer(ScriptData.Buffer.CACHE1)
                                .clearBuffer(ScriptData.Buffer.CACHE2).getScript())
                .showPressButton()
                .setHeader(ScriptAssembler.HashType.NONE, ScriptAssembler.SignType.EDDSA)
                .getScript();
        return script;
    }

}
