# Script Generator 

Script Generator 為 CoolbitX 的一個開源的工具，你可藉由此工具所支援的演算法以及提供的指令，產生 Coolwallet 簽章所需要的 script ，經由 Coolwallet 組成 transaction payload 並簽署，最後產生出交易簽章。

目前支援 Coolwallet Pro SE 最低版本為 `v308`
## Introduction

Use the command (Script) to copy/code the data between the buffers... etc. to compose the signature data and display the transaction content correctly

## Supported algorithms

- SHA1
- SHA256
- SHA512
- SHA3256
- SHA3512
- Keccak256
- Keccak512
- RipeMD160
- SHA256RipeMD160
- DoubleSHA256
- CRC16
- Blake2b256
- Blake2b512


## Buffer type


Buffer type | Description | Buffer size (bytes)
---|---|---
script | store script | 600 bytes
argument | Save the signature parameters, each transaction is different, but the data is in a fixed format | 3800 bytes
free | Staging space | 300 bytes
extended | Staging space | 300 bytes
transaction | The final composition of the transaction content (raw signing data) | 3800 bytes
detail | Transaction summary displayed on the card (symbol/amount/address) | 100 bytes


## Composition


  [Header][setCoinType Command][Payload Command]...[Display Command]...

script 組成由 header、coin type、payload、display detail 四個部分組成，payload 以及 display detail 為多個指令組成

The Header is followed by a sequence of commands that run in order.

### Header

  [headerLength 1B][version 1B][hashType 1B][signType 1B][remainDataType 1B]


When the header length is 03, it means that the remainDataType is not required and the 80A2(txPrepArgument) will generate a signature without executing the 80A4.

When the header length is 04, it means that the remainDataType is needed for 80A4(txPrepUtxo) to execute, then 80A2 will not generate a signature.

#### Example

- ETH script header: 03000601
- BTC script header: 0400000010
### Other command

你可以到 XXX 查看詳細的函式庫用法。

## Usage

- 修改檔案 src/main/java/com/coolbitx/wallet/signing/main/main.java
- 依照交易的 payload 決定傳入卡片所需要的 Argument
- 決定 header 格式
- 寫入 coin type
- 組合 payload string
- 組合 display string
- 執行程式: `mvn compile -q`

```java class:"lineNo"
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

        // Step 6. Generate the script using maven
        //
        // $ mvn compile -q
        //
        // ------------------------------ Print the result -----------------------------------//
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

```

