package com.seedoilz.maker.template.model;

import com.seedoilz.maker.template.model.FileFilterConfig;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 模板制作文件配置
 */
@Data
public class TemplateMakerFileConfig {

    private List<FileInfoConfig> files;

    private FileGroupConfig fileGroupConfig;

    @NoArgsConstructor
    @Data
    public static class FileInfoConfig {

        /**
         * 文件（目录）路径
         */
        private String path;

        /**
         * 文件过滤配置
         */
        private List<FileFilterConfig> fileFilterConfigList;
    }

    @Data
    public static class FileGroupConfig {

        private String condition;

        private String groupKey;

        private String groupName;
    }
}