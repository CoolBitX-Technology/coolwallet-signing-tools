package com.coolbitx.wallet.signing.scriptlib;

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
        System.out.println("Sol transfer to self: \n" + getTransferToSelfScript() + "\n");
        System.out.println("Sol Smart Contract: \n" + getSolSmartScript() + "\n");
        System.out.println("Sol Sign-In: \n" + getSignInScript() + "\n");
        System.out.println("Sol Sign Message: \n" + getSignMessageScript() + "\n");
        System.out.println("Sol Associate Account: \n" + getAssociateTokenAccountScript() + "\n");
        System.out.println("Sol transfer spl token-2022: \n" + getTransferSplToken22Script() + "\n");
        System.out.println("Sol transfer spl token-2022 with compute budget: \n" + getTransferSplToken22WithComputeBudgetScript() + "\n");
        System.out.println("Sol create and transfer spl token-2022: \n" + getCreateAndTransferSplToken22Script() + "\n");
        System.out.println("Sol Delegate: \n" + getDelegateScript() + "\n");
        System.out.println("Sol Undelegate: \n" + getUndelegateScript() + "\n");
        System.out.println(
                "Sol Delegate And Create Account With Seed \n"
                + getDelegateAndCreateAccountWithSeedScript()
                + "\n");
        System.out.println("Sol Withdraw: \n" + getStackingWithdrawScript() + "\n");
    }

    private static String EMPTY_PUBLIC_KEY = "0000000000000000000000000000000000000000000000000000000000000000";

    public static String getTransferScript() {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptData keysCount = sac.getArgument(1);
        ScriptData fromAccount = sac.getArgument(32);
        ScriptData toAccount = sac.getArgument(32);
        ScriptData programId = sac.getArgument(32);
        ScriptData recentBlockHash = sac.getArgument(32);
        ScriptData keyIndices = sac.getArgument(2);
        ScriptData dataLength = sac.getArgument(1);
        ScriptData programIdIndex = sac.getArgument(4);
        ScriptData data = sac.getArgumentAll();

        return transferScript(new TransferScriptData(keysCount, fromAccount, toAccount, programId, recentBlockHash, keyIndices, dataLength, programIdIndex, data));
    }

    public static String getTransferScriptSignature = Strings.padStart("3045022026CCAB06DA64DEBE4CF10D8CE3C7C27946991DCC98314AE20A6EBF0A1A71F047022100D321983320786B1A5BE3270F1AC0DF8AA403875A625532FC3B9C23E181F0B2F7", 144, '0');

    public static String getTransferToSelfScript() {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptData keysCount = sac.getArgument(1);
        ScriptData fromAccount = sac.getArgument(32);
        ScriptData programId = sac.getArgument(32);
        ScriptData recentBlockHash = sac.getArgument(32);
        ScriptData keyIndices = sac.getArgument(2);
        ScriptData dataLength = sac.getArgument(1);
        ScriptData programIdIndex = sac.getArgument(4);
        ScriptData data = sac.getArgumentAll();

        return transferScript(new TransferScriptData(keysCount, fromAccount, null, programId, recentBlockHash, keyIndices, dataLength, programIdIndex, data));
    }

    public static String getTransferToSelfScriptSignature = Strings.padStart("3046022100f7f513983d00ec91d6ea3fef37441cc63e75cc4729cd15d79b5a823b52cc71e702210096b3ea7f64500eedfc7e1eefcbb12fd048fe61789f394b32ecdd2bf754789dce", 144, '0');

    static class TransferScriptData {

        ScriptData keysCount;
        ScriptData fromAccount;
        ScriptData toAccount;
        ScriptData programId;
        ScriptData recentBlockHash;
        ScriptData keyIndices;
        ScriptData dataLength;
        ScriptData programIdIndex;
        ScriptData data;

        public TransferScriptData(ScriptData keysCount,
                ScriptData fromAccount,
                ScriptData toAccount,
                ScriptData programId,
                ScriptData recentBlockHash,
                ScriptData keyIndices,
                ScriptData dataLength,
                ScriptData programIdIndex,
                ScriptData data) {
            this.keysCount = keysCount;
            this.fromAccount = fromAccount;
            this.toAccount = toAccount;
            this.programId = programId;
            this.recentBlockHash = recentBlockHash;
            this.keyIndices = keyIndices;
            this.dataLength = dataLength;
            this.programIdIndex = programIdIndex;
            this.data = data;
        }
    }

    private static String transferScript(TransferScriptData transferScriptData) {
        ScriptData keysCount = transferScriptData.keysCount;
        ScriptData fromAccount = transferScriptData.fromAccount;
        ScriptData toAccount = transferScriptData.toAccount;
        ScriptData programId = transferScriptData.programId;
        ScriptData recentBlockHash = transferScriptData.recentBlockHash;
        ScriptData keyIndices = transferScriptData.keyIndices;
        ScriptData dataLength = transferScriptData.dataLength;
        ScriptData programIdIndex = transferScriptData.programIdIndex;
        ScriptData data = transferScriptData.data;

        return new ScriptAssembler()
                .setCoinType(0x01f5)
                .copyString("01")
                .copyString("00")
                .copyString("01")
                .copyArgument(keysCount)
                .copyArgument(fromAccount)
                // keysCount 03 means keys: [fromAccount, toAccount, programId]
                // keysCount 02 means keys: [fromAccount, programId], toAccount = fromAccount
                .ifEqual(keysCount, "02", "", new ScriptAssembler().copyArgument(toAccount).getScript())
                .copyArgument(programId)
                .copyArgument(recentBlockHash)
                // instruction count
                .copyString("01")
                // index of ProgramIds
                .ifEqual(
                        keysCount,
                        "02",
                        new ScriptAssembler().copyString("01").getScript(),
                        new ScriptAssembler().copyString("02").getScript())
                .copyString("02")
                .copyArgument(keyIndices)
                .copyArgument(dataLength)
                .copyArgument(programIdIndex)
                .copyArgument(data)
                .showMessage("SOL")
                // keyIndices 0001 means fromAccount != toAccount
                // keyIndices 0000 means fromAccount == toAccount
                .ifEqual(
                        keyIndices,
                        "0001",
                        new ScriptAssembler()
                                .baseConvert(
                                        toAccount,
                                        Buffer.CACHE2,
                                        0,
                                        ScriptAssembler.base58Charset,
                                        ScriptAssembler.zeroInherit)
                                .showAddress(ScriptData.getDataBufferAll(Buffer.CACHE2))
                                .getScript(),
                        new ScriptAssembler()
                                .baseConvert(
                                        fromAccount,
                                        Buffer.CACHE2,
                                        0,
                                        ScriptAssembler.base58Charset,
                                        ScriptAssembler.zeroInherit)
                                .showAddress(ScriptData.getDataBufferAll(Buffer.CACHE2))
                                .getScript())
                .clearBuffer(Buffer.CACHE2)
                .baseConvert(
                        data,
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

    public static String getAssociateTokenAccountScript() {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptData keyCount = sac.getArgument(1);
        ScriptData publicKey0 = sac.getArgument(32);
        ScriptData publicKey1 = sac.getArgument(32);
        ScriptData publicKey2 = sac.getArgument(32);
        ScriptData publicKey3 = sac.getArgument(32);
        ScriptData publicKey4 = sac.getArgument(32);
        ScriptData publicKey5 = sac.getArgument(32);
        ScriptData publicKey6 = sac.getArgument(32);
        ScriptData publicKey7 = sac.getArgument(32);
        ScriptData recentBlockhash = sac.getArgument(32);
        ScriptData programIndex = sac.getArgument(1);
        ScriptData keyIndices = sac.getArgument(7);

        ScriptAssembler scriptAsb = new ScriptAssembler();

        return scriptAsb
                .setCoinType(0x01f5)
                // numRequiredSignatures
                .copyString("01")
                // numReadonlySignedAccounts
                .copyString("00")
                // numReadonlyUnsignedAccounts
                .ifEqual(
                        keyCount,
                        "08",
                        new ScriptAssembler().copyString("06").getScript(),
                        new ScriptAssembler().copyString("05").getScript())
                // keyCount
                .copyArgument(keyCount)
                .copyArgument(publicKey0)
                .copyArgument(publicKey1)
                .copyArgument(publicKey2)
                .copyArgument(publicKey3)
                .copyArgument(publicKey4)
                .copyArgument(publicKey5)
                .copyArgument(publicKey6)
                .ifEqual(keyCount, "08", new ScriptAssembler().copyArgument(publicKey7).getScript(), "")
                .copyArgument(recentBlockhash)
                // instruction count
                .copyString("01")
                .copyArgument(programIndex)
                // account length
                .copyString("07")
                .copyArgument(keyIndices)
                // empty data
                .copyString("00")
                .showMessage("SOL")
                .showWrap("ToKEN", "ACCoUNT")
                // Since associatedToken is writable and not singer, it will always in index 1.
                .baseConvert(
                        publicKey1,
                        Buffer.CACHE2,
                        0,
                        ScriptAssembler.base58Charset,
                        ScriptAssembler.zeroInherit)
                .showAddress(ScriptData.getDataBufferAll(Buffer.CACHE2))
                .showPressButton()
                .setHeader(HashType.NONE, SignType.EDDSA)
                .getScript();
    }

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

    public enum SplTxType {
        TRANSFER("00"),
        TRANSFER_WITH_COMPUTE_BUDGET("01");
        private final String signLabel;

        SplTxType(String signLabel) {
            this.signLabel = signLabel;
        }

        @Override
        public String toString() {
            return signLabel;
        }
    }

    public static String getTransferSplToken22Script() {
        return splTransferScript(SplTxType.TRANSFER);
    }
    public static String getTransferSplToken22ScriptSignature = Strings.padStart("30460221009607028b2a852dbc684b5cf621cac990d332af87748f6e5f03a6bcfc2d6554d3022100ffe29102ad067bf24be054224494e53e94eae02432ba93019799237d2ce5902a", 144, '0');

    public static String getTransferSplToken22WithComputeBudgetScript() {
        return splTransferScript(SplTxType.TRANSFER_WITH_COMPUTE_BUDGET);
    }
    public static String getTransferSplToken22WithComputeBudgetScriptSignature = Strings.padStart("3045022100990fe9ab2724e589effdee777dbab9716febff1b5d3c46709720cb6dd5238852022076de7f880a9a4571059e1ed29440726e80759e28f43899ed1cafe83f964552fd", 144, '0');

    private static String splTransferScript(SplTxType txType) {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptData keysCount = sac.getArgument(1);
        // SplTxType.TRANSFER
        // to other : [ownerAccount, toAssociateAccount, fromAssociateAccount, tokenAccount, tokenProgramId]
        // to self  : [ownerAccount, fromAssociateAccount, tokenAccount, tokenProgramId]
        // SplTxType.TRANSFER_WITH_COMPUTE_BUDGET
        // to other : [ownerAccount, toAssociateAccount, fromAssociateAccount, computeBudgetProgramId, tokenAccount, tokenProgramId]
        // to self  : [ownerAccount, fromAssociateAccount, computeBudgetProgramId, tokenAccount, tokenProgramId]
        ScriptData publicKey1 = sac.getArgument(32);
        ScriptData publicKey2 = sac.getArgument(32);
        ScriptData publicKey3 = sac.getArgument(32);
        ScriptData publicKey4 = sac.getArgument(32);
        ScriptData publicKey5 = sac.getArgument(32);
        ScriptData publicKey6 = sac.getArgument(32);

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
        if (txType == SplTxType.TRANSFER_WITH_COMPUTE_BUDGET) {
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
        ScriptData keyIndices = sac.getArgument(4);
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

        String instructionScript = "";
        if (txType == SplTxType.TRANSFER) {
            instructionScript += new ScriptAssembler()
                    .copyString("01") // instructions count
                    // 1st instruction
                    .copyArgument(tokenProgramIdIndex)
                    .copyArgument(keyIndicesLength)
                    .copyArgument(keyIndices)
                    .copyArgument(dataLength)
                    .copyArgument(tokenInstruction)
                    .copyArgument(tokenAmount) // amount
                    .copyArgument(customTokenDecimals)
                    .getScript();
        } else if (txType == SplTxType.TRANSFER_WITH_COMPUTE_BUDGET) {
            instructionScript += new ScriptAssembler()
                    .copyString("03") // instructions count
                    // 1st instruction
                    .copyArgument(gasPriceProgramIdIndex)
                    .copyArgument(gasPriceKeyLength)
                    .copyArgument(gasPriceDataLength)
                    .copyArgument(gasPriceComputeBudgetInstructionType)
                    .copyArgument(gasPrice)
                    // 2nd instruction
                    .copyArgument(gasLimitProgramIdIndex)
                    .copyArgument(gasLimitKeyLength)
                    .copyArgument(gasLimitDataLength)
                    .copyArgument(gasLimitComputeBudgetInstructionType) // SetComputeUnitLimit
                    .copyArgument(gasLimit)
                    // 3rd instruction
                    .copyArgument(tokenProgramIdIndex)
                    .copyArgument(keyIndicesLength)
                    .copyArgument(keyIndices)
                    .copyArgument(dataLength)
                    .copyArgument(tokenInstruction)
                    .copyArgument(tokenAmount)
                    .copyArgument(customTokenDecimals)
                    .getScript();
        }

        return new ScriptAssembler()
                .setCoinType(0x01f5)
                //numRequiredSignatures
                .copyString("01")
                //numReadonlySignedAccounts
                .copyString("00")
                //numReadonlyUnsignedAccounts
                .copyString("03")
                .copyArgument(keysCount)
                .copyArgument(publicKey1)
                .copyArgument(publicKey2)
                .copyArgument(publicKey3)
                .copyArgument(publicKey4)
                .ifEqual(publicKey5, EMPTY_PUBLIC_KEY, "", new ScriptAssembler().copyArgument(publicKey5).getScript())
                .ifEqual(publicKey6, EMPTY_PUBLIC_KEY, "", new ScriptAssembler().copyArgument(publicKey6).getScript())
                // transactionType 00 means keys: [ownerAccount, toAssociateAccount, fromAssociateAccount, tokenAccount, tokenProgramId]
                // transactionType 01 means keys: [ownerAccount, fromAssociateAccount, tokenAccount, tokenProgramId], toAssociateAccount = fromAssociateAccount
                // transactionType 02 means keys: [ownerAccount, toAssociateAccount, fromAssociateAccount, computeBudgetProgramId, tokenAccount, tokenProgramId]
                // transactionType 03 means keys: [ownerAccount, fromAssociateAccount, computeBudgetProgramId, tokenAccount, tokenProgramId], toAssociateAccount = fromAssociateAccount
                .copyArgument(recentBlockHash)
                .insertString(instructionScript)
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
                .baseConvert(
                        publicKey2,
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
                .setHeader(HashType.NONE, SignType.EDDSA)
                .getScript();
    }

    public static String getCreateAndTransferSplToken22Script() {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptData keysCount = sac.getArgument(1);
        ScriptData publicKey0 = sac.getArgument(32);
        ScriptData publicKey1 = sac.getArgument(32);
        ScriptData publicKey2 = sac.getArgument(32);
        ScriptData publicKey3 = sac.getArgument(32);
        ScriptData publicKey4 = sac.getArgument(32);
        ScriptData publicKey5 = sac.getArgument(32);
        ScriptData publicKey6 = sac.getArgument(32);
        ScriptData publicKey7 = sac.getArgument(32);
        ScriptData recentBlockhash = sac.getArgument(32);
        // Create Account Instruction
        ScriptData indexToCreateProgramIds = sac.getArgument(1);

        ScriptData keyIndices0 = sac.getArgument(1);
        ScriptData keyIndices1 = sac.getArgument(1);
        ScriptData keyIndices2 = sac.getArgument(1);
        ScriptData remainKeys = sac.getArgument(3);
        // Transfer SPL Token Instruction
        ScriptData indexToTransferProgramIds = sac.getArgument(1);
        ScriptData transferKeyIndices0 = sac.getArgument(4);
        ScriptData transferInstruction = sac.getArgument(1);
        ScriptData amount = sac.getArgument(8);
        ScriptData customTokenDecimals = sac.getArgument(1);
        ScriptData tokenInfo = sac.getArgumentUnion(0, 41);
        ScriptData tokenDecimals = sac.getArgument(1);
        ScriptData tokenNameLength = sac.getArgument(1);
        ScriptData tokenName = sac.getArgumentVariableLength(7);
        ScriptData tokenAddr = sac.getArgument(32);
        ScriptData tokenSign = sac.getArgument(72);

        ScriptAssembler scriptAsb = new ScriptAssembler();
        String script
                = scriptAsb
                        .setCoinType(0x01f5)
                        // numRequiredSignatures
                        .copyString("01")
                        // numReadonlySignedAccounts
                        .copyString("00")
                        // numReadonlyUnsignedAccounts
                        .copyString("05")
                        // key count
                        .copyArgument(keysCount)
                        .copyArgument(publicKey0)
                        .copyArgument(publicKey1)
                        .copyArgument(publicKey2)
                        .copyArgument(publicKey3)
                        .copyArgument(publicKey4)
                        .copyArgument(publicKey5)
                        .copyArgument(publicKey6)
                        .copyArgument(publicKey7)
                        .copyArgument(recentBlockhash)
                        // instructions count
                        .copyString("02")
                        // create account instruction
                        .copyArgument(indexToCreateProgramIds)
                        // key Indices length
                        .copyString("06")
                        .copyArgument(keyIndices0)
                        .copyArgument(keyIndices1)
                        .copyArgument(keyIndices2)
                        .copyArgument(remainKeys)
                        // data length zero
                        .copyString("00")
                        // transfer instruction
                        .copyArgument(indexToTransferProgramIds)
                        // key Indices length
                        .copyString("04")
                        .copyArgument(transferKeyIndices0)
                        // data length = TokenInstruction (1 bytes) + amount (8 bytes) + decimals (1 bytes)
                        .copyString("0a")
                        .copyArgument(transferInstruction)
                        .copyArgument(amount)
                        .copyArgument(customTokenDecimals)
                        .showMessage("SOL")
                        // display token name
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
                        // Show owner sol address, since sol address is not signer and not writable, it will have many possibilities.
                        .ifEqual(
                                keyIndices2,
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
                                keyIndices2,
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
                                keyIndices2,
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
                                keyIndices2,
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
                                keyIndices2,
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
                        // show amount
                        .baseConvert(
                                amount,
                                Buffer.CACHE1,
                                8,
                                ScriptAssembler.binaryCharset,
                                ScriptAssembler.inLittleEndian)
                        .setBufferInt(tokenDecimals, 0, 20)
                        .showAmount(ScriptData.getDataBufferAll(Buffer.CACHE1), ScriptData.bufInt)
                        .clearBuffer(Buffer.CACHE1)
                        .showPressButton()
                        .setHeader(HashType.NONE, SignType.EDDSA)
                        .getScript();

        return script;
    }

    public static String getCreateAndTransferSplTokenScriptSignature = Strings.padStart("304502204da918f88d8a2821e4d1cfc03fc0420d4913245258995f7214c5b1a0154cf0c1022100e3797f9c27e58a5532540d28b6580744a98ea37554c0a771a6bd3ae028d4131a", 144, '0');

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
