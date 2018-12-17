/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package npcgame;

import java.util.List;

/**
 *
 * @author millerti
 */
public class Script {
    Code trigger, actions;
    
    Script() {}
    Script(String t, String a) {
        trigger = Code.parse(t);
        actions = Code.parse(a);
    }
    
    public boolean test(Avatar av) {
        return trigger.execute(av).getBool();
    }
        
    public Property execute(Avatar av) {
        return actions.execute(av);
    }
}
