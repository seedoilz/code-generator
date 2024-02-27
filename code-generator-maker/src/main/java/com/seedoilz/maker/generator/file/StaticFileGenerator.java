package com.seedoilz.maker.generator.file;

import cn.hutool.core.io.FileUtil;

public class StaticFileGenerator {

    /**
     * @param inputPath
     * @param outputPath
     * @return void
     * @description 通过Hutool来完成文件复制
     * @author ruohao.zhang
     * @date 2024/02/24 14:45
     */
    public static void copyFilesByHutool(String inputPath, String outputPath) {
        FileUtil.copy(inputPath, outputPath, false);
    }
}
