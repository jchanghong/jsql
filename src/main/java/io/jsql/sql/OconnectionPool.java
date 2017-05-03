package io.jsql.sql;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by 长宏 on 2017/5/2 0002.
 */
@Component
public class OconnectionPool {
    @Value("${connection.number.max}")
    private long max;
    private Set<OConnection> oConnections = new HashSet<>();

    public boolean checkMax() {
        if (max == 0) {
            return true;
        }
        else {
            return oConnections.size() <= max;
        }
    }

    public void add(OConnection connection) {
        oConnections.add(connection);
    }

    public void remove(OConnection connection) {
        oConnections.remove(connection);
    }
}
