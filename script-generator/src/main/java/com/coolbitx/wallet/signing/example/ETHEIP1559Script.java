package com.coolbitx.wallet.signing.example;

import org.spongycastle.util.encoders.Hex;
import com.coolbitx.wallet.signing.utils.*;
import com.coolbitx.wallet.signing.utils.ScriptBuffer.BufferType;

public class ETHEIP1559Script {
	
    public static void main(String[] args) throws Exception {
	    System.out.println("\nETH EIP-1559 Normal Script: " + getNormalScript());
	    System.out.println("\nETH EIP-1559 ERC-20 Script: " + getERC20Script());
	    System.out.println("\nETH EIP-1559 Smart Contract Script: " + getSmartContractScript());
	    System.out.println();
	}
	
    public static String getNormalScript() {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptBuffer argTo = sac.getArgument(20);
        ScriptBuffer argValue = sac.getArgumentRightJustified(10);
        ScriptBuffer argGasTipCap = sac.getArgumentRightJustified(10);
        ScriptBuffer argGasFeeCap = sac.getArgumentRightJustified(10);
        ScriptBuffer argGasLimit = sac.getArgumentRightJustified(10);
        ScriptBuffer argNonce = sac.getArgumentRightJustified(8);

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
                + ScriptAssembler.copyString(HexUtil.toHexString("0x"), BufferType.FREE)
                + ScriptAssembler.baseConvert(argTo, BufferType.FREE, 0, ScriptAssembler.hexadecimalCharset, ScriptAssembler.leftJustify)
                + ScriptAssembler.showAddress(ScriptBuffer.getDataBufferAll(BufferType.FREE))
                + ScriptAssembler.showAmount(argValue, 18)
                + ScriptAssembler.showPressButton();

        return header + coinType + payload + display;
    }
    
    public static String getERC20Script() {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptBuffer argTo = sac.getArgument(20);
        ScriptBuffer argValue = sac.getArgument(12);
        ScriptBuffer argGasTipCap = sac.getArgumentRightJustified(10);
        ScriptBuffer argGasFeeCap = sac.getArgumentRightJustified(10);
        ScriptBuffer argGasLimit = sac.getArgumentRightJustified(10);
        ScriptBuffer argNonce = sac.getArgumentRightJustified(8);
        ScriptBuffer argTokenInfo = sac.getArgumentUnion(0, 29);
        ScriptBuffer argDecimal = sac.getArgument(1);
        ScriptBuffer argNameLength = sac.getArgument(1);
        ScriptBuffer argName = sac.getArgumentVariableLength(7);
        ScriptBuffer argContractAddress = sac.getArgument(20);
        ScriptBuffer argSign = sac.getArgument(72);

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
                        ScriptAssembler.copyString(HexUtil.toHexString("@"), BufferType.FREE)
                )
                + ScriptAssembler.setBufferInt(argNameLength, 1, 7)
                + ScriptAssembler.copyArgument(argName, BufferType.FREE)
                + ScriptAssembler.showMessage(ScriptBuffer.getDataBufferAll(BufferType.FREE))
                + ScriptAssembler.resetDest(BufferType.FREE)
                + ScriptAssembler.copyString(HexUtil.toHexString("0x"), BufferType.FREE)
                + ScriptAssembler.baseConvert(argTo, BufferType.FREE, 0, ScriptAssembler.hexadecimalCharset, ScriptAssembler.leftJustify)
                + ScriptAssembler.showAddress(ScriptBuffer.getDataBufferAll(BufferType.FREE))
                + ScriptAssembler.setBufferInt(argDecimal, 0, 20)
                + ScriptAssembler.showAmount(argValue, 1000)
                + ScriptAssembler.showPressButton();

        return header + coinType + payload + display;
    }

    public static String getSmartContractScript() {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptBuffer argTo = sac.getArgument(20);
        ScriptBuffer argValue = sac.getArgumentRightJustified(10);
        ScriptBuffer argGasTipCap = sac.getArgumentRightJustified(10);
        ScriptBuffer argGasFeeCap = sac.getArgumentRightJustified(10);
        ScriptBuffer argGasLimit = sac.getArgumentRightJustified(10);
        ScriptBuffer argNonce = sac.getArgumentRightJustified(8);
        ScriptBuffer argData = sac.getArgumentAll();

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
