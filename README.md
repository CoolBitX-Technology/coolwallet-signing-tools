# CoolWallet Signing Tools

## What is CoolWallet Signing
The core firmware of CoolWallet Pro is re-deisgned to ease the integration of new blockchain signing capability. The firmware works as a virtual machine which accepts a signing script and input arguments, and then returns the signature which could be included in the blockchain transaction message.

The firmware comes with a set of built-in commands and crypto algorithms so that developers could use them in the script. The firmware could be updated to add support of more commands and algorithms.

This repository provides tools to facilitate script generation and testing. The generated scripts could be integrated back into CoolWallet SDK to add more crypto currency support.

## How to use this tools

CoolWallet signing tools provide script-generator and script-tester for developers to develop and test scripts.

- script-generator: The Script generation tool allows the developers to create their own CoolWallet Signing Script given the arguments describing the format of the transaction and the currency address.

- script-tester: The script tester allows the developers to test the CoolWallet Signing Script generated with the script generator.


## Reference

Contact CoolBitX Product Team for further exploration

product-team-cw@coolbitx.com

* [NOTICE](./NOTICE)
* [License](./LICENSE)




