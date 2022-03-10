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
        script = (!isTestnet? scriptAsb.showMessage("BNB")
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
        script = (!isTestnet? scriptAsb.copyString(HexUtil.toHexString("\",\"chain_id\":\"Binance-Chain-Tigris\",\"data\":null,\"memo\":\""))
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
        script = !isTestnet? script + ""
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
        script = (!isTestnet? script += "" 
                : scriptAsb.showWrap("BNB", "TESTNET").getScript());
        script = scriptAsb
                .showWrap("CANCEL", "BNB?")
                //version=00 ScriptAssembler.hash=02=sha256 sign=01=ECDSA
                .setHeader(HashType.SHA256, SignType.ECDSA)
                .getScript();
        return script;
    }

    public static String BNBCancelOrderScriptSignature = "003045022029B49D1404F5CE4988AC9753E00683D2C7E1B106F4A80F6BCA9F9DB1546BE2F0022100A20805E8EA152437303335E82AF48DF6462BFAE3B738AA0ED39E2ECC39AFD809";

    public static String getBSCScript() {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptData argTo = sac.getArgument(20);
        ScriptData argValue = sac.getArgumentRightJustified(10);
        ScriptData argGasPrice = sac.getArgumentRightJustified(10);
        ScriptData argGasLimit = sac.getArgumentRightJustified(10);
        ScriptData argNonce = sac.getArgumentRightJustified(8);
        // ScriptData argChainId = sac.getArgumentRightJustified(2);
        
        String script = new ScriptAssembler()
                // set coinType to 3C
                .setCoinType(0x3C)
                // temp byte for rlpList
                .copyString("C0")
                // nonce
                .rlpString(argNonce)
                // gasPrice
                .rlpString(argGasPrice)
                // gasLimit
                .rlpString(argGasLimit)
                // toAddress
                .copyString("94").copyArgument(argTo)
                // value
                .rlpString(argValue)
                // data
                .copyString("80")
                // chainId v = 56
                .copyString("38", Buffer.CACHE1)
                .rlpString(ScriptData.getDataBufferAll(Buffer.CACHE1))
                // r,s
                .copyString("8080")
                .rlpList(1)
                .showMessage("BSC")
                .showMessage("BNB")
                .copyString(HexUtil.toHexString("0x"), Buffer.CACHE2)
                .baseConvert(argTo, Buffer.CACHE2, 0, ScriptAssembler.hexadecimalCharset,
                        ScriptAssembler.zeroInherit)
                .showAddress(ScriptData.getDataBufferAll(Buffer.CACHE2))
                .showAmount(argValue, 18)
                .showPressButton()
                // version=00 ScriptAssembler.hash=06=ScriptAssembler.Keccak256 sign=01=ECDSA
                .setHeader(HashType.Keccak256, SignType.ECDSA)
                .getScript();
        return script;
    }

    public static String BSCScriptSignature = "00304502206A9D1E267D9AC65B28FFB49286F73041D3FF3834F68C5CAB1A700607C57DB052022100BB46FD3AB7A402AF163FA90EB0348A3AE14CF51E4CA4364931104CD1996F99E6";

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

        String script = new ScriptAssembler()
                .setCoinType(0x3C)
                // set coinType to 3C
                .copyString("F800") 
                .rlpString(argNonce)
                .rlpString(argGasPrice) 
                .rlpString(argGasLimit)
                .copyString("94") 
                .copyArgument(argContractAddress)
                .copyString("80B844a9059cbb000000000000000000000000")
                // value = 0 ,
                // dataLength = 68
                .copyArgument(argTo)
                .copyString("0000000000000000000000000000000000000000")
                .copyArgument(argValue)
                // chainId v = 56
                .copyString("38", Buffer.CACHE1)
                .rlpString(ScriptData.getDataBufferAll(Buffer.CACHE1))
                .copyString("8080") 
                .rlpList(2)
                .showMessage("BSC")
                .ifSigned(argTokenInfo, argSign, "",
                        new ScriptAssembler().copyString(HexUtil.toHexString("@"), Buffer.CACHE2).getScript())
                .setBufferInt(argNameLength, 1, 7)
                .copyArgument(argName, Buffer.CACHE2)
                .showMessage(ScriptData.getDataBufferAll(Buffer.CACHE2))
                .clearBuffer(Buffer.CACHE2)
                .copyString(HexUtil.toHexString("0x"), Buffer.CACHE2)
                .baseConvert(argTo, Buffer.CACHE2, 0, ScriptAssembler.hexadecimalCharset,
                        ScriptAssembler.zeroInherit)
                .showAddress(ScriptData.getDataBufferAll(Buffer.CACHE2))
                .setBufferInt(argDecimal, 0, 20)
                .showAmount(argValue, 1000) 
                .showPressButton()
                // version=00 ScriptAssembler.hash=06=ScriptAssembler.Keccak256 sign=01=ECDSA
                .setHeader(HashType.Keccak256, SignType.ECDSA)
                .getScript();
        return script;
    }

    public static String BEP20ScriptSignature = "00304502202B33814A04EE43EFC342DD3345652DAF34606EAD6599EF1A3C45F78727CC7283022100E20E6E30C3D5B8E04B5DD5C90FBFE064077CA9FCBF2689DDD2020FF51A1D69EE";

    public static String getBSCSmartContractBlindScript() {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptData argTo = sac.getArgument(20);
        ScriptData argValue = sac.getArgumentRightJustified(10);
        ScriptData argGasPrice = sac.getArgumentRightJustified(10);
        ScriptData argGasLimit = sac.getArgumentRightJustified(10);
        ScriptData argNonce = sac.getArgumentRightJustified(8);
        ScriptData argData = sac.getArgumentAll();

        String script = new ScriptAssembler()
                .setCoinType(0x3C)
                // set coinType to 3C
                .copyString("F800").rlpString(argNonce)
                // nonce
                .rlpString(argGasPrice)
                // gasPrice
                .rlpString(argGasLimit)
                // gasLimit
                .copyString("94").copyArgument(argTo)
                // toAddress
                .rlpString(argValue)
                // value
                .rlpString(argData)
                // chainId v = 56
                .copyString("38", Buffer.CACHE1)
                .rlpString(ScriptData.getDataBufferAll(Buffer.CACHE1))
                .copyString("8080") 
                .rlpList(2)
                .showMessage("BSC") 
                .showWrap("SMART", "")
                .showPressButton()
                // version=00 ScriptAssembler.hash=06=ScriptAssembler.Keccak256 sign=01=ECDSA
                .setHeader(HashType.Keccak256, SignType.ECDSA)
                .getScript();
        return script;
    }

    public static String BSCSmartContractBlindScriptSignature = "000030440220429DF67EB2A0D1ED5681F912FCCE313C457829D7A76123B59F427E94A2FD8B0A02204FCC18E46AB820323D2CA5ED52FCEAA5DFFF70A3BF2DC4D060E30CFDCAE08D99";

    public static String getBSCMessageBlindScript() {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptData argMessage = sac.getArgumentAll();

        String script = new ScriptAssembler()
                .setCoinType(0x3C)
                // set coinType to 3C
                .copyString("19457468657265756D205369676E6564204D6573736167653A0A")
                .copyArgument(argMessage)
                .showMessage("BSC") 
                .showWrap("MESSAGE", "")
                .showPressButton()
                // version=00 ScriptAssembler.hash=06=ScriptAssembler.Keccak256 sign=01=ECDSA
                .setHeader(HashType.Keccak256, SignType.ECDSA)
                .getScript();
        return script;
    }

    public static String BSCMessageBlindScriptSignature = "0000304402204C3BC0B32823EE53EBAA156DA9C6145029652E42D6FF1CEFF777FEC37738920C02200C52F02969FD648F160CCEC52D61403C854B694ABB059C17C70FBDA245E49CCC";

    public static String getBSCTypedDataBlindScript() {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptData argDomainSeparator = sac.getArgument(32);
        ScriptData argMessage = sac.getArgumentAll();

        String script = new ScriptAssembler()
                .setCoinType(0x3C)
                // set coinType to 3C
                .copyString("1901") 
                .copyArgument(argDomainSeparator)
                .hash(argMessage, Buffer.TRANSACTION, ScriptAssembler.Keccak256)
                .showMessage("BSC") 
                .showWrap("TYPED", "DATA")
                .showPressButton()
                // version=00 ScriptAssembler.hash=06=ScriptAssembler.Keccak256 sign=01=ECDSA
                .setHeader(HashType.Keccak256, SignType.ECDSA)
                .getScript();
        return script;
    }

    public static String BSCTypedDataBlindScriptSignature = "003045022008935AF6BA11B9F720E59BE61AFF6F62A7A48FF2A39863AFD8B3920F355A1265022100E81EA86AC2FA3864CBC8773B10AF550B91F6A0E1FB68512DF32D8D35BC9FF3C8";

}
