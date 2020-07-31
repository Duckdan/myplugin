package com.codeless.plugin

import com.android.build.gradle.BaseExtension
import org.gradle.api.Plugin
import org.gradle.api.Project

class InjectPluginImpl implements Plugin<Project> {
    @Override
    void apply(Project project) {
        // project.extensions.create('codelessConfig', InjectPluginParams)

        System.out.println("------------- start BuriedPoint tranform -----------------")
        project.extensions.create('codelessConfig', InjectPluginParams)
        registerBuriedPointTransform(project);
        System.out.println("------------- end BuriedPoint tranform -----------------")


    }

    def static registerBuriedPointTransform(Project project) {

        BaseExtension android = project.extensions.getByType(BaseExtension)
        InjectTransform transform = new InjectTransform(project)
        android.registerTransform(transform)
        initDir(project);
        project.afterEvaluate {
            com.codeless.plugin.utils.Log.setQuiet(project.codelessConfig.keepQuiet);
            com.codeless.plugin.utils.Log.setShowHelp(project.codelessConfig.showHelp);
            com.codeless.plugin.utils.Log.logHelp();
            if (project.codelessConfig.watchTimeConsume) {
                com.codeless.plugin.utils.Log.info "watchTimeConsume enabled"
                project.gradle.addListener(new TimeListener())
            } else {
                com.codeless.plugin.utils.Log.info "watchTimeConsume disabled"
            }
        }
    }


    static void initDir(Project project) {
        File pluginTmpDir = new File(project.buildDir, 'LazierTracker')
        if (!pluginTmpDir.exists()) {
            pluginTmpDir.mkdir()
        }
        com.codeless.plugin.utils.DataHelper.ext.pluginTmpDir = pluginTmpDir
    }
}
