/*
 * To change this license header, choose License Headers in Project Properties. To change this
 * template file, choose Tools | Templates and open the template in the editor.
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
     * 989680 80 03
     * 80 80
     */
    public static String getTransferScript(int coinType) {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptData argTo = sac.getArgument(20);
        ScriptData argValue = sac.getArgumentRightJustified(10);
        ScriptData argGasPrice = sac.getArgumentRightJustified(10);
        ScriptData argGasLimit = sac.getArgumentRightJustified(10);
        ScriptData argNonce = sac.getArgumentRightJustified(8);
        // Chain Information Info
        ScriptData argChainIdLength = sac.getArgument(1);
        ScriptData argChainId = sac.getArgumentVariableLength(6);

        ScriptAssembler scriptAssembler = new ScriptAssembler()
                // set coinType to 3C
                .setCoinType(coinType)
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
                .setBufferInt(argChainIdLength, 1, 6)
                .rlpString(argChainId)
                // r,s
                .copyString("8080")
                .arrayEnd(TYPE_RLP);

        // tx detail
        displayChainId(scriptAssembler, argChainId, argChainIdLength)
                .copyString(HexUtil.toHexString("0x"), Buffer.CACHE2)
                .baseConvert(
                        argTo,
                        Buffer.CACHE2,
                        0,
                        ScriptAssembler.hexadecimalCharset,
                        ScriptAssembler.zeroInherit)
                .showAddress(ScriptData.getDataBufferAll(Buffer.CACHE2))
                .showAmount(argValue, 18)
                .showPressButton()
                // version=00 ScriptAssembler.hash=06=ScriptAssembler.Keccak256
                // sign=01=ECDSA
                .setHeader(HashType.Keccak256, SignType.ECDSA);
        return scriptAssembler.getScript();
    }

    public static String TransferScriptSignature = Strings.padStart(
            "30450220139187cbe56bd0338fe28b8afeea6880fe0e4a2df26436be074eb527c95f85170221008373a3ee87178a4bce4caedeee955fcfead319d5e951552580f0ef486a0d8671",
            144,
            '0');

    public static String getERC20Script(int coinType) {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptData argTo = sac.getArgument(20);
        ScriptData argValue = sac.getArgument(12);
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
                .setCoinType(coinType)
                .arrayPointer()
                .rlpString(argNonce)
                .rlpString(argGasPrice)
                .rlpString(argGasLimit)
                // address
                .copyString("94")
                .copyArgument(argContractAddress)
                // data, Length = 68
                .copyString("80B844a9059cbb000000000000000000000000")
                .copyArgument(argTo)
                .copyString("0000000000000000000000000000000000000000")
                .copyArgument(argValue)
                // chainId v
                .setBufferInt(argChainIdLength, 1, 6)
                .rlpString(argChainId)
                // empty r,s
                .copyString("8080")
                .arrayEnd(TYPE_RLP);

        // tx detail
        displayChainId(scriptAssembler, argChainId, argChainIdLength)
                .copyString(HexUtil.toHexString("@"), Buffer.CACHE2)
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
                        ScriptAssembler.zeroInherit)
                .showAddress(ScriptData.getDataBufferAll(Buffer.CACHE2))
                .setBufferInt(argDecimal, 0, 20)
                .showAmount(argValue, ScriptData.bufInt)
                .showPressButton()
                // version=00 ScriptAssembler.hash=06=ScriptAssembler.Keccak256
                // sign=01=ECDSA
                .setHeader(HashType.Keccak256, SignType.ECDSA);
        return scriptAssembler.getScript();
    }

    public static String ERC20ScriptSignature = Strings.padStart(
            "3045022100c36a14ef60bea98590119a621c84fc5962f0e232c5c675a66667617be6403cb2022056cdb47788be550ec25f05474672cdaf115ceffba2a13fb31ab35bd8d7c14c44",
            144,
            '0');

    private static final String emptyAddress = "0000000000000000000000000000000000000000";

    public static String getSmartContractScript(int coinType) {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptData argTo = sac.getArgument(20);
        ScriptData argValue = sac.getArgumentRightJustified(10);
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
                .setCoinType(coinType)
                .arrayPointer()
                // nonce
                .rlpString(argNonce)
                // gasPrice
                .rlpString(argGasPrice)
                // gasLimit
                .rlpString(argGasLimit)
                // toAddress
                .ifEqual(argTo, emptyAddress,
                        new ScriptAssembler()
                                .copyString("80")
                                .getScript(),
                        new ScriptAssembler()
                                .copyString("94")
                                .copyArgument(argTo)
                                .getScript())
                // value
                .rlpString(argValue)
                // data
                .rlpString(argData)
                // chainId v
                .setBufferInt(argChainIdLength, 1, 6)
                .rlpString(argChainId)
                // r, s
                .copyString("8080")
                .arrayEnd(TYPE_RLP);

        // Display phase
        displayChainId(scriptAssembler, argChainId, argChainIdLength)
                .showWrap("SMART", "")
                .showPressButton()
                // version=05 ScriptAssembler.hash=06=ScriptAssembler.Keccak256
                // sign=01=ECDSA
                .setHeader(HashType.Keccak256, SignType.ECDSA);

        return scriptAssembler.getScript();
    }

    public static String SmartContractScriptSignature = Strings.padStart(
            "30450221008bd26f2ae80c316b678433e8e9fa4a1e04d6d87dd1a7be33f7f68867df0c30dd0220708aa67f90928e100ec03c4d350e5c71f96430493d7c86c04171cc105b91811c",
            144,
            '0');

    public static String getSmartContractSegmentScript(int coinType) {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptData argTo = sac.getArgument(20);
        ScriptData argValue = sac.getArgumentRightJustified(10);
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
                .setCoinType(coinType)
                .arrayPointer()
                // nonce
                .rlpString(argNonce)
                // gasPrice
                .rlpString(argGasPrice)
                // gasLimit
                .rlpString(argGasLimit)
                // toAddress
                .ifEqual(argTo, emptyAddress,
                        new ScriptAssembler()
                                .copyString("80")
                                .getScript(),
                        new ScriptAssembler()
                                .copyString("94")
                                .copyArgument(argTo)
                                .getScript())
                // value
                .rlpString(argValue)
                // data
                .rlpDataPlaceholder(argData)
                // chainId v
                .setBufferInt(argChainIdLength, 1, 6)
                .rlpString(argChainId)
                // r, s
                .copyString("8080")
                .arrayEnd(TYPE_RLP);

        // Display phase
        displayChainId(scriptAssembler, argChainId, argChainIdLength)
                .showWrap("SMART", "")
                .showPressButton()
                // version=05 ScriptAssembler.hash=06=ScriptAssembler.Keccak256
                // sign=01=ECDSA
                .setHeader(HashType.Keccak256, SignType.ECDSA);
        return scriptAssembler.getScript();
    }

    public static String SmartContractSegmentScriptSignature = Strings.padStart(
            "304502203ec212a7a74ddecfd09b1992ebb6244a3a7643ea5a9326926de91d9a499b0938022100d37fe100a50cc51537d8ba910d697ba660c75f239ef9190fa7493b28a5d4183e",
            144,
            '0');

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
                .setCoinType(coinType)
                .copyString("1901")
                .copyArgument(argDomainSeparator)
                .hash(argMessage, Buffer.TRANSACTION, HashType.Keccak256);

        // Display phase
        displayChainId(scriptAssembler, argChainId, argChainIdLength)
                .showWrap("EIP712", "")
                .showPressButton()
                // version=00 ScriptAssembler.hash=06=ScriptAssembler.Keccak256 sign=01=ECDSA
                .setHeader(HashType.Keccak256, SignType.ECDSA);

        return scriptAssembler.getScript();
    }

    public static String TypedDataScriptSignature = Strings.padStart(
            "3046022100edb220ef5f538f9a581c91c81502d369322d03d4eeac29e5926438827a1a14dc02210098c9930f917ed6fb72215a3177733d6f9ede7ea55d1117644b0a5cca581f6737",
            144,
            '0');

    public static String getMessageScript(int coinType) {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        // Chain Information Info
        ScriptData argChainIdLength = sac.getArgument(1);
        ScriptData argChainId = sac.getArgumentVariableLength(6);
        ScriptData argMessage = sac.getArgumentAll();

        ScriptAssembler scriptAssembler = new ScriptAssembler()
                // set coinType to 3C
                .setCoinType(coinType)
                .copyString("19457468657265756D205369676E6564204D6573736167653A0A")
                .copyArgument(argMessage);

        // txDetail
        displayChainId(scriptAssembler, argChainId, argChainIdLength)
                .showWrap("MESSAGE", "")
                .showPressButton()
                // version=00 ScriptAssembler.hash=06=ScriptAssembler.Keccak256 sign=01=ECDSA
                .setHeader(HashType.Keccak256, SignType.ECDSA);
        return scriptAssembler.getScript();
    }

    public static String MessageScriptSignature = Strings.padStart(
            "304502200784b5392cceb1fea5f077d90125cd153d871e164c77aefa31ef4b94690f1fd00221009f77e5af2ee01a8c3b484cc0e3cdac77d984ca24664c336142bdf87d70ba7549",
            144,
            '0');

    private static ScriptAssembler displayChainId(ScriptAssembler scriptAssembler, ScriptData argChainId,
            ScriptData argChainIdLength) {
        return scriptAssembler
                // display chainId e.g.: c534352 represent Scroll chainId=534352

                // put prefix "c"
                .copyString(HexUtil.toHexString("c"), Buffer.CACHE2)

                // convert chainId to chars and concat to CACHE2
                .setBufferInt(argChainIdLength, 1, 6)
                .copyArgument(argChainId, Buffer.CACHE1)
                .baseConvert(
                        ScriptData.getDataBufferAll(Buffer.CACHE1),
                        Buffer.CACHE2, 0,
                        ScriptAssembler.decimalCharset,
                        ScriptAssembler.zeroInherit)

                .showMessage(ScriptData.getDataBufferAll(Buffer.CACHE2))
                .clearBuffer(Buffer.CACHE1)
                .clearBuffer(Buffer.CACHE2);
    }

    public static String getEIP1559TransferScript(int coinType) {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptData argTo = sac.getArgument(20);
        ScriptData argValue = sac.getArgumentRightJustified(10);
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
                .copyString("02")
                .arrayPointer()
                // chainId v
                .setBufferInt(argChainIdLength, 1, 6)
                .rlpString(argChainId)
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
                .arrayEnd(TYPE_RLP);

        // txDetail
        // display chainId
        displayChainId(scriptAssembler, argChainId, argChainIdLength)
                // display toAddress
                .copyString(HexUtil.toHexString("0x"), Buffer.CACHE2)
                .baseConvert(
                        argTo,
                        Buffer.CACHE2,
                        0,
                        ScriptAssembler.hexadecimalCharset,
                        ScriptAssembler.zeroInherit)
                .showAddress(ScriptData.getDataBufferAll(Buffer.CACHE2))
                // display amount
                .showAmount(argValue, 18)
                .showPressButton()
                // version=04 ScriptAssembler.hash=06=ScriptAssembler.Keccak256 sign=01=ECDSA
                .setHeader(HashType.Keccak256, SignType.ECDSA);

        return scriptAssembler.getScript();
    }

    public static String EIP1559TransferScriptSignature = Strings.padStart(
            "3044022075004ded6e769a341ddce52c62bcad825c7209486ebe86cbf4cd9748e12f9fb202203e7d925119744262931ad3c56cb6cc35f3588aaccf8d13cb71ac8f229dbdaed5",
            144,
            '0');

    public static String getEIP1559ERC20Script(int coinType) {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptData argTo = sac.getArgument(20);
        ScriptData argValue = sac.getArgument(12);
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
                .copyString("02")
                .arrayPointer()
                // chainId
                .setBufferInt(argChainIdLength, 1, 6)
                .rlpString(argChainId)
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
                .arrayEnd(TYPE_RLP);

        displayChainId(scriptAssembler, argChainId, argChainIdLength)
                // scriptAssembler
                // txDetail
                .copyString(HexUtil.toHexString("@"), Buffer.CACHE2)
                .setBufferInt(argNameLength, 1, 7)
                .copyArgument(argName, Buffer.CACHE2)
                .showMessage(ScriptData.getDataBufferAll(Buffer.CACHE2))
                .clearBuffer(Buffer.CACHE2)
                // show toAddress
                .copyString(HexUtil.toHexString("0x"), Buffer.CACHE2)
                .baseConvert(
                        argTo,
                        Buffer.CACHE2,
                        0,
                        ScriptAssembler.hexadecimalCharset,
                        ScriptAssembler.zeroInherit)
                .showAddress(ScriptData.getDataBufferAll(Buffer.CACHE2))
                // show amount
                .setBufferInt(argDecimal, 0, 20)
                .showAmount(argValue, ScriptData.bufInt)
                .showPressButton()
                // version=04 ScriptAssembler.hash=06=ScriptAssembler.Keccak256 sign=01=ECDSA
                .setHeader(HashType.Keccak256, SignType.ECDSA);

        return scriptAssembler.getScript();
    }

    public static String EIP1559ERC20ScriptSignature = Strings.padStart(
            "3044022034f24075d205fe20041ce69de2df6f4cbb0ba25461a8074475fa72b33e9b6e7f0220553c5439eeda1ecfe4ef416174fb43d3a894557301fcb6b45be20389cac7e2fb",
            144,
            '0');

    public static String getEIP1559SmartContractScript(int coinType) {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptData argTo = sac.getArgument(20);
        ScriptData argValue = sac.getArgumentRightJustified(10);
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
                new ScriptAssembler()
                        .setCoinType(coinType)
                        // txType (EIP-2718)
                        .copyString("02")
                        .arrayPointer()
                        // chainId
                        .setBufferInt(argChainIdLength, 1, 6)
                        .rlpString(argChainId)
                        .rlpString(argNonce)
                        .rlpString(argGasTipCap)
                        .rlpString(argGasFeeCap)
                        .rlpString(argGasLimit)
                        .ifEqual(argTo, emptyAddress,
                                new ScriptAssembler()
                                        .copyString("80")
                                        .getScript(),
                                new ScriptAssembler()
                                        .copyString("94")
                                        .copyArgument(argTo)
                                        .getScript())
                        .rlpString(argValue)
                        .rlpString(argData)
                        // accessList
                        .copyString("C0")
                        .arrayEnd(TYPE_RLP);

        // txDetail
        return displayChainId(scriptAssembler, argChainId, argChainIdLength)
                .showWrap("SMART", "")
                .showPressButton()
                // version=04 ScriptAssembler.hash=06=ScriptAssembler.Keccak256 sign=01=ECDSA
                .setHeader(HashType.Keccak256, SignType.ECDSA)
                .getScript();
    }

    public static String EIP1559SmartContractScriptSignature = Strings.padStart(
            "30450221009b806140d5c17c6286e2101c4d841e97d6f1b3226d73a545beefb645b6c16c1702203450279abcb613c5ef6ac49be9217227fd555011a444b61871694951601ff8fc",
            144,
            '0');

    public static String getEIP1559SmartContractSegmentScript(int coinType) {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptData argTo = sac.getArgument(20);
        ScriptData argValue = sac.getArgumentRightJustified(10);
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
                .copyString("02")
                .arrayPointer()
                // chainId
                .setBufferInt(argChainIdLength, 1, 6)
                .rlpString(argChainId)
                .rlpString(argNonce)
                .rlpString(argGasTipCap)
                .rlpString(argGasFeeCap)
                .rlpString(argGasLimit)
                .ifEqual(argTo, emptyAddress,
                        new ScriptAssembler()
                                .copyString("80")
                                .getScript(),
                        new ScriptAssembler()
                                .copyString("94")
                                .copyArgument(argTo)
                                .getScript())
                .rlpString(argValue)
                .rlpDataPlaceholder(argData)
                // accessList
                .copyString("C0")
                .arrayEnd(TYPE_RLP);

        displayChainId(scriptAssembler, argChainId, argChainIdLength)
                .showWrap("SMART", "")
                .showPressButton()
                // version=05 ScriptAssembler.hash=06=ScriptAssembler.Keccak256 sign=01=ECDSA
                .setHeader(HashType.Keccak256, SignType.ECDSA);

        return scriptAssembler.getScript();
    }

    public static String EIP1559SmartContractSegmentScriptSignature = Strings.padStart(
            "304502201c7513a62629aa7442362004e5bd277a37d539b5dba0517eed10803b5e5ae1d7022100c0978de5e959bfa1fbe13924588c5bb5565b5ec3bc99b7d6648c0414e323c373",
            144,
            '0');
}
