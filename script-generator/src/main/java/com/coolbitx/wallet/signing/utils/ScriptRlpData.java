/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.coolbitx.wallet.signing.utils;

/**
 *
 * @author hank.liu
 */
public abstract class ScriptRlpData implements ScriptDataInterface {

    protected int rlpLayer;
    protected int rlpIndex;
    protected byte[] path;

    public ScriptRlpData() {
    }

    @Override
    public int getBufferParameter1() {
        return rlpLayer;
    }

    @Override
    public void setBufferParameter1(int parameter) {
        this.rlpLayer = parameter;
    }

    @Override
    public int getBufferParameter2() {
        return rlpIndex;
    }

    @Override
    public void setBufferParameter2(int parameter) {
        this.rlpIndex = parameter;
    }
}
