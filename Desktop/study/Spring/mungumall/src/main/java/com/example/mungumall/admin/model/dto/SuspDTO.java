package com.example.mungumall.admin.model.dto;

import com.example.mungumall.member.model.dto.MemberDTO;
import lombok.Data;

import java.util.Date;

@Data
public class SuspDTO {

    private int suspNo;
    private MemberDTO memberId;
    private int accSuspCount;
    private String accSuspDesc;
    private Date accSuspDate;
}
