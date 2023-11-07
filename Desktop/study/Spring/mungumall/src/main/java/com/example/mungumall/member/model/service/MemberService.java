package com.example.mungumall.member.model.service;


import com.example.mungumall.member.model.dto.MemberDTO;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * Spring Security 모듈 UserDetailsService 상속 받아 로그인/로그아웃 로직 처리
 */
public interface MemberService extends UserDetailsService {

    int checkId(String memberId);

    int checkEmail(String email);

    boolean signUpMember(MemberDTO member) throws Exception;

}
