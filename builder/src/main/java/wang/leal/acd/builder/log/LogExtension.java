package wang.leal.acd.builder.log;

import org.gradle.api.Action;
import org.gradle.api.Project;

import wang.leal.acd.builder.log.method.Method;
import wang.leal.acd.builder.log.clazz.Class;

public class LogExtension {

    private Project project;
    private Method method;
    private Class clazz;

    public LogExtension(Project project){
        this.project = project;
        method = project.getObjects().newInstance(Method.class);
    }

    public void method(Action<Method> action){
        action.execute(method);
    }

    public void clazz(Class clazz){
        this.clazz = clazz;
    }

    public Method getMethod() {
        return method;
    }

    public Class getClazz() {
        return clazz;
    }
}
