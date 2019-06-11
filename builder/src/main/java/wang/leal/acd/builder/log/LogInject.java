package wang.leal.acd.builder.log;

import com.android.build.gradle.AppExtension;
import com.android.utils.FileUtils;

import org.gradle.api.Project;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;

public class LogInject {

    private static ClassPool pool = ClassPool.getDefault();

    public static void injectLog(String path, Project project) {
        AppExtension appExtension = project.getExtensions().getByType(AppExtension.class);
        String packageName = appExtension.getDefaultConfig().getApplicationId();

        LogExtension logExtension = project.getExtensions().getByType(LogExtension.class);
        try {
            Properties properties = new Properties();
            properties.load(new FileInputStream(project.getRootProject().file("local.properties")));
            String sdkDir = properties.getProperty("sdk.dir");
            String androidClassPath = sdkDir+"\\platforms\\"+appExtension.getCompileSdkVersion()+"\\android.jar";
            System.out.println("Android class path:"+androidClassPath);
            pool.appendClassPath(androidClassPath);
            pool.appendClassPath(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
        File dir = new File(path);
        if (dir.isDirectory()) {
            FileUtils.getAllFiles(dir).forEach(file -> {
                String filePath = file.getAbsolutePath();
                if (filePath.endsWith(".class")
                        && !filePath.contains("R$")
                        && !filePath.contains("R.class")
                        && !filePath.contains("BuildConfig.class")) {
                    try {
                        String packagePath = packageName.replace(".","\\");
                        int index = filePath.indexOf(packagePath);
                        System.out.println("file name:"+file.getName()+",path:"+filePath+",packagePath:"+packagePath+",index:"+index);
                        boolean isCurrentPackage = index != -1;
                        if (isCurrentPackage) {
                            int end = filePath.length() - 6;// .class = 6
                            String className = filePath.substring(index, end)
                                    .replace('\\', '.');
                            System.out.println("class name:"+className);
                            CtClass c = pool.getCtClass(className);
                            System.out.println("class package name:"+c.getPackageName());
                            if (c.getPackageName().contains(packageName)){
                                if (c.isFrozen()) {
                                    c.defrost();
                                }
                                pool.importPackage("android.util");
                                CtMethod[] methods = c.getDeclaredMethods();
                                System.out.println("enable:"+logExtension.getMethod().isEnable());
                                if (methods!=null&&methods.length>0&&logExtension.getMethod().isEnable()){
                                    for (CtMethod method:methods){
                                        method.insertBefore("Log.e(\""+file.getName().substring(0,file.getName().length()-6)+"\",\""+method.getLongName()+"\");");
                                    }
                                }
                                c.writeFile(path);
                                c.detach();
                            }
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

}
