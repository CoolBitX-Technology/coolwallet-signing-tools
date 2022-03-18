package com.coolbitx.wallet.signing.main;

import com.coolbitx.wallet.signing.scriptlib.*;

public class AllScript {

    public static void main(String[] args) throws Exception {
        System.out.println("----- script list -----");
        AtomScript.listAll();
        BnbScript.listAll();
        BtcFamilyScript.listAll();
        DotScript.listAll();
        EthScript.listAll();
        IcxScript.listAll();
        TrxScript.listAll();
        XlmScript.listAll();
        XrpScript.listAll();
        AdaScript.listAll();
        EtcScript.listAll();
        CronosScript.listAll();
        CroScript.listAll();
        XtzScript.listAll();
        LunaScript.listAll();
        MaticScript.listAll();
        TerraScript.listAll();
    }

}
