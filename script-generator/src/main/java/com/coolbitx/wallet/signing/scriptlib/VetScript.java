package com.coolbitx.wallet.signing.scriptlib;

import com.coolbitx.wallet.signing.utils.ScriptArgumentComposer;
import com.coolbitx.wallet.signing.utils.ScriptAssembler;
import com.coolbitx.wallet.signing.utils.ScriptData;
import com.coolbitx.wallet.signing.utils.ScriptAssembler.HashType;
import com.coolbitx.wallet.signing.utils.ScriptAssembler.SignType;
import com.coolbitx.wallet.signing.utils.ScriptData.Buffer;

import static com.coolbitx.wallet.signing.utils.ScriptAssembler.TYPE_RLP;

import com.coolbitx.wallet.signing.utils.HexUtil;

public class VetScript {
  
  public static void listAll() {
    System.out.println("VET Transaction: \n" + getTransactionScript() + "\n");
    System.out.println("VET Token: \n" + getTokenScript() + "\n");
    System.out.println("VET Certificate: \n" + getCertificateScript() + "\n");
  }

  public static String getTransactionScript() {

    /*
     * blockRef - 8 byte - fixed length
     * expiration - 4 byte - rlp
     * clauses - array
     * to - 20 byte - fixed length (nullable)
     * value - 32 byte - rlp
     * data - variant length
     * gasPriceCoef - 1 byte - rlp
     * gas - 8 byte - rlp
     * dependsOn - 32 byte - fixed length (nullable)
     * Nonce 8 byte - rlp
     */
    ScriptArgumentComposer sac = new ScriptArgumentComposer();
    ScriptData argBlockRef = sac.getArgumentRightJustified(8);
    ScriptData argExpiration = sac.getArgumentRightJustified(4);
    ScriptData argTo = sac.getArgument(20);
    ScriptData argValue = sac.getArgumentRightJustified(32);
    ScriptData argGasPrice = sac.getArgument(1);
    ScriptData argGas = sac.getArgumentRightJustified(8);
    ScriptData argDependsOn = sac.getArgumentRightJustified(32);
    ScriptData argNonce = sac.getArgumentRightJustified(8);
    ScriptData argIsVIP191 = sac.getArgument(1);
    ScriptData argIsSmart = sac.getArgument(1);
    ScriptData argData = sac.getArgumentAll();

    String script = new ScriptAssembler()
        // set coinType to 0332
        .setCoinType(0x0332)
        .arrayPointer()
        // chainTag
        .copyString("4a")
        // blockRef
        .rlpString(argBlockRef)
        // expiration
        .rlpString(argExpiration)
        // array of clauses
        .arrayPointer()
        .arrayPointer()
        // to
        .copyString("94").copyArgument(argTo)
        // value
        .rlpString(argValue)
        // data
        .ifEqual(argIsSmart, "01",
                new ScriptAssembler().rlpString(argData).getScript(),
                new ScriptAssembler().copyString("80").getScript()
        )
        .arrayEnd(TYPE_RLP)
        .arrayEnd(TYPE_RLP)
        // gas price
        .rlpString(argGasPrice)
        // gas
        .rlpString(argGas)
        // dependon
        .rlpString(argDependsOn)
        // nonce
        .rlpString(argNonce)
        // reserved
        .ifEqual(argIsVIP191, "01",
                new ScriptAssembler().copyString("c101").getScript(),
                new ScriptAssembler().copyString("c0").getScript()
        )
        .arrayEnd(TYPE_RLP)

        // txDetail

        .ifEqual(argIsVIP191, "01",
                new ScriptAssembler().showWrap("VET", "VIP191").getScript(),
                new ScriptAssembler().showWrap("VET", "").getScript()
        )
        .ifEqual(argIsSmart, "01",
                new ScriptAssembler().showWrap("SMART", "").getScript(),
                ""
        )
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
        // version=00, hash=0E, sign=01
        .setHeader(HashType.Blake2b256, SignType.ECDSA)
        .getScript();

    return script;
  }

  public static String getTokenScript() {

    ScriptArgumentComposer sac = new ScriptArgumentComposer();
    ScriptData argBlockRef = sac.getArgumentRightJustified(8);
    ScriptData argExpiration = sac.getArgumentRightJustified(4);
    ScriptData argTo = sac.getArgument(20);
    ScriptData argValue = sac.getArgument(32);
    ScriptData argGasPrice = sac.getArgument(1);
    ScriptData argGas = sac.getArgumentRightJustified(8);
    ScriptData argDependsOn = sac.getArgumentRightJustified(32);
    ScriptData argNonce = sac.getArgumentRightJustified(8);
    ScriptData argIsVIP191 = sac.getArgument(1);

    // ERC 20 Token Info
    ScriptData argTokenInfo = sac.getArgumentUnion(0, 29);
    ScriptData argDecimal = sac.getArgument(1);
    ScriptData argNameLength = sac.getArgument(1);
    ScriptData argName = sac.getArgumentVariableLength(7);
    ScriptData argContractAddress = sac.getArgument(20);
    ScriptData argTokenSign = sac.getArgument(72);

    String script = new ScriptAssembler()
        // set coinType to 0332
        .setCoinType(0x0332)
        .arrayPointer()
        // chainTag
        .copyString("4a")
        // blockRef
        .rlpString(argBlockRef)
        // expiration
        .rlpString(argExpiration)
        // array of clauses
        .arrayPointer()
        .arrayPointer()
        // to
        .copyString("94").copyArgument(argContractAddress)
        // value and data
        .copyString("80B844a9059cbb000000000000000000000000")
        .copyArgument(argTo)
        .copyArgument(argValue)
        .arrayEnd(TYPE_RLP)
        .arrayEnd(TYPE_RLP)
        // gas price
        .rlpString(argGasPrice)
        // gas
        .rlpString(argGas)
        // dependon
        .rlpString(argDependsOn)
        // nonce
        .rlpString(argNonce)
        // reserved
        .ifEqual(argIsVIP191, "01",
                new ScriptAssembler().copyString("c101").getScript(),
                new ScriptAssembler().copyString("c0").getScript()
        )
        .arrayEnd(TYPE_RLP)

        // txDetail

        .ifEqual(argIsVIP191, "01",
                new ScriptAssembler().showWrap("VET", "VIP191").getScript(),
                new ScriptAssembler().showWrap("VET", "").getScript()
        )
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
        // version=00, hash=0E, sign=01
        .setHeader(HashType.Blake2b256, SignType.ECDSA)
        .getScript();

    return script;
  }

  public static String getCertificateScript() {
    ScriptArgumentComposer sac = new ScriptArgumentComposer();
    ScriptData argMessage = sac.getArgumentAll();

    String script = new ScriptAssembler()
        // set coinType to 3C
        .setCoinType(0x0332)
        .copyArgument(argMessage)
        // txDetail
        .showMessage("VET")
        .showWrap("CERT", "")
        .showPressButton()
        // version=00, hash=0E, sign=01
        .setHeader(HashType.Blake2b256, SignType.ECDSA)
        .getScript();
    return script;
  }
}
