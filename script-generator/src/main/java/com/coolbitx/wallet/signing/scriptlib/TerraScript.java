package com.coolbitx.wallet.signing.scriptlib;

import com.coolbitx.wallet.signing.utils.HexUtil;
import com.coolbitx.wallet.signing.utils.ScriptArgumentComposer;
import com.coolbitx.wallet.signing.utils.ScriptAssembler;
import com.coolbitx.wallet.signing.utils.ScriptData;
import com.coolbitx.wallet.signing.utils.ScriptAssembler.HashType;
import com.coolbitx.wallet.signing.utils.ScriptAssembler.SignType;
import com.coolbitx.wallet.signing.utils.ScriptData.Buffer;
import com.google.common.base.Strings;

public class TerraScript {

    public static void main(String[] args) {
        listAll();
    }

    public static void listAll() {
        //String sSTART = "\u001B[31m"; // red
        //String sSTART = "\u001B[32m"; // green
        String sSTART = "\u001B[34m"; // blue
        String sEND = "\u001B[0m";

        // chain_id - columbus-5 (main-net)
        String classic = "1a0A636f6c756d6275732d35";
        // chain_id - phoenix-1 (main-net)
        String mainnet = "1a0970686f656e69782d31";
        // chain_id - bombay-12 (test-net)
        // String testnet = "1a09626f6d6261792d3132";
        // chain_id - pisco-1 (test-net)
        String testnet = "1a07706973636f2d31";
        
        // System.out.println("Terra SEND = {");
        // System.out.println("  script: `" + sSTART + getTerraScript(TerraTxType.SEND, mainnet) + sEND + "`,");
        // System.out.println("  signature: `" + TerraSendScriptSignature + "`,");
        // System.out.println("  script_test: `" + sSTART + getTerraScript(TerraTxType.SEND, testnet) + sEND + "`,");
        // System.out.println("  signature_test: `" + TerraTestSendScriptSignature + "`,");
        // System.out.println("  script_classic: `" + sSTART + getTerraScript(TerraTxType.SEND, classic) + sEND + "`,");
        // System.out.println("  signature_classic: `" + TerraClassicSendScriptSignature + "`,");
        // System.out.println("}\n");

        // System.out.println("Terra DELEGATE = {");
        // System.out.println("  script: `" + sSTART + getTerraScript(TerraTxType.DELEGATE, mainnet) + sEND + "`,");
        // System.out.println("  signature: `" + TerraDelegateScriptSignature + "`,");
        // System.out.println("  script_test: `" + sSTART + getTerraScript(TerraTxType.DELEGATE, testnet) + sEND + "`,");
        // System.out.println("  signature_test: `" + TerraTestDelegateScriptSignature + "`,");
        // System.out.println("  script_classic: `" + sSTART + getTerraScript(TerraTxType.DELEGATE, classic) + sEND + "`,");
        // System.out.println("  signature_classic: `" + TerraClassicDelegateScriptSignature + "`,");
        // System.out.println("}\n");

        // System.out.println("Terra UNDELEGATE = {");
        // System.out.println("  script: `" + sSTART + getTerraScript(TerraTxType.UNDELEGATE, mainnet) + sEND + "`,");
        // System.out.println("  signature: `" + TerraUndelegateScriptSignature + "`,");
        // System.out.println("  script_test: `" + sSTART + getTerraScript(TerraTxType.UNDELEGATE, testnet) + sEND + "`,");
        // System.out.println("  signature_test: `" + TerraTestUndelegateScriptSignature + "`,");
        // System.out.println("  script_classic: `" + sSTART + getTerraScript(TerraTxType.UNDELEGATE, classic) + sEND + "`,");
        // System.out.println("  signature_classic: `" + TerraClassicUndelegateScriptSignature + "`,");
        // System.out.println("}\n");

        // System.out.println("Terra WITHDRAW = {");
        // System.out.println("  script: `" + sSTART + getTerraScript(TerraTxType.WITHDRAW, mainnet) + sEND + "`,");
        // System.out.println("  signature: `" + TerraWithdrawScriptSignature + "`,");
        // System.out.println("  script_test: `" + sSTART + getTerraScript(TerraTxType.WITHDRAW, testnet) + sEND + "`,");
        // System.out.println("  signature_test: `" + TerraTestWithdrawScriptSignature + "`,");
        // System.out.println("  script_classic: `" + sSTART + getTerraScript(TerraTxType.WITHDRAW, classic) + sEND + "`,");
        // System.out.println("  signature_classic: `" + TerraClassicWithdrawScriptSignature + "`,");
        // System.out.println("}\n");

        // System.out.println("Terra SMART = {");
        // System.out.println("  script: `" + sSTART + getTerraSmartScript(mainnet, false) + sEND + "`,");
        // System.out.println("  signature: `" + TerraSmartScriptSignature + "`,");
        // System.out.println("  script_test: `" + sSTART + getTerraSmartScript(testnet, false) + sEND + "`,");
        // System.out.println("  signature_test: `" + TerraTestSmartScriptSignature + "`,");
        // System.out.println("  script_classic: `" + sSTART + getTerraSmartScript(classic, true) + sEND + "`,");
        // System.out.println("  signature_classic: `" + TerraClassicSmartScriptSignature + "`,");
        // System.out.println("}\n");

        // System.out.println("Terra CW20 = {");
        // System.out.println("  script: `" + sSTART + getTerraCW20Script(mainnet, false) + sEND + "`,");
        // System.out.println("  signature: `" + TerraCW20ScriptSignature + "`,");
        // System.out.println("  script_test: `" + sSTART + getTerraCW20Script(testnet, false) + sEND + "`,");
        // System.out.println("  signature_test: `" + TerraTestCW20ScriptSignature + "`,");
        // System.out.println("  script_classic: `" + sSTART + getTerraCW20Script(classic, true) + sEND + "`,");
        // System.out.println("  signature_classic: `" + TerraClassicCW20ScriptSignature + "`,");
        // System.out.println("}\n");

        // System.out.println("Terra BLIND = {");
        // System.out.println("  script: `" + sSTART + getTerraBlindScript(mainnet) + sEND + "`,");
        // System.out.println("  signature: `" + TerraBlindScriptSignature + "`,");
        // System.out.println("  script_test: `" + sSTART + getTerraBlindScript(testnet) + sEND + "`,");
        // System.out.println("  signature_test: `" + TerraTestBlindScriptSignature + "`,");
        // System.out.println("  script_classic: `" + sSTART + getTerraBlindScript(classic) + sEND + "`,");
        // System.out.println("  signature_classic: `" + TerraClassicBlindScriptSignature + "`,");
        // System.out.println("}\n");

        System.out.println("Terra Send: \n" + getTerraScript(TerraTxType.SEND, mainnet) + "\n");
        System.out.println("Terra Send Signature: \n" + TerraSendScriptSignature + "\n");
        System.out.println("Terra Delegate: \n" + getTerraScript(TerraTxType.DELEGATE, mainnet) + "\n");
        System.out.println("Terra Delegate Signature: \n" + TerraDelegateScriptSignature + "\n");
        System.out.println("Terra Undelegate: \n" + getTerraScript(TerraTxType.UNDELEGATE, mainnet) + "\n");
        System.out.println("Terra Undelegate Signature: \n" + TerraUndelegateScriptSignature + "\n");
        System.out.println("Terra Withdraw: \n" + getTerraScript(TerraTxType.WITHDRAW, mainnet) + "\n");
        System.out.println("Terra Withdraw Signature: \n" + TerraWithdrawScriptSignature + "\n");
        System.out.println("Terra Smart Contract: \n" + getTerraSmartScript(mainnet, false) + "\n");
        System.out.println("Terra Smart Contract Signature: \n" + TerraSmartScriptSignature + "\n");
        System.out.println("Terra CW20: \n" + getTerraCW20Script(mainnet, false) + "\n");
        System.out.println("Terra CW20 Signature: \n" + TerraCW20ScriptSignature + "\n");
        System.out.println("Terra Blind: \n" + getTerraBlindScript(mainnet) + "\n");
        System.out.println("Terra Blind Signature: \n" + TerraBlindScriptSignature + "\n");

        System.out.println("Terra Test Send: \n" + getTerraScript(TerraTxType.SEND, testnet) + "\n");
        System.out.println("Terra Test Send Signature: \n" + TerraTestSendScriptSignature + "\n");
        System.out.println("Terra Test Delegate: \n" + getTerraScript(TerraTxType.DELEGATE, testnet) + "\n");
        System.out.println("Terra Test Delegate Signature: \n" + TerraTestDelegateScriptSignature + "\n");
        System.out.println("Terra Test Undelegate: \n" + getTerraScript(TerraTxType.UNDELEGATE, testnet) + "\n");
        System.out.println("Terra Test Undelegate Signature: \n" + TerraTestUndelegateScriptSignature + "\n");
        System.out.println("Terra Test Withdraw: \n" + getTerraScript(TerraTxType.WITHDRAW, testnet) + "\n");
        System.out.println("Terra Test Withdraw Signature: \n" + TerraTestWithdrawScriptSignature + "\n");
        System.out.println("Terra Test Smart Contract: \n" + getTerraSmartScript(testnet, false) + "\n");
        System.out.println("Terra Test Smart Contract Signature: \n" + TerraTestSmartScriptSignature + "\n");
        System.out.println("Terra Test CW20: \n" + getTerraCW20Script(testnet, false) + "\n");
        System.out.println("Terra Test CW20 Signature: \n" + TerraTestCW20ScriptSignature + "\n");
        System.out.println("Terra Test Blind: \n" + getTerraBlindScript(testnet) + "\n");
        System.out.println("Terra Test Blind Signature: \n" + TerraTestBlindScriptSignature + "\n");   
    }

    public enum TerraTxType {
        SEND, DELEGATE, UNDELEGATE, WITHDRAW
    }

    private static final int typeString = 2;
    private static final int typeInt = 0;

    public static String getTerraScript(TerraTxType type, String chainId) {
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
        if (type != TerraTxType.WITHDRAW) {
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
                .ifEqual(argSequence, "0", "", 
                        new ScriptAssembler()
                                .copyString("18")
                                .protobuf(argSequence, typeInt)
                                .getScript()
                )
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
                .getScript();
        if (null != type) {
            switch (type) {
                case SEND:
                    scriptAsb.showMessage(argDenomLabel).getScript();
                    break;
                case DELEGATE:
                    scriptAsb.showMessage("LUNA")
                            .showMessage("Delgt")
                            .getScript();
                    break;
                case UNDELEGATE:
                    scriptAsb.showMessage("LUNA")
                            .showMessage("UnDel")
                            .getScript();
                    break;
                case WITHDRAW:
                    scriptAsb.showMessage("LUNA")
                            .showMessage("Reward")
                            .getScript();
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

    public static String getTerraSmartScript(String chainId, boolean isClassic) {
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

        // classic: /terra.wasm.v1beta1.MsgExecuteContract
        String url = "0a262f74657272612e7761736d2e763162657461312e4d736745786563757465436f6e7472616374";
        if(!isClassic){
        // Terra2.0 /cosmwasm.wasm.v1.MsgExecuteContract
                url = "0a242f636f736d7761736d2e7761736d2e76312e4d736745786563757465436f6e7472616374";
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
                .ifEqual(argSequence, "0", "", 
                        new ScriptAssembler()
                                .copyString("18")
                                .protobuf(argSequence, typeInt)
                                .getScript()
                )
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
                .showWrap("SMART", "")
                .showAddress(argContract)
                // Display Funds
                // .ifEqual(argFundsDenomInfo, Strings.padStart("", 16, '0'), 
                //         "",
                //         new ScriptAssembler()
                //         .showMessage(argFundsDenomLabel)
                //         .showAmount(argFundsAmount, 6)
                //         .getScript()
                // )
                .showPressButton()
                // version=03 ScriptAssembler.hash=02=sha256 sign=01=ECDSA
                .setHeader(HashType.SHA256, SignType.ECDSA)
                .getScript();
        return script;
    }

    public static String getTerraCW20Script(String chainId, boolean isClassic) {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptData argPublicKey = sac.getArgument(33);
        ScriptData argSender = sac.getArgumentRightJustified(64);
        ScriptData argContract = sac.getArgumentRightJustified(64);

        ScriptData argTo = sac.getArgumentRightJustified(64);
        ScriptData argValue = sac.getArgument(8);

        ScriptData argFeeDenomInfo = sac.getArgumentUnion(0, 16);
        ScriptData argFeeDenomLabel = sac.getArgumentRightJustified(8);
        ScriptData argFeeDenom = sac.getArgumentRightJustified(8);
        ScriptData argFeeDenomSign = sac.getArgument(72);
        ScriptData argFeeAmount = sac.getArgument(8);
        ScriptData argGas = sac.getArgument(8);
        ScriptData argAccountNumber = sac.getArgument(8);
        ScriptData argSequence = sac.getArgument(8);
        ScriptData argMemo = sac.getArgumentRightJustified(128);

        ScriptData argTokenInfo = sac.getArgumentUnion(0, 53);
        ScriptData argDecimal = sac.getArgument(1);
        ScriptData argNameLength = sac.getArgument(1);
        ScriptData argName = sac.getArgumentVariableLength(7);
        ScriptData argContractHex = sac.getArgument(44);
        ScriptData argSign = sac.getArgument(72);

        ScriptData argExecuteMsg = sac.getArgumentAll();

        // classic: /terra.wasm.v1beta1.MsgExecuteContract
        String url = "0a262f74657272612e7761736d2e763162657461312e4d736745786563757465436f6e7472616374";
        if(!isClassic){
        // Terra2.0 /cosmwasm.wasm.v1.MsgExecuteContract
                url = "0a242f636f736d7761736d2e7761736d2e76312e4d736745786563757465436f6e7472616374";
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
                // sender_address
                .copyString("0a").protobuf(argSender, typeString)
                // contract_address
                .copyString("12").protobuf(argContract, typeString)
                //execute_msg
                .copyString("1a").protobuf(argExecuteMsg, typeString)
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
                .ifEqual(argSequence, "0", "", 
                        new ScriptAssembler()
                                .copyString("18")
                                .protobuf(argSequence, typeInt)
                                .getScript()
                )
                .arrayEnd() // signer_info end
                // fee
                .copyString("12").arrayPointer()
                // amount<Coin>
                .copyString("0a").arrayPointer()
                // coin.denom - from argument
                .ifSigned(argFeeDenomInfo, argFeeDenomSign, "", ScriptAssembler.throwSEError)
                .copyArgument(argFeeDenom)
                // fee.amount
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
                .ifSigned(argTokenInfo, argSign, "",
                        new ScriptAssembler().copyString(HexUtil.toHexString("@"), Buffer.CACHE2)
                                .getScript())
                .setBufferInt(argNameLength, 1, 7)
                .copyArgument(argName, Buffer.CACHE2)
                .showMessage(ScriptData.getDataBufferAll(Buffer.CACHE2))
                .clearBuffer(Buffer.CACHE2)
                .showAddress(argTo)
                .showAmount(argValue, 6)
                .showPressButton()
                // version=03 ScriptAssembler.hash=02=sha256 sign=01=ECDSA
                .setHeader(HashType.SHA256, SignType.ECDSA)
                .getScript();
        return script;
    }

    public static String getTerraBlindScript(String chainId) {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptData argPublicKey = sac.getArgument(33);
        ScriptData argFeeAmount = sac.getArgument(8);
        ScriptData argGas = sac.getArgument(8);
        ScriptData argAccountNumber = sac.getArgument(8);
        ScriptData argSequence = sac.getArgument(8);
        ScriptData argFeeDenomInfo = sac.getArgumentUnion(0, 16);
        ScriptData argFeeDenomLabel = sac.getArgumentRightJustified(8);
        ScriptData argFeeDenom = sac.getArgumentRightJustified(8);
        ScriptData argFeeDenomSign = sac.getArgument(72);
        ScriptData argDataPlaceHolder = sac.getArgument(4);

        return new ScriptAssembler()
            .setCoinType(0x014a)
            // txBody
            .copyString("0a")
            .protobufDataPlaceholder(argDataPlaceHolder)
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
            .ifEqual(argSequence, "0", "", 
                new ScriptAssembler()
                        .copyString("18")
                        .protobuf(argSequence, typeInt)
                        .getScript()
            )
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
            .showWrap("SMART", "")
            .showPressButton()
            .setHeader(HashType.SHA256, SignType.ECDSA)
            .getScript();
    }
    public static String TerraSendScriptSignature = Strings.padStart(
            "304402203B1E7BC90B933C9C3FF8CD99EF4747BAD2F8D53BC016C9A7165EE3BB6677641002203CCBA95EBFD0F93D2A2DBBB48FB27CDF94668FFE09EA08AE0F4417159E2520AA",
            144, '0');
    public static String TerraDelegateScriptSignature = Strings.padStart(
            "3044022050B20513BB8117E2F68070C818D32B5F3889FFEF7F656D7EA804B2CEA39E1CA802207012DC5EDE4EFA291BB07A9A12BE3853CCFB1FC6AF33ADBA511A9A3F7F45DC25",
            144, '0');
    public static String TerraUndelegateScriptSignature = Strings.padStart(
            "3045022100EC05963DA3F06ADE559A8410A6C14F2D16E29F70E6B423A084A32929E6CA07A9022006495A9EBAEEBF29FBD1343174090776DE72D54C0A083FC504FB3096E2FE76D6",
            144, '0');
    public static String TerraWithdrawScriptSignature = Strings.padStart(
            "3046022100D914E14B5AE0E3CD848B3D8C3D00AB7CD453945CFAD5A0D6670587B2A885BA70022100B24EAB09827DBCB4F6B3D6EC80CD04F34C502F9CFEFF1AD83761D78307B53058",
            144, '0');
    public static String TerraSmartScriptSignature = Strings.padStart(
            "3044022076F7410C6AE2B67BB662293AFF400AD27FDBE2CC59804A04A9575720957F5BC4022029097F8F7E103E26155522804C6EAA808080214362BC47A45F56267ACEDAEF23",
            144, '0');
    public static String TerraCW20ScriptSignature = Strings.padStart(
            "30440220501AEEB9216C3039867C7349EEFDDF53C93DB0F0F36481394E89A36340E510C102205D53538C192AE5A1B9FD47D5B7BC8F524A092B637606ED0E1B9B91711420A17F",
            144, '0');
    public static String TerraBlindScriptSignature = Strings.padStart(
            "3046022100C4D6169891EFF8163C564F61255F7173F5ACFE11A371FADC296A020285515A38022100D51D5B03040067EFFF7D89F45A8802578D3779F7098F090ACDC8B5CE4A222979",
            144, '0');

    public static String TerraTestSendScriptSignature = Strings.padStart(
            "304402206B56D95E04E55C8E365B50A6A884AC7ED69F20BF9B87E27607AFF2250BCC7E7E02202B4C1785C60D69CB6D07E59F565D0ED0B2A6023D046D77FA18F401F0EA3DF470",
            144, '0');
    public static String TerraTestDelegateScriptSignature = Strings.padStart(
            "30450221008854B24B5E0662C30A858B7C3DBD33613D63DA1AC8CBD3B2FDC57D488BC14B0F02202C9D6922505594A452092EB1BB0FDA3F55F3AD0026B76E339DF5E78A4E6C0A0E",
            144, '0');
    public static String TerraTestUndelegateScriptSignature = Strings.padStart(
            "304402206E5EB948D3B0446AD9D87B062D61429C1A4094A7378ED222249180BB547DF4880220331BECC2866247C37D18FEA5F509BEA79943C76DC39225B8741438115882E334",
            144, '0');
    public static String TerraTestWithdrawScriptSignature = Strings.padStart(
            "30450221009399BA7328C728323C158D1208948475E2F1FF590D05099976524A7A69656049022040A943FE7EE0E3382ECC82F21B516B4B4D5701EA55AECEB3A6B227EE8584FE51",
            144, '0');
    public static String TerraTestSmartScriptSignature = Strings.padStart(
            "304502202F818EA91A66F558D7A38DD094BE1833C407EACFCA9F6293A2F16C6FDAA35485022100E96BDBAF6F5C59B5ABDA0B136731CF1A831B3CD9BE946260186CAA22ABA92B97",
            144, '0');
    public static String TerraTestCW20ScriptSignature = Strings.padStart(
            "3046022100BAE7CFDDDC2921546AB29A3976EBC1BD7141F76D70E61CFC5BD0C9F3EE4C1F0B022100BC6FDCB571EFFBEE502BD6143CD09AE6D19E1CE8991FB33E9FDB11DCACC20513",
            144, '0');
    public static String TerraTestBlindScriptSignature = Strings.padStart(
            "304402207680633C55188A5523715A6363794C9C49E73F2BB1110E053EEF919C23A664C40220760D6C78E9CF0F3B5D86F92D940F113303FC315CE5BED368A22AEC2A13871936",
            144, '0');

    public static String TerraClassicSendScriptSignature = Strings.padStart(
            "3046022100A4AB713DFA6596B2170ECA55469826221B1208A2A173D44E0085BED84A4448C6022100C1E69E46F3FF8FBAEA75EFE0C146A252CB480288D81FB9F65AE07F6E8ED9B53E",
            144, '0');
    public static String TerraClassicDelegateScriptSignature = Strings.padStart(
            "3045022100A8ED8D732EE5A72F942D5781B5A871BA7523F96FC0E5DF57BED23F0E836AAD8A022045E880556EEA39C7EB6DE1F0C42D5EA868DCCF30481AE13359C1E768D824AAA1",
            144, '0');
    public static String TerraClassicUndelegateScriptSignature = Strings.padStart(
            "3046022100A7A180536748283699C6BE1AFBC43211C6A17BD4D3AA421CA0B2BE04C330D50D022100B573CA5D7A9D72D11ED50F0C0DD40A37D577EE40F6B866CD968939F20828320A", 
            144, '0');
    public static String TerraClassicWithdrawScriptSignature = Strings.padStart(
            "304502210083F98AC5133702D564D09FA4CED5FE59B33923DD79FD07AB15360AC114EE7B7B022071B6A87C08F1DCC4FCFD383BE168725E52FE83108305823EBB0605FE7BEC84F0",
            144, '0');
    public static String TerraClassicSmartScriptSignature = Strings.padStart(
            "30440220372058927E6630E5EC3DB87EFEC7FCB9BECF661E70D216B3E8DF2E32F0AD4A4802205B89A47282DAAE9DC3B7CFFB27F56429748D9F57D1DAF32E83D6C10D4DC0D490",
            144, '0');
    public static String TerraClassicCW20ScriptSignature = Strings.padStart(
            "3046022100B431CD6B42577C8F4C59217F8ED82958CEFEE7CEF2A3731B014D536C1BEA74610221008D6047F81F29AF3CA24F51D1D520704847D2382FC40DFCBBC60314336C2DAF25",
            144, '0');
    public static String TerraClassicBlindScriptSignature = Strings.padStart(
            "3046022100A4280E8D024E5FC419024390D480035B13EE8DCD61316874C48D6E0DA104C7D6022100AC75823DC815457031DD399B04F7EB4650B3907C67B3636CB91A95C9A0DCB57D",
            144, '0');
}
