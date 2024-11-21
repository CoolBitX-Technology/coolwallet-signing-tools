package com.coolbitx.wallet.signing.scriptlib;

import com.coolbitx.wallet.signing.utils.HexUtil;
import com.coolbitx.wallet.signing.utils.ScriptArgumentComposer;
import com.coolbitx.wallet.signing.utils.ScriptAssembler;
import com.coolbitx.wallet.signing.utils.ScriptData;
import com.coolbitx.wallet.signing.utils.ScriptAssembler.HashType;
import com.coolbitx.wallet.signing.utils.ScriptAssembler.SignType;
import com.coolbitx.wallet.signing.utils.ScriptData.Buffer;
import com.google.common.base.Strings;

public class TonScript {

    public static void main(String[] args) {
        listAll();
    }

    public static void listAll() {
        System.out.println("Ton Transfer: \n" + getTonTransferScript() + "\n");
        System.out.println("Ton Token Transfer: \n" + getTonTokenTransferScript() + "\n");
        System.out.println("Ton Token Transfer Blind: \n" + getTonTokenTransferBlindScript() + "\n");
    }

    public static String getTonTransferScript() {

        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptData seqno = sac.getArgument(4);
        ScriptData expireAt = sac.getArgument(4);
        ScriptData sendMode = sac.getArgument(1);
        ScriptData cell2Length = sac.getArgument(8);
        ScriptData isBounceable = sac.getArgument(1);
        ScriptData receiver = sac.getArgument(256);
        ScriptData amountLength = sac.getArgument(4);
        ScriptData amount = sac.getArgumentVariableLength(120);
        ScriptData memoLength = sac.getArgument(2);
        ScriptData memo = sac.getArgumentVariableLength(512);

        String script = new ScriptAssembler()
                // coinType: 0x8000025f(607)
                .setCoinType(0x025f) 
                
                // cell1 data (bytes data)
                .copyString("01", Buffer.CACHE1) // cell1's refs length is 1
                .copyString("1C", Buffer.CACHE1) // the hex length is 28 (from walletId to sendMode)
                .copyString("29A9A317", Buffer.CACHE1) // v4r2 walletId: 698983191
                .ifEqual(
                    seqno,
                    "00000000",
                    new ScriptAssembler().copyString("FFFFFFFF", Buffer.CACHE1).getScript(), // seqno: 0 -> FFFFFFFF
                    new ScriptAssembler().copyArgument(expireAt, Buffer.CACHE1).getScript() // seqno > 0 -> expireAt
                )
                .copyArgument(seqno, Buffer.CACHE1) // seqno
                .copyString("00", Buffer.CACHE1) // op
                .copyArgument(sendMode, Buffer.CACHE1)
                .copyString("0000", Buffer.CACHE1) // cell's max depth is 0, please refer Cell.getMaxDepthAsArray.
                
                  // cell2 data (bits data)
                .copyString("0000000000000000") // cell2's refs length is 0
                .copyArgument(cell2Length)
                .copyString("00") // headFlag: false
                .copyString("01") // ihrDisabled: true
                .copyArgument(isBounceable)
                .copyString("00") // bounced: false
                .copyString("0000") // src: null
                .copyString("010000") // dest: divider
                .copyString("0000000000000000") // work chain: 0
                .copyArgument(receiver)
                .copyArgument(amountLength)
                .copyString("00", Buffer.CACHE2) // -----------trim amount start
                .copyArgument(amountLength, Buffer.CACHE2)
                .copyString("000000", Buffer.CACHE2) // mutilple by 8 to trim amount
                .bitToByte(ScriptData.getDataBufferAll(Buffer.CACHE2), Buffer.CACHE2)
                .setBufferIntUnsafe(ScriptData.getDataBufferAll(Buffer.CACHE2, 8))
                .clearBuffer(Buffer.CACHE2) // -----------trim amount end
                .copyArgument(amount)
                .copyString("00") // currencyCollection: false
                .copyString("00000000") // ihrFees
                .copyString("00000000") // fwdFees
                .copyString("00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000") // createdLt (64 bits 0)
                .copyString("0000000000000000000000000000000000000000000000000000000000000000") // createdAt (32 bits 0)
                .copyString("00") // stateInit: null
                .copyString("00") // payload(memo) divider
                .setBufferIntUnsafe(memoLength)
                .copyArgument(memo)

                
                // hash cell2 data and then append to cell1
                .bitToByte(ScriptData.getDataBufferAll(Buffer.TRANSACTION), Buffer.CACHE2)
                .clearBuffer(Buffer.TRANSACTION)
                .hash(ScriptData.getDataBufferAll(Buffer.CACHE2), Buffer.CACHE1, HashType.SHA256)
                .clearBuffer(Buffer.CACHE2)
                .copyArgument(ScriptData.getDataBufferAll(Buffer.CACHE1), Buffer.TRANSACTION)
                .clearBuffer(Buffer.CACHE1)

                // show message on card
                .showMessage("TON")
                
                // show receiver address
                .ifEqual( 
                        isBounceable,
                        "01",
                        new ScriptAssembler().copyString("11", Buffer.CACHE2).getScript(), // bounceable_tag = 0x11
                        new ScriptAssembler().copyString("51", Buffer.CACHE2).getScript() // non_bounceable_tag = 0x51
                ) 
                .copyString("00", Buffer.CACHE2) // work chain: 0
                .bitToByte(receiver, Buffer.CACHE2)
                .hash(ScriptData.getDataBufferAll(Buffer.CACHE2), Buffer.CACHE2, HashType.CRC16)
                .copyString(HexUtil.toHexString("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-_"), Buffer.CACHE1) // '-', '_' is for ton's user friendly address
                .baseConvert(ScriptData.getBuffer(Buffer.CACHE2, 0, 36), Buffer.CACHE2, 48, ScriptAssembler.cache1Charset, ScriptAssembler.zeroInherit)
                .showAddress(ScriptData.getDataBufferAll(Buffer.CACHE2, 36))
                .clearBuffer(Buffer.CACHE1)
                .clearBuffer(Buffer.CACHE2)
                
                 // show amount
                .copyString("00", Buffer.CACHE1)
                .copyArgument(amountLength, Buffer.CACHE1)
                .copyString("000000", Buffer.CACHE1) // mutilple by 8 to trim amount
                .bitToByte(ScriptData.getDataBufferAll(Buffer.CACHE1), Buffer.CACHE1)
                .setBufferIntUnsafe(ScriptData.getDataBufferAll(Buffer.CACHE1, 8))
                .clearBuffer(Buffer.CACHE1)
                .bitToByte(amount, Buffer.CACHE1)
                .showAmount(ScriptData.getDataBufferAll(Buffer.CACHE1), 9)
                .clearBuffer(Buffer.CACHE1)
                
                // press card
                .showPressButton()
                .setHeader(HashType.SHA256, SignType.EDDSA)
                .getScript();
        
        return script;
    }

    public static String getTonTransferScriptSignature = Strings.padEnd("FA", 144, '0');
    
    public static String getTonTokenTransferScript() {

        ScriptArgumentComposer sac = new ScriptArgumentComposer();

        // Cell3
        ScriptData cell3Length = sac.getArgument(8);
        ScriptData jettonAmountLength = sac.getArgument(4);
        ScriptData jettonAmount = sac.getArgumentVariableLength(120);
        ScriptData receiver = sac.getArgument(256);
        ScriptData receiverWorkchain = sac.getArgument(8);
        ScriptData responser = sac.getArgument(256);
        ScriptData responserWorkchain = sac.getArgument(8);
        ScriptData forwardAmountLength = sac.getArgument(4);
        ScriptData forwardAmount = sac.getArgumentVariableLength(120);
        ScriptData memoLength = sac.getArgument(2);
        ScriptData memo = sac.getArgumentVariableLength(512);
        
        // Cell2
        ScriptData cell2Length = sac.getArgument(8);
        ScriptData fromTokenAccount = sac.getArgument(256);
        ScriptData fromTokenAccountIsBounceable = sac.getArgument(1);
        ScriptData fromTokenAccountWorkchain = sac.getArgument(8);
        ScriptData amountLength = sac.getArgument(4);
        ScriptData amount = sac.getArgumentVariableLength(120);
        
        // Cell1
        ScriptData seqno = sac.getArgument(4);
        ScriptData expireAt = sac.getArgument(4);
        ScriptData sendMode = sac.getArgument(1);
        
        // Token Info
        ScriptData tokenInfo = sac.getArgumentUnion(0, 45);
        ScriptData tokenDecimal = sac.getArgument(1);
        ScriptData tokenNameLength = sac.getArgument(1);
        ScriptData tokenName = sac.getArgumentVariableLength(7);
        ScriptData tokenContractAddress = sac.getArgument(36);
        ScriptData tokenSign = sac.getArgument(72);
        
        String script = new ScriptAssembler()
                // coinType: 0x8000025f(607)
                .setCoinType(0x025f) 
                
                // cell3 data (bits data)
                .copyString("0000000000000000") // cell3's refs length is 0
                .copyArgument(cell3Length)
                .copyString("0000000001010101010000000100010000010101010101000100010000010001") // op code 0xf8a7ea5
                .copyString("00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000") // queryId (8B)
                .copyArgument(jettonAmountLength)
                .copyString("00", Buffer.CACHE2) // -----------trim amount start
                .copyArgument(jettonAmountLength, Buffer.CACHE2)
                .copyString("000000", Buffer.CACHE2) // mutilple by 8 to trim amount
                .bitToByte(ScriptData.getDataBufferAll(Buffer.CACHE2), Buffer.CACHE2)
                .setBufferIntUnsafe(ScriptData.getDataBufferAll(Buffer.CACHE2, 8))
                .clearBuffer(Buffer.CACHE2) // -----------trim amount end
                .copyArgument(jettonAmount)
                .copyString("010000") // address's divider
                .copyArgument(receiverWorkchain) // work chain: 0 or 1
                .copyArgument(receiver)
                .copyString("010000") // address's divider
                .copyArgument(responserWorkchain) // work chain: 0 or 1
                .copyArgument(responser)
                .copyString("00") // custom_payload null
                .copyArgument(forwardAmountLength)
                .copyString("00", Buffer.CACHE2) // -----------trim amount start
                .copyArgument(forwardAmountLength, Buffer.CACHE2)
                .copyString("000000", Buffer.CACHE2) // mutilple by 8 to trim amount
                .bitToByte(ScriptData.getDataBufferAll(Buffer.CACHE2), Buffer.CACHE2)
                .setBufferIntUnsafe(ScriptData.getDataBufferAll(Buffer.CACHE2, 8))
                .clearBuffer(Buffer.CACHE2) // -----------trim amount end
                .copyArgument(forwardAmount)
                .copyString("00") // both has memo or not are 0b0
                .setBufferIntUnsafe(memoLength)
                .copyArgument(memo)
                .bitToByte(ScriptData.getDataBufferAll(Buffer.TRANSACTION), Buffer.CACHE2)
                .clearBuffer(Buffer.TRANSACTION)
                .hash(ScriptData.getDataBufferAll(Buffer.CACHE2), Buffer.CACHE1, HashType.SHA256) // cell3's hash has saved.
                .clearBuffer(Buffer.CACHE2)


                // cell2 data (bits data)
                .copyString("0000000000000001") // cell2's refs length is 1
                .copyArgument(cell2Length)
                .copyString("00") // headFlag: false
                .copyString("01") // ihrDisabled: true
                .copyArgument(fromTokenAccountIsBounceable)
                .copyString("00") // bounced: false
                .copyString("0000") // src: null
                .copyString("010000") // dest: divider
                .copyArgument(fromTokenAccountWorkchain) // work chain: 0 or 1
                .copyArgument(fromTokenAccount)
                .copyArgument(amountLength)
                .copyString("00", Buffer.CACHE2) // -----------trim amount start
                .copyArgument(amountLength, Buffer.CACHE2)
                .copyString("000000", Buffer.CACHE2) // mutilple by 8 to trim amount
                .bitToByte(ScriptData.getDataBufferAll(Buffer.CACHE2), Buffer.CACHE2)
                .setBufferIntUnsafe(ScriptData.getDataBufferAll(Buffer.CACHE2, 8))
                .clearBuffer(Buffer.CACHE2) // -----------trim amount end
                .copyArgument(amount)
                .copyString("00") // currencyCollection: false
                .copyString("00000000") // ihrFees
                .copyString("00000000") // fwdFees
                .copyString("00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000") // createdLt (64 bits 0)
                .copyString("0000000000000000000000000000000000000000000000000000000000000000") // createdAt (32 bits 0)
                .copyString("00") // stateInit: null
                .copyString("01") // payload(memo) divider
                .copyString("00000000000000000000000000000000") // cell's max depth is 0, please refer Cell.getMaxDepthAsArray.
                .bitToByte(ScriptData.getDataBufferAll(Buffer.TRANSACTION), Buffer.CACHE2)
                .clearBuffer(Buffer.TRANSACTION)
                .copyArgument(ScriptData.getDataBufferAll(Buffer.CACHE1), Buffer.CACHE2)
                .clearBuffer(Buffer.CACHE1)
                .hash(ScriptData.getDataBufferAll(Buffer.CACHE2), Buffer.CACHE1, HashType.SHA256) // cell2's hash has saved.
                .clearBuffer(Buffer.CACHE2)

                // cell1 data (bytes data)
                .copyString("01") // cell1's refs length is 1
                .copyString("1C") // the hex length is 28 (from walletId to sendMode)
                .copyString("29A9A317") // v4r2 walletId: 698983191
                .ifEqual(
                    seqno,
                    "00000000",
                    new ScriptAssembler().copyString("FFFFFFFF").getScript(), // seqno: 0 -> FFFFFFFF
                    new ScriptAssembler().copyArgument(expireAt).getScript() // seqno > 0 -> expireAt
                )
                .copyArgument(seqno) // seqno
                .copyString("00") // op
                .copyArgument(sendMode)
                .copyString("0001") // cell's max depth is 1, please refer Cell.getMaxDepthAsArray.
                .copyArgument(ScriptData.getDataBufferAll(Buffer.CACHE1))
                .clearBuffer(Buffer.CACHE1)

                // show chain
                .showMessage("TON")
                
                // show token
                .ifSigned(
                        tokenInfo,
                        tokenSign,
                        "",
                        new ScriptAssembler().copyString(HexUtil.toHexString("@"), Buffer.CACHE2).getScript()
                )
                .setBufferInt(tokenNameLength, 1, 7)
                .copyArgument(tokenName, Buffer.CACHE2)
                .showMessage(ScriptData.getDataBufferAll(Buffer.CACHE2))
                .clearBuffer(Buffer.CACHE2)
                
                // show receiver address
                .ifEqual(fromTokenAccountIsBounceable,
                        "01",
                        new ScriptAssembler().copyString("11", Buffer.CACHE2).getScript(), // bounceable_tag = 0x11
                        new ScriptAssembler().copyString("51", Buffer.CACHE2).getScript() // non_bounceable_tag = 0x51
                ) 
                .bitToByte(receiverWorkchain, Buffer.CACHE2) // work chain: 0 or 1
                .bitToByte(receiver, Buffer.CACHE2)
                .hash(ScriptData.getDataBufferAll(Buffer.CACHE2), Buffer.CACHE2, HashType.CRC16)
                .copyString(HexUtil.toHexString("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-_"), Buffer.CACHE1) // '-', '_' is for ton's user friendly address
                .baseConvert(ScriptData.getBuffer(Buffer.CACHE2, 0, 36), Buffer.CACHE2, 48, ScriptAssembler.cache1Charset, ScriptAssembler.zeroInherit)
                .showAddress(ScriptData.getDataBufferAll(Buffer.CACHE2, 36))
                .clearBuffer(Buffer.CACHE1)
                .clearBuffer(Buffer.CACHE2)

                // show amount
                .copyString("00", Buffer.CACHE1)
                .copyArgument(jettonAmountLength, Buffer.CACHE1)
                .copyString("000000", Buffer.CACHE1) // mutilple by 8 to trim amount
                .bitToByte(ScriptData.getDataBufferAll(Buffer.CACHE1), Buffer.CACHE1)
                .setBufferIntUnsafe(ScriptData.getDataBufferAll(Buffer.CACHE1, 8))
                .clearBuffer(Buffer.CACHE1)
                .bitToByte(jettonAmount, Buffer.CACHE1)
                .setBufferInt(tokenDecimal, 0, 30)
                .showAmount(ScriptData.getDataBufferAll(Buffer.CACHE1), ScriptData.bufInt)
                .clearBuffer(Buffer.CACHE1)

                // press card
                .showPressButton()
                .setHeader(HashType.SHA256, SignType.EDDSA)
                .getScript();
        
        return script;
    }
    
    public static String getTonTokenTransferBlindScript() {

        ScriptArgumentComposer sac = new ScriptArgumentComposer();

        // Cell3
        ScriptData cell3Length = sac.getArgument(8);
        ScriptData jettonAmountLength = sac.getArgument(4);
        ScriptData jettonAmount = sac.getArgumentVariableLength(120);
        ScriptData receiver = sac.getArgument(256);
        ScriptData receiverWorkchain = sac.getArgument(8);
        ScriptData responser = sac.getArgument(256);
        ScriptData responserWorkchain = sac.getArgument(8);
        ScriptData forwardAmountLength = sac.getArgument(4);
        ScriptData forwardAmount = sac.getArgumentVariableLength(120);
        ScriptData memoLength = sac.getArgument(2);
        ScriptData memo = sac.getArgumentVariableLength(512);
        
        // Cell2
        ScriptData cell2Length = sac.getArgument(8);
        ScriptData fromTokenAccount = sac.getArgument(256);
        ScriptData fromTokenAccountIsBounceable = sac.getArgument(1);
        ScriptData fromTokenAccountWorkchain = sac.getArgument(8);
        ScriptData amountLength = sac.getArgument(4);
        ScriptData amount = sac.getArgumentVariableLength(120);
        
        // Cell1
        ScriptData seqno = sac.getArgument(4);
        ScriptData expireAt = sac.getArgument(4);
        ScriptData sendMode = sac.getArgument(1);
        
        // Token Info
        ScriptData tokenInfo = sac.getArgumentUnion(0, 45);
        ScriptData tokenDecimal = sac.getArgument(1);
        ScriptData tokenNameLength = sac.getArgument(1);
        ScriptData tokenName = sac.getArgumentVariableLength(7);
        ScriptData tokenContractAddress = sac.getArgument(36);
        ScriptData tokenSign = sac.getArgument(72);
        
        String script = new ScriptAssembler()
                // coinType: 0x8000025f(607)
                .setCoinType(0x025f) 
                
                // cell3 data (bits data)
                .copyString("0000000000000000") // cell3's refs length is 0
                .copyArgument(cell3Length)
                .copyString("0000000001010101010000000100010000010101010101000100010000010001") // op code 0xf8a7ea5
                .copyString("00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000") // queryId (8B)
                .copyArgument(jettonAmountLength)
                .copyString("00", Buffer.CACHE2) // -----------trim amount start
                .copyArgument(jettonAmountLength, Buffer.CACHE2)
                .copyString("000000", Buffer.CACHE2) // mutilple by 8 to trim amount
                .bitToByte(ScriptData.getDataBufferAll(Buffer.CACHE2), Buffer.CACHE2)
                .setBufferIntUnsafe(ScriptData.getDataBufferAll(Buffer.CACHE2, 8))
                .clearBuffer(Buffer.CACHE2) // -----------trim amount end
                .copyArgument(jettonAmount)
                .copyString("010000") // address's divider
                .copyArgument(receiverWorkchain) // work chain: 0 or 1
                .copyArgument(receiver)
                .copyString("010000") // address's divider
                .copyArgument(responserWorkchain) // work chain: 0 or 1
                .copyArgument(responser)
                .copyString("00") // custom_payload null
                .copyArgument(forwardAmountLength)
                .copyString("00", Buffer.CACHE2) // -----------trim amount start
                .copyArgument(forwardAmountLength, Buffer.CACHE2)
                .copyString("000000", Buffer.CACHE2) // mutilple by 8 to trim amount
                .bitToByte(ScriptData.getDataBufferAll(Buffer.CACHE2), Buffer.CACHE2)
                .setBufferIntUnsafe(ScriptData.getDataBufferAll(Buffer.CACHE2, 8))
                .clearBuffer(Buffer.CACHE2) // -----------trim amount end
                .copyArgument(forwardAmount)
                .copyString("00") // both has memo or not are 0b0
                .setBufferIntUnsafe(memoLength)
                .copyArgument(memo)
                .bitToByte(ScriptData.getDataBufferAll(Buffer.TRANSACTION), Buffer.CACHE2)
                .clearBuffer(Buffer.TRANSACTION)
                .hash(ScriptData.getDataBufferAll(Buffer.CACHE2), Buffer.CACHE1, HashType.SHA256) // cell3's hash has saved.
                .clearBuffer(Buffer.CACHE2)


                // cell2 data (bits data)
                .copyString("0000000000000001") // cell2's refs length is 1
                .copyArgument(cell2Length)
                .copyString("00") // headFlag: false
                .copyString("01") // ihrDisabled: true
                .copyArgument(fromTokenAccountIsBounceable)
                .copyString("00") // bounced: false
                .copyString("0000") // src: null
                .copyString("010000") // dest: divider
                .copyArgument(fromTokenAccountWorkchain) // work chain: 0 or 1
                .copyArgument(fromTokenAccount)
                .copyArgument(amountLength)
                .copyString("00", Buffer.CACHE2) // -----------trim amount start
                .copyArgument(amountLength, Buffer.CACHE2)
                .copyString("000000", Buffer.CACHE2) // mutilple by 8 to trim amount
                .bitToByte(ScriptData.getDataBufferAll(Buffer.CACHE2), Buffer.CACHE2)
                .setBufferIntUnsafe(ScriptData.getDataBufferAll(Buffer.CACHE2, 8))
                .clearBuffer(Buffer.CACHE2) // -----------trim amount end
                .copyArgument(amount)
                .copyString("00") // currencyCollection: false
                .copyString("00000000") // ihrFees
                .copyString("00000000") // fwdFees
                .copyString("00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000") // createdLt (64 bits 0)
                .copyString("0000000000000000000000000000000000000000000000000000000000000000") // createdAt (32 bits 0)
                .copyString("00") // stateInit: null
                .copyString("01") // payload(memo) divider
                .copyString("00000000000000000000000000000000") // cell's max depth is 0, please refer Cell.getMaxDepthAsArray.
                .bitToByte(ScriptData.getDataBufferAll(Buffer.TRANSACTION), Buffer.CACHE2)
                .clearBuffer(Buffer.TRANSACTION)
                .copyArgument(ScriptData.getDataBufferAll(Buffer.CACHE1), Buffer.CACHE2)
                .clearBuffer(Buffer.CACHE1)
                .hash(ScriptData.getDataBufferAll(Buffer.CACHE2), Buffer.CACHE1, HashType.SHA256) // cell2's hash has saved.
                .clearBuffer(Buffer.CACHE2)

                // cell1 data (bytes data)
                .copyString("01") // cell1's refs length is 1
                .copyString("1C") // the hex length is 28 (from walletId to sendMode)
                .copyString("29A9A317") // v4r2 walletId: 698983191
                .ifEqual(
                    seqno,
                    "00000000",
                    new ScriptAssembler().copyString("FFFFFFFF").getScript(), // seqno: 0 -> FFFFFFFF
                    new ScriptAssembler().copyArgument(expireAt).getScript() // seqno > 0 -> expireAt
                )
                .copyArgument(seqno) // seqno
                .copyString("00") // op
                .copyArgument(sendMode)
                .copyString("0001") // cell's max depth is 1, please refer Cell.getMaxDepthAsArray.
                .copyArgument(ScriptData.getDataBufferAll(Buffer.CACHE1))
                .clearBuffer(Buffer.CACHE1)

                // show chain
                .showMessage("TON")
                
                // display smart
                .showWrap("SMART", "")

                // press card
                .showPressButton()
                .setHeader(HashType.SHA256, SignType.EDDSA)
                .getScript();
        
        return script;
    }
    
    public static String TonTokenTransferBlindScriptSignature = Strings.padStart("3046022100dec0d0eeb203bba1d842868db59088e9efec3de630bc4b82dc223123d76857830221009f7de912cc81ef28c5fe5512be2f641b807ab63ef297f7d26cf3e9dd4bd53ebc", 144, '0');
}
