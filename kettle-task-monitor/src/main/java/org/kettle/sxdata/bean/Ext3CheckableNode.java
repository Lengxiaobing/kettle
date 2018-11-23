package org.kettle.sxdata.bean;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @description: Ext3检查节点
 * @author: ZX
 * @date: 2018/11/20 12:06
 */
@Data
public class Ext3CheckableNode extends Ext3Node {

    private boolean checked = false;
    private List children = new ArrayList(0);

    public Ext3CheckableNode(String text) {
        super(text);
    }

    public Ext3CheckableNode(String id, String text) {
        super(id, text);
    }

}
