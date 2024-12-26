/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.coolbitx.wallet.signing.utils;

import com.coolbitx.wallet.signing.utils.ScriptRlpData.MutableScriptData;

/**
 * This class is designed for creating diverse script data which is used in
 * generating script in ScriptAssembler class.
 *
 * @author Hank Liu (hankliu@coolbitx.com)
 */
public class ScriptRlpArgumentComposer {

    private int rlpLayer;
    private int rlpIndex;

    public ScriptRlpArgumentComposer() {
        this.rlpLayer = 0;
        this.rlpIndex = 0;

    }

    public ScriptRlpData getRlpItemArgument() {
        ScriptRlpData item = ScriptRlpData.createBuffer(rlpIndex);
        rlpIndex++;
        return item;
    }

//    public MutableScriptDataInterface getRlpArrayArgument(ScriptRlpData... rlpItems) {
//        if (rlpItems.length == 0) {
//
//        }
//
//        return ScriptRlpData.getMultiBuffer(rlpCount++);
//    }
    public MutableScriptData getRlpArrayArgument() {
        return ScriptRlpData.createMutableBuffer(this.rlpLayer, this.rlpIndex);
    }

}
