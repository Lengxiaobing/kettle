package org.kettle.sxdata.controller;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.kettle.ext.utils.RepositoryUtils;
import org.kettle.sxdata.entity.DatabaseConnEntity;
import org.kettle.sxdata.service.CommonService;
import org.pentaho.di.core.database.DatabaseMeta;
import org.pentaho.di.trans.TransMeta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.List;

/**
 * @description: 通用控制器
 *
 * @author:   ZX
 * @date:     2018/11/20 11:48
 */
@Controller
@RequestMapping(value = "/common")
public class CommonController {
    @Autowired
    protected CommonService cService;

    /**
     * 获取所有的数据库连接
     *
     * @param response
     * @param request
     * @throws Exception
     */
    @RequestMapping(value = "/getDatabases")
    @ResponseBody
    protected void getDatabases(HttpServletResponse response, HttpServletRequest request) throws Exception {
        try {
            List<DatabaseConnEntity> items = cService.getDatabases();
            String result = JSONArray.fromObject(items).toString();
            response.setContentType("text/html;charset=utf-8");
            PrintWriter out = response.getWriter();
            out.write(result);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }

    /**
     * 获取元数据
     *
     * @param response
     * @param databaseConn
     * @throws Exception
     */
    @RequestMapping(value = "/getDatabaseMeta")
    @ResponseBody
    protected void getDatabaseMeta(HttpServletResponse response, @RequestParam String databaseConn) throws Exception {
        try {

            TransMeta tra = RepositoryUtils.loadTransByPath("/test");
            DatabaseMeta inf = tra.findDatabase(databaseConn);
            String result = JSONObject.fromObject(inf).toString();
            response.setContentType("text/html;charset=utf-8");
            PrintWriter out = response.getWriter();
            out.write(result);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }

    /**
     * 删除数据库连接
     *
     * @param id
     * @throws Exception
     */
    @RequestMapping(value = "/deleteDatabaseConn")
    @ResponseBody
    protected void deleteDatabaseConn(@RequestParam String id) throws Exception {
        cService.deleteDatabaseConn(Integer.valueOf(id));
    }
}
