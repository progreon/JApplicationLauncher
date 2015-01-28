/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package japplicationlauncher;

/**
 *
 * @author marco
 */
public class JApplicationLauncher {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Main_Gui main = new Main_Gui(true);
        main.setLocationRelativeTo(null);
        main.setTitle("Application launcher");
        main.setVisible(true);
        main.load();
    }
    
}
