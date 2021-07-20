/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.coolbitx.wallet.signing.scriptlib;
import com.coolbitx.wallet.signing.utils.ScriptArgumentComposer;

/**
 *
 * @author derek
 */
public class ScriptLibrary23 {

    public static void listAll() {
        System.out.println("BTC : " + getBTCOutputScript(Coin.BTC, false));
        System.out.println("ETH : " + getETHScript());
        System.out.println("LTC : " + getBTCOutputScript(Coin.LTC, false));
        System.out.println("XRP : " + getXRPScript(false));
        System.out.println("BCH : " + getBTCOutputScript(Coin.BCH, false));
        System.out.println("ZEN : " + getBTCOutputScript(Coin.ZEN, false));
        System.out.println("USDT : " + getBTCOutputScript(Coin.USDT, false));
        System.out.println("ERC20 : " + getERC20Script());
        System.out.println("BlindETHSmartContract : " + getEtherContractBlindScript());
        System.out.println("BlindETHSignMessage : " + getEtherMessageBlindScript(false));
        System.out.println("BlindETHTypedData : " + getEtherTypedDataBlindScript(false));
        System.out.println("BNB : " + getBNBScript(false));
        System.out.println("BEP2Token : " + getBEP2Script(false));
        System.out.println("BNBPlaceOrder : " + getBNBPlaceOrderScript(false));
        System.out.println("BNBCancelOrder : " + getBNBCancelOrderScript(false));
        System.out.println("ICX : " + getICXScript(false));
        System.out.println("XLM : " + getStellarScript(XLM, false));
        System.out.println("KAU : " + getStellarScript(KAU, false));
        System.out.println("KAG : " + getStellarScript(KAG, false));
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
        ScriptBuffer argOutputScriptType = sac.getArgument(1);
        ScriptBuffer argUsdtDust = sac.getArgumentUnion(6, 2);
        ScriptBuffer argOutputAmount = sac.getArgument(8);
        ScriptBuffer argOutputDest20 = sac.getArgumentUnion(12, 20);
        ScriptBuffer argOutputDest32 = sac.getArgument(32);
        ScriptBuffer argOutputBlockHash = coin == Coin.ZEN ? sac.getArgument(32) : null;
        ScriptBuffer argOutputBlockHeight = coin == Coin.ZEN ? sac.getArgument(3) : null;
        ScriptBuffer argHaveChange = sac.getArgument(1);
        ScriptBuffer argChangeScriptType = sac.getArgument(1);
        ScriptBuffer argChangeAmount = sac.getArgument(8);
        ScriptBuffer argChangePath = sac.getArgument(21);
        ScriptBuffer argChangeBlockHash = coin == Coin.ZEN ? sac.getArgument(32) : null;
        ScriptBuffer argChangeBlockHeight = coin == Coin.ZEN ? sac.getArgument(3) : null;
        ScriptBuffer argHashPrevoutSequence = sac.getArgument(64);
        ScriptBuffer argLtcNewAddress = sac.getArgumentUnion(0, 1);
        ScriptBuffer argUsdtAmount = sac.getArgumentUnion(0, 8);

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
                + ScriptAssembler.switchString(argHaveChange, BufferType.TRANSACTION, "01,02")
                + (!isUSDT
                        ? ScriptAssembler.baseConvert(argOutputAmount, BufferType.TRANSACTION, 8, ScriptAssembler.binaryCharset, ScriptAssembler.littleEndian)
                        : ScriptAssembler.baseConvert(argUsdtDust, BufferType.TRANSACTION, 2, ScriptAssembler.binaryCharset, ScriptAssembler.littleEndian)
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
                        ScriptAssembler.baseConvert(argChangeAmount, BufferType.TRANSACTION, 8, ScriptAssembler.binaryCharset, ScriptAssembler.littleEndian)
                        + ScriptAssembler.btcScript(argChangeScriptType, supportSegwit ? 3 : (coin == Coin.ZEN ? 79 : 2),
                                ScriptAssembler.derivePublicKey(argChangePath, BufferType.FREE)
                                + ScriptAssembler.ifEqual(argChangeScriptType, "00", // if P2PKH
                                        ScriptAssembler.hash(ScriptBuffer.getDataBufferAll(BufferType.FREE), BufferType.TRANSACTION, ScriptAssembler.SHA256RipeMD160),
                                        "")
                                + ScriptAssembler.ifEqual(argChangeScriptType, "01", // if P2WPKH in P2SH
                                        ScriptAssembler.copyString("0014", BufferType.EXTENDED)
                                        + ScriptAssembler.hash(ScriptBuffer.getDataBufferAll(BufferType.FREE), BufferType.EXTENDED, ScriptAssembler.SHA256RipeMD160)
                                        + ScriptAssembler.hash(ScriptBuffer.getDataBufferAll(BufferType.EXTENDED), BufferType.TRANSACTION, ScriptAssembler.SHA256RipeMD160),
                                        "")
                                + (supportSegwit
                                        ? ScriptAssembler.ifEqual(argChangeScriptType, "02", // if P2WPKH
                                                ScriptAssembler.hash(ScriptBuffer.getDataBufferAll(BufferType.FREE), BufferType.TRANSACTION, ScriptAssembler.SHA256RipeMD160),
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
                + ScriptAssembler.resetDest(BufferType.EXTENDED)
                + ScriptAssembler.resetDest(BufferType.FREE)
                + (coin != Coin.BCH
                        ? // if isnot BCH
                        ScriptAssembler.ifRange(argOutputScriptType, "00", "01", // if P2PKH/P2SH , base58 address
                                (coin.getCoinType() == Coin.BTC.getCoinType()
                                ? ScriptAssembler.switchString(argOutputScriptType, BufferType.FREE, !isTestnet ? "00,05" : "6F,C4") // 1,3:mn,2
                                : (coin == Coin.LTC
                                        ? ScriptAssembler.ifEqual(argOutputScriptType, "00",
                                                ScriptAssembler.copyString(!isTestnet ? "30" : "6F", BufferType.FREE) // L:mn
                                                ,
                                                 ScriptAssembler.switchString(argLtcNewAddress, BufferType.FREE, !isTestnet ? "05,32" : "C4,3A") // 3,M:2,Q
                                        )
                                        : // type==ZEN
                                        ScriptAssembler.switchString(argOutputScriptType, BufferType.FREE, "2089,2096") // zn,zs
                                ))
                                + ScriptAssembler.copyArgument(argOutputDest20, BufferType.FREE)
                                + ScriptAssembler.hash(ScriptBuffer.getDataBufferAll(BufferType.FREE), BufferType.FREE, ScriptAssembler.DoubleSHA256)
                                + ScriptAssembler.baseConvert(ScriptBuffer.getBufer(BufferType.FREE, 0, coin != Coin.ZEN ? 25 : 26), BufferType.EXTENDED, 0, ScriptAssembler.base58Charset, ScriptAssembler.zeroInherit)
                                + ScriptAssembler.showAddress(ScriptBuffer.getDataBufferAll(BufferType.EXTENDED)), // else P2WPKH/P2WSH , bech32 address
                                (supportSegwit
                                        ? ScriptAssembler.copyString(hrpExpand + "00", BufferType.FREE)
                                        + ScriptAssembler.ifEqual(argOutputScriptType, "02",
                                                ScriptAssembler.baseConvert(argOutputDest20, BufferType.FREE, 32, ScriptAssembler.binary32Charset, ScriptAssembler.bitLeftJustify8to5),
                                                ScriptAssembler.baseConvert(argOutputDest32, BufferType.FREE, 52, ScriptAssembler.binary32Charset, ScriptAssembler.bitLeftJustify8to5)
                                        )
                                        + ScriptAssembler.copyString("000000000000", BufferType.FREE)
                                        + ScriptAssembler.bech32Polymod(ScriptBuffer.getDataBufferAll(BufferType.FREE), BufferType.EXTENDED)
                                        + ScriptAssembler.resetDest(BufferType.FREE)
                                        + ScriptAssembler.copyString(HexUtil.toHexString(hrp + "1q"), BufferType.FREE)
                                        + ScriptAssembler.ifEqual(argOutputScriptType, "02",
                                                ScriptAssembler.baseConvert(argOutputDest20, BufferType.FREE, 32, ScriptAssembler.base32BitcoinCashCharset, ScriptAssembler.bitLeftJustify8to5),
                                                ScriptAssembler.baseConvert(argOutputDest32, BufferType.FREE, 52, ScriptAssembler.base32BitcoinCashCharset, ScriptAssembler.bitLeftJustify8to5)
                                        )
                                        + ScriptAssembler.baseConvert(ScriptBuffer.getDataBufferAll(BufferType.EXTENDED), BufferType.FREE, 6, ScriptAssembler.base32BitcoinCashCharset, 0)
                                        + ScriptAssembler.showAddress(ScriptBuffer.getDataBufferAll(BufferType.FREE))
                                        : ScriptAssembler.throwSEError)
                        )
                        : // else BCH CashAddress
                        ScriptAssembler.copyString(!isTestnet ? "020914030F090E0301130800" : "0203081405131400", BufferType.FREE)
                        + ScriptAssembler.switchString(argOutputScriptType, BufferType.EXTENDED, "00,08")
                        + ScriptAssembler.copyArgument(argOutputDest20, BufferType.EXTENDED)
                        + ScriptAssembler.baseConvert(ScriptBuffer.getDataBufferAll(BufferType.EXTENDED), BufferType.FREE, 34, ScriptAssembler.binary32Charset, ScriptAssembler.bitLeftJustify8to5)
                        + ScriptAssembler.copyString("0000000000000000", BufferType.FREE)
                        + ScriptAssembler.bchPolymod(ScriptBuffer.getDataBufferAll(BufferType.FREE), BufferType.EXTENDED)
                        + ScriptAssembler.resetDest(BufferType.FREE)
                        + ScriptAssembler.baseConvert(ScriptBuffer.getBufer(BufferType.EXTENDED, 0, 21), BufferType.FREE, 34, ScriptAssembler.base32BitcoinCashCharset, ScriptAssembler.bitLeftJustify8to5)
                        + ScriptAssembler.baseConvert(ScriptBuffer.getBufer(BufferType.EXTENDED, 21, 5), BufferType.FREE, 8, ScriptAssembler.base32BitcoinCashCharset, ScriptAssembler.bitLeftJustify8to5)
                        + ScriptAssembler.showAddress(ScriptBuffer.getDataBufferAll(BufferType.FREE)))
                + ScriptAssembler.showAmount(!isUSDT ? argOutputAmount : argUsdtAmount, 8)
                + ScriptAssembler.showPressButton()
                + ScriptAssembler.resetDest(BufferType.EXTENDED)
                + ScriptAssembler.copyArgument(argHashPrevoutSequence, BufferType.EXTENDED);
    }

    /*
E7
2A
85 09C74AFE1F
82 5208
94 A3255ECFE3F6727A62D938F4C29B2F73C361B26C
83 989680
80
03
80
80
     */
    public static String getETHScript() {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptBuffer argTo = sac.getArgument(20);
        ScriptBuffer argValue = sac.getArgumentRightJustified(10);
        ScriptBuffer argGasPrice = sac.getArgumentRightJustified(10);
        ScriptBuffer argGasLimit = sac.getArgumentRightJustified(10);
        ScriptBuffer argNonce = sac.getArgumentRightJustified(8);
        ScriptBuffer argChainId = sac.getArgumentRightJustified(2);
        //version=00 ScriptAssembler.hash=06=ScriptAssembler.Keccak256 sign=01=ECDSA
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
                + ScriptAssembler.copyString("94")
                + ScriptAssembler.copyArgument(argTo)
                // value
                + ScriptAssembler.rlpString(argValue)
                // data
                + ScriptAssembler.copyString("80")
                // chainId v
                // + ScriptAssembler.rlpString(argChainId)
                + ScriptAssembler.copyString("01", BufferType.EXTENDED)
                + ScriptAssembler.rlpString(ScriptBuffer.getDataBufferAll(BufferType.EXTENDED))
                // r,s
                + ScriptAssembler.copyString("8080")
                + ScriptAssembler.rlpList(1)
                + ScriptAssembler.showMessage("ETH")
                + ScriptAssembler.copyString(HexUtil.toHexString("0x"), BufferType.FREE)
                + ScriptAssembler.baseConvert(argTo, BufferType.FREE, 0, ScriptAssembler.hexadecimalCharset, ScriptAssembler.leftJustify)
                + ScriptAssembler.showAddress(ScriptBuffer.getDataBufferAll(BufferType.FREE))
                + ScriptAssembler.showAmount(argValue, 18)
                + ScriptAssembler.showPressButton();
    }

    /*
txType(EIP-2718) : 02
rlpLength :        e5
chainId :          01
nonce :            80
maxPriorityFee :   01
maxFee :           81 ff
gasLimit :         84 02625a00
to address :       94 cccccccccccccccccccccccccccccccccccccccc
value :            83 0186a0
data :             80
accessList :       c0
     */
    public static String getETHEIP1559Script() {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptBuffer argTo = sac.getArgument(20);
        ScriptBuffer argValue = sac.getArgumentRightJustified(10);
        ScriptBuffer argGasTipCap = sac.getArgumentRightJustified(10);
        ScriptBuffer argGasFeeCap = sac.getArgumentRightJustified(10);
        ScriptBuffer argGasLimit = sac.getArgumentRightJustified(10);
        ScriptBuffer argNonce = sac.getArgumentRightJustified(8);
        //version=04 ScriptAssembler.hash=06=ScriptAssembler.Keccak256 sign=01=ECDSA
        return "03040601"
                // set coinType to 3C
                + ScriptAssembler.setCoinType(0x3C)
                // txType (EIP-2718)
                + ScriptAssembler.copyString("02")
                + ScriptAssembler.arrayPointer()
                // chainId
                + ScriptAssembler.copyString("01")
                // nonce
                + ScriptAssembler.rlpString(argNonce)
                // gasTipCap (maxPriorityFeePerGas)
                + ScriptAssembler.rlpString(argGasTipCap)
                // gasFeeCap (maxFeePerGas)
                + ScriptAssembler.rlpString(argGasFeeCap)
                // gasLimit
                + ScriptAssembler.rlpString(argGasLimit)
                // toAddress
                + ScriptAssembler.copyString("94")
                + ScriptAssembler.copyArgument(argTo)
                // value
                + ScriptAssembler.rlpString(argValue)
                // data
                + ScriptAssembler.copyString("80")
                // accessList
                + ScriptAssembler.copyString("C0")
                + ScriptAssembler.arrayEnd(1)
                + ScriptAssembler.showMessage("ETH")
                + ScriptAssembler.copyString(HexUtil.toHexString("0x"), BufferType.FREE)
                + ScriptAssembler.baseConvert(argTo, BufferType.FREE, 0, ScriptAssembler.hexadecimalCharset, ScriptAssembler.leftJustify)
                + ScriptAssembler.showAddress(ScriptBuffer.getDataBufferAll(BufferType.FREE))
                + ScriptAssembler.showAmount(argValue, 18)
                + ScriptAssembler.showPressButton();
    }

    /*
txType(EIP-2718) : 02
rlpLength :        f86a
chainId :          01
nonce :            80
maxPriorityFee :   01
maxFee :           81 ff
gasLimit :         84 02625a00
to address :       94 cccccccccccccccccccccccccccccccccccccccc
value :            83 0186a0
data :             b844 a9059cbb
                   0000000000000000000000009e3a6e03298780078003d3965689b8f653ee87b1
                   0000000000000000000000000000000000000000000000004563918244f40000
accessList :       c0
     */
    public static String getETHEIP1559ERC20Script() {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptBuffer argTo = sac.getArgument(20);
        ScriptBuffer argValue = sac.getArgument(12);
        ScriptBuffer argGasTipCap = sac.getArgumentRightJustified(10);
        ScriptBuffer argGasFeeCap = sac.getArgumentRightJustified(10);
        ScriptBuffer argGasLimit = sac.getArgumentRightJustified(10);
        ScriptBuffer argNonce = sac.getArgumentRightJustified(8);
        ScriptBuffer argTokenInfo = sac.getArgumentUnion(0, 29);
        ScriptBuffer argDecimal = sac.getArgument(1);
        ScriptBuffer argNameLength = sac.getArgument(1);
        ScriptBuffer argName = sac.getArgumentVariableLength(7);
        ScriptBuffer argContractAddress = sac.getArgument(20);
        ScriptBuffer argSign = sac.getArgument(72);
        //version=04 ScriptAssembler.hash=06=ScriptAssembler.Keccak256 sign=01=ECDSA
        return "03040601"
                // set coinType to 3C
                + ScriptAssembler.setCoinType(0x3C)
                // txType (EIP-2718)
                + ScriptAssembler.copyString("02")
                + ScriptAssembler.arrayPointer()
                // chainId
                + ScriptAssembler.copyString("01")
                + ScriptAssembler.rlpString(argNonce)
                + ScriptAssembler.rlpString(argGasTipCap)
                + ScriptAssembler.rlpString(argGasFeeCap)
                + ScriptAssembler.rlpString(argGasLimit)
                + ScriptAssembler.copyString("94")
                + ScriptAssembler.copyArgument(argContractAddress)
                // data, Length = 68
                + ScriptAssembler.copyString("80B844a9059cbb000000000000000000000000")
                + ScriptAssembler.copyArgument(argTo)
                + ScriptAssembler.copyString("0000000000000000000000000000000000000000")
                + ScriptAssembler.copyArgument(argValue)
                // accessList
                + ScriptAssembler.copyString("C0")
                + ScriptAssembler.arrayEnd(1)
                + ScriptAssembler.showMessage("ETH")
                + ScriptAssembler.ifSigned(argTokenInfo, argSign, "",
                        ScriptAssembler.copyString(HexUtil.toHexString("@"), BufferType.FREE)
                )
                + ScriptAssembler.setBufferInt(argNameLength, 1, 7)
                + ScriptAssembler.copyArgument(argName, BufferType.FREE)
                + ScriptAssembler.showMessage(ScriptBuffer.getDataBufferAll(BufferType.FREE))
                + ScriptAssembler.resetDest(BufferType.FREE)
                + ScriptAssembler.copyString(HexUtil.toHexString("0x"), BufferType.FREE)
                + ScriptAssembler.baseConvert(argTo, BufferType.FREE, 0, ScriptAssembler.hexadecimalCharset, ScriptAssembler.leftJustify)
                + ScriptAssembler.showAddress(ScriptBuffer.getDataBufferAll(BufferType.FREE))
                + ScriptAssembler.setBufferInt(argDecimal, 0, 20)
                + ScriptAssembler.showAmount(argValue, 1000)
                + ScriptAssembler.showPressButton();
    }

    public static String getETHEIP1559SmartScript() {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptBuffer argTo = sac.getArgument(20);
        ScriptBuffer argValue = sac.getArgumentRightJustified(10);
        ScriptBuffer argGasTipCap = sac.getArgumentRightJustified(10);
        ScriptBuffer argGasFeeCap = sac.getArgumentRightJustified(10);
        ScriptBuffer argGasLimit = sac.getArgumentRightJustified(10);
        ScriptBuffer argNonce = sac.getArgumentRightJustified(8);
        ScriptBuffer argData = sac.getArgumentAll();

        // version=04 ScriptAssembler.hash=06=ScriptAssembler.Keccak256 sign=01=ECDSA
        return "03040601"
                // set coinType to 3C
                + ScriptAssembler.setCoinType(0x3C)
                // txType (EIP-2718)
                + ScriptAssembler.copyString("02")
                + ScriptAssembler.arrayPointer()
                // chainId
                + ScriptAssembler.copyString("01")
                + ScriptAssembler.rlpString(argNonce)
                + ScriptAssembler.rlpString(argGasTipCap)
                + ScriptAssembler.rlpString(argGasFeeCap)
                + ScriptAssembler.rlpString(argGasLimit)
                + ScriptAssembler.copyString("94")
                + ScriptAssembler.copyArgument(argTo)
                + ScriptAssembler.rlpString(argValue)
                + ScriptAssembler.rlpString(argData)
                // accessList
                + ScriptAssembler.copyString("C0")
                + ScriptAssembler.arrayEnd(1)
                + ScriptAssembler.showMessage("ETH")
                + ScriptAssembler.showWrap("SMART", "")
                + ScriptAssembler.showPressButton();
    }

    /*
f86a
1e
85 01718c7e00
83 030d40
94 86fa049857e0209aa7d9e616f7eb3b3b78ecfdb0
80
b844 a9059cbb
     0000000000000000000000009e3a6e03298780078003d3965689b8f653ee87b1
     0000000000000000000000000000000000000000000000004563918244f40000
01
80
80
     */
    public static String getERC20Script() {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptBuffer argTo = sac.getArgument(20);
        ScriptBuffer argValue = sac.getArgument(12);
        ScriptBuffer argGasPrice = sac.getArgumentRightJustified(10);
        ScriptBuffer argGasLimit = sac.getArgumentRightJustified(10);
        ScriptBuffer argNonce = sac.getArgumentRightJustified(8);
        ScriptBuffer argChainId = sac.getArgumentRightJustified(2);
        ScriptBuffer argTokenInfo = sac.getArgumentUnion(0, 29);
        ScriptBuffer argDecimal = sac.getArgument(1);
        ScriptBuffer argNameLength = sac.getArgument(1);
        ScriptBuffer argName = sac.getArgumentVariableLength(7);
        ScriptBuffer argContractAddress = sac.getArgument(20);
        ScriptBuffer argSign = sac.getArgument(72);
        // version=00 ScriptAssembler.hash=06=ScriptAssembler.Keccak256 sign=01=ECDSA
        return "03000601"
                // set coinType to 3C
                + ScriptAssembler.setCoinType(0x3C)
                + ScriptAssembler.copyString("F800")
                + ScriptAssembler.rlpString(argNonce)
                + ScriptAssembler.rlpString(argGasPrice)
                + ScriptAssembler.rlpString(argGasLimit)
                + ScriptAssembler.copyString("94")
                + ScriptAssembler.copyArgument(argContractAddress)
                // data, Length = 68
                + ScriptAssembler.copyString("80B844a9059cbb000000000000000000000000")
                + ScriptAssembler.copyArgument(argTo)
                + ScriptAssembler.copyString("0000000000000000000000000000000000000000")
                + ScriptAssembler.copyArgument(argValue)
                // chainId v
                // + ScriptAssembler.rlpString(argChainId)
                + ScriptAssembler.copyString("01", BufferType.EXTENDED)
                + ScriptAssembler.rlpString(ScriptBuffer.getDataBufferAll(BufferType.EXTENDED))
                // r,s
                + ScriptAssembler.copyString("8080")
                + ScriptAssembler.rlpList(2)
                + ScriptAssembler.showMessage("ETH")
                + ScriptAssembler.ifSigned(argTokenInfo, argSign, "",
                        ScriptAssembler.copyString(HexUtil.toHexString("@"), BufferType.FREE)
                )
                + ScriptAssembler.setBufferInt(argNameLength, 1, 7)
                + ScriptAssembler.copyArgument(argName, BufferType.FREE)
                + ScriptAssembler.showMessage(ScriptBuffer.getDataBufferAll(BufferType.FREE))
                + ScriptAssembler.resetDest(BufferType.FREE)
                + ScriptAssembler.copyString(HexUtil.toHexString("0x"), BufferType.FREE)
                + ScriptAssembler.baseConvert(argTo, BufferType.FREE, 0, ScriptAssembler.hexadecimalCharset, ScriptAssembler.leftJustify)
                + ScriptAssembler.showAddress(ScriptBuffer.getDataBufferAll(BufferType.FREE))
                + ScriptAssembler.setBufferInt(argDecimal, 0, 20)
                + ScriptAssembler.showAmount(argValue, 1000)
                + ScriptAssembler.showPressButton();
    }

    public static String getEtherContractBlindScript() {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptBuffer argTo = sac.getArgument(20);
        ScriptBuffer argValue = sac.getArgumentRightJustified(10);
        ScriptBuffer argGasPrice = sac.getArgumentRightJustified(10);
        ScriptBuffer argGasLimit = sac.getArgumentRightJustified(10);
        ScriptBuffer argNonce = sac.getArgumentRightJustified(8);
        ScriptBuffer argChainId = sac.getArgumentRightJustified(2);
        ScriptBuffer argData = sac.getArgumentAll();

        // version=00 ScriptAssembler.hash=06=ScriptAssembler.Keccak256 sign=01=ECDSA
        return "03000601"
                // set coinType to 3C
                + ScriptAssembler.setCoinType(0x3C)
                + ScriptAssembler.copyString("F800")
                //nonce
                + ScriptAssembler.rlpString(argNonce)
                // gasPrice
                + ScriptAssembler.rlpString(argGasPrice)
                // gasLimit
                + ScriptAssembler.rlpString(argGasLimit)
                // toAddress
                + ScriptAssembler.copyString("94")
                + ScriptAssembler.copyArgument(argTo)
                // value
                + ScriptAssembler.rlpString(argValue)
                // data
                + ScriptAssembler.rlpString(argData)
                // chainId v
                // + ScriptAssembler.rlpString(argChainId)
                + ScriptAssembler.copyString("01", BufferType.EXTENDED)
                + ScriptAssembler.rlpString(ScriptBuffer.getDataBufferAll(BufferType.EXTENDED))
                + ScriptAssembler.copyString("8080")
                + ScriptAssembler.rlpList(2)
                + ScriptAssembler.showMessage("ETH")
                + ScriptAssembler.showWrap("SMART", "")
                + ScriptAssembler.showPressButton();
    }

    public static String getEtherMessageBlindScript(boolean isTestnet) {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptBuffer argMessage = sac.getArgumentAll();

        return "03000601"
                + //version=00 ScriptAssembler.hash=06=ScriptAssembler.Keccak256 sign=01=ECDSA
                ScriptAssembler.setCoinType(0x3C)
                + // set coinType to 3C
                ScriptAssembler.copyString("19457468657265756D205369676E6564204D6573736167653A0A3332")
                + ScriptAssembler.hash(argMessage, BufferType.TRANSACTION, ScriptAssembler.Keccak256)
                + ScriptAssembler.showMessage("ETH")
                + ScriptAssembler.showWrap("MESSAGE", "")
                + ScriptAssembler.showPressButton();
    }

    public static String getEtherTypedDataBlindScript(boolean isTestnet) {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptBuffer argDomainSeparator = sac.getArgument(32);
        ScriptBuffer argMessage = sac.getArgumentAll();

        return "03000601"
                + //version=00 ScriptAssembler.hash=06=ScriptAssembler.Keccak256 sign=01=ECDSA
                ScriptAssembler.setCoinType(0x3C)
                + // set coinType to 3C
                ScriptAssembler.copyString("1901")
                + ScriptAssembler.copyArgument(argDomainSeparator)
                + ScriptAssembler.hash(argMessage, BufferType.TRANSACTION, ScriptAssembler.Keccak256)
                + ScriptAssembler.showMessage("ETH")
                + ScriptAssembler.showWrap("TYPED", "DATA")
                + ScriptAssembler.showPressButton();
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
        ScriptBuffer argFrom = sac.getArgumentRightJustified(64);
        ScriptBuffer argTo = sac.getArgumentRightJustified(64);
        ScriptBuffer argValue = sac.getArgument(8);
        ScriptBuffer argAccountNumber = sac.getArgument(8);
        ScriptBuffer argSequence = sac.getArgument(8);
        ScriptBuffer argSource = sac.getArgument(8);
        ScriptBuffer argMemo = sac.getArgumentAll();

        return "03000201"
                + //version=00 ScriptAssembler.hash=02=sha256 sign=01=ECDSA
                ScriptAssembler.setCoinType(0x02CA)
                + // set coinType to 02CA
                ScriptAssembler.copyString(HexUtil.toHexString("{\"account_number\":\""))
                + ScriptAssembler.baseConvert(argAccountNumber, BufferType.TRANSACTION, 0, ScriptAssembler.decimalCharset, ScriptAssembler.leftJustify)
                + (!isTestnet
                        ? ScriptAssembler.copyString(HexUtil.toHexString("\",\"chain_id\":\"Binance-Chain-Tigris\",\"data\":null,\"memo\":\""))
                        : ScriptAssembler.copyString(HexUtil.toHexString("\",\"chain_id\":\"Binance-Chain-Nile\",\"data\":null,\"memo\":\"")))
                + ScriptAssembler.copyRegularString(argMemo)
                + ScriptAssembler.copyString(HexUtil.toHexString("\",\"msgs\":[{\"inputs\":[{\"address\":\""))
                + ScriptAssembler.copyRegularString(argFrom)
                + ScriptAssembler.copyString(HexUtil.toHexString("\",\"coins\":[{\"amount\":"))
                + ScriptAssembler.baseConvert(argValue, BufferType.TRANSACTION, 0, ScriptAssembler.decimalCharset, ScriptAssembler.leftJustify)
                + ScriptAssembler.copyString(HexUtil.toHexString(",\"denom\":\"BNB\"}]}],\"outputs\":[{\"address\":\""))
                + ScriptAssembler.copyRegularString(argTo)
                + ScriptAssembler.copyString(HexUtil.toHexString("\",\"coins\":[{\"amount\":"))
                + ScriptAssembler.baseConvert(argValue, BufferType.TRANSACTION, 0, ScriptAssembler.decimalCharset, ScriptAssembler.leftJustify)
                + ScriptAssembler.copyString(HexUtil.toHexString(",\"denom\":\"BNB\"}]}]}],\"sequence\":\""))
                + ScriptAssembler.baseConvert(argSequence, BufferType.TRANSACTION, 0, ScriptAssembler.decimalCharset, ScriptAssembler.leftJustify)
                + ScriptAssembler.copyString(HexUtil.toHexString("\",\"source\":\""))
                + ScriptAssembler.baseConvert(argSource, BufferType.TRANSACTION, 0, ScriptAssembler.decimalCharset, ScriptAssembler.leftJustify)
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
        ScriptBuffer argFrom = sac.getArgumentRightJustified(64);
        ScriptBuffer argTo = sac.getArgumentRightJustified(64);
        ScriptBuffer argValue = sac.getArgument(8);
        ScriptBuffer argAccountNumber = sac.getArgument(8);
        ScriptBuffer argSequence = sac.getArgument(8);
        ScriptBuffer argSource = sac.getArgument(8);
        ScriptBuffer argTokenName = sac.getArgumentRightJustified(20);
        ScriptBuffer argTokenCheck = sac.getArgumentRightJustified(20);
        ScriptBuffer argTokenSignature = sac.getArgument(72);
        ScriptBuffer argMemo = sac.getArgumentAll();

        return "03000201"
                + //version=00 ScriptAssembler.hash=02=sha256 sign=01=ECDSA
                ScriptAssembler.setCoinType(0x02CA)
                + // set coinType to 02CA
                ScriptAssembler.copyString(HexUtil.toHexString("{\"account_number\":\""))
                + ScriptAssembler.baseConvert(argAccountNumber, BufferType.TRANSACTION, 0, ScriptAssembler.decimalCharset, ScriptAssembler.leftJustify)
                + (!isTestnet
                        ? ScriptAssembler.copyString(HexUtil.toHexString("\",\"chain_id\":\"Binance-Chain-Tigris\",\"data\":null,\"memo\":\""))
                        : ScriptAssembler.copyString(HexUtil.toHexString("\",\"chain_id\":\"Binance-Chain-Nile\",\"data\":null,\"memo\":\"")))
                + ScriptAssembler.copyRegularString(argMemo)
                + ScriptAssembler.copyString(HexUtil.toHexString("\",\"msgs\":[{\"inputs\":[{\"address\":\""))
                + ScriptAssembler.copyRegularString(argFrom)
                + ScriptAssembler.copyString(HexUtil.toHexString("\",\"coins\":[{\"amount\":"))
                + ScriptAssembler.baseConvert(argValue, BufferType.TRANSACTION, 0, ScriptAssembler.decimalCharset, ScriptAssembler.leftJustify)
                + ScriptAssembler.copyString(HexUtil.toHexString(",\"denom\":\""))
                + ScriptAssembler.copyRegularString(argTokenName, BufferType.FREE)
                + ScriptAssembler.copyString(HexUtil.toHexString("-"), BufferType.FREE)
                + ScriptAssembler.copyRegularString(argTokenCheck, BufferType.FREE)
                + ScriptAssembler.copyArgument(ScriptBuffer.getDataBufferAll(BufferType.FREE))
                + ScriptAssembler.copyString(HexUtil.toHexString("\"}]}],\"outputs\":[{\"address\":\""))
                + ScriptAssembler.copyRegularString(argTo)
                + ScriptAssembler.copyString(HexUtil.toHexString("\",\"coins\":[{\"amount\":"))
                + ScriptAssembler.baseConvert(argValue, BufferType.TRANSACTION, 0, ScriptAssembler.decimalCharset, ScriptAssembler.leftJustify)
                + ScriptAssembler.copyString(HexUtil.toHexString(",\"denom\":\""))
                + ScriptAssembler.copyArgument(ScriptBuffer.getDataBufferAll(BufferType.FREE))
                + ScriptAssembler.copyString(HexUtil.toHexString("\"}]}]}],\"sequence\":\""))
                + ScriptAssembler.baseConvert(argSequence, BufferType.TRANSACTION, 0, ScriptAssembler.decimalCharset, ScriptAssembler.leftJustify)
                + ScriptAssembler.copyString(HexUtil.toHexString("\",\"source\":\""))
                + ScriptAssembler.baseConvert(argSource, BufferType.TRANSACTION, 0, ScriptAssembler.decimalCharset, ScriptAssembler.leftJustify)
                + ScriptAssembler.copyString(HexUtil.toHexString("\"}"))
                + ScriptAssembler.resetDest(BufferType.FREE)
                + ScriptAssembler.showMessage("BNB")
                + ScriptAssembler.copyRegularString(argTokenName, BufferType.FREE)
                + ScriptAssembler.showMessage(ScriptBuffer.getDataBufferAll(BufferType.FREE))
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
        ScriptBuffer argOrderAddress = sac.getArgument(40);
        ScriptBuffer argOrderSequence = sac.getArgument(8);
        ScriptBuffer argSender = sac.getArgumentRightJustified(64);
        ScriptBuffer argSide = sac.getArgument(1);
        ScriptBuffer argQuoteToken = sac.getArgumentRightJustified(20);
        ScriptBuffer argBaseToken = sac.getArgumentRightJustified(20);
        ScriptBuffer argQuantity = sac.getArgument(8);
        ScriptBuffer argPrice = sac.getArgument(8);
        ScriptBuffer argIsImmediate = sac.getArgument(1);
        ScriptBuffer argAccountNumber = sac.getArgument(8);
        ScriptBuffer argSequence = sac.getArgument(8);
        ScriptBuffer argSource = sac.getArgument(8);

        return "03000201"
                + //version=00 ScriptAssembler.hash=02=sha256 sign=01=ECDSA
                ScriptAssembler.setCoinType(0x02CA)
                + // set coinType to 02CA
                ScriptAssembler.copyString(HexUtil.toHexString("{\"account_number\":\""))
                + ScriptAssembler.baseConvert(argAccountNumber, BufferType.TRANSACTION, 0, ScriptAssembler.decimalCharset, ScriptAssembler.leftJustify)
                + (!isTestnet
                        ? ScriptAssembler.copyString(HexUtil.toHexString("\",\"chain_id\":\"Binance-Chain-Tigris\",\"data\":null,\"memo\":\"\",\"msgs\":[{\"id\":\""))
                        : ScriptAssembler.copyString(HexUtil.toHexString("\",\"chain_id\":\"Binance-Chain-Nile\",\"data\":null,\"memo\":\"\",\"msgs\":[{\"id\":\"")))
                + ScriptAssembler.copyRegularString(argOrderAddress)
                + ScriptAssembler.copyString(HexUtil.toHexString("-"))
                + ScriptAssembler.baseConvert(argOrderSequence, BufferType.TRANSACTION, 0, ScriptAssembler.decimalCharset, ScriptAssembler.leftJustify)
                + ScriptAssembler.copyString(HexUtil.toHexString("\",\"ordertype\":2,\"price\":"))
                + ScriptAssembler.baseConvert(argPrice, BufferType.TRANSACTION, 0, ScriptAssembler.decimalCharset, ScriptAssembler.leftJustify)
                + ScriptAssembler.copyString(HexUtil.toHexString(",\"quantity\":"))
                + ScriptAssembler.baseConvert(argQuantity, BufferType.TRANSACTION, 0, ScriptAssembler.decimalCharset, ScriptAssembler.leftJustify)
                + ScriptAssembler.copyString(HexUtil.toHexString(",\"sender\":\""))
                + ScriptAssembler.copyRegularString(argSender)
                + ScriptAssembler.copyString(HexUtil.toHexString("\",\"side\":"))
                + ScriptAssembler.switchString(argSide, BufferType.TRANSACTION, HexUtil.toHexString("1") + "," + HexUtil.toHexString("2"))
                + //0->"1" for buy, 1->"2" for sell
                ScriptAssembler.copyString(HexUtil.toHexString(",\"symbol\":\""))
                + ScriptAssembler.copyRegularString(argQuoteToken)
                + ScriptAssembler.copyString(HexUtil.toHexString("_"))
                + ScriptAssembler.copyRegularString(argBaseToken)
                + ScriptAssembler.copyString(HexUtil.toHexString("\",\"timeinforce\":"))
                + ScriptAssembler.switchString(argIsImmediate, BufferType.TRANSACTION, HexUtil.toHexString("1") + "," + HexUtil.toHexString("3"))
                + //0->"1" for GoodTillExpire, 1->"3" for ImmediateOrCancel
                ScriptAssembler.copyString(HexUtil.toHexString("}],\"sequence\":\""))
                + ScriptAssembler.baseConvert(argSequence, BufferType.TRANSACTION, 0, ScriptAssembler.decimalCharset, ScriptAssembler.leftJustify)
                + ScriptAssembler.copyString(HexUtil.toHexString("\",\"source\":\""))
                + ScriptAssembler.baseConvert(argSource, BufferType.TRANSACTION, 0, ScriptAssembler.decimalCharset, ScriptAssembler.leftJustify)
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
        ScriptBuffer argOrderAddress = sac.getArgument(40);
        ScriptBuffer argOrderSequence = sac.getArgument(8);
        ScriptBuffer argSender = sac.getArgumentRightJustified(64);
        ScriptBuffer argQuoteToken = sac.getArgumentRightJustified(20);
        ScriptBuffer argBaseToken = sac.getArgumentRightJustified(20);
        ScriptBuffer argAccountNumber = sac.getArgument(8);
        ScriptBuffer argSequence = sac.getArgument(8);
        ScriptBuffer argSource = sac.getArgument(8);

        return "03000201"
                + //version=00 ScriptAssembler.hash=02=sha256 sign=01=ECDSA
                ScriptAssembler.setCoinType(0x02CA)
                + // set coinType to 02CA
                ScriptAssembler.copyString(HexUtil.toHexString("{\"account_number\":\""))
                + ScriptAssembler.baseConvert(argAccountNumber, BufferType.TRANSACTION, 0, ScriptAssembler.decimalCharset, ScriptAssembler.leftJustify)
                + (!isTestnet
                        ? ScriptAssembler.copyString(HexUtil.toHexString("\",\"chain_id\":\"Binance-Chain-Tigris\",\"data\":null,\"memo\":\"\",\"msgs\":[{\"refid\":\""))
                        : ScriptAssembler.copyString(HexUtil.toHexString("\",\"chain_id\":\"Binance-Chain-Nile\",\"data\":null,\"memo\":\"\",\"msgs\":[{\"refid\":\"")))
                + ScriptAssembler.copyRegularString(argOrderAddress)
                + ScriptAssembler.copyString(HexUtil.toHexString("-"))
                + ScriptAssembler.baseConvert(argOrderSequence, BufferType.TRANSACTION, 0, ScriptAssembler.decimalCharset, ScriptAssembler.leftJustify)
                + ScriptAssembler.copyString(HexUtil.toHexString("\",\"sender\":\""))
                + ScriptAssembler.copyRegularString(argSender)
                + ScriptAssembler.copyString(HexUtil.toHexString("\",\"symbol\":\""))
                + ScriptAssembler.copyRegularString(argQuoteToken)
                + ScriptAssembler.copyString(HexUtil.toHexString("_"))
                + ScriptAssembler.copyRegularString(argBaseToken)
                + ScriptAssembler.copyString(HexUtil.toHexString("\"}],\"sequence\":\""))
                + ScriptAssembler.baseConvert(argSequence, BufferType.TRANSACTION, 0, ScriptAssembler.decimalCharset, ScriptAssembler.leftJustify)
                + ScriptAssembler.copyString(HexUtil.toHexString("\",\"source\":\""))
                + ScriptAssembler.baseConvert(argSource, BufferType.TRANSACTION, 0, ScriptAssembler.decimalCharset, ScriptAssembler.leftJustify)
                + ScriptAssembler.copyString(HexUtil.toHexString("\"}"))
                + (!isTestnet ? ""
                        : ScriptAssembler.showWrap("BNB", "TESTNET"))
                + ScriptAssembler.showWrap("CANCEL", "BNB?");
    }

    public static String getICXScript(boolean isTestnet) {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptBuffer argFrom = sac.getArgument(20);
        ScriptBuffer argTo = sac.getArgument(20);
        ScriptBuffer argValue = sac.getArgument(10);
        ScriptBuffer argTime = sac.getArgument(10);
        ScriptBuffer argNetworkId = sac.getArgument(2);

        return "03000401"
                + //version=00 ScriptAssembler.hash=04=sha3256 sign=01=ECDSA
                ScriptAssembler.setCoinType(0x4A)
                + // set coinType to 4A
                ScriptAssembler.copyString(HexUtil.toHexString("icx_sendTransaction.from.hx"))
                + ScriptAssembler.baseConvert(argFrom, BufferType.TRANSACTION, 0, ScriptAssembler.hexadecimalCharset, ScriptAssembler.leftJustify)
                + ScriptAssembler.copyString(HexUtil.toHexString(".nid.0x"))
                + ScriptAssembler.baseConvert(argNetworkId, BufferType.TRANSACTION, 0, ScriptAssembler.hexadecimalCharset, ScriptAssembler.leftJustify)
                + ScriptAssembler.copyString(HexUtil.toHexString(".stepLimit.0x186a0.timestamp.0x"))
                + ScriptAssembler.baseConvert(argTime, BufferType.TRANSACTION, 0, ScriptAssembler.hexadecimalCharset, ScriptAssembler.leftJustify)
                + ScriptAssembler.copyString(HexUtil.toHexString(".to.hx"), BufferType.FREE)
                + ScriptAssembler.baseConvert(argTo, BufferType.FREE, 0, ScriptAssembler.hexadecimalCharset, ScriptAssembler.leftJustify)
                + ScriptAssembler.copyArgument(ScriptBuffer.getDataBufferAll(BufferType.FREE))
                + ScriptAssembler.copyString(HexUtil.toHexString(".value.0x"))
                + ScriptAssembler.baseConvert(argValue, BufferType.TRANSACTION, 0, ScriptAssembler.hexadecimalCharset, ScriptAssembler.leftJustify)
                + ScriptAssembler.copyString(HexUtil.toHexString(".version.0x3"))
                + ScriptAssembler.showMessage("ICX")
                + ScriptAssembler.showAddress(ScriptBuffer.getDataBufferAll(BufferType.FREE, 4))
                + ScriptAssembler.showAmount(argValue, 18)
                + ScriptAssembler.showPressButton();
    }

    public static String getXRPScript(boolean isTestnet) {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptBuffer argAccount = sac.getArgument(20);
        ScriptBuffer argPublicKey = sac.getArgument(33);
        ScriptBuffer argDest = sac.getArgument(20);
        ScriptBuffer argPadding1 = sac.getArgument(1);
        ScriptBuffer argAmount = sac.getArgument(7);
        ScriptBuffer argPadding2 = sac.getArgument(1);
        ScriptBuffer argFee = sac.getArgument(7);
        ScriptBuffer argSequence = sac.getArgument(4);
        ScriptBuffer argLastLedgerSequence = sac.getArgument(4);
        ScriptBuffer argTag = sac.getArgument(4);
        ScriptBuffer argFlags = sac.getArgument(4);

        return "03000301"
                + ScriptAssembler.setCoinType(0x90)
                + ScriptAssembler.copyString("5354580012000022")
                + ScriptAssembler.copyArgument(argFlags)
                + ScriptAssembler.copyString("24")
                + ScriptAssembler.copyArgument(argSequence)
                + ScriptAssembler.copyString("2E")
                + ScriptAssembler.copyArgument(argTag)
                + ScriptAssembler.copyString("201B")
                + ScriptAssembler.copyArgument(argLastLedgerSequence)
                + ScriptAssembler.copyString("6140")
                + ScriptAssembler.copyArgument(argAmount)
                + ScriptAssembler.copyString("6840")
                + ScriptAssembler.copyArgument(argFee)
                + ScriptAssembler.copyString("7321")
                + ScriptAssembler.copyArgument(argPublicKey)
                + ScriptAssembler.copyString("8114")
                + ScriptAssembler.copyArgument(argAccount)
                + ScriptAssembler.copyString("8314")
                + ScriptAssembler.copyArgument(argDest)
                + ScriptAssembler.showMessage("XRP")
                + ScriptAssembler.copyString("00", BufferType.FREE)
                + ScriptAssembler.copyArgument(argDest, BufferType.FREE)
                + ScriptAssembler.hash(ScriptBuffer.getDataBufferAll(BufferType.FREE), BufferType.FREE, ScriptAssembler.DoubleSHA256)
                + ScriptAssembler.copyString(HexUtil.toHexString("rpshnaf39wBUDNEGHJKLM4PQRST7VWXYZ2bcdeCg65jkm8oFqi1tuvAxyz"), BufferType.EXTENDED)
                + ScriptAssembler.baseConvert(ScriptBuffer.getBufer(BufferType.FREE, 0, 25), BufferType.FREE, 45, ScriptAssembler.extendedCharset, ScriptAssembler.zeroInherit)
                + ScriptAssembler.showAddress(ScriptBuffer.getDataBufferAll(BufferType.FREE, 53))
                + ScriptAssembler.showAmount(argAmount, 6)
                + ScriptAssembler.showPressButton();
    }

    public static final int XLM = 0;
    public static final int KAU = 1;
    public static final int KAG = 2;

    public static String getStellarScript(int type, boolean isTestnet) {
        if (type < 0 || type > 2) {
            return "typeError";
        }
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptBuffer argSourceAccountId = sac.getArgument(32);
        ScriptBuffer argDestAccountId = sac.getArgument(32);
        ScriptBuffer argAmount = sac.getArgument(8);
        ScriptBuffer argFee4 = sac.getArgumentUnion(4, 4);
        ScriptBuffer argFee8 = sac.getArgument(8);
        ScriptBuffer argSequence = sac.getArgument(8);
        ScriptBuffer argTimeBounds = sac.getArgument(16);
        ScriptBuffer argMemoType = sac.getArgument(1);
        ScriptBuffer argMemo8 = sac.getArgumentUnion(24, 8);
        ScriptBuffer argMemo32 = sac.getArgumentUnion(0, 32);
        ScriptBuffer argMemoSpace = sac.getArgument(4);
        ScriptBuffer argMemoRJ = sac.getArgumentRightJustified(28);
        ScriptBuffer argIsCreate = sac.getArgument(1);

        final String[] header = {
            "7ac33997544e3175d266bd022439b22cdb16508c01163f26e5cb2a3e1045a979", // XLM
            "cee0302d59844d32bdca915c8203dd44b33fbb7edc19051ea37abedf28ecd472", // XLM test
            "3282241a3ca14ba8807e0dd869ae1f3b29a47a383badfbd6fe493f514c617314", // KAU
            "599c6897b37bd161229877d86992678a7524aede87a0b2c29baa9331bb283bea", // KAU test
            "e59a5c2b639c122cd32faaa88957660dedacf292485e45c45f6d57011790c131", // KAG
            "1061c82002783387ff47fb45656a0802e3610d0ec7e78037e3924165ce68ca4f"};// KAG test

        final String[] symbol = {"XLM", "KAU", "KAG"};

        return "03000202"
                + ScriptAssembler.setCoinType(0x94)
                + ScriptAssembler.copyString(header[type * 2 + (isTestnet ? 1 : 0)] + "0000000200000000")
                + //header[32] + envelopType[4] + sourceAccountIdType[4]
                ScriptAssembler.copyArgument(argSourceAccountId)
                + ScriptAssembler.copyArgument(type == XLM ? argFee4 : argFee8)
                + ScriptAssembler.copyArgument(argSequence) // TODO
                + ScriptAssembler.copyString("00000001")
                + ScriptAssembler.copyArgument(argTimeBounds)
                + ScriptAssembler.copyString("000000")
                + ScriptAssembler.copyArgument(argMemoType)
                + ScriptAssembler.ifRange(argMemoType, "00", "04", "",
                        ScriptAssembler.throwSEError
                )
                + ScriptAssembler.ifEqual(argMemoType, "01",
                        ScriptAssembler.setBufferIntToDataLength(argMemoRJ)
                        + ScriptAssembler.copyString("0000")
                        + ScriptAssembler.putBufferInt(BufferType.TRANSACTION)
                        + ScriptAssembler.copyArgument(argMemoRJ)
                        + ScriptAssembler.paddingZero(BufferType.TRANSACTION, 4),
                        "")
                + ScriptAssembler.ifEqual(argMemoType, "02",
                        ScriptAssembler.copyArgument(argMemo8),
                        "")
                + ScriptAssembler.ifRange(argMemoType, "03", "04",
                        ScriptAssembler.copyArgument(argMemo32),
                        "")
                + ScriptAssembler.copyString("0000000100000000000000")
                + ScriptAssembler.switchString(argIsCreate, BufferType.TRANSACTION, "01,00")
                + ScriptAssembler.copyString("00000000")
                + ScriptAssembler.copyArgument(argDestAccountId)
                + ScriptAssembler.ifEqual(argIsCreate, "00",
                        ScriptAssembler.copyString("00000000"),
                        "")
                + ScriptAssembler.copyArgument(argAmount)
                + ScriptAssembler.copyString("00000000")
                + (!isTestnet
                        ? ScriptAssembler.showMessage(symbol[type])
                        : ScriptAssembler.showWrap(symbol[type], "TESTNET"))
                + ScriptAssembler.copyString("30", BufferType.FREE)
                + ScriptAssembler.copyArgument(argDestAccountId, BufferType.FREE)
                + ScriptAssembler.hash(ScriptBuffer.getDataBufferAll(BufferType.FREE), BufferType.EXTENDED, ScriptAssembler.CRC16)
                + ScriptAssembler.baseConvert(ScriptBuffer.getDataBufferAll(BufferType.EXTENDED), BufferType.FREE, 2, ScriptAssembler.binaryCharset, ScriptAssembler.littleEndian)
                + ScriptAssembler.resetDest(BufferType.EXTENDED)
                + ScriptAssembler.copyString(HexUtil.toHexString("ABCDEFGHIJKLMNOPQRSTUVWXYZ234567"), BufferType.EXTENDED)
                + ScriptAssembler.baseConvert(ScriptBuffer.getBufer(BufferType.FREE, 0, 35), BufferType.FREE, 56, ScriptAssembler.extendedCharset, ScriptAssembler.bitLeftJustify8to5)
                + ScriptAssembler.showAddress(ScriptBuffer.getDataBufferAll(BufferType.FREE, 35))
                + ScriptAssembler.showAmount(argAmount, 7)
                + ScriptAssembler.showPressButton();
    }

    private static int typeString = 2;
    private static int typeInt = 0;

    public static String getTRXScript(boolean isTestnet) {
//        ScriptArgumentComposer sac = new ScriptArgumentComposer();
//        ScriptBuffer argBlockBytes = sac.getBufer(2);
//        ScriptBuffer argBlockHash = sac.getBufer(8);
//        ScriptBuffer argExpiration = sac.getArgumentRightJustified(10);
//        ScriptBuffer argOwnerAddr = sac.getBufer(21);
//        ScriptBuffer argToAddr = sac.getBufer(21);
//        ScriptBuffer argAmount = sac.getArgumentRightJustified(10);
//        ScriptBuffer argTimestamp = sac.getArgumentRightJustified(10);
        //version=03 ScriptAssembler.hash=02=ScriptAssembler.SHA256 sign=01=ECDSA

        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptBuffer argBlockBytes = sac.getArgument(2);
        ScriptBuffer argBlockHash = sac.getArgument(8);
        ScriptBuffer argExpiration = sac.getArgumentRightJustified(10);
        ScriptBuffer argOwnerAddr = sac.getArgument(21);
        ScriptBuffer argToAddr = sac.getArgument(21);
        ScriptBuffer argAmount = sac.getArgumentRightJustified(10);
        ScriptBuffer argTimestamp = sac.getArgumentRightJustified(10);

        return "03030201"
                // set coinType to C3
                + ScriptAssembler.setCoinType(0xC3)
                // ref_block_bytes
                + ScriptAssembler.copyString("0a")
                + ScriptAssembler.protobuf(argBlockBytes, typeString)
                // ref_block_hash
                + ScriptAssembler.copyString("22")
                + ScriptAssembler.protobuf(argBlockHash, typeString)
                // expiration
                + ScriptAssembler.copyString("40")
                + ScriptAssembler.protobuf(argExpiration, typeInt)
                // contract object
                + ScriptAssembler.copyString("5a")
                + ScriptAssembler.arrayPointer()
                // contract type
                + ScriptAssembler.copyString("0801")
                // parameter object
                + ScriptAssembler.copyString("12")
                + ScriptAssembler.arrayPointer()
                // type url
                + ScriptAssembler.copyString("0a2d")
                + ScriptAssembler.copyString(Hex.toHexString("type.googleapis.com/protocol.TransferContract".getBytes()))
                // value object
                + ScriptAssembler.copyString("12")
                + ScriptAssembler.arrayPointer()
                // owner address
                + ScriptAssembler.copyString("0a")
                + ScriptAssembler.protobuf(argOwnerAddr, typeString)
                // to address
                + ScriptAssembler.copyString("12")
                + ScriptAssembler.protobuf(argToAddr, typeString)
                // amount
                + ScriptAssembler.copyString("18")
                + ScriptAssembler.protobuf(argAmount, typeInt)
                + ScriptAssembler.arrayEnd()
                + ScriptAssembler.arrayEnd()
                + ScriptAssembler.arrayEnd()
                // timestamp
                + ScriptAssembler.copyString("70")
                + ScriptAssembler.protobuf(argTimestamp, typeInt)
                + ScriptAssembler.showMessage("TRX")
                + ScriptAssembler.copyArgument(argToAddr, BufferType.FREE)
                + ScriptAssembler.hash(ScriptBuffer.getDataBufferAll(BufferType.FREE), BufferType.FREE, ScriptAssembler.DoubleSHA256)
                + ScriptAssembler.baseConvert(ScriptBuffer.getBufer(BufferType.FREE, 0, 25), BufferType.EXTENDED, 0, ScriptAssembler.base58Charset, ScriptAssembler.zeroInherit)
                + ScriptAssembler.showAddress(ScriptBuffer.getDataBufferAll(BufferType.EXTENDED))
                + ScriptAssembler.showAmount(argAmount, 6)
                + ScriptAssembler.showWrap("PRESS", "BUTToN");
    }

    public static String getTRC20Script(boolean isTestnet) {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptBuffer argBlockBytes = sac.getArgument(2);
        ScriptBuffer argBlockHash = sac.getArgument(8);
        ScriptBuffer argExpiration = sac.getArgumentRightJustified(10);
        ScriptBuffer argOwnerAddr = sac.getArgument(20);
        ScriptBuffer argTokenInfo = sac.getArgumentUnion(0, 29);
        ScriptBuffer argDecimal = sac.getArgument(1);
        ScriptBuffer argNameLength = sac.getArgument(1);
        ScriptBuffer argName = sac.getArgumentVariableLength(7);
        ScriptBuffer argContractAddr = sac.getArgument(20);
        ScriptBuffer argSign = sac.getArgument(72);
        ScriptBuffer argTo = sac.getArgument(20);
        ScriptBuffer argValue = sac.getArgument(12);
        ScriptBuffer argTimestamp = sac.getArgumentRightJustified(10);
        ScriptBuffer argFeeLimit = sac.getArgumentRightJustified(10);
        //version=03 ScriptAssembler.hash=02=ScriptAssembler.SHA256 sign=01=ECDSA
        return "03030201"
                // set coinType to C3
                + ScriptAssembler.setCoinType(0xC3)
                // ref_block_bytes
                + ScriptAssembler.copyString("0a")
                + ScriptAssembler.protobuf(argBlockBytes, typeString)
                // ref_block_hash
                + ScriptAssembler.copyString("22")
                + ScriptAssembler.protobuf(argBlockHash, typeString)
                // expiration
                + ScriptAssembler.copyString("40")
                + ScriptAssembler.protobuf(argExpiration, typeInt)
                // contract object
                + ScriptAssembler.copyString("5a")
                + ScriptAssembler.arrayPointer()
                // contract type    
                + ScriptAssembler.copyString("081f")
                // parameter object
                + ScriptAssembler.copyString("12")
                + ScriptAssembler.arrayPointer()
                // type url
                + ScriptAssembler.copyString("0a31")
                + ScriptAssembler.copyString(Hex.toHexString("type.googleapis.com/protocol.TriggerSmartContract".getBytes()))
                // value object
                + ScriptAssembler.copyString("12")
                + ScriptAssembler.arrayPointer()
                // owner address
                + ScriptAssembler.copyString("0a")
                + ScriptAssembler.copyString("41", BufferType.FREE)
                + ScriptAssembler.copyArgument(argOwnerAddr, BufferType.FREE)
                + ScriptAssembler.protobuf(ScriptBuffer.getDataBufferAll(BufferType.FREE), typeString)
                // contract address
                + ScriptAssembler.copyString("12")
                + ScriptAssembler.resetDest(BufferType.FREE)
                + ScriptAssembler.copyString("41", BufferType.FREE)
                + ScriptAssembler.copyArgument(argContractAddr, BufferType.FREE)
                + ScriptAssembler.protobuf(ScriptBuffer.getDataBufferAll(BufferType.FREE), typeString)
                // data
                + ScriptAssembler.copyString("22")
                + ScriptAssembler.resetDest(BufferType.FREE)
                + ScriptAssembler.copyString("a9059cbb", BufferType.FREE)
                + ScriptAssembler.copyString("000000000000000000000000", BufferType.FREE)
                + ScriptAssembler.copyArgument(argTo, BufferType.FREE)
                + ScriptAssembler.copyString("0000000000000000000000000000000000000000", BufferType.FREE)
                + ScriptAssembler.copyArgument(argValue, BufferType.FREE)
                + ScriptAssembler.protobuf(ScriptBuffer.getDataBufferAll(BufferType.FREE), typeString)
                + ScriptAssembler.arrayEnd()
                + ScriptAssembler.arrayEnd()
                + ScriptAssembler.arrayEnd()
                // timestamp
                + ScriptAssembler.copyString("70")
                + ScriptAssembler.protobuf(argTimestamp, typeInt)
                // fee limit
                + ScriptAssembler.copyString("9001")
                + ScriptAssembler.protobuf(argFeeLimit, typeInt)
                // display chain
                + ScriptAssembler.showMessage("TRX")
                // display token
                + ScriptAssembler.resetDest(BufferType.FREE)
                + ScriptAssembler.ifSigned(argTokenInfo, argSign, "",
                        ScriptAssembler.copyString(HexUtil.toHexString("@"), BufferType.FREE)
                )
                + ScriptAssembler.setBufferInt(argNameLength, 1, 7)
                + ScriptAssembler.copyArgument(argName, BufferType.FREE)
                + ScriptAssembler.showMessage(ScriptBuffer.getDataBufferAll(BufferType.FREE))
                // display to address
                + ScriptAssembler.resetDest(BufferType.FREE)
                + ScriptAssembler.copyString("41", BufferType.FREE)
                + ScriptAssembler.copyArgument(argTo, BufferType.FREE)
                + ScriptAssembler.hash(ScriptBuffer.getDataBufferAll(BufferType.FREE), BufferType.FREE, ScriptAssembler.DoubleSHA256)
                + ScriptAssembler.baseConvert(ScriptBuffer.getBufer(BufferType.FREE, 0, 25), BufferType.EXTENDED, 0, ScriptAssembler.base58Charset, ScriptAssembler.zeroInherit)
                + ScriptAssembler.showAddress(ScriptBuffer.getDataBufferAll(BufferType.EXTENDED))
                // display amount
                + ScriptAssembler.setBufferInt(argDecimal, 0, 20)
                + ScriptAssembler.showAmount(argValue, 1000)
                + ScriptAssembler.showPressButton();
    }

    public static String getTRXFreezeScript(boolean isTestnet) {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptBuffer argBlockBytes = sac.getArgument(2);
        ScriptBuffer argBlockHash = sac.getArgument(8);
        ScriptBuffer argExpiration = sac.getArgumentRightJustified(10);
        ScriptBuffer argOwnerAddr = sac.getArgument(21);
        ScriptBuffer argFrozenBalance = sac.getArgumentRightJustified(10);
        ScriptBuffer argFrozenDuration = sac.getArgumentRightJustified(10);
        ScriptBuffer argResource = sac.getArgument(1);
        ScriptBuffer argReceiverAddr = sac.getArgument(21);
        ScriptBuffer argTimestamp = sac.getArgumentRightJustified(10);
        //version=03 ScriptAssembler.hash=02=ScriptAssembler.SHA256 sign=01=ECDSA
        return "03030201"
                // set coinType to C3
                + ScriptAssembler.setCoinType(0xC3)
                // ref_block_bytes
                + ScriptAssembler.copyString("0a")
                + ScriptAssembler.protobuf(argBlockBytes, typeString)
                // ref_block_hash
                + ScriptAssembler.copyString("22")
                + ScriptAssembler.protobuf(argBlockHash, typeString)
                // expiration
                + ScriptAssembler.copyString("40")
                + ScriptAssembler.protobuf(argExpiration, typeInt)
                // contracts array
                + ScriptAssembler.copyString("5a")
                + ScriptAssembler.arrayPointer()
                // contract type
                + ScriptAssembler.copyString("080b")
                // parameter object
                + ScriptAssembler.copyString("12")
                + ScriptAssembler.arrayPointer()
                // type url
                + ScriptAssembler.copyString("0a32")
                + ScriptAssembler.copyString(Hex.toHexString("type.googleapis.com/protocol.FreezeBalanceContract".getBytes()))
                // value object
                + ScriptAssembler.copyString("12")
                + ScriptAssembler.arrayPointer()
                // ownerAddr
                + ScriptAssembler.copyString("0a")
                + ScriptAssembler.protobuf(argOwnerAddr, typeString)
                // frozenBalance
                + ScriptAssembler.copyString("10")
                + ScriptAssembler.protobuf(argFrozenBalance, typeInt)
                // frozenDuration
                + ScriptAssembler.copyString("18")
                + ScriptAssembler.protobuf(argFrozenDuration, typeInt)
                // resource
                + ScriptAssembler.ifEqual(argResource, "01", ScriptAssembler.copyString("5001"), "")
                + ScriptAssembler.ifEqual(argResource, "02", ScriptAssembler.copyString("5002"), "")
                // receiverAddr
                + ScriptAssembler.copyString("7a")
                + ScriptAssembler.protobuf(argReceiverAddr, typeString)
                + ScriptAssembler.arrayEnd()
                + ScriptAssembler.arrayEnd()
                + ScriptAssembler.arrayEnd()
                // timestamp
                + ScriptAssembler.copyString("70")
                + ScriptAssembler.protobuf(argTimestamp, typeInt)
                + ScriptAssembler.showMessage("TRX")
                + ScriptAssembler.showMessage("Freeze")
                + ScriptAssembler.copyArgument(argReceiverAddr, BufferType.FREE)
                + ScriptAssembler.hash(ScriptBuffer.getDataBufferAll(BufferType.FREE), BufferType.FREE, ScriptAssembler.DoubleSHA256)
                + ScriptAssembler.baseConvert(ScriptBuffer.getBufer(BufferType.FREE, 0, 25), BufferType.EXTENDED, 0, ScriptAssembler.base58Charset, ScriptAssembler.zeroInherit)
                + ScriptAssembler.showAddress(ScriptBuffer.getDataBufferAll(BufferType.EXTENDED))
                + ScriptAssembler.showAmount(argFrozenBalance, 6)
                + ScriptAssembler.showPressButton();
    }

    public static String getTRXFreezeScriptNoReceiver(boolean isTestnet) {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptBuffer argBlockBytes = sac.getArgument(2);
        ScriptBuffer argBlockHash = sac.getArgument(8);
        ScriptBuffer argExpiration = sac.getArgumentRightJustified(10);
        ScriptBuffer argOwnerAddr = sac.getArgument(21);
        ScriptBuffer argFrozenBalance = sac.getArgumentRightJustified(10);
        ScriptBuffer argFrozenDuration = sac.getArgumentRightJustified(10);
        ScriptBuffer argResource = sac.getArgument(1);
        ScriptBuffer argTimestamp = sac.getArgumentRightJustified(10);
        //version=03 ScriptAssembler.hash=02=ScriptAssembler.SHA256 sign=01=ECDSA
        return "03030201"
                // set coinType to C3
                + ScriptAssembler.setCoinType(0xC3)
                // ref_block_bytes
                + ScriptAssembler.copyString("0a")
                + ScriptAssembler.protobuf(argBlockBytes, typeString)
                // ref_block_hash
                + ScriptAssembler.copyString("22")
                + ScriptAssembler.protobuf(argBlockHash, typeString)
                // expiration
                + ScriptAssembler.copyString("40")
                + ScriptAssembler.protobuf(argExpiration, typeInt)
                // contracts array
                + ScriptAssembler.copyString("5a")
                + ScriptAssembler.arrayPointer()
                // contract type
                + ScriptAssembler.copyString("080b")
                // parameter object
                + ScriptAssembler.copyString("12")
                + ScriptAssembler.arrayPointer()
                // type url
                + ScriptAssembler.copyString("0a32")
                + ScriptAssembler.copyString(Hex.toHexString("type.googleapis.com/protocol.FreezeBalanceContract".getBytes()))
                // value object
                + ScriptAssembler.copyString("12")
                + ScriptAssembler.arrayPointer()
                // ownerAddr
                + ScriptAssembler.copyString("0a")
                + ScriptAssembler.protobuf(argOwnerAddr, typeString)
                // frozenBalance
                + ScriptAssembler.copyString("10")
                + ScriptAssembler.protobuf(argFrozenBalance, typeInt)
                // frozenDuration
                + ScriptAssembler.copyString("18")
                + ScriptAssembler.protobuf(argFrozenDuration, typeInt)
                // resource
                + ScriptAssembler.ifEqual(argResource, "01", ScriptAssembler.copyString("5001"), "")
                + ScriptAssembler.ifEqual(argResource, "02", ScriptAssembler.copyString("5002"), "")
                + ScriptAssembler.arrayEnd()
                + ScriptAssembler.arrayEnd()
                + ScriptAssembler.arrayEnd()
                // timestamp
                + ScriptAssembler.copyString("70")
                + ScriptAssembler.protobuf(argTimestamp, typeInt)
                + ScriptAssembler.showMessage("TRX")
                + ScriptAssembler.showMessage("Freeze")
                + ScriptAssembler.copyArgument(argOwnerAddr, BufferType.FREE)
                + ScriptAssembler.hash(ScriptBuffer.getDataBufferAll(BufferType.FREE), BufferType.FREE, ScriptAssembler.DoubleSHA256)
                + ScriptAssembler.baseConvert(ScriptBuffer.getBufer(BufferType.FREE, 0, 25), BufferType.EXTENDED, 0, ScriptAssembler.base58Charset, ScriptAssembler.zeroInherit)
                + ScriptAssembler.showAddress(ScriptBuffer.getDataBufferAll(BufferType.EXTENDED))
                + ScriptAssembler.showAmount(argFrozenBalance, 6)
                + ScriptAssembler.showPressButton();
    }

    public static String getTRXUnfreezeScript(boolean isTestnet) {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptBuffer argBlockBytes = sac.getArgument(2);
        ScriptBuffer argBlockHash = sac.getArgument(8);
        ScriptBuffer argExpiration = sac.getArgumentRightJustified(10);
        ScriptBuffer argOwnerAddr = sac.getArgument(21);
        ScriptBuffer argResource = sac.getArgument(1);
        ScriptBuffer argReceiverAddr = sac.getArgument(21);
        ScriptBuffer argTimestamp = sac.getArgumentRightJustified(10);
        //version=03 ScriptAssembler.hash=02=ScriptAssembler.SHA256 sign=01=ECDSA
        return "03030201"
                // set coinType to C3
                + ScriptAssembler.setCoinType(0xC3)
                // ref_block_bytes
                + ScriptAssembler.copyString("0a")
                + ScriptAssembler.protobuf(argBlockBytes, typeString)
                // ref_block_hash
                + ScriptAssembler.copyString("22")
                + ScriptAssembler.protobuf(argBlockHash, typeString)
                // expiration
                + ScriptAssembler.copyString("40")
                + ScriptAssembler.protobuf(argExpiration, typeInt)
                // contracts array
                + ScriptAssembler.copyString("5a")
                + ScriptAssembler.arrayPointer()
                // contract type
                + ScriptAssembler.copyString("080c")
                // parameter object
                + ScriptAssembler.copyString("12")
                + ScriptAssembler.arrayPointer()
                // type url
                + ScriptAssembler.copyString("0a34")
                + ScriptAssembler.copyString(Hex.toHexString("type.googleapis.com/protocol.UnfreezeBalanceContract".getBytes()))
                // value object
                + ScriptAssembler.copyString("12")
                + ScriptAssembler.arrayPointer()
                // ownerAddr
                + ScriptAssembler.copyString("0a")
                + ScriptAssembler.protobuf(argOwnerAddr, typeString)
                // resource
                + ScriptAssembler.ifEqual(argResource, "01", ScriptAssembler.copyString("5001"), "")
                + ScriptAssembler.ifEqual(argResource, "02", ScriptAssembler.copyString("5002"), "")
                // receiverAddr
                + ScriptAssembler.copyString("7a")
                + ScriptAssembler.protobuf(argReceiverAddr, typeString)
                + ScriptAssembler.arrayEnd()
                + ScriptAssembler.arrayEnd()
                + ScriptAssembler.arrayEnd()
                // timestamp
                + ScriptAssembler.copyString("70")
                + ScriptAssembler.protobuf(argTimestamp, typeInt)
                + ScriptAssembler.showMessage("TRX")
                + ScriptAssembler.showMessage("Unfrz")
                + ScriptAssembler.copyArgument(argReceiverAddr, BufferType.FREE)
                + ScriptAssembler.hash(ScriptBuffer.getDataBufferAll(BufferType.FREE), BufferType.FREE, ScriptAssembler.DoubleSHA256)
                + ScriptAssembler.baseConvert(ScriptBuffer.getBufer(BufferType.FREE, 0, 25), BufferType.EXTENDED, 0, ScriptAssembler.base58Charset, ScriptAssembler.zeroInherit)
                + ScriptAssembler.showAddress(ScriptBuffer.getDataBufferAll(BufferType.EXTENDED))
                + ScriptAssembler.showPressButton();
    }

    public static String getTRXUnfreezeScriptNoReceiver(boolean isTestnet) {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptBuffer argBlockBytes = sac.getArgument(2);
        ScriptBuffer argBlockHash = sac.getArgument(8);
        ScriptBuffer argExpiration = sac.getArgumentRightJustified(10);
        ScriptBuffer argOwnerAddr = sac.getArgument(21);
        ScriptBuffer argResource = sac.getArgument(1);
        ScriptBuffer argTimestamp = sac.getArgumentRightJustified(10);
        //version=03 ScriptAssembler.hash=02=ScriptAssembler.SHA256 sign=01=ECDSA
        return "03030201"
                // set coinType to C3
                + ScriptAssembler.setCoinType(0xC3)
                // ref_block_bytes
                + ScriptAssembler.copyString("0a")
                + ScriptAssembler.protobuf(argBlockBytes, typeString)
                // ref_block_hash
                + ScriptAssembler.copyString("22")
                + ScriptAssembler.protobuf(argBlockHash, typeString)
                // expiration
                + ScriptAssembler.copyString("40")
                + ScriptAssembler.protobuf(argExpiration, typeInt)
                // contracts array
                + ScriptAssembler.copyString("5a")
                + ScriptAssembler.arrayPointer()
                // contract type
                + ScriptAssembler.copyString("080c")
                // parameter object
                + ScriptAssembler.copyString("12")
                + ScriptAssembler.arrayPointer()
                // type url
                + ScriptAssembler.copyString("0a34")
                + ScriptAssembler.copyString(Hex.toHexString("type.googleapis.com/protocol.UnfreezeBalanceContract".getBytes()))
                // value object
                + ScriptAssembler.copyString("12")
                + ScriptAssembler.arrayPointer()
                // ownerAddr
                + ScriptAssembler.copyString("0a")
                + ScriptAssembler.protobuf(argOwnerAddr, typeString)
                // resource
                + ScriptAssembler.ifEqual(argResource, "01", ScriptAssembler.copyString("5001"), "")
                + ScriptAssembler.ifEqual(argResource, "02", ScriptAssembler.copyString("5002"), "")
                + ScriptAssembler.arrayEnd()
                + ScriptAssembler.arrayEnd()
                + ScriptAssembler.arrayEnd()
                // timestamp
                + ScriptAssembler.copyString("70")
                + ScriptAssembler.protobuf(argTimestamp, typeInt)
                + ScriptAssembler.showMessage("TRX")
                + ScriptAssembler.showMessage("Unfrz")
                + ScriptAssembler.copyArgument(argOwnerAddr, BufferType.FREE)
                + ScriptAssembler.hash(ScriptBuffer.getDataBufferAll(BufferType.FREE), BufferType.FREE, ScriptAssembler.DoubleSHA256)
                + ScriptAssembler.baseConvert(ScriptBuffer.getBufer(BufferType.FREE, 0, 25), BufferType.EXTENDED, 0, ScriptAssembler.base58Charset, ScriptAssembler.zeroInherit)
                + ScriptAssembler.showAddress(ScriptBuffer.getDataBufferAll(BufferType.EXTENDED))
                + ScriptAssembler.showPressButton();
    }

    public static String getTRXVoteWitnessScript(boolean isTestnet) {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptBuffer argBlockBytes = sac.getArgument(2);
        ScriptBuffer argBlockHash = sac.getArgument(8);
        ScriptBuffer argExpiration = sac.getArgumentRightJustified(10);
        ScriptBuffer argOwnerAddr = sac.getArgument(21);
        ScriptBuffer argVoteAddr = sac.getArgument(21);
        ScriptBuffer argVoteCount = sac.getArgumentRightJustified(10);
        ScriptBuffer argTimestamp = sac.getArgumentRightJustified(10);
        //version=03 ScriptAssembler.hash=02=ScriptAssembler.SHA256 sign=01=ECDSA
        return "03030201"
                // set coinType to C3
                + ScriptAssembler.setCoinType(0xC3)
                // ref_block_bytes
                + ScriptAssembler.copyString("0a")
                + ScriptAssembler.protobuf(argBlockBytes, typeString)
                // ref_block_hash
                + ScriptAssembler.copyString("22")
                + ScriptAssembler.protobuf(argBlockHash, typeString)
                // expiration
                + ScriptAssembler.copyString("40")
                + ScriptAssembler.protobuf(argExpiration, typeInt)
                // contracts array
                + ScriptAssembler.copyString("5a")
                + ScriptAssembler.arrayPointer()
                // contract type
                + ScriptAssembler.copyString("0804")
                // parameter object
                + ScriptAssembler.copyString("12")
                + ScriptAssembler.arrayPointer()
                // type url
                + ScriptAssembler.copyString("0a30")
                + ScriptAssembler.copyString(Hex.toHexString("type.googleapis.com/protocol.VoteWitnessContract".getBytes()))
                // value object
                + ScriptAssembler.copyString("12")
                + ScriptAssembler.arrayPointer()
                // ownerAddr
                + ScriptAssembler.copyString("0a")
                + ScriptAssembler.protobuf(argOwnerAddr, typeString)
                // votes array
                + ScriptAssembler.copyString("12")
                + ScriptAssembler.arrayPointer()
                // vote address
                + ScriptAssembler.copyString("0a")
                + ScriptAssembler.protobuf(argVoteAddr, typeString)
                // vote count
                + ScriptAssembler.copyString("10")
                + ScriptAssembler.protobuf(argVoteCount, typeInt)
                + ScriptAssembler.arrayEnd()
                + ScriptAssembler.arrayEnd()
                + ScriptAssembler.arrayEnd()
                + ScriptAssembler.arrayEnd()
                // timestamp
                + ScriptAssembler.copyString("70")
                + ScriptAssembler.protobuf(argTimestamp, typeInt)
                + ScriptAssembler.showMessage("TRX")
                + ScriptAssembler.showMessage("Vote")
                + ScriptAssembler.copyArgument(argVoteAddr, BufferType.FREE)
                + ScriptAssembler.hash(ScriptBuffer.getDataBufferAll(BufferType.FREE), BufferType.FREE, ScriptAssembler.DoubleSHA256)
                + ScriptAssembler.baseConvert(ScriptBuffer.getBufer(BufferType.FREE, 0, 25), BufferType.EXTENDED, 0, ScriptAssembler.base58Charset, ScriptAssembler.zeroInherit)
                + ScriptAssembler.showAddress(ScriptBuffer.getDataBufferAll(BufferType.EXTENDED))
                + ScriptAssembler.showAmount(argVoteCount, 0)
                + ScriptAssembler.showPressButton();
    }

    public static String getTRXWithdrawScript(boolean isTestnet) {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptBuffer argBlockBytes = sac.getArgument(2);
        ScriptBuffer argBlockHash = sac.getArgument(8);
        ScriptBuffer argExpiration = sac.getArgumentRightJustified(10);
        ScriptBuffer argOwnerAddr = sac.getArgument(21);
        ScriptBuffer argTimestamp = sac.getArgumentRightJustified(10);
        //version=03 ScriptAssembler.hash=02=ScriptAssembler.SHA256 sign=01=ECDSA
        return "03030201"
                // set coinType to C3
                + ScriptAssembler.setCoinType(0xC3)
                // ref_block_bytes
                + ScriptAssembler.copyString("0a")
                + ScriptAssembler.protobuf(argBlockBytes, typeString)
                // ref_block_hash
                + ScriptAssembler.copyString("22")
                + ScriptAssembler.protobuf(argBlockHash, typeString)
                // expiration
                + ScriptAssembler.copyString("40")
                + ScriptAssembler.protobuf(argExpiration, typeInt)
                // contracts array
                + ScriptAssembler.copyString("5a")
                + ScriptAssembler.arrayPointer()
                // contract type
                + ScriptAssembler.copyString("080d")
                // parameter object
                + ScriptAssembler.copyString("12")
                + ScriptAssembler.arrayPointer()
                // type url
                + ScriptAssembler.copyString("0a34")
                + ScriptAssembler.copyString(Hex.toHexString("type.googleapis.com/protocol.WithdrawBalanceContract".getBytes()))
                // value object
                + ScriptAssembler.copyString("12")
                + ScriptAssembler.arrayPointer()
                // ownerAddr
                + ScriptAssembler.copyString("0a")
                + ScriptAssembler.protobuf(argOwnerAddr, typeString)
                + ScriptAssembler.arrayEnd()
                + ScriptAssembler.arrayEnd()
                + ScriptAssembler.arrayEnd()
                // timestamp
                + ScriptAssembler.copyString("70")
                + ScriptAssembler.protobuf(argTimestamp, typeInt)
                + ScriptAssembler.showMessage("TRX")
                + ScriptAssembler.showMessage("Reward")
                + ScriptAssembler.copyArgument(argOwnerAddr, BufferType.FREE)
                + ScriptAssembler.hash(ScriptBuffer.getDataBufferAll(BufferType.FREE), BufferType.FREE, ScriptAssembler.DoubleSHA256)
                + ScriptAssembler.baseConvert(ScriptBuffer.getBufer(BufferType.FREE, 0, 25), BufferType.EXTENDED, 0, ScriptAssembler.base58Charset, ScriptAssembler.zeroInherit)
                + ScriptAssembler.showAddress(ScriptBuffer.getDataBufferAll(BufferType.EXTENDED))
                + ScriptAssembler.showPressButton();
    }

    public enum CosmosTxType {
        SEND,
        DELEGATE,
        UNDELEGATE,
        WITHDRAW
    }

    public static String getCosmosScript(CosmosTxType type) {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptBuffer argPublicKey = sac.getArgument(33);
        ScriptBuffer argFromOrDelegator = sac.getArgumentRightJustified(64);
        ScriptBuffer argToOrValidator = sac.getArgumentRightJustified(64);
        ScriptBuffer argAmount = sac.getArgument(0);
        if (type != CosmosTxType.WITHDRAW) {
            argAmount = sac.getArgument(8);
        }
        ScriptBuffer argFeeAmount = sac.getArgument(8);
        ScriptBuffer argGas = sac.getArgument(8);
        ScriptBuffer argAccountNumber = sac.getArgument(8);
        ScriptBuffer argSequence = sac.getArgument(8);
        ScriptBuffer argMemo = sac.getArgumentAll();

        String url = "";
        if (type == CosmosTxType.SEND) {
            // message.url - /cosmos.bank.v1beta1.MsgSend
            url = "0a1c2f636f736d6f732e62616e6b2e763162657461312e4d736753656e64";
        } else if (type == CosmosTxType.DELEGATE) {
            // message.url - /cosmos.staking.v1beta1.MsgDelegate
            url = "0a232f636f736d6f732e7374616b696e672e763162657461312e4d736744656c6567617465";
        } else if (type == CosmosTxType.UNDELEGATE) {
            // message.url - /cosmos.staking.v1beta1.MsgUndelegate
            url = "0a252f636f736d6f732e7374616b696e672e763162657461312e4d7367556e64656c6567617465";
        } else if (type == CosmosTxType.WITHDRAW) {
            // message.url - /cosmos.distribution.v1beta1.MsgWithdrawDelegatorReward
            url = "0a372f636f736d6f732e646973747269627574696f6e2e763162657461312e4d7367576974686472617744656c656761746f72526577617264";
        }

        String script = "03030201"
                //version=03 ScriptAssembler.hash=02=sha256 sign=01=ECDSA
                + ScriptAssembler.setCoinType(0x76)
                // tx_body
                + ScriptAssembler.copyString("0a")
                + ScriptAssembler.arrayPointer()
                // message
                + ScriptAssembler.copyString("0a")
                + ScriptAssembler.arrayPointer()
                // message.url
                + ScriptAssembler.copyString(url)
                // message.value
                + ScriptAssembler.copyString("12")
                + ScriptAssembler.arrayPointer()
                // from_or_delegator_address
                + ScriptAssembler.copyString("0a")
                + ScriptAssembler.protobuf(argFromOrDelegator, typeString)
                // to_or_validator_address
                + ScriptAssembler.copyString("12")
                + ScriptAssembler.protobuf(argToOrValidator, typeString);

        if (type != CosmosTxType.WITHDRAW) {
            script = script
                    // amount<Coin>
                    + ScriptAssembler.copyString("1a")
                    + ScriptAssembler.arrayPointer()
                    // coin.denom - uatom
                    + ScriptAssembler.copyString("0a057561746f6d")
                    // coin.amount
                    + ScriptAssembler.copyString("12")
                    + ScriptAssembler.arrayPointer()
                    + ScriptAssembler.baseConvert(argAmount, BufferType.TRANSACTION, 0, ScriptAssembler.decimalCharset, ScriptAssembler.leftJustify)
                    + ScriptAssembler.arrayEnd() // coin.amount end
                    + ScriptAssembler.arrayEnd(); // amount<coin> end
        }

        script = script
                + ScriptAssembler.arrayEnd() // message.value end
                + ScriptAssembler.arrayEnd() // message end
                // memo
                + ScriptAssembler.copyString("12")
                + ScriptAssembler.arrayPointer()
                + ScriptAssembler.copyRegularString(argMemo)
                + ScriptAssembler.arrayEnd() // memo end
                + ScriptAssembler.arrayEnd() // tx_body end

                // auth_info
                + ScriptAssembler.copyString("12")
                + ScriptAssembler.arrayPointer()
                // signer_info
                + ScriptAssembler.copyString("0a")
                + ScriptAssembler.arrayPointer()
                // pubkey
                + ScriptAssembler.copyString("0a460a1f2f636f736d6f732e63727970746f2e736563703235366b312e5075624b657912230a21")
                + ScriptAssembler.copyArgument(argPublicKey)
                // mode_info
                + ScriptAssembler.copyString("12040a020801")
                // sequence
                + ScriptAssembler.copyString("18")
                + ScriptAssembler.protobuf(argSequence, typeInt)
                + ScriptAssembler.arrayEnd() // signer_info end
                // fee
                + ScriptAssembler.copyString("12")
                + ScriptAssembler.arrayPointer()
                // amount<Coin>
                + ScriptAssembler.copyString("0a")
                + ScriptAssembler.arrayPointer()
                // coin.denom - uatom
                + ScriptAssembler.copyString("0a057561746f6d")
                // coin.amount
                + ScriptAssembler.copyString("12")
                + ScriptAssembler.arrayPointer()
                + ScriptAssembler.baseConvert(argFeeAmount, BufferType.TRANSACTION, 0, ScriptAssembler.decimalCharset, ScriptAssembler.leftJustify)
                + ScriptAssembler.arrayEnd() // coin.amount end
                + ScriptAssembler.arrayEnd() // amount<coin> end
                // gas_limit
                + ScriptAssembler.copyString("10")
                + ScriptAssembler.protobuf(argGas, typeInt)
                + ScriptAssembler.arrayEnd() // fee end
                + ScriptAssembler.arrayEnd() // auth_info end
                // chain_id
                + ScriptAssembler.copyString("1a0b636f736d6f736875622d34")
                // account_number
                + ScriptAssembler.copyString("20")
                + ScriptAssembler.protobuf(argAccountNumber, typeInt)
                // display
                + ScriptAssembler.showMessage("ATOM");

        if (type == CosmosTxType.DELEGATE) {
            script += ScriptAssembler.showMessage("Delgt");
        } else if (type == CosmosTxType.UNDELEGATE) {
            script += ScriptAssembler.showMessage("UnDel");
        } else if (type == CosmosTxType.WITHDRAW) {
            script += ScriptAssembler.showMessage("Reward");
        }

        script += ScriptAssembler.showAddress(argToOrValidator);

        if (type != CosmosTxType.WITHDRAW) {
            script += ScriptAssembler.showAmount(argAmount, 6);
        }

        script += ScriptAssembler.showPressButton();

        return script;
    }

    public static String getDOTScript(boolean isTestnet) {
    	ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptBuffer argCallIndex = sac.getArgument(2);
        ScriptBuffer argToAddr = sac.getArgument(32);
        ScriptBuffer argAmount = sac.getArgumentRightJustified(10);
        ScriptBuffer argMortalEra = sac.getArgumentRightJustified(5);
        ScriptBuffer argNonce = sac.getArgumentRightJustified(5);
        ScriptBuffer argTip = sac.getArgumentRightJustified(5);
        ScriptBuffer argSpecVer = sac.getArgument(4);
        ScriptBuffer argTxVer = sac.getArgument(4);
        ScriptBuffer argGenesisHash = sac.getArgument(32);
        ScriptBuffer argBlockHash = sac.getArgument(32);
        //version=02 ScriptAssembler.hash=0E=ScriptAssembler.Blake2b256 sign=01=ECDSA
        return "03020E01"
                // set coinType to 0162
                + ScriptAssembler.setCoinType(0x0162)
                // call index
                + ScriptAssembler.copyArgument(argCallIndex)
                //  dest address
                + ScriptAssembler.copyString("00")
                + ScriptAssembler.copyArgument(argToAddr)
                // value
                + ScriptAssembler.scaleEncode(argAmount, BufferType.TRANSACTION)
                // MortalEra
                + ScriptAssembler.copyArgument(argMortalEra)
                // nonce
                + ScriptAssembler.scaleEncode(argNonce, BufferType.TRANSACTION)
                // tip
                + ScriptAssembler.scaleEncode(argTip, BufferType.TRANSACTION)
                // spec ver
                + ScriptAssembler.copyArgument(argSpecVer)
                // tx ver
                + ScriptAssembler.copyArgument(argTxVer)
                // genesis hash
                + ScriptAssembler.copyArgument(argGenesisHash)
                // block hash
                + ScriptAssembler.copyArgument(argBlockHash)
                + ScriptAssembler.showMessage("DOT")
                + ScriptAssembler.copyString(Hex.toHexString("SS58PRE".getBytes()), BufferType.FREE)
                + ScriptAssembler.copyString("00", BufferType.FREE)
                + ScriptAssembler.copyArgument(argToAddr, BufferType.FREE)
                + ScriptAssembler.hash(ScriptBuffer.getDataBufferAll(BufferType.FREE), BufferType.FREE, ScriptAssembler.Blake2b512)
                + ScriptAssembler.baseConvert(ScriptBuffer.getBufer(BufferType.FREE, 7, 35), BufferType.EXTENDED, 0, ScriptAssembler.base58Charset, ScriptAssembler.zeroInherit)
                + ScriptAssembler.showAddress(ScriptBuffer.getDataBufferAll(BufferType.EXTENDED))
                + ScriptAssembler.showAmount(argAmount, 10)
                + ScriptAssembler.showWrap("PRESS", "BUTToN");
    }

    public static String getDOTBondScript(boolean isTestnet) {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptBuffer argCallIndex = sac.getArgument(2);
        ScriptBuffer argControllerAddr = sac.getArgument(32);
        ScriptBuffer argAmount = sac.getArgumentRightJustified(10);
        ScriptBuffer argPayeeType = sac.getArgument(1);
        ScriptBuffer argMortalEra = sac.getArgumentRightJustified(5);
        ScriptBuffer argNonce = sac.getArgumentRightJustified(5);
        ScriptBuffer argTip = sac.getArgumentRightJustified(5);
        ScriptBuffer argSpecVer = sac.getArgument(4);
        ScriptBuffer argTxVer = sac.getArgument(4);
        ScriptBuffer argGenesisHash = sac.getArgument(32);
        ScriptBuffer argBlockHash = sac.getArgument(32);
        //version=02 ScriptAssembler.hash=0E=ScriptAssembler.Blake2b256 sign=01=ECDSA
        return "03020E01"
                // set coinType to 0162
                + ScriptAssembler.setCoinType(0x0162)
                // call index
                + ScriptAssembler.copyArgument(argCallIndex)
                //  controller address
                + ScriptAssembler.copyString("00")
                + ScriptAssembler.copyArgument(argControllerAddr)
                // value
                + ScriptAssembler.scaleEncode(argAmount, BufferType.TRANSACTION)
                // payeeType
                + ScriptAssembler.copyArgument(argPayeeType)
                // MortalEra
                + ScriptAssembler.copyArgument(argMortalEra)
                // nonce
                + ScriptAssembler.scaleEncode(argNonce, BufferType.TRANSACTION)
                // tip
                + ScriptAssembler.scaleEncode(argTip, BufferType.TRANSACTION)
                // spec ver
                + ScriptAssembler.copyArgument(argSpecVer)
                // tx ver
                + ScriptAssembler.copyArgument(argTxVer)
                // genesis hash
                + ScriptAssembler.copyArgument(argGenesisHash)
                // block hash
                + ScriptAssembler.copyArgument(argBlockHash)
                + ScriptAssembler.showMessage("DOT")
                + ScriptAssembler.showMessage("Bond")
                + ScriptAssembler.copyString(Hex.toHexString("SS58PRE".getBytes()), BufferType.FREE)
                + ScriptAssembler.copyString("00", BufferType.FREE)
                + ScriptAssembler.copyArgument(argControllerAddr, BufferType.FREE)
                + ScriptAssembler.hash(ScriptBuffer.getDataBufferAll(BufferType.FREE), BufferType.FREE, ScriptAssembler.Blake2b512)
                + ScriptAssembler.baseConvert(ScriptBuffer.getBufer(BufferType.FREE, 7, 35), BufferType.EXTENDED, 0, ScriptAssembler.base58Charset, ScriptAssembler.zeroInherit)
                + ScriptAssembler.showAddress(ScriptBuffer.getDataBufferAll(BufferType.EXTENDED))
                + ScriptAssembler.showAmount(argAmount, 10)
                + ScriptAssembler.showWrap("PRESS", "BUTToN");
    }

    public static String getDOTUnbondScript(boolean isTestnet) {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptBuffer argCallIndex = sac.getArgument(2);
        ScriptBuffer argAmount = sac.getArgumentRightJustified(10);
        ScriptBuffer argMortalEra = sac.getArgumentRightJustified(5);
        ScriptBuffer argNonce = sac.getArgumentRightJustified(5);
        ScriptBuffer argTip = sac.getArgumentRightJustified(5);
        ScriptBuffer argSpecVer = sac.getArgument(4);
        ScriptBuffer argTxVer = sac.getArgument(4);
        ScriptBuffer argGenesisHash = sac.getArgument(32);
        ScriptBuffer argBlockHash = sac.getArgument(32);
        //version=02 ScriptAssembler.hash=0E=ScriptAssembler.Blake2b256 sign=01=ECDSA
        return "03020E01"
                // set coinType to 0162
                + ScriptAssembler.setCoinType(0x0162)
                // call index
                + ScriptAssembler.copyArgument(argCallIndex)
                // value
                + ScriptAssembler.scaleEncode(argAmount, BufferType.TRANSACTION)
                // MortalEra
                + ScriptAssembler.copyArgument(argMortalEra)
                // nonce
                + ScriptAssembler.scaleEncode(argNonce, BufferType.TRANSACTION)
                // tip
                + ScriptAssembler.scaleEncode(argTip, BufferType.TRANSACTION)
                // spec ver
                + ScriptAssembler.copyArgument(argSpecVer)
                // tx ver
                + ScriptAssembler.copyArgument(argTxVer)
                // genesis hash
                + ScriptAssembler.copyArgument(argGenesisHash)
                // block hash
                + ScriptAssembler.copyArgument(argBlockHash)
                + ScriptAssembler.showMessage("DOT")
                + ScriptAssembler.showMessage("Unbond")
                + ScriptAssembler.showAmount(argAmount, 10)
                + ScriptAssembler.showWrap("PRESS", "BUTToN");
    }

    public static String getDOTNominateSingleHashScript() {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptBuffer argCallIndex = sac.getArgument(2);
//        ScriptBuffer argTargetCount = sac.getBufer(1);
//        ScriptBuffer argTargetAddr = sac.getArgumentRightJustified(512);
        ScriptBuffer argMortalEra = sac.getArgumentRightJustified(5);
        ScriptBuffer argNonce = sac.getArgumentRightJustified(5);
        ScriptBuffer argTip = sac.getArgumentRightJustified(5);
        ScriptBuffer argSpecVer = sac.getArgument(4);
        ScriptBuffer argTxVer = sac.getArgument(4);
        ScriptBuffer argGenesisHash = sac.getArgument(32);
        ScriptBuffer argBlockHash = sac.getArgument(32);
        ScriptBuffer argTargetCount = sac.getArgument(1);
        ScriptBuffer argTargetAddrs = sac.getArgumentAll();
        //version=02 ScriptAssembler.hash=0E=ScriptAssembler.Blake2b256 sign=01=ECDSA
        return "03020E01"
                // set coinType to 0162
                + ScriptAssembler.setCoinType(0x0162)
                // call index
                + ScriptAssembler.copyArgument(argCallIndex)
                // target count
                + ScriptAssembler.scaleEncode(argTargetCount, BufferType.TRANSACTION)
                // target address
                + ScriptAssembler.copyArgument(argTargetAddrs)
                // MortalEra
                + ScriptAssembler.copyArgument(argMortalEra)
                // nonce
                + ScriptAssembler.scaleEncode(argNonce, BufferType.TRANSACTION)
                // tip
                + ScriptAssembler.scaleEncode(argTip, BufferType.TRANSACTION)
                // spec ver
                + ScriptAssembler.copyArgument(argSpecVer)
                // tx ver
                + ScriptAssembler.copyArgument(argTxVer)
                // genesis hash
                + ScriptAssembler.copyArgument(argGenesisHash)
                // block hash
                + ScriptAssembler.copyArgument(argBlockHash)
                + ScriptAssembler.showMessage("DOT")
                + ScriptAssembler.showMessage("Nomint")
                + ScriptAssembler.showWrap("PRESS", "BUTToN");
    }


    public static String getDOTNominateDoubleHashScript() {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptBuffer argCallIndex = sac.getArgument(2);
//        ScriptBuffer argTargetCount = sac.getBufer(1);
//        ScriptBuffer argTargetAddr = sac.getArgumentRightJustified(512);
        ScriptBuffer argMortalEra = sac.getArgumentRightJustified(5);
        ScriptBuffer argNonce = sac.getArgumentRightJustified(5);
        ScriptBuffer argTip = sac.getArgumentRightJustified(5);
        ScriptBuffer argSpecVer = sac.getArgument(4);
        ScriptBuffer argTxVer = sac.getArgument(4);
        ScriptBuffer argGenesisHash = sac.getArgument(32);
        ScriptBuffer argBlockHash = sac.getArgument(32);
        ScriptBuffer argTargetCount = sac.getArgument(1);
        ScriptBuffer argTargetAddrs = sac.getArgumentAll();
        //version=02 ScriptAssembler.hash=0E=ScriptAssembler.Blake2b256 sign=01=ECDSA
        return "03020E01"
                // set coinType to 0162
                + ScriptAssembler.setCoinType(0x0162)
                // call index
                + ScriptAssembler.copyArgument(argCallIndex)
                // target count
                + ScriptAssembler.scaleEncode(argTargetCount, BufferType.TRANSACTION)
                // target address
                + ScriptAssembler.copyArgument(argTargetAddrs)
                // MortalEra
                + ScriptAssembler.copyArgument(argMortalEra)
                // nonce
                + ScriptAssembler.scaleEncode(argNonce, BufferType.TRANSACTION)
                // tip
                + ScriptAssembler.scaleEncode(argTip, BufferType.TRANSACTION)
                // spec ver
                + ScriptAssembler.copyArgument(argSpecVer)
                // tx ver
                + ScriptAssembler.copyArgument(argTxVer)
                // genesis hash
                + ScriptAssembler.copyArgument(argGenesisHash)
                // block hash
                + ScriptAssembler.copyArgument(argBlockHash)
                + ScriptAssembler.hash(ScriptBuffer.getDataBufferAll(BufferType.TRANSACTION), BufferType.FREE, ScriptAssembler.Blake2b256)
                + ScriptAssembler.resetDest(BufferType.TRANSACTION)
                + ScriptAssembler.copyArgument(ScriptBuffer.getDataBufferAll(BufferType.FREE), BufferType.TRANSACTION)
                + ScriptAssembler.showMessage("DOT")
                + ScriptAssembler.showMessage("Nomint")
                + ScriptAssembler.showWrap("PRESS", "BUTToN");
    }

    public static String getDOTWithdrawScript(boolean isTestnet) {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptBuffer argCallIndex = sac.getArgument(2);
        ScriptBuffer argSlashingSpansNum = sac.getArgument(4);
        ScriptBuffer argMortalEra = sac.getArgumentRightJustified(5);
        ScriptBuffer argNonce = sac.getArgumentRightJustified(5);
        ScriptBuffer argTip = sac.getArgumentRightJustified(5);
        ScriptBuffer argSpecVer = sac.getArgument(4);
        ScriptBuffer argTxVer = sac.getArgument(4);
        ScriptBuffer argGenesisHash = sac.getArgument(32);
        ScriptBuffer argBlockHash = sac.getArgument(32);
        //version=02 ScriptAssembler.hash=0E=ScriptAssembler.Blake2b256 sign=01=ECDSA
        return "03020E01"
                // set coinType to 0162
                + ScriptAssembler.setCoinType(0x0162)
                // call index
                + ScriptAssembler.copyArgument(argCallIndex)
                // target num_slashing_spans
                + ScriptAssembler.ifEqual(argSlashingSpansNum, "00000000",
                  ScriptAssembler.copyString("00000000"),
                  ScriptAssembler.baseConvert(argSlashingSpansNum, BufferType.TRANSACTION, 4, ScriptAssembler.binaryCharset, ScriptAssembler.littleEndian)
								)
                // MortalEra
                + ScriptAssembler.copyArgument(argMortalEra)
                // nonce
                + ScriptAssembler.scaleEncode(argNonce, BufferType.TRANSACTION)
                // tip
                + ScriptAssembler.scaleEncode(argTip, BufferType.TRANSACTION)
                // spec ver
                + ScriptAssembler.copyArgument(argSpecVer)
                // tx ver
                + ScriptAssembler.copyArgument(argTxVer)
                // genesis hash
                + ScriptAssembler.copyArgument(argGenesisHash)
                // block hash
                + ScriptAssembler.copyArgument(argBlockHash)
                + ScriptAssembler.showMessage("DOT")
                + ScriptAssembler.showMessage("Withdr")
                + ScriptAssembler.showWrap("PRESS", "BUTToN");
    }

    public static String getDOTBondExtraScript(boolean isTestnet) {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptBuffer argCallIndex = sac.getArgument(2);
        ScriptBuffer argMaxAdditional = sac.getArgumentRightJustified(10);
        ScriptBuffer argMortalEra = sac.getArgumentRightJustified(5);
        ScriptBuffer argNonce = sac.getArgumentRightJustified(5);
        ScriptBuffer argTip = sac.getArgumentRightJustified(5);
        ScriptBuffer argSpecVer = sac.getArgument(4);
        ScriptBuffer argTxVer = sac.getArgument(4);
        ScriptBuffer argGenesisHash = sac.getArgument(32);
        ScriptBuffer argBlockHash = sac.getArgument(32);
        //version=02 ScriptAssembler.hash=0E=ScriptAssembler.Blake2b256 sign=01=ECDSA
        return "03020E01"
                // set coinType to 0162
                + ScriptAssembler.setCoinType(0x0162)
                // call index
                + ScriptAssembler.copyArgument(argCallIndex)
                // value
                + ScriptAssembler.scaleEncode(argMaxAdditional, BufferType.TRANSACTION)
                // MortalEra
                + ScriptAssembler.copyArgument(argMortalEra)
                // nonce
                + ScriptAssembler.scaleEncode(argNonce, BufferType.TRANSACTION)
                // tip
                + ScriptAssembler.scaleEncode(argTip, BufferType.TRANSACTION)
                // spec ver
                + ScriptAssembler.copyArgument(argSpecVer)
                // tx ver
                + ScriptAssembler.copyArgument(argTxVer)
                // genesis hash
                + ScriptAssembler.copyArgument(argGenesisHash)
                // block hash
                + ScriptAssembler.copyArgument(argBlockHash)
                + ScriptAssembler.showMessage("DOT")
                + ScriptAssembler.showMessage("BondExt")
                + ScriptAssembler.showAmount(argMaxAdditional, 10)
                + ScriptAssembler.showWrap("PRESS", "BUTToN");
    }
    
    public static String getBSCScript() {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptBuffer argTo = sac.getArgument(20);
        ScriptBuffer argValue = sac.getArgumentRightJustified(10);
        ScriptBuffer argGasPrice = sac.getArgumentRightJustified(10);
        ScriptBuffer argGasLimit = sac.getArgumentRightJustified(10);
        ScriptBuffer argNonce = sac.getArgumentRightJustified(8);
//        ScriptBuffer argChainId = sac.getArgumentRightJustified(2);
        //version=00 ScriptAssembler.hash=06=ScriptAssembler.Keccak256 sign=01=ECDSA
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
                + ScriptAssembler.copyString("94")
                + ScriptAssembler.copyArgument(argTo)
                // value
                + ScriptAssembler.rlpString(argValue)
                // data
                + ScriptAssembler.copyString("80")
                // chainId v = 56
                + ScriptAssembler.copyString("38", BufferType.EXTENDED) 
                + ScriptAssembler.rlpString(ScriptBuffer.getDataBufferAll(BufferType.EXTENDED))
                // r,s
                + ScriptAssembler.copyString("8080")
                + ScriptAssembler.rlpList(1)
                + ScriptAssembler.showMessage("BSC")
                + ScriptAssembler.showMessage("BNB")
                + ScriptAssembler.copyString(HexUtil.toHexString("0x"), BufferType.FREE)
                + ScriptAssembler.baseConvert(argTo, BufferType.FREE, 0, ScriptAssembler.hexadecimalCharset, ScriptAssembler.leftJustify)
                + ScriptAssembler.showAddress(ScriptBuffer.getDataBufferAll(BufferType.FREE))
                + ScriptAssembler.showAmount(argValue, 18)
                + ScriptAssembler.showPressButton();
    }


    /*
f86a
1e
85 01718c7e00
83 030d40
94 86fa049857e0209aa7d9e616f7eb3b3b78ecfdb0
80
b844 a9059cbb
     0000000000000000000000009e3a6e03298780078003d3965689b8f653ee87b1
     0000000000000000000000000000000000000000000000004563918244f40000
01
80
80
     */
    public static String getBEP20Script() {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptBuffer argTo = sac.getArgument(20);
        ScriptBuffer argValue = sac.getArgument(12);
        ScriptBuffer argGasPrice = sac.getArgumentRightJustified(10);
        ScriptBuffer argGasLimit = sac.getArgumentRightJustified(10);
        ScriptBuffer argNonce = sac.getArgumentRightJustified(8);
        ScriptBuffer argTokenInfo = sac.getArgumentUnion(0, 29);
        ScriptBuffer argDecimal = sac.getArgument(1);
        ScriptBuffer argNameLength = sac.getArgument(1);
        ScriptBuffer argName = sac.getArgumentVariableLength(7);
        ScriptBuffer argContractAddress = sac.getArgument(20);
        ScriptBuffer argSign = sac.getArgument(72);

        return "03000601"
                + //version=00 ScriptAssembler.hash=06=ScriptAssembler.Keccak256 sign=01=ECDSA
                ScriptAssembler.setCoinType(0x3C)
                + // set coinType to 3C
                ScriptAssembler.copyString("F800")
                + ScriptAssembler.rlpString(argNonce)
                + ScriptAssembler.rlpString(argGasPrice)
                + ScriptAssembler.rlpString(argGasLimit)
                + ScriptAssembler.copyString("94")
                + ScriptAssembler.copyArgument(argContractAddress)
                + ScriptAssembler.copyString("80B844a9059cbb000000000000000000000000")
                + //value = 0 , dataLength = 68
                ScriptAssembler.copyArgument(argTo)
                + ScriptAssembler.copyString("0000000000000000000000000000000000000000")
                + ScriptAssembler.copyArgument(argValue)
                // chainId v = 56
                + ScriptAssembler.copyString("38", BufferType.EXTENDED) 
                + ScriptAssembler.rlpString(ScriptBuffer.getDataBufferAll(BufferType.EXTENDED))
                + ScriptAssembler.copyString("8080")
                + ScriptAssembler.rlpList(2)
                + ScriptAssembler.showMessage("BSC")
                + ScriptAssembler.ifSigned(argTokenInfo, argSign, "",
                        ScriptAssembler.copyString(HexUtil.toHexString("@"), BufferType.FREE)
                )
                + ScriptAssembler.setBufferInt(argNameLength, 1, 7)
                + ScriptAssembler.copyArgument(argName, BufferType.FREE)
                + ScriptAssembler.showMessage(ScriptBuffer.getDataBufferAll(BufferType.FREE))
                + ScriptAssembler.resetDest(BufferType.FREE)
                + ScriptAssembler.copyString(HexUtil.toHexString("0x"), BufferType.FREE)
                + ScriptAssembler.baseConvert(argTo, BufferType.FREE, 0, ScriptAssembler.hexadecimalCharset, ScriptAssembler.leftJustify)
                + ScriptAssembler.showAddress(ScriptBuffer.getDataBufferAll(BufferType.FREE))
                + ScriptAssembler.setBufferInt(argDecimal, 0, 20)
                + ScriptAssembler.showAmount(argValue, 1000)
                + ScriptAssembler.showPressButton();
    }
    

    public static String getBSCSmartContractBlindScript() {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptBuffer argTo = sac.getArgument(20);
        ScriptBuffer argValue = sac.getArgumentRightJustified(10);
        ScriptBuffer argGasPrice = sac.getArgumentRightJustified(10);
        ScriptBuffer argGasLimit = sac.getArgumentRightJustified(10);
        ScriptBuffer argNonce = sac.getArgumentRightJustified(8);
        ScriptBuffer argData = sac.getArgumentAll();

        return "03000601"
                + //version=00 ScriptAssembler.hash=06=ScriptAssembler.Keccak256 sign=01=ECDSA
                ScriptAssembler.setCoinType(0x3C)
                + // set coinType to 3C
                ScriptAssembler.copyString("F800")
                + ScriptAssembler.rlpString(argNonce)
                + //nonce
                ScriptAssembler.rlpString(argGasPrice)
                + //gasPrice
                ScriptAssembler.rlpString(argGasLimit)
                + //gasLimit
                ScriptAssembler.copyString("94")
                + ScriptAssembler.copyArgument(argTo)
                + //toAddress
                ScriptAssembler.rlpString(argValue)
                + //value
                ScriptAssembler.rlpString(argData)        
                // chainId v = 56
                + ScriptAssembler.copyString("38", BufferType.EXTENDED) 
                + ScriptAssembler.rlpString(ScriptBuffer.getDataBufferAll(BufferType.EXTENDED))
                
                + ScriptAssembler.copyString("8080")
                + ScriptAssembler.rlpList(2)
                + ScriptAssembler.showMessage("BSC")
                + ScriptAssembler.showWrap("SMART", "")
                + ScriptAssembler.showPressButton();
    }

    

    public static String getBSCMessageBlindScript() {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptBuffer argMessage = sac.getArgumentAll();

        return "03000601"
                + //version=00 ScriptAssembler.hash=06=ScriptAssembler.Keccak256 sign=01=ECDSA
                ScriptAssembler.setCoinType(0x3C)
                + // set coinType to 3C
                ScriptAssembler.copyString("19457468657265756D205369676E6564204D6573736167653A0A3332")
                + ScriptAssembler.hash(argMessage, BufferType.TRANSACTION, ScriptAssembler.Keccak256)
                + ScriptAssembler.showMessage("BSC")
                + ScriptAssembler.showWrap("MESSAGE", "")
                + ScriptAssembler.showPressButton();
    }

    public static String getBSCTypedDataBlindScript() {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptBuffer argDomainSeparator = sac.getArgument(32);
        ScriptBuffer argMessage = sac.getArgumentAll();

        return "03000601"
                + //version=00 ScriptAssembler.hash=06=ScriptAssembler.Keccak256 sign=01=ECDSA
                ScriptAssembler.setCoinType(0x3C)
                + // set coinType to 3C
                ScriptAssembler.copyString("1901")
                + ScriptAssembler.copyArgument(argDomainSeparator)
                + ScriptAssembler.hash(argMessage, BufferType.TRANSACTION, ScriptAssembler.Keccak256)
                + ScriptAssembler.showMessage("BSC")
                + ScriptAssembler.showWrap("TYPED", "DATA")
                + ScriptAssembler.showPressButton();
    }
    
    public static String getKSMScript() {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptBuffer argCallIndex = sac.getArgument(2);
        ScriptBuffer argToAddr = sac.getArgument(32);
        ScriptBuffer argAmount = sac.getArgumentRightJustified(10);
        ScriptBuffer argMortalEra = sac.getArgumentRightJustified(5);
        ScriptBuffer argNonce = sac.getArgumentRightJustified(5);
        ScriptBuffer argTip = sac.getArgumentRightJustified(5);
        ScriptBuffer argSpecVer = sac.getArgument(4);
        ScriptBuffer argTxVer = sac.getArgument(4);
        ScriptBuffer argGenesisHash = sac.getArgument(32);
        ScriptBuffer argBlockHash = sac.getArgument(32);
        //version=02 ScriptAssembler.hash=0E=ScriptAssembler.Blake2b256 sign=01=ECDSA
        return "03020E01"
                // set coinType to 0162
                + ScriptAssembler.setCoinType(0x01b2)
                // call index
                + ScriptAssembler.copyArgument(argCallIndex)
                //  dest address
                + ScriptAssembler.copyString("00")
                + ScriptAssembler.copyArgument(argToAddr)
                // value
                + ScriptAssembler.scaleEncode(argAmount, BufferType.TRANSACTION)
                // MortalEra
                + ScriptAssembler.copyArgument(argMortalEra)
                // nonce
                + ScriptAssembler.scaleEncode(argNonce, BufferType.TRANSACTION)
                // tip
                + ScriptAssembler.scaleEncode(argTip, BufferType.TRANSACTION)
                // spec ver
                + ScriptAssembler.copyArgument(argSpecVer)
                // tx ver
                + ScriptAssembler.copyArgument(argTxVer)
                // genesis hash
                + ScriptAssembler.copyArgument(argGenesisHash)
                // block hash
                + ScriptAssembler.copyArgument(argBlockHash)
                + ScriptAssembler.showMessage("KSM")
                + ScriptAssembler.copyString(Hex.toHexString("SS58PRE".getBytes()), BufferType.FREE)
                + ScriptAssembler.copyString("02", BufferType.FREE)
                + ScriptAssembler.copyArgument(argToAddr, BufferType.FREE)
                + ScriptAssembler.hash(ScriptBuffer.getDataBufferAll(BufferType.FREE), BufferType.FREE, ScriptAssembler.Blake2b512)
                + ScriptAssembler.baseConvert(ScriptBuffer.getBufer(BufferType.FREE, 7, 35), BufferType.EXTENDED, 0, ScriptAssembler.base58Charset, ScriptAssembler.zeroInherit)
                + ScriptAssembler.showAddress(ScriptBuffer.getDataBufferAll(BufferType.EXTENDED))
                + ScriptAssembler.showAmount(argAmount, 12)
                + ScriptAssembler.showWrap("PRESS", "BUTToN");
    }

    public static String getKSMBondScript() {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptBuffer argCallIndex = sac.getArgument(2);
        ScriptBuffer argControllerAddr = sac.getArgument(32);
        ScriptBuffer argAmount = sac.getArgumentRightJustified(10);
        ScriptBuffer argPayeeType = sac.getArgument(1);
        ScriptBuffer argMortalEra = sac.getArgumentRightJustified(5);
        ScriptBuffer argNonce = sac.getArgumentRightJustified(5);
        ScriptBuffer argTip = sac.getArgumentRightJustified(5);
        ScriptBuffer argSpecVer = sac.getArgument(4);
        ScriptBuffer argTxVer = sac.getArgument(4);
        ScriptBuffer argGenesisHash = sac.getArgument(32);
        ScriptBuffer argBlockHash = sac.getArgument(32);
        //version=02 ScriptAssembler.hash=0E=ScriptAssembler.Blake2b256 sign=01=ECDSA
        return "03020E01"
                // set coinType to 0162
                + ScriptAssembler.setCoinType(0x01b2)
                // call index
                + ScriptAssembler.copyArgument(argCallIndex)
                //  controller address
                + ScriptAssembler.copyString("00")
                + ScriptAssembler.copyArgument(argControllerAddr)
                // value
                + ScriptAssembler.scaleEncode(argAmount, BufferType.TRANSACTION)
                // payeeType
                + ScriptAssembler.copyArgument(argPayeeType)
                // MortalEra
                + ScriptAssembler.copyArgument(argMortalEra)
                // nonce
                + ScriptAssembler.scaleEncode(argNonce, BufferType.TRANSACTION)
                // tip
                + ScriptAssembler.scaleEncode(argTip, BufferType.TRANSACTION)
                // spec ver
                + ScriptAssembler.copyArgument(argSpecVer)
                // tx ver
                + ScriptAssembler.copyArgument(argTxVer)
                // genesis hash
                + ScriptAssembler.copyArgument(argGenesisHash)
                // block hash
                + ScriptAssembler.copyArgument(argBlockHash)
                + ScriptAssembler.showMessage("KSM")
                + ScriptAssembler.showMessage("Bond")
                + ScriptAssembler.copyString(Hex.toHexString("SS58PRE".getBytes()), BufferType.FREE)
                + ScriptAssembler.copyString("02", BufferType.FREE)
                + ScriptAssembler.copyArgument(argControllerAddr, BufferType.FREE)
                + ScriptAssembler.hash(ScriptBuffer.getDataBufferAll(BufferType.FREE), BufferType.FREE, ScriptAssembler.Blake2b512)
                + ScriptAssembler.baseConvert(ScriptBuffer.getBufer(BufferType.FREE, 7, 35), BufferType.EXTENDED, 0, ScriptAssembler.base58Charset, ScriptAssembler.zeroInherit)
                + ScriptAssembler.showAddress(ScriptBuffer.getDataBufferAll(BufferType.EXTENDED))
                + ScriptAssembler.showAmount(argAmount, 12)
                + ScriptAssembler.showWrap("PRESS", "BUTToN");
    }


    public static String getKSMUnbondScript() {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptBuffer argCallIndex = sac.getArgument(2);
        ScriptBuffer argAmount = sac.getArgumentRightJustified(10);
        ScriptBuffer argMortalEra = sac.getArgumentRightJustified(5);
        ScriptBuffer argNonce = sac.getArgumentRightJustified(5);
        ScriptBuffer argTip = sac.getArgumentRightJustified(5);
        ScriptBuffer argSpecVer = sac.getArgument(4);
        ScriptBuffer argTxVer = sac.getArgument(4);
        ScriptBuffer argGenesisHash = sac.getArgument(32);
        ScriptBuffer argBlockHash = sac.getArgument(32);
        //version=02 ScriptAssembler.hash=0E=ScriptAssembler.Blake2b256 sign=01=ECDSA
        return "03020E01"
                // set coinType to 0162
                + ScriptAssembler.setCoinType(0x01b2)
                // call index
                + ScriptAssembler.copyArgument(argCallIndex)
                // value
                + ScriptAssembler.scaleEncode(argAmount, BufferType.TRANSACTION)
                // MortalEra
                + ScriptAssembler.copyArgument(argMortalEra)
                // nonce
                + ScriptAssembler.scaleEncode(argNonce, BufferType.TRANSACTION)
                // tip
                + ScriptAssembler.scaleEncode(argTip, BufferType.TRANSACTION)
                // spec ver
                + ScriptAssembler.copyArgument(argSpecVer)
                // tx ver
                + ScriptAssembler.copyArgument(argTxVer)
                // genesis hash
                + ScriptAssembler.copyArgument(argGenesisHash)
                // block hash
                + ScriptAssembler.copyArgument(argBlockHash)
                + ScriptAssembler.showMessage("KSM")
                + ScriptAssembler.showMessage("Unbond")
                + ScriptAssembler.showAmount(argAmount, 12)
                + ScriptAssembler.showWrap("PRESS", "BUTToN");
    }


    public static String getKSMBondExtraScript() {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptBuffer argCallIndex = sac.getArgument(2);
        ScriptBuffer argMaxAdditional = sac.getArgumentRightJustified(10);
        ScriptBuffer argMortalEra = sac.getArgumentRightJustified(5);
        ScriptBuffer argNonce = sac.getArgumentRightJustified(5);
        ScriptBuffer argTip = sac.getArgumentRightJustified(5);
        ScriptBuffer argSpecVer = sac.getArgument(4);
        ScriptBuffer argTxVer = sac.getArgument(4);
        ScriptBuffer argGenesisHash = sac.getArgument(32);
        ScriptBuffer argBlockHash = sac.getArgument(32);
        //version=02 ScriptAssembler.hash=0E=ScriptAssembler.Blake2b256 sign=01=ECDSA
        return "03020E01"
                // set coinType to 0162
                + ScriptAssembler.setCoinType(0x01b2)
                // call index
                + ScriptAssembler.copyArgument(argCallIndex)
                // value
                + ScriptAssembler.scaleEncode(argMaxAdditional, BufferType.TRANSACTION)
                // MortalEra
                + ScriptAssembler.copyArgument(argMortalEra)
                // nonce
                + ScriptAssembler.scaleEncode(argNonce, BufferType.TRANSACTION)
                // tip
                + ScriptAssembler.scaleEncode(argTip, BufferType.TRANSACTION)
                // spec ver
                + ScriptAssembler.copyArgument(argSpecVer)
                // tx ver
                + ScriptAssembler.copyArgument(argTxVer)
                // genesis hash
                + ScriptAssembler.copyArgument(argGenesisHash)
                // block hash
                + ScriptAssembler.copyArgument(argBlockHash)
                + ScriptAssembler.showMessage("KSM")
                + ScriptAssembler.showMessage("BondExt")
                + ScriptAssembler.showAmount(argMaxAdditional, 12)
                + ScriptAssembler.showWrap("PRESS", "BUTToN");
    }


    public static String getKSMNominateSingleHashScript() {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptBuffer argCallIndex = sac.getArgument(2);
        ScriptBuffer argMortalEra = sac.getArgumentRightJustified(5);
        ScriptBuffer argNonce = sac.getArgumentRightJustified(5);
        ScriptBuffer argTip = sac.getArgumentRightJustified(5);
        ScriptBuffer argSpecVer = sac.getArgument(4);
        ScriptBuffer argTxVer = sac.getArgument(4);
        ScriptBuffer argGenesisHash = sac.getArgument(32);
        ScriptBuffer argBlockHash = sac.getArgument(32);
        ScriptBuffer argTargetCount = sac.getArgument(1);
        ScriptBuffer argTargetAddrs = sac.getArgumentAll();
        //version=02 ScriptAssembler.hash=0E=ScriptAssembler.Blake2b256 sign=01=ECDSA
        return "03020E01"
                // set coinType to 0162
                + ScriptAssembler.setCoinType(0x01b2)
                // call index
                + ScriptAssembler.copyArgument(argCallIndex)
                // target count
                + ScriptAssembler.scaleEncode(argTargetCount, BufferType.TRANSACTION)
                // target address
                + ScriptAssembler.copyArgument(argTargetAddrs)
                // MortalEra
                + ScriptAssembler.copyArgument(argMortalEra)
                // nonce
                + ScriptAssembler.scaleEncode(argNonce, BufferType.TRANSACTION)
                // tip
                + ScriptAssembler.scaleEncode(argTip, BufferType.TRANSACTION)
                // spec ver
                + ScriptAssembler.copyArgument(argSpecVer)
                // tx ver
                + ScriptAssembler.copyArgument(argTxVer)
                // genesis hash
                + ScriptAssembler.copyArgument(argGenesisHash)
                // block hash
                + ScriptAssembler.copyArgument(argBlockHash)
                + ScriptAssembler.showMessage("KSM")
                + ScriptAssembler.showMessage("Nomint")
                + ScriptAssembler.showWrap("PRESS", "BUTToN");
    }

    public static String getKSMNominateDoubleHashScript() {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptBuffer argCallIndex = sac.getArgument(2);
//        ScriptBuffer argTargetCount = sac.getBufer(1);
//        ScriptBuffer argTargetAddr = sac.getArgumentRightJustified(512);
        ScriptBuffer argMortalEra = sac.getArgumentRightJustified(5);
        ScriptBuffer argNonce = sac.getArgumentRightJustified(5);
        ScriptBuffer argTip = sac.getArgumentRightJustified(5);
        ScriptBuffer argSpecVer = sac.getArgument(4);
        ScriptBuffer argTxVer = sac.getArgument(4);
        ScriptBuffer argGenesisHash = sac.getArgument(32);
        ScriptBuffer argBlockHash = sac.getArgument(32);
        ScriptBuffer argTargetCount = sac.getArgument(1);
        ScriptBuffer argTargetAddrs = sac.getArgumentAll();
        //version=02 ScriptAssembler.hash=0E=ScriptAssembler.Blake2b256 sign=01=ECDSA
        return "03020E01"
                // set coinType to 0162
                + ScriptAssembler.setCoinType(0x01b2)
                // call index
                + ScriptAssembler.copyArgument(argCallIndex)
                // target count
                + ScriptAssembler.scaleEncode(argTargetCount, BufferType.TRANSACTION)
                // target address
                + ScriptAssembler.copyArgument(argTargetAddrs)
                // MortalEra
                + ScriptAssembler.copyArgument(argMortalEra)
                // nonce
                + ScriptAssembler.scaleEncode(argNonce, BufferType.TRANSACTION)
                // tip
                + ScriptAssembler.scaleEncode(argTip, BufferType.TRANSACTION)
                // spec ver
                + ScriptAssembler.copyArgument(argSpecVer)
                // tx ver
                + ScriptAssembler.copyArgument(argTxVer)
                // genesis hash
                + ScriptAssembler.copyArgument(argGenesisHash)
                // block hash
                + ScriptAssembler.copyArgument(argBlockHash)
                + ScriptAssembler.hash(ScriptBuffer.getDataBufferAll(BufferType.TRANSACTION), BufferType.FREE, ScriptAssembler.Blake2b256)
                + ScriptAssembler.resetDest(BufferType.TRANSACTION)
                + ScriptAssembler.copyArgument(ScriptBuffer.getDataBufferAll(BufferType.FREE), BufferType.TRANSACTION)
                + ScriptAssembler.showMessage("KSM")
                + ScriptAssembler.showMessage("Nomint")
                + ScriptAssembler.showWrap("PRESS", "BUTToN");
    }

    public static String getKSMWithdrawScript() {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptBuffer argCallIndex = sac.getArgument(2);
        ScriptBuffer argSlashingSpansNum = sac.getArgument(4);
        ScriptBuffer argMortalEra = sac.getArgumentRightJustified(5);
        ScriptBuffer argNonce = sac.getArgumentRightJustified(5);
        ScriptBuffer argTip = sac.getArgumentRightJustified(5);
        ScriptBuffer argSpecVer = sac.getArgument(4);
        ScriptBuffer argTxVer = sac.getArgument(4);
        ScriptBuffer argGenesisHash = sac.getArgument(32);
        ScriptBuffer argBlockHash = sac.getArgument(32);
        //version=02 ScriptAssembler.hash=0E=ScriptAssembler.Blake2b256 sign=01=ECDSA
        return "03020E01"
                // set coinType to 0162
                + ScriptAssembler.setCoinType(0x01b2)
                // call index
                + ScriptAssembler.copyArgument(argCallIndex)
                // target num_slashing_spans
                + ScriptAssembler.ifEqual(argSlashingSpansNum, "00000000",
                  ScriptAssembler.copyString("00000000"),
                  ScriptAssembler.baseConvert(argSlashingSpansNum, BufferType.TRANSACTION, 4, ScriptAssembler.binaryCharset, ScriptAssembler.littleEndian)
								)
                // MortalEra
                + ScriptAssembler.copyArgument(argMortalEra)
                // nonce
                + ScriptAssembler.scaleEncode(argNonce, BufferType.TRANSACTION)
                // tip
                + ScriptAssembler.scaleEncode(argTip, BufferType.TRANSACTION)
                // spec ver
                + ScriptAssembler.copyArgument(argSpecVer)
                // tx ver
                + ScriptAssembler.copyArgument(argTxVer)
                // genesis hash
                + ScriptAssembler.copyArgument(argGenesisHash)
                // block hash
                + ScriptAssembler.copyArgument(argBlockHash)
                + ScriptAssembler.showMessage("KSM")
                + ScriptAssembler.showMessage("Withdr")
                + ScriptAssembler.showWrap("PRESS", "BUTToN");
    }

}
