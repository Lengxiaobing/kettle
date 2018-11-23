package org.kettle.ext.job.step;

import com.mxgraph.model.mxCell;
import org.pentaho.di.core.Const;
import org.pentaho.di.core.database.DatabaseMeta;
import org.pentaho.di.core.plugins.JobEntryPluginType;
import org.pentaho.di.core.plugins.PluginInterface;
import org.pentaho.di.core.plugins.PluginRegistry;
import org.pentaho.di.job.entry.JobEntryCopy;
import org.pentaho.di.job.entry.JobEntryInterface;
import org.pentaho.metastore.api.IMetaStore;
import org.w3c.dom.Element;

import java.util.List;

/**
 * @description: 作业抽象类
 * @author: ZX
 * @date: 2018/11/20 18:28
 */
public abstract class AbstractJobEntry implements JobEntryEncoder, JobEntryDecoder {

    @Override
    public JobEntryCopy decodeStep(mxCell cell, List<DatabaseMeta> databases, IMetaStore metaStore) throws Exception {
        String stepId = cell.getAttribute("ctype");
        String stepName = cell.getAttribute("label");

        PluginRegistry registry = PluginRegistry.getInstance();
        PluginInterface jobPlugin = registry.findPluginWithId(JobEntryPluginType.class, stepId);
        JobEntryInterface entry = registry.loadClass(jobPlugin, JobEntryInterface.class);

        if (entry != null) {
            decode(entry, cell, databases, metaStore);
            if (jobPlugin != null) {
                entry.setPluginId(jobPlugin.getIds()[0]);
            }
            // inject metastore
            entry.setMetaStore(metaStore);

            // entry.loadXML( entrynode, databases, slaveServers, rep, metaStore );
            // compatibleLoadXml( entrynode, databases, slaveServers, rep );

            // Handle GUI information: nr & location?
            JobEntryCopy je = new JobEntryCopy(entry);
            je.setName(stepName);
            je.setNr(Const.toInt(cell.getAttribute("nr"), 0));
            je.setLaunchingInParallel("Y".equalsIgnoreCase(cell.getAttribute("parallel")));
            je.setDrawn("Y".equalsIgnoreCase(cell.getAttribute("draw")));
            je.setLocation((int) cell.getGeometry().getX(), (int) cell.getGeometry().getY());

//			attributesMap = AttributesUtil.loadAttributes(XMLHandler.getSubNode(entrynode, AttributesUtil.XML_TAG));

            return je;
        }

        return null;
    }

    @Override
    public Element encodeStep(JobEntryCopy je) throws Exception {
        Element e = encode(je.getEntry());

        e.setAttribute("label", je.getName());
        e.setAttribute("ctype", je.getEntry().getPluginId());
        e.setAttribute("draw", je.isDrawn() ? "Y" : "N");
        e.setAttribute("nr", String.valueOf(je.getNr()));
        e.setAttribute("parallel", je.isLaunchingInParallel() ? "Y" : "N");

        return e;
    }

    /**
     * 解码
     *
     * @param jobEntry
     * @param cell
     * @param databases
     * @param metaStore
     * @throws Exception
     */
    public abstract void decode(JobEntryInterface jobEntry, mxCell cell, List<DatabaseMeta> databases, IMetaStore metaStore) throws Exception;

    /**
     * 元素编码
     *
     * @param jobEntry
     * @return
     * @throws Exception
     */
    public abstract Element encode(JobEntryInterface jobEntry) throws Exception;

}
