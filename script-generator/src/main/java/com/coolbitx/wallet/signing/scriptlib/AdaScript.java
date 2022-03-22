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

        ScriptData changeAddressLength = sac.getArgument(1);
        ScriptData changeAddress = sac.getArgumentVariableLength(57);
        ScriptData changeAmountLength = sac.getArgument(1);
        ScriptData changeAmountPrefix = sac.getArgument(1);
        ScriptData changeAmount = sac.getArgumentVariableLength(8);

        ScriptData receiverAddressLength = sac.getArgument(1);
        ScriptData receiverAddress = sac.getArgumentVariableLength(57);
        ScriptData receiverAmountLength = sac.getArgument(1);
        ScriptData receiverAmountPrefix = sac.getArgument(1);
        ScriptData receiverAmount = sac.getArgumentVariableLength(8);

        ScriptData feeLength = sac.getArgument(1);
        ScriptData feePrefix = sac.getArgument(1);
        ScriptData fee = sac.getArgumentVariableLength(8);

        ScriptData ttlLength = sac.getArgument(1);
        ScriptData ttlPrefix = sac.getArgument(1);
        ScriptData ttl = sac.getArgumentVariableLength(8);

        ScriptData inputs = sac.getArgumentAll();

        String script = new ScriptAssembler()
                .setCoinType(0x0717)
                // -- payload start --
                .copyString("a4")
                // --- intput start ---
                .copyArgument(inputs)
                // --- intput end ---
                // --- output start ---
                .copyString("01")
                .ifEqual(changeAmount, "0000000000000000",
                    // ---- output count start ----
                    new ScriptAssembler().copyString("81").getScript(),
                    new ScriptAssembler().copyString("82").getScript()
                    // ---- output count end ----
                )
                // --- output receive start ---
                .copyString("8258")
                .copyArgument(receiverAddressLength)
                .setBufferInt(receiverAddressLength, 29, 57)
                .copyArgument(receiverAddress)
                .copyArgument(receiverAmountPrefix)
                .setBufferInt(receiverAmountLength, 1, 8)
                .ifRange(receiverAmount, "00", "17", "",
                    new ScriptAssembler()
                    .copyArgument(receiverAmount)
                    .getScript()
                )
                // --- output receive end ---
                .ifEqual(changeAmount, "0000000000000000", "",
                    // --- output change start ---
                    new ScriptAssembler().copyString("8258")
                    .copyArgument(changeAddressLength)
                    .setBufferInt(changeAddressLength, 29, 57)
                    .copyArgument(changeAddress)
                    .copyArgument(changeAmountPrefix)
                    .setBufferInt(changeAmountLength, 0, 8)
                    .copyArgument(changeAmount).getScript()
                    // --- output change end ---
                )
                // --- output end ---
                // --- fee start ---
                .copyString("02")
                .copyArgument(feePrefix)
                .setBufferInt(feeLength, 0, 8)
                .copyArgument(fee)
                // --- fee end ---
                // --- ttl start ---
                .copyString("03")
                .copyArgument(ttlPrefix)
                .setBufferInt(ttlLength, 0, 8)
                .copyArgument(ttl)
                // --- ttl end ---
                // -- payload end --
                .showMessage("ADA")
                // -- show address start --
                // expanded human readable part of "addr"
                .copyString("030303030001040412", Buffer.CACHE2)
                // checksum to buffer 2
                .setBufferInt(receiverAddressLength, 29, 57)
                .baseConvert(receiverAddress, Buffer.CACHE2, 92, ScriptAssembler.binary32Charset, ScriptAssembler.bitLeftJustify8to5)
                .copyString("000000000000", Buffer.CACHE2)
                .bech32Polymod(ScriptData.getDataBufferAll(Buffer.CACHE2), Buffer.CACHE1)
                .clearBuffer(Buffer.CACHE2)
                .baseConvert(ScriptData.getDataBufferAll(Buffer.CACHE1), Buffer.CACHE2, 6, ScriptAssembler.base32BitcoinCashCharset, 0)
                .clearBuffer(Buffer.CACHE1)
                // data
                .copyString(HexUtil.toHexString("addr1"), Buffer.CACHE1)
                .baseConvert(receiverAddress, Buffer.CACHE1, 92, ScriptAssembler.base32BitcoinCashCharset, ScriptAssembler.bitLeftJustify8to5)
                .copyArgument(ScriptData.getDataBufferAll(Buffer.CACHE2), Buffer.CACHE1)
                .showAddress(ScriptData.getDataBufferAll(Buffer.CACHE1))
                .clearBuffer(Buffer.CACHE2)
                .clearBuffer(Buffer.CACHE1)
                // -- show address end --
                // -- show amount start --
                .setBufferInt(receiverAmountLength, 1, 8)
                .showAmount(receiverAmount, 6)
                // -- show amount end --
                .showPressButton()
                // version=04 ScriptAssembler.hash=0E=ScriptAssembler.Blake2b256 sign=03=BIP32EDDSA
                .setHeader(HashType.Blake2b256, SignType.BIP32EDDSA)
                .getScript();
        return script;
        // 01a74ecc
    }
    
    public static String ADATransactionScriptSignature = "003045022100B190289BCFF78978300C820D4DAC8D879EECE6E92E7D347D221926FC21D71DAA0220538F8B702D7008540BEFAC715CBA71EBFD022429A7A103E0E5B86AF69073B346";

}
