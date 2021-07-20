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
        System.out.println("BNB : ");
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


}
