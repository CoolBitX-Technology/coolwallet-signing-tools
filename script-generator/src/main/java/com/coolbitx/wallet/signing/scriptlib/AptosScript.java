/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.coolbitx.wallet.signing.scriptlib;

import com.coolbitx.wallet.signing.utils.HexUtil;
import com.coolbitx.wallet.signing.utils.ScriptArgumentComposer;
import com.coolbitx.wallet.signing.utils.ScriptAssembler;
import com.coolbitx.wallet.signing.utils.ScriptData;
import com.coolbitx.wallet.signing.utils.ScriptAssembler.HashType;
import com.coolbitx.wallet.signing.utils.ScriptAssembler.SignType;
import com.coolbitx.wallet.signing.utils.ScriptData.Buffer;

public class AptosScript {

    public static void listAll() {
        System.out.println("Aptos Transfer: \n" + getAptosTransferScript() + "\n");
    }

    /*
       prefix               b5e97db07fa0bd0e5598aa3643a9bc6f6693bddc1a9fec9e674a461eaa00b193
       sender               f336d88238bd579bc97613c6ef346743589a36cf603eb6c7c99c08c05ab815ab
       sequence             0000000000000000 (0)
       payload              02 (2)
       module_address         0000000000000000000000000000000000000000000000000000000000000001
       module_name            0d 6170746f735f6163636f756e74 (aptos_account)
       function_name          08 7472616e73666572 (transfer)
       ty_args                00 (0)
       args                   02 (2)
       (Bytes)                  20 9a8e4faab04114f8abe1a59a07ed9f980d2592203ebae37b5b6c79dd605533fd
       (Bytes)                  08 e803000000000000
       max_gas              e803000000000000 (1000)
       gas_price            0100000000000000 (1)
       expiration           3eae156300000000 (1662365246)
       chain_id             1b (27)
     */

    public static String getAptosTransferScript() {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptData argSender = sac.getArgument(32);
        ScriptData argSequence = sac.getArgument(8);
        ScriptData argReceiver = sac.getArgument(32);
        ScriptData argAmount = sac.getArgument(8);
        ScriptData argGasLimit = sac.getArgument(8);
        ScriptData argGasPrice = sac.getArgument(8);
        ScriptData argExpiration = sac.getArgument(8);
        // ScriptData argChainID = sac.getArgument(1);

        ScriptAssembler scriptAsb = new ScriptAssembler();
        String script = scriptAsb
                .setCoinType(0x27d)
                .copyString("b5e97db07fa0bd0e5598aa3643a9bc6f6693bddc1a9fec9e674a461eaa00b193")
                .copyArgument(argSender)
                .copyArgument(argSequence)
                .copyString("020000000000000000000000000000000000000000000000000000000000000001")
                .copyString("0d6170746f735f6163636f756e74")
                .copyString("087472616e73666572")
                .copyString("000220")
                .copyArgument(argReceiver)
                .copyString("08")
                .copyArgument(argAmount)
                .copyArgument(argGasLimit)
                .copyArgument(argGasPrice)
                .copyArgument(argExpiration)
                .copyString("1b")

                // .showMessage("APTOS")
                .showWrap("APTOS", "Devnet")
                .showAddress(argReceiver)
                .baseConvert(argAmount, Buffer.CACHE1, 8, ScriptAssembler.binaryCharset, ScriptAssembler.littleEndian)
                .showAmount(ScriptData.getDataBufferAll(Buffer.CACHE1), 8)
                .clearBuffer(Buffer.CACHE1)
                .showPressButton()
                .setHeader(HashType.NONE, SignType.EDDSA)
                .getScript();
        return script;
    }

    public static String AptosScriptSignature = "";

}
