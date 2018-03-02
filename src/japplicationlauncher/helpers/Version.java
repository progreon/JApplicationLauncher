/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package japplicationlauncher.helpers;

import java.util.Arrays;
import java.util.Objects;

/**
 * A class used to represent the version of an application, for easy conversion.
 * TODO: check subversion?
 *
 * @author marco
 */
public class Version {

    private ReleaseStatus releaseStatus;
    private int[] versionNo;
    private String subversion;

    /**
     * Parses the given version string. The format used is: - one character that
     * indicates release status alpha (a), beta (b), release (r) or snapshot
     * (s); - the version numbers, levels separated by a dot; - (optional) if a
     * subversion is wanted, you can add it to the string, connected with an
     * '_'; - examples: b1.8.10, r2.3, s2.4_pre1
     *
     * @param version The version string
     * @throws VersionFormatException When the string doesn't follow the
     * formatting rules
     */
    public Version(String version) throws VersionFormatException {
        this.parseVersionString(version);
    }

    /**
     * Takes a ReleaseStatus and an array of the version numbers to form a
     * Version.
     *
     * @param releaseStatus The ReleaseStatus object
     * @param versionNo The array of version numbers (which would normally be
     * separated by dots)
     */
    public Version(ReleaseStatus releaseStatus, int[] versionNo) {
        this(releaseStatus, versionNo, "");
    }

    /**
     * Takes a ReleaseStatus, an array of the version numbers and the subversion
     * to form a Version.
     *
     * @param releaseStatus The ReleaseStatus object
     * @param versionNo The array of version numbers (which would normally be
     * separated by dots)
     * @param subversion The subversion string (which would normally be
     * separated by an underscore)
     */
    public Version(ReleaseStatus releaseStatus, int[] versionNo, String subversion) {
        this.releaseStatus = releaseStatus;
        this.versionNo = new int[versionNo.length];
        System.arraycopy(versionNo, 0, this.versionNo, 0, versionNo.length);
        this.subversion = subversion;
    }

    /**
     * Gets the release status of the version as a string.
     *
     * @return The release status
     */
    public String getReleaseStatus() {
        return this.releaseStatus.toString();
    }

    /**
     * Gets the version number of the version as a string, the numbers separated
     * by dots.
     *
     * @return The version number
     */
    public String getVersionNumber() {
        String no = "" + versionNo[0];
        for (int i = 1; i < versionNo.length; i++) {
            no += "." + versionNo[i];
        }
        return no;
    }

    /**
     * Gets the subversion string of the version.
     *
     * @return The subversion
     */
    public String getSubversion() {
        return subversion;
    }

    /**
     * Gets all the info of the version in one string (but leaves out
     * 'release').
     *
     * @return Info
     */
    public String getInfo() {
        return (releaseStatus == ReleaseStatus.RELEASE ? "" : (releaseStatus + " ")) + getVersionNumber() + (subversion.equals("") ? "" : ("_" + subversion));
    }

    /**
     * Gets all the info of the version in one string.
     *
     * @return More info
     */
    public String getFullInfo() {
        return releaseStatus + " " + getVersionNumber() + (subversion.equals("") ? "" : ("_" + subversion));
    }

    @Override
    public String toString() {
        return releaseStatus.toString().substring(0, 1) + getVersionNumber() + (subversion.equals("") ? "" : ("_" + subversion));
    }

    /**
     * Checks if this version is newer than a given version (ignores subversion, for now at least).
     *
     * @param version The version to compare this version with
     * @return True if current is the newer one, false otherwise
     */
    public boolean isNewerThan(Version version) {
        if (releaseStatus.level > version.releaseStatus.level) {
            return true;
        } else if (releaseStatus.level == version.releaseStatus.level) {
            int i = 0;
            while (i < Math.min(versionNo.length, version.versionNo.length) && versionNo[i] == version.versionNo[i]) {
                i++;
            }
            return (i < Math.min(versionNo.length, version.versionNo.length) && versionNo[i] > version.versionNo[i])
                    || (i == Math.min(versionNo.length, version.versionNo.length) && versionNo.length > version.versionNo.length);
        } else {
            return false;
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Version) {
            Version version = (Version) obj;
            boolean checkNo = versionNo.length == version.versionNo.length;
            if (checkNo) {
                int i = 0;
                while (i < versionNo.length && versionNo[i] == version.versionNo[i]) {
                    i++;
                }
                checkNo = i == versionNo.length;
            }
            return checkNo && releaseStatus == version.releaseStatus && subversion.equals(version.subversion);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 71 * hash + Objects.hashCode(this.releaseStatus);
        hash = 71 * hash + Arrays.hashCode(this.versionNo);
        hash = 71 * hash + Objects.hashCode(this.subversion);
        return hash;
    }

    private void parseVersionString(String version) throws VersionFormatException {
        // get the release status
        char st = version.charAt(0);
        switch (st) {
            case 'a':
                releaseStatus = ReleaseStatus.ALPHA;
                break;
            case 'b':
                releaseStatus = ReleaseStatus.BETA;
                break;
            case 'r':
                releaseStatus = ReleaseStatus.RELEASE;
                break;
            case 's':
                releaseStatus = ReleaseStatus.SNAPSHOT;
                break;
            default:
                throw new VersionFormatException("Failed to parse version: " + version);
        }
        String remaining = version.substring(1);

        // get the subversion
        int i = remaining.indexOf('_');
        if (i >= 0) {
            subversion = remaining.substring(i + 1);
            remaining = remaining.substring(0, i);
        } else {
            subversion = "";
        }

        // get the (clean) version number, e.g. 1.2.3
        String nos[] = remaining.split("\\.");
        versionNo = new int[nos.length];
        if (versionNo.length == 0) {
            throw new VersionFormatException("Failed to parse version: " + version);
        }

        for (int j = 0; j < nos.length; j++) {
            try {
                versionNo[j] = Integer.parseInt(nos[j]);
            } catch (NumberFormatException nfe) {
                throw new VersionFormatException("Failed to parse version: " + version);
            }
        }
    }

}
