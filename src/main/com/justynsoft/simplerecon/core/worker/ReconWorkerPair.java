package com.justynsoft.simplerecon.core.worker;

import com.justynsoft.simplerecon.core.reports.ReconReport;

import javax.persistence.*;

@Entity
@Table(name="recon_pair_config")
public class ReconWorkerPair {

    public enum PAIR_TYPE{
        EXACT, PRIMARY;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long Id;

    @Column(name="recon_service_id")
    private long reconServiceId;

    @Column(name="primary_recon_worker_id")
    private long primaryReconWorkerId;

    @Column(name="secondary_recon_worker_id")
    private long secondaryReconWorkerId;

    @Column(name="pair_type")
    private PAIR_TYPE pairType;

    @Transient
    private Boolean isPrimaryWorkerReady = false;
    @Transient
    private Boolean isSecondaryWorkerReady = false;

    @Transient
    private ReconReport report;

    public long getId() {
        return Id;
    }

    public void setId(long id) {
        Id = id;
    }

    public Boolean getPrimaryWorkerReady() {
        return isPrimaryWorkerReady;
    }

    public void setPrimaryWorkerReady(Boolean primaryWorkerReady) {
        isPrimaryWorkerReady = primaryWorkerReady;
    }

    public Boolean getSecondaryWorkerReady() {
        return isSecondaryWorkerReady;
    }

    public void setSecondaryWorkerReady(Boolean secondaryWorkerReady) {
        isSecondaryWorkerReady = secondaryWorkerReady;
    }

    public long getReconServiceId() {
        return reconServiceId;
    }

    public void setReconServiceId(long reconServiceId) {
        this.reconServiceId = reconServiceId;
    }

    public Long getPrimaryReconWorkerId() {
        return primaryReconWorkerId;
    }

    public void setPrimaryReconWorkerId(long primaryReconWorkerId) {
        this.primaryReconWorkerId = primaryReconWorkerId;
    }

    public Long getSecondaryReconWorkerId() {
        return secondaryReconWorkerId;
    }

    public void setSecondaryReconWorkerId(long secondaryReconWorkerId) {
        this.secondaryReconWorkerId = secondaryReconWorkerId;
    }

    public PAIR_TYPE getPairType() {
        return pairType;
    }

    public void setPairType(PAIR_TYPE pairType) {
        this.pairType = pairType;
    }

    public ReconReport getReport() {
        return report;
    }

    public void setReport(ReconReport report) {
        this.report = report;
    }
}
