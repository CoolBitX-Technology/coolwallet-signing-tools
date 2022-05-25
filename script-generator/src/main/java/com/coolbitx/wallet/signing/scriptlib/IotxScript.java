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

public class IotxScript {

    private static final int typeInt = 0;
    private static final int typeString = 2;

    public static void listAll() {
        System.out.println("IOTX Transfer: \n" + getTransferScript() + "\n");
        System.out.println("IOTX XRC20: \n" + getXRC20Script() + "\n");
        System.out.println("IOTX Execute: \n" + getExecuteScript() + "\n");
        System.out.println("IOTX StakeCreate: \n" + getStakeCreateScript() + "\n");
        System.out.println("IOTX StakeUnstake: \n" + getStakeUnstakeScript() + "\n");
        System.out.println("IOTX StakeWithdraw: \n" + getStakeWithdrawScript() + "\n");
        System.out.println("IOTX StakeAddDeposit: \n" + getStakeAddDepositScript() + "\n");
    }

    //  version:     00001 (1)  000 (0) | 08 01 (1)
    //  nonce:       00010 (2)  000 (0) | 10 df06 (863)
    //  gasLimit:    00011 (3)  000 (0) | 18 a08d06 (100000)
    //  gasPrice:    00100 (4)  010 (2) | 22 0d (13) 31303030303030303030303030
    //  transfer:    01010 (10) 010 (2) | 52 40 (64)
    //    amount:      00001 (1)  010 (2) | 0a 13 (19) 31303030303030303030303030303030303030
    //    recipient:   00010 (2)  010 (2) | 12 29 (41) 696f3167357575773973356a703668386a6a77723578367a6c6567616b71383738676b73707732616a

    public static String getTransferScript() {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptData argNonce = sac.getArgumentRightJustified(8);
        ScriptData argGasLimit = sac.getArgumentRightJustified(8);
        ScriptData argGasPrice = sac.getArgumentRightJustified(12);
        ScriptData argAmount = sac.getArgumentRightJustified(12);
        ScriptData argRecipient = sac.getArgument(41);
        ScriptData argIsPayload = sac.getArgument(1);
        ScriptData argPayload = sac.getArgumentAll();

        String script = new ScriptAssembler()
            .setCoinType(0x0130)
            .copyString("0801")
            .ifEqual(argNonce, "0000000000000000", "",
                new ScriptAssembler().copyString("10").protobuf(argNonce, typeInt).getScript())
            .ifEqual(argGasLimit, "00000000000000000000", "",
                new ScriptAssembler().copyString("18").protobuf(argGasLimit, typeInt).getScript())
            .baseConvert(argGasPrice, Buffer.CACHE1, 0, ScriptAssembler.decimalCharset, ScriptAssembler.leftJustify)
            .copyString("22").protobuf(ScriptData.getDataBufferAll(Buffer.CACHE1), typeString)
            .clearBuffer(Buffer.CACHE1)
            // transfer
            .copyString("52").arrayPointer()
            .baseConvert(argAmount, Buffer.CACHE1, 0, ScriptAssembler.decimalCharset, ScriptAssembler.leftJustify)
            .copyString("0a").protobuf(ScriptData.getDataBufferAll(Buffer.CACHE1), typeString)
            .clearBuffer(Buffer.CACHE1)
            .copyString("1229").copyArgument(argRecipient)
            .ifEqual(argIsPayload, "00", "",
                new ScriptAssembler().copyString("1a").protobuf(argPayload, typeString).getScript())
            .arrayEnd()
            // verification
            .showMessage("IOTX")
            .showWrap("TRANSFER", "")
            .showAddress(argRecipient)
            .showAmount(argAmount, 18)
            .showPressButton()
            .setHeader(HashType.Keccak256, SignType.ECDSA)
            .getScript();

        return script;
    }

    //  version:    00001 (1)  000 (0) | 08 01 (1)
    //  nonce:      00010 (2)  000 (0) | 10 df06 (863)
    //  gasLimit:   00011 (3)  000 (0) | 18 a08d06 (100000)
    //  gasPrice:   00100 (4)  010 (2) | 22 0d (13) 31303030303030303030303030
    //  execution:  01100 (12) 010 (2) | 62 9601 (150)
    //    amount:     00001 (1)  010 (2) | 0a 01 (1) 30
    //    contract:   00010 (2)  010 (2) | 12 29 (41) 696f316a6d713065706373777a753776797175786c72396a396a76706c7770767463346435307a6539
    //    data:       00011 (3)  010 (2) | 1a 44 (68) a9059cbb0000000000000000000000008896780a7912829781f70344ab93e589dddb29300000000000000000000000000000000000000000000000001bc16d674ec80000

    public static String getXRC20Script() {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptData argNonce = sac.getArgumentRightJustified(8);
        ScriptData argGasLimit = sac.getArgumentRightJustified(8);
        ScriptData argGasPrice = sac.getArgumentRightJustified(12);
        ScriptData argAmount = sac.getArgument(12);
        ScriptData argRecipient = sac.getArgument(20);
        ScriptData argTokenInfo = sac.getArgumentUnion(0, 50);
        ScriptData argDecimal = sac.getArgument(1);
        ScriptData argNameLength = sac.getArgument(1);
        ScriptData argName = sac.getArgumentVariableLength(7);
        ScriptData argContract = sac.getArgument(41);
        ScriptData argSign = sac.getArgument(72);

        String script = new ScriptAssembler()
            .setCoinType(0x0130)
            .copyString("0801")
            .ifEqual(argNonce, "0000000000000000", "",
                new ScriptAssembler().copyString("10").protobuf(argNonce, typeInt).getScript())
            .ifEqual(argGasLimit, "00000000000000000000", "",
                new ScriptAssembler().copyString("18").protobuf(argGasLimit, typeInt).getScript())
            .baseConvert(argGasPrice, Buffer.CACHE1, 0, ScriptAssembler.decimalCharset, ScriptAssembler.leftJustify)
            .copyString("22").protobuf(ScriptData.getDataBufferAll(Buffer.CACHE1), typeString)
            .clearBuffer(Buffer.CACHE1)
            // execution
            .copyString("62").arrayPointer()
            // amount
            .copyString("0a0130")
            // contract
            .copyString("1229").copyArgument(argContract)
            // data
            .copyString("1a44")
            .copyString("a9059cbb000000000000000000000000").copyArgument(argRecipient)
            .copyString("0000000000000000000000000000000000000000").copyArgument(argAmount)
            .arrayEnd()
            // verification
            .showMessage("IOTX")
            // show name
            .ifSigned(argTokenInfo, argSign, "",
                    new ScriptAssembler().copyString(HexUtil.toHexString("@"), Buffer.CACHE2)
                            .getScript())
            .setBufferInt(argNameLength, 1, 7)
            .copyArgument(argName, Buffer.CACHE2)
            .showMessage(ScriptData.getDataBufferAll(Buffer.CACHE2))
            .clearBuffer(Buffer.CACHE2)
            // show address
            .copyString("030300090f", Buffer.CACHE1)
            .baseConvert(argRecipient, Buffer.CACHE1, 32, ScriptAssembler.binary32Charset, ScriptAssembler.bitLeftJustify8to5)
            .copyString("000000000000", Buffer.CACHE1)
            .bech32Polymod(ScriptData.getDataBufferAll(Buffer.CACHE1), Buffer.CACHE2)
            .clearBuffer(Buffer.CACHE1)
            .baseConvert(ScriptData.getDataBufferAll(Buffer.CACHE2), Buffer.CACHE1, 6, ScriptAssembler.base32BitcoinCashCharset, 0)
            .clearBuffer(Buffer.CACHE2)
            .copyString(HexUtil.toHexString("io1"), Buffer.CACHE2)
            .baseConvert(argRecipient, Buffer.CACHE2, 32, ScriptAssembler.base32BitcoinCashCharset, ScriptAssembler.bitLeftJustify8to5)
            .copyArgument(ScriptData.getDataBufferAll(Buffer.CACHE1), Buffer.CACHE2)
            .showAddress(ScriptData.getDataBufferAll(Buffer.CACHE2))
            .clearBuffer(Buffer.CACHE1)
            .clearBuffer(Buffer.CACHE2)
            // show amount
            .setBufferInt(argDecimal, 0, 24)
            .showAmount(argAmount, ScriptData.bufInt)
            .showPressButton()
            .setHeader(HashType.Keccak256, SignType.ECDSA)
            .getScript();

        return script;
    }

    public static String getExecuteScript() {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptData argNonce = sac.getArgumentRightJustified(8);
        ScriptData argGasLimit = sac.getArgumentRightJustified(8);
        ScriptData argGasPrice = sac.getArgumentRightJustified(12);
        ScriptData argAmount = sac.getArgumentRightJustified(12);
        ScriptData argContract = sac.getArgument(41);
        ScriptData argIsData = sac.getArgument(1);
        ScriptData argData = sac.getArgumentAll();

        String script = new ScriptAssembler()
            .setCoinType(0x0130)
            .copyString("0801")
            .ifEqual(argNonce, "0000000000000000", "",
                new ScriptAssembler().copyString("10").protobuf(argNonce, typeInt).getScript())
            .ifEqual(argGasLimit, "00000000000000000000", "",
                new ScriptAssembler().copyString("18").protobuf(argGasLimit, typeInt).getScript())
            .baseConvert(argGasPrice, Buffer.CACHE1, 0, ScriptAssembler.decimalCharset, ScriptAssembler.leftJustify)
            .copyString("22").protobuf(ScriptData.getDataBufferAll(Buffer.CACHE1), typeString)
            .clearBuffer(Buffer.CACHE1)
            // execution
            .copyString("62").arrayPointer()
            .baseConvert(argAmount, Buffer.CACHE1, 0, ScriptAssembler.decimalCharset, ScriptAssembler.leftJustify)
            .copyString("0a").protobuf(ScriptData.getDataBufferAll(Buffer.CACHE1), typeString)
            .clearBuffer(Buffer.CACHE1)
            .copyString("1229").copyArgument(argContract)
            .ifEqual(argIsData, "00", "",
                new ScriptAssembler().copyString("1a").protobuf(argData, typeString).getScript())
            .arrayEnd()
            // verification
            .showMessage("IOTX")
            .showWrap("SMART", "")
            .showAddress(argContract)
            .showAmount(argAmount, 18)
            .showPressButton()
            .setHeader(HashType.Keccak256, SignType.ECDSA)
            .getScript();

        return script;
    }

    //  version:          0001 (1)   000 (0) | 08 01 (1)
    //  nonce:            0010 (2)   000 (0) | 10 e606 (870)
    //  gasLimit:         0011 (3)   000 (0) | 18 904e (10000)
    //  gasPrice:         0100 (4)   010 (2) | 22 0d (13) 31303030303030303030303030
    //  stakeCreate:      01000 (40) 010 (2) | c202 28 (40)
    //    candidateName:    00001 (1)   010 (2) | 0a 0c (12) 726f626f7462703030303030
    //    stakeAmount:      00010 (2)   010 (2) | 12 14 (20) 3930303030303030303030303030303030303030
    //    stakeDuration:    00011 (3)   000 (0) | 18 01 (1)
    //    autoStake:        00100 (4)   000 (0) | 20 01 (1)

    public static String getStakeCreateScript() {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptData argNonce = sac.getArgumentRightJustified(8);
        ScriptData argGasLimit = sac.getArgumentRightJustified(8);
        ScriptData argGasPrice = sac.getArgumentRightJustified(12);
        ScriptData argCandidateName = sac.getArgumentRightJustified(20);
        ScriptData argAmount = sac.getArgumentRightJustified(12);
        ScriptData argDuration = sac.getArgumentRightJustified(8);
        ScriptData argIsAuto = sac.getArgument(1);
        ScriptData argIsPayload = sac.getArgument(1);
        ScriptData argPayload = sac.getArgumentAll();

        String script = new ScriptAssembler()
            .setCoinType(0x0130)
            .copyString("0801")
            .ifEqual(argNonce, "0000000000000000", "",
                new ScriptAssembler().copyString("10").protobuf(argNonce, typeInt).getScript())
            .ifEqual(argGasLimit, "00000000000000000000", "",
                new ScriptAssembler().copyString("18").protobuf(argGasLimit, typeInt).getScript())
            .baseConvert(argGasPrice, Buffer.CACHE1, 0, ScriptAssembler.decimalCharset, ScriptAssembler.leftJustify)
            .copyString("22").protobuf(ScriptData.getDataBufferAll(Buffer.CACHE1), typeString)
            .clearBuffer(Buffer.CACHE1)
            // stakeCreate
            .copyString("c202").arrayPointer()
            .copyString("0a").protobuf(argCandidateName, typeString)
            .baseConvert(argAmount, Buffer.CACHE1, 0, ScriptAssembler.decimalCharset, ScriptAssembler.leftJustify)
            .copyString("12").protobuf(ScriptData.getDataBufferAll(Buffer.CACHE1), typeString)
            .clearBuffer(Buffer.CACHE1)
            .ifEqual(argDuration, "0000000000000000", "",
                new ScriptAssembler().copyString("18").protobuf(argDuration, typeInt).getScript())
            .ifEqual(argIsAuto, "00", "",
                new ScriptAssembler().copyString("2001").getScript())
            .ifEqual(argIsPayload, "00", "",
                new ScriptAssembler().copyString("2a").protobuf(argPayload, typeString).getScript())
            .arrayEnd()
            // verification
            .showMessage("IOTX")
            .showWrap("CREATE", "STAKE")
            .showAmount(argAmount, 18)
            .showPressButton()
            .setHeader(HashType.Keccak256, SignType.ECDSA)
            .getScript();

        return script;
    }

    //  version:          0001 (1)   000 (0) | 08 01 (1)
    //  nonce:            0010 (2)   000 (0) | 10 e606 (870)
    //  gasLimit:         0011 (3)   000 (0) | 18 904e (10000)
    //  gasPrice:         0100 (4)   010 (2) | 22 0d (13) 31303030303030303030303030
    //  stakeUnstake:     01001 (41) 010 (2) | ca02 02 (2)
    //    bucketIndex:      00001 (1)   000 (0) | 08 38 (56)

    public static String getStakeUnstakeScript() {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptData argNonce = sac.getArgumentRightJustified(8);
        ScriptData argGasLimit = sac.getArgumentRightJustified(8);
        ScriptData argGasPrice = sac.getArgumentRightJustified(12);
        ScriptData argBucketIndex = sac.getArgumentRightJustified(8);
        ScriptData argIsPayload = sac.getArgument(1);
        ScriptData argPayload = sac.getArgumentAll();

        String script = new ScriptAssembler()
            .setCoinType(0x0130)
            .copyString("0801")
            .ifEqual(argNonce, "0000000000000000", "",
                new ScriptAssembler().copyString("10").protobuf(argNonce, typeInt).getScript())
            .ifEqual(argGasLimit, "00000000000000000000", "",
                new ScriptAssembler().copyString("18").protobuf(argGasLimit, typeInt).getScript())
            .baseConvert(argGasPrice, Buffer.CACHE1, 0, ScriptAssembler.decimalCharset, ScriptAssembler.leftJustify)
            .copyString("22").protobuf(ScriptData.getDataBufferAll(Buffer.CACHE1), typeString)
            .clearBuffer(Buffer.CACHE1)
            // stakeUnstake
            .copyString("ca02").arrayPointer()
            .ifEqual(argBucketIndex, "0000000000000000", "",
                new ScriptAssembler().copyString("08").protobuf(argBucketIndex, typeInt).getScript())
            .ifEqual(argIsPayload, "00", "",
                new ScriptAssembler().copyString("12").protobuf(argPayload, typeString).getScript())
            .arrayEnd()
            // verification
            .showMessage("IOTX")
            .showWrap("UNSTAKE", "")
            .showPressButton()
            .setHeader(HashType.Keccak256, SignType.ECDSA)
            .getScript();

        return script;
    }

    //  version:          0001 (1)   000 (0) | 08 01 (1)
    //  nonce:            0010 (2)   000 (0) | 10 e606 (870)
    //  gasLimit:         0011 (3)   000 (0) | 18 904e (10000)
    //  gasPrice:         0100 (4)   010 (2) | 22 0d (13) 31303030303030303030303030
    //  stakeWithdraw:    01010 (42) 010 (2) | d202 02 (2)
    //    bucketIndex:      00001 (1)   000 (0) | 08 38 (56)

    public static String getStakeWithdrawScript() {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptData argNonce = sac.getArgumentRightJustified(8);
        ScriptData argGasLimit = sac.getArgumentRightJustified(8);
        ScriptData argGasPrice = sac.getArgumentRightJustified(12);
        ScriptData argBucketIndex = sac.getArgumentRightJustified(8);
        ScriptData argIsPayload = sac.getArgument(1);
        ScriptData argPayload = sac.getArgumentAll();

        String script = new ScriptAssembler()
            .setCoinType(0x0130)
            .copyString("0801")
            .ifEqual(argNonce, "0000000000000000", "",
                new ScriptAssembler().copyString("10").protobuf(argNonce, typeInt).getScript())
            .ifEqual(argGasLimit, "00000000000000000000", "",
                new ScriptAssembler().copyString("18").protobuf(argGasLimit, typeInt).getScript())
            .baseConvert(argGasPrice, Buffer.CACHE1, 0, ScriptAssembler.decimalCharset, ScriptAssembler.leftJustify)
            .copyString("22").protobuf(ScriptData.getDataBufferAll(Buffer.CACHE1), typeString)
            .clearBuffer(Buffer.CACHE1)
            // stakeWithdraw
            .copyString("d202").arrayPointer()
            .ifEqual(argBucketIndex, "0000000000000000", "",
                new ScriptAssembler().copyString("08").protobuf(argBucketIndex, typeInt).getScript())
            .ifEqual(argIsPayload, "00", "",
                new ScriptAssembler().copyString("12").protobuf(argPayload, typeString).getScript())
            .arrayEnd()
            // verification
            .showMessage("IOTX")
            .showWrap("WITHDRAW", "STAKE")
            .showPressButton()
            .setHeader(HashType.Keccak256, SignType.ECDSA)
            .getScript();

        return script;
    }

    public static String getStakeAddDepositScript() {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptData argNonce = sac.getArgumentRightJustified(8);
        ScriptData argGasLimit = sac.getArgumentRightJustified(8);
        ScriptData argGasPrice = sac.getArgumentRightJustified(12);
        ScriptData argBucketIndex = sac.getArgumentRightJustified(8);
        ScriptData argAmount = sac.getArgumentRightJustified(12);
        ScriptData argIsPayload = sac.getArgument(1);
        ScriptData argPayload = sac.getArgumentAll();

        String script = new ScriptAssembler()
            .setCoinType(0x0130)
            .copyString("0801")
            .ifEqual(argNonce, "0000000000000000", "",
                new ScriptAssembler().copyString("10").protobuf(argNonce, typeInt).getScript())
            .ifEqual(argGasLimit, "00000000000000000000", "",
                new ScriptAssembler().copyString("18").protobuf(argGasLimit, typeInt).getScript())
            .baseConvert(argGasPrice, Buffer.CACHE1, 0, ScriptAssembler.decimalCharset, ScriptAssembler.leftJustify)
            .copyString("22").protobuf(ScriptData.getDataBufferAll(Buffer.CACHE1), typeString)
            .clearBuffer(Buffer.CACHE1)
            // stakeAddDeposit
            .copyString("da02").arrayPointer()
            .ifEqual(argBucketIndex, "0000000000000000", "",
                new ScriptAssembler().copyString("08").protobuf(argBucketIndex, typeInt).getScript())
            .baseConvert(argAmount, Buffer.CACHE1, 0, ScriptAssembler.decimalCharset, ScriptAssembler.leftJustify)
            .copyString("12").protobuf(ScriptData.getDataBufferAll(Buffer.CACHE1), typeString)
            .clearBuffer(Buffer.CACHE1)
            .ifEqual(argIsPayload, "00", "",
                new ScriptAssembler().copyString("1a").protobuf(argPayload, typeString).getScript())
            .arrayEnd()
            // verification
            .showMessage("IOTX")
            .showWrap("DEPOSIT", "STAKE")
            .showAmount(argAmount, 18)
            .showPressButton()
            .setHeader(HashType.Keccak256, SignType.ECDSA)
            .getScript();

        return script;
    }

    public static String IOTXTransferScriptSignature = "3044022005f5dbe68ec115cb5f448a7f327910a6258bd9021670d67c562224c1c9cdb98f0220380afcd52785a0a35aeffa448986cd76f4fdbe15d4be66ddc0b7307c6a6e18f7";
    public static String IOTXXRC20ScriptSignature = "3045022100eef62a118aceac7d0870aa00a0403696b42192c203b2be6259d00bf3934c4fa4022019351476afe69ac9a440a699b292b131696ed9cba5fc9fc3c6eed234cee51bd4";
    public static String IOTXExecuteScriptSignature = "3045022100a38bde99e35339ab83b6a4896943358a443ebe614403f104300489bcc6b047a40220755dd329852e4b0a4eed3f22e31ec0e97d6550b07f1bd670a37478fa2eef7788";
    public static String IOTXStakeCreateScriptSignature = "3045022100b5bd3aa72264399ec21fe338af2fe8ae6fca8dab38e3d0258cc82f57936a7a9f02203d9c9c86c2a7520b3c97f2e9cf047de605662bac3ee81b08c733126d7c83cb94";
    public static String IOTXStakeUnstakeScriptSignature = "3044022048fca64bea3d158a9649082341dfc06bedc312404616cb172ceefb40000ea2030220404a41a0f1c36039301e1f1b19157c6a21eb66b5be31928d6218cdbd1e2338d0";
    public static String IOTXStakeWithdrawScriptSignature = "30440220419fc74da37b83c079a177351c519b5860d350db5688717d84ff32429e4bfcce02201d7c58c37a8e1b4a6c73605fcc1ceb86201604b08e93ad03b4f4f14b6371c9aa";
    public static String IOTXStakeAddDepositScriptSignature = "3045022100d7f799ed4be8d6eb96388e8bff885d5876777af310d2d5fe906bbebd6b70f93e02205951aa757735a38a45467167af6bbf7feb929b31e6fd1b43729e23bd36db8885";
}
