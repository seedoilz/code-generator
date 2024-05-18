package com.seedoilz.maker.meta;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.seedoilz.maker.meta.enums.FileGeneratorTypeEnum;
import com.seedoilz.maker.meta.enums.FileTypeEnum;
import com.seedoilz.maker.meta.enums.ModelTypeEnum;

import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class MetaValidator {

    public static void doValidaAndFill(Meta meta) {
        // 基础信息校验和默认值
        validAndFillMetaRoot(meta);
        // fileConfig 校验和默认值
        validAndFillFileConfig(meta);
        // modelConfig 校验和默认值
        validAndFillModelConfig(meta);
    }

    private static void validAndFillModelConfig(Meta meta) {
        Meta.ModelConfig modelConfig = meta.getModelConfig();
        if (modelConfig == null) {
            return;
        }
        List<Meta.ModelConfig.ModelInfo> models = modelConfig.getModels();
        if (!CollectionUtil.isNotEmpty(models)) {
            return;
        }
        for (Meta.ModelConfig.ModelInfo model : models) {
            // 第六期，新增 groupKey，标记分组配置
            String groupKey = model.getGroupKey();
            if (StrUtil.isNotEmpty(groupKey)) {
                // 生成中间参数
                List<Meta.ModelConfig.ModelInfo> subModelInfoList = model.getModels();
                String allArgsStr = model.getModels().stream()
                        .map(subModelInfo -> String.format("\"--%s\"", subModelInfo.getFieldName()))
                        .collect(Collectors.joining(", "));
                model.setAllArgsStr(allArgsStr);
                continue;
            }
            String fieldName = model.getFieldName();
            if (StrUtil.isBlank(fieldName)) {
                throw new MetaException("未填写 fieldName");
            }

            String type = StrUtil.emptyToDefault(model.getType(), ModelTypeEnum.STRING.getValue());
            model.setType(type);
        }
    }

    private static void validAndFillFileConfig(Meta meta) {
        Meta.FileConfig fileConfig = meta.getFileConfig();
        if (fileConfig == null) {
            return;
        }
        String sourceRootPath = fileConfig.getSourceRootPath();
        if (StrUtil.isBlank(sourceRootPath)) {
            throw new MetaException("未填写 SourceRootPath");
        }

        String defaultInputRootPath = "source/" + FileUtil.getLastPathEle(Paths.get(sourceRootPath)).getFileName().toString();
        String inputRootPath = StrUtil.blankToDefault(fileConfig.getInputRootPath(), defaultInputRootPath);
        fileConfig.setInputRootPath(defaultInputRootPath);

        String outputRootPath = StrUtil.blankToDefault(fileConfig.getOutputRootPath(), "generated");
        fileConfig.setOutputRootPath(outputRootPath);

        String fileConfigType = StrUtil.emptyToDefault(fileConfig.getType(), FileTypeEnum.DIR.getValue());
        fileConfig.setType(fileConfigType);

        // fileInfo 默认值
        List<Meta.FileConfig.FileInfo> fileInfoList = fileConfig.getFiles();
        if (CollectionUtil.isNotEmpty(fileInfoList)) {
            for (Meta.FileConfig.FileInfo fileInfo : fileInfoList) {
                // 第六期，新增 group 分组类别
                String type = fileInfo.getType();
                if (FileTypeEnum.GROUP.getValue().equals(type)) {
                    continue;
                }

                // inputPath 必填
                String inputPath = fileInfo.getInputPath();
                if (StrUtil.isBlank(inputPath)) {
                    throw new MetaException("未填写 inputPath");
                }

                // outputPath 默认等于 inputPath
                String outputPath = StrUtil.emptyToDefault(fileInfo.getOutputPath(), inputPath);
                fileInfo.setOutputPath(outputPath);

                // type: 默认 inputPath 有文件后缀（如.java）为 file，否则为 dir
                if (StrUtil.isBlank(type)) {
                    // 无文件后缀
                    if (StrUtil.isBlank(FileUtil.getSuffix(inputPath))) {
                        fileInfo.setType(FileTypeEnum.DIR.getValue());
                    } else {
                        fileInfo.setType(FileTypeEnum.FILE.getValue());
                    }
                }

                // generateType: 如果文件结尾不为 .ftl，默认为static，否则为 dynamic
                String generateType = fileInfo.getGenerateType();
                if (StrUtil.isBlank(generateType)) {
                    // 动态模板
                    if (inputPath.endsWith(".ftl")) {
                        fileInfo.setGenerateType(FileGeneratorTypeEnum.DYNAMIC.getValue());
                    } else {
                        fileInfo.setGenerateType(FileGeneratorTypeEnum.STATIC.getValue());
                    }
                }
            }
        }
    }

    private static void validAndFillMetaRoot(Meta meta) {
        String name = StrUtil.blankToDefault(meta.getName(), "seedoilz-generator");
        meta.setName(name);

        String description = StrUtil.blankToDefault(meta.getDescription(), "我的模版代码生成器");
        meta.setDescription(description);

        String basePackage = StrUtil.blankToDefault(meta.getBasePackage(), "com.seedoilz");
        meta.setBasePackage(basePackage);

        String version = StrUtil.emptyToDefault(meta.getVersion(), "1.0");
        meta.setVersion(version);

        String author = StrUtil.emptyToDefault(meta.getAuthor(), "seedoilz");
        meta.setAuthor(author);

        String createTime = StrUtil.emptyToDefault(meta.getCreateTime(), DateUtil.now());
        meta.setCreateTime(createTime);
    }
}
