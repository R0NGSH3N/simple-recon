package com.justynsoft.simplerecon.traderecon;

import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;

@Transactional
public interface TradeAllocationDAO extends CrudRepository<TradeAllocation, Long>{
}
