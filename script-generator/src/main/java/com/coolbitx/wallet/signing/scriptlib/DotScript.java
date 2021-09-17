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
            // version=02 ScriptAssembler.hash=0E=ScriptAssembler.Blake2b256 sign=01=ECDSA
            return "03020E01"
                            // set coinType to 0162
                            + ScriptAssembler.setCoinType(0x0162)
                            // call index
                            + ScriptAssembler.copyArgument(argCallIndex)
                            // dest address
                            + ScriptAssembler.copyString("00") + ScriptAssembler.copyArgument(argToAddr)
                            // value
                            + ScriptAssembler.scaleEncode(argAmount, Buffer.TRANSACTION)
                            // MortalEra
                            + ScriptAssembler.copyArgument(argMortalEra)
                            // nonce
                            + ScriptAssembler.scaleEncode(argNonce, Buffer.TRANSACTION)
                            // tip
                            + ScriptAssembler.scaleEncode(argTip, Buffer.TRANSACTION)
                            // spec ver
                            + ScriptAssembler.copyArgument(argSpecVer)
                            // tx ver
                            + ScriptAssembler.copyArgument(argTxVer)
                            // genesis hash
                            + ScriptAssembler.copyArgument(argGenesisHash)
                            // block hash
                            + ScriptAssembler.copyArgument(argBlockHash) + ScriptAssembler.showMessage("DOT")
                            + ScriptAssembler.copyString(HexUtil.toHexString("SS58PRE".getBytes()), Buffer.CACHE2)
                            + ScriptAssembler.copyString("00", Buffer.CACHE2)
                            + ScriptAssembler.copyArgument(argToAddr, Buffer.CACHE2)
                            + ScriptAssembler.hash(ScriptData.getDataBufferAll(Buffer.CACHE2), Buffer.CACHE2,
                                            ScriptAssembler.Blake2b512)
                            + ScriptAssembler.baseConvert(ScriptData.getBuffer(Buffer.CACHE2, 7, 35),
                                            Buffer.CACHE1, 0, ScriptAssembler.base58Charset,
                                            ScriptAssembler.zeroInherit)
                            + ScriptAssembler.showAddress(ScriptData.getDataBufferAll(Buffer.CACHE1))
                            + ScriptAssembler.showAmount(argAmount, 10) + ScriptAssembler.showWrap("PRESS", "BUTToN");
    }

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
            // version=02 ScriptAssembler.hash=0E=ScriptAssembler.Blake2b256 sign=01=ECDSA
            return "03020E01"
                            // set coinType to 0162
                            + ScriptAssembler.setCoinType(0x0162)
                            // call index
                            + ScriptAssembler.copyArgument(argCallIndex)
                            // controller address
                            + ScriptAssembler.copyString("00") + ScriptAssembler.copyArgument(argControllerAddr)
                            // value
                            + ScriptAssembler.scaleEncode(argAmount, Buffer.TRANSACTION)
                            // payeeType
                            + ScriptAssembler.copyArgument(argPayeeType)
                            // MortalEra
                            + ScriptAssembler.copyArgument(argMortalEra)
                            // nonce
                            + ScriptAssembler.scaleEncode(argNonce, Buffer.TRANSACTION)
                            // tip
                            + ScriptAssembler.scaleEncode(argTip, Buffer.TRANSACTION)
                            // spec ver
                            + ScriptAssembler.copyArgument(argSpecVer)
                            // tx ver
                            + ScriptAssembler.copyArgument(argTxVer)
                            // genesis hash
                            + ScriptAssembler.copyArgument(argGenesisHash)
                            // block hash
                            + ScriptAssembler.copyArgument(argBlockHash) + ScriptAssembler.showMessage("DOT")
                            + ScriptAssembler.showMessage("Bond")
                            + ScriptAssembler.copyString(HexUtil.toHexString("SS58PRE".getBytes()), Buffer.CACHE2)
                            + ScriptAssembler.copyString("00", Buffer.CACHE2)
                            + ScriptAssembler.copyArgument(argControllerAddr, Buffer.CACHE2)
                            + ScriptAssembler.hash(ScriptData.getDataBufferAll(Buffer.CACHE2), Buffer.CACHE2,
                                            ScriptAssembler.Blake2b512)
                            + ScriptAssembler.baseConvert(ScriptData.getBuffer(Buffer.CACHE2, 7, 35),
                                            Buffer.CACHE1, 0, ScriptAssembler.base58Charset,
                                            ScriptAssembler.zeroInherit)
                            + ScriptAssembler.showAddress(ScriptData.getDataBufferAll(Buffer.CACHE1))
                            + ScriptAssembler.showAmount(argAmount, 10) + ScriptAssembler.showWrap("PRESS", "BUTToN");
    }

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
            // version=02 ScriptAssembler.hash=0E=ScriptAssembler.Blake2b256 sign=01=ECDSA
            return "03020E01"
                            // set coinType to 0162
                            + ScriptAssembler.setCoinType(0x0162)
                            // call index
                            + ScriptAssembler.copyArgument(argCallIndex)
                            // value
                            + ScriptAssembler.scaleEncode(argMaxAdditional, Buffer.TRANSACTION)
                            // MortalEra
                            + ScriptAssembler.copyArgument(argMortalEra)
                            // nonce
                            + ScriptAssembler.scaleEncode(argNonce, Buffer.TRANSACTION)
                            // tip
                            + ScriptAssembler.scaleEncode(argTip, Buffer.TRANSACTION)
                            // spec ver
                            + ScriptAssembler.copyArgument(argSpecVer)
                            // tx ver
                            + ScriptAssembler.copyArgument(argTxVer)
                            // genesis hash
                            + ScriptAssembler.copyArgument(argGenesisHash)
                            // block hash
                            + ScriptAssembler.copyArgument(argBlockHash) + ScriptAssembler.showMessage("DOT")
                            + ScriptAssembler.showMessage("BondExt") + ScriptAssembler.showAmount(argMaxAdditional, 10)
                            + ScriptAssembler.showWrap("PRESS", "BUTToN");
    }

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
            // version=02 ScriptAssembler.hash=0E=ScriptAssembler.Blake2b256 sign=01=ECDSA
            return "03020E01"
                            // set coinType to 0162
                            + ScriptAssembler.setCoinType(0x0162)
                            // call index
                            + ScriptAssembler.copyArgument(argCallIndex)
                            // value
                            + ScriptAssembler.scaleEncode(argAmount, Buffer.TRANSACTION)
                            // MortalEra
                            + ScriptAssembler.copyArgument(argMortalEra)
                            // nonce
                            + ScriptAssembler.scaleEncode(argNonce, Buffer.TRANSACTION)
                            // tip
                            + ScriptAssembler.scaleEncode(argTip, Buffer.TRANSACTION)
                            // spec ver
                            + ScriptAssembler.copyArgument(argSpecVer)
                            // tx ver
                            + ScriptAssembler.copyArgument(argTxVer)
                            // genesis hash
                            + ScriptAssembler.copyArgument(argGenesisHash)
                            // block hash
                            + ScriptAssembler.copyArgument(argBlockHash) + ScriptAssembler.showMessage("DOT")
                            + ScriptAssembler.showMessage("Unbond") + ScriptAssembler.showAmount(argAmount, 10)
                            + ScriptAssembler.showWrap("PRESS", "BUTToN");
    }

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
            // version=02 ScriptAssembler.hash=0E=ScriptAssembler.Blake2b256 sign=01=ECDSA
            return "03020E01"
                            // set coinType to 0162
                            + ScriptAssembler.setCoinType(0x0162)
                            // call index
                            + ScriptAssembler.copyArgument(argCallIndex)
                            // target count
                            + ScriptAssembler.scaleEncode(argTargetCount, Buffer.TRANSACTION)
                            // target address
                            + ScriptAssembler.copyArgument(argTargetAddrs)
                            // MortalEra
                            + ScriptAssembler.copyArgument(argMortalEra)
                            // nonce
                            + ScriptAssembler.scaleEncode(argNonce, Buffer.TRANSACTION)
                            // tip
                            + ScriptAssembler.scaleEncode(argTip, Buffer.TRANSACTION)
                            // spec ver
                            + ScriptAssembler.copyArgument(argSpecVer)
                            // tx ver
                            + ScriptAssembler.copyArgument(argTxVer)
                            // genesis hash
                            + ScriptAssembler.copyArgument(argGenesisHash)
                            // block hash
                            + ScriptAssembler.copyArgument(argBlockHash) + ScriptAssembler.showMessage("DOT")
                            + ScriptAssembler.showMessage("Nomint") + ScriptAssembler.showWrap("PRESS", "BUTToN");
    }

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
            // version=02 ScriptAssembler.hash=0E=ScriptAssembler.Blake2b256 sign=01=ECDSA
            return "03020E01"
                            // set coinType to 0162
                            + ScriptAssembler.setCoinType(0x0162)
                            // call index
                            + ScriptAssembler.copyArgument(argCallIndex)
                            // target count
                            + ScriptAssembler.scaleEncode(argTargetCount, Buffer.TRANSACTION)
                            // target address
                            + ScriptAssembler.copyArgument(argTargetAddrs)
                            // MortalEra
                            + ScriptAssembler.copyArgument(argMortalEra)
                            // nonce
                            + ScriptAssembler.scaleEncode(argNonce, Buffer.TRANSACTION)
                            // tip
                            + ScriptAssembler.scaleEncode(argTip, Buffer.TRANSACTION)
                            // spec ver
                            + ScriptAssembler.copyArgument(argSpecVer)
                            // tx ver
                            + ScriptAssembler.copyArgument(argTxVer)
                            // genesis hash
                            + ScriptAssembler.copyArgument(argGenesisHash)
                            // block hash
                            + ScriptAssembler.copyArgument(argBlockHash)
                            + ScriptAssembler.hash(ScriptData.getDataBufferAll(Buffer.TRANSACTION),
                                            Buffer.CACHE2, ScriptAssembler.Blake2b256)
                            + ScriptAssembler.clearBuffer(Buffer.TRANSACTION)
                            + ScriptAssembler.copyArgument(ScriptData.getDataBufferAll(Buffer.CACHE2),
                                            Buffer.TRANSACTION)
                            + ScriptAssembler.showMessage("DOT") + ScriptAssembler.showMessage("Nomint")
                            + ScriptAssembler.showWrap("PRESS", "BUTToN");
    }

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
            // version=02 ScriptAssembler.hash=0E=ScriptAssembler.Blake2b256 sign=01=ECDSA
            return "03020E01"
                            // set coinType to 0162
                            + ScriptAssembler.setCoinType(0x0162)
                            // call index
                            + ScriptAssembler.copyArgument(argCallIndex)
                            // target num_slashing_spans
                            + ScriptAssembler.ifEqual(argSlashingSpansNum, "00000000",
                                            ScriptAssembler.copyString("00000000"),
                                            ScriptAssembler.baseConvert(argSlashingSpansNum, Buffer.TRANSACTION, 4,
                                                            ScriptAssembler.binaryCharset,
                                                            ScriptAssembler.littleEndian))
                            // MortalEra
                            + ScriptAssembler.copyArgument(argMortalEra)
                            // nonce
                            + ScriptAssembler.scaleEncode(argNonce, Buffer.TRANSACTION)
                            // tip
                            + ScriptAssembler.scaleEncode(argTip, Buffer.TRANSACTION)
                            // spec ver
                            + ScriptAssembler.copyArgument(argSpecVer)
                            // tx ver
                            + ScriptAssembler.copyArgument(argTxVer)
                            // genesis hash
                            + ScriptAssembler.copyArgument(argGenesisHash)
                            // block hash
                            + ScriptAssembler.copyArgument(argBlockHash) + ScriptAssembler.showMessage("DOT")
                            + ScriptAssembler.showMessage("Withdr") + ScriptAssembler.showWrap("PRESS", "BUTToN");
    }

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
            // version=02 ScriptAssembler.hash=0E=ScriptAssembler.Blake2b256 sign=01=ECDSA
            return "03020E01"
                            // set coinType to 0162
                            + ScriptAssembler.setCoinType(0x0162)
                            // call index
                            + ScriptAssembler.copyArgument(argCallIndex)
                            // MortalEra
                            + ScriptAssembler.copyArgument(argMortalEra)
                            // nonce
                            + ScriptAssembler.scaleEncode(argNonce, Buffer.TRANSACTION)
                            // tip
                            + ScriptAssembler.scaleEncode(argTip, Buffer.TRANSACTION)
                            // spec ver
                            + ScriptAssembler.copyArgument(argSpecVer)
                            // tx ver
                            + ScriptAssembler.copyArgument(argTxVer)
                            // genesis hash
                            + ScriptAssembler.copyArgument(argGenesisHash)
                            // block hash
                            + ScriptAssembler.copyArgument(argBlockHash) 
                            + ScriptAssembler.showMessage("DOT")
                            + ScriptAssembler.showMessage("Chill")
                            + ScriptAssembler.showWrap("PRESS", "BUTToN");
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
            // version=02 ScriptAssembler.hash=0E=ScriptAssembler.Blake2b256 sign=01=ECDSA
            return "03020E01"
                            // set coinType to 0162
                            + ScriptAssembler.setCoinType(0x01b2)
                            // call index
                            + ScriptAssembler.copyArgument(argCallIndex)
                            // dest address
                            + ScriptAssembler.copyString("00") + ScriptAssembler.copyArgument(argToAddr)
                            // value
                            + ScriptAssembler.scaleEncode(argAmount, Buffer.TRANSACTION)
                            // MortalEra
                            + ScriptAssembler.copyArgument(argMortalEra)
                            // nonce
                            + ScriptAssembler.scaleEncode(argNonce, Buffer.TRANSACTION)
                            // tip
                            + ScriptAssembler.scaleEncode(argTip, Buffer.TRANSACTION)
                            // spec ver
                            + ScriptAssembler.copyArgument(argSpecVer)
                            // tx ver
                            + ScriptAssembler.copyArgument(argTxVer)
                            // genesis hash
                            + ScriptAssembler.copyArgument(argGenesisHash)
                            // block hash
                            + ScriptAssembler.copyArgument(argBlockHash) + ScriptAssembler.showMessage("KSM")
                            + ScriptAssembler.copyString(HexUtil.toHexString("SS58PRE".getBytes()), Buffer.CACHE2)
                            + ScriptAssembler.copyString("02", Buffer.CACHE2)
                            + ScriptAssembler.copyArgument(argToAddr, Buffer.CACHE2)
                            + ScriptAssembler.hash(ScriptData.getDataBufferAll(Buffer.CACHE2), Buffer.CACHE2,
                                            ScriptAssembler.Blake2b512)
                            + ScriptAssembler.baseConvert(ScriptData.getBuffer(Buffer.CACHE2, 7, 35),
                                            Buffer.CACHE1, 0, ScriptAssembler.base58Charset,
                                            ScriptAssembler.zeroInherit)
                            + ScriptAssembler.showAddress(ScriptData.getDataBufferAll(Buffer.CACHE1))
                            + ScriptAssembler.showAmount(argAmount, 12) + ScriptAssembler.showWrap("PRESS", "BUTToN");
    }

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
            // version=02 ScriptAssembler.hash=0E=ScriptAssembler.Blake2b256 sign=01=ECDSA
            return "03020E01"
                            // set coinType to 0162
                            + ScriptAssembler.setCoinType(0x01b2)
                            // call index
                            + ScriptAssembler.copyArgument(argCallIndex)
                            // controller address
                            + ScriptAssembler.copyString("00") + ScriptAssembler.copyArgument(argControllerAddr)
                            // value
                            + ScriptAssembler.scaleEncode(argAmount, Buffer.TRANSACTION)
                            // payeeType
                            + ScriptAssembler.copyArgument(argPayeeType)
                            // MortalEra
                            + ScriptAssembler.copyArgument(argMortalEra)
                            // nonce
                            + ScriptAssembler.scaleEncode(argNonce, Buffer.TRANSACTION)
                            // tip
                            + ScriptAssembler.scaleEncode(argTip, Buffer.TRANSACTION)
                            // spec ver
                            + ScriptAssembler.copyArgument(argSpecVer)
                            // tx ver
                            + ScriptAssembler.copyArgument(argTxVer)
                            // genesis hash
                            + ScriptAssembler.copyArgument(argGenesisHash)
                            // block hash
                            + ScriptAssembler.copyArgument(argBlockHash) + ScriptAssembler.showMessage("KSM")
                            + ScriptAssembler.showMessage("Bond")
                            + ScriptAssembler.copyString(HexUtil.toHexString("SS58PRE".getBytes()), Buffer.CACHE2)
                            + ScriptAssembler.copyString("02", Buffer.CACHE2)
                            + ScriptAssembler.copyArgument(argControllerAddr, Buffer.CACHE2)
                            + ScriptAssembler.hash(ScriptData.getDataBufferAll(Buffer.CACHE2), Buffer.CACHE2,
                                            ScriptAssembler.Blake2b512)
                            + ScriptAssembler.baseConvert(ScriptData.getBuffer(Buffer.CACHE2, 7, 35),
                                            Buffer.CACHE1, 0, ScriptAssembler.base58Charset,
                                            ScriptAssembler.zeroInherit)
                            + ScriptAssembler.showAddress(ScriptData.getDataBufferAll(Buffer.CACHE1))
                            + ScriptAssembler.showAmount(argAmount, 12) + ScriptAssembler.showWrap("PRESS", "BUTToN");
    }

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
            // version=02 ScriptAssembler.hash=0E=ScriptAssembler.Blake2b256 sign=01=ECDSA
            return "03020E01"
                            // set coinType to 0162
                            + ScriptAssembler.setCoinType(0x01b2)
                            // call index
                            + ScriptAssembler.copyArgument(argCallIndex)
                            // value
                            + ScriptAssembler.scaleEncode(argAmount, Buffer.TRANSACTION)
                            // MortalEra
                            + ScriptAssembler.copyArgument(argMortalEra)
                            // nonce
                            + ScriptAssembler.scaleEncode(argNonce, Buffer.TRANSACTION)
                            // tip
                            + ScriptAssembler.scaleEncode(argTip, Buffer.TRANSACTION)
                            // spec ver
                            + ScriptAssembler.copyArgument(argSpecVer)
                            // tx ver
                            + ScriptAssembler.copyArgument(argTxVer)
                            // genesis hash
                            + ScriptAssembler.copyArgument(argGenesisHash)
                            // block hash
                            + ScriptAssembler.copyArgument(argBlockHash) + ScriptAssembler.showMessage("KSM")
                            + ScriptAssembler.showMessage("Unbond") + ScriptAssembler.showAmount(argAmount, 12)
                            + ScriptAssembler.showWrap("PRESS", "BUTToN");
    }

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
            // version=02 ScriptAssembler.hash=0E=ScriptAssembler.Blake2b256 sign=01=ECDSA
            return "03020E01"
                            // set coinType to 0162
                            + ScriptAssembler.setCoinType(0x01b2)
                            // call index
                            + ScriptAssembler.copyArgument(argCallIndex)
                            // value
                            + ScriptAssembler.scaleEncode(argMaxAdditional, Buffer.TRANSACTION)
                            // MortalEra
                            + ScriptAssembler.copyArgument(argMortalEra)
                            // nonce
                            + ScriptAssembler.scaleEncode(argNonce, Buffer.TRANSACTION)
                            // tip
                            + ScriptAssembler.scaleEncode(argTip, Buffer.TRANSACTION)
                            // spec ver
                            + ScriptAssembler.copyArgument(argSpecVer)
                            // tx ver
                            + ScriptAssembler.copyArgument(argTxVer)
                            // genesis hash
                            + ScriptAssembler.copyArgument(argGenesisHash)
                            // block hash
                            + ScriptAssembler.copyArgument(argBlockHash) + ScriptAssembler.showMessage("KSM")
                            + ScriptAssembler.showMessage("BondExt") + ScriptAssembler.showAmount(argMaxAdditional, 12)
                            + ScriptAssembler.showWrap("PRESS", "BUTToN");
    }

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
            // version=02 ScriptAssembler.hash=0E=ScriptAssembler.Blake2b256 sign=01=ECDSA
            return "03020E01"
                            // set coinType to 0162
                            + ScriptAssembler.setCoinType(0x01b2)
                            // call index
                            + ScriptAssembler.copyArgument(argCallIndex)
                            // target count
                            + ScriptAssembler.scaleEncode(argTargetCount, Buffer.TRANSACTION)
                            // target address
                            + ScriptAssembler.copyArgument(argTargetAddrs)
                            // MortalEra
                            + ScriptAssembler.copyArgument(argMortalEra)
                            // nonce
                            + ScriptAssembler.scaleEncode(argNonce, Buffer.TRANSACTION)
                            // tip
                            + ScriptAssembler.scaleEncode(argTip, Buffer.TRANSACTION)
                            // spec ver
                            + ScriptAssembler.copyArgument(argSpecVer)
                            // tx ver
                            + ScriptAssembler.copyArgument(argTxVer)
                            // genesis hash
                            + ScriptAssembler.copyArgument(argGenesisHash)
                            // block hash
                            + ScriptAssembler.copyArgument(argBlockHash) + ScriptAssembler.showMessage("KSM")
                            + ScriptAssembler.showMessage("Nomint") + ScriptAssembler.showWrap("PRESS", "BUTToN");
    }

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
            // version=02 ScriptAssembler.hash=0E=ScriptAssembler.Blake2b256 sign=01=ECDSA
            return "03020E01"
                            // set coinType to 0162
                            + ScriptAssembler.setCoinType(0x01b2)
                            // call index
                            + ScriptAssembler.copyArgument(argCallIndex)
                            // target count
                            + ScriptAssembler.scaleEncode(argTargetCount, Buffer.TRANSACTION)
                            // target address
                            + ScriptAssembler.copyArgument(argTargetAddrs)
                            // MortalEra
                            + ScriptAssembler.copyArgument(argMortalEra)
                            // nonce
                            + ScriptAssembler.scaleEncode(argNonce, Buffer.TRANSACTION)
                            // tip
                            + ScriptAssembler.scaleEncode(argTip, Buffer.TRANSACTION)
                            // spec ver
                            + ScriptAssembler.copyArgument(argSpecVer)
                            // tx ver
                            + ScriptAssembler.copyArgument(argTxVer)
                            // genesis hash
                            + ScriptAssembler.copyArgument(argGenesisHash)
                            // block hash
                            + ScriptAssembler.copyArgument(argBlockHash)
                            + ScriptAssembler.hash(ScriptData.getDataBufferAll(Buffer.TRANSACTION),
                                            Buffer.CACHE2, ScriptAssembler.Blake2b256)
                            + ScriptAssembler.clearBuffer(Buffer.TRANSACTION)
                            + ScriptAssembler.copyArgument(ScriptData.getDataBufferAll(Buffer.CACHE2),
                                            Buffer.TRANSACTION)
                            + ScriptAssembler.showMessage("KSM") + ScriptAssembler.showMessage("Nomint")
                            + ScriptAssembler.showWrap("PRESS", "BUTToN");
    }

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
            // version=02 ScriptAssembler.hash=0E=ScriptAssembler.Blake2b256 sign=01=ECDSA
            return "03020E01"
                            // set coinType to 0162
                            + ScriptAssembler.setCoinType(0x01b2)
                            // call index
                            + ScriptAssembler.copyArgument(argCallIndex)
                            // target num_slashing_spans
                            + ScriptAssembler.ifEqual(argSlashingSpansNum, "00000000",
                                            ScriptAssembler.copyString("00000000"),
                                            ScriptAssembler.baseConvert(argSlashingSpansNum, Buffer.TRANSACTION, 4,
                                                            ScriptAssembler.binaryCharset,
                                                            ScriptAssembler.littleEndian))
                            // MortalEra
                            + ScriptAssembler.copyArgument(argMortalEra)
                            // nonce
                            + ScriptAssembler.scaleEncode(argNonce, Buffer.TRANSACTION)
                            // tip
                            + ScriptAssembler.scaleEncode(argTip, Buffer.TRANSACTION)
                            // spec ver
                            + ScriptAssembler.copyArgument(argSpecVer)
                            // tx ver
                            + ScriptAssembler.copyArgument(argTxVer)
                            // genesis hash
                            + ScriptAssembler.copyArgument(argGenesisHash)
                            // block hash
                            + ScriptAssembler.copyArgument(argBlockHash) + ScriptAssembler.showMessage("KSM")
                            + ScriptAssembler.showMessage("Withdr") + ScriptAssembler.showWrap("PRESS", "BUTToN");
    }

}
