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

public class BtcFamilyScript {

    public static void listAll() {
        System.out.println("Btc: \n" + getBTCOutputScript(Coin.BTC, false) + "\n");
        System.out.println("Ltc: \n" + getBTCOutputScript(Coin.LTC, false) + "\n");
        System.out.println("Zen: \n" + getBTCOutputScript(Coin.ZEN, false) + "\n");
        System.out.println("Bch: \n" + getBTCOutputScript(Coin.BCH, false) + "\n");
        System.out.println("Usdt: \n" + getBTCOutputScript(Coin.USDT, false) + "\n");
    }

    /*
01000000 //const
01 //inputCount = const
88fd8402286041ab66d230bd23592b75493e5be21f8694c6491440aad7117bfc 00000000 //block place = constLength randomData
19 76a914027d3f3c7c3cfa357d97fbe7d80d70f4ab1cac0d88ac //script
ffffffff //sequence = const

02 //outputCount
1027000000000000 // amount
19 76a91439af5ea4dd0b3b9771945596fa3d4ed3ff76170588ac
8818000000000000 // change amount
19 76a914c71def97f262b778997b3cb64e1d72c4a4d9de4188ac

00000000 // timelock = const
81000000 // ScriptAssembler.hashType
     */
    public enum Coin {
        BTC("BTC", 0x00), LTC("LTC", 0x02), ZEN("ZEN", 0x79), BCH("BCH", 0x91), USDT("USDT", 0x00);
        private final String name;
        private final int coinType;

        private Coin(String name, int coinType) {
            this.name = name;
            this.coinType = coinType;
        }

        public String getName() {
            return name;
        }

        public int getCoinType() {
            return coinType;
        }

    }

    public static String getBTCOutputScript(Coin coin, boolean isTestnet) {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptData argOutputScriptType = sac.getArgument(1);
        ScriptData argUsdtDust = sac.getArgumentUnion(6, 2);
        ScriptData argOutputAmount = sac.getArgument(8);
        ScriptData argOutputDest20 = sac.getArgumentUnion(12, 20);
        ScriptData argOutputDest32 = sac.getArgument(32);
        ScriptData argOutputBlockHash = coin == Coin.ZEN ? sac.getArgument(32) : null;
        ScriptData argOutputBlockHeight = coin == Coin.ZEN ? sac.getArgument(3) : null;
        ScriptData argHaveChange = sac.getArgument(1);
        ScriptData argChangeScriptType = sac.getArgument(1);
        ScriptData argChangeAmount = sac.getArgument(8);
        ScriptData argChangePath = sac.getArgument(21);
        ScriptData argChangeBlockHash = coin == Coin.ZEN ? sac.getArgument(32) : null;
        ScriptData argChangeBlockHeight = coin == Coin.ZEN ? sac.getArgument(3) : null;
        ScriptData argHashPrevoutSequence = sac.getArgument(64);
        ScriptData argLtcNewAddress = sac.getArgumentUnion(0, 1);
        ScriptData argUsdtAmount = sac.getArgumentUnion(0, 8);

        boolean isUSDT = false;
        if (coin == Coin.USDT) {
            isUSDT = true;
        }

        boolean supportSegwit = coin.getCoinType() == Coin.BTC.getCoinType() || coin == Coin.LTC;

        String hrp = coin.getCoinType() == Coin.BTC.getCoinType() ? (!isTestnet ? "bc" : "tb") : (!isTestnet ? "ltc" : "tltc");
        String hrpExpand = "";
        for (int i = 0; i < hrp.length(); i++) {
            hrpExpand += HexUtil.toHexString((hrp.charAt(i) >> 5) & 7, 1);
        }
        hrpExpand += "00";
        for (int i = 0; i < hrp.length(); i++) {
            hrpExpand += HexUtil.toHexString(hrp.charAt(i) & 31, 1);
        }

        String outputScript = supportSegwit
                ? new ScriptAssembler().ifEqual(argChangeScriptType, "02", // if P2WPKH
                        new ScriptAssembler().hash(ScriptData.getDataBufferAll(Buffer.CACHE2), Buffer.TRANSACTION, ScriptAssembler.HashType.SHA256RipeMD160).getScript(),
                        "").getScript()
                : "";

        String ifCoinZen = coin != Coin.ZEN ? ""
                : new ScriptAssembler()
                        .copyString("20")
                        .copyArgument(argChangeBlockHash)
                        .copyString("03")
                        .copyArgument(argChangeBlockHeight)
                        .copyString("B4")
                        .getScript();

        String nonBchNonSegwit = supportSegwit
                ? new ScriptAssembler().copyString(hrpExpand + "00", Buffer.CACHE2)
                        .ifEqual(argOutputScriptType, "02",
                                new ScriptAssembler().baseConvert(argOutputDest20, Buffer.CACHE2, 32, ScriptAssembler.binary32Charset, ScriptAssembler.bitLeftJustify8to5).getScript(),
                                new ScriptAssembler().baseConvert(argOutputDest32, Buffer.CACHE2, 52, ScriptAssembler.binary32Charset, ScriptAssembler.bitLeftJustify8to5).getScript()
                        )
                        .copyString("000000000000", Buffer.CACHE2)
                        .bech32Polymod(ScriptData.getDataBufferAll(Buffer.CACHE2), Buffer.CACHE1)
                        .clearBuffer(Buffer.CACHE2)
                        .copyString(HexUtil.toHexString(hrp + "1q"), Buffer.CACHE2)
                        .ifEqual(argOutputScriptType, "02",
                                new ScriptAssembler().baseConvert(argOutputDest20, Buffer.CACHE2, 32, ScriptAssembler.base32BitcoinCashCharset, ScriptAssembler.bitLeftJustify8to5).getScript(),
                                new ScriptAssembler().baseConvert(argOutputDest32, Buffer.CACHE2, 52, ScriptAssembler.base32BitcoinCashCharset, ScriptAssembler.bitLeftJustify8to5).getScript()
                        )
                        .baseConvert(ScriptData.getDataBufferAll(Buffer.CACHE1), Buffer.CACHE2, 6, ScriptAssembler.base32BitcoinCashCharset, 0)
                        .showAddress(ScriptData.getDataBufferAll(Buffer.CACHE2))
                        .getScript()
                : ScriptAssembler.throwSEError;

        ScriptAssembler scriptAsb = new ScriptAssembler();

        String script = scriptAsb
                .setCoinType(coin.getCoinType())
                .switchString(argHaveChange, Buffer.TRANSACTION, "01,02")
                .getScript();

        script = (!isUSDT
                ? scriptAsb.baseConvert(argOutputAmount, Buffer.TRANSACTION, 8, ScriptAssembler.binaryCharset, ScriptAssembler.littleEndian)
                : scriptAsb.baseConvert(argUsdtDust, Buffer.TRANSACTION, 2, ScriptAssembler.binaryCharset, ScriptAssembler.littleEndian)
                        .copyString("000000000000"))
                .btcScript(argOutputScriptType, supportSegwit ? 4 : (coin == Coin.ZEN ? 79 : 2),
                        (supportSegwit ? new ScriptAssembler().ifEqual(argOutputScriptType, "03",
                                        new ScriptAssembler().copyArgument(argOutputDest32).getScript(),
                                        new ScriptAssembler().copyArgument(argOutputDest20).getScript()).getScript()
                                : new ScriptAssembler().copyArgument(argOutputDest20).getScript())
                )
                .getScript();

        script = coin != Coin.ZEN
                ? script + ""
                : scriptAsb.copyString("20")
                        .copyArgument(argOutputBlockHash)
                        .copyString("03")
                        .copyArgument(argOutputBlockHeight)
                        .copyString("B4")
                        .getScript();

        script = !isUSDT
                ? script + ""
                : scriptAsb.copyString("0000000000000000166a146f6d6e69000000000000001f")
                        .copyArgument(argUsdtAmount)
                        .getScript();

        script = scriptAsb.ifEqual(argHaveChange, "01", // if haveChange
                new ScriptAssembler().baseConvert(argChangeAmount, Buffer.TRANSACTION, 8, ScriptAssembler.binaryCharset, ScriptAssembler.littleEndian)
                        .btcScript(argChangeScriptType, supportSegwit ? 3 : (coin == Coin.ZEN ? 79 : 2),
                                new ScriptAssembler().derivePublicKey(argChangePath, Buffer.CACHE2)
                                        .ifEqual(argChangeScriptType, "00", // if P2PKH
                                                new ScriptAssembler().hash(ScriptData.getDataBufferAll(Buffer.CACHE2), Buffer.TRANSACTION, ScriptAssembler.HashType.SHA256RipeMD160).getScript(),
                                                "")
                                        .ifEqual(argChangeScriptType, "01", // if P2WPKH in P2SH
                                                new ScriptAssembler().copyString("0014", Buffer.CACHE1)
                                                        .hash(ScriptData.getDataBufferAll(Buffer.CACHE2), Buffer.CACHE1, ScriptAssembler.HashType.SHA256RipeMD160)
                                                        .hash(ScriptData.getDataBufferAll(Buffer.CACHE1), Buffer.TRANSACTION, ScriptAssembler.HashType.SHA256RipeMD160).getScript(),
                                                "")
                                        .getScript()
                                + outputScript)
                        .getScript() + ifCoinZen,
                "")
                .getScript();

        script = isUSDT
                ? scriptAsb.showMessage("BTC").getScript()
                : script + "";

        script = (!isTestnet)
                ? scriptAsb.showMessage(coin.getName()).getScript()
                : scriptAsb.showWrap(coin.getName(), "TESTNET").getScript();

        script = scriptAsb
                .clearBuffer(Buffer.CACHE1)
                .clearBuffer(Buffer.CACHE2)
                .getScript();

        script = (coin != Coin.BCH
                ? // if isnot BCH
                scriptAsb.ifRange(argOutputScriptType, "00", "01", // if P2PKH/P2SH , base58 address
                        (coin.getCoinType() == Coin.BTC.getCoinType()
                                ? new ScriptAssembler().switchString(argOutputScriptType, Buffer.CACHE2, !isTestnet ? "00,05" : "6F,C4") // 1,3:mn,2
                                : (coin == Coin.LTC
                                        ? new ScriptAssembler().ifEqual(argOutputScriptType, "00",
                                                new ScriptAssembler().copyString(!isTestnet ? "30" : "6F", Buffer.CACHE2).getScript() // L:mn
                                                ,
                                                 new ScriptAssembler().switchString(argLtcNewAddress, Buffer.CACHE2, !isTestnet ? "05,32" : "C4,3A").getScript() // 3,M:2,Q
                                        )
                                        : // type==ZEN
                                        new ScriptAssembler().switchString(argOutputScriptType, Buffer.CACHE2, "2089,2096")) // zn,zs
                                )
                                .copyArgument(argOutputDest20, Buffer.CACHE2)
                                .hash(ScriptData.getDataBufferAll(Buffer.CACHE2), Buffer.CACHE2, ScriptAssembler.HashType.DoubleSHA256)
                                .baseConvert(ScriptData.getBuffer(Buffer.CACHE2, 0, coin != Coin.ZEN ? 25 : 26), Buffer.CACHE1, 0, ScriptAssembler.base58Charset, ScriptAssembler.zeroInherit)
                                .showAddress(ScriptData.getDataBufferAll(Buffer.CACHE1))
                                .getScript(), // else P2WPKH/P2WSH , bech32 address
                        nonBchNonSegwit
                )
                : // else BCH CashAddress
                scriptAsb.copyString(!isTestnet ? "020914030F090E0301130800" : "0203081405131400", Buffer.CACHE2)
                        .switchString(argOutputScriptType, Buffer.CACHE1, "00,08")
                        .copyArgument(argOutputDest20, Buffer.CACHE1)
                        .baseConvert(ScriptData.getDataBufferAll(Buffer.CACHE1), Buffer.CACHE2, 34, ScriptAssembler.binary32Charset, ScriptAssembler.bitLeftJustify8to5)
                        .copyString("0000000000000000", Buffer.CACHE2)
                        .bchPolymod(ScriptData.getDataBufferAll(Buffer.CACHE2), Buffer.CACHE1)
                        .clearBuffer(Buffer.CACHE2)
                        .baseConvert(ScriptData.getBuffer(Buffer.CACHE1, 0, 21), Buffer.CACHE2, 34, ScriptAssembler.base32BitcoinCashCharset, ScriptAssembler.bitLeftJustify8to5)
                        .baseConvert(ScriptData.getBuffer(Buffer.CACHE1, 21, 5), Buffer.CACHE2, 8, ScriptAssembler.base32BitcoinCashCharset, ScriptAssembler.bitLeftJustify8to5)
                        .showAddress(ScriptData.getDataBufferAll(Buffer.CACHE2)))
                .showAmount(!isUSDT ? argOutputAmount : argUsdtAmount, 8)
                .showPressButton()
                .clearBuffer(Buffer.CACHE1)
                .copyArgument(argHashPrevoutSequence, Buffer.CACHE1)
                .getScript();
        return "0400000010" + script;
    }

    public static String BTCOutputScriptSignature = "00003044022054D20BC70E47EE7F5195A342F8C5D6985C82C57FE55F676AD09A9BCC383ED58D0220799D0585CBF5BD1ACEF8E5134E6B83D317DABE30C5B6868448622858B67B14A8";
    public static String LTCOutputScriptSignature = "003045022100EC80F2512E0B569B4B7B822E2E416DD67291F0D2A2D32BA4DC2065E31DB8A4F902200753ABD1A73484EFA79B273AC071A61E7EC3734A6D1F017023F5A5836C932D53";
    public static String ZENOutputScriptSignature = "003045022100C542A471117AAD2062CF3C73406D433DB90730CA417B43434713B9C6B8F7BBBD0220166EB92302109D6491887735196D455D797480771BC5B923255A8D73A70EA148";
    public static String BCHOutputScriptSignature = "0000304402205373A0D150DEFF91E14BCE79F85EF7D051766D0B0B40A6B70BE03559D2BB21C402204D28ADDAE87CDE01FFDFBF0D3C3E176686E51A29CBEEA95A41FDFE7F7953D482";
    public static String USDTOutputScriptSignature = "00304502205E6405A61F96D4820221CFAADE76AA8EC91F2553F3B2031D9E7337F7818D1F3B022100EDA6CD704AE1783339E1A44E77FB20C5FE11B50296838839D2ECBB62DD1F60A6";

}
