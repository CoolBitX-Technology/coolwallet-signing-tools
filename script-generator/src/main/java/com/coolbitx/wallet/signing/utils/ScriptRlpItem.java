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
public class ScriptRlpItem extends ScriptRlpData {

    private ScriptRlpItem(int rlpLayer, int rlpIndex, byte[] path) {
        super();
        this.rlpLayer = rlpLayer;
        this.rlpIndex = rlpIndex;
        this.path = new byte[rlpLayer + 1];
        if (path != null) {
            System.arraycopy(path, 0, this.path, 0, path.length);
        }
        this.path[rlpLayer] = (byte) rlpIndex;
    }

    public ScriptRlpItem() {
        super();
    }

    public int getRlpLayer() {
        return rlpLayer;
    }

    public void setRlpLayer(int rlpLayer) {
        this.rlpLayer = rlpLayer;
    }

    public int getRlpIndex() {
        return rlpIndex;
    }

    public void setRlpIndex(int rlpIndex) {
        this.rlpIndex = rlpIndex;
    }

    @Override
    public String toString() {
        return "[Item: " + "rlpLayer=" + rlpLayer + ", rlpIndex=" + rlpIndex + ", path=" + Hex.encode(path) + "]";
    }
//
    public static ScriptRlpItem createBuffer(int rlpLayer, int rlpIndex, byte[] path) {
        ScriptRlpItem item = new ScriptRlpItem(rlpLayer, rlpIndex, path);
        return item;
    }
//
//    @Override
//    public int getBufferParameter1() {
//        return rlpLayer;
//    }
//
//    @Override
//    public void setBufferParameter1(int parameter) {
//        this.rlpLayer = parameter;
//    }
//
//    @Override
//    public int getBufferParameter2() {
//        return 0;
//    }
//
//    @Override
//    public void setBufferParameter2(int parameter) {
//    }

}
