package util.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.LogUtil;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LogUtilImpl implements LogUtil,InvocationHandler {

    private final static Logger logger = LoggerFactory.getLogger(ObjUtil.class);

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public void loggerError(String str) {
        logger.error(String.format("error message : %s", str));
    }

    @Override
    public void loggerInfo(String str) {
        logger.info(String.format("message info : %s", str));
    }

    @Override
    public void loggerWarn(String str) {
        logger.warn(String.format("warn message : %s", str));
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        logger.warn("--------------------------");
        logger.warn(String.format("current time : %s",sdf.format(new Date())));
        method.invoke(proxy,args);
        logger.warn("--------------------------");
        return null;
    }
}
