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

public class XlmScript {

    public static void listAll() {
        System.out.println("Xlm: \n" + getStellarScript(XLM, false) + "\n");
        System.out.println("Kau: \n" + getStellarScript(KAU, false) + "\n");
        System.out.println("Kug: \n" + getStellarScript(KAG, false) + "\n");
    }

    public static final int XLM = 0;
    public static final int KAU = 1;
    public static final int KAG = 2;

    public static String getStellarScript(int type, boolean isTestnet) {
        if (type < 0 || type > 2) {
            return "typeError";
        }
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptData argSourceAccountId = sac.getArgument(32);
        ScriptData argDestAccountId = sac.getArgument(32);
        ScriptData argAmount = sac.getArgument(8);
        ScriptData argFee4 = sac.getArgumentUnion(4, 4);
        ScriptData argFee8 = sac.getArgument(8);
        ScriptData argSequence = sac.getArgument(8);
        ScriptData argTimeBounds = sac.getArgument(16);
        ScriptData argMemoType = sac.getArgument(1);
        ScriptData argMemo8 = sac.getArgumentUnion(24, 8);
        ScriptData argMemo32 = sac.getArgumentUnion(0, 32);
        ScriptData argMemoSpace = sac.getArgument(4);
        ScriptData argMemoRJ = sac.getArgumentRightJustified(28);
        ScriptData argIsCreate = sac.getArgument(1);

        final String[] header = {"7ac33997544e3175d266bd022439b22cdb16508c01163f26e5cb2a3e1045a979", // XLM
            "cee0302d59844d32bdca915c8203dd44b33fbb7edc19051ea37abedf28ecd472", // XLM test
            "3282241a3ca14ba8807e0dd869ae1f3b29a47a383badfbd6fe493f514c617314", // KAU
            "599c6897b37bd161229877d86992678a7524aede87a0b2c29baa9331bb283bea", // KAU test
            "e59a5c2b639c122cd32faaa88957660dedacf292485e45c45f6d57011790c131", // KAG
            "1061c82002783387ff47fb45656a0802e3610d0ec7e78037e3924165ce68ca4f"};// KAG test

        final String[] symbol = {"XLM", "KAU", "KAG"};

        ScriptAssembler scriptAsb = new ScriptAssembler();
        String script = scriptAsb
                .setCoinType(0x94)
                .copyString(header[type * 2 + (isTestnet ? 1 : 0)] + "0000000200000000")
                .copyArgument(argSourceAccountId)
                .copyArgument(type == XLM ? argFee4 : argFee8)
                .copyArgument(argSequence) // TODO
                .copyString("00000001")
                .copyArgument(argTimeBounds)
                .copyString("000000")
                .copyArgument(argMemoType)
                .ifRange(argMemoType, "00", "04", "", ScriptAssembler.throwSEError)
                .ifEqual(argMemoType, "01",
                        new ScriptAssembler().setBufferIntFromDataLength(argMemoRJ)
                                .copyString("0000")
                                .putBufferInt(Buffer.TRANSACTION)
                                .copyArgument(argMemoRJ)
                                .paddingZero(Buffer.TRANSACTION, 4)
                                .getScript(),
                         "")
                .ifEqual(argMemoType, "02", new ScriptAssembler().copyArgument(argMemo8).getScript(), "")
                .ifRange(argMemoType, "03", "04", new ScriptAssembler().copyArgument(argMemo32).getScript(), "")
                .copyString("0000000100000000000000")
                .switchString(argIsCreate, Buffer.TRANSACTION, "01,00")
                .copyString("00000000")
                .copyArgument(argDestAccountId)
                .ifEqual(argIsCreate, "00", new ScriptAssembler().copyString("00000000").getScript(), "")
                .copyArgument(argAmount)
                .copyString("00000000")
                .getScript();
        script = (!isTestnet ? scriptAsb.showMessage(symbol[type])
                : scriptAsb.showWrap(symbol[type], "TESTNET"))
                .copyString("30", Buffer.CACHE2)
                .copyArgument(argDestAccountId, Buffer.CACHE2)
                .hash(ScriptData.getDataBufferAll(Buffer.CACHE2), Buffer.CACHE1,
                        HashType.CRC16)
                .baseConvert(ScriptData.getDataBufferAll(Buffer.CACHE1),
                        Buffer.CACHE2, 2, ScriptAssembler.binaryCharset,
                        ScriptAssembler.littleEndian)
                .clearBuffer(Buffer.CACHE1)
                .copyString(HexUtil.toHexString("ABCDEFGHIJKLMNOPQRSTUVWXYZ234567"),
                        Buffer.CACHE1)
                .baseConvert(ScriptData.getBuffer(Buffer.CACHE2, 0, 35),
                        Buffer.CACHE2, 56, ScriptAssembler.cache1Charset,
                        ScriptAssembler.bitLeftJustify8to5)
                .showAddress(ScriptData.getDataBufferAll(Buffer.CACHE2, 35))
                .showAmount(argAmount, 7)
                .showPressButton()
                .setHeader(HashType.SHA256, SignType.EDDSA)
                .getScript();
        return script;
    }

    public static String StellarScriptSignature = "0030450221008832DD699A98B4EAFA26994C18EBFEFD234914F25492B03BFE36D3DDEFF7C3B30220364A691A115CAD6D283D618813F485BDBF4F5FCCAAD76FCA69D8F165E5DA0173";

}
