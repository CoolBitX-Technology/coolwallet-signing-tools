package com.coolbitx.wallet.signing.scriptlib;

import com.coolbitx.wallet.signing.utils.HexUtil;
import com.coolbitx.wallet.signing.utils.ScriptArgumentComposer;
import com.coolbitx.wallet.signing.utils.ScriptAssembler;
import com.coolbitx.wallet.signing.utils.ScriptData;
import com.coolbitx.wallet.signing.utils.ScriptAssembler.HashType;
import com.coolbitx.wallet.signing.utils.ScriptAssembler.SignType;
import com.coolbitx.wallet.signing.utils.ScriptData.Buffer;

public class ThetaScript {

    public static void listAll() {
        System.out.println("Theta Send: \n" + getSendScript() + "\n");
        System.out.println("Theta Stake Validator: \n" + getStakeValidatorScript() + "\n");
        System.out.println("Theta Stake Guardian: \n" + getStakeGuardianScript() + "\n");
        System.out.println("Theta Stake Edge: \n" + getStakeEdgeScript() + "\n");
        System.out.println("Theta Withdraw: \n" + getWithdrawScript() + "\n");
        System.out.println("Theta Smart: \n" + getSmartScript() + "\n");
        System.out.println("Theta EVM: \n" + getEvmScript() + "\n");
    }

    // *** Send ***
    //
    // type          02
    // rlpList       f8 4f
    // fee             ca
    //   theta           80
    //   tfuel           88 0429d069189e0000
    // inputs          e3
    //   input1          e2
    //     from            94 0000000000000000000000000000000000000000
    //     coin            ca
    //       theta           80
    //       tfuel           88 042a2b5c29184000
    //     sequence        01
    //     signature       80
    // outputs         df
    //   output1         de
    //     to              94 3d65d8d9792b8777188c481e6b8d859e29de293c
    //     coin            c8
    //       theta           80
    //       tfuel           86 5af3107a4000

    public static String getSendScript() {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptData argTheta = sac.getArgumentRightJustified(12);
        ScriptData argTFuel = sac.getArgumentRightJustified(12);
        ScriptData argTFuelPlusFee = sac.getArgumentRightJustified(12);
        ScriptData argSequence = sac.getArgumentRightJustified(8);
        ScriptData argFrom = sac.getArgument(20);
        ScriptData argTo = sac.getArgument(20);

        String script = new ScriptAssembler()
                .setCoinType(500)

                // ETH Wrapper
                .arrayPointer()
                .copyString("80808094000000000000000000000000000000000000000080")
                .arrayPointer()

                // chainId + txType
                .copyString("876d61696e6e657402")
                .arrayPointer()

                // constant fee 0.3
                .copyString("ca80880429d069189e0000")

                // inputs.input1
                .arrayPointer()
                .arrayPointer()
                .copyString("94").copyArgument(argFrom) // from
                .arrayPointer() // coin
                .rlpString(argTheta)
                .rlpString(argTFuelPlusFee)
                .arrayEnd(1)
                .rlpString(argSequence) // sequence
                .copyString("80") // signature
                .arrayEnd(1)
                .arrayEnd(1)

                // outputs.output1
                .arrayPointer()
                .arrayPointer()
                .copyString("94").copyArgument(argTo) // to
                .arrayPointer() // coin
                .rlpString(argTheta)
                .rlpString(argTFuel)
                .arrayEnd(1)
                .arrayEnd(1)
                .arrayEnd(1)
                .arrayEnd(1)
                .arrayEnd(1)
                .arrayEnd(1)

                .showMessage("THETA")
                .showWrap("SEND", "")
                .copyString(HexUtil.toHexString("0x"), Buffer.CACHE2)
                .baseConvert(argTo, Buffer.CACHE2, 0, ScriptAssembler.hexadecimalCharset, ScriptAssembler.zeroInherit)
                .showAddress(ScriptData.getDataBufferAll(Buffer.CACHE2))
                .showAmount(argTheta, 18)
                .showWrap("TFUEL", "")
                .showAmount(argTFuel, 18)
                .showPressButton()
                //version=04 ScriptAssembler.hash=06=ScriptAssembler.Keccak256 sign=01=ECDSA
                .setHeader(HashType.Keccak256, SignType.ECDSA)
                .getScript();
        return script;
    }

    // *** Stake ***
    //
    // type           08
    // rlpList        f8 47
    // fee              ca
    //   theta            80
    //   tfuel            88 0429d069189e0000
    // source (input)   e1
    //   from             94 7959923bafadd39f3b31957c182ea3a4db447bc5
    //   coin             c9
    //     theta            87 b1a2bc2ec50000
    //     tfuel            80
    //   sequence         01
    //   signature        80
    // holder (output)  d8
    //   to               94 3d65d8d9792b8777188c481e6b8d859e29de293c
    //   coin             c2
    //     theta            80
    //     tfuel            80
    // purpose          00

    public static String getStakeValidatorScript() {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptData argTheta = sac.getArgumentRightJustified(12);
        ScriptData argSequence = sac.getArgumentRightJustified(8);
        ScriptData argFrom = sac.getArgument(20);
        ScriptData argTo = sac.getArgument(20);

        String script = new ScriptAssembler()
                .setCoinType(500)

                // ETH Wrapper
                .arrayPointer()
                .copyString("80808094000000000000000000000000000000000000000080")
                .arrayPointer()

                // chainId + txType
                .copyString("876d61696e6e657408")
                .arrayPointer()

                // constant fee 0.3
                .copyString("ca80880429d069189e0000")

                // source
                .arrayPointer()
                .copyString("94").copyArgument(argFrom) // from
                .arrayPointer() // coin
                .rlpString(argTheta)
                .copyString("80")
                .arrayEnd(1)
                .rlpString(argSequence) // sequence
                .copyString("80") // signature
                .arrayEnd(1)

                // holder
                .copyString("d894").copyArgument(argTo) // to
                .copyString("c28080") // coin

                // purpose
                .copyString("00")
                .arrayEnd(1)
                .arrayEnd(1)
                .arrayEnd(1)

                .showMessage("THETA")
                .showWrap("STAKE", "VALIDAT")
                .copyString(HexUtil.toHexString("0x"), Buffer.CACHE2)
                .baseConvert(argTo, Buffer.CACHE2, 0, ScriptAssembler.hexadecimalCharset, ScriptAssembler.zeroInherit)
                .showAddress(ScriptData.getDataBufferAll(Buffer.CACHE2))
                .showAmount(argTheta, 18)
                .showPressButton()
                //version=04 ScriptAssembler.hash=06=ScriptAssembler.Keccak256 sign=01=ECDSA
                .setHeader(HashType.Keccak256, SignType.ECDSA)
                .getScript();
        return script;
    }

    // *** Stake v2 ***
    //
    // type             0a
    // rlpList            f9 011d
    // fee                  ca
    //   theta                80
    //   tfuel                88 0429d069189e0000
    // source (input)       e1
    //   from                 94 7959923bafadd39f3b31957c182ea3a4db447bc5
    //   coin                 c9
    //     theta                87 b1a2bc2ec50000
    //     tfuel                80
    //   sequence             01
    //   signature            80
    // holder (output)      d8
    //   to                   94 e9931a0016e2accc2179ed9ba575d2ced817d09f
    //   coin                 c2
    //     theta                80
    //     tfuel                80
    // purpose              01
    // blsPubkeyBytes       b0 af3389266cc60396b95d9893444a7cc5567c96b37ea3d16f
    //                         8bf1b61247c6ed4830ae675a9b2f11173db5f8f913bdc135
    // blsPopBytes          b8 60 b6c48e68aff41ee9dc3bfb6f2aa77bf7dbb050308cb10ff2
    //                            82a529d3c0253a8d90bb0adcaeb7913fa04e9d96abebeccb
    //                            012570311744cb583264a06b2578dd767ec6a0c0c7302b0d
    //                            28fcd22f1d337088cb68d959a41fdd74ce74d093971ba6a9
    // holderSigBytes       b8 41 116876996bc474382051c122b0d4bf00
    //                            7cbf6c7e961af5a396a2622136e54e45
    //                            2f7b0b54ae0e64c57a2abd6e524fd6cc
    //                            2370ccc3756412979bdeff8256062291
    //                            00

    public static String getStakeGuardianScript() {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptData argTheta = sac.getArgumentRightJustified(12);
        ScriptData argSequence = sac.getArgumentRightJustified(8);
        ScriptData argFrom = sac.getArgument(20);
        // holderSummary (229): [to (20)][pubkey (48)][pop (96)][sig (65)]
        ScriptData argTo = sac.getArgument(20);
        ScriptData argPubKey = sac.getArgumentRightJustified(48);
        ScriptData argPop = sac.getArgumentRightJustified(96);
        ScriptData argHolderSig = sac.getArgumentRightJustified(65);

        String script = new ScriptAssembler()
                .setCoinType(500)

                // ETH Wrapper
                .arrayPointer()
                .copyString("80808094000000000000000000000000000000000000000080")
                .arrayPointer()

                // chainId + txType
                .copyString("876d61696e6e65740a")
                .arrayPointer()

                // constant fee 0.3
                .copyString("ca80880429d069189e0000")

                // source
                .arrayPointer()
                .copyString("94").copyArgument(argFrom) // from
                .arrayPointer() // coin
                .rlpString(argTheta)
                .copyString("80")
                .arrayEnd(1)
                .rlpString(argSequence) // sequence
                .copyString("80") // signature
                .arrayEnd(1)

                // holder
                .copyString("d894").copyArgument(argTo) // to
                .copyString("c28080") // coin

                // purpose
                .copyString("01")

                // nodeKey
                .copyString("b0").copyArgument(argPubKey)
                .copyString("b860").copyArgument(argPop)
                .copyString("b841").copyArgument(argHolderSig)
                .arrayEnd(1)
                .arrayEnd(1)
                .arrayEnd(1)

                .showMessage("THETA")
                .showWrap("STAKE", "GUARDIA")
                .copyString(HexUtil.toHexString("0x"), Buffer.CACHE2)
                .baseConvert(argTo, Buffer.CACHE2, 0, ScriptAssembler.hexadecimalCharset, ScriptAssembler.zeroInherit)
                .showAddress(ScriptData.getDataBufferAll(Buffer.CACHE2))
                .showAmount(argTheta, 18)
                .showPressButton()
                //version=04 ScriptAssembler.hash=06=ScriptAssembler.Keccak256 sign=01=ECDSA
                .setHeader(HashType.Keccak256, SignType.ECDSA)
                .getScript();
        return script;
    }

    public static String getStakeEdgeScript() {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptData argTFuel = sac.getArgumentRightJustified(12);
        ScriptData argSequence = sac.getArgumentRightJustified(8);
        ScriptData argFrom = sac.getArgument(20);
        // holderSummary (229): [to (20)][pubkey (48)][pop (96)][sig (65)]
        ScriptData argTo = sac.getArgument(20);
        ScriptData argPubKey = sac.getArgumentRightJustified(48);
        ScriptData argPop = sac.getArgumentRightJustified(96);
        ScriptData argHolderSig = sac.getArgumentRightJustified(65);

        String script = new ScriptAssembler()
                .setCoinType(500)

                // ETH Wrapper
                .arrayPointer()
                .copyString("80808094000000000000000000000000000000000000000080")
                .arrayPointer()

                // chainId + txType
                .copyString("876d61696e6e65740a")
                .arrayPointer()

                // constant fee 0.3
                .copyString("ca80880429d069189e0000")

                // source
                .arrayPointer()
                .copyString("94").copyArgument(argFrom) // from
                .arrayPointer() // coin
                .copyString("80")
                .rlpString(argTFuel)
                .arrayEnd(1)
                .rlpString(argSequence) // sequence
                .copyString("80") // signature
                .arrayEnd(1)

                // holder
                .copyString("d894").copyArgument(argTo) // to
                .copyString("c28080") // coin

                // purpose
                .copyString("02")

                // nodeKey
                .copyString("b0").copyArgument(argPubKey)
                .copyString("b860").copyArgument(argPop)
                .copyString("b841").copyArgument(argHolderSig)
                .arrayEnd(1)
                .arrayEnd(1)
                .arrayEnd(1)

                .showMessage("THETA")
                .showWrap("STAKE", "EDGE")
                .copyString(HexUtil.toHexString("0x"), Buffer.CACHE2)
                .baseConvert(argTo, Buffer.CACHE2, 0, ScriptAssembler.hexadecimalCharset, ScriptAssembler.zeroInherit)
                .showAddress(ScriptData.getDataBufferAll(Buffer.CACHE2))
                .showAmount(argTFuel, 18)
                .showPressButton()
                //version=04 ScriptAssembler.hash=06=ScriptAssembler.Keccak256 sign=01=ECDSA
                .setHeader(HashType.Keccak256, SignType.ECDSA)
                .getScript();
        return script;
    }

    // *** Withdraw ***
    //
    // type            09
    // rlpList         f8 40
    // fee               ca
    //   theta             80
    //   tfuel             88 0429d069189e0000
    // source (input)    da
    //   from              94 7959923bafadd39f3b31957c182ea3a4db447bc5
    //   coin              c2
    //     theta             80
    //     tfuel             80
    //   sequence          01
    //   signature         80
    // holder (output)   d8
    //   to                94 3d65d8d9792b8777188c481e6b8d859e29de293c
    //   coin              c2
    //     theta             80
    //     tfuel             80
    // purpose           80

    public static String getWithdrawScript() {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptData argPurpose = sac.getArgument(1);
        ScriptData argSequence = sac.getArgumentRightJustified(8);
        ScriptData argFrom = sac.getArgument(20);
        ScriptData argTo = sac.getArgument(20);

        String script = new ScriptAssembler()
                .setCoinType(500)

                // ETH Wrapper
                .arrayPointer()
                .copyString("80808094000000000000000000000000000000000000000080")
                .arrayPointer()

                // chainId + txType
                .copyString("876d61696e6e657409")
                .arrayPointer()

                // constant fee 0.3
                .copyString("ca80880429d069189e0000")

                // source
                .arrayPointer()
                .copyString("94").copyArgument(argFrom) // from
                .copyString("c28080") // coin
                .rlpString(argSequence) // sequence
                .copyString("80") // signature
                .arrayEnd(1)

                // holder
                .copyString("d894").copyArgument(argTo) // to
                .copyString("c28080") // coin

                // purpose
                .copyArgument(argPurpose)
                .arrayEnd(1)
                .arrayEnd(1)
                .arrayEnd(1)

                .showMessage("THETA")
                .showWrap("WITHDRAW", "")
                .copyString(HexUtil.toHexString("0x"), Buffer.CACHE2)
                .baseConvert(argTo, Buffer.CACHE2, 0, ScriptAssembler.hexadecimalCharset, ScriptAssembler.zeroInherit)
                .showAddress(ScriptData.getDataBufferAll(Buffer.CACHE2))
                .showPressButton()
                //version=04 ScriptAssembler.hash=06=ScriptAssembler.Keccak256 sign=01=ECDSA
                .setHeader(HashType.Keccak256, SignType.ECDSA)
                .getScript();
        return script;
    }

    // *** Smart ***
    //
    // type        07
    // rlpList     f8 85
    // input         da
    //   from          94 67a96cf71b307f5130c6e59296ea463ddbd0aa67
    //   coin          c2
    //     theta         80
    //     tfuel         80
    //   sequence      01
    //   signature     80
    // output        d8
    //   to            94 0196a888ab58533bf9bd135503f7f552ff3bf987
    //   coin          c2
    //     theta         80
    //     tfuel         80
    // gasLimit      83 989680
    // gasPrice      86 03a352944000
    // data          b8 44 a9059cbb
    //                     0000000000000000000000003d65d8d9792b8777188c481e6b8d859e29de293c
    //                     00000000000000000000000000000000000000000000000000000000000003e8

    public static String getSmartScript() {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptData argValue = sac.getArgumentRightJustified(12);
        ScriptData argSequence = sac.getArgumentRightJustified(8);
        ScriptData argFrom = sac.getArgument(20);
        ScriptData argTo = sac.getArgument(20);
        ScriptData argGasLimit = sac.getArgumentRightJustified(10);
        ScriptData argData = sac.getArgumentAll();

        String script = new ScriptAssembler()
                .setCoinType(500)

                // ETH Wrapper
                .arrayPointer()
                .copyString("80808094000000000000000000000000000000000000000080")
                .arrayPointer()

                // chainId + txType
                .copyString("876d61696e6e657407")
                .arrayPointer()

                // input
                .arrayPointer()
                .copyString("94").copyArgument(argFrom) // from
                .arrayPointer() // coin
                .copyString("80")
                .rlpString(argValue)
                .arrayEnd(1)
                .rlpString(argSequence) // sequence
                .copyString("80") // signature
                .arrayEnd(1)

                // output
                .copyString("d894").copyArgument(argTo) // to
                .copyString("c28080") // coin

                .rlpString(argGasLimit) // gasLimit
                .copyString("8603a352944000") // gasPrice
                .rlpString(argData) // data
                .arrayEnd(1)
                .arrayEnd(1)
                .arrayEnd(1)

                .showMessage("THETA")
                .showWrap("SMART", "")
                .copyString(HexUtil.toHexString("0x"), Buffer.CACHE2)
                .baseConvert(argTo, Buffer.CACHE2, 0, ScriptAssembler.hexadecimalCharset, ScriptAssembler.zeroInherit)
                .showAddress(ScriptData.getDataBufferAll(Buffer.CACHE2))
                .showAmount(argValue, 18)
                .showPressButton()
                //version=04 ScriptAssembler.hash=06=ScriptAssembler.Keccak256 sign=01=ECDSA
                .setHeader(HashType.Keccak256, SignType.ECDSA)
                .getScript();
        return script;
    }

    public static String getEvmScript() {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptData argTo = sac.getArgument(20);
        ScriptData argValue = sac.getArgumentRightJustified(12);
        ScriptData argGasLimit = sac.getArgumentRightJustified(10);
        ScriptData argNonce = sac.getArgumentRightJustified(8);
        ScriptData argData = sac.getArgumentAll();

        String script = new ScriptAssembler()
                .setCoinType(500)
                .arrayPointer()
                .rlpString(argNonce)
                .copyString("8603a352944000") // gasPrice = 0.0004
                .rlpString(argGasLimit)
                .copyString("94").copyArgument(argTo)
                .rlpString(argValue)
                .rlpString(argData)
                .copyString("0169", Buffer.CACHE1) // chainId = 361
                .rlpString(ScriptData.getDataBufferAll(Buffer.CACHE1))
                .copyString("8080") // r, s
                .arrayEnd(1)
                .showMessage("THETA")
                .showWrap("EVM", "")
                .copyString(HexUtil.toHexString("0x"), Buffer.CACHE2)
                .baseConvert(argTo, Buffer.CACHE2, 0, ScriptAssembler.hexadecimalCharset, ScriptAssembler.zeroInherit)
                .showAddress(ScriptData.getDataBufferAll(Buffer.CACHE2))
                .showAmount(argValue, 18)
                .showPressButton()
                .setHeader(HashType.Keccak256, SignType.ECDSA)
                .getScript();
        return script;
    }
}
