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
    System.out.println("Vet: \n" + getVetTransactionScript() + "\n");
    System.out.println("VIP191 Origin: \n" + getVIP191OriginScript() + "\n");
    System.out.println("VIP191 Delegator: \n" + getVIP191DelegatorScript() + "\n");
    System.out.println("VIP191 Vet Cert: \n" + getVetCertificateScript() + "\n");
  }

  public static String getVetTransactionScript() {

    /*
     * chainTag - 1byte - rlp
     * blockRef - 8 byte - fixed length
     * expiration - 4 byte - rlp
     * clauses - array
     * to - 20 byte - fixed length (nullable)
     * value - 32 byte - rlp
     * data - no mention (blob kind)
     * gasPriceCoef - 1 byte - rlp
     * gas - 8 byte - rlp
     * dependsOn - 32 byte - fixed length (nullable)
     * Nonce 8 byte - rlp
     */
    ScriptArgumentComposer sac = new ScriptArgumentComposer();
    ScriptData argChainTag = sac.getArgumentRightJustified(1);
    ScriptData argBlockRef = sac.getArgumentRightJustified(8);
    ScriptData argExpiration = sac.getArgumentRightJustified(4);
    ScriptData argTo = sac.getArgument(20);
    ScriptData argValue = sac.getArgumentRightJustified(32);
    ScriptData argData = sac.getArgumentRightJustified(68);
    ScriptData argGasPrice = sac.getArgumentRightJustified(1);
    ScriptData argGas = sac.getArgumentRightJustified(8);
    ScriptData argDependsOn = sac.getArgument(32);
    ScriptData argNonce = sac.getArgumentRightJustified(8);

    String script = new ScriptAssembler()
        // set coinType to 0332
        .setCoinType(0x0332)
        .arrayPointer()
        // chainTag
        .rlpString(argChainTag)
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
        .rlpString(argData)
        .arrayEnd(TYPE_RLP)
        .arrayEnd(TYPE_RLP)
        // gas price
        //.copyString("81").copyArgument(argGasPrice)
        .rlpString(argGasPrice)
        // gas
        .rlpString(argGas)
        // dependon
        .ifEqual(
            argDependsOn, "0000000000000000000000000000000000000000000000000000000000000000",
            // ---- output count start ----
            new ScriptAssembler().copyString("80").getScript(),
            new ScriptAssembler().rlpString(argDependsOn).getScript()
        // ---- output count end ----
        )
        // nonce
        .rlpString(argNonce)
        // reserved
        .copyString("c0")
        .arrayEnd(TYPE_RLP)
        .showMessage("VET")
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

  public static String getVIP191OriginScript() {
    /*
     * chainTag - 1byte - rlp
     * blockRef - 8 byte - fixed length
     * expiration - 4 byte - rlp
     * clauses - array
     * to - 20 byte - fixed length (nullable)
     * value - 32 byte - rlp
     * data - no mention (blob kind)
     * gasPriceCoef - 1 byte - rlp
     * gas - 8 byte - rlp
     * dependsOn - 32 byte - fixed length (nullable)
     * Nonce 8 byte - rlp
     * Feature - 1byte- rlp (from reserved) - (buffer kind)
     */
    ScriptArgumentComposer sac = new ScriptArgumentComposer();
    ScriptData argChainTag = sac.getArgumentRightJustified(1);
    ScriptData argBlockRef = sac.getArgumentRightJustified(8);
    ScriptData argExpiration = sac.getArgumentRightJustified(4);
    ScriptData argTo = sac.getArgument(20);
    ScriptData argValue = sac.getArgumentRightJustified(32);
    ScriptData argData = sac.getArgumentRightJustified(4);
    ScriptData argGasPrice = sac.getArgumentRightJustified(1);
    ScriptData argGas = sac.getArgumentRightJustified(8);
    ScriptData argDependsOn = sac.getArgument(32);
    ScriptData argNonce = sac.getArgumentRightJustified(8);
    ScriptData argFeatures = sac.getArgumentRightJustified(1);

    String script = new ScriptAssembler()
        // set coinType to 0332
        .setCoinType(0x0332)
        .arrayPointer()
        // chainTag
        .rlpString(argChainTag)
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
        .rlpString(argData)
        .arrayEnd(TYPE_RLP)
        .arrayEnd(TYPE_RLP)
        // gas price
        .rlpString(argGasPrice)
        // gas
        .rlpString(argGas)
        // dependon
        .ifEqual(
            argDependsOn, "0000000000000000000000000000000000000000000000000000000000000000",
            // ---- output count start ----
            new ScriptAssembler().copyString("80").getScript(),
            new ScriptAssembler().rlpString(argDependsOn).getScript()
        // ---- output count end ----
        )
        // .copyArgument(argDependsOn)
        // .copyString("80")
        // nonce
        .rlpString(argNonce)
        .arrayPointer()
        .rlpString(argFeatures)
        .arrayEnd(TYPE_RLP)
        .arrayEnd(TYPE_RLP)
        .showMessage("VET")
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

  public static String getVIP191DelegatorScript() {
    /*
     * chainTag - 1byte - rlp
     * blockRef - 8 byte - fixed length
     * expiration - 4 byte - rlp
     * clauses - array
     * to - 20 byte - fixed length (nullable)
     * value - 32 byte - rlp
     * data - no mention (blob kind)
     * gasPriceCoef - 1 byte - rlp
     * gas - 8 byte - rlp
     * dependsOn - 32 byte - fixed length (nullable)
     * Nonce 8 byte - rlp
     * Feature  - 1byte- rlp (from reserved) -  (buffer kind)
     */
    ScriptArgumentComposer sac = new ScriptArgumentComposer();
    ScriptData argChainTag = sac.getArgumentRightJustified(1);
    ScriptData argBlockRef = sac.getArgumentRightJustified(8);
    ScriptData argExpiration = sac.getArgumentRightJustified(4);
    ScriptData argTo = sac.getArgument(20);
    ScriptData argValue = sac.getArgumentRightJustified(32);
    ScriptData argData = sac.getArgumentRightJustified(4);
    ScriptData argGasPrice = sac.getArgumentRightJustified(1);
    ScriptData argGas = sac.getArgumentRightJustified(8);
    ScriptData argDependsOn = sac.getArgument(32);
    ScriptData argNonce = sac.getArgumentRightJustified(8);
    ScriptData argFeatures = sac.getArgumentRightJustified(1);
    ScriptData argDelegatorFor = sac.getArgument(20);

    String script = new ScriptAssembler()
        // set coinType to 0332
        .setCoinType(0x0332)
        .arrayPointer()
        // chainTag
        .rlpString(argChainTag)
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
        .rlpString(argData)
        .arrayEnd(TYPE_RLP)
        .arrayEnd(TYPE_RLP)
        // gas price
        .rlpString(argGasPrice)
        // gas
        .rlpString(argGas)
        // dependon
        .ifEqual(
            argDependsOn, "0000000000000000000000000000000000000000000000000000000000000000",
            // ---- output count start ----
            new ScriptAssembler().copyString("80").getScript(),
            new ScriptAssembler().rlpString(argDependsOn).getScript()
        // ---- output count end ----
        )
        // nonce
        .rlpString(argNonce)
        .arrayPointer()
        .rlpString(argFeatures)
        .arrayEnd(TYPE_RLP)
        .arrayEnd(TYPE_RLP)
        .hash(ScriptData.getDataBufferAll(Buffer.TRANSACTION), Buffer.CACHE2,
            ScriptAssembler.HashType.Blake2b256)
        .clearBuffer(Buffer.TRANSACTION)
        .copyArgument(ScriptData.getDataBufferAll(Buffer.CACHE2),
            Buffer.TRANSACTION)
        .copyArgument(argDelegatorFor)
        .showMessage("VET")
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

  public static String getVetCertificateScript() {
    ScriptArgumentComposer sac = new ScriptArgumentComposer();
    ScriptData argMessage = sac.getArgumentAll();

    String script = new ScriptAssembler()
        // set coinType to 3C
        .setCoinType(0x0332)
        .copyArgument(argMessage)
        // txDetail
        .showMessage("VET")
        .showWrap("MESSAGE", "")
        .showPressButton()
        // version=00, hash=0E, sign=01
        .setHeader(HashType.Blake2b256, SignType.ECDSA)
        .getScript();
    return script;
  }

}
