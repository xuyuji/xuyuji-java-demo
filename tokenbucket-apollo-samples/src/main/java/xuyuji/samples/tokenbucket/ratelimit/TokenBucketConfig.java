package xuyuji.samples.tokenbucket.ratelimit;

public class TokenBucketConfig {

    private Long bucketCapacity;
    private Long recoverySpeed;
    private Long bucketInterval;

    public TokenBucketConfig() {

    }

    public TokenBucketConfig(Long bucketCapacity, Long recoverySpeed, Long bucketInterval) {
        this.bucketCapacity = bucketCapacity;
        this.recoverySpeed = recoverySpeed;
        this.bucketInterval = bucketInterval;
    }

    public Long getBucketCapacity() {
        return bucketCapacity;
    }

    public void setBucketCapacity(Long bucketCapacity) {
        this.bucketCapacity = bucketCapacity;
    }

    public Long getRecoverySpeed() {
        return recoverySpeed;
    }

    public void setRecoverySpeed(Long recoverySpeed) {
        this.recoverySpeed = recoverySpeed;
    }

    public Long getBucketInterval() {
        return bucketInterval;
    }

    public void setBucketInterval(Long bucketInterval) {
        this.bucketInterval = bucketInterval;
    }

    public boolean isNull() {
        return bucketCapacity == null && recoverySpeed == null && bucketInterval == null;
    }

    public static TokenBucketConfig build(TokenBucketConfig config, TokenBucketConfig defaultConfig) {
        TokenBucketConfig newConfig = new TokenBucketConfig();

        newConfig.setBucketCapacity(config.getBucketCapacity() != null ? config.getBucketCapacity()
                : defaultConfig.getBucketCapacity());

        newConfig.setRecoverySpeed(config.getRecoverySpeed() != null ? config.getRecoverySpeed()
                : defaultConfig.getRecoverySpeed());

        newConfig.setBucketInterval(config.getBucketInterval() != null ? config.getBucketInterval()
                : defaultConfig.getBucketInterval());

        return newConfig;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("TokenBucketConfig [bucketCapacity=");
        builder.append(bucketCapacity);
        builder.append(", recoverySpeed=");
        builder.append(recoverySpeed);
        builder.append(", bucketInterval=");
        builder.append(bucketInterval);
        builder.append("]");
        return builder.toString();
    }
}
