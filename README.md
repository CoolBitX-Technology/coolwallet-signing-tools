# Coolwallet Scriptable Signing SDK

Scriptable Signing SDK 為一個開源的工具，將工具產生出的 script ，經由 Coolwallet Pro 組成 transaction payload 進而產生出交易簽章。

這個函式庫支援


目前支援 Coolwallet Pro SE 最低版本為 `v308`
## Introduction
---
Use the command (Script) to copy/code the data between the buffers... etc. to compose the signature data and display the transaction content correctly

## Composition
---

  [Header][Command][Command] … [Command]

The Header is followed by a sequence of commands that run in order.

### Header

  [headerLength 1B][version 1B][hashType 1B][signType 1B][remainDataType 1B]


When the header length is 03, it means that the remainDataType is not required and the 80A2 will generate a signature without executing the 80A4.

When the header length is 04, it means that the remainDataType is needed for 80A4 to execute, then 80A2 will not generate a signature.

Example.

0300[hashType][signType]

0400[hashType][signType][remainDataType]



## Supported algorithms
---
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
---

Buffer type | Description | Buffer size (bytes)
---|---|---
script | store script | 600 bytes
argument | Save the signature parameters, each transaction is different, but the data is in a fixed format | 3800 bytes
free | Staging space | 300 bytes
extended | Staging space | 300 bytes
transaction | The final composition of the transaction content (raw signing data) | 3800 bytes
detail | Transaction summary displayed on the card (symbol/amount/address) | 100 bytes

## Library documentation
---

你可以到 XXX 查看詳細的函式庫用法。

### Usage
---

