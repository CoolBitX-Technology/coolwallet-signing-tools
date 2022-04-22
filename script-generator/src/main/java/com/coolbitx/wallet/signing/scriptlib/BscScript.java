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

public class BscScript {

    public static void main(String[] args) {
        BscScript.listAll();
    }

    public static void listAll() {
        System.out.println("Bsc: \n" + getTransferScript() + "\n");
        System.out.println("Signature: \n" + TransferScriptSignature + "\n");

        System.out.println("Bsc Bep20: \n" + getBEP20Script() + "\n");
        System.out.println("Signature: \n" + BEP20ScriptSignature + "\n");

        System.out.println("Bsc Smart Contract: \n" + getSmartContractScript() + "\n");
        System.out.println("Signature: \n" + SmartContractScriptSignature + "\n");

        System.out.println("Bsc Smart Contract Segment: \n" + getSmartContractSegmentScript() + "\n");
        System.out.println("Signature: \n" + SmartContractSegmentScriptSignature + "\n");

        System.out.println("Bsc Message: \n" + getMessageScript() + "\n");
        System.out.println("Signature: \n" + MessageScriptSignature + "\n");

        System.out.println("Bsc TypedData: \n" + getTypedDataScript() + "\n");
        System.out.println("Signature: \n" + TypedDataScriptSignature + "\n");
    }

    public static String getTransferScript() {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptData argTo = sac.getArgument(20);
        ScriptData argValue = sac.getArgumentRightJustified(10);
        ScriptData argGasPrice = sac.getArgumentRightJustified(10);
        ScriptData argGasLimit = sac.getArgumentRightJustified(10);
        ScriptData argNonce = sac.getArgumentRightJustified(8);
        // ScriptData argChainId = sac.getArgumentRightJustified(2);

        String script = new ScriptAssembler()
                // set coinType to 3C
                .setCoinType(0x3C)
                // temp byte for rlpList
                .copyString("C0")
                // nonce
                .rlpString(argNonce)
                // gasPrice
                .rlpString(argGasPrice)
                // gasLimit
                .rlpString(argGasLimit)
                // toAddress
                .copyString("94").copyArgument(argTo)
                // value
                .rlpString(argValue)
                // data
                .copyString("80")
                // chainId v = 56
                .copyString("38", Buffer.CACHE1)
                .rlpString(ScriptData.getDataBufferAll(Buffer.CACHE1))
                // r,s
                .copyString("8080")
                .rlpList(1)
                .showMessage("BSC")
                .showMessage("BNB")
                .copyString(HexUtil.toHexString("0x"), Buffer.CACHE2)
                .baseConvert(argTo, Buffer.CACHE2, 0, ScriptAssembler.hexadecimalCharset,
                        ScriptAssembler.zeroInherit)
                .showAddress(ScriptData.getDataBufferAll(Buffer.CACHE2))
                .showAmount(argValue, 18)
                .showPressButton()
                // version=00 ScriptAssembler.hash=06=ScriptAssembler.Keccak256 sign=01=ECDSA
                .setHeader(HashType.Keccak256, SignType.ECDSA)
                .getScript();
        return script;
    }

    public static String TransferScriptSignature = Strings.padStart("304502206A9D1E267D9AC65B28FFB49286F73041D3FF3834F68C5CAB1A700607C57DB052022100BB46FD3AB7A402AF163FA90EB0348A3AE14CF51E4CA4364931104CD1996F99E6", 144, '0');

    /*
     * f86a 1e 85 01718c7e00 83 030d40 94 86fa049857e0209aa7d9e616f7eb3b3b78ecfdb0
     * 80 b844 a9059cbb
     * 0000000000000000000000009e3a6e03298780078003d3965689b8f653ee87b1
     * 0000000000000000000000000000000000000000000000004563918244f40000 01 80 80
     */
    public static String getBEP20Script() {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptData argTo = sac.getArgument(20);
        ScriptData argValue = sac.getArgument(12);
        ScriptData argGasPrice = sac.getArgumentRightJustified(10);
        ScriptData argGasLimit = sac.getArgumentRightJustified(10);
        ScriptData argNonce = sac.getArgumentRightJustified(8);
        ScriptData argTokenInfo = sac.getArgumentUnion(0, 29);
        ScriptData argDecimal = sac.getArgument(1);
        ScriptData argNameLength = sac.getArgument(1);
        ScriptData argName = sac.getArgumentVariableLength(7);
        ScriptData argContractAddress = sac.getArgument(20);
        ScriptData argSign = sac.getArgument(72);

        String script = new ScriptAssembler()
                .setCoinType(0x3C)
                // set coinType to 3C
                .copyString("F800")
                .rlpString(argNonce)
                .rlpString(argGasPrice)
                .rlpString(argGasLimit)
                .copyString("94")
                .copyArgument(argContractAddress)
                .copyString("80B844a9059cbb000000000000000000000000")
                // value = 0 ,
                // dataLength = 68
                .copyArgument(argTo)
                .copyString("0000000000000000000000000000000000000000")
                .copyArgument(argValue)
                // chainId v = 56
                .copyString("38", Buffer.CACHE1)
                .rlpString(ScriptData.getDataBufferAll(Buffer.CACHE1))
                .copyString("8080")
                .rlpList(2)
                .showMessage("BSC")
                .ifSigned(argTokenInfo, argSign, "",
                        new ScriptAssembler().copyString(HexUtil.toHexString("@"), Buffer.CACHE2).getScript())
                .setBufferInt(argNameLength, 1, 7)
                .copyArgument(argName, Buffer.CACHE2)
                .showMessage(ScriptData.getDataBufferAll(Buffer.CACHE2))
                .clearBuffer(Buffer.CACHE2)
                .copyString(HexUtil.toHexString("0x"), Buffer.CACHE2)
                .baseConvert(argTo, Buffer.CACHE2, 0, ScriptAssembler.hexadecimalCharset,
                        ScriptAssembler.zeroInherit)
                .showAddress(ScriptData.getDataBufferAll(Buffer.CACHE2))
                .setBufferInt(argDecimal, 0, 20)
                .showAmount(argValue, ScriptData.bufInt)
                .showPressButton()
                // version=00 ScriptAssembler.hash=06=ScriptAssembler.Keccak256 sign=01=ECDSA
                .setHeader(HashType.Keccak256, SignType.ECDSA)
                .getScript();
        return script;
    }

    public static String BEP20ScriptSignature = Strings.padStart("304502202B33814A04EE43EFC342DD3345652DAF34606EAD6599EF1A3C45F78727CC7283022100E20E6E30C3D5B8E04B5DD5C90FBFE064077CA9FCBF2689DDD2020FF51A1D69EE", 144, '0');

    public static String getSmartContractScript() {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptData argTo = sac.getArgument(20);
        ScriptData argValue = sac.getArgumentRightJustified(10);
        ScriptData argGasPrice = sac.getArgumentRightJustified(10);
        ScriptData argGasLimit = sac.getArgumentRightJustified(10);
        ScriptData argNonce = sac.getArgumentRightJustified(8);
        ScriptData argData = sac.getArgumentAll();

        String script = new ScriptAssembler()
                .setCoinType(0x3C)
                // set coinType to 3C
                .copyString("F800").rlpString(argNonce)
                // nonce
                .rlpString(argGasPrice)
                // gasPrice
                .rlpString(argGasLimit)
                // gasLimit
                .copyString("94").copyArgument(argTo)
                // toAddress
                .rlpString(argValue)
                // value
                .rlpString(argData)
                // chainId v = 56
                .copyString("38", Buffer.CACHE1)
                .rlpString(ScriptData.getDataBufferAll(Buffer.CACHE1))
                .copyString("8080")
                .rlpList(2)
                .showMessage("BSC")
                .showWrap("SMART", "")
                .showPressButton()
                // version=00 ScriptAssembler.hash=06=ScriptAssembler.Keccak256 sign=01=ECDSA
                .setHeader(HashType.Keccak256, SignType.ECDSA)
                .getScript();
        return script;
    }

    public static String SmartContractScriptSignature = Strings.padStart("30440220429DF67EB2A0D1ED5681F912FCCE313C457829D7A76123B59F427E94A2FD8B0A02204FCC18E46AB820323D2CA5ED52FCEAA5DFFF70A3BF2DC4D060E30CFDCAE08D99", 144, '0');

    public static String getSmartContractSegmentScript() {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptData argTo = sac.getArgument(20);
        ScriptData argValue = sac.getArgumentRightJustified(10);
        ScriptData argGasPrice = sac.getArgumentRightJustified(10);
        ScriptData argGasLimit = sac.getArgumentRightJustified(10);
        ScriptData argNonce = sac.getArgumentRightJustified(8);
        ScriptData argData = sac.getArgument(4);

        String script = new ScriptAssembler()
                // set coinType to 3C
                .setCoinType(0x3C)
                .arrayPointer()
                // nonce
                .rlpString(argNonce)
                // gasPrice
                .rlpString(argGasPrice)
                // gasLimit
                .rlpString(argGasLimit)
                // toAddress
                .copyString("94")
                .copyArgument(argTo)
                // value
                .rlpString(argValue)
                .rlpDataPlaceholder(argData)
                // chainId v
                .copyString("38", Buffer.CACHE1)
                .rlpString(ScriptData.getDataBufferAll(Buffer.CACHE1))
                .copyString("8080")
                .arrayEnd(1)
                .showMessage("BSC")
                .showWrap("SMART", "")
                .showPressButton()
                // version=05 ScriptAssembler.hash=06=ScriptAssembler.Keccak256 sign=01=ECDSA
                .setHeader(HashType.Keccak256, SignType.ECDSA)
                .getScript();
        return script;
    }

    public static String SmartContractSegmentScriptSignature = Strings.padStart("3044022027a77af545abe0084cb55bafb2e295f361f76ef9636fc7879ff16358d113ffb3022067a27e1b08a24f67ce9f221181825fb7b9b04a163ec56acc286d32ebe3d01e01", 144, '0');

    public static String getMessageScript() {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptData argMessage = sac.getArgumentAll();

        String script = new ScriptAssembler()
                .setCoinType(0x3C)
                // set coinType to 3C
                .copyString("19457468657265756D205369676E6564204D6573736167653A0A")
                .copyArgument(argMessage)
                .showMessage("BSC")
                .showWrap("MESSAGE", "")
                .showPressButton()
                // version=00 ScriptAssembler.hash=06=ScriptAssembler.Keccak256 sign=01=ECDSA
                .setHeader(HashType.Keccak256, SignType.ECDSA)
                .getScript();
        return script;
    }

    public static String MessageScriptSignature = Strings.padStart("304402204C3BC0B32823EE53EBAA156DA9C6145029652E42D6FF1CEFF777FEC37738920C02200C52F02969FD648F160CCEC52D61403C854B694ABB059C17C70FBDA245E49CCC", 144, '0');

    public static String getTypedDataScript() {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptData argDomainSeparator = sac.getArgument(32);
        ScriptData argMessage = sac.getArgumentAll();

        String script = new ScriptAssembler()
                .setCoinType(0x3C)
                // set coinType to 3C
                .copyString("1901")
                .copyArgument(argDomainSeparator)
                .hash(argMessage, Buffer.TRANSACTION, ScriptAssembler.Keccak256)
                .showMessage("BSC")
                .showWrap("TYPED", "DATA")
                .showPressButton()
                // version=00 ScriptAssembler.hash=06=ScriptAssembler.Keccak256 sign=01=ECDSA
                .setHeader(HashType.Keccak256, SignType.ECDSA)
                .getScript();
        return script;
    }

    public static String TypedDataScriptSignature = Strings.padStart("3045022008935AF6BA11B9F720E59BE61AFF6F62A7A48FF2A39863AFD8B3920F355A1265022100E81EA86AC2FA3864CBC8773B10AF550B91F6A0E1FB68512DF32D8D35BC9FF3C8", 144, '0');

}
