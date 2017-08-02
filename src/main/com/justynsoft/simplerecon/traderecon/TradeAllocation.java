package com.justynsoft.simplerecon.traderecon;

import com.justynsoft.simplerecon.core.object.ReconObject;
import org.apache.commons.csv.CSVRecord;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

@Entity
@Table( name = "allocation")
public class TradeAllocation extends ReconObject{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long Id;
    @Column(name = "block_id")
    private String BlockId;
    @Column (name = "portfolio_id")
    private String portfolioId;
    @Column (name = "net_amt")
    private BigDecimal netAmt;
    @Column (name = "quantity")
    private BigDecimal quantity;

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getBlockId() {
        return BlockId;
    }

    public void setBlockId(String blockId) {
        BlockId = blockId;
    }

    public String getPortfolioId() {
        return portfolioId;
    }

    public void setPortfolioId(String portfolioId) {
        this.portfolioId = portfolioId;
    }

    public BigDecimal getNetAmt() {
        return netAmt;
    }

    public void setNetAmt(BigDecimal netAmt) {
        this.netAmt = netAmt;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    @Override
    public int compareTo(ReconObject o) {
        if(o == null  || !(o instanceof TradeAllocation)){
           return -1;
        }else{
            TradeAllocation ta = (TradeAllocation)o;
            if(ta.getId() == this.getId()){
                return 0;
            }else{
                return 1;
            }
        }
    }

    public TradeAllocation(ResultSet rs) throws SQLException{
        super(rs);
       this.Id = rs.getLong("id");
       this.BlockId = rs.getString("block_id");
       this.portfolioId = rs.getString("portfolio_id");
       this.netAmt = rs.getBigDecimal("net_amt");
       this.quantity = rs.getBigDecimal("quantity");
    }

    public TradeAllocation(CSVRecord csvRecord){
        super(csvRecord);
        this.Id = Long.parseLong(csvRecord.get("id"));
        this.BlockId = csvRecord.get("block_id");
        this.portfolioId = csvRecord.get("portfolio_id");
        this.netAmt = new BigDecimal(csvRecord.get("net_amt"));
        this.quantity = new BigDecimal(csvRecord.get("quantity"));
    }

    public TradeAllocation(){

    }

}

