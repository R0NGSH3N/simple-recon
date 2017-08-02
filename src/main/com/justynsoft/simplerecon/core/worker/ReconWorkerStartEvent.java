package com.justynsoft.simplerecon.core.worker;

import org.springframework.context.ApplicationEvent;

public class ReconWorkerStartEvent<T> extends ApplicationEvent {
    public Long reconWorkerId;

    public Long getReconWorkerId() {
        return reconWorkerId;
    }

    public void setReconWorkerId(Long reconWorkerId) {
        this.reconWorkerId = reconWorkerId;
    }

    public ReconWorkerStartEvent(Object source){
        super(source);
    }

}
