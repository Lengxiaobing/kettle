package org.kettle.ext.trans.steps;

import java.util.List;

import org.kettle.ext.core.PropsUI;
import org.kettle.ext.trans.step.AbstractStep;
import org.kettle.ext.utils.JsonArray;
import org.kettle.ext.utils.JsonObject;
import org.pentaho.di.core.database.DatabaseMeta;
import org.pentaho.di.trans.step.StepMetaInterface;
import org.pentaho.di.trans.steps.randomvalue.RandomValueMeta;
import org.pentaho.metastore.api.IMetaStore;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.mxgraph.model.mxCell;
import com.mxgraph.util.mxUtils;

@Component("RandomValue")
@Scope("prototype")
public class RandomValue extends AbstractStep {

	@Override
	public void decode(StepMetaInterface stepMetaInterface, mxCell cell, List<DatabaseMeta> databases, IMetaStore metaStore) throws Exception {
		RandomValueMeta randomValueMeta = (RandomValueMeta) stepMetaInterface;
		
		String fields = cell.getAttribute("fields");
		JsonArray jsonArray = JsonArray.fromObject(fields);
		String[] fieldName = new String[jsonArray.size()];
		int[] fieldType = new int[jsonArray.size()];
		
		for(int i=0; i<jsonArray.size(); i++) {
			JsonObject jsonObject = jsonArray.getJSONObject(i);
			fieldName[i] = jsonObject.optString("name");
			fieldType[i] = jsonObject.optInt("type");
		}
		randomValueMeta.setFieldName(fieldName);
		randomValueMeta.setFieldType(fieldType);
	}

	@Override
	public Element encode(StepMetaInterface stepMetaInterface) throws Exception {
		RandomValueMeta randomValueMeta = (RandomValueMeta) stepMetaInterface;
		Document doc = mxUtils.createDocument();
		Element e = doc.createElement(PropsUI.TRANS_STEP_NAME);
		
		JsonArray jsonArray = new JsonArray();
		String[] fieldName = randomValueMeta.getFieldName();
		if(fieldName != null) {
			for(int i=0; i<fieldName.length; i++) {
				JsonObject jsonObject = new JsonObject();
				jsonObject.put("name", fieldName[i]);
				jsonObject.put("type", randomValueMeta.getFieldType()[i]);
				jsonArray.add(jsonObject);
			}
		}
		e.setAttribute("fields", jsonArray.toString());
		
		return e;
	}

}
