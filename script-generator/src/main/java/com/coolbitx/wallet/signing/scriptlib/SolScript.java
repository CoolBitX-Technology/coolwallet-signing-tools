package com.coolbitx.wallet.signing.scriptlib;

import com.coolbitx.wallet.signing.utils.Hex;
import com.coolbitx.wallet.signing.utils.HexUtil;
import com.coolbitx.wallet.signing.utils.ScriptArgumentComposer;
import com.coolbitx.wallet.signing.utils.ScriptAssembler;
import com.coolbitx.wallet.signing.utils.ScriptAssembler.HashType;
import com.coolbitx.wallet.signing.utils.ScriptAssembler.SignType;
import com.coolbitx.wallet.signing.utils.ScriptData;
import com.coolbitx.wallet.signing.utils.ScriptData.Buffer;
import com.google.common.base.Strings;

public class SolScript {

    public static void main(String[] args) {
        listAll();
    }

    public static void listAll() {
        System.out.println("Sol transfer: \n" + getTransferScript() + "\n");
        System.out.println("Sol transfer with compute budget: \n" + getTransferWithComputeBudgetScript() + "\n");
        System.out.println("Sol Smart Contract: \n" + getSolSmartScript() + "\n");
        System.out.println("Sol Sign-In: \n" + getSignInScript() + "\n");
        System.out.println("Sol Sign Message: \n" + getSignMessageScript() + "\n");
        System.out.println("Sol transfer spl token-2022: \n" + getTransferSplToken22Script() + "\n");
        System.out.println("Sol transfer spl token-2022 with compute budget: \n" + getTransferSplToken22WithComputeBudgetScript() + "\n");
        System.out.println("Sol create and transfer spl token-2022: \n" + getCreateAndTransferSplToken22Script() + "\n");
        System.out.println("Sol create and transfer spl token-2022 with compute budget: \n" + getCreateAndTransferSplToken22WithComputeBudgetScript() + "\n");
        System.out.println("Sol Delegate: \n" + getDelegateScript() + "\n");
        System.out.println("Sol Undelegate: \n" + getUndelegateScript() + "\n");
        System.out.println(
                "Sol Delegate And Create Account With Seed \n"
                + getDelegateAndCreateAccountWithSeedScript()
                + "\n");
        System.out.println("Sol Withdraw: \n" + getStackingWithdrawScript() + "\n");
    }

    public static String EMPTY_PUBLIC_KEY = Hex.encode("--------------------------------".getBytes());

    public enum TxType {
        TRANSFER("01"),
        TRANSFER_WITH_COMPUTE_BUDGET("03"),
        CREATE_AND_TRANSFER("02"),
        CREATE_AND_TRANSFER_WITH_COMPUTE_BUDGET("04");
        private final String signLabel;

        TxType(String signLabel) {
            this.signLabel = signLabel;
        }

        @Override
        public String toString() {
            return signLabel;
        }
    }

    public static String getTransferScript() {
        return transferScript(TxType.TRANSFER);
    }
    public static String getTransferScriptSignature = Strings.padStart("304502201feef74de887b6d8513f56371cebf5e9d7f05e2ddbef25eb6dd91cd088910018022100d4afd1ea9d5f9ae142cdab5419e8c0974f10651376af03a141a26e49b08691dc", 144, '0');

    public static String getTransferWithComputeBudgetScript() {
        return transferScript(TxType.TRANSFER_WITH_COMPUTE_BUDGET);
    }
    public static String getTransferWithComputeBudgetScriptSignature = Strings.padStart("3044022032851d9ca464de5ae92c063f33ada384fa0dee27d6e3d469197a367ef9cc9cbe0220137ffa77407186912ffbb5fd74529b3818c7e8fa1d4eb13d4a924a44cb7caaf9", 144, '0');

    private static String transferScript(TxType txType) {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptData header = sac.getArgument(3);
        ScriptData keysCount = sac.getArgument(1);
        // SplTxType.TRANSFER
        // to other : [ownerAccount, toAccount, programId]
        // to self  : [ownerAccount, programId]
        // SplTxType.TRANSFER_WITH_COMPUTE_BUDGET
        // to other : [ownerAccount, toAccount, computeBudgetProgramId, programId]
        // to self  : [ownerAccount, computeBudgetProgramId, programId]
        ScriptData publicKey0 = sac.getArgument(32);
        ScriptData publicKey1 = sac.getArgument(32);
        ScriptData publicKey2 = sac.getArgument(32);
        ScriptData publicKey3 = sac.getArgument(32);
        ScriptData publicKey4 = sac.getArgument(32);
        ScriptData publicKey5 = sac.getArgument(32);
        ScriptData recentBlockHash = sac.getArgument(32);
        ScriptData gasPriceProgramIdIndex = null;
        ScriptData gasPriceKeyLength = null;
        ScriptData gasPriceDataLength = null;
        ScriptData gasPriceComputeBudgetInstructionType = null;
        ScriptData gasPrice = null;
        ScriptData gasLimitProgramIdIndex = null;
        ScriptData gasLimitKeyLength = null;
        ScriptData gasLimitDataLength = null;
        ScriptData gasLimitComputeBudgetInstructionType = null;
        ScriptData gasLimit = null;
        if (txType == TxType.TRANSFER_WITH_COMPUTE_BUDGET) {
            gasPriceProgramIdIndex = sac.getArgument(1);
            gasPriceKeyLength = sac.getArgument(1);
            gasPriceDataLength = sac.getArgument(1);
            gasPriceComputeBudgetInstructionType = sac.getArgument(1); // 03 for SetComputeUnitPrice
            gasPrice = sac.getArgument(8);

            gasLimitProgramIdIndex = sac.getArgument(1);
            gasLimitKeyLength = sac.getArgument(1);
            gasLimitDataLength = sac.getArgument(1);
            gasLimitComputeBudgetInstructionType = sac.getArgument(1); // 02 for SetComputeUnitLimit
            gasLimit = sac.getArgument(4);
        }
        ScriptData systemProgramIdIndex = sac.getArgument(1);
        ScriptData keyIndicesLength = sac.getArgument(1);
        ScriptData fromKeyIndex = sac.getArgument(1);
        ScriptData toKeyIndex = sac.getArgument(1);
        ScriptData dataLength = sac.getArgument(1);
        ScriptData instruction = sac.getArgument(4);
        ScriptData amount = sac.getArgument(8);

        ScriptAssembler script = new ScriptAssembler()
                .setCoinType(0x01f5)
                .copyArgument(header)
                .copyArgument(keysCount)
                .copyArgument(publicKey0)
                .copyArgument(publicKey1)
                .ifEqual(publicKey2, EMPTY_PUBLIC_KEY, "", new ScriptAssembler().copyArgument(publicKey2).getScript())
                .ifEqual(publicKey3, EMPTY_PUBLIC_KEY, "", new ScriptAssembler().copyArgument(publicKey3).getScript())
                .ifEqual(publicKey4, EMPTY_PUBLIC_KEY, "", new ScriptAssembler().copyArgument(publicKey4).getScript())
                .ifEqual(publicKey5, EMPTY_PUBLIC_KEY, "", new ScriptAssembler().copyArgument(publicKey5).getScript())
                .copyArgument(recentBlockHash);
        // instructions count
        switch (txType) {
            case TRANSFER:
                script.copyString("01");
                break;
            case TRANSFER_WITH_COMPUTE_BUDGET:
                script.copyString("03");
                break;
            default:
        }
        if (txType == TxType.TRANSFER_WITH_COMPUTE_BUDGET) {
            script
                    //  Compute Budget: Set Compute Unit Price instruction
                    .copyArgument(gasPriceProgramIdIndex)
                    .copyArgument(gasPriceKeyLength)
                    .copyArgument(gasPriceDataLength)
                    .copyArgument(gasPriceComputeBudgetInstructionType)
                    .copyArgument(gasPrice)
                    // Compute Budget: Set Compute Unit Limit instruction
                    .copyArgument(gasLimitProgramIdIndex)
                    .copyArgument(gasLimitKeyLength)
                    .copyArgument(gasLimitDataLength)
                    .copyArgument(gasLimitComputeBudgetInstructionType) // SetComputeUnitLimit
                    .copyArgument(gasLimit);
        }
        script
                // Transfer instruction
                .copyArgument(systemProgramIdIndex)
                .copyArgument(keyIndicesLength)
                .copyArgument(fromKeyIndex)
                .copyArgument(toKeyIndex)
                .copyArgument(dataLength)
                .copyArgument(instruction)
                .copyArgument(amount)
                .showMessage("SOL")
                .ifEqual(
                        toKeyIndex,
                        "00",
                        new ScriptAssembler()
                                .baseConvert(
                                        publicKey0,
                                        Buffer.CACHE2,
                                        0,
                                        ScriptAssembler.base58Charset,
                                        ScriptAssembler.zeroInherit)
                                .showAddress(ScriptData.getDataBufferAll(Buffer.CACHE2))
                                .getScript(),
                        "")
                .ifEqual(
                        toKeyIndex,
                        "01",
                        new ScriptAssembler()
                                .baseConvert(
                                        publicKey1,
                                        Buffer.CACHE2,
                                        0,
                                        ScriptAssembler.base58Charset,
                                        ScriptAssembler.zeroInherit)
                                .showAddress(ScriptData.getDataBufferAll(Buffer.CACHE2))
                                .getScript(),
                        "")
                .clearBuffer(Buffer.CACHE2)
                .baseConvert(
                        amount,
                        Buffer.CACHE1,
                        8,
                        ScriptAssembler.binaryCharset,
                        ScriptAssembler.inLittleEndian)
                .showAmount(ScriptData.getDataBufferAll(Buffer.CACHE1), 9)
                .clearBuffer(Buffer.CACHE1)
                .showPressButton()
                .setHeader(HashType.NONE, SignType.EDDSA)
                .getScript();

        return script.getScript();
    }

    public static String getSolSmartScript() {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptData data = sac.getArgumentAll();

        ScriptAssembler scriptAsb = new ScriptAssembler();
        return scriptAsb
                .setCoinType(0x01f5)
                .copyArgument(data)
                .showMessage("SOL")
                .showWrap("SMART", "")
                .showPressButton()
                .setHeader(HashType.NONE, SignType.EDDSA)
                .getScript();
    }

    public static String getSignInScript() {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptData domainLength = sac.getArgument(1);
        ScriptData domain = sac.getArgumentVariableLength(128);
        ScriptData optionalMessageLength = sac.getArgument(2);
        ScriptData optionalMessage = sac.getArgumentVariableLength(1024);
        ScriptData argAddressPath = sac.getArgument(17);
        //" wants you to sign in with your Solana account:\n"
        String fixedText = "2077616e747320796f7520746f207369676e20696e207769746820796f757220536f6c616e61206163636f756e743a0a";

        ScriptAssembler scriptAsb = new ScriptAssembler();
        return scriptAsb
                .setCoinType(0x01f5)
                .setBufferIntUnsafe(domainLength)
                .copyArgument(domain)
                .copyString(fixedText)
                .derivePublicKey(argAddressPath, Buffer.CACHE2)
                .baseConvert(
                        ScriptData.getDataBufferAll(Buffer.CACHE2),
                        Buffer.TRANSACTION,
                        0,
                        ScriptAssembler.base58Charset,
                        ScriptAssembler.zeroInherit)
                .setBufferIntUnsafe(optionalMessageLength)
                .copyArgument(optionalMessage)
                .showMessage("SOL")
                .showWrap("SIGN IN", "")
                .showPressButton()
                .setHeader(HashType.NONE, SignType.EDDSA)
                .getScript();
    }

    public static String getSignInScriptSignature = Strings.padEnd("FA", 144, '0');

    public static String getSignMessageScript() {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptData data = sac.getArgumentAll();

        ScriptAssembler scriptAsb = new ScriptAssembler();
        return scriptAsb
                .setCoinType(0x01f5)
                .copyArgument(data)
                .showMessage("SOL")
                .showWrap("MESSAGE", "")
                .showPressButton()
                .setHeader(HashType.NONE, SignType.EDDSA)
                .getScript();
    }

    public static String getSignMessageScriptSignature = Strings.padEnd("FA", 144, '0');

    public static String getDelegateScript() {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptData publicKey0 = sac.getArgument(32);
        ScriptData publicKey1 = sac.getArgument(32);
        ScriptData publicKey2 = sac.getArgument(32);
        ScriptData publicKey3 = sac.getArgument(32);
        ScriptData publicKey4 = sac.getArgument(32);
        ScriptData publicKey5 = sac.getArgument(32);
        ScriptData publicKey6 = sac.getArgument(32);
        ScriptData recentBlockhash = sac.getArgument(32);
        ScriptData programIndex = sac.getArgument(1);
        ScriptData keyIndex0 = sac.getArgument(1);
        ScriptData keyIndex1 = sac.getArgument(1);
        ScriptData keyIndexOthers = sac.getArgument(4);
        ScriptData dataLength = sac.getArgument(1);
        ScriptData data = sac.getArgument(4);

        ScriptAssembler scriptAsb = new ScriptAssembler();

        return scriptAsb
                .setCoinType(0x01f5)
                .copyString("01")
                .copyString("00")
                .copyString("05")
                .copyString("07")
                .copyArgument(publicKey0)
                .copyArgument(publicKey1)
                .copyArgument(publicKey2)
                .copyArgument(publicKey3)
                .copyArgument(publicKey4)
                .copyArgument(publicKey5)
                .copyArgument(publicKey6)
                .copyArgument(recentBlockhash)
                // instructions count
                .copyString("01")
                .copyArgument(programIndex)
                // account length
                .copyString("06")
                .copyArgument(keyIndex0)
                .copyArgument(keyIndex1)
                .copyArgument(keyIndexOthers)
                .copyArgument(dataLength)
                .copyArgument(data)
                .showMessage("SOL")
                .showMessage("STAKE")
                .ifEqual(
                        keyIndex1,
                        "02",
                        new ScriptAssembler()
                                .baseConvert(
                                        publicKey2,
                                        Buffer.CACHE2,
                                        0,
                                        ScriptAssembler.base58Charset,
                                        ScriptAssembler.zeroInherit)
                                .showAddress(ScriptData.getDataBufferAll(Buffer.CACHE2))
                                .clearBuffer(Buffer.CACHE2)
                                .getScript(),
                        "")
                .ifEqual(
                        keyIndex1,
                        "03",
                        new ScriptAssembler()
                                .baseConvert(
                                        publicKey3,
                                        Buffer.CACHE2,
                                        0,
                                        ScriptAssembler.base58Charset,
                                        ScriptAssembler.zeroInherit)
                                .showAddress(ScriptData.getDataBufferAll(Buffer.CACHE2))
                                .clearBuffer(Buffer.CACHE2)
                                .getScript(),
                        "")
                .ifEqual(
                        keyIndex1,
                        "04",
                        new ScriptAssembler()
                                .baseConvert(
                                        publicKey4,
                                        Buffer.CACHE2,
                                        0,
                                        ScriptAssembler.base58Charset,
                                        ScriptAssembler.zeroInherit)
                                .showAddress(ScriptData.getDataBufferAll(Buffer.CACHE2))
                                .clearBuffer(Buffer.CACHE2)
                                .getScript(),
                        "")
                .ifEqual(
                        keyIndex1,
                        "05",
                        new ScriptAssembler()
                                .baseConvert(
                                        publicKey5,
                                        Buffer.CACHE2,
                                        0,
                                        ScriptAssembler.base58Charset,
                                        ScriptAssembler.zeroInherit)
                                .showAddress(ScriptData.getDataBufferAll(Buffer.CACHE2))
                                .clearBuffer(Buffer.CACHE2)
                                .getScript(),
                        "")
                .ifEqual(
                        keyIndex1,
                        "06",
                        new ScriptAssembler()
                                .baseConvert(
                                        publicKey6,
                                        Buffer.CACHE2,
                                        0,
                                        ScriptAssembler.base58Charset,
                                        ScriptAssembler.zeroInherit)
                                .showAddress(ScriptData.getDataBufferAll(Buffer.CACHE2))
                                .clearBuffer(Buffer.CACHE2)
                                .getScript(),
                        "")
                .showPressButton()
                .setHeader(HashType.NONE, SignType.EDDSA)
                .getScript();
    }

    public static String getDelegateAndCreateAccountWithSeedScript() {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptData publicKey0 = sac.getArgument(32);
        ScriptData publicKey1 = sac.getArgument(32);
        ScriptData publicKey2 = sac.getArgument(32);
        ScriptData publicKey3 = sac.getArgument(32);
        ScriptData publicKey4 = sac.getArgument(32);
        ScriptData publicKey5 = sac.getArgument(32);
        ScriptData publicKey6 = sac.getArgument(32);
        ScriptData publicKey7 = sac.getArgument(32);
        ScriptData publicKey8 = sac.getArgument(32);
        ScriptData recentBlockhash = sac.getArgument(32);
        ScriptData programIndex0 = sac.getArgument(1);
        ScriptData keyIndices0 = sac.getArgument(2);
        ScriptData data0Length = sac.getArgument(1);
        ScriptData data0Prefix = sac.getArgument(44);
        ScriptData seed = sac.getArgumentRightJustified(32);
        ScriptData lamports = sac.getArgument(8);
        ScriptData data0Postfix = sac.getArgument(40);
        ScriptData programIndex1 = sac.getArgument(1);
        ScriptData keyIndices1 = sac.getArgument(2);
        ScriptData data1Length = sac.getArgument(1);
        ScriptData data1 = sac.getArgument(116);
        ScriptData programIndex2 = sac.getArgument(1);
        ScriptData keyIndex0 = sac.getArgument(1);
        ScriptData keyIndex1 = sac.getArgument(1);
        ScriptData keyIndexOthers = sac.getArgument(4);
        ScriptData data2Length = sac.getArgument(1);
        ScriptData data2 = sac.getArgument(4);

        ScriptAssembler scriptAsb = new ScriptAssembler();

        return scriptAsb
                .setCoinType(0x01f5)
                .copyString("01")
                .copyString("00")
                .copyString("07")
                .copyString("09")
                .copyArgument(publicKey0)
                .copyArgument(publicKey1)
                .copyArgument(publicKey2)
                .copyArgument(publicKey3)
                .copyArgument(publicKey4)
                .copyArgument(publicKey5)
                .copyArgument(publicKey6)
                .copyArgument(publicKey7)
                .copyArgument(publicKey8)
                .copyArgument(recentBlockhash)
                // instructions count
                .copyString("03")
                .copyArgument(programIndex0)
                // account length
                .copyString("02")
                .copyArgument(keyIndices0)
                .copyArgument(data0Length)
                .copyArgument(data0Prefix)
                .copyArgument(seed)
                .copyArgument(lamports)
                .copyArgument(data0Postfix)
                // separator
                .copyArgument(programIndex1)
                // account length
                .copyString("02")
                .copyArgument(keyIndices1)
                .copyArgument(data1Length)
                .copyArgument(data1)
                // separator
                .copyArgument(programIndex2)
                // account length
                .copyString("06")
                .copyArgument(keyIndex0)
                .copyArgument(keyIndex1)
                .copyArgument(keyIndexOthers)
                .copyArgument(data2Length)
                .copyArgument(data2)
                .showMessage("SOL")
                .showMessage("STAKE")
                .ifEqual(
                        keyIndex1,
                        "03",
                        new ScriptAssembler()
                                .baseConvert(
                                        publicKey3,
                                        Buffer.CACHE2,
                                        0,
                                        ScriptAssembler.base58Charset,
                                        ScriptAssembler.zeroInherit)
                                .showAddress(ScriptData.getDataBufferAll(Buffer.CACHE2))
                                .clearBuffer(Buffer.CACHE2)
                                .getScript(),
                        "")
                .ifEqual(
                        keyIndex1,
                        "04",
                        new ScriptAssembler()
                                .baseConvert(
                                        publicKey4,
                                        Buffer.CACHE2,
                                        0,
                                        ScriptAssembler.base58Charset,
                                        ScriptAssembler.zeroInherit)
                                .showAddress(ScriptData.getDataBufferAll(Buffer.CACHE2))
                                .clearBuffer(Buffer.CACHE2)
                                .getScript(),
                        "")
                .ifEqual(
                        keyIndex1,
                        "05",
                        new ScriptAssembler()
                                .baseConvert(
                                        publicKey5,
                                        Buffer.CACHE2,
                                        0,
                                        ScriptAssembler.base58Charset,
                                        ScriptAssembler.zeroInherit)
                                .showAddress(ScriptData.getDataBufferAll(Buffer.CACHE2))
                                .clearBuffer(Buffer.CACHE2)
                                .getScript(),
                        "")
                .ifEqual(
                        keyIndex1,
                        "06",
                        new ScriptAssembler()
                                .baseConvert(
                                        publicKey6,
                                        Buffer.CACHE2,
                                        0,
                                        ScriptAssembler.base58Charset,
                                        ScriptAssembler.zeroInherit)
                                .showAddress(ScriptData.getDataBufferAll(Buffer.CACHE2))
                                .clearBuffer(Buffer.CACHE2)
                                .getScript(),
                        "")
                .ifEqual(
                        keyIndex1,
                        "07",
                        new ScriptAssembler()
                                .baseConvert(
                                        publicKey7,
                                        Buffer.CACHE2,
                                        0,
                                        ScriptAssembler.base58Charset,
                                        ScriptAssembler.zeroInherit)
                                .showAddress(ScriptData.getDataBufferAll(Buffer.CACHE2))
                                .clearBuffer(Buffer.CACHE2)
                                .getScript(),
                        "")
                .ifEqual(
                        keyIndex1,
                        "08",
                        new ScriptAssembler()
                                .baseConvert(
                                        publicKey8,
                                        Buffer.CACHE2,
                                        0,
                                        ScriptAssembler.base58Charset,
                                        ScriptAssembler.zeroInherit)
                                .showAddress(ScriptData.getDataBufferAll(Buffer.CACHE2))
                                .clearBuffer(Buffer.CACHE2)
                                .getScript(),
                        "")
                .baseConvert(
                        lamports,
                        Buffer.CACHE1,
                        8,
                        ScriptAssembler.binaryCharset,
                        ScriptAssembler.inLittleEndian)
                .showAmount(ScriptData.getDataBufferAll(Buffer.CACHE1), 9)
                .clearBuffer(Buffer.CACHE1)
                .showPressButton()
                .setHeader(HashType.NONE, SignType.EDDSA)
                .getScript();
    }

    public static String getTransferSplToken22Script() {
        return splTransferScript(TxType.TRANSFER);
    }
    public static String getTransferSplToken22ScriptSignature = Strings.padStart("3045022054e1f282e197b1288576809da57b3b4578dc3f49bc7f442743d0e18e452f2655022100a98f56d089b72e06886cbb8fa7d914d3784f1286f90790b30bcec4f16944075a", 144, '0');

    public static String getTransferSplToken22WithComputeBudgetScript() {
        return splTransferScript(TxType.TRANSFER_WITH_COMPUTE_BUDGET);
    }
    public static String getTransferSplToken22WithComputeBudgetScriptSignature = Strings.padStart("3045022100b1d99c57f0132e26550c5c0a4a6a353c11601d8b7fa84ad5505d5f292791c27f02204827c2e8fcbdd75cd4f41dfceefed7efc58efca2aae17987bcb342e8270c48e0", 144, '0');

    public static String getCreateAndTransferSplToken22Script() {
        return splTransferScript(TxType.CREATE_AND_TRANSFER);
    }
    public static String getCreateAndTransferSplTokenScriptSignature = Strings.padStart("30460221008c46b69d4255c1ce52b04e79789d601325971dc81aaa0848b121e0192107b768022100df369fd9fb65f9580cce98cee7f09f46a24c1f540b9a43c85ae457d27c161f68", 144, '0');

    public static String getCreateAndTransferSplToken22WithComputeBudgetScript() {
        return splTransferScript(TxType.CREATE_AND_TRANSFER_WITH_COMPUTE_BUDGET);
    }
    public static String getCreateAndTransferSplToken22WithComputeBudgetScriptSignature = Strings.padStart("3046022100a61bb0137dc6c5a07ab97f1e3ea0df59ff6234c5deb54dda9b90d50e0439d9c5022100c34d56584dc02680c155f98d8165455273bba350df54fbd752f1b565e95f021d", 144, '0');

    private static String splTransferScript(TxType txType) {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptData keysCount = sac.getArgument(1);
        // SplTxType.TRANSFER
        // to other : [ownerAccount, toAssociateAccount, fromAssociateAccount, tokenAccount, tokenProgramId]
        // to self  : [ownerAccount, fromAssociateAccount, tokenAccount, tokenProgramId]
        // SplTxType.TRANSFER_WITH_COMPUTE_BUDGET
        // to other : [ownerAccount, toAssociateAccount, fromAssociateAccount, computeBudgetProgramId, tokenAccount, tokenProgramId]
        // to self  : [ownerAccount, fromAssociateAccount, computeBudgetProgramId, tokenAccount, tokenProgramId]
        ScriptData publicKey0 = sac.getArgument(32);
        ScriptData publicKey1 = sac.getArgument(32);
        ScriptData publicKey2 = sac.getArgument(32);
        ScriptData publicKey3 = sac.getArgument(32);
        ScriptData publicKey4 = sac.getArgument(32);
        ScriptData publicKey5 = sac.getArgument(32);

        ScriptData publicKey6 = null;
        ScriptData publicKey7 = null;
        ScriptData publicKey8 = null;
        if (txType == TxType.CREATE_AND_TRANSFER || txType == TxType.CREATE_AND_TRANSFER_WITH_COMPUTE_BUDGET) {
            publicKey6 = sac.getArgument(32);
            publicKey7 = sac.getArgument(32);
            publicKey8 = sac.getArgument(32);
        }
        ScriptData recentBlockHash = sac.getArgument(32);

        ScriptData createAssociatedProgramIdIndex = null;
        ScriptData createAssociatedKeyIndicesLength = null;
        ScriptData createAssociatedKeyIndices = null;
        ScriptData createAssociatedDataLength = null;

        if (txType == TxType.CREATE_AND_TRANSFER || txType == TxType.CREATE_AND_TRANSFER_WITH_COMPUTE_BUDGET) {
            createAssociatedProgramIdIndex = sac.getArgument(1);
            createAssociatedKeyIndicesLength = sac.getArgument(1);
            createAssociatedKeyIndices = sac.getArgument(6);
            createAssociatedDataLength = sac.getArgument(1);
        }

        ScriptData gasPriceProgramIdIndex = null;
        ScriptData gasPriceKeyLength = null;
        ScriptData gasPriceDataLength = null;
        ScriptData gasPriceComputeBudgetInstructionType = null;
        ScriptData gasPrice = null;
        ScriptData gasLimitProgramIdIndex = null;
        ScriptData gasLimitKeyLength = null;
        ScriptData gasLimitDataLength = null;
        ScriptData gasLimitComputeBudgetInstructionType = null;
        ScriptData gasLimit = null;
        if (txType == TxType.TRANSFER_WITH_COMPUTE_BUDGET || txType == TxType.CREATE_AND_TRANSFER_WITH_COMPUTE_BUDGET) {
            gasPriceProgramIdIndex = sac.getArgument(1);
            gasPriceKeyLength = sac.getArgument(1);
            gasPriceDataLength = sac.getArgument(1);
            gasPriceComputeBudgetInstructionType = sac.getArgument(1); // 03 for SetComputeUnitPrice
            gasPrice = sac.getArgument(8);

            gasLimitProgramIdIndex = sac.getArgument(1);
            gasLimitKeyLength = sac.getArgument(1);
            gasLimitDataLength = sac.getArgument(1);
            gasLimitComputeBudgetInstructionType = sac.getArgument(1); // 02 for SetComputeUnitLimit
            gasLimit = sac.getArgument(4);
        }
        ScriptData tokenProgramIdIndex = sac.getArgument(1);
        ScriptData keyIndicesLength = sac.getArgument(1);
        ScriptData keyIndices = sac.getArgument(4); // [from token address, contract address, to token address, owner address]
        ScriptData dataLength = sac.getArgument(1);
        ScriptData tokenInstruction = sac.getArgument(1); // 0C for TransferChecked
        ScriptData tokenAmount = sac.getArgument(8);
        ScriptData customTokenDecimals = sac.getArgument(1);

        ScriptData tokenInfo = sac.getArgumentUnion(0, 41);
        ScriptData tokenDecimals = sac.getArgument(1);
        ScriptData tokenNameLength = sac.getArgument(1);
        ScriptData tokenName = sac.getArgumentVariableLength(7);
        ScriptData tokenAddr = sac.getArgument(32);
        ScriptData tokenSign = sac.getArgument(72);

        ScriptAssembler script = new ScriptAssembler()
                .setCoinType(0x01f5)
                //numRequiredSignatures
                .copyString("01")
                //numReadonlySignedAccounts
                .copyString("00");
        //numReadonlyUnsignedAccounts
        switch (txType) {
            case TRANSFER:
                script.copyString("02");
                break;
            case TRANSFER_WITH_COMPUTE_BUDGET:
                script.copyString("03");
                break;
            case CREATE_AND_TRANSFER:
                script.copyString("05");
                break;
            case CREATE_AND_TRANSFER_WITH_COMPUTE_BUDGET:
                script.copyString("06");
                break;
            default:
                break;
        }
        script
                .copyArgument(keysCount);
        // transfer token to other [ownerAccount, toAssociateAccount, fromAssociateAccount, tokenAccount, tokenProgramId]
        // transfer token to self  [ownerAccount, fromAssociateAccount, tokenAccount, tokenProgramId], toAssociateAccount = fromAssociateAccount
        // transfer token with compute budget to other [ownerAccount, toAssociateAccount, fromAssociateAccount, computeBudgetProgramId, tokenAccount, tokenProgramId]
        // transfer token with compute budget to self  fromAssociateAccount, computeBudgetProgramId, tokenAccount, tokenProgramId], toAssociateAccount = fromAssociateAccount
        if (txType == TxType.TRANSFER || txType == TxType.TRANSFER_WITH_COMPUTE_BUDGET) {
            script
                    .copyArgument(publicKey0)
                    .copyArgument(publicKey1)
                    .copyArgument(publicKey2)
                    .copyArgument(publicKey3)
                    .ifEqual(publicKey4, EMPTY_PUBLIC_KEY, "", new ScriptAssembler().copyArgument(publicKey4).getScript())
                    .ifEqual(publicKey5, EMPTY_PUBLIC_KEY, "", new ScriptAssembler().copyArgument(publicKey5).getScript());
        }
        if (txType == TxType.CREATE_AND_TRANSFER || txType == TxType.CREATE_AND_TRANSFER_WITH_COMPUTE_BUDGET) {
            script
                    .copyArgument(publicKey0)
                    .copyArgument(publicKey1)
                    .copyArgument(publicKey2)
                    .copyArgument(publicKey3)
                    .copyArgument(publicKey4)
                    .copyArgument(publicKey5)
                    .copyArgument(publicKey6)
                    .copyArgument(publicKey7)
                    .ifEqual(publicKey8, EMPTY_PUBLIC_KEY, "", new ScriptAssembler().copyArgument(publicKey8).getScript());
        }

        script.copyArgument(recentBlockHash);
        // instructions count
        switch (txType) {
            case TRANSFER:
                script.copyString("01");
                break;
            case CREATE_AND_TRANSFER:
                script.copyString("02");
                break;
            case TRANSFER_WITH_COMPUTE_BUDGET:
                script.copyString("03");
                break;
            case CREATE_AND_TRANSFER_WITH_COMPUTE_BUDGET:
                script.copyString("04");
                break;
            default:
        }

        if (txType == TxType.CREATE_AND_TRANSFER || txType == TxType.CREATE_AND_TRANSFER_WITH_COMPUTE_BUDGET) {
            script
                    // Create Associated Account instruction
                    .copyArgument(createAssociatedProgramIdIndex)
                    .copyArgument(createAssociatedKeyIndicesLength)
                    .copyArgument(createAssociatedKeyIndices)
                    .copyArgument(createAssociatedDataLength);
        }
        if (txType == TxType.TRANSFER_WITH_COMPUTE_BUDGET || txType == TxType.CREATE_AND_TRANSFER_WITH_COMPUTE_BUDGET) {
            script
                    //  Compute Budget: Set Compute Unit Price instruction
                    .copyArgument(gasPriceProgramIdIndex)
                    .copyArgument(gasPriceKeyLength)
                    .copyArgument(gasPriceDataLength)
                    .copyArgument(gasPriceComputeBudgetInstructionType)
                    .copyArgument(gasPrice)
                    // Compute Budget: Set Compute Unit Limit instruction
                    .copyArgument(gasLimitProgramIdIndex)
                    .copyArgument(gasLimitKeyLength)
                    .copyArgument(gasLimitDataLength)
                    .copyArgument(gasLimitComputeBudgetInstructionType) // SetComputeUnitLimit
                    .copyArgument(gasLimit);
        }
        script
                // Token transfer instruction
                .copyArgument(tokenProgramIdIndex)
                .copyArgument(keyIndicesLength)
                .copyArgument(keyIndices)
                .copyArgument(dataLength)
                .copyArgument(tokenInstruction)
                .copyArgument(tokenAmount)
                .copyArgument(customTokenDecimals);

        script
                .showMessage("SOL")
                .ifSigned(
                        tokenInfo,
                        tokenSign,
                        "",
                        new ScriptAssembler()
                                .copyString(HexUtil.toHexString("@"), Buffer.CACHE2)
                                .getScript())
                .setBufferInt(tokenNameLength, 1, 7)
                .copyArgument(tokenName, Buffer.CACHE2)
                .showMessage(ScriptData.getDataBufferAll(Buffer.CACHE2))
                .clearBuffer(Buffer.CACHE2)
                .baseConvert(publicKey1,
                        Buffer.CACHE2,
                        0,
                        ScriptAssembler.base58Charset,
                        ScriptAssembler.zeroInherit)
                .showAddress(ScriptData.getDataBufferAll(Buffer.CACHE2))
                .clearBuffer(Buffer.CACHE2)
                .baseConvert(
                        tokenAmount,
                        Buffer.CACHE1,
                        8,
                        ScriptAssembler.binaryCharset,
                        ScriptAssembler.inLittleEndian)
                .setBufferInt(tokenDecimals, 0, 20)
                .showAmount(ScriptData.getDataBufferAll(Buffer.CACHE1), ScriptData.bufInt)
                .clearBuffer(Buffer.CACHE1)
                .showPressButton()
                .setHeader(HashType.NONE, SignType.EDDSA);
        return script.getScript();
    }

    public static String getUndelegateScript() {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptData publicKey0 = sac.getArgument(32);
        ScriptData publicKey1 = sac.getArgument(32);
        ScriptData publicKey2 = sac.getArgument(32);
        ScriptData publicKey3 = sac.getArgument(32);
        ScriptData recentBlockhash = sac.getArgument(32);
        ScriptData programIdIndex = sac.getArgument(1);
        ScriptData keyIndex0 = sac.getArgument(1);
        ScriptData keyIndex1 = sac.getArgument(1);
        ScriptData keyIndex2 = sac.getArgument(1);
        ScriptAssembler scriptAsb = new ScriptAssembler();

        return scriptAsb
                .copyString("01")
                .copyString("00")
                .copyString("02")
                .copyString("04")
                .copyArgument(publicKey0)
                .copyArgument(publicKey1)
                .copyArgument(publicKey2)
                .copyArgument(publicKey3)
                .copyArgument(recentBlockhash)
                // instruction count
                .copyString("01")
                .copyArgument(programIdIndex)
                // accounts key count
                .copyString("03")
                .copyArgument(keyIndex0)
                .copyArgument(keyIndex1)
                .copyArgument(keyIndex2)
                // data length
                .copyString("04")
                .copyString("05000000")
                .showMessage("SOL")
                .showMessage("UnDel")
                .baseConvert(
                        publicKey0,
                        Buffer.CACHE2,
                        0,
                        ScriptAssembler.base58Charset,
                        ScriptAssembler.zeroInherit)
                .showAddress(ScriptData.getDataBufferAll(Buffer.CACHE2))
                .showPressButton()
                .clearBuffer(Buffer.CACHE2)
                .setHeader(HashType.NONE, SignType.EDDSA)
                .getScript();
    }

    public static String getStackingWithdrawScript() {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptData keysCount = sac.getArgument(1);
        ScriptData authorizedPubkey = sac.getArgument(32);
        ScriptData publicKey1 = sac.getArgument(32);
        ScriptData publicKey2 = sac.getArgument(32);
        ScriptData publicKey3 = sac.getArgument(32);
        ScriptData publicKey4 = sac.getArgument(32);
        ScriptData publicKey5 = sac.getArgument(32);
        ScriptData recentBlockHash = sac.getArgument(32);
        ScriptData programIdIndex = sac.getArgument(1);
        ScriptData keyIndex0 = sac.getArgument(1);
        ScriptData keyIndex1 = sac.getArgument(1);
        ScriptData remainKeyIndices = sac.getArgument(3);
        ScriptData dataLength = sac.getArgument(1);
        ScriptData instruction = sac.getArgument(4);
        ScriptData data = sac.getArgument(8);

        ScriptAssembler scriptAsb = new ScriptAssembler();

        return scriptAsb
                .setCoinType(0x01f5)
                // numRequiredSignatures
                .copyString("01")
                // numReadonlySignedAccounts
                .copyString("00")
                // numReadonlyUnsignedAccounts
                .copyString("03")
                // keyCount
                .copyArgument(keysCount)
                .copyArgument(authorizedPubkey)
                .copyArgument(publicKey1)
                .copyArgument(publicKey2)
                .copyArgument(publicKey3)
                .copyArgument(publicKey4)
                .ifEqual(keysCount, "06", new ScriptAssembler().copyArgument(publicKey5).getScript(), "")
                .copyArgument(recentBlockHash)
                // instruction count
                .copyString("01")
                .copyArgument(programIdIndex)
                .copyString("05")
                .copyArgument(keyIndex0)
                .copyArgument(keyIndex1)
                .copyArgument(remainKeyIndices)
                .copyArgument(dataLength)
                .copyArgument(instruction)
                .copyArgument(data)
                .showMessage("SOL")
                .showMessage("Reward")
                .ifEqual(
                        keyIndex1,
                        "00",
                        new ScriptAssembler()
                                .baseConvert(
                                        authorizedPubkey,
                                        Buffer.CACHE2,
                                        0,
                                        ScriptAssembler.base58Charset,
                                        ScriptAssembler.zeroInherit)
                                .showAddress(ScriptData.getDataBufferAll(Buffer.CACHE2))
                                .clearBuffer(Buffer.CACHE2)
                                .getScript(),
                        "")
                .ifEqual(
                        keyIndex1,
                        "01",
                        new ScriptAssembler()
                                .baseConvert(
                                        publicKey1,
                                        Buffer.CACHE2,
                                        0,
                                        ScriptAssembler.base58Charset,
                                        ScriptAssembler.zeroInherit)
                                .showAddress(ScriptData.getDataBufferAll(Buffer.CACHE2))
                                .clearBuffer(Buffer.CACHE2)
                                .getScript(),
                        "")
                .ifEqual(
                        keyIndex1,
                        "02",
                        new ScriptAssembler()
                                .baseConvert(
                                        publicKey2,
                                        Buffer.CACHE2,
                                        0,
                                        ScriptAssembler.base58Charset,
                                        ScriptAssembler.zeroInherit)
                                .showAddress(ScriptData.getDataBufferAll(Buffer.CACHE2))
                                .clearBuffer(Buffer.CACHE2)
                                .getScript(),
                        "")
                .baseConvert(
                        data, Buffer.CACHE1, 8, ScriptAssembler.binaryCharset, ScriptAssembler.inLittleEndian)
                .showAmount(ScriptData.getDataBufferAll(Buffer.CACHE1), 9)
                .clearBuffer(Buffer.CACHE1)
                .showPressButton()
                .setHeader(HashType.NONE, SignType.EDDSA)
                .getScript();
    }
}
