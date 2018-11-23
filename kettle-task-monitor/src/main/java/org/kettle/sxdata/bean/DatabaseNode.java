package org.kettle.sxdata.bean;

import lombok.Data;

/**
 * @description: 数据库节点
 * @author: ZX
 * @date: 2018/11/20 12:05
 */
@Data
public class DatabaseNode extends Ext3Node {

    private String nodeId;

    private String schema;

    public DatabaseNode(String text) {
        super(text);
    }

    public static DatabaseNode initNode(String text, String nodeId) {
        return initNode(text, nodeId, false);
    }

    public static DatabaseNode initNode(String text, String nodeId, String iconCls) {
        return initNode(text, iconCls, nodeId, true);
    }

    public static DatabaseNode initNode(String text, String nodeId, String iconCls, boolean leaf) {
        return initNode(text, iconCls, nodeId, leaf, false);
    }

    public static DatabaseNode initNode(String text, String nodeId, boolean expanded) {
        return initNode(text, null, nodeId, false, expanded);
    }

    public static DatabaseNode initNode(String text, String iconCls, String nodeId, boolean leaf, boolean expanded) {
        DatabaseNode node = new DatabaseNode(text);
        node.setLeaf(leaf);
        if (iconCls == null && !leaf) {
            node.setIconCls("imageFolder");
        } else {
            node.setIconCls(iconCls);
        }
        node.setExpanded(expanded);
        node.setNodeId(nodeId);
        return node;
    }

}
