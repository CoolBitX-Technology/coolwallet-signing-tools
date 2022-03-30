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

// Address Encode Types
//
// 0: Byron address
// 1: Shelley address with payment and delegation parts
// 2: Shelley address with payment part only
// 3: Shelley address with "stake" prefix

public class AdaScript {

    public static void listAll() {
        System.out.println("ADA: \n" + getADATransactionScript() + "\n");
    }

    public static String getADATransactionScript() {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();

        ScriptData changeAddressLength = sac.getArgument(1);
        ScriptData changeAddress = sac.getArgumentVariableLength(90);
        ScriptData changeAmountLength = sac.getArgument(1);
        ScriptData changeAmountPrefix = sac.getArgument(1);
        ScriptData changeAmount = sac.getArgumentVariableLength(8);

        ScriptData receiverAddressEncodeType = sac.getArgument(1);
        ScriptData receiverAddressLength = sac.getArgument(1);
        ScriptData receiverAddress = sac.getArgumentVariableLength(90);
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
                // not support shelley stake address
                .ifRange(receiverAddressEncodeType, "00", "02", "", ScriptAssembler.throwSEError)
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
                .setBufferInt(receiverAddressLength, 29, 90) // Shelley max : 57, Byron max : 83
                .copyArgument(receiverAddress)
                .copyArgument(receiverAmountPrefix)
                .setBufferInt(receiverAmountLength, 0, 8)
                .copyArgument(receiverAmount)
                // --- output receive end ---
                .ifEqual(changeAmount, "0000000000000000", "",
                    // --- output change start ---
                    new ScriptAssembler().copyString("8258")
                    .copyArgument(changeAddressLength)
                    .setBufferInt(changeAddressLength, 29, 90)
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
                .ifEqual(receiverAddressEncodeType, "00",

                    // Base58 - Byron
                    new ScriptAssembler()
                    .setBufferInt(receiverAddressLength, 29, 90)
                    .baseConvert(receiverAddress, Buffer.CACHE1, 120, ScriptAssembler.base58Charset, ScriptAssembler.zeroInherit)
                    .getScript(),

                    // Bech32 - Shelley
                    new ScriptAssembler()
                    // expanded human readable part of "addr"
                    .copyString("030303030001040412", Buffer.CACHE2)
                    // checksum to buffer 2
                    .setBufferInt(receiverAddressLength, 29, 57)
                    .ifEqual(receiverAddressEncodeType, "01",
                      new ScriptAssembler().baseConvert(receiverAddress, Buffer.CACHE2, 92, ScriptAssembler.binary32Charset, ScriptAssembler.bitLeftJustify8to5).getScript(),
                      new ScriptAssembler().baseConvert(receiverAddress, Buffer.CACHE2, 47, ScriptAssembler.binary32Charset, ScriptAssembler.bitLeftJustify8to5).getScript()
                    )
                    .copyString("000000000000", Buffer.CACHE2)
                    .bech32Polymod(ScriptData.getDataBufferAll(Buffer.CACHE2), Buffer.CACHE1)
                    .clearBuffer(Buffer.CACHE2)
                    .baseConvert(ScriptData.getDataBufferAll(Buffer.CACHE1), Buffer.CACHE2, 6, ScriptAssembler.base32BitcoinCashCharset, 0)
                    .clearBuffer(Buffer.CACHE1)
                    // data
                    .copyString(HexUtil.toHexString("addr1"), Buffer.CACHE1)
                    .ifEqual(receiverAddressEncodeType, "01",
                      new ScriptAssembler().baseConvert(receiverAddress, Buffer.CACHE1, 92, ScriptAssembler.base32BitcoinCashCharset, ScriptAssembler.bitLeftJustify8to5).getScript(),
                      new ScriptAssembler().baseConvert(receiverAddress, Buffer.CACHE1, 47, ScriptAssembler.base32BitcoinCashCharset, ScriptAssembler.bitLeftJustify8to5).getScript()
                    )
                    .copyArgument(ScriptData.getDataBufferAll(Buffer.CACHE2), Buffer.CACHE1)
                    .getScript()
                )
                .showAddress(ScriptData.getDataBufferAll(Buffer.CACHE1))
                .clearBuffer(Buffer.CACHE2)
                .clearBuffer(Buffer.CACHE1)
                // -- show address end --
                // -- show amount start --
                .ifRange(receiverAmountPrefix, "00", "17",
                    new ScriptAssembler()
                      .showAmount(receiverAmountPrefix, 6)
                      .getScript(),
                    new ScriptAssembler()
                      .setBufferInt(receiverAmountLength, 1, 8)
                      .showAmount(receiverAmount, 6)
                      .getScript()
                )
                // -- show amount end --
                .showPressButton()
                // version=04 ScriptAssembler.hash=0E=ScriptAssembler.Blake2b256 sign=03=BIP32EDDSA
                .setHeader(HashType.Blake2b256, SignType.BIP32EDDSA)
                .getScript();
        return script;
        // 01a74ecc
    }
    
    public static String ADATransactionScriptSignature = "0000304402202A93139827D7ED463320B2B9D31317EB71CB361FBA3612B7B5E363CAFCCD735C022037657AE592284219D45B236A34346FB797FC3290ED23593C836DDF95CB156CDC";

}
