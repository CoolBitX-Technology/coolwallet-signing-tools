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
public class ScriptRlpArgumentComposer {

    private int rlpCount;

    public ScriptRlpArgumentComposer() {
        this.rlpCount = 0;
    }

    /**
     * Get an argument which length is determined by argument(Encoded in rlp).
     *
     * <code>
     *   // The argument of address should be:
     *   // 94(RLP encoded prefix) 2b0b9ae953fca925f0aeb04dec3054b996a05ed1(address in hex)
     *   ScriptData address = sac.getArgumentRLPItem();
     * </code>
     *
     * @return Buffer data with the rest argument length.
     */
    public ScriptRlpData getArgumentRlpItem() {
        return ScriptRlpData.getBuffer(rlpCount++);
    }
}
