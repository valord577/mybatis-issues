package boot.conf.props;

import boot.conf.PropLoader;

/**
 * @author valor.
 */
public class OssConf {

    /**
     * 地域节点
     */
    private final String endpoint;

    /**
     * 子账户accessKey
     */
    private final String key;

    /**
     * 子账户accessSecret
     */
    private final String secret;

    /**
     * 存储空间
     */
    private final String bucket;

    /**
     * bucket host
     */
    private final String bucketPoint;

    /**
     * bucket 网址
     */
    private final String bucketHost;

    /**
     * cdn 网址
     */
    private final String cdnHost;

    public OssConf() {
        this.endpoint = "";
        this.key = "";
        this.secret = "";
        this.bucket = "";
        this.bucketPoint = "";
        this.bucketHost = "";
        this.cdnHost = "";
    }

    public static OssConf instance() {
        return PropLoader.getProps(OssConf.class);
    }

    public String getEndpoint() {
        return endpoint;
    }

    public String getKey() {
        return key;
    }

    public String getSecret() {
        return secret;
    }

    public String getBucket() {
        return bucket;
    }

    public String getBucketPoint() {
        return bucketPoint;
    }

    public String getBucketHost() {
        return bucketHost;
    }

    public String getCdnHost() {
        return cdnHost;
    }
}
