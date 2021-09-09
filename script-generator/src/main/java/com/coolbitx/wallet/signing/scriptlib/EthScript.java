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

public class EthScript {
    
    public static void listAll() {
        System.out.println("Eth eip1559: \n" + getETHEIP1559Script() + "\n");
        System.out.println("Eth eip1559 erc20: \n" + getETHEIP1559ERC20Script() + "\n");
        System.out.println("Eth eip1559 Smart Contract: \n" + getETHEIP1559SmartScript() + "\n");
        System.out.println("Eth: \n" + getETHScript() + "\n");
        System.out.println("Eth erc20: \n" + getERC20Script() + "\n");
        System.out.println("Eth Smart Contract: \n" + getEtherContractBlindScript() + "\n");
        System.out.println("Eth Message: \n" + getEtherMessageBlindScript() + "\n");
        System.out.println("Eth TypedData: \n" + getEtherTypedDataBlindScript() + "\n");
    }

    /*
txType(EIP-2718) : 02
rlpLength :        e5
chainId :          01
nonce :            80
maxPriorityFee :   01
maxFee :           81 ff
gasLimit :         84 02625a00
to address :       94 cccccccccccccccccccccccccccccccccccccccc
value :            83 0186a0
data :             80
accessList :       c0
     */
    public static String getETHEIP1559Script() {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptData argTo = sac.getArgument(20);
        ScriptData argValue = sac.getArgumentRightJustified(10);
        ScriptData argGasTipCap = sac.getArgumentRightJustified(10);
        ScriptData argGasFeeCap = sac.getArgumentRightJustified(10);
        ScriptData argGasLimit = sac.getArgumentRightJustified(10);
        ScriptData argNonce = sac.getArgumentRightJustified(8);
        //version=04 ScriptAssembler.hash=06=ScriptAssembler.Keccak256 sign=01=ECDSA
        return "03040601"
                // set coinType to 3C
                + ScriptAssembler.setCoinType(0x3C)
                // txType (EIP-2718)
                + ScriptAssembler.copyString("02")
                + ScriptAssembler.arrayPointer()
                // chainId
                + ScriptAssembler.copyString("01")
                // nonce
                + ScriptAssembler.rlpString(argNonce)
                // gasTipCap (maxPriorityFeePerGas)
                + ScriptAssembler.rlpString(argGasTipCap)
                // gasFeeCap (maxFeePerGas)
                + ScriptAssembler.rlpString(argGasFeeCap)
                // gasLimit
                + ScriptAssembler.rlpString(argGasLimit)
                // toAddress
                + ScriptAssembler.copyString("94")
                + ScriptAssembler.copyArgument(argTo)
                // value
                + ScriptAssembler.rlpString(argValue)
                // data
                + ScriptAssembler.copyString("80")
                // accessList
                + ScriptAssembler.copyString("C0")
                + ScriptAssembler.arrayEnd(1)
                + ScriptAssembler.showMessage("ETH")
                + ScriptAssembler.copyString(HexUtil.toHexString("0x"), Buffer.FREE)
                + ScriptAssembler.baseConvert(argTo, Buffer.FREE, 0, ScriptAssembler.hexadecimalCharset, ScriptAssembler.leftJustify)
                + ScriptAssembler.showAddress(ScriptData.getDataBufferAll(Buffer.FREE))
                + ScriptAssembler.showAmount(argValue, 18)
                + ScriptAssembler.showPressButton();
    }

    /*
txType(EIP-2718) : 02
rlpLength :        f86a
chainId :          01
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
    public static String getETHEIP1559ERC20Script() {
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
        //version=04 ScriptAssembler.hash=06=ScriptAssembler.Keccak256 sign=01=ECDSA
        return "03040601"
                // set coinType to 3C
                + ScriptAssembler.setCoinType(0x3C)
                // txType (EIP-2718)
                + ScriptAssembler.copyString("02")
                + ScriptAssembler.arrayPointer()
                // chainId
                + ScriptAssembler.copyString("01")
                + ScriptAssembler.rlpString(argNonce)
                + ScriptAssembler.rlpString(argGasTipCap)
                + ScriptAssembler.rlpString(argGasFeeCap)
                + ScriptAssembler.rlpString(argGasLimit)
                + ScriptAssembler.copyString("94")
                + ScriptAssembler.copyArgument(argContractAddress)
                // data, Length = 68
                + ScriptAssembler.copyString("80B844a9059cbb000000000000000000000000")
                + ScriptAssembler.copyArgument(argTo)
                + ScriptAssembler.copyString("0000000000000000000000000000000000000000")
                + ScriptAssembler.copyArgument(argValue)
                // accessList
                + ScriptAssembler.copyString("C0")
                + ScriptAssembler.arrayEnd(1)
                + ScriptAssembler.showMessage("ETH")
                + ScriptAssembler.ifSigned(argTokenInfo, argSign, "",
                        ScriptAssembler.copyString(HexUtil.toHexString("@"), Buffer.FREE)
                )
                + ScriptAssembler.setBufferInt(argNameLength, 1, 7)
                + ScriptAssembler.copyArgument(argName, Buffer.FREE)
                + ScriptAssembler.showMessage(ScriptData.getDataBufferAll(Buffer.FREE))
                + ScriptAssembler.clearBuffer(Buffer.FREE)
                + ScriptAssembler.copyString(HexUtil.toHexString("0x"), Buffer.FREE)
                + ScriptAssembler.baseConvert(argTo, Buffer.FREE, 0, ScriptAssembler.hexadecimalCharset, ScriptAssembler.leftJustify)
                + ScriptAssembler.showAddress(ScriptData.getDataBufferAll(Buffer.FREE))
                + ScriptAssembler.setBufferInt(argDecimal, 0, 20)
                + ScriptAssembler.showAmount(argValue, 1000)
                + ScriptAssembler.showPressButton();
    }

    public static String getETHEIP1559SmartScript() {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptData argTo = sac.getArgument(20);
        ScriptData argValue = sac.getArgumentRightJustified(10);
        ScriptData argGasTipCap = sac.getArgumentRightJustified(10);
        ScriptData argGasFeeCap = sac.getArgumentRightJustified(10);
        ScriptData argGasLimit = sac.getArgumentRightJustified(10);
        ScriptData argNonce = sac.getArgumentRightJustified(8);
        ScriptData argData = sac.getArgumentAll();

        // version=04 ScriptAssembler.hash=06=ScriptAssembler.Keccak256 sign=01=ECDSA
        return "03040601"
                // set coinType to 3C
                + ScriptAssembler.setCoinType(0x3C)
                // txType (EIP-2718)
                + ScriptAssembler.copyString("02")
                + ScriptAssembler.arrayPointer()
                // chainId
                + ScriptAssembler.copyString("01")
                + ScriptAssembler.rlpString(argNonce)
                + ScriptAssembler.rlpString(argGasTipCap)
                + ScriptAssembler.rlpString(argGasFeeCap)
                + ScriptAssembler.rlpString(argGasLimit)
                + ScriptAssembler.copyString("94")
                + ScriptAssembler.copyArgument(argTo)
                + ScriptAssembler.rlpString(argValue)
                + ScriptAssembler.rlpString(argData)
                // accessList
                + ScriptAssembler.copyString("C0")
                + ScriptAssembler.arrayEnd(1)
                + ScriptAssembler.showMessage("ETH")
                + ScriptAssembler.showWrap("SMART", "")
                + ScriptAssembler.showPressButton();
    }

    /*
     * E7 2A 85 09C74AFE1F 82 5208 94 A3255ECFE3F6727A62D938F4C29B2F73C361B26C 83
     * 989680 80 03 80 80
     */
    public static String getETHScript() {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptData argTo = sac.getArgument(20);
        ScriptData argValue = sac.getArgumentRightJustified(10);
        ScriptData argGasPrice = sac.getArgumentRightJustified(10);
        ScriptData argGasLimit = sac.getArgumentRightJustified(10);
        ScriptData argNonce = sac.getArgumentRightJustified(8);
        // version=00 ScriptAssembler.hash=06=ScriptAssembler.Keccak256 sign=01=ECDSA
        return "03000601"
                // set coinType to 3C
                + ScriptAssembler.setCoinType(0x3C)
                // temp byte for rlpList
                + ScriptAssembler.copyString("C0")
                // nonce
                + ScriptAssembler.rlpString(argNonce)
                // gasPrice
                + ScriptAssembler.rlpString(argGasPrice)
                // gasLimit
                + ScriptAssembler.rlpString(argGasLimit)
                // toAddress
                + ScriptAssembler.copyString("94") + ScriptAssembler.copyArgument(argTo)
                // value
                + ScriptAssembler.rlpString(argValue)
                // data
                + ScriptAssembler.copyString("80")
                // chainId v
                // + ScriptAssembler.rlpString(argChainId)
                + ScriptAssembler.copyString("01", Buffer.EXTENDED)
                + ScriptAssembler.rlpString(ScriptData.getDataBufferAll(Buffer.EXTENDED))
                // r,s
                + ScriptAssembler.copyString("8080") + ScriptAssembler.rlpList(1) + ScriptAssembler.showMessage("ETH")
                + ScriptAssembler.copyString(HexUtil.toHexString("0x"), Buffer.FREE)
                + ScriptAssembler.baseConvert(argTo, Buffer.FREE, 0, ScriptAssembler.hexadecimalCharset,
                        ScriptAssembler.leftJustify)
                + ScriptAssembler.showAddress(ScriptData.getDataBufferAll(Buffer.FREE))
                + ScriptAssembler.showAmount(argValue, 18) + ScriptAssembler.showPressButton();
    }
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
        ScriptData argChainId = sac.getArgumentRightJustified(2);
        ScriptData argTokenInfo = sac.getArgumentUnion(0, 29);
        ScriptData argDecimal = sac.getArgument(1);
        ScriptData argNameLength = sac.getArgument(1);
        ScriptData argName = sac.getArgumentVariableLength(7);
        ScriptData argContractAddress = sac.getArgument(20);
        ScriptData argSign = sac.getArgument(72);
        // version=00 ScriptAssembler.hash=06=ScriptAssembler.Keccak256 sign=01=ECDSA
        return "03000601"
                // set coinType to 3C
                + ScriptAssembler.setCoinType(0x3C)
                + ScriptAssembler.copyString("F800")
                + ScriptAssembler.rlpString(argNonce)
                + ScriptAssembler.rlpString(argGasPrice)
                + ScriptAssembler.rlpString(argGasLimit)
                + ScriptAssembler.copyString("94")
                + ScriptAssembler.copyArgument(argContractAddress)
                // data, Length = 68
                + ScriptAssembler.copyString("80B844a9059cbb000000000000000000000000")
                + ScriptAssembler.copyArgument(argTo)
                + ScriptAssembler.copyString("0000000000000000000000000000000000000000")
                + ScriptAssembler.copyArgument(argValue)
                // chainId v
                // + ScriptAssembler.rlpString(argChainId)
                + ScriptAssembler.copyString("01", Buffer.EXTENDED)
                + ScriptAssembler.rlpString(ScriptData.getDataBufferAll(Buffer.EXTENDED))
                // r,s
                + ScriptAssembler.copyString("8080")
                + ScriptAssembler.rlpList(2)
                + ScriptAssembler.showMessage("ETH")
                + ScriptAssembler.ifSigned(argTokenInfo, argSign, "",
                        ScriptAssembler.copyString(HexUtil.toHexString("@"), Buffer.FREE)
                )
                + ScriptAssembler.setBufferInt(argNameLength, 1, 7)
                + ScriptAssembler.copyArgument(argName, Buffer.FREE)
                + ScriptAssembler.showMessage(ScriptData.getDataBufferAll(Buffer.FREE))
                + ScriptAssembler.clearBuffer(Buffer.FREE)
                + ScriptAssembler.copyString(HexUtil.toHexString("0x"), Buffer.FREE)
                + ScriptAssembler.baseConvert(argTo, Buffer.FREE, 0, ScriptAssembler.hexadecimalCharset, ScriptAssembler.leftJustify)
                + ScriptAssembler.showAddress(ScriptData.getDataBufferAll(Buffer.FREE))
                + ScriptAssembler.setBufferInt(argDecimal, 0, 20)
                + ScriptAssembler.showAmount(argValue, 1000)
                + ScriptAssembler.showPressButton();
    }

    public static String getEtherContractBlindScript() {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptData argTo = sac.getArgument(20);
        ScriptData argValue = sac.getArgumentRightJustified(10);
        ScriptData argGasPrice = sac.getArgumentRightJustified(10);
        ScriptData argGasLimit = sac.getArgumentRightJustified(10);
        ScriptData argNonce = sac.getArgumentRightJustified(8);
        ScriptData argChainId = sac.getArgumentRightJustified(2);
        ScriptData argData = sac.getArgumentAll();

        // version=00 ScriptAssembler.hash=06=ScriptAssembler.Keccak256 sign=01=ECDSA
        return "03000601"
                // set coinType to 3C
                + ScriptAssembler.setCoinType(0x3C)
                + ScriptAssembler.copyString("F800")
                //nonce
                + ScriptAssembler.rlpString(argNonce)
                // gasPrice
                + ScriptAssembler.rlpString(argGasPrice)
                // gasLimit
                + ScriptAssembler.rlpString(argGasLimit)
                // toAddress
                + ScriptAssembler.copyString("94")
                + ScriptAssembler.copyArgument(argTo)
                // value
                + ScriptAssembler.rlpString(argValue)
                // data
                + ScriptAssembler.rlpString(argData)
                // chainId v
                // + ScriptAssembler.rlpString(argChainId)
                + ScriptAssembler.copyString("01", Buffer.EXTENDED)
                + ScriptAssembler.rlpString(ScriptData.getDataBufferAll(Buffer.EXTENDED))
                + ScriptAssembler.copyString("8080")
                + ScriptAssembler.rlpList(2)
                + ScriptAssembler.showMessage("ETH")
                + ScriptAssembler.showWrap("SMART", "")
                + ScriptAssembler.showPressButton();
    }

    public static String getEtherMessageBlindScript() {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptData argMessage = sac.getArgumentAll();

        return "03000601"
                + //version=00 ScriptAssembler.hash=06=ScriptAssembler.Keccak256 sign=01=ECDSA
                ScriptAssembler.setCoinType(0x3C)
                + // set coinType to 3C
                ScriptAssembler.copyString("19457468657265756D205369676E6564204D6573736167653A0A")
                + ScriptAssembler.copyArgument(argMessage)
                + ScriptAssembler.showMessage("ETH")
                + ScriptAssembler.showWrap("MESSAGE", "")
                + ScriptAssembler.showPressButton();
    }

    public static String getEtherTypedDataBlindScript() {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptData argDomainSeparator = sac.getArgument(32);
        ScriptData argMessage = sac.getArgumentAll();

        return "03000601"
                + //version=00 ScriptAssembler.hash=06=ScriptAssembler.Keccak256 sign=01=ECDSA
                ScriptAssembler.setCoinType(0x3C)
                + // set coinType to 3C
                ScriptAssembler.copyString("1901")
                + ScriptAssembler.copyArgument(argDomainSeparator)
                + ScriptAssembler.hash(argMessage, Buffer.TRANSACTION, ScriptAssembler.Keccak256)
                + ScriptAssembler.showMessage("ETH")
                + ScriptAssembler.showWrap("TYPED", "DATA")
                + ScriptAssembler.showPressButton();
    }

}
