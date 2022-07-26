package com.coolbitx.wallet.signing.scriptlib;

import com.coolbitx.wallet.signing.utils.HexUtil;
import com.coolbitx.wallet.signing.utils.ScriptArgumentComposer;
import com.coolbitx.wallet.signing.utils.ScriptAssembler;
import com.coolbitx.wallet.signing.utils.ScriptAssembler.HashType;
import com.coolbitx.wallet.signing.utils.ScriptAssembler.SignType;
import com.coolbitx.wallet.signing.utils.ScriptData;
import com.coolbitx.wallet.signing.utils.ScriptData.Buffer;

public class CosmosScript {
  public static void main(String[] args) {
    listAll();
  }

  private enum WireType {
    Int(0),
    String(2);
    private int num;

    WireType(int numVal) {
      this.num = numVal;
    }

    public int valueOf() {
      return this.num;
    }
  }

  private enum CoinType {
    Atom(0x076),
    Kava(0x1CB),
    Thor(0x3A3);
    private int num;

    CoinType(int numVal) {
      this.num = numVal;
    }

    public int valueOf() {
      return this.num;
    }
  }

  public enum MsgType {
    // message.url - /cosmos.bank.v1beta1.MsgSend
    SEND("1c2f636f736d6f732e62616e6b2e763162657461312e4d736753656e64"),
    // message.url - /types.MsgSend
    THOR_SEND("0e2f74797065732e4d736753656e64"),
    // message.url - /types.MsgDeposit
    THOR_DEPOSIT("0e2f74797065732e4d736753656e64"),
    // message.url - /cosmos.staking.v1beta1.MsgDelegate
    DELEGATE("232f636f736d6f732e7374616b696e672e763162657461312e4d736744656c6567617465"),
    // message.url - /cosmos.staking.v1beta1.MsgUndelegate
    UNDELEGATE("252f636f736d6f732e7374616b696e672e763162657461312e4d7367556e64656c6567617465"),
    // message.url - /cosmos.distribution.v1beta1.MsgWithdrawDelegatorReward
    WITHDRAW(
        "372f636f736d6f732e646973747269627574696f6e2e763162657461312e4d7367576974686472617744656c656761746f72526577617264");
    private final String url;

    MsgType(String url) {
      this.url = url;
    }

    @Override
    public String toString() {
      return url;
    }
  }

  public static void listAll() {
    System.out.println(
        "Atom Transfer: \n" + getCosmosCoinScript(CoinType.Atom.valueOf(), MsgType.SEND) + "\n");
    System.out.println(
        "Atom Delegate: \n"
            + getCosmosCoinScript(CoinType.Atom.valueOf(), MsgType.DELEGATE)
            + "\n");
    System.out.println(
        "Atom Undelegate: \n"
            + getCosmosCoinScript(CoinType.Atom.valueOf(), MsgType.UNDELEGATE)
            + "\n");
    System.out.println(
        "Atom Withdraw: \n" + getCosmosWithdrawScript(CoinType.Atom.valueOf()) + "\n");
    System.out.println(
        "Kava Transfer: \n" + getCosmosCoinScript(CoinType.Kava.valueOf(), MsgType.SEND) + "\n");
    System.out.println(
        "Kava Delegate: \n"
            + getCosmosCoinScript(CoinType.Kava.valueOf(), MsgType.DELEGATE)
            + "\n");
    System.out.println(
        "Kava Undelegate: \n"
            + getCosmosCoinScript(CoinType.Kava.valueOf(), MsgType.UNDELEGATE)
            + "\n");
    System.out.println(
        "Kava Withdraw: \n" + getCosmosWithdrawScript(CoinType.Kava.valueOf()) + "\n");
    System.out.println(
        "Thor Transfer: \n"
            + getCosmosCoinScript(CoinType.Thor.valueOf(), MsgType.THOR_SEND)
            + "\n");
    System.out.println(
        "Thor Delegate: \n"
            + getCosmosCoinScript(CoinType.Thor.valueOf(), MsgType.DELEGATE)
            + "\n");
    System.out.println(
        "Thor Undelegate: \n"
            + getCosmosCoinScript(CoinType.Thor.valueOf(), MsgType.UNDELEGATE)
            + "\n");
    System.out.println(
        "Thor Withdraw: \n" + getCosmosWithdrawScript(CoinType.Thor.valueOf()) + "\n");
  }

  private static class CosmosTransactionMaker {
    private final ScriptAssembler scriptAssembler;

    CosmosTransactionMaker(ScriptAssembler scriptAssembler) {
      this.scriptAssembler = scriptAssembler;
    }

    /**
     * Using this function to create Coin.<br>
     * Proto definition:
     *
     * <pre>{@code
     * message Coin {
     *   string denom = 1;
     *   string amount = 2;
     * }
     * }</pre>
     *
     * @param argDenomInfo Denom Information.
     * @param argDenom Denom string.
     * @param argDenomSignature Denom signature which is used to validate denom information.
     * @param argAmount Amount of Coin.
     */
    public void makeCoin(
        ScriptData argDenomInfo,
        ScriptData argDenom,
        ScriptData argDenomSignature,
        ScriptData argAmount) {
      scriptAssembler
          // amount<Coin>
          .ifSigned(argDenomInfo, argDenomSignature, "", ScriptAssembler.throwSEError)
          .arrayPointer()
          // coin.denom
          .copyString("0a")
          .protobuf(argDenom, WireType.String.valueOf())
          // coin.amount
          .copyString("12")
          .arrayPointer()
          .baseConvert(
              argAmount,
              Buffer.TRANSACTION,
              0,
              ScriptAssembler.decimalCharset,
              ScriptAssembler.leftJustify)
          .arrayEnd() // coin.amount end
          .arrayEnd(); // amount<coin> end
    }

    /**
     * Using this function to create MsgSend, MsgDelegate, MsgUndelegate.<br>
     * Proto definition:
     *
     * <pre>{@code
     * message MsgSend {
     *   string from = 1;
     *   string to = 2;
     *   repeated Coin amount = 3;
     * }
     * }</pre>
     *
     * Json formation:
     *
     * <pre>{@code
     * const MsgSend = {
     *   typeUrl: url,
     *   value: {
     *     from: '...',
     *     to: '...',
     *     amount: [
     *      {
     *        denom: '',
     *        amount: '',
     *      }
     *     ]
     *   }
     * }
     * }</pre>
     *
     * @param url The typeUrl of the message.
     * @param argFrom First string argument for protobuf.
     * @param argTo First string argument for protobuf.
     * @param argDenomInfo Denom Information.
     * @param argDenom Denom string.
     * @param argDenomSignature Denom signature which is used to validate denom information.
     * @param argAmount Amount of Coin.
     */
    public void makeMsgWithCoin(
        String url,
        ScriptData argFrom,
        ScriptData argTo,
        ScriptData argDenomInfo,
        ScriptData argDenom,
        ScriptData argDenomSignature,
        ScriptData argAmount) {
      scriptAssembler
          .arrayPointer()
          // message.url
          .copyString("0a")
          .copyString(url)
          // message.value
          .copyString("12")
          .arrayPointer()
          // from_address
          .copyString("0a")
          .protobuf(argFrom, WireType.String.valueOf())
          // to_address
          .copyString("12")
          .protobuf(argTo, WireType.String.valueOf());
      scriptAssembler.copyString("1a");
      makeCoin(argDenomInfo, argDenom, argDenomSignature, argAmount);
      scriptAssembler.arrayEnd(); // message.value end
      scriptAssembler.arrayEnd(); // message end
    }

    /**
     * Using this function to create MsgWithdrawDelegatorReward.<br>
     *
     * <p>Proto definition:
     *
     * <pre>{@code
     * message MsgWithdrawDelegatorReward {
     *   string from = 1;
     *   string to = 2;
     * }
     * }</pre>
     *
     * Json formation:
     *
     * <pre>{@code
     * const MsgWithdrawDelegatorReward = {
     *   typeUrl: url,
     *   value: {
     *     from: '...',
     *     to: '...',
     *   }
     * }
     * }</pre>
     *
     * @param url The typeUrl of the message.
     * @param argFrom First string argument for protobuf.
     * @param argTo First string argument for protobuf.
     */
    public void makeMsgWithoutCoin(String url, ScriptData argFrom, ScriptData argTo) {
      scriptAssembler
          .arrayPointer()
          // message.url
          .copyString("0a")
          .copyString(url)
          // message.value
          .copyString("12")
          .arrayPointer()
          // from_address
          .copyString("0a")
          .protobuf(argFrom, WireType.String.valueOf())
          // to_address
          .copyString("12")
          .protobuf(argTo, WireType.String.valueOf());
      scriptAssembler.arrayEnd(); // message.value end
      scriptAssembler.arrayEnd(); // message end
    }

    /**
     * Using this function to create SignerInfo.<br>
     *
     * <p>Proto definition:
     *
     * <pre>{@code
     * message SignerInfo {
     *   Any public_key = 1;
     *   ModeInfo mode_info = 2;
     *   uint64 sequence = 3;
     * }
     * }</pre>
     *
     * @param argPublicKey User public key.
     * @param argSequence Account sequence number.
     */
    public void makeSignerInfo(ScriptData argPublicKey, ScriptData argSequence) {
      // signer_info
      scriptAssembler
          .arrayPointer()
          // pubkey
          // typeUrl: '/cosmos.crypto.secp256k1.PubKey'
          .copyString(
              "0a460a1f2f636f736d6f732e63727970746f2e736563703235366b312e5075624b657912230a21")
          .copyArgument(argPublicKey)
          // mode_info
          .copyString("12040a020801");
      // sequence
      scriptAssembler.ifEqual(
          argSequence,
          "00",
          "",
          new ScriptAssembler()
              .copyString("18")
              .protobuf(argSequence, WireType.Int.valueOf())
              .getScript());

      scriptAssembler.arrayEnd(); // signer_info end
    }

    /**
     * Using this function to create AuthInfo.<br>
     *
     * <p>Proto definition:
     *
     * <pre>{@code
     * message SignerInfo {
     *   repeated SignerInfo signer_infos = 1;
     *   Fee fee = 2;
     * }
     * }</pre>
     *
     * @param argDenomInfo Fee denom information.
     * @param argDenom Fee denom string.
     * @param argDenomSignature Fee Denom signature which is used to validate fee denom information.
     * @param argFeeAmount Fee amount.
     * @param argGas Gas Limit.
     * @param argPublicKey User public key.
     * @param argSequence Account sequence number.
     */
    public void makeAuthInfo(
        int coinType,
        ScriptData argDenomInfo,
        ScriptData argDenom,
        ScriptData argDenomSignature,
        ScriptData argFeeAmount,
        ScriptData argGas,
        ScriptData argPublicKey,
        ScriptData argSequence) {
      // auth_info
      scriptAssembler.arrayPointer();
      scriptAssembler.copyString("0a");
      // fee
      makeSignerInfo(argPublicKey, argSequence);
      scriptAssembler.copyString("12").arrayPointer();
      // Thor chain transaction will not have fee.amount
      if (coinType != CoinType.Thor.valueOf()) {
        scriptAssembler.copyString("0a");
        makeCoin(argDenomInfo, argDenom, argDenomSignature, argFeeAmount);
      }
      // gas_limit
      scriptAssembler.copyString("10").protobuf(argGas, WireType.Int.valueOf());
      scriptAssembler.arrayEnd(); // fee end
      scriptAssembler.arrayEnd(); // auth_info end
    }

    /**
     * Using this to create memo in txBody
     *
     * @param argMemoLength Argument which carry memo length.
     * @param argMemo Memo string.
     */
    public void makeMemo(ScriptData argMemoLength, ScriptData argMemo) {
      scriptAssembler.ifEqual(
          argMemoLength,
          "00",
          "",
          new ScriptAssembler()
              .copyString("12")
              .arrayPointer()
              .setBufferInt(argMemoLength, 1, 127)
              .copyRegularString(argMemo)
              .arrayEnd() // memo end
              .getScript());
    }
  }

  public static String getCosmosCoinScript(int coinType, MsgType msgType) {
    ScriptArgumentComposer sac = new ScriptArgumentComposer();
    ScriptData argPublicKey = sac.getArgument(33);
    ScriptData argFrom = sac.getArgumentRightJustified(64);
    ScriptData argTo = sac.getArgumentRightJustified(64);
    ScriptData argAmount = sac.getArgument(8);
    ScriptData argFeeAmount = sac.getArgument(8);
    ScriptData argGas = sac.getArgument(8);
    ScriptData argAccountNumber = sac.getArgument(8);
    ScriptData argSequence = sac.getArgument(8);
    // amount
    ScriptData argDenomInfo = sac.getArgumentUnion(0, 18);
    ScriptData argDecimal = sac.getArgument(1);
    ScriptData argDenom = sac.getArgumentRightJustified(10);
    ScriptData argDenomSymbol = sac.getArgumentRightJustified(7);
    ScriptData argDenomSignature = sac.getArgument(72);
    // fee
    ScriptData argFeeDenomInfo = sac.getArgumentUnion(0, 18);
    ScriptData argFeeDecimal = sac.getArgument(1);
    ScriptData argFeeDenom = sac.getArgumentRightJustified(10);
    ScriptData argFeeDenomSymbol = sac.getArgumentRightJustified(7);
    ScriptData argFeeDenomSignature = sac.getArgument(72);
    // chainId and symbol
    ScriptData argChainInfo = sac.getArgumentUnion(0, 59);
    ScriptData argChainIdLength = sac.getArgument(1);
    ScriptData argChainId = sac.getArgumentVariableLength(50);
    ScriptData argSymbolLength = sac.getArgument(1);
    ScriptData argSymbol = sac.getArgumentVariableLength(7);
    ScriptData argChainInfoSignature = sac.getArgument(72);

    ScriptData argMemoLength = sac.getArgument(1);
    ScriptData argMemo = sac.getArgumentVariableLength(127);
    ScriptAssembler scriptAsb = new ScriptAssembler();
    CosmosTransactionMaker maker = new CosmosTransactionMaker(scriptAsb);
    scriptAsb.setCoinType(coinType);
    scriptAsb.ifSigned(argChainInfo, argChainInfoSignature, "", ScriptAssembler.throwSEError);
    // tx_body
    scriptAsb.copyString("0a");
    scriptAsb.arrayPointer();
    scriptAsb.copyString("0a");
    maker.makeMsgWithCoin(
        msgType.toString(), argFrom, argTo, argDenomInfo, argDenom, argDenomSignature, argAmount);
    // memo
    maker.makeMemo(argMemoLength, argMemo);
    scriptAsb.arrayEnd(); // end tx_body
    // auth_info
    scriptAsb.copyString("12");
    maker.makeAuthInfo(
        coinType,
        argFeeDenomInfo,
        argFeeDenom,
        argFeeDenomSignature,
        argFeeAmount,
        argGas,
        argPublicKey,
        argSequence);
    // chain_id
    scriptAsb.copyString("1a");
    scriptAsb.setBufferInt(argChainIdLength, 1, 50);
    scriptAsb.protobuf(argChainId, WireType.String.valueOf());
    // account_number
    scriptAsb.copyString("20");
    scriptAsb.protobuf(argAccountNumber, WireType.Int.valueOf());
    // display
    scriptAsb.setBufferInt(argSymbolLength, 1, 7);
    scriptAsb.showMessage(argSymbol);
    scriptAsb.showMessage(argDenomSymbol);
    if (msgType == MsgType.DELEGATE) {
      scriptAsb.showMessage("Delgt");
    } else if (msgType == MsgType.UNDELEGATE) {
      scriptAsb.showMessage("UnDel");
    }
    if (coinType == CoinType.Thor.valueOf()) {
      // Bech32
      scriptAsb.clearBuffer(Buffer.CACHE2);
      // expanded human readable part of "thor"
      scriptAsb.copyString("030303030014080f12", Buffer.CACHE2);
      // checksum to buffer 2
      scriptAsb.baseConvert(
          argTo,
          Buffer.CACHE2,
          32,
          ScriptAssembler.binary32Charset,
          ScriptAssembler.bitLeftJustify8to5);
      scriptAsb
          .copyString("000000000000", Buffer.CACHE2)
          .bech32Polymod(ScriptData.getDataBufferAll(Buffer.CACHE2), Buffer.CACHE1)
          .clearBuffer(Buffer.CACHE2)
          .baseConvert(
              ScriptData.getDataBufferAll(Buffer.CACHE1),
              Buffer.CACHE2,
              6,
              ScriptAssembler.base32BitcoinCashCharset,
              0)
          .clearBuffer(Buffer.CACHE1);
      scriptAsb.copyString(HexUtil.toHexString("thor1"), Buffer.CACHE1);
      scriptAsb
          .baseConvert(
              argTo,
              Buffer.CACHE1,
              32,
              ScriptAssembler.base32BitcoinCashCharset,
              ScriptAssembler.bitLeftJustify8to5);
      scriptAsb
          .copyArgument(ScriptData.getDataBufferAll(Buffer.CACHE2), Buffer.CACHE1)
          .showAddress(ScriptData.getDataBufferAll(Buffer.CACHE1));
    } else {
      scriptAsb.showAddress(argTo);
    }
    scriptAsb.setBufferInt(argDecimal, 1, 24);
    scriptAsb.showAmount(argAmount, ScriptData.bufInt);
    scriptAsb.showPressButton();
    // version=03 ScriptAssembler.hash=02=sha256 sign=01=ECDSA
    return scriptAsb.setHeader(HashType.SHA256, SignType.ECDSA).getScript();
  }

  public static String getCosmosWithdrawScript(int coinType) {
    ScriptArgumentComposer sac = new ScriptArgumentComposer();
    ScriptData argPublicKey = sac.getArgument(33);
    ScriptData argFrom = sac.getArgumentRightJustified(64);
    ScriptData argTo = sac.getArgumentRightJustified(64);
    ScriptData argAmount = sac.getArgument(8);
    ScriptData argFeeAmount = sac.getArgument(8);
    ScriptData argGas = sac.getArgument(8);
    ScriptData argAccountNumber = sac.getArgument(8);
    ScriptData argSequence = sac.getArgument(8);
    // amount
    ScriptData argDenomInfo = sac.getArgumentUnion(0, 18);
    ScriptData argDecimal = sac.getArgument(1);
    ScriptData argDenom = sac.getArgumentRightJustified(10);
    ScriptData argDenomSymbol = sac.getArgumentRightJustified(7);
    ScriptData argDenomSignature = sac.getArgument(72);
    // fee
    ScriptData argFeeDenomInfo = sac.getArgumentUnion(0, 18);
    ScriptData argFeeDecimal = sac.getArgument(1);
    ScriptData argFeeDenom = sac.getArgumentRightJustified(10);
    ScriptData argFeeDenomSymbol = sac.getArgumentRightJustified(7);
    ScriptData argFeeDenomSignature = sac.getArgument(72);
    // chainId and symbol
    ScriptData argChainInfo = sac.getArgumentUnion(0, 59);
    ScriptData argChainIdLength = sac.getArgument(1);
    ScriptData argChainId = sac.getArgumentVariableLength(50);
    ScriptData argSymbolLength = sac.getArgument(1);
    ScriptData argSymbol = sac.getArgumentVariableLength(7);
    ScriptData argChainInfoSignature = sac.getArgument(72);

    ScriptData argMemoLength = sac.getArgument(1);
    ScriptData argMemo = sac.getArgumentVariableLength(127);
    ScriptAssembler scriptAsb = new ScriptAssembler();
    CosmosTransactionMaker maker = new CosmosTransactionMaker(scriptAsb);
    scriptAsb.setCoinType(coinType);
    scriptAsb.ifSigned(argChainInfo, argChainInfoSignature, "", ScriptAssembler.throwSEError);
    scriptAsb.ifSigned(argDenomInfo, argDenomSignature, "", ScriptAssembler.throwSEError);
    // tx_body
    scriptAsb.copyString("0a");
    scriptAsb.arrayPointer();
    scriptAsb.copyString("0a");
    maker.makeMsgWithoutCoin(MsgType.WITHDRAW.toString(), argFrom, argTo);
    // memo
    maker.makeMemo(argMemoLength, argMemo);
    scriptAsb.arrayEnd(); // end tx_body
    // auth_info
    scriptAsb.copyString("12");
    maker.makeAuthInfo(
        coinType,
        argFeeDenomInfo,
        argFeeDenom,
        argFeeDenomSignature,
        argFeeAmount,
        argGas,
        argPublicKey,
        argSequence);
    // chain_id
    scriptAsb.copyString("1a");
    scriptAsb.setBufferInt(argChainIdLength, 1, 50);
    scriptAsb.protobuf(argChainId, WireType.String.valueOf());
    // account_number
    scriptAsb.copyString("20");
    scriptAsb.protobuf(argAccountNumber, WireType.Int.valueOf());
    // display
    scriptAsb.setBufferInt(argSymbolLength, 1, 7);
    scriptAsb.showMessage(argSymbol);
    scriptAsb.showMessage(argDenomSymbol);
    scriptAsb.showMessage("Reward");
    scriptAsb.showAddress(argTo);
    scriptAsb.showPressButton();
    // version=03 ScriptAssembler.hash=02=sha256 sign=01=ECDSA
    return scriptAsb.setHeader(HashType.SHA256, SignType.ECDSA).getScript();
  }
}
