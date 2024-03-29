package com.example.mungumall.member.model.dao;

import com.example.mungumall.member.model.dto.MemberDTO;
import com.example.mungumall.member.model.dto.RoleDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MemberMapper {

    /* 회원가입 */
    int checkId(String memberId);                //아이디 중복 검사
    int checkEmail(String email);                //이메일 중복 검사
    int insertMember(MemberDTO member);          //회원 정보 등록
    int insertRole(RoleDTO role);                //회원 권한 등록
    MemberDTO findMemberById(String username);

    /* 로그인 */
    void updateFailCountReset(String username);	//성공 시 로그인 실패 횟수 리셋

    void updateFailCount(String username);		//실패 시 로그인 실패 횟수 증가

    int checkLoginFailureCount(String username);//로그인 실패 횟수 조회

    void deactivateUsername(String username);
}
