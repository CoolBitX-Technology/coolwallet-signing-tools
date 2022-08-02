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
        System.out.println("Sol Smart Contract: \n" + getSolSmartScript() + "\n");
        System.out.println("Sol Associate Account: \n" + getAssociateTokenAccountScript() + "\n");
        System.out.println("Sol transfer spl token: \n" + getTransferSplTokenScript() + "\n");
        System.out.println(
                "Sol Create and transfer spl token: \n" + getCreateAndTransferSplTokenScript() + "\n");
        System.out.println("Sol Delegate: \n" + getDelegateScript() + "\n");
        System.out.println("Sol Undelegate: \n" + getUndelegateScript() + "\n");
        System.out.println(
                "Sol Delegate And Create Account With Seed \n"
                + getDelegateAndCreateAccountWithSeedScript()
                + "\n");
        System.out.println("Sol Withdraw: \n" + getStackingWithdrawScript() + "\n");
    }

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

        ScriptAssembler scriptAsb = new ScriptAssembler();
        String script
                = scriptAsb
                        .setCoinType(0x01f5)
                        .copyString("01")
                        .copyString("00")
                        .copyString("01")
                        .copyArgument(keysCount)
                        .copyArgument(fromAccount)
                        .ifEqual(keysCount, "02", "", new ScriptAssembler().copyArgument(toAccount).getScript())
                        .copyArgument(programId)
                        .copyArgument(recentBlockHash)
                        // instruction count
                        .copyString("01")
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

        return script;
    }

    public static String getSolSmartScript() {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptData data = sac.getArgumentAll();

        ScriptAssembler scriptAsb = new ScriptAssembler();
        String script
                = scriptAsb
                        .setCoinType(0x01f5)
                        .copyArgument(data)
                        .showMessage("SOL")
                        .showWrap("SMART", "")
                        .showPressButton()
                        .setHeader(HashType.NONE, SignType.EDDSA)
                        .getScript();
        return script;
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

    public static String getTransferSplTokenScript() {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptData keysCount = sac.getArgument(1);
        ScriptData ownerAccount = sac.getArgument(32);
        ScriptData fromAssociateAccount = sac.getArgument(32);
        ScriptData toAssociateAccount = sac.getArgument(32);
        ScriptData programId = sac.getArgument(32);

        ScriptData recentBlockHash = sac.getArgument(32);
        ScriptData keyIndices = sac.getArgument(3);
        ScriptData dataLength = sac.getArgument(1);
        ScriptData programIdIndex = sac.getArgument(1);
        ScriptData data = sac.getArgument(8);
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
                        .copyString("01")
                        .copyString("00")
                        .copyString("01")
                        .copyArgument(keysCount)
                        .copyArgument(ownerAccount)
                        .copyArgument(fromAssociateAccount)
                        .ifEqual(
                                keysCount,
                                "03",
                                "",
                                new ScriptAssembler().copyArgument(toAssociateAccount).getScript())
                        .copyArgument(programId)
                        .copyArgument(recentBlockHash)
                        .copyString("01")
                        .ifEqual(
                                keysCount,
                                "03",
                                new ScriptAssembler().copyString("02").getScript(),
                                new ScriptAssembler().copyString("03").getScript())
                        .copyString("03")
                        .copyArgument(keyIndices)
                        .copyArgument(dataLength)
                        .copyArgument(programIdIndex)
                        .copyArgument(data)
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
                        .ifEqual(
                                keyIndices,
                                "010200",
                                new ScriptAssembler()
                                        .baseConvert(
                                                toAssociateAccount,
                                                Buffer.CACHE2,
                                                0,
                                                ScriptAssembler.base58Charset,
                                                ScriptAssembler.zeroInherit)
                                        .showAddress(ScriptData.getDataBufferAll(Buffer.CACHE2))
                                        .getScript(),
                                new ScriptAssembler()
                                        .baseConvert(
                                                fromAssociateAccount,
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
                        .setBufferInt(tokenDecimals, 0, 20)
                        .showAmount(ScriptData.getDataBufferAll(Buffer.CACHE1), ScriptData.bufInt)
                        .clearBuffer(Buffer.CACHE1)
                        .showPressButton()
                        .setHeader(HashType.NONE, SignType.EDDSA)
                        .getScript();

        return script;
    }

    public static String getCreateAndTransferSplTokenScript() {
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
        // Create Account Instruction
        ScriptData programIdIndex0 = sac.getArgument(1);
        ScriptData keyIndex0 = sac.getArgument(1);
        ScriptData keyIndex1 = sac.getArgument(1);
        ScriptData keyIndex2 = sac.getArgument(1);
        ScriptData remainKeys = sac.getArgument(4);
        // Transfer SPL Token Instruction
        ScriptData programIdIndex1 = sac.getArgument(1);
        ScriptData keys = sac.getArgument(3);
        ScriptData transferInstruction = sac.getArgument(1);
        ScriptData amount = sac.getArgument(8);
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
                        .copyString("01")
                        .copyString("00")
                        .copyString("06")
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
                        .copyString("02")
                        // create account instruction
                        .copyArgument(programIdIndex0)
                        // key Indices length
                        .copyString("07")
                        .copyArgument(keyIndex0)
                        .copyArgument(keyIndex1)
                        .copyArgument(keyIndex2)
                        .copyArgument(remainKeys)
                        // data length zero
                        .copyString("00")
                        // transfer instruction
                        .copyArgument(programIdIndex1)
                        // key Indices length
                        .copyString("03")
                        .copyArgument(keys)
                        // data length Token program id index(1 bytes) + amount (8 bytes)
                        .copyString("09")
                        .copyArgument(transferInstruction)
                        .copyArgument(amount)
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
                                keyIndex2,
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
                                keyIndex2,
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
                                keyIndex2,
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
                                keyIndex2,
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
                                keyIndex2,
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
                                keyIndex2,
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
