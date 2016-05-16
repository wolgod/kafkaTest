package foo.seria;

import foo.exceptions.FrankKafkaException;
import kafka.serializer.Encoder;
import kafka.utils.VerifiableProperties;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * @author <a href="mailto:heshan664754022@gmail.com">Frank</a>
 * @version V1.0
 * @description
 * @date 2016/5/16 11:46
 */
public class BidDefaultEncoder implements Encoder<Serializable> {

    public BidDefaultEncoder(VerifiableProperties props) {
        super();
    }

    @Override
    public byte[] toBytes(Serializable o) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(baos);
            out.writeObject(o);
            try {
                return baos.toByteArray();
            } finally {
                out.close();
            }
        } catch (Exception e) {
            throw new FrankKafkaException(e.getMessage(), e);
        }
    }
}