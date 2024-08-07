package org.makechtec.web.carpinteria.components.bom;

import org.makechtec.software.sql_support.ConnectionInformation;
import org.makechtec.software.sql_support.mysql.MysqlEngine;
import org.makechtec.software.sql_support.query_process.statement.ParamType;
import org.makechtec.web.carpinteria.components.materials.MaterialProduct;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class BOMDatabaseProxy {

    private final ConnectionInformation connectionInformation;

    public BOMDatabaseProxy(ConnectionInformation connectionInformation) {
        this.connectionInformation = connectionInformation;
    }

    public Set<BomPreview> all(){

        var response = new HashSet<BomPreview>();

        (new MysqlEngine<Void>(connectionInformation))
                .queryString("SELECT id FROM boms ORDER BY id;")
                .run(resultSet -> {
                    while (resultSet.next()) {
                        response.add(new BomPreview(resultSet.getString("id")));
                    }

                    return null;
                });

        return response;
    }

    public Set<AttachedMaterial> allAttachedMaterialsForBOM(String bomId){

        var response = new HashSet<AttachedMaterial>();

        (new MysqlEngine<Void>(connectionInformation))
                .queryString("""
                        SELECT b.bom_id, b.material_id, b.quantity, m.name
                        FROM bom_materials AS b
                        INNER JOIN material_templates AS m
                        ON b.material_id = m.id
                        WHERE bom_id = ?
                        ORDER BY b.material_id""")
                .isPrepared()
                .addParamAtPosition(1, Integer.parseInt(bomId), ParamType.TYPE_INTEGER)
                .run(resultSet -> {
                    while(resultSet.next()){
                        response.add(new AttachedMaterial(
                                resultSet.getString("b.bom_id"),
                                resultSet.getString("b.material_id"),
                                resultSet.getString("m.name"),
                                resultSet.getInt("b.quantity")
                        ));
                    }

                    return null;
                });

        return response;
    }

    public String create(){
        try {
            return String.valueOf(
                (new MysqlEngine<Void>(connectionInformation))
                        .queryString("INSERT INTO boms() VALUES();")
                        .isPrepared()
                        .updateWithGeneratedKey(resultSet -> {
                            resultSet.next();
                            return resultSet.getLong(1);
                        }));
        } catch (SQLException | IllegalAccessException | InstantiationException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void attachMaterialProductToBOM(String materialProductId, String bomId, int cantidad){
        (new MysqlEngine<Void>(connectionInformation))
                .queryString("INSERT INTO bom_materials(material_id, bom_id, quantity) VALUES(?, ?, ?);")
                .isPrepared()
                .addParamAtPosition(1, Integer.valueOf(materialProductId), ParamType.TYPE_INTEGER)
                .addParamAtPosition(2, Integer.valueOf(bomId), ParamType.TYPE_INTEGER)
                .addParamAtPosition(3, cantidad, ParamType.TYPE_INTEGER)
                .update();
    }

}
