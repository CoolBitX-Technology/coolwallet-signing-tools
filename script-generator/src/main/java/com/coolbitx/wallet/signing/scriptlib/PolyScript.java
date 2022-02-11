/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.coolbitx.wallet.signing.scriptlib;

import com.coolbitx.wallet.signing.utils.HexUtil;
import com.coolbitx.wallet.signing.utils.ScriptArgumentComposer;
import com.coolbitx.wallet.signing.utils.ScriptAssembler;
import com.coolbitx.wallet.signing.utils.ScriptAssembler.HashType;
import com.coolbitx.wallet.signing.utils.ScriptAssembler.SignType;
import com.coolbitx.wallet.signing.utils.ScriptData;
import com.coolbitx.wallet.signing.utils.ScriptData.Buffer;

public class PolyScript {

  public static void listAll() {
    System.out.println("Poly eip1559: \n" + getPolyEIP1559Script() + "\n");
    System.out.println("Poly eip1559 erc20: \n" + getPolyEIP1559ERC20Script() + "\n");
    System.out.println("Poly eip1559 Smart Contract: \n" + getPolyEIP1559SmartScript() + "\n");
    System.out.println("Poly: \n" + getPolyScript() + "\n");
    System.out.println("Poly erc20: \n" + getERC20Script() + "\n");
    System.out.println("Poly Smart Contract: \n" + getPolyContractBlindScript() + "\n");
    System.out.println("Poly Message: \n" + getPolyMessageBlindScript() + "\n");
    System.out.println("Poly TypedData: \n" + getPolyTypedDataBlindScript() + "\n");
  }

  /*
txType(EIP-2718) : 02
rlpLength :        e5
chainId :          89
nonce :            80
maxPriorityFee :   01
maxFee :           81 ff
gasLimit :         84 02625a00
to address :       94 cccccccccccccccccccccccccccccccccccccccc
value :            83 0186a0
data :             80
accessList :       c0
     */
  public static String getPolyEIP1559Script() {
    ScriptArgumentComposer sac = new ScriptArgumentComposer();
    ScriptData argTo = sac.getArgument(20);
    ScriptData argValue = sac.getArgumentRightJustified(10);
    ScriptData argGasTipCap = sac.getArgumentRightJustified(10);
    ScriptData argGasFeeCap = sac.getArgumentRightJustified(10);
    ScriptData argGasLimit = sac.getArgumentRightJustified(10);
    ScriptData argNonce = sac.getArgumentRightJustified(8);

    String script = new ScriptAssembler()
      // set coinType to 3C
      .setCoinType(0x3C)
      // txType (EIP-2718)
      .copyString("02")
      .arrayPointer()
      // chainId
      .copyString("81")
      .copyString("89")
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
      .showMessage("MATIC")
      .copyString(HexUtil.toHexString("0x"), Buffer.CACHE2)
      .baseConvert(argTo, Buffer.CACHE2, 0, ScriptAssembler.hexadecimalCharset, ScriptAssembler.zeroInherit)
      .showAddress(ScriptData.getDataBufferAll(Buffer.CACHE2))
      .showAmount(argValue, 18)
      .showPressButton()
      //version=04 ScriptAssembler.hash=06=ScriptAssembler.Keccak256 sign=01=ECDSA
      .setHeader(HashType.Keccak256, SignType.ECDSA)
      .getScript();
    return script;
  }

  public static String PolyEIP1559ScriptSignature = "";

  /*
txType(EIP-2718) : 02
rlpLength :        f86a
chainId :          89
nonce :            80
maxPriorityFee :   01
maxFee :           81 ff
gasLimit :         84 02625a00
to address :       94 cccccccccccccccccccccccccccccccccccccccc
value :            83 0186a0
data :             b844 a9059cbb
                   0000000000000000000000009e3a6e03298780078003d3965689b8f653ee87b1
                   0000000000000000000000000000000000000000000000004563918244f40000
accessList :       c0
     */
  public static String getPolyEIP1559ERC20Script() {
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

    String script = new ScriptAssembler()
      // set coinType to 3C
      .setCoinType(0x3C)
      // txType (EIP-2718)
      .copyString("02")
      .arrayPointer()
      // chainId
      .copyString("8189")
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
      .showMessage("MATIC")
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
      // version=04 ScriptAssembler.hash=06=ScriptAssembler.Keccak256 sign=01=ECDSA
      .setHeader(HashType.Keccak256, SignType.ECDSA)
      .getScript();
    return script;
  }

  public static String PolyEIP1559ERC20ScriptSignature = "";

  public static String getPolyEIP1559SmartScript() {
    ScriptArgumentComposer sac = new ScriptArgumentComposer();
    ScriptData argTo = sac.getArgument(20);
    ScriptData argValue = sac.getArgumentRightJustified(10);
    ScriptData argGasTipCap = sac.getArgumentRightJustified(10);
    ScriptData argGasFeeCap = sac.getArgumentRightJustified(10);
    ScriptData argGasLimit = sac.getArgumentRightJustified(10);
    ScriptData argNonce = sac.getArgumentRightJustified(8);
    ScriptData argData = sac.getArgument(4);

    String script = new ScriptAssembler()
      // set coinType to 3C
      .setCoinType(0x3C)
      // txType (EIP-2718)
      .copyString("02")
      .arrayPointer()
      // chainId
      .copyString("8189")
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
      .showMessage("MATIC")
      .showWrap("SMART", "")
      .showPressButton()
      // version=05 ScriptAssembler.hash=06=ScriptAssembler.Keccak256 sign=01=ECDSA
      .setHeader(HashType.Keccak256, SignType.ECDSA)
      .getScript();
    return script;
  }

  public static String PolyEIP1559SmartScriptSignature = "";

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
  public static String getPolyScript() {
    ScriptArgumentComposer sac = new ScriptArgumentComposer();
    ScriptData argTo = sac.getArgument(20);
    ScriptData argValue = sac.getArgumentRightJustified(10);
    ScriptData argGasPrice = sac.getArgumentRightJustified(10);
    ScriptData argGasLimit = sac.getArgumentRightJustified(10);
    ScriptData argNonce = sac.getArgumentRightJustified(8);
    // ScriptData argChainId = sac.getArgumentRightJustified(5);

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
      .copyString("89", Buffer.CACHE1)
      .rlpString(ScriptData.getDataBufferAll(Buffer.CACHE1))
      // r,s
      .copyString("8080")
      .arrayEnd(1)
      // .rlpList(1)
      .showMessage("MATIC")
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

  public static String PolyScriptSignature = "";

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
      .arrayPointer()
      // .copyString("F800")
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
      // .rlpString(argChainId)
      .copyString("89", Buffer.CACHE1)
      .rlpString(ScriptData.getDataBufferAll(Buffer.CACHE1))
      // r,s
      .copyString("8080")
      // .rlpList(2)
      .arrayEnd(1)
      .showMessage("MATIC")
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

  public static String ERC20ScriptSignature = "";

  public static String getPolyContractBlindScript() {
    ScriptArgumentComposer sac = new ScriptArgumentComposer();
    ScriptData argTo = sac.getArgument(20);
    ScriptData argValue = sac.getArgumentRightJustified(10);
    ScriptData argGasPrice = sac.getArgumentRightJustified(10);
    ScriptData argGasLimit = sac.getArgumentRightJustified(10);
    ScriptData argNonce = sac.getArgumentRightJustified(8);
    // ScriptData argChainId = sac.getArgumentRightJustified(2);
    ScriptData argData = sac.getArgument(4);

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
      .copyString("94")
      .copyArgument(argTo)
      // value
      .rlpString(argValue)
      // data
      .rlpDataPlaceholder(argData)
      // chainId v
      //.rlpString(argChainId)
      .copyString("89", Buffer.CACHE1)
      .rlpString(ScriptData.getDataBufferAll(Buffer.CACHE1))
      .copyString("8080")
      .arrayEnd(1)
      .showMessage("MATIC")
      .showWrap("SMART", "")
      .showPressButton()
      // version=05 ScriptAssembler.hash=06=ScriptAssembler.Keccak256 sign=01=ECDSA
      .setHeader(HashType.Keccak256, SignType.ECDSA)
      .getScript();
    return script;
  }

  public static String PolyContractBlindScriptSignature = "";

  public static String getPolyMessageBlindScript() {
    ScriptArgumentComposer sac = new ScriptArgumentComposer();
    ScriptData argMessage = sac.getArgumentAll();

    String script = new ScriptAssembler()
      // set coinType to 3C
      .setCoinType(0x3C)
      .copyString("19457468657265756D205369676E6564204D6573736167653A0A")
      .copyArgument(argMessage)
      .showMessage("MATIC")
      .showWrap("MESSAGE", "")
      .showPressButton()
      //version=00 ScriptAssembler.hash=06=ScriptAssembler.Keccak256 sign=01=ECDSA
      .setHeader(HashType.Keccak256, SignType.ECDSA)
      .getScript();
    return script;
  }
  public static String PolyMessageBlindScriptSignature = "";

  public static String getPolyTypedDataBlindScript() {
    ScriptArgumentComposer sac = new ScriptArgumentComposer();
    ScriptData argDomainSeparator = sac.getArgument(32);
    ScriptData argMessage = sac.getArgumentAll();

    String script = new ScriptAssembler()
      // set coinType to 3C   
      .setCoinType(0x3C)
      .copyString("1901")
      .copyArgument(argDomainSeparator)
      .hash(argMessage, Buffer.TRANSACTION, ScriptAssembler.Keccak256)
      .showMessage("MATIC")
      .showWrap("EIP712", "")
      .showPressButton()
      //version=00 ScriptAssembler.hash=06=ScriptAssembler.Keccak256 sign=01=ECDSA
      .setHeader(HashType.Keccak256, SignType.ECDSA)
      .getScript();
    return script;
  }