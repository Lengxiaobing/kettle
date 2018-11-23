package org.kettle.webapp.core;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.springframework.util.LinkedCaseInsensitiveMap;

/**
 * @description: 文件节点类型
 * @author: ZX
 * @date: 2018/11/21 14:29
 */
public enum FileNodeType {

    /**
     * 所有文件
     */
    ALL(1, "所有文件", "*"),
    /**
     * 作业文件
     */
    KJB(2, "作业文件", "kjb"),
    /**
     * 转换文件
     */
    KTR(4, "转换文件", "ktr"),
    /**
     * 文本文件
     */
    TXT(8, "文本文件", "txt"),
    /**
     * CSV文件
     */
    CSV(16, "CSV文件", "csv");

    private int type;
    private String typeDesc;
    private String extension;


    FileNodeType(int type, String typeDesc, String extension) {
        this.type = type;
        this.typeDesc = typeDesc;
        this.extension = extension;
    }

    public static boolean includeNone(int type) {
        return type == 0;
    }

    public static boolean match(String extension, int type) throws IllegalArgumentException, IllegalAccessException {
        if ((FileNodeType.ALL.type & type) > 0) {
            return true;
        }
        for (Field field : FileNodeType.class.getDeclaredFields()) {
            if (field.getType().equals(FileNodeType.class)) {
                FileNodeType fnt = (FileNodeType) field.get(null);

                if ((fnt.type & type) == 0) {
                    continue;
                }

                return fnt.extension.equalsIgnoreCase(extension);
            }
        }

        return false;
    }

    public static String getExtension(String fileName) {
        if (fileName == null || fileName.indexOf(".") < 0) {
            return null;
        }
        return fileName.substring(fileName.indexOf(".") + 1);
    }

    public static List toList(int type) throws IllegalArgumentException, IllegalAccessException {
        ArrayList list = new ArrayList();
        for (Field field : FileNodeType.class.getDeclaredFields()) {
            if (field.getType().equals(FileNodeType.class)) {
                FileNodeType fnt = (FileNodeType) field.get(null);

                if ((fnt.type & type) > 0 || fnt.type == 1) {
                    LinkedCaseInsensitiveMap record = new LinkedCaseInsensitiveMap();
                    record.put("type", fnt.type);
                    record.put("desc", fnt.typeDesc);
                    list.add(record);
                }
            }
        }
        return list;
    }
}
