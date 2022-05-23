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
import static com.coolbitx.wallet.signing.utils.ScriptAssembler.TYPE_RLP;
import com.coolbitx.wallet.signing.utils.ScriptData;
import com.coolbitx.wallet.signing.utils.ScriptData.Buffer;
import com.google.common.base.Strings;

public class MaticScript {

    public static void main(String[] args) {
        MaticScript.listAll();
    }

    public static void listAll() {
        System.out.println("Poly eip1559: \n" + getEIP1559TransferScript() + "\n");
        System.out.println("Signature: \n" + EIP1559TransferScriptSignature + "\n");

        System.out.println("Poly eip1559 erc20: \n" + getEIP1559ERC20Script() + "\n");
        System.out.println("Signature: \n" + EIP1559ERC20ScriptSignature + "\n");

        System.out.println("Poly eip1559 Smart Contract: \n" + getEIP1559SmartContractScript() + "\n");
        System.out.println("Signature: \n" + EIP1559SmartContractScriptSignature + "\n");

        System.out.println("Poly eip1559 Smart Contract Segment: \n" + getEIP1559SmartContractSegmentScript() + "\n");
        System.out.println("Signature: \n" + EIP1559SmartContractSegmentScriptSignature + "\n");

        System.out.println("Poly: \n" + getTransferScript() + "\n");
        System.out.println("Signature: \n" + TransferScriptSignature + "\n");

        System.out.println("Poly erc20: \n" + getERC20Script() + "\n");
        System.out.println("Signature: \n" + ERC20ScriptSignature + "\n");

        System.out.println("Poly Smart Contract: \n" + getSmartContractScript() + "\n");
        System.out.println("Signature: \n" + SmartContractScriptSignature + "\n");

        System.out.println("Poly Smart Contract Segment: \n" + getSmartContractSegmentScript() + "\n");
        System.out.println("Signature: \n" + SmartContractSegmentScriptSignature + "\n");

        System.out.println("Poly Message: \n" + getMessageScript() + "\n");
        System.out.println("Signature: \n" + MessageScriptSignature + "\n");

        System.out.println("Poly TypedData: \n" + getTypedDataScript() + "\n");
        System.out.println("Signature: \n" + TypedDataScriptSignature + "\n");
    }

    /*
txType(EIP-2718) : 02
rlpLength :        e5
chainId :          89
nonce :            80
maxPriorityFee :   01
maxFee :           81 ff
gasLimit :         84 02625a00
to address :       94 cccccccccccccccccccccccccccccccccccccccc
value :            83 0186a0
data :             80
accessList :       c0
     */
    public static String getEIP1559TransferScript() {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptData argTo = sac.getArgument(20);
        ScriptData argValue = sac.getArgumentRightJustified(10);
        ScriptData argGasTipCap = sac.getArgumentRightJustified(10);
        ScriptData argGasFeeCap = sac.getArgumentRightJustified(10);
        ScriptData argGasLimit = sac.getArgumentRightJustified(10);
        ScriptData argNonce = sac.getArgumentRightJustified(8);

        String script = new ScriptAssembler()
                // set coinType to 3C
                .setCoinType(0x3C)
                // txType (EIP-2718)
                .copyString("02")
                .arrayPointer()
                // chainId
                .copyString("81")
                .copyString("89")
                // nonce
                .rlpString(argNonce)
                // gasTipCap (maxPriorityFeePerGas)
                .rlpString(argGasTipCap)
                // gasFeeCap (maxFeePerGas)
                .rlpString(argGasFeeCap)
                // gasLimit
                .rlpString(argGasLimit)
                // toAddress
                .copyString("94")
                .copyArgument(argTo)
                // value
                .rlpString(argValue)
                // data
                .copyString("80")
                // accessList
                .copyString("C0")
                .arrayEnd(TYPE_RLP)
                .showMessage("MATIC")
                .copyString(HexUtil.toHexString("0x"), Buffer.CACHE2)
                .baseConvert(
                        argTo,
                        Buffer.CACHE2,
                        0,
                        ScriptAssembler.hexadecimalCharset,
                        ScriptAssembler.zeroInherit
                )
                .showAddress(ScriptData.getDataBufferAll(Buffer.CACHE2))
                .showAmount(argValue, 18)
                .showPressButton()
                //version=04 ScriptAssembler.hash=06=ScriptAssembler.Keccak256 sign=01=ECDSA
                .setHeader(HashType.Keccak256, SignType.ECDSA)
                .getScript();
        return script;
    }

    public static String EIP1559TransferScriptSignature = Strings.padStart("3045022100d128877b574811b72be0ea71c8133a38674e8199c2e777585d1dac7a8b464ac5022047ce752a9e4601d0928c6f432ad39ace729f05379b28da21c92d44655c99d436", 144, '0');

    /*
txType(EIP-2718) : 02
rlpLength :        f86a
chainId :          89
nonce :            80
maxPriorityFee :   01
maxFee :           81 ff
gasLimit :         84 02625a00
to address :       94 cccccccccccccccccccccccccccccccccccccccc
value :            83 0186a0
data :             b844 a9059cbb
                   0000000000000000000000009e3a6e03298780078003d3965689b8f653ee87b1
                   0000000000000000000000000000000000000000000000004563918244f40000
accessList :       c0
     */
    public static String getEIP1559ERC20Script() {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptData argTo = sac.getArgument(20);
        ScriptData argValue = sac.getArgument(12);
        ScriptData argGasTipCap = sac.getArgumentRightJustified(10);
        ScriptData argGasFeeCap = sac.getArgumentRightJustified(10);
        ScriptData argGasLimit = sac.getArgumentRightJustified(10);
        ScriptData argNonce = sac.getArgumentRightJustified(8);
        ScriptData argTokenInfo = sac.getArgumentUnion(0, 29);
        ScriptData argDecimal = sac.getArgument(1);
        ScriptData argNameLength = sac.getArgument(1);
        ScriptData argName = sac.getArgumentVariableLength(7);
        ScriptData argContractAddress = sac.getArgument(20);
        ScriptData argSign = sac.getArgument(72);

        String script = new ScriptAssembler()
                // set coinType to 3C
                .setCoinType(0x3C)
                // txType (EIP-2718)
                .copyString("02")
                .arrayPointer()
                // chainId
                .copyString("8189")
                .rlpString(argNonce)
                .rlpString(argGasTipCap)
                .rlpString(argGasFeeCap)
                .rlpString(argGasLimit)
                .copyString("94")
                .copyArgument(argContractAddress)
                // data, Length = 68
                .copyString("80B844a9059cbb000000000000000000000000")
                .copyArgument(argTo)
                .copyString("0000000000000000000000000000000000000000")
                .copyArgument(argValue)
                // accessList
                .copyString("C0")
                .arrayEnd(TYPE_RLP)
                .showMessage("MATIC")
                .ifSigned(
                        argTokenInfo,
                        argSign,
                        "",
                        new ScriptAssembler()
                                .copyString(HexUtil.toHexString("@"), Buffer.CACHE2)
                                .getScript()
                )
                .setBufferInt(argNameLength, 1, 7)
                .copyArgument(argName, Buffer.CACHE2)
                .showMessage(ScriptData.getDataBufferAll(Buffer.CACHE2))
                .clearBuffer(Buffer.CACHE2)
                .copyString(HexUtil.toHexString("0x"), Buffer.CACHE2)
                .baseConvert(
                        argTo,
                        Buffer.CACHE2,
                        0,
                        ScriptAssembler.hexadecimalCharset,
                        ScriptAssembler.zeroInherit
                )
                .showAddress(ScriptData.getDataBufferAll(Buffer.CACHE2))
                .setBufferInt(argDecimal, 0, 20)
                .showAmount(argValue, ScriptData.bufInt)
                .showPressButton()
                // version=04 ScriptAssembler.hash=06=ScriptAssembler.Keccak256 sign=01=ECDSA
                .setHeader(HashType.Keccak256, SignType.ECDSA)
                .getScript();
        return script;
    }

    public static String EIP1559ERC20ScriptSignature = Strings.padStart("304502203bd3874e3cdc22d1864a636a1e6efc24d908b4dbbdab5c787e8b28204e604e1e022100c943dacc88ed850f171aa466d0a4a3d0e7dfad64f29c971abd0e3b353e3f938a", 144, '0');

    public static String getEIP1559SmartContractScript() {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptData argTo = sac.getArgument(20);
        ScriptData argValue = sac.getArgumentRightJustified(10);
        ScriptData argGasTipCap = sac.getArgumentRightJustified(10);
        ScriptData argGasFeeCap = sac.getArgumentRightJustified(10);
        ScriptData argGasLimit = sac.getArgumentRightJustified(10);
        ScriptData argNonce = sac.getArgumentRightJustified(8);
        ScriptData argData = sac.getArgumentAll();

        String script = new ScriptAssembler()
                // set coinType to 3C
                .setCoinType(0x3C)
                // txType (EIP-2718)
                .copyString("02")
                .arrayPointer()
                // chainId
                .copyString("8189")
                .rlpString(argNonce)
                .rlpString(argGasTipCap)
                .rlpString(argGasFeeCap)
                .rlpString(argGasLimit)
                .copyString("94")
                .copyArgument(argTo)
                .rlpString(argValue)
                .rlpString(argData)
                // accessList
                .copyString("C0")
                .arrayEnd(TYPE_RLP)
                .showMessage("MATIC")
                .showWrap("SMART", "")
                .showPressButton()
                // version=05 ScriptAssembler.hash=06=ScriptAssembler.Keccak256 sign=01=ECDSA
                .setHeader(HashType.Keccak256, SignType.ECDSA)
                .getScript();
        return script;
    }

    public static String EIP1559SmartContractScriptSignature = Strings.padStart("3044022038a9e5280f856afb3ece88785304e5b040923da4b8e4c3297b46b9746db1012502205e841b922ff256750680b4905a89f5c4d38b224e1a4f93e80e8f45fd609b016d", 144, '0');

    public static String getEIP1559SmartContractSegmentScript() {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptData argTo = sac.getArgument(20);
        ScriptData argValue = sac.getArgumentRightJustified(10);
        ScriptData argGasTipCap = sac.getArgumentRightJustified(10);
        ScriptData argGasFeeCap = sac.getArgumentRightJustified(10);
        ScriptData argGasLimit = sac.getArgumentRightJustified(10);
        ScriptData argNonce = sac.getArgumentRightJustified(8);
        ScriptData argData = sac.getArgument(4);

        String script = new ScriptAssembler()
                // set coinType to 3C
                .setCoinType(0x3C)
                // txType (EIP-2718)
                .copyString("02")
                .arrayPointer()
                // chainId
                .copyString("8189")
                .rlpString(argNonce)
                .rlpString(argGasTipCap)
                .rlpString(argGasFeeCap)
                .rlpString(argGasLimit)
                .copyString("94")
                .copyArgument(argTo)
                .rlpString(argValue)
                .rlpDataPlaceholder(argData)
                // accessList
                .copyString("C0")
                .arrayEnd(TYPE_RLP)
                .showMessage("MATIC")
                .showWrap("SMART", "")
                .showPressButton()
                // version=05 ScriptAssembler.hash=06=ScriptAssembler.Keccak256 sign=01=ECDSA
                .setHeader(HashType.Keccak256, SignType.ECDSA)
                .getScript();
        return script;
    }

    public static String EIP1559SmartContractSegmentScriptSignature = Strings.padStart("304502204418f1450e5aa176d8e0001985294ee8b98981943b32990e7a9ab6cbe6ac2362022100fee0a14cbff961495177858ef381c7a31d8103383b0d5fb6c2365896c16d33b1", 144, '0');


    /*
    E7
    2A
    85 09C74AFE1F
    82 5208
    94 A3255ECFE3F6727A62D938F4C29B2F73C361B26C
    83 989680
    80
    03
    80
    80
     */
    public static String getTransferScript() {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptData argTo = sac.getArgument(20);
        ScriptData argValue = sac.getArgumentRightJustified(10);
        ScriptData argGasPrice = sac.getArgumentRightJustified(10);
        ScriptData argGasLimit = sac.getArgumentRightJustified(10);
        ScriptData argNonce = sac.getArgumentRightJustified(8);
        // ScriptData argChainId = sac.getArgumentRightJustified(5);

        String script = new ScriptAssembler()
                // set coinType to 3C
                .setCoinType(0x3C)
                // temp byte for rlpList
                // .copyString("C0")
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
                // data
                .copyString("80")
                // chainId v
                // + ScriptAssembler.rlpString(argChainId)
                .copyString("89", Buffer.CACHE1)
                .rlpString(ScriptData.getDataBufferAll(Buffer.CACHE1))
                // r,s
                .copyString("8080")
                .arrayEnd(TYPE_RLP)
                // .rlpList(1)
                .showMessage("MATIC")
                .copyString(HexUtil.toHexString("0x"), Buffer.CACHE2)
                .baseConvert(
                        argTo,
                        Buffer.CACHE2,
                        0,
                        ScriptAssembler.hexadecimalCharset,
                        ScriptAssembler.zeroInherit
                )
                .showAddress(ScriptData.getDataBufferAll(Buffer.CACHE2))
                .showAmount(argValue, 18)
                .showPressButton()
                // version=00 ScriptAssembler.hash=06=ScriptAssembler.Keccak256 sign=01=ECDSA
                .setHeader(HashType.Keccak256, SignType.ECDSA)
                .getScript();
        return script;
    }

    public static String TransferScriptSignature = Strings.padStart("304502207c347e621b20d6e6963a3bf6eafecc22b526d3f7086d200f132e9cceabe007780221009275cb85be3518d65cdcc3cfd9bf8d9e30c3144b79ac3f637210b96fa753036e", 144, '0');

    /*
f86a
1e
85 01718c7e00
83 030d40
94 86fa049857e0209aa7d9e616f7eb3b3b78ecfdb0
80
b844 a9059cbb
     0000000000000000000000009e3a6e03298780078003d3965689b8f653ee87b1
     0000000000000000000000000000000000000000000000004563918244f40000
01
80
80
     */
    public static String getERC20Script() {
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
                // set coinType to 3C
                .setCoinType(0x3C)
                .arrayPointer()
                // .copyString("F800")
                .rlpString(argNonce)
                .rlpString(argGasPrice)
                .rlpString(argGasLimit)
                .copyString("94")
                .copyArgument(argContractAddress)
                // data, Length = 68
                .copyString("80B844a9059cbb000000000000000000000000")
                .copyArgument(argTo)
                .copyString("0000000000000000000000000000000000000000")
                .copyArgument(argValue)
                // chainId v
                // .rlpString(argChainId)
                .copyString("89", Buffer.CACHE1)
                .rlpString(ScriptData.getDataBufferAll(Buffer.CACHE1))
                // r,s
                .copyString("8080")
                // .rlpList(2)
                .arrayEnd(TYPE_RLP)
                .showMessage("MATIC")
                .ifSigned(
                        argTokenInfo,
                        argSign,
                        "",
                        new ScriptAssembler()
                                .copyString(HexUtil.toHexString("@"), Buffer.CACHE2)
                                .getScript()
                )
                .setBufferInt(argNameLength, 1, 7)
                .copyArgument(argName, Buffer.CACHE2)
                .showMessage(ScriptData.getDataBufferAll(Buffer.CACHE2))
                .clearBuffer(Buffer.CACHE2)
                .copyString(HexUtil.toHexString("0x"), Buffer.CACHE2)
                .baseConvert(
                        argTo,
                        Buffer.CACHE2,
                        0,
                        ScriptAssembler.hexadecimalCharset,
                        ScriptAssembler.zeroInherit
                )
                .showAddress(ScriptData.getDataBufferAll(Buffer.CACHE2))
                .setBufferInt(argDecimal, 0, 20)
                .showAmount(argValue, ScriptData.bufInt)
                .showPressButton()
                // version=00 ScriptAssembler.hash=06=ScriptAssembler.Keccak256 sign=01=ECDSA
                .setHeader(HashType.Keccak256, SignType.ECDSA)
                .getScript();
        return script;
    }

    public static String ERC20ScriptSignature = Strings.padStart("30460221009b873fd0fd00023d8bde582b9bc8a195beb7730c5eb7dd48e783c5574b8dd185022100f722f0f0813a60c4b87275aa4d21397597f0073e646aa32efa8b0ea8a96db8ba", 144, '0');

    public static String getSmartContractScript() {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptData argTo = sac.getArgument(20);
        ScriptData argValue = sac.getArgumentRightJustified(10);
        ScriptData argGasPrice = sac.getArgumentRightJustified(10);
        ScriptData argGasLimit = sac.getArgumentRightJustified(10);
        ScriptData argNonce = sac.getArgumentRightJustified(8);
        ScriptData argData = sac.getArgumentAll();

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
                // data
                .rlpString(argData)
                // chainId v
                //.rlpString(argChainId)
                .copyString("89", Buffer.CACHE1)
                .rlpString(ScriptData.getDataBufferAll(Buffer.CACHE1))
                .copyString("8080")
                .arrayEnd(TYPE_RLP)
                .showMessage("MATIC")
                .showWrap("SMART", "")
                .showPressButton()
                // version=05 ScriptAssembler.hash=06=ScriptAssembler.Keccak256 sign=01=ECDSA
                .setHeader(HashType.Keccak256, SignType.ECDSA)
                .getScript();
        return script;
    }

    public static String SmartContractScriptSignature = Strings.padStart("3046022100a32dbe7b0b5f3a3559e967c5bcb2ce313c81c9320a5fe9867fc8e041ced50c78022100e774aaea0df2d1db0818a3ad6b2d1234902af44d7b44176f1a75437716751c71", 144, '0');

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
                .copyString("89", Buffer.CACHE1)
                .rlpString(ScriptData.getDataBufferAll(Buffer.CACHE1))
                .copyString("8080")
                .arrayEnd(TYPE_RLP)
                .showMessage("MATIC")
                .showWrap("SMART", "")
                .showPressButton()
                // version=05 ScriptAssembler.hash=06=ScriptAssembler.Keccak256 sign=01=ECDSA
                .setHeader(HashType.Keccak256, SignType.ECDSA)
                .getScript();
        return script;
    }

    public static String SmartContractSegmentScriptSignature = Strings.padStart("304402201834eef7c38f4e8c7fedb8c6d1f9b2726a7ef5035748875f3a9f150b40635ab002206bb088b39a220dfe4a62b80ec61d048e96ce7651ac1b8832ea837457e7304b69", 144, '0');

    public static String getMessageScript() {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptData argMessage = sac.getArgumentAll();

        String script = new ScriptAssembler()
                // set coinType to 3C
                .setCoinType(0x3C)
                .copyString("19457468657265756D205369676E6564204D6573736167653A0A")
                .copyArgument(argMessage)
                .showMessage("MATIC")
                .showWrap("MESSAGE", "")
                .showPressButton()
                //version=00 ScriptAssembler.hash=06=ScriptAssembler.Keccak256 sign=01=ECDSA
                .setHeader(HashType.Keccak256, SignType.ECDSA)
                .getScript();
        return script;
    }

    public static String MessageScriptSignature = Strings.padStart("3045022100a33765ca5929cfd400ab9471bbd92261e061a94e4d3eeca2b7421091655fd0fc02206fa50d324f2affd9d24d9b45c08a8b3f859c043d1158dd1a8dad00e7e193a24b", 144, '0');

    public static String getTypedDataScript() {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptData argDomainSeparator = sac.getArgument(32);
        ScriptData argMessage = sac.getArgumentAll();

        String script = new ScriptAssembler()
                // set coinType to 3C
                .setCoinType(0x3C)
                .copyString("1901")
                .copyArgument(argDomainSeparator)
                .hash(argMessage, Buffer.TRANSACTION, HashType.Keccak256)
                .showMessage("MATIC")
                .showWrap("EIP712", "")
                .showPressButton()
                //version=00 ScriptAssembler.hash=06=ScriptAssembler.Keccak256 sign=01=ECDSA
                .setHeader(HashType.Keccak256, SignType.ECDSA)
                .getScript();
        return script;
    }

    public static String TypedDataScriptSignature = Strings.padStart("3046022100c70e80e0668a137a4bac8d3ef3e8d83efda57a8e5f31c5ca7c0340d17d02d209022100e03bff92275d8b342b6c209ec8ab3055c1bcc3f48a677d43e92f1b6d9350ec4f", 144, '0');
}
