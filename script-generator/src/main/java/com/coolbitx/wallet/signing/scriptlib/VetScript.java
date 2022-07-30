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
     * Reserved - no mention (buffer kind)
     */
    ScriptArgumentComposer sac = new ScriptArgumentComposer();
    ScriptData argChainTag = sac.getArgumentRightJustified(1);
    ScriptData argBlockRef = sac.getArgumentRightJustified(8);
    ScriptData argExpiration = sac.getArgumentRightJustified(4);
    ScriptData argTo = sac.getArgument(20);
    ScriptData argValue = sac.getArgumentRightJustified(32);
    ScriptData argData = sac.getArgument(6);
    ScriptData argGasPrice = sac.getArgumentRightJustified(1);
    ScriptData argGas = sac.getArgumentRightJustified(8);
    ScriptData argDependsOn = sac.getArgument(32);
    ScriptData argNonce = sac.getArgumentRightJustified(8);

    String script = new ScriptAssembler()
        // set coinType to 0332
        .setCoinType(0x0332)
        // .copyString("F800")
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
        .copyString("81").copyArgument(argGasPrice)
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
        // r,s
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

}
