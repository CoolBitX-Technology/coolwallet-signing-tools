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
import com.google.common.base.Strings;

public class TrxScript {

    public static void main(String[] args) {
        listAll();
    }

    public static void listAll() {
        System.out.println("Trx: \n" + getTRXScript() + "\n");
        System.out.println("Trx trc20: \n" + getTRC20Script() + "\n");
        System.out.println("Trx trc20Blind: \n" + getTRC20BlindScript() + "\n");
        System.out.println("Trx Freeze: \n" + getTRXFreezeScript() + "\n");
        System.out.println("Trx Freeze Without Receiver: \n" + getTRXFreezeScriptNoReceiver() + "\n");
        System.out.println("Trx Unfreeze: \n" + getTRXUnfreezeScript() + "\n");
        System.out.println("Trx Unfreeze Without Receiver: \n" + getTRXUnfreezeScriptNoReceiver() + "\n");
        System.out.println("Trx Vote Witness: \n" + getTRXVoteWitnessScript() + "\n");
        System.out.println("Trx Withdraw: \n" + getTRXWithdrawScript() + "\n");
        System.out.println("Trx Freeze V2: \n" + getTRXFreezeV2Script() + "\n");
        System.out.println("Trx Unfreeze V2: \n" + getTRXUnfreezeV2Script() + "\n");
        System.out.println("Trx Cancel All Unfreeze V2: \n" + getTRXCancelAllUnfreezeV2Script() + "\n");
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

        String script = new ScriptAssembler()
                // set coinType to C3
                .setCoinType(0xC3)
                // ref_block_bytes
                .copyString("0a").protobuf(argBlockBytes, typeString)
                // ref_block_hash
                .copyString("22").protobuf(argBlockHash, typeString)
                // expiration
                .copyString("40").protobuf(argExpiration, typeInt)
                // contract object
                .copyString("5a")
                .arrayPointer()
                // contract type
                .copyString("0801")
                // parameter object
                .copyString("12").arrayPointer()
                // type url
                .copyString("0a2d")
                .copyString(
                        HexUtil.toHexString("type.googleapis.com/protocol.TransferContract".getBytes()))
                // value object
                .copyString("12").arrayPointer()
                // owner address
                .copyString("0a").protobuf(argOwnerAddr, typeString)
                // to address
                .copyString("12").protobuf(argToAddr, typeString)
                // amount
                .copyString("18").protobuf(argAmount, typeInt)
                .arrayEnd().arrayEnd().arrayEnd()
                // timestamp
                .copyString("70").protobuf(argTimestamp, typeInt).showMessage("TRX")
                .copyArgument(argToAddr, Buffer.CACHE2)
                .hash(ScriptData.getDataBufferAll(Buffer.CACHE2), Buffer.CACHE2,
                        HashType.DoubleSHA256)
                .baseConvert(ScriptData.getBuffer(Buffer.CACHE2, 0, 25),
                        Buffer.CACHE1, 0, ScriptAssembler.base58Charset,
                        ScriptAssembler.zeroInherit)
                .showAddress(ScriptData.getDataBufferAll(Buffer.CACHE1))
                .showAmount(argAmount, 6)
                .showWrap("PRESS", "BUTToN")
                .setHeader(HashType.SHA256, SignType.ECDSA)
                .getScript();
        return script;
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

        String script = new ScriptAssembler()
                // set coinType to C3
                .setCoinType(0xC3)
                // ref_block_bytes
                .copyString("0a").protobuf(argBlockBytes, typeString)
                // ref_block_hash
                .copyString("22").protobuf(argBlockHash, typeString)
                // expiration
                .copyString("40").protobuf(argExpiration, typeInt)
                // contract object
                .copyString("5a").arrayPointer()
                // contract type
                .copyString("081f")
                // parameter object
                .copyString("12").arrayPointer()
                // type url
                .copyString("0a31")
                .copyString(HexUtil.toHexString(
                        "type.googleapis.com/protocol.TriggerSmartContract".getBytes()))
                // value object
                .copyString("12").arrayPointer()
                // owner address
                .copyString("0a")
                .copyString("41", Buffer.CACHE2)
                .copyArgument(argOwnerAddr, Buffer.CACHE2)
                .protobuf(ScriptData.getDataBufferAll(Buffer.CACHE2), typeString)
                // contract address
                .copyString("12").clearBuffer(Buffer.CACHE2).copyString("41", Buffer.CACHE2)
                .copyArgument(argContractAddr, Buffer.CACHE2)
                .protobuf(ScriptData.getDataBufferAll(Buffer.CACHE2), typeString)
                // data
                .copyString("22")
                .clearBuffer(Buffer.CACHE2)
                .copyString("a9059cbb", Buffer.CACHE2)
                .copyString("000000000000000000000000", Buffer.CACHE2)
                .copyArgument(argTo, Buffer.CACHE2)
                .copyString("0000000000000000000000000000000000000000", Buffer.CACHE2)
                .copyArgument(argValue, Buffer.CACHE2)
                .protobuf(ScriptData.getDataBufferAll(Buffer.CACHE2), typeString)
                .arrayEnd().arrayEnd().arrayEnd()
                // timestamp
                .copyString("70").protobuf(argTimestamp, typeInt)
                // fee limit
                .copyString("9001").protobuf(argFeeLimit, typeInt)
                // display chain
                .showMessage("TRX")
                // display token
                .clearBuffer(Buffer.CACHE2)
                .ifSigned(argTokenInfo, argSign, "",
                        new ScriptAssembler().copyString(HexUtil.toHexString("@"), Buffer.CACHE2).getScript())
                .setBufferInt(argNameLength, 1, 7)
                .copyArgument(argName, Buffer.CACHE2)
                .showMessage(ScriptData.getDataBufferAll(Buffer.CACHE2))
                // display to address
                .clearBuffer(Buffer.CACHE2)
                .copyString("41", Buffer.CACHE2)
                .copyArgument(argTo, Buffer.CACHE2)
                .hash(ScriptData.getDataBufferAll(Buffer.CACHE2), Buffer.CACHE2,
                        HashType.DoubleSHA256)
                .baseConvert(ScriptData.getBuffer(Buffer.CACHE2, 0, 25),
                        Buffer.CACHE1, 0, ScriptAssembler.base58Charset,
                        ScriptAssembler.zeroInherit)
                .showAddress(ScriptData.getDataBufferAll(Buffer.CACHE1))
                // display amount
                .setBufferInt(argDecimal, 0, 20)
                .showAmount(argValue, ScriptData.bufInt)
                .showPressButton()
                // version=03 ScriptAssembler.hash=02=ScriptAssembler.SHA256 sign=01=ECDSA
                .setHeader(HashType.SHA256, SignType.ECDSA)
                .getScript();
        return script;
    }

    public static String TRC20ScriptSignature = "0000304402202D928B902A6D63BFD40FCDB4A5BC24977049081248F256D779D869D5A1925688022015E6630A34B4161E342EE855F698F7272AC83B781F273E726DC77DA40C75F86C";
    
    public static String getTRC20BlindScript() {
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

        String script = new ScriptAssembler()
                // set coinType to C3
                .setCoinType(0xC3)
                // ref_block_bytes
                .copyString("0a").protobuf(argBlockBytes, typeString)
                // ref_block_hash
                .copyString("22").protobuf(argBlockHash, typeString)
                // expiration
                .copyString("40").protobuf(argExpiration, typeInt)
                // contract object
                .copyString("5a").arrayPointer()
                // contract type
                .copyString("081f")
                // parameter object
                .copyString("12").arrayPointer()
                // type url
                .copyString("0a31")
                .copyString(HexUtil.toHexString(
                        "type.googleapis.com/protocol.TriggerSmartContract".getBytes()))
                // value object
                .copyString("12").arrayPointer()
                // owner address
                .copyString("0a")
                .copyString("41", Buffer.CACHE2)
                .copyArgument(argOwnerAddr, Buffer.CACHE2)
                .protobuf(ScriptData.getDataBufferAll(Buffer.CACHE2), typeString)
                // contract address
                .copyString("12").clearBuffer(Buffer.CACHE2).copyString("41", Buffer.CACHE2)
                .copyArgument(argContractAddr, Buffer.CACHE2)
                .protobuf(ScriptData.getDataBufferAll(Buffer.CACHE2), typeString)
                // data
                .copyString("22")
                .clearBuffer(Buffer.CACHE2)
                .copyString("a9059cbb", Buffer.CACHE2)
                .copyString("000000000000000000000000", Buffer.CACHE2)
                .copyArgument(argTo, Buffer.CACHE2)
                .copyString("0000000000000000000000000000000000000000", Buffer.CACHE2)
                .copyArgument(argValue, Buffer.CACHE2)
                .protobuf(ScriptData.getDataBufferAll(Buffer.CACHE2), typeString)
                .arrayEnd().arrayEnd().arrayEnd()
                // timestamp
                .copyString("70").protobuf(argTimestamp, typeInt)
                // fee limit
                .copyString("9001").protobuf(argFeeLimit, typeInt)
                // display chain
                .showMessage("TRX")
                // display smart
                .showWrap("SMART", "")
                .showPressButton()
                // version=03 ScriptAssembler.hash=02=ScriptAssembler.SHA256 sign=01=ECDSA
                .setHeader(HashType.SHA256, SignType.ECDSA)
                .getScript();
        return script;
    }

    public static String TRC20BlindScriptSignature = Strings.padStart(
        "3045022100ca438fe128c179c664913428fc59d5f889b7b9c206debfd62459b9a7a969cd35022019e126e352737808d01fc16d4cadcac4ed12f1d4dce32527c3a6819e4326235e",
        144,
        '0');

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

        String script = new ScriptAssembler()
                // set coinType to C3
                .setCoinType(0xC3)
                // ref_block_bytes
                .copyString("0a").protobuf(argBlockBytes, typeString)
                // ref_block_hash
                .copyString("22").protobuf(argBlockHash, typeString)
                // expiration
                .copyString("40").protobuf(argExpiration, typeInt)
                // contracts array
                .copyString("5a").arrayPointer()
                // contract type
                .copyString("080b")
                // parameter object
                .copyString("12").arrayPointer()
                // type url
                .copyString("0a32")
                .copyString(HexUtil.toHexString(
                        "type.googleapis.com/protocol.FreezeBalanceContract".getBytes()))
                // value object
                .copyString("12").arrayPointer()
                // ownerAddr
                .copyString("0a").protobuf(argOwnerAddr, typeString)
                // frozenBalance
                .copyString("10").protobuf(argFrozenBalance, typeInt)
                // frozenDuration
                .copyString("18").protobuf(argFrozenDuration, typeInt)
                // resource
                .ifEqual(argResource, "01", new ScriptAssembler().copyString("5001").getScript(), "")
                .ifEqual(argResource, "02", new ScriptAssembler().copyString("5002").getScript(), "")
                // receiverAddr
                .copyString("7a")
                .protobuf(argReceiverAddr, typeString)
                .arrayEnd().arrayEnd().arrayEnd()
                // timestamp
                .copyString("70")
                .protobuf(argTimestamp, typeInt)
                .showMessage("TRX")
                .showMessage("Freeze")
                .copyArgument(argReceiverAddr, Buffer.CACHE2)
                .hash(ScriptData.getDataBufferAll(Buffer.CACHE2), Buffer.CACHE2,
                        HashType.DoubleSHA256)
                .baseConvert(ScriptData.getBuffer(Buffer.CACHE2, 0, 25),
                        Buffer.CACHE1, 0, ScriptAssembler.base58Charset,
                        ScriptAssembler.zeroInherit)
                .showAddress(ScriptData.getDataBufferAll(Buffer.CACHE1))
                .showAmount(argFrozenBalance, 6)
                .showPressButton()
                // version=03 ScriptAssembler.hash=02=ScriptAssembler.SHA256 sign=01=ECDSA
                .setHeader(HashType.SHA256, SignType.ECDSA)
                .getScript();
        return script;
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

        String script = new ScriptAssembler()
                // set coinType to C3
                .setCoinType(0xC3)
                // ref_block_bytes
                .copyString("0a")
                .protobuf(argBlockBytes, typeString)
                // ref_block_hash
                .copyString("22")
                .protobuf(argBlockHash, typeString)
                // expiration
                .copyString("40")
                .protobuf(argExpiration, typeInt)
                // contracts array
                .copyString("5a")
                .arrayPointer()
                // contract type
                .copyString("080b")
                // parameter object
                .copyString("12")
                .arrayPointer()
                // type url
                .copyString("0a32")
                .copyString(HexUtil.toHexString(
                        "type.googleapis.com/protocol.FreezeBalanceContract".getBytes()))
                // value object
                .copyString("12")
                .arrayPointer()
                // ownerAddr
                .copyString("0a")
                .protobuf(argOwnerAddr, typeString)
                // frozenBalance
                .copyString("10")
                .protobuf(argFrozenBalance, typeInt)
                // frozenDuration
                .copyString("18")
                .protobuf(argFrozenDuration, typeInt)
                // resource
                .ifEqual(argResource, "01", new ScriptAssembler().copyString("5001").getScript(), "")
                .ifEqual(argResource, "02", new ScriptAssembler().copyString("5002").getScript(), "")
                .arrayEnd()
                .arrayEnd()
                .arrayEnd()
                // timestamp
                .copyString("70")
                .protobuf(argTimestamp, typeInt)
                .showMessage("TRX")
                .showMessage("Freeze")
                .copyArgument(argOwnerAddr, Buffer.CACHE2)
                .hash(ScriptData.getDataBufferAll(Buffer.CACHE2), Buffer.CACHE2,
                        HashType.DoubleSHA256)
                .baseConvert(ScriptData.getBuffer(Buffer.CACHE2, 0, 25),
                        Buffer.CACHE1, 0, ScriptAssembler.base58Charset,
                        ScriptAssembler.zeroInherit)
                .showAddress(ScriptData.getDataBufferAll(Buffer.CACHE1))
                .showAmount(argFrozenBalance, 6)
                .showPressButton()
                // version=03 ScriptAssembler.hash=02=ScriptAssembler.SHA256 sign=01=ECDSA
                .setHeader(HashType.SHA256, SignType.ECDSA)
                .getScript();
        return script;
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

        String script = new ScriptAssembler()
                // set coinType to C3
                .setCoinType(0xC3)
                // ref_block_bytes
                .copyString("0a")
                .protobuf(argBlockBytes, typeString)
                // ref_block_hash
                .copyString("22")
                .protobuf(argBlockHash, typeString)
                // expiration
                .copyString("40")
                .protobuf(argExpiration, typeInt)
                // contracts array
                .copyString("5a")
                .arrayPointer()
                // contract type
                .copyString("080c")
                // parameter object
                .copyString("12")
                .arrayPointer()
                // type url
                .copyString("0a34")
                .copyString(HexUtil.toHexString(
                        "type.googleapis.com/protocol.UnfreezeBalanceContract".getBytes()))
                // value object
                .copyString("12")
                .arrayPointer()
                // ownerAddr
                .copyString("0a")
                .protobuf(argOwnerAddr, typeString)
                // resource
                .ifEqual(argResource, "01", new ScriptAssembler().copyString("5001").getScript(), "")
                .ifEqual(argResource, "02", new ScriptAssembler().copyString("5002").getScript(), "")
                // receiverAddr
                .copyString("7a")
                .protobuf(argReceiverAddr, typeString)
                .arrayEnd()
                .arrayEnd()
                .arrayEnd()
                // timestamp
                .copyString("70")
                .protobuf(argTimestamp, typeInt)
                .showMessage("TRX")
                .showMessage("Unfrz")
                .copyArgument(argReceiverAddr, Buffer.CACHE2)
                .hash(ScriptData.getDataBufferAll(Buffer.CACHE2), Buffer.CACHE2,
                        HashType.DoubleSHA256)
                .baseConvert(ScriptData.getBuffer(Buffer.CACHE2, 0, 25),
                        Buffer.CACHE1, 0, ScriptAssembler.base58Charset,
                        ScriptAssembler.zeroInherit)
                .showAddress(ScriptData.getDataBufferAll(Buffer.CACHE1))
                .showPressButton()
                // version=03 ScriptAssembler.hash=02=ScriptAssembler.SHA256 sign=01=ECDSA
                .setHeader(HashType.SHA256, SignType.ECDSA)
                .getScript();
        return script;
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

        String script = new ScriptAssembler()
                // set coinType to C3
                .setCoinType(0xC3)
                // ref_block_bytes
                .copyString("0a")
                .protobuf(argBlockBytes, typeString)
                // ref_block_hash
                .copyString("22")
                .protobuf(argBlockHash, typeString)
                // expiration
                .copyString("40")
                .protobuf(argExpiration, typeInt)
                // contracts array
                .copyString("5a")
                .arrayPointer()
                // contract type
                .copyString("080c")
                // parameter object
                .copyString("12")
                .arrayPointer()
                // type url
                .copyString("0a34")
                .copyString(HexUtil.toHexString(
                        "type.googleapis.com/protocol.UnfreezeBalanceContract".getBytes()))
                // value object
                .copyString("12")
                .arrayPointer()
                // ownerAddr
                .copyString("0a")
                .protobuf(argOwnerAddr, typeString)
                // resource
                .ifEqual(argResource, "01", new ScriptAssembler().copyString("5001").getScript(), "")
                .ifEqual(argResource, "02", new ScriptAssembler().copyString("5002").getScript(), "")
                .arrayEnd()
                .arrayEnd()
                .arrayEnd()
                // timestamp
                .copyString("70")
                .protobuf(argTimestamp, typeInt)
                .showMessage("TRX")
                .showMessage("Unfrz")
                .copyArgument(argOwnerAddr, Buffer.CACHE2)
                .hash(ScriptData.getDataBufferAll(Buffer.CACHE2), Buffer.CACHE2,
                        HashType.DoubleSHA256)
                .baseConvert(ScriptData.getBuffer(Buffer.CACHE2, 0, 25),
                        Buffer.CACHE1, 0, ScriptAssembler.base58Charset,
                        ScriptAssembler.zeroInherit)
                .showAddress(ScriptData.getDataBufferAll(Buffer.CACHE1))
                .showPressButton()
                // version=03 ScriptAssembler.hash=02=ScriptAssembler.SHA256 sign=01=ECDSA
                .setHeader(HashType.SHA256, SignType.ECDSA)
                .getScript();
        return script;
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

        String script = new ScriptAssembler()
                // set coinType to C3
                .setCoinType(0xC3)
                // ref_block_bytes
                .copyString("0a")
                .protobuf(argBlockBytes, typeString)
                // ref_block_hash
                .copyString("22")
                .protobuf(argBlockHash, typeString)
                // expiration
                .copyString("40")
                .protobuf(argExpiration, typeInt)
                // contracts array
                .copyString("5a")
                .arrayPointer()
                // contract type
                .copyString("0804")
                // parameter object
                .copyString("12")
                .arrayPointer()
                // type url
                .copyString("0a30")
                .copyString(HexUtil.toHexString("type.googleapis.com/protocol.VoteWitnessContract".getBytes()))
                // value object
                .copyString("12")
                .arrayPointer()
                // ownerAddr
                .copyString("0a")
                .protobuf(argOwnerAddr, typeString)
                // votes array
                .copyString("12")
                .arrayPointer()
                // vote address
                .copyString("0a")
                .protobuf(argVoteAddr, typeString)
                // vote count
                .copyString("10")
                .protobuf(argVoteCount, typeInt)
                .arrayEnd()
                .arrayEnd()
                .arrayEnd()
                .arrayEnd()
                // timestamp
                .copyString("70")
                .protobuf(argTimestamp, typeInt)
                .showMessage("TRX")
                .showMessage("Vote")
                .copyArgument(argVoteAddr, Buffer.CACHE2)
                .hash(ScriptData.getDataBufferAll(Buffer.CACHE2), Buffer.CACHE2,
                        HashType.DoubleSHA256)
                .baseConvert(ScriptData.getBuffer(Buffer.CACHE2, 0, 25),
                        Buffer.CACHE1, 0, ScriptAssembler.base58Charset,
                        ScriptAssembler.zeroInherit)
                .showAddress(ScriptData.getDataBufferAll(Buffer.CACHE1))
                .showAmount(argVoteCount, 0)
                .showPressButton()
                // version=03 ScriptAssembler.hash=02=ScriptAssembler.SHA256 sign=01=ECDSA
                .setHeader(HashType.SHA256, SignType.ECDSA)
                .getScript();
        return script;
    }

    public static String TRXVoteWitnessScriptSignature = "00304502201944C100D05CD8BC50D530778B78AF53EB5E7551AADCF0A21946B01CE266ED17022100983CC86BDCE4A4A8DD403B3BB53861879E970A8344B57F9862C88C783CDEEC85";

    public static String getTRXWithdrawScript() {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptData argBlockBytes = sac.getArgument(2);
        ScriptData argBlockHash = sac.getArgument(8);
        ScriptData argExpiration = sac.getArgumentRightJustified(10);
        ScriptData argOwnerAddr = sac.getArgument(21);
        ScriptData argTimestamp = sac.getArgumentRightJustified(10);

        String script = new ScriptAssembler()
                // set coinType to C3
                .setCoinType(0xC3)
                // ref_block_bytes
                .copyString("0a")
                .protobuf(argBlockBytes, typeString)
                // ref_block_hash
                .copyString("22")
                .protobuf(argBlockHash, typeString)
                // expiration
                .copyString("40")
                .protobuf(argExpiration, typeInt)
                // contracts array
                .copyString("5a")
                .arrayPointer()
                // contract type
                .copyString("080d")
                // parameter object
                .copyString("12")
                .arrayPointer()
                // type url
                .copyString("0a34")
                .copyString(HexUtil.toHexString(
                        "type.googleapis.com/protocol.WithdrawBalanceContract".getBytes()))
                // value object
                .copyString("12")
                .arrayPointer()
                // ownerAddr
                .copyString("0a")
                .protobuf(argOwnerAddr, typeString)
                .arrayEnd()
                .arrayEnd()
                .arrayEnd()
                // timestamp
                .copyString("70")
                .protobuf(argTimestamp, typeInt)
                .showMessage("TRX")
                .showMessage("Reward")
                .copyArgument(argOwnerAddr, Buffer.CACHE2)
                .hash(ScriptData.getDataBufferAll(Buffer.CACHE2), Buffer.CACHE2,
                        HashType.DoubleSHA256)
                .baseConvert(ScriptData.getBuffer(Buffer.CACHE2, 0, 25),
                        Buffer.CACHE1, 0, ScriptAssembler.base58Charset,
                        ScriptAssembler.zeroInherit)
                .showAddress(ScriptData.getDataBufferAll(Buffer.CACHE1))
                .showPressButton()
                // version=03 ScriptAssembler.hash=02=ScriptAssembler.SHA256 sign=01=ECDSA
                .setHeader(HashType.SHA256, SignType.ECDSA)
                .getScript();
        return script;
    }

    public static String TRXWithdrawScriptSignature = "3046022100AABEE6C90E63CF3752F426A93E1758A8E77CCFF39569F4778DAD28BD03297F48022100AF717A307446DE373740DEA06781564AFE4D5060462DA386DC2F167BA280B585";

    public static String getTRXFreezeV2Script() {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptData argBlockBytes = sac.getArgument(2);
        ScriptData argBlockHash = sac.getArgument(8);
        ScriptData argExpiration = sac.getArgumentRightJustified(10);
        ScriptData argOwnerAddr = sac.getArgument(21);
        ScriptData argFrozenBalance = sac.getArgumentRightJustified(10);
        ScriptData argResource = sac.getArgument(1);
        ScriptData argTimestamp = sac.getArgumentRightJustified(10);

        String script = new ScriptAssembler()
                // set coinType to C3
                .setCoinType(0xC3)
                // ref_block_bytes
                .copyString("0a").protobuf(argBlockBytes, typeString)
                // ref_block_hash
                .copyString("22").protobuf(argBlockHash, typeString)
                // expiration
                .copyString("40").protobuf(argExpiration, typeInt)
                // contracts array
                .copyString("5a").arrayPointer()
                // contract type
                .copyString("0836")
                // parameter object
                .copyString("12").arrayPointer()
                // type url
                .copyString("0a34")
                .copyString(HexUtil.toHexString(
                        "type.googleapis.com/protocol.FreezeBalanceV2Contract".getBytes()))
                // value object
                .copyString("12").arrayPointer()
                // ownerAddr
                .copyString("0a").protobuf(argOwnerAddr, typeString)
                // frozenBalance
                .copyString("10").protobuf(argFrozenBalance, typeInt)
                // resource
                .ifEqual(argResource, "01", new ScriptAssembler().copyString("1801").getScript(), "")
                .arrayEnd().arrayEnd().arrayEnd()
                // timestamp
                .copyString("70")
                .protobuf(argTimestamp, typeInt)
                .showMessage("TRX")
                .showMessage("Freeze")
                .copyArgument(argOwnerAddr, Buffer.CACHE2)
                .hash(ScriptData.getDataBufferAll(Buffer.CACHE2), Buffer.CACHE2,
                        HashType.DoubleSHA256)
                .baseConvert(ScriptData.getBuffer(Buffer.CACHE2, 0, 25),
                        Buffer.CACHE1, 0, ScriptAssembler.base58Charset,
                        ScriptAssembler.zeroInherit)
                .showAddress(ScriptData.getDataBufferAll(Buffer.CACHE1))
                .showAmount(argFrozenBalance, 6)
                .showPressButton()
                // version=03 ScriptAssembler.hash=02=ScriptAssembler.SHA256 sign=01=ECDSA
                .setHeader(HashType.SHA256, SignType.ECDSA)
                .getScript();
        return script;
    }

    public static String TRXFreezeV2ScriptSignature = "003045022039527863aaf58fdc19ea3f5fbcaa728cc6e4efdabb4bcbb431928c5ad7581ec0022100cfba499029bf1ca8231fac2ac372c16ca05c58e5693c0110dc4f44859223a360";

    public static String getTRXUnfreezeV2Script() {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptData argBlockBytes = sac.getArgument(2);
        ScriptData argBlockHash = sac.getArgument(8);
        ScriptData argExpiration = sac.getArgumentRightJustified(10);
        ScriptData argOwnerAddr = sac.getArgument(21);
        ScriptData argUnfrozenBalance = sac.getArgumentRightJustified(10);
        ScriptData argResource = sac.getArgument(1);
        ScriptData argTimestamp = sac.getArgumentRightJustified(10);

        String script = new ScriptAssembler()
                // set coinType to C3
                .setCoinType(0xC3)
                // ref_block_bytes
                .copyString("0a")
                .protobuf(argBlockBytes, typeString)
                // ref_block_hash
                .copyString("22")
                .protobuf(argBlockHash, typeString)
                // expiration
                .copyString("40")
                .protobuf(argExpiration, typeInt)
                // contracts array
                .copyString("5a")
                .arrayPointer()
                // contract type
                .copyString("0837")
                // parameter object
                .copyString("12")
                .arrayPointer()
                // type url
                .copyString("0a36")
                .copyString(HexUtil.toHexString(
                        "type.googleapis.com/protocol.UnfreezeBalanceV2Contract".getBytes()))
                // value object
                .copyString("12")
                .arrayPointer()
                // ownerAddr
                .copyString("0a")
                .protobuf(argOwnerAddr, typeString)
                // unfrozenBalance
                .copyString("10").protobuf(argUnfrozenBalance, typeInt)
                // resource
                .ifEqual(argResource, "01", new ScriptAssembler().copyString("1801").getScript(), "")
                .arrayEnd()
                .arrayEnd()
                .arrayEnd()
                // timestamp
                .copyString("70")
                .protobuf(argTimestamp, typeInt)
                .showMessage("TRX")
                .showMessage("Unfrz")
                .copyArgument(argOwnerAddr, Buffer.CACHE2)
                .hash(ScriptData.getDataBufferAll(Buffer.CACHE2), Buffer.CACHE2,
                        HashType.DoubleSHA256)
                .baseConvert(ScriptData.getBuffer(Buffer.CACHE2, 0, 25),
                        Buffer.CACHE1, 0, ScriptAssembler.base58Charset,
                        ScriptAssembler.zeroInherit)
                .showAddress(ScriptData.getDataBufferAll(Buffer.CACHE1))
                .showAmount(argUnfrozenBalance, 6)
                .showPressButton()
                // version=03 ScriptAssembler.hash=02=ScriptAssembler.SHA256 sign=01=ECDSA
                .setHeader(HashType.SHA256, SignType.ECDSA)
                .getScript();
        return script;
    }

    public static String TRXUnfreezeV2ScriptSignature = "003045022100a278a75fb8c774579600afaebab63b34cec21e77fc57f44385b98358078d390d0220498ca95c4b827dbf4c8d50f2691b63b7c26f618374062622da0fa2101f48e864";

    public static String getTRXCancelAllUnfreezeV2Script() {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptData argBlockBytes = sac.getArgument(2);
        ScriptData argBlockHash = sac.getArgument(8);
        ScriptData argExpiration = sac.getArgumentRightJustified(10);
        ScriptData argOwnerAddr = sac.getArgument(21);
        ScriptData argTimestamp = sac.getArgumentRightJustified(10);

        String script = new ScriptAssembler()
                // set coinType to C3
                .setCoinType(0xC3)
                // ref_block_bytes
                .copyString("0a").protobuf(argBlockBytes, typeString)
                // ref_block_hash
                .copyString("22").protobuf(argBlockHash, typeString)
                // expiration
                .copyString("40").protobuf(argExpiration, typeInt)
                // contracts array
                .copyString("5a").arrayPointer()
                // contract type
                .copyString("083b")
                // parameter object
                .copyString("12").arrayPointer()
                // type url
                .copyString("0a38")
                .copyString(HexUtil.toHexString(
                        "type.googleapis.com/protocol.CancelAllUnfreezeV2Contract".getBytes()))
                // value object
                .copyString("12").arrayPointer()
                // ownerAddr
                .copyString("0a").protobuf(argOwnerAddr, typeString)
                .arrayEnd().arrayEnd().arrayEnd()
                // timestamp
                .copyString("70")
                .protobuf(argTimestamp, typeInt)
                .showMessage("TRX")
                .showWrap("Cancel", "Unfrz")
                .copyArgument(argOwnerAddr, Buffer.CACHE2)
                .hash(ScriptData.getDataBufferAll(Buffer.CACHE2), Buffer.CACHE2,
                        HashType.DoubleSHA256)
                .baseConvert(ScriptData.getBuffer(Buffer.CACHE2, 0, 25),
                        Buffer.CACHE1, 0, ScriptAssembler.base58Charset,
                        ScriptAssembler.zeroInherit)
                .showAddress(ScriptData.getDataBufferAll(Buffer.CACHE1))
                .showPressButton()
                // version=03 ScriptAssembler.hash=02=ScriptAssembler.SHA256 sign=01=ECDSA
                .setHeader(HashType.SHA256, SignType.ECDSA)
                .getScript();
        return script;
    }

    public static String TRXCancelAllUnfreezeV2ScriptSignature = Strings.padStart("30440220521963e48ce132d80a54af21918be2f123b7c33c74de34150597a10202f194c1022004d0652cce3931ff11a1b46ba3d4dc45a708b8c7761beef85546028b5480ecce", 144, '0');
    
    public static String getTRXWithdrawExpireUnfreezeScript() {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptData argBlockBytes = sac.getArgument(2);
        ScriptData argBlockHash = sac.getArgument(8);
        ScriptData argExpiration = sac.getArgumentRightJustified(10);
        ScriptData argOwnerAddr = sac.getArgument(21);
        ScriptData argTimestamp = sac.getArgumentRightJustified(10);

        String script = new ScriptAssembler()
                // set coinType to C3
                .setCoinType(0xC3)
                // ref_block_bytes
                .copyString("0a").protobuf(argBlockBytes, typeString)
                // ref_block_hash
                .copyString("22").protobuf(argBlockHash, typeString)
                // expiration
                .copyString("40").protobuf(argExpiration, typeInt)
                // contracts array
                .copyString("5a").arrayPointer()
                // contract type
                .copyString("0838")
                // parameter object
                .copyString("12").arrayPointer()
                // type url
                .copyString("0a3b")
                .copyString(HexUtil.toHexString(
                        "type.googleapis.com/protocol.WithdrawExpireUnfreezeContract".getBytes()))
                // value object
                .copyString("12").arrayPointer()
                // ownerAddr
                .copyString("0a").protobuf(argOwnerAddr, typeString)
                .arrayEnd().arrayEnd().arrayEnd()
                // timestamp
                .copyString("70")
                .protobuf(argTimestamp, typeInt)
                .showMessage("TRX")
                .showMessage("Withdraw")
                .copyArgument(argOwnerAddr, Buffer.CACHE2)
                .hash(ScriptData.getDataBufferAll(Buffer.CACHE2), Buffer.CACHE2,
                        HashType.DoubleSHA256)
                .baseConvert(ScriptData.getBuffer(Buffer.CACHE2, 0, 25),
                        Buffer.CACHE1, 0, ScriptAssembler.base58Charset,
                        ScriptAssembler.zeroInherit)
                .showAddress(ScriptData.getDataBufferAll(Buffer.CACHE1))
                .showPressButton()
                // version=03 ScriptAssembler.hash=02=ScriptAssembler.SHA256 sign=01=ECDSA
                .setHeader(HashType.SHA256, SignType.ECDSA)
                .getScript();
        return script;
    }

    public static String TRXWithdrawExpireUnfreezeScriptSignature = Strings.padStart("3046022100abfa3c6ac84c20fe91d630b7f83c822fa6e037e7bb6de2d17eba4e3997f9fa9a022100dc45c119c277363849e08c181d8f61a80795ddd30469d4fb3091ccd6af6a7fe5", 144, '0');
}
