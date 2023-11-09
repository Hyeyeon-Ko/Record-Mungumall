package com.example.mungumall.admin.model.service;

import com.example.mungumall.member.model.dto.MemberDTO;

import java.util.List;

public interface AdminService {

    List<MemberDTO> getMemberList();

    MemberDTO getMemberDetails(String memberId);

    int getTotalNumber();

    int getRegularNumber();

    int getAdminNumber();
}
