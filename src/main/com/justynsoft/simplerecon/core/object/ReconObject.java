package com.justynsoft.simplerecon.core.object;

import org.apache.commons.csv.CSVRecord;

import javax.persistence.MappedSuperclass;
import java.sql.ResultSet;
import java.sql.SQLException;

@MappedSuperclass
public abstract class ReconObject implements Comparable<ReconObject> {

    public ReconObject(ResultSet rs) throws SQLException{ }

    public ReconObject(CSVRecord record){ }

    public ReconObject(){}
}
