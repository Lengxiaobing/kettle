package org.kettle.sxdata.bean;

import lombok.Data;
import org.pentaho.di.repository.RepositoryObjectType;

/**
 * @description: 存储库节点
 * @author: ZX
 * @date: 2018/11/20 12:09
 */
@Data
public class RepositoryNode extends Ext3Node {

    private String path;

    private String type;

    public RepositoryNode(String text) {
        super(text);
    }

    public static RepositoryNode initNode(String text, String path) {
        return initNode(text, path, null, false, false, null);
    }

    public static RepositoryNode initNode(String text, String path, RepositoryObjectType type) {
        if (RepositoryObjectType.TRANSFORMATION.equals(type)) {
            return initNode(text, path, "trans", true, false, type.getTypeDescription());
        } else if (RepositoryObjectType.JOB.equals(type)) {
            return initNode(text, path, "job", true, false, type.getTypeDescription());
        }
        return null;
    }

    public static RepositoryNode initNode(String text, String path, String iconCls, boolean leaf, boolean expanded, String type) {
        RepositoryNode node = new RepositoryNode(text);
        node.setPath(path);
        if (iconCls == null && !leaf) {
            node.setIconCls("imageFolder");
        } else {
            node.setIconCls(iconCls);
        }
        node.setLeaf(leaf);
        node.setExpanded(expanded);
        node.setType(type);
        return node;
    }

}
