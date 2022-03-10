package com.coolbitx.wallet.signing.scriptlib;

import com.coolbitx.wallet.signing.utils.ScriptArgumentComposer;
import com.coolbitx.wallet.signing.utils.ScriptAssembler;
import com.coolbitx.wallet.signing.utils.ScriptData;
import com.coolbitx.wallet.signing.utils.ScriptAssembler.HashType;
import com.coolbitx.wallet.signing.utils.ScriptAssembler.SignType;
import com.coolbitx.wallet.signing.utils.ScriptData.Buffer;
import com.google.common.base.Strings;

public class CroScript {

    public static void listAll() {
        System.out.println("Cro Send: \n" + getCROScript(CroTxType.SEND) + "\n");
        System.out.println("Cro Delegate: \n" + getCROScript(CroTxType.DELEGATE) + "\n");
        System.out.println("Cro Undelegate: \n" + getCROScript(CroTxType.UNDELEGATE) + "\n");
        System.out.println("Cro Withdraw: \n" + getCROScript(CroTxType.WITHDRAW) + "\n");
    }

    public enum CroTxType {
        SEND, DELEGATE, UNDELEGATE, WITHDRAW
    }

    private static final int typeString = 2;
    private static final int typeInt = 0;

    public static String getCROScript(CroTxType type) {
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
        if (null != type) {
            switch (type) {
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
        if (type != CroTxType.WITHDRAW) {
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
                .copyString("1a1A63727970746f2d6f72672d636861696e2d6d61696e6e65742d31")
                // account_number
                .copyString("20")
                .protobuf(argAccountNumber, typeInt)
                // display
                .showMessage("CRO")
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
        if (type != CroTxType.WITHDRAW) {
            scriptAsb.showAmount(argAmount, 8).getScript();
        }
        script = scriptAsb.showPressButton()
                // version=03 ScriptAssembler.hash=02=sha256 sign=01=ECDSA
                .setHeader(HashType.SHA256, SignType.ECDSA)
                .getScript();
        return script;
    }

    public static String CroScriptSignature = Strings.padStart(
            "304502205507429A2145100A4D5F3F29EE3EAC2257056390036B0F36AF76FFB1264D2E410221008487F937338FDA74CEE56C659D93C8BF69E3845F6EB116C7B056124C29A2A80E",
            144, '0');
    public static String CroDelegateScriptSignature = Strings.padStart(
            "3045022100AC4C9109D4F1772F40C866220091F091E306C19EF80B886608F890CE3874112502207C5282B72D8ABA1EA6A1D8110ECE11376EB5BC242855D142057AA7C642FA9C7A",
            144, '0');
    public static String CroUndelegateScriptSignature = Strings.padStart(
            "3045022100C69BB84850469C4884CD7C549C2A3E5D01647E0D2CE18C17CC2A29EE3426096102204CB8F05FBF30360D3AEDBCF7DB1D884AE6BD48F3D00F80EE213F33EDAF206BF6",
            144, '0');
    public static String CroWithdrawScriptSignature = Strings.padStart(
            "3045022100B7EFF2B51BE50D9D7111F94938304D7B10EE3E4DCC021C18675A19F8814C6B9C02206DA3EB9D3639B0291C0AB3769BA65D0A6A10B92632F7C337BE481CCD1E18E3CC",
            144, '0');
}
