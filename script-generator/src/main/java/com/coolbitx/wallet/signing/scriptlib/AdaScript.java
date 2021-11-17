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

public class AdaScript {

    public static void listAll() {
        System.out.println("ADA: \n" + getADATransactionScript() + "\n");
    }

    public static String getADATransactionScript() {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        // ScriptData txId = sac.getArgument(32);
        // ScriptData txIdIndex = sac.getArgument(1);
        ScriptData changeAddress = sac.getArgument(56);
        ScriptData changeAmountPrefix = sac.getArgument(1);
        ScriptData changeAmount = sac.getArgument(8);
        ScriptData receiverAddress = sac.getArgument(56);
        ScriptData receiveAmountPrefix = sac.getArgument(1);
        ScriptData receiveAmount = sac.getArgument(8);
        ScriptData feePrefix = sac.getArgument(1);
        ScriptData fee = sac.getArgument(8);
        ScriptData invalidHereafterPrefix = sac.getArgument(1);
        ScriptData invalidHereafter = sac.getArgument(8);
        ScriptData intputCount = sac.getArgument(1);
        ScriptData inputList = sac.getArgumentAll();

        // version=02 ScriptAssembler.hash=0E=ScriptAssembler.Blake2b256 sign=03=BIP32EDDSA
        return "03020E03"
                + ScriptAssembler.setCoinType(0x0717)
                // -- payload start --
                + ScriptAssembler.copyString("83a4")
                + ScriptAssembler.copyString("00")
                + ScriptAssembler.copyArgument(intputCount)
                // --- intput start (need for loop) ---
                // + ScriptAssembler.copyString("825820")
                // + ScriptAssembler.copyArgument(txId)
                // + ScriptAssembler.copyArgument(txIdIndex)
                + ScriptAssembler.copyArgument(inputList)
                // --- intput end ---
                // --- output change start ---
                + ScriptAssembler.ifEqual(changeAmount, "0000000000000000",
                        // ---- output count start ----
                        ScriptAssembler.copyString("0181"),
                        ScriptAssembler.copyString("0182")
                        // ---- output count end ----
                        + ScriptAssembler.copyString("82583901")
                        + ScriptAssembler.copyArgument(changeAddress)
                        + ScriptAssembler.copyArgument(changeAmountPrefix)
                        + ScriptAssembler.copyArgument(changeAmount, Buffer.CACHE2)
                        + ScriptAssembler.ifEqual(changeAmountPrefix, "18", ScriptAssembler.copyArgument(ScriptData.getDataBufferAll(Buffer.CACHE2, 7)), "")
                        + ScriptAssembler.ifEqual(changeAmountPrefix, "19", ScriptAssembler.copyArgument(ScriptData.getDataBufferAll(Buffer.CACHE2, 6)), "")
                        + ScriptAssembler.ifEqual(changeAmountPrefix, "1a", ScriptAssembler.copyArgument(ScriptData.getDataBufferAll(Buffer.CACHE2, 4)), "")
                        + ScriptAssembler.ifEqual(changeAmountPrefix, "1b", ScriptAssembler.copyArgument(ScriptData.getDataBufferAll(Buffer.CACHE2)), "")
                        + ScriptAssembler.clearBuffer(Buffer.CACHE2)
                )
                // --- output change end ---
                // --- output receive start ---
                + ScriptAssembler.copyString("82583900")
                + ScriptAssembler.copyArgument(receiverAddress)
                + ScriptAssembler.copyArgument(receiveAmountPrefix)
                + ScriptAssembler.copyArgument(receiveAmount, Buffer.CACHE2)
                + ScriptAssembler.ifEqual(receiveAmountPrefix, "18", ScriptAssembler.copyArgument(ScriptData.getDataBufferAll(Buffer.CACHE2, 7)), "")
                + ScriptAssembler.ifEqual(receiveAmountPrefix, "19", ScriptAssembler.copyArgument(ScriptData.getDataBufferAll(Buffer.CACHE2, 6)), "")
                + ScriptAssembler.ifEqual(receiveAmountPrefix, "1a", ScriptAssembler.copyArgument(ScriptData.getDataBufferAll(Buffer.CACHE2, 4)), "")
                + ScriptAssembler.ifEqual(receiveAmountPrefix, "1b", ScriptAssembler.copyArgument(ScriptData.getDataBufferAll(Buffer.CACHE2)), "")
                + ScriptAssembler.clearBuffer(Buffer.CACHE2)
                // --- output receive end ---
                // --- fee start ---
                + ScriptAssembler.copyString("02")
                + ScriptAssembler.copyArgument(feePrefix)
                + ScriptAssembler.copyArgument(fee, Buffer.CACHE2)
                + ScriptAssembler.ifEqual(feePrefix, "18", ScriptAssembler.copyArgument(ScriptData.getDataBufferAll(Buffer.CACHE2, 7)), "")
                + ScriptAssembler.ifEqual(feePrefix, "19", ScriptAssembler.copyArgument(ScriptData.getDataBufferAll(Buffer.CACHE2, 6)), "")
                + ScriptAssembler.ifEqual(feePrefix, "1a", ScriptAssembler.copyArgument(ScriptData.getDataBufferAll(Buffer.CACHE2, 4)), "")
                + ScriptAssembler.ifEqual(feePrefix, "1b", ScriptAssembler.copyArgument(ScriptData.getDataBufferAll(Buffer.CACHE2)), "")
                + ScriptAssembler.clearBuffer(Buffer.CACHE2)
                // --- fee end ---
                // --- invalid hereafter start ---
                + ScriptAssembler.copyString("03")
                + ScriptAssembler.copyArgument(invalidHereafterPrefix)
                + ScriptAssembler.copyArgument(invalidHereafter, Buffer.CACHE2)
                + ScriptAssembler.ifEqual(invalidHereafterPrefix, "18", ScriptAssembler.copyArgument(ScriptData.getDataBufferAll(Buffer.CACHE2, 7)), "")
                + ScriptAssembler.ifEqual(invalidHereafterPrefix, "19", ScriptAssembler.copyArgument(ScriptData.getDataBufferAll(Buffer.CACHE2, 6)), "")
                + ScriptAssembler.ifEqual(invalidHereafterPrefix, "1a", ScriptAssembler.copyArgument(ScriptData.getDataBufferAll(Buffer.CACHE2, 4)), "")
                + ScriptAssembler.ifEqual(invalidHereafterPrefix, "1b", ScriptAssembler.copyArgument(ScriptData.getDataBufferAll(Buffer.CACHE2)), "")
                + ScriptAssembler.clearBuffer(Buffer.CACHE2)
                // --- invalid hereafter end ---
                + ScriptAssembler.copyString("9ffff6")
                // -- payload end --
                + ScriptAssembler.showMessage("ADA")
                // -- show address start --
                + ScriptAssembler.copyString("030303030001040412", Buffer.CACHE2)
                + ScriptAssembler.copyString("01", Buffer.CACHE1)
                + ScriptAssembler.copyArgument(receiverAddress, Buffer.CACHE1)
                + ScriptAssembler.baseConvert(ScriptData.getDataBufferAll(Buffer.CACHE1), Buffer.CACHE2, 92, ScriptAssembler.binary32Charset, ScriptAssembler.bitLeftJustify8to5)
                + ScriptAssembler.copyString("000000000000", Buffer.CACHE2)
                + ScriptAssembler.clearBuffer(Buffer.CACHE1)
                + ScriptAssembler.bech32Polymod(ScriptData.getDataBufferAll(Buffer.CACHE2), Buffer.CACHE1)
                + ScriptAssembler.clearBuffer(Buffer.CACHE2)
                + ScriptAssembler.copyString(HexUtil.toHexString("addr1"), Buffer.CACHE2)
                + ScriptAssembler.copyString("01", Buffer.CACHE1)
                + ScriptAssembler.copyArgument(receiverAddress, Buffer.CACHE1) // EXTENDED : [Polymod(4B)][01(1B)][receiverAddress(56B)]
                + ScriptAssembler.baseConvert(ScriptData.getBuffer(Buffer.CACHE1, 4, 57), Buffer.CACHE2, 92, ScriptAssembler.base32BitcoinCashCharset, ScriptAssembler.bitLeftJustify8to5)
                + ScriptAssembler.baseConvert(ScriptData.getBuffer(Buffer.CACHE1, 0, 4), Buffer.CACHE2, 6, ScriptAssembler.base32BitcoinCashCharset, 0)
                + ScriptAssembler.showAddress(ScriptData.getDataBufferAll(Buffer.CACHE2))
                + ScriptAssembler.clearBuffer(Buffer.CACHE2)
                + ScriptAssembler.clearBuffer(Buffer.CACHE1)
                // -- show address end --
                // -- show amount start --
                + ScriptAssembler.showAmount(receiveAmount, 6)
                // -- show amount end --
                + ScriptAssembler.showPressButton();

        // 01a74ecc
    }

}
