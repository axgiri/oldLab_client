package github.oldLab.oldLab.controller;

import github.oldLab.oldLab.Enum.ReportStatusEnum;
import github.oldLab.oldLab.dto.request.ReportRequest;
import github.oldLab.oldLab.dto.response.ReportResponse;
import github.oldLab.oldLab.service.ReportService;
import github.oldLab.oldLab.serviceImpl.RateLimiterServiceImpl;
import io.github.bucket4j.Bucket;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import jakarta.servlet.http.HttpServletRequest;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/reports")
public class ReportController {

    private final ReportService service;
    private final RateLimiterServiceImpl rateLimiterService;

    @PostMapping("/create")
    public ResponseEntity<Void> createReport(
<<<<<<< Updated upstream
            @RequestBody ReportRequest request) {
=======
            @RequestBody ReportRequest request,
            HttpServletRequest httpRequest) {
        log.debug("creating report: {}", request);
>>>>>>> Stashed changes
        service.createReport(request);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<ReportResponse>> getAllReports(
            @RequestParam(defaultValue = "0") int page,
<<<<<<< Updated upstream
            @RequestParam(defaultValue = "20") int size) {
=======
            @RequestParam(defaultValue = "20") int size,
            HttpServletRequest httpRequest) {
        log.debug("getting all reports page: {}, size: {}", page, size);
>>>>>>> Stashed changes
        List<ReportResponse> responses = service.getAllReports(page, size);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<ReportResponse>> getReportsByStatus(
            @PathVariable ReportStatusEnum status,
            @RequestParam(defaultValue = "0") int page,
<<<<<<< Updated upstream
            @RequestParam(defaultValue = "20") int size) {
=======
            @RequestParam(defaultValue = "20") int size,
            HttpServletRequest httpRequest) {
        log.debug("getting reports by status: {} page: {}, size: {}", status, page, size);
>>>>>>> Stashed changes
        List<ReportResponse> responses = service.getReportsByStatus(status, page, size);
        return ResponseEntity.ok(responses);
    }

    @PatchMapping("/{reportId}/status")

    public ResponseEntity<Void> updateReportStatus(
            @PathVariable Long reportId,
            @RequestParam ReportStatusEnum status,
<<<<<<< Updated upstream
            @RequestHeader("X-Moderator-Id") Long moderatorId) {
=======
            @RequestHeader("X-Moderator-Id") Long moderatorId,
            HttpServletRequest httpRequest) {
        log.debug("updating report {} status to {} by moderator {}", reportId, status, moderatorId);
>>>>>>> Stashed changes
        service.updateReportStatus(reportId, status, moderatorId);
        return ResponseEntity.ok().build();
    }
}
