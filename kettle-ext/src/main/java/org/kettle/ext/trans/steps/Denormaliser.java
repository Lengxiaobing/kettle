package org.kettle.ext.trans.steps;

import com.mxgraph.model.mxCell;
import com.mxgraph.util.mxUtils;
import org.kettle.ext.core.PropsUI;
import org.kettle.ext.trans.step.AbstractStep;
import org.kettle.ext.utils.JsonArray;
import org.kettle.ext.utils.JsonObject;
import org.pentaho.di.core.Const;
import org.pentaho.di.core.database.DatabaseMeta;
import org.pentaho.di.trans.step.StepMetaInterface;
import org.pentaho.di.trans.steps.denormaliser.DenormaliserMeta;
import org.pentaho.di.trans.steps.denormaliser.DenormaliserTargetField;
import org.pentaho.metastore.api.IMetaStore;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import java.util.List;

/**
 * Created by cRAZY on 2017/5/27.
 * 列转行
 */
@Component("Denormaliser")
@Scope("prototype")
public class Denormaliser extends AbstractStep {
    @Override
    public void decode(StepMetaInterface stepMetaInterface, mxCell cell, List<DatabaseMeta> databases, IMetaStore metaStore) throws Exception {
        DenormaliserMeta denor=(DenormaliserMeta)stepMetaInterface;
        denor.setKeyField(cell.getAttribute("key_field"));
        String group=cell.getAttribute("group");
        String fields=cell.getAttribute("fields");
        JsonArray groupJSONArray= JsonArray.fromObject(group);
        JsonArray fieldsJSONArray=JsonArray.fromObject(fields);

        if(groupJSONArray.size()>0){
            String[] groupField=new String[groupJSONArray.size()];
            for(int i=0;i<groupJSONArray.size();i++){
                JsonObject jsonObject = groupJSONArray.getJSONObject(i);
                groupField[i]=jsonObject.optString("name");
            }
            denor.setGroupField(groupField);
        }

        if(fieldsJSONArray.size()>0){
            DenormaliserTargetField[] denormaliserTargetField=new DenormaliserTargetField[fieldsJSONArray.size()];
            for(int j=0;j<fieldsJSONArray.size();j++){
                DenormaliserTargetField dtf=new DenormaliserTargetField();
                JsonObject jsonOBJ=fieldsJSONArray.getJSONObject(j);
                dtf.setFieldName(jsonOBJ.optString("field_name"));
                dtf.setKeyValue(jsonOBJ.optString("key_value"));

                dtf.setKeyValue(jsonOBJ.optString("key_value"));
                dtf.setTargetName(jsonOBJ.optString("target_name"));
                dtf.setTargetType(jsonOBJ.optString("target_type"));
                dtf.setTargetFormat(jsonOBJ.optString("target_format"));
                dtf.setTargetLength(Const.toInt(jsonOBJ.optString("target_length"), -1));
                dtf.setTargetPrecision(Const.toInt(jsonOBJ.optString("target_precision"), -1));
                dtf.setTargetDecimalSymbol(jsonOBJ.optString("target_decimal_symbol"));
                dtf.setTargetGroupingSymbol(jsonOBJ.optString("target_grouping_symbol"));
                dtf.setTargetCurrencySymbol(jsonOBJ.optString("target_currency_symbol"));
                dtf.setTargetNullString(jsonOBJ.optString("target_null_string"));
                dtf.setTargetAggregationType(jsonOBJ.optString("target_aggregation_type"));

                denormaliserTargetField[j]=dtf;
            }
            denor.setDenormaliserTargetField(denormaliserTargetField);
        }
    }

    @Override
    public Element encode(StepMetaInterface stepMetaInterface) throws Exception {
        DenormaliserMeta denor=(DenormaliserMeta)stepMetaInterface;
        Document doc = mxUtils.createDocument();
        Element e = doc.createElement(PropsUI.TRANS_STEP_NAME);
        e.setAttribute("key_field",denor.getKeyField());
        JsonArray jsonArray1=new JsonArray();
        for(int i=0;i<denor.getGroupField().length;i++){
            JsonObject jsonObj=new JsonObject();
            jsonObj.put("name",denor.getGroupField()[i]);
            jsonArray1.add(jsonObj);
        }
        e.setAttribute("group",jsonArray1.toString());

        JsonArray jsonArray2=new JsonArray();
        for(int j=0;j<denor.getDenormaliserTargetField().length;j++){
            DenormaliserTargetField field = denor.getDenormaliserTargetField()[j];
            JsonObject jsonOBJ=new JsonObject();
            jsonOBJ.put("field_name",field.getFieldName());
            jsonOBJ.put("key_value",field.getKeyValue());
            jsonOBJ.put("target_name",field.getTargetName());
            jsonOBJ.put("target_type",field.getTargetTypeDesc());
            jsonOBJ.put("target_format",field.getTargetFormat());
            jsonOBJ.put("target_length",field.getTargetLength());
            jsonOBJ.put("target_precision",field.getTargetPrecision());
            jsonOBJ.put("target_decimal_symbol",field.getTargetDecimalSymbol());
            jsonOBJ.put("target_grouping_symbol",field.getTargetGroupingSymbol());
            jsonOBJ.put("target_currency_symbol",field.getTargetCurrencySymbol());
            jsonOBJ.put("target_null_string",field.getTargetNullString());
            jsonOBJ.put("target_aggregation_type",field.getTargetAggregationTypeDesc());
            jsonArray2.add(jsonOBJ);
        }
        e.setAttribute("fields",jsonArray2.toString());

        return e;
    }
}
