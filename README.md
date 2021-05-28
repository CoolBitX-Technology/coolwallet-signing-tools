# Coolwallet Scriptable Signing SDK

Scriptable Signing SDK 為一個開源的工具，將工具產生出的 script ，經由 Coolwallet Pro 組成 transaction payload 進而產生出交易簽章。

這個函式庫支援


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

script 組成由 header、coin type、該幣種的payload、螢幕顯示資訊，四個部分組成，payload 以及顯示資訊為多個指令組成

The Header is followed by a sequence of commands that run in order.

### Header

  [headerLength 1B][version 1B][hashType 1B][signType 1B][remainDataType 1B]


When the header length is 03, it means that the remainDataType is not required and the 80A2(txPrepArgument) will generate a signature without executing the 80A4.

When the header length is 04, it means that the remainDataType is needed for 80A4(txPrepUtxo) to execute, then 80A2 will not generate a signature.

Example.

ETH script header: 03000601
BTC script header: 0400000010
## Other command

你可以到 XXX 查看詳細的函式庫用法。

### Usage

- 依照交易的 payload 決定傳入卡片所需要的 Argument
- 決定 header 格式
- 寫入 coin type
- 組合 payload string
- 組合 display string
- 執行程式

```java class:"lineNo"
public class ETHScript {
	
    public static void main(String[] args) throws Exception {
	    System.out.println("ETHScript: " + getETHScript());
	}
	
    public static String getETHScript() {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptBuffer argTo = sac.getArgument(20);
        ScriptBuffer argValue = sac.getArgumentRightJustified(10);
        ScriptBuffer argGasPrice = sac.getArgumentRightJustified(10);
        ScriptBuffer argGasLimit = sac.getArgumentRightJustified(10);
        ScriptBuffer argNonce = sac.getArgumentRightJustified(8);
        ScriptBuffer argChainId = sac.getArgumentRightJustified(2);
        //version=00 ScriptAssembler.hash=06=ScriptAssembler.Keccak256 sign=01=ECDSA
        String header = "03000601";
                // set coinType to 3C
        String coinType = ScriptAssembler.setCoinType(0x3C);
                // temp byte for rlpList
        String payload = ScriptAssembler.copyString("C0")
                // nonce
                + ScriptAssembler.rlpString(argNonce)
                // gasPrice
                + ScriptAssembler.rlpString(argGasPrice)
                // gasLimit
                + ScriptAssembler.rlpString(argGasLimit)
                // toAddress
                + ScriptAssembler.copyString("94")
                + ScriptAssembler.copyArgument(argTo)
                // value
                + ScriptAssembler.rlpString(argValue)
                // data
                + ScriptAssembler.copyString("80")
                // chainId v
                + ScriptAssembler.rlpString(argChainId)
                // r,s
                + ScriptAssembler.copyString("8080")
                + ScriptAssembler.rlpList(1);
        String display = ScriptAssembler.showMessage("ETH")
                + ScriptAssembler.copyString(HexUtil.toHexString("0x"), BufferType.FREE)
                + ScriptAssembler.baseConvert(argTo, BufferType.FREE, 0, ScriptAssembler.hexadecimalCharset, ScriptAssembler.leftJustify)
                + ScriptAssembler.showAddress(ScriptBuffer.getDataBufferAll(BufferType.FREE))
                + ScriptAssembler.showAmount(argValue, 18)
                + ScriptAssembler.showPressButton();
        return header + coinType + payload + display;
    }
    
}

```

