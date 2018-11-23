package org.kettle.webapp.bean;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import org.pentaho.di.repository.RepositoryObjectType;

/**
 * @description: 存储库检查节点
 * @author: ZX
 * @date: 2018/11/21 14:20
 */
@Data
public class RepositoryCheckNode extends Ext3CheckableNode {

    private String path;

    private String type;

    public RepositoryCheckNode(String text) {
        super(text);
    }

    public RepositoryCheckNode(String text, boolean checked) {
        super(text);
        setChecked(checked);
    }

    public static RepositoryCheckNode initNode(String text, String path) {
        return initNode(text, path, null, false, true, null, new ArrayList(0), true);
    }

    public static RepositoryCheckNode initNode(String text, String path, List children) {
        return initNode(text, path, null, false, false, null, children, false);
    }

    public static RepositoryCheckNode initNode(String text, String path, RepositoryObjectType type) {
        if (RepositoryObjectType.TRANSFORMATION.equals(type)) {
            return initNode(text, path, type, false);
        } else if (RepositoryObjectType.JOB.equals(type)) {
            return initNode(text, path, type, false);
        }
        return null;
    }

    public static RepositoryCheckNode initNode(String text, String path, RepositoryObjectType type, boolean checked) {
        if (RepositoryObjectType.TRANSFORMATION.equals(type)) {
            return initNode(text, path, "trans", true, false, type.getTypeDescription(), null, checked);
        } else if (RepositoryObjectType.JOB.equals(type)) {
            return initNode(text, path, "job", true, false, type.getTypeDescription(), null, checked);
        }
        return null;
    }

    public static RepositoryCheckNode initNode(String text, String path, String iconCls, boolean leaf, boolean expanded, String type, List children, boolean checked) {
        RepositoryCheckNode node = new RepositoryCheckNode(text);
        node.setPath(path);
        if (iconCls == null && !leaf) {
            node.setIconCls("imageFolder");
        } else {
            node.setIconCls(iconCls);
        }
        node.setLeaf(leaf);
        node.setExpanded(expanded);
        node.setType(type);
        node.setChildren(children);
        node.setChecked(checked);
        return node;
    }
}
