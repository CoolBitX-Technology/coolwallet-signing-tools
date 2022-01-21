package com.coolbitx.wallet.signing.example;

import com.coolbitx.wallet.signing.utils.*;
import com.coolbitx.wallet.signing.utils.ScriptAssembler.HashType;
import com.coolbitx.wallet.signing.utils.ScriptAssembler.SignType;
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

        // set coinType to 3C
        ScriptAssembler scriptAsb = new ScriptAssembler();
        String coinType = scriptAsb.setCoinType(0x3C).getScript();

        // txType (EIP-2718)
        String payload = scriptAsb
                .copyString("02")
                .arrayPointer()
                // chainId
                .copyString("8189")
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
                .arrayEnd(1)
                .getScript();

        String display = scriptAsb
                .showMessage("ETH")
                .copyString(HexUtil.toHexString("0x"), Buffer.CACHE2)
                .baseConvert(argTo, Buffer.CACHE2, 0, ScriptAssembler.hexadecimalCharset, ScriptAssembler.zeroInherit)
                .showAddress(ScriptData.getDataBufferAll(Buffer.CACHE2))
                .showAmount(argValue, 18)
                .showPressButton()
                .getScript();

        // length=03 version=04 are auto-generated based on the composition of the payload
        // ScriptAssembler.hash.Keccak256=06 sign=ECDSA=01
        // header =  "03040601"
        String header = scriptAsb.setHeader(HashType.Keccak256, SignType.ECDSA).getScript();
        //script = header + coinType + payload + display
        return scriptAsb.getScript();
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

        // set coinType to 3C
        ScriptAssembler scriptAsb = new ScriptAssembler();
        String coinType = scriptAsb.setCoinType(0x3C).getScript();

        // txType (EIP-2718)
        String payload = scriptAsb
                .copyString("02")
                .arrayPointer()
                // chainId
                .copyString("01")
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
                .arrayEnd(1)
                .getScript();

        String display = scriptAsb.showMessage("ETH")
                .ifSigned(argTokenInfo, argSign, "",
                        new ScriptAssembler().copyString(HexUtil.toHexString("@"), Buffer.CACHE2).getScript()
                )
                .setBufferInt(argNameLength, 1, 7)
                .copyArgument(argName, Buffer.CACHE2)
                .showMessage(ScriptData.getDataBufferAll(Buffer.CACHE2))
                .clearBuffer(Buffer.CACHE2)
                .copyString(HexUtil.toHexString("0x"), Buffer.CACHE2)
                .baseConvert(argTo, Buffer.CACHE2, 0, ScriptAssembler.hexadecimalCharset, ScriptAssembler.zeroInherit)
                .showAddress(ScriptData.getDataBufferAll(Buffer.CACHE2))
                .setBufferInt(argDecimal, 0, 20)
                .showAmount(argValue, 1000)
                .showPressButton()
                .getScript();
        
        // length=03 version=04 are auto-generated based on the composition of the payload
        // ScriptAssembler.hash.Keccak256=06 sign=ECDSA=01
        // header =  "03040601"
        String header = scriptAsb.setHeader(HashType.Keccak256, SignType.ECDSA).getScript();
        //script = header + coinType + payload + display
        return scriptAsb.getScript();
    }

    public static String getSmartContractScript() {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptData argTo = sac.getArgument(20);
        ScriptData argValue = sac.getArgumentRightJustified(10);
        ScriptData argGasTipCap = sac.getArgumentRightJustified(10);
        ScriptData argGasFeeCap = sac.getArgumentRightJustified(10);
        ScriptData argGasLimit = sac.getArgumentRightJustified(10);
        ScriptData argNonce = sac.getArgumentRightJustified(8);
        ScriptData argData = sac.getArgument(4);

        // set coinType to 3C
        ScriptAssembler scriptAsb = new ScriptAssembler();
        String coinType = scriptAsb.setCoinType(0x3C).getScript();

        // txType (EIP-2718)
        String payload = scriptAsb
                .copyString("02")
                .arrayPointer()
                // chainId
                .copyString("01")
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
                .arrayEnd(1)
                .getScript();

        String display = scriptAsb
                .showMessage("ETH")
                .showWrap("SMART", "")
                .showPressButton()
                .getScript();

        // length=03 version=04 are auto-generated based on the composition of the payload
        // ScriptAssembler.hash.Keccak256=06 sign=ECDSA=01
        // header =  "03040601"
        String header = scriptAsb.setHeader(HashType.Keccak256, SignType.ECDSA).getScript();
        //script = header + coinType + payload + display
        return scriptAsb.getScript();
    }
}
