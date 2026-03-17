/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.coolbitx.wallet.signing.scriptlib;

import com.coolbitx.wallet.signing.utils.HexUtil;
import com.coolbitx.wallet.signing.utils.ScriptArgumentComposer;
import com.coolbitx.wallet.signing.utils.ScriptAssembler;
import com.coolbitx.wallet.signing.utils.ScriptAssembler.HashType;
import com.coolbitx.wallet.signing.utils.ScriptAssembler.SignType;
import com.coolbitx.wallet.signing.utils.ScriptData;
import com.coolbitx.wallet.signing.utils.ScriptData.Buffer;
import com.coolbitx.wallet.signing.utils.ScriptRlpArray;
import com.coolbitx.wallet.signing.utils.ScriptRlpData;
import com.google.common.base.Strings;

public class XrpScript {

    public static void main(String[] args) {
        listAll();
    }

    public static void listAll() {
        System.out.println("Xrp: \n" + getXRPScript() + "\n");
        System.out.println("Xrp new script: \n" + getXRPNewScript() + "\n");
        System.out.println("Xrp RLP: \n" + getXRPRlpArgumentScript() + "\n");
    }

    public static String getXRPScript() {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptData argAccount = sac.getArgument(20);
        ScriptData argPublicKey = sac.getArgument(33);
        ScriptData argDest = sac.getArgument(20);
        ScriptData argPadding1 = sac.getArgument(1);
        ScriptData argAmount = sac.getArgument(7);
        ScriptData argPadding2 = sac.getArgument(1);
        ScriptData argFee = sac.getArgument(7);
        ScriptData argSequence = sac.getArgument(4);
        ScriptData argLastLedgerSequence = sac.getArgument(4);
        ScriptData argTag = sac.getArgument(4);
        ScriptData argFlags = sac.getArgument(4);

        String script = new ScriptAssembler().setCoinType(0x90)
            .copyString("5354580012000022")
            .copyArgument(argFlags)
            .copyString("24")
            .copyArgument(argSequence)
            .copyString("2E")
            .copyArgument(argTag)
            .copyString("201B")
            .copyArgument(argLastLedgerSequence)
            .copyString("6140")
            .copyArgument(argAmount)
            .copyString("6840")
            .copyArgument(argFee)
            .copyString("7321")
            .copyArgument(argPublicKey)
            .copyString("8114")
            .copyArgument(argAccount)
            .copyString("8314")
            .copyArgument(argDest)
            .showMessage("XRP")
            .copyString("00", Buffer.CACHE2)
            .copyArgument(argDest, Buffer.CACHE2)
            .hash(ScriptData.getDataBufferAll(Buffer.CACHE2), Buffer.CACHE2, HashType.DoubleSHA256)
            .copyString(HexUtil.toHexString("rpshnaf39wBUDNEGHJKLM4PQRST7VWXYZ2bcdeCg65jkm8oFqi1tuvAxyz"),
                Buffer.CACHE1)
            .baseConvert(ScriptData.getBuffer(Buffer.CACHE2, 0, 25), Buffer.CACHE2, 45, ScriptAssembler.cache1Charset,
                ScriptAssembler.zeroInherit)
            .showAddress(ScriptData.getDataBufferAll(Buffer.CACHE2, 53))
            .showAmount(argAmount, 6)
            .showPressButton()
            .setHeader(HashType.SHA512, SignType.ECDSA)
            .getScript();
        return script;
    }

    public static String XRPScriptSignature = Strings.padStart(
        "304402206B2A707864EB98033BF83A80E8FDD7FCF903CC059ABC0E4FBB317040B6E9AD1D02203DCD2BDC4480B88DB0D9DC74948BAF6BD62203E90AE39990978999ABEAEABA63",
        144, '0');

    public static String getXRPNewScript() {
        ScriptRlpArray array = new ScriptRlpArray();
        ScriptRlpData argFlags = array.getRlpItemArgument();
        ScriptRlpData argSequence = array.getRlpItemArgument();
        ScriptRlpData argDestinationTag = array.getRlpItemArgument();
        ScriptRlpData argLastLedgerSequence = array.getRlpItemArgument();
        ScriptRlpData argAmount = array.getRlpItemArgument();
        ScriptRlpData argFee = array.getRlpItemArgument();
        ScriptRlpData argPublicKey = array.getRlpItemArgument();
        ScriptRlpData argAccount = array.getRlpItemArgument();
        ScriptRlpData argDest = array.getRlpItemArgument();
        ScriptRlpArray argMemos = array.getRlpArrayArgument();
        ScriptRlpData argMemoData = argMemos.getRlpItemArgument();
        ScriptRlpData argMemoType = argMemos.getRlpItemArgument();
        ScriptRlpData argMemoFormat = argMemos.getRlpItemArgument();

        String script = new ScriptAssembler().setCoinType(0x90)
            .copyString("53545800")
            .copyString("12") // TransactionType
            .copyString("0000")
            .isEmpty(argFlags, "", new ScriptAssembler().copyString("22").copyArgument(argFlags).getScript()) // Flags
            .copyString("24") // Sequence
            .copyArgument(argSequence)
            .isEmpty(argDestinationTag, "",
                new ScriptAssembler().copyString("2E").copyArgument(argDestinationTag).getScript()) // DestinationTag
            .copyString("201B") // LastLedgerSequence, although optional, is strongly recommended
            .copyArgument(argLastLedgerSequence)
            .copyString("6140") // Amount
            .copyArgument(argAmount)
            .copyString("6840") // Fee
            .copyArgument(argFee)
            .copyString("7321") // SigningPubKey
            .copyArgument(argPublicKey)
            .copyString("8114") // Account
            .copyArgument(argAccount)
            .copyString("8314") // Destination
            .copyArgument(argDest)
            .isEmpty(argMemos, "", new ScriptAssembler().copyString("F9")
                .copyString("EA")
                .isEmpty(argMemoData, "", new ScriptAssembler().copyString("7C").copyArgument(argMemoData).getScript())
                .isEmpty(argMemoType, "", new ScriptAssembler().copyString("7D").copyArgument(argMemoType).getScript())
                .isEmpty(argMemoFormat, "",
                    new ScriptAssembler().copyString("7E").copyArgument(argMemoFormat).getScript())
                .copyString("E1")
                .copyString("F1")
                .getScript())
            .showMessage("XRP")
            .copyString("00", Buffer.CACHE2)
            .copyArgument(argDest, Buffer.CACHE2)
            .hash(ScriptData.getDataBufferAll(Buffer.CACHE2), Buffer.CACHE2, HashType.DoubleSHA256)
            .copyString(HexUtil.toHexString("rpshnaf39wBUDNEGHJKLM4PQRST7VWXYZ2bcdeCg65jkm8oFqi1tuvAxyz"),
                Buffer.CACHE1)
            .baseConvert(ScriptData.getBuffer(Buffer.CACHE2, 0, 25), Buffer.CACHE2, 45, ScriptAssembler.cache1Charset,
                ScriptAssembler.zeroInherit)
            .showAddress(ScriptData.getDataBufferAll(Buffer.CACHE2, 53))
            .showAmount(argAmount, 6)
            .showPressButton()
            .setHeader(HashType.SHA512, SignType.ECDSA)
            .getScript();
        return script;
    }

    public static String XRPNewScriptSignature = Strings.padStart(
        "304402207c10ec80d90b59e09f586c1ac76b0b7aba8d9bfc2c554ce3d9430b5590a5a1530220719885ad5cc9fde206739b20744b800d0b215af302f8a820f5b4ac307f9d08c8",
        144, '0');

    public static String getXRPRlpArgumentScript() {
        ScriptRlpArray array = new ScriptRlpArray();
        ScriptRlpData argAccount = array.getRlpItemArgument();
        ScriptRlpData argPublicKey = array.getRlpItemArgument();
        ScriptRlpData argDest = array.getRlpItemArgument();
        ScriptRlpData argAmount = array.getRlpItemArgument();
        ScriptRlpData argFee = array.getRlpItemArgument();
        ScriptRlpData argSequence = array.getRlpItemArgument();
        ScriptRlpData argLastLedgerSequence = array.getRlpItemArgument();
        ScriptRlpData argTag = array.getRlpItemArgument();
        ScriptRlpData argFlags = array.getRlpItemArgument();

        String script = new ScriptAssembler().setCoinType(0x90)
            .copyString("5354580012000022")
            .copyArgument(argFlags)
            .copyString("24")
            .copyArgument(argSequence)
            .copyString("2E")
            .copyArgument(argTag)
            .copyString("201B")
            .copyArgument(argLastLedgerSequence)
            .copyString("6140")
            .copyArgument(argAmount)
            .copyString("6840")
            .copyArgument(argFee)
            .copyString("7321")
            .copyArgument(argPublicKey)
            .copyString("8114")
            .copyArgument(argAccount)
            .copyString("8314")
            .copyArgument(argDest)
            .showMessage("XRP")
            .copyString("00", Buffer.CACHE2)
            .copyArgument(argDest, Buffer.CACHE2)
            .hash(ScriptData.getDataBufferAll(Buffer.CACHE2), Buffer.CACHE2, HashType.DoubleSHA256)
            .copyString(HexUtil.toHexString("rpshnaf39wBUDNEGHJKLM4PQRST7VWXYZ2bcdeCg65jkm8oFqi1tuvAxyz"),
                Buffer.CACHE1)
            .baseConvert(ScriptData.getBuffer(Buffer.CACHE2, 0, 25), Buffer.CACHE2, 45, ScriptAssembler.cache1Charset,
                ScriptAssembler.zeroInherit)
            .showAddress(ScriptData.getDataBufferAll(Buffer.CACHE2, 53))
            .showAmount(argAmount, 6)
            .showPressButton()
            .setHeader(HashType.SHA512, SignType.ECDSA)
            .getScript();
        return script;
    }

    public static String XRPRlpArgumentScriptSignature = Strings.padEnd("FA", 144, '0');

}
