package xuyuji.samples.tokenbucket.ratelimit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TokenBucket {

    private static Logger LOG = LoggerFactory.getLogger(TokenBucket.class);

    private String name;
    private volatile long lastTime;
    private volatile long tokenNum;
    private volatile long bucketCapacity;
    private volatile long recoverySpeed;
    private volatile long bucketInterval;

    public TokenBucket(String name, TokenBucketConfig tokenBucketConfig) {
        this.name = name;
        this.bucketCapacity = tokenBucketConfig.getBucketCapacity();
        this.recoverySpeed = tokenBucketConfig.getRecoverySpeed();
        this.bucketInterval = tokenBucketConfig.getBucketInterval();
        this.lastTime = System.currentTimeMillis() - this.bucketInterval;
        this.tokenNum = this.bucketCapacity;
    }

    public synchronized boolean visit() {
        long now = System.currentTimeMillis();
        if ((now - lastTime) <= bucketInterval) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("令牌桶[{}]访问间隔小于{}ms，不允许访问。", name, bucketInterval);
            }
            return false;
        }

        long nowTokenNum = tokenNum + (now - lastTime) / recoverySpeed;
        if (nowTokenNum > bucketCapacity) {
            nowTokenNum = bucketCapacity;
        }
        if (nowTokenNum > 0) {
            lastTime = now;
            tokenNum = nowTokenNum - 1;
            if (LOG.isDebugEnabled()) {
                LOG.debug("令牌桶[{}]消耗一次访问令牌，允许访问。剩余令牌数:{}", name, tokenNum);
            }
            return true;
        } else {
            if (LOG.isDebugEnabled()) {
                LOG.debug("令牌桶[{}]无访问令牌，不允许访问。", name, bucketInterval);
            }
            return false;
        }
    }

    public synchronized void updateConfig(TokenBucketConfig tokenBucketConfig) {
        if (tokenBucketConfig.getBucketCapacity() != null) {
            this.bucketCapacity = tokenBucketConfig.getBucketCapacity();
        }

        if (tokenBucketConfig.getRecoverySpeed() != null) {
            this.recoverySpeed = tokenBucketConfig.getRecoverySpeed();
        }

        if (tokenBucketConfig.getBucketInterval() != null) {
            this.bucketInterval = tokenBucketConfig.getBucketInterval();
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("TokenBucket [name=");
        builder.append(name);
        builder.append(", lastTime=");
        builder.append(lastTime);
        builder.append(", tokenNum=");
        builder.append(tokenNum);
        builder.append(", bucketCapacity=");
        builder.append(bucketCapacity);
        builder.append(", recoverySpeed=");
        builder.append(recoverySpeed);
        builder.append(", bucketInterval=");
        builder.append(bucketInterval);
        builder.append("]");
        return builder.toString();
    }
}
