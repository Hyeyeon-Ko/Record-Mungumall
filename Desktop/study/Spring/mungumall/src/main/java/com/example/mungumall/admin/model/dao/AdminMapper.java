package com.example.mungumall.admin.model.dao;

import com.example.mungumall.member.model.dto.MemberDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AdminMapper {

    List<MemberDTO> getMemberList();

    MemberDTO getMemberDetails(String memberId);

    int getTotalNumber();

    int getRegularNumber();

    int getAdminNumber();

}
