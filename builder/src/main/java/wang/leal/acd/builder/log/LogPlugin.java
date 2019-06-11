package wang.leal.acd.builder.log;

import com.android.build.api.transform.Transform;
import com.android.build.gradle.AppExtension;
import org.gradle.api.Plugin;
import org.gradle.api.Project;

public abstract class LogPlugin extends Transform implements Plugin<Project>{
    @Override
    public void apply(Project project) {
        System.out.println("apply.......................................");
        project.getExtensions().create("log",LogExtension.class,project);
        AppExtension android = project.getExtensions().getByType(AppExtension.class);
        android.registerTransform(this);
        project.getBuildDir().deleteOnExit();
        project.getRootProject().getBuildDir().deleteOnExit();
    }

}