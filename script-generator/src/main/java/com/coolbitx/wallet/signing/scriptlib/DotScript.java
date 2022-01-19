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
import com.coolbitx.wallet.signing.utils.ScriptAssembler.HashType;
import com.coolbitx.wallet.signing.utils.ScriptAssembler.SignType;
import com.coolbitx.wallet.signing.utils.ScriptData.Buffer;

public class DotScript {

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
        System.out.println("Ksm Bond Extra: \n" + getKSMUnbondScript() + "\n");
        System.out.println("Ksm Unbond: \n" + getKSMBondExtraScript() + "\n");
        System.out.println("Ksm Nominate Single Hash: \n" + getKSMNominateSingleHashScript() + "\n");
        System.out.println("Ksm Nominate Double Hash: \n" + getKSMNominateDoubleHashScript() + "\n");
        System.out.println("Ksm Withdraw: \n" + getKSMWithdrawScript() + "\n");
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
                // spec ver
                .copyArgument(argSpecVer)
                // tx ver
                .copyArgument(argTxVer)
                // genesis hash
                .copyArgument(argGenesisHash)
                // block hash
                .copyArgument(argBlockHash) 
                .showMessage("DOT")
                .copyString(HexUtil.toHexString("SS58PRE".getBytes()), Buffer.CACHE2)
                .copyString("00", Buffer.CACHE2)
                .copyArgument(argToAddr, Buffer.CACHE2)
                .hash(ScriptData.getDataBufferAll(Buffer.CACHE2), Buffer.CACHE2,
                        ScriptAssembler.Blake2b512)
                .baseConvert(ScriptData.getBuffer(Buffer.CACHE2, 7, 35),
                        Buffer.CACHE1, 0, ScriptAssembler.base58Charset,
                        ScriptAssembler.zeroInherit)
                .showAddress(ScriptData.getDataBufferAll(Buffer.CACHE1))
                .showAmount(argAmount, 10) 
                .showWrap("PRESS", "BUTToN")
                // version=02 ScriptAssembler.hash=0E=ScriptAssembler.Blake2b256 sign=01=ECDSA
                .setHeader(HashType.Blake2b256, SignType.ECDSA)
                .getScript();
        return script;
    }
    
    public static String DOTScriptSignature = "00003044022037AC2FFEEA6AF1DC07793EDBAC2FF1B6DE1E2812AD78D4C0D3CB950C6913726F022064370FC4A38247BB531396AD3C866DDB5A5350150C542806D9C1994A76F86645";

    public static String getDOTBondScript() {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptData argCallIndex = sac.getArgument(2);
        ScriptData argControllerAddr = sac.getArgument(32);
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
                // controller address
                .copyString("00").copyArgument(argControllerAddr)
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
                // spec ver
                .copyArgument(argSpecVer)
                // tx ver
                .copyArgument(argTxVer)
                // genesis hash
                .copyArgument(argGenesisHash)
                // block hash
                .copyArgument(argBlockHash) 
                .showMessage("DOT")
                .showMessage("Bond")
                .copyString(HexUtil.toHexString("SS58PRE".getBytes()), Buffer.CACHE2)
                .copyString("00", Buffer.CACHE2)
                .copyArgument(argControllerAddr, Buffer.CACHE2)
                .hash(ScriptData.getDataBufferAll(Buffer.CACHE2), Buffer.CACHE2,
                        ScriptAssembler.Blake2b512)
                .baseConvert(ScriptData.getBuffer(Buffer.CACHE2, 7, 35),
                        Buffer.CACHE1, 0, ScriptAssembler.base58Charset,
                        ScriptAssembler.zeroInherit)
                .showAddress(ScriptData.getDataBufferAll(Buffer.CACHE1))
                .showAmount(argAmount, 10) 
                .showWrap("PRESS", "BUTToN")
                // version=02 ScriptAssembler.hash=0E=ScriptAssembler.Blake2b256 sign=01=ECDSA
                .setHeader(HashType.Blake2b256, SignType.ECDSA)
                .getScript();
        return script;
    }

    public static String DOTBondScriptSignature = "00304502202199859BC3BB3C2ACC00272286B8D3FA3E326DC807553D15669FC22D7B82B8F8022100BE967FEA54A9466850C360BE24EFE481975234B650F9ED5EAEBFD06E80AC9BA4";

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
                // spec ver
                .copyArgument(argSpecVer)
                // tx ver
                .copyArgument(argTxVer)
                // genesis hash
                .copyArgument(argGenesisHash)
                // block hash
                .copyArgument(argBlockHash) 
                .showMessage("DOT")
                .showMessage("BondExt") 
                .showAmount(argMaxAdditional, 10)
                .showWrap("PRESS", "BUTToN")
                // version=02 ScriptAssembler.hash=0E=ScriptAssembler.Blake2b256 sign=01=ECDSA
                .setHeader(HashType.Blake2b256, SignType.ECDSA)
                .getScript();
        return script;
    }
    
    public static String DOTBondExtraScriptSignature = "00304502202CA928F80508DA5AECD768C6CB27B3C2C452D9788883B86FA40D9F089C8EFFCB022100FAAD5F3957A2EF2AB1C434AF027FA726A90FC9364D3E02EE817600626717C843";

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
                // spec ver
                .copyArgument(argSpecVer)
                // tx ver
                .copyArgument(argTxVer)
                // genesis hash
                .copyArgument(argGenesisHash)
                // block hash
                .copyArgument(argBlockHash) 
                .showMessage("DOT")
                .showMessage("Unbond") 
                .showAmount(argAmount, 10)
                .showWrap("PRESS", "BUTToN")
                // version=02 ScriptAssembler.hash=0E=ScriptAssembler.Blake2b256 sign=01=ECDSA
                .setHeader(HashType.Blake2b256, SignType.ECDSA)
                .getScript();
        return script;
    }
    
    public static String DOTUnbondScriptSignature = "30460221008748B8EB0559F5A0B5A24AFCFEDA61F1BFF88E7979FEBD109D47CBAB78A898010221008FA0D28A420069E6FE3B1F060A8B7176BC78B3B04579920F846879FF0B38A74B";

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
                // spec ver
                .copyArgument(argSpecVer)
                // tx ver
                .copyArgument(argTxVer)
                // genesis hash
                .copyArgument(argGenesisHash)
                // block hash
                .copyArgument(argBlockHash) 
                .showMessage("DOT")
                .showMessage("Nomint") 
                .showWrap("PRESS", "BUTToN")
                // version=02 ScriptAssembler.hash=0E=ScriptAssembler.Blake2b256 sign=01=ECDSA
                .setHeader(HashType.Blake2b256, SignType.ECDSA)
                .getScript();
        return script;
    }
    
    public static String DOTNominateSingleHashScriptSignature = "003045022100DC4EFBB4018886F2B3EE78817CCFA69A8C04D97EDE8CA007130D74983F9D3DD502207F181DD24288A2B809ADA6241EEAD72E94538A8B5A9A076D98B7E9A7AD6CB981";

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
                // spec ver
                .copyArgument(argSpecVer)
                // tx ver
                .copyArgument(argTxVer)
                // genesis hash
                .copyArgument(argGenesisHash)
                // block hash
                .copyArgument(argBlockHash)
                .hash(ScriptData.getDataBufferAll(Buffer.TRANSACTION),
                        Buffer.CACHE2, ScriptAssembler.Blake2b256)
                .clearBuffer(Buffer.TRANSACTION)
                .copyArgument(ScriptData.getDataBufferAll(Buffer.CACHE2),
                        Buffer.TRANSACTION)
                .showMessage("DOT") 
                .showMessage("Nomint")
                .showWrap("PRESS", "BUTToN")
                // version=02 ScriptAssembler.hash=0E=ScriptAssembler.Blake2b256 sign=01=ECDSA
                .setHeader(HashType.Blake2b256, SignType.ECDSA)
                .getScript();
        return script;
    }
    
    public static String DOTNominateDoubleHashScriptSignature = "304602210096D39AE252F24B8015187E93DCB27F642F6C87A5B4C22F3F1F01B9D0DE01986E022100ADF61CCE67428EA2F2139101DC483B428A59063F7A800D28220408EB0707FC7B";

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
                .ifEqual(argSlashingSpansNum, "00000000",
                        new ScriptAssembler().copyString("00000000").getScript(),
                        new ScriptAssembler().baseConvert(argSlashingSpansNum, Buffer.TRANSACTION, 4,
                                ScriptAssembler.binaryCharset,
                                ScriptAssembler.littleEndian).getScript())
                // MortalEra
                .copyArgument(argMortalEra)
                // nonce
                .scaleEncode(argNonce, Buffer.TRANSACTION)
                // tip
                .scaleEncode(argTip, Buffer.TRANSACTION)
                // spec ver
                .copyArgument(argSpecVer)
                // tx ver
                .copyArgument(argTxVer)
                // genesis hash
                .copyArgument(argGenesisHash)
                // block hash
                .copyArgument(argBlockHash) 
                .showMessage("DOT")
                .showMessage("Withdr") 
                .showWrap("PRESS", "BUTToN")
                // version=02 ScriptAssembler.hash=0E=ScriptAssembler.Blake2b256 sign=01=ECDSA
                .setHeader(HashType.Blake2b256, SignType.ECDSA)
                .getScript();
        return script;
    }
    
    public static String DOTWithdrawScriptSignature = "3046022100C223174F70194926F1E000B8887F1900C0D785BDBE7110D8BD43B6761A1A3564022100BEAB252B60C3BC08963FC016E5206F93CFCA8FB62CC90559184DD7D3016953E7";

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
                // spec ver
                .copyArgument(argSpecVer)
                // tx ver
                .copyArgument(argTxVer)
                // genesis hash
                .copyArgument(argGenesisHash)
                // block hash
                .copyArgument(argBlockHash)
                .showMessage("DOT")
                .showMessage("Chill")
                .showWrap("PRESS", "BUTToN")
                // version=02 ScriptAssembler.hash=0E=ScriptAssembler.Blake2b256 sign=01=ECDSA
                .setHeader(HashType.Blake2b256, SignType.ECDSA)
                .getScript();
        return script;
    }

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
                // set coinType to 0162
                .setCoinType(0x01b2)
                // call index
                .copyArgument(argCallIndex)
                // dest address
                .copyString("00")
                .copyArgument(argToAddr)
                // value
                .scaleEncode(argAmount, Buffer.TRANSACTION)
                // MortalEra
                .copyArgument(argMortalEra)
                // nonce
                .scaleEncode(argNonce, Buffer.TRANSACTION)
                // tip
                .scaleEncode(argTip, Buffer.TRANSACTION)
                // spec ver
                .copyArgument(argSpecVer)
                // tx ver
                .copyArgument(argTxVer)
                // genesis hash
                .copyArgument(argGenesisHash)
                // block hash
                .copyArgument(argBlockHash) 
                .showMessage("KSM")
                .copyString(HexUtil.toHexString("SS58PRE".getBytes()), Buffer.CACHE2)
                .copyString("02", Buffer.CACHE2)
                .copyArgument(argToAddr, Buffer.CACHE2)
                .hash(ScriptData.getDataBufferAll(Buffer.CACHE2), Buffer.CACHE2,
                        ScriptAssembler.Blake2b512)
                .baseConvert(ScriptData.getBuffer(Buffer.CACHE2, 7, 35),
                        Buffer.CACHE1, 0, ScriptAssembler.base58Charset,
                        ScriptAssembler.zeroInherit)
                .showAddress(ScriptData.getDataBufferAll(Buffer.CACHE1))
                .showAmount(argAmount, 12) 
                .showWrap("PRESS", "BUTToN")
                // version=02 ScriptAssembler.hash=0E=ScriptAssembler.Blake2b256 sign=01=ECDSA
                .setHeader(HashType.Blake2b256, SignType.ECDSA)
                .getScript();
        return script;
    }
    
    public static String KSMScriptSignature = "0000304402204007801EDD02289A1566636F1B63D8F81B45C8E3DAF4954C61AE7B555CFD1FA602202773CD1315D787D53CC956F540B68C5DB806FEE254139CD8F02F556A1BAFA7DC";

    public static String getKSMBondScript() {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptData argCallIndex = sac.getArgument(2);
        ScriptData argControllerAddr = sac.getArgument(32);
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
                .setCoinType(0x01b2)
                // call index
                .copyArgument(argCallIndex)
                // controller address
                .copyString("00") 
                .copyArgument(argControllerAddr)
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
                // spec ver
                .copyArgument(argSpecVer)
                // tx ver
                .copyArgument(argTxVer)
                // genesis hash
                .copyArgument(argGenesisHash)
                // block hash
                .copyArgument(argBlockHash) 
                .showMessage("KSM")
                .showMessage("Bond")
                .copyString(HexUtil.toHexString("SS58PRE".getBytes()), Buffer.CACHE2)
                .copyString("02", Buffer.CACHE2)
                .copyArgument(argControllerAddr, Buffer.CACHE2)
                .hash(ScriptData.getDataBufferAll(Buffer.CACHE2), Buffer.CACHE2,
                        ScriptAssembler.Blake2b512)
                .baseConvert(ScriptData.getBuffer(Buffer.CACHE2, 7, 35),
                        Buffer.CACHE1, 0, ScriptAssembler.base58Charset,
                        ScriptAssembler.zeroInherit)
                .showAddress(ScriptData.getDataBufferAll(Buffer.CACHE1))
                .showAmount(argAmount, 12) 
                .showWrap("PRESS", "BUTToN")
                // version=02 ScriptAssembler.hash=0E=ScriptAssembler.Blake2b256 sign=01=ECDSA
                .setHeader(HashType.Blake2b256, SignType.ECDSA)
                .getScript();
        return script;
    }
    
    public static String KSMBondScriptSignature = "00003044022054C4D1F81B043D19B238402E55285AB9BE0AA381026A228C1A43818550FDAF1A022051B7F4B2583B0A983D8991EA3AF09979B422B4BA8BF4FBAF6F365D0820D87A67";

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
                // set coinType to 0162
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
                // spec ver
                .copyArgument(argSpecVer)
                // tx ver
                .copyArgument(argTxVer)
                // genesis hash
                .copyArgument(argGenesisHash)
                // block hash
                .copyArgument(argBlockHash) 
                .showMessage("KSM")
                .showMessage("Unbond") 
                .showAmount(argAmount, 12)
                .showWrap("PRESS", "BUTToN")
                // version=02 ScriptAssembler.hash=0E=ScriptAssembler.Blake2b256 sign=01=ECDSA
                .setHeader(HashType.Blake2b256, SignType.ECDSA)
                .getScript();
        return script;
    }
    
    public static String KSMUnbondScriptSignature = "0030450221008D024F8E7B5EA2DE278F2F6077FE7A256294EF8D9D21C226491FE8FF9A20602C022056C31E390F935C6CBC5754D40EEE1EDF00E6F99227238D8431C4A0CD49A6F664";

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
                // set coinType to 0162
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
                // spec ver
                .copyArgument(argSpecVer)
                // tx ver
                .copyArgument(argTxVer)
                // genesis hash
                .copyArgument(argGenesisHash)
                // block hash
                .copyArgument(argBlockHash) 
                .showMessage("KSM")
                .showMessage("BondExt") 
                .showAmount(argMaxAdditional, 12)
                .showWrap("PRESS", "BUTToN")
                // version=02 ScriptAssembler.hash=0E=ScriptAssembler.Blake2b256 sign=01=ECDSA
                .setHeader(HashType.Blake2b256, SignType.ECDSA)
                .getScript();
        return script;
    }
    
    public static String KSMBondExtraScriptSignature = "003045022078B69F8B1E20CB722A2628EFE01AE5C0B4160533595D949A9C940255ACE30AE4022100E60F87E439C426FBEE9B6A4AD97B2A820501857A0D3126E508C32551133927BD";

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
                // set coinType to 0162
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
                // spec ver
                .copyArgument(argSpecVer)
                // tx ver
                .copyArgument(argTxVer)
                // genesis hash
                .copyArgument(argGenesisHash)
                // block hash
                .copyArgument(argBlockHash) 
                .showMessage("KSM")
                .showMessage("Nomint") 
                .showWrap("PRESS", "BUTToN")
                // version=02 ScriptAssembler.hash=0E=ScriptAssembler.Blake2b256 sign=01=ECDSA
                .setHeader(HashType.Blake2b256, SignType.ECDSA)
                .getScript();
        return script;
    }
    
    public static String KSMNominateSingleHashScriptSignature = "003045022050E47F2D21A88EB79DCB0AAD6B085B204C4A1103D90B2B19CB5DDC0582C6A73B0221008BE23FAE193108E22F56D97C7E2B38BC53A345B5191E2DF0106C52B9721E0F94";

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
                // set coinType to 0162
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
                // spec ver
                .copyArgument(argSpecVer)
                // tx ver
                .copyArgument(argTxVer)
                // genesis hash
                .copyArgument(argGenesisHash)
                // block hash
                .copyArgument(argBlockHash)
                .hash(ScriptData.getDataBufferAll(Buffer.TRANSACTION),
                        Buffer.CACHE2, ScriptAssembler.Blake2b256)
                .clearBuffer(Buffer.TRANSACTION)
                .copyArgument(ScriptData.getDataBufferAll(Buffer.CACHE2),
                        Buffer.TRANSACTION)
                .showMessage("KSM") 
                .showMessage("Nomint")
                .showWrap("PRESS", "BUTToN")
                // version=02 ScriptAssembler.hash=0E=ScriptAssembler.Blake2b256 sign=01=ECDSA
                .setHeader(HashType.Blake2b256, SignType.ECDSA)
                .getScript();
        return script;
    }
    
    public static String KSMNominateDoubleHashScriptSignature = "3046022100CF50DC25FA98B4FC9D545D8712836A949E92EA1F862BA3E7C6358C87DCA8AF57022100A04B35B38C897AC4ED0D93CD27E401DB6C85E25D83C32D51F176F92C11FBFE5D";

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
                // set coinType to 0162
                .setCoinType(0x01b2)
                // call index
                .copyArgument(argCallIndex)
                // target num_slashing_spans
                .ifEqual(argSlashingSpansNum, "00000000",
                        new ScriptAssembler().copyString("00000000").getScript(),
                        new ScriptAssembler().baseConvert(argSlashingSpansNum, Buffer.TRANSACTION, 4,
                                ScriptAssembler.binaryCharset,
                                ScriptAssembler.littleEndian).getScript())
                // MortalEra
                .copyArgument(argMortalEra)
                // nonce
                .scaleEncode(argNonce, Buffer.TRANSACTION)
                // tip
                .scaleEncode(argTip, Buffer.TRANSACTION)
                // spec ver
                .copyArgument(argSpecVer)
                // tx ver
                .copyArgument(argTxVer)
                // genesis hash
                .copyArgument(argGenesisHash)
                // block hash
                .copyArgument(argBlockHash) 
                .showMessage("KSM")
                .showMessage("Withdr") 
                .showWrap("PRESS", "BUTToN")
                // version=02 ScriptAssembler.hash=0E=ScriptAssembler.Blake2b256 sign=01=ECDSA
                .setHeader(HashType.Blake2b256, SignType.ECDSA)
                .getScript();
        return script;
        
    }
    
    public static String KSMWithdrawScriptSignature = "3046022100C2409FB6AC6E7B8F851C58CD16A8C9B1E9D3FD34EB57D10909C5D1937323EFA3022100D775322BE66104258839F9444BA1114A8EDB07E620F5EEF7F8D2A1CF052E047C";

}
