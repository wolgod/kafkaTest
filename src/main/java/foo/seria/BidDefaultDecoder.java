package foo.seria;

import foo.exceptions.FrankKafkaException;
import kafka.serializer.Decoder;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;

/**
 * @author <a href="mailto:heshan664754022@gmail.com">Frank</a>
 * @version V1.0
 * @description
 * @date 2016/5/16 11:45
 */
public class BidDefaultDecoder implements Decoder<Object> {

    @Override
    public Object fromBytes(byte[] bytes) {
        try {
            ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
            ObjectInputStream in = new ObjectInputStream(bais);
            try {
                return in.readObject();
            } finally {
                in.close();
            }
        } catch (Exception e) {
            throw new FrankKafkaException(e.getMessage(), e);
        }
    }
}