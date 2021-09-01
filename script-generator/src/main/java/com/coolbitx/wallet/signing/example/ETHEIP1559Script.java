package com.coolbitx.wallet.signing.example;

import com.coolbitx.wallet.signing.utils.*;
import com.coolbitx.wallet.signing.utils.ScriptData.Buffer;

public class ETHEIP1559Script {
	
    public static void main(String[] args) throws Exception {
	    System.out.println("\nETH EIP-1559 Normal Script: " + getNormalScript());
	    System.out.println("\nETH EIP-1559 ERC-20 Script: " + getERC20Script());
	    System.out.println("\nETH EIP-1559 Smart Contract Script: " + getSmartContractScript());
	    System.out.println();
	}
	
    public static String getNormalScript() {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptData argTo = sac.getArgument(20);
        ScriptData argValue = sac.getArgumentRightJustified(10);
        ScriptData argGasTipCap = sac.getArgumentRightJustified(10);
        ScriptData argGasFeeCap = sac.getArgumentRightJustified(10);
        ScriptData argGasLimit = sac.getArgumentRightJustified(10);
        ScriptData argNonce = sac.getArgumentRightJustified(8);

        // version=04 ScriptAssembler.hash=06=ScriptAssembler.Keccak256 sign=01=ECDSA
        String header =  "03040601";

                // set coinType to 3C
        String coinType = ScriptAssembler.setCoinType(0x3C);

                // txType (EIP-2718)
        String payload = ScriptAssembler.copyString("02")
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
                + ScriptAssembler.arrayEnd(1);

        String display = ScriptAssembler.showMessage("ETH")
                + ScriptAssembler.copyString(HexUtil.toHexString("0x"), Buffer.FREE)
                + ScriptAssembler.baseConvert(argTo, Buffer.FREE, 0, ScriptAssembler.hexadecimalCharset, ScriptAssembler.leftJustify)
                + ScriptAssembler.showAddress(ScriptData.getDataBufferAll(Buffer.FREE))
                + ScriptAssembler.showAmount(argValue, 18)
                + ScriptAssembler.showPressButton();

        return header + coinType + payload + display;
    }
    
    public static String getERC20Script() {
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

        // version=04 ScriptAssembler.hash=06=ScriptAssembler.Keccak256 sign=01=ECDSA
        String header =  "03040601";

                // set coinType to 3C
        String coinType = ScriptAssembler.setCoinType(0x3C);

                // txType (EIP-2718)
        String payload = ScriptAssembler.copyString("02")
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
                + ScriptAssembler.arrayEnd(1);

        String display = ScriptAssembler.showMessage("ETH")
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

        return header + coinType + payload + display;
    }

    public static String getSmartContractScript() {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptData argTo = sac.getArgument(20);
        ScriptData argValue = sac.getArgumentRightJustified(10);
        ScriptData argGasTipCap = sac.getArgumentRightJustified(10);
        ScriptData argGasFeeCap = sac.getArgumentRightJustified(10);
        ScriptData argGasLimit = sac.getArgumentRightJustified(10);
        ScriptData argNonce = sac.getArgumentRightJustified(8);
        ScriptData argData = sac.getArgumentAll();

        // version=04 ScriptAssembler.hash=06=ScriptAssembler.Keccak256 sign=01=ECDSA
        String header =  "03040601";

                // set coinType to 3C
        String coinType = ScriptAssembler.setCoinType(0x3C);

                // txType (EIP-2718)
        String payload = ScriptAssembler.copyString("02")
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
                + ScriptAssembler.arrayEnd(1);

        String display = ScriptAssembler.showMessage("ETH")
                + ScriptAssembler.showWrap("SMART", "")
                + ScriptAssembler.showPressButton();

        return header + coinType + payload + display;
    }
}
