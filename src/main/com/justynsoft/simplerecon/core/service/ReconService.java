package com.justynsoft.simplerecon.core.service;

import com.justynsoft.simplerecon.core.object.ReconObject;
import com.justynsoft.simplerecon.core.reports.ReconReport;
import com.justynsoft.simplerecon.core.reports.ReconReportHanlder;
import com.justynsoft.simplerecon.core.worker.ReconWorker;
import com.justynsoft.simplerecon.core.worker.ReconWorkerPair;
import org.springframework.scheduling.annotation.Async;

import javax.persistence.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Entity
@Table(name = "recon_services")
public class ReconService<T extends ReconObject> {

    public enum STATE {
        PENDING, STARTED, COMPLETED;
    }


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(name = "name")
    private String name;
    @Column(name = "create_datetime")
    private Date createDate;
    @Column(name = "description")
    private String description;
    @Column(name = "method_type")
    private String methodType;
    @Column(name = "report_handler")
    private String reportHandlerClassName;

    @Transient
    private ReconReportHanlder reportHandler;

    @Transient
    private STATE state = STATE.PENDING;

    public STATE getState() {
        return state;
    }

    public void setState(STATE state) {
        this.state = state;
    }

    @Transient
    private List<ReconWorker> workers;
    @Transient
    private List<ReconWorkerPair> reconWorkerPairs;

    //TODO need change this to standard spring cache
    @Transient
    private ConcurrentHashMap<Long, List<T>> dataCache;

    @Transient
    private List<ReconReport> resultCache = new ArrayList<>();

    public List<ReconReport> getResults() {
        return resultCache;
    }

    public void setResultCache(List<ReconReport> resultCache) {
        this.resultCache = resultCache;
    }

    public List<ReconWorkerPair> getReconWorkerPairs() {
        return reconWorkerPairs;
    }

    public void setReconWorkerPairs(List<ReconWorkerPair> reconWorkerPairs) {
        this.reconWorkerPairs = reconWorkerPairs;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getReportHandlerClassName() {
        return reportHandlerClassName;
    }

    public void setReportHandlerClassName(String reportHandlerClassName) {
        this.reportHandlerClassName = reportHandlerClassName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMethodType() {
        return methodType;
    }

    public void setMethodType(String methodType) {
        this.methodType = methodType;
    }

    public List<ReconWorker> getWorkers() {
        if (this.workers == null) {
            workers = new ArrayList<>();
        }
        return workers;
    }

    public ReconReportHanlder getReportHandler() {
        return reportHandler;
    }

    public void setReportHandler(ReconReportHanlder reportHandler) {
        this.reportHandler = reportHandler;
    }

    public void setWorkers(List<ReconWorker> workers) {
        this.workers = workers;
    }

    public ConcurrentHashMap getDataCache() {
        return dataCache;
    }

    public void setDataCache(ConcurrentHashMap dataCache) {
        this.dataCache = dataCache;
    }

    public void initCache() {
        this.dataCache = new ConcurrentHashMap();
    }

    public void addLoadedData(Long reconWorkerId, List<T> result) {
        this.dataCache.put(reconWorkerId, result);
        notifyWorkerReady(reconWorkerId);
    }

    public ReconReport primaryMatch(List<T> primary, List<T> secondary) {
        ReconReport report = new ReconReport();
        if (primary == null && secondary == null) {
            report.setErrorMessage("Both Data sets are NULL.");
            report.setMatch(false);
            return report;
        }

        if (primary == null || primary.size() == 0) {
            report.setErrorMessage("Primary Data set is NULL.");
            report.setMatch(false);
            return report;
        }

        if (secondary == null || secondary.size() == 0) {
            report.setErrorMessage("Primary Data set is NULL.");
            report.setMatch(false);
            return report;
        }

        List primaryClone = new ArrayList<ReconObject>(primary);
        List secondaryClone = new ArrayList<ReconObject>(secondary);
        Collections.sort(primaryClone);
        Collections.sort(secondaryClone);

        primaryClone.removeAll(secondaryClone);
        if (primaryClone.size() == 0) {
            report.setMatch(true);
        } else {
            report.setMatch(false);
            report.setPrimaryList(primaryClone);
        }

        return report;
    }

    public ReconReport exactMatch(List<T> primary, List<T> secondary) {
        ReconReport report = new ReconReport();
        if (primary == null && secondary == null) {
            report.setErrorMessage("Both Data sets are NULL.");
            report.setMatch(false);
            return report;
        }

        if (primary == null || primary.size() == 0) {
            report.setErrorMessage("Primary Data set is NULL.");
            report.setMatch(false);
            return report;

        }

        if (secondary == null || secondary.size() == 0) {
            report.setErrorMessage("Primary Data set is NULL.");
            report.setMatch(false);
            return report;

        }

        List primaryClone = new ArrayList<ReconObject>(primary);
        List secondaryClone = new ArrayList<ReconObject>(secondary);
        Collections.sort(primaryClone);
        Collections.sort(secondaryClone);

        primaryClone.removeAll(secondaryClone);

        //reset primary
        primaryClone = new ArrayList<ReconObject>(primary);
        Collections.sort(primaryClone);
        secondaryClone.removeAll(primaryClone);

        /**
         * if both set are empty, then it is true, otherwise,
         * return no empty set
         */
        if (primaryClone.size() == 0 && secondaryClone.size() == 0) {
            report.setMatch(true);
        } else {
            if (primaryClone.size() != 0) {
                report.setMatch(false);
                report.setPrimaryList(primaryClone);
            }
            if (secondaryClone.size() != 0) {
                report.setMatch(false);
                report.setSecondaryList(secondaryClone);
            }
        }

        return report;
    }

    @Async
    public void recon(ReconWorkerPair reconWorkerPair) {
        List<T> primarySideData = this.dataCache.get(reconWorkerPair.getPrimaryReconWorkerId());
        List<T> secondarySideData = this.dataCache.get(reconWorkerPair.getSecondaryReconWorkerId());
        if (reconWorkerPair.getPairType() == ReconWorkerPair.PAIR_TYPE.EXACT) {
            ReconReport result = exactMatch(primarySideData, secondarySideData);
            result.setPrimaryReconWorkerId(reconWorkerPair.getPrimaryReconWorkerId());
            result.setSecondaryReconWorkerId(reconWorkerPair.getSecondaryReconWorkerId());
            this.resultCache.add(result);
        } else {
            ReconReport result = primaryMatch(primarySideData, secondarySideData);
            result.setPrimaryReconWorkerId(reconWorkerPair.getPrimaryReconWorkerId());
            result.setSecondaryReconWorkerId(reconWorkerPair.getSecondaryReconWorkerId());
            this.resultCache.add(result);
        }

        //check if all the results done
        if (resultCache.size() == reconWorkerPairs.size()) {
            this.state = STATE.COMPLETED;
            //TODO send result to email
        }
    }

    public void reset() {
        if (this.state == STATE.STARTED) {
            state = STATE.PENDING;
        }
        this.dataCache = new ConcurrentHashMap<>();
        this.resultCache = new ArrayList<>();
    }

    private void notifyWorkerReady(Long reconWorkerId) {

        if (state != STATE.STARTED) {
            state = STATE.STARTED;
        }

        //find all the pair that contain this worker.
        this.reconWorkerPairs.forEach(reconWorkerPair -> {
            if (reconWorkerPair.getPrimaryReconWorkerId().equals(reconWorkerId)) {
                reconWorkerPair.setPrimaryWorkerReady(true);
            } else if (reconWorkerPair.getSecondaryReconWorkerId().equals(reconWorkerId)) {
                reconWorkerPair.setSecondaryWorkerReady(true);
            }
            if (reconWorkerPair.getPrimaryWorkerReady() && reconWorkerPair.getSecondaryWorkerReady()) {
                recon(reconWorkerPair);
            }

        });
    }
}
