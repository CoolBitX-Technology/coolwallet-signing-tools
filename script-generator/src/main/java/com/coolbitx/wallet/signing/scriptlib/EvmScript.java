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
import com.coolbitx.wallet.signing.utils.ScriptData;
import com.coolbitx.wallet.signing.utils.ScriptData.Buffer;
import com.google.common.base.Strings;

public class EvmScript {
  public static void listAll() {
    System.out.println("Evm: \n" + getTransferScript() + "\n");
    System.out.println("Evm erc20: \n" + getERC20Script() + "\n");
    System.out.println("Evm Smart Contract: \n" + getSmartContractScript() + "\n");
    System.out.println("Evm Smart Contract Segment: \n" + getSmartContractSegmentScript() + "\n");
    System.out.println("Evm EIP-712 Message: \n" + getMessageScript() + "\n");
    System.out.println("Evm EIP-712 Typed Data: \n" + getTypedDataScript() + "\n");
    System.out.println("Evm EIP-1559: \n" + getEIP1559TransferScript() + "\n");
    System.out.println("Evm EIP-1559 erc20: \n" + getEIP1559ERC20Script() + "\n");
    System.out.println("Evm EIP-1559 Smart Contract: \n" + getEIP1559SmartContractScript() + "\n");
    System.out.println(
        "Evm EIP-1559 Smart Contract Segment: \n" + getEIP1559SmartContractSegmentScript() + "\n");
  }

  /*
   * E7 2A 85 09C74AFE1F 82 5208 94 A3255ECFE3F6727A62D938F4C29B2F73C361B26C 83 989680 80 03
   * 80 80
   */
  public static String getTransferScript() {
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

    String script =
        new ScriptAssembler()
            // set coinType to 3C
            .setCoinType(0x3C)
            .ifSigned(argChainInfo, argChainSign, "", ScriptAssembler.throwSEError)
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
            .arrayEnd(1)
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

  public static String TransferScriptSignature =
      Strings.padStart(
          "304502202952FF45A7A11B07B2C602576991B51F857D2F6850FBA409E0613CAF76A0FDF3022100DB01858EA4802CCC0367AF22F8E102FE91445C975E4E6A9C2C8089C65517A380",
          144,
          '0');

  public static String getERC20Script() {
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

    String script =
        new ScriptAssembler()
            // set coinType to 3C
            .setCoinType(0x3C)
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
            .arrayEnd(1)
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

  public static String ERC20ScriptSignature =
      Strings.padStart(
          "304402200931D82BFBE67B7D3C952E37DD7F7BF4CB75DF4056BDBD59737B05BA76A2314D02207A111E627614014F1D8E0C89D4849A93F86F45B59BCF197B522626BC8F15CEFE",
          144,
          '0');

  public static String getSmartContractScript() {
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

    String script =
        new ScriptAssembler()
            // set coinType to 3C
            .setCoinType(0x3C)
            .ifSigned(argChainInfo, argChainSign, "", ScriptAssembler.throwSEError)
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
            .setBufferInt(argChainIdLength, 1, 6)
            .rlpString(argChainId)
            // r, s
            .copyString("8080")
            .arrayEnd(1)
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

  public static String SmartContractScriptSignature =
      Strings.padStart(
          "304402204383F3A8224DAAD23DD338B79B8FA0E6343937F2439CAD65544CBA13329C7B0A022029B7382255BEDF63B138E2B865A56F608E893BE28AF8C7BDAE8D5AD1C8DFA712",
          144,
          '0');

  public static String getSmartContractSegmentScript() {
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

    String script =
        new ScriptAssembler()
            // set coinType to 3C
            .setCoinType(0x3C)
            .ifSigned(argChainInfo, argChainSign, "", ScriptAssembler.throwSEError)
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
            .rlpDataPlaceholder(argData)
            // chainId v
            .setBufferInt(argChainIdLength, 1, 6)
            .rlpString(argChainId)
            // r, s
            .copyString("8080")
            .arrayEnd(1)
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

  public static String SmartContractSegmentScriptSignature =
      Strings.padStart(
          "30450221009DB1CC0E1E93B95FD96DAB99DEA9565055F3CA017AB5B68C28DDE0C9ABCB46D6022020F478DE39018A6E6400BABB717AA31D6AC3FAA2C76C813F4C0C82651738A12E",
          144,
          '0');

  public static String getTypedDataScript() {
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

    String script =
        new ScriptAssembler()
            // set coinType to 3C
            .setCoinType(0x3C)
            .ifSigned(argChainInfo, argChainSign, "", ScriptAssembler.throwSEError)
            .copyString("1901")
            .copyArgument(argDomainSeparator)
            .hash(argMessage, Buffer.TRANSACTION, ScriptAssembler.Keccak256)
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

  public static String TypedDataScriptSignature =
      Strings.padStart(
          "30440220143FB2C95A93D37E71C6D515BEC791F31CDB877A341309B25C1C758B56FABD95022077E827B37F2B60B1EC59D28017CF91929BDB3D5917FB25D7EC08C4A833B0C001",
          144,
          '0');

  public static String getMessageScript() {
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

    String script =
        new ScriptAssembler()
            // set coinType to 3C
            .setCoinType(0x3C)
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

  public static String MessageScriptSignature =
      Strings.padStart(
          "304402206CA24A735298A41C0BE8A9D32A07C6FD3429A990AA5D5B6EE2A5A82F880E595802207E963B836CA60C2F86F16F828A9B03E0A1F6B2CCD6FD4ED6B882AFA0199BBFFA",
          144,
          '0');

  public static String getEIP1559TransferScript() {
    ScriptArgumentComposer sac = new ScriptArgumentComposer();
    ScriptData argTo = sac.getArgument(20);
    ScriptData argValue = sac.getArgumentRightJustified(10);
    ScriptData argGasTipCap = sac.getArgumentRightJustified(10);
    ScriptData argGasFeeCap = sac.getArgumentRightJustified(10);
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

    String script =
        new ScriptAssembler()
            // set coinType to 3C
            .setCoinType(0x3C)
            .ifSigned(argChainInfo, argChainSign, "", ScriptAssembler.throwSEError)
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
            .arrayEnd(1)
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
            // version=04 ScriptAssembler.hash=06=ScriptAssembler.Keccak256 sign=01=ECDSA
            .setHeader(HashType.Keccak256, SignType.ECDSA)
            .getScript();
    return script;
  }

  public static String EIP1559TransferScriptSignature =
      Strings.padStart(
          "3046022100E25FA7F60387C9F611FDAE2167D37B86CCB38D2A73D1827694212C26D3733EA2022100E7CF618651590869C18F72FEECC473B0D554D7C269EA4EB84A98AD23805EE4BA",
          144,
          '0');

  public static String getEIP1559ERC20Script() {
    ScriptArgumentComposer sac = new ScriptArgumentComposer();
    ScriptData argTo = sac.getArgument(20);
    ScriptData argValue = sac.getArgument(12);
    ScriptData argGasTipCap = sac.getArgumentRightJustified(10);
    ScriptData argGasFeeCap = sac.getArgumentRightJustified(10);
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
    // ERC20 Token Info
    ScriptData argTokenInfo = sac.getArgumentUnion(0, 29);
    ScriptData argDecimal = sac.getArgument(1);
    ScriptData argNameLength = sac.getArgument(1);
    ScriptData argName = sac.getArgumentVariableLength(7);
    ScriptData argContractAddress = sac.getArgument(20);
    ScriptData argSign = sac.getArgument(72);

    String script =
        new ScriptAssembler()
            // set coinType to 3C
            .setCoinType(0x3C)
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
                argSign,
                "",
                new ScriptAssembler()
                    .copyString(HexUtil.toHexString("@"), Buffer.CACHE2)
                    .getScript())
            // txDetail
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
            // version=04 ScriptAssembler.hash=06=ScriptAssembler.Keccak256 sign=01=ECDSA
            .setHeader(HashType.Keccak256, SignType.ECDSA)
            .getScript();
    return script;
  }

  public static String EIP1559ERC20ScriptSignature =
      Strings.padStart(
          "3045022100BBC9E58E4EC05AB9EFE7B6DF2006AA845B2A37FFA1284ADA927FFCE873B5C733022050E00C708D69C94EB8A9B2B281B48CFAFC250891022AAE5E7BE1227DBF1A8F1F",
          144,
          '0');

  public static String getEIP1559SmartContractScript() {
    ScriptArgumentComposer sac = new ScriptArgumentComposer();
    ScriptData argTo = sac.getArgument(20);
    ScriptData argValue = sac.getArgumentRightJustified(10);
    ScriptData argGasTipCap = sac.getArgumentRightJustified(10);
    ScriptData argGasFeeCap = sac.getArgumentRightJustified(10);
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
    // Variant Data
    ScriptData argData = sac.getArgumentAll();

    String script =
        // set coinType to 3C
        new ScriptAssembler()
            .setCoinType(0x3C)
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
            .copyString("94")
            .copyArgument(argTo)
            .rlpString(argValue)
            .rlpString(argData)
            // accessList
            .copyString("C0")
            .arrayEnd(1)
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
            .showWrap("SMART", "")
            .showPressButton()
            // version=04 ScriptAssembler.hash=06=ScriptAssembler.Keccak256 sign=01=ECDSA
            .setHeader(HashType.Keccak256, SignType.ECDSA)
            .getScript();

    return script;
  }

  public static String EIP1559SmartContractScriptSignature =
      Strings.padStart(
          "304402201AF7A6D845B936B146302915A8B185C3F596995E9E02D17CA900391E9B47BEF0022056F1ED2DC0FA5F1CE33BEDF697C364B6B0A0871AFD82F20B08CBC2B69165BCFD",
          144,
          '0');

  public static String getEIP1559SmartContractSegmentScript() {
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

    String script =
        new ScriptAssembler()
            // set coinType to 3C
            .setCoinType(0x3C)
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
            .copyString("94")
            .copyArgument(argTo)
            .rlpString(argValue)
            .rlpDataPlaceholder(argData)
            // accessList
            .copyString("C0")
            .arrayEnd(1)
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

  public static String EIP1559SmartContractSegmentScriptSignature =
      Strings.padStart(
          "3045022100B9DBD9E6ECDC64B198F9CB37F82354CE283837ABB731D49620357C492248EA8702200EB6133B4CE43D65CD47249021A4EF85933292C452341F49F01CE7F2F0AD07D5",
          144,
          '0');
}
