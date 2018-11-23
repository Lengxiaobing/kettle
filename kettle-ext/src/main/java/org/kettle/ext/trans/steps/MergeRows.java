package org.kettle.ext.trans.steps;

import java.util.List;

import org.kettle.ext.core.PropsUI;
import org.kettle.ext.trans.step.AbstractStep;
import org.kettle.ext.utils.JsonArray;
import org.kettle.ext.utils.JsonObject;
import org.pentaho.di.core.database.DatabaseMeta;
import org.pentaho.di.trans.step.StepMetaInterface;
import org.pentaho.di.trans.step.errorhandling.StreamInterface;
import org.pentaho.di.trans.steps.mergerows.MergeRowsMeta;
import org.pentaho.metastore.api.IMetaStore;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.mxgraph.model.mxCell;
import com.mxgraph.util.mxUtils;

@Component("MergeRows")
@Scope("prototype")
public class MergeRows extends AbstractStep {

	@Override
	public void decode(StepMetaInterface stepMetaInterface, mxCell cell, List<DatabaseMeta> databases, IMetaStore metaStore) throws Exception {
		MergeRowsMeta mergeRowsMeta = (MergeRowsMeta) stepMetaInterface;
		
		List<StreamInterface> infoStreams = mergeRowsMeta.getStepIOMeta().getInfoStreams();
		StreamInterface referenceStream = infoStreams.get(0);
		StreamInterface compareStream = infoStreams.get(1);

		compareStream.setSubject(cell.getAttribute("compare"));
		referenceStream.setSubject(cell.getAttribute("reference"));
		
		mergeRowsMeta.setFlagField(cell.getAttribute("flag_field"));
		
		String keys = cell.getAttribute("keys");
		JsonArray jsonArray = JsonArray.fromObject(keys);
		String[] keyFields = new String[jsonArray.size()];
		for(int i=0; i<jsonArray.size(); i++) {
			JsonObject jsonObject = jsonArray.getJSONObject(i);
			keyFields[i] = jsonObject.optString("key");
		}
		mergeRowsMeta.setKeyFields(keyFields);
		
		String values = cell.getAttribute("values");
		jsonArray = JsonArray.fromObject(values);
		String[] valueFields = new String[jsonArray.size()];
		for(int i=0; i<jsonArray.size(); i++) {
			JsonObject jsonObject = jsonArray.getJSONObject(i);
			valueFields[i] = jsonObject.optString("value");
		}
		mergeRowsMeta.setValueFields(valueFields);
	}

	@Override
	public Element encode(StepMetaInterface stepMetaInterface) throws Exception {
		Document doc = mxUtils.createDocument();
		Element e = doc.createElement(PropsUI.TRANS_STEP_NAME);
		MergeRowsMeta mergeRowsMeta = (MergeRowsMeta) stepMetaInterface;
		
		List<StreamInterface> infoStreams = mergeRowsMeta.getStepIOMeta().getInfoStreams();
		e.setAttribute("reference", infoStreams.get(0).getStepname());
		e.setAttribute("compare", infoStreams.get(1).getStepname());
		e.setAttribute("flag_field", mergeRowsMeta.getFlagField());
		
		JsonArray jsonArray = new JsonArray();
		String[] keyFields = mergeRowsMeta.getKeyFields();
		for(int j=0; j<keyFields.length; j++) {
			JsonObject jsonObject = new JsonObject();
			jsonObject.put("key", keyFields[j]);
			jsonArray.add(jsonObject);
		}
		e.setAttribute("keys", jsonArray.toString());
		
		jsonArray = new JsonArray();
		String[] valueFields = mergeRowsMeta.getValueFields();
		for(int j=0; j<valueFields.length; j++) {
			JsonObject jsonObject = new JsonObject();
			jsonObject.put("value", valueFields[j]);
			jsonArray.add(jsonObject);
		}
		e.setAttribute("values", jsonArray.toString());
		
		return e;
	}

}