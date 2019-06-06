package wang.leal.acd.builder.log;

import wang.leal.acd.builder.log.method.Method;
import wang.leal.acd.builder.log.clazz.Class;

public class LogExtension {

    private Method method;
    private Class clazz;
    public LogExtension(Method method){
        this.method = method;
    }

    public LogExtension(Class clazz){
        this.clazz = clazz;
    }

}
