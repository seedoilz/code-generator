package com.seedoilz.maker.template.enums;

import cn.hutool.core.util.ObjUtil;
import lombok.Getter;

@Getter
public enum FileFilterRangeEnum {

    FILE_NAME("文件名称", "fileName"),
    FILE_CONTENT("文件内容", "fileContent");

    private final String text;
    private final String value;

    FileFilterRangeEnum(String text, String value){
        this.text = text;
        this.value = value;
    }

    public static FileFilterRangeEnum getEnumByValue(String value) {
        if (ObjUtil.isEmpty(value)) {
            return null;
        }
        for (FileFilterRangeEnum fileFilterRangeEnum : FileFilterRangeEnum.values()) {
            if (fileFilterRangeEnum.value.equals(value)) {
                return fileFilterRangeEnum;
            }
        }
        return null;
    }
}
