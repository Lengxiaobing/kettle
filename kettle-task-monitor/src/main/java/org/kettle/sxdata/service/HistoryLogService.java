package org.kettle.sxdata.service;

/**
 * @description: HistoryLogService
 * @author: ZX
 * @date: 2018/11/20 17:20
 */
public interface HistoryLogService {
    /**
     * 获取所有历史记录
     *
     * @param start
     * @param limit
     * @param statu
     * @param type
     * @param startDate
     * @param taskName
     * @param userGroupName
     * @return
     * @throws Exception
     */
    String getAllHistoryLog(int start, int limit, String statu, String type, String startDate, String taskName, String userGroupName) throws Exception;

    /**
     * 根据ID获取执行跟踪
     *
     * @param id
     * @return
     * @throws Exception
     */
    String getExecutionTraceById(Integer id) throws Exception;

}
