/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package example;

import org.spongycastle.util.encoders.Hex;

import utils.HexUtil;
import utils.ScriptArgumentComposer;
import utils.ScriptAssembler;
import utils.ScriptBuffer;
import utils.ScriptBuffer.BufferType;

/**
 *
 * @author derek
 */
public class ETHScript {
	
    public static void main(String[] args) throws Exception {
	    System.out.println("ETHScript: " + getETHScript());
	    System.out.println("ERC20Script: " + getERC20Script());
	}
	
    public static String getETHScript() {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptBuffer argTo = sac.getArgument(20);
        ScriptBuffer argValue = sac.getArgumentRightJustified(10);
        ScriptBuffer argGasPrice = sac.getArgumentRightJustified(10);
        ScriptBuffer argGasLimit = sac.getArgumentRightJustified(10);
        ScriptBuffer argNonce = sac.getArgumentRightJustified(8);
        ScriptBuffer argChainId = sac.getArgumentRightJustified(2);
        //version=00 ScriptAssembler.hash=06=ScriptAssembler.Keccak256 sign=01=ECDSA
        String header = "03000601";
                // set coinType to 3C
        String coinType = ScriptAssembler.setCoinType(0x3C);
                // temp byte for rlpList
        String payload = ScriptAssembler.copyString("C0")
                // nonce
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
                + ScriptAssembler.copyString("80")
                // chainId v
                + ScriptAssembler.rlpString(argChainId)
                // r,s
                + ScriptAssembler.copyString("8080")
                + ScriptAssembler.rlpList(1);
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
        ScriptBuffer argGasPrice = sac.getArgumentRightJustified(10);
        ScriptBuffer argGasLimit = sac.getArgumentRightJustified(10);
        ScriptBuffer argNonce = sac.getArgumentRightJustified(8);
        ScriptBuffer argChainId = sac.getArgumentRightJustified(2);
        ScriptBuffer argTokenInfo = sac.getArgumentUnion(0, 29);
        ScriptBuffer argDecimal = sac.getArgument(1);
        ScriptBuffer argNameLength = sac.getArgument(1);
        ScriptBuffer argName = sac.getArgumentVariableLength(7);
        ScriptBuffer argContractAddress = sac.getArgument(20);
        ScriptBuffer argSign = sac.getArgument(72);

        return "03000601"
                + //version=00 ScriptAssembler.hash=06=ScriptAssembler.Keccak256 sign=01=ECDSA
                ScriptAssembler.setCoinType(0x3C)
                + // set coinType to 3C
                ScriptAssembler.copyString("F800")
                + ScriptAssembler.rlpString(argNonce)
                + ScriptAssembler.rlpString(argGasPrice)
                + ScriptAssembler.rlpString(argGasLimit)
                + ScriptAssembler.copyString("94")
                + ScriptAssembler.copyArgument(argContractAddress)
                + ScriptAssembler.copyString("80B844a9059cbb000000000000000000000000")
                + //value = 0 , dataLength = 68
                ScriptAssembler.copyArgument(argTo)
                + ScriptAssembler.copyString("0000000000000000000000000000000000000000")
                + ScriptAssembler.copyArgument(argValue)
                + ScriptAssembler.rlpString(argChainId)
                + //chainId v
                ScriptAssembler.copyString("8080")
                + ScriptAssembler.rlpList(2)
                + ScriptAssembler.showMessage("ETH")
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
    }
}
