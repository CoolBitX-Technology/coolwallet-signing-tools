/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.coolbitx.wallet.signing.scriptlib;

import com.coolbitx.wallet.signing.utils.HexUtil;
import com.coolbitx.wallet.signing.utils.ScriptArgumentComposer;
import com.coolbitx.wallet.signing.utils.ScriptAssembler;
import com.coolbitx.wallet.signing.utils.ScriptData;
import com.coolbitx.wallet.signing.utils.ScriptAssembler.HashType;
import com.coolbitx.wallet.signing.utils.ScriptAssembler.SignType;
import com.coolbitx.wallet.signing.utils.ScriptData.Buffer;

public class EthScript {

    public static void listAll() {
        System.out.println("Eth eip1559: \n" + getETHEIP1559Script() + "\n");
        System.out.println("Eth eip1559 erc20: \n" + getETHEIP1559ERC20Script() + "\n");
        System.out.println("Eth eip1559 Smart Contract: \n" + getETHEIP1559SmartScript() + "\n");
        System.out.println("Eth: \n" + getETHScript() + "\n");
        System.out.println("Eth erc20: \n" + getERC20Script() + "\n");
        System.out.println("Eth Smart Contract: \n" + getEtherContractBlindScript() + "\n");
        System.out.println("Eth Message: \n" + getEtherMessageBlindScript() + "\n");
        System.out.println("Eth TypedData: \n" + getEtherTypedDataBlindScript() + "\n");
    }

    /*
txType(EIP-2718) : 02
rlpLength :        e5
chainId :          01
nonce :            80
maxPriorityFee :   01
maxFee :           81 ff
gasLimit :         84 02625a00
to address :       94 cccccccccccccccccccccccccccccccccccccccc
value :            83 0186a0
data :             80
accessList :       c0
     */
    public static String getETHEIP1559Script() {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptData argTo = sac.getArgument(20);
        ScriptData argValue = sac.getArgumentRightJustified(10);
        ScriptData argGasTipCap = sac.getArgumentRightJustified(10);
        ScriptData argGasFeeCap = sac.getArgumentRightJustified(10);
        ScriptData argGasLimit = sac.getArgumentRightJustified(10);
        ScriptData argNonce = sac.getArgumentRightJustified(8);

        String script = 
                // set coinType to 3C
                ScriptAssembler.setCoinType(0x3C)
                // txType (EIP-2718)
                + ScriptAssembler.copyString("02")
                + ScriptAssembler.arrayPointer()
                // chainId
                + ScriptAssembler.copyString("01")
                // nonce
                + ScriptAssembler.rlpString(argNonce)
                // gasTipCap (maxPriorityFeePerGas)
                + ScriptAssembler.rlpString(argGasTipCap)
                // gasFeeCap (maxFeePerGas)
                + ScriptAssembler.rlpString(argGasFeeCap)
                // gasLimit
                + ScriptAssembler.rlpString(argGasLimit)
                // toAddress
                + ScriptAssembler.copyString("94")
                + ScriptAssembler.copyArgument(argTo)
                // value
                + ScriptAssembler.rlpString(argValue)
                // data
                + ScriptAssembler.copyString("80")
                // accessList
                + ScriptAssembler.copyString("C0")
                + ScriptAssembler.arrayEnd(1)
                + ScriptAssembler.showMessage("ETH")
                + ScriptAssembler.copyString(HexUtil.toHexString("0x"), Buffer.CACHE2)
                + ScriptAssembler.baseConvert(argTo, Buffer.CACHE2, 0, ScriptAssembler.hexadecimalCharset, ScriptAssembler.zeroInherit)
                + ScriptAssembler.showAddress(ScriptData.getDataBufferAll(Buffer.CACHE2))
                + ScriptAssembler.showAmount(argValue, 18)
                + ScriptAssembler.showPressButton();

        //version=04 ScriptAssembler.hash=06=ScriptAssembler.Keccak256 sign=01=ECDSA
        return ScriptAssembler.setHeader(HashType.Keccak256, SignType.ECDSA) + script;
    }

    public static String ETHEIP1559ScriptSignature = "3046022100F3CA891D06B8284C01B9E51CD478E7BBA14CD99F137383F2EAD642747222E2F9022100A121B0DE524F00D063DAB9E51E86B4CAF1B1874F79570AEE1AA437DFAD750C1C";

    /*
txType(EIP-2718) : 02
rlpLength :        f86a
chainId :          01
nonce :            80
maxPriorityFee :   01
maxFee :           81 ff
gasLimit :         84 02625a00
to address :       94 cccccccccccccccccccccccccccccccccccccccc
value :            83 0186a0
data :             b844 a9059cbb
                   0000000000000000000000009e3a6e03298780078003d3965689b8f653ee87b1
                   0000000000000000000000000000000000000000000000004563918244f40000
accessList :       c0
     */
    public static String getETHEIP1559ERC20Script() {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptData argTo = sac.getArgument(20);
        ScriptData argValue = sac.getArgument(12);
        ScriptData argGasTipCap = sac.getArgumentRightJustified(10);
        ScriptData argGasFeeCap = sac.getArgumentRightJustified(10);
        ScriptData argGasLimit = sac.getArgumentRightJustified(10);
        ScriptData argNonce = sac.getArgumentRightJustified(8);
        ScriptData argTokenInfo = sac.getArgumentUnion(0, 29);
        ScriptData argDecimal = sac.getArgument(1);
        ScriptData argNameLength = sac.getArgument(1);
        ScriptData argName = sac.getArgumentVariableLength(7);
        ScriptData argContractAddress = sac.getArgument(20);
        ScriptData argSign = sac.getArgument(72);

        String script = 
                // set coinType to 3C
                ScriptAssembler.setCoinType(0x3C)
                // txType (EIP-2718)
                + ScriptAssembler.copyString("02")
                + ScriptAssembler.arrayPointer()
                // chainId
                + ScriptAssembler.copyString("01")
                + ScriptAssembler.rlpString(argNonce)
                + ScriptAssembler.rlpString(argGasTipCap)
                + ScriptAssembler.rlpString(argGasFeeCap)
                + ScriptAssembler.rlpString(argGasLimit)
                + ScriptAssembler.copyString("94")
                + ScriptAssembler.copyArgument(argContractAddress)
                // data, Length = 68
                + ScriptAssembler.copyString("80B844a9059cbb000000000000000000000000")
                + ScriptAssembler.copyArgument(argTo)
                + ScriptAssembler.copyString("0000000000000000000000000000000000000000")
                + ScriptAssembler.copyArgument(argValue)
                // accessList
                + ScriptAssembler.copyString("C0")
                + ScriptAssembler.arrayEnd(1)
                + ScriptAssembler.showMessage("ETH")
                + ScriptAssembler.ifSigned(argTokenInfo, argSign, "",
                        ScriptAssembler.copyString(HexUtil.toHexString("@"), Buffer.CACHE2)
                )
                + ScriptAssembler.setBufferInt(argNameLength, 1, 7)
                + ScriptAssembler.copyArgument(argName, Buffer.CACHE2)
                + ScriptAssembler.showMessage(ScriptData.getDataBufferAll(Buffer.CACHE2))
                + ScriptAssembler.clearBuffer(Buffer.CACHE2)
                + ScriptAssembler.copyString(HexUtil.toHexString("0x"), Buffer.CACHE2)
                + ScriptAssembler.baseConvert(argTo, Buffer.CACHE2, 0, ScriptAssembler.hexadecimalCharset, ScriptAssembler.zeroInherit)
                + ScriptAssembler.showAddress(ScriptData.getDataBufferAll(Buffer.CACHE2))
                + ScriptAssembler.setBufferInt(argDecimal, 0, 20)
                + ScriptAssembler.showAmount(argValue, 1000)
                + ScriptAssembler.showPressButton();

        //version=04 ScriptAssembler.hash=06=ScriptAssembler.Keccak256 sign=01=ECDSA
        return ScriptAssembler.setHeader(HashType.Keccak256, SignType.ECDSA) + script;
    }

    public static String ETHEIP1559ERC20ScriptSignature = "00304502207A63FB17CEA7E123C1BF12CBE3687614FCD677DE13171CC0B275E9659421762A022100ED2A5EC6AB2736C1D9087033CB48181784859A10C8A9674ADEFE34A84D520646";

    public static String getETHEIP1559SmartScript() {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptData argTo = sac.getArgument(20);
        ScriptData argValue = sac.getArgumentRightJustified(10);
        ScriptData argGasTipCap = sac.getArgumentRightJustified(10);
        ScriptData argGasFeeCap = sac.getArgumentRightJustified(10);
        ScriptData argGasLimit = sac.getArgumentRightJustified(10);
        ScriptData argNonce = sac.getArgumentRightJustified(8);
        ScriptData argData = sac.getArgument(4);

        // version=05 ScriptAssembler.hash=06=ScriptAssembler.Keccak256 sign=01=ECDSA
        return "03050601"
                // set coinType to 3C
                + ScriptAssembler.setCoinType(0x3C)
                // txType (EIP-2718)
                + ScriptAssembler.copyString("02")
                + ScriptAssembler.arrayPointer()
                // chainId
                + ScriptAssembler.copyString("01")
                + ScriptAssembler.rlpString(argNonce)
                + ScriptAssembler.rlpString(argGasTipCap)
                + ScriptAssembler.rlpString(argGasFeeCap)
                + ScriptAssembler.rlpString(argGasLimit)
                + ScriptAssembler.copyString("94")
                + ScriptAssembler.copyArgument(argTo)
                + ScriptAssembler.rlpString(argValue)
                + ScriptAssembler.rlpDataPlaceholder(argData)
                // accessList
                + ScriptAssembler.copyString("C0")
                + ScriptAssembler.arrayEnd(1)
                + ScriptAssembler.showMessage("ETH")
                + ScriptAssembler.showWrap("SMART", "")
                + ScriptAssembler.showPressButton();

        // version=04 ScriptAssembler.hash=06=ScriptAssembler.Keccak256 sign=01=ECDSA
        return ScriptAssembler.setHeader(HashType.Keccak256, SignType.ECDSA) + script;
    }

    public static String ETHEIP1559SmartScriptSignature = "003045022100F994CBD35DA01724AE1189B299F80FB5DD6A304EB0C27E5DBA7B9AD6588C92E70220170BBE5ADFF78C30FDA82784ED4C647D6D855EBACC5F13D8C171C5E529E1AF39";

    /*
    E7
    2A
    85 09C74AFE1F
    82 5208
    94 A3255ECFE3F6727A62D938F4C29B2F73C361B26C
    83 989680
    80
    03
    80
    80
     */
    public static String getETHScript() {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptData argTo = sac.getArgument(20);
        ScriptData argValue = sac.getArgumentRightJustified(10);
        ScriptData argGasPrice = sac.getArgumentRightJustified(10);
        ScriptData argGasLimit = sac.getArgumentRightJustified(10);
        ScriptData argNonce = sac.getArgumentRightJustified(8);
        
        String script = 
                // set coinType to 3C
                ScriptAssembler.setCoinType(0x3C)
                // temp byte for rlpList
                + ScriptAssembler.copyString("C0")
                // nonce
                + ScriptAssembler.rlpString(argNonce)
                // gasPrice
                + ScriptAssembler.rlpString(argGasPrice)
                // gasLimit
                + ScriptAssembler.rlpString(argGasLimit)
                // toAddress
                + ScriptAssembler.copyString("94") + ScriptAssembler.copyArgument(argTo)
                // value
                + ScriptAssembler.rlpString(argValue)
                // data
                + ScriptAssembler.copyString("80")
                // chainId v
                // + ScriptAssembler.rlpString(argChainId)
                + ScriptAssembler.copyString("01", Buffer.CACHE1)
                + ScriptAssembler.rlpString(ScriptData.getDataBufferAll(Buffer.CACHE1))
                // r,s
                + ScriptAssembler.copyString("8080") + ScriptAssembler.rlpList(1) + ScriptAssembler.showMessage("ETH")
                + ScriptAssembler.copyString(HexUtil.toHexString("0x"), Buffer.CACHE2)
                + ScriptAssembler.baseConvert(argTo, Buffer.CACHE2, 0, ScriptAssembler.hexadecimalCharset,
                        ScriptAssembler.zeroInherit)
                + ScriptAssembler.showAddress(ScriptData.getDataBufferAll(Buffer.CACHE2))
                + ScriptAssembler.showAmount(argValue, 18) + ScriptAssembler.showPressButton();

        // version=00 ScriptAssembler.hash=06=ScriptAssembler.Keccak256 sign=01=ECDSA
        return ScriptAssembler.setHeader(HashType.Keccak256, SignType.ECDSA) + script;
    }

    public static String ETHScriptSignature = "0030450220201C3ADEEF531C6CD6E8F082477FF048E45F39B85086C2F40BE96840CA4840F6022100C8A36252C7606D9F2D9E6F58538F967C7F6DEFEE52B536439512CB8CD9993DB0";

    /*
f86a
1e
85 01718c7e00
83 030d40
94 86fa049857e0209aa7d9e616f7eb3b3b78ecfdb0
80
b844 a9059cbb
     0000000000000000000000009e3a6e03298780078003d3965689b8f653ee87b1
     0000000000000000000000000000000000000000000000004563918244f40000
01
80
80
     */
    public static String getERC20Script() {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptData argTo = sac.getArgument(20);
        ScriptData argValue = sac.getArgument(12);
        ScriptData argGasPrice = sac.getArgumentRightJustified(10);
        ScriptData argGasLimit = sac.getArgumentRightJustified(10);
        ScriptData argNonce = sac.getArgumentRightJustified(8);
        ScriptData argChainId = sac.getArgumentRightJustified(2);
        ScriptData argTokenInfo = sac.getArgumentUnion(0, 29);
        ScriptData argDecimal = sac.getArgument(1);
        ScriptData argNameLength = sac.getArgument(1);
        ScriptData argName = sac.getArgumentVariableLength(7);
        ScriptData argContractAddress = sac.getArgument(20);
        ScriptData argSign = sac.getArgument(72);
        
        String script = 
                // set coinType to 3C
                ScriptAssembler.setCoinType(0x3C)
                + ScriptAssembler.copyString("F800")
                + ScriptAssembler.rlpString(argNonce)
                + ScriptAssembler.rlpString(argGasPrice)
                + ScriptAssembler.rlpString(argGasLimit)
                + ScriptAssembler.copyString("94")
                + ScriptAssembler.copyArgument(argContractAddress)
                // data, Length = 68
                + ScriptAssembler.copyString("80B844a9059cbb000000000000000000000000")
                + ScriptAssembler.copyArgument(argTo)
                + ScriptAssembler.copyString("0000000000000000000000000000000000000000")
                + ScriptAssembler.copyArgument(argValue)
                // chainId v
                // + ScriptAssembler.rlpString(argChainId)
                + ScriptAssembler.copyString("01", Buffer.CACHE1)
                + ScriptAssembler.rlpString(ScriptData.getDataBufferAll(Buffer.CACHE1))
                // r,s
                + ScriptAssembler.copyString("8080")
                + ScriptAssembler.rlpList(2)
                + ScriptAssembler.showMessage("ETH")
                + ScriptAssembler.ifSigned(argTokenInfo, argSign, "",
                        ScriptAssembler.copyString(HexUtil.toHexString("@"), Buffer.CACHE2)
                )
                + ScriptAssembler.setBufferInt(argNameLength, 1, 7)
                + ScriptAssembler.copyArgument(argName, Buffer.CACHE2)
                + ScriptAssembler.showMessage(ScriptData.getDataBufferAll(Buffer.CACHE2))
                + ScriptAssembler.clearBuffer(Buffer.CACHE2)
                + ScriptAssembler.copyString(HexUtil.toHexString("0x"), Buffer.CACHE2)
                + ScriptAssembler.baseConvert(argTo, Buffer.CACHE2, 0, ScriptAssembler.hexadecimalCharset, ScriptAssembler.zeroInherit)
                + ScriptAssembler.showAddress(ScriptData.getDataBufferAll(Buffer.CACHE2))
                + ScriptAssembler.setBufferInt(argDecimal, 0, 20)
                + ScriptAssembler.showAmount(argValue, 1000)
                + ScriptAssembler.showPressButton();

        // version=00 ScriptAssembler.hash=06=ScriptAssembler.Keccak256 sign=01=ECDSA
        return ScriptAssembler.setHeader(HashType.Keccak256, SignType.ECDSA) + script;
    }

    public static String ERC20ScriptSignature = "30460221009A706915A2EE0AE663ACF90D9DD59BBEEC111EB12B099E4751219DDC993A01E7022100BA25635AB68F4EF7711D8D880A0BB1A81CA899C78884ECC4183B715F8F047D69";

    public static String getEtherContractBlindScript() {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptData argTo = sac.getArgument(20);
        ScriptData argValue = sac.getArgumentRightJustified(10);
        ScriptData argGasPrice = sac.getArgumentRightJustified(10);
        ScriptData argGasLimit = sac.getArgumentRightJustified(10);
        ScriptData argNonce = sac.getArgumentRightJustified(8);
        ScriptData argData = sac.getArgument(4);

        // version=05 ScriptAssembler.hash=06=ScriptAssembler.Keccak256 sign=01=ECDSA
        return "03050601"
                // set coinType to 3C
                + ScriptAssembler.setCoinType(0x3C)
                + ScriptAssembler.arrayPointer()
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
                + ScriptAssembler.rlpDataPlaceholder(argData)
                // chainId v
                + ScriptAssembler.copyString("01", Buffer.CACHE1)
                + ScriptAssembler.rlpString(ScriptData.getDataBufferAll(Buffer.CACHE1))
                + ScriptAssembler.copyString("8080")
                + ScriptAssembler.arrayEnd(1)
                + ScriptAssembler.showMessage("ETH")
                + ScriptAssembler.showWrap("SMART", "")
                + ScriptAssembler.showPressButton();

        // version=00 ScriptAssembler.hash=06=ScriptAssembler.Keccak256 sign=01=ECDSA
        return ScriptAssembler.setHeader(HashType.Keccak256, SignType.ECDSA) + script;
    }

    public static String EtherContractBlindScriptSignature = "003045022060FC29B9ACA3BDFA237ED377E410A9A9BB12FB88416C06298857AE17D688992D022100F87D2C80BBECDC7192FF64D13A28F7763D6E1610FC1B1254B3D6446822CBA414";

    public static String getEtherMessageBlindScript() {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptData argMessage = sac.getArgumentAll();

        String script = 
                // set coinType to 3C
                ScriptAssembler.setCoinType(0x3C)
                + ScriptAssembler.copyString("19457468657265756D205369676E6564204D6573736167653A0A")
                + ScriptAssembler.copyArgument(argMessage)
                + ScriptAssembler.showMessage("ETH")
                + ScriptAssembler.showWrap("MESSAGE", "")
                + ScriptAssembler.showPressButton();

        //version=00 ScriptAssembler.hash=06=ScriptAssembler.Keccak256 sign=01=ECDSA
        return ScriptAssembler.setHeader(HashType.Keccak256, SignType.ECDSA) + script;
    }

    public static String EtherMessageBlindScriptSignature = "0000304402200745C5665A9CE0FA0C2894E77629A33077D9AE76F23566DC804C64BF38D27FC0022076645BEEF5A522A02D272DA3D7065D1F092C5C03B024A3F1B3A19C144CF98970";

    public static String getEtherTypedDataBlindScript() {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptData argDomainSeparator = sac.getArgument(32);
        ScriptData argMessage = sac.getArgumentAll();

        String script = 
                // set coinType to 3C   
                ScriptAssembler.setCoinType(0x3C)
                + ScriptAssembler.copyString("1901")
                + ScriptAssembler.copyArgument(argDomainSeparator)
                + ScriptAssembler.hash(argMessage, Buffer.TRANSACTION, ScriptAssembler.Keccak256)
                + ScriptAssembler.showMessage("ETH")
                + ScriptAssembler.showWrap("EIP712", "")
                + ScriptAssembler.showPressButton();

        //version=00 ScriptAssembler.hash=06=ScriptAssembler.Keccak256 sign=01=ECDSA
        return ScriptAssembler.setHeader(HashType.Keccak256, SignType.ECDSA) + script;
    }

}
