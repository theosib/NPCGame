/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package npcgame;

/**
 *
 * @author millerti
 */
public class NPCGame {
    
    static Avatar globals = new Avatar();

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        globals.setScript("base", "", "2 3 + 4 < random$");
        Property out = globals.execScript("base");
        System.out.println(out.getStr());
    }
    
}
