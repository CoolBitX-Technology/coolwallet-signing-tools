/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.coolbitx.wallet.signing.utils;

import com.coolbitx.wallet.signing.utils.ScriptBuffer.BufferType;

/**
 *
 * @author Hank Liu (hankliu@coolbitx.com)
 */
public class ScriptArgumentComposer {

    private int offset;

    public ScriptArgumentComposer() {
        this.offset = 0;
    }

    /**
     * Get fixed length long argument buffer, and add length to the
     * ScriptArgumentComposer‘s offset.
     *
     * @param length The length of argument.
     * @return Buffer object with fixed length.
     */
    public ScriptBuffer getArgument(int length) {
        ScriptBuffer db = ScriptBuffer.getBufer(BufferType.ARGUMENT, this.offset, length);
        this.offset += length;
        return db;
    }

    /**
     * Get variable length long argument buffer, and add length to the
     * ScriptArgumentComposer offset.
     *
     * @param length The length of argument.
     * @return Buffer object with zero padding to left-side of argument.
     */
    public ScriptBuffer getArgumentRightJustified(int length) {
        ScriptBuffer db = ScriptBuffer.getBufer(BufferType.ARGUMENT, this.offset, -length);
        this.offset += length;
        return db;
    }

    /**
     * Get specified range long argument buffer from the offset now, and won't
     * change the ScriptArgumentComposer offset.
     *
     * @param offset The offset from ScriptArgumentComposer offset now.
     * @param length The length of argument.
     * @return Buffer object with fixed length.
     */
    public ScriptBuffer getArgumentUnion(int offset, int length) {
        return ScriptBuffer.getBufer(BufferType.ARGUMENT, this.offset + offset, length);
    }

    /**
     * Get fixed length long argument buffer, but the actual usage length
     * defined in BufferInt. Will add length to the ScriptArgumentComposer offset.
     *
     * @param length The max length of argument.
     * @return Buffer object with zero padding to left-side of argument.
     */
    public ScriptBuffer getArgumentVariableLength(int length) {
        ScriptBuffer db = ScriptBuffer.getBufer(BufferType.ARGUMENT, this.offset, ScriptBuffer.bufInt);
        this.offset += length;
        return db;
    }

    /**
     * Get all rest argument buffer.
     *
     * @return Buffer object with the rest argument length.
     */
    public ScriptBuffer getArgumentAll() {
        ScriptBuffer db = ScriptBuffer.getBufer(BufferType.ARGUMENT, this.offset, ScriptBuffer.max);
        this.offset += 9999;
        return db;
    }
}
