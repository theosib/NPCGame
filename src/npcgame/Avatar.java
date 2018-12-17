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
public class Avatar {
    Map<String,Property> props = new HashMap<>();
    Map<String,Script> scripts = new HashMap<>();
    
    public Script findScript(String name) {
        Script s = scripts.get(name);
        if (s == null && this != NPCGame.globals) {
            s = NPCGame.globals.findScript(name);
        }
        return s;
    }
    
    public Property execScript(String name) {
        Script s = findScript(name);
        if (s == null) return Property.null_prop;
        return s.execute(this);
    }
    
    public void setScript(String name, String trigger, String code) {
        scripts.put(name, new Script(trigger, code));
    }
    
    public Property findProp(String name) {
        if (name.substring(0, 5).equalsIgnoreCase("game.")) {
            return NPCGame.globals.findProp(name.substring(5));
        }
        
        Property prop = props.get(name);
        if (prop == null) return Property.null_prop;
        return prop;
    }
    
    public void setProp(String name, Property val) {
        if (name.substring(0, 5).equalsIgnoreCase("game.")) {
            NPCGame.globals.setProp(name.substring(5), val);
            return;
        }

        props.put(name, val);
    }
    
    public void tick(double delta) {
        boolean moving = findProp("moving").getBool();
        if (moving) {
            double[] pos = findProp("pos").getVec();
        }
    }
}
