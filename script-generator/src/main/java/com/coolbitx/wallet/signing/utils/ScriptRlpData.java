/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.coolbitx.wallet.signing.utils;

/**
 *
 * @author Hank Liu (hankliu@coolbitx.com)
 */
public class ScriptRlpData implements ScriptDataInterface {

    public enum Buffer {
        RLP_ITEM;
    }


    protected Buffer bufferType;
    private int rlpCount;

    private ScriptRlpData(int rlpCount) {
        this.bufferType = Buffer.RLP_ITEM;
        this.rlpCount = rlpCount;
    }

    @Override
    public String toString() {
        return "[" + "rlpCount=" + rlpCount + "]";
    }

    public static ScriptRlpData getBuffer(int rlpCount) {
        return new ScriptRlpData(rlpCount);
    }

    @Override
    public int getBufferParameter1() {
        return rlpCount;
    }

    @Override
    public void setBufferParameter1(int parameter) {
        this.rlpCount = parameter;
    }

    @Override
    public int getBufferParameter2() {
        return 0;
    }

    @Override
    public void setBufferParameter2(int parameter) {
    }

}
