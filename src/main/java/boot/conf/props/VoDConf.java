package boot.conf.props;

import boot.conf.PropLoader;

/**
 * @author valor.
 */
public class VoDConf {

    private final String format;
    private final String regionId;
    private final String host;
    private final String version;
    private final String signMethod;
    private final String signVersion;
    private final String accessKey;
    private final String accessSecret;
    private final String secretToSign;
    private final String cdnHost;
    private final String playKey;
    private final String playSecret;

    public VoDConf() {
        this.format = "";
        this.regionId = "";
        this.host = "";
        this.version = "";
        this.signMethod = "";
        this.signVersion = "";
        this.accessKey = "";
        this.accessSecret = "";
        this.secretToSign = "";
        this.cdnHost = "";
        this.playKey = "";
        this.playSecret = "";
    }

    public static VoDConf instance() {
        return PropLoader.getProps(VoDConf.class);
    }

    public String getFormat() {
        return format;
    }

    public String getRegionId() {
        return regionId;
    }

    public String getHost() {
        return host;
    }

    public String getVersion() {
        return version;
    }

    public String getSignMethod() {
        return signMethod;
    }

    public String getSignVersion() {
        return signVersion;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public String getAccessSecret() {
        return accessSecret;
    }

    public String getSecretToSign() {
        return secretToSign;
    }

    public String getCdnHost() {
        return cdnHost;
    }

    public String getPlayKey() {
        return playKey;
    }

    public String getPlaySecret() {
        return playSecret;
    }
}
