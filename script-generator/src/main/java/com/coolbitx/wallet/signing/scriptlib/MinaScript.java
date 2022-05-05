package com.coolbitx.wallet.signing.scriptlib;

import com.coolbitx.wallet.signing.utils.ScriptArgumentComposer;
import com.coolbitx.wallet.signing.utils.ScriptAssembler;
import com.coolbitx.wallet.signing.utils.ScriptData;
import com.coolbitx.wallet.signing.utils.ScriptAssembler.HashType;
import com.coolbitx.wallet.signing.utils.ScriptAssembler.SignType;
import com.coolbitx.wallet.signing.utils.ScriptData.Buffer;

public class MinaScript {


    public enum MinaTxType {
        SEND, DELEGATE
    }

    public static void listAll() {
        // https://github.com/jspada/mina-signer/blob/main/src/lib.rs
        int mainNet = 0;
        int testNet = 1;

        System.out.println("MINA Send : \n" + getMinaScript(MinaTxType.SEND, testNet) + "\n");
        System.out.println("MINA Delegate: \n" + getMinaScript(MinaTxType.DELEGATE, testNet) + "\n");
    }
    

    public static String getMinaScript(MinaTxType type, int networkId) {
        //https://github.com/jspada/mina-signer/blob/main/tests/tests.rs
        //https://github.com/LeastAuthority/Mina-Signer-SDK/blob/master/example/lib/test/tests.dart


        //https://github.com/MinaProtocol/mina/blob/develop/rfcs/0038-rosetta-construction-api.md
        // Taken from Client-SDK code
        // type stakeDelegation = {
        //     to_: publicKey,
        //     from: publicKey,
        //     fee: uint64,
        //     nonce: uint32,
        //     memo: option(string),
        //     validUntil: option(uint32),
        //   };
        
        //   type payment = {
        //     to_: publicKey,
        //     from: publicKey,
        //     fee: uint64,
        //     amount: uint64,
        //     nonce: uint32,
        //     memo: option(string),
        //     validUntil: option(uint32),
        //   };

        // https://github.com/MinaProtocol/c-reference-signer/blob/master/unit_tests.c
        int[] tag = {0,0,0};

        if(type == MinaTxType.DELEGATE){
            tag[2] = 1;
        }

        ScriptArgumentComposer sac = new ScriptArgumentComposer();

        ScriptData argTo = sac.getArgument(55);
        ScriptData argFrom = sac.getArgument(55);
        ScriptData argAmount = sac.getArgument(10);
        ScriptData argFee = sac.getArgumentRightJustified(10);
        ScriptData argNonce = sac.getArgumentRightJustified(8);
        ScriptData argMemo = sac.getArgumentAll();
        ScriptData argValidUntil = sac.getArgumentRightJustified(10);


        ScriptAssembler scriptMina = new ScriptAssembler();

        String script = scriptMina
                .setCoinType(0x312a)
                //to
                .copyArgument(argTo)
                //from
                .copyArgument(argFrom)
                //fee
                .copyArgument(argFee)
                //nonce
                .copyArgument(argNonce)
                //memo
                .copyArgument(argMemo)
                //valid until
                .copyArgument(argValidUntil)
                .showMessage("Mina Send")
                .showAddress(ScriptData.getDataBufferAll(Buffer.CACHE1))
                .getScript();

                if (type == MinaTxType.DELEGATE) {
                    script = scriptMina.showMessage("Mina Delegate").getScript();
                }else if(type == MinaTxType.SEND) {
                    script = scriptMina
                            //amount
                            .copyArgument(argAmount)
                            .showAmount(argAmount, 9).getScript();
                }

                script = scriptMina.showPressButton()
                .setHeader(HashType.Poseidon, SignType.Schnorr)
                .getScript();

        return script;
    }

    public static String MinaSendScriptSignature = "";
    public static String MinaDelegateScriptSignature = "";

}
