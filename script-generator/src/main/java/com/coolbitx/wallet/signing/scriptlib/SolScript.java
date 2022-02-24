package com.coolbitx.wallet.signing.scriptlib;
import com.coolbitx.wallet.signing.utils.HexUtil;
import com.coolbitx.wallet.signing.utils.ScriptArgumentComposer;
import com.coolbitx.wallet.signing.utils.ScriptAssembler;
import com.coolbitx.wallet.signing.utils.ScriptData;
import com.coolbitx.wallet.signing.utils.ScriptAssembler.HashType;
import com.coolbitx.wallet.signing.utils.ScriptAssembler.SignType;
import com.coolbitx.wallet.signing.utils.ScriptData.Buffer;

public class SolScript {
    

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
    public static void main(String[] args) {
        System.out.println("Sol transfer: "+getSolScript());
    }

    public String script = "03000202C70700000001F5CAA01700CAA11700CAAC170002CAAC170003CAACC7000460CAACC7006410CAAC170074CAAC170075CAACC7007602CAAC170078DDFC970023DAACC7C0790C07D207CC05065052455353425554546F4E";

    public static void listAll() {
        System.out.println("Sol: \n" + getSolScript() + "\n");
    }

    public static String getSolScript() {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptData numberRequireSignature = sac.getArgument(5);
        ScriptData numberReadonlySignedAccount = sac.getArgument(5);
        ScriptData numberReadonlyUnSignedAccount = sac.getArgument(5);
        ScriptData keyCount = sac.getArgument(2); 
        ScriptData keys = sac.getArgument(96);
        ScriptData recentBlockHash = sac.getArgument(32);
        ScriptData prefixHeader = sac.getArgument(1);
        ScriptData programIdIndex = sac.getArgument(1);
        ScriptData keyIndicesCount = sac.getArgument(1);
        ScriptData keyIndices = sac.getArgument(2);
        ScriptData dataLength = sac.getArgument(1);
        ScriptData dataIndex = sac.getArgument(1);
        ScriptData data = sac.getArgument(3);

        ScriptAssembler scriptAsb = new ScriptAssembler();
        String script = scriptAsb
                .setCoinType(0x01f5)
                .copyArgument(numberRequireSignature)
                .copyArgument(numberReadonlySignedAccount)
                .copyArgument(numberReadonlyUnSignedAccount)
                .copyArgument(keyCount)
                .copyArgument(keys)
                .copyArgument(recentBlockHash)
                .copyArgument(prefixHeader)
                .copyArgument(programIdIndex)
                .copyArgument(keyIndicesCount)
                .copyArgument(keyIndices)
                .copyArgument(dataLength)
                .copyArgument(dataIndex)
                .copyArgument(data)

                .showMessage("SOL")
                .showAddress(ScriptData.getDataBufferAll(Buffer.CACHE2, 35))
                .showAmount(data, 9)
                .showPressButton()
                .setHeader(HashType.SHA256, SignType.EDDSA)
                .getScript();
        return script;
    }
}
