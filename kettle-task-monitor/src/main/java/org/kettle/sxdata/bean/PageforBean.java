package org.kettle.sxdata.bean;

import lombok.*;

import java.util.List;

/**
 * @description: 分页查询
 * @author: ZX
 * @date: 2018/11/20 12:07
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class PageforBean {
    /**
     * 分页的总记录条数
     */
    private int totalProperty;
    /**
     * 对象集合,表示本次的查询结果
     */
    private List root;
}
