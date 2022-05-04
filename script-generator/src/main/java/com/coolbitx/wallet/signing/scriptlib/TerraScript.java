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
        System.out.println("Terra CW20: \n" + getTerraCW20Script(mainnet) + "\n");
        System.out.println("Terra CW20 Signature: \n" + TerraCW20ScriptSignature + "\n");
        System.out.println("Terra Blind: \n" + getTerraBlindScript(mainnet) + "\n");

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
                .copyString("0a262f74657272612e7761736d2e763162657461312e4d736745786563757465436f6e7472616374")
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
            "304502203213ED60C19C5E64F4A52F80F9EDCEC7E7081DE3657704D338C791526FE869BB0221008C936D28248FE8B7BDE31EDA79229CC92EC69317DC1183BAAC2575179100B2B1",
            144, '0');
    public static String TerraDelegateScriptSignature = Strings.padStart(
            "30450220073FBB49A7A51DBAA03C4E8D68E01BB02C2ED73936D2F1A667474D09DF74A89A022100D9930172BBE0CAB5EB023D1833B2E214016BAE1F809F7B5B991FA7E8A6FB88EB",
            144, '0');
    public static String TerraUndelegateScriptSignature = Strings.padStart(
            "304502202EE1CB2C101DB7B5250CD91040C06846D978422CD6AF2BC90BB7410E27CE53A20221008DB95BC8C0A82E12AA2D40B11205EFB5E1EB1758F4FB80FB3D0CBEECABC1C6B8",
            144, '0');
    public static String TerraWithdrawScriptSignature = Strings.padStart(
            "30450221008F008A721BEE998992F47F3E72C7BA38AA43896CE6E41255BE6B5FD21467AAD3022052EC824AE12B25C51D84A5C590277BEBA6555385603D40D140E9843F0D73744C",
            144, '0');
    public static String TerraSmartScriptSignature = Strings.padStart(
            "304502210081917F513F1DEE39D26DF556ED10207FC886ABC1BC706AAF06D36952C76254ED0220338B0A93D7EA8EA0990C2ECE437EF6547CA99E0BACD6D462A4B785F61B21D79E",
            144, '0');
    public static String TerraCW20ScriptSignature = Strings.padStart(
            "3045022100DEA07F9EA9C85887CF85A479C8336BB87DD4310498979EEC0E46AD74CF05B7BD022059FFCF2DF8CE12539B645F2CD092D23D3C1A4CDD2971465F91B04F99801C0E65",
            144, '0');
    public static String TerraBlindScriptSignature = Strings.padStart(
            "304502207D01F7E5734E9F25EA09D9E8268B4AB45753E868330EC4D78D5044130FB3917E022100C657C2146529F6D2455AAA4975D0207305AF7B0F5889DF8437AF3631D30637E8",
            144, '0');

    public static String TerraTestSendScriptSignature = Strings.padStart(
            "3046022100AED3450B1B9C921E02C16D88043921645449AE924A2DB355B326DAEBF654DC5602210081F0234EC9BE6AF86D7672BA49D45C4F3628D7C2601B42F3F4BEE267DF870AE5",
            144, '0');
    public static String TerraTestDelegateScriptSignature = Strings.padStart(
            "30450221009C27248E54079CCCE3CBD76D159053F8193221C61CF073E94364AD51133FB6690220362326F8D0FC47BF6B7B540664F7052722BEF4DC14A14A4BF164E38850E39A98",
            144, '0');
    public static String TerraTestUndelegateScriptSignature = Strings.padStart(
            "3046022100E66D9847C7C91B5424B29DBC1F93CED2C1D6036AE851C8F6163F33162C1218D602210093F1C9D77CB3C6340C59A1946A21BFAEEA19A98F7C76D4DE30E85227617F7FBC",
            144, '0');
    public static String TerraTestWithdrawScriptSignature = Strings.padStart(
            "304402202F81F4F05F7EEAA9D64CAD2232C6E6D9104D5D5146D8BE046EA9788DE9A4ADC502205887F261F9B5923A864765EC492E335B31497C214C3B96D41E3E04CE46F67642",
            144, '0');
    public static String TerraTestSmartScriptSignature = Strings.padStart(
            "3046022100B821F9FA8EBF5A2142282F4CF62A2E7EC5E24BB07E74D77413999C8F820F2D72022100AF7D8147B621E018D43676B7B438CBB71EB247D8731807B3767B85E6C19E0CC1",
            144, '0');
    public static String TerraTestCW20ScriptSignature = Strings.padStart(
            "304602210085D47B950DE28BCA15692D87B13902B5CFAD189721D0BEFEEEE1E73D3691A9C9022100888CF4A5BF2B74BA920164A72C702C7E2C0D5BE67E87CF5243986C1CBC47BD95",
            144, '0');
    public static String TerraTestBlindScriptSignature = Strings.padStart(
            "3044022035C1CF4FA752AAB4FA299266727DCD202BA3BF2961DB1B8B5481959B186C958E0220222A658D89C39F8B5376B6DDD20D4E4B773B7D92BE22A6B2E6AF6F7897191700",
            144, '0');
}
