package com.justynsoft.simplerecon.core;

import com.justynsoft.simplerecon.core.reports.ReconReport;
import com.justynsoft.simplerecon.core.reports.ReconReportHanlder;

public class FileReportHandler implements ReconReportHanlder{

    private String reportToString;

    public String getReportToString() {
        return reportToString;
    }

    public void setReportToString(String reportToString) {
        this.reportToString = reportToString;
    }

    @Override
    public void handleReport(ReconReport report) {
        this.reportToString = report.toString();
    }
}
