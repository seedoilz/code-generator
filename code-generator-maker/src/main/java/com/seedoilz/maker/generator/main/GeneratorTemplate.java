package com.seedoilz.maker.generator.main;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.resource.ClassPathResource;
import cn.hutool.core.util.StrUtil;
import com.seedoilz.maker.generator.JarGenerator;
import com.seedoilz.maker.generator.ScriptGenerator;
import com.seedoilz.maker.generator.file.DynamicFileGenerator;
import com.seedoilz.maker.meta.Meta;
import com.seedoilz.maker.meta.MetaManager;
import freemarker.template.TemplateException;

import java.io.File;
import java.io.IOException;

public abstract class GeneratorTemplate {

    /**
     * 创建方法
     * @throws IOException
     * @throws TemplateException
     * @throws InterruptedException
     */
    public void doGenerate() throws IOException, TemplateException, InterruptedException {
        Meta meta = MetaManager.getMetaObject();

        // 0.输出的根路径
        String projectPath = System.getProperty("user.dir");
        String outputPath = projectPath + File.separator + "generated" + File.separator + meta.getName();
        if (!FileUtil.exist(outputPath)) {
            FileUtil.mkdir(outputPath);
        }

        // 1.复制原始文件
        String sourceCopyDestPath = copySource(meta, outputPath);

        // 2.代码生成
        generateCode(meta, outputPath);

        // 3.构建 jar 包
        String jarPath = buildJar(meta, outputPath);

        // 4.封装脚本
        String shellOutputFilePath = buildScript(outputPath, jarPath);

        // 5.生成精简版的程序（产物包）
        buildDist(outputPath, jarPath, shellOutputFilePath, sourceCopyDestPath);
    }

    /**
     * 复制模版文件
     * @param meta
     * @param outputPath
     * @return
     */
    protected String copySource(Meta meta, String outputPath) {
        String sourceRootPath = meta.getFileConfig().getSourceRootPath();
        // 将模板文件复制到项目目录下
        String sourceCopyDestPath = outputPath + File.separator + "./source";
        FileUtil.copy(sourceRootPath, sourceCopyDestPath, false);
        return sourceCopyDestPath;
    }

    /**
     * 生成代码文件
     * @param meta
     * @param outputPath
     * @throws IOException
     * @throws TemplateException
     * @throws InterruptedException
     */
    protected void generateCode(Meta meta, String outputPath) throws IOException, TemplateException, InterruptedException {
        ClassPathResource classPathResource = new ClassPathResource("");
        String inputResourcePath = classPathResource.getAbsolutePath();

        // Java包的基础路径
        // com.seedoilz
        String outputBasePackage = meta.getBasePackage();
        // com/seedoilz
        String outputBasePackagePath = StrUtil.join("/", StrUtil.split(outputBasePackage, "."));
        // generated/acm-template-pro-generator/src/main/java/com/seedoilz
        String outputBaseJavaPackagePath = outputPath + File.separator + "src/main/java" + File.separator + outputBasePackagePath;

        String inputFilePath;
        String outputFilePath;

        // model.DataModel
        inputFilePath = inputResourcePath + File.separator + "templates/java/model/DataModel.java.ftl";
        outputFilePath = outputBaseJavaPackagePath + File.separator + "/model/DataModel.java";
        DynamicFileGenerator.doGenerate(inputFilePath, outputFilePath, meta);

        inputFilePath = inputResourcePath + File.separator + "templates/java/cli/command/GenerateCommand.java.ftl";
        outputFilePath = outputBaseJavaPackagePath + File.separator + "/cli/command/GenerateCommand.java";
        DynamicFileGenerator.doGenerate(inputFilePath, outputFilePath, meta);

        // cli.command.ConfigCommand
        inputFilePath = inputResourcePath + File.separator + "templates/java/cli/command/ConfigCommand.java.ftl";
        outputFilePath = outputBaseJavaPackagePath + "/cli/command/ConfigCommand.java";
        DynamicFileGenerator.doGenerate(inputFilePath, outputFilePath, meta);

        // cli.command.GenerateCommand
        inputFilePath = inputResourcePath + File.separator + "templates/java/cli/command/GenerateCommand.java.ftl";
        outputFilePath = outputBaseJavaPackagePath + "/cli/command/GenerateCommand.java";
        DynamicFileGenerator.doGenerate(inputFilePath, outputFilePath, meta);

        // cli.command.ListCommand
        inputFilePath = inputResourcePath + File.separator + "templates/java/cli/command/ListCommand.java.ftl";
        outputFilePath = outputBaseJavaPackagePath + "/cli/command/ListCommand.java";
        DynamicFileGenerator.doGenerate(inputFilePath, outputFilePath, meta);

        // cli.CommandExecutor
        inputFilePath = inputResourcePath + File.separator + "templates/java/cli/CommandExecutor.java.ftl";
        outputFilePath = outputBaseJavaPackagePath + "/cli/CommandExecutor.java";
        DynamicFileGenerator.doGenerate(inputFilePath, outputFilePath, meta);

        // Main
        inputFilePath = inputResourcePath + File.separator + "templates/java/Main.java.ftl";
        outputFilePath = outputBaseJavaPackagePath + "/Main.java";
        DynamicFileGenerator.doGenerate(inputFilePath, outputFilePath, meta);

        // generator.StaticFileGenerator
        inputFilePath = inputResourcePath + File.separator + "templates/java/generator/StaticFileGenerator.java.ftl";
        outputFilePath = outputBaseJavaPackagePath + "/generator/StaticFileGenerator.java";
        DynamicFileGenerator.doGenerate(inputFilePath, outputFilePath, meta);

        // generator.DynamicFileGenerator
        inputFilePath = inputResourcePath + File.separator + "templates/java/generator/DynamicFileGenerator.java.ftl";
        outputFilePath = outputBaseJavaPackagePath + "/generator/DynamicFileGenerator.java";
        DynamicFileGenerator.doGenerate(inputFilePath, outputFilePath, meta);

        // generator.MainGenerator
        inputFilePath = inputResourcePath + File.separator + "templates/java/generator/FileGenerator.java.ftl";
        outputFilePath = outputBaseJavaPackagePath + "/generator/FileGenerator.java";
        DynamicFileGenerator.doGenerate(inputFilePath, outputFilePath, meta);

        // pom.xml
        inputFilePath = inputResourcePath + File.separator + "templates/pom.xml.ftl";
        outputFilePath = outputPath + File.separator + "pom.xml";
        DynamicFileGenerator.doGenerate(inputFilePath, outputFilePath, meta);

        // README.md
        inputFilePath = inputResourcePath + File.separator + "templates/README.md.ftl";
        outputFilePath = outputPath + File.separator + "README.md";
        DynamicFileGenerator.doGenerate(inputFilePath, outputFilePath, meta);

        // git初始化
        if (meta.isGit()) {
            gitInit(outputPath);
            inputFilePath = inputResourcePath + File.separator + "templates/.gitignore";
            outputFilePath = outputPath + File.separator + ".gitignore";
            FileUtil.copy(inputFilePath, outputFilePath, false);
        }
    }

    /**
     * 创建脚本
     * @param outputPath
     * @param jarPath
     * @return
     */
    protected String buildScript(String outputPath, String jarPath) {
        String shellOutputFilePath = outputPath + File.separator + "generator";
        ScriptGenerator.doGenerate(shellOutputFilePath, jarPath);
        return shellOutputFilePath;
    }

    /**
     * 打包Jar包
     * @param meta
     * @param outputPath
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    protected String buildJar(Meta meta, String outputPath) throws IOException, InterruptedException {
        String jarName = String.format("%s-%s-jar-with-dependencies.jar", meta.getName(), meta.getVersion());
        String jarPath = "target/" + jarName;
        JarGenerator.doGenerate(outputPath);
        return jarPath;
    }

    /**
     * 生成精简版的程序
     * @param outputPath
     * @param jarPath
     * @param shellOutputFilePath
     * @param sourceCopyDestPath
     */
    protected void buildDist(String outputPath, String jarPath, String shellOutputFilePath, String sourceCopyDestPath) {
        String distOutputPath = outputPath + "-dist";
        //  - 拷贝jar包
        String targetAbsolutePath = distOutputPath + File.separator + "target";
        FileUtil.mkdir(targetAbsolutePath);
        String jarAbsolutePath = outputPath + File.separator + jarPath;
        FileUtil.copy(jarAbsolutePath, targetAbsolutePath, true);
        //  - 拷贝脚本文件
        FileUtil.copy(shellOutputFilePath, distOutputPath, true);
        //  - 拷贝模板文件
        FileUtil.copy(sourceCopyDestPath, distOutputPath, true);
    }


    /**
     * git仓库初始化
     * @param projectDir
     * @throws IOException
     * @throws InterruptedException
     */
    private void gitInit(String projectDir) throws IOException, InterruptedException {
        String gitInitCommand = "git init " + projectDir;

        ProcessBuilder processBuilder = new ProcessBuilder(gitInitCommand.split(" "));
        processBuilder.directory(new File(projectDir));

        Process process = processBuilder.start();

        int exitCode = process.waitFor();
        System.out.println("命令执行结束" + exitCode);
    }
}
