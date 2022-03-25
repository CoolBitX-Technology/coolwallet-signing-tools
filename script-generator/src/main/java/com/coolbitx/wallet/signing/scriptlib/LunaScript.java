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
        System.out.println("Luna Delegate: \n" + getLunaScript(LunaTxType.DELEGATE) + "\n");
        System.out.println("Luna Undelegate: \n" + getLunaScript(LunaTxType.UNDELEGATE) + "\n");
        System.out.println("Luna Withdraw: \n" + getLunaScript(LunaTxType.WITHDRAW) + "\n");
        System.out.println("Luna Smart: \n" + getLunaSmartScript(LunaTxType.SMART) + "\n");
    }

    public enum LunaTxType{
        SEND, DELEGATE, UNDELEGATE, WITHDRAW, SMART
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
                case DELEGATE:
                    // message.url - /cosmos.staking.v1beta1.MsgDelegate
                    url = "0a232f636f736d6f732e7374616b696e672e763162657461312e4d736744656c6567617465";
                    break;
                case UNDELEGATE:
                    // message.url - /cosmos.staking.v1beta1.MsgUndelegate
                    url = "0a252f636f736d6f732e7374616b696e672e763162657461312e4d7367556e64656c6567617465";
                    break;
                case WITHDRAW:
                    // message.url - /cosmos.distribution.v1beta1.MsgWithdrawDelegatorReward
                    url = "0a372f636f736d6f732e646973747269627574696f6e2e763162657461312e4d7367576974686472617744656c656761746f72526577617264";
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
    public static String LunaDelegateScriptSignature = Strings.padStart(
        "30440220202B429154DD883F01C542C0A9CDF96EDC3B58A29AB55A9C38A89D63A480BBE7022003894D7967A9EA991BF629D6865F548F6FA2349FB891902D002F780D0EDD9396", 
        144, '0');
    public static String LunaUndelegateScriptSignature = Strings.padStart(
        "3045022100F23EC6B6A3AC9FEE817F8FA8384389DFD1EE79795F2F7D498F9C0CEB04BE46CD022078FBD3FA9F07CC161D9A295AE5AB0A68EBAEC66019C3C78C146D830EE6E9C346", 
        144, '0');
    public static String LunaWithdrawScriptSignature = Strings.padStart(
        "3045022100D030134AC32A92779D7CD88144175CAECE74CC898EA3348CA10D58EBA3F3414B02204FF11662B86DF15F64F1FBAABD52249B0F702AF09FE17B0B6EDCCCF47628877A", 
        144, '0');
    
    public static String getLunaSmartScript(LunaTxType type){
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptData argPublicKey = sac.getArgument(33);
        ScriptData argSender = sac.getArgumentRightJustified(64);
        ScriptData argContract = sac.getArgumentRightJustified(64);
        ScriptData argFundsDenom = sac.getArgumentRightJustified(8);
        ScriptData argFundsAmount = sac.getArgument(8);
        ScriptData argFeeAmount = sac.getArgument(8);
        ScriptData argGas = sac.getArgument(8);
        ScriptData argAccountNumber = sac.getArgument(8);
        ScriptData argSequence = sac.getArgument(8);
        ScriptData argMemo = sac.getArgumentRightJustified(128);
        ScriptData argExecuteMsg = sac.getArgumentAll();
        
        ScriptAssembler scriptAsb = new ScriptAssembler();
        String script = scriptAsb
            .setCoinType(0x014a)
            // tx_body
            .copyString("0a").arrayPointer()
            // message
            .copyString("0a").arrayPointer()
            // message.url - /terra.wasm.v1beta1.MsgExecuteContract
            .copyString("0a262f74657272612e7761736d2e763162657461312e4d736745786563757465436f6e7472616374")
            // message.value
            .copyString("12").arrayPointer()
            // sender_address
            .copyString("0a").protobuf(argSender, typeString)
            // contract_address
            .copyString("12").protobuf(argContract, typeString)
            // execute_msg
            .copyString("1a").protobuf(argExecuteMsg, typeString)
            // identify if funds === undefined
            .ifEqual(argFundsDenom, Strings.padStart("", 16, '0'), 
                    "",
                    new ScriptAssembler()
                    // funds<Coin>
                    .copyString("2a").arrayPointer()
                    // coin.denom
                    .copyString("0a").protobuf(argFundsDenom, typeString)
                    // coin.amount
                    .copyString("12").arrayPointer()
                    .baseConvert(argFundsAmount, Buffer.TRANSACTION, 0,
                            ScriptAssembler.decimalCharset, ScriptAssembler.leftJustify)
                    .arrayEnd() // coin.amount end   
                    .arrayEnd() // funds<coin> end
                    .getScript()
            )
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
            .showWrap("SMART", "")
            .showAddress(argContract)
            .ifEqual(argFundsDenom, Strings.padStart("", 16, '0'), 
                    "",
                    new ScriptAssembler()
                    .showAmount(argFundsAmount, 6)
                    .getScript()
            )
            .showPressButton()
            // version=03 ScriptAssembler.hash=02=sha256 sign=01=ECDSA
            .setHeader(HashType.SHA256, SignType.ECDSA)
            .getScript();
        return script;
    }

    public static String LunaSmartScriptSignature = Strings.padStart(
        "003045022100E641F3C06EBEBB2268E486DD397D3AA46709200281DCCB0821376FBCCA16F6530220340DE9C5629891A7F96BB6B3F9BB18789248DC1FD55DAD8B543305C49C27F8B3", 
        144, '0');
}