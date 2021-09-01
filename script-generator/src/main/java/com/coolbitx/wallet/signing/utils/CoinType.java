/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.coolbitx.wallet.signing.utils;

/**
 *
 * @author derek
 */
public class CoinType {


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

}
