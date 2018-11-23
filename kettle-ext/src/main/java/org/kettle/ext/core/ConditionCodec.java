package org.kettle.ext.core;

import org.kettle.ext.core.row.ValueMetaAndDataCodec;
import org.kettle.ext.utils.JsonArray;
import org.kettle.ext.utils.JsonObject;
import org.pentaho.di.core.Condition;
import org.pentaho.di.core.exception.KettleValueException;
import org.pentaho.di.core.row.ValueMetaAndData;

/**
 * @description: 条件编解码器
 * @author: ZX
 * @date: 2018/11/20 18:13
 */
public class ConditionCodec {

    public static JsonObject encode(Condition condition) throws KettleValueException {
        JsonObject jsonObject = new JsonObject();
        jsonObject.put("negated", condition.isNegated());
        jsonObject.put("operator", condition.getOperator());

        if (condition.isAtomic()) {
            jsonObject.put("left_valuename", condition.getLeftValuename());
            jsonObject.put("func", condition.getFunction());
            jsonObject.put("right_valuename", condition.getRightValuename());

            if (condition.getRightExact() != null) {
                ValueMetaAndData rightExact = condition.getRightExact();
                jsonObject.put("right_exact", ValueMetaAndDataCodec.encode(rightExact));
            }
        } else {
            JsonArray conditions = new JsonArray();
            for (int i = 0; i < condition.nrConditions(); i++) {
                Condition child = condition.getCondition(i);
                conditions.add(encode(child));
            }

            jsonObject.put("conditions", conditions);
        }

        return jsonObject;
    }

    public static Condition decode(JsonObject jsonObject) throws KettleValueException {
        Condition condition = new Condition();
        condition.setNegated(jsonObject.optBoolean("negated"));
        condition.setOperator(jsonObject.optInt("operator"));

        JsonArray conditions = jsonObject.optJSONArray("conditions");
        if (conditions == null || conditions.size() == 0) {
            condition.setLeftValuename(jsonObject.optString("left_valuename"));
            condition.setFunction(jsonObject.optInt("func"));
            condition.setRightValuename(jsonObject.optString("right_valuename"));
            JsonObject right_exact = jsonObject.optJSONObject("right_exact");
            if (right_exact != null) {
                ValueMetaAndData exact = ValueMetaAndDataCodec.decode(right_exact);
                condition.setRightExact(exact);
            }
        } else {
            for (int i = 0; i < conditions.size(); i++) {
                JsonObject child = conditions.getJSONObject(i);
                condition.addCondition(decode(child));
            }
        }

        return condition;
    }

}
