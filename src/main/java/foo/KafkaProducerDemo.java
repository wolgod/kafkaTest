package foo;


import java.util.concurrent.TimeUnit;

public class KafkaProducerDemo implements KafkaProperties{
    public static void main(String[] args){
        int i=0;
        while (true) {
            i++;
            StartThread(1, "testTopic", 10*i);
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    /**
     * @param numsOfProducer  生产者的数目
     * @param topic        消息的主题
     * @param numsOfMessage    每个生产者生产的消息树
     * @return 
     */
    public static void StartThread(int numsOfProducer,String topic,int numsOfMessage){
        for(int i = 1; i <= numsOfProducer; i ++ ){
            String name = "Producer" + i;
            new Producer(name,topic,numsOfMessage).start();    
        }


    }
}