package com.justynsoft.simplerecon.core.service;

import com.google.common.collect.Lists;
import com.justynsoft.simplerecon.core.dao.ReconServiceDAO;
import com.justynsoft.simplerecon.core.dao.ReconWorkerEntityDAO;
import com.justynsoft.simplerecon.core.dao.ReconWorkerPairDAO;
import com.justynsoft.simplerecon.core.reports.ReconReportHanlder;
import com.justynsoft.simplerecon.core.worker.ReconWorker;
import com.justynsoft.simplerecon.core.worker.ReconWorkerEntity;
import com.justynsoft.simplerecon.core.worker.ReconWorkerPair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReconServiceLoader {
    private static final Logger logger = LoggerFactory.getLogger(ReconServiceLoader.class);
    @Autowired
    ReconServiceDAO reconServiceDAO;
    @Autowired
    ReconWorkerEntityDAO reconWorkerEntityDAO;
    @Autowired
    ReconWorkerPairDAO reconWorkerPairDAO;
    @Autowired
    private ApplicationContext context;
    private Map<Long, ReconService> serviceMap;
    private Map<Long, ReconWorker> reconWorkerMap;

    public Map<Long, ReconService> getServiceMap() {
        return serviceMap;
    }

    public void setServiceMap(Map<Long, ReconService> serviceMap) {
        this.serviceMap = serviceMap;
    }

    public Map<Long, ReconWorker> getReconWorkerMap() {
        return reconWorkerMap;
    }

    public void setReconWorkerMap(Map<Long, ReconWorker> reconWorkerMap) {
        this.reconWorkerMap = reconWorkerMap;
    }

    @PostConstruct
    public void loadReconServices() {
        logger.info("loading services...");

        //first load all the recon services from the database
        if (this.serviceMap == null) {
            serviceMap = new HashMap<>();
        }
        reconServiceDAO.findAll().forEach(reconService -> {
                    serviceMap.put(reconService.getId(), reconService);
                }
        );
        //second load all the recon workers pair
        Map<Long, List<ReconWorkerPair>> reconWorkerPairMap = new HashMap<>();
        reconWorkerPairDAO.findAll().forEach(reconWorkerPair -> {
            List<ReconWorkerPair> reconWorkerPairList = reconWorkerPairMap.get(reconWorkerPair.getReconServiceId());
            if (reconWorkerPairList == null) {
                reconWorkerPairList = new ArrayList<>();
                reconWorkerPairMap.put(reconWorkerPair.getReconServiceId(), reconWorkerPairList);
            }
            reconWorkerPairList.add(reconWorkerPair);
        });

        //assign reconWorkerPairs to Recon Service & init cache
        for (Map.Entry<Long, ReconService> entry : serviceMap.entrySet()) {
            entry.getValue().setReconWorkerPairs(reconWorkerPairMap.get(entry.getKey()));
            entry.getValue().initCache();
            try {
                Class classDef = Class.forName(entry.getValue().getReportHandlerClassName());
                ReconReportHanlder reportHandler = (ReconReportHanlder) classDef.newInstance();
                entry.getValue().setReportHandler(reportHandler);
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
                e.printStackTrace();

            }
        }

        //Instantiate all the workers
        List<ReconWorkerEntity> reconWorkerEntityList = Lists.newArrayList(reconWorkerEntityDAO.findAll());
        reconWorkerEntityList.forEach(reconWorkerEntity -> {
            try {

                /**
                 * Hybrid mode:
                 * if entity has bean name, then call the bean, otherwise instantiate the object from
                 * class name.
                 */
                ReconWorker worker = null;
                if (reconWorkerEntity.getBeanName() != null) {
                    worker = (ReconWorker) context.getBean(reconWorkerEntity.getBeanName());
                } else {
                    Class classDef = Class.forName(reconWorkerEntity.getClassName());
                    worker = (ReconWorker) classDef.newInstance();
                }
                worker.setEntity(reconWorkerEntity);
                Class targetObjClass = Class.forName(reconWorkerEntity.getTargetObjectClassName());
                worker.setClazz(targetObjClass);
                //initial
                if (this.reconWorkerMap == null) {
                    reconWorkerMap = new HashMap<>();
                }
                this.reconWorkerMap.put(worker.getId(), worker);
                ReconService currenReconService = serviceMap.get(worker.getReconServiceId());
                worker.setReconService(currenReconService);
                currenReconService.getWorkers().add(worker);
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        });
    }
}
