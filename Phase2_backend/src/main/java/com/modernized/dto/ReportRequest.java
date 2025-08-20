package com.modernized.dto;

import jakarta.validation.constraints.*;

public class ReportRequest {
    
    @NotBlank(message = "Report type cannot be empty")
    @Pattern(regexp = "MONTHLY|YEARLY|CUSTOM", message = "Report type must be MONTHLY, YEARLY, or CUSTOM")
    private String reportType;

    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "Start date must be in YYYY-MM-DD format")
    private String startDate;

    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "End date must be in YYYY-MM-DD format")
    private String endDate;

    @NotBlank(message = "Confirmation is required")
    @Pattern(regexp = "[YN]", message = "Confirmation must be Y or N")
    private String confirmation;

    public ReportRequest() {}

    public String getReportType() { return reportType; }
    public void setReportType(String reportType) { this.reportType = reportType; }

    public String getStartDate() { return startDate; }
    public void setStartDate(String startDate) { this.startDate = startDate; }

    public String getEndDate() { return endDate; }
    public void setEndDate(String endDate) { this.endDate = endDate; }

    public String getConfirmation() { return confirmation; }
    public void setConfirmation(String confirmation) { this.confirmation = confirmation; }
}
