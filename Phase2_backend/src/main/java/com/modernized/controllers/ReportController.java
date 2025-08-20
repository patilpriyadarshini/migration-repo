package com.modernized.controllers;

import com.modernized.dto.ReportRequest;
import com.modernized.dto.ReportResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * Transaction Reports Controller
 * Handles report generation based on SCREEN-013 (Transaction Reports)
 * Generates transaction reports with flexible date range options
 */
@RestController
@RequestMapping("/api/reports")
public class ReportController {

    /**
     * Generate Monthly Report
     * POST /api/reports/monthly
     * 
     * Generates monthly transaction report for current month.
     * Based on SCREEN-013 (Transaction Reports) monthly option.
     * 
     * @param reportRequest Report generation confirmation
     * @return ReportResponse with job submission details
     */
    @PostMapping("/monthly")
    public ResponseEntity<ReportResponse> generateMonthlyReport(@Valid @RequestBody ReportRequest reportRequest) {
        if (!"MONTHLY".equals(reportRequest.getReportType())) {
            throw new IllegalArgumentException("Invalid report type for monthly report");
        }
        
        if (!"Y".equals(reportRequest.getConfirmation())) {
            return ResponseEntity.ok(new ReportResponse(
                "MONTHLY", null, null, null, "CANCELLED", "Report not submitted"
            ));
        }
        
        LocalDate now = LocalDate.now();
        String startDate = now.withDayOfMonth(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String endDate = now.withDayOfMonth(now.lengthOfMonth()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String jobId = "MONTHLY-" + UUID.randomUUID().toString().substring(0, 8);
        
        return ResponseEntity.ok(new ReportResponse(
            "MONTHLY", startDate, endDate, jobId, "SUBMITTED", "Monthly report job submitted successfully"
        ));
    }

    /**
     * Generate Yearly Report
     * POST /api/reports/yearly
     * 
     * Generates yearly transaction report for current year.
     * Based on SCREEN-013 (Transaction Reports) yearly option.
     * 
     * @param reportRequest Report generation confirmation
     * @return ReportResponse with job submission details
     */
    @PostMapping("/yearly")
    public ResponseEntity<ReportResponse> generateYearlyReport(@Valid @RequestBody ReportRequest reportRequest) {
        if (!"YEARLY".equals(reportRequest.getReportType())) {
            throw new IllegalArgumentException("Invalid report type for yearly report");
        }
        
        if (!"Y".equals(reportRequest.getConfirmation())) {
            return ResponseEntity.ok(new ReportResponse(
                "YEARLY", null, null, null, "CANCELLED", "Report not submitted"
            ));
        }
        
        LocalDate now = LocalDate.now();
        String startDate = now.withDayOfYear(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String endDate = now.withDayOfYear(now.lengthOfYear()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String jobId = "YEARLY-" + UUID.randomUUID().toString().substring(0, 8);
        
        return ResponseEntity.ok(new ReportResponse(
            "YEARLY", startDate, endDate, jobId, "SUBMITTED", "Yearly report job submitted successfully"
        ));
    }

    /**
     * Generate Custom Date Range Report
     * POST /api/reports/custom
     * 
     * Generates transaction report for custom date range.
     * Based on SCREEN-013 (Transaction Reports) custom option.
     * 
     * @param reportRequest Report generation data with custom date range
     * @return ReportResponse with job submission details
     */
    @PostMapping("/custom")
    public ResponseEntity<ReportResponse> generateCustomReport(@Valid @RequestBody ReportRequest reportRequest) {
        if (!"CUSTOM".equals(reportRequest.getReportType())) {
            throw new IllegalArgumentException("Invalid report type for custom report");
        }
        
        if (reportRequest.getStartDate() == null || reportRequest.getEndDate() == null) {
            throw new IllegalArgumentException("Start date and end date are required for custom reports");
        }
        
        if (!"Y".equals(reportRequest.getConfirmation())) {
            return ResponseEntity.ok(new ReportResponse(
                "CUSTOM", reportRequest.getStartDate(), reportRequest.getEndDate(), 
                null, "CANCELLED", "Report not submitted"
            ));
        }
        
        try {
            LocalDate startDate = LocalDate.parse(reportRequest.getStartDate());
            LocalDate endDate = LocalDate.parse(reportRequest.getEndDate());
            
            if (startDate.isAfter(endDate)) {
                throw new IllegalArgumentException("Start date must be before or equal to end date");
            }
            
            String jobId = "CUSTOM-" + UUID.randomUUID().toString().substring(0, 8);
            
            return ResponseEntity.ok(new ReportResponse(
                "CUSTOM", reportRequest.getStartDate(), reportRequest.getEndDate(), 
                jobId, "SUBMITTED", "Custom report job submitted successfully"
            ));
            
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid date format. Use YYYY-MM-DD format");
        }
    }
}
