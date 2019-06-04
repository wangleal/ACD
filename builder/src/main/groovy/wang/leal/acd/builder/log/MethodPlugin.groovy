package wang.leal.acd.builder.log

import org.gradle.api.Plugin
import org.gradle.api.Project

class MethodPlugin implements Plugin<Project>{
    @Override
    void apply(Project project) {
        println("------------------------------------------------------------method----------------------------------------------------------")
    }
}