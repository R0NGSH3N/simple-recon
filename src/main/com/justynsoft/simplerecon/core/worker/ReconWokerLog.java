package com.justynsoft.simplerecon.core.worker;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table( name = "recon_work_load_log")
public class ReconWokerLog {

    public ReconWokerLog( Long reconServiceId, Long reconWorkerId, Date createTime, Integer numberOfRow){
        this.reconServiceId = reconServiceId;
        this.reconWorkerId = reconWorkerId;
        this.createTime = createTime;
        this.numberOfRow = numberOfRow;
    }

    public ReconWokerLog(){}

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "recon_service_id")
    private Long reconServiceId;

    @Column(name="recon_worker_id")
    private Long reconWorkerId;

    @Column(name = "create_time")
    private Date createTime;

    @Column (name = " number_of_row")
    private Integer numberOfRow;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getReconServiceId() {
        return reconServiceId;
    }

    public void setReconServiceId(Long reconServiceId) {
        this.reconServiceId = reconServiceId;
    }

    public Long getReconWorkerId() {
        return reconWorkerId;
    }

    public void setReconWorkerId(Long reconWorkerId) {
        this.reconWorkerId = reconWorkerId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getNumberOfRow() {
        return numberOfRow;
    }

    public void setNumberOfRow(Integer numberOfRow) {
        this.numberOfRow = numberOfRow;
    }
}
