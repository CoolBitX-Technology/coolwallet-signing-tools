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
import com.coolbitx.wallet.signing.utils.ScriptAssembler.HashType;
import com.coolbitx.wallet.signing.utils.ScriptAssembler.SignType;
import com.coolbitx.wallet.signing.utils.ScriptData.Buffer;

public class BnbScript {

    public static void listAll() {
        System.out.println("Bnb: \n" + getBNBScript(false) + "\n");
        System.out.println("Bnb Bep2: \n" + getBEP2Script(false) + "\n");
        System.out.println("Bnb Place Order: \n" + getBNBPlaceOrderScript(false) + "\n");
        System.out.println("Bnb Cancel Order: \n" + getBNBCancelOrderScript(false) + "\n");
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

        ScriptAssembler scriptAsb = new ScriptAssembler();
        String script = scriptAsb
                .setCoinType(0x02CA)
                // set coinType to 02CA
                .copyString(HexUtil.toHexString("{\"account_number\":\""))
                .baseConvert(argAccountNumber, Buffer.TRANSACTION, 0, ScriptAssembler.decimalCharset, ScriptAssembler.leftJustify)
                .getScript();
        script = (!isTestnet ? scriptAsb.copyString(HexUtil.toHexString("\",\"chain_id\":\"Binance-Chain-Tigris\",\"data\":null,\"memo\":\""))
                : scriptAsb.copyString(HexUtil.toHexString("\",\"chain_id\":\"Binance-Chain-Nile\",\"data\":null,\"memo\":\"")))
                .copyRegularString(argMemo)
                .copyString(HexUtil.toHexString("\",\"msgs\":[{\"inputs\":[{\"address\":\""))
                .copyRegularString(argFrom)
                .copyString(HexUtil.toHexString("\",\"coins\":[{\"amount\":"))
                .baseConvert(argValue, Buffer.TRANSACTION, 0, ScriptAssembler.decimalCharset, ScriptAssembler.leftJustify)
                .copyString(HexUtil.toHexString(",\"denom\":\"BNB\"}]}],\"outputs\":[{\"address\":\""))
                .copyRegularString(argTo)
                .copyString(HexUtil.toHexString("\",\"coins\":[{\"amount\":"))
                .baseConvert(argValue, Buffer.TRANSACTION, 0, ScriptAssembler.decimalCharset, ScriptAssembler.leftJustify)
                .copyString(HexUtil.toHexString(",\"denom\":\"BNB\"}]}]}],\"sequence\":\""))
                .baseConvert(argSequence, Buffer.TRANSACTION, 0, ScriptAssembler.decimalCharset, ScriptAssembler.leftJustify)
                .copyString(HexUtil.toHexString("\",\"source\":\""))
                .baseConvert(argSource, Buffer.TRANSACTION, 0, ScriptAssembler.decimalCharset, ScriptAssembler.leftJustify)
                .copyString(HexUtil.toHexString("\"}"))
                .getScript();
        script = (!isTestnet ? scriptAsb.showMessage("BNB")
                : scriptAsb.showWrap("BNB", "TESTNET"))
                .showAddress(argTo)
                .showAmount(argValue, 8)
                .showPressButton()
                //version=00 ScriptAssembler.hash=02=sha256 sign=01=ECDSA
                .setHeader(HashType.SHA256, SignType.ECDSA)
                .getScript();
        return script;
    }

    public static String BNBScriptSignature = "FA0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000";
    public static String BNBTestScriptSignature = "0000304402204568AC219BC19BCA3579FF35C5D513BC0EF221373162615D6329B6F1D112F1FB02203720E18E0EB27A44C52B647F91EC6FF5CCC263373A28039392CDCF05F00B462B";

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

        ScriptAssembler scriptAsb = new ScriptAssembler();
        String script = scriptAsb
                .setCoinType(0x02CA)
                // set coinType to 02CA
                .copyString(HexUtil.toHexString("{\"account_number\":\""))
                .baseConvert(argAccountNumber, Buffer.TRANSACTION, 0, ScriptAssembler.decimalCharset, ScriptAssembler.leftJustify)
                .getScript();
        script = (!isTestnet ? scriptAsb.copyString(HexUtil.toHexString("\",\"chain_id\":\"Binance-Chain-Tigris\",\"data\":null,\"memo\":\""))
                : scriptAsb.copyString(HexUtil.toHexString("\",\"chain_id\":\"Binance-Chain-Nile\",\"data\":null,\"memo\":\"")))
                .copyRegularString(argMemo)
                .copyString(HexUtil.toHexString("\",\"msgs\":[{\"inputs\":[{\"address\":\""))
                .copyRegularString(argFrom)
                .copyString(HexUtil.toHexString("\",\"coins\":[{\"amount\":"))
                .baseConvert(argValue, Buffer.TRANSACTION, 0, ScriptAssembler.decimalCharset, ScriptAssembler.leftJustify)
                .copyString(HexUtil.toHexString(",\"denom\":\""))
                .copyRegularString(argTokenName, Buffer.CACHE2)
                .copyString(HexUtil.toHexString("-"), Buffer.CACHE2)
                .copyRegularString(argTokenCheck, Buffer.CACHE2)
                .copyArgument(ScriptData.getDataBufferAll(Buffer.CACHE2))
                .copyString(HexUtil.toHexString("\"}]}],\"outputs\":[{\"address\":\""))
                .copyRegularString(argTo)
                .copyString(HexUtil.toHexString("\",\"coins\":[{\"amount\":"))
                .baseConvert(argValue, Buffer.TRANSACTION, 0, ScriptAssembler.decimalCharset, ScriptAssembler.leftJustify)
                .copyString(HexUtil.toHexString(",\"denom\":\""))
                .copyArgument(ScriptData.getDataBufferAll(Buffer.CACHE2))
                .copyString(HexUtil.toHexString("\"}]}]}],\"sequence\":\""))
                .baseConvert(argSequence, Buffer.TRANSACTION, 0, ScriptAssembler.decimalCharset, ScriptAssembler.leftJustify)
                .copyString(HexUtil.toHexString("\",\"source\":\""))
                .baseConvert(argSource, Buffer.TRANSACTION, 0, ScriptAssembler.decimalCharset, ScriptAssembler.leftJustify)
                .copyString(HexUtil.toHexString("\"}"))
                .clearBuffer(Buffer.CACHE2)
                .showMessage("BNB")
                .copyRegularString(argTokenName, Buffer.CACHE2)
                .showMessage(ScriptData.getDataBufferAll(Buffer.CACHE2))
                .getScript();
        script = (!isTestnet
                ? script += ""
                : scriptAsb.showWrap("BEP2", "TESTNET").getScript());
        script = scriptAsb
                .showAddress(argTo)
                .showAmount(argValue, 8)
                .showPressButton()
                //version=00 ScriptAssembler.hash=02=sha256 sign=01=ECDSA
                .setHeader(HashType.SHA256, SignType.ECDSA)
                .getScript();
        return script;
    }

    public static String getBEP2ScriptSignature = "0000304402203183C36E6E4E20A2AAED4E1E3518EBDE01FA0382B680168F80636B4128C6ECA3022064D214390B1F572C1392FCC8230D0336CAB06DEF6EECB8C8F4115841DD93CDF2";

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

        ScriptAssembler scriptAsb = new ScriptAssembler();
        String script = scriptAsb
                .setCoinType(0x02CA)
                // set coinType to 02CA
                .copyString(HexUtil.toHexString("{\"account_number\":\""))
                .baseConvert(argAccountNumber, Buffer.TRANSACTION, 0, ScriptAssembler.decimalCharset, ScriptAssembler.leftJustify)
                .getScript();
        script = (!isTestnet
                ? scriptAsb.copyString(HexUtil.toHexString("\",\"chain_id\":\"Binance-Chain-Tigris\",\"data\":null,\"memo\":\"\",\"msgs\":[{\"id\":\""))
                : scriptAsb.copyString(HexUtil.toHexString("\",\"chain_id\":\"Binance-Chain-Nile\",\"data\":null,\"memo\":\"\",\"msgs\":[{\"id\":\"")))
                .copyRegularString(argOrderAddress)
                .copyString(HexUtil.toHexString("-"))
                .baseConvert(argOrderSequence, Buffer.TRANSACTION, 0, ScriptAssembler.decimalCharset, ScriptAssembler.leftJustify)
                .copyString(HexUtil.toHexString("\",\"ordertype\":2,\"price\":"))
                .baseConvert(argPrice, Buffer.TRANSACTION, 0, ScriptAssembler.decimalCharset, ScriptAssembler.leftJustify)
                .copyString(HexUtil.toHexString(",\"quantity\":"))
                .baseConvert(argQuantity, Buffer.TRANSACTION, 0, ScriptAssembler.decimalCharset, ScriptAssembler.leftJustify)
                .copyString(HexUtil.toHexString(",\"sender\":\""))
                .copyRegularString(argSender)
                .copyString(HexUtil.toHexString("\",\"side\":"))
                .switchString(argSide, Buffer.TRANSACTION, HexUtil.toHexString("1") + "," + HexUtil.toHexString("2"))
                //0->"1" for buy, 1->"2" for sell
                .copyString(HexUtil.toHexString(",\"symbol\":\""))
                .copyRegularString(argQuoteToken)
                .copyString(HexUtil.toHexString("_"))
                .copyRegularString(argBaseToken)
                .copyString(HexUtil.toHexString("\",\"timeinforce\":"))
                .switchString(argIsImmediate, Buffer.TRANSACTION, HexUtil.toHexString("1") + "," + HexUtil.toHexString("3"))
                //0->"1" for GoodTillExpire, 1->"3" for ImmediateOrCancel
                .copyString(HexUtil.toHexString("}],\"sequence\":\""))
                .baseConvert(argSequence, Buffer.TRANSACTION, 0, ScriptAssembler.decimalCharset, ScriptAssembler.leftJustify)
                .copyString(HexUtil.toHexString("\",\"source\":\""))
                .baseConvert(argSource, Buffer.TRANSACTION, 0, ScriptAssembler.decimalCharset, ScriptAssembler.leftJustify)
                .copyString(HexUtil.toHexString("\"}"))
                .getScript();
        script = !isTestnet ? script + ""
                : scriptAsb.showWrap("BNB DEX", "TESTNET").getScript();
        script = scriptAsb
                .ifEqual(argSide, "00", new ScriptAssembler().showWrap("BNB DEX", "BUY").getScript(), new ScriptAssembler().showWrap("BNB DEX", "SELL").getScript())
                .showMessage(argQuoteToken)
                .showAmount(argQuantity, 8)
                .showMessage(argBaseToken)
                .showAmount(argPrice, 8)
                .showPressButton()
                //version=00 ScriptAssembler.hash=02=sha256 sign=01=ECDSA
                .setHeader(HashType.SHA256, SignType.ECDSA)
                .getScript();
        return script;
    }

    public static String BNBPlaceOrderScriptSignature = "00003044022073A02061F441CE9EA379D31A2303C78F4F3770CBBAAD4C3158DC7C829D74B13C022003B9D4CC8722883B732EDD2E163D2B9E830A202BB23ED73F092944BFB6E6BF58";
    public static String BNBPlaceOrderTestScriptSignature = "304502203624FE6786C375BDC19EC72F73EE0A37C5675572C22995D3455F498F3C4EE6CE022100FA880FA0B2306F85CC3FE34F56CAC62B9AB37EDEB16600225823297E5A8205B8";

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

        ScriptAssembler scriptAsb = new ScriptAssembler();
        String script = scriptAsb
                .setCoinType(0x02CA)
                // set coinType to 02CA
                .copyString(HexUtil.toHexString("{\"account_number\":\""))
                .baseConvert(argAccountNumber, Buffer.TRANSACTION, 0, ScriptAssembler.decimalCharset, ScriptAssembler.leftJustify)
                .getScript();
        script = (!isTestnet
                ? scriptAsb.copyString(HexUtil.toHexString("\",\"chain_id\":\"Binance-Chain-Tigris\",\"data\":null,\"memo\":\"\",\"msgs\":[{\"refid\":\""))
                : scriptAsb.copyString(HexUtil.toHexString("\",\"chain_id\":\"Binance-Chain-Nile\",\"data\":null,\"memo\":\"\",\"msgs\":[{\"refid\":\"")))
                .copyRegularString(argOrderAddress)
                .copyString(HexUtil.toHexString("-"))
                .baseConvert(argOrderSequence, Buffer.TRANSACTION, 0, ScriptAssembler.decimalCharset, ScriptAssembler.leftJustify)
                .copyString(HexUtil.toHexString("\",\"sender\":\""))
                .copyRegularString(argSender)
                .copyString(HexUtil.toHexString("\",\"symbol\":\""))
                .copyRegularString(argQuoteToken)
                .copyString(HexUtil.toHexString("_"))
                .copyRegularString(argBaseToken)
                .copyString(HexUtil.toHexString("\"}],\"sequence\":\""))
                .baseConvert(argSequence, Buffer.TRANSACTION, 0, ScriptAssembler.decimalCharset, ScriptAssembler.leftJustify)
                .copyString(HexUtil.toHexString("\",\"source\":\""))
                .baseConvert(argSource, Buffer.TRANSACTION, 0, ScriptAssembler.decimalCharset, ScriptAssembler.leftJustify)
                .copyString(HexUtil.toHexString("\"}"))
                .getScript();
        script = (!isTestnet ? script += ""
                : scriptAsb.showWrap("BNB", "TESTNET").getScript());
        script = scriptAsb
                .showWrap("CANCEL", "BNB?")
                //version=00 ScriptAssembler.hash=02=sha256 sign=01=ECDSA
                .setHeader(HashType.SHA256, SignType.ECDSA)
                .getScript();
        return script;
    }

    public static String BNBCancelOrderScriptSignature = "003045022029B49D1404F5CE4988AC9753E00683D2C7E1B106F4A80F6BCA9F9DB1546BE2F0022100A20805E8EA152437303335E82AF48DF6462BFAE3B738AA0ED39E2ECC39AFD809";
}
