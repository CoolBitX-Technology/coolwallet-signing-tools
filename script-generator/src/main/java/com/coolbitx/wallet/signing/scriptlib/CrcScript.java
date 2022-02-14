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

public class CrcScript {

    public static void listAll() {
        System.out.println("Crc: \n" + getCRCScript() + "\n");
        System.out.println("Crc erc20: \n" + getCRC20Script() + "\n");
    }

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
    public static String getCRCScript() {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptData argTo = sac.getArgument(20);
        ScriptData argValue = sac.getArgumentRightJustified(10);
        ScriptData argGasPrice = sac.getArgumentRightJustified(10);
        ScriptData argGasLimit = sac.getArgumentRightJustified(10);
        ScriptData argNonce = sac.getArgumentRightJustified(8);

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
                .copyString("94").copyArgument(argTo)
                // value
                .rlpString(argValue)
                // data
                .copyString("80")
                // chainId v
                .copyString("19", Buffer.CACHE1)
                .rlpString(ScriptData.getDataBufferAll(Buffer.CACHE1))
                // r,s
                .copyString("8080")
                .arrayEnd(1)
                .showMessage("CRO")
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

    public static String CRCScriptSignature = "003045022100B3D3C5F089BFD31021357B05F769256299460DAA54B4DBD8777158C047A3729E02206078F1403E17FB750E7B4BAE197566165AC5F5061C0764F325AC7327EC2B5EB9";

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
    public static String getCRC20Script() {
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
                .copyString("19", Buffer.CACHE1)
                .rlpString(ScriptData.getDataBufferAll(Buffer.CACHE1))
                // r,s
                .copyString("8080")
                .arrayEnd(1)
                .showMessage("CRO")
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
                // version=00 ScriptAssembler.hash=06=ScriptAssembler.Keccak256 sign=01=ECDSA
                .setHeader(HashType.Keccak256, SignType.ECDSA)
                .getScript();
        return script;
    }

    public static String CRC20ScriptSignature = "00304502207254B9195694D839E5A385F7B787CD316C03738006A8F4E0978FD351AADF20C9022100DAE520982AABC5B0D6C03F9D1BA4A99B17B9D05E17ECEEDFC6CC7E8E3786EF04";

}
