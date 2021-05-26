/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

/**
 *
 * @author Hank Liu <hankliu@coolbitx.com>
 */
public class ScriptBuffer {

    protected BufferType bufferType;
    protected int offset;
    protected int length;

    private ScriptBuffer(BufferType type, int offset, int length) {
        this.bufferType = type;
        this.offset = offset;
        this.length = length;
    }

    public static enum BufferType {
        ARGUMENT,
        TRANSACTION,
        EXTENDED,
        FREE;
    }

    @Override
    public String toString() {
        return "[" + "bufferType=" + bufferType + ", offset=" + offset + ", length=" + length + "]";
    }

    public static final int bufInt = 1000;
    public static final int max = 2000;

    public static ScriptBuffer getBufer(BufferType bufferType, int offset, int length) {
        return new ScriptBuffer(bufferType, offset, length);
    }

    public static ScriptBuffer getDataBufferAll(BufferType buf) {
        return getDataBufferAll(buf, 0);
    }

    public static ScriptBuffer getDataBufferAll(BufferType buf, int offset) {
        return new ScriptBuffer(buf, offset, max);
    }
}
