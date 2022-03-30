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
        // chain_id - columbus-5 (main-net)
        String mainnet = "1a0A636f6c756d6275732d35";
        // chain_id - bombay-12 (test-net)
        String testnet = "1a09626f6d6261792d3132";

        System.out.println("Terra Send: \n" + getTerraScript(TerraTxType.SEND, mainnet) + "\n");
        System.out.println("Terra Send Signature: \n" + TerraSendScriptSignature + "\n");
        System.out.println("Terra Delegate: \n" + getTerraScript(TerraTxType.DELEGATE, mainnet) + "\n");
        System.out.println("Terra Delegate Signature: \n" + TerraDelegateScriptSignature + "\n");
        System.out.println("Terra Undelegate: \n" + getTerraScript(TerraTxType.UNDELEGATE, mainnet) + "\n");
        System.out.println("Terra Undelegate Signature: \n" + TerraUndelegateScriptSignature + "\n");
        System.out.println("Terra Withdraw: \n" + getTerraScript(TerraTxType.WITHDRAW, mainnet) + "\n");
        System.out.println("Terra Withdraw Signature: \n" + TerraWithdrawScriptSignature + "\n");
        System.out.println("Terra Smart Contract: \n" + getTerraSmartScript(mainnet) + "\n");
        System.out.println("Terra Smart Contract Signature: \n" + TerraSmartScriptSignature + "\n");

        System.out.println("Terra Test Send: \n" + getTerraScript(TerraTxType.SEND, testnet) + "\n");
        System.out.println("Terra Test Send Signature: \n" + TerraTestSendScriptSignature + "\n");
        System.out.println("Terra Test Delegate: \n" + getTerraScript(TerraTxType.DELEGATE, testnet) + "\n");
        System.out.println("Terra Test Delegate Signature: \n" + TerraTestDelegateScriptSignature + "\n");
        System.out.println("Terra Test Undelegate: \n" + getTerraScript(TerraTxType.UNDELEGATE, testnet) + "\n");
        System.out.println("Terra Test Undelegate Signature: \n" + TerraTestUndelegateScriptSignature + "\n");
        System.out.println("Terra Test Withdraw: \n" + getTerraScript(TerraTxType.WITHDRAW, testnet) + "\n");
        System.out.println("Terra Test Withdraw Signature: \n" + TerraTestWithdrawScriptSignature + "\n");
        System.out.println("Terra Test Smart Contract: \n" + getTerraSmartScript(testnet) + "\n");
        System.out.println("Terra Test Smart Contract Signature: \n" + TerraTestSmartScriptSignature + "\n");
    }

    public enum TerraTxType{
        SEND, DELEGATE, UNDELEGATE, WITHDRAW
    }

    private static final int typeString = 2;
    private static final int typeInt = 0;

    public static String getTerraScript(TerraTxType type, String chainId){
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
                
        ScriptData argDenomInfo = sac.getArgument(0);
        ScriptData argDenomLabel = sac.getArgument(0);
        ScriptData argDenom = sac.getArgument(0);
        ScriptData argDenomSign = sac.getArgument(0);
        if (type != TerraTxType.WITHDRAW) {
            argDenomInfo = sac.getArgumentUnion(0, 16);
            argDenomLabel = sac.getArgumentRightJustified(8);
            argDenom = sac.getArgumentRightJustified(8);
            argDenomSign = sac.getArgument(72);
        }

        ScriptData argFeeDenomInfo = sac.getArgumentUnion(0, 16);
        ScriptData argFeeDenomLabel = sac.getArgumentRightJustified(8);
        if (type == TerraTxType.WITHDRAW) {
            argDenomLabel = argFeeDenomLabel;
        }
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
                // chain_id
                .copyString(chainId)
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
    
    public static String getTerraSmartScript(String chainId){
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptData argPublicKey = sac.getArgument(33);
        ScriptData argSender = sac.getArgumentRightJustified(64);
        ScriptData argContract = sac.getArgumentRightJustified(64);
        ScriptData argFundsDenomInfo = sac.getArgumentUnion(0, 16);
        ScriptData argFundsDenomLabel = sac.getArgumentRightJustified(8);
        ScriptData argFundsDenom = sac.getArgumentRightJustified(8);
        ScriptData argFundsDenomSign = sac.getArgument(72);
        ScriptData argFundsAmount = sac.getArgument(8);
        ScriptData argFeeDenomInfo = sac.getArgumentUnion(0, 16);
        ScriptData argFeeDenomLabel = sac.getArgumentRightJustified(8);
        ScriptData argFeeDenom = sac.getArgumentRightJustified(8);
        ScriptData argFeeDenomSign = sac.getArgument(72);
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
            .ifEqual(argFundsDenomInfo, Strings.padStart("", 16, '0'), 
                    "",
                    new ScriptAssembler()
                    // funds<Coin>
                    .copyString("2a").arrayPointer()
                    // coin.denom - from argument
                    .ifSigned(argFundsDenomInfo, argFundsDenomSign, "", ScriptAssembler.throwSEError)
                    .copyArgument(argFundsDenom)
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
            // chain_id
            .copyString(chainId)
            // account_number
            .copyString("20")
            .protobuf(argAccountNumber, typeInt)
            // display
            .showMessage("LUNA")
            .showWrap("SMART", "")
            .showAddress(argContract)
            .ifEqual(argFundsDenomInfo, Strings.padStart("", 16, '0'), 
                    "",
                    new ScriptAssembler()
                    .showMessage(argFundsDenomLabel)
                    .showAmount(argFundsAmount, 6)
                    .getScript()
            )
            .showPressButton()
            // version=03 ScriptAssembler.hash=02=sha256 sign=01=ECDSA
            .setHeader(HashType.SHA256, SignType.ECDSA)
            .getScript();
        return script;
    }

    public static String TerraSendScriptSignature = Strings.padStart(
        "304502203213ED60C19C5E64F4A52F80F9EDCEC7E7081DE3657704D338C791526FE869BB0221008C936D28248FE8B7BDE31EDA79229CC92EC69317DC1183BAAC2575179100B2B1", 
        144, '0');
    public static String TerraDelegateScriptSignature = Strings.padStart(
        "30450220481EF5838F8B91171E9C0C1E270190BB2AE6A414890E38425E01D81D4263C023022100DA6EEA82D2CAB16DC718A5F027857C3758C02F328710366010860A4205E88D0E", 
        144, '0');
    public static String TerraUndelegateScriptSignature = Strings.padStart(
        "3045022060A0DE165A565D8A81E42B753C0C6F92E24B53FF6092938BF16C9037F8B973EC022100EC2EEA96DE2E988B36DD064138DD180A6E6F138B25BEBD234415FFC67F3DC22C", 
        144, '0');
    public static String TerraWithdrawScriptSignature = Strings.padStart(
        "3044022075FFEC543A760F437E66BBC9490D204188E6A57324EADBAAAAC304EF754B191002200EEE8D90DCE52B790CA5D56EDB2CC0D9D8744ECD913F2E9AEC4B3A765F414048", 
        144, '0');
    public static String TerraSmartScriptSignature = Strings.padStart(
        "3045022100E77792E57E91446794A1A66948C7398D4C002A9A3CA808E5B897388CDCFC1D6A02203B9D8C2879E670D8387E2BD876BEF04286A7FA853532609429A6D763BA83A0F0", 
        144, '0');

    public static String TerraTestSendScriptSignature = Strings.padStart(
        "3046022100AED3450B1B9C921E02C16D88043921645449AE924A2DB355B326DAEBF654DC5602210081F0234EC9BE6AF86D7672BA49D45C4F3628D7C2601B42F3F4BEE267DF870AE5", 
        144, '0');
    public static String TerraTestDelegateScriptSignature = Strings.padStart(
        "3045022100F92F9EB921D8D1DAF6F9312831DF646F7B1BE54F87D5F04296FBFAAEDE047F46022013D6DDB8053EB7A7C9B8ECF81CCC035B0619FCF48CC8F18649CCDD97B9EDD00D", 
        144, '0');
    public static String TerraTestUndelegateScriptSignature = Strings.padStart(
        "3045022031321B08CC503B9F745408DB0092F601D1958EFF4CD50C3F746C4059F4814006022100D01CF54A4EC223406DA27A7CFD9B7DD59A8B91159437CE4473FE8043360C70EA", 
        144, '0');
    public static String TerraTestWithdrawScriptSignature = Strings.padStart(
        "3046022100D4C5CADA66BC8362B6594F3C3C14974101FBDC146724270179A9AEBBB36D53D0022100F5358B1B7F9B8D08F03ADE8E72785B4CC4E4E4C866AA5CF17BC1885C9F64A90E", 
        144, '0');
    public static String TerraTestSmartScriptSignature = Strings.padStart(
        "3046022100FC407346F1F0A8019993E99E662B83480FED580B1539333CB42BDE87EF7BAC05022100DE349584B53C945469448DD1C9043D920524DF08EC3A417D0BA795F4FFB64045", 
        144, '0');
}