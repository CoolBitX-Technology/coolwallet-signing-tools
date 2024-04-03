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
        System.out.println("Sol Delegate And Create Account With Seed \n" + getDelegateAndCreateAccountWithSeedScript() + "\n");
        System.out.println("Sol Undelegate: \n" + getUndelegateScript() + "\n");
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

    public enum StakeType {
        DELEGATE,
        DELEGATE_AND_CREATE,
        UNDELEGATE,
        WITHDRAW;
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

    public static String getTransferSplToken22Script() {
        return splTransferScript(TxType.TRANSFER);
    }
    public static String getTransferSplToken22ScriptSignature = Strings.padStart("30450221008ca60fc77d2ab62548366000044c4972ae2f6cca5716472bb78483cc5064cb7b022075ad4281dc8cded2139b5cd414305d10c55c2625c9dae47ec0c5a113e8752551", 144, '0');

    public static String getTransferSplToken22WithComputeBudgetScript() {
        return splTransferScript(TxType.TRANSFER_WITH_COMPUTE_BUDGET);
    }
    public static String getTransferSplToken22WithComputeBudgetScriptSignature = Strings.padStart("3046022100f7bcc7b763b598a4aa43ace333e3416b45307190b5e9bcff45c9415d3caf4ee8022100bb990f958b7c12f13bd25b8ad5f5cd1a49cd89d4cae9f45b85adff699f747285", 144, '0');

    public static String getCreateAndTransferSplToken22Script() {
        return splTransferScript(TxType.CREATE_AND_TRANSFER);
    }
    public static String getCreateAndTransferSplTokenScriptSignature = Strings.padStart("30460221009d7b2401e110fd30b7db32f60bbc0aefd1d2ff71a75f470c005348f59ce113080221009dd281a9dd7951e93013787694433dc74104a4dd663cf7c5d8c2a6be4335fcdf", 144, '0');

    public static String getCreateAndTransferSplToken22WithComputeBudgetScript() {
        return splTransferScript(TxType.CREATE_AND_TRANSFER_WITH_COMPUTE_BUDGET);
    }
    public static String getCreateAndTransferSplToken22WithComputeBudgetScriptSignature = Strings.padStart("304402205b763b3e3dc745dadda5f6fbddd2af9990bcc0fd7b32a1ab97d4af14d31ae6230220203177f933a507026a145c796ea8b4b1fe221eda157f0dda28bdb7290da9ee53", 144, '0');

    private static String splTransferScript(TxType txType) {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptData header = sac.getArgument(3);
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
                .copyArgument(header) // numRequiredSignatures(1B) numReadonlySignedAccounts(1B) numReadonlyUnsignedAccounts(1B)
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

    public static String getDelegateAndCreateAccountWithSeedScript() {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptData header = sac.getArgument(3);
        ScriptData keysCount = sac.getArgument(1);
        ScriptData publicKey0 = sac.getArgument(32);
        ScriptData publicKey1 = sac.getArgument(32);
        ScriptData publicKey2 = sac.getArgument(32);
        ScriptData publicKey3 = sac.getArgument(32);
        ScriptData publicKey4 = sac.getArgument(32);
        ScriptData publicKey5 = sac.getArgument(32);
        ScriptData publicKey6 = sac.getArgument(32);
        ScriptData publicKey7 = sac.getArgument(32);
        ScriptData publicKey8 = sac.getArgument(32);
        ScriptData publicKey9 = sac.getArgument(32);
        ScriptData recentBlockhash = sac.getArgument(32);
        ScriptData instructionsCount = sac.getArgument(1);

        ScriptData gasPriceProgramIdIndex = sac.getArgument(1);
        ScriptData gasPriceKeyLength = sac.getArgument(1);
        ScriptData gasPriceDataLength = sac.getArgument(1);
        ScriptData gasPriceComputeBudgetInstructionType = sac.getArgument(1); // 03 for SetComputeUnitPrice
        ScriptData gasPrice = sac.getArgument(8);

        ScriptData gasLimitProgramIdIndex = sac.getArgument(1);
        ScriptData gasLimitKeyLength = sac.getArgument(1);
        ScriptData gasLimitDataLength = sac.getArgument(1);
        ScriptData gasLimitComputeBudgetInstructionType = sac.getArgument(1); // 02 for SetComputeUnitLimit
        ScriptData gasLimit = sac.getArgument(4);

        ScriptData createAccountProgramIdIndex = sac.getArgument(1);
        ScriptData createAccountKeyIndicesLength = sac.getArgument(1);
        ScriptData createAccountKeyIndices = sac.getArgument(2);
        ScriptData createAccountDataLength = sac.getArgument(1);
        ScriptData createAccountInstruction = sac.getArgument(4);
        ScriptData createAccountBase = sac.getArgument(32);
        ScriptData createAccountSeedLength = sac.getArgument(4);
        ScriptData createAccountSeedPaddingLength = sac.getArgument(4);
        ScriptData createAccountSeed = sac.getArgumentRightJustified(32);
        ScriptData createAccountLamports = sac.getArgument(8);
        ScriptData createAccountSpace = sac.getArgument(8);
        ScriptData createAccountProgramId = sac.getArgument(32);
        
        ScriptData initializeProgramIdIndex = sac.getArgument(1);
        ScriptData initializeKeyIndicesLength = sac.getArgument(1);
        ScriptData initializeKeyIndices = sac.getArgument(2);
        ScriptData initializeDataLength = sac.getArgument(1);
        ScriptData initializeData = sac.getArgument(116);
        
        ScriptData delegateProgramIdIndex = sac.getArgument(1);
        ScriptData delegateKeyIndicesLength = sac.getArgument(1);
        ScriptData delegateKeyIndex0 = sac.getArgument(1);
        ScriptData delegateKeyIndex1 = sac.getArgument(1);
        ScriptData delegateKeyIndexOthers = sac.getArgument(4);
        ScriptData delegateDataLength = sac.getArgument(1);
        ScriptData delegateData = sac.getArgument(4);

        ScriptAssembler scriptAsb = new ScriptAssembler();

        return scriptAsb
                .setCoinType(0x01f5)
                .copyArgument(header)
                .copyArgument(keysCount)
                .copyArgument(publicKey0)
                .copyArgument(publicKey1)
                .copyArgument(publicKey2)
                .copyArgument(publicKey3)
                .copyArgument(publicKey4)
                .copyArgument(publicKey5)
                .copyArgument(publicKey6)
                .copyArgument(publicKey7)
                .copyArgument(publicKey8)
                .ifEqual(publicKey9, EMPTY_PUBLIC_KEY, "", new ScriptAssembler().copyArgument(publicKey9).getScript())
                .copyArgument(recentBlockhash)
                .copyArgument(instructionsCount)
                .ifEqual(gasPriceDataLength,
                        "00",
                        "", new ScriptAssembler()
                                .copyArgument(gasPriceProgramIdIndex)
                                .copyArgument(gasPriceKeyLength)
                                .copyArgument(gasPriceDataLength)
                                .copyArgument(gasPriceComputeBudgetInstructionType)
                                .copyArgument(gasPrice).getScript()
                )
                .ifEqual(gasLimitDataLength,
                        "00",
                        "", new ScriptAssembler()
                                .copyArgument(gasLimitProgramIdIndex)
                                .copyArgument(gasLimitKeyLength)
                                .copyArgument(gasLimitDataLength)
                                .copyArgument(gasLimitComputeBudgetInstructionType)
                                .copyArgument(gasLimit).getScript()
                )
                .copyArgument(createAccountProgramIdIndex)
                .copyArgument(createAccountKeyIndicesLength)
                .copyArgument(createAccountKeyIndices)
                .copyArgument(createAccountDataLength)
                .copyArgument(createAccountInstruction) // 03000000
                .copyArgument(createAccountBase)
                .copyArgument(createAccountSeedLength)
                .copyArgument(createAccountSeedPaddingLength)
                .copyArgument(createAccountSeed)
                .copyArgument(createAccountLamports)
                .copyArgument(createAccountSpace)
                .copyArgument(createAccountProgramId)
                .copyArgument(initializeProgramIdIndex)
                .copyArgument(initializeKeyIndicesLength)
                .copyArgument(initializeKeyIndices)
                .copyArgument(initializeDataLength)
                .copyArgument(initializeData)
                .copyArgument(delegateProgramIdIndex)
                .copyArgument(delegateKeyIndicesLength)
                .copyArgument(delegateKeyIndex0)
                .copyArgument(delegateKeyIndex1)
                .copyArgument(delegateKeyIndexOthers)
                .copyArgument(delegateDataLength)
                .copyArgument(delegateData) // 02000000
                .showMessage("SOL")
                .showMessage("STAKE")
                .baseConvert(
                        publicKey3,
                        Buffer.CACHE2,
                        0,
                        ScriptAssembler.base58Charset,
                        ScriptAssembler.zeroInherit)
                .showAddress(ScriptData.getDataBufferAll(Buffer.CACHE2))
                .clearBuffer(Buffer.CACHE2)
                .baseConvert(
                        createAccountLamports,
                        Buffer.CACHE1,
                        8,
                        ScriptAssembler.binaryCharset,
                        ScriptAssembler.inLittleEndian)
                .showAmount(ScriptData.getDataBufferAll(Buffer.CACHE1), 9)
                .showPressButton()
                .clearBuffer(Buffer.CACHE2)
                .setHeader(HashType.NONE, SignType.EDDSA)
                .getScript();
    }

    public static String getDelegateAndCreateAccountWithSeedScriptSignature = Strings.padStart("3044022026ea0b1c5ab42fc52fa4542db17b9a0a92b42ffc941a53b82ec8cedd7fa4fd1d02200b2edd468c0d6761add53a597774cdc1c5d1599b186a1ade4b2458c559771411", 144, '0');

     public static String getUndelegateScript() {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptData header = sac.getArgument(3);
        ScriptData keysCount = sac.getArgument(1);
        ScriptData publicKey0 = sac.getArgument(32);
        ScriptData publicKey1 = sac.getArgument(32);
        ScriptData publicKey2 = sac.getArgument(32);
        ScriptData publicKey3 = sac.getArgument(32);
        ScriptData publicKey4 = sac.getArgument(32);
        ScriptData recentBlockhash = sac.getArgument(32);
        ScriptData instructionsCount = sac.getArgument(1);

        ScriptData gasPriceProgramIdIndex = sac.getArgument(1);
        ScriptData gasPriceKeyLength = sac.getArgument(1);
        ScriptData gasPriceDataLength = sac.getArgument(1);
        ScriptData gasPriceComputeBudgetInstructionType = sac.getArgument(1); // 03 for SetComputeUnitPrice
        ScriptData gasPrice = sac.getArgument(8);

        ScriptData gasLimitProgramIdIndex = sac.getArgument(1);
        ScriptData gasLimitKeyLength = sac.getArgument(1);
        ScriptData gasLimitDataLength = sac.getArgument(1);
        ScriptData gasLimitComputeBudgetInstructionType = sac.getArgument(1); // 02 for SetComputeUnitLimit
        ScriptData gasLimit = sac.getArgument(4);

        ScriptData programIdIndex = sac.getArgument(1);
        ScriptData keyIndicesLength = sac.getArgument(1);
        ScriptData keyIndices = sac.getArgument(3);
        ScriptData dataLength = sac.getArgument(1);
        ScriptData data = sac.getArgument(4);

        ScriptAssembler scriptAsb = new ScriptAssembler();

        return scriptAsb
                .setCoinType(0x01f5)
                .copyArgument(header)
                .copyArgument(keysCount)
                .copyArgument(publicKey0)
                .copyArgument(publicKey1)
                .copyArgument(publicKey2)
                .copyArgument(publicKey3)
                .ifEqual(publicKey4, EMPTY_PUBLIC_KEY, "", new ScriptAssembler().copyArgument(publicKey4).getScript())
                .copyArgument(recentBlockhash)
                .copyArgument(instructionsCount)
                .ifEqual(gasPriceDataLength,
                        "00",
                        "", new ScriptAssembler()
                                .copyArgument(gasPriceProgramIdIndex)
                                .copyArgument(gasPriceKeyLength)
                                .copyArgument(gasPriceDataLength)
                                .copyArgument(gasPriceComputeBudgetInstructionType)
                                .copyArgument(gasPrice).getScript()
                )
                .ifEqual(gasLimitDataLength,
                        "00",
                        "", new ScriptAssembler()
                                .copyArgument(gasLimitProgramIdIndex)
                                .copyArgument(gasLimitKeyLength)
                                .copyArgument(gasLimitDataLength)
                                .copyArgument(gasLimitComputeBudgetInstructionType)
                                .copyArgument(gasLimit).getScript()
                )
                .copyArgument(programIdIndex)
                .copyArgument(keyIndicesLength)
                .copyArgument(keyIndices)
                .copyArgument(dataLength)
                .copyArgument(data) // 05000000
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

    public static String getUndelegateScriptSignature = Strings.padStart("3044022040054884f42bb7257f16c9cc121f15d2466ce8d9efb5e83f7b96973ed29d1f260220328d320b2ce9339123d923e3a2d81ffb6382a3588da19e93d8795d0265dfe561", 144, '0');

    public static String getStackingWithdrawScript() {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptData header = sac.getArgument(3);
        ScriptData keysCount = sac.getArgument(1);
        ScriptData pubkeyKey0 = sac.getArgument(32);
        ScriptData publicKey1 = sac.getArgument(32);
        ScriptData publicKey2 = sac.getArgument(32);
        ScriptData publicKey3 = sac.getArgument(32);
        ScriptData publicKey4 = sac.getArgument(32);
        ScriptData publicKey5 = sac.getArgument(32);
        ScriptData publicKey6 = sac.getArgument(32);
        ScriptData recentBlockHash = sac.getArgument(32);
        ScriptData instructionsCount = sac.getArgument(1);

        ScriptData gasPriceProgramIdIndex = sac.getArgument(1);
        ScriptData gasPriceKeyLength = sac.getArgument(1);
        ScriptData gasPriceDataLength = sac.getArgument(1);
        ScriptData gasPriceComputeBudgetInstructionType = sac.getArgument(1); // 03 for SetComputeUnitPrice
        ScriptData gasPrice = sac.getArgument(8);

        ScriptData gasLimitProgramIdIndex = sac.getArgument(1);
        ScriptData gasLimitKeyLength = sac.getArgument(1);
        ScriptData gasLimitDataLength = sac.getArgument(1);
        ScriptData gasLimitComputeBudgetInstructionType = sac.getArgument(1); // 02 for SetComputeUnitLimit
        ScriptData gasLimit = sac.getArgument(4);

        ScriptData withdrawProgramIdIndex = sac.getArgument(1);
        ScriptData withdrawKeyIndicesLength = sac.getArgument(1);
        ScriptData withdrawKeyIndex0 = sac.getArgument(1);
        ScriptData withdrawKeyIndex1 = sac.getArgument(1);
        ScriptData withdrawRemainKeyIndices = sac.getArgument(3);
        ScriptData withdrawDataLength = sac.getArgument(1);
        ScriptData withdrawInstruction = sac.getArgument(4);
        ScriptData withdrawData = sac.getArgument(8);

        ScriptAssembler scriptAsb = new ScriptAssembler();

        return scriptAsb
                .setCoinType(0x01f5)
                .copyArgument(header)
                .copyArgument(keysCount)
                .copyArgument(pubkeyKey0)
                .copyArgument(publicKey1)
                .copyArgument(publicKey2)
                .copyArgument(publicKey3)
                .copyArgument(publicKey4)
                .ifEqual(publicKey5, EMPTY_PUBLIC_KEY, "", new ScriptAssembler().copyArgument(publicKey5).getScript())
                .ifEqual(publicKey6, EMPTY_PUBLIC_KEY, "", new ScriptAssembler().copyArgument(publicKey6).getScript())
                .copyArgument(recentBlockHash)
                .copyArgument(instructionsCount)
                .ifEqual(gasPriceDataLength,
                        "00",
                        "", new ScriptAssembler()
                                .copyArgument(gasPriceProgramIdIndex)
                                .copyArgument(gasPriceKeyLength)
                                .copyArgument(gasPriceDataLength)
                                .copyArgument(gasPriceComputeBudgetInstructionType)
                                .copyArgument(gasPrice).getScript()
                )
                .ifEqual(gasLimitDataLength,
                        "00",
                        "", new ScriptAssembler()
                                .copyArgument(gasLimitProgramIdIndex)
                                .copyArgument(gasLimitKeyLength)
                                .copyArgument(gasLimitDataLength)
                                .copyArgument(gasLimitComputeBudgetInstructionType)
                                .copyArgument(gasLimit).getScript()
                )
                .copyArgument(withdrawProgramIdIndex)
                .copyArgument(withdrawKeyIndicesLength)
                .copyArgument(withdrawKeyIndex0)
                .copyArgument(withdrawKeyIndex1)
                .copyArgument(withdrawRemainKeyIndices)
                .copyArgument(withdrawDataLength)
                .copyArgument(withdrawInstruction) // 04000000
                .copyArgument(withdrawData)
                .showMessage("SOL")
                .showMessage("Reward")
                .ifEqual(withdrawKeyIndex1,
                        "00",
                        new ScriptAssembler()
                                .baseConvert(pubkeyKey0,
                                        Buffer.CACHE2,
                                        0,
                                        ScriptAssembler.base58Charset,
                                        ScriptAssembler.zeroInherit)
                                .showAddress(ScriptData.getDataBufferAll(Buffer.CACHE2))
                                .clearBuffer(Buffer.CACHE2)
                                .getScript(),
                        "")
                .ifEqual(
                        withdrawKeyIndex1,
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
                        withdrawKeyIndex1,
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
                        withdrawData, Buffer.CACHE1, 8, ScriptAssembler.binaryCharset, ScriptAssembler.inLittleEndian)
                .showAmount(ScriptData.getDataBufferAll(Buffer.CACHE1), 9)
                .clearBuffer(Buffer.CACHE1)
                .showPressButton()
                .setHeader(HashType.NONE, SignType.EDDSA)
                .getScript();
    }
    public static String getStackingWithdrawScriptSignature = Strings.padStart("304402200329a67f8ce88f59b5dc7514488c07434fe0dc05cf9e25f151d964fff052eed102204ef7f0b5ae72bf209f093968bbaa48aeda3216103f04667cc6481fd49d82defc", 144, '0');
}
