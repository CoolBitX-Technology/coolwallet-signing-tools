/*
 * To change this license header, choose License Headers in Project Properties. To change this
 * template file, choose Tools | Templates and open the template in the editor.
 */
package com.coolbitx.wallet.signing.scriptlib;

import static com.coolbitx.wallet.signing.utils.ScriptAssembler.TYPE_RLP;

import com.coolbitx.wallet.signing.utils.HexUtil;
import com.coolbitx.wallet.signing.utils.ScriptArgumentComposer;
import com.coolbitx.wallet.signing.utils.ScriptAssembler;
import com.coolbitx.wallet.signing.utils.ScriptAssembler.HashType;
import com.coolbitx.wallet.signing.utils.ScriptAssembler.SignType;
import com.coolbitx.wallet.signing.utils.ScriptData;
import com.coolbitx.wallet.signing.utils.ScriptData.Buffer;
import com.google.common.base.Strings;

public class CustomEvmScript {
    public static void main(String[] args) {
        listAll();
    }

    public static void listAll() {
        System.out.println("CustomEvm: \n" + getTransferScript(0x3c) + "\n");
        System.out.println("CustomEvm erc20: \n" + getERC20Script(0x3c) + "\n");
        System.out.println("CustomEvm Smart Contract: \n" + getSmartContractScript(0x3c) + "\n");
        System.out.println("CustomEvm Smart Contract Segment: \n" + getSmartContractSegmentScript(0x3c) + "\n");

        System.out.println("CustomEvm EIP-712 Message: \n" + getMessageScript(0x3c) + "\n");
        System.out.println("CustomEvm EIP-712 Typed Data: \n" + getTypedDataScript(0x3c) + "\n");

        System.out.println("CustomEvm EIP-1559: \n" + getEIP1559TransferScript(0x3c) + "\n");
        System.out.println("CustomEvm EIP-1559 erc20: \n" + getEIP1559ERC20Script(0x3c) + "\n");
        System.out.println("CustomEvm EIP-1559 Smart Contract: \n" + getEIP1559SmartContractScript(0x3c) + "\n");
        System.out.println(
            "CustomEvm EIP-1559 Smart Contract Segment: \n" + getEIP1559SmartContractSegmentScript(0x3c) + "\n");
    }

    /*
     * E7 2A 85 09C74AFE1F 82 5208 94 A3255ECFE3F6727A62D938F4C29B2F73C361B26C 83
     * 989680 80 03 80 80
     */
    public static String getTransferScript(int coinType) {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptData argTo = sac.getArgument(20);
        ScriptData argValue = sac.getArgumentRightJustified(32);
        ScriptData argGasPrice = sac.getArgumentRightJustified(10);
        ScriptData argGasLimit = sac.getArgumentRightJustified(10);
        ScriptData argNonce = sac.getArgumentRightJustified(8);
        // Chain Information Info
        ScriptData argChainIdLength = sac.getArgument(1);
        ScriptData argChainId = sac.getArgumentVariableLength(6);

        ScriptAssembler scriptAssembler = new ScriptAssembler()
            // set coinType to 3C
            .setCoinType(coinType).arrayPointer()
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
            .setBufferInt(argChainIdLength, 1, 6).rlpString(argChainId)
            // r,s
            .copyString("8080").arrayEnd(TYPE_RLP);

        // tx detail
        displayChainId(scriptAssembler, argChainId, argChainIdLength)
            .copyString(HexUtil.toHexString("0x"), Buffer.CACHE2)
            .baseConvert(argTo, Buffer.CACHE2, 0, ScriptAssembler.hexadecimalCharset, ScriptAssembler.zeroInherit)
            .showAddress(ScriptData.getDataBufferAll(Buffer.CACHE2)).showAmount(argValue, 18).showPressButton()
            // version=00 ScriptAssembler.hash=06=ScriptAssembler.Keccak256
            // sign=01=ECDSA
            .setHeader(HashType.Keccak256, SignType.ECDSA);
        return scriptAssembler.getScript();
    }

    public static String TransferScriptSignature = Strings.padStart(
        "304602210094d5692371a68d79319631712d6d40a69e30fa81c2bd5771ddba44d33581285d022100e26eeb4947e399a077a03fe0f70b4715d9f2c13b6483b25cc1797e2d41e00f3a",
        144, '0');

    public static String getERC20Script(int coinType) {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptData argTo = sac.getArgument(20);
        ScriptData argValue = sac.getArgument(32);
        ScriptData argGasPrice = sac.getArgumentRightJustified(10);
        ScriptData argGasLimit = sac.getArgumentRightJustified(10);
        ScriptData argNonce = sac.getArgumentRightJustified(8);
        // Chain Information Info
        ScriptData argChainInfo = sac.getArgumentUnion(0, 23);
        ScriptData argChainIdLength = sac.getArgument(1);
        ScriptData argChainId = sac.getArgumentVariableLength(6);

        // ERC 20 Token Info
        ScriptData argDecimal = sac.getArgument(1);
        ScriptData argNameLength = sac.getArgument(1);
        ScriptData argName = sac.getArgumentVariableLength(7);
        ScriptData argContractAddress = sac.getArgument(20);

        ScriptAssembler scriptAssembler = new ScriptAssembler()
            // set coinType to 3C
            .setCoinType(coinType).arrayPointer().rlpString(argNonce).rlpString(argGasPrice).rlpString(argGasLimit)
            // address
            .copyString("94").copyArgument(argContractAddress)
            // data, Length = 68
            .copyString("80B844a9059cbb000000000000000000000000").copyArgument(argTo).copyArgument(argValue)
            // chainId v
            .setBufferInt(argChainIdLength, 1, 6).rlpString(argChainId)
            // empty r,s
            .copyString("8080").arrayEnd(TYPE_RLP);

        // tx detail
        displayChainId(scriptAssembler, argChainId, argChainIdLength)
            .copyString(HexUtil.toHexString("@"), Buffer.CACHE2).setBufferInt(argNameLength, 1, 7)
            .copyArgument(argName, Buffer.CACHE2).showMessage(ScriptData.getDataBufferAll(Buffer.CACHE2))
            .clearBuffer(Buffer.CACHE2).copyString(HexUtil.toHexString("0x"), Buffer.CACHE2)
            .baseConvert(argTo, Buffer.CACHE2, 0, ScriptAssembler.hexadecimalCharset, ScriptAssembler.zeroInherit)
            .showAddress(ScriptData.getDataBufferAll(Buffer.CACHE2)).setBufferInt(argDecimal, 0, 20)
            .showAmount(argValue, ScriptData.bufInt).showPressButton()
            // version=00 ScriptAssembler.hash=06=ScriptAssembler.Keccak256
            // sign=01=ECDSA
            .setHeader(HashType.Keccak256, SignType.ECDSA);
        return scriptAssembler.getScript();
    }

    public static String ERC20ScriptSignature = Strings.padStart(
        "304402205b9ca3917952bc04bd302a54656122f590a778eac890ce43e83a8e872b10e555022067d285c838ab7e69e9e4add83fba7bb0248fcf4c8ef7aa594985691492521670",
        144, '0');

    private static final String emptyAddress = "0000000000000000000000000000000000000000";

    public static String getSmartContractScript(int coinType) {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptData argTo = sac.getArgument(20);
        ScriptData argValue = sac.getArgumentRightJustified(32);
        ScriptData argGasPrice = sac.getArgumentRightJustified(10);
        ScriptData argGasLimit = sac.getArgumentRightJustified(10);
        ScriptData argNonce = sac.getArgumentRightJustified(8);
        // Chain Information Info
        ScriptData argChainIdLength = sac.getArgument(1);
        ScriptData argChainId = sac.getArgumentVariableLength(6);
        // smart contract data
        ScriptData argData = sac.getArgumentAll();

        ScriptAssembler scriptAssembler = new ScriptAssembler()
            // set coinType to 3C
            .setCoinType(coinType).arrayPointer()
            // nonce
            .rlpString(argNonce)
            // gasPrice
            .rlpString(argGasPrice)
            // gasLimit
            .rlpString(argGasLimit)
            // toAddress
            .ifEqual(argTo, emptyAddress, new ScriptAssembler().copyString("80").getScript(),
                new ScriptAssembler().copyString("94").copyArgument(argTo).getScript())
            // value
            .rlpString(argValue)
            // data
            .rlpString(argData)
            // chainId v
            .setBufferInt(argChainIdLength, 1, 6).rlpString(argChainId)
            // r, s
            .copyString("8080").arrayEnd(TYPE_RLP);

        // Display phase
        displayChainId(scriptAssembler, argChainId, argChainIdLength).showWrap("SMART", "").showPressButton()
            // version=05 ScriptAssembler.hash=06=ScriptAssembler.Keccak256
            // sign=01=ECDSA
            .setHeader(HashType.Keccak256, SignType.ECDSA);

        return scriptAssembler.getScript();
    }

    public static String SmartContractScriptSignature = Strings.padStart(
        "3044022040e9d3b51937d2e24bfe8ecbcfd5d99e7ca214b94c8a966f91fa34be1c700ae002205e24981b520eb79bb3813a6b8c214849af814bf7d16f03a728566e0494c3516d",
        144, '0');

    public static String getSmartContractSegmentScript(int coinType) {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptData argTo = sac.getArgument(20);
        ScriptData argValue = sac.getArgumentRightJustified(32);
        ScriptData argGasPrice = sac.getArgumentRightJustified(10);
        ScriptData argGasLimit = sac.getArgumentRightJustified(10);
        ScriptData argNonce = sac.getArgumentRightJustified(8);
        ScriptData argData = sac.getArgument(4);
        // Chain Information Info
        ScriptData argChainInfo = sac.getArgumentUnion(0, 23);
        ScriptData argChainIdLength = sac.getArgument(1);
        ScriptData argChainId = sac.getArgumentVariableLength(6);

        ScriptAssembler scriptAssembler = new ScriptAssembler()
            // set coinType to 3C
            .setCoinType(coinType).arrayPointer()
            // nonce
            .rlpString(argNonce)
            // gasPrice
            .rlpString(argGasPrice)
            // gasLimit
            .rlpString(argGasLimit)
            // toAddress
            .ifEqual(argTo, emptyAddress, new ScriptAssembler().copyString("80").getScript(),
                new ScriptAssembler().copyString("94").copyArgument(argTo).getScript())
            // value
            .rlpString(argValue)
            // data
            .rlpDataPlaceholder(argData)
            // chainId v
            .setBufferInt(argChainIdLength, 1, 6).rlpString(argChainId)
            // r, s
            .copyString("8080").arrayEnd(TYPE_RLP);

        // Display phase
        displayChainId(scriptAssembler, argChainId, argChainIdLength).showWrap("SMART", "").showPressButton()
            // version=05 ScriptAssembler.hash=06=ScriptAssembler.Keccak256
            // sign=01=ECDSA
            .setHeader(HashType.Keccak256, SignType.ECDSA);
        return scriptAssembler.getScript();
    }

    public static String SmartContractSegmentScriptSignature = Strings.padStart(
        "30440220565a93f8a94d573ee468f8edb507ec0f22f2b6b299117e1db4c48cd75eb9f4d502206c4542b3b2a07b0cca7258dfaf6aafae93e89dd6578fc5ad1a5d222373b695e8",
        144, '0');

    public static String getTypedDataScript(int coinType) {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptData argDomainSeparator = sac.getArgument(32);
        // Chain Information Info
        ScriptData argChainIdLength = sac.getArgument(1);
        ScriptData argChainId = sac.getArgumentVariableLength(6);
        // typedData
        ScriptData argMessage = sac.getArgumentAll();

        ScriptAssembler scriptAssembler = new ScriptAssembler()
            // set coinType to 3C
            .setCoinType(coinType).copyString("1901").copyArgument(argDomainSeparator)
            .hash(argMessage, Buffer.TRANSACTION, HashType.Keccak256);

        // Display phase
        displayChainId(scriptAssembler, argChainId, argChainIdLength).showWrap("EIP712", "").showPressButton()
            // version=00 ScriptAssembler.hash=06=ScriptAssembler.Keccak256 sign=01=ECDSA
            .setHeader(HashType.Keccak256, SignType.ECDSA);

        return scriptAssembler.getScript();
    }

    public static String TypedDataScriptSignature = Strings.padStart(
        "3046022100edb220ef5f538f9a581c91c81502d369322d03d4eeac29e5926438827a1a14dc02210098c9930f917ed6fb72215a3177733d6f9ede7ea55d1117644b0a5cca581f6737",
        144, '0');

    public static String getMessageScript(int coinType) {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        // Chain Information Info
        ScriptData argChainIdLength = sac.getArgument(1);
        ScriptData argChainId = sac.getArgumentVariableLength(6);
        ScriptData argMessage = sac.getArgumentAll();

        ScriptAssembler scriptAssembler = new ScriptAssembler()
            // set coinType to 3C
            .setCoinType(coinType).copyString("19457468657265756D205369676E6564204D6573736167653A0A")
            .copyArgument(argMessage);

        // txDetail
        displayChainId(scriptAssembler, argChainId, argChainIdLength).showWrap("MESSAGE", "").showPressButton()
            // version=00 ScriptAssembler.hash=06=ScriptAssembler.Keccak256 sign=01=ECDSA
            .setHeader(HashType.Keccak256, SignType.ECDSA);
        return scriptAssembler.getScript();
    }

    public static String MessageScriptSignature = Strings.padStart(
        "304502200784b5392cceb1fea5f077d90125cd153d871e164c77aefa31ef4b94690f1fd00221009f77e5af2ee01a8c3b484cc0e3cdac77d984ca24664c336142bdf87d70ba7549",
        144, '0');

    private static ScriptAssembler displayChainId(ScriptAssembler scriptAssembler, ScriptData argChainId,
        ScriptData argChainIdLength) {
        return scriptAssembler
            // display chainId e.g.: c534352 represent Scroll chainId=534352

            // put prefix "c"
            .copyString(HexUtil.toHexString("c"), Buffer.CACHE2)

            // convert chainId to chars and concat to CACHE2
            .setBufferInt(argChainIdLength, 1, 6).copyArgument(argChainId, Buffer.CACHE1)
            .baseConvert(ScriptData.getDataBufferAll(Buffer.CACHE1), Buffer.CACHE2, 0, ScriptAssembler.decimalCharset,
                ScriptAssembler.zeroInherit)

            .showMessage(ScriptData.getDataBufferAll(Buffer.CACHE2)).clearBuffer(Buffer.CACHE1)
            .clearBuffer(Buffer.CACHE2);
    }

    public static String getEIP1559TransferScript(int coinType) {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptData argTo = sac.getArgument(20);
        ScriptData argValue = sac.getArgumentRightJustified(32);
        ScriptData argGasTipCap = sac.getArgumentRightJustified(10);
        ScriptData argGasFeeCap = sac.getArgumentRightJustified(10);
        ScriptData argGasLimit = sac.getArgumentRightJustified(10);
        ScriptData argNonce = sac.getArgumentRightJustified(8);
        // Chain Information Info
        ScriptData argChainIdLength = sac.getArgument(1);
        ScriptData argChainId = sac.getArgumentVariableLength(6);

        ScriptAssembler scriptAssembler = new ScriptAssembler();
        scriptAssembler
            // set coinType to 3C
            .setCoinType(coinType)
            // .ifSigned(argChainInfo, argChainSign, "", ScriptAssembler.throwSEError)
            // txType (EIP-2718)
            .copyString("02").arrayPointer()
            // chainId v
            .setBufferInt(argChainIdLength, 1, 6).rlpString(argChainId)
            // nonce
            .rlpString(argNonce)
            // gasTipCap (maxPriorityFeePerGas)
            .rlpString(argGasTipCap)
            // gasFeeCap (maxFeePerGas)
            .rlpString(argGasFeeCap)
            // gasLimit
            .rlpString(argGasLimit)
            // toAddress
            .copyString("94").copyArgument(argTo)
            // value
            .rlpString(argValue)
            // data
            .copyString("80")
            // accessList
            .copyString("C0").arrayEnd(TYPE_RLP);

        // txDetail
        // display chainId
        displayChainId(scriptAssembler, argChainId, argChainIdLength)
            // display toAddress
            .copyString(HexUtil.toHexString("0x"), Buffer.CACHE2)
            .baseConvert(argTo, Buffer.CACHE2, 0, ScriptAssembler.hexadecimalCharset, ScriptAssembler.zeroInherit)
            .showAddress(ScriptData.getDataBufferAll(Buffer.CACHE2))
            // display amount
            .showAmount(argValue, 18).showPressButton()
            // version=04 ScriptAssembler.hash=06=ScriptAssembler.Keccak256 sign=01=ECDSA
            .setHeader(HashType.Keccak256, SignType.ECDSA);

        return scriptAssembler.getScript();
    }

    public static String EIP1559TransferScriptSignature = Strings.padStart(
        "3046022100952de2a00277bdb803459d9fc4087c6148b18680c3b2b48a1985cd25e08a2404022100d9eedb1b9720fb96e3b85729a60b392488a1a0d3ede1255cc3886d400b014045",
        144, '0');

    public static String getEIP1559ERC20Script(int coinType) {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptData argTo = sac.getArgument(20);
        ScriptData argValue = sac.getArgument(32);
        ScriptData argGasTipCap = sac.getArgumentRightJustified(10);
        ScriptData argGasFeeCap = sac.getArgumentRightJustified(10);
        ScriptData argGasLimit = sac.getArgumentRightJustified(10);
        ScriptData argNonce = sac.getArgumentRightJustified(8);
        // Chain Information Info
        ScriptData argChainIdLength = sac.getArgument(1);
        ScriptData argChainId = sac.getArgumentVariableLength(6);
        // ERC20 Token Info
        ScriptData argDecimal = sac.getArgument(1);
        ScriptData argNameLength = sac.getArgument(1);
        ScriptData argName = sac.getArgumentVariableLength(7);
        ScriptData argContractAddress = sac.getArgument(20);

        ScriptAssembler scriptAssembler = new ScriptAssembler();
        scriptAssembler = scriptAssembler
            // set coinType to 3C
            .setCoinType(coinType)
            // txType (EIP-2718)
            .copyString("02").arrayPointer()
            // chainId
            .setBufferInt(argChainIdLength, 1, 6).rlpString(argChainId).rlpString(argNonce).rlpString(argGasTipCap)
            .rlpString(argGasFeeCap).rlpString(argGasLimit).copyString("94").copyArgument(argContractAddress)
            // data, Length = 68
            .copyString("80B844a9059cbb000000000000000000000000").copyArgument(argTo).copyArgument(argValue)
            // accessList
            .copyString("C0").arrayEnd(TYPE_RLP);

        displayChainId(scriptAssembler, argChainId, argChainIdLength)
            // scriptAssembler
            // txDetail
            .copyString(HexUtil.toHexString("@"), Buffer.CACHE2).setBufferInt(argNameLength, 1, 7)
            .copyArgument(argName, Buffer.CACHE2).showMessage(ScriptData.getDataBufferAll(Buffer.CACHE2))
            .clearBuffer(Buffer.CACHE2)
            // show toAddress
            .copyString(HexUtil.toHexString("0x"), Buffer.CACHE2)
            .baseConvert(argTo, Buffer.CACHE2, 0, ScriptAssembler.hexadecimalCharset, ScriptAssembler.zeroInherit)
            .showAddress(ScriptData.getDataBufferAll(Buffer.CACHE2))
            // show amount
            .setBufferInt(argDecimal, 0, 20).showAmount(argValue, ScriptData.bufInt).showPressButton()
            // version=04 ScriptAssembler.hash=06=ScriptAssembler.Keccak256 sign=01=ECDSA
            .setHeader(HashType.Keccak256, SignType.ECDSA);

        return scriptAssembler.getScript();
    }

    public static String EIP1559ERC20ScriptSignature = Strings.padStart(
        "3045022100e3cf0d0260d1303147e6b10f733dd59836331b412916f991114c67f68d15cbff022025e118aa9fba9684a3a1ee7426451d486bb1a5d4a6405f191df40eb6a5b9125d",
        144, '0');

    public static String getEIP1559SmartContractScript(int coinType) {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptData argTo = sac.getArgument(20);
        ScriptData argValue = sac.getArgumentRightJustified(32);
        ScriptData argGasTipCap = sac.getArgumentRightJustified(10);
        ScriptData argGasFeeCap = sac.getArgumentRightJustified(10);
        ScriptData argGasLimit = sac.getArgumentRightJustified(10);
        ScriptData argNonce = sac.getArgumentRightJustified(8);
        // Chain Information Info
        ScriptData argChainIdLength = sac.getArgument(1);
        ScriptData argChainId = sac.getArgumentVariableLength(6);
        // Variant Data
        ScriptData argData = sac.getArgumentAll();

        ScriptAssembler scriptAssembler = // set coinType to 3C
            new ScriptAssembler().setCoinType(coinType)
                // txType (EIP-2718)
                .copyString("02").arrayPointer()
                // chainId
                .setBufferInt(argChainIdLength, 1, 6).rlpString(argChainId).rlpString(argNonce).rlpString(argGasTipCap)
                .rlpString(argGasFeeCap).rlpString(argGasLimit)
                .ifEqual(argTo, emptyAddress, new ScriptAssembler().copyString("80").getScript(),
                    new ScriptAssembler().copyString("94").copyArgument(argTo).getScript())
                .rlpString(argValue).rlpString(argData)
                // accessList
                .copyString("C0").arrayEnd(TYPE_RLP);

        // txDetail
        return displayChainId(scriptAssembler, argChainId, argChainIdLength).showWrap("SMART", "").showPressButton()
            // version=04 ScriptAssembler.hash=06=ScriptAssembler.Keccak256 sign=01=ECDSA
            .setHeader(HashType.Keccak256, SignType.ECDSA).getScript();
    }

    public static String EIP1559SmartContractScriptSignature = Strings.padStart(
        "3046022100a8d4506590a6c478cc812ee65c868310259be0f3afc9d0ad8e8b27bafc0ac5060221009a81ad218a3a276dbdd92348e7f11079064ec16e05593e836382008b0a084a35",
        144, '0');

    public static String getEIP1559SmartContractSegmentScript(int coinType) {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptData argTo = sac.getArgument(20);
        ScriptData argValue = sac.getArgumentRightJustified(32);
        ScriptData argGasTipCap = sac.getArgumentRightJustified(10);
        ScriptData argGasFeeCap = sac.getArgumentRightJustified(10);
        ScriptData argGasLimit = sac.getArgumentRightJustified(10);
        ScriptData argNonce = sac.getArgumentRightJustified(8);
        ScriptData argData = sac.getArgument(4);
        // Chain Information Info
        ScriptData argChainIdLength = sac.getArgument(1);
        ScriptData argChainId = sac.getArgumentVariableLength(6);

        ScriptAssembler scriptAssembler = new ScriptAssembler()
            // set coinType to 3C
            .setCoinType(coinType)
            // txType (EIP-2718)
            .copyString("02").arrayPointer()
            // chainId
            .setBufferInt(argChainIdLength, 1, 6).rlpString(argChainId).rlpString(argNonce).rlpString(argGasTipCap)
            .rlpString(argGasFeeCap).rlpString(argGasLimit)
            .ifEqual(argTo, emptyAddress, new ScriptAssembler().copyString("80").getScript(),
                new ScriptAssembler().copyString("94").copyArgument(argTo).getScript())
            .rlpString(argValue).rlpDataPlaceholder(argData)
            // accessList
            .copyString("C0").arrayEnd(TYPE_RLP);

        displayChainId(scriptAssembler, argChainId, argChainIdLength).showWrap("SMART", "").showPressButton()
            // version=05 ScriptAssembler.hash=06=ScriptAssembler.Keccak256 sign=01=ECDSA
            .setHeader(HashType.Keccak256, SignType.ECDSA);

        return scriptAssembler.getScript();
    }

    public static String EIP1559SmartContractSegmentScriptSignature = Strings.padStart(
        "3045022100b64d74b36c8509833a379b1a501e31f33a5d8ba435e1ff039b62482bcc74be1d022037f68392a77e161d95191c878afdd94c80e2d203c315d25c922276a0ac49be7a",
        144, '0');
}
