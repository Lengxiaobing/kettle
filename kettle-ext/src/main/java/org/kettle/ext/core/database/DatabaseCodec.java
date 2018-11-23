package org.kettle.ext.core.database;

import org.kettle.ext.utils.JsonArray;
import org.kettle.ext.utils.JsonObject;
import org.kettle.ext.utils.StringEscapeHelper;
import org.pentaho.di.core.database.*;
import org.pentaho.di.core.database.sap.SAPR3DatabaseMeta;
import org.pentaho.di.core.encryption.Encr;
import org.pentaho.di.core.exception.KettleDatabaseException;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * @description: 数据库编解码器
 * @author: ZX
 * @date: 2018/11/20 18:09
 */
public class DatabaseCodec {

    public static JsonObject encode(DatabaseMeta databaseMeta) {
        JsonObject jsonObject = new JsonObject();

        jsonObject.put("name", databaseMeta.getDisplayName());
        jsonObject.put("type", databaseMeta.getPluginId());
        jsonObject.put("access", databaseMeta.getAccessType());

        jsonObject.put("hostname", databaseMeta.getHostname());
        jsonObject.put("databaseName", databaseMeta.getDatabaseName());
        jsonObject.put("username", databaseMeta.getUsername());
        jsonObject.put("password", Encr.decryptPasswordOptionallyEncrypted(databaseMeta.getPassword()));
        if (databaseMeta.isStreamingResults()) {
            jsonObject.put("streamingResults", databaseMeta.isStreamingResults());
        }
        jsonObject.put("dataTablespace", databaseMeta.getDataTablespace());
        jsonObject.put("indexTablespace", databaseMeta.getIndexTablespace());
        if (databaseMeta.getSQLServerInstance() != null) {
            jsonObject.put("sqlServerInstance", databaseMeta.getSQLServerInstance());
        }
        if (databaseMeta.isUsingDoubleDecimalAsSchemaTableSeparator()) {
            jsonObject.put("usingDoubleDecimalAsSchemaTableSeparator", databaseMeta.isUsingDoubleDecimalAsSchemaTableSeparator());
        }
        jsonObject.put(SAPR3DatabaseMeta.ATTRIBUTE_SAP_LANGUAGE, databaseMeta.getAttributes().getProperty(SAPR3DatabaseMeta.ATTRIBUTE_SAP_LANGUAGE));
        jsonObject.put(SAPR3DatabaseMeta.ATTRIBUTE_SAP_SYSTEM_NUMBER, databaseMeta.getAttributes().getProperty(SAPR3DatabaseMeta.ATTRIBUTE_SAP_SYSTEM_NUMBER));
        jsonObject.put(SAPR3DatabaseMeta.ATTRIBUTE_SAP_CLIENT, databaseMeta.getAttributes().getProperty(SAPR3DatabaseMeta.ATTRIBUTE_SAP_CLIENT));

        jsonObject.put(GenericDatabaseMeta.ATRRIBUTE_CUSTOM_URL, databaseMeta.getAttributes().getProperty(GenericDatabaseMeta.ATRRIBUTE_CUSTOM_URL));
        jsonObject.put(GenericDatabaseMeta.ATRRIBUTE_CUSTOM_DRIVER_CLASS, databaseMeta.getAttributes().getProperty(GenericDatabaseMeta.ATRRIBUTE_CUSTOM_DRIVER_CLASS));
        jsonObject.put("servername", databaseMeta.getServername());

        Object v = databaseMeta.getAttributes().get(MSSQLServerNativeDatabaseMeta.ATTRIBUTE_USE_INTEGRATED_SECURITY);
        if (v != null && v instanceof String) {
            String useIntegratedSecurity = (String) v;
            jsonObject.put(MSSQLServerNativeDatabaseMeta.ATTRIBUTE_USE_INTEGRATED_SECURITY, Boolean.parseBoolean(useIntegratedSecurity));
        } else {
            jsonObject.put(MSSQLServerNativeDatabaseMeta.ATTRIBUTE_USE_INTEGRATED_SECURITY, false);
        }

        jsonObject.put("port", databaseMeta.getDatabasePortNumberString());

        // Option parameters:
        Map<String, String> extraOptions = databaseMeta.getExtraOptions();
        JsonArray options = new JsonArray();
        if (extraOptions != null) {
            Iterator<String> keys = extraOptions.keySet().iterator();
            String currentType = databaseMeta.getPluginId();
            while (keys.hasNext()) {

                String parameter = keys.next();
                String value = extraOptions.get(parameter);
                if (!StringUtils.hasText(value) || (value.equals(DatabaseMeta.EMPTY_OPTIONS_STRING))) {
                    value = "";
                }

                int dotIndex = parameter.indexOf('.');
                if (dotIndex >= 0) {
                    String parameterOption = parameter.substring(dotIndex + 1);
                    String databaseType = parameter.substring(0, dotIndex);
                    if (currentType != null && currentType.equals(databaseType)) {
                        JsonObject jsonObject2 = new JsonObject();
                        jsonObject2.put("name", parameterOption);
                        jsonObject2.put("value", value);
                        options.add(jsonObject2);
                    }
                }
            }
        }
        jsonObject.put("extraOptions", options);

        // Advanced panel settings:
        jsonObject.put("supportBooleanDataType", databaseMeta.supportsBooleanDataType());
        jsonObject.put("supportTimestampDataType", databaseMeta.supportsTimestampDataType());
        jsonObject.put("quoteIdentifiersCheck", databaseMeta.isQuoteAllFields());
        jsonObject.put("lowerCaseIdentifiersCheck", databaseMeta.isForcingIdentifiersToLowerCase());
        jsonObject.put("upperCaseIdentifiersCheck", databaseMeta.isForcingIdentifiersToUpperCase());
        jsonObject.put("preserveReservedCaseCheck", databaseMeta.preserveReservedCase());
        jsonObject.put("preferredSchemaName", databaseMeta.getPreferredSchemaName());
        jsonObject.put("connectSQL", StringEscapeHelper.encode(databaseMeta.getConnectSQL()));

        // Cluster panel settings
        jsonObject.put("partitioned", databaseMeta.isPartitioned() ? "Y" : "N");

        JsonArray partitionInfo = new JsonArray();
        PartitionDatabaseMeta[] clusterInformation = databaseMeta.getPartitioningInformation();
        if (clusterInformation != null) {
            for (int i = 0; i < clusterInformation.length; i++) {
                PartitionDatabaseMeta meta = clusterInformation[i];
                JsonObject jsonObject2 = new JsonObject();
                jsonObject2.put("partitionId", meta.getPartitionId());
                jsonObject2.put("hostname", meta.getHostname());
                jsonObject2.put("port", meta.getPort());
                jsonObject2.put("databaseName", meta.getDatabaseName());
                jsonObject2.put("username", meta.getUsername());
                jsonObject2.put("password", meta.getPassword());

                partitionInfo.add(jsonObject2);
            }
        }
        jsonObject.put("partitionInfo", partitionInfo);

        // Pooling panel settings
        jsonObject.put("usingConnectionPool", databaseMeta.isUsingConnectionPool() ? "Y" : "N");
        jsonObject.put("initialPoolSize", databaseMeta.getInitialPoolSize());
        jsonObject.put("maximumPoolSize", databaseMeta.getMaximumPoolSize());
        Properties properties = databaseMeta.getConnectionPoolingProperties();
        JsonArray jsonArray2 = new JsonArray();
        for (DatabaseConnectionPoolParameter parameter : BaseDatabaseMeta.poolingParameters) {
            JsonObject jsonObject2 = new JsonObject();
            jsonObject2.put("enabled", properties.containsKey(parameter.getParameter()));
            jsonObject2.put("name", parameter.getParameter());
            jsonObject2.put("defValue", parameter.getDefaultValue());
            jsonObject2.put("description", StringEscapeHelper.encode(parameter.getDescription()));
            jsonArray2.add(jsonObject2);
        }

        jsonObject.put("pool_params", jsonArray2);
        jsonObject.put("read_only", databaseMeta.isReadOnly());

        return jsonObject;
    }

    public static DatabaseMeta decode(JsonObject jsonObject) throws KettleDatabaseException {
        DatabaseMeta databaseMeta = new DatabaseMeta();

        databaseMeta.setName(jsonObject.optString("name"));
        databaseMeta.setDisplayName(databaseMeta.getName());
        databaseMeta.setDatabaseType(jsonObject.optString("type"));
        //如果未选择连接方式会报错
        if (jsonObject.get("access") instanceof List) {
            throw new RuntimeException("请选择连接方式");
        }
        databaseMeta.setAccessType(jsonObject.optInt("access"));

        if (jsonObject.containsKey("hostname")) {
            databaseMeta.setHostname(jsonObject.optString("hostname"));
        }
        if (jsonObject.containsKey("databaseName")) {
            databaseMeta.setDBName(jsonObject.optString("databaseName"));
        }
        if (jsonObject.containsKey("username")) {
            databaseMeta.setUsername(jsonObject.optString("username"));
        }
        if (jsonObject.containsKey("password")) {
            databaseMeta.setPassword(jsonObject.optString("password"));
        }
        // infobright-jndi
        if (jsonObject.containsKey("streamingResults")) {
            databaseMeta.setStreamingResults(true);
        }
        //oracle-jndi
        if (jsonObject.containsKey("dataTablespace")) {
            databaseMeta.setDataTablespace(jsonObject.optString("dataTablespace"));
        }
        //oracle-jndi
        if (jsonObject.containsKey("indexTablespace")) {
            databaseMeta.setIndexTablespace(jsonObject.optString("indexTablespace"));
        }
        //mssql-native
        if (jsonObject.containsKey("sqlServerInstance")) {
            databaseMeta.setSQLServerInstance(jsonObject.optString("sqlServerInstance"));
        }
        //mssql-jndi
        if (jsonObject.containsKey("usingDoubleDecimalAsSchemaTableSeparator")) {
            databaseMeta.setUsingDoubleDecimalAsSchemaTableSeparator(jsonObject.optBoolean("usingDoubleDecimalAsSchemaTableSeparator"));
        }

        // SAP Attributes...
        if (jsonObject.containsKey(SAPR3DatabaseMeta.ATTRIBUTE_SAP_LANGUAGE)) {
            databaseMeta.getAttributes().put(SAPR3DatabaseMeta.ATTRIBUTE_SAP_LANGUAGE, jsonObject.optString(SAPR3DatabaseMeta.ATTRIBUTE_SAP_LANGUAGE));
        }
        if (jsonObject.containsKey(SAPR3DatabaseMeta.ATTRIBUTE_SAP_SYSTEM_NUMBER)) {
            databaseMeta.getAttributes().put(SAPR3DatabaseMeta.ATTRIBUTE_SAP_SYSTEM_NUMBER, jsonObject.optString(SAPR3DatabaseMeta.ATTRIBUTE_SAP_SYSTEM_NUMBER));
        }
        if (jsonObject.containsKey(SAPR3DatabaseMeta.ATTRIBUTE_SAP_CLIENT)) {
            databaseMeta.getAttributes().put(SAPR3DatabaseMeta.ATTRIBUTE_SAP_CLIENT, jsonObject.optString(SAPR3DatabaseMeta.ATTRIBUTE_SAP_CLIENT));
        }

        // Generic settings...
        if (jsonObject.containsKey(GenericDatabaseMeta.ATRRIBUTE_CUSTOM_URL)) {
            databaseMeta.getAttributes().put(GenericDatabaseMeta.ATRRIBUTE_CUSTOM_URL, jsonObject.optString(GenericDatabaseMeta.ATRRIBUTE_CUSTOM_URL));
        }
        if (jsonObject.containsKey(GenericDatabaseMeta.ATRRIBUTE_CUSTOM_DRIVER_CLASS)) {
            databaseMeta.getAttributes().put(GenericDatabaseMeta.ATRRIBUTE_CUSTOM_DRIVER_CLASS, jsonObject.optString(GenericDatabaseMeta.ATRRIBUTE_CUSTOM_DRIVER_CLASS));
        }

        // Server Name: (Informix)
        if (jsonObject.containsKey("servername")) {
            databaseMeta.setServername(jsonObject.optString("servername"));
        }

        // Microsoft SQL Server Use Integrated Security
        if (jsonObject.containsKey(MSSQLServerNativeDatabaseMeta.ATTRIBUTE_USE_INTEGRATED_SECURITY)) {
            boolean flag = jsonObject.optBoolean(MSSQLServerNativeDatabaseMeta.ATTRIBUTE_USE_INTEGRATED_SECURITY);
            if (flag) {
                databaseMeta.getAttributes().put(MSSQLServerNativeDatabaseMeta.ATTRIBUTE_USE_INTEGRATED_SECURITY, flag);
            }
        }

        if (jsonObject.containsKey("port")) {
            databaseMeta.setDBPort(jsonObject.optString("port"));
        }
        // Option parameters:

        JsonArray options = jsonObject.optJSONArray("extraOptions");
        if (options != null) {
            for (int i = 0; i < options.size(); i++) {
                JsonObject jsonObject2 = options.getJSONObject(i);
                String parameter = jsonObject2.optString("name");
                String value = jsonObject2.optString("value");

                if (value == null) {
                    value = "";
                }

                if ((parameter != null) && (parameter.trim().length() > 0)) {
                    if (value.trim().length() <= 0) {
                        value = DatabaseMeta.EMPTY_OPTIONS_STRING;
                    }

                    databaseMeta.addExtraOption(databaseMeta.getPluginId(), parameter, value);
                }
            }
        }

        // Advanced panel settings:

        if (jsonObject.containsKey("supportBooleanDataType")) {
            databaseMeta.setSupportsBooleanDataType(jsonObject.optBoolean("supportBooleanDataType"));
        }

        if (jsonObject.containsKey("supportTimestampDataType")) {
            databaseMeta.setSupportsTimestampDataType(jsonObject.optBoolean("supportTimestampDataType"));
        }

        if (jsonObject.containsKey("quoteIdentifiersCheck")) {
            databaseMeta.setQuoteAllFields(jsonObject.optBoolean("quoteIdentifiersCheck"));
        }

        if (jsonObject.containsKey("lowerCaseIdentifiersCheck")) {
            databaseMeta.setForcingIdentifiersToLowerCase(jsonObject.optBoolean("lowerCaseIdentifiersCheck"));
        }

        if (jsonObject.containsKey("upperCaseIdentifiersCheck")) {
            databaseMeta.setForcingIdentifiersToUpperCase(jsonObject.optBoolean("upperCaseIdentifiersCheck"));
        }

        if (jsonObject.containsKey("preserveReservedCaseCheck")) {
            databaseMeta.setPreserveReservedCase(jsonObject.optBoolean("preserveReservedCaseCheck"));
        }

        if (jsonObject.containsKey("preferredSchemaName")) {
            databaseMeta.setPreferredSchemaName(jsonObject.optString("preferredSchemaName"));
        }

        if (jsonObject.containsKey("connectSQL")) {
            databaseMeta.setConnectSQL(StringEscapeHelper.decode(jsonObject.optString("connectSQL")));
        }

        // Cluster panel settings
        databaseMeta.setPartitioned("Y".equalsIgnoreCase(jsonObject.optString("partitioned")));
        if ("Y".equalsIgnoreCase(jsonObject.optString("partitioned"))) {
            JsonArray partitionInfo = jsonObject.optJSONArray("partitionInfo");
            if (partitionInfo != null) {
                ArrayList<PartitionDatabaseMeta> list = new ArrayList<PartitionDatabaseMeta>();
                for (int i = 0; i < partitionInfo.size(); i++) {
                    JsonObject jsonObject2 = partitionInfo.getJSONObject(i);
                    PartitionDatabaseMeta meta = new PartitionDatabaseMeta();

                    String partitionId = jsonObject2.optString("partitionId");
                    if ((partitionId == null) || (partitionId.trim().length() <= 0)) {
                        continue;
                    }

                    meta.setPartitionId(jsonObject2.optString("partitionId"));
                    meta.setHostname(jsonObject2.optString("hostname"));
                    meta.setPort(jsonObject2.optString("port"));
                    meta.setDatabaseName(jsonObject2.optString("databaseName"));
                    meta.setUsername(jsonObject2.optString("username"));
                    meta.setPassword(jsonObject2.optString("password"));
                    list.add(meta);
                }
                if (list.size() > 0) {
                    databaseMeta.setPartitioningInformation(list.toArray(new PartitionDatabaseMeta[list.size()]));
                }
            }
        }

        if ("Y".equalsIgnoreCase(jsonObject.optString("usingConnectionPool"))) {
            databaseMeta.setUsingConnectionPool(true);

            try {
                databaseMeta.setInitialPoolSize(jsonObject.optInt("initialPoolSize"));
            } catch (Exception e) {
            }

            try {
                databaseMeta.setMaximumPoolSize(jsonObject.optInt("maximumPoolSize"));
            } catch (Exception e) {
            }

            JsonArray pool_params = jsonObject.optJSONArray("pool_params");
            if (pool_params != null) {
                Properties properties = new Properties();
                for (int i = 0; i < pool_params.size(); i++) {
                    JsonObject jsonObject2 = pool_params.getJSONObject(i);
                    Boolean enabled = jsonObject2.optBoolean("enabled");
                    String parameter = jsonObject2.optString("name");
                    String value = jsonObject2.optString("defValue");

                    if (!enabled) {
                        continue;
                    }

                    if (StringUtils.hasText(parameter) && StringUtils.hasText(value)) {
                        properties.setProperty(parameter, value);
                    }
                }
                databaseMeta.setConnectionPoolingProperties(properties);
            }
        }

        databaseMeta.setReadOnly(jsonObject.optBoolean("read_only"));
        return databaseMeta;
    }

}
