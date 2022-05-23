/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.coolbitx.wallet.signing.scriptlib;

import com.coolbitx.wallet.signing.utils.ScriptArgumentComposer;
import com.coolbitx.wallet.signing.utils.ScriptAssembler;
import com.coolbitx.wallet.signing.utils.ScriptData;
import com.coolbitx.wallet.signing.utils.ScriptAssembler.HashType;
import com.coolbitx.wallet.signing.utils.ScriptAssembler.SignType;
import com.coolbitx.wallet.signing.utils.ScriptData.Buffer;

public class XtzScript {

    public static void listAll() {
        System.out.println("XTZ Reveal: \n" + getTezosRevealScript() + "\n");
        System.out.println("XTZ Transfer: \n" + getTezosTransferScript() + "\n");
        System.out.println("XTZ Delegation: \n" + getTezosDelegationScript() + "\n");
        System.out.println("XTZ Undelegate: \n" + getTezosUndelegationScript() + "\n");
        //System.out.println("XTZ Contract: \n" + getTezosContractScript() + "\n");
    }

    //private static int typeString = 2;
    private static int typeInt = 0;

    public static String getTezosRevealScript() {
        // Step 1. Define Arguments.
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptData argBranch = sac.getArgument(32);
        ScriptData argSourceAddressType = sac.getArgument(1);
        ScriptData argSourceAddress = sac.getArgument(20);
        ScriptData argFee = sac.getArgumentRightJustified(10);
        ScriptData argCount = sac.getArgumentRightJustified(10);
        ScriptData argGasLimit = sac.getArgumentRightJustified(10);
        ScriptData argStorageLimit = sac.getArgumentRightJustified(10);
        ScriptData argPublicKey = sac.getArgument(33);

        String script = new ScriptAssembler()
                // Step 2. Set BIP-44/SLIP0010 CoinType for validation to the path. Tezos (1729)
                .setCoinType(0x06C1)
                // Step 3. Compose the raw transaction from arguments for signing.
                // Watermark
                .copyString("03")
                // Branch: block hash (32 Bytes)
                .copyArgument(argBranch)
                // Tag: 107 for Reveal (1 Byte)
                .copyString("6B")
                // Source address type (1 Byte)
                .copyArgument(argSourceAddressType)
                // Source pub key hash (20 Bytes)
                .copyArgument(argSourceAddress)
                // fee (variable size)
                .protobuf(argFee, typeInt)
                // count (Variable size)
                .protobuf(argCount, typeInt)
                // gas limit (Variable size)
                .protobuf(argGasLimit, typeInt)
                // storage limit (variable size)
                .protobuf(argStorageLimit, typeInt)
                // public key (33 Bytes)
                .copyArgument(argPublicKey)
                // Step 4. Define which parts of the arguments shall be showed on the screen to be validated.
                .showMessage("XTZ")
                .showMessage("Reveal")
                .showPressButton()
                // Step 5. Set up Script Header.      
                .setHeader(HashType.Blake2b256, SignType.EDDSA)
                .getScript();

        return script;
    }

    public static String getTezosTransferScript() {
        // Step 1. Define Arguments.
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptData argBranch = sac.getArgument(32);
        ScriptData argSourceAddressType = sac.getArgument(1);
        ScriptData argSourceAddress = sac.getArgument(20);
        ScriptData argFee = sac.getArgumentRightJustified(10);
        ScriptData argCount = sac.getArgumentRightJustified(10);
        ScriptData argGasLimit = sac.getArgumentRightJustified(10);
        ScriptData argStorageLimit = sac.getArgumentRightJustified(10);
        ScriptData argAmount = sac.getArgumentRightJustified(10);
        ScriptData argDestinationAccountType = sac.getArgument(1);
        ScriptData argDestinationAddressType = sac.getArgument(1);
        ScriptData argDestinationAddress = sac.getArgument(20);

        String script = new ScriptAssembler()
                // Step 2. Set BIP-44/SLIP0010 CoinType for validation to the path. Tezos (1729)
                .setCoinType(0x06C1)
                // Step 3. Compose the raw transaction from arguments for signing.        
                // Watermaek
                .copyString("03")
                // Branch: block hash (32 Bytes)
                .copyArgument(argBranch)
                // Tag: 108 for Transaction (1 Byte)
                .copyString("6C")
                // Source address type (1 Byte)
                .copyArgument(argSourceAddressType)
                // Source address (20 Bytes)
                .copyArgument(argSourceAddress)
                // fee (variable size)
                .protobuf(argFee, typeInt)
                // count (variable size)
                .protobuf(argCount, typeInt)
                // gas limit (variable size)
                .protobuf(argGasLimit, typeInt)
                // storage limit (variable size)
                .protobuf(argStorageLimit, typeInt)
                // amount (variable size)
                .protobuf(argAmount, typeInt)
                // Destination (22 Bytes)
                .ifEqual(argDestinationAccountType, "00",
                        new ScriptAssembler().copyArgument(argDestinationAccountType).copyArgument(argDestinationAddressType).copyArgument(argDestinationAddress).getScript(),
                        new ScriptAssembler().copyArgument(argDestinationAccountType).copyArgument(argDestinationAddress).copyArgument(argDestinationAddressType).getScript())
                // 0 for transfer 
                .copyString("00")
                // Step 4. Define which parts of the arguments shall be showed on the screen to be validated.
                .showMessage("XTZ")
                //Show receiving address
                .ifEqual(argDestinationAccountType, "01",
                        // Originated accounts (KT) 
                        new ScriptAssembler().copyString("025a79", Buffer.CACHE2).getScript(),
                        // Implicit accounts (tz) 
                        new ScriptAssembler()
                                // Implicit accounts (tz1)
                                .ifEqual(argDestinationAddressType, "00", new ScriptAssembler().copyString("06a19f", Buffer.CACHE2).getScript(), "")
                                // Implicit accounts (tz2)
                                .ifEqual(argDestinationAddressType, "01", new ScriptAssembler().copyString("06a1a1", Buffer.CACHE2).getScript(), "")
                                // Implicit accounts (tz3)
                                .ifEqual(argDestinationAddressType, "02", new ScriptAssembler().copyString("06a1a4", Buffer.CACHE2).getScript(), "")
                                .getScript()
                )
                .copyArgument(argDestinationAddress, Buffer.CACHE2)
                .hash(ScriptData.getDataBufferAll(Buffer.CACHE2), Buffer.CACHE1, HashType.DoubleSHA256)
                .clearBuffer(Buffer.CACHE2)
                // Show receiving address
                .ifEqual(argDestinationAccountType, "01",
                        // Originated accounts (KT) 
                        new ScriptAssembler().copyString("025a79", Buffer.CACHE2).getScript(),
                        // Implicit accounts (tz) 
                        new ScriptAssembler()
                                // Implicit accounts (tz1)
                                .ifEqual(argDestinationAddressType, "00", new ScriptAssembler().copyString("06a19f", Buffer.CACHE2).getScript(), "")
                                // Implicit accounts (tz2)
                                .ifEqual(argDestinationAddressType, "01", new ScriptAssembler().copyString("06a1a1", Buffer.CACHE2).getScript(), "")
                                // Implicit accounts (tz3)
                                .ifEqual(argDestinationAddressType, "02", new ScriptAssembler().copyString("06a1a4", Buffer.CACHE2).getScript(), "")
                                .getScript()
                )
                .copyArgument(argDestinationAddress, Buffer.CACHE2)
                .copyArgument(ScriptData.getBuffer(Buffer.CACHE1, 0, 4), Buffer.CACHE2)
                .clearBuffer(Buffer.CACHE1)
                .baseConvert(ScriptData.getDataBufferAll(Buffer.CACHE2), Buffer.CACHE1, 0, ScriptAssembler.base58Charset, ScriptAssembler.zeroInherit)
                .showAddress(ScriptData.getDataBufferAll(Buffer.CACHE1))
                // Show Amount
                .showAmount(argAmount, 6)
                .showPressButton()
                // Step 5. Set up Script Header.
                .setHeader(HashType.Blake2b256, SignType.EDDSA)
                .getScript();

        return script;
    }

    // TBD
    public static String getTezosContractScript() {
        // Step 1. Define Arguments.
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptData argBranch = sac.getArgument(32);
        ScriptData argSourceAddressType = sac.getArgument(1);
        ScriptData argSourceAddress = sac.getArgument(20);
        ScriptData argFee = sac.getArgumentRightJustified(10);
        ScriptData argCount = sac.getArgumentRightJustified(10);
        ScriptData argGasLimit = sac.getArgumentRightJustified(10);
        ScriptData argStorageLimit = sac.getArgumentRightJustified(10);
        ScriptData argAmount = sac.getArgumentRightJustified(10);
        ScriptData argDestinationAccountType = sac.getArgument(1);
        ScriptData argDestinationAddressType = sac.getArgument(1);
        ScriptData argDestinationAddress = sac.getArgument(20);
        ScriptData argParameters = sac.getArgumentAll();

        String script = new ScriptAssembler()
                // Step 2. Set BIP-44/SLIP0010 CoinType for validation to the path. Tezos (1729)
                .setCoinType(0x06C1)
                // Step 3. Compose the raw transaction from arguments for signing.        
                // Watermark
                .copyString("03")
                // Branch: block hash (32 Bytes)
                .copyArgument(argBranch)
                // Tag: 108 for Transaction (1 Byte)
                .copyString("6C")
                // Source address type (1 Byte)
                .copyArgument(argSourceAddressType)
                // Source address (20 Bytes)
                .copyArgument(argSourceAddress)
                // fee (variable size)
                .protobuf(argFee, typeInt)
                // count (variable size)
                .protobuf(argCount, typeInt)
                // gas limit (variable size)
                .protobuf(argGasLimit, typeInt)
                // storage limit (variable size)
                .protobuf(argStorageLimit, typeInt)
                // amount (variable size)
                .protobuf(argAmount, typeInt)
                // Destination (22 Bytes)
                .copyArgument(argDestinationAccountType).copyArgument(argDestinationAddress).copyArgument(argDestinationAddressType)
                // 255 for contract interaction
                .copyString("FF")
                // contract parameters
                .copyArgument(argParameters)
                // Step 4. Define which parts of the arguments shall be showed on the screen to be validated.
                .showMessage("XTZ CT")
                .showPressButton()
                // Step 5. Set up Script Header.               
                .setHeader(HashType.Blake2b256, SignType.EDDSA)
                .getScript();

        return script;
    }

    public static String getTezosDelegationScript() {
        // Step 1. Define Arguments.
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptData argBranch = sac.getArgument(32);
        ScriptData argSourceAddressType = sac.getArgument(1);
        ScriptData argSourceAddress = sac.getArgument(20);
        ScriptData argFee = sac.getArgumentRightJustified(10);
        ScriptData argCount = sac.getArgumentRightJustified(10);
        ScriptData argGasLimit = sac.getArgumentRightJustified(10);
        ScriptData argStorageLimit = sac.getArgumentRightJustified(10);
        ScriptData argDelegateAddressType = sac.getArgument(1);
        ScriptData argDelegateAddress = sac.getArgument(20);

        String script = new ScriptAssembler()
                // Step 2. Set BIP-44/SLIP0010 CoinType for validation to the path. Tezos (1729)
                .setCoinType(0x06C1)
                // Step 3. Compose the raw transaction from arguments for signing.        
                // Watermark
                .copyString("03")
                // Branch: block hash (32 Bytes)
                .copyArgument(argBranch)
                // Tag: 110 for Delegation (1 Byte)
                .copyString("6E")
                // Source address type  (1 Byte)
                .copyArgument(argSourceAddressType)
                // Source address (20 Bytes)
                .copyArgument(argSourceAddress)
                // fee (variable size)
                .protobuf(argFee, typeInt)
                // count (variable size)
                .protobuf(argCount, typeInt)
                // gas limit (variable size)
                .protobuf(argGasLimit, typeInt)
                // storage limit (variable size)
                .protobuf(argStorageLimit, typeInt)
                // 255 for delegation
                .copyString("FF")
                // baker address type (1 Byte)
                .copyArgument(argDelegateAddressType)
                // baker address (20 Bytes)
                .copyArgument(argDelegateAddress)
                // Step 4. Define which parts of the arguments shall be showed on the screen to be validated.
                .showMessage("XTZ")
                .showMessage("Delgt")
                .showPressButton()
                // Step 5. Set up Script Header.                 
                .setHeader(HashType.Blake2b256, SignType.EDDSA)
                .getScript();

        return script;
    }

    public static String getTezosUndelegationScript() {
        // Step 1. Define Arguments.
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptData argBranch = sac.getArgument(32);
        ScriptData argSourceAddressType = sac.getArgument(1);
        ScriptData argSourceAddress = sac.getArgument(20);
        ScriptData argFee = sac.getArgumentRightJustified(10);
        ScriptData argCount = sac.getArgumentRightJustified(10);
        ScriptData argGasLimit = sac.getArgumentRightJustified(10);
        ScriptData argStorageLimit = sac.getArgumentRightJustified(10);

        String script = new ScriptAssembler()
                // Step 2. Set BIP-44/SLIP0010 CoinType for validation to the path. Tezos (1729)
                .setCoinType(0x06C1)
                // Step 3. Compose the raw transaction from arguments for signing.        
                // Watermark
                .copyString("03")
                // Branch: block hash (32 Bytes)
                .copyArgument(argBranch)
                // Tag: 110 for Undelegation (1 Byte)
                .copyString("6E")
                // Source address type (1 Byte)
                .copyArgument(argSourceAddressType)
                // Source address (20 Bytes)
                .copyArgument(argSourceAddress)
                // fee (variable size)
                .protobuf(argFee, typeInt)
                // count (variable size)
                .protobuf(argCount, typeInt)
                // gas limit (variable size)
                .protobuf(argGasLimit, typeInt)
                // storage limit (variable size)
                .protobuf(argStorageLimit, typeInt)
                // 0 for undelegation 
                .copyString("00")
                // Step 4. Define which parts of the arguments shall be showed on the screen to be validated.
                .showMessage("XTZ")
                .showMessage("UnDel")
                .showPressButton()
                // Step 5. Set up Script Header. 
                .setHeader(HashType.Blake2b256, SignType.EDDSA)
                .getScript();

        return script;
    }
}
