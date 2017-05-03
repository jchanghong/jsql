package io.jsql.sql.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 长宏 on 2017/5/3 0003.
 */
@Component
@Order(1)
public class AllHanders {
    @Autowired
    private List<SqlStatementHander> handerList;
    public Map<Class, SqlStatementHander> handerMap = new HashMap<>();
    Logger logger = LoggerFactory.getLogger(AllHanders.class.getName());
    @PostConstruct
    public void init() {
        handerList.forEach(a -> handerMap.put(a.supportSQLstatement(), a));
    }
}
