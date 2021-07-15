/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.coolbitx.wallet.signing.utils;

import com.coolbitx.wallet.signing.utils.ScriptData.Buffer;

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
     * Get specified length argument data, and add the length to the offset of
     * argument buffer.
     *
     * @param length The length of argument.
     * @return Buffer data with fixed length.
     */
    public ScriptData getArgument(int length) {
        ScriptData db = ScriptData.getBufer(Buffer.ARGUMENT, this.offset, length);
        this.offset += length;
        return db;
    }

    /**
     * Get specified length argument data, if the data length is not long
     * enough, padding zero on the left-side to the specified length. Will add
     * length to the offset of argument buffer.
     *
     * @param length The length of argument.
     * @return Buffer data with zero padding to left-side of argument.
     */
    public ScriptData getArgumentRightJustified(int length) {
        ScriptData db = ScriptData.getBufer(Buffer.ARGUMENT, this.offset, -length);
        this.offset += length;
        return db;
    }

    /**
     * Get specified range long argument data, and won't change the offset of
     * argument buffer.
     *
     * @param offset The offset diff from ScriptArgumentComposer offset now.
     * @param length The length of argument data.
     * @return Buffer data with fixed length.
     */
    public ScriptData getArgumentUnion(int offset, int length) {
        return ScriptData.getBufer(Buffer.ARGUMENT, this.offset + offset, length);
    }

    /**
     * Get specified length argument data, but the actual usage length defined
     * in BufferInt. Will add length to the offset of argument buffer.
     *
     * @param length The max length of argument.
     * @return Buffer data with zero padding to left-side of argument.
     */
    public ScriptData getArgumentVariableLength(int length) {
        ScriptData db = ScriptData.getBufer(Buffer.ARGUMENT, this.offset, ScriptData.bufInt);
        this.offset += length;
        return db;
    }

    /**
     * Get all the rest argument buffer.
     *
     * @return Buffer data with the rest argument length.
     */
    public ScriptData getArgumentAll() {
        ScriptData db = ScriptData.getBufer(Buffer.ARGUMENT, this.offset, ScriptData.max);
        this.offset += 9999;
        return db;
    }
}
