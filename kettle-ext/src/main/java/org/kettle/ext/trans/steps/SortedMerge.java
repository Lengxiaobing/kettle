package org.kettle.ext.trans.steps;

import java.util.List;

import org.kettle.ext.core.PropsUI;
import org.kettle.ext.trans.step.AbstractStep;
import org.kettle.ext.utils.JsonArray;
import org.kettle.ext.utils.JsonObject;
import org.pentaho.di.core.database.DatabaseMeta;
import org.pentaho.di.trans.step.StepMetaInterface;
import org.pentaho.di.trans.steps.sortedmerge.SortedMergeMeta;
import org.pentaho.metastore.api.IMetaStore;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.mxgraph.model.mxCell;
import com.mxgraph.util.mxUtils;

@Component("SortedMerge")
@Scope("prototype")
public class SortedMerge extends AbstractStep {

	@Override
	public void decode(StepMetaInterface stepMetaInterface, mxCell cell, List<DatabaseMeta> databases, IMetaStore metaStore) throws Exception {
		SortedMergeMeta sortedMergeMeta = (SortedMergeMeta) stepMetaInterface;
		
		String fields = cell.getAttribute("fields");
		JsonArray jsonArray = JsonArray.fromObject(fields);
		String[] fieldName = new String[jsonArray.size()];
		boolean[] ascending = new boolean[jsonArray.size()];
		for(int i=0; i<jsonArray.size(); i++) {
			JsonObject jsonObject = jsonArray.getJSONObject(i);
			fieldName[i] = jsonObject.optString("name");
			ascending[i] = "Y".equalsIgnoreCase("ascending");
		}
		
		sortedMergeMeta.setFieldName(fieldName);
		sortedMergeMeta.setAscending(ascending);
	}

	@Override
	public Element encode(StepMetaInterface stepMetaInterface) throws Exception {
		Document doc = mxUtils.createDocument();
		Element e = doc.createElement(PropsUI.TRANS_STEP_NAME);
		SortedMergeMeta sortedMergeMeta = (SortedMergeMeta) stepMetaInterface;
		
		JsonArray jsonArray = new JsonArray();
		String[] fieldName = sortedMergeMeta.getFieldName();
		boolean[] ascending = sortedMergeMeta.getAscending();
		for(int j=0; j<fieldName.length; j++) {
			JsonObject jsonObject = new JsonObject();
			jsonObject.put("name", fieldName[j]);
			jsonObject.put("ascending", ascending[j] ? "Y" : "N");
			jsonArray.add(jsonObject);
		}
		e.setAttribute("fields", jsonArray.toString());
		
		return e;
	}

}

