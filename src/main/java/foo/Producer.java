package foo;

import java.util.Properties;

import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

public class Producer extends Thread{
    private final kafka.javaapi.producer.Producer<Integer, String> producer;
    private final String topic;
    private final String name;
    private final int numsOfMessage;
    private final Properties props = new Properties();

    public Producer(String name,String topic,int numsOfMessage){
        props.put("zk.connect", "192.168.131.137:2181");
        props.put("serializer.class", "kafka.serializer.StringEncoder");
        props.put("metadata.broker.list", "192.168.131.137:9092");
        //异步发送
        //props.put("producer.type", "async");
        //每次发送多少条
        //props.put("batch.num.messages", "100");
        producer = new kafka.javaapi.producer.Producer<Integer, String>(new ProducerConfig(props));
        this.topic = topic;
        this.name = name;
        this.numsOfMessage = numsOfMessage;
    }
  
      public void run() {
          int messageNo = 1;
          while(messageNo <= numsOfMessage) { //每个生产者生产的消息数；
              String message = new String(name+"'s    Message_" + messageNo+"******");
              KeyedMessage<Integer, String> messageForSend = new KeyedMessage<Integer, String>(topic, message);
              producer.send(messageForSend);
              messageNo++;
          }
          producer.close();
     }
}