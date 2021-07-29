# Script Generator 

Script Generator is an open source tool of CoolbitX, You can use the algorithms and command provided by this tool 
to generate the script required for the CoolWallet signature.
CoolWallet will compose and sign the transaction payload to generate a transaction signature.

The minimum version of CoolWallet Pro SE currently supported  is `v308`
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

The script composition consists of four parts: header, coin type, payload, and display detail.
The payload and display detail are composed of multiple commands.

The Header is followed by a sequence of commands that run in order.

### Header

  [headerLength 1B][version 1B][hashType 1B][signType 1B][remainDataType 1B]


When the header length is 03, it means that the remainDataType is not required and the 80A2(txPrepArgument) will generate a signature without executing the 80A4.

When the header length is 04, it means that the remainDataType is needed for 80A4(txPrepUtxo) to execute, then 80A2 will not generate a signature.

#### Example

- ETH script header: 03000601
- BTC script header: 0400000010
### Other command

You can go to [command page](https://special-carnival-8b270ec3.pages.github.io/) for detailed library usage.

## Usage

### Generate script

- Modify file: src/main/java/com/coolbitx/wallet/signing/main/main.java
- Determine the Argument required to enter the card according to the transaction payload.
- Decide the header format
- Enter coin type
- Make up all payload string
- Make up all display string
- Run the program: `mvn compile -q`

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

### Ｐrepare argument

#### Account model: ETH

```java
ScriptArgumentComposer sac = new ScriptArgumentComposer();
ScriptData argTo = sac.getArgument(20);
ScriptData argValue = sac.getArgumentRightJustified(10);
ScriptData argGasPrice = sac.getArgumentRightJustified(10);
ScriptData argGasLimit = sac.getArgumentRightJustified(10);
ScriptData argNonce = sac.getArgumentRightJustified(8);
```

**argument**
[toAddress(20B)] [amount(10B)] [gasPrice(10B)] [gasLimit(10B)] [nonce(8B)] [chainId(2B)]


```text
"a3255ecfe3f6727a62d938f4c29b2f73c361b26c" // to
"00000000000000989680" // amount
"000000000009c74afe1f" // gasPrice
"00000000000000005208" // gasLimit
"000000000000002a" // nonce
"0003"; // chainId v

```

you need to add the path of the currency before the argument in each transaction that needs to be signed. 
The path rules are as follows:

```
15	path length（Hexadecimal）
32	bip32
8000002C
${coinType}
80000000
00000000
${addressIdxHex}
```


**eth path**
```
15	
32	
8000002c
8000003c
80000000
00000000
00000000
```

**Full Argument:**
```
15328000002c8000003c800000000000000000000000a3255ecfe3f6727a62d938f4c29b2f73c361b26c00000000000000989680000000000009c74afe1f00000000000000005208000000000000002a0003
```

<br>


#### UTXO: BTC

In the CoolWallet signing design, developers only need to design the output script.
But the argument needs to provide input & output argument.

**Input(utxo) Argument**
[outPoint(32+4B)] [inputScriptType(1B)] [inputAmount(8B)] [inputHash(20B)]


```
"88fd8402286041ab66d230bd23592b75493e5be21f8694c6491440aad7117bfc00000000"+ // outPoint
"00"
"0000000000004E20"
"027d3f3c7c3cfa357d97fbe7d80d70f4ab1cac0d"; // input P2PKH 0x4E20sat pubkeyHash:0x027d3f....ac0d
```

**Btc Intput Path**
```
15
32
8000002C
80000000 // coinType
80000000
00000000
00000000 // address index hex
```

**Full Argument** (path + input(utxo) argument):
```
15328000002C8000000080000000000000000000000088fd8402286041ab66d230bd23592b75493e5be21f8694c6491440aad7117bfc00000000000000000000004E20027d3f3c7c3cfa357d97fbe7d80d70f4ab1cac0d
```


**Output Argument**
[outputScriptType(1B)] [outputAmount(8B)] [outputHash(12+20B)] [haveChange(1B)] [changeScriptType(1B)] [changeAmount(8B)] [changePath(21B)] [hashPrevouts(32B] [hashSequence(32B)]

```
"00"
"0000000000002710"
"000000000000000000000000"+"39af5ea4dd0b3b9771945596fa3d4ed3ff761705"+ //output P2PKH 0x2710sat dest:39af...1705 (0x00*12 is for padding)
"01"+// have change
"00"
"0000000000002710"
"32"+"8000002C"+"80000000"+"80000000"+"00000000"+"00000005"+ // change P2PKH 0x1888sat dest:BIP32 m/44'/0'/0'/0/5 (purpose/cointype/account/change/index)
"a2c0d9aa66bc2a92bfdd22f6f05e3eda486f80015079a5144d732f157b5c5222"+ // hashPrevouts
"03bae88710f05ebf15c1c34f7ea4c1ad55ee8c5d7d6ee2b6f9ecd26cf663ca08"; // hashSequence
```

**Btc Output Path** (Btc output doesn't need a signature, so the argument doesn't require path, but need path length.)
```
00
```

**Full Argument** (path length + output argument):
```
0000000000000000271000000000000000000000000039af5ea4dd0b3b9771945596fa3d4ed3ff76170501000000000000002710328000002C80000000800000000000000000000005a2c0d9aa66bc2a92bfdd22f6f05e3eda486f80015079a5144d732f157b5c522203bae88710f05ebf15c1c34f7ea4c1ad55ee8c5d7d6ee2b6f9ecd26cf663ca08
```
