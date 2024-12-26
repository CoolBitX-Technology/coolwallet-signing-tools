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

    private int rlpLayer = 0;
    private int rlpIndex = 0;

    private ScriptRlpData(int rlpIndex) {
        this.rlpIndex = rlpIndex;
    }

    private ScriptRlpData() {
//        this.rlpLayer = rlpLayer;
//        this.rlpIndex = rlpIndex;
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
        return "[" + "rlpLayer=" + rlpLayer + ", rlpIndex=" + rlpIndex + "]";
    }

    public static ScriptRlpData createBuffer(int rlpIndex) {
        ScriptRlpData item = new ScriptRlpData(rlpIndex);
        return item;
    }

    public static MutableScriptData createMutableBuffer(int rlpLayer, int rlpIndex) {
        return new ScriptRlpData().new MutableScriptData(rlpLayer, rlpIndex);
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
        return 0;
    }

    @Override
    public void setBufferParameter2(int parameter) {
    }

    public class MutableScriptData extends ScriptRlpData implements MutableScriptDataInterface {

        private int rlpLayer_;
        private int rlpIndex_;

        private MutableScriptData(int rlpLayer, int rlpIndex) {
            super();
//            this.rlpLayer = 0;
//            this.rlpIndex++;
        }

//        public static MutableScriptData getBuffer1() {
//            return new ScriptRlpData().new MutableScriptData();
//        }
        public ScriptRlpData getRlpItemArgument() {
            return ScriptRlpData.createBuffer(1);
        }

        @Override
        public int getDataAmonut() {
            return 0;
        }

        @Override
        public void setDataAmonut(int parameter) {
        }
    }

}
