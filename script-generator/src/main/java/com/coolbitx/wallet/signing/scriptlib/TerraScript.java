package com.coolbitx.wallet.signing.scriptlib;

import com.coolbitx.wallet.signing.utils.HexUtil;
import com.coolbitx.wallet.signing.utils.ScriptArgumentComposer;
import com.coolbitx.wallet.signing.utils.ScriptAssembler;
import com.coolbitx.wallet.signing.utils.ScriptData;
import com.coolbitx.wallet.signing.utils.ScriptAssembler.HashType;
import com.coolbitx.wallet.signing.utils.ScriptAssembler.SignType;
import com.coolbitx.wallet.signing.utils.ScriptData.Buffer;
import com.google.common.base.Strings;

public class TerraScript{

    public static void listAll(){
        System.out.println("Terra Send: \n" + getTerraScript(TerraTxType.SEND) + "\n");
        System.out.println("Terra Send Signature: \n" + TerraTestScriptSignature + "\n");
        System.out.println("Terra Delegate: \n" + getTerraScript(TerraTxType.DELEGATE) + "\n");
        System.out.println("Terra Undelegate: \n" + getTerraScript(TerraTxType.UNDELEGATE) + "\n");
        System.out.println("Terra Withdraw: \n" + getTerraScript(TerraTxType.WITHDRAW) + "\n");
    }

    public enum TerraTxType{
        SEND, DELEGATE, UNDELEGATE, WITHDRAW
    }

    private static final int typeString = 2;
    private static final int typeInt = 0;

    public static String getTerraScript(TerraTxType type){
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptData argPublicKey = sac.getArgument(33);
        ScriptData argFromOrDelegator = sac.getArgumentRightJustified(64);
        ScriptData argToOrValidator = sac.getArgumentRightJustified(64);
        ScriptData argAmount = sac.getArgument(0);
        if (type != TerraTxType.WITHDRAW) {
            argAmount = sac.getArgument(8);
        }
        ScriptData argFeeAmount = sac.getArgument(8);
        ScriptData argGas = sac.getArgument(8);
        ScriptData argAccountNumber = sac.getArgument(8);
        ScriptData argSequence = sac.getArgument(8);
        ScriptData argDenomInfo = sac.getArgumentUnion(0, 16);
        ScriptData argDenomLabel = sac.getArgumentRightJustified(8);
        ScriptData argDenom = sac.getArgumentRightJustified(8);
        ScriptData argDenomSign = sac.getArgument(72);
        ScriptData argFeeDenomInfo = sac.getArgumentUnion(0, 16);
        ScriptData argFeeDenomLabel = sac.getArgumentRightJustified(8);
        ScriptData argFeeDenom = sac.getArgumentRightJustified(8);
        ScriptData argFeeDenomSign = sac.getArgument(72);
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
        if (type != TerraTxType.WITHDRAW){
            script = scriptAsb
                    // amount<Coin>
                    .copyString("1a").arrayPointer()
                    // coin.denom - from argument
                    .ifSigned(argDenomInfo, argDenomSign, "", ScriptAssembler.throwSEError)
                    .copyArgument(argDenom)
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
                // coin.denom - from argument
                .ifSigned(argFeeDenomInfo, argFeeDenomSign, "", ScriptAssembler.throwSEError)
                .copyArgument(argFeeDenom)
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
                // chain_id - columbus-5 (main-net)
                .copyString("1a0A636f6c756d6275732d35")
                // chain_id - bombay-12 (test-net)
                //.copyString("1a09626f6d6261792d3132")
                // account_number
                .copyString("20")
                .protobuf(argAccountNumber, typeInt)
                // display
                .showMessage("TERRA")
                // dispay - from argument
                .showMessage(argDenomLabel)
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
        if (type != TerraTxType.WITHDRAW) {
            scriptAsb.showAmount(argAmount, 6).getScript();
        }
        script = scriptAsb.showPressButton()
                // version=03 ScriptAssembler.hash=02=sha256 sign=01=ECDSA
                .setHeader(HashType.SHA256, SignType.ECDSA)
                .getScript();
        return script;
    }

    public static String TerraScriptSignature = Strings.padStart(
        "30440220761A5381FF53A4A95C6476CF184F81556D5B9E6744FFD7FE4FFDE945ACF2715602203D0890277D5745AADF8B9B43C321EC61CEBAC2FD68CADB470E81245DCA573F70", 
        144, '0');
    public static String TerraDelegateScriptSignature = Strings.padStart(
        "30440220202B429154DD883F01C542C0A9CDF96EDC3B58A29AB55A9C38A89D63A480BBE7022003894D7967A9EA991BF629D6865F548F6FA2349FB891902D002F780D0EDD9396", 
        144, '0');
    public static String TerraUndelegateScriptSignature = Strings.padStart(
        "3045022100F23EC6B6A3AC9FEE817F8FA8384389DFD1EE79795F2F7D498F9C0CEB04BE46CD022078FBD3FA9F07CC161D9A295AE5AB0A68EBAEC66019C3C78C146D830EE6E9C346", 
        144, '0');
    public static String TerraWithdrawScriptSignature = Strings.padStart(
        "3045022100D030134AC32A92779D7CD88144175CAECE74CC898EA3348CA10D58EBA3F3414B02204FF11662B86DF15F64F1FBAABD52249B0F702AF09FE17B0B6EDCCCF47628877A", 
        144, '0');

    public static String TerraTestScriptSignature = Strings.padStart(
        "30440220783521C66992D19A0FC7F117A4334E869761C47FFFF993DAEE13EC79D412C64302200EA5E9911E2E6ADDF00AB29FDC87D176FDB1159334797C7A04FD9BAFBAB7692C", 
        144, '0');
}