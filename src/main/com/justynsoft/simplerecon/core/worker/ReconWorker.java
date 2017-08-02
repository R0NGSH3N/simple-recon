package com.justynsoft.simplerecon.core.worker;

import com.justynsoft.simplerecon.core.dao.ReconWorkerLogDAO;
import com.justynsoft.simplerecon.core.object.ReconObject;
import com.justynsoft.simplerecon.core.service.ReconService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;

import java.util.Date;
import java.util.List;

public abstract class ReconWorker<T extends ReconObject> {
    private ReconWorkerEntity entity;
    private ReconService reconService;
    List<ReconObject> reconObjectList;
    @Autowired
    private ReconWorkerLogDAO workerLogDAO;
    private Class<T> clazz;
    private ReconService.STATE state = ReconService.STATE.PENDING;

    @Required
    public void setClazz(Class<T> clazz) {
        this.clazz = clazz;
    }

    public Class<T> getClazz() {
        return clazz;
    }

    public void startLoad(ReconWorkerStartEvent event) {
        if(state == ReconService.STATE.STARTED){
            throw new RuntimeException(" Worker is in loading data, you need either wait for completed or reset the service");
        }else{
            state = ReconService.STATE.STARTED;
        }
        List<T> result = load(event);
        reconService.addLoadedData(getId(), result);

        ReconWokerLog log = new ReconWokerLog(reconService.getId(), this.getId(), new Date(), result.size());
        workerLogDAO.save(log);
    }

    public List<ReconWokerLog> getLoadHistory(){
        return workerLogDAO.findByReconWorkerId(this.getId());
    }

    public ReconService getReconService() {
        return reconService;
    }

    public void setReconService(ReconService reconService) {
        this.reconService = reconService;
    }

    abstract List load(ReconWorkerStartEvent event);

    public ReconWorkerEntity getEntity() {
        return entity;
    }

    public void setEntity(ReconWorkerEntity entity) {
        //validation check
        if(entity.getReconServiceId() == 0L || entity.getId() == 0L ){
            throw new RuntimeException("The Recon Service Id or Entity Id is empty");
        }
        this.entity = entity;

    }

    public Long getReconServiceId() {
        return this.entity.getReconServiceId();
    }

    public Long getId() {
        return this.entity.getId();
    }
}
