package com.justynsoft.simplerecon.core.dao;

import com.justynsoft.simplerecon.core.worker.ReconWokerLog;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
public interface ReconWorkerLogDAO extends CrudRepository<ReconWokerLog, Long>{
    public List<ReconWokerLog> findByReconWorkerId(Long reconWorkerId);
}
