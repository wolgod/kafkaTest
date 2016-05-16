package foo;

import foo.exceptions.FrankKafkaException;
import foo.seria.BidDefaultDecoder;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import kafka.message.MessageAndMetadata;
import kafka.serializer.Decoder;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author <a href="mailto:heshan664754022@gmail.com">Frank</a>
 * @version V1.0
 * @description
 * @date 2016/5/16 11:42
 */
public  abstract class KafkaConsumer<M extends Serializable>{


    private Logger logger = Logger.getLogger(KafkaConsumer.class);


    /**
     * 消费者连接
     */
    private ConsumerConnector consumer;

    /**
     * 消费主题
     */
    protected String topic;

    /**
     * 解码器
     */
    @SuppressWarnings("rawtypes")
    protected Decoder decoder = new BidDefaultDecoder();

    /**BidDefaultDecoder
     * 消费数据处理线程数
     */
    protected int threadNum = 1;

    /**
     *
     */
    private ExecutorService executor = null;

    /**
     *
     */
    private Properties configProp = new Properties();


    public Properties getConfigProp() {
        return configProp;
    }


    @SuppressWarnings("unchecked")
    public void afterPropertiesSet() throws java.lang.Exception {

        Properties popAll = new Properties();

        //对配置进行处理
        if (configProp != null && configProp.size() > 0) {
            popAll.putAll(getConfigProp()); //如果用户直接指定了配置对象，则使用用户自定义的配置对象
        } else {
            //Properties properties = ConfigUtils.getProperties(); //直接从 ConfigUtils里面拿所有的配置进行构造，可能会有其他配置项干扰
            //popAll.putAll(properties);
        }
        ConsumerConfig config = new ConsumerConfig(popAll);

        //根据配置创建消费者连接
        consumer = kafka.consumer.Consumer.createJavaConsumerConnector(config);

        //配置解码器
        if (StringUtils.isNotBlank(popAll.getProperty("deserializer.class"))) {
            Class<?> decoderClazz = Class.forName(popAll.getProperty("deserializer.class"));
            try {
                decoder = (Decoder<?>) decoderClazz.newInstance();
            } catch (Exception e) {
                throw new FrankKafkaException("配置错误，消费者配置文件中deserializer.class配置项配置出错，该类需实现kafka.serializer.Decoder<T>接口!");
            }
        }


        //从消费者连接中获取对应主题分区中的消费流
        Map<String, Integer> topickMap = new HashMap<String, Integer>();
        topickMap.put(topic, threadNum);
        Map<String, List<KafkaStream<byte[], Object>>> streamMap = consumer.createMessageStreams(topickMap, null, decoder);
        List<KafkaStream<byte[], Object>> streams = streamMap.get(topic);

        //消费消息
        executor = Executors.newFixedThreadPool(threadNum);
        for (final KafkaStream<byte[], Object> stream : streams) {
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    ConsumerIterator<byte[], Object> it = stream.iterator();
                    while (true) {
                        if (it.hasNext()) {
                            MessageAndMetadata<byte[], Object> mm = it.next();
                            onMessageArrive((M) mm.message());
                        }
                    }
                }
            });
        }

        logger.debug("KafkaConsumer is running..");
    }

    protected void destory() {
        if (executor != null) {
            executor.shutdownNow();
        }
        logger.debug("KafkaConsumer is destroyed.");
    }

    /**
     * 消息到达回调方法
     *
     * @param message
     */
    protected abstract void onMessageArrive(M message);


    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public Decoder<?> getDecoder() {
        return decoder;
    }

    public void setDecoder(Decoder<?> decoder) {
        this.decoder = decoder;
    }

    public int getThreadNum() {
        return threadNum;
    }

    public void setThreadNum(int threadNum) {
        this.threadNum = threadNum;
    }

    protected ConsumerConnector getConsumer() {
        return consumer;
    }


}