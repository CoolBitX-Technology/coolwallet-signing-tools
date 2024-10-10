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
        // System.out.println("CustomEvm: \n" + getTransferScript(0x3c) + "\n");
        // System.out.println("Evm erc20: \n" + getERC20Script(0x3c) + "\n");
        // System.out.println("Evm Smart Contract: \n" + getSmartContractScript(0x3c) +
        // "\n");
        // System.out.println("Evm Smart Contract Segment: \n" +
        // getSmartContractSegmentScript(0x3c) + "\n");
        // System.out.println("Evm EIP-712 Message: \n" + getMessageScript(0x3c) +
        // "\n");
        // System.out.println("Evm EIP-712 Typed Data: \n" + getTypedDataScript(0x3c) +
        // "\n");
        System.out.println("CustomEvm EIP-1559: \n" + getEIP1559TransferScript(0x3c) + "\n");
        System.out.println("CustomEvm EIP-1559 erc20: \n" + getEIP1559ERC20Script(0x3c) + "\n");
        System.out.println("Evm EIP-1559 Smart Contract: \n" + getEIP1559SmartContractScript(0x3c) + "\n");
        // System.out.println(
        // "Evm EIP-1559 Smart Contract Segment: \n" +
        // getEIP1559SmartContractSegmentScript(0x3c) + "\n");
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

        String script = new ScriptAssembler()
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
                .arrayEnd(TYPE_RLP)
                // display chainId
                // .copyArgument(argChainId, Buffer.CACHE1)
                // .showMessage(ScriptData.getDataBufferAll(Buffer.CACHE1))
                // .clearBuffer(Buffer.CACHE1)
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
                .setHeader(HashType.Keccak256, SignType.ECDSA)
                .getScript();
        return script;
    }

    // TODO: use production signature
    public static String TransferScriptSignature = Strings.padStart(
            "fa0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000",
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
        ScriptData argSymbolLength = sac.getArgument(1);
        ScriptData argSymbol = sac.getArgumentVariableLength(7);
        ScriptData argLayerLength = sac.getArgument(1);
        ScriptData argLayerSymbol = sac.getArgumentVariableLength(7);
        ScriptData argChainSign = sac.getArgument(72);

        // ERC 20 Token Info
        ScriptData argTokenInfo = sac.getArgumentUnion(0, 29);
        ScriptData argDecimal = sac.getArgument(1);
        ScriptData argNameLength = sac.getArgument(1);
        ScriptData argName = sac.getArgumentVariableLength(7);
        ScriptData argContractAddress = sac.getArgument(20);
        ScriptData argTokenSign = sac.getArgument(72);

        String script = new ScriptAssembler()
                // set coinType to 3C
                .setCoinType(coinType)
                .ifSigned(argChainInfo, argChainSign, "", ScriptAssembler.throwSEError)
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
                .arrayEnd(TYPE_RLP)
                // Show symbol
                .ifEqual(
                        argLayerLength,
                        "00",
                        "",
                        new ScriptAssembler()
                                .setBufferInt(argLayerLength, 1, 7)
                                .copyArgument(argLayerSymbol, Buffer.CACHE1)
                                .showMessage(ScriptData.getDataBufferAll(Buffer.CACHE1))
                                .clearBuffer(Buffer.CACHE1)
                                .getScript())
                .setBufferInt(argSymbolLength, 1, 7)
                .copyArgument(argSymbol, Buffer.CACHE1)
                .showMessage(ScriptData.getDataBufferAll(Buffer.CACHE1))
                .clearBuffer(Buffer.CACHE1)
                .ifSigned(
                        argTokenInfo,
                        argTokenSign,
                        "",
                        new ScriptAssembler()
                                .copyString(HexUtil.toHexString("@"), Buffer.CACHE2)
                                .getScript())
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
                .setHeader(HashType.Keccak256, SignType.ECDSA)
                .getScript();
        return script;
    }

    public static String ERC20ScriptSignature = Strings.padStart(
            "304402200B343A6EBBD31DE90495CD482D04677B02A3278AF4F546F0865D86C9071C651D02202F666136FEEB43EE85FC93C47E92AB30FF2C8D268FC9DD3CFB1F3E06FFE1D208",
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
        ScriptData argChainInfo = sac.getArgumentUnion(0, 23);
        ScriptData argChainIdLength = sac.getArgument(1);
        ScriptData argChainId = sac.getArgumentVariableLength(6);
        ScriptData argSymbolLength = sac.getArgument(1);
        ScriptData argSymbol = sac.getArgumentVariableLength(7);
        ScriptData argLayerLength = sac.getArgument(1);
        ScriptData argLayerSymbol = sac.getArgumentVariableLength(7);
        ScriptData argChainSign = sac.getArgument(72);
        ScriptData argData = sac.getArgumentAll();

        String script = new ScriptAssembler()
                // set coinType to 3C
                .setCoinType(coinType)
                .ifSigned(argChainInfo, argChainSign, "", ScriptAssembler.throwSEError)
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
                .arrayEnd(TYPE_RLP)
                // Display phase
                .ifEqual(
                        argLayerLength,
                        "00",
                        "",
                        new ScriptAssembler()
                                .setBufferInt(argLayerLength, 1, 7)
                                .copyArgument(argLayerSymbol, Buffer.CACHE1)
                                .showMessage(ScriptData.getDataBufferAll(Buffer.CACHE1))
                                .clearBuffer(Buffer.CACHE1)
                                .getScript())
                .setBufferInt(argSymbolLength, 1, 7)
                .copyArgument(argSymbol, Buffer.CACHE1)
                .showMessage(ScriptData.getDataBufferAll(Buffer.CACHE1))
                .clearBuffer(Buffer.CACHE1)
                .showWrap("SMART", "")
                .showPressButton()
                // version=05 ScriptAssembler.hash=06=ScriptAssembler.Keccak256
                // sign=01=ECDSA
                .setHeader(HashType.Keccak256, SignType.ECDSA)
                .getScript();
        return script;
    }

    public static String SmartContractScriptSignature = Strings.padStart(
            "30450221008D508FDA3E15A6BF55E681C8960B7DBECB82080C708EC1EC200CA8A37DC6367302203AB71C34F44359FD10508358A2C3953C26D173B06A4876C1DCEEE2657DFC26FF",
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
        ScriptData argSymbolLength = sac.getArgument(1);
        ScriptData argSymbol = sac.getArgumentVariableLength(7);
        ScriptData argLayerLength = sac.getArgument(1);
        ScriptData argLayerSymbol = sac.getArgumentVariableLength(7);
        ScriptData argChainSign = sac.getArgument(72);

        String script = new ScriptAssembler()
                // set coinType to 3C
                .setCoinType(coinType)
                .ifSigned(argChainInfo, argChainSign, "", ScriptAssembler.throwSEError)
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
                .arrayEnd(TYPE_RLP)
                // Display phase
                .ifEqual(
                        argLayerLength,
                        "00",
                        "",
                        new ScriptAssembler()
                                .setBufferInt(argLayerLength, 1, 7)
                                .copyArgument(argLayerSymbol, Buffer.CACHE1)
                                .showMessage(ScriptData.getDataBufferAll(Buffer.CACHE1))
                                .clearBuffer(Buffer.CACHE1)
                                .getScript())
                .setBufferInt(argSymbolLength, 1, 7)
                .copyArgument(argSymbol, Buffer.CACHE1)
                .showMessage(ScriptData.getDataBufferAll(Buffer.CACHE1))
                .clearBuffer(Buffer.CACHE1)
                .showWrap("SMART", "")
                .showPressButton()
                // version=05 ScriptAssembler.hash=06=ScriptAssembler.Keccak256
                // sign=01=ECDSA
                .setHeader(HashType.Keccak256, SignType.ECDSA)
                .getScript();
        return script;
    }

    public static String SmartContractSegmentScriptSignature = Strings.padStart(
            "3046022100CA24B84A65567CDCDD3FB73770804C4551FF332C360A36203442425730EDBC87022100DCB7E67EA5431A0CC050C8AE85D371A7977FC3F828497EA70287C13020593BD9",
            144,
            '0');

    public static String getTypedDataScript(int coinType) {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptData argDomainSeparator = sac.getArgument(32);
        // Chain Information Info
        ScriptData argChainInfo = sac.getArgumentUnion(0, 23);
        ScriptData argChainIdLength = sac.getArgument(1);
        ScriptData argChainId = sac.getArgumentVariableLength(6);
        ScriptData argSymbolLength = sac.getArgument(1);
        ScriptData argSymbol = sac.getArgumentVariableLength(7);
        ScriptData argLayerLength = sac.getArgument(1);
        ScriptData argLayerSymbol = sac.getArgumentVariableLength(7);
        ScriptData argChainSign = sac.getArgument(72);
        ScriptData argMessage = sac.getArgumentAll();

        String script = new ScriptAssembler()
                // set coinType to 3C
                .setCoinType(coinType)
                .ifSigned(argChainInfo, argChainSign, "", ScriptAssembler.throwSEError)
                .copyString("1901")
                .copyArgument(argDomainSeparator)
                .hash(argMessage, Buffer.TRANSACTION, HashType.Keccak256)
                // Display phase
                .ifEqual(
                        argLayerLength,
                        "00",
                        "",
                        new ScriptAssembler()
                                .setBufferInt(argLayerLength, 1, 7)
                                .copyArgument(argLayerSymbol, Buffer.CACHE1)
                                .showMessage(ScriptData.getDataBufferAll(Buffer.CACHE1))
                                .clearBuffer(Buffer.CACHE1)
                                .getScript())
                .setBufferInt(argSymbolLength, 1, 7)
                .copyArgument(argSymbol, Buffer.CACHE1)
                .showMessage(ScriptData.getDataBufferAll(Buffer.CACHE1))
                .clearBuffer(Buffer.CACHE1)
                .showWrap("EIP712", "")
                .showPressButton()
                // version=00 ScriptAssembler.hash=06=ScriptAssembler.Keccak256 sign=01=ECDSA
                .setHeader(HashType.Keccak256, SignType.ECDSA)
                .getScript();
        return script;
    }

    public static String TypedDataScriptSignature = Strings.padStart(
            "3045022100F34BCB5427693483C29047DC6F66C42A9D818B26CA11EC4551E394738DFC3366022035873EB1211D39441CB94798FA20D7773FE8DE121ED5C6EE66E289DA9E992E95",
            144,
            '0');

    public static String getMessageScript(int coinType) {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        // Chain Information Info
        ScriptData argChainInfo = sac.getArgumentUnion(0, 23);
        ScriptData argChainIdLength = sac.getArgument(1);
        ScriptData argChainId = sac.getArgumentVariableLength(6);
        ScriptData argSymbolLength = sac.getArgument(1);
        ScriptData argSymbol = sac.getArgumentVariableLength(7);
        ScriptData argLayerLength = sac.getArgument(1);
        ScriptData argLayerSymbol = sac.getArgumentVariableLength(7);
        ScriptData argChainSign = sac.getArgument(72);
        ScriptData argMessage = sac.getArgumentAll();

        String script = new ScriptAssembler()
                // set coinType to 3C
                .setCoinType(coinType)
                .ifSigned(argChainInfo, argChainSign, "", ScriptAssembler.throwSEError)
                .copyString("19457468657265756D205369676E6564204D6573736167653A0A")
                .copyArgument(argMessage)
                // txDetail
                .ifEqual(
                        argLayerLength,
                        "00",
                        "",
                        new ScriptAssembler()
                                .setBufferInt(argLayerLength, 1, 7)
                                .copyArgument(argLayerSymbol, Buffer.CACHE1)
                                .showMessage(ScriptData.getDataBufferAll(Buffer.CACHE1))
                                .clearBuffer(Buffer.CACHE1)
                                .getScript())
                .setBufferInt(argSymbolLength, 1, 7)
                .copyArgument(argSymbol, Buffer.CACHE1)
                .showMessage(ScriptData.getDataBufferAll(Buffer.CACHE1))
                .clearBuffer(Buffer.CACHE1)
                .showWrap("MESSAGE", "")
                .showPressButton()
                // version=00 ScriptAssembler.hash=06=ScriptAssembler.Keccak256 sign=01=ECDSA
                .setHeader(HashType.Keccak256, SignType.ECDSA)
                .getScript();
        return script;
    }

    public static String MessageScriptSignature = Strings.padStart(
            "30440220423678B5E6EC62692F0702153AC88E67F8799E263537755388DEDD4F8C5170E00220364EDAE205034711C178E8B26A4F5346ADCE92B689A102D32C60BA4D1EE82E37",
            144,
            '0');

    private static ScriptAssembler displayChainId(ScriptAssembler scriptAssembler, ScriptData argChainId) {
        return scriptAssembler
                // display chainId e.g.: c534352 represent Scroll chainId=534352
                .copyString(HexUtil.toHexString("c"), Buffer.CACHE2)
                .baseConvert(
                        argChainId,
                        Buffer.CACHE1, 0,
                        ScriptAssembler.decimalCharset,
                        ScriptAssembler.zeroInherit)
                .copyArgument(ScriptData.getDataBufferAll(Buffer.CACHE1), Buffer.CACHE2)
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
        displayChainId(scriptAssembler, argChainId)
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

    // TODO: use production signature
    public static String EIP1559TransferScriptSignature = Strings.padStart(
            "fa0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000",
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

        displayChainId(scriptAssembler, argChainId)
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
            "fa0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000",
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
        return displayChainId(scriptAssembler, argChainId)
                .showWrap("SMART", "")
                .showPressButton()
                // version=04 ScriptAssembler.hash=06=ScriptAssembler.Keccak256 sign=01=ECDSA
                .setHeader(HashType.Keccak256, SignType.ECDSA)
                .getScript();
    }

    // TODO: use production signature
    public static String EIP1559SmartContractScriptSignature = Strings.padStart(
            "fa0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000",
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
        ScriptData argChainInfo = sac.getArgumentUnion(0, 23);
        ScriptData argChainIdLength = sac.getArgument(1);
        ScriptData argChainId = sac.getArgumentVariableLength(6);
        ScriptData argSymbolLength = sac.getArgument(1);
        ScriptData argSymbol = sac.getArgumentVariableLength(7);
        ScriptData argLayerLength = sac.getArgument(1);
        ScriptData argLayerSymbol = sac.getArgumentVariableLength(7);
        ScriptData argChainSign = sac.getArgument(72);

        String script = new ScriptAssembler()
                // set coinType to 3C
                .setCoinType(coinType)
                .ifSigned(argChainInfo, argChainSign, "", ScriptAssembler.throwSEError)
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
                .arrayEnd(TYPE_RLP)
                .ifEqual(
                        argLayerLength,
                        "00",
                        "",
                        new ScriptAssembler()
                                .setBufferInt(argLayerLength, 1, 7)
                                .copyArgument(argLayerSymbol, Buffer.CACHE1)
                                .showMessage(ScriptData.getDataBufferAll(Buffer.CACHE1))
                                .clearBuffer(Buffer.CACHE1)
                                .getScript())
                .setBufferInt(argSymbolLength, 1, 7)
                .copyArgument(argSymbol, Buffer.CACHE1)
                .showMessage(ScriptData.getDataBufferAll(Buffer.CACHE1))
                .clearBuffer(Buffer.CACHE1)
                .showWrap("SMART", "")
                .showPressButton()
                // version=05 ScriptAssembler.hash=06=ScriptAssembler.Keccak256 sign=01=ECDSA
                .setHeader(HashType.Keccak256, SignType.ECDSA)
                .getScript();
        return script;
    }

    public static String EIP1559SmartContractSegmentScriptSignature = Strings.padStart(
            "30460221009FCD7C75763F56788FE4293FBACE40FC3ED17228BDEFBC43E5D8E72F4D85197E022100CA7B7BF1118B1FE611C9E422F3C4DEE66A375502F1305D9B6F4BDABAE5CDD2B5",
            144,
            '0');
}
