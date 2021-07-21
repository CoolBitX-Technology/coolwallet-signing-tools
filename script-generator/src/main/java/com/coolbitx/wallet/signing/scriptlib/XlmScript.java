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

            final String[] header = { "7ac33997544e3175d266bd022439b22cdb16508c01163f26e5cb2a3e1045a979", // XLM
                            "cee0302d59844d32bdca915c8203dd44b33fbb7edc19051ea37abedf28ecd472", // XLM test
                            "3282241a3ca14ba8807e0dd869ae1f3b29a47a383badfbd6fe493f514c617314", // KAU
                            "599c6897b37bd161229877d86992678a7524aede87a0b2c29baa9331bb283bea", // KAU test
                            "e59a5c2b639c122cd32faaa88957660dedacf292485e45c45f6d57011790c131", // KAG
                            "1061c82002783387ff47fb45656a0802e3610d0ec7e78037e3924165ce68ca4f" };// KAG test

            final String[] symbol = { "XLM", "KAU", "KAG" };

            return "03000202" + ScriptAssembler.setCoinType(0x94)
                            + ScriptAssembler.copyString(header[type * 2 + (isTestnet ? 1 : 0)] + "0000000200000000") + // header[32]
                                                                                                                        // +
                                                                                                                        // envelopType[4]
                                                                                                                        // +
                                                                                                                        // sourceAccountIdType[4]
                            ScriptAssembler.copyArgument(argSourceAccountId)
                            + ScriptAssembler.copyArgument(type == XLM ? argFee4 : argFee8)
                            + ScriptAssembler.copyArgument(argSequence) // TODO
                            + ScriptAssembler.copyString("00000001") + ScriptAssembler.copyArgument(argTimeBounds)
                            + ScriptAssembler.copyString("000000") + ScriptAssembler.copyArgument(argMemoType)
                            + ScriptAssembler.ifRange(argMemoType, "00", "04", "", ScriptAssembler.throwSEError)
                            + ScriptAssembler.ifEqual(argMemoType, "01",
                                            ScriptAssembler.setBufferIntFromDataLength(argMemoRJ)
                                                            + ScriptAssembler.copyString("0000")
                                                            + ScriptAssembler.putBufferInt(Buffer.TRANSACTION)
                                                            + ScriptAssembler.copyArgument(argMemoRJ)
                                                            + ScriptAssembler.paddingZero(Buffer.TRANSACTION, 4),
                                            "")
                            + ScriptAssembler.ifEqual(argMemoType, "02", ScriptAssembler.copyArgument(argMemo8), "")
                            + ScriptAssembler.ifRange(argMemoType, "03", "04", ScriptAssembler.copyArgument(argMemo32),
                                            "")
                            + ScriptAssembler.copyString("0000000100000000000000")
                            + ScriptAssembler.switchString(argIsCreate, Buffer.TRANSACTION, "01,00")
                            + ScriptAssembler.copyString("00000000") + ScriptAssembler.copyArgument(argDestAccountId)
                            + ScriptAssembler.ifEqual(argIsCreate, "00", ScriptAssembler.copyString("00000000"), "")
                            + ScriptAssembler.copyArgument(argAmount) + ScriptAssembler.copyString("00000000")
                            + (!isTestnet ? ScriptAssembler.showMessage(symbol[type])
                                            : ScriptAssembler.showWrap(symbol[type], "TESTNET"))
                            + ScriptAssembler.copyString("30", Buffer.FREE)
                            + ScriptAssembler.copyArgument(argDestAccountId, Buffer.FREE)
                            + ScriptAssembler.hash(ScriptData.getDataBufferAll(Buffer.FREE), Buffer.EXTENDED,
                                            ScriptAssembler.CRC16)
                            + ScriptAssembler.baseConvert(ScriptData.getDataBufferAll(Buffer.EXTENDED),
                                            Buffer.FREE, 2, ScriptAssembler.binaryCharset,
                                            ScriptAssembler.littleEndian)
                            + ScriptAssembler.clearBuffer(Buffer.EXTENDED)
                            + ScriptAssembler.copyString(HexUtil.toHexString("ABCDEFGHIJKLMNOPQRSTUVWXYZ234567"),
                                            Buffer.EXTENDED)
                            + ScriptAssembler.baseConvert(ScriptData.getBufer(Buffer.FREE, 0, 35),
                                            Buffer.FREE, 56, ScriptAssembler.extendedCharset,
                                            ScriptAssembler.bitLeftJustify8to5)
                            + ScriptAssembler.showAddress(ScriptData.getDataBufferAll(Buffer.FREE, 35))
                            + ScriptAssembler.showAmount(argAmount, 7) + ScriptAssembler.showPressButton();
    }

}
