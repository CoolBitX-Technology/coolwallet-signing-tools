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

}
