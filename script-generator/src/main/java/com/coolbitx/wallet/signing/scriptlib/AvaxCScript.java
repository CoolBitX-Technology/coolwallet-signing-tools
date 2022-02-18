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
    }

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
                .copyString("C0")
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
                .copyString("43113", ScriptData.Buffer.CACHE1)
                .rlpString(ScriptData.getDataBufferAll(ScriptData.Buffer.CACHE1))
                // r,s
                .copyString("8080")
                .rlpList(1)
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
}
