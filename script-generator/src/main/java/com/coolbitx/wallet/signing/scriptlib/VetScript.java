package com.coolbitx.wallet.signing.scriptlib;

import com.coolbitx.wallet.signing.utils.ScriptArgumentComposer;
import com.coolbitx.wallet.signing.utils.ScriptAssembler;
import com.coolbitx.wallet.signing.utils.ScriptData;
import com.coolbitx.wallet.signing.utils.ScriptAssembler.HashType;
import com.coolbitx.wallet.signing.utils.ScriptAssembler.SignType;
import static com.coolbitx.wallet.signing.utils.ScriptAssembler.TYPE_RLP;

public class VetScript {
  
  public static void listAll() {
    System.out.println("Vet: \n" + getVetScript() + "\n");
  }

  public static String getVetScript() {
    
    /*
     * chainTag - 1byte - rlp
     * blockRef - 8 byte - fixed length
     * expiration - 4 byte - rlp
     * clauses - array
     *  to - 20 byte - fixed length (nullable)
     *  value - 32 byte - rlp
     *  data - no mention (blob kind)
     * gasPriceCoef - 1 byte - rlp
     * gas - 8 byte - rlp
     * dependsOn - 32 byte - fixed length (nullable)
     * Nonce 8 byte - rlp
     * Reserved - no mention (buffer kind)
     */
    ScriptArgumentComposer sac = new ScriptArgumentComposer();
    ScriptData argChainTag = sac.getArgumentRightJustified(1);
    ScriptData argBlockRef = sac.getArgument(8);
    ScriptData argExpiration = sac.getArgumentRightJustified(4);
    ScriptData argTo = sac.getArgument(20);
    ScriptData argValue = sac.getArgumentRightJustified(32);
    ScriptData argData = sac.getArgumentAll();
    ScriptData argGasPrice = sac.getArgumentRightJustified(1);
    ScriptData argGas = sac.getArgumentRightJustified(8);
    ScriptData argDependsOn = sac.getArgument(32);
    ScriptData argNonce = sac.getArgumentRightJustified(8);
    ScriptData argReserved = sac.getArgumentAll();

    String script = new ScriptAssembler()
            // set coinType to 0332
            .setCoinType(0x0332)
            // chainTag
            .rlpString(argChainTag)
            // chainTag
            .copyArgument(argBlockRef)
            // chainTag
            .rlpString(argExpiration)
            // array of clauses
            .arrayPointer()
            // to
            .copyArgument(argTo)
            // value
            .rlpString(argValue)
            // data
            .copyArgument(argData)
            .arrayEnd()
            // gas price
            .rlpString(argGasPrice)
            // gas
            .rlpString(argGas)
            // dependon
            .copyArgument(argDependsOn)
            // nonce
            .rlpString(argNonce)
            // reserved
            .copyArgument(argReserved)
            .showMessage("VET")
            .showAmount(argValue, ScriptData.bufInt)
            .showPressButton()
            // version=00, hash=0E, sign=01
            .setHeader(HashType.Blake2b256, SignType.ECDSA)
            .getScript();

    return script;
  }

}
