/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package japplicationlauncher;

import japplicationlauncher.helpers.Version;
import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The updater handles checking for updates, updating and getting the history
 * page.
 *
 * @author marco
 */
public class Updater {

    private final ResourceBundle rsb;
    private final String resourceFile = "updater";

    private final String keyVersionUrl = "versionURL";
    private final String keyHistoryUrl = "historyURL";
//    private final String keyUpdateRootFolder = "updateRootFolderURL";

    private final String offlineHistory = "<html><body style='background-color: #7b3803; color: #cccccc; padding: 10'>"
            + "<h2>This is the offline history page!</h2>"
            + "</body></html>";

    /**
     * TODO
     */
    public Updater() {
        rsb = ResourceBundle.getBundle(resourceFile);
    }

    /**
     * TODO
     *
     * @return
     * @throws Exception
     */
    public Version getLatestVersion() throws Exception {
        String data = getData(rsb.getString(keyVersionUrl));
        data = data.substring(data.indexOf("[version]") + 9, data.indexOf("[/version]"));
        Version version = new Version(data);
        System.out.println("Latest version = " + version.getInfo());

        return version;
    }

    /**
     * TODO
     *
     * @return
     * @throws Exception
     */
    public Version getLatestSnapshot() throws Exception {
        String data = getData(rsb.getString(keyVersionUrl));
        data = data.substring(data.indexOf("[snapshot]") + 10, data.indexOf("[/snapshot]"));
        Version version = new Version(data);
        System.out.println("Latest snapshot = " + version.getInfo());

        return version;
    }

    /**
     * TODO
     *
     * @return
     */
    public String getHistory() {
        String history;
        System.out.println("History URL = " + rsb.getString(keyHistoryUrl));
        try {
            history = getData(rsb.getString(keyHistoryUrl));
            history = history.substring(history.indexOf("<html"), history.indexOf("</html>") + 7);
        } catch (Exception ex) {
            Logger.getLogger(Updater.class.getName()).log(Level.WARNING, ex.getMessage());
            System.out.println("Couldn't get online history page, loading local one ...");
            history = offlineHistory;
        }
        return history;
    }

    /**
     * TODO
     */
    public void update() {
        // TODO
        throw new UnsupportedOperationException("update(): Not supported yet.");
    }

    /**
     * TODO
     *
     * @param address
     * @return
     * @throws Exception
     */
    public String getData(String address) throws Exception {
        URL url = new URL(address);
        InputStream html = url.openStream();

        int c = 0;
        String buffer = "";

        while (c != -1) {
            c = html.read();
            buffer += (char) c;
        }

        return buffer;
    }

}
