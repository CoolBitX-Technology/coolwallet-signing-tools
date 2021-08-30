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

        // version=02 ScriptAssembler.hash=0E=ScriptAssembler.Blake2b256 sign=02=EDDSA
        return "03020E02"
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
                + ScriptAssembler.copyString("0182")
                + ScriptAssembler.copyString("82583901")
                + ScriptAssembler.copyArgument(changeAddress)
                + ScriptAssembler.copyArgument(changeAmountPrefix)
                + ScriptAssembler.copyArgument(changeAmount, Buffer.FREE)
                + ScriptAssembler.ifEqual(changeAmountPrefix, "18", ScriptAssembler.copyArgument(ScriptData.getDataBufferAll(Buffer.FREE, 7)), "")
                + ScriptAssembler.ifEqual(changeAmountPrefix, "19", ScriptAssembler.copyArgument(ScriptData.getDataBufferAll(Buffer.FREE, 6)), "")
                + ScriptAssembler.ifEqual(changeAmountPrefix, "1a", ScriptAssembler.copyArgument(ScriptData.getDataBufferAll(Buffer.FREE, 4)), "")
                + ScriptAssembler.ifEqual(changeAmountPrefix, "1b", ScriptAssembler.copyArgument(ScriptData.getDataBufferAll(Buffer.FREE)), "")
                + ScriptAssembler.clearBuffer(Buffer.FREE)
                // --- output change end ---
                // --- output receive start ---
                + ScriptAssembler.copyString("82583900")
                + ScriptAssembler.copyArgument(receiverAddress)
                + ScriptAssembler.copyArgument(receiveAmountPrefix)
                + ScriptAssembler.copyArgument(receiveAmount, Buffer.FREE)
                + ScriptAssembler.ifEqual(receiveAmountPrefix, "18", ScriptAssembler.copyArgument(ScriptData.getDataBufferAll(Buffer.FREE, 7)), "")
                + ScriptAssembler.ifEqual(receiveAmountPrefix, "19", ScriptAssembler.copyArgument(ScriptData.getDataBufferAll(Buffer.FREE, 6)), "")
                + ScriptAssembler.ifEqual(receiveAmountPrefix, "1a", ScriptAssembler.copyArgument(ScriptData.getDataBufferAll(Buffer.FREE, 4)), "")
                + ScriptAssembler.ifEqual(receiveAmountPrefix, "1b", ScriptAssembler.copyArgument(ScriptData.getDataBufferAll(Buffer.FREE)), "")
                + ScriptAssembler.clearBuffer(Buffer.FREE)
                // --- output receive end ---
                // --- fee start ---
                + ScriptAssembler.copyString("02")
                + ScriptAssembler.copyArgument(feePrefix)
                + ScriptAssembler.copyArgument(fee, Buffer.FREE)
                + ScriptAssembler.ifEqual(feePrefix, "18", ScriptAssembler.copyArgument(ScriptData.getDataBufferAll(Buffer.FREE, 7)), "")
                + ScriptAssembler.ifEqual(feePrefix, "19", ScriptAssembler.copyArgument(ScriptData.getDataBufferAll(Buffer.FREE, 6)), "")
                + ScriptAssembler.ifEqual(feePrefix, "1a", ScriptAssembler.copyArgument(ScriptData.getDataBufferAll(Buffer.FREE, 4)), "")
                + ScriptAssembler.ifEqual(feePrefix, "1b", ScriptAssembler.copyArgument(ScriptData.getDataBufferAll(Buffer.FREE)), "")
                + ScriptAssembler.clearBuffer(Buffer.FREE)
                // --- fee end ---
                // --- invalid hereafter start ---
                + ScriptAssembler.copyString("03")
                + ScriptAssembler.copyArgument(invalidHereafterPrefix)
                + ScriptAssembler.copyArgument(invalidHereafter, Buffer.FREE)
                + ScriptAssembler.ifEqual(invalidHereafterPrefix, "18", ScriptAssembler.copyArgument(ScriptData.getDataBufferAll(Buffer.FREE, 7)), "")
                + ScriptAssembler.ifEqual(invalidHereafterPrefix, "19", ScriptAssembler.copyArgument(ScriptData.getDataBufferAll(Buffer.FREE, 6)), "")
                + ScriptAssembler.ifEqual(invalidHereafterPrefix, "1a", ScriptAssembler.copyArgument(ScriptData.getDataBufferAll(Buffer.FREE, 4)), "")
                + ScriptAssembler.ifEqual(invalidHereafterPrefix, "1b", ScriptAssembler.copyArgument(ScriptData.getDataBufferAll(Buffer.FREE)), "")
                + ScriptAssembler.clearBuffer(Buffer.FREE)
                // --- invalid hereafter end ---
                + ScriptAssembler.copyString("9ffff6")
                // -- payload end --
                + ScriptAssembler.showMessage("ADA")
                // -- show address start --
                + ScriptAssembler.copyString("030303030001040412", Buffer.FREE)
                + ScriptAssembler.copyString("01", Buffer.EXTENDED)
                + ScriptAssembler.copyArgument(receiverAddress, Buffer.EXTENDED)
                + ScriptAssembler.baseConvert(ScriptData.getDataBufferAll(Buffer.EXTENDED), Buffer.FREE, 92, ScriptAssembler.binary32Charset, ScriptAssembler.bitLeftJustify8to5)
                + ScriptAssembler.copyString("000000000000", Buffer.FREE)
                + ScriptAssembler.clearBuffer(Buffer.EXTENDED)
                + ScriptAssembler.bech32Polymod(ScriptData.getDataBufferAll(Buffer.FREE), Buffer.EXTENDED)
                + ScriptAssembler.clearBuffer(Buffer.FREE)
                + ScriptAssembler.copyString(HexUtil.toHexString("addr1"), Buffer.FREE)
                + ScriptAssembler.copyString("01", Buffer.EXTENDED)
                + ScriptAssembler.copyArgument(receiverAddress, Buffer.EXTENDED) // EXTENDED : [Polymod(4B)][01(1B)][receiverAddress(56B)]
                + ScriptAssembler.baseConvert(ScriptData.getBuffer(Buffer.EXTENDED, 4, 57), Buffer.FREE, 92, ScriptAssembler.base32BitcoinCashCharset, ScriptAssembler.bitLeftJustify8to5)
                + ScriptAssembler.baseConvert(ScriptData.getBuffer(Buffer.EXTENDED, 0, 4), Buffer.FREE, 6, ScriptAssembler.base32BitcoinCashCharset, 0)
                + ScriptAssembler.showAddress(ScriptData.getDataBufferAll(Buffer.FREE))
                + ScriptAssembler.clearBuffer(Buffer.FREE)
                + ScriptAssembler.clearBuffer(Buffer.EXTENDED)
                // -- show address end --
                // -- show amount start --
                + ScriptAssembler.showAmount(receiveAmount, 6)
                // -- show amount end --
                + ScriptAssembler.showPressButton();

        // 01a74ecc
    }

}
