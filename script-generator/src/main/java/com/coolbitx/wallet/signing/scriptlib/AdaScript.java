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

        String script = new ScriptAssembler()
                .setCoinType(0x0717)
                // -- payload start --
                .copyString("83a4")
                .copyString("00")
                .copyArgument(intputCount)
                // --- intput start (need for loop) ---
                // + ScriptAssembler.copyString("825820")
                // + ScriptAssembler.copyArgument(txId)
                // + ScriptAssembler.copyArgument(txIdIndex)
                .copyArgument(inputList)
                // --- intput end ---
                // --- output change start ---
                .ifEqual(changeAmount, "0000000000000000",
                        // ---- output count start ----
                        new ScriptAssembler().copyString("0181").getScript(),
                        new ScriptAssembler().copyString("0182")
                        // ---- output count end ----
                        .copyString("82583901")
                        .copyArgument(changeAddress)
                        .copyArgument(changeAmountPrefix)
                        .copyArgument(changeAmount, Buffer.CACHE2)
                        .ifEqual(changeAmountPrefix, "18", new ScriptAssembler().copyArgument(ScriptData.getDataBufferAll(Buffer.CACHE2, 7)).getScript(), "")
                        .ifEqual(changeAmountPrefix, "19", new ScriptAssembler().copyArgument(ScriptData.getDataBufferAll(Buffer.CACHE2, 6)).getScript(), "")
                        .ifEqual(changeAmountPrefix, "1a", new ScriptAssembler().copyArgument(ScriptData.getDataBufferAll(Buffer.CACHE2, 4)).getScript(), "")
                        .ifEqual(changeAmountPrefix, "1b", new ScriptAssembler().copyArgument(ScriptData.getDataBufferAll(Buffer.CACHE2)).getScript(), "")
                        .clearBuffer(Buffer.CACHE2).getScript()
                )
                // --- output change end ---
                // --- output receive start ---
                .copyString("82583900")
                .copyArgument(receiverAddress)
                .copyArgument(receiveAmountPrefix)
                .copyArgument(receiveAmount, Buffer.CACHE2)
                .ifEqual(receiveAmountPrefix, "18", new ScriptAssembler().copyArgument(ScriptData.getDataBufferAll(Buffer.CACHE2, 7)).getScript(), "")
                .ifEqual(receiveAmountPrefix, "19", new ScriptAssembler().copyArgument(ScriptData.getDataBufferAll(Buffer.CACHE2, 6)).getScript(), "")
                .ifEqual(receiveAmountPrefix, "1a", new ScriptAssembler().copyArgument(ScriptData.getDataBufferAll(Buffer.CACHE2, 4)).getScript(), "")
                .ifEqual(receiveAmountPrefix, "1b", new ScriptAssembler().copyArgument(ScriptData.getDataBufferAll(Buffer.CACHE2)).getScript(), "")
                .clearBuffer(Buffer.CACHE2)
                // --- output receive end ---
                // --- fee start ---
                .copyString("02")
                .copyArgument(feePrefix)
                .copyArgument(fee, Buffer.CACHE2)
                .ifEqual(feePrefix, "18", new ScriptAssembler().copyArgument(ScriptData.getDataBufferAll(Buffer.CACHE2, 7)).getScript(), "")
                .ifEqual(feePrefix, "19", new ScriptAssembler().copyArgument(ScriptData.getDataBufferAll(Buffer.CACHE2, 6)).getScript(), "")
                .ifEqual(feePrefix, "1a", new ScriptAssembler().copyArgument(ScriptData.getDataBufferAll(Buffer.CACHE2, 4)).getScript(), "")
                .ifEqual(feePrefix, "1b", new ScriptAssembler().copyArgument(ScriptData.getDataBufferAll(Buffer.CACHE2)).getScript(), "")
                .clearBuffer(Buffer.CACHE2)
                // --- fee end ---
                // --- invalid hereafter start ---
                .copyString("03")
                .copyArgument(invalidHereafterPrefix)
                .copyArgument(invalidHereafter, Buffer.CACHE2)
                .ifEqual(invalidHereafterPrefix, "18", new ScriptAssembler().copyArgument(ScriptData.getDataBufferAll(Buffer.CACHE2, 7)).getScript(), "")
                .ifEqual(invalidHereafterPrefix, "19", new ScriptAssembler().copyArgument(ScriptData.getDataBufferAll(Buffer.CACHE2, 6)).getScript(), "")
                .ifEqual(invalidHereafterPrefix, "1a", new ScriptAssembler().copyArgument(ScriptData.getDataBufferAll(Buffer.CACHE2, 4)).getScript(), "")
                .ifEqual(invalidHereafterPrefix, "1b", new ScriptAssembler().copyArgument(ScriptData.getDataBufferAll(Buffer.CACHE2)).getScript(), "")
                .clearBuffer(Buffer.CACHE2)
                // --- invalid hereafter end ---
                .copyString("9ffff6")
                // -- payload end --
                .showMessage("ADA")
                // -- show address start --
                .copyString("030303030001040412", Buffer.CACHE2)
                .copyString("01", Buffer.CACHE1)
                .copyArgument(receiverAddress, Buffer.CACHE1)
                .baseConvert(ScriptData.getDataBufferAll(Buffer.CACHE1), Buffer.CACHE2, 92, ScriptAssembler.binary32Charset, ScriptAssembler.bitLeftJustify8to5)
                .copyString("000000000000", Buffer.CACHE2)
                .clearBuffer(Buffer.CACHE1)
                .bech32Polymod(ScriptData.getDataBufferAll(Buffer.CACHE2), Buffer.CACHE1)
                .clearBuffer(Buffer.CACHE2)
                .copyString(HexUtil.toHexString("addr1"), Buffer.CACHE2)
                .copyString("01", Buffer.CACHE1)
                .copyArgument(receiverAddress, Buffer.CACHE1) // EXTENDED : [Polymod(4B)][01(1B)][receiverAddress(56B)]
                .baseConvert(ScriptData.getBuffer(Buffer.CACHE1, 4, 57), Buffer.CACHE2, 92, ScriptAssembler.base32BitcoinCashCharset, ScriptAssembler.bitLeftJustify8to5)
                .baseConvert(ScriptData.getBuffer(Buffer.CACHE1, 0, 4), Buffer.CACHE2, 6, ScriptAssembler.base32BitcoinCashCharset, 0)
                .showAddress(ScriptData.getDataBufferAll(Buffer.CACHE2))
                .clearBuffer(Buffer.CACHE2)
                .clearBuffer(Buffer.CACHE1)
                // -- show address end --
                // -- show amount start --
                .showAmount(receiveAmount, 6)
                // -- show amount end --
                .showPressButton()
                // version=04 ScriptAssembler.hash=0E=ScriptAssembler.Blake2b256 sign=03=BIP32EDDSA
                .setHeader(HashType.Blake2b256, SignType.BIP32EDDSA)
                .getScript();
        return script;
        // 01a74ecc
    }
    
    public static String ADATransactionScriptSignature = "3046022100C3AC9E75BD1BBA972111FF13EA93ED425FCA37D68F8537C5FA0CE109F99E340602210080A03317325CC7651E51E74C0C3A6A0A485A4E63A096B114CB5554ED02CB29C7";

}
