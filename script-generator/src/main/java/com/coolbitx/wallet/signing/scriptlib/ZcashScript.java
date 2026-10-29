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
import com.coolbitx.wallet.signing.utils.ScriptObjectAbstract;
import com.coolbitx.wallet.signing.utils.ScriptRlpArray;
import com.coolbitx.wallet.signing.utils.ScriptRlpData;
import com.google.common.base.Strings;

public class ZcashScript {
    // header(0x80000005, overwintered v5) | nVersionGroupId(0x26A7270A), little endian
    private static final String V5_VERSION_AND_GROUP_ID = "050000800A27A726";

    private static final String SIGHASH_ALL_ANYONECANPAY = "81";

    // BLAKE2b-256 of empty data under "ZTxIdPrevoutHash", "ZTxTrAmountsHash", "ZTxTrScriptsHash",
    // "ZTxIdSequencHash": ANYONECANPAY commits to none of the other inputs
    private static final String EMPTY_OTHER_INPUTS_DIGESTS =
        "A04B16834DC939CCF632D15DDDAA6BCAED253D12068DCA169FBD28BC403CF3BA"
            + "CBDC27BD0946A43495971D2887D6DD1EB4BFDE3807B0F74E195E9DC17D4EB3F0"
            + "F1C4AD734FE0613F1078B0598D9BAA9A9663DE48F390393CF842AA37FE095B98"
            + "3A0033336603235001742438238D713C09C1C55BF67BD20B80805AEA0B46EBA5";

    // BLAKE2b-256 of empty data under "ZTxIdSaplingHash", "ZTxIdOrchardHash": a transparent-only transaction has
    // no shielded bundle
    private static final String EMPTY_SHIELDED_DIGESTS =
        "6F2FC8F98FEAFD94E74A0DF4BED74391EE0B5A69945E4CED8CA8A095206F00AE"
            + "9FBE4ED13B0C08E671C11A3407D84E1117CD45028A2EEE1B9FEAE78B48A6E2C1";

    public static void main(String[] args) {
        listAll();
    }

    public static void listAll() {
        System.out.println("Zcash transparent transaction: \n" + getZECScript(false) + "\n");
        System.out.println("Zcash v5 transparent transaction: \n" + getZECV5Script(false) + "\n");
    }

    /**
     * @deprecated Used only by the v4 script; remove with getZECScript.
     */
    @Deprecated
    public static String getAddressScript(boolean isTestnet, ScriptObjectAbstract argOutputScriptType,
        ScriptObjectAbstract argOutputDest) {
        String addressScript = new ScriptAssembler()
            .switchString(argOutputScriptType, Buffer.CACHE2, !isTestnet ? "1CB8,1CBD" : "1D25,1CBA") // t1,t3:tm,t2
            .copyArgument(argOutputDest, Buffer.CACHE2)
            .hash(ScriptData.getDataBufferAll(Buffer.CACHE2), Buffer.CACHE2, ScriptAssembler.HashType.DoubleSHA256)
            .baseConvert(ScriptData.getBuffer(Buffer.CACHE2, 0, 26), Buffer.CACHE1, 0, ScriptAssembler.base58Charset,
                ScriptAssembler.zeroInherit)
            .showAddress(ScriptData.getDataBufferAll(Buffer.CACHE1))
            .getScript();
        return addressScript;
    }

    /**
     * @deprecated v4 transactions are no longer valid once NU7 activates (around 2026-11-05); remove after that, along
     * with getAddressScript and ZECScriptSignature. Use getZECV5Script.
     */
    @Deprecated
    public static String getZECScript(boolean isTestnet) {
        ScriptRlpArray array = new ScriptRlpArray();
        ScriptRlpData argReverseVersion = array.getRlpItemArgument();
        ScriptRlpData argReverseGroupId = array.getRlpItemArgument();
        ScriptRlpData argHashPrevouts = array.getRlpItemArgument();
        ScriptRlpData argHashSequences = array.getRlpItemArgument();
        ScriptRlpData argOutputAmount = array.getRlpItemArgument();
        ScriptRlpData argOutputScriptType = array.getRlpItemArgument();
        ScriptRlpData argOutputDest = array.getRlpItemArgument();

        ScriptRlpData argHaveChange = array.getRlpItemArgument();
        ScriptRlpData argChangeAmount = array.getRlpItemArgument();
        ScriptRlpData argChangeScriptType = array.getRlpItemArgument();
        ScriptRlpData argChangePath = array.getRlpItemArgument();
        ScriptRlpData argOutputHashPersonal = array.getRlpItemArgument();

        ScriptRlpData argLockTime = array.getRlpItemArgument();
        ScriptRlpData argExpiryHeight = array.getRlpItemArgument();
        ScriptRlpData argReverseHashType = array.getRlpItemArgument();

        String addressScript = getAddressScript(isTestnet, argOutputScriptType, argOutputDest);

        String script = new ScriptAssembler().setCoinType(0x85)
            .copyArgument(argReverseVersion)
            .copyArgument(argReverseGroupId)
            .copyArgument(argHashPrevouts)
            .copyArgument(argHashSequences)
            .baseConvert(argOutputAmount, Buffer.CACHE1, 8, ScriptAssembler.binaryCharset, ScriptAssembler.littleEndian)
            // switch redeemScript P2PKH=00,P2SH=01
            .switchString(argOutputScriptType, Buffer.CACHE1, "1976A914,17A914")
            .copyArgument(argOutputDest, Buffer.CACHE1)
            .switchString(argOutputScriptType, Buffer.CACHE1, "88AC,87")
            // if haveChange
            .ifEqual(argHaveChange, "01",
                new ScriptAssembler()
                    .baseConvert(argChangeAmount, Buffer.CACHE1, 8, ScriptAssembler.binaryCharset,
                        ScriptAssembler.littleEndian)
                    .derivePublicKey(argChangePath, Buffer.CACHE2)
                    .copyString("1976A914", Buffer.CACHE1)
                    // if P2PKH
                    .ifEqual(argChangeScriptType, "00",
                        new ScriptAssembler()
                            .hash(ScriptData.getDataBufferAll(Buffer.CACHE2), Buffer.CACHE1,
                                ScriptAssembler.HashType.SHA256RipeMD160)
                            .getScript(),
                        "")
                    .copyString("88AC", Buffer.CACHE1)
                    .getScript(),
                "")
            .advancedHash(ScriptData.getDataBufferAll(Buffer.CACHE1), argOutputHashPersonal, Buffer.TRANSACTION,
                ScriptAssembler.AdvancedHashType.Blake2b256Personal)
            .copyString("0000000000000000000000000000000000000000000000000000000000000000") // Hash JoinSplits
            .copyString("0000000000000000000000000000000000000000000000000000000000000000") // Hash ShieldedSpends
            .copyString("0000000000000000000000000000000000000000000000000000000000000000") // Hash ShieldedOutputs
            .copyArgument(argLockTime)
            .copyArgument(argExpiryHeight)
            .copyString("0000000000000000") // Value Balance
            .copyArgument(argReverseHashType)
            .utxoDataPlaceholder()
            .clearBuffer(Buffer.CACHE1)
            .clearBuffer(Buffer.CACHE2)
            .showMessage("ZEC")
            .insertString(addressScript)
            .showAmount(argOutputAmount, 8)
            .showPressButton()
            .setHeader(ScriptAssembler.AdvancedHashType.Blake2b256Personal, ScriptAssembler.SignType.ECDSA)
            .getScript();
        return script;
    }

    // sighash = H("ZcashTxHash_"+branchId,
    // │   header_digest  = H("ZTxIdHeadersHash", the 20-byte header)
    // │   transparent_sig_digest = H("ZTxIdTranspaHash",
    // │   │   hashType (0x81, 1 byte)
    // │   │   prevouts_digest      ┐
    // │   │   amounts_digest       │ ANYONECANPAY: commits to no other input,
    // │   │   scriptpubkeys_digest │ so each is the hash of empty data, a constant
    // │   │   sequence_digest      ┘
    // │   │   outputs_digest = H("ZTxIdOutputsHash", all outputs)   ← what the user sees on the card
    // │   │   txin_sig_digest = H("Zcash___TxInHash",
    // │   │         prevout | value | scriptPubKey | sequence)      ← the input being signed
    // │   )
    // │   sapling_digest  = H("ZTxIdSaplingHash", empty)   ← constant
    // │   orchard_digest  = H("ZTxIdOrchardHash", empty)   ← constant
    // )
    public static String getZECV5Script(boolean isTestnet) {
        ScriptRlpArray array = new ScriptRlpArray();
        ScriptRlpData argOutputAmount = array.getRlpItemArgument();
        ScriptRlpData argOutputScriptType = array.getRlpItemArgument();
        ScriptRlpData argOutputDest = array.getRlpItemArgument();

        ScriptRlpData argHaveChange = array.getRlpItemArgument();
        ScriptRlpData argChangeAmount = array.getRlpItemArgument();
        ScriptRlpData argChangeScriptType = array.getRlpItemArgument();
        ScriptRlpData argChangePath = array.getRlpItemArgument();

        ScriptRlpData argConsensusBranchId = array.getRlpItemArgument();
        ScriptRlpData argLockTime = array.getRlpItemArgument();
        ScriptRlpData argExpiryHeight = array.getRlpItemArgument();

        String addressScript = getV5AddressScript(isTestnet, argOutputScriptType, argOutputDest);

        String script = new ScriptAssembler().setCoinType(0x85)
            // T.1 header digest
            .copyString(V5_VERSION_AND_GROUP_ID, Buffer.CACHE1)
            .copyArgument(argConsensusBranchId, Buffer.CACHE1)
            .copyArgument(argLockTime, Buffer.CACHE1)
            .copyArgument(argExpiryHeight, Buffer.CACHE1)
            .copyString(HexUtil.toHexString("ZTxIdHeadersHash"), Buffer.CACHE2)
            .advancedHash(ScriptData.getDataBufferAll(Buffer.CACHE1), ScriptData.getDataBufferAll(Buffer.CACHE2),
                Buffer.TRANSACTION, ScriptAssembler.AdvancedHashType.Blake2b256Personal)
            .clearBuffer(Buffer.CACHE1)
            .clearBuffer(Buffer.CACHE2)

            // T.2c outputs digest
            .baseConvert(argOutputAmount, Buffer.CACHE1, 8, ScriptAssembler.binaryCharset, ScriptAssembler.littleEndian)
            // switch redeemScript P2PKH=00,P2SH=01
            .switchString(argOutputScriptType, Buffer.CACHE1, "1976A914,17A914")
            // the same 20 bytes as the address shown (getV5AddressScript)
            .baseConvert(argOutputDest, Buffer.CACHE1, 20, ScriptAssembler.binaryCharset, 0)
            .switchString(argOutputScriptType, Buffer.CACHE1, "88AC,87")
            // if haveChange
            .ifEqual(argHaveChange, "01",
                new ScriptAssembler()
                    .baseConvert(argChangeAmount, Buffer.CACHE1, 8, ScriptAssembler.binaryCharset,
                        ScriptAssembler.littleEndian)
                    .derivePublicKey(argChangePath, Buffer.CACHE2)
                    .copyString("1976A914", Buffer.CACHE1)
                    // if P2PKH
                    .ifEqual(argChangeScriptType, "00",
                        new ScriptAssembler()
                            .hash(ScriptData.getDataBufferAll(Buffer.CACHE2), Buffer.CACHE1,
                                ScriptAssembler.HashType.SHA256RipeMD160)
                            .getScript(),
                        "")
                    .copyString("88AC", Buffer.CACHE1)
                    .getScript(),
                "")
            .clearBuffer(Buffer.CACHE2)
            .copyString(HexUtil.toHexString("ZTxIdOutputsHash"), Buffer.CACHE2)
            .advancedHash(ScriptData.getDataBufferAll(Buffer.CACHE1), ScriptData.getDataBufferAll(Buffer.CACHE2),
                Buffer.TRANSACTION, ScriptAssembler.AdvancedHashType.Blake2b256Personal)
            .copyArgument(argConsensusBranchId)
            .clearBuffer(Buffer.CACHE1)
            .clearBuffer(Buffer.CACHE2)
            .showMessage("ZEC")
            .insertString(addressScript)
            .showAmount(argOutputAmount, 8)
            .showPressButton()
            .utxoSection()
            .insertString(getZECV5UtxoSectionScript())
            .setHeader(ScriptAssembler.HashType.NONE, ScriptAssembler.SignType.ECDSA)
            .getScript();
        return script;
    }

    /**
     * Runs once per input with prevout(36) | value(8, LE) | pubKeyHash(20) | sequence(4) as the argument, and appends
     * the input's signature digest to TRANSACTION for the SE to sign as is.
     */
    private static String getZECV5UtxoSectionScript() {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptData argPrevout = sac.getArgument(36);
        ScriptData argValue = sac.getArgument(8);
        ScriptData argPubKeyHash = sac.getArgument(20);
        ScriptData argSequence = sac.getArgument(4);

        ScriptData headerDigest = ScriptData.getBuffer(Buffer.TRANSACTION, 0, 32);
        ScriptData outputsDigest = ScriptData.getBuffer(Buffer.TRANSACTION, 32, 32);
        ScriptData consensusBranchId = ScriptData.getBuffer(Buffer.TRANSACTION, 64, 4);
        // each digest lands in CACHE2 right after the 16-byte personalization it was hashed with
        ScriptData lastDigest = ScriptData.getBuffer(Buffer.CACHE2, 16, 32);

        return new ScriptAssembler()
            // S.2g txin digest: the spent output is P2PKH
            .copyArgument(argPrevout, Buffer.CACHE1)
            .copyArgument(argValue, Buffer.CACHE1)
            .copyString("1976A914", Buffer.CACHE1)
            .copyArgument(argPubKeyHash, Buffer.CACHE1)
            .copyString("88AC", Buffer.CACHE1)
            .copyArgument(argSequence, Buffer.CACHE1)
            .copyString(HexUtil.toHexString("Zcash___TxInHash"), Buffer.CACHE2)
            .advancedHash(ScriptData.getDataBufferAll(Buffer.CACHE1), ScriptData.getDataBufferAll(Buffer.CACHE2),
                Buffer.CACHE2, ScriptAssembler.AdvancedHashType.Blake2b256Personal)
            
            // S.2 transparent digest
            .clearBuffer(Buffer.CACHE1)
            .copyString(SIGHASH_ALL_ANYONECANPAY + EMPTY_OTHER_INPUTS_DIGESTS, Buffer.CACHE1)
            .copyArgument(outputsDigest, Buffer.CACHE1)
            .copyArgument(lastDigest, Buffer.CACHE1)
            .clearBuffer(Buffer.CACHE2)
            .copyString(HexUtil.toHexString("ZTxIdTranspaHash"), Buffer.CACHE2)
            .advancedHash(ScriptData.getDataBufferAll(Buffer.CACHE1), ScriptData.getDataBufferAll(Buffer.CACHE2),
                Buffer.CACHE2, ScriptAssembler.AdvancedHashType.Blake2b256Personal)
            
            // signature digest, personalized with the consensus branch id
            .clearBuffer(Buffer.CACHE1)
            .copyArgument(headerDigest, Buffer.CACHE1)
            .copyArgument(lastDigest, Buffer.CACHE1)
            .copyString(EMPTY_SHIELDED_DIGESTS, Buffer.CACHE1)
            .clearBuffer(Buffer.CACHE2)
            .copyString(HexUtil.toHexString("ZcashTxHash_"), Buffer.CACHE2)
            .copyArgument(consensusBranchId, Buffer.CACHE2)
            .advancedHash(ScriptData.getDataBufferAll(Buffer.CACHE1), ScriptData.getDataBufferAll(Buffer.CACHE2),
                Buffer.TRANSACTION, ScriptAssembler.AdvancedHashType.Blake2b256Personal)
            .getScript();
    }

    /**
     * Like getAddressScript, but the destination is converted to exactly 20 bytes (longer: 6B02) instead of copied as
     * is. The address shows only the first 26 bytes of prefix | destination | checksum, so a longer destination would
     * let the host choose the checksum shown and put bytes in the outputs digest that the user never sees.
     */
    private static String getV5AddressScript(boolean isTestnet, ScriptObjectAbstract argOutputScriptType,
        ScriptObjectAbstract argOutputDest) {
        return new ScriptAssembler()
            .switchString(argOutputScriptType, Buffer.CACHE2, !isTestnet ? "1CB8,1CBD" : "1D25,1CBA") // t1,t3:tm,t2
            .baseConvert(argOutputDest, Buffer.CACHE2, 20, ScriptAssembler.binaryCharset, 0)
            .hash(ScriptData.getDataBufferAll(Buffer.CACHE2), Buffer.CACHE2, ScriptAssembler.HashType.DoubleSHA256)
            .baseConvert(ScriptData.getBuffer(Buffer.CACHE2, 0, 26), Buffer.CACHE1, 0, ScriptAssembler.base58Charset,
                ScriptAssembler.zeroInherit)
            .showAddress(ScriptData.getDataBufferAll(Buffer.CACHE1))
            .getScript();
    }

    public static String ZECV5ScriptSignature = Strings.padStart("304502202a197556ad97eed71cb6c7166f08e7fd513e396821506ec34848e04ba774680e022100a7f347ecb608a3e0a1afb344d133e25e0eb67fb25d185d067dc2a4c642a1dd22", 144, '0');

    /**
     * @deprecated The signature of the v4 script; remove with getZECScript.
     */
    @Deprecated
    public static String ZECScriptSignature =Strings.padStart("3046022100eebe7b79978d11b265ceb3977a683df311d7d3a4844fc513a611d8ea20fb6d42022100ea45cb25c9aa448d693ec06334ee47a90f5dfdf84e868198cf957754c4547213", 144, '0');

}
