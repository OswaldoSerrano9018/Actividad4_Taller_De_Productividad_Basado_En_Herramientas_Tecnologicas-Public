package org.makechtec.web.carpinteria.components.product;

import org.makechtec.software.sql_support.ConnectionInformation;
import org.makechtec.software.sql_support.mysql.MysqlEngine;
import org.makechtec.software.sql_support.query_process.statement.ParamType;

import java.util.HashSet;
import java.util.Set;

public class ProductDatabaseProxy {

    private final ConnectionInformation connectionInformation;

    public ProductDatabaseProxy(ConnectionInformation connectionInformation) {
        this.connectionInformation = connectionInformation;
    }

    public Set<ProductRow> all(){

        var result = new HashSet<ProductRow>();

        (new MysqlEngine<Void>(connectionInformation))
                .queryString("SELECT id, name FROM products;")
                .run(resultSet -> {
                    while (resultSet.next()){
                        result.add(new ProductRow(resultSet.getString("id"), resultSet.getString("name")));
                    }

                    return null;
                });

        return result;
    }

    public ProductModelView byid(String id){
        return
                (new MysqlEngine<ProductModelView>(connectionInformation))
                        .isPrepared()
                        .queryString("SELECT id, name, bom_id, dimensions FROM products WHERE id = ?;")
                        .addParamAtPosition(1, id, ParamType.TYPE_STRING)
                        .run(resultSet -> {
                            resultSet.next();

                            return new ProductModelView(
                                    resultSet.getString("id"),
                                    resultSet.getString("name"),
                                    resultSet.getString("bom_id"),
                                    resultSet.getString("dimensions")
                            );

                        });
    }

    public void register(ProductRegisterRequest request){
        (new MysqlEngine<Void>(connectionInformation))
                .isPrepared()
                .queryString("INSERT INTO products (name, bom_id, dimensions) VALUES (?, ?, ?);")
                .addParamAtPosition(1, request.name(), ParamType.TYPE_STRING)
                .addParamAtPosition(2, request.bomId(), ParamType.TYPE_STRING)
                .addParamAtPosition(3, request.dimensions(), ParamType.TYPE_STRING)
                .update();
    }

}
