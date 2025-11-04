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
        ScriptData argMortalEra = sac.getArgumentRightJustified(5);
        ScriptData argNonce = sac.getArgumentRightJustified(5);
        ScriptData argTip = sac.getArgumentRightJustified(5);
        ScriptData argSpecVer = sac.getArgument(4);
        ScriptData argTxVer = sac.getArgument(4);
        ScriptData argGenesisHash = sac.getArgument(32);
        ScriptData argBlockHash = sac.getArgument(32);

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
            // mode
            .copyString("00")
            // spec ver
            .copyArgument(argSpecVer)
            // tx ver
            .copyArgument(argTxVer)
            // genesis hash
            .copyArgument(argGenesisHash)
            // block hash
            .copyArgument(argBlockHash)
            // metaDataHash
            .copyString("00").showMessage("DOT").copyString(HexUtil.toHexString("SS58PRE".getBytes()), Buffer.CACHE2)
            .copyString("00", Buffer.CACHE2).copyArgument(argToAddr, Buffer.CACHE2)
            .hash(ScriptData.getDataBufferAll(Buffer.CACHE2), Buffer.CACHE2, HashType.Blake2b512)
            .baseConvert(ScriptData.getBuffer(Buffer.CACHE2, 7, 35), Buffer.CACHE1, 0, ScriptAssembler.base58Charset,
                ScriptAssembler.zeroInherit)
            .showAddress(ScriptData.getDataBufferAll(Buffer.CACHE1)).showAmount(argAmount, 10)
            .showWrap("PRESS", "BUTToN")
            // version=02 ScriptAssembler.hash=0E=ScriptAssembler.Blake2b256 sign=01=ECDSA
            .setHeader(HashType.Blake2b256, SignType.ECDSA).getScript();
        return script;
    }

    public static String DOTScriptSignature = Strings.padStart(
        "30450221009c565247e07f7cf0c23f2ff92e24a81800b849a7fce4c7198d8a7b5a49ae009b02204fcaffcf743adc77cf38f3688617c1b4507488c63020aadd69c01b5588910b66",
        144, '0');

    public static String getDOTBondScript() {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptData argCallIndex = sac.getArgument(2);
        ScriptData argAmount = sac.getArgumentRightJustified(10);
        ScriptData argPayeeType = sac.getArgument(1);
        ScriptData argMortalEra = sac.getArgumentRightJustified(5);
        ScriptData argNonce = sac.getArgumentRightJustified(5);
        ScriptData argTip = sac.getArgumentRightJustified(5);
        ScriptData argSpecVer = sac.getArgument(4);
        ScriptData argTxVer = sac.getArgument(4);
        ScriptData argGenesisHash = sac.getArgument(32);
        ScriptData argBlockHash = sac.getArgument(32);

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
            // mode
            .copyString("00")
            // spec ver
            .copyArgument(argSpecVer)
            // tx ver
            .copyArgument(argTxVer)
            // genesis hash
            .copyArgument(argGenesisHash)
            // block hash
            .copyArgument(argBlockHash)
            // metaDataHash
            .copyString("00").showMessage("DOT").showMessage("Bond").showAmount(argAmount, 10)
            .showWrap("PRESS", "BUTToN")
            // version=02 ScriptAssembler.hash=0E=ScriptAssembler.Blake2b256 sign=01=ECDSA
            .setHeader(HashType.Blake2b256, SignType.ECDSA).getScript();
        return script;
    }

    public static String DOTBondScriptSignature = Strings.padStart(
        "3045022062eb23c4525f7aac3165a19ce9c6c2ddb1093a9335451682fcafad340a01aa40022100b9face959049ee49a95a2a98f830d88b1bba65fe4d2ad61f7045d4de7a45dda1",
        144, '0');

    public static String getDOTBondExtraScript() {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptData argCallIndex = sac.getArgument(2);
        ScriptData argMaxAdditional = sac.getArgumentRightJustified(10);
        ScriptData argMortalEra = sac.getArgumentRightJustified(5);
        ScriptData argNonce = sac.getArgumentRightJustified(5);
        ScriptData argTip = sac.getArgumentRightJustified(5);
        ScriptData argSpecVer = sac.getArgument(4);
        ScriptData argTxVer = sac.getArgument(4);
        ScriptData argGenesisHash = sac.getArgument(32);
        ScriptData argBlockHash = sac.getArgument(32);

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
            // mode
            .copyString("00")
            // spec ver
            .copyArgument(argSpecVer)
            // tx ver
            .copyArgument(argTxVer)
            // genesis hash
            .copyArgument(argGenesisHash)
            // block hash
            .copyArgument(argBlockHash)
            // metaDataHash
            .copyString("00").showMessage("DOT").showMessage("BondExt").showAmount(argMaxAdditional, 10)
            .showWrap("PRESS", "BUTToN")
            // version=02 ScriptAssembler.hash=0E=ScriptAssembler.Blake2b256 sign=01=ECDSA
            .setHeader(HashType.Blake2b256, SignType.ECDSA).getScript();
        return script;
    }

    public static String DOTBondExtraScriptSignature = Strings.padStart(
        "3046022100efca7ebba545a200be03115608b63971a4ef6947476cd0bd56e4644be1028ba0022100b2084f6b13340ffb793d5aa49f3dfd19179614555a58b0d094a7c5acdb17b12a",
        144, '0');

    public static String getDOTUnbondScript() {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptData argCallIndex = sac.getArgument(2);
        ScriptData argAmount = sac.getArgumentRightJustified(10);
        ScriptData argMortalEra = sac.getArgumentRightJustified(5);
        ScriptData argNonce = sac.getArgumentRightJustified(5);
        ScriptData argTip = sac.getArgumentRightJustified(5);
        ScriptData argSpecVer = sac.getArgument(4);
        ScriptData argTxVer = sac.getArgument(4);
        ScriptData argGenesisHash = sac.getArgument(32);
        ScriptData argBlockHash = sac.getArgument(32);

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
            // mode
            .copyString("00")
            // spec ver
            .copyArgument(argSpecVer)
            // tx ver
            .copyArgument(argTxVer)
            // genesis hash
            .copyArgument(argGenesisHash)
            // block hash
            .copyArgument(argBlockHash)
            // metaDataHash
            .copyString("00").showMessage("DOT").showMessage("Unbond").showAmount(argAmount, 10)
            .showWrap("PRESS", "BUTToN")
            // version=02 ScriptAssembler.hash=0E=ScriptAssembler.Blake2b256 sign=01=ECDSA
            .setHeader(HashType.Blake2b256, SignType.ECDSA).getScript();
        return script;
    }

    public static String DOTUnbondScriptSignature = Strings.padStart(
        "3045022047d26e1225ed4dd84af0b0618e88c75c6cb764b856b523525f3a9fc0d9513efa02210099a497e9586fd95f4c482545dfde737fd196255f83e01cc01ac4ae11cf10f813",
        144, '0');

    public static String getDOTNominateSingleHashScript() {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptData argCallIndex = sac.getArgument(2);
        // ScriptData argTargetCount = sac.getBufer(1);
        // ScriptData argTargetAddr = sac.getArgumentRightJustified(512);
        ScriptData argMortalEra = sac.getArgumentRightJustified(5);
        ScriptData argNonce = sac.getArgumentRightJustified(5);
        ScriptData argTip = sac.getArgumentRightJustified(5);
        ScriptData argSpecVer = sac.getArgument(4);
        ScriptData argTxVer = sac.getArgument(4);
        ScriptData argGenesisHash = sac.getArgument(32);
        ScriptData argBlockHash = sac.getArgument(32);
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
            // mode
            .copyString("00")
            // spec ver
            .copyArgument(argSpecVer)
            // tx ver
            .copyArgument(argTxVer)
            // genesis hash
            .copyArgument(argGenesisHash)
            // block hash
            .copyArgument(argBlockHash)
            // metaDataHash
            .copyString("00").showMessage("DOT").showMessage("Nomint").showWrap("PRESS", "BUTToN")
            // version=02 ScriptAssembler.hash=0E=ScriptAssembler.Blake2b256 sign=01=ECDSA
            .setHeader(HashType.Blake2b256, SignType.ECDSA).getScript();
        return script;
    }

    public static String DOTNominateSingleHashScriptSignature = Strings.padStart(
        "30450220607247af076ff32bc032bb8b46bcf34b161e32908191acb9d3d0a2382ccc90df022100d6058bf876a5660d76be18b826a0baa541953f1d6d3eaa27787a56c9b13b96d8",
        144, '0');

    public static String getDOTNominateDoubleHashScript() {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptData argCallIndex = sac.getArgument(2);
        // ScriptData argTargetCount = sac.getBufer(1);
        // ScriptData argTargetAddr = sac.getArgumentRightJustified(512);
        ScriptData argMortalEra = sac.getArgumentRightJustified(5);
        ScriptData argNonce = sac.getArgumentRightJustified(5);
        ScriptData argTip = sac.getArgumentRightJustified(5);
        ScriptData argSpecVer = sac.getArgument(4);
        ScriptData argTxVer = sac.getArgument(4);
        ScriptData argGenesisHash = sac.getArgument(32);
        ScriptData argBlockHash = sac.getArgument(32);
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
            // mode
            .copyString("00")
            // spec ver
            .copyArgument(argSpecVer)
            // tx ver
            .copyArgument(argTxVer)
            // genesis hash
            .copyArgument(argGenesisHash)
            // block hash
            .copyArgument(argBlockHash)
            // metaDataHash
            .copyString("00").hash(ScriptData.getDataBufferAll(Buffer.TRANSACTION), Buffer.CACHE2, HashType.Blake2b256)
            .clearBuffer(Buffer.TRANSACTION)
            .copyArgument(ScriptData.getDataBufferAll(Buffer.CACHE2), Buffer.TRANSACTION).showMessage("DOT")
            .showMessage("Nomint").showWrap("PRESS", "BUTToN")
            // version=02 ScriptAssembler.hash=0E=ScriptAssembler.Blake2b256 sign=01=ECDSA
            .setHeader(HashType.Blake2b256, SignType.ECDSA).getScript();
        return script;
    }

    public static String DOTNominateDoubleHashScriptSignature = Strings.padStart(
        "3045022100defb254d89d891d10fe2cfd775304470bb6d36358e6c7c5671798af58aed2b5902200e62740d9c487274ad0789cd6144fe6cbd7e87a26b2ba4612909dc8ab52c5904",
        144, '0');

    public static String getDOTWithdrawScript() {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptData argCallIndex = sac.getArgument(2);
        ScriptData argSlashingSpansNum = sac.getArgument(4);
        ScriptData argMortalEra = sac.getArgumentRightJustified(5);
        ScriptData argNonce = sac.getArgumentRightJustified(5);
        ScriptData argTip = sac.getArgumentRightJustified(5);
        ScriptData argSpecVer = sac.getArgument(4);
        ScriptData argTxVer = sac.getArgument(4);
        ScriptData argGenesisHash = sac.getArgument(32);
        ScriptData argBlockHash = sac.getArgument(32);

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
            // mode
            .copyString("00")
            // spec ver
            .copyArgument(argSpecVer)
            // tx ver
            .copyArgument(argTxVer)
            // genesis hash
            .copyArgument(argGenesisHash)
            // block hash
            .copyArgument(argBlockHash)
            // metaDataHash
            .copyString("00").showMessage("DOT").showMessage("Withdr").showWrap("PRESS", "BUTToN")
            // version=02 ScriptAssembler.hash=0E=ScriptAssembler.Blake2b256 sign=01=ECDSA
            .setHeader(HashType.Blake2b256, SignType.ECDSA).getScript();
        return script;
    }

    public static String DOTWithdrawScriptSignature = Strings.padStart(
        "3046022100824b324fb91724ded545287851555722a3362f3e4d11954d3cce2a387e1c7955022100c755f164f781c94c161bb58fe1b25b61fcdcaeb18b0059887a0441f2c9a22512",
        144, '0');

    public static String getDOTChillScript() {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptData argCallIndex = sac.getArgument(2);
        ScriptData argMortalEra = sac.getArgumentRightJustified(5);
        ScriptData argNonce = sac.getArgumentRightJustified(5);
        ScriptData argTip = sac.getArgumentRightJustified(5);
        ScriptData argSpecVer = sac.getArgument(4);
        ScriptData argTxVer = sac.getArgument(4);
        ScriptData argGenesisHash = sac.getArgument(32);
        ScriptData argBlockHash = sac.getArgument(32);

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
            // mode
            .copyString("00")
            // spec ver
            .copyArgument(argSpecVer)
            // tx ver
            .copyArgument(argTxVer)
            // genesis hash
            .copyArgument(argGenesisHash)
            // block hash
            .copyArgument(argBlockHash)
            // metaDataHash
            .copyString("00").showMessage("DOT").showMessage("Chill").showWrap("PRESS", "BUTToN")
            // version=02 ScriptAssembler.hash=0E=ScriptAssembler.Blake2b256 sign=01=ECDSA
            .setHeader(HashType.Blake2b256, SignType.ECDSA).getScript();
        return script;
    }

    public static String DOTChillScriptSignature = Strings.padStart(
        "30440220650736bcaf307a1c96e943731858589cf9a8771e72c2f9aaae24a32c6770feb2022066e7e2d153a0e5c9f905d74dab17b0a8326ecca38d20e67cfc960af78a6169d4",
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
