/*
 * Java-based distributed database like Mysql
 */

package io.jsql.springaop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * \* Created with IntelliJ IDEA.
 * \* User: jiang
 * \* Date: 2017/8/25 0025
 * \* Time: 9:04
 * \
 */
@Aspect
@Component
public class LogAop {
    private static Logger logger = LoggerFactory.getLogger(LogAop.class);
    private final boolean debug = true;

    @Pointcut("within(io.jsql..*)")
    public void point1() {
    }

    //    @Before("point1()")
    public void log(JoinPoint joinPoint) {
        if (debug) {
            logger.info(joinPoint.getTarget().getClass().toGenericString() + joinPoint.toShortString());
        }
    }
}
