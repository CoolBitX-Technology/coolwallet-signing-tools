# Coolwallet Scriptable Signing SDK


### Introduction

Use the command (Script) to copy/code the data between the buffers... etc. to compose the signature data and display the transaction content correctly

### Buffer type

Buffer type | Description | Buffer size (bytes)
---|---|---
script | store script | 600 bytes
argument | Save the signature parameters, each transaction is different, but the data is in a fixed format | 1500 bytes
free | Staging space | 300 bytes
extended | Staging space | 300 bytes
transaction | The final composition of the transaction content (raw signing data) | 1500 bytes
detail | Transaction summary displayed on the card (symbol/amount/address) | 100 bytes


### Example
