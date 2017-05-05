package io.jsql.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintStream;

/**
 * Created by 长宏 on 2017/5/5 0005.
 */
public interface Mlogger {
    PrintStream PRINT_WRITER = System.out;
    default Logger getlogger() {
        return LoggerFactory.getLogger(this.getClass().getName());
    }
}
