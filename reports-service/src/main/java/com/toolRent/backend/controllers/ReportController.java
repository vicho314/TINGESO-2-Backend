package com.toolRent.backend.controllers;

import com.toolRent.backend.dtos.ClientDelayDTO;
import com.toolRent.backend.dtos.LendingDTO;
import com.toolRent.backend.dtos.ToolRankingDTO;
import com.toolRent.backend.services.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/reports")
public class ReportController {

    @Autowired
    private ReportService reportService;

    /**
     * M6 - #22: List active lendings and their state (current, late)
     * @param startDate Optional start date for filtering
     * @param endDate Optional end date for filtering
     */
    @GetMapping("/active-lendings")
    public List<LendingDTO> getActiveLendings(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {
        
        LocalDate start = startDate != null ? LocalDate.parse(startDate) : LocalDate.now().minusMonths(1);
        LocalDate end = endDate != null ? LocalDate.parse(endDate) : LocalDate.now();
        
        return reportService.getActiveLendingsReport(start, end);
    }

    /**
     * M6 - #23: List clients with delays
     * @param startDate Optional start date for filtering
     * @param endDate Optional end date for filtering
     */
    @GetMapping("/clients-with-delays")
    public List<ClientDelayDTO> getClientsWithDelays(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {
        
        LocalDate start = startDate != null ? LocalDate.parse(startDate) : LocalDate.now().minusMonths(1);
        LocalDate end = endDate != null ? LocalDate.parse(endDate) : LocalDate.now();
        
        return reportService.getClientsWithDelaysReport(start, end);
    }

    /**
     * M6 - #24: Report of most lended tools (Ranking)
     * @param startDate Optional start date for filtering
     * @param endDate Optional end date for filtering
     */
    @GetMapping("/most-lended-tools")
    public List<ToolRankingDTO> getMostLendedTools(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {
        
        LocalDate start = startDate != null ? LocalDate.parse(startDate) : LocalDate.now().minusMonths(1);
        LocalDate end = endDate != null ? LocalDate.parse(endDate) : LocalDate.now();
        
        return reportService.getMostLendedToolsReport(start, end);
    }
}
