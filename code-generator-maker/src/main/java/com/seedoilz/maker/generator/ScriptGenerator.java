package com.seedoilz.maker.generator;

import cn.hutool.core.io.FileUtil;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.Set;

public class ScriptGenerator {
    /**
     * @param outputPath 输出文件的路径
	 * @param jarPath 脚本中包含jar包的路径
     * @return void
     * @description 脚本文件生成方法
     * @author ruohao.zhang
     * @date 2024/02/28 20:25
     */
    public static void doGenerate(String outputPath, String jarPath) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("#!/bin/bash").append("\n");
        stringBuilder.append(String.format("java -jar %s \"$@\"", jarPath)).append("\n");
        FileUtil.writeBytes(stringBuilder.toString().getBytes(StandardCharsets.UTF_8), outputPath);
        // 添加可执行权限
        try {
            Set<PosixFilePermission> permissions = PosixFilePermissions.fromString("rwxrwxrwx");
            Files.setPosixFilePermissions(Paths.get(outputPath), permissions);
        } catch (Exception e){

        }
    }
}
