package com.justynsoft.simplerecon.core.worker;

import com.justynsoft.simplerecon.core.service.ReconServiceLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;

public class ReconWorkersManager implements ApplicationListener<ReconWorkerStartEvent> {

    public ReconWorker getReconWorkerByWorkerId(Long workerId){
        return serviceLoader.getReconWorkerMap().get(workerId);
    }

    @Autowired
    private ReconServiceLoader serviceLoader;
    /**
     * @param reconWorkerStartEvent This method will be called when this recon worker is "external event driven"
     */
    @Override
    public void onApplicationEvent(ReconWorkerStartEvent reconWorkerStartEvent) {
        Long reconWorkerId = reconWorkerStartEvent.getReconWorkerId();
        if(reconWorkerId == null){
            throw new RuntimeException(" We need Recon Worker Id to start the Recon worker");
        }else{
            ReconWorker reconWorker = serviceLoader.getReconWorkerMap().get(reconWorkerId);
            reconWorker.startLoad(reconWorkerStartEvent);
        }

    }
}
