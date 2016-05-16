package foo;

import foo.enums.MessageSecurityLevel;
import foo.exceptions.FrankKafkaException;

import java.io.Serializable;
import java.util.List;

/**
 * @author <a href="mailto:heshan664754022@gmail.com">Frank</a>
 * @version V1.0
 * @description
 * @date 2016/5/16 11:31
 */
 public interface KafkaProducerService {

        /**
         * 发送单条消息
         *
         * @param topic         消息主题
         * @param msg           待发送的一条消息，消息需实现可序列化接口
         * @param securityLevel 安全级别
         * @see foo.enums.MessageSecurityLevel
         */
        public void sendMessage(String topic, Serializable msg, MessageSecurityLevel securityLevel) throws FrankKafkaException;

        /**
         * 发送有序的单条消息
         *
         * @param topic         消息主题
         * @param msg           待发送的一条消息，消息需实现可序列化接口
         * @param securityLevel 安全级别
         * @see foo.enums.MessageSecurityLevel
         */
        public void sendOrderedMessage(String topic, Serializable msg, MessageSecurityLevel securityLevel) throws FrankKafkaException;

        /**
         * 批量发送消息，批量发送方式性能要好于单条发送方式
         *
         * @param topic         消息主题
         * @param msgs          待发送的一批消息，每条消息需实现可序列化接口
         * @param securityLevel 安全级别
         * @see foo.enums.MessageSecurityLevel
         */
        public void sendMessages(String topic, List<Serializable> msgs, MessageSecurityLevel securityLevel) throws FrankKafkaException;

        /**
         * 批量发送有序的消息，批量发送方式性能要好于单条发送方式
         *
         * @param topic         the topic
         * @param msgs          the msgs
         * @param securityLevel the security level
         * @throws FrankKafkaException the bid kafka exception
         * @author : <a href="mailto:heshan664754022@gmail.com">Frank</a>
         */
        public void sendOrderedMessages(String topic, List<Serializable> msgs, MessageSecurityLevel securityLevel) throws FrankKafkaException;


    }

