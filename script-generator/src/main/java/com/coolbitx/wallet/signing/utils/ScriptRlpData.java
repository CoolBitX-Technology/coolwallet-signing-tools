/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.coolbitx.wallet.signing.utils;

/**
 *
 * @author hank.liu
 */
public abstract class ScriptRlpData extends ScriptObjectAbstract {

    protected int rlpLayer;
    protected int rlpIndex;
    protected byte[] path;

    private static class ScriptRlpDataImpl extends ScriptRlpData {

        private ScriptRlpDataImpl(int rlpLayer, int rlpIndex, byte[] path) {
            super(rlpLayer, rlpIndex, path);
        }
    }

    private ScriptRlpData(int rlpLayer, int rlpIndex, byte[] path) {
        super();
        this.rlpLayer = rlpLayer;
        this.rlpIndex = rlpIndex;
        this.path = new byte[rlpLayer + 1];
        if (path != null) {
            System.arraycopy(path, 0, this.path, 0, path.length);
        }
        this.path[rlpLayer] = (byte) rlpIndex;
    }

    @Override
    public String toString() {
        return "[Item: " + "rlpLayer=" + rlpLayer + ", rlpIndex=" + rlpIndex + ", path=" + Hex.encode(path) + "]";
    }

    public static ScriptRlpData createBuffer(int rlpLayer, int rlpIndex, byte[] path) {
        ScriptRlpData item = new ScriptRlpDataImpl(rlpLayer, rlpIndex, path);
        return item;
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

    public byte[] getPath() {
        return this.path;
    }

}
