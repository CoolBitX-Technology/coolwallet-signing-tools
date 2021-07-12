package com.coolbitx.wallet.signing.main;

import org.spongycastle.util.encoders.Hex;
import com.coolbitx.wallet.signing.utils.*;
import com.coolbitx.wallet.signing.utils.ScriptBuffer.BufferType;

public class main {

    public static void main(String[] args) throws Exception {

        // Step 1. Define Arguments
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptBuffer argTo = sac.getArgument(20);
        ScriptBuffer argValue = sac.getArgumentRightJustified(10);

        // Step 2. Set up Script Header
        // length | version | hash | sign
        String header =  "03040601";
        // length: 03
        // version: 04
        // hash: choose one in ScriptAssembler
        // sign: 01 (ECDSA), 02 (EDDSA)

        // Step 3. Define BIP-44/SLIP0010 CoinType for validation in the path
        String coinType = ScriptAssembler.setCoinType(0x3C);

        // Step 4. Define Script Header
        String payload = ScriptAssembler.copyString("02")
                + ScriptAssembler.arrayPointer()
                + ScriptAssembler.copyString("94")
                + ScriptAssembler.copyArgument(argTo)
                + ScriptAssembler.rlpString(argValue)
                + ScriptAssembler.copyString("C0")
                + ScriptAssembler.arrayEnd(1);

        // Step 5. Define which parts of the arguments shall be showed on the screen
        String display = ScriptAssembler.showMessage("TEMPLATE")
                + ScriptAssembler.showAmount(argValue, 1000)
                + ScriptAssembler.showPressButton();

        String script = header + coinType + payload + display;
	      System.out.println("\n============================== Script Start ==============================\n");
	      System.out.println(script);
	      System.out.println("\n============================== Script End ==============================\n");
    }
}
