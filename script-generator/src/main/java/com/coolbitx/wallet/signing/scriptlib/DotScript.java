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
        ScriptData argMortalEra = sac.getArgumentRightJustified(5);
        ScriptData argNonce = sac.getArgumentRightJustified(5);
        ScriptData argTip = sac.getArgumentRightJustified(5);
        ScriptData argSpecVer = sac.getArgument(4);
        ScriptData argTxVer = sac.getArgument(4);
        ScriptData argGenesisHash = sac.getArgument(32);
        ScriptData argBlockHash = sac.getArgument(32);

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
            .copyString("00").showMessage("KSM").copyString(HexUtil.toHexString("SS58PRE".getBytes()), Buffer.CACHE2)
            .copyString("02", Buffer.CACHE2).copyArgument(argToAddr, Buffer.CACHE2)
            .hash(ScriptData.getDataBufferAll(Buffer.CACHE2), Buffer.CACHE2, HashType.Blake2b512)
            .baseConvert(ScriptData.getBuffer(Buffer.CACHE2, 7, 35), Buffer.CACHE1, 0, ScriptAssembler.base58Charset,
                ScriptAssembler.zeroInherit)
            .showAddress(ScriptData.getDataBufferAll(Buffer.CACHE1)).showAmount(argAmount, 12)
            .showWrap("PRESS", "BUTToN")
            // version=02 ScriptAssembler.hash=0E=ScriptAssembler.Blake2b256 sign=01=ECDSA
            .setHeader(HashType.Blake2b256, SignType.ECDSA).getScript();
        return script;
    }

    public static String KSMScriptSignature = Strings.padStart(
        "3046022100ffb62a7ac5004f19c55b13f1808fb9cec8c40466af088f0431a4f09ac7b6b7bb022100b8ff56cdd0e177451a908e2d787c69a3c244dc6c1de3eff0efd542859c671fd6",
        144, '0');

    public static String getKSMBondScript() {
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
            .copyString("00").showMessage("KSM").showMessage("Bond").showAmount(argAmount, 12)
            .showWrap("PRESS", "BUTToN")
            // version=02 ScriptAssembler.hash=0E=ScriptAssembler.Blake2b256 sign=01=ECDSA
            .setHeader(HashType.Blake2b256, SignType.ECDSA).getScript();
        return script;
    }

    public static String KSMBondScriptSignature = Strings.padStart(
        "3046022100b6a7b39e3a4a89e3df21d9e6f1fbbb1d5441ab8877432a5348b1640883b2ea9b0221008e5ef4ae778d7e877d208cbf2e890170bfb642789e90878888ab57c6eba639e2",
        144, '0');

    public static String getKSMBondExtraScript() {
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
            .copyString("00").showMessage("KSM").showMessage("BondExt").showAmount(argMaxAdditional, 12)
            .showWrap("PRESS", "BUTToN")
            // version=02 ScriptAssembler.hash=0E=ScriptAssembler.Blake2b256 sign=01=ECDSA
            .setHeader(HashType.Blake2b256, SignType.ECDSA).getScript();
        return script;
    }

    public static String KSMBondExtraScriptSignature = Strings.padStart(
        "3045022100c795d3f15772dc2d21ff5a5431eb09f02515371ac438402c61846d03ce3ab6a80220396cf63032f75b5e513d36b41b9ec4e8230a30d4629ae0ebdb8661d9fc4a5bc4",
        144, '0');

    public static String getKSMUnbondScript() {
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
            .copyString("00").showMessage("KSM").showMessage("Unbond").showAmount(argAmount, 12)
            .showWrap("PRESS", "BUTToN")
            // version=02 ScriptAssembler.hash=0E=ScriptAssembler.Blake2b256 sign=01=ECDSA
            .setHeader(HashType.Blake2b256, SignType.ECDSA).getScript();
        return script;
    }

    public static String KSMUnbondScriptSignature = Strings.padStart(
        "304502207feb25ae89c5763872178b5c9057f7de904531a5126631e087295557942cedc60221008c23b64933c4c4239f4a118a5242216c5d68d925804ca0e5ad2eda51a1b08cfc",
        144, '0');

    public static String getKSMNominateSingleHashScript() {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptData argCallIndex = sac.getArgument(2);
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
            .copyString("00").showMessage("KSM").showMessage("Nomint").showWrap("PRESS", "BUTToN")
            // version=02 ScriptAssembler.hash=0E=ScriptAssembler.Blake2b256 sign=01=ECDSA
            .setHeader(HashType.Blake2b256, SignType.ECDSA).getScript();
        return script;
    }

    public static String KSMNominateSingleHashScriptSignature = Strings.padStart(
        "304402203598deafeaac8b0b71fd27f6bd455c6fd7fb8602c92e2d48f2993559b6ebce850220361c0347ce7483720ffd16b5ed50b4dbea198cbf2aa5b329fd095664042e0238",
        144, '0');

    public static String getKSMNominateDoubleHashScript() {
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
            .copyArgument(ScriptData.getDataBufferAll(Buffer.CACHE2), Buffer.TRANSACTION).showMessage("KSM")
            .showMessage("Nomint").showWrap("PRESS", "BUTToN")
            // version=02 ScriptAssembler.hash=0E=ScriptAssembler.Blake2b256 sign=01=ECDSA
            .setHeader(HashType.Blake2b256, SignType.ECDSA).getScript();
        return script;
    }

    public static String KSMNominateDoubleHashScriptSignature = Strings.padStart(
        "3046022100d8a181a6fd5adfbcd6ff51e362cb45a82f6344c219b22a7f06ffa28ce0ffda80022100a4409b5cddba174375068e56197444901e781c6d1f4f09ac53c1fefcf7a4e8ad",
        144, '0');

    public static String getKSMWithdrawScript() {
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
            .copyString("00").showMessage("KSM").showMessage("Withdr").showWrap("PRESS", "BUTToN")
            // version=02 ScriptAssembler.hash=0E=ScriptAssembler.Blake2b256 sign=01=ECDSA
            .setHeader(HashType.Blake2b256, SignType.ECDSA).getScript();
        return script;

    }

    public static String KSMWithdrawScriptSignature = Strings.padStart(
        "3046022100cafd70d98b3238276f566c9475301b46a6432216ca550f3777540ae12deccff9022100cb5136b0e7d96e807e047f23390e05f43dbf82b44e43026d698d4ee3a9e6bc5e",
        144, '0');

    public static String getKSMChillScript() {
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
            .copyString("00").showMessage("KSM").showMessage("Chill").showWrap("PRESS", "BUTToN")
            // version=02 ScriptAssembler.hash=0E=ScriptAssembler.Blake2b256 sign=01=ECDSA
            .setHeader(HashType.Blake2b256, SignType.ECDSA).getScript();
        return script;
    }

    public static String KSMChillScriptSignature = Strings.padStart(
        "304402200968ed85b7f76e64068e9a8e2537bf76a8982c2da7a149577f7d9bf78082f86e02202f39b27caf65174dfe844ebdebc35aa70892032b7cd8458065abe184f7066f4f",
        144, '0');

}
