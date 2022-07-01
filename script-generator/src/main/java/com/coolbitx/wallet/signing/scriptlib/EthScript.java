/*
 * To change this license header, choose License Headers in Project Properties. To change this
 * template file, choose Tools | Templates and open the template in the editor.
 */
package com.coolbitx.wallet.signing.scriptlib;

import com.coolbitx.wallet.signing.utils.HexUtil;
import com.coolbitx.wallet.signing.utils.ScriptArgumentComposer;
import com.coolbitx.wallet.signing.utils.ScriptAssembler;
import com.coolbitx.wallet.signing.utils.ScriptData;
import com.coolbitx.wallet.signing.utils.ScriptAssembler.HashType;
import com.coolbitx.wallet.signing.utils.ScriptAssembler.SignType;
import static com.coolbitx.wallet.signing.utils.ScriptAssembler.TYPE_RLP;
import com.coolbitx.wallet.signing.utils.ScriptData.Buffer;
import com.google.common.base.Strings;

public class EthScript {

    public static void listAll() {
        System.out.println("Eth eip1559: \n" + getETHEIP1559Script() + "\n");
        System.out.println("Eth eip1559 erc20: \n" + getETHEIP1559ERC20Script() + "\n");
        System.out.println("Eth eip1559 Smart Contract: \n" + getETHEIP1559SmartScript() + "\n");
        System.out.println("Eth eip1559 Smart Contract Segment: \n" + getETHEIP1559SmartSegmentScript() + "\n");
        System.out.println("Eth: \n" + getETHScript() + "\n");
        System.out.println("Eth erc20: \n" + getERC20Script() + "\n");
        System.out.println("Eth Smart Contract: \n" + getEtherContractBlindScript() + "\n");
        System.out.println("Eth Smart Contract Segment: \n" + getEtherContractBlindSegmentScript() + "\n");
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

        String script = new ScriptAssembler()
                // set coinType to 3C
                .setCoinType(0x3C)
                // txType (EIP-2718)
                .copyString("02").arrayPointer()
                // chainId
                .copyString("01")
                // nonce
                .rlpString(argNonce)
                // gasTipCap (maxPriorityFeePerGas)
                .rlpString(argGasTipCap)
                // gasFeeCap (maxFeePerGas)
                .rlpString(argGasFeeCap)
                // gasLimit
                .rlpString(argGasLimit)
                // toAddress
                .copyString("94").copyArgument(argTo)
                // value
                .rlpString(argValue)
                // data
                .copyString("80")
                // accessList
                .copyString("C0")
                .arrayEnd(TYPE_RLP)
                // txDetail
                .showMessage("ETH")
                .copyString(HexUtil.toHexString("0x"), Buffer.CACHE2)
                .baseConvert(argTo, Buffer.CACHE2, 0, ScriptAssembler.hexadecimalCharset,
                        ScriptAssembler.zeroInherit)
                .showAddress(ScriptData.getDataBufferAll(Buffer.CACHE2)).showAmount(argValue, 18)
                .showPressButton()
                // version=04 ScriptAssembler.hash=06=ScriptAssembler.Keccak256 sign=01=ECDSA
                .setHeader(HashType.Keccak256, SignType.ECDSA).getScript();
        return script;
    }

    public static String ETHEIP1559ScriptSignature
            = "3046022100F3CA891D06B8284C01B9E51CD478E7BBA14CD99F137383F2EAD642747222E2F9022100A121B0DE524F00D063DAB9E51E86B4CAF1B1874F79570AEE1AA437DFAD750C1C";

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

        String script = new ScriptAssembler()
                // set coinType to 3C
                .setCoinType(0x3C)
                // txType (EIP-2718)
                .copyString("02")
                .arrayPointer()
                // chainId
                .copyString("01").rlpString(argNonce).rlpString(argGasTipCap)
                .rlpString(argGasFeeCap).rlpString(argGasLimit).copyString("94")
                .copyArgument(argContractAddress)
                // data, Length = 68
                .copyString("80B844a9059cbb000000000000000000000000").copyArgument(argTo)
                .copyString("0000000000000000000000000000000000000000").copyArgument(argValue)
                // accessList
                .copyString("C0").arrayEnd(TYPE_RLP).showMessage("ETH")
                .ifSigned(argTokenInfo, argSign, "",
                        new ScriptAssembler().copyString(HexUtil.toHexString("@"), Buffer.CACHE2)
                                .getScript())
                // txDetail
                .setBufferInt(argNameLength, 1, 7)
                .copyArgument(argName, Buffer.CACHE2)
                .showMessage(ScriptData.getDataBufferAll(Buffer.CACHE2))
                .clearBuffer(Buffer.CACHE2)
                .copyString(HexUtil.toHexString("0x"), Buffer.CACHE2)
                .baseConvert(argTo, Buffer.CACHE2, 0, ScriptAssembler.hexadecimalCharset,
                        ScriptAssembler.zeroInherit)
                .showAddress(ScriptData.getDataBufferAll(Buffer.CACHE2))
                .setBufferInt(argDecimal, 0, 20)
                .showAmount(argValue, ScriptData.bufInt)
                .showPressButton()
                // version=04 ScriptAssembler.hash=06=ScriptAssembler.Keccak256 sign=01=ECDSA
                .setHeader(HashType.Keccak256, SignType.ECDSA).getScript();
        return script;
    }

    public static String ETHEIP1559ERC20ScriptSignature
            = "00304502207A63FB17CEA7E123C1BF12CBE3687614FCD677DE13171CC0B275E9659421762A022100ED2A5EC6AB2736C1D9087033CB48181784859A10C8A9674ADEFE34A84D520646";

    private static final String emptyAddress = "0000000000000000000000000000000000000000";
    public static String getETHEIP1559SmartScript() {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptData argTo = sac.getArgument(20);
        ScriptData argValue = sac.getArgumentRightJustified(10);
        ScriptData argGasTipCap = sac.getArgumentRightJustified(10);
        ScriptData argGasFeeCap = sac.getArgumentRightJustified(10);
        ScriptData argGasLimit = sac.getArgumentRightJustified(10);
        ScriptData argNonce = sac.getArgumentRightJustified(8);
        ScriptData argData = sac.getArgumentAll();

        String script
                = // set coinType to 3C
                new ScriptAssembler()
                        .setCoinType(0x3C)
                        // txType (EIP-2718)
                        .copyString("02")
                        .arrayPointer()
                        // chainId
                        .copyString("01")
                        //.copyString("03") // Ropsten chainId
                        .rlpString(argNonce)
                        .rlpString(argGasTipCap)
                        .rlpString(argGasFeeCap)
                        .rlpString(argGasLimit)
                        .ifEqual(argTo, emptyAddress, 
                                new ScriptAssembler()
                                        .copyString("80")
                                        .getScript(), 
                                new ScriptAssembler()
                                        .copyString("94")
                                        .copyArgument(argTo)
                                        .getScript())
                        .rlpString(argValue)
                        .rlpString(argData)
                        // accessList
                        .copyString("C0")
                        .arrayEnd(TYPE_RLP)
                        // txDetail
                        .showMessage("ETH")
                        .showWrap("SMART", "")
                        .showPressButton()
                        // version=04 ScriptAssembler.hash=06=ScriptAssembler.Keccak256 sign=01=ECDSA
                        .setHeader(HashType.Keccak256, SignType.ECDSA)
                        .getScript();

        return script;
    }

    public static String ETHEIP1559SmartScriptSignature
            = Strings.padStart(
                "30450221008E37E8BADC645B749B0608A8DB0A9577798582F96BAEC5EC53694485A9DFFDB7022011A2A6D060577CC185C94AD17AF441AB0F5BCAE301A9F0E7611B0FF8614237C1",
                144, '0');

    public static String getETHEIP1559SmartSegmentScript() {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptData argTo = sac.getArgument(20);
        ScriptData argValue = sac.getArgumentRightJustified(10);
        ScriptData argGasTipCap = sac.getArgumentRightJustified(10);
        ScriptData argGasFeeCap = sac.getArgumentRightJustified(10);
        ScriptData argGasLimit = sac.getArgumentRightJustified(10);
        ScriptData argNonce = sac.getArgumentRightJustified(8);
        ScriptData argData = sac.getArgument(4);

        String script = new ScriptAssembler()
                // set coinType to 3C
                .setCoinType(0x3C)
                // txType (EIP-2718)
                .copyString("02")
                .arrayPointer()
                // chainId
                .copyString("01")
                //.copyString("03") // Ropsten chainId
                .rlpString(argNonce)
                .rlpString(argGasTipCap)
                .rlpString(argGasFeeCap)
                .rlpString(argGasLimit)
                .ifEqual(argTo, emptyAddress, 
                        new ScriptAssembler()
                                .copyString("80")
                                .getScript(), 
                        new ScriptAssembler()
                                .copyString("94")
                                .copyArgument(argTo)
                                .getScript())
                .rlpString(argValue)
                .rlpDataPlaceholder(argData)
                // accessList
                .copyString("C0").arrayEnd(TYPE_RLP).showMessage("ETH").showWrap("SMART", "")
                .showPressButton()
                // version=05 ScriptAssembler.hash=06=ScriptAssembler.Keccak256 sign=01=ECDSA
                .setHeader(HashType.Keccak256, SignType.ECDSA).getScript();
        return script;
    }

    public static String ETHEIP1559SmartSegmentScriptSignature
            = Strings.padStart(
                "304402204FB15EF6265448A62E72CDB21FA585F97D05DE852AFC08CC792FCE1C3CAA472802204DD8B51801089F1D510222664D6D7A9D291671FAC8480458EA5D19E5B354B976",
                144, '0');

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

        String script = new ScriptAssembler()
                // set coinType to 3C
                .setCoinType(0x3C)
                // temp byte for rlpList
                .copyString("C0")
                // nonce
                .rlpString(argNonce)
                // gasPrice
                .rlpString(argGasPrice)
                // gasLimit
                .rlpString(argGasLimit)
                // toAddress
                .copyString("94").copyArgument(argTo)
                // value
                .rlpString(argValue)
                // data
                .copyString("80")
                // chainId v
                // + ScriptAssembler.rlpString(argChainId)
                .copyString("01", Buffer.CACHE1)
                .rlpString(ScriptData.getDataBufferAll(Buffer.CACHE1))
                // r,s
                .copyString("8080")
                .rlpList(1)
                // txDetail
                .showMessage("ETH")
                .copyString(HexUtil.toHexString("0x"), Buffer.CACHE2)
                .baseConvert(argTo, Buffer.CACHE2, 0, ScriptAssembler.hexadecimalCharset,
                        ScriptAssembler.zeroInherit)
                .showAddress(ScriptData.getDataBufferAll(Buffer.CACHE2))
                .showAmount(argValue, 18)
                .showPressButton()
                // version=00 ScriptAssembler.hash=06=ScriptAssembler.Keccak256 sign=01=ECDSA
                .setHeader(HashType.Keccak256, SignType.ECDSA).getScript();
        return script;
    }

    public static String ETHScriptSignature
            = "0030450220201C3ADEEF531C6CD6E8F082477FF048E45F39B85086C2F40BE96840CA4840F6022100C8A36252C7606D9F2D9E6F58538F967C7F6DEFEE52B536439512CB8CD9993DB0";

    /*
     * f86a 1e 85 01718c7e00 83 030d40 94 86fa049857e0209aa7d9e616f7eb3b3b78ecfdb0 80 b844 a9059cbb
     * 0000000000000000000000009e3a6e03298780078003d3965689b8f653ee87b1
     * 0000000000000000000000000000000000000000000000004563918244f40000 01 80 80
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

        String script = new ScriptAssembler()
                // set coinType to 3C
                .setCoinType(0x3C)
                .copyString("F800")
                .rlpString(argNonce)
                .rlpString(argGasPrice)
                .rlpString(argGasLimit)
                // contract address
                .copyString("94").copyArgument(argContractAddress)
                // data, Length = 68
                .copyString("80B844a9059cbb000000000000000000000000").copyArgument(argTo)
                .copyString("0000000000000000000000000000000000000000").copyArgument(argValue)
                // chainId v
                // + ScriptAssembler.rlpString(argChainId)
                .copyString("01", Buffer.CACHE1)
                .rlpString(ScriptData.getDataBufferAll(Buffer.CACHE1))
                // r,s
                .copyString("8080")
                .rlpList(2)
                // txDetail
                .showMessage("ETH")
                .ifSigned(argTokenInfo, argSign, "",
                        new ScriptAssembler().copyString(HexUtil.toHexString("@"), Buffer.CACHE2)
                                .getScript())
                .setBufferInt(argNameLength, 1, 7)
                .copyArgument(argName, Buffer.CACHE2)
                .showMessage(ScriptData.getDataBufferAll(Buffer.CACHE2))
                .clearBuffer(Buffer.CACHE2)
                .copyString(HexUtil.toHexString("0x"), Buffer.CACHE2)
                .baseConvert(argTo, Buffer.CACHE2, 0, ScriptAssembler.hexadecimalCharset,
                        ScriptAssembler.zeroInherit)
                .showAddress(ScriptData.getDataBufferAll(Buffer.CACHE2))
                .setBufferInt(argDecimal, 0, 20)
                .showAmount(argValue, ScriptData.bufInt)
                .showPressButton()
                // version=00 ScriptAssembler.hash=06=ScriptAssembler.Keccak256 sign=01=ECDSA
                .setHeader(HashType.Keccak256, SignType.ECDSA).getScript();
        return script;
    }

    public static String ERC20ScriptSignature
            = "30460221009A706915A2EE0AE663ACF90D9DD59BBEEC111EB12B099E4751219DDC993A01E7022100BA25635AB68F4EF7711D8D880A0BB1A81CA899C78884ECC4183B715F8F047D69";

    public static String getEtherContractBlindScript() {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptData argTo = sac.getArgument(20);
        ScriptData argValue = sac.getArgumentRightJustified(10);
        ScriptData argGasPrice = sac.getArgumentRightJustified(10);
        ScriptData argGasLimit = sac.getArgumentRightJustified(10);
        ScriptData argNonce = sac.getArgumentRightJustified(8);
        ScriptData argChainId = sac.getArgumentRightJustified(2);
        ScriptData argData = sac.getArgumentAll();

        String script
                = // set coinType to 3C
                new ScriptAssembler()
                        .setCoinType(0x3c)
                        .copyString("F800")
                        // nonce
                        .rlpString(argNonce)
                        // gasPrice
                        .rlpString(argGasPrice)
                        // gasLimit
                        .rlpString(argGasLimit)
                        // toAddress
                        .ifEqual(argTo, emptyAddress, 
                                new ScriptAssembler()
                                        .copyString("80")
                                        .getScript(), 
                                new ScriptAssembler()
                                        .copyString("94")
                                        .copyArgument(argTo)
                                        .getScript())
                        // value
                        .rlpString(argValue)
                        // data
                        .rlpString(argData)
                        // chainId v
                        // .rlpString(argChainId)
                        .copyString("01", Buffer.CACHE1)
                        //.copyString("03", Buffer.CACHE1)  // Ropsten chainId
                        .rlpString(ScriptData.getDataBufferAll(Buffer.CACHE1))
                        // r, s
                        .copyString("8080").rlpList(2)
                        // txDetail
                        .showMessage("ETH").showWrap("SMART", "").showPressButton()
                        // version=00 ScriptAssembler.hash=06=ScriptAssembler.Keccak256
                        // sign=01=ECDSA
                        .setHeader(HashType.Keccak256, SignType.ECDSA).getScript();
        return script;
    }

    public static String EtherContractBlindScriptSignature
            = Strings.padStart(
                "304602210089EC4D1DDD9EEDA00C2A10DDB1513B7CF9EE13C36067688C52CA54A681A14A650221008D15D8509DC140D3DBB81D31C01CAD1917812B5C18A613186F904BC84857BA1C",
                144, '0');

    public static String getEtherContractBlindSegmentScript() {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptData argTo = sac.getArgument(20);
        ScriptData argValue = sac.getArgumentRightJustified(10);
        ScriptData argGasPrice = sac.getArgumentRightJustified(10);
        ScriptData argGasLimit = sac.getArgumentRightJustified(10);
        ScriptData argNonce = sac.getArgumentRightJustified(8);
        ScriptData argData = sac.getArgument(4);

        String script = new ScriptAssembler()
                // set coinType to 3C
                .setCoinType(0x3C).arrayPointer()
                // nonce
                .rlpString(argNonce)
                // gasPrice
                .rlpString(argGasPrice)
                // gasLimit
                .rlpString(argGasLimit)
                // toAddress
                .ifEqual(argTo, emptyAddress, 
                        new ScriptAssembler()
                                .copyString("80")
                                .getScript(), 
                        new ScriptAssembler()
                                .copyString("94")
                                .copyArgument(argTo)
                                .getScript())
                // value
                .rlpString(argValue)
                // data
                .rlpDataPlaceholder(argData)
                // chainId v
                .copyString("01", Buffer.CACHE1)
                //.copyString("03", Buffer.CACHE1)  // Ropsten chainId
                .rlpString(ScriptData.getDataBufferAll(Buffer.CACHE1))
                .copyString("8080")
                .arrayEnd(TYPE_RLP)
                // txDetail
                .showMessage("ETH")
                .showWrap("SMART", "")
                .showPressButton()
                // version=05 ScriptAssembler.hash=06=ScriptAssembler.Keccak256 sign=01=ECDSA
                .setHeader(HashType.Keccak256, SignType.ECDSA).getScript();
        return script;
    }

    public static String EtherContractBlindSegmentScriptSignature
            = Strings.padStart(
                "30450220418A010D1F92DAD570FE7D74424BB28927A14474AB68A008440EB4B49F2B77E30221008C3E598B4BE9D33F54C8C04B40AF5136733FE497791A2E43D107EE53E4D87DD6",
                144, '0');

    public static String getEtherMessageBlindScript() {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptData argMessage = sac.getArgumentAll();

        String script = new ScriptAssembler()
                // set coinType to 3C
                .setCoinType(0x3C)
                .copyString("19457468657265756D205369676E6564204D6573736167653A0A")
                .copyArgument(argMessage)
                // txDetail
                .showMessage("ETH")
                .showWrap("MESSAGE", "")
                .showPressButton()
                // version=00 ScriptAssembler.hash=06=ScriptAssembler.Keccak256 sign=01=ECDSA
                .setHeader(HashType.Keccak256, SignType.ECDSA).getScript();
        return script;
    }

    public static String EtherMessageBlindScriptSignature
            = "0000304402200745C5665A9CE0FA0C2894E77629A33077D9AE76F23566DC804C64BF38D27FC0022076645BEEF5A522A02D272DA3D7065D1F092C5C03B024A3F1B3A19C144CF98970";

    public static String getEtherTypedDataBlindScript() {
        ScriptArgumentComposer sac = new ScriptArgumentComposer();
        ScriptData argDomainSeparator = sac.getArgument(32);
        ScriptData argMessage = sac.getArgumentAll();

        String script = new ScriptAssembler()
                // set coinType to 3C
                .setCoinType(0x3C)
                .copyString("1901")
                .copyArgument(argDomainSeparator)
                .hash(argMessage, Buffer.TRANSACTION, HashType.Keccak256)
                // txDetail
                .showMessage("ETH")
                .showWrap("EIP712", "")
                .showPressButton()
                // version=00 ScriptAssembler.hash=06=ScriptAssembler.Keccak256 sign=01=ECDSA
                .setHeader(HashType.Keccak256, SignType.ECDSA).getScript();
        return script;
    }

}
