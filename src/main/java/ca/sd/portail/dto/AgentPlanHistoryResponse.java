package ca.sd.portail.dto;

import java.util.List;

/**
 * DTO de réponse paginée pour l'historique des plans d'agents.
 */
public class AgentPlanHistoryResponse {

    private final int page;
    private final int size;
    private final long totalElements;
    private final int totalPages;
    private final List<AgentPlanResponse> content;

    public AgentPlanHistoryResponse(int page, int size, long totalElements,
                                     int totalPages, List<AgentPlanResponse> content) {
        this.page = page;
        this.size = size;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.content = content;
    }

    public int getPage() { return page; }
    public int getSize() { return size; }
    public long getTotalElements() { return totalElements; }
    public int getTotalPages() { return totalPages; }
    public List<AgentPlanResponse> getContent() { return content; }
}
