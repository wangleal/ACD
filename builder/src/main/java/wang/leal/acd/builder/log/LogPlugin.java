package wang.leal.acd.builder.log;

import com.android.build.api.transform.Transform;
import com.android.build.gradle.AppExtension;
import org.gradle.api.Plugin;
import org.gradle.api.Project;

public abstract class LogPlugin extends Transform implements Plugin<Project>{
    @Override
    public void apply(Project project) {
        Object[] containers = createContainers(project);
        project.getExtensions().create("log",LogExtension.class,containers);
        AppExtension android = project.getExtensions().getByType(AppExtension.class);
        android.registerTransform(this);
    }

    public abstract Object[] createContainers(Project project);

}