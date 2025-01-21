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
public class ScriptData extends ScriptObjectAbstract {

    public enum Buffer {
        ARGUMENT,
        TRANSACTION,
        CACHE1,
        CACHE2;
    }

    public static final int bufInt = 88888888;
    public static final int max = 99999999;

    protected Buffer bufferType;
    private int offset;
    private int length;

    @Override
    public int getBufferParameter1() {
        return offset;
    }

    @Override
    public void setBufferParameter1(int parameter) {
        this.offset = parameter;
    }

    @Override
    public int getBufferParameter2() {
        return length;
    }

    @Override
    public void setBufferParameter2(int parameter) {
        this.length = parameter;
    }

    private ScriptData(Buffer type, int offset, int length) {
        this.bufferType = type;
        this.offset = offset;
        this.length = length;
    }

    @Override
    public String toString() {
        return "[" + "bufferType=" + bufferType + ", offset=" + offset + ", length=" + length + "]";
    }

    public static ScriptData getBuffer(Buffer bufferType, int offset, int length) {
        return new ScriptData(bufferType, offset, length);
    }

    public static ScriptData getDataBufferAll(Buffer buf) {
        return getDataBufferAll(buf, 0);
    }

    public static ScriptData getDataBufferAll(Buffer buf, int offset) {
        return new ScriptData(buf, offset, max);
    }
}
