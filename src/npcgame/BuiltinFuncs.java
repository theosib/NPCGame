/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package npcgame;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author millerti
 */
public class BuiltinFuncs {
    static interface Func {
        Property exec(Code.Context con);
    }
    
    private static Map<String,Func> funcTable;
    static {
        funcTable = new HashMap<>();
        funcTable.put("random", (con) -> new Property(Math.random()));
    }
    
    static Property call(String name, Code.Context con) {
        Func f = funcTable.get(name);
        if (f == null) return null;
        return f.exec(con);
    }
}
