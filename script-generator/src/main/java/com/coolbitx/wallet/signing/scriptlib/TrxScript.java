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

public class TrxScript {

    public static void listAll() {
        System.out.println("Trx: \n" + getTRXScript() + "\n");
        System.out.println("Trx trc20: \n" + getTRC20Script() + "\n");
        System.out.println("Trx Freeze: \n" + getTRXFreezeScript() + "\n");
        System.out.println("Trx Freeze Without Receiver: \n" + getTRXFreezeScriptNoReceiver() + "\n");
        System.out.println("Trx Unfreeze: \n" + getTRXUnfreezeScript() + "\n");
        System.out.println("Trx Unfreeze Without Receiver: \n" + getTRXUnfreezeScriptNoReceiver() + "\n");
        System.out.println("Trx Vote Witness: \n" + getTRXVoteWitnessScript() + "\n");
        System.out.println("Trx Withdraw: \n" + getTRXWithdrawScript() + "\n");
    }

    private static int typeString = 2;
    private static int typeInt = 0;

    public static String getTRXScript() {
        // ScriptArgumentComposer sac = new ScriptArgumentComposer();
        // ScriptData argBlockBytes = sac.getBufer(2);
        // ScriptData argBlockHash = sac.getBufer(8);
        // ScriptData argExpiration = sac.getArgumentRightJustified(10);
        // ScriptData argOwnerAddr = sac.getBufer(21);
        // ScriptData argToAddr = sac.getBufer(21);
        // ScriptData argAmount = sac.getArgumentRightJustified(10);
        // ScriptData argTimestamp = sac.getArgumentRightJustified(10);
        // version=03 ScriptAssembler.hash=02=ScriptAssembler.SHA256 sign=01=ECDSA

        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptData argBlockBytes = sac.getArgument(2);
        ScriptData argBlockHash = sac.getArgument(8);
        ScriptData argExpiration = sac.getArgumentRightJustified(10);
        ScriptData argOwnerAddr = sac.getArgument(21);
        ScriptData argToAddr = sac.getArgument(21);
        ScriptData argAmount = sac.getArgumentRightJustified(10);
        ScriptData argTimestamp = sac.getArgumentRightJustified(10);

        return "03030201"
                // set coinType to C3
                + ScriptAssembler.setCoinType(0xC3)
                // ref_block_bytes
                + ScriptAssembler.copyString("0a") + ScriptAssembler.protobuf(argBlockBytes, typeString)
                // ref_block_hash
                + ScriptAssembler.copyString("22") + ScriptAssembler.protobuf(argBlockHash, typeString)
                // expiration
                + ScriptAssembler.copyString("40") + ScriptAssembler.protobuf(argExpiration, typeInt)
                // contract object
                + ScriptAssembler.copyString("5a") + ScriptAssembler.arrayPointer()
                // contract type
                + ScriptAssembler.copyString("0801")
                // parameter object
                + ScriptAssembler.copyString("12") + ScriptAssembler.arrayPointer()
                // type url
                + ScriptAssembler.copyString("0a2d") + ScriptAssembler.copyString(
                HexUtil.toHexString("type.googleapis.com/protocol.TransferContract".getBytes()))
                // value object
                + ScriptAssembler.copyString("12") + ScriptAssembler.arrayPointer()
                // owner address
                + ScriptAssembler.copyString("0a") + ScriptAssembler.protobuf(argOwnerAddr, typeString)
                // to address
                + ScriptAssembler.copyString("12") + ScriptAssembler.protobuf(argToAddr, typeString)
                // amount
                + ScriptAssembler.copyString("18") + ScriptAssembler.protobuf(argAmount, typeInt)
                + ScriptAssembler.arrayEnd() + ScriptAssembler.arrayEnd() + ScriptAssembler.arrayEnd()
                // timestamp
                + ScriptAssembler.copyString("70") + ScriptAssembler.protobuf(argTimestamp, typeInt)
                + ScriptAssembler.showMessage("TRX")
                + ScriptAssembler.copyArgument(argToAddr, Buffer.CACHE2)
                + ScriptAssembler.hash(ScriptData.getDataBufferAll(Buffer.CACHE2), Buffer.CACHE2,
                        ScriptAssembler.DoubleSHA256)
                + ScriptAssembler.baseConvert(ScriptData.getBuffer(Buffer.CACHE2, 0, 25),
                        Buffer.CACHE1, 0, ScriptAssembler.base58Charset,
                        ScriptAssembler.zeroInherit)
                + ScriptAssembler.showAddress(ScriptData.getDataBufferAll(Buffer.CACHE1))
                + ScriptAssembler.showAmount(argAmount, 6) + ScriptAssembler.showWrap("PRESS", "BUTToN");
    }
    
    public static String TRXScriptSignature = "000030440220753082AA5C31111BC1A081601E46F79219A77D49A0EBC4D70C1F66792FD858400220483BFC13CAB301E4E54F6716251E58410A6F50D117C49A891069F18D56391CBD";

    public static String getTRC20Script() {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptData argBlockBytes = sac.getArgument(2);
        ScriptData argBlockHash = sac.getArgument(8);
        ScriptData argExpiration = sac.getArgumentRightJustified(10);
        ScriptData argOwnerAddr = sac.getArgument(20);
        ScriptData argTokenInfo = sac.getArgumentUnion(0, 29);
        ScriptData argDecimal = sac.getArgument(1);
        ScriptData argNameLength = sac.getArgument(1);
        ScriptData argName = sac.getArgumentVariableLength(7);
        ScriptData argContractAddr = sac.getArgument(20);
        ScriptData argSign = sac.getArgument(72);
        ScriptData argTo = sac.getArgument(20);
        ScriptData argValue = sac.getArgument(12);
        ScriptData argTimestamp = sac.getArgumentRightJustified(10);
        ScriptData argFeeLimit = sac.getArgumentRightJustified(10);
        // version=03 ScriptAssembler.hash=02=ScriptAssembler.SHA256 sign=01=ECDSA
        return "03030201"
                // set coinType to C3
                + ScriptAssembler.setCoinType(0xC3)
                // ref_block_bytes
                + ScriptAssembler.copyString("0a") + ScriptAssembler.protobuf(argBlockBytes, typeString)
                // ref_block_hash
                + ScriptAssembler.copyString("22") + ScriptAssembler.protobuf(argBlockHash, typeString)
                // expiration
                + ScriptAssembler.copyString("40") + ScriptAssembler.protobuf(argExpiration, typeInt)
                // contract object
                + ScriptAssembler.copyString("5a") + ScriptAssembler.arrayPointer()
                // contract type
                + ScriptAssembler.copyString("081f")
                // parameter object
                + ScriptAssembler.copyString("12") + ScriptAssembler.arrayPointer()
                // type url
                + ScriptAssembler.copyString("0a31") + ScriptAssembler.copyString(HexUtil.toHexString(
                "type.googleapis.com/protocol.TriggerSmartContract".getBytes()))
                // value object
                + ScriptAssembler.copyString("12") + ScriptAssembler.arrayPointer()
                // owner address
                + ScriptAssembler.copyString("0a") + ScriptAssembler.copyString("41", Buffer.CACHE2)
                + ScriptAssembler.copyArgument(argOwnerAddr, Buffer.CACHE2)
                + ScriptAssembler.protobuf(ScriptData.getDataBufferAll(Buffer.CACHE2), typeString)
                // contract address
                + ScriptAssembler.copyString("12") + ScriptAssembler.clearBuffer(Buffer.CACHE2)
                + ScriptAssembler.copyString("41", Buffer.CACHE2)
                + ScriptAssembler.copyArgument(argContractAddr, Buffer.CACHE2)
                + ScriptAssembler.protobuf(ScriptData.getDataBufferAll(Buffer.CACHE2), typeString)
                // data
                + ScriptAssembler.copyString("22") + ScriptAssembler.clearBuffer(Buffer.CACHE2)
                + ScriptAssembler.copyString("a9059cbb", Buffer.CACHE2)
                + ScriptAssembler.copyString("000000000000000000000000", Buffer.CACHE2)
                + ScriptAssembler.copyArgument(argTo, Buffer.CACHE2)
                + ScriptAssembler.copyString("0000000000000000000000000000000000000000", Buffer.CACHE2)
                + ScriptAssembler.copyArgument(argValue, Buffer.CACHE2)
                + ScriptAssembler.protobuf(ScriptData.getDataBufferAll(Buffer.CACHE2), typeString)
                + ScriptAssembler.arrayEnd() + ScriptAssembler.arrayEnd() + ScriptAssembler.arrayEnd()
                // timestamp
                + ScriptAssembler.copyString("70") + ScriptAssembler.protobuf(argTimestamp, typeInt)
                // fee limit
                + ScriptAssembler.copyString("9001") + ScriptAssembler.protobuf(argFeeLimit, typeInt)
                // display chain
                + ScriptAssembler.showMessage("TRX")
                // display token
                + ScriptAssembler.clearBuffer(Buffer.CACHE2)
                + ScriptAssembler.ifSigned(argTokenInfo, argSign, "",
                        ScriptAssembler.copyString(HexUtil.toHexString("@"), Buffer.CACHE2))
                + ScriptAssembler.setBufferInt(argNameLength, 1, 7)
                + ScriptAssembler.copyArgument(argName, Buffer.CACHE2)
                + ScriptAssembler.showMessage(ScriptData.getDataBufferAll(Buffer.CACHE2))
                // display to address
                + ScriptAssembler.clearBuffer(Buffer.CACHE2)
                + ScriptAssembler.copyString("41", Buffer.CACHE2)
                + ScriptAssembler.copyArgument(argTo, Buffer.CACHE2)
                + ScriptAssembler.hash(ScriptData.getDataBufferAll(Buffer.CACHE2), Buffer.CACHE2,
                        ScriptAssembler.DoubleSHA256)
                + ScriptAssembler.baseConvert(ScriptData.getBuffer(Buffer.CACHE2, 0, 25),
                        Buffer.CACHE1, 0, ScriptAssembler.base58Charset,
                        ScriptAssembler.zeroInherit)
                + ScriptAssembler.showAddress(ScriptData.getDataBufferAll(Buffer.CACHE1))
                // display amount
                + ScriptAssembler.setBufferInt(argDecimal, 0, 20)
                + ScriptAssembler.showAmount(argValue, 1000) + ScriptAssembler.showPressButton();
    }

    public static String TRC20ScriptSignature = "0000304402202D928B902A6D63BFD40FCDB4A5BC24977049081248F256D779D869D5A1925688022015E6630A34B4161E342EE855F698F7272AC83B781F273E726DC77DA40C75F86C";

    public static String getTRXFreezeScript() {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptData argBlockBytes = sac.getArgument(2);
        ScriptData argBlockHash = sac.getArgument(8);
        ScriptData argExpiration = sac.getArgumentRightJustified(10);
        ScriptData argOwnerAddr = sac.getArgument(21);
        ScriptData argFrozenBalance = sac.getArgumentRightJustified(10);
        ScriptData argFrozenDuration = sac.getArgumentRightJustified(10);
        ScriptData argResource = sac.getArgument(1);
        ScriptData argReceiverAddr = sac.getArgument(21);
        ScriptData argTimestamp = sac.getArgumentRightJustified(10);
        // version=03 ScriptAssembler.hash=02=ScriptAssembler.SHA256 sign=01=ECDSA
        return "03030201"
                // set coinType to C3
                + ScriptAssembler.setCoinType(0xC3)
                // ref_block_bytes
                + ScriptAssembler.copyString("0a") + ScriptAssembler.protobuf(argBlockBytes, typeString)
                // ref_block_hash
                + ScriptAssembler.copyString("22") + ScriptAssembler.protobuf(argBlockHash, typeString)
                // expiration
                + ScriptAssembler.copyString("40") + ScriptAssembler.protobuf(argExpiration, typeInt)
                // contracts array
                + ScriptAssembler.copyString("5a") + ScriptAssembler.arrayPointer()
                // contract type
                + ScriptAssembler.copyString("080b")
                // parameter object
                + ScriptAssembler.copyString("12") + ScriptAssembler.arrayPointer()
                // type url
                + ScriptAssembler.copyString("0a32") + ScriptAssembler.copyString(HexUtil.toHexString(
                "type.googleapis.com/protocol.FreezeBalanceContract".getBytes()))
                // value object
                + ScriptAssembler.copyString("12") + ScriptAssembler.arrayPointer()
                // ownerAddr
                + ScriptAssembler.copyString("0a") + ScriptAssembler.protobuf(argOwnerAddr, typeString)
                // frozenBalance
                + ScriptAssembler.copyString("10") + ScriptAssembler.protobuf(argFrozenBalance, typeInt)
                // frozenDuration
                + ScriptAssembler.copyString("18") + ScriptAssembler.protobuf(argFrozenDuration, typeInt)
                // resource
                + ScriptAssembler.ifEqual(argResource, "01", ScriptAssembler.copyString("5001"), "")
                + ScriptAssembler.ifEqual(argResource, "02", ScriptAssembler.copyString("5002"), "")
                // receiverAddr
                + ScriptAssembler.copyString("7a") + ScriptAssembler.protobuf(argReceiverAddr, typeString)
                + ScriptAssembler.arrayEnd() + ScriptAssembler.arrayEnd() + ScriptAssembler.arrayEnd()
                // timestamp
                + ScriptAssembler.copyString("70") + ScriptAssembler.protobuf(argTimestamp, typeInt)
                + ScriptAssembler.showMessage("TRX") + ScriptAssembler.showMessage("Freeze")
                + ScriptAssembler.copyArgument(argReceiverAddr, Buffer.CACHE2)
                + ScriptAssembler.hash(ScriptData.getDataBufferAll(Buffer.CACHE2), Buffer.CACHE2,
                        ScriptAssembler.DoubleSHA256)
                + ScriptAssembler.baseConvert(ScriptData.getBuffer(Buffer.CACHE2, 0, 25),
                        Buffer.CACHE1, 0, ScriptAssembler.base58Charset,
                        ScriptAssembler.zeroInherit)
                + ScriptAssembler.showAddress(ScriptData.getDataBufferAll(Buffer.CACHE1))
                + ScriptAssembler.showAmount(argFrozenBalance, 6) + ScriptAssembler.showPressButton();
    }

    public static String TRXFreezeScriptSignature = "0030450220480BB8323075EB39062305E3EAB8E126F4A80477EA254468BDEA0D4948619AEA022100976633A4A2C5F09727CEB41751CFD0F8FE037BBE0E18436AB7507124A71426C6";

    public static String getTRXFreezeScriptNoReceiver() {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptData argBlockBytes = sac.getArgument(2);
        ScriptData argBlockHash = sac.getArgument(8);
        ScriptData argExpiration = sac.getArgumentRightJustified(10);
        ScriptData argOwnerAddr = sac.getArgument(21);
        ScriptData argFrozenBalance = sac.getArgumentRightJustified(10);
        ScriptData argFrozenDuration = sac.getArgumentRightJustified(10);
        ScriptData argResource = sac.getArgument(1);
        ScriptData argTimestamp = sac.getArgumentRightJustified(10);
        // version=03 ScriptAssembler.hash=02=ScriptAssembler.SHA256 sign=01=ECDSA
        return "03030201"
                // set coinType to C3
                + ScriptAssembler.setCoinType(0xC3)
                // ref_block_bytes
                + ScriptAssembler.copyString("0a") + ScriptAssembler.protobuf(argBlockBytes, typeString)
                // ref_block_hash
                + ScriptAssembler.copyString("22") + ScriptAssembler.protobuf(argBlockHash, typeString)
                // expiration
                + ScriptAssembler.copyString("40") + ScriptAssembler.protobuf(argExpiration, typeInt)
                // contracts array
                + ScriptAssembler.copyString("5a") + ScriptAssembler.arrayPointer()
                // contract type
                + ScriptAssembler.copyString("080b")
                // parameter object
                + ScriptAssembler.copyString("12") + ScriptAssembler.arrayPointer()
                // type url
                + ScriptAssembler.copyString("0a32") + ScriptAssembler.copyString(HexUtil.toHexString(
                "type.googleapis.com/protocol.FreezeBalanceContract".getBytes()))
                // value object
                + ScriptAssembler.copyString("12") + ScriptAssembler.arrayPointer()
                // ownerAddr
                + ScriptAssembler.copyString("0a") + ScriptAssembler.protobuf(argOwnerAddr, typeString)
                // frozenBalance
                + ScriptAssembler.copyString("10") + ScriptAssembler.protobuf(argFrozenBalance, typeInt)
                // frozenDuration
                + ScriptAssembler.copyString("18") + ScriptAssembler.protobuf(argFrozenDuration, typeInt)
                // resource
                + ScriptAssembler.ifEqual(argResource, "01", ScriptAssembler.copyString("5001"), "")
                + ScriptAssembler.ifEqual(argResource, "02", ScriptAssembler.copyString("5002"), "")
                + ScriptAssembler.arrayEnd() + ScriptAssembler.arrayEnd() + ScriptAssembler.arrayEnd()
                // timestamp
                + ScriptAssembler.copyString("70") + ScriptAssembler.protobuf(argTimestamp, typeInt)
                + ScriptAssembler.showMessage("TRX") + ScriptAssembler.showMessage("Freeze")
                + ScriptAssembler.copyArgument(argOwnerAddr, Buffer.CACHE2)
                + ScriptAssembler.hash(ScriptData.getDataBufferAll(Buffer.CACHE2), Buffer.CACHE2,
                        ScriptAssembler.DoubleSHA256)
                + ScriptAssembler.baseConvert(ScriptData.getBuffer(Buffer.CACHE2, 0, 25),
                        Buffer.CACHE1, 0, ScriptAssembler.base58Charset,
                        ScriptAssembler.zeroInherit)
                + ScriptAssembler.showAddress(ScriptData.getDataBufferAll(Buffer.CACHE1))
                + ScriptAssembler.showAmount(argFrozenBalance, 6) + ScriptAssembler.showPressButton();
    }

    public static String TRXFreezeScriptNoReceiverSignature = "000030440220555A9F776A0FE0147C800E677539962B59247496BCAF834371502C6B85EFCF340220545E0D0C7E2EA3294F6EB7E24644F9FFC9BDBFB5BA1A9CDEEF533B8BB190C94F";

    public static String getTRXUnfreezeScript() {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptData argBlockBytes = sac.getArgument(2);
        ScriptData argBlockHash = sac.getArgument(8);
        ScriptData argExpiration = sac.getArgumentRightJustified(10);
        ScriptData argOwnerAddr = sac.getArgument(21);
        ScriptData argResource = sac.getArgument(1);
        ScriptData argReceiverAddr = sac.getArgument(21);
        ScriptData argTimestamp = sac.getArgumentRightJustified(10);
        // version=03 ScriptAssembler.hash=02=ScriptAssembler.SHA256 sign=01=ECDSA
        return "03030201"
                // set coinType to C3
                + ScriptAssembler.setCoinType(0xC3)
                // ref_block_bytes
                + ScriptAssembler.copyString("0a") + ScriptAssembler.protobuf(argBlockBytes, typeString)
                // ref_block_hash
                + ScriptAssembler.copyString("22") + ScriptAssembler.protobuf(argBlockHash, typeString)
                // expiration
                + ScriptAssembler.copyString("40") + ScriptAssembler.protobuf(argExpiration, typeInt)
                // contracts array
                + ScriptAssembler.copyString("5a") + ScriptAssembler.arrayPointer()
                // contract type
                + ScriptAssembler.copyString("080c")
                // parameter object
                + ScriptAssembler.copyString("12") + ScriptAssembler.arrayPointer()
                // type url
                + ScriptAssembler.copyString("0a34") + ScriptAssembler.copyString(HexUtil.toHexString(
                "type.googleapis.com/protocol.UnfreezeBalanceContract".getBytes()))
                // value object
                + ScriptAssembler.copyString("12") + ScriptAssembler.arrayPointer()
                // ownerAddr
                + ScriptAssembler.copyString("0a") + ScriptAssembler.protobuf(argOwnerAddr, typeString)
                // resource
                + ScriptAssembler.ifEqual(argResource, "01", ScriptAssembler.copyString("5001"), "")
                + ScriptAssembler.ifEqual(argResource, "02", ScriptAssembler.copyString("5002"), "")
                // receiverAddr
                + ScriptAssembler.copyString("7a") + ScriptAssembler.protobuf(argReceiverAddr, typeString)
                + ScriptAssembler.arrayEnd() + ScriptAssembler.arrayEnd() + ScriptAssembler.arrayEnd()
                // timestamp
                + ScriptAssembler.copyString("70") + ScriptAssembler.protobuf(argTimestamp, typeInt)
                + ScriptAssembler.showMessage("TRX") + ScriptAssembler.showMessage("Unfrz")
                + ScriptAssembler.copyArgument(argReceiverAddr, Buffer.CACHE2)
                + ScriptAssembler.hash(ScriptData.getDataBufferAll(Buffer.CACHE2), Buffer.CACHE2,
                        ScriptAssembler.DoubleSHA256)
                + ScriptAssembler.baseConvert(ScriptData.getBuffer(Buffer.CACHE2, 0, 25),
                        Buffer.CACHE1, 0, ScriptAssembler.base58Charset,
                        ScriptAssembler.zeroInherit)
                + ScriptAssembler.showAddress(ScriptData.getDataBufferAll(Buffer.CACHE1))
                + ScriptAssembler.showPressButton();
    }

    public static String TRXUnfreezeScriptSignature = "003045022100BE36FCF8E1FDE17689E2DBB8677A562788DB606FF537CDC5010C87BF40E3E22802207D16265C1F51011DBE73EEBA4F6959F0F7FCD56C9B1C1562F2BAFEF53496735D";

    public static String getTRXUnfreezeScriptNoReceiver() {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptData argBlockBytes = sac.getArgument(2);
        ScriptData argBlockHash = sac.getArgument(8);
        ScriptData argExpiration = sac.getArgumentRightJustified(10);
        ScriptData argOwnerAddr = sac.getArgument(21);
        ScriptData argResource = sac.getArgument(1);
        ScriptData argTimestamp = sac.getArgumentRightJustified(10);
        // version=03 ScriptAssembler.hash=02=ScriptAssembler.SHA256 sign=01=ECDSA
        return "03030201"
                // set coinType to C3
                + ScriptAssembler.setCoinType(0xC3)
                // ref_block_bytes
                + ScriptAssembler.copyString("0a") + ScriptAssembler.protobuf(argBlockBytes, typeString)
                // ref_block_hash
                + ScriptAssembler.copyString("22") + ScriptAssembler.protobuf(argBlockHash, typeString)
                // expiration
                + ScriptAssembler.copyString("40") + ScriptAssembler.protobuf(argExpiration, typeInt)
                // contracts array
                + ScriptAssembler.copyString("5a") + ScriptAssembler.arrayPointer()
                // contract type
                + ScriptAssembler.copyString("080c")
                // parameter object
                + ScriptAssembler.copyString("12") + ScriptAssembler.arrayPointer()
                // type url
                + ScriptAssembler.copyString("0a34") + ScriptAssembler.copyString(HexUtil.toHexString(
                "type.googleapis.com/protocol.UnfreezeBalanceContract".getBytes()))
                // value object
                + ScriptAssembler.copyString("12") + ScriptAssembler.arrayPointer()
                // ownerAddr
                + ScriptAssembler.copyString("0a") + ScriptAssembler.protobuf(argOwnerAddr, typeString)
                // resource
                + ScriptAssembler.ifEqual(argResource, "01", ScriptAssembler.copyString("5001"), "")
                + ScriptAssembler.ifEqual(argResource, "02", ScriptAssembler.copyString("5002"), "")
                + ScriptAssembler.arrayEnd() + ScriptAssembler.arrayEnd() + ScriptAssembler.arrayEnd()
                // timestamp
                + ScriptAssembler.copyString("70") + ScriptAssembler.protobuf(argTimestamp, typeInt)
                + ScriptAssembler.showMessage("TRX") + ScriptAssembler.showMessage("Unfrz")
                + ScriptAssembler.copyArgument(argOwnerAddr, Buffer.CACHE2)
                + ScriptAssembler.hash(ScriptData.getDataBufferAll(Buffer.CACHE2), Buffer.CACHE2,
                        ScriptAssembler.DoubleSHA256)
                + ScriptAssembler.baseConvert(ScriptData.getBuffer(Buffer.CACHE2, 0, 25),
                        Buffer.CACHE1, 0, ScriptAssembler.base58Charset,
                        ScriptAssembler.zeroInherit)
                + ScriptAssembler.showAddress(ScriptData.getDataBufferAll(Buffer.CACHE1))
                + ScriptAssembler.showPressButton();
    }
    
    public static String TRXUnfreezeScriptNoReceiverSignature = "3046022100ACEEAFD1797FBF203209DBF988BCEA19C0991E22FAA177615C64C30CB68046E10221008A2B58546CDDF91C0C865E82339C607FE22C4099F4EACA31CCD40DC85A69E357";

    public static String getTRXVoteWitnessScript() {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptData argBlockBytes = sac.getArgument(2);
        ScriptData argBlockHash = sac.getArgument(8);
        ScriptData argExpiration = sac.getArgumentRightJustified(10);
        ScriptData argOwnerAddr = sac.getArgument(21);
        ScriptData argVoteAddr = sac.getArgument(21);
        ScriptData argVoteCount = sac.getArgumentRightJustified(10);
        ScriptData argTimestamp = sac.getArgumentRightJustified(10);
        // version=03 ScriptAssembler.hash=02=ScriptAssembler.SHA256 sign=01=ECDSA
        return "03030201"
                // set coinType to C3
                + ScriptAssembler.setCoinType(0xC3)
                // ref_block_bytes
                + ScriptAssembler.copyString("0a") + ScriptAssembler.protobuf(argBlockBytes, typeString)
                // ref_block_hash
                + ScriptAssembler.copyString("22") + ScriptAssembler.protobuf(argBlockHash, typeString)
                // expiration
                + ScriptAssembler.copyString("40") + ScriptAssembler.protobuf(argExpiration, typeInt)
                // contracts array
                + ScriptAssembler.copyString("5a") + ScriptAssembler.arrayPointer()
                // contract type
                + ScriptAssembler.copyString("0804")
                // parameter object
                + ScriptAssembler.copyString("12") + ScriptAssembler.arrayPointer()
                // type url
                + ScriptAssembler.copyString("0a30") + ScriptAssembler.copyString(HexUtil.toHexString("type.googleapis.com/protocol.VoteWitnessContract".getBytes()))
                // value object
                + ScriptAssembler.copyString("12") + ScriptAssembler.arrayPointer()
                // ownerAddr
                + ScriptAssembler.copyString("0a") + ScriptAssembler.protobuf(argOwnerAddr, typeString)
                // votes array
                + ScriptAssembler.copyString("12") + ScriptAssembler.arrayPointer()
                // vote address
                + ScriptAssembler.copyString("0a") + ScriptAssembler.protobuf(argVoteAddr, typeString)
                // vote count
                + ScriptAssembler.copyString("10") + ScriptAssembler.protobuf(argVoteCount, typeInt)
                + ScriptAssembler.arrayEnd() + ScriptAssembler.arrayEnd() + ScriptAssembler.arrayEnd()
                + ScriptAssembler.arrayEnd()
                // timestamp
                + ScriptAssembler.copyString("70") + ScriptAssembler.protobuf(argTimestamp, typeInt)
                + ScriptAssembler.showMessage("TRX") + ScriptAssembler.showMessage("Vote")
                + ScriptAssembler.copyArgument(argVoteAddr, Buffer.CACHE2)
                + ScriptAssembler.hash(ScriptData.getDataBufferAll(Buffer.CACHE2), Buffer.CACHE2,
                        ScriptAssembler.DoubleSHA256)
                + ScriptAssembler.baseConvert(ScriptData.getBuffer(Buffer.CACHE2, 0, 25),
                        Buffer.CACHE1, 0, ScriptAssembler.base58Charset,
                        ScriptAssembler.zeroInherit)
                + ScriptAssembler.showAddress(ScriptData.getDataBufferAll(Buffer.CACHE1))
                + ScriptAssembler.showAmount(argVoteCount, 0) + ScriptAssembler.showPressButton();
    }
    
    public static String TRXVoteWitnessScriptSignature = "00304502201944C100D05CD8BC50D530778B78AF53EB5E7551AADCF0A21946B01CE266ED17022100983CC86BDCE4A4A8DD403B3BB53861879E970A8344B57F9862C88C783CDEEC85";

    public static String getTRXWithdrawScript() {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptData argBlockBytes = sac.getArgument(2);
        ScriptData argBlockHash = sac.getArgument(8);
        ScriptData argExpiration = sac.getArgumentRightJustified(10);
        ScriptData argOwnerAddr = sac.getArgument(21);
        ScriptData argTimestamp = sac.getArgumentRightJustified(10);
        // version=03 ScriptAssembler.hash=02=ScriptAssembler.SHA256 sign=01=ECDSA
        return "03030201"
                // set coinType to C3
                + ScriptAssembler.setCoinType(0xC3)
                // ref_block_bytes
                + ScriptAssembler.copyString("0a") + ScriptAssembler.protobuf(argBlockBytes, typeString)
                // ref_block_hash
                + ScriptAssembler.copyString("22") + ScriptAssembler.protobuf(argBlockHash, typeString)
                // expiration
                + ScriptAssembler.copyString("40") + ScriptAssembler.protobuf(argExpiration, typeInt)
                // contracts array
                + ScriptAssembler.copyString("5a") + ScriptAssembler.arrayPointer()
                // contract type
                + ScriptAssembler.copyString("080d")
                // parameter object
                + ScriptAssembler.copyString("12") + ScriptAssembler.arrayPointer()
                // type url
                + ScriptAssembler.copyString("0a34") + ScriptAssembler.copyString(HexUtil.toHexString(
                "type.googleapis.com/protocol.WithdrawBalanceContract".getBytes()))
                // value object
                + ScriptAssembler.copyString("12") + ScriptAssembler.arrayPointer()
                // ownerAddr
                + ScriptAssembler.copyString("0a") + ScriptAssembler.protobuf(argOwnerAddr, typeString)
                + ScriptAssembler.arrayEnd() + ScriptAssembler.arrayEnd() + ScriptAssembler.arrayEnd()
                // timestamp
                + ScriptAssembler.copyString("70") + ScriptAssembler.protobuf(argTimestamp, typeInt)
                + ScriptAssembler.showMessage("TRX") + ScriptAssembler.showMessage("Reward")
                + ScriptAssembler.copyArgument(argOwnerAddr, Buffer.CACHE2)
                + ScriptAssembler.hash(ScriptData.getDataBufferAll(Buffer.CACHE2), Buffer.CACHE2,
                        ScriptAssembler.DoubleSHA256)
                + ScriptAssembler.baseConvert(ScriptData.getBuffer(Buffer.CACHE2, 0, 25),
                        Buffer.CACHE1, 0, ScriptAssembler.base58Charset,
                        ScriptAssembler.zeroInherit)
                + ScriptAssembler.showAddress(ScriptData.getDataBufferAll(Buffer.CACHE1))
                + ScriptAssembler.showPressButton();
    }
    
    public static String TRXWithdrawScriptSignature = "3046022100AABEE6C90E63CF3752F426A93E1758A8E77CCFF39569F4778DAD28BD03297F48022100AF717A307446DE373740DEA06781564AFE4D5060462DA386DC2F167BA280B585";

}
