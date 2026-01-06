package com.toolRent.backend.services;

import com.toolRent.backend.clients.ClientServiceClient;
import com.toolRent.backend.clients.LendServiceClient;
import com.toolRent.backend.clients.ToolServiceClient;
import com.toolRent.backend.dtos.ClientDelayDTO;
import com.toolRent.backend.dtos.LendingDTO;
import com.toolRent.backend.dtos.ToolRankingDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReportService {

    @Autowired
    private LendServiceClient lendServiceClient;

    @Autowired
    private ClientServiceClient clientServiceClient;

    @Autowired
    private ToolServiceClient toolServiceClient;

    /**
     * M6 - Reports: List active lendings and their state (current, late)
     */
    public List<LendingDTO> getActiveLendingsReport(LocalDate startDate, LocalDate endDate) {
        try {
            List<LendingDTO> lendings = lendServiceClient.getLendingsByDateRange(startDate, endDate);
            
            // Enrich with tool names
            return lendings.stream().map(lend -> {
                try {
                    var tool = toolServiceClient.getToolById(lend.getToolId());
                    lend.setToolName(tool.name);
                } catch (Exception e) {
                    lend.setToolName("N/A");
                }
                
                // Determine if late
                if (lend.getReturnDay() != null && LocalDate.now().isAfter(lend.getReturnDay())) {
                    lend.setState("LATE");
                    lend.setDaysLate(LocalDate.now().getDayOfYear() - lend.getReturnDay().getDayOfYear());
                } else {
                    lend.setState("ACTIVE");
                }
                
                return lend;
            }).collect(Collectors.toList());
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    /**
     * M6 - Reports: List clients with delays
     */
    public List<ClientDelayDTO> getClientsWithDelaysReport(LocalDate startDate, LocalDate endDate) {
        try {
            Map<Long, ClientDelayDTO> clientDelays = new HashMap<>();
            
            // Get all late lendings
            List<LendingDTO> lateLendings = lendServiceClient.getLateLendings();
            
            for (LendingDTO lend : lateLendings) {
                clientDelays.putIfAbsent(lend.getClientId(), new ClientDelayDTO());
                
                ClientDelayDTO dto = clientDelays.get(lend.getClientId());
                dto.setClientId(lend.getClientId());
                dto.setDelayedLendings(dto.getDelayedLendings() + 1);
                
                if (lend.getDaysLate() != null) {
                    dto.setTotalDaysLate((dto.getTotalDaysLate() == null ? 0L : dto.getTotalDaysLate()) + lend.getDaysLate());
                }
            }
            
            // Enrich with client info
            return clientDelays.values().stream().map(dto -> {
                try {
                    var client = clientServiceClient.getClientById(dto.getClientId());
                    dto.setClientName(client.name);
                    dto.setClientDNI(client.rut);
                    dto.setClientState(client.state);
                } catch (Exception e) {
                    dto.setClientName("N/A");
                }
                return dto;
            }).collect(Collectors.toList());
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    /**
     * M6 - Reports: Report of most lended tools (Ranking)
     */
    public List<ToolRankingDTO> getMostLendedToolsReport(LocalDate startDate, LocalDate endDate) {
        try {
            Map<Long, Long> toolLendingCount = new HashMap<>();
            
            // Get all lendings in date range
            List<LendingDTO> lendings = lendServiceClient.getLendingsByDateRange(startDate, endDate);
            
            // Count lendings by tool
            for (LendingDTO lend : lendings) {
                toolLendingCount.put(
                    lend.getToolId(),
                    toolLendingCount.getOrDefault(lend.getToolId(), 0L) + 1
                );
            }
            
            // Get all tools and enrich with lending counts
            List<ToolRankingDTO> ranking = new ArrayList<>();
            
            toolLendingCount.forEach((toolId, count) -> {
                try {
                    var tool = toolServiceClient.getToolById(toolId);
                    ToolRankingDTO dto = new ToolRankingDTO();
                    dto.setToolId(toolId);
                    dto.setToolName(tool.name);
                    dto.setToolCategory(tool.category);
                    dto.setTotalLendings(count);
                    ranking.add(dto);
                } catch (Exception e) {
                    // Skip tool if not found
                }
            });
            
            // Sort by lending count descending and assign ranks
            ranking.sort((a, b) -> Long.compare(b.getTotalLendings(), a.getTotalLendings()));
            
            for (int i = 0; i < ranking.size(); i++) {
                ranking.get(i).setRank((long) i + 1);
            }
            
            return ranking;
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
}
