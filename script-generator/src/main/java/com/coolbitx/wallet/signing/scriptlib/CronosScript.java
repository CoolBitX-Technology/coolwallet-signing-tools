/*
 * To change this license header, choose License Headers in Project Properties. To change this
 * template file, choose Tools | Templates and open the template in the editor.
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

public class CronosScript {

    public static void listAll() {
        System.out.println("Cronos: \n" + getCRCScript() + "\n");
        System.out.println("Cronos erc20: \n" + getCRC20Script() + "\n");
        System.out.println("Cronos Smart Contract: \n " + getCROContractBlindScript() + "\n");
        System.out.println("Cronos Smart Contract Segment: \n " + getCROContractBlindSegmentScript() + "\n");
    }

    /*
         * E7 2A 85 09C74AFE1F 82 5208 94 A3255ECFE3F6727A62D938F4C29B2F73C361B26C 83 989680 80 03
         * 80 80
     */
    public static String getCRCScript() {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptData argTo = sac.getArgument(20);
        ScriptData argValue = sac.getArgumentRightJustified(10);
        ScriptData argGasPrice = sac.getArgumentRightJustified(10);
        ScriptData argGasLimit = sac.getArgumentRightJustified(10);
        ScriptData argNonce = sac.getArgumentRightJustified(8);

        String script = new ScriptAssembler()
                // set coinType to 3C
                .setCoinType(0x3C).arrayPointer()
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
                // chainId v
                .copyString("19", Buffer.CACHE1)
                .rlpString(ScriptData.getDataBufferAll(Buffer.CACHE1))
                // r,s
                .copyString("8080").arrayEnd(1).showMessage("CRO")
                .copyString(HexUtil.toHexString("0x"), Buffer.CACHE2)
                .baseConvert(argTo, Buffer.CACHE2, 0,
                        ScriptAssembler.hexadecimalCharset,
                        ScriptAssembler.zeroInherit)
                .showAddress(ScriptData.getDataBufferAll(Buffer.CACHE2))
                .showAmount(argValue, 18).showPressButton()
                // version=00 ScriptAssembler.hash=06=ScriptAssembler.Keccak256
                // sign=01=ECDSA
                .setHeader(HashType.Keccak256, SignType.ECDSA).getScript();
        return script;
    }

    public static String CRCScriptSignature
            = "003045022100B3D3C5F089BFD31021357B05F769256299460DAA54B4DBD8777158C047A3729E02206078F1403E17FB750E7B4BAE197566165AC5F5061C0764F325AC7327EC2B5EB9";

    public static String getCRC20Script() {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptData argTo = sac.getArgument(20);
        ScriptData argValue = sac.getArgument(12);
        ScriptData argGasPrice = sac.getArgumentRightJustified(10);
        ScriptData argGasLimit = sac.getArgumentRightJustified(10);
        ScriptData argNonce = sac.getArgumentRightJustified(8);
        // Chain Information Info
        ScriptData argChainInfo = sac.getArgumentUnion(0, 15);
        ScriptData argChainIdLength = sac.getArgument(1);
        ScriptData argChainId = sac.getArgumentVariableLength(6);
        ScriptData argSymbolLength = sac.getArgument(1);
        ScriptData argSymbol = sac.getArgumentVariableLength(7);
        ScriptData argChainSign = sac.getArgument(72);

        // ERC 20 Token Info
        ScriptData argTokenInfo = sac.getArgumentUnion(0, 29);
        ScriptData argDecimal = sac.getArgument(1);
        ScriptData argNameLength = sac.getArgument(1);
        ScriptData argName = sac.getArgumentVariableLength(7);
        ScriptData argContractAddress = sac.getArgument(20);
        ScriptData argTokenSign = sac.getArgument(72);

        String script = new ScriptAssembler()
                // set coinType to 3C
                .setCoinType(0x3C)
                .ifSigned(argChainInfo, argChainSign, "",
                        ScriptAssembler.throwSEError)
                .arrayPointer().rlpString(argNonce).rlpString(argGasPrice)
                .rlpString(argGasLimit)
                // address
                .copyString("94").copyArgument(argContractAddress)
                // data, Length = 68
                .copyString("80B844a9059cbb000000000000000000000000")
                .copyArgument(argTo)
                .copyString("0000000000000000000000000000000000000000")
                .copyArgument(argValue)
                // chainId v
                .setBufferInt(argChainIdLength, 1, 6).rlpString(argChainId)
                // empty r,s
                .copyString("8080").arrayEnd(1)
                // Show symbol
                .setBufferInt(argSymbolLength, 1, 7)
                .copyArgument(argSymbol, Buffer.CACHE1)
                .showMessage(ScriptData.getDataBufferAll(Buffer.CACHE1))
                .clearBuffer(Buffer.CACHE1)
                .ifSigned(argTokenInfo, argTokenSign, "", new ScriptAssembler()
                        .copyString(HexUtil.toHexString("@"), Buffer.CACHE2)
                        .getScript())
                .setBufferInt(argNameLength, 1, 7)
                .copyArgument(argName, Buffer.CACHE2)
                .showMessage(ScriptData.getDataBufferAll(Buffer.CACHE2))
                .clearBuffer(Buffer.CACHE2)
                .copyString(HexUtil.toHexString("0x"), Buffer.CACHE2)
                .baseConvert(argTo, Buffer.CACHE2, 0,
                        ScriptAssembler.hexadecimalCharset,
                        ScriptAssembler.zeroInherit)
                .showAddress(ScriptData.getDataBufferAll(Buffer.CACHE2))
                .setBufferInt(argDecimal, 0, 20).showAmount(argValue, 1000)
                .showPressButton()
                // version=00 ScriptAssembler.hash=06=ScriptAssembler.Keccak256
                // sign=01=ECDSA
                .setHeader(HashType.Keccak256, SignType.ECDSA).getScript();
        return script;
    }

    public static String CRC20ScriptSignature = Strings.padStart(
            "3046022100A121463CD1D1D5263A737FDF0228D350A1C1FE77638A314A7F38FF58986EFEFA0221008E5533FC8AA33045049BF9B516AE8FA4E80627A62C085F249C81C0D777EC6F0F",
            144, '0');

    public static String getCROContractBlindScript() {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptData argTo = sac.getArgument(20);
        ScriptData argValue = sac.getArgumentRightJustified(10);
        ScriptData argGasPrice = sac.getArgumentRightJustified(10);
        ScriptData argGasLimit = sac.getArgumentRightJustified(10);
        ScriptData argNonce = sac.getArgumentRightJustified(8);
        ScriptData argData = sac.getArgumentAll();

        String script = new ScriptAssembler()
                // set coinType to 3C
                .setCoinType(0x3C).arrayPointer()
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
                .rlpString(argData)
                // chainId v
                .copyString("19", Buffer.CACHE1)
                .rlpString(ScriptData.getDataBufferAll(Buffer.CACHE1))
                .copyString("8080").arrayEnd(1)
                // Display phase
                .showMessage("CRO").showWrap("SMART", "").showPressButton()
                // version=05 ScriptAssembler.hash=06=ScriptAssembler.Keccak256
                // sign=01=ECDSA
                .setHeader(HashType.Keccak256, SignType.ECDSA).getScript();
        return script;
    }

    public static String CROSmartContractBlindScriptSignature = Strings.padStart(
            "3045022072902FD446A0E80824EAEF45ACCA5F800BE52AD3CE9D011AF90C8D85710059D0022100C467A0FE1BF941B4900CB269B80BD2D1999EEB5B8633676AA0383E8EDE863FA3",
            144, '0');

    public static String getCROContractBlindSegmentScript() {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptData argTo = sac.getArgument(20);
        ScriptData argValue = sac.getArgumentRightJustified(10);
        ScriptData argGasPrice = sac.getArgumentRightJustified(10);
        ScriptData argGasLimit = sac.getArgumentRightJustified(10);
        ScriptData argNonce = sac.getArgumentRightJustified(8);
        ScriptData argData = sac.getArgument(4);

        String script = new ScriptAssembler()
                // set coinType to 3C
                .setCoinType(0x3C).arrayPointer()
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
                .rlpDataPlaceholder(argData)
                // chainId v
                .copyString("19", Buffer.CACHE1)
                .rlpString(ScriptData.getDataBufferAll(Buffer.CACHE1))
                .copyString("8080").arrayEnd(1)
                // Display phase
                .showMessage("CRO").showWrap("SMART", "").showPressButton()
                // version=05 ScriptAssembler.hash=06=ScriptAssembler.Keccak256
                // sign=01=ECDSA
                .setHeader(HashType.Keccak256, SignType.ECDSA).getScript();
        return script;
    }

    public static String CROContractBlindSegmentScriptSignature = Strings.padStart(
            "3046022100A0188B33A3CCCF98BF055FB82BF621E9D43AFE7DD066D907DD446688E91141C0022100EF67E474862FA59DC5DE49079FD5258CDB6939C41C84E0170132399E1CB96592",
            144, '0');
}
