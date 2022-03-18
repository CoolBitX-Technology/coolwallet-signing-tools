package com.coolbitx.wallet.signing.scriptlib;

import com.coolbitx.wallet.signing.utils.HexUtil;
import com.coolbitx.wallet.signing.utils.ScriptArgumentComposer;
import com.coolbitx.wallet.signing.utils.ScriptAssembler;
import com.coolbitx.wallet.signing.utils.ScriptData;

public class AvaxCScript {

    public static void main(String[] args) {
        listAll();
    }

    public static void listAll() {
        System.out.println("Avax: \n" + getAvaxCScript() + "\n");
        System.out.println("Avax ERC20: \n" + getERC20Script() + "\n");
        System.out.println("Avax Smart Contract: \n" + getAvaxContractBlindScript() + "\n");
        System.out.println("Avax Message: \n" + getAvaxMessageBlindScript() + "\n");
        System.out.println("Avax TypedData: \n" + getAvaxTypedDataBlindScript() + "\n");
    }

    // todo: change chainid to 43114
    public static String getAvaxCScript() {

        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptData argTo = sac.getArgument(20);
        ScriptData argValue = sac.getArgumentRightJustified(10);
        ScriptData argGasPrice = sac.getArgumentRightJustified(10);
        ScriptData argGasLimit = sac.getArgumentRightJustified(10);
        ScriptData argNonce = sac.getArgumentRightJustified(8);

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
                .copyString("94").copyArgument(argTo)
                // value
                .rlpString(argValue)
                // data
                .copyString("80")
                // chainId v
                // + ScriptAssembler.rlpString(argChainId)
                .copyString("A869", ScriptData.Buffer.CACHE1)
                .rlpString(ScriptData.getDataBufferAll(ScriptData.Buffer.CACHE1))
                // r,s
                .copyString("8080")
                // .rlpList(1)
                .arrayEnd(1)
                .showMessage("AVAX")
                .copyString(HexUtil.toHexString("0x"), ScriptData.Buffer.CACHE2)
                .baseConvert(argTo, ScriptData.Buffer.CACHE2, 0, ScriptAssembler.hexadecimalCharset,
                        ScriptAssembler.zeroInherit)
                .showAddress(ScriptData.getDataBufferAll(ScriptData.Buffer.CACHE2))
                .showAmount(argValue, 18)
                .showPressButton()
                // version=00 ScriptAssembler.hash=06=ScriptAssembler.Keccak256 sign=01=ECDSA
                .setHeader(ScriptAssembler.HashType.Keccak256, ScriptAssembler.SignType.ECDSA)
                .getScript();
        return script;
    }

    // todo: change chainid to 43114
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

        String script = new ScriptAssembler()
                // set coinType to 3C
                .setCoinType(0x3C)
                // .copyString("F800")
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
                // + ScriptAssembler.rlpString(argChainId)
                .copyString("A869", ScriptData.Buffer.CACHE1)
                .rlpString(ScriptData.getDataBufferAll(ScriptData.Buffer.CACHE1))
                // r,s
                .copyString("8080")
                // .rlpList(2)
                .arrayEnd(1)
                .showMessage("AVAX")
                .ifSigned(argTokenInfo, argSign, "",
                        new ScriptAssembler().copyString(HexUtil.toHexString("@"), ScriptData.Buffer.CACHE2).getScript()
                )
                .setBufferInt(argNameLength, 1, 7)
                .copyArgument(argName, ScriptData.Buffer.CACHE2)
                .showMessage(ScriptData.getDataBufferAll(ScriptData.Buffer.CACHE2))
                .clearBuffer(ScriptData.Buffer.CACHE2)
                .copyString(HexUtil.toHexString("0x"), ScriptData.Buffer.CACHE2)
                .baseConvert(argTo, ScriptData.Buffer.CACHE2, 0, ScriptAssembler.hexadecimalCharset, ScriptAssembler.zeroInherit)
                .showAddress(ScriptData.getDataBufferAll(ScriptData.Buffer.CACHE2))
                .setBufferInt(argDecimal, 0, 20)
                .showAmount(argValue, 1000)
                .showPressButton()
                // version=00 ScriptAssembler.hash=06=ScriptAssembler.Keccak256 sign=01=ECDSA
                .setHeader(ScriptAssembler.HashType.Keccak256, ScriptAssembler.SignType.ECDSA)
                .getScript();
        return script;
    }

    // todo: change chainid to 43114
    public static String getAvaxContractBlindScript() {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptData argTo = sac.getArgument(20);
        ScriptData argValue = sac.getArgumentRightJustified(10);
        ScriptData argGasPrice = sac.getArgumentRightJustified(10);
        ScriptData argGasLimit = sac.getArgumentRightJustified(10);
        ScriptData argNonce = sac.getArgumentRightJustified(8);
        ScriptData argChainId = sac.getArgumentRightJustified(2);
        ScriptData argData = sac.getArgumentAll();

        String script = new ScriptAssembler()
                // set coinType to 3C
                .setCoinType(0x3C)
                // .copyString("F800")
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
                // .rlpString(argChainId)
                .copyString("A869", ScriptData.Buffer.CACHE1)
                .rlpString(ScriptData.getDataBufferAll(ScriptData.Buffer.CACHE1))
                // r, s
                .copyString("8080")
                // .rlpList(2)
                .arrayEnd(1)
                // txDetail
                .showMessage("AVAX").showWrap("SMART", "").showPressButton()
                // version=00 ScriptAssembler.hash=06=ScriptAssembler.Keccak256
                // sign=01=ECDSA
                .setHeader(ScriptAssembler.HashType.Keccak256, ScriptAssembler.SignType.ECDSA).getScript();
        return script;
    }

    public static String getAvaxMessageBlindScript() {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptData argMessage = sac.getArgumentAll();

        String script = new ScriptAssembler()
                // set coinType to 3C
                .setCoinType(0x3C)
                .copyString("19457468657265756D205369676E6564204D6573736167653A0A")
                .copyArgument(argMessage)
                // txDetail
                .showMessage("AVAX")
                .showWrap("MESSAGE", "")
                .showPressButton()
                // version=00 ScriptAssembler.hash=06=ScriptAssembler.Keccak256 sign=01=ECDSA
                .setHeader(ScriptAssembler.HashType.Keccak256, ScriptAssembler.SignType.ECDSA).getScript();
        return script;
    }

    public static String getAvaxTypedDataBlindScript() {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptData argDomainSeparator = sac.getArgument(32);
        ScriptData argMessage = sac.getArgumentAll();

        String script = new ScriptAssembler()
                // set coinType to 3C
                .setCoinType(0x3C)
                .copyString("1901")
                .copyArgument(argDomainSeparator)
                .hash(argMessage, ScriptData.Buffer.TRANSACTION, ScriptAssembler.Keccak256)
                // txDetail
                .showMessage("AVAX")
                .showWrap("EIP712", "")
                .showPressButton()
                // version=00 ScriptAssembler.hash=06=ScriptAssembler.Keccak256 sign=01=ECDSA
                .setHeader(ScriptAssembler.HashType.Keccak256, ScriptAssembler.SignType.ECDSA).getScript();
        return script;
    }
}