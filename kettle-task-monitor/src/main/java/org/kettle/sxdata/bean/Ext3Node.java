package org.kettle.sxdata.bean;

import lombok.Data;

import java.util.UUID;

/**
 * @description: Ext3节点
 * @author: ZX
 * @date: 2018/11/20 12:06
 */
@Data
public class Ext3Node {

    private String id;
    private String text;
    private String iconCls = "imageFolder";
    private boolean expanded = false;
    private boolean leaf = false;

    public Ext3Node(String text) {
        this(UUID.randomUUID().toString().replaceAll("-", ""), text);
    }

    public Ext3Node(String id, String text) {
        this.id = id;
        this.text = text;
    }

    public static Ext3Node initNode(String id, String text) {
        return initNode(id, text, null, false);
    }

    public static Ext3Node initNode(String id, String text, boolean leaf) {
        return initNode(id, text, null, true);
    }

    public static Ext3Node initNode(String id, String text, String iconCls, boolean leaf) {
        Ext3Node fn = new Ext3Node(id, text);
        if (iconCls == null && !leaf) {
            fn.setIconCls("imageFolder");
        } else {
            fn.setIconCls(iconCls);
        }
        fn.setLeaf(leaf);
        return fn;
    }

}
