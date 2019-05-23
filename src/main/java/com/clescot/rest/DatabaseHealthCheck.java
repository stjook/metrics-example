package com.clescot.rest;


import com.codahale.metrics.health.HealthCheck;

import javax.inject.Inject;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseHealthCheck extends HealthCheck {

    private DataSource datasource;

    @Inject
    public DatabaseHealthCheck(DataSource datasource) {
        this.datasource = datasource;
    }

    @Override
    protected Result check() throws SQLException {

        HealthCheck.Result result;
        try (Connection connection = datasource.getConnection()) {
            Statement statement;
            ResultSet resultSet;
            statement = connection.createStatement();
            resultSet = statement.executeQuery("select 1 from dual");
            if (resultSet.next()) {
                result = Result.healthy("HEALTHY");
            } else {
                result = Result.unhealthy("UNHEALTHY");
            }
        } catch (Throwable t) {
            result = HealthCheck.Result.unhealthy("UNHEALTHY", t);
        }

        return result;
    }

}

