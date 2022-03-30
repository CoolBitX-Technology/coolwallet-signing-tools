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
        System.out.println("IOTX Execute: \n" + getExecuteScript() + "\n");
        System.out.println("IOTX StakeCreate: \n" + getStakeCreateScript() + "\n");
        System.out.println("IOTX StakeUnstake: \n" + getStakeUnstakeScript() + "\n");
        System.out.println("IOTX StakeWithdraw: \n" + getStakeWithdrawScript() + "\n");
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
            .arrayEnd()
            // verification
            .showMessage("IOTX")
            .showWrap("WITHDRAW", "STAKE")
            .showPressButton()
            .setHeader(HashType.Keccak256, SignType.ECDSA)
            .getScript();

        return script;
    }

    public static String IOTXTransferScriptSignature = "304502201092334B1BB00A0ED408EEBBF3C9E52F501C0A09981D13E01D42D1F4B7C845F1022100A2B38EE1795D925B7F24C39D47C1848FAB5C7D93799DA9C5B532B1ED01D01DB6";
    public static String IOTXExecuteScriptSignature = "304402200E2B57A5A24AF72BCD7995F12B3C1DC3BDE18F3531BC588597502B4F3E6DB0350220615F6EF87BB602707CD536C5AE13EA52D81A6B366F9E7C187E30D639FC50BDAD";
    public static String IOTXStakeCreateScriptSignature = "3046022100D5C495BC689304FBE9D1344DF6EB07F288860DE826C0CEED10F0709A9EF442D5022100B791DFDC3871C80886DCF57F22EED5276B765FB3F6703D1BB450B5D35B7C20CE";
    public static String IOTXStakeUnstakeScriptSignature = "3045022100AB95150EC6CA2C22435008258112DF8969492BE70A2B408509618C92FECCD62E022020BA27FC9C321E25645944FE90E04C06F5885BCE18E88DBFA022EC72B5E777C1";
    public static String IOTXStakeWithdrawScriptSignature = "3045022100EC308FCA9322494C34E9C946AEE69838E5F713EB296C7CC43750B7E559C054AF02206F795316291AE5DA364B7D97EF5EDC8FEF5AB27FE47C502A3EE0C89E5B6F913F";
}
