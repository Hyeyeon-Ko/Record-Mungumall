package com.example.mungumall.paging.model.dto;

import lombok.Data;
import org.springframework.web.util.UriComponentsBuilder;

@Data
public class Criteria {

    private int currentPageNo;
    private int recordsPerPage;
    private String category;
    private String keyword;

    public Criteria() {
        this(1, 10);
    }

    public Criteria(int currentPageNo, int recordsPerPage) {
        this.currentPageNo = currentPageNo;
        this.recordsPerPage = recordsPerPage;
    }

    public String getListLink() {
        UriComponentsBuilder builder = UriComponentsBuilder.fromPath("")
                .queryParam("currentPageNo", currentPageNo)
                .queryParam("recordsPerPage", recordsPerPage);
        return builder.toUriString();
    }
}
