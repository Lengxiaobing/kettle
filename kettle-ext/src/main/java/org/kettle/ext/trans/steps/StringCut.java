package org.kettle.ext.trans.steps;

import java.util.List;

import org.kettle.ext.core.PropsUI;
import org.kettle.ext.trans.step.AbstractStep;
import org.kettle.ext.utils.JsonArray;
import org.kettle.ext.utils.JsonObject;
import org.pentaho.di.core.database.DatabaseMeta;
import org.pentaho.di.trans.step.StepMetaInterface;
import org.pentaho.di.trans.steps.stringcut.StringCutMeta;
import org.pentaho.metastore.api.IMetaStore;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.mxgraph.model.mxCell;
import com.mxgraph.util.mxUtils;

@Component("StringCut")
@Scope("prototype")
public class StringCut extends AbstractStep {

	@Override
	public void decode(StepMetaInterface stepMetaInterface, mxCell cell, List<DatabaseMeta> databases, IMetaStore metaStore) throws Exception {
		StringCutMeta stringCutMeta = (StringCutMeta) stepMetaInterface;
		
		String fields = cell.getAttribute("fields");
		JsonArray jsonArray = JsonArray.fromObject(fields);
		String[] fieldInStream = new String[jsonArray.size()];
		String[] fieldOutStream = new String[jsonArray.size()];
		String[] cutFrom = new String[jsonArray.size()];
		String[] cutTo = new String[jsonArray.size()];
		for(int i=0; i<jsonArray.size(); i++) {
			JsonObject jsonObject = jsonArray.getJSONObject(i);
			fieldInStream[i] = jsonObject.optString("in_stream_name");
			fieldOutStream[i] = jsonObject.optString("out_stream_name");
			cutFrom[i] = jsonObject.optString("cut_from");
			cutTo[i] = jsonObject.optString("cut_to");
		}
		stringCutMeta.setFieldInStream(fieldInStream);
		stringCutMeta.setFieldOutStream(fieldOutStream);
		stringCutMeta.setCutFrom(cutFrom);
		stringCutMeta.setCutTo(cutTo);
	}

	@Override
	public Element encode(StepMetaInterface stepMetaInterface) throws Exception {
		Document doc = mxUtils.createDocument();
		Element e = doc.createElement(PropsUI.TRANS_STEP_NAME);
		StringCutMeta stringCutMeta = (StringCutMeta) stepMetaInterface;
		
	    JsonArray jsonArray = new JsonArray();
		String[] fieldInStream = stringCutMeta.getFieldInStream();
		String[] fieldOutStream = stringCutMeta.getFieldOutStream();
		String[] cutFrom = stringCutMeta.getCutFrom();
		String[] cutTo = stringCutMeta.getCutTo();
		for(int j=0; j<fieldInStream.length; j++) {
			JsonObject jsonObject = new JsonObject();
			jsonObject.put("in_stream_name", fieldInStream[j]);
			jsonObject.put("out_stream_name", fieldOutStream[j]);
			jsonObject.put("cut_from", cutFrom[j]);
			jsonObject.put("cut_to", cutTo[j]);
			jsonArray.add(jsonObject);
		}
		e.setAttribute("fields", jsonArray.toString());
		
		return e;
	}

}
