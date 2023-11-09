package com.example.mungumall.admin.model.dto;

import com.example.mungumall.member.model.dto.MemberDTO;
import lombok.Data;

@Data
public class MemberSuspDTO {

    private MemberDTO member;
    private SuspDTO susp;
}
