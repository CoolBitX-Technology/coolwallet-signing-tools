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
        // chain_id - columbus-5 (main-net)
        // String mainnet = "1a0A636f6c756d6275732d35";
        // chain_id - phoenix-1 (main-net)
        String mainnet = "1a0970686f656e69782d31";
        // chain_id - bombay-12 (test-net)
        // String testnet = "1a09626f6d6261792d3132";
        // chain_id - pisco-1 (test-net)
        String testnet = "1a07706973636f2d31";

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
        System.out.println("Terra CW20: \n" + getTerraCW20Script(mainnet) + "\n");
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
        System.out.println("Terra Test Smart Contract: \n" + getTerraSmartScript(testnet) + "\n");
        System.out.println("Terra Test Smart Contract Signature: \n" + TerraTestSmartScriptSignature + "\n");
        System.out.println("Terra Test CW20: \n" + getTerraCW20Script(testnet) + "\n");
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

    public static String getTerraSmartScript(String chainId) {
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
                //.copyString("0a262f74657272612e7761736d2e763162657461312e4d736745786563757465436f6e7472616374")
                // message.url - /cosmwasm.wasm.v1.MsgExecuteContract
                .copyString("0a242f636f736d7761736d2e7761736d2e76312e4d736745786563757465436f6e7472616374")
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

    public static String getTerraCW20Script(String chainId) {
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

        ScriptAssembler scriptAsb = new ScriptAssembler();
        String script = scriptAsb
                .setCoinType(0x014a)
                // tx_body
                .copyString("0a").arrayPointer()
                // message
                .copyString("0a").arrayPointer()
                // message.url - /terra.wasm.v1beta1.MsgExecuteContract
                //.copyString("0a262f74657272612e7761736d2e763162657461312e4d736745786563757465436f6e7472616374")
                // message.url - /cosmwasm.wasm.v1.MsgExecuteContract
                .copyString("0a242f636f736d7761736d2e7761736d2e76312e4d736745786563757465436f6e7472616374")
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
            .showWrap("SMART", "")
            .showPressButton()
            .setHeader(HashType.SHA256, SignType.ECDSA)
            .getScript();
    }
    public static String TerraSendScriptSignature = Strings.padStart(
            "30450220643934547386477E4809D786D1F86EFA102EE2EB179CEC69994CA8665E556DAE022100C075C1BF90C9F9BE4A01EC8A6B748EFC98241F04167590C40CE316C73C89ABC0",
            144, '0');
    public static String TerraDelegateScriptSignature = Strings.padStart(
            "3046022100F8D017F6508D46611495B192279F0F17722C57877ED8D73315736D165B5F83B402210095D9A210D25DD318C435C82C35F1B3B1D2E8DA4EE0C6A87371B18FD74D7BF2D2",
            144, '0');
    public static String TerraUndelegateScriptSignature = Strings.padStart(
            "304502201705F504F0A5C164ABF16309A887C7A2481AFE72A7B9AB0482427508480EC1F7022100D05C5E3A3A337B568B4C58136DEF36E38DF28BE138D4B4E798941D93BC5F3A8A",
            144, '0');
    public static String TerraWithdrawScriptSignature = Strings.padStart(
            "304402206E8678BACDB54D938D6CA5EE3F9CDE28991C66F7C20157A335DEFD86E9562A2B02201BB2B4EB687D220ADDE37705156AC545536BE0043FBDC1CFE4C78514BC5E9A75",
            144, '0');
    public static String TerraSmartScriptSignature = Strings.padStart(
            "30450220709E2E4517448112C2B5DC1BAB4E4C8247E2D39883F80816996BDEE61E31FE1C0221009BE956FEF1F8C9C17929E4FC4721384F4E5B8F0E9FD1B25A01C14255751F756B",
            144, '0');
    public static String TerraCW20ScriptSignature = Strings.padStart(
            "3046022100FA272DE68A3BA8337EB76EF14C1DEDF19CDC28D58636FCCD42974F72A74E4307022100CA46D162AF521BACB705495452379090E4CCC176C84C80729A46873C7214F125",
            144, '0');
    public static String TerraBlindScriptSignature = Strings.padStart(
            "3045022100E6C990C29692F0144D9E6CFA340F335891314115DCF7B4ED8BAE3BE071F8C3E102206FF426F2941D9D9D2446A53457CECC7EE5B7450D51E509F544BF40D40B31B2B2",
            144, '0');

    public static String TerraTestSendScriptSignature = Strings.padStart(
            "3045022027B2C5A2E180383D87CFB80E6CC0D74325629644B3250B45721E40989DE1ADFC022100A7C51E7A2210580725FD9F9EB444F4E10E29EED79452BAE11933794DB4ED3A1C",
            144, '0');
    public static String TerraTestDelegateScriptSignature = Strings.padStart(
            "30460221008849BF12A92956D1BFAF30E7E1700A9CC1B248218CC65598536A976E750DC25D022100F0DDDA991F1F06BC5DF095446542CC34A5370ED73E8DA3D07A338519C00448D7",
            144, '0');
    public static String TerraTestUndelegateScriptSignature = Strings.padStart(
            "304602210091DF0046598F42683DA8F5EF0ECA9B851ACA07B9B471A46C78DB61C667B0D3310221008AC88A8788486E331D7358490DE55228CF97B84028E49F93F787626FEED7DA07",
            144, '0');
    public static String TerraTestWithdrawScriptSignature = Strings.padStart(
            "304402203086EC0A189E3D3E4FA10CD7084F87A33853E4A8B01F6B3B953F2B351E05525602202FC11180FAA2574CD5EFD285C155E0AB9B9E58BD642C324FD40F9860D9A153C2",
            144, '0');
    public static String TerraTestSmartScriptSignature = Strings.padStart(
            "304402202C52100B2196D994CD2E4BA22CE26F48DDB3187237D5D307512B1025E6D028E202200F22C6D0F43DD1F569DBE0E05E3090DEC3A06DFE9ECFAE5BBF3E2E1601F44E91",
            144, '0');
    public static String TerraTestCW20ScriptSignature = Strings.padStart(
            "3044022051D169D562F1F5A59084522E21F0822C035FC64FCE617C06A9A9E23130595D1F02201AC6660834FBD6DF95FA354E022A9F5D5BF47F9A584A93FE2B9E106C9B7499AC",
            144, '0');
    public static String TerraTestBlindScriptSignature = Strings.padStart(
            "30440220119C86637A4ADFADEBDAB976D3560AB656319C8D3BEE18CAB4B0E4420625B9230220688B5A4D1996BAB74AD9C4462DE95F147E5ACDC52F024006B419E1C80432FA7C",
            144, '0');
}
