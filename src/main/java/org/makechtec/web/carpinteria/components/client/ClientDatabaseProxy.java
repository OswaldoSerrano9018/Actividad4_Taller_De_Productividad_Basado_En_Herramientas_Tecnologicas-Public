package org.makechtec.web.carpinteria.components.client;

import org.makechtec.software.sql_support.ConnectionInformation;
import org.makechtec.software.sql_support.mysql.MysqlEngine;
import org.makechtec.software.sql_support.query_process.statement.ParamType;

import java.util.HashSet;
import java.util.Set;

public class ClientDatabaseProxy {

    private final ConnectionInformation connectionInformation;

    public ClientDatabaseProxy(ConnectionInformation connectionInformation) {
        this.connectionInformation = connectionInformation;
    }

    public Set<ClientRow> all(){

        var result = new HashSet<ClientRow>();

        (new MysqlEngine<Void>(connectionInformation))
                .queryString("SELECT id, name FROM clients;")
                .run(resultSet -> {
                    while (resultSet.next()) {
                        result.add(new ClientRow(
                                resultSet.getString("name"),
                                resultSet.getString("id")
                        ));
                    }

                    return null;
                });

        return result;
    }

    public ClientModelView byId(String id) {

        return
                (new MysqlEngine<ClientModelView>(connectionInformation))
                        .isPrepared()
                        .queryString("SELECT id, name, contact FROM clients WHERE id = ?;")
                        .addParamAtPosition(1, Integer.parseInt(id), ParamType.TYPE_INTEGER)
                        .run(resultSet -> {

                            resultSet.next();

                            return new ClientModelView(
                                    resultSet.getString("name"),
                                    resultSet.getString("id"),
                                    resultSet.getString("contact")
                            );
                        });
    }

    public void register(ClientRegisterRequest request){

        (new MysqlEngine<Void>(connectionInformation))
                .queryString("INSERT INTO clients(name, contact) VALUES(?, ?);")
                .isPrepared()
                .addParamAtPosition(1, request.name(), ParamType.TYPE_STRING)
                .addParamAtPosition(2, request.contact(), ParamType.TYPE_STRING)
                .update();

    }

}
