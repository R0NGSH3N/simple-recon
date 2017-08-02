package com.justynsoft.simplerecon.core.worker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class DatabaseReconWorker<T> extends ReconWorker{

    private JdbcTemplate jdbcTemplate;

    private String SQL;

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Required
    public void setSQL(String SQL) {
        this.SQL = SQL;
    }

    @Override
    public List load(ReconWorkerStartEvent event) {
        Class[] classArgs = new Class[1];
        classArgs[0] = ResultSet.class;
        return jdbcTemplate.query(this.SQL, new RowMapper<T>() {
            @Override
            public T mapRow(ResultSet resultSet, int i) throws SQLException{
                try {
                    return (T)getClazz().getDeclaredConstructor(classArgs).newInstance(resultSet);
                } catch (InstantiationException |IllegalAccessException | NoSuchMethodException |InvocationTargetException e) {
                    e.printStackTrace();
                }
                return null;
            }
        });
    }

}
