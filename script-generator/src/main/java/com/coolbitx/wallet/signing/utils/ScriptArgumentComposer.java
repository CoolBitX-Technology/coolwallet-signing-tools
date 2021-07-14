/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.coolbitx.wallet.signing.utils;

import com.coolbitx.wallet.signing.utils.ScriptData.BufferType;

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
    public ScriptData getArgument(int length) {
        ScriptData db = ScriptData.getBufer(BufferType.ARGUMENT, this.offset, length);
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
    public ScriptData getArgumentRightJustified(int length) {
        ScriptData db = ScriptData.getBufer(BufferType.ARGUMENT, this.offset, -length);
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
    public ScriptData getArgumentUnion(int offset, int length) {
        return ScriptData.getBufer(BufferType.ARGUMENT, this.offset + offset, length);
    }

    /**
     * Get fixed length long argument buffer, but the actual usage length
     * defined in BufferInt. Will add length to the ScriptArgumentComposer offset.
     *
     * @param length The max length of argument.
     * @return Buffer object with zero padding to left-side of argument.
     */
    public ScriptData getArgumentVariableLength(int length) {
        ScriptData db = ScriptData.getBufer(BufferType.ARGUMENT, this.offset, ScriptData.bufInt);
        this.offset += length;
        return db;
    }

    /**
     * Get all rest argument buffer.
     *
     * @return Buffer object with the rest argument length.
     */
    public ScriptData getArgumentAll() {
        ScriptData db = ScriptData.getBufer(BufferType.ARGUMENT, this.offset, ScriptData.max);
        this.offset += 9999;
        return db;
    }
}
