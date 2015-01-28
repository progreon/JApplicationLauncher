/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package japplicationlauncher;

import japplicationlauncher.helpers.ReleaseStatus;
import japplicationlauncher.helpers.Version;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

/**
 *
 * @author marco
 */
public class Main_Gui extends JFrame {

    private final ResourceBundle rsb;
    private final String resourceFile = "updater";

    private final Version currrentVersion = new Version(ReleaseStatus.ALPHA, new int[]{0, 1});
    private final boolean checkUpdates;
    private final Updater updater;

    //// GUI part ////
    private JPanel viewPane;

    private JScrollPane scp;
    private JTextPane infoPane;

    private JPanel buttonPane;
    private JLabel lblVersionInfo;
    private JButton btnUpdate;
    private JButton btnStart;
    private JButton btnExit;
    //// End of GUI part ////

    //// Strings ////
    private final String updateString = "Update & start application";
    private final String startString = "Start application";
    private final String exitString = "Exit";

    private final String appJarName = ""; // Without the '.jar' at the end
    private final String startErrorMessage = "Failed to start application!";
    //// End of strings ////

    /**
     * Show the launcher GUI.
     *
     * @param checkUpdates Whether to check for updates or not.
     */
    public Main_Gui(boolean checkUpdates) {
        this.checkUpdates = checkUpdates;
        rsb = ResourceBundle.getBundle(resourceFile);
        updater = new Updater();

        initComponents();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(800, 600));
        pack();
        setResizable(false);
    }

    /**
     * Load content on screen.
     *
     */
    public void load() {
        infoPane.setText(updater.getHistory());
        if (checkUpdates) {
            try {
                Version latestVersion = updater.getLatestVersion();
                lblVersionInfo.setText(lblVersionInfo.getText() + " (latest: " + latestVersion + ")");
                updater.getLatestSnapshot();
                btnUpdate.setEnabled(latestVersion.isNewerThan(currrentVersion));
            } catch (Exception ex) {
                Logger.getLogger(Main_Gui.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void startApp() {
        String appJar = rsb.getString("applicationfolder") + "/" + appJarName;
        String[] command = {"java", "-jar", appJar};
        try {
            File file = new File(appJar);
            if (file.exists()) {
                Runtime.getRuntime().exec(command);
                System.exit(0);
            } else {
                throw new IOException("The application jar does not exist!");
            }
        } catch (IOException ex) {
            Logger.getLogger(Main_Gui.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this, startErrorMessage, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateApp() {
        this.updater.update();
    }

    private void initComponents() {
        infoPane = new JTextPane();
        infoPane.setContentType("text/html");
        infoPane.setEditable(false);
        infoPane.setBorder(null);
        scp = new JScrollPane(infoPane);

        lblVersionInfo = new JLabel("Current version: " + currrentVersion);
        btnUpdate = new JButton(this.updateString);
        btnUpdate.addActionListener((ActionEvent e) -> {
            updateApp();
            startApp();
        });
        btnUpdate.setEnabled(false);
        btnStart = new JButton(this.startString);
        btnStart.addActionListener((ActionEvent e) -> {
            startApp();
        });
        btnExit = new JButton(this.exitString);
        btnExit.addActionListener((ActionEvent e) -> {
            dispose();
        });

        buttonPane = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPane.add(lblVersionInfo);
        buttonPane.add(btnUpdate);
        buttonPane.add(btnStart);
        buttonPane.add(btnExit);

        viewPane = new JPanel(new BorderLayout());
        viewPane.add(scp, BorderLayout.CENTER);
        viewPane.add(buttonPane, BorderLayout.SOUTH);
        setContentPane(viewPane);
    }

}
