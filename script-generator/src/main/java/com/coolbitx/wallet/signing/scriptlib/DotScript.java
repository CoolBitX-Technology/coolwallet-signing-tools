/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.coolbitx.wallet.signing.scriptlib;

import com.coolbitx.wallet.signing.utils.HexUtil;
import com.coolbitx.wallet.signing.utils.ScriptArgumentComposer;
import com.coolbitx.wallet.signing.utils.ScriptAssembler;
import com.coolbitx.wallet.signing.utils.ScriptAssembler.HashType;
import com.coolbitx.wallet.signing.utils.ScriptAssembler.SignType;
import com.coolbitx.wallet.signing.utils.ScriptData;
import com.coolbitx.wallet.signing.utils.ScriptData.Buffer;
import com.google.common.base.Strings;

public class DotScript {

    public static void main(String[] args) {
        listAll();
    }

    public static void listAll() {
        System.out.println("Dot: \n" + getDOTScript() + "\n");
        System.out.println("Dot Bond: \n" + getDOTBondScript() + "\n");
        System.out.println("Dot Bond Extra: \n" + getDOTBondExtraScript() + "\n");
        System.out.println("Dot Unbond: \n" + getDOTUnbondScript() + "\n");
        System.out.println("Dot Nominate Single Hash: \n" + getDOTNominateSingleHashScript() + "\n");
        System.out.println("Dot Nominate Double Hash: \n" + getDOTNominateDoubleHashScript() + "\n");
        System.out.println("Dot Withdraw: \n" + getDOTWithdrawScript() + "\n");
        System.out.println("Dot Chill: \n" + getDOTChillScript() + "\n");

        System.out.println("Ksm: \n" + getKSMScript() + "\n");
        System.out.println("Ksm Bond: \n" + getKSMBondScript() + "\n");
        System.out.println("Ksm Bond Extra: \n" + getKSMBondExtraScript() + "\n");
        System.out.println("Ksm Unbond: \n" + getKSMUnbondScript() + "\n");
        System.out.println("Ksm Nominate Single Hash: \n" + getKSMNominateSingleHashScript() + "\n");
        System.out.println("Ksm Nominate Double Hash: \n" + getKSMNominateDoubleHashScript() + "\n");
        System.out.println("Ksm Withdraw: \n" + getKSMWithdrawScript() + "\n");
        System.out.println("Ksm Chill: \n" + getKSMChillScript() + "\n");
    }

    public static String getDOTScript() {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptData argCallIndex = sac.getArgument(2);
        ScriptData argToAddr = sac.getArgument(32);
        ScriptData argAmount = sac.getArgumentRightJustified(10);
        ScriptData argMortalEra = sac.getArgument(2);
        ScriptData argNonce = sac.getArgumentRightJustified(5);
        ScriptData argTip = sac.getArgumentRightJustified(5);
        ScriptData argAssetIdLength = sac.getArgument(1);
        ScriptData argAssetId = sac.getArgumentVariableLength(4);
        ScriptData argMode = sac.getArgument(1);
        ScriptData argSpecVer = sac.getArgument(4);
        ScriptData argTxVer = sac.getArgument(4);
        ScriptData argGenesisHash = sac.getArgument(32);
        ScriptData argBlockHash = sac.getArgument(32);
        ScriptData argMetaDataHash = sac.getArgument(32);

        String script = new ScriptAssembler()
            // set coinType to 0162
            .setCoinType(0x0162)
            // call index
            .copyArgument(argCallIndex)
            // dest address
            .copyString("00").copyArgument(argToAddr)
            // value
            .scaleEncode(argAmount, Buffer.TRANSACTION)
            // MortalEra
            .copyArgument(argMortalEra)
            // nonce
            .scaleEncode(argNonce, Buffer.TRANSACTION)
            // tip
            .scaleEncode(argTip, Buffer.TRANSACTION)
            // AssetId
            .ifEqual(argAssetIdLength, "00", new ScriptAssembler().copyString("00").getScript(),
                new ScriptAssembler().copyString("01").setBufferInt(argAssetIdLength, 0, 4)
                    .scaleEncode(argAssetId, Buffer.TRANSACTION).getScript())
            // mode
            .copyArgument(argMode)
            // spec ver
            .copyArgument(argSpecVer)
            // tx ver
            .copyArgument(argTxVer)
            // genesis hash
            .copyArgument(argGenesisHash)
            // block hash
            .copyArgument(argBlockHash)
            // metaDataHash
            .ifEqual(argMetaDataHash, "0000000000000000000000000000000000000000000000000000000000000000",
                new ScriptAssembler().copyString("00").getScript(),
                new ScriptAssembler().copyString("01").copyArgument(argMetaDataHash).getScript())
            .showMessage("DOT").copyString(HexUtil.toHexString("SS58PRE".getBytes()), Buffer.CACHE2)
            .copyString("00", Buffer.CACHE2).copyArgument(argToAddr, Buffer.CACHE2)
            .hash(ScriptData.getDataBufferAll(Buffer.CACHE2), Buffer.CACHE2, HashType.Blake2b512)
            .baseConvert(ScriptData.getBuffer(Buffer.CACHE2, 7, 35), Buffer.CACHE1, 0, ScriptAssembler.base58Charset,
                ScriptAssembler.zeroInherit)
            .showAddress(ScriptData.getDataBufferAll(Buffer.CACHE1)).showAmount(argAmount, 10).showPressButton()
            // version=02 ScriptAssembler.hash=0E=ScriptAssembler.Blake2b256 sign=01=ECDSA
            .setHeader(HashType.Blake2b256, SignType.ECDSA).getScript();
        return script;
    }

    public static String DOTScriptSignature = Strings.padStart(
        "3044022062cd0da208b6e10d2c206fb3a06220a127a88b394be8e371895e8b7de34ec85602202d3e7f3bfed7ddcb886a0784faed2b99ca70c118e8d3654eb49817188e9736de",
        144, '0');

    public static String getDOTBondScript() {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptData argCallIndex = sac.getArgument(2);
        ScriptData argAmount = sac.getArgumentRightJustified(10);
        ScriptData argPayeeType = sac.getArgument(1);
        ScriptData argMortalEra = sac.getArgument(2);
        ScriptData argNonce = sac.getArgumentRightJustified(5);
        ScriptData argTip = sac.getArgumentRightJustified(5);
        ScriptData argAssetIdLength = sac.getArgument(1);
        ScriptData argAssetId = sac.getArgumentVariableLength(4);
        ScriptData argMode = sac.getArgument(1);
        ScriptData argSpecVer = sac.getArgument(4);
        ScriptData argTxVer = sac.getArgument(4);
        ScriptData argGenesisHash = sac.getArgument(32);
        ScriptData argBlockHash = sac.getArgument(32);
        ScriptData argMetaDataHash = sac.getArgument(32);

        String script = new ScriptAssembler()
            // set coinType to 0162
            .setCoinType(0x0162)
            // call index
            .copyArgument(argCallIndex)
            // value
            .scaleEncode(argAmount, Buffer.TRANSACTION)
            // payeeType
            .copyArgument(argPayeeType)
            // MortalEra
            .copyArgument(argMortalEra)
            // nonce
            .scaleEncode(argNonce, Buffer.TRANSACTION)
            // tip
            .scaleEncode(argTip, Buffer.TRANSACTION)
            // AssetId
            .ifEqual(argAssetIdLength, "00", new ScriptAssembler().copyString("00").getScript(),
                new ScriptAssembler().copyString("01").setBufferInt(argAssetIdLength, 0, 4)
                    .scaleEncode(argAssetId, Buffer.TRANSACTION).getScript())
            // mode
            .copyArgument(argMode)
            // spec ver
            .copyArgument(argSpecVer)
            // tx ver
            .copyArgument(argTxVer)
            // genesis hash
            .copyArgument(argGenesisHash)
            // block hash
            .copyArgument(argBlockHash)
            .ifEqual(argMetaDataHash, "0000000000000000000000000000000000000000000000000000000000000000",
                new ScriptAssembler().copyString("00").getScript(),
                new ScriptAssembler().copyString("01").copyArgument(argMetaDataHash).getScript())
            .showMessage("DOT").showMessage("Bond").showAmount(argAmount, 10).showPressButton()
            // version=02 ScriptAssembler.hash=0E=ScriptAssembler.Blake2b256 sign=01=ECDSA
            .setHeader(HashType.Blake2b256, SignType.ECDSA).getScript();
        return script;
    }

    public static String DOTBondScriptSignature = Strings.padStart(
        "30440220568e3a5b99a59b4a6fa89df9302bfd7c9071d79961f5945915cda6d5cc6ad3ec02205be773ed609ce766c4d537134a2bf81f45b24e9112b289701bec08b45555b6f1",
        144, '0');

    public static String getDOTBondExtraScript() {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptData argCallIndex = sac.getArgument(2);
        ScriptData argMaxAdditional = sac.getArgumentRightJustified(10);
        ScriptData argMortalEra = sac.getArgument(2);
        ScriptData argNonce = sac.getArgumentRightJustified(5);
        ScriptData argTip = sac.getArgumentRightJustified(5);
        ScriptData argAssetIdLength = sac.getArgument(1);
        ScriptData argAssetId = sac.getArgumentVariableLength(4);
        ScriptData argMode = sac.getArgument(1);
        ScriptData argSpecVer = sac.getArgument(4);
        ScriptData argTxVer = sac.getArgument(4);
        ScriptData argGenesisHash = sac.getArgument(32);
        ScriptData argBlockHash = sac.getArgument(32);
        ScriptData argMetaDataHash = sac.getArgument(32);

        String script = new ScriptAssembler()
            // set coinType to 0162
            .setCoinType(0x0162)
            // call index
            .copyArgument(argCallIndex)
            // value
            .scaleEncode(argMaxAdditional, Buffer.TRANSACTION)
            // MortalEra
            .copyArgument(argMortalEra)
            // nonce
            .scaleEncode(argNonce, Buffer.TRANSACTION)
            // tip
            .scaleEncode(argTip, Buffer.TRANSACTION)
            // AssetId
            .ifEqual(argAssetIdLength, "00", new ScriptAssembler().copyString("00").getScript(),
                new ScriptAssembler().copyString("01").setBufferInt(argAssetIdLength, 0, 4)
                    .scaleEncode(argAssetId, Buffer.TRANSACTION).getScript())
            // mode
            .copyArgument(argMode)
            // spec ver
            .copyArgument(argSpecVer)
            // tx ver
            .copyArgument(argTxVer)
            // genesis hash
            .copyArgument(argGenesisHash)
            // block hash
            .copyArgument(argBlockHash)
            // metaDataHash
            .ifEqual(argMetaDataHash, "0000000000000000000000000000000000000000000000000000000000000000",
                new ScriptAssembler().copyString("00").getScript(),
                new ScriptAssembler().copyString("01").copyArgument(argMetaDataHash).getScript())
            .showMessage("DOT").showMessage("BondExt").showAmount(argMaxAdditional, 10).showPressButton()
            // version=02 ScriptAssembler.hash=0E=ScriptAssembler.Blake2b256 sign=01=ECDSA
            .setHeader(HashType.Blake2b256, SignType.ECDSA).getScript();
        return script;
    }

    public static String DOTBondExtraScriptSignature = Strings.padStart(
        "3046022100db71ce117f4670c7c01f3bd4c192b15e89a910f1df0fbe29d386dfb1832a7b80022100f756692f82c876beb8b98a8af20d23aaccb63c849cdb3fe8270a5b53f85d5b5b",
        144, '0');

    public static String getDOTUnbondScript() {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptData argCallIndex = sac.getArgument(2);
        ScriptData argAmount = sac.getArgumentRightJustified(10);
        ScriptData argMortalEra = sac.getArgument(2);
        ScriptData argNonce = sac.getArgumentRightJustified(5);
        ScriptData argTip = sac.getArgumentRightJustified(5);
        ScriptData argAssetIdLength = sac.getArgument(1);
        ScriptData argAssetId = sac.getArgumentVariableLength(4);
        ScriptData argMode = sac.getArgument(1);
        ScriptData argSpecVer = sac.getArgument(4);
        ScriptData argTxVer = sac.getArgument(4);
        ScriptData argGenesisHash = sac.getArgument(32);
        ScriptData argBlockHash = sac.getArgument(32);
        ScriptData argMetaDataHash = sac.getArgument(32);

        String script = new ScriptAssembler()
            // set coinType to 0162
            .setCoinType(0x0162)
            // call index
            .copyArgument(argCallIndex)
            // value
            .scaleEncode(argAmount, Buffer.TRANSACTION)
            // MortalEra
            .copyArgument(argMortalEra)
            // nonce
            .scaleEncode(argNonce, Buffer.TRANSACTION)
            // tip
            .scaleEncode(argTip, Buffer.TRANSACTION)
            // AssetId
            .ifEqual(argAssetIdLength, "00", new ScriptAssembler().copyString("00").getScript(),
                new ScriptAssembler().copyString("01").setBufferInt(argAssetIdLength, 0, 4)
                    .scaleEncode(argAssetId, Buffer.TRANSACTION).getScript())
            // mode
            .copyArgument(argMode)
            // spec ver
            .copyArgument(argSpecVer)
            // tx ver
            .copyArgument(argTxVer)
            // genesis hash
            .copyArgument(argGenesisHash)
            // block hash
            .copyArgument(argBlockHash)
            // metaDataHash
            .ifEqual(argMetaDataHash, "0000000000000000000000000000000000000000000000000000000000000000",
                new ScriptAssembler().copyString("00").getScript(),
                new ScriptAssembler().copyString("01").copyArgument(argMetaDataHash).getScript())
            .showMessage("DOT").showMessage("Unbond").showAmount(argAmount, 10).showPressButton()
            // version=02 ScriptAssembler.hash=0E=ScriptAssembler.Blake2b256 sign=01=ECDSA
            .setHeader(HashType.Blake2b256, SignType.ECDSA).getScript();
        return script;
    }

    public static String DOTUnbondScriptSignature = Strings.padStart(
        "30440220556740baa474fb9dd90416c734a69eefb27c77633260a483055a0d59b9c3e4c302202632e749aee62ada9e7e2d76eeae3263bf11cff1b38533348e73140deca4efa5",
        144, '0');

    public static String getDOTNominateSingleHashScript() {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptData argCallIndex = sac.getArgument(2);
        ScriptData argMortalEra = sac.getArgument(2);
        ScriptData argNonce = sac.getArgumentRightJustified(5);
        ScriptData argTip = sac.getArgumentRightJustified(5);
        ScriptData argAssetIdLength = sac.getArgument(1);
        ScriptData argAssetId = sac.getArgumentVariableLength(4);
        ScriptData argMode = sac.getArgument(1);
        ScriptData argSpecVer = sac.getArgument(4);
        ScriptData argTxVer = sac.getArgument(4);
        ScriptData argGenesisHash = sac.getArgument(32);
        ScriptData argBlockHash = sac.getArgument(32);
        ScriptData argMetaDataHash = sac.getArgument(32);
        ScriptData argTargetCount = sac.getArgument(1);
        ScriptData argTargetAddrs = sac.getArgumentAll();

        String script = new ScriptAssembler()
            // set coinType to 0162
            .setCoinType(0x0162)
            // call index
            .copyArgument(argCallIndex)
            // target count
            .scaleEncode(argTargetCount, Buffer.TRANSACTION)
            // target address
            .copyArgument(argTargetAddrs)
            // MortalEra
            .copyArgument(argMortalEra)
            // nonce
            .scaleEncode(argNonce, Buffer.TRANSACTION)
            // tip
            .scaleEncode(argTip, Buffer.TRANSACTION)
            // AssetId
            .ifEqual(argAssetIdLength, "00", new ScriptAssembler().copyString("00").getScript(),
                new ScriptAssembler().copyString("01").setBufferInt(argAssetIdLength, 0, 4)
                    .scaleEncode(argAssetId, Buffer.TRANSACTION).getScript())
            // mode
            .copyArgument(argMode)
            // spec ver
            .copyArgument(argSpecVer)
            // tx ver
            .copyArgument(argTxVer)
            // genesis hash
            .copyArgument(argGenesisHash)
            // block hash
            .copyArgument(argBlockHash)
            // metaDataHash
            .ifEqual(argMetaDataHash, "0000000000000000000000000000000000000000000000000000000000000000",
                new ScriptAssembler().copyString("00").getScript(),
                new ScriptAssembler().copyString("01").copyArgument(argMetaDataHash).getScript())
            .showMessage("DOT").showMessage("Nomint").showPressButton()
            // version=02 ScriptAssembler.hash=0E=ScriptAssembler.Blake2b256 sign=01=ECDSA
            .setHeader(HashType.Blake2b256, SignType.ECDSA).getScript();
        return script;
    }

    public static String DOTNominateSingleHashScriptSignature = Strings.padStart(
        "3044022040740b746887633fcf14b7839284629624f4f752dc1723771ce2b955a83fdbca02200825ee3e8b3e7d20b35468013e78a1e8ef66e4ce807e5082ff0f33b7deab1008",
        144, '0');

    public static String getDOTNominateDoubleHashScript() {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptData argCallIndex = sac.getArgument(2);
        // ScriptData argTargetCount = sac.getBufer(1);
        // ScriptData argTargetAddr = sac.getArgumentRightJustified(512);
        ScriptData argMortalEra = sac.getArgument(2);
        ScriptData argNonce = sac.getArgumentRightJustified(5);
        ScriptData argTip = sac.getArgumentRightJustified(5);
        ScriptData argAssetIdLength = sac.getArgument(1);
        ScriptData argAssetId = sac.getArgumentVariableLength(4);
        ScriptData argMode = sac.getArgument(1);
        ScriptData argSpecVer = sac.getArgument(4);
        ScriptData argTxVer = sac.getArgument(4);
        ScriptData argGenesisHash = sac.getArgument(32);
        ScriptData argBlockHash = sac.getArgument(32);
        ScriptData argMetaDataHash = sac.getArgument(32);
        ScriptData argTargetCount = sac.getArgument(1);
        ScriptData argTargetAddrs = sac.getArgumentAll();

        String script = new ScriptAssembler()
            // set coinType to 0162
            .setCoinType(0x0162)
            // call index
            .copyArgument(argCallIndex)
            // target count
            .scaleEncode(argTargetCount, Buffer.TRANSACTION)
            // target address
            .copyArgument(argTargetAddrs)
            // MortalEra
            .copyArgument(argMortalEra)
            // nonce
            .scaleEncode(argNonce, Buffer.TRANSACTION)
            // tip
            .scaleEncode(argTip, Buffer.TRANSACTION)
            // AssetId
            .ifEqual(argAssetIdLength, "00", new ScriptAssembler().copyString("00").getScript(),
                new ScriptAssembler().copyString("01").setBufferInt(argAssetIdLength, 0, 4)
                    .scaleEncode(argAssetId, Buffer.TRANSACTION).getScript())
            // mode
            .copyArgument(argMode)
            // spec ver
            .copyArgument(argSpecVer)
            // tx ver
            .copyArgument(argTxVer)
            // genesis hash
            .copyArgument(argGenesisHash)
            // block hash
            .copyArgument(argBlockHash)
            // metaDataHash
            .ifEqual(argMetaDataHash, "0000000000000000000000000000000000000000000000000000000000000000",
                new ScriptAssembler().copyString("00").getScript(),
                new ScriptAssembler().copyString("01").copyArgument(argMetaDataHash).getScript())
            .hash(ScriptData.getDataBufferAll(Buffer.TRANSACTION), Buffer.CACHE2, HashType.Blake2b256)
            .clearBuffer(Buffer.TRANSACTION)
            .copyArgument(ScriptData.getDataBufferAll(Buffer.CACHE2), Buffer.TRANSACTION).showMessage("DOT")
            .showMessage("Nomint").showPressButton()
            // version=02 ScriptAssembler.hash=0E=ScriptAssembler.Blake2b256 sign=01=ECDSA
            .setHeader(HashType.Blake2b256, SignType.ECDSA).getScript();
        return script;
    }

    public static String DOTNominateDoubleHashScriptSignature = Strings.padStart(
        "3044022008b855108278aa1591ecef25b996c8c9f0c9e183d38c9e281f673af4c64cf0d902202d887bdc556c247cc3c2dc88b6e39fc63985bb22bebbfb7930cae88408ffdfd3",
        144, '0');

    public static String getDOTWithdrawScript() {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptData argCallIndex = sac.getArgument(2);
        ScriptData argSlashingSpansNum = sac.getArgument(4);
        ScriptData argMortalEra = sac.getArgument(2);
        ScriptData argNonce = sac.getArgumentRightJustified(5);
        ScriptData argTip = sac.getArgumentRightJustified(5);
        ScriptData argAssetIdLength = sac.getArgument(1);
        ScriptData argAssetId = sac.getArgumentVariableLength(4);
        ScriptData argMode = sac.getArgument(1);
        ScriptData argSpecVer = sac.getArgument(4);
        ScriptData argTxVer = sac.getArgument(4);
        ScriptData argGenesisHash = sac.getArgument(32);
        ScriptData argBlockHash = sac.getArgument(32);
        ScriptData argMetaDataHash = sac.getArgument(32);

        String script = new ScriptAssembler()
            // set coinType to 0162
            .setCoinType(0x0162)
            // call index
            .copyArgument(argCallIndex)
            // target num_slashing_spans
            .ifEqual(argSlashingSpansNum, "00000000", new ScriptAssembler().copyString("00000000").getScript(),
                new ScriptAssembler().baseConvert(argSlashingSpansNum, Buffer.TRANSACTION, 4,
                    ScriptAssembler.binaryCharset, ScriptAssembler.littleEndian).getScript())
            // MortalEra
            .copyArgument(argMortalEra)
            // nonce
            .scaleEncode(argNonce, Buffer.TRANSACTION)
            // tip
            .scaleEncode(argTip, Buffer.TRANSACTION)
            // AssetId
            .ifEqual(argAssetIdLength, "00", new ScriptAssembler().copyString("00").getScript(),
                new ScriptAssembler().copyString("01").setBufferInt(argAssetIdLength, 0, 4)
                    .scaleEncode(argAssetId, Buffer.TRANSACTION).getScript())
            // mode
            .copyArgument(argMode)
            // spec ver
            .copyArgument(argSpecVer)
            // tx ver
            .copyArgument(argTxVer)
            // genesis hash
            .copyArgument(argGenesisHash)
            // block hash
            .copyArgument(argBlockHash)
            // metaDataHash
            .ifEqual(argMetaDataHash, "0000000000000000000000000000000000000000000000000000000000000000",
                new ScriptAssembler().copyString("00").getScript(),
                new ScriptAssembler().copyString("01").copyArgument(argMetaDataHash).getScript())
            .showMessage("DOT").showMessage("Withdr").showPressButton()
            // version=02 ScriptAssembler.hash=0E=ScriptAssembler.Blake2b256 sign=01=ECDSA
            .setHeader(HashType.Blake2b256, SignType.ECDSA).getScript();
        return script;

    }

    public static String DOTWithdrawScriptSignature = Strings.padStart(
        "3045022100d656e1a567c08b3ee65387b4acd07b7e70838221b0f0f4c54f18998a851766cf02204f24766f4d3919a2b9877cc9f35a8cc9269b44bd09ab4f5ede8529e4b8f1b082",
        144, '0');

    public static String getDOTChillScript() {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptData argCallIndex = sac.getArgument(2);
        ScriptData argMortalEra = sac.getArgument(2);
        ScriptData argNonce = sac.getArgumentRightJustified(5);
        ScriptData argTip = sac.getArgumentRightJustified(5);
        ScriptData argAssetIdLength = sac.getArgument(1);
        ScriptData argAssetId = sac.getArgumentVariableLength(4);
        ScriptData argMode = sac.getArgument(1);
        ScriptData argSpecVer = sac.getArgument(4);
        ScriptData argTxVer = sac.getArgument(4);
        ScriptData argGenesisHash = sac.getArgument(32);
        ScriptData argBlockHash = sac.getArgument(32);
        ScriptData argMetaDataHash = sac.getArgument(32);

        String script = new ScriptAssembler()
            // set coinType to 0162
            .setCoinType(0x0162)
            // call index
            .copyArgument(argCallIndex)
            // MortalEra
            .copyArgument(argMortalEra)
            // nonce
            .scaleEncode(argNonce, Buffer.TRANSACTION)
            // tip
            .scaleEncode(argTip, Buffer.TRANSACTION)
            // AssetId
            .ifEqual(argAssetIdLength, "00", new ScriptAssembler().copyString("00").getScript(),
                new ScriptAssembler().copyString("01").setBufferInt(argAssetIdLength, 0, 4)
                    .scaleEncode(argAssetId, Buffer.TRANSACTION).getScript())
            // mode
            .copyArgument(argMode)
            // spec ver
            .copyArgument(argSpecVer)
            // tx ver
            .copyArgument(argTxVer)
            // genesis hash
            .copyArgument(argGenesisHash)
            // block hash
            .copyArgument(argBlockHash)
            // metaDataHash
            .ifEqual(argMetaDataHash, "0000000000000000000000000000000000000000000000000000000000000000",
                new ScriptAssembler().copyString("00").getScript(),
                new ScriptAssembler().copyString("01").copyArgument(argMetaDataHash).getScript())
            .showMessage("DOT").showMessage("Chill").showPressButton()
            // version=02 ScriptAssembler.hash=0E=ScriptAssembler.Blake2b256 sign=01=ECDSA
            .setHeader(HashType.Blake2b256, SignType.ECDSA).getScript();
        return script;
    }

    public static String DOTChillScriptSignature = Strings.padStart(
        "3046022100d689da3b206f4f13ee5a443999552614d21533c21c2ce32d25c8fc3a5643b97702210087c93edb352c2b4d24ff42eaecff04d3e9f16d397fb9acdb19e9278f00b319ed",
        144, '0');

    public static String getKSMScript() {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptData argCallIndex = sac.getArgument(2);
        ScriptData argToAddr = sac.getArgument(32);
        ScriptData argAmount = sac.getArgumentRightJustified(10);
        ScriptData argMortalEra = sac.getArgument(2);
        ScriptData argNonce = sac.getArgumentRightJustified(5);
        ScriptData argTip = sac.getArgumentRightJustified(5);
        ScriptData argAssetIdLength = sac.getArgument(1);
        ScriptData argAssetId = sac.getArgumentVariableLength(4);
        ScriptData argMode = sac.getArgument(1);
        ScriptData argSpecVer = sac.getArgument(4);
        ScriptData argTxVer = sac.getArgument(4);
        ScriptData argGenesisHash = sac.getArgument(32);
        ScriptData argBlockHash = sac.getArgument(32);
        ScriptData argMetaDataHash = sac.getArgument(32);

        String script = new ScriptAssembler()
            // set coinType to 01b2
            .setCoinType(0x01b2)
            // call index
            .copyArgument(argCallIndex)
            // dest address
            .copyString("00").copyArgument(argToAddr)
            // value
            .scaleEncode(argAmount, Buffer.TRANSACTION)
            // MortalEra
            .copyArgument(argMortalEra)
            // nonce
            .scaleEncode(argNonce, Buffer.TRANSACTION)
            // tip
            .scaleEncode(argTip, Buffer.TRANSACTION)
            // AssetId
            .ifEqual(argAssetIdLength, "00", new ScriptAssembler().copyString("00").getScript(),
                new ScriptAssembler().copyString("01").setBufferInt(argAssetIdLength, 0, 4)
                    .scaleEncode(argAssetId, Buffer.TRANSACTION).getScript())
            // mode
            .copyArgument(argMode)
            // spec ver
            .copyArgument(argSpecVer)
            // tx ver
            .copyArgument(argTxVer)
            // genesis hash
            .copyArgument(argGenesisHash)
            // block hash
            .copyArgument(argBlockHash)
            // metaDataHash
            .ifEqual(argMetaDataHash, "0000000000000000000000000000000000000000000000000000000000000000",
                new ScriptAssembler().copyString("00").getScript(),
                new ScriptAssembler().copyString("01").copyArgument(argMetaDataHash).getScript())
            .showMessage("KSM").copyString(HexUtil.toHexString("SS58PRE".getBytes()), Buffer.CACHE2)
            .copyString("02", Buffer.CACHE2).copyArgument(argToAddr, Buffer.CACHE2)
            .hash(ScriptData.getDataBufferAll(Buffer.CACHE2), Buffer.CACHE2, HashType.Blake2b512)
            .baseConvert(ScriptData.getBuffer(Buffer.CACHE2, 7, 35), Buffer.CACHE1, 0, ScriptAssembler.base58Charset,
                ScriptAssembler.zeroInherit)
            .showAddress(ScriptData.getDataBufferAll(Buffer.CACHE1)).showAmount(argAmount, 12).showPressButton()
            // version=02 ScriptAssembler.hash=0E=ScriptAssembler.Blake2b256 sign=01=ECDSA
            .setHeader(HashType.Blake2b256, SignType.ECDSA).getScript();
        return script;
    }

    public static String KSMScriptSignature = Strings.padStart(
        "30460221008bb835aefde28336da279e1ad3efe3e64e93dc7a18570b857ce0db38096783d8022100d74b16e8e7faa5e43aeb318ea7d687c862a391d9f1204ea0adbf94624c1ad39e",
        144, '0');

    public static String getKSMBondScript() {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptData argCallIndex = sac.getArgument(2);
        ScriptData argAmount = sac.getArgumentRightJustified(10);
        ScriptData argPayeeType = sac.getArgument(1);
        ScriptData argMortalEra = sac.getArgument(2);
        ScriptData argNonce = sac.getArgumentRightJustified(5);
        ScriptData argTip = sac.getArgumentRightJustified(5);
        ScriptData argAssetIdLength = sac.getArgument(1);
        ScriptData argAssetId = sac.getArgumentVariableLength(4);
        ScriptData argMode = sac.getArgument(1);
        ScriptData argSpecVer = sac.getArgument(4);
        ScriptData argTxVer = sac.getArgument(4);
        ScriptData argGenesisHash = sac.getArgument(32);
        ScriptData argBlockHash = sac.getArgument(32);
        ScriptData argMetaDataHash = sac.getArgument(32);

        String script = new ScriptAssembler()
            // set coinType to 01b2
            .setCoinType(0x01b2)
            // call index
            .copyArgument(argCallIndex)
            // value
            .scaleEncode(argAmount, Buffer.TRANSACTION)
            // payeeType
            .copyArgument(argPayeeType)
            // MortalEra
            .copyArgument(argMortalEra)
            // nonce
            .scaleEncode(argNonce, Buffer.TRANSACTION)
            // tip
            .scaleEncode(argTip, Buffer.TRANSACTION)
            // AssetId
            .ifEqual(argAssetIdLength, "00", new ScriptAssembler().copyString("00").getScript(),
                new ScriptAssembler().copyString("01").setBufferInt(argAssetIdLength, 0, 4)
                    .scaleEncode(argAssetId, Buffer.TRANSACTION).getScript())
            // mode
            .copyArgument(argMode)
            // spec ver
            .copyArgument(argSpecVer)
            // tx ver
            .copyArgument(argTxVer)
            // genesis hash
            .copyArgument(argGenesisHash)
            // block hash
            .copyArgument(argBlockHash)
            .ifEqual(argMetaDataHash, "0000000000000000000000000000000000000000000000000000000000000000",
                new ScriptAssembler().copyString("00").getScript(),
                new ScriptAssembler().copyString("01").copyArgument(argMetaDataHash).getScript())
            .showMessage("KSM").showMessage("Bond").showAmount(argAmount, 12).showPressButton()
            // version=02 ScriptAssembler.hash=0E=ScriptAssembler.Blake2b256 sign=01=ECDSA
            .setHeader(HashType.Blake2b256, SignType.ECDSA).getScript();
        return script;
    }

    public static String KSMBondScriptSignature = Strings.padStart(
        "3045022009e3a3addd4087426b1015267247acd5180d9cdaa2da9058fa366bf831ff8e27022100efa86ca72b832257a53b623ff5a17e40f0c1f69d4c827f9d7f43e2f0ecbc0767",
        144, '0');

    public static String getKSMBondExtraScript() {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptData argCallIndex = sac.getArgument(2);
        ScriptData argMaxAdditional = sac.getArgumentRightJustified(10);
        ScriptData argMortalEra = sac.getArgument(2);
        ScriptData argNonce = sac.getArgumentRightJustified(5);
        ScriptData argTip = sac.getArgumentRightJustified(5);
        ScriptData argAssetIdLength = sac.getArgument(1);
        ScriptData argAssetId = sac.getArgumentVariableLength(4);
        ScriptData argMode = sac.getArgument(1);
        ScriptData argSpecVer = sac.getArgument(4);
        ScriptData argTxVer = sac.getArgument(4);
        ScriptData argGenesisHash = sac.getArgument(32);
        ScriptData argBlockHash = sac.getArgument(32);
        ScriptData argMetaDataHash = sac.getArgument(32);

        String script = new ScriptAssembler()
            // set coinType to 01b2
            .setCoinType(0x01b2)
            // call index
            .copyArgument(argCallIndex)
            // value
            .scaleEncode(argMaxAdditional, Buffer.TRANSACTION)
            // MortalEra
            .copyArgument(argMortalEra)
            // nonce
            .scaleEncode(argNonce, Buffer.TRANSACTION)
            // tip
            .scaleEncode(argTip, Buffer.TRANSACTION)
            // AssetId
            .ifEqual(argAssetIdLength, "00", new ScriptAssembler().copyString("00").getScript(),
                new ScriptAssembler().copyString("01").setBufferInt(argAssetIdLength, 0, 4)
                    .scaleEncode(argAssetId, Buffer.TRANSACTION).getScript())
            // mode
            .copyArgument(argMode)
            // spec ver
            .copyArgument(argSpecVer)
            // tx ver
            .copyArgument(argTxVer)
            // genesis hash
            .copyArgument(argGenesisHash)
            // block hash
            .copyArgument(argBlockHash)
            // metaDataHash
            .ifEqual(argMetaDataHash, "0000000000000000000000000000000000000000000000000000000000000000",
                new ScriptAssembler().copyString("00").getScript(),
                new ScriptAssembler().copyString("01").copyArgument(argMetaDataHash).getScript())
            .showMessage("KSM").showMessage("BondExt").showAmount(argMaxAdditional, 12).showPressButton()
            // version=02 ScriptAssembler.hash=0E=ScriptAssembler.Blake2b256 sign=01=ECDSA
            .setHeader(HashType.Blake2b256, SignType.ECDSA).getScript();
        return script;
    }

    public static String KSMBondExtraScriptSignature = Strings.padStart(
        "3044022037184abfe630fcf2b4ea4ea619ba4aa326cfb907a211790cdfa85394fa004a89022018e838c259eaf8f2001fa7885baa21e4b7e26adfad14a6277961d2843766a270",
        144, '0');

    public static String getKSMUnbondScript() {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptData argCallIndex = sac.getArgument(2);
        ScriptData argAmount = sac.getArgumentRightJustified(10);
        ScriptData argMortalEra = sac.getArgument(2);
        ScriptData argNonce = sac.getArgumentRightJustified(5);
        ScriptData argTip = sac.getArgumentRightJustified(5);
        ScriptData argAssetIdLength = sac.getArgument(1);
        ScriptData argAssetId = sac.getArgumentVariableLength(4);
        ScriptData argMode = sac.getArgument(1);
        ScriptData argSpecVer = sac.getArgument(4);
        ScriptData argTxVer = sac.getArgument(4);
        ScriptData argGenesisHash = sac.getArgument(32);
        ScriptData argBlockHash = sac.getArgument(32);
        ScriptData argMetaDataHash = sac.getArgument(32);

        String script = new ScriptAssembler()
            // set coinType to 01b2
            .setCoinType(0x01b2)
            // call index
            .copyArgument(argCallIndex)
            // value
            .scaleEncode(argAmount, Buffer.TRANSACTION)
            // MortalEra
            .copyArgument(argMortalEra)
            // nonce
            .scaleEncode(argNonce, Buffer.TRANSACTION)
            // tip
            .scaleEncode(argTip, Buffer.TRANSACTION)
            // AssetId
            .ifEqual(argAssetIdLength, "00", new ScriptAssembler().copyString("00").getScript(),
                new ScriptAssembler().copyString("01").setBufferInt(argAssetIdLength, 0, 4)
                    .scaleEncode(argAssetId, Buffer.TRANSACTION).getScript())
            // mode
            .copyArgument(argMode)
            // spec ver
            .copyArgument(argSpecVer)
            // tx ver
            .copyArgument(argTxVer)
            // genesis hash
            .copyArgument(argGenesisHash)
            // block hash
            .copyArgument(argBlockHash)
            // metaDataHash
            .ifEqual(argMetaDataHash, "0000000000000000000000000000000000000000000000000000000000000000",
                new ScriptAssembler().copyString("00").getScript(),
                new ScriptAssembler().copyString("01").copyArgument(argMetaDataHash).getScript())
            .showMessage("KSM").showMessage("Unbond").showAmount(argAmount, 12).showPressButton()
            // version=02 ScriptAssembler.hash=0E=ScriptAssembler.Blake2b256 sign=01=ECDSA
            .setHeader(HashType.Blake2b256, SignType.ECDSA).getScript();
        return script;
    }

    public static String KSMUnbondScriptSignature = Strings.padStart(
        "304402202e8961b85758caf28e565a4f3c69f74feea51f935926d050092456417210a19b0220145837edc4da1f0257832f6a918eb22f86d57ac5990839e76db04cb4f1a03f5c",
        144, '0');

    public static String getKSMNominateSingleHashScript() {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptData argCallIndex = sac.getArgument(2);
        ScriptData argMortalEra = sac.getArgument(2);
        ScriptData argNonce = sac.getArgumentRightJustified(5);
        ScriptData argTip = sac.getArgumentRightJustified(5);
        ScriptData argAssetIdLength = sac.getArgument(1);
        ScriptData argAssetId = sac.getArgumentVariableLength(4);
        ScriptData argMode = sac.getArgument(1);
        ScriptData argSpecVer = sac.getArgument(4);
        ScriptData argTxVer = sac.getArgument(4);
        ScriptData argGenesisHash = sac.getArgument(32);
        ScriptData argBlockHash = sac.getArgument(32);
        ScriptData argMetaDataHash = sac.getArgument(32);
        ScriptData argTargetCount = sac.getArgument(1);
        ScriptData argTargetAddrs = sac.getArgumentAll();

        String script = new ScriptAssembler()
            // set coinType to 01b2
            .setCoinType(0x01b2)
            // call index
            .copyArgument(argCallIndex)
            // target count
            .scaleEncode(argTargetCount, Buffer.TRANSACTION)
            // target address
            .copyArgument(argTargetAddrs)
            // MortalEra
            .copyArgument(argMortalEra)
            // nonce
            .scaleEncode(argNonce, Buffer.TRANSACTION)
            // tip
            .scaleEncode(argTip, Buffer.TRANSACTION)
            // AssetId
            .ifEqual(argAssetIdLength, "00", new ScriptAssembler().copyString("00").getScript(),
                new ScriptAssembler().copyString("01").setBufferInt(argAssetIdLength, 0, 4)
                    .scaleEncode(argAssetId, Buffer.TRANSACTION).getScript())
            // mode
            .copyArgument(argMode)
            // spec ver
            .copyArgument(argSpecVer)
            // tx ver
            .copyArgument(argTxVer)
            // genesis hash
            .copyArgument(argGenesisHash)
            // block hash
            .copyArgument(argBlockHash)
            // metaDataHash
            .ifEqual(argMetaDataHash, "0000000000000000000000000000000000000000000000000000000000000000",
                new ScriptAssembler().copyString("00").getScript(),
                new ScriptAssembler().copyString("01").copyArgument(argMetaDataHash).getScript())
            .showMessage("KSM").showMessage("Nomint").showPressButton()
            // version=02 ScriptAssembler.hash=0E=ScriptAssembler.Blake2b256 sign=01=ECDSA
            .setHeader(HashType.Blake2b256, SignType.ECDSA).getScript();
        return script;
    }

    public static String KSMNominateSingleHashScriptSignature = Strings.padStart(
        "3044022040bf95094f8a15d4eefa79930e281c0ac362d07d9abc15751377c58e05bed812022000fa6e4df6e081f4e9450adcdd88cafa6ff6ad379579bfd5887bfc8c05d2a472",
        144, '0');

    public static String getKSMNominateDoubleHashScript() {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptData argCallIndex = sac.getArgument(2);
        // ScriptData argTargetCount = sac.getBufer(1);
        // ScriptData argTargetAddr = sac.getArgumentRightJustified(512);
        ScriptData argMortalEra = sac.getArgument(2);
        ScriptData argNonce = sac.getArgumentRightJustified(5);
        ScriptData argTip = sac.getArgumentRightJustified(5);
        ScriptData argAssetIdLength = sac.getArgument(1);
        ScriptData argAssetId = sac.getArgumentVariableLength(4);
        ScriptData argMode = sac.getArgument(1);
        ScriptData argSpecVer = sac.getArgument(4);
        ScriptData argTxVer = sac.getArgument(4);
        ScriptData argGenesisHash = sac.getArgument(32);
        ScriptData argBlockHash = sac.getArgument(32);
        ScriptData argMetaDataHash = sac.getArgument(32);
        ScriptData argTargetCount = sac.getArgument(1);
        ScriptData argTargetAddrs = sac.getArgumentAll();

        String script = new ScriptAssembler()
            // set coinType to 01b2
            .setCoinType(0x01b2)
            // call index
            .copyArgument(argCallIndex)
            // target count
            .scaleEncode(argTargetCount, Buffer.TRANSACTION)
            // target address
            .copyArgument(argTargetAddrs)
            // MortalEra
            .copyArgument(argMortalEra)
            // nonce
            .scaleEncode(argNonce, Buffer.TRANSACTION)
            // tip
            .scaleEncode(argTip, Buffer.TRANSACTION)
            // AssetId
            .ifEqual(argAssetIdLength, "00", new ScriptAssembler().copyString("00").getScript(),
                new ScriptAssembler().copyString("01").setBufferInt(argAssetIdLength, 0, 4)
                    .scaleEncode(argAssetId, Buffer.TRANSACTION).getScript())
            // mode
            .copyArgument(argMode)
            // spec ver
            .copyArgument(argSpecVer)
            // tx ver
            .copyArgument(argTxVer)
            // genesis hash
            .copyArgument(argGenesisHash)
            // block hash
            .copyArgument(argBlockHash)
            // metaDataHash
            .ifEqual(argMetaDataHash, "0000000000000000000000000000000000000000000000000000000000000000",
                new ScriptAssembler().copyString("00").getScript(),
                new ScriptAssembler().copyString("01").copyArgument(argMetaDataHash).getScript())
            .hash(ScriptData.getDataBufferAll(Buffer.TRANSACTION), Buffer.CACHE2, HashType.Blake2b256)
            .clearBuffer(Buffer.TRANSACTION)
            .copyArgument(ScriptData.getDataBufferAll(Buffer.CACHE2), Buffer.TRANSACTION).showMessage("KSM")
            .showMessage("Nomint").showPressButton()
            // version=02 ScriptAssembler.hash=0E=ScriptAssembler.Blake2b256 sign=01=ECDSA
            .setHeader(HashType.Blake2b256, SignType.ECDSA).getScript();
        return script;
    }

    public static String KSMNominateDoubleHashScriptSignature = Strings.padStart(
        "3045022100cee7e85ce9f14570798f5ee784bc18b9207f68c8dbf17b77a339aab92b424527022024cb178c665660f81c2433d3c4e7f0fdf900cf0beaa325efc6e21c56cfaeba75",
        144, '0');

    public static String getKSMWithdrawScript() {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptData argCallIndex = sac.getArgument(2);
        ScriptData argSlashingSpansNum = sac.getArgument(4);
        ScriptData argMortalEra = sac.getArgument(2);
        ScriptData argNonce = sac.getArgumentRightJustified(5);
        ScriptData argTip = sac.getArgumentRightJustified(5);
        ScriptData argAssetIdLength = sac.getArgument(1);
        ScriptData argAssetId = sac.getArgumentVariableLength(4);
        ScriptData argMode = sac.getArgument(1);
        ScriptData argSpecVer = sac.getArgument(4);
        ScriptData argTxVer = sac.getArgument(4);
        ScriptData argGenesisHash = sac.getArgument(32);
        ScriptData argBlockHash = sac.getArgument(32);
        ScriptData argMetaDataHash = sac.getArgument(32);

        String script = new ScriptAssembler()
            // set coinType to 01b2
            .setCoinType(0x01b2)
            // call index
            .copyArgument(argCallIndex)
            // target num_slashing_spans
            .ifEqual(argSlashingSpansNum, "00000000", new ScriptAssembler().copyString("00000000").getScript(),
                new ScriptAssembler().baseConvert(argSlashingSpansNum, Buffer.TRANSACTION, 4,
                    ScriptAssembler.binaryCharset, ScriptAssembler.littleEndian).getScript())
            // MortalEra
            .copyArgument(argMortalEra)
            // nonce
            .scaleEncode(argNonce, Buffer.TRANSACTION)
            // tip
            .scaleEncode(argTip, Buffer.TRANSACTION)
            // AssetId
            .ifEqual(argAssetIdLength, "00", new ScriptAssembler().copyString("00").getScript(),
                new ScriptAssembler().copyString("01").setBufferInt(argAssetIdLength, 0, 4)
                    .scaleEncode(argAssetId, Buffer.TRANSACTION).getScript())
            // mode
            .copyArgument(argMode)
            // spec ver
            .copyArgument(argSpecVer)
            // tx ver
            .copyArgument(argTxVer)
            // genesis hash
            .copyArgument(argGenesisHash)
            // block hash
            .copyArgument(argBlockHash)
            // metaDataHash
            .ifEqual(argMetaDataHash, "0000000000000000000000000000000000000000000000000000000000000000",
                new ScriptAssembler().copyString("00").getScript(),
                new ScriptAssembler().copyString("01").copyArgument(argMetaDataHash).getScript())
            .showMessage("KSM").showMessage("Withdr").showPressButton()
            // version=02 ScriptAssembler.hash=0E=ScriptAssembler.Blake2b256 sign=01=ECDSA
            .setHeader(HashType.Blake2b256, SignType.ECDSA).getScript();
        return script;

    }

    public static String KSMWithdrawScriptSignature = Strings.padStart(
        "304502201f95b13345d68023aec80f84b71232dacec5e16e89864201a8cd4b47ec6ad40a022100be439a3f50848df42a477a6abd0a60a74560bac1b656fd276f825303c0efe387",
        144, '0');

    public static String getKSMChillScript() {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptData argCallIndex = sac.getArgument(2);
        ScriptData argMortalEra = sac.getArgument(2);
        ScriptData argNonce = sac.getArgumentRightJustified(5);
        ScriptData argTip = sac.getArgumentRightJustified(5);
        ScriptData argAssetIdLength = sac.getArgument(1);
        ScriptData argAssetId = sac.getArgumentVariableLength(4);
        ScriptData argMode = sac.getArgument(1);
        ScriptData argSpecVer = sac.getArgument(4);
        ScriptData argTxVer = sac.getArgument(4);
        ScriptData argGenesisHash = sac.getArgument(32);
        ScriptData argBlockHash = sac.getArgument(32);
        ScriptData argMetaDataHash = sac.getArgument(32);

        String script = new ScriptAssembler()
            // set coinType to 01b2
            .setCoinType(0x01b2)
            // call index
            .copyArgument(argCallIndex)
            // MortalEra
            .copyArgument(argMortalEra)
            // nonce
            .scaleEncode(argNonce, Buffer.TRANSACTION)
            // tip
            .scaleEncode(argTip, Buffer.TRANSACTION)
            // AssetId
            .ifEqual(argAssetIdLength, "00", new ScriptAssembler().copyString("00").getScript(),
                new ScriptAssembler().copyString("01").setBufferInt(argAssetIdLength, 0, 4)
                    .scaleEncode(argAssetId, Buffer.TRANSACTION).getScript())
            // mode
            .copyArgument(argMode)
            // spec ver
            .copyArgument(argSpecVer)
            // tx ver
            .copyArgument(argTxVer)
            // genesis hash
            .copyArgument(argGenesisHash)
            // block hash
            .copyArgument(argBlockHash)
            // metaDataHash
            .ifEqual(argMetaDataHash, "0000000000000000000000000000000000000000000000000000000000000000",
                new ScriptAssembler().copyString("00").getScript(),
                new ScriptAssembler().copyString("01").copyArgument(argMetaDataHash).getScript())
            .showMessage("KSM").showMessage("Chill").showPressButton()
            // version=02 ScriptAssembler.hash=0E=ScriptAssembler.Blake2b256 sign=01=ECDSA
            .setHeader(HashType.Blake2b256, SignType.ECDSA).getScript();
        return script;
    }

    public static String KSMChillScriptSignature = Strings.padStart(
        "304502207ee453ba6496bbb9a6b6da310829019ef19d4729b564f37224ee9ef96dbf859002210096d11eb93df8c341cb124792c0224329e77c3cd8443d62b1089df9e231a47af7",
        144, '0');

}
