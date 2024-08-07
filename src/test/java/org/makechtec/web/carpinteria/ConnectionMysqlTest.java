package org.makechtec.web.carpinteria;

import org.junit.jupiter.api.Test;
import org.makechtec.software.sql_support.ConnectionInformation;
import org.makechtec.software.sql_support.mysql.MysqlEngine;

public class ConnectionMysqlTest {

    @Test
    public void testConnection() {
        (new MysqlEngine<Void>(new ConnectionInformation(
                "root",
                "zZKj1Ug36Ksk",
                "localhost",
                "3306",
                "carpinteria"
        )))
                .queryString("SELECT * FROM boms;")
                .run(resultSet -> {
                    while(resultSet.next()) {
                        System.out.println(resultSet.getString("id"));
                    }
                    return null;
                });
    }

}
