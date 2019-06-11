package wang.leal.acd.builder.log.method;

import com.android.build.api.transform.Format;
import com.android.build.api.transform.QualifiedContent;
import com.android.build.api.transform.TransformInput;
import com.android.build.api.transform.TransformInvocation;
import com.android.build.api.transform.TransformOutputProvider;
import com.android.build.gradle.internal.pipeline.TransformManager;
import com.android.utils.FileUtils;

import org.apache.commons.codec.digest.DigestUtils;
import org.gradle.api.Project;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Set;

import wang.leal.acd.builder.log.LogInject;
import wang.leal.acd.builder.log.LogPlugin;

class MethodLogPlugin extends LogPlugin {
    private Project project;

    @Override
    public void apply(Project project) {
        super.apply(project);
        this.project = project;
    }

    @Override
    public String getName() {
        return "MethodLog";
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

    @Override
    public void transform(TransformInvocation transformInvocation) {
        System.out.println("transform.......................................");
        Collection<TransformInput> inputs = transformInvocation.getInputs();
        TransformOutputProvider outputProvider = transformInvocation.getOutputProvider();
        inputs.forEach(
                transformInput -> {
                    transformInput.getDirectoryInputs().forEach(directoryInput -> {
                        LogInject.injectLog(directoryInput.getFile().getAbsolutePath(), project);
                        File dest = outputProvider.getContentLocation(directoryInput.getName(),
                                directoryInput.getContentTypes(), directoryInput.getScopes(),
                                Format.DIRECTORY);
                        try {
                            FileUtils.copyDirectory(directoryInput.getFile(), dest);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                    //对类型为jar文件的input进行遍历
                    transformInput.getJarInputs().forEach(jarInput -> {
                        String jarName = jarInput.getName();
                        String md5Name = DigestUtils.md5Hex(jarInput.getFile().getAbsolutePath());
                        if (jarName.endsWith(".jar")) {
                            jarName = jarName.substring(0, jarName.length() - 4);
                        }
                        //生成输出路径
                        File dest = outputProvider.getContentLocation(jarName + md5Name,
                                jarInput.getContentTypes(), jarInput.getScopes(), Format.JAR);
                        try {
                            FileUtils.copyFile(jarInput.getFile(), dest);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                }
        );
    }
}