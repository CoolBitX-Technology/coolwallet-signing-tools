package com.coolbitx.wallet.signing.scriptlib;

import com.coolbitx.wallet.signing.utils.*;
import com.coolbitx.wallet.signing.utils.ScriptData.Buffer;

public class FilScript {
    public static void listAll() {
        System.out.println("COIN-Fil SCRIPTS");
        System.out.println("==================================================");
        System.out.println("Normal and Smart Transaction: \n" + getTransactionScript() + "\n");
        System.out.println("==================================================");
    }

    //             8a (Array)
    // version     00 (Uint)
    // to          55 (Byte) 01e10e644ce8f5a28e19d11a8d0e7b5b561ccecdb9
    // from        55 (Byte) 01347aa4d721acf77d2c2a75948fcf5b2d242868b5
    // nonce       01 (Uint)
    // value       4a (Byte) 00056bc75e2d63100000
    // gasLimit    1a (Uint) 00095501
    // gasFeeCap   44 (Byte) 000189ea
    // gasPremium  44 (Byte) 000185cc
    // Method      00 (Uint)
    // Params      40 (Byte)

    // Protocol 0. ID:        f0 | base10( leb128-varint )
    // Protocol 1. SECP256K1: f1 | base32( blake2b-160        | 4 bytes)
    // Protocol 2. Actor:     f2 | base32( SHA256             | 4 bytes)
    // Protocol 3. BLS:       f3 | base32( 48 byte BLS PubKey | 4 bytes)

    public static String getTransactionScript() {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();

        ScriptData toPrefix = sac.getArgument(1);
        ScriptData toArgument = sac.getArgument(1);
        ScriptData toProtocol = sac.getArgument(1);
        ScriptData toPayloadType = sac.getArgument(1); // 0: varint, 1: 20 bytes, 2: 32 bytes, 3: 48 bytes
        ScriptData toPayload = sac.getArgument(48);
        ScriptData toChecksum = sac.getArgument(4);

        ScriptData fromPayload = sac.getArgument(20);

        ScriptData noncePrefix = sac.getArgument(1);
        ScriptData nonceArgumentLength = sac.getArgument(1);
        ScriptData nonceArgument = sac.getArgumentVariableLength(8);

        ScriptData valuePrefix = sac.getArgument(1);
        ScriptData valueArgument = sac.getArgument(1);
        ScriptData valueLength = sac.getArgument(1);
        ScriptData value = sac.getArgumentVariableLength(16);

        ScriptData gasArgsLength = sac.getArgument(1);
        ScriptData gasArgs = sac.getArgumentVariableLength(40);

        ScriptData smartExist = sac.getArgument(1);
        ScriptData smartArguments = sac.getArgumentAll();

        ScriptAssembler scriptAsb = new ScriptAssembler();
        String script = scriptAsb.setCoinType(0x1cd)

            // ---- Payload Start ----

            // Array and Version

            .copyString("8a00")

            // To Address

            .copyArgument(toPrefix)
            .ifEqual(toArgument, "00", "", new ScriptAssembler().copyArgument(toArgument).getScript())
            .copyArgument(toProtocol)
            .ifEqual(toPayloadType, "00",
                new ScriptAssembler()
                    .varint(toPayload, Buffer.TRANSACTION)
                    .getScript(),
                new ScriptAssembler()
                    .ifEqual(toPayloadType, "01",
                        new ScriptAssembler()
                          .copyArgument(toPayload, Buffer.CACHE1)
                          .copyArgument(ScriptData.getDataBufferAll(Buffer.CACHE1, 28))
                          .getScript(), "")
                    .ifEqual(toPayloadType, "02",
                        new ScriptAssembler()
                          .copyArgument(toPayload, Buffer.CACHE1)
                          .copyArgument(ScriptData.getDataBufferAll(Buffer.CACHE1, 16))
                          .getScript(), "")
                    .ifEqual(toPayloadType, "03",
                        new ScriptAssembler()
                          .copyArgument(toPayload)
                          .getScript(), "")
                    .clearBuffer(Buffer.CACHE1)
                    .getScript()
            )

            // From Address

            .copyString("5501")
            .copyArgument(fromPayload)

            // Nonce

            .copyArgument(noncePrefix)
            .setBufferInt(nonceArgumentLength, 0, 8)
            .copyArgument(nonceArgument)

            // Value

            .copyArgument(valuePrefix)
            .ifEqual(valueArgument, "00", "", new ScriptAssembler().copyArgument(valueArgument).getScript())
            .setBufferInt(valueLength, 0, 16)
            .copyArgument(value)

            // GasLimit and GasFeeCap and GasPremium

            .setBufferInt(gasArgsLength, 1, 40)
            .copyArgument(gasArgs)

            // Method and Params

            .ifEqual(smartExist, "00",
                new ScriptAssembler()
                    .copyString("0040")
                    .showMessage("FIL")
                    .getScript(),
                new ScriptAssembler()
                    .copyArgument(smartArguments)
                    .showMessage("FIL")
                    .showWrap("SMART", "")
                    .getScript()
            )

            // CID

            .copyString("0171a0e40220", Buffer.CACHE1)
            .hash(ScriptData.getDataBufferAll(Buffer.TRANSACTION), Buffer.CACHE1, ScriptAssembler.HashType.Blake2b256)
            .clearBuffer(Buffer.TRANSACTION)
            .copyArgument(ScriptData.getDataBufferAll(Buffer.CACHE1))
            .clearBuffer(Buffer.CACHE1)

            // Show Address

            .ifEqual(toPayloadType, "00",
                new ScriptAssembler()
                    .baseConvert(toPayload,
                        Buffer.CACHE2, 0, ScriptAssembler.decimalCharset,
                        ScriptAssembler.leftJustify)
                    .getScript(),
                new ScriptAssembler()
                    .copyArgument(toPayload, Buffer.CACHE1)
                    .ifEqual(toPayloadType, "01",
                        new ScriptAssembler()
                            .copyArgument(ScriptData.getDataBufferAll(Buffer.CACHE1, 28), Buffer.CACHE2).getScript(), "")
                    .ifEqual(toPayloadType, "02",
                        new ScriptAssembler()
                            .copyArgument(ScriptData.getDataBufferAll(Buffer.CACHE1, 16), Buffer.CACHE2).getScript(), "")
                    .ifEqual(toPayloadType, "03",
                        new ScriptAssembler()
                            .copyArgument(ScriptData.getDataBufferAll(Buffer.CACHE1), Buffer.CACHE2).getScript(), "")
                    .copyArgument(toChecksum, Buffer.CACHE2)
                    .clearBuffer(Buffer.CACHE1)
                    .copyString(HexUtil.toHexString("ABCDEFGHIJKLMNOPQRSTUVWXYZ234567"), Buffer.CACHE1)
                    .ifEqual(toPayloadType, "01",
                        new ScriptAssembler()
                            .baseConvert(ScriptData.getBuffer(Buffer.CACHE2, 0, 24),
                                Buffer.CACHE1, 39, ScriptAssembler.cache1Charset,
                                ScriptAssembler.bitLeftJustify8to5).getScript(), "")
                    .ifEqual(toPayloadType, "02",
                        new ScriptAssembler()
                            .baseConvert(ScriptData.getBuffer(Buffer.CACHE2, 0, 36),
                                Buffer.CACHE1, 58, ScriptAssembler.cache1Charset,
                                ScriptAssembler.bitLeftJustify8to5).getScript(), "")
                    .ifEqual(toPayloadType, "03",
                        new ScriptAssembler()
                            .baseConvert(ScriptData.getBuffer(Buffer.CACHE2, 0, 48),
                                Buffer.CACHE1, 77, ScriptAssembler.cache1Charset,
                                ScriptAssembler.bitLeftJustify8to5).getScript(), "")
                    .clearBuffer(Buffer.CACHE2)
                    .copyArgument(ScriptData.getDataBufferAll(Buffer.CACHE1, 32), Buffer.CACHE2)
                    .clearBuffer(Buffer.CACHE1)
                    .getScript()
            )
            .copyString(HexUtil.toHexString("F"), Buffer.CACHE1)
            .baseConvert(toProtocol, Buffer.CACHE1, 0, ScriptAssembler.decimalCharset, ScriptAssembler.leftJustify)
            .copyArgument(ScriptData.getDataBufferAll(Buffer.CACHE2), Buffer.CACHE1)
            .showAddress(ScriptData.getDataBufferAll(Buffer.CACHE1))
            .clearBuffer(Buffer.CACHE1)
            .clearBuffer(Buffer.CACHE2)

            // Show Amount

            .setBufferInt(valueLength, 0, 16)
            .showAmount(value, 18)

            .showPressButton()
            .setHeader(ScriptAssembler.HashType.Blake2b256, ScriptAssembler.SignType.ECDSA)
            .getScript();
        return script;
    }
}
