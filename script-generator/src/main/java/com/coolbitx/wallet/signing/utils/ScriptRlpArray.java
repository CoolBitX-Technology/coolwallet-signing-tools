/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.coolbitx.wallet.signing.utils;

/**
 * This class is designed for creating diverse script data which is used in
 * generating script in ScriptAssembler class.
 *
 * @author Hank Liu (hankliu@coolbitx.com)
 */
public class ScriptRlpArray extends ScriptRlpItem implements MutableScriptDataInterface {

    private int rlpLayer;
    private int rlpIndex;

    public ScriptRlpArray() {
        super();
        this.rlpLayer = 0;
        this.rlpIndex = 0;
    }

    private ScriptRlpArray(int rlpLayer) {
        super();
        this.rlpLayer = rlpLayer;
        this.rlpIndex = 0;
    }

    public ScriptRlpItem getRlpItemArgument() {
        ScriptRlpItem item = ScriptRlpItem.createBuffer(rlpLayer, rlpIndex);
        rlpIndex++;
        return item;
    }

    public ScriptRlpArray getRlpArrayArgument() {
        int newRlpLayer = rlpLayer + 1;
        ScriptRlpArray array = new ScriptRlpArray(newRlpLayer);
        rlpIndex++;
        return array;
    }

    public static ScriptRlpArray createMutableBuffer(int rlpIndex) {
        return new ScriptRlpArray();
    }

//    public MutableScriptDataInterface getRlpArrayArgument(ScriptRlpData... rlpItems) {
//        if (rlpItems.length == 0) {
//
//        }
//
//        return ScriptRlpData.getMultiBuffer(rlpCount++);
//    }
//    public MutableScriptData getRlpArrayArgument() {
//        return ScriptRlpItemData.createMutableBuffer(this.rlpLayer, this.rlpIndex);
//    }
    @Override
    public int getDataAmonut() {
        return 0;
    }

    @Override
    public void setDataAmonut(int parameter) {
    }

}
