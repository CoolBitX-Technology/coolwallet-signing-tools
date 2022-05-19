package com.coolbitx.wallet.signing.scriptlib;

import com.coolbitx.wallet.signing.utils.HexUtil;
import com.coolbitx.wallet.signing.utils.ScriptArgumentComposer;
import com.coolbitx.wallet.signing.utils.ScriptAssembler;
import com.coolbitx.wallet.signing.utils.ScriptAssembler.HashType;
import com.coolbitx.wallet.signing.utils.ScriptAssembler.SignType;
import com.coolbitx.wallet.signing.utils.ScriptData;
import com.coolbitx.wallet.signing.utils.ScriptData.Buffer;

public class SolScript {
  public static void main(String[] args) {
    listAll();
  }

  public static void listAll() {
    System.out.println("Sol transfer: \n" + getTransferScript() + "\n");
    System.out.println("Sol Smart Contract: \n" + getSolSmartScript() + "\n");
    System.out.println("Sol Associate Account: \n" + getAssociateTokenAccountScript() + "\n");
    System.out.println("Sol transfer spl token: \n" + getTransferSplTokenScript() + "\n");
    System.out.println("Sol Stacking Withdraw: \n" + getStackingWithdrawScript() + "\n");
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
    String script =
        scriptAsb
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
    String script =
        scriptAsb
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
    ScriptData tokenInfo = sac.getArgumentUnion(0, 73);
    ScriptData tokenDecimals = sac.getArgument(1);
    ScriptData tokenNameLength = sac.getArgument(1);
    ScriptData tokenName = sac.getArgumentVariableLength(7);
    ScriptData tokenSign = sac.getArgument(72);

    ScriptAssembler scriptAsb = new ScriptAssembler();
    String script =
        scriptAsb
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
            .showWrap("SOL", "SPL")
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
  
  public static String getStackingWithdrawScript() {
    ScriptArgumentComposer sac = new ScriptArgumentComposer();
    ScriptData keysCount = sac.getArgument(1);
    ScriptData fromAccount = sac.getArgument(32);
    ScriptData toAccount = sac.getArgument(32);
    ScriptData programId = sac.getArgument(32);
    ScriptData publicKey1 = sac.getArgument(32);
    ScriptData publicKey2 = sac.getArgument(32);
    ScriptData recentBlockHash = sac.getArgument(32);
    ScriptData keyIndices = sac.getArgument(5);
    ScriptData dataLength = sac.getArgument(1);
    ScriptData data = sac.getArgumentAll();

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
        .copyArgument(fromAccount)
        .copyArgument(toAccount)
        .copyArgument(programId)
        .copyArgument(publicKey1)
        .copyArgument(publicKey2)
        .copyArgument(recentBlockHash)
        // instruction count
        .copyString("01")
        .copyString("02")
        .copyString("05")
        .copyArgument(keyIndices)
        .copyArgument(dataLength)
        .copyArgument(data)
        .showMessage("SOL")
        .showMessage("Reward")
        .showPressButton()
        .setHeader(HashType.NONE, SignType.EDDSA)
        .getScript();
  }
}
