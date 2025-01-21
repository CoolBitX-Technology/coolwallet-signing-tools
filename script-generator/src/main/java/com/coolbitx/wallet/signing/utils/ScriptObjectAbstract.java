/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.coolbitx.wallet.signing.utils;

import com.coolbitx.wallet.signing.utils.ScriptInterface.DataType;

/**
 *
 * @author hank.liu
 */
public abstract class ScriptObjectAbstract implements ScriptInterface {

    public DataType getType() {
        return DataType.OBJECT;
    }
}
