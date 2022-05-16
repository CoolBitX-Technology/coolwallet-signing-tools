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

public class NearScript {

    public static void listAll() {
        System.out.println("Near Transfer: \n" + getNearTransferScript() + "\n");
        System.out.println("Near Stake: \n" + getNearStakeScript() + "\n");
        System.out.println("Near Smart: \n" + getNearSCScript() + "\n");
        System.out.println("Near SC Stake: \n" + getNearSCStakeScript() + "\n");
        System.out.println("Near SC Unstake All: \n" + getNearSCUnstakeAllScript() + "\n");
        System.out.println("Near SC Withdraw All: \n" + getNearSCWithdrawAllScript() + "\n");
        System.out.println("Near SC Unstake: \n" + getNearSCUnstakeScript() + "\n");
        System.out.println("Near SC Withdraw: \n" + getNearSCWithdrawScript() + "\n");
    }

    public static String getNearTransferScript() {

        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptData signerLength = sac.getArgument(1);
        ScriptData signer = sac.getArgumentVariableLength(68);
        ScriptData publicKey = sac.getArgument(32);
        ScriptData nonce = sac.getArgument(8);
        ScriptData receiverLength = sac.getArgument(1);
        ScriptData receiver = sac.getArgumentVariableLength(68);
        ScriptData blockHash = sac.getArgument(32);
        ScriptData deposit = sac.getArgument(16);

        String script = new ScriptAssembler()
            .setCoinType(0x018d)
            .copyArgument(signerLength)
            .copyString("000000")
            .setBufferInt(signerLength, 2, 64)
            .copyArgument(signer)
            .copyString("00")
            .copyArgument(publicKey)
            .baseConvert(nonce, Buffer.TRANSACTION, 8, ScriptAssembler.binaryCharset, ScriptAssembler.inLittleEndian)
            .copyArgument(receiverLength)
            .copyString("000000")
            .setBufferInt(receiverLength, 2, 64)
            .copyArgument(receiver)
            .copyArgument(blockHash)
            // action transfer
            .copyString("0100000003")
            .baseConvert(deposit, Buffer.TRANSACTION, 16, ScriptAssembler.binaryCharset, ScriptAssembler.inLittleEndian)
            .showMessage("NEAR")
            .showAddress(receiver)
            .showAmount(deposit, 24)
            .showPressButton()
            .setHeader(HashType.SHA256, SignType.EDDSA)
            .getScript();

        return script;
    }

    public static String getNearStakeScript() {

        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptData signerLength = sac.getArgument(1);
        ScriptData signer = sac.getArgumentVariableLength(68);
        ScriptData publicKey = sac.getArgument(32);
        ScriptData nonce = sac.getArgument(8);
        ScriptData receiverLength = sac.getArgument(1);
        ScriptData receiver = sac.getArgumentVariableLength(68);
        ScriptData blockHash = sac.getArgument(32);
        ScriptData deposit = sac.getArgument(16);
        ScriptData validatorPublicKey = sac.getArgument(32);

        String script = new ScriptAssembler()
            .setCoinType(0x018d)
            .copyArgument(signerLength)
            .copyString("000000")
            .setBufferInt(signerLength, 2, 64)
            .copyArgument(signer)
            .copyString("00")
            .copyArgument(publicKey)
            .baseConvert(nonce, Buffer.TRANSACTION, 8, ScriptAssembler.binaryCharset, ScriptAssembler.inLittleEndian)
            .copyArgument(receiverLength)
            .copyString("000000")
            .setBufferInt(receiverLength, 2, 64)
            .copyArgument(receiver)
            .copyArgument(blockHash)
            // action stake
            .copyString("0100000004")
            .baseConvert(deposit, Buffer.TRANSACTION, 16, ScriptAssembler.binaryCharset, ScriptAssembler.inLittleEndian)
            .copyString("00")
            .copyArgument(validatorPublicKey)
            .showMessage("NEAR")
            .showMessage("STAKE")
            .showAmount(deposit, 24)
            .showPressButton()
            .setHeader(HashType.SHA256, SignType.EDDSA)
            .getScript();

        return script;
    }

    public static String getNearSCScript() {
        
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptData signerLength = sac.getArgument(1);
        ScriptData signer = sac.getArgumentVariableLength(68);
        ScriptData publicKey = sac.getArgument(32);
        ScriptData nonce = sac.getArgument(8);
        ScriptData receiverLength = sac.getArgument(1);
        ScriptData receiver = sac.getArgumentVariableLength(68);
        ScriptData blockHash = sac.getArgument(32);
        ScriptData methodLength = sac.getArgument(1);
        ScriptData method = sac.getArgumentVariableLength(68);
        ScriptData methodArgsLength = sac.getArgument(1);
        ScriptData methodArgs = sac.getArgumentVariableLength(68);
        ScriptData gas = sac.getArgument(8);
        ScriptData deposit = sac.getArgument(16);

        String script = new ScriptAssembler()
            .setCoinType(0x018d)
            .copyArgument(signerLength)
            .copyString("000000")
            .setBufferInt(signerLength, 2, 64)
            .copyArgument(signer)
            .copyString("00")
            .copyArgument(publicKey)
            .baseConvert(nonce, Buffer.TRANSACTION, 8, ScriptAssembler.binaryCharset, ScriptAssembler.inLittleEndian)
            .copyArgument(receiverLength)
            .copyString("000000")
            .setBufferInt(receiverLength, 2, 64)
            .copyArgument(receiver)
            .copyArgument(blockHash)
            // action smart
            .copyString("0100000002")
            .copyArgument(methodLength)
            .copyString("000000")
            .setBufferInt(methodLength, 2, 64)
            .copyArgument(method)
            .copyArgument(methodArgsLength)
            .copyString("000000")
            .setBufferInt(methodArgsLength, 2, 64)
            .copyArgument(methodArgs)
            .baseConvert(gas, Buffer.TRANSACTION, 8, ScriptAssembler.binaryCharset, ScriptAssembler.inLittleEndian)
            .baseConvert(deposit, Buffer.TRANSACTION, 16, ScriptAssembler.binaryCharset, ScriptAssembler.inLittleEndian)
            .showMessage("NEAR")
            .showMessage("SMART")
            .showAmount(deposit, 24)
            .showPressButton()
            .setHeader(HashType.SHA256, SignType.EDDSA)
            .getScript();

        return script;
    }

    public static String getNearSCStakeScript() {
        
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptData signerLength = sac.getArgument(1);
        ScriptData signer = sac.getArgumentVariableLength(68);
        ScriptData publicKey = sac.getArgument(32);
        ScriptData nonce = sac.getArgument(8);
        ScriptData receiverLength = sac.getArgument(1);
        ScriptData receiver = sac.getArgumentVariableLength(68);
        ScriptData blockHash = sac.getArgument(32);
        ScriptData gas = sac.getArgument(8);
        ScriptData deposit = sac.getArgument(16);

        String script = new ScriptAssembler()
            .setCoinType(0x018d)
            .copyArgument(signerLength)
            .copyString("000000")
            .setBufferInt(signerLength, 2, 64)
            .copyArgument(signer)
            .copyString("00")
            .copyArgument(publicKey)
            .baseConvert(nonce, Buffer.TRANSACTION, 8, ScriptAssembler.binaryCharset, ScriptAssembler.inLittleEndian)
            .copyArgument(receiverLength)
            .copyString("000000")
            .setBufferInt(receiverLength, 2, 64)
            .copyArgument(receiver)
            .copyArgument(blockHash)
            // action smart
            .copyString("0100000002")
            .copyString("110000006465706f7369745f616e645f7374616b65")
            .copyString("020000007b7d")
            .baseConvert(gas, Buffer.TRANSACTION, 8, ScriptAssembler.binaryCharset, ScriptAssembler.inLittleEndian)
            .baseConvert(deposit, Buffer.TRANSACTION, 16, ScriptAssembler.binaryCharset, ScriptAssembler.inLittleEndian)
            .showMessage("NEAR")
            .showWrap("STAKE", "")
            .showAmount(deposit, 24)
            .showPressButton()
            .setHeader(HashType.SHA256, SignType.EDDSA)
            .getScript();

        return script;
    }

    public static String getNearSCUnstakeAllScript() {

        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptData signerLength = sac.getArgument(1);
        ScriptData signer = sac.getArgumentVariableLength(68);
        ScriptData publicKey = sac.getArgument(32);
        ScriptData nonce = sac.getArgument(8);
        ScriptData receiverLength = sac.getArgument(1);
        ScriptData receiver = sac.getArgumentVariableLength(68);
        ScriptData blockHash = sac.getArgument(32);
        ScriptData gas = sac.getArgument(8);

        String script = new ScriptAssembler()
            .setCoinType(0x018d)
            .copyArgument(signerLength)
            .copyString("000000")
            .setBufferInt(signerLength, 2, 64)
            .copyArgument(signer)
            .copyString("00")
            .copyArgument(publicKey)
            .baseConvert(nonce, Buffer.TRANSACTION, 8, ScriptAssembler.binaryCharset, ScriptAssembler.inLittleEndian)
            .copyArgument(receiverLength)
            .copyString("000000")
            .setBufferInt(receiverLength, 2, 64)
            .copyArgument(receiver)
            .copyArgument(blockHash)
            // action smart
            .copyString("0100000002")
            .copyString("0b000000")
            .copyString(HexUtil.toHexString("unstake_all"))
            .copyString("020000007b7d")
            .baseConvert(gas, Buffer.TRANSACTION, 8, ScriptAssembler.binaryCharset, ScriptAssembler.inLittleEndian)
            .copyString("00000000000000000000000000000000")
            .showMessage("NEAR")
            .showWrap("UNSTAKE", "ALL")
            .showPressButton()
            .setHeader(HashType.SHA256, SignType.EDDSA)
            .getScript();

        return script;
    }

    public static String getNearSCWithdrawAllScript() {

        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptData signerLength = sac.getArgument(1);
        ScriptData signer = sac.getArgumentVariableLength(68);
        ScriptData publicKey = sac.getArgument(32);
        ScriptData nonce = sac.getArgument(8);
        ScriptData receiverLength = sac.getArgument(1);
        ScriptData receiver = sac.getArgumentVariableLength(68);
        ScriptData blockHash = sac.getArgument(32);
        ScriptData gas = sac.getArgument(8);

        String script = new ScriptAssembler()
            .setCoinType(0x018d)
            .copyArgument(signerLength)
            .copyString("000000")
            .setBufferInt(signerLength, 2, 64)
            .copyArgument(signer)
            .copyString("00")
            .copyArgument(publicKey)
            .baseConvert(nonce, Buffer.TRANSACTION, 8, ScriptAssembler.binaryCharset, ScriptAssembler.inLittleEndian)
            .copyArgument(receiverLength)
            .copyString("000000")
            .setBufferInt(receiverLength, 2, 64)
            .copyArgument(receiver)
            .copyArgument(blockHash)
            // action smart
            .copyString("0100000002")
            .copyString("0c000000")
            .copyString(HexUtil.toHexString("withdraw_all"))
            .copyString("020000007b7d")
            .baseConvert(gas, Buffer.TRANSACTION, 8, ScriptAssembler.binaryCharset, ScriptAssembler.inLittleEndian)
            .copyString("00000000000000000000000000000000")
            .showMessage("NEAR")
            .showWrap("WITHDRAW", "ALL")
            .showPressButton()
            .setHeader(HashType.SHA256, SignType.EDDSA)
            .getScript();

        return script;
    }

    public static String getNearSCUnstakeScript() {

        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptData signerLength = sac.getArgument(1);
        ScriptData signer = sac.getArgumentVariableLength(68);
        ScriptData publicKey = sac.getArgument(32);
        ScriptData nonce = sac.getArgument(8);
        ScriptData receiverLength = sac.getArgument(1);
        ScriptData receiver = sac.getArgumentVariableLength(68);
        ScriptData blockHash = sac.getArgument(32);
        ScriptData methodArgsLength = sac.getArgument(1);
        ScriptData amount = sac.getArgument(16);
        ScriptData gas = sac.getArgument(8);

        String script = new ScriptAssembler()
            .setCoinType(0x018d)
            .copyArgument(signerLength)
            .copyString("000000")
            .setBufferInt(signerLength, 2, 64)
            .copyArgument(signer)
            .copyString("00")
            .copyArgument(publicKey)
            .baseConvert(nonce, Buffer.TRANSACTION, 8, ScriptAssembler.binaryCharset, ScriptAssembler.inLittleEndian)
            .copyArgument(receiverLength)
            .copyString("000000")
            .setBufferInt(receiverLength, 2, 64)
            .copyArgument(receiver)
            .copyArgument(blockHash)
            // action smart
            .copyString("0100000002")
            .copyString("07000000")
            .copyString(HexUtil.toHexString("unstake"))
            .copyArgument(methodArgsLength)
            .copyString("000000")
            .copyString(HexUtil.toHexString("{\"amount\":\""))
            .baseConvert(amount, Buffer.TRANSACTION, 0, ScriptAssembler.decimalCharset, ScriptAssembler.leftJustify)
            .copyString(HexUtil.toHexString("\"}"))
            .baseConvert(gas, Buffer.TRANSACTION, 8, ScriptAssembler.binaryCharset, ScriptAssembler.inLittleEndian)
            .copyString("00000000000000000000000000000000")
            .showMessage("NEAR")
            .showWrap("UNSTAKE", "")
            .showAmount(amount, 24)
            .showPressButton()
            .setHeader(HashType.SHA256, SignType.EDDSA)
            .getScript();

        return script;
    }

    public static String getNearSCWithdrawScript() {

        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptData signerLength = sac.getArgument(1);
        ScriptData signer = sac.getArgumentVariableLength(68);
        ScriptData publicKey = sac.getArgument(32);
        ScriptData nonce = sac.getArgument(8);
        ScriptData receiverLength = sac.getArgument(1);
        ScriptData receiver = sac.getArgumentVariableLength(68);
        ScriptData blockHash = sac.getArgument(32);
        ScriptData methodArgsLength = sac.getArgument(1);
        ScriptData amount = sac.getArgument(16);
        ScriptData gas = sac.getArgument(8);

        String script = new ScriptAssembler()
            .setCoinType(0x018d)
            .copyArgument(signerLength)
            .copyString("000000")
            .setBufferInt(signerLength, 2, 64)
            .copyArgument(signer)
            .copyString("00")
            .copyArgument(publicKey)
            .baseConvert(nonce, Buffer.TRANSACTION, 8, ScriptAssembler.binaryCharset, ScriptAssembler.inLittleEndian)
            .copyArgument(receiverLength)
            .copyString("000000")
            .setBufferInt(receiverLength, 2, 64)
            .copyArgument(receiver)
            .copyArgument(blockHash)
            // action smart
            .copyString("0100000002")
            .copyString("08000000")
            .copyString(HexUtil.toHexString("withdraw"))
            .copyArgument(methodArgsLength)
            .copyString("000000")
            .copyString(HexUtil.toHexString("{\"amount\":\""))
            .baseConvert(amount, Buffer.TRANSACTION, 0, ScriptAssembler.decimalCharset, ScriptAssembler.leftJustify)
            .copyString(HexUtil.toHexString("\"}"))
            .baseConvert(gas, Buffer.TRANSACTION, 8, ScriptAssembler.binaryCharset, ScriptAssembler.inLittleEndian)
            .copyString("00000000000000000000000000000000")
            .showMessage("NEAR")
            .showWrap("WITHDRAW", "")
            .showAmount(amount, 24)
            .showPressButton()
            .setHeader(HashType.SHA256, SignType.EDDSA)
            .getScript();

        return script;
    }

}
