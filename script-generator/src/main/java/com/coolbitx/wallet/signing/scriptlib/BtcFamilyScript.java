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

        return "0400000010"
                + ScriptAssembler.setCoinType(coin.getCoinType())
                + ScriptAssembler.switchString(argHaveChange, Buffer.TRANSACTION, "01,02")
                + (!isUSDT
                        ? ScriptAssembler.baseConvert(argOutputAmount, Buffer.TRANSACTION, 8, ScriptAssembler.binaryCharset, ScriptAssembler.littleEndian)
                        : ScriptAssembler.baseConvert(argUsdtDust, Buffer.TRANSACTION, 2, ScriptAssembler.binaryCharset, ScriptAssembler.littleEndian)
                        + ScriptAssembler.copyString("000000000000"))
                + ScriptAssembler.btcScript(argOutputScriptType, supportSegwit ? 4 : (coin == Coin.ZEN ? 79 : 2),
                        (supportSegwit
                                ? ScriptAssembler.ifEqual(argOutputScriptType, "03",
                                        ScriptAssembler.copyArgument(argOutputDest32),
                                        ScriptAssembler.copyArgument(argOutputDest20)
                                )
                                : ScriptAssembler.copyArgument(argOutputDest20))
                )
                + (coin != Coin.ZEN
                        ? ""
                        : ScriptAssembler.copyString("20")
                        + ScriptAssembler.copyArgument(argOutputBlockHash)
                        + ScriptAssembler.copyString("03")
                        + ScriptAssembler.copyArgument(argOutputBlockHeight)
                        + ScriptAssembler.copyString("B4"))
                + (!isUSDT
                        ? ""
                        : ScriptAssembler.copyString("0000000000000000166a146f6d6e69000000000000001f")
                        + ScriptAssembler.copyArgument(argUsdtAmount))
                + ScriptAssembler.ifEqual(argHaveChange, "01", // if haveChange
                        ScriptAssembler.baseConvert(argChangeAmount, Buffer.TRANSACTION, 8, ScriptAssembler.binaryCharset, ScriptAssembler.littleEndian)
                        + ScriptAssembler.btcScript(argChangeScriptType, supportSegwit ? 3 : (coin == Coin.ZEN ? 79 : 2),
                                ScriptAssembler.derivePublicKey(argChangePath, Buffer.CACHE2)
                                + ScriptAssembler.ifEqual(argChangeScriptType, "00", // if P2PKH
                                        ScriptAssembler.hash(ScriptData.getDataBufferAll(Buffer.CACHE2), Buffer.TRANSACTION, ScriptAssembler.SHA256RipeMD160),
                                        "")
                                + ScriptAssembler.ifEqual(argChangeScriptType, "01", // if P2WPKH in P2SH
                                        ScriptAssembler.copyString("0014", Buffer.CACHE1)
                                        + ScriptAssembler.hash(ScriptData.getDataBufferAll(Buffer.CACHE2), Buffer.CACHE1, ScriptAssembler.SHA256RipeMD160)
                                        + ScriptAssembler.hash(ScriptData.getDataBufferAll(Buffer.CACHE1), Buffer.TRANSACTION, ScriptAssembler.SHA256RipeMD160),
                                        "")
                                + (supportSegwit
                                        ? ScriptAssembler.ifEqual(argChangeScriptType, "02", // if P2WPKH
                                                ScriptAssembler.hash(ScriptData.getDataBufferAll(Buffer.CACHE2), Buffer.TRANSACTION, ScriptAssembler.SHA256RipeMD160),
                                                "")
                                        : "")
                        )
                        + (coin != Coin.ZEN
                                ? ""
                                : ScriptAssembler.copyString("20")
                                + ScriptAssembler.copyArgument(argChangeBlockHash)
                                + ScriptAssembler.copyString("03")
                                + ScriptAssembler.copyArgument(argChangeBlockHeight)
                                + ScriptAssembler.copyString("B4")),
                        "")
                + (isUSDT
                        ? ScriptAssembler.showMessage("BTC")
                        : "")
                + (!isTestnet
                        ? ScriptAssembler.showMessage(coin.getName())
                        : ScriptAssembler.showWrap(coin.getName(), "TESTNET"))
                + ScriptAssembler.clearBuffer(Buffer.CACHE1)
                + ScriptAssembler.clearBuffer(Buffer.CACHE2)
                + (coin != Coin.BCH
                        ? // if isnot BCH
                        ScriptAssembler.ifRange(argOutputScriptType, "00", "01", // if P2PKH/P2SH , base58 address
                                (coin.getCoinType() == Coin.BTC.getCoinType()
                                ? ScriptAssembler.switchString(argOutputScriptType, Buffer.CACHE2, !isTestnet ? "00,05" : "6F,C4") // 1,3:mn,2
                                : (coin == Coin.LTC
                                        ? ScriptAssembler.ifEqual(argOutputScriptType, "00",
                                                ScriptAssembler.copyString(!isTestnet ? "30" : "6F", Buffer.CACHE2) // L:mn
                                                ,
                                                 ScriptAssembler.switchString(argLtcNewAddress, Buffer.CACHE2, !isTestnet ? "05,32" : "C4,3A") // 3,M:2,Q
                                        )
                                        : // type==ZEN
                                        ScriptAssembler.switchString(argOutputScriptType, Buffer.CACHE2, "2089,2096") // zn,zs
                                ))
                                + ScriptAssembler.copyArgument(argOutputDest20, Buffer.CACHE2)
                                + ScriptAssembler.hash(ScriptData.getDataBufferAll(Buffer.CACHE2), Buffer.CACHE2, ScriptAssembler.DoubleSHA256)
                                + ScriptAssembler.baseConvert(ScriptData.getBuffer(Buffer.CACHE2, 0, coin != Coin.ZEN ? 25 : 26), Buffer.CACHE1, 0, ScriptAssembler.base58Charset, ScriptAssembler.zeroInherit)
                                + ScriptAssembler.showAddress(ScriptData.getDataBufferAll(Buffer.CACHE1)), // else P2WPKH/P2WSH , bech32 address
                                (supportSegwit
                                        ? ScriptAssembler.copyString(hrpExpand + "00", Buffer.CACHE2)
                                        + ScriptAssembler.ifEqual(argOutputScriptType, "02",
                                                ScriptAssembler.baseConvert(argOutputDest20, Buffer.CACHE2, 32, ScriptAssembler.binary32Charset, ScriptAssembler.bitLeftJustify8to5),
                                                ScriptAssembler.baseConvert(argOutputDest32, Buffer.CACHE2, 52, ScriptAssembler.binary32Charset, ScriptAssembler.bitLeftJustify8to5)
                                        )
                                        + ScriptAssembler.copyString("000000000000", Buffer.CACHE2)
                                        + ScriptAssembler.bech32Polymod(ScriptData.getDataBufferAll(Buffer.CACHE2), Buffer.CACHE1)
                                        + ScriptAssembler.clearBuffer(Buffer.CACHE2)
                                        + ScriptAssembler.copyString(HexUtil.toHexString(hrp + "1q"), Buffer.CACHE2)
                                        + ScriptAssembler.ifEqual(argOutputScriptType, "02",
                                                ScriptAssembler.baseConvert(argOutputDest20, Buffer.CACHE2, 32, ScriptAssembler.base32BitcoinCashCharset, ScriptAssembler.bitLeftJustify8to5),
                                                ScriptAssembler.baseConvert(argOutputDest32, Buffer.CACHE2, 52, ScriptAssembler.base32BitcoinCashCharset, ScriptAssembler.bitLeftJustify8to5)
                                        )
                                        + ScriptAssembler.baseConvert(ScriptData.getDataBufferAll(Buffer.CACHE1), Buffer.CACHE2, 6, ScriptAssembler.base32BitcoinCashCharset, 0)
                                        + ScriptAssembler.showAddress(ScriptData.getDataBufferAll(Buffer.CACHE2))
                                        : ScriptAssembler.throwSEError)
                        )
                        : // else BCH CashAddress
                        ScriptAssembler.copyString(!isTestnet ? "020914030F090E0301130800" : "0203081405131400", Buffer.CACHE2)
                        + ScriptAssembler.switchString(argOutputScriptType, Buffer.CACHE1, "00,08")
                        + ScriptAssembler.copyArgument(argOutputDest20, Buffer.CACHE1)
                        + ScriptAssembler.baseConvert(ScriptData.getDataBufferAll(Buffer.CACHE1), Buffer.CACHE2, 34, ScriptAssembler.binary32Charset, ScriptAssembler.bitLeftJustify8to5)
                        + ScriptAssembler.copyString("0000000000000000", Buffer.CACHE2)
                        + ScriptAssembler.bchPolymod(ScriptData.getDataBufferAll(Buffer.CACHE2), Buffer.CACHE1)
                        + ScriptAssembler.clearBuffer(Buffer.CACHE2)
                        + ScriptAssembler.baseConvert(ScriptData.getBuffer(Buffer.CACHE1, 0, 21), Buffer.CACHE2, 34, ScriptAssembler.base32BitcoinCashCharset, ScriptAssembler.bitLeftJustify8to5)
                        + ScriptAssembler.baseConvert(ScriptData.getBuffer(Buffer.CACHE1, 21, 5), Buffer.CACHE2, 8, ScriptAssembler.base32BitcoinCashCharset, ScriptAssembler.bitLeftJustify8to5)
                        + ScriptAssembler.showAddress(ScriptData.getDataBufferAll(Buffer.CACHE2)))
                + ScriptAssembler.showAmount(!isUSDT ? argOutputAmount : argUsdtAmount, 8)
                + ScriptAssembler.showPressButton()
                + ScriptAssembler.clearBuffer(Buffer.CACHE1)
                + ScriptAssembler.copyArgument(argHashPrevoutSequence, Buffer.CACHE1);
    }
    
    public static String BTCOutputScriptSignature = "00003044022054D20BC70E47EE7F5195A342F8C5D6985C82C57FE55F676AD09A9BCC383ED58D0220799D0585CBF5BD1ACEF8E5134E6B83D317DABE30C5B6868448622858B67B14A8";
    public static String LTCOutputScriptSignature = "003045022100EC80F2512E0B569B4B7B822E2E416DD67291F0D2A2D32BA4DC2065E31DB8A4F902200753ABD1A73484EFA79B273AC071A61E7EC3734A6D1F017023F5A5836C932D53";
    public static String ZENOutputScriptSignature = "003045022100C542A471117AAD2062CF3C73406D433DB90730CA417B43434713B9C6B8F7BBBD0220166EB92302109D6491887735196D455D797480771BC5B923255A8D73A70EA148";
    public static String BCHOutputScriptSignature = "0000304402205373A0D150DEFF91E14BCE79F85EF7D051766D0B0B40A6B70BE03559D2BB21C402204D28ADDAE87CDE01FFDFBF0D3C3E176686E51A29CBEEA95A41FDFE7F7953D482";
    public static String USDTOutputScriptSignature = "00304502205E6405A61F96D4820221CFAADE76AA8EC91F2553F3B2031D9E7337F7818D1F3B022100EDA6CD704AE1783339E1A44E77FB20C5FE11B50296838839D2ECBB62DD1F60A6";

}
