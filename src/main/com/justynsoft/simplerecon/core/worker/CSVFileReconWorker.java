package com.justynsoft.simplerecon.core.worker;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class CSVFileReconWorker<T> extends ReconWorker{

    private String fileName;
    private Reader reader;

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setReader(Reader reader) {
        this.reader = reader;
    }

    @Override
    public List<T> load(ReconWorkerStartEvent event) {
        List<T> listTrades = new ArrayList<>();
        Class[] classArgs = new Class[1];
        classArgs[0] = CSVRecord.class;
        Iterable<CSVRecord> records = null;
        try {
            if(this.reader!= null){
                 records = CSVFormat.EXCEL.withHeader().parse(reader);
            }else{
                Reader in = new FileReader(fileName);
                records = CSVFormat.EXCEL.parse(in);
            }
            records.forEach( csvRecord ->{
                try {
                    listTrades.add((T)getClazz().getDeclaredConstructor(classArgs).newInstance(csvRecord));
                } catch (InstantiationException |IllegalAccessException | NoSuchMethodException |InvocationTargetException e) {
                    //TODO need to throw or handler exception
                    e.printStackTrace();
                }
            });
        } catch (IOException e) {
            //TODO need to throw or handler exception
            e.printStackTrace();
        }

        return listTrades;
    }
}
