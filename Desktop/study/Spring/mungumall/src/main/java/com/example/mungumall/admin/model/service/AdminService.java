package com.example.mungumall.admin.model.service;

import com.example.mungumall.member.model.dto.MemberDTO;

import java.util.List;

public interface AdminService {

    List<MemberDTO> getMemberList();

    MemberDTO getMemberDetails(String memberId);

    int getTotalNumber();

    int getRegularNumber();

    int getAdminNumber();

    int getClosedNumber();

    List<MemberDTO> getMemberOnly();

    List<MemberDTO> getAdminOnly();

    List<MemberDTO> getClosedOnly();

    int searchAuthById(String memberId);

    int deleteAuthAsAdmin(String memberId);

    int insertAuthAsAdmin(String memberId);

    int updateAccSuspension(String memberId);

    int insertAccSuspension(String memberId, String accSuspDesc);

    int updateAccActivation(String memberId);
}
