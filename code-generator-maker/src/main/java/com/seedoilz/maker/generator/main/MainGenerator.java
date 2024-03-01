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

import java.io.*;

public class MainGenerator {
    public static void main(String[] args) throws TemplateException, IOException, InterruptedException {
        Meta meta = MetaManager.getMetaObject();
        System.out.println(meta);

        String projectPath = System.getProperty("user.dir");
        String outputPath = projectPath + File.separator + "generated" + File.separator + meta.getName();
        if (!FileUtil.exist(outputPath)) {
            FileUtil.mkdir(outputPath);
        }

        String sourceRootPath = meta.getFileConfig().getSourceRootPath();
        // 将模板文件复制到项目目录下
        String sourceCopyDestPath = outputPath + File.separator + "./source";
        FileUtil.copy(sourceRootPath, sourceCopyDestPath, false);

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

        // 生成Jar包
        JarGenerator.doGenerate(outputPath);

        // 封装脚本
        String shellOutputFilePath = outputPath + File.separator + "generator";
        String jarName = String.format("%s-%s-jar-with-dependencies.jar", meta.getName(), meta.getVersion());
        String jarPath = "target/" + jarName;
        ScriptGenerator.doGenerate(shellOutputFilePath, jarPath);

        // 生成精简版的程序（产物包）
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

    private static void gitInit(String projectDir) throws IOException, InterruptedException {
        String gitInitCommand = "git init " + projectDir;

        ProcessBuilder processBuilder = new ProcessBuilder(gitInitCommand.split(" "));
        processBuilder.directory(new File(projectDir));

        Process process = processBuilder.start();

        int exitCode = process.waitFor();
        System.out.println("命令执行结束" + exitCode);
    }
}
