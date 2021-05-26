/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import utils.ScriptBuffer.BufferType;

/**
 *
 * @author Hank Liu <hankliu@coolbitx.com>
 */
public class ScriptArgumentComposer {

    private int offset;

    public ScriptArgumentComposer() {
        this.offset = 0;
    }

    public ScriptBuffer getArgument(int length) {
        ScriptBuffer db = ScriptBuffer.getBufer(BufferType.ARGUMENT, this.offset, length);
        this.offset += length;
        return db;
    }

    public ScriptBuffer getArgumentRightJustified(int length) {
        ScriptBuffer db = ScriptBuffer.getBufer(BufferType.ARGUMENT, this.offset, -length);
        this.offset += length;
        return db;
    }

    public ScriptBuffer getArgumentUnion(int offset, int length) {
        return ScriptBuffer.getBufer(BufferType.ARGUMENT, this.offset + offset, length);
    }
    
    public ScriptBuffer getArgumentVariableLength(int length) {
        ScriptBuffer db =  ScriptBuffer.getBufer(BufferType.ARGUMENT, this.offset, ScriptBuffer.bufInt);
        this.offset += length;
        return db;
    }

    public ScriptBuffer getArgumentAll() {
        ScriptBuffer db =  ScriptBuffer.getBufer(BufferType.ARGUMENT, this.offset, ScriptBuffer.max);
        this.offset += 9999;
        return db;
    }

//    private static int getDataOffset(String data) {
//        String range = data.substring(8, data.length());
//
//        String s = range.substring(1, 4);
//        int ret;
//        switch (s) {
//            case "buf":
//                ret = 1000;
//                break;
//            case "max":
//                ret = 2000;
//                break;
//            default:
//                ret = Integer.parseInt(s);
//                break;
//        }
//
//        return ret;
//    }
//
//    private static int getDataLength(String data) {
//        String range = data.substring(8, data.length());
//
//        String s = range.substring(5, 8);
//        int ret;
//        switch (s) {
//            case "buf":
//                ret = 1000;
//                break;
//            case "max":
//                ret = 2000;
//                break;
//            default:
//                ret = Integer.parseInt(s);
//                break;
//        }
//
//        if (data.charAt(4) == '~') {
//            ret -= getDataOffset(data);
//        }
//
//        return ret;
//    }
}
