/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package japplicationlauncher.helpers;

/**
 * A class that represents the release status of an application, one of: alpha,
 * beta, release, snapshot.
 *
 * @author marco
 */
public final class ReleaseStatus {

    public static ReleaseStatus ALPHA = new ReleaseStatus("alpha", 0);
    public static ReleaseStatus BETA = new ReleaseStatus("beta", 1);
    public static ReleaseStatus RELEASE = new ReleaseStatus("release", 2);
    public static ReleaseStatus SNAPSHOT = new ReleaseStatus("snapshot", -1);

    private final String status;
    /**
     * TODO
     */
    public final int level;

    private ReleaseStatus(String status, int id) {
        this.status = status;
        this.level = id;
    }

    @Override
    public String toString() {
        return status;
    }

}
