package org.kettle.ext.trans.steps;

import java.util.List;

import org.kettle.ext.core.PropsUI;
import org.kettle.ext.trans.step.AbstractStep;
import org.kettle.ext.utils.JsonArray;
import org.kettle.ext.utils.JsonObject;
import org.pentaho.di.core.database.DatabaseMeta;
import org.pentaho.di.trans.step.StepMetaInterface;
import org.pentaho.di.trans.steps.flattener.FlattenerMeta;
import org.pentaho.metastore.api.IMetaStore;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.mxgraph.model.mxCell;
import com.mxgraph.util.mxUtils;

@Component("Flattener")
@Scope("prototype")
public class Flattener extends AbstractStep {

	@Override
	public void decode(StepMetaInterface stepMetaInterface, mxCell cell, List<DatabaseMeta> databases, IMetaStore metaStore) throws Exception {
		FlattenerMeta  flattenerMeta = (FlattenerMeta) stepMetaInterface;
		
		flattenerMeta.setFieldName(cell.getAttribute("field_name"));
		String fields = cell.getAttribute("fields");
		JsonArray jsonArray = JsonArray.fromObject(fields);
		String[] targetField = new String[jsonArray.size()];
		for(int i=0; i<jsonArray.size(); i++) {
			JsonObject jsonObject = jsonArray.getJSONObject(i);
			targetField[i] = jsonObject.optString("name");
		}
		flattenerMeta.setTargetField(targetField);
	}

	@Override
	public Element encode(StepMetaInterface stepMetaInterface) throws Exception {
		Document doc = mxUtils.createDocument();
		Element e = doc.createElement(PropsUI.TRANS_STEP_NAME);
		FlattenerMeta  flattenerMeta = (FlattenerMeta) stepMetaInterface;
		
		e.setAttribute("field_name", flattenerMeta.getFieldName());
		JsonArray jsonArray = new JsonArray();
		String[] targetField = flattenerMeta.getTargetField();
		for(int j=0; j<targetField.length; j++) {
			JsonObject jsonObject = new JsonObject();
			jsonObject.put("name", targetField[j]);
			jsonArray.add(jsonObject);
		}
		e.setAttribute("fields", jsonArray.toString());
		
		return e;
	}

}

