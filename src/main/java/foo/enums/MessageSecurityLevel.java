package foo.enums;

/**
 * @author <a href="mailto:heshan664754022@gmail.com">Frank</a>
 * @version V1.0
 * @description
 * @date 2016/5/16 11:32
 */
public enum MessageSecurityLevel {

    /**
     * 低安全级别
     * <p/>
     * 采用【异步】方式发送消息，消息会在生产者中缓存，当达到配置的刷新条件才将消息发送到broker集群
     * 性能最好，消息丢失率较高，当Producer所在的进程突然死掉或broker leader突然死掉都可能会丢失消息
     * 测试发送低安全级别消息，共发送1000条，耗时28毫秒，TPS: 35714.285156
     */
    LOW(0),

    /**
     * 中安全级别
     * <p/>
     * 采用【同步】的方式发送消息，等待broker leader的消息接收确认就算发送完成,
     * 性能较好，消息丢失率低，只有收到消息的broker leader突然死掉了才会丢失消息
     * 测试发送中安全级别消息，共发送1000条，耗时550毫秒，TPS: 1818.181763
     */
    MEDIUM(1),

    /**
     * 高安全级别
     * <p/>
     * 此生产者采用【同步】的方式发送消息，需等待消息在broker集群中复制完成后的确认才算发送完成
     * 性能一般，可保证消息送达broker集群
     * 测试发送高安全级别消息，共发送1000条，耗时2108毫秒，TPS: 474.383301
     */
    HIGH(-1);

    private Integer level;

    MessageSecurityLevel(Integer level) {
        this.level = level;
    }

    public Integer getLevel() {
        return level;
    }

    public static MessageSecurityLevel parse(Integer level) {
        MessageSecurityLevel[] values = MessageSecurityLevel.values();
        for (MessageSecurityLevel item : values) {
            if (item.getLevel().equals(level)) {
                return item;
            }
        }
        return null;
    }
}