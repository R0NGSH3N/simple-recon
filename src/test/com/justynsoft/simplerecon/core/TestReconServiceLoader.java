package com.justynsoft.simplerecon.core;


import com.justynsoft.simplerecon.core.dao.ReconServiceDAO;
import com.justynsoft.simplerecon.core.dao.ReconWorkerEntityDAO;
import com.justynsoft.simplerecon.core.dao.ReconWorkerPairDAO;
import com.justynsoft.simplerecon.core.service.ReconService;
import com.justynsoft.simplerecon.core.service.ReconServiceLoader;
import com.justynsoft.simplerecon.core.worker.ReconWorker;
import com.justynsoft.simplerecon.core.worker.ReconWorkerEntity;
import com.justynsoft.simplerecon.core.worker.ReconWorkerPair;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TestReconServiceLoader {
    @Autowired
    ReconServiceDAO reconsServiceDAO;
    @Autowired
    ReconWorkerEntityDAO reconWorkerEntityDAO;
    @Autowired
    ReconWorkerPairDAO reconWorkerPairDAO;
    @Autowired
    ReconServiceLoader serviceLoader;

    private Long reconServiceId;

    @Before
    public void setUp() {
        //prepare recon service
        ReconService testService = new ReconService();
        testService.setName("TestService");
        testService.setDescription("unit test for recon service");
        testService.setCreateDate(new Date());
        testService.setReportHandlerClassName("com.justynsoft.simplerecon.core.FileReportHandler");
        testService = reconsServiceDAO.save(testService);
        this.reconServiceId = testService.getId();

        //prepare the recon worker entity
        ReconWorkerEntity databaseWorker = new ReconWorkerEntity();
        databaseWorker.setTargetObjectClassName("com.justynsoft.simplerecon.traderecon.TradeAllocation");
        databaseWorker.setBeanName("TradeAllocationDatabaseReconWorker");
        databaseWorker.setName("TestDatabaseReconWorker");
        databaseWorker.setReconServiceId(testService.getId());
        databaseWorker = reconWorkerEntityDAO.save(databaseWorker);

        ReconWorkerEntity fileWorker = new ReconWorkerEntity();
        fileWorker.setBeanName("TradeAllocationCSVReconWorker");
        fileWorker.setTargetObjectClassName("com.justynsoft.simplerecon.traderecon.TradeAllocation");
        fileWorker.setName("TestFileReconWorker");
        fileWorker.setReconServiceId(testService.getId());
        fileWorker = reconWorkerEntityDAO.save(fileWorker);

        //setup pair
        ReconWorkerPair pair = new ReconWorkerPair();
        pair.setReconServiceId(testService.getId());
        pair.setPairType(ReconWorkerPair.PAIR_TYPE.EXACT);
        pair.setPrimaryReconWorkerId(databaseWorker.getId());
        pair.setSecondaryReconWorkerId(fileWorker.getId());
        reconWorkerPairDAO.save(pair);

    }

    @Test
    public void testLoadReconServices() {
        serviceLoader.loadReconServices();
        ReconService testService = serviceLoader.getServiceMap().get(this.reconServiceId);
        assertTrue(testService.getName().equals("TestService"));
        assertTrue(testService.getDescription().equals("unit test for recon service"));
        assertTrue(testService.getReportHandler().getClass().getName().contains("FileReportHandler"));
        assertTrue(testService.getCreateDate() != null);

        List<ReconWorker> reconWorkerList = testService.getWorkers();
        assertTrue(reconWorkerList != null && reconWorkerList.size() == 2);
    }
}
