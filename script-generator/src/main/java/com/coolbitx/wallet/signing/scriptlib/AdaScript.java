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

// Address Encode Types
//
// 0: Byron address
// 1: Shelley address with payment and delegation parts
// 2: Shelley address with payment part only
// (not support) Shelley address with "stake" prefix
public class AdaScript {

    public static void main(String[] args) {
        listAll();
    }

    public static void listAll() {
        System.out.println("ADA Transfer: \n" + getADATransactionScript() + "\n");
        System.out.println("ADA Stake Registration: \n" + getADAStakeRegistrationScript() + "\n");
        System.out
            .println("ADA Stake Registration And Delegation: \n" + getADAStakeRegistrationAndDelegationScript() + "\n");
        System.out.println("ADA Stake Delegation: \n" + getADAStakeDelegationScript() + "\n");
        System.out.println("ADA Stake Deregistration: \n" + getADAStakeDeregistrationScript() + "\n");
        System.out.println("ADA Rewards Withdrawal: \n" + getADARewardsWithdrawalScript() + "\n");
        System.out.println("ADA Governance Vote DRep Abstain: \n" + getADAGovernanceVoteDRepAbstainScript() + "\n");
    }

    public static String getShowAddressScript(ScriptData encodeType, ScriptData addrLen, ScriptData addrHex) {
        return new ScriptAssembler().ifEqual(encodeType, "00",
            // Base58 - Byron
            new ScriptAssembler().setBufferInt(addrLen, 29, 90)
                .baseConvert(addrHex, Buffer.CACHE1, 120, ScriptAssembler.base58Charset, ScriptAssembler.zeroInherit)
                .getScript(),
            // Bech32 - Shelley
            new ScriptAssembler()
                // expanded human readable part of "addr"
                .copyString("030303030001040412", Buffer.CACHE2)
                // checksum to buffer 2
                .setBufferInt(addrLen, 29, 57)
                .ifEqual(encodeType, "01",
                    new ScriptAssembler().baseConvert(addrHex, Buffer.CACHE2, 92, ScriptAssembler.binary32Charset,
                        ScriptAssembler.bitLeftJustify8to5).getScript(),
                    new ScriptAssembler().baseConvert(addrHex, Buffer.CACHE2, 47, ScriptAssembler.binary32Charset,
                        ScriptAssembler.bitLeftJustify8to5).getScript())
                .copyString("000000000000", Buffer.CACHE2)
                .bech32Polymod(ScriptData.getDataBufferAll(Buffer.CACHE2), Buffer.CACHE1).clearBuffer(Buffer.CACHE2)
                .baseConvert(ScriptData.getDataBufferAll(Buffer.CACHE1), Buffer.CACHE2, 6,
                    ScriptAssembler.base32BitcoinCashCharset, 0)
                .clearBuffer(Buffer.CACHE1)
                // data
                .copyString(HexUtil.toHexString("addr1"), Buffer.CACHE1)
                .ifEqual(encodeType, "01",
                    new ScriptAssembler().baseConvert(addrHex, Buffer.CACHE1, 92,
                        ScriptAssembler.base32BitcoinCashCharset, ScriptAssembler.bitLeftJustify8to5).getScript(),
                    new ScriptAssembler().baseConvert(addrHex, Buffer.CACHE1, 47,
                        ScriptAssembler.base32BitcoinCashCharset, ScriptAssembler.bitLeftJustify8to5).getScript())
                .copyArgument(ScriptData.getDataBufferAll(Buffer.CACHE2), Buffer.CACHE1).getScript())
            .showAddress(ScriptData.getDataBufferAll(Buffer.CACHE1)).clearBuffer(Buffer.CACHE2)
            .clearBuffer(Buffer.CACHE1).getScript();
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

        String script = new ScriptAssembler().setCoinType(0x0717)
            // not supported address encode type
            .ifRange(receiverAddressEncodeType, "00", "02", "", ScriptAssembler.throwSEError)
            // -- payload start --
            .copyString("a4")
            // --- intput start ---
            .copyArgument(inputs)
            // --- intput end ---
            // --- output start ---
            .copyString("01").ifEqual(changeAmount, "0000000000000000",
                // ---- output count start ----
                new ScriptAssembler().copyString("81").getScript(), new ScriptAssembler().copyString("82").getScript()
            // ---- output count end ----
            )
            // --- output receive start ---
            .copyString("8258").copyArgument(receiverAddressLength)
            // Shelley max : 57,
            // Byron max : 83
            .setBufferInt(receiverAddressLength, 29, 90).copyArgument(receiverAddress)
            .copyArgument(receiverAmountPrefix).setBufferInt(receiverAmountLength, 0, 8).copyArgument(receiverAmount)
            // --- output receive end ---
            .ifEqual(changeAmount, "0000000000000000", "",
                // --- output change start ---
                new ScriptAssembler().copyString("8258").copyArgument(changeAddressLength)
                    .setBufferInt(changeAddressLength, 29, 90).copyArgument(changeAddress)
                    .copyArgument(changeAmountPrefix).setBufferInt(changeAmountLength, 0, 8).copyArgument(changeAmount)
                    .getScript()
            // --- output change end ---
            )
            // --- output end ---
            // --- fee start ---
            .copyString("02").copyArgument(feePrefix).setBufferInt(feeLength, 0, 8).copyArgument(fee)
            // --- fee end ---
            // --- ttl start ---
            .copyString("03").copyArgument(ttlPrefix).setBufferInt(ttlLength, 0, 8).copyArgument(ttl)
            // --- ttl end ---
            // -- payload end --
            .showMessage("ADA")
            // -- show address start --
            .insertString(getShowAddressScript(receiverAddressEncodeType, receiverAddressLength, receiverAddress))
            // -- show address end --
            // -- show amount start --
            .ifRange(receiverAmountPrefix, "00", "17",
                new ScriptAssembler().showAmount(receiverAmountPrefix, 6).getScript(),
                new ScriptAssembler().setBufferInt(receiverAmountLength, 1, 8).showAmount(receiverAmount, 6)
                    .getScript())
            // -- show amount end --
            .showPressButton()
            // version=04 ScriptAssembler.hash=0E=ScriptAssembler.Blake2b256
            // sign=03=BIP32EDDSA
            .setHeader(HashType.Blake2b256, SignType.BIP32EDDSA).getScript();
        return script;
        // 01a74ecc
    }

    public static final String ADATransactionScriptSignature = Strings.padStart(
        "3045022100F0399524A966B863DF60F0B21095343D4993605BC89EEFB257586467B52CFD9402204AD6EEAE4A6872FC376838AB4216A2C476B154BB3E1674CB9242F7009CA159CA",
        144, '0');

    public static String getADAStakeRegistrationScript() {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();

        ScriptData changeAddressLength = sac.getArgument(1);
        ScriptData changeAddress = sac.getArgumentVariableLength(90);
        ScriptData changeAmountLength = sac.getArgument(1);
        ScriptData changeAmountPrefix = sac.getArgument(1);
        ScriptData changeAmount = sac.getArgumentVariableLength(8);

        ScriptData feeLength = sac.getArgument(1);
        ScriptData feePrefix = sac.getArgument(1);
        ScriptData fee = sac.getArgumentVariableLength(8);

        ScriptData ttlLength = sac.getArgument(1);
        ScriptData ttlPrefix = sac.getArgument(1);
        ScriptData ttl = sac.getArgumentVariableLength(8);

        ScriptData stakeKeyHash = sac.getArgument(28);

        ScriptData inputs = sac.getArgumentAll();

        String script = new ScriptAssembler().setCoinType(0x0717)
            // -- payload start --
            .copyString("a5")
            // --- intput start ---
            .copyArgument(inputs)
            // --- intput end ---
            // --- output start ---
            .copyString("01").ifEqual(changeAmount, "0000000000000000",
                // ---- output count start ----
                new ScriptAssembler().copyString("80").getScript(), new ScriptAssembler().copyString("81")
                    // ---- output count end ----
                    // --- output change start ---
                    .copyString("8258").copyArgument(changeAddressLength).setBufferInt(changeAddressLength, 29, 90)
                    .copyArgument(changeAddress).copyArgument(changeAmountPrefix).setBufferInt(changeAmountLength, 0, 8)
                    .copyArgument(changeAmount).getScript()
            // --- output change end ---
            )
            // --- output end ---
            // --- fee start ---
            .copyString("02").copyArgument(feePrefix).setBufferInt(feeLength, 0, 8).copyArgument(fee)
            // --- fee end ---
            // ttl 03 (Uint)
            // 1a (Uint) 02126ed9

            // --- ttl start ---
            .copyString("03").copyArgument(ttlPrefix).setBufferInt(ttlLength, 0, 8).copyArgument(ttl)
            // --- ttl end ---
            // --- certs start ---
            // certs 04 (Uint)
            // 81 (Array)
            // cert1 82 (Array)
            // register 00 (Uint)
            // credential 82 (Array)
            // type 00 (Uint)
            // addrKH 58 (Byte) 1c b7ef7a17a5eb9d5c6e82046cc4b22b6f25509cf225c5a4c848988567
            .copyString("048182008200581c").copyArgument(stakeKeyHash)
            // --- certs end ---

            // -- payload end --
            .showMessage("ADA").showMessage("Reg").showPressButton()
            // version=04 ScriptAssembler.hash=0E=ScriptAssembler.Blake2b256
            // sign=03=BIP32EDDSA
            .setHeader(HashType.Blake2b256, SignType.BIP32EDDSA).getScript();
        return script;
    }

    public static final String ADAStakeRegistrationScriptSignature = Strings.padStart(
        "3045022100D5F4EA82D4CEF766C92B46E531DF8272A68BE84C38914F25D48AF927671F64F7022010C9EE292134B514BB29EDDC33F01D053DDB77F23162E03EEF0D73F4AFB67775",
        144, '0');

    public static String getADAStakeRegistrationAndDelegationScript() {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();

        ScriptData changeAddressLength = sac.getArgument(1);
        ScriptData changeAddress = sac.getArgumentVariableLength(90);
        ScriptData changeAmountLength = sac.getArgument(1);
        ScriptData changeAmountPrefix = sac.getArgument(1);
        ScriptData changeAmount = sac.getArgumentVariableLength(8);

        ScriptData feeLength = sac.getArgument(1);
        ScriptData feePrefix = sac.getArgument(1);
        ScriptData fee = sac.getArgumentVariableLength(8);

        ScriptData ttlLength = sac.getArgument(1);
        ScriptData ttlPrefix = sac.getArgument(1);
        ScriptData ttl = sac.getArgumentVariableLength(8);

        ScriptData stakeKeyHash = sac.getArgument(28);
        ScriptData poolKeyHash = sac.getArgument(28);

        ScriptData inputs = sac.getArgumentAll();

        String script = new ScriptAssembler().setCoinType(0x0717)
            // -- payload start --
            .copyString("a5")
            // --- intput start ---
            .copyArgument(inputs)
            // --- intput end ---
            // --- output start ---
            .copyString("01").ifEqual(changeAmount, "0000000000000000",
                // ---- output count start ----
                new ScriptAssembler().copyString("80").getScript(), new ScriptAssembler().copyString("81")
                    // ---- output count end ----
                    // --- output change start ---
                    .copyString("8258").copyArgument(changeAddressLength).setBufferInt(changeAddressLength, 29, 90)
                    .copyArgument(changeAddress).copyArgument(changeAmountPrefix).setBufferInt(changeAmountLength, 0, 8)
                    .copyArgument(changeAmount).getScript()
            // --- output change end ---
            )
            // --- output end ---
            // --- fee start ---
            .copyString("02").copyArgument(feePrefix).setBufferInt(feeLength, 0, 8).copyArgument(fee)
            // --- fee end ---
            // ttl 03 (Uint)
            // 1a (Uint) 02126ed9

            // --- ttl start ---
            .copyString("03").copyArgument(ttlPrefix).setBufferInt(ttlLength, 0, 8).copyArgument(ttl)
            // --- ttl end ---
            // --- certs start ---
            // certs 04 (Uint)
            // 82 (Array)
            // cert1 82 (Array)
            // register 00 (Uint)
            // credential 82 (Array)
            // type 00 (Uint)
            // addrKH 58 (Byte) 1c b7ef7a17a5eb9d5c6e82046cc4b22b6f25509cf225c5a4c848988567
            // cert2 83 (Array)
            // delegate 02 (Uint)
            // credential 82 (Array)
            // type 00 (Uint)
            // addrKH 58 (Byte) 1c b7ef7a17a5eb9d5c6e82046cc4b22b6f25509cf225c5a4c848988567
            // poolKH 58 (Byte) 1c 007c8cf86eb1eebd45871699623a283e77400e96789ffd2fa7d9a4b1
            .copyString("048282008200581c").copyArgument(stakeKeyHash).copyString("83028200581c")
            .copyArgument(stakeKeyHash).copyString("581c").copyArgument(poolKeyHash)
            // --- certs end ---

            // -- payload end --
            .showMessage("ADA").showMessage("Delgt").showPressButton()
            // version=04 ScriptAssembler.hash=0E=ScriptAssembler.Blake2b256
            // sign=03=BIP32EDDSA
            .setHeader(HashType.Blake2b256, SignType.BIP32EDDSA).getScript();
        return script;
    }

    public static final String ADAStakeRegistrationAndDelegationScriptSignature = Strings.padStart(
        "3046022100C458F53070161F5BD73F9F0BD7FE26CF5D861DFD170F0FE51DEDD97B25B8B808022100FA97152CC285E9B518F18371B617613D56A3A548344206C619D98F169A93841F",
        144, '0');

    public static String getADAStakeDelegationScript() {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();

        ScriptData changeAddressLength = sac.getArgument(1);
        ScriptData changeAddress = sac.getArgumentVariableLength(90);
        ScriptData changeAmountLength = sac.getArgument(1);
        ScriptData changeAmountPrefix = sac.getArgument(1);
        ScriptData changeAmount = sac.getArgumentVariableLength(8);

        ScriptData feeLength = sac.getArgument(1);
        ScriptData feePrefix = sac.getArgument(1);
        ScriptData fee = sac.getArgumentVariableLength(8);

        ScriptData ttlLength = sac.getArgument(1);
        ScriptData ttlPrefix = sac.getArgument(1);
        ScriptData ttl = sac.getArgumentVariableLength(8);

        ScriptData stakeKeyHash = sac.getArgument(28);
        ScriptData poolKeyHash = sac.getArgument(28);

        ScriptData inputs = sac.getArgumentAll();

        String script = new ScriptAssembler().setCoinType(0x0717)
            // -- payload start --
            .copyString("a5")
            // --- intput start ---
            .copyArgument(inputs)
            // --- intput end ---
            // --- output start ---
            .copyString("01").ifEqual(changeAmount, "0000000000000000",
                // ---- output count start ----
                new ScriptAssembler().copyString("80").getScript(), new ScriptAssembler().copyString("81")
                    // ---- output count end ----
                    // --- output change start ---
                    .copyString("8258").copyArgument(changeAddressLength).setBufferInt(changeAddressLength, 29, 90)
                    .copyArgument(changeAddress).copyArgument(changeAmountPrefix).setBufferInt(changeAmountLength, 0, 8)
                    .copyArgument(changeAmount).getScript()
            // --- output change end ---
            )
            // --- output end ---
            // --- fee start ---
            .copyString("02").copyArgument(feePrefix).setBufferInt(feeLength, 0, 8).copyArgument(fee)
            // --- fee end ---
            // ttl 03 (Uint)
            // 1a (Uint) 02126ed9

            // --- ttl start ---
            .copyString("03").copyArgument(ttlPrefix).setBufferInt(ttlLength, 0, 8).copyArgument(ttl)
            // --- ttl end ---
            // --- certs start ---
            // certs 04 (Uint)
            // 81 (Array)
            // cert1 83 (Array)
            // delegate 02 (Uint)
            // credential 82 (Array)
            // type 00 (Uint)
            // addrKH 58 (Byte) 1c b7ef7a17a5eb9d5c6e82046cc4b22b6f25509cf225c5a4c848988567
            // poolKH 58 (Byte) 1c 007c8cf86eb1eebd45871699623a283e77400e96789ffd2fa7d9a4b1
            .copyString("048183028200581c").copyArgument(stakeKeyHash).copyString("581c").copyArgument(poolKeyHash)
            // --- certs end ---

            // -- payload end --
            .showMessage("ADA").showMessage("Delgt").showPressButton()
            // version=04 ScriptAssembler.hash=0E=ScriptAssembler.Blake2b256
            // sign=03=BIP32EDDSA
            .setHeader(HashType.Blake2b256, SignType.BIP32EDDSA).getScript();
        return script;
    }

    public static final String ADAStakeDelegationScriptSignature = Strings.padStart(
        "304602210099B45BC4C655C45842B43202275BC267100253609D81F9121C96FC86E3BCC72A022100A17D228C4BA9911D34A90B2B85D3E5A2160F0D07DCBB4D96A4E96264FBD2A1DF",
        144, '0');

    public static String getADAStakeDeregistrationScript() {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();

        ScriptData changeAddressLength = sac.getArgument(1);
        ScriptData changeAddress = sac.getArgumentVariableLength(90);
        ScriptData changeAmountLength = sac.getArgument(1);
        ScriptData changeAmountPrefix = sac.getArgument(1);
        ScriptData changeAmount = sac.getArgumentVariableLength(8);

        ScriptData feeLength = sac.getArgument(1);
        ScriptData feePrefix = sac.getArgument(1);
        ScriptData fee = sac.getArgumentVariableLength(8);

        ScriptData ttlLength = sac.getArgument(1);
        ScriptData ttlPrefix = sac.getArgument(1);
        ScriptData ttl = sac.getArgumentVariableLength(8);

        ScriptData stakeKeyHash = sac.getArgument(28);

        ScriptData inputs = sac.getArgumentAll();

        String script = new ScriptAssembler().setCoinType(0x0717)
            // -- payload start --
            .copyString("a5")
            // --- intput start ---
            .copyArgument(inputs)
            // --- intput end ---
            // --- output start ---
            .copyString("01").ifEqual(changeAmount, "0000000000000000",
                // ---- output count start ----
                new ScriptAssembler().copyString("80").getScript(), new ScriptAssembler().copyString("81")
                    // ---- output count end ----
                    // --- output change start ---
                    .copyString("8258").copyArgument(changeAddressLength).setBufferInt(changeAddressLength, 29, 90)
                    .copyArgument(changeAddress).copyArgument(changeAmountPrefix).setBufferInt(changeAmountLength, 0, 8)
                    .copyArgument(changeAmount).getScript()
            // --- output change end ---
            )
            // --- output end ---
            // --- fee start ---
            .copyString("02").copyArgument(feePrefix).setBufferInt(feeLength, 0, 8).copyArgument(fee)
            // --- fee end ---
            // ttl 03 (Uint)
            // 1a (Uint) 02126ed9

            // --- ttl start ---
            .copyString("03").copyArgument(ttlPrefix).setBufferInt(ttlLength, 0, 8).copyArgument(ttl)
            // --- ttl end ---
            // --- certs start ---
            // certs 04 (Uint)
            // 81 (Array)
            // cert1 82 (Array)
            // deregister 01 (Uint)
            // credential 82 (Array)
            // type 00 (Uint)
            // addrKH 58 (Byte) 1c b7ef7a17a5eb9d5c6e82046cc4b22b6f25509cf225c5a4c848988567
            .copyString("048182018200581c").copyArgument(stakeKeyHash)
            // --- certs end ---

            // -- payload end --
            .showMessage("ADA").showMessage("Dereg").showPressButton()
            // version=04 ScriptAssembler.hash=0E=ScriptAssembler.Blake2b256
            // sign=03=BIP32EDDSA
            .setHeader(HashType.Blake2b256, SignType.BIP32EDDSA).getScript();
        return script;
    }

    public static final String ADAStakeDeregistrationScriptSignature = Strings.padStart(
        "30450221008D1D857FE283FDE0B659C4E89C33D6C1BDD219E06FD18AC02859F8C48AE7BA81022062F39064D0742685564CD1FC8E8C650156AE6F112CFEC21E8A4F8E59DA6F0F12",
        144, '0');

    public static String getADARewardsWithdrawalScript() {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();

        ScriptData changeAddressLength = sac.getArgument(1);
        ScriptData changeAddress = sac.getArgumentVariableLength(90);
        ScriptData changeAmountLength = sac.getArgument(1);
        ScriptData changeAmountPrefix = sac.getArgument(1);
        ScriptData changeAmount = sac.getArgumentVariableLength(8);

        ScriptData feeLength = sac.getArgument(1);
        ScriptData feePrefix = sac.getArgument(1);
        ScriptData fee = sac.getArgumentVariableLength(8);

        ScriptData ttlLength = sac.getArgument(1);
        ScriptData ttlPrefix = sac.getArgument(1);
        ScriptData ttl = sac.getArgumentVariableLength(8);

        ScriptData stakeKeyHash = sac.getArgument(28);

        ScriptData withdrawAmountLength = sac.getArgument(1);
        ScriptData withdrawAmountPrefix = sac.getArgument(1);
        ScriptData withdrawAmount = sac.getArgumentVariableLength(8);

        ScriptData inputs = sac.getArgumentAll();

        String script = new ScriptAssembler().setCoinType(0x0717)
            // -- payload start --
            .copyString("a5")
            // --- intput start ---
            .copyArgument(inputs)
            // --- intput end ---
            // --- output start ---
            .copyString("01").ifEqual(changeAmount, "0000000000000000",
                // ---- output count start ----
                new ScriptAssembler().copyString("80").getScript(), new ScriptAssembler().copyString("81")
                    // ---- output count end ----
                    // --- output change start ---
                    .copyString("8258").copyArgument(changeAddressLength).setBufferInt(changeAddressLength, 29, 90)
                    .copyArgument(changeAddress).copyArgument(changeAmountPrefix).setBufferInt(changeAmountLength, 0, 8)
                    .copyArgument(changeAmount).getScript()
            // --- output change end ---
            )
            // --- output end ---
            // --- fee start ---
            .copyString("02").copyArgument(feePrefix).setBufferInt(feeLength, 0, 8).copyArgument(fee)
            // --- fee end ---
            // ttl 03 (Uint)
            // 1a (Uint) 02126ed9

            // --- ttl start ---
            .copyString("03").copyArgument(ttlPrefix).setBufferInt(ttlLength, 0, 8).copyArgument(ttl)
            // --- ttl end ---
            // --- withdrawals start ---
            // withdrawals 05 (Uint)
            // a1 (Map)
            // rewardAcc 58 (Byte) 1d e1
            // b7ef7a17a5eb9d5c6e82046cc4b22b6f25509cf225c5a4c848988567
            // coin 18 (Uint) 64
            .copyString("05a1581de1").copyArgument(stakeKeyHash).copyArgument(withdrawAmountPrefix)
            .setBufferInt(withdrawAmountLength, 0, 8).copyArgument(withdrawAmount)
            // --- withdrawals end ---

            // -- payload end --
            .showMessage("ADA").showMessage("Withdr").showPressButton()
            // version=04 ScriptAssembler.hash=0E=ScriptAssembler.Blake2b256
            // sign=03=BIP32EDDSA
            .setHeader(HashType.Blake2b256, SignType.BIP32EDDSA).getScript();
        return script;
    }

    public static final String ADARewardsWithdrawalScriptSignature = Strings.padStart(
        "3044022054BF2BC0FAECDA40CBA09D4C269022A9C5D707D1895039C4D1776941A6012EEC02205DC643B7BA31BE18F9F4D145722FB7B3442E4C5EE2B16064B40B9BD2236F6C80",
        144, '0');

    public static String getADAGovernanceVoteDRepAbstainScript() { // Delegated Representative Abstain
        ScriptArgumentComposer sac = new ScriptArgumentComposer();

        ScriptData changeAddressLength = sac.getArgument(1);
        ScriptData changeAddress = sac.getArgumentVariableLength(90);
        ScriptData changeAmountLength = sac.getArgument(1);
        ScriptData changeAmountPrefix = sac.getArgument(1);
        ScriptData changeAmount = sac.getArgumentVariableLength(8);

        ScriptData feeLength = sac.getArgument(1);
        ScriptData feePrefix = sac.getArgument(1);
        ScriptData fee = sac.getArgumentVariableLength(8);

        ScriptData ttlLength = sac.getArgument(1);
        ScriptData ttlPrefix = sac.getArgument(1);
        ScriptData ttl = sac.getArgumentVariableLength(8);

        ScriptData stakeKeyHash = sac.getArgument(28);

        ScriptData inputs = sac.getArgumentAll();

        String script = new ScriptAssembler().setCoinType(0x0717)
            // -- payload start --
            .copyString("a5")
            // --- intput start ---
            .copyArgument(inputs)
            // --- intput end ---
            // --- output start ---
            .copyString("01").ifEqual(changeAmount, "0000000000000000",
                // ---- output count start ----
                new ScriptAssembler().copyString("80").getScript(), new ScriptAssembler().copyString("81")
                    // ---- output count end ----
                    // --- output change start ---
                    .copyString("8258").copyArgument(changeAddressLength).setBufferInt(changeAddressLength, 29, 90)
                    .copyArgument(changeAddress).copyArgument(changeAmountPrefix).setBufferInt(changeAmountLength, 0, 8)
                    .copyArgument(changeAmount).getScript()
            // --- output change end ---
            )
            // --- output end ---
            // --- fee start ---
            .copyString("02").copyArgument(feePrefix).setBufferInt(feeLength, 0, 8).copyArgument(fee)
            // --- fee end ---
            // ttl 03 (Uint)
            // 1a (Uint) 02126ed9

            // --- ttl start ---
            .copyString("03").copyArgument(ttlPrefix).setBufferInt(ttlLength, 0, 8).copyArgument(ttl)
            // --- ttl end ---
            // --- certs start ---
            // 04 // certificates
            // d90102 // tag(258)
            // 81 // Array with length 1
            // 83 // Array with length 3
            // 09 // DRep Always Abstain
            // 82
            // 00 // credential type
            // 581c d5c85e06499c113db681255b6850f54ce0f889648193859399dbf50a // stake key
            // hash
            // 81 // Array with length 1
            // 02 // Always Abstain marker
            .copyString("04d901028183098200581c").copyArgument(stakeKeyHash).copyString("8102")
            // --- certs end ---

            // -- payload end --
            .showMessage("ADA").showMessage("Abstain").showPressButton()
            // version=04 ScriptAssembler.hash=0E=ScriptAssembler.Blake2b256
            // sign=03=BIP32EDDSA
            .setHeader(HashType.Blake2b256, SignType.BIP32EDDSA).getScript();
        return script;
    }

    public static final String ADAGovernanceVoteDRepAbstainScriptSignature = Strings.padStart(
        "3044022009dc4cb3ae3657b1da1451d95198b96e0193a2d6afbe9616bc51cc45cb30d61002206658fada489dbb9c27ab3eb0c6da09ab24e7ea120224fd3ce0f6a9cae1c13e09",
        144, '0');

    // [
    // protected_header, # 包含演算法資訊（ex: a1 01 26 → Ed25519）
    // unprotected_header, # 這裡通常是空 {}
    // payload, # 你要簽的原始訊息
    // signature # 真正的簽章 bytes
    // ]
    public static String getADASignMessagRlpScript() {
        ScriptRlpArray array = new ScriptRlpArray();
        ScriptRlpData argMessageLength = array.getRlpItemArgument();
        ScriptRlpData argMessage = array.getRlpItemArgument();

        String script = new ScriptAssembler().setCoinType(0x0717).copyString("84")
            // -- protected_header --
            .copyString("43").copyString("a10126")
            // --- unprotected_header ---
            .copyString("a0")
            // --- payload ---
            .ifRange(argMessageLength, "00", "FF", new ScriptAssembler().copyString("58").getScript(),
                new ScriptAssembler().copyString("59").getScript())
            .copyArgument(argMessageLength).copyArgument(argMessage)
            // --- signature ---
            .copyString("f6").showMessage("ADA").showWrap("MESSAGE", "").showPressButton()
            // version=04 ScriptAssembler.hash=0E=ScriptAssembler.Blake2b256
            // sign=03=BIP32EDDSA
            .setHeader(HashType.Blake2b256, SignType.BIP32EDDSA).getScript();
        return script;
    }

    public static final String ADASignMessageRlpScriptSignature = Strings.padEnd("FA", 144, '0');

    // [
    // "Signature1",
    // protected header,
    // external_aad,
    // payload
    // ]
    public static String getADASignMessageArgumentScript() {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();

        ScriptData receiverAddressLength = sac.getArgument(1);
        ScriptData receiverAddress = sac.getArgumentVariableLength(90);
        ScriptData messagePrefix = sac.getArgumentRightJustified(3);
        ScriptData message = sac.getArgumentAll();

        String script = new ScriptAssembler().setCoinType(0x0717).copyString("84") // Array with four elements
            // -- "Signature1" --
            .copyString("6a") // UTF-8(10 bytes)
            .copyString(HexUtil.toHexString("Signature1".getBytes()))
            // --- protected headers ---
            .copyString("58").copyString("46").copyString("a2") // Map with two elements
            .copyString("01").copyString("27") // key: alg = Ed25519
            .copyString("67").copyString(HexUtil.toHexString("address".getBytes())) // key: address
            .copyString("58").copyArgument(receiverAddressLength).setBufferInt(receiverAddressLength, 29, 90)
            .copyArgument(receiverAddress)
            // --- external_aad ---
            .copyString("40")
            // --- payload ---
            .copyArgument(messagePrefix).copyArgument(message).showMessage("ADA").showWrap("MESSAGE", "")
            .showPressButton()
            // // version=04 ScriptAssembler.hash=0E=ScriptAssembler.Blake2b256
            // sign=03=BIP32EDDSA
            .setHeader(HashType.NONE, SignType.BIP32EDDSA).getScript();
        return script;
    }

    public static final String ADASignMessageArgumentScriptSignature = Strings.padStart(
        "3044022017c4ea35c02e2ce2ffd22c8e01c964fc69916195066af306edd8c894b960e4e302205cb2a8e8d5c57095865f576c1e2749ef57be105dd6ce5a721f50cedbb4e86bf6",
        144, '0');

}
