package com.coolbitx.wallet.signing.main;

import org.spongycastle.util.encoders.Hex;
import com.coolbitx.wallet.signing.utils.*;
import com.coolbitx.wallet.signing.utils.ScriptBuffer.BufferType;

public class main {

    public static void main(String[] args) throws Exception {

        // Step 1. Define Arguments.
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptBuffer argTo = sac.getArgument(20);
        ScriptBuffer argValue = sac.getArgumentRightJustified(10);
        ScriptBuffer argDecimal = sac.getArgument(1);

        // Step 2. Set up Script Header.
        // length | version | hash | sign
        String header =  "03040601";
        // length: 03
        // version: 04
        // hash: choose one in ScriptAssembler
        // sign: 01 (ECDSA), 02 (EDDSA)

        // Step 3. Set BIP-44/SLIP0010 CoinType for validation to the path.
        String coinType = ScriptAssembler.setCoinType(0x3C);

        // Step 4. Compose the raw transaction from arguments for signing.
        String payload = ScriptAssembler.copyString("02")
                + ScriptAssembler.arrayPointer()
                + ScriptAssembler.copyString("94")
                + ScriptAssembler.copyArgument(argTo)
                + ScriptAssembler.rlpString(argValue)
                + ScriptAssembler.copyString("C0")
                + ScriptAssembler.arrayEnd(1);

        // Step 5. Define which parts of the arguments shall be showed on the screen to be validated.
        String display = ScriptAssembler.showMessage("TEMPLATE")
                + ScriptAssembler.setBufferInt(argDecimal, 0, 20)
                + ScriptAssembler.showAmount(argValue, 1000)
                + ScriptAssembler.showPressButton();

        String script = header + coinType + payload + display;
	      System.out.println("\n============================== Script Start ==============================\n");
	      System.out.println(script);
	      System.out.println("\n============================== Script End ==============================\n");
	      System.out.println("Please copy the above script to test in the script-tester.\n");
	      System.out.println("The argument input should be a hex string which composed of");
        System.out.println("the arguments in the order you defined.");
        System.out.println("\nFor example, below arguments");
        System.out.println("  to: 86fa049857e0209aa7d9e616f7eb3b3b78ecfdb0");
        System.out.println("  value: 000000004563918244f4");
        System.out.println("  decimal: 12");
        System.out.println("\nshould composed to the argument input.");
        System.out.println("  86fa049857e0209aa7d9e616f7eb3b3b78ecfdb0000000004563918244f412\n");
    }
}
