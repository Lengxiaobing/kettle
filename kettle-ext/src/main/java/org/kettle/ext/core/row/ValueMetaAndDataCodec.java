package org.kettle.ext.core.row;

import org.kettle.ext.utils.JsonObject;
import org.pentaho.di.core.Const;
import org.pentaho.di.core.exception.KettleValueException;
import org.pentaho.di.core.row.ValueMeta;
import org.pentaho.di.core.row.ValueMetaAndData;
import org.pentaho.di.core.row.ValueMetaInterface;
import org.pentaho.di.core.row.value.ValueMetaBase;

/**
 * @description: 元数据编解码器
 * @author: ZX
 * @date: 2018/11/20 18:13
 */
public class ValueMetaAndDataCodec {

    public static JsonObject encode(ValueMetaAndData valueMetaAndData) throws KettleValueException {
        JsonObject jsonObject = new JsonObject();
        ValueMetaInterface meta = valueMetaAndData.getValueMeta().clone();
        meta.setDecimalSymbol(".");
        meta.setGroupingSymbol(null);
        meta.setCurrencySymbol(null);

        jsonObject.put("name", meta.getName());
        jsonObject.put("type", meta.getTypeDesc());
        jsonObject.put("text", meta.getCompatibleString(valueMetaAndData.getValueData()));
        jsonObject.put("length", meta.getLength());
        jsonObject.put("precision", meta.getPrecision());

        jsonObject.put("isnull", meta.isNull(valueMetaAndData.getValueData()));
        jsonObject.put("mask", meta.getConversionMask());
        return jsonObject;
    }

    public static ValueMetaAndData decode(JsonObject jsonObject) throws KettleValueException {
        ValueMetaAndData valueMetaAndData = new ValueMetaAndData();

        String valname = jsonObject.optString("name");
        int valtype = ValueMetaBase.getType(jsonObject.optString("type"));
        String text = jsonObject.optString("text");
        boolean isnull = jsonObject.optBoolean("isnull");
        int len = jsonObject.optInt("length", -1);
        int prec = jsonObject.optInt("precision", -1);

        String mask = jsonObject.optString("mask");

        ValueMeta valueMeta = new ValueMeta(valname, valtype);
        valueMeta.setLength(len);
        valueMeta.setPrecision(prec);
        if (mask != null) {
            valueMeta.setConversionMask(mask);
        }

        valueMetaAndData.setValueData(text);

        if (valtype != ValueMetaInterface.TYPE_STRING) {
            ValueMetaInterface originMeta = new ValueMeta(valname, ValueMetaInterface.TYPE_STRING);
            if (valueMeta.isNumeric()) {
                originMeta.setDecimalSymbol(".");
                originMeta.setGroupingSymbol(null);
                originMeta.setCurrencySymbol(null);
            }
            valueMetaAndData.setValueData(valueMeta.convertData(originMeta, Const.trim(text)));
        }

        if (isnull) {
            valueMetaAndData.setValueData(null);
        }

        valueMetaAndData.setValueMeta(valueMeta);

        return valueMetaAndData;
    }

}
