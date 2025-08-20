package com.modernized.dto;

public class ReportResponse {
    private String reportType;
    private String startDate;
    private String endDate;
    private String jobId;
    private String status;
    private String message;

    public ReportResponse() {}

    public ReportResponse(String reportType, String startDate, String endDate, 
                         String jobId, String status, String message) {
        this.reportType = reportType;
        this.startDate = startDate;
        this.endDate = endDate;
        this.jobId = jobId;
        this.status = status;
        this.message = message;
    }

    public String getReportType() { return reportType; }
    public void setReportType(String reportType) { this.reportType = reportType; }

    public String getStartDate() { return startDate; }
    public void setStartDate(String startDate) { this.startDate = startDate; }

    public String getEndDate() { return endDate; }
    public void setEndDate(String endDate) { this.endDate = endDate; }

    public String getJobId() { return jobId; }
    public void setJobId(String jobId) { this.jobId = jobId; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}
