/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.coolbitx.wallet.signing.example;

import com.coolbitx.wallet.signing.utils.*;
import com.coolbitx.wallet.signing.utils.ScriptData.Buffer;

/**
 *
 * @author derek
 */
public class ETHScript {

    public static void main(String[] args) throws Exception {
        System.out.println("\nETH Normal Script: " + getNormalScript());
        System.out.println("\nETH ERC-20 Script: " + getERC20Script());
        System.out.println();
    }

    public static String getNormalScript() {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptData argTo = sac.getArgument(20);
        ScriptData argValue = sac.getArgumentRightJustified(10);
        ScriptData argGasPrice = sac.getArgumentRightJustified(10);
        ScriptData argGasLimit = sac.getArgumentRightJustified(10);
        ScriptData argNonce = sac.getArgumentRightJustified(8);
        ScriptData argChainId = sac.getArgumentRightJustified(3);
        //version=00 ScriptAssembler.hash=06=ScriptAssembler.Keccak256 sign=01=ECDSA
        String header = "03000601";
        // set coinType to 3C
        String coinType = ScriptAssembler.setCoinType(0x03C6);
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
        String display = ScriptAssembler.showMessage("MATIC")
                + ScriptAssembler.copyString(HexUtil.toHexString("0x"), Buffer.CACHE2)
                + ScriptAssembler.baseConvert(argTo, Buffer.CACHE2, 0, ScriptAssembler.hexadecimalCharset, ScriptAssembler.zeroInherit)
                + ScriptAssembler.showAddress(ScriptData.getDataBufferAll(Buffer.CACHE2))
                + ScriptAssembler.showAmount(argValue, 18)
                + ScriptAssembler.showPressButton();
        return header + coinType + payload + display;
    }

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
                        ScriptAssembler.copyString(HexUtil.toHexString("@"), Buffer.CACHE2)
                )
                + ScriptAssembler.setBufferInt(argNameLength, 1, 7)
                + ScriptAssembler.copyArgument(argName, Buffer.CACHE2)
                + ScriptAssembler.showMessage(ScriptData.getDataBufferAll(Buffer.CACHE2))
                + ScriptAssembler.clearBuffer(Buffer.CACHE2)
                + ScriptAssembler.copyString(HexUtil.toHexString("0x"), Buffer.CACHE2)
                + ScriptAssembler.baseConvert(argTo, Buffer.CACHE2, 0, ScriptAssembler.hexadecimalCharset, ScriptAssembler.zeroInherit)
                + ScriptAssembler.showAddress(ScriptData.getDataBufferAll(Buffer.CACHE2))
                + ScriptAssembler.setBufferInt(argDecimal, 0, 20)
                + ScriptAssembler.showAmount(argValue, 1000)
                + ScriptAssembler.showPressButton();
    }
}
