package foo;

public interface KafkaProperties {
	final static String zkConnect = "192.168.131.137:2181";
	final static String groupId = "group1";
	final static String topic = "testTopic";
	final static String kafkaServerURL = "192.168.131.137";
	final static int kafkaServerPort = 9092;
	final static int kafkaProducerBufferSize = 64 * 1024;
	final static int connectionTimeOut = 100000;
	final static int reconnectInterval = 10000;
	final static String clientId = "SimpleConsumerDemoClient";
}