package com.justynsoft.simplerecon.core;


import com.justynsoft.simplerecon.core.dao.ReconServiceDAO;
import com.justynsoft.simplerecon.core.dao.ReconWorkerEntityDAO;
import com.justynsoft.simplerecon.core.dao.ReconWorkerPairDAO;
import com.justynsoft.simplerecon.core.service.ReconService;
import com.justynsoft.simplerecon.core.service.ReconServiceLoader;
import com.justynsoft.simplerecon.core.worker.*;
import com.justynsoft.simplerecon.traderecon.TradeAllocation;
import com.justynsoft.simplerecon.traderecon.TradeAllocationDAO;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TestReconWorkersManager implements ApplicationEventPublisherAware{


    @Autowired
    private ReconWorkersManager workersManager;
    private ApplicationEventPublisher publisher;

    @Autowired
    ReconServiceDAO reconsServiceDAO;
    @Autowired
    ReconWorkerEntityDAO reconWorkerEntityDAO;
    @Autowired
    ReconWorkerPairDAO reconWorkerPairDAO;
    @Autowired
    ReconServiceLoader serviceLoader;
    @Autowired
    TradeAllocationDAO tradeAllocationDAO;


    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
            this.publisher = applicationEventPublisher;
    }

    @Before
    public void setUp() {
        //prepare recon service
        ReconService testService = new ReconService();
        testService.setName("TestService");
        testService.setDescription("unit test for recon service");
        testService.setCreateDate(new Date());
        testService.setReportHandlerClassName("com.justynsoft.simplerecon.core.FileReportHandler");
        testService = reconsServiceDAO.save(testService);

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

        //setup test data for database recon worker
        TradeAllocation allocation = new TradeAllocation();
        allocation.setBlockId("1234");
        allocation.setNetAmt(new BigDecimal(1.0));
        allocation.setPortfolioId("10W");
        allocation.setQuantity(new BigDecimal(1.0));
        tradeAllocationDAO.save(allocation);

        allocation = new TradeAllocation();
        allocation.setBlockId("2345");
        allocation.setNetAmt(new BigDecimal(2.0));
        allocation.setQuantity(new BigDecimal(1.0));
        tradeAllocationDAO.save(allocation);

        allocation = new TradeAllocation();
        allocation.setBlockId("1234");
        allocation.setNetAmt(new BigDecimal(3.0));
        allocation.setQuantity(new BigDecimal(1.0));
        tradeAllocationDAO.save(allocation);
    }

    @Test
    public void test(){
        serviceLoader.loadReconServices();
        ReconWorkerStartEvent startEvent = new ReconWorkerStartEvent(this);
        //database recon worker
        startEvent.setReconWorkerId(1L);
        publisher.publishEvent(startEvent);

        ReconWorker reconWorker = workersManager.getReconWorkerByWorkerId(1L);
        List<ReconWokerLog> history = reconWorker.getLoadHistory();
        assertTrue(history.size() == 1);
        ReconWokerLog log = history.get(0);
        assertTrue(log.getId() == 1);
        assertTrue(log.getReconServiceId() == 1);
        assertTrue(log.getReconWorkerId() == 1);
        assertTrue(log.getNumberOfRow() == 3);

    }
}
