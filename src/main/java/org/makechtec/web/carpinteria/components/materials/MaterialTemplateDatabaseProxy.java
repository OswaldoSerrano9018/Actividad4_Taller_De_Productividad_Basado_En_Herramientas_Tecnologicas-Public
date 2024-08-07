package org.makechtec.web.carpinteria.components.materials;

import org.makechtec.software.sql_support.ConnectionInformation;
import org.makechtec.software.sql_support.mysql.MysqlEngine;
import org.makechtec.software.sql_support.query_process.statement.ParamType;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class MaterialTemplateDatabaseProxy {

    private final ConnectionInformation connectionInformation;

    public MaterialTemplateDatabaseProxy(ConnectionInformation connectionInformation) {
        this.connectionInformation = connectionInformation;
    }

    public void register(MaterialTemplateRegisterRequest template){
        (new MysqlEngine<Void>(connectionInformation))
                .queryString("INSERT INTO material_templates(name, unit, unit_price, quantity) VALUES(?,?,?, 0);")
                .isPrepared()
                .addParamAtPosition(1, template.name(), ParamType.TYPE_STRING)
                .addParamAtPosition(2, template.unit(), ParamType.TYPE_STRING)
                .addParamAtPosition(3, template.unitPrice(), ParamType.TYPE_DOUBLE)
                .update();
    }

    public MaterialTemplateModelView byId(String id){

        return
            (new MysqlEngine<MaterialTemplateModelView>(connectionInformation))
                    .queryString("SELECT id, name, unit, unit_price, quantity FROM material_templates WHERE id = ? LIMIT 1;")
                    .isPrepared()
                    .addParamAtPosition(1, id, ParamType.TYPE_STRING)
                    .run(resultSet -> {

                        resultSet.next();

                        return new MaterialTemplateModelView(
                                String.valueOf(resultSet.getInt("id")),
                                resultSet.getString("name"),
                                resultSet.getString("unit"),
                                resultSet.getDouble("unit_price"),
                                resultSet.getInt("quantity")
                        );
                    });

    }

    public Set<MaterialTemplateRow> all(){

        var result = new HashSet<MaterialTemplateRow>();


        (new MysqlEngine<Void>(connectionInformation))
                .queryString("SELECT id, name FROM material_templates;")
                .run(resultSet -> {

                    while(resultSet.next()){

                        result.add(new MaterialTemplateRow(
                            String.valueOf(resultSet.getInt("id")),
                            resultSet.getString("name")
                        ));
                    }

                    return null;
                });

        return result;
    }


}
