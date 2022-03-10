package com.coolbitx.wallet.signing.scriptlib;

import com.coolbitx.wallet.signing.utils.ScriptArgumentComposer;
import com.coolbitx.wallet.signing.utils.ScriptAssembler;
import com.coolbitx.wallet.signing.utils.ScriptData;
import com.coolbitx.wallet.signing.utils.ScriptAssembler.HashType;
import com.coolbitx.wallet.signing.utils.ScriptAssembler.SignType;
import com.coolbitx.wallet.signing.utils.ScriptData.Buffer;
import com.google.common.base.Strings;

public class LunaScript{

    public static void listAll(){
        System.out.println("Luna Send: \n" + getLunaScript(LunaTxType.SEND) + "\n");
    }

    public enum LunaTxType{
        SEND, DELEGATE, UNDELEGATE, WITHDRAW
    }

    private static final int typeString = 2;
    private static final int typeInt = 0;

    public static String getLunaScript(LunaTxType type){
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptData argPublicKey = sac.getArgument(33);
        ScriptData argFromOrDelegator = sac.getArgumentRightJustified(64);
        ScriptData argToOrValidator = sac.getArgumentRightJustified(64);
        ScriptData argAmount = sac.getArgument(0);
        if (type != LunaTxType.WITHDRAW) {
            argAmount = sac.getArgument(8);
        }
        ScriptData argFeeAmount = sac.getArgument(8);
        ScriptData argGas = sac.getArgument(8);
        ScriptData argAccountNumber = sac.getArgument(8);
        ScriptData argSequence = sac.getArgument(8);
        ScriptData argMemo = sac.getArgumentAll();

        String url = "";
        if(null != type){
            switch (type){
                case SEND:
                    // message.url - /cosmos.bank.v1beta1.MsgSend
                    url = "0a1c2f636f736d6f732e62616e6b2e763162657461312e4d736753656e64";
                    break;
                default:
                break;
            }
        }

        ScriptAssembler scriptAsb = new ScriptAssembler();
        String script = scriptAsb
            .setCoinType(0x014a)
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
        if (type != LunaTxType.WITHDRAW){
            script = scriptAsb
                    // amount<Coin>
                    .copyString("1a").arrayPointer()
                    // coin.denom - uluna
                    .copyString("0a05756c756e61")
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
                // coin.denom - uluna
                .copyString("0a05756c756e61")
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
                // chain_id - columbus-5
                .copyString("1a0A636f6c756d6275732d35")
                // account_number
                .copyString("20")
                .protobuf(argAccountNumber, typeInt)
                // display
                .showMessage("LUNA")
                .getScript();
        if (null != type) {
            switch (type) {
                case DELEGATE:
                    scriptAsb.showMessage("Delgt").getScript();
                    break;
                case UNDELEGATE:
                    scriptAsb.showMessage("UnDel").getScript();
                    break;
                case WITHDRAW:
                    scriptAsb.showMessage("Reward").getScript();
                    break;
                default:
                    break;
            }
        }
        scriptAsb.showAddress(argToOrValidator);
        if (type != LunaTxType.WITHDRAW) {
            scriptAsb.showAmount(argAmount, 6).getScript();
        }
        script = scriptAsb.showPressButton()
                // version=03 ScriptAssembler.hash=02=sha256 sign=01=ECDSA
                .setHeader(HashType.SHA256, SignType.ECDSA)
                .getScript();
        return script;
    }

    public static String LunaScriptSignature = Strings.padStart(
        "30450221009834810F91ECBE5A8CAA8DEC50E6B92D8C812C8DACA2D4587C4E3E2AE33F59CB02205266A77E6DAD87D2CBECD6F0F8AAB6F5AE76492A8757EF8B5D5394B0C34BE681", 
        144, '0');
}