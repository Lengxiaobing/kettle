package org.kettle.webapp.repository.beans;

/**
 * @description: 存储库节点类型
 * @author: ZX
 * @date: 2018/11/21 15:10
 */
public class RepositoryNodeType {

    private static final int LOAD_TRANS = 0x0001;
    private static final int LOAD_JOB = 0x0002;

    public static boolean includeTrans(int loadElement) {
        return (loadElement & LOAD_TRANS) > 0;
    }

    public static boolean includeJob(int loadElement) {
        return (loadElement & LOAD_JOB) > 0;
    }

}
