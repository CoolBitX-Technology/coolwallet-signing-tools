package com.coolbitx.wallet.signing.scriptlib;

import com.coolbitx.wallet.signing.utils.ScriptArgumentComposer;
import com.coolbitx.wallet.signing.utils.ScriptAssembler;
import com.coolbitx.wallet.signing.utils.ScriptData;
import com.coolbitx.wallet.signing.utils.ScriptAssembler.HashType;
import com.coolbitx.wallet.signing.utils.ScriptAssembler.SignType;
import com.coolbitx.wallet.signing.utils.ScriptData.Buffer;

public class CroScript {
    public static void listAll() {
        System.out.println("Cro Send: \n" + getCROScript(CroTxType.SEND) + "\n");
    }

    public enum CroTxType {
        SEND, DELEGATE, UNDELEGATE, WITHDRAW
    }

    private static int typeString = 2;
    private static int typeInt = 0;

    public static String getCROScript(CroTxType type){
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptData argPublicKey = sac.getArgument(33);
        ScriptData argFromOrDelegator = sac.getArgumentRightJustified(64);
        ScriptData argToOrValidator = sac.getArgumentRightJustified(64);
        ScriptData argAmount = sac.getArgument(0);
        if (type != CroTxType.WITHDRAW) {
            argAmount = sac.getArgument(8);
        }
        ScriptData argFeeAmount = sac.getArgument(8);
        ScriptData argGas = sac.getArgument(8);
        ScriptData argAccountNumber = sac.getArgument(8);
        ScriptData argSequence = sac.getArgument(8);
        ScriptData argMemo = sac.getArgumentAll();

        String url = "";
        if(type == CroTxType.SEND){
            url = "0a1c2f636f736d6f732e62616e6b2e763162657461312e4d736753656e64";
        }
        ScriptAssembler scriptAsb = new ScriptAssembler();
        String script = scriptAsb
                .setCoinType(0x018a)
                // tx_body
                .copyString("0a").arrayPointer()
                // message
                .copyString("0a").arrayPointer()
                // message.url
                .copyString(url)
                // message.value
                .copyString("12").arrayPointer()
                // from_or_delegator_address
                .copyString("0a").protobuf(argFromOrDelegator, typeString)
                // to_or_validator_address
                .copyString("12").protobuf(argToOrValidator, typeString)
                .getScript();
        if(type != CroTxType.WITHDRAW){
            script = scriptAsb
            // amount<Coin>
            .copyString("1a").arrayPointer()
            // coin.denom - basecro
            .copyString("0a076261736563726f")
            // coin.amount
            .copyString("12").arrayPointer()
            .baseConvert(argAmount, Buffer.TRANSACTION, 0, 
                    ScriptAssembler.decimalCharset, ScriptAssembler.leftJustify)
            .arrayEnd() // coin.amount end   
            .arrayEnd() // amount<coin> end
            .getScript();
        }

        script = scriptAsb
            .arrayEnd() // message.value end
            .arrayEnd() // message end
            // memo
            .copyString("12").arrayPointer()
            .copyRegularString(argMemo)
            .arrayEnd() // memo end
            .arrayEnd() // tx_body end

            // auth_info
            .copyString("12").arrayPointer()
            // signer_info
            .copyString("0a").arrayPointer()
            // pubkey
            .copyString("0a460a1f2f636f736d6f732e63727970746f2e736563703235366b312e5075624b657912230a21")
            .copyArgument(argPublicKey)
            // mode_info
            .copyString("12040a020801")
            // sequence
            .copyString("18")
            .protobuf(argSequence, typeInt)
            .arrayEnd() // signer_info end
            // fee
            .copyString("12").arrayPointer()
            // amount<Coin>
            .copyString("0a").arrayPointer()
            // coin.denom - basecro
            .copyString("0a076261736563726f")
            // coin.amount
            .copyString("12").arrayPointer()
            .baseConvert(argFeeAmount, Buffer.TRANSACTION, 0, ScriptAssembler.decimalCharset, ScriptAssembler.leftJustify)
            .arrayEnd() // coin.amount end
            .arrayEnd() // amount<coin> end
            // gas_limit
            .copyString("10") 
            .protobuf(argGas, typeInt)
            .arrayEnd() // fee end
            .arrayEnd() // auth_info end
            // chain_id
            .copyString("63727970746f2d6f72672d636861696e2d6d61696e6e65742d31")
            // account_number
            .copyString("20") 
            .protobuf(argAccountNumber, typeInt)
            // display
            .showMessage("CRO")
            .showAddress(argToOrValidator)
            .getScript();
        if(type != CroTxType.WITHDRAW){
            script = scriptAsb.showAmount(argAmount, 6).getScript();
        }
        script = scriptAsb.showPressButton()
            // version=03 ScriptAssembler.hash=02=sha256 sign=01=ECDSA
            .setHeader(HashType.SHA256, SignType.ECDSA)
            .getScript();
        return script;
    }
}
