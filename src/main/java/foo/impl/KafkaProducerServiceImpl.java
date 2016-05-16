package foo.impl;

import foo.KafkaProducerService;
import foo.enums.MessageSecurityLevel;
import foo.exceptions.FrankKafkaException;
import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @author <a href="mailto:heshan664754022@gmail.com">Frank</a>
 * @version V1.0
 * @description
 * @date 2016/5/16 11:35
 */
public class KafkaProducerServiceImpl implements KafkaProducerService {
    /**
     *
     */
    private Properties configProp = new Properties();

    /**
     * 低安全消息生产者
     * <p/>
     * 此生产者采用异步方式发送消息，消息会在生产者中缓存，当达到配置的刷新条件才将消息发送到broker集群
     * 性能最好，消息丢失率较高，当Producer所在的进程突然死掉或broker leader突然死掉都可能会丢失消息
     */
    private Producer<String, Serializable> producer;

    /**
     * 中等安全消息生产者
     * <p/>
     * 此生产者采用同步的方式发送消息，等待broker leader的消息接收确认就算发送完成, 性能较好，消息丢失率低，只有收到消息的broker
     * leader突然死掉了才会丢失消息
     */
    private Producer<String, Serializable> mediumSecurityProducer;

    /**
     * 高安全消息生产者
     * <p/>
     * 此生产者采用同步的方式发送消息，需等待消息在broker集群中复制完成后的确认才算发送完成 性能一般，可保证消息送达broker集群
     */
    private Producer<String, Serializable> highSecurityProducer;

    @Override
    public void sendMessage(String topic, Serializable msg,
                            MessageSecurityLevel securityLevel) throws FrankKafkaException {
        KeyedMessage<String, Serializable> keyedMessage = new KeyedMessage<String, Serializable>(
                topic, msg);
        switch (securityLevel) {
            case LOW:
                producer.send(keyedMessage);
                break;
            case MEDIUM:
                mediumSecurityProducer.send(keyedMessage);
                break;
            case HIGH:
                highSecurityProducer.send(keyedMessage);
                break;
        }
    }

    @Override
    public void sendOrderedMessage(String topic, Serializable msg, MessageSecurityLevel securityLevel) throws FrankKafkaException {
        KeyedMessage<String, Serializable> keyedMessage = new KeyedMessage<String, Serializable>(
                topic, null, topic, msg);
        switch (securityLevel) {
            case LOW:
                producer.send(keyedMessage);
                break;
            case MEDIUM:
                mediumSecurityProducer.send(keyedMessage);
                break;
            case HIGH:
                highSecurityProducer.send(keyedMessage);
                break;
        }
    }

    @Override
    public void sendMessages(String topic, List<Serializable> msgs,
                             MessageSecurityLevel securityLevel) throws FrankKafkaException {
        List<KeyedMessage<String, Serializable>> messages = new ArrayList<KeyedMessage<String, Serializable>>();
        for (Serializable msg : msgs) {
            KeyedMessage<String, Serializable> keyedMessage = new KeyedMessage<String, Serializable>(
                    topic, msg);
            messages.add(keyedMessage);
        }
        switch (securityLevel) {
            case LOW:
                producer.send(messages);
                break;
            case MEDIUM:
                mediumSecurityProducer.send(messages);
                break;
            case HIGH:
                highSecurityProducer.send(messages);
                break;
        }
    }
    @Override
    public void sendOrderedMessages(String topic, List<Serializable> msgs,
                                    MessageSecurityLevel securityLevel) throws FrankKafkaException {
        List<KeyedMessage<String, Serializable>> messages = new ArrayList<KeyedMessage<String, Serializable>>();
        for (Serializable msg : msgs) {
            KeyedMessage<String, Serializable> keyedMessage = new KeyedMessage<String, Serializable>(
                    topic, null, topic, msg);
            messages.add(keyedMessage);
        }
        switch (securityLevel) {
            case LOW:
                producer.send(messages);
                break;
            case MEDIUM:
                mediumSecurityProducer.send(messages);
                break;
            case HIGH:
                highSecurityProducer.send(messages);
                break;
        }
    }

    public void afterPropertiesSet() throws Exception {

        //定义生产者配置属性对象
        Properties producerProperties = new Properties();

        //装入配置
        if (getConfigProp() != null && getConfigProp().size() > 0) {
            producerProperties.putAll(getConfigProp()); //如果用户明确指定了配置对象，则按用户指定的配置进行构造
        } else {
           // producerProperties.putAll(ConfigUtils.getProperties()); //如果用户未指定配置对象，直接从 ConfigUtils里面拿所有的配置进行构造，可能会有其他配置项干扰
        }

        //
        Properties mediumSecurityProperties = (Properties) producerProperties.clone();

        // 基于用户配置生成中等安全消息生产者配置
        mediumSecurityProperties.setProperty("producer.type", "sync");
        mediumSecurityProperties.setProperty("request.required.acks", "0");

        Properties highSecurityProperties = (Properties) producerProperties.clone();
        // 基于用户配置生成高安全消息生产者配置
        highSecurityProperties.setProperty("producer.type", "sync");
        highSecurityProperties.setProperty("request.required.acks", "-1");

        producer = new Producer<String, Serializable>(new ProducerConfig(
                producerProperties));
        mediumSecurityProducer = new Producer<String, Serializable>(
                new ProducerConfig(mediumSecurityProperties));
        highSecurityProducer = new Producer<String, Serializable>(
                new ProducerConfig(highSecurityProperties));
    }


    public Properties getConfigProp() {
        return configProp;
    }

    public void setConfigProp(Properties configProp) {
        this.configProp = configProp;
    }

    public void destroy() {
        if (producer != null)
            producer.close();
    }
}
