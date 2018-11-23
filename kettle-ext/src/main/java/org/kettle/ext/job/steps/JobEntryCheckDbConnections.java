package org.kettle.ext.job.steps;

import java.util.List;

import org.kettle.ext.core.PropsUI;
import org.kettle.ext.job.step.AbstractJobEntry;
import org.kettle.ext.utils.JsonArray;
import org.kettle.ext.utils.JsonObject;
import org.pentaho.di.core.Const;
import org.pentaho.di.core.database.DatabaseMeta;
import org.pentaho.di.job.entry.JobEntryInterface;
import org.pentaho.metastore.api.IMetaStore;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.mxgraph.model.mxCell;
import com.mxgraph.util.mxUtils;

/**
 * @description: 作业输入--检查Db连接
 *
 * @author:   ZX
 * @date:     2018/11/21 10:36
 */
@Component("CHECK_DB_CONNECTIONS")
@Scope("prototype")
public class JobEntryCheckDbConnections extends AbstractJobEntry {

    @Override
    public void decode(JobEntryInterface jobEntry, mxCell cell, List<DatabaseMeta> databases, IMetaStore metaStore) throws Exception {
        org.pentaho.di.job.entries.checkdbconnection.JobEntryCheckDbConnections jobEntryCheckDbConnections = (org.pentaho.di.job.entries.checkdbconnection.JobEntryCheckDbConnections) jobEntry;

        String fields = cell.getAttribute("connections");
        JsonArray jsonArray = JsonArray.fromObject(fields);
        jobEntryCheckDbConnections.setConnections(new DatabaseMeta[jsonArray.size()]);
        jobEntryCheckDbConnections.setWaittimes(new int[jsonArray.size()]);
        jobEntryCheckDbConnections.setWaitfors(new String[jsonArray.size()]);
        for (int i = 0; i < jsonArray.size(); i++) {
            JsonObject jsonObject = jsonArray.getJSONObject(i);
            jobEntryCheckDbConnections.getConnections()[i] = DatabaseMeta.findDatabase(databases, jsonObject.optString("name"));
            jobEntryCheckDbConnections.getWaittimes()[i] = jobEntryCheckDbConnections.getWaitTimeByDesc(Const.NVL(jsonObject.optString("waittime"), ""));
            jobEntryCheckDbConnections.getWaitfors()[i] = jsonObject.optString("waitfor");
        }
    }

    @Override
    public Element encode(JobEntryInterface jobEntry) throws Exception {
        Document doc = mxUtils.createDocument();
        Element e = doc.createElement(PropsUI.JOB_JOBENTRY_NAME);
        org.pentaho.di.job.entries.checkdbconnection.JobEntryCheckDbConnections jobEntryCheckDbConnections = (org.pentaho.di.job.entries.checkdbconnection.JobEntryCheckDbConnections) jobEntry;

        JsonArray jsonArray = new JsonArray();
        DatabaseMeta[] connections = jobEntryCheckDbConnections.getUsedDatabaseConnections();
        if (connections != null) {
            for (int j = 0; j < connections.length; j++) {
                JsonObject jsonObject = new JsonObject();
                jsonObject.put("name", connections[j].getName());
                jsonObject.put("waittime", jobEntryCheckDbConnections.unitTimeCode[jobEntryCheckDbConnections.getWaittimes()[j]]);
                jsonObject.put("waitfor", jobEntryCheckDbConnections.getWaitfors()[j]);
                jsonArray.add(jsonObject);
            }
        }

        e.setAttribute("connections", jsonArray.toString());

        return e;
    }


}

