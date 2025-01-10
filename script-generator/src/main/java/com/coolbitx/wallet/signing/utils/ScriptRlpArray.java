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
//public class ScriptRlpArray extends ScriptRlpData {
public class ScriptRlpArray extends ScriptArrayAbstract {

    protected int rlpLayer;
    protected int rlpIndex;
    protected byte[] path;
    private int rlpSubIndex;

    public ScriptRlpArray() {
        super();
        this.rlpLayer = -1;
        this.rlpIndex = 0;
        this.rlpSubIndex = 0;
        this.path = new byte[1];
        this.path[0] = (byte) rlpLayer;
    }

    private ScriptRlpArray(int rlpLayer, int rlpIndex, byte[] path) {
        super();
        this.rlpLayer = rlpLayer;
        this.rlpIndex = rlpIndex;
        this.rlpSubIndex = 0;
        if (rlpLayer + 1 > 0) {
            this.path = new byte[rlpLayer + 1];
        }
        if (path != null) {
            System.arraycopy(path, 0, this.path, 0, path.length);
        }
        this.path[rlpLayer] = (byte) rlpIndex;
    }

    public ScriptRlpItem getRlpItemArgument() {
        int rlpItemLayer = rlpLayer + 1;
        ScriptRlpItem item = ScriptRlpItem.createBuffer(rlpItemLayer, rlpSubIndex, path);
        rlpSubIndex++;
        return item;
    }

    public ScriptRlpArray getRlpArrayArgument() {
        int rlpSubLayer = rlpLayer + 1;
        ScriptRlpArray array = new ScriptRlpArray(rlpSubLayer, rlpSubIndex, path);
        rlpSubIndex++;
        return array;
    }

    @Override
    public String toString() {
        return "[Array: " + "rlpLayer=" + rlpLayer + ", rlpIndex=" + rlpIndex + ", path=" + Hex.encode(path) + "]";
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
