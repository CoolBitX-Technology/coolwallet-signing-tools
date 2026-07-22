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
        System.out.println("ADA Token Transfer: \n" + getADATokenTransferScript() + "\n");
        System.out.println("ADA Token Transfer Blind: \n" + getADATokenTransferBlindScript() + "\n");
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

    // Change output bytes shared by every ADA tx type: 8258 <addrLen> <addr> <value-blob>.
    // value-blob is a pre-encoded CBOR value supplied in the argument: a bare uint for an
    // ADA-only change, or 82 <lovelace> <multiasset_map> when the change carries native
    // tokens. Encoding the value upstream lets one change layout serve both cases, so a UTXO
    // holding tokens can be spent without a dedicated script — the tokens ride back in change.
    //
    // changeValueLength is a 2-byte field (value blob is up to 2048 bytes, so 1 byte / max 255
    // no longer suffices) read via setBufferIntUnsafe (no on-card range check). The SDK already
    // rejects a change value over 2048 bytes in getChangeArgument, so an on-card guard would be
    // redundant. 2048 bytes holds ~50 distinct-policy tokens (or ~250 sharing a policy) in change.
    public static String getChangeOutputScript(ScriptData changeAddressLength, ScriptData changeAddress,
        ScriptData changeValueLength, ScriptData changeValue) {
        return new ScriptAssembler().copyString("8258").copyArgument(changeAddressLength)
            .setBufferInt(changeAddressLength, 29, 90).copyArgument(changeAddress)
            .setBufferIntUnsafe(changeValueLength).copyArgument(changeValue).getScript();
    }

    // Output section for staking txs (no receiver output): output map key 01, then the array
    // count 80 (no change) or 81 (change only) followed by the change output. Change presence
    // keys on changeAddressLength == 00 because the value-blob layout has no separate amount.
    public static String getStakingOutputSectionScript(ScriptData changeAddressLength, ScriptData changeAddress,
        ScriptData changeValueLength, ScriptData changeValue) {
        return new ScriptAssembler().copyString("01").ifEqual(changeAddressLength, "00",
            new ScriptAssembler().copyString("80").getScript(),
            new ScriptAssembler().copyString("81")
                .insertString(getChangeOutputScript(changeAddressLength, changeAddress, changeValueLength, changeValue))
                .getScript())
            .getScript();
    }

    // "ADA" label + the min-ADA (lovelace) amount at 6 decimals. Shared by the normal and blind
    // token-transfer displays: the lovelace is always small enough for the SE to render, so it is
    // shown even when the (possibly huge) token amount is blinded out as SMART.
    public static String getShowLovelaceScript(ScriptData lovelacePrefix, ScriptData lovelaceLength,
        ScriptData lovelace) {
        return new ScriptAssembler().showMessage("ADA")
            .ifRange(lovelacePrefix, "00", "17",
                new ScriptAssembler().showAmount(lovelacePrefix, 6).getScript(),
                new ScriptAssembler().setBufferInt(lovelaceLength, 1, 8).showAmount(lovelace, 6).getScript())
            .getScript();
    }

    public static String getADATransactionScript() {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();

        ScriptData changeAddressLength = sac.getArgument(1);
        ScriptData changeAddress = sac.getArgumentVariableLength(90);
        ScriptData changeValueLength = sac.getArgument(2);
        ScriptData changeValue = sac.getArgumentVariableLength(2048);

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
            // output count: 1 (receiver only) when no change, 2 when change present
            .copyString("01").ifEqual(changeAddressLength, "00",
                new ScriptAssembler().copyString("81").getScript(), new ScriptAssembler().copyString("82").getScript()
            )
            // --- output receive start ---
            .copyString("8258").copyArgument(receiverAddressLength)
            // Shelley max : 57,
            // Byron max : 83
            .setBufferInt(receiverAddressLength, 29, 90).copyArgument(receiverAddress)
            .copyArgument(receiverAmountPrefix).setBufferInt(receiverAmountLength, 0, 8).copyArgument(receiverAmount)
            // --- output receive end ---
            // --- output change start (skipped when changeAddressLength == 00) ---
            .ifEqual(changeAddressLength, "00", "",
                getChangeOutputScript(changeAddressLength, changeAddress, changeValueLength, changeValue)
            )
            // --- output change end ---
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
        "3045022100d0f91da94578a94dfb36f4c48f2dcb0f539a5d10801aa88ac8b727300cc861810220406e5722461dbafda58027220377ab514b361b70f29e26748f88497b37379deb",
        144, '0');

    // Native-token transfer: the receiver output carries exactly one native token (per the SDK
    // "one token per tx, the rest becomes change" rule). Unlike the plain transfer whose receiver
    // value is a bare lovelace uint, here the value is [lovelace, multiasset] where multiasset is a
    // single-policy / single-asset map:
    //     8258 <addrLen> <addr> 82 <lovelace> a1 581c<policyId> a1 <assetNameCbor> <tokenAmount>
    // The change output is unchanged (still the shared value-blob layout, which may itself carry the
    // leftover tokens as change). Because the to-address is external, the token bytes in the receiver
    // output are security-critical, so the card must show the user WHICH token and HOW MUCH. The
    // token identity+metadata (decimals, symbol, policyId, assetNameCbor) is verified against a
    // CoolBitX-key signature via ifSigned: a verified token shows its symbol, an unverified one is
    // shown with a leading "@". The very bytes that are signature-checked (policyId, assetNameCbor)
    // are the same bytes copied into the body, so a tampered token can never display a trusted symbol.
    public static String getADATokenTransferScript() {
        return tokenTransferScript(false);
    }

    // Blind variant for amounts the SE can't render (a displayed integer >= 1e8): the body bytes are
    // identical to the normal script; only the display changes to "ADA -> SMART -> PRESS". The SDK
    // switches to this script when the human-readable token amount reaches 1e8 (see coin-ada index.ts).
    public static String getADATokenTransferBlindScript() {
        return tokenTransferScript(true);
    }

    // Shared arguments + body for both token-transfer variants; `blind` selects the display tail only,
    // so the two scripts always build the exact same signed body from the exact same argument layout.
    private static String tokenTransferScript(boolean blind) {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();

        ScriptData changeAddressLength = sac.getArgument(1);
        ScriptData changeAddress = sac.getArgumentVariableLength(90);
        ScriptData changeValueLength = sac.getArgument(2);
        ScriptData changeValue = sac.getArgumentVariableLength(2048);

        ScriptData receiverAddressEncodeType = sac.getArgument(1);
        ScriptData receiverAddressLength = sac.getArgument(1);
        ScriptData receiverAddress = sac.getArgumentVariableLength(90);
        ScriptData lovelaceLength = sac.getArgument(1);
        ScriptData lovelacePrefix = sac.getArgument(1);
        ScriptData lovelace = sac.getArgumentVariableLength(8);

        // Token info: one contiguous, fixed-width block so ifSigned can hash it as a union. Layout:
        // decimals(1) + symbolLength(1) + symbol(7) + policyId(28) + assetNameCborLength(1) +
        // assetNameCbor(34) = 72 bytes. symbol and assetNameCbor sit in fixed slots (zero-padded on
        // the right); their real length is carried by the *Length fields. The SDK signs SHA256 of
        // exactly these 72 bytes with the CoolBitX key.
        ScriptData tokenInfo = sac.getArgumentUnion(0, 72);
        ScriptData tokenDecimals = sac.getArgument(1);
        ScriptData tokenSymbolLength = sac.getArgument(1);
        ScriptData tokenSymbol = sac.getArgumentVariableLength(7);
        ScriptData policyId = sac.getArgument(28);
        ScriptData assetNameCborLength = sac.getArgument(1);
        ScriptData assetNameCbor = sac.getArgumentVariableLength(34);
        ScriptData tokenSign = sac.getArgument(72);

        ScriptData tokenAmountLength = sac.getArgument(1);
        ScriptData tokenAmountPrefix = sac.getArgument(1);
        ScriptData tokenAmount = sac.getArgumentVariableLength(8);

        ScriptData feeLength = sac.getArgument(1);
        ScriptData feePrefix = sac.getArgument(1);
        ScriptData fee = sac.getArgumentVariableLength(8);

        ScriptData ttlLength = sac.getArgument(1);
        ScriptData ttlPrefix = sac.getArgument(1);
        ScriptData ttl = sac.getArgumentVariableLength(8);

        ScriptData inputs = sac.getArgumentAll();

        ScriptAssembler asm = new ScriptAssembler().setCoinType(0x0717)
            // not supported address encode type
            .ifRange(receiverAddressEncodeType, "00", "02", "", ScriptAssembler.throwSEError)
            // -- payload start --
            .copyString("a4")
            // --- input start ---
            .copyArgument(inputs)
            // --- input end ---
            // --- output start ---
            // output count: 1 (receiver only) when no change, 2 when change present
            .copyString("01").ifEqual(changeAddressLength, "00",
                new ScriptAssembler().copyString("81").getScript(), new ScriptAssembler().copyString("82").getScript()
            )
            // --- output receive start ---
            .copyString("8258").copyArgument(receiverAddressLength)
            // Shelley max : 57, Byron max : 83
            .setBufferInt(receiverAddressLength, 29, 90).copyArgument(receiverAddress)
            // value = [lovelace, multiasset]
            .copyString("82")
            .copyArgument(lovelacePrefix).setBufferInt(lovelaceLength, 0, 8).copyArgument(lovelace)
            // multiasset: one policy, one asset under it
            .copyString("a1").copyString("581c").copyArgument(policyId)
            .copyString("a1").setBufferInt(assetNameCborLength, 1, 34).copyArgument(assetNameCbor)
            .copyArgument(tokenAmountPrefix).setBufferInt(tokenAmountLength, 0, 8).copyArgument(tokenAmount)
            // --- output receive end ---
            // --- output change start (skipped when changeAddressLength == 00) ---
            .ifEqual(changeAddressLength, "00", "",
                getChangeOutputScript(changeAddressLength, changeAddress, changeValueLength, changeValue)
            )
            // --- output change end ---
            // --- output end ---
            // --- fee start ---
            .copyString("02").copyArgument(feePrefix).setBufferInt(feeLength, 0, 8).copyArgument(fee)
            // --- fee end ---
            // --- ttl start ---
            .copyString("03").copyArgument(ttlPrefix).setBufferInt(ttlLength, 0, 8).copyArgument(ttl)
            // --- ttl end / payload end ---
            .showMessage("ADA");

        if (blind) {
            // Full blind, matching every other chain's token blind sign (TON/TRC20/ERC20/Cronos/...):
            // the token symbol, receiver address and amounts are all replaced by one SMART page. Used
            // when the token amount would exceed what the SE can render; body bytes are unchanged.
            asm.showWrap("SMART", "");
        } else {
            asm
                // -- show token symbol: verified official token shows its symbol, otherwise "@" prefix --
                .clearBuffer(Buffer.CACHE2)
                .ifSigned(tokenInfo, tokenSign, "",
                    new ScriptAssembler().copyString(HexUtil.toHexString("@"), Buffer.CACHE2).getScript())
                .setBufferInt(tokenSymbolLength, 1, 7).copyArgument(tokenSymbol, Buffer.CACHE2)
                .showMessage(ScriptData.getDataBufferAll(Buffer.CACHE2)).clearBuffer(Buffer.CACHE2)
                // -- show receiver address --
                .insertString(getShowAddressScript(receiverAddressEncodeType, receiverAddressLength, receiverAddress))
                // -- show token amount: decimals come from the (signed) metadata, so the amount is staged
                //    into CACHE2 and shown with decimals from bufferInt. Small values live in the CBOR
                //    prefix byte (<= 0x17); larger ones in the value bytes. --
                .ifRange(tokenAmountPrefix, "00", "17",
                    new ScriptAssembler().setBufferInt(tokenDecimals, 0, 20)
                        .showAmount(tokenAmountPrefix, ScriptData.bufInt).getScript(),
                    new ScriptAssembler().setBufferInt(tokenAmountLength, 1, 8).copyArgument(tokenAmount, Buffer.CACHE2)
                        .setBufferInt(tokenDecimals, 0, 20)
                        .showAmount(ScriptData.getDataBufferAll(Buffer.CACHE2), ScriptData.bufInt)
                        .clearBuffer(Buffer.CACHE2).getScript())
                // -- show lovelace label + amount (min-ADA leaving the wallet) --
                .insertString(getShowLovelaceScript(lovelacePrefix, lovelaceLength, lovelace));
        }

        // version=04 hash=0E=Blake2b256 sign=03=BIP32EDDSA
        return asm.showPressButton().setHeader(HashType.Blake2b256, SignType.BIP32EDDSA).getScript();
    }

    public static final String ADATokenTransferScriptSignature = Strings.padStart("3045022100d9afc0a2d579f0d1d61243ad758888af13e8b147c816c2a76f2322b24fe6d4e6022038bf456f6f5d4db47d2c57eadeb9b864f3d8f956691441043e59ec1ddabacdce", 144, '0');

    public static final String ADATokenTransferBlindScriptSignature = Strings.padStart("304602210096bb33f72f8056b90d8945e64366d8c087d3acc7288673c627e7149c45c71041022100958dc351e731ed909ed1f20b3a4f589a916933e1527af6e0881451207cf9ffa6", 144, '0');

    public static String getADAStakeRegistrationScript() {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();

        ScriptData changeAddressLength = sac.getArgument(1);
        ScriptData changeAddress = sac.getArgumentVariableLength(90);
        ScriptData changeValueLength = sac.getArgument(2);
        ScriptData changeValue = sac.getArgumentVariableLength(2048);

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
            .insertString(getStakingOutputSectionScript(changeAddressLength, changeAddress, changeValueLength, changeValue))
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
        "3044022062d8f402f5ea4aa034c624c9a03680490ca57d213743d67877d3c6e896726016022047fddd68f98ba707b5f34561bf885ecd23306ebe96251a7dafb8e8077fcea073",
        144, '0');

    public static String getADAStakeRegistrationAndDelegationScript() {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();

        ScriptData changeAddressLength = sac.getArgument(1);
        ScriptData changeAddress = sac.getArgumentVariableLength(90);
        ScriptData changeValueLength = sac.getArgument(2);
        ScriptData changeValue = sac.getArgumentVariableLength(2048);

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
            .insertString(getStakingOutputSectionScript(changeAddressLength, changeAddress, changeValueLength, changeValue))
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
        "3043021f4372c71ee74aa5cbdb5f6bfdef55e01d7610df5507df9d157b18a74c3d504c022003eb0f7eddeedb348db7e32e7bea59d329aa778aaa3f5dc8808dc2a6502a8cd5",
        144, '0');

    public static String getADAStakeDelegationScript() {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();

        ScriptData changeAddressLength = sac.getArgument(1);
        ScriptData changeAddress = sac.getArgumentVariableLength(90);
        ScriptData changeValueLength = sac.getArgument(2);
        ScriptData changeValue = sac.getArgumentVariableLength(2048);

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
            .insertString(getStakingOutputSectionScript(changeAddressLength, changeAddress, changeValueLength, changeValue))
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
        "3046022100fd6b647d4882b11ee9ad5bdf9e2a2ab9967edaadf2d0851fe5f02563d974a4d1022100884b7770d1ba24f4c0702fb74b1757e1ebae342fa6d68c5f8982ce80f6d31ffa",
        144, '0');

    public static String getADAStakeDeregistrationScript() {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();

        ScriptData changeAddressLength = sac.getArgument(1);
        ScriptData changeAddress = sac.getArgumentVariableLength(90);
        ScriptData changeValueLength = sac.getArgument(2);
        ScriptData changeValue = sac.getArgumentVariableLength(2048);

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
            .insertString(getStakingOutputSectionScript(changeAddressLength, changeAddress, changeValueLength, changeValue))
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
        "30440220081976b1fbe7181fc3ff16b6514ecf86b4ce74d126853c8f08463791c4a0c7a8022024758fab5decadf8d14a01e980b7867390248e0b3f7ffb6e03573aea4942ebe6",
        144, '0');

    public static String getADARewardsWithdrawalScript() {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();

        ScriptData changeAddressLength = sac.getArgument(1);
        ScriptData changeAddress = sac.getArgumentVariableLength(90);
        ScriptData changeValueLength = sac.getArgument(2);
        ScriptData changeValue = sac.getArgumentVariableLength(2048);

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
            .insertString(getStakingOutputSectionScript(changeAddressLength, changeAddress, changeValueLength, changeValue))
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
        "3045022012241ac33675a95019439698107a0dfc4e2c816c1dba0e25a8d0314cf13e7f87022100cc36c421209d9bfd2cf88d6ddce5825143888c98fb526f040a0421bd7bc7ed61",
        144, '0');

    public static String getADAGovernanceVoteDRepAbstainScript() { // Delegated Representative Abstain
        ScriptArgumentComposer sac = new ScriptArgumentComposer();

        ScriptData changeAddressLength = sac.getArgument(1);
        ScriptData changeAddress = sac.getArgumentVariableLength(90);
        ScriptData changeValueLength = sac.getArgument(2);
        ScriptData changeValue = sac.getArgumentVariableLength(2048);

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
            .insertString(getStakingOutputSectionScript(changeAddressLength, changeAddress, changeValueLength, changeValue))
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
        "30440220027e3327ee64cdbdf534b6bfa99d3024db5e6f8742fcdcbe7f2eef8e5c186cba0220567011d3066ac42c0ad6fada8e8e7632ef3afb5036cde623cebfe1cb13b6391b",
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
