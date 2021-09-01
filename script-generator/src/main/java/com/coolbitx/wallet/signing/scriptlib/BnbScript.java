/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.coolbitx.wallet.signing.scriptlib;
import com.coolbitx.wallet.signing.utils.HexUtil;
import com.coolbitx.wallet.signing.utils.ScriptArgumentComposer;
import com.coolbitx.wallet.signing.utils.ScriptAssembler;
import com.coolbitx.wallet.signing.utils.ScriptData;
import com.coolbitx.wallet.signing.utils.ScriptData.Buffer;

public class BnbScript {
    
    public static void listAll() {
        System.out.println("Bnb: \n" + getBNBScript(false) + "\n");
        System.out.println("Bnb Bep2: \n" + getBEP2Script(false) + "\n");
        System.out.println("Bnb Place Order: \n" + getBNBPlaceOrderScript(false) + "\n");
        System.out.println("Bnb Cancel Order: \n" + getBNBCancelOrderScript(false) + "\n");
        System.out.println("Bsc: \n" + getBSCScript() + "\n");
        System.out.println("Bsc Bep20: \n" + getBEP20Script() + "\n");
        System.out.println("Bsc Smart Contract: \n" + getBSCSmartContractBlindScript() + "\n");
        System.out.println("Bsc Message: \n" + getBSCMessageBlindScript() + "\n");
        System.out.println("Bsc TypedData: \n" + getBSCTypedDataBlindScript() + "\n");
    }

    /*
{\"account_number\":\"37131\"
,\"chain_id\":\"Binance-Chain-Nile\"
,\"data\":null
,\"memo\":\"test signature\"
,\"msgs\":[{
  \"inputs\":[{
    \"address\":\"tbnb18ay4ac5qrxtys47tkudwsapx522kf2z8u0rjk3\",
    \"coins\":[{
      \"amount\":500000
      ,\"denom\":\"BNB\"}]}]
  ,\"outputs\":[{
    \"address\":\"tbnb15pall8l7p5qdwpqnq309rj8fpxpnx8yl9n78fq\"
    ,\"coins\":[{
      \"amount\":500000
      ,\"denom\":\"BNB\"}]}]}]
,\"sequence\":\"3\"
,\"source\":\"1\"}
     */
    public static String getBNBScript(boolean isTestnet) {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptData argFrom = sac.getArgumentRightJustified(64);
        ScriptData argTo = sac.getArgumentRightJustified(64);
        ScriptData argValue = sac.getArgument(8);
        ScriptData argAccountNumber = sac.getArgument(8);
        ScriptData argSequence = sac.getArgument(8);
        ScriptData argSource = sac.getArgument(8);
        ScriptData argMemo = sac.getArgumentAll();

        return "03000201"
                + //version=00 ScriptAssembler.hash=02=sha256 sign=01=ECDSA
                ScriptAssembler.setCoinType(0x02CA)
                + // set coinType to 02CA
                ScriptAssembler.copyString(HexUtil.toHexString("{\"account_number\":\""))
                + ScriptAssembler.baseConvert(argAccountNumber, Buffer.TRANSACTION, 0, ScriptAssembler.decimalCharset, ScriptAssembler.leftJustify)
                + (!isTestnet
                        ? ScriptAssembler.copyString(HexUtil.toHexString("\",\"chain_id\":\"Binance-Chain-Tigris\",\"data\":null,\"memo\":\""))
                        : ScriptAssembler.copyString(HexUtil.toHexString("\",\"chain_id\":\"Binance-Chain-Nile\",\"data\":null,\"memo\":\"")))
                + ScriptAssembler.copyRegularString(argMemo)
                + ScriptAssembler.copyString(HexUtil.toHexString("\",\"msgs\":[{\"inputs\":[{\"address\":\""))
                + ScriptAssembler.copyRegularString(argFrom)
                + ScriptAssembler.copyString(HexUtil.toHexString("\",\"coins\":[{\"amount\":"))
                + ScriptAssembler.baseConvert(argValue, Buffer.TRANSACTION, 0, ScriptAssembler.decimalCharset, ScriptAssembler.leftJustify)
                + ScriptAssembler.copyString(HexUtil.toHexString(",\"denom\":\"BNB\"}]}],\"outputs\":[{\"address\":\""))
                + ScriptAssembler.copyRegularString(argTo)
                + ScriptAssembler.copyString(HexUtil.toHexString("\",\"coins\":[{\"amount\":"))
                + ScriptAssembler.baseConvert(argValue, Buffer.TRANSACTION, 0, ScriptAssembler.decimalCharset, ScriptAssembler.leftJustify)
                + ScriptAssembler.copyString(HexUtil.toHexString(",\"denom\":\"BNB\"}]}]}],\"sequence\":\""))
                + ScriptAssembler.baseConvert(argSequence, Buffer.TRANSACTION, 0, ScriptAssembler.decimalCharset, ScriptAssembler.leftJustify)
                + ScriptAssembler.copyString(HexUtil.toHexString("\",\"source\":\""))
                + ScriptAssembler.baseConvert(argSource, Buffer.TRANSACTION, 0, ScriptAssembler.decimalCharset, ScriptAssembler.leftJustify)
                + ScriptAssembler.copyString(HexUtil.toHexString("\"}"))
                + (!isTestnet
                        ? ScriptAssembler.showMessage("BNB")
                        : ScriptAssembler.showWrap("BNB", "TESTNET"))
                + ScriptAssembler.showAddress(argTo)
                + ScriptAssembler.showAmount(argValue, 8)
                + ScriptAssembler.showPressButton();
    }

    public static String getBEP2Script(boolean isTestnet) {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptData argFrom = sac.getArgumentRightJustified(64);
        ScriptData argTo = sac.getArgumentRightJustified(64);
        ScriptData argValue = sac.getArgument(8);
        ScriptData argAccountNumber = sac.getArgument(8);
        ScriptData argSequence = sac.getArgument(8);
        ScriptData argSource = sac.getArgument(8);
        ScriptData argTokenName = sac.getArgumentRightJustified(20);
        ScriptData argTokenCheck = sac.getArgumentRightJustified(20);
        ScriptData argTokenSignature = sac.getArgument(72);
        ScriptData argMemo = sac.getArgumentAll();

        return "03000201"
                + //version=00 ScriptAssembler.hash=02=sha256 sign=01=ECDSA
                ScriptAssembler.setCoinType(0x02CA)
                + // set coinType to 02CA
                ScriptAssembler.copyString(HexUtil.toHexString("{\"account_number\":\""))
                + ScriptAssembler.baseConvert(argAccountNumber, Buffer.TRANSACTION, 0, ScriptAssembler.decimalCharset, ScriptAssembler.leftJustify)
                + (!isTestnet
                        ? ScriptAssembler.copyString(HexUtil.toHexString("\",\"chain_id\":\"Binance-Chain-Tigris\",\"data\":null,\"memo\":\""))
                        : ScriptAssembler.copyString(HexUtil.toHexString("\",\"chain_id\":\"Binance-Chain-Nile\",\"data\":null,\"memo\":\"")))
                + ScriptAssembler.copyRegularString(argMemo)
                + ScriptAssembler.copyString(HexUtil.toHexString("\",\"msgs\":[{\"inputs\":[{\"address\":\""))
                + ScriptAssembler.copyRegularString(argFrom)
                + ScriptAssembler.copyString(HexUtil.toHexString("\",\"coins\":[{\"amount\":"))
                + ScriptAssembler.baseConvert(argValue, Buffer.TRANSACTION, 0, ScriptAssembler.decimalCharset, ScriptAssembler.leftJustify)
                + ScriptAssembler.copyString(HexUtil.toHexString(",\"denom\":\""))
                + ScriptAssembler.copyRegularString(argTokenName, Buffer.FREE)
                + ScriptAssembler.copyString(HexUtil.toHexString("-"), Buffer.FREE)
                + ScriptAssembler.copyRegularString(argTokenCheck, Buffer.FREE)
                + ScriptAssembler.copyArgument(ScriptData.getDataBufferAll(Buffer.FREE))
                + ScriptAssembler.copyString(HexUtil.toHexString("\"}]}],\"outputs\":[{\"address\":\""))
                + ScriptAssembler.copyRegularString(argTo)
                + ScriptAssembler.copyString(HexUtil.toHexString("\",\"coins\":[{\"amount\":"))
                + ScriptAssembler.baseConvert(argValue, Buffer.TRANSACTION, 0, ScriptAssembler.decimalCharset, ScriptAssembler.leftJustify)
                + ScriptAssembler.copyString(HexUtil.toHexString(",\"denom\":\""))
                + ScriptAssembler.copyArgument(ScriptData.getDataBufferAll(Buffer.FREE))
                + ScriptAssembler.copyString(HexUtil.toHexString("\"}]}]}],\"sequence\":\""))
                + ScriptAssembler.baseConvert(argSequence, Buffer.TRANSACTION, 0, ScriptAssembler.decimalCharset, ScriptAssembler.leftJustify)
                + ScriptAssembler.copyString(HexUtil.toHexString("\",\"source\":\""))
                + ScriptAssembler.baseConvert(argSource, Buffer.TRANSACTION, 0, ScriptAssembler.decimalCharset, ScriptAssembler.leftJustify)
                + ScriptAssembler.copyString(HexUtil.toHexString("\"}"))
                + ScriptAssembler.clearBuffer(Buffer.FREE)
                + ScriptAssembler.showMessage("BNB")
                + ScriptAssembler.copyRegularString(argTokenName, Buffer.FREE)
                + ScriptAssembler.showMessage(ScriptData.getDataBufferAll(Buffer.FREE))
                + (!isTestnet ? ""
                        : ScriptAssembler.showWrap("BEP2", "TESTNET"))
                + ScriptAssembler.showAddress(argTo)
                + ScriptAssembler.showAmount(argValue, 8)
                + ScriptAssembler.showPressButton();
    }

    /*
{\"account_number\":\"37132\"
,\"chain_id\":\"Binance-Chain-Tigris\"
,\"data\":null
,\"memo\":\"\"
,\"msgs\":[{
   \"id\":\"DA02D8838A1C948E57099B7AC12864B53296F5BF-5\"
  ,\"ordertype\":2
  ,\"price\":39600
  ,\"quantity\":100000000
  ,\"sender\":\"tbnb1mgpd3qu2rj2gu4cfndavz2ryk5efdadl9ze4xj\"
  ,\"side\":1
  ,\"symbol\":\"AAS-361_BNB\"
  ,\"timeinforce\":1}]
,\"sequence\":\"4\"
,\"source\":\"1\"}


{\"account_number\":\"37132\"
,\"chain_id\":\"Binance-Chain-Nile\"
,\"data\":null
,\"memo\":\"\"
,\"msgs\":[{
   \"id\":\"DA02D8838A1C948E57099B7AC12864B53296F5BF-5\"
  ,\"ordertype\":2
  ,\"price\":39600
  ,\"quantity\":100000000
  ,\"sender\":\"tbnb1mgpd3qu2rj2gu4cfndavz2ryk5efdadl9ze4xj\"
  ,\"side\":2
  ,\"symbol\":\"AAS-361_BNB\"
  ,\"timeinforce\":1}]
,\"sequence\":\"4\"
,\"source\":\"1\"}
     */
    public static String getBNBPlaceOrderScript(boolean isTestnet) {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptData argOrderAddress = sac.getArgument(40);
        ScriptData argOrderSequence = sac.getArgument(8);
        ScriptData argSender = sac.getArgumentRightJustified(64);
        ScriptData argSide = sac.getArgument(1);
        ScriptData argQuoteToken = sac.getArgumentRightJustified(20);
        ScriptData argBaseToken = sac.getArgumentRightJustified(20);
        ScriptData argQuantity = sac.getArgument(8);
        ScriptData argPrice = sac.getArgument(8);
        ScriptData argIsImmediate = sac.getArgument(1);
        ScriptData argAccountNumber = sac.getArgument(8);
        ScriptData argSequence = sac.getArgument(8);
        ScriptData argSource = sac.getArgument(8);

        return "03000201"
                + //version=00 ScriptAssembler.hash=02=sha256 sign=01=ECDSA
                ScriptAssembler.setCoinType(0x02CA)
                + // set coinType to 02CA
                ScriptAssembler.copyString(HexUtil.toHexString("{\"account_number\":\""))
                + ScriptAssembler.baseConvert(argAccountNumber, Buffer.TRANSACTION, 0, ScriptAssembler.decimalCharset, ScriptAssembler.leftJustify)
                + (!isTestnet
                        ? ScriptAssembler.copyString(HexUtil.toHexString("\",\"chain_id\":\"Binance-Chain-Tigris\",\"data\":null,\"memo\":\"\",\"msgs\":[{\"id\":\""))
                        : ScriptAssembler.copyString(HexUtil.toHexString("\",\"chain_id\":\"Binance-Chain-Nile\",\"data\":null,\"memo\":\"\",\"msgs\":[{\"id\":\"")))
                + ScriptAssembler.copyRegularString(argOrderAddress)
                + ScriptAssembler.copyString(HexUtil.toHexString("-"))
                + ScriptAssembler.baseConvert(argOrderSequence, Buffer.TRANSACTION, 0, ScriptAssembler.decimalCharset, ScriptAssembler.leftJustify)
                + ScriptAssembler.copyString(HexUtil.toHexString("\",\"ordertype\":2,\"price\":"))
                + ScriptAssembler.baseConvert(argPrice, Buffer.TRANSACTION, 0, ScriptAssembler.decimalCharset, ScriptAssembler.leftJustify)
                + ScriptAssembler.copyString(HexUtil.toHexString(",\"quantity\":"))
                + ScriptAssembler.baseConvert(argQuantity, Buffer.TRANSACTION, 0, ScriptAssembler.decimalCharset, ScriptAssembler.leftJustify)
                + ScriptAssembler.copyString(HexUtil.toHexString(",\"sender\":\""))
                + ScriptAssembler.copyRegularString(argSender)
                + ScriptAssembler.copyString(HexUtil.toHexString("\",\"side\":"))
                + ScriptAssembler.switchString(argSide, Buffer.TRANSACTION, HexUtil.toHexString("1") + "," + HexUtil.toHexString("2"))
                + //0->"1" for buy, 1->"2" for sell
                ScriptAssembler.copyString(HexUtil.toHexString(",\"symbol\":\""))
                + ScriptAssembler.copyRegularString(argQuoteToken)
                + ScriptAssembler.copyString(HexUtil.toHexString("_"))
                + ScriptAssembler.copyRegularString(argBaseToken)
                + ScriptAssembler.copyString(HexUtil.toHexString("\",\"timeinforce\":"))
                + ScriptAssembler.switchString(argIsImmediate, Buffer.TRANSACTION, HexUtil.toHexString("1") + "," + HexUtil.toHexString("3"))
                + //0->"1" for GoodTillExpire, 1->"3" for ImmediateOrCancel
                ScriptAssembler.copyString(HexUtil.toHexString("}],\"sequence\":\""))
                + ScriptAssembler.baseConvert(argSequence, Buffer.TRANSACTION, 0, ScriptAssembler.decimalCharset, ScriptAssembler.leftJustify)
                + ScriptAssembler.copyString(HexUtil.toHexString("\",\"source\":\""))
                + ScriptAssembler.baseConvert(argSource, Buffer.TRANSACTION, 0, ScriptAssembler.decimalCharset, ScriptAssembler.leftJustify)
                + ScriptAssembler.copyString(HexUtil.toHexString("\"}"))
                + (!isTestnet ? ""
                        : ScriptAssembler.showWrap("BNB DEX", "TESTNET"))
                + ScriptAssembler.ifEqual(argSide, "00",
                        ScriptAssembler.showWrap("BNB DEX", "BUY"),
                        ScriptAssembler.showWrap("BNB DEX", "SELL")
                )
                + ScriptAssembler.showMessage(argQuoteToken)
                + ScriptAssembler.showAmount(argQuantity, 8)
                + ScriptAssembler.showMessage(argBaseToken)
                + ScriptAssembler.showAmount(argPrice, 8)
                + ScriptAssembler.showPressButton();
    }

    /*
{\"account_number\":\"37132\"
,\"chain_id\":\"Binance-Chain-Tigris\"
,\"data\":null
,\"memo\":\"\"
,\"msgs\":[{
   \"refid\":\"DA02D8838A1C948E57099B7AC12864B53296F5BF-5\"
  ,\"sender\":\"tbnb1mgpd3qu2rj2gu4cfndavz2ryk5efdadl9ze4xj\"
  ,\"symbol\":\"AAS-361_BNB\"}]
,\"sequence\":\"5\"
,\"source\":\"1\"}
     */
    public static String getBNBCancelOrderScript(boolean isTestnet) {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptData argOrderAddress = sac.getArgument(40);
        ScriptData argOrderSequence = sac.getArgument(8);
        ScriptData argSender = sac.getArgumentRightJustified(64);
        ScriptData argQuoteToken = sac.getArgumentRightJustified(20);
        ScriptData argBaseToken = sac.getArgumentRightJustified(20);
        ScriptData argAccountNumber = sac.getArgument(8);
        ScriptData argSequence = sac.getArgument(8);
        ScriptData argSource = sac.getArgument(8);

        return "03000201"
                + //version=00 ScriptAssembler.hash=02=sha256 sign=01=ECDSA
                ScriptAssembler.setCoinType(0x02CA)
                + // set coinType to 02CA
                ScriptAssembler.copyString(HexUtil.toHexString("{\"account_number\":\""))
                + ScriptAssembler.baseConvert(argAccountNumber, Buffer.TRANSACTION, 0, ScriptAssembler.decimalCharset, ScriptAssembler.leftJustify)
                + (!isTestnet
                        ? ScriptAssembler.copyString(HexUtil.toHexString("\",\"chain_id\":\"Binance-Chain-Tigris\",\"data\":null,\"memo\":\"\",\"msgs\":[{\"refid\":\""))
                        : ScriptAssembler.copyString(HexUtil.toHexString("\",\"chain_id\":\"Binance-Chain-Nile\",\"data\":null,\"memo\":\"\",\"msgs\":[{\"refid\":\"")))
                + ScriptAssembler.copyRegularString(argOrderAddress)
                + ScriptAssembler.copyString(HexUtil.toHexString("-"))
                + ScriptAssembler.baseConvert(argOrderSequence, Buffer.TRANSACTION, 0, ScriptAssembler.decimalCharset, ScriptAssembler.leftJustify)
                + ScriptAssembler.copyString(HexUtil.toHexString("\",\"sender\":\""))
                + ScriptAssembler.copyRegularString(argSender)
                + ScriptAssembler.copyString(HexUtil.toHexString("\",\"symbol\":\""))
                + ScriptAssembler.copyRegularString(argQuoteToken)
                + ScriptAssembler.copyString(HexUtil.toHexString("_"))
                + ScriptAssembler.copyRegularString(argBaseToken)
                + ScriptAssembler.copyString(HexUtil.toHexString("\"}],\"sequence\":\""))
                + ScriptAssembler.baseConvert(argSequence, Buffer.TRANSACTION, 0, ScriptAssembler.decimalCharset, ScriptAssembler.leftJustify)
                + ScriptAssembler.copyString(HexUtil.toHexString("\",\"source\":\""))
                + ScriptAssembler.baseConvert(argSource, Buffer.TRANSACTION, 0, ScriptAssembler.decimalCharset, ScriptAssembler.leftJustify)
                + ScriptAssembler.copyString(HexUtil.toHexString("\"}"))
                + (!isTestnet ? ""
                        : ScriptAssembler.showWrap("BNB", "TESTNET"))
                + ScriptAssembler.showWrap("CANCEL", "BNB?");
    }

    public static String getBSCScript() {
            ScriptArgumentComposer sac = new ScriptArgumentComposer();
            ScriptData argTo = sac.getArgument(20);
            ScriptData argValue = sac.getArgumentRightJustified(10);
            ScriptData argGasPrice = sac.getArgumentRightJustified(10);
            ScriptData argGasLimit = sac.getArgumentRightJustified(10);
            ScriptData argNonce = sac.getArgumentRightJustified(8);
            // ScriptData argChainId = sac.getArgumentRightJustified(2);
            // version=00 ScriptAssembler.hash=06=ScriptAssembler.Keccak256 sign=01=ECDSA
            return "03000601"
                            // set coinType to 3C
                            + ScriptAssembler.setCoinType(0x3C)
                            // temp byte for rlpList
                            + ScriptAssembler.copyString("C0")
                            // nonce
                            + ScriptAssembler.rlpString(argNonce)
                            // gasPrice
                            + ScriptAssembler.rlpString(argGasPrice)
                            // gasLimit
                            + ScriptAssembler.rlpString(argGasLimit)
                            // toAddress
                            + ScriptAssembler.copyString("94") + ScriptAssembler.copyArgument(argTo)
                            // value
                            + ScriptAssembler.rlpString(argValue)
                            // data
                            + ScriptAssembler.copyString("80")
                            // chainId v = 56
                            + ScriptAssembler.copyString("38", Buffer.EXTENDED)
                            + ScriptAssembler.rlpString(ScriptData.getDataBufferAll(Buffer.EXTENDED))
                            // r,s
                            + ScriptAssembler.copyString("8080") + ScriptAssembler.rlpList(1)
                            + ScriptAssembler.showMessage("BSC") + ScriptAssembler.showMessage("BNB")
                            + ScriptAssembler.copyString(HexUtil.toHexString("0x"), Buffer.FREE)
                            + ScriptAssembler.baseConvert(argTo, Buffer.FREE, 0, ScriptAssembler.hexadecimalCharset,
                                            ScriptAssembler.leftJustify)
                            + ScriptAssembler.showAddress(ScriptData.getDataBufferAll(Buffer.FREE))
                            + ScriptAssembler.showAmount(argValue, 18) + ScriptAssembler.showPressButton();
    }

    /*
     * f86a 1e 85 01718c7e00 83 030d40 94 86fa049857e0209aa7d9e616f7eb3b3b78ecfdb0
     * 80 b844 a9059cbb
     * 0000000000000000000000009e3a6e03298780078003d3965689b8f653ee87b1
     * 0000000000000000000000000000000000000000000000004563918244f40000 01 80 80
     */
    public static String getBEP20Script() {
            ScriptArgumentComposer sac = new ScriptArgumentComposer();
            ScriptData argTo = sac.getArgument(20);
            ScriptData argValue = sac.getArgument(12);
            ScriptData argGasPrice = sac.getArgumentRightJustified(10);
            ScriptData argGasLimit = sac.getArgumentRightJustified(10);
            ScriptData argNonce = sac.getArgumentRightJustified(8);
            ScriptData argTokenInfo = sac.getArgumentUnion(0, 29);
            ScriptData argDecimal = sac.getArgument(1);
            ScriptData argNameLength = sac.getArgument(1);
            ScriptData argName = sac.getArgumentVariableLength(7);
            ScriptData argContractAddress = sac.getArgument(20);
            ScriptData argSign = sac.getArgument(72);

            return "03000601" + // version=00 ScriptAssembler.hash=06=ScriptAssembler.Keccak256 sign=01=ECDSA
                            ScriptAssembler.setCoinType(0x3C) + // set coinType to 3C
                            ScriptAssembler.copyString("F800") + ScriptAssembler.rlpString(argNonce)
                            + ScriptAssembler.rlpString(argGasPrice) + ScriptAssembler.rlpString(argGasLimit)
                            + ScriptAssembler.copyString("94") + ScriptAssembler.copyArgument(argContractAddress)
                            + ScriptAssembler.copyString("80B844a9059cbb000000000000000000000000") + // value = 0 ,
                                                                                                     // dataLength = 68
                            ScriptAssembler.copyArgument(argTo)
                            + ScriptAssembler.copyString("0000000000000000000000000000000000000000")
                            + ScriptAssembler.copyArgument(argValue)
                            // chainId v = 56
                            + ScriptAssembler.copyString("38", Buffer.EXTENDED)
                            + ScriptAssembler.rlpString(ScriptData.getDataBufferAll(Buffer.EXTENDED))
                            + ScriptAssembler.copyString("8080") + ScriptAssembler.rlpList(2)
                            + ScriptAssembler.showMessage("BSC")
                            + ScriptAssembler.ifSigned(argTokenInfo, argSign, "",
                                            ScriptAssembler.copyString(HexUtil.toHexString("@"), Buffer.FREE))
                            + ScriptAssembler.setBufferInt(argNameLength, 1, 7)
                            + ScriptAssembler.copyArgument(argName, Buffer.FREE)
                            + ScriptAssembler.showMessage(ScriptData.getDataBufferAll(Buffer.FREE))
                            + ScriptAssembler.clearBuffer(Buffer.FREE)
                            + ScriptAssembler.copyString(HexUtil.toHexString("0x"), Buffer.FREE)
                            + ScriptAssembler.baseConvert(argTo, Buffer.FREE, 0, ScriptAssembler.hexadecimalCharset,
                                            ScriptAssembler.leftJustify)
                            + ScriptAssembler.showAddress(ScriptData.getDataBufferAll(Buffer.FREE))
                            + ScriptAssembler.setBufferInt(argDecimal, 0, 20)
                            + ScriptAssembler.showAmount(argValue, 1000) + ScriptAssembler.showPressButton();
    }

    public static String getBSCSmartContractBlindScript() {
            ScriptArgumentComposer sac = new ScriptArgumentComposer();
            ScriptData argTo = sac.getArgument(20);
            ScriptData argValue = sac.getArgumentRightJustified(10);
            ScriptData argGasPrice = sac.getArgumentRightJustified(10);
            ScriptData argGasLimit = sac.getArgumentRightJustified(10);
            ScriptData argNonce = sac.getArgumentRightJustified(8);
            ScriptData argData = sac.getArgumentAll();

            return "03000601" + // version=00 ScriptAssembler.hash=06=ScriptAssembler.Keccak256 sign=01=ECDSA
                            ScriptAssembler.setCoinType(0x3C) + // set coinType to 3C
                            ScriptAssembler.copyString("F800") + ScriptAssembler.rlpString(argNonce) + // nonce
                            ScriptAssembler.rlpString(argGasPrice) + // gasPrice
                            ScriptAssembler.rlpString(argGasLimit) + // gasLimit
                            ScriptAssembler.copyString("94") + ScriptAssembler.copyArgument(argTo) + // toAddress
                            ScriptAssembler.rlpString(argValue) + // value
                            ScriptAssembler.rlpString(argData)
                            // chainId v = 56
                            + ScriptAssembler.copyString("38", Buffer.EXTENDED)
                            + ScriptAssembler.rlpString(ScriptData.getDataBufferAll(Buffer.EXTENDED))

                            + ScriptAssembler.copyString("8080") + ScriptAssembler.rlpList(2)
                            + ScriptAssembler.showMessage("BSC") + ScriptAssembler.showWrap("SMART", "")
                            + ScriptAssembler.showPressButton();
    }

    public static String getBSCMessageBlindScript() {
            ScriptArgumentComposer sac = new ScriptArgumentComposer();
            ScriptData argMessage = sac.getArgumentAll();

            return "03000601" + // version=00 ScriptAssembler.hash=06=ScriptAssembler.Keccak256 sign=01=ECDSA
                            ScriptAssembler.setCoinType(0x3C) + // set coinType to 3C
                            ScriptAssembler.copyString("19457468657265756D205369676E6564204D6573736167653A0A3332")
                            + ScriptAssembler.hash(argMessage, Buffer.TRANSACTION, ScriptAssembler.Keccak256)
                            + ScriptAssembler.showMessage("BSC") + ScriptAssembler.showWrap("MESSAGE", "")
                            + ScriptAssembler.showPressButton();
    }

    public static String getBSCTypedDataBlindScript() {
            ScriptArgumentComposer sac = new ScriptArgumentComposer();
            ScriptData argDomainSeparator = sac.getArgument(32);
            ScriptData argMessage = sac.getArgumentAll();

            return "03000601" + // version=00 ScriptAssembler.hash=06=ScriptAssembler.Keccak256 sign=01=ECDSA
                            ScriptAssembler.setCoinType(0x3C) + // set coinType to 3C
                            ScriptAssembler.copyString("1901") + ScriptAssembler.copyArgument(argDomainSeparator)
                            + ScriptAssembler.hash(argMessage, Buffer.TRANSACTION, ScriptAssembler.Keccak256)
                            + ScriptAssembler.showMessage("BSC") + ScriptAssembler.showWrap("TYPED", "DATA")
                            + ScriptAssembler.showPressButton();
    }

}
