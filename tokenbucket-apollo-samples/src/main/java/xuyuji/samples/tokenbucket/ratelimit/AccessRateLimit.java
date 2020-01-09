package xuyuji.samples.tokenbucket.ratelimit;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.ConfigChangeListener;
import com.ctrip.framework.apollo.model.ConfigChange;
import com.ctrip.framework.apollo.model.ConfigChangeEvent;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfig;

@Service
public class AccessRateLimit implements InitializingBean {

    private static Logger LOG = LoggerFactory.getLogger(AccessRateLimit.class);

    private static final String SEPARATOR = ".";
    private static final String LIMIT_PREFIX = "ratelimit.ns";
    private static final String DEFAULT_NAMESPACE = "default";
    private static final String DEFAULT_LIMIT_PREFIX = LIMIT_PREFIX + SEPARATOR + DEFAULT_NAMESPACE;

    private static final String BUCKET_CAPACITY = "bucket.capacity";
    private static final String BUCKET_RECOVERYSPEED = "bucket.recoveryspeed";
    private static final String BUCKET_INTERVAL = "bucket.interval";

    private static final String DEFAULT_BUCKET_CAPACITY_KEY =
            DEFAULT_LIMIT_PREFIX + SEPARATOR + BUCKET_CAPACITY;
    private static final String DEFAULT_BUCKET_RECOVERYSPEED_KEY =
            DEFAULT_LIMIT_PREFIX + SEPARATOR + BUCKET_RECOVERYSPEED;
    private static final String DEFAULT_BUCKET_INTERVAL_KEY =
            DEFAULT_LIMIT_PREFIX + SEPARATOR + BUCKET_INTERVAL;

    @ApolloConfig(value = "NS-RATELIMIT")
    private Config config;

    @Value("${ratelimit.switch:Y}")
    private String rateLimitSwitch;
    
    private ConcurrentHashMap<String, TokenBucket> tokenBucketCache;

    private Map<String, TokenBucketConfig> configMapping;

    @Override
    public void afterPropertiesSet() throws Exception {
        tokenBucketCache = new ConcurrentHashMap<>();
        configMapping = new HashMap<>();
        init();

        config.addChangeListener(new ConfigChangeListener() {

            @Override
            public void onChange(ConfigChangeEvent changeEvent) {
                Set<String> changeNamespaceSet = new HashSet<>();
                for (String changeKey : changeEvent.changedKeys()) {
                    ConfigChange change = changeEvent.getChange(changeKey);
                    LOG.info("apollo参数变更 - key: {}, oldValue: {}, newValue: {}, changeType: {}",
                            change.getPropertyName(), change.getOldValue(), change.getNewValue(),
                            change.getChangeType());

                    if (changeKey.startsWith(LIMIT_PREFIX)) {
                        String namespace = updateTokenBucketConfig(changeKey);
                        changeNamespaceSet.add(namespace);
                    }
                }
                updateTokenBucket(changeNamespaceSet);
            }
        });
    }

    private void init() {
        Long bucketCapacity = config.getLongProperty(DEFAULT_BUCKET_CAPACITY_KEY, 5L);
        Long recoverySpeed = config.getLongProperty(DEFAULT_BUCKET_RECOVERYSPEED_KEY, 17280000L);
        Long bucketInterval = config.getLongProperty(DEFAULT_BUCKET_INTERVAL_KEY, 3600000L);
        configMapping.put(DEFAULT_NAMESPACE,
                new TokenBucketConfig(bucketCapacity, recoverySpeed, bucketInterval));
        LOG.info("限流初始化默认参数:bucketCapacity:{},recoverySpeed:{},bucketInterval:{}", bucketCapacity,
                recoverySpeed, bucketInterval);

        Set<String> changeNamespaceSet = new HashSet<>();
        changeNamespaceSet.add(DEFAULT_NAMESPACE);
        for (String propertyName : config.getPropertyNames()) {
            if (propertyName.startsWith(LIMIT_PREFIX)
                    && !propertyName.startsWith(DEFAULT_LIMIT_PREFIX)) {
                String namespace = updateTokenBucketConfig(propertyName);
                changeNamespaceSet.add(namespace);
            }
        }
        updateTokenBucket(changeNamespaceSet);
    }

    private String updateTokenBucketConfig(String apolloPropertyName) {
        String str = apolloPropertyName.substring(LIMIT_PREFIX.length() + 1);
        String namespace = str.substring(0, str.indexOf(SEPARATOR));
        String key = str.substring(str.indexOf(SEPARATOR) + 1);
        Long value = config.getLongProperty(apolloPropertyName, null);

        TokenBucketConfig tokenBucketConfig = configMapping.get(namespace);
        if (tokenBucketConfig == null) {
            tokenBucketConfig = new TokenBucketConfig();
            configMapping.put(namespace, tokenBucketConfig);
        }

        switch (key) {
            case BUCKET_CAPACITY:
                tokenBucketConfig.setBucketCapacity(value);
                break;
            case BUCKET_RECOVERYSPEED:
                tokenBucketConfig.setRecoverySpeed(value);
                break;
            case BUCKET_INTERVAL:
                tokenBucketConfig.setBucketInterval(value);
                break;
            default:
                LOG.warn("无效限流配置项:{}", str);
                break;
        }

        return namespace;
    }

    private void updateTokenBucket(Set<String> changeNamespaceSet) {
        if (CollectionUtils.isEmpty(changeNamespaceSet)) {
            LOG.debug("无命名空间变更，不更新令牌桶。");
            return;
        }

        for (String namespace : changeNamespaceSet) {
            TokenBucketConfig tokenBucketConfig = configMapping.get(namespace);
            if (tokenBucketConfig.isNull()) {
                configMapping.remove(namespace);
                tokenBucketCache.remove(namespace);
                if(LOG.isDebugEnabled()) {
                    LOG.debug("删除令牌桶[{}]", namespace);
                }
            } else {
                TokenBucket tokenBucket = tokenBucketCache.get(namespace);
                if (tokenBucket == null) {
                    TokenBucketConfig newConfig = TokenBucketConfig.build(tokenBucketConfig,
                            configMapping.get(DEFAULT_NAMESPACE));
                    tokenBucket = new TokenBucket(namespace, newConfig);
                    tokenBucketCache.put(namespace, tokenBucket);
                    if(LOG.isDebugEnabled()) {
                        LOG.debug("新增令牌桶[{}]:{}", namespace, tokenBucket.toString());
                    }
                } else {
                    tokenBucket.updateConfig(tokenBucketConfig);
                    if(LOG.isDebugEnabled()) {
                        LOG.debug("更新令牌桶[{}]:{}", namespace, tokenBucket.toString());
                    }
                }
            }
        }
    }

    public boolean visit() {
        return visit(DEFAULT_NAMESPACE);
    }

    public boolean visit(String namespace) {
        if(!"Y".equals(rateLimitSwitch)) {
            return true;
        }
        
        TokenBucket tokenBucket = tokenBucketCache.get(namespace);
        if (tokenBucket == null) {
            tokenBucket = tokenBucketCache.get(DEFAULT_NAMESPACE);
        }
        if (tokenBucket == null) {
            return false;
        }
        return tokenBucket.visit();
    }
}
