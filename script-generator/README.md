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

### Get script

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

### How to prepare argument

#### Account model: ETH

```java
ScriptArgumentComposer sac = new ScriptArgumentComposer();
ScriptData argTo = sac.getArgument(20);
ScriptData argValue = sac.getArgumentRightJustified(10);
ScriptData argGasPrice = sac.getArgumentRightJustified(10);
ScriptData argGasLimit = sac.getArgumentRightJustified(10);
ScriptData argNonce = sac.getArgumentRightJustified(8);
```

argument: [toAddress(20B)] [amount(10B)] [gasPrice(10B)] [gasLimit(10B)] [nonce(8B)] [chainId(2B)]
- toAddress: 對象的地址扣掉 "0x"
- amount: 最小單位 wei (1ETH = 10^18 wei)
- chainId: v r s的v。


```text
"a3255ecfe3f6727a62d938f4c29b2f73c361b26c" // to
"00000000000000989680" // amount
"000000000009c74afe1f" // gasPrice
"00000000000000005208" // gasLimit
"000000000000002a" // nonce
"0003"; // chainId v

```

每個 argument 前面都需要再加上該幣種的 path，path 規則如下：

```
15	path length（Hexadecimal）
32	bip32
8000002C
${coinType}
80000000
00000000
${addressIdxHex}
```

Full Argument:
```
15328000002c8000003c800000000000000000000000a3255ecfe3f6727a62d938f4c29b2f73c361b26c00000000000000989680000000000009c74afe1f00000000000000005208000000000000002a0003
```

#### UTXO: BTC

在 Coolwallet signing 設計中，開發者只需要設計 output script，
不過 argument 則需要提供 input & output argument。

input(utxo) argument:[outPoint(32+4B)] [inputScriptType(1B)] [inputAmount(8B)] [inputHash(20B)]
- outPoint:這個input的來源block的hash以及當時的output編號
- inputScriptType:P2PKH & P2WPKH = 00，P2SH & P2WSH = 01
- inputAmount: value
- inputHash: input hash

```
"88fd8402286041ab66d230bd23592b75493e5be21f8694c6491440aad7117bfc00000000"+ // outPoint
"00"
"0000000000004E20"
"027d3f3c7c3cfa357d97fbe7d80d70f4ab1cac0d"; // input P2PKH 0x4E20sat pubkeyHash:0x027d3f....ac0d
```

btc intput path
```
15
32
8000002C
80000000 // coinType
80000000
00000000
00000000 // address index hex
```

Full Argument (path + input(utxo) argument):
```
15328000002C8000000080000000000000000000000088fd8402286041ab66d230bd23592b75493e5be21f8694c6491440aad7117bfc00000000000000000000004E20027d3f3c7c3cfa357d97fbe7d80d70f4ab1cac0d
```


output argument: [outputScriptType(1B)] [outputAmount(8B)] [outputHash(12+20B)] [haveChange(1B)] [changeScriptType(1B)] [changeAmount(8B)] [changePath(21B)] [hashPrevouts(32B] [hashSequence(32B)]
- outputScriptType: output 的 scriptType，由對方地址決定。

開頭 | 解析 | type | outputScriptType
---|---|---|---
1 | - | P2PKH | 00
3 | - | P2SH | 01
bc1 | 20B | P2WPKH | 02
bc1 | 32B | P2WSH | 03


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


