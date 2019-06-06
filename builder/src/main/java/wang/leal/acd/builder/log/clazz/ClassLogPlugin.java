package wang.leal.acd.builder.log.clazz;

import com.android.build.api.transform.QualifiedContent;
import com.android.build.gradle.internal.pipeline.TransformManager;
import org.gradle.api.Project;

import java.util.Set;

import wang.leal.acd.builder.log.LogPlugin;

class ClassLogPlugin extends LogPlugin{
    @Override
    public Object[] createContainers(Project project) {
        return new Object[0];
    }

    @Override
    public String getName() {
        return "TransformClassesWithPreDexForClassLog";
    }

    @Override
    public Set<QualifiedContent.ContentType> getInputTypes() {
        return TransformManager.CONTENT_CLASS;
    }

    @Override
    public Set<? super QualifiedContent.Scope> getScopes() {
        return TransformManager.SCOPE_FULL_PROJECT;
    }

    @Override
    public boolean isIncremental() {
        return false;
    }
}