/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.coolbitx.wallet.signing.scriptlib;

import com.coolbitx.wallet.signing.utils.ScriptArgumentComposer;
import com.coolbitx.wallet.signing.utils.ScriptAssembler;
import com.coolbitx.wallet.signing.utils.ScriptData;
import com.coolbitx.wallet.signing.utils.ScriptData.Buffer;

public class AtomScript {

    public static void listAll() {
        System.out.println("Atom Send: \n" + getCosmosScript(CosmosTxType.SEND) + "\n");
        System.out.println("Atom Delegate: \n" + getCosmosScript(CosmosTxType.DELEGATE) + "\n");
        System.out.println("Atom Undelegate: \n" + getCosmosScript(CosmosTxType.UNDELEGATE) + "\n");
        System.out.println("Atom Withdraw: \n" + getCosmosScript(CosmosTxType.WITHDRAW) + "\n");
    }

    public enum CosmosTxType {
        SEND, DELEGATE, UNDELEGATE, WITHDRAW
    }

    private static int typeString = 2;
    private static int typeInt = 0;

    public static String getCosmosScript(CosmosTxType type) {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptData argPublicKey = sac.getArgument(33);
        ScriptData argFromOrDelegator = sac.getArgumentRightJustified(64);
        ScriptData argToOrValidator = sac.getArgumentRightJustified(64);
        ScriptData argAmount = sac.getArgument(0);
        if (type != CosmosTxType.WITHDRAW) {
            argAmount = sac.getArgument(8);
        }
        ScriptData argFeeAmount = sac.getArgument(8);
        ScriptData argGas = sac.getArgument(8);
        ScriptData argAccountNumber = sac.getArgument(8);
        ScriptData argSequence = sac.getArgument(8);
        ScriptData argMemo = sac.getArgumentAll();

        String url = "";
        if (type == CosmosTxType.SEND) {
            // message.url - /cosmos.bank.v1beta1.MsgSend
            url = "0a1c2f636f736d6f732e62616e6b2e763162657461312e4d736753656e64";
        } else if (type == CosmosTxType.DELEGATE) {
            // message.url - /cosmos.staking.v1beta1.MsgDelegate
            url = "0a232f636f736d6f732e7374616b696e672e763162657461312e4d736744656c6567617465";
        } else if (type == CosmosTxType.UNDELEGATE) {
            // message.url - /cosmos.staking.v1beta1.MsgUndelegate
            url = "0a252f636f736d6f732e7374616b696e672e763162657461312e4d7367556e64656c6567617465";
        } else if (type == CosmosTxType.WITHDRAW) {
            // message.url - /cosmos.distribution.v1beta1.MsgWithdrawDelegatorReward
            url = "0a372f636f736d6f732e646973747269627574696f6e2e763162657461312e4d7367576974686472617744656c656761746f72526577617264";
        }

        String script = "03030201"
                // version=03 ScriptAssembler.hash=02=sha256 sign=01=ECDSA
                + ScriptAssembler.setCoinType(0x76)
                // tx_body
                + ScriptAssembler.copyString("0a") + ScriptAssembler.arrayPointer()
                // message
                + ScriptAssembler.copyString("0a") + ScriptAssembler.arrayPointer()
                // message.url
                + ScriptAssembler.copyString(url)
                // message.value
                + ScriptAssembler.copyString("12") + ScriptAssembler.arrayPointer()
                // from_or_delegator_address
                + ScriptAssembler.copyString("0a")
                + ScriptAssembler.protobuf(argFromOrDelegator, typeString)
                // to_or_validator_address
                + ScriptAssembler.copyString("12")
                + ScriptAssembler.protobuf(argToOrValidator, typeString);

        if (type != CosmosTxType.WITHDRAW) {
            script = script
                    // amount<Coin>
                    + ScriptAssembler.copyString("1a") + ScriptAssembler.arrayPointer()
                    // coin.denom - uatom
                    + ScriptAssembler.copyString("0a057561746f6d")
                    // coin.amount
                    + ScriptAssembler.copyString("12") + ScriptAssembler.arrayPointer()
                    + ScriptAssembler.baseConvert(argAmount, Buffer.TRANSACTION, 0,
                            ScriptAssembler.decimalCharset, ScriptAssembler.leftJustify)
                    + ScriptAssembler.arrayEnd() // coin.amount end
                    + ScriptAssembler.arrayEnd(); // amount<coin> end
        }

        script = script + ScriptAssembler.arrayEnd() // message.value end
                + ScriptAssembler.arrayEnd() // message end
                // memo
                + ScriptAssembler.copyString("12") + ScriptAssembler.arrayPointer()
                + ScriptAssembler.copyRegularString(argMemo) + ScriptAssembler.arrayEnd() // memo end
                + ScriptAssembler.arrayEnd() // tx_body end

                // auth_info
                + ScriptAssembler.copyString("12") + ScriptAssembler.arrayPointer()
                // signer_info
                + ScriptAssembler.copyString("0a") + ScriptAssembler.arrayPointer()
                // pubkey
                + ScriptAssembler.copyString(
                        "0a460a1f2f636f736d6f732e63727970746f2e736563703235366b312e5075624b657912230a21")
                + ScriptAssembler.copyArgument(argPublicKey)
                // mode_info
                + ScriptAssembler.copyString("12040a020801")
                // sequence
                + ScriptAssembler.copyString("18") + ScriptAssembler.protobuf(argSequence, typeInt)
                + ScriptAssembler.arrayEnd() // signer_info end
                // fee
                + ScriptAssembler.copyString("12") + ScriptAssembler.arrayPointer()
                // amount<Coin>
                + ScriptAssembler.copyString("0a") + ScriptAssembler.arrayPointer()
                // coin.denom - uatom
                + ScriptAssembler.copyString("0a057561746f6d")
                // coin.amount
                + ScriptAssembler.copyString("12") + ScriptAssembler.arrayPointer()
                + ScriptAssembler.baseConvert(argFeeAmount, Buffer.TRANSACTION, 0,
                        ScriptAssembler.decimalCharset, ScriptAssembler.leftJustify)
                + ScriptAssembler.arrayEnd() // coin.amount end
                + ScriptAssembler.arrayEnd() // amount<coin> end
                // gas_limit
                + ScriptAssembler.copyString("10") + ScriptAssembler.protobuf(argGas, typeInt)
                + ScriptAssembler.arrayEnd() // fee end
                + ScriptAssembler.arrayEnd() // auth_info end
                // chain_id
                + ScriptAssembler.copyString("1a0b636f736d6f736875622d34")
                // account_number
                + ScriptAssembler.copyString("20") + ScriptAssembler.protobuf(argAccountNumber, typeInt)
                // display
                + ScriptAssembler.showMessage("ATOM");

        if (type == CosmosTxType.DELEGATE) {
            script += ScriptAssembler.showMessage("Delgt");
        } else if (type == CosmosTxType.UNDELEGATE) {
            script += ScriptAssembler.showMessage("UnDel");
        } else if (type == CosmosTxType.WITHDRAW) {
            script += ScriptAssembler.showMessage("Reward");
        }

        script += ScriptAssembler.showAddress(argToOrValidator);

        if (type != CosmosTxType.WITHDRAW) {
            script += ScriptAssembler.showAmount(argAmount, 6);
        }

        script += ScriptAssembler.showPressButton();

        return script;
    }
    public static String CosmosScriptSignature = "00304502202591B0C6EF53FC5FDF4EC653EA516C59E5C4198E3A4ABD07BD59C1E591F58F89022100FFF19AFBF03C2FE5F789DC27013460AC2839E282ADD5EC7350DDC7062D527F82";
    public static String CosmosDelegateScriptSignature = "00304502207468FBB76F93B56D2EDED7DFD66B36E8BC0A8C144396B63C72090020F150E806022100F0AC223412E0928789C487CC07BADADCBCD4669F8676CE2F0DAD09A9918133C3";
    public static String CosmosUndelegateScriptSignature = "0000304402206DA3C4C965D8AC20C19F09814BD910A5D7DAE4DA63A5BB1D7EDBA42EA13216C10220520DD56D0E89BC420402C6890CA617D3FFFC8543C4EA439D5FB854370310EB62";
    public static String CosmosWithdrawScriptSignature = "00304502201F7900CAFC564A09C770BBBC6C3CC80FDB9AB0A6DE3AFE071ADF6BF1DE3EF40A0221008026AC45EA5919AF4EF21C790EC9DE6AFA351D1FADB15B26BFCE821EF0DB02B6";
}
