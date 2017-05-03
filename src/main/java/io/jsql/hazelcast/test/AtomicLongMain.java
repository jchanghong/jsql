package io.jsql.hazelcast.test;
import com.hazelcast.core.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by 长宏 on 2017/5/3 0003.
 */
public class AtomicLongMain  {
    static Logger logger = LoggerFactory.getLogger("me");
    public static void main(String[] args) {
        HazelcastInstance hz = Hazelcast.newHazelcastInstance();
        IAtomicLong counter = hz.getAtomicLong("counter");
        counter.addAndGet(3); // value is now 3
        counter.alter(new MultiplyByTwo());//value is now 6
        IdGenerator idGenerator = hz.getIdGenerator("myid");

        logger.info(idGenerator.newId() + "");
        logger.info(idGenerator.newId() + "");
        logger.info(idGenerator.newId() + "");
        logger.info(idGenerator.newId() + "");
        logger.info(idGenerator.newId() + "");
        logger.info(idGenerator.newId() + "");
        logger.info(idGenerator.newId() + "");
        logger.info(idGenerator.newId() + "");
        logger.info(idGenerator.newId() + "");
        System.out.println("counter: "+counter.get());
    }

    public static class MultiplyByTwo implements IFunction<Long,Long> {
        @Override
        public Long apply(Long input) {
            return input*2;
        }
    }

}
