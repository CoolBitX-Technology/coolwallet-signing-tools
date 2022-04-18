/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.coolbitx.wallet.signing.scriptlib;

import com.coolbitx.wallet.signing.utils.ScriptArgumentComposer;
import com.coolbitx.wallet.signing.utils.ScriptAssembler;
import com.coolbitx.wallet.signing.utils.ScriptData;
import com.coolbitx.wallet.signing.utils.ScriptAssembler.HashType;
import com.coolbitx.wallet.signing.utils.ScriptAssembler.SignType;
import com.coolbitx.wallet.signing.utils.ScriptData.Buffer;
import com.google.common.base.Strings;

public class NearScript {

    public static void listAll() {
        System.out.println("Near: \n" + getNearScript() + "\n");
    }

    public static String getNearScript() {

        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptData signerLength = sac.getArgument(1);
        ScriptData signer = sac.getArgumentVariableLength(68);
        ScriptData publicKey = sac.getArgument(32);
        ScriptData nonce = sac.getArgument(8);
        ScriptData receiverLength = sac.getArgument(1);
        ScriptData receiver = sac.getArgumentVariableLength(68);
        ScriptData blockHash = sac.getArgument(32);
        ScriptData deposit = sac.getArgument(16);
        ScriptData amount = sac.getArgument(10);

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
            .copyString("0100000003")
            .baseConvert(deposit, Buffer.TRANSACTION, 16, ScriptAssembler.binaryCharset, ScriptAssembler.inLittleEndian)
            .showMessage("NEAR")
            .showAmount(amount, 10)
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
        ScriptData amount = sac.getArgument(10);

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
            .copyString("0100000004")
            .baseConvert(deposit, Buffer.TRANSACTION, 16, ScriptAssembler.binaryCharset, ScriptAssembler.inLittleEndian)
            .copyString("00")
            .copyArgument(validatorPublicKey)
            .showMessage("NEAR")
            .showMessage("SMART")
            .showAmount(amount, 10)
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
        ScriptData amount = sac.getArgument(10);

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
            .showMessage("STAKE")
            .ifEqual(amount, Strings.padStart("", 20, '0'), "", new ScriptAssembler().showAmount(amount, 10).getScript())
            .showPressButton()
            .setHeader(HashType.SHA256, SignType.EDDSA)
            .getScript();

        return script;
    }
}
