package com.justynsoft.simplerecon.core;

import com.justynsoft.simplerecon.core.worker.CSVFileReconWorker;
import com.justynsoft.simplerecon.core.worker.ReconWorkerStartEvent;
import com.justynsoft.simplerecon.traderecon.TradeAllocation;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

import static junit.framework.TestCase.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TestCSVFileReconWorker {

    @Autowired
    private CSVFileReconWorker csvFileReconWorker;

    @Test
    public void test(){
        InputStream is = getClass().getResourceAsStream("data.csv");
        Reader reader = new InputStreamReader(is);
        csvFileReconWorker.setReader(reader);
        List<TradeAllocation> result = csvFileReconWorker.load(new ReconWorkerStartEvent(this));

        assertTrue(result.size() == 3);

        TradeAllocation firstAlloc = result.get(0);
        assertTrue(firstAlloc.getBlockId().equals("1234"));

        TradeAllocation secondAlloc = result.get(1);
        assertTrue(secondAlloc.getBlockId().equals("2345"));

        TradeAllocation thirdAlloc = result.get(2);
        assertTrue(thirdAlloc.getBlockId().equals("3456"));

    }
}
