package com.example.mungumall.member.model.service;

import com.example.mungumall.config.MungumallApplication;
import com.example.mungumall.member.model.dto.MemberDTO;
import com.example.mungumall.member.model.dto.RoleDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ContextConfiguration(classes = {MungumallApplication.class})
public class MemberServiceTests {

    @Autowired
    public MemberService memberService;

    @Test
    @DisplayName("서비스 인터페이스 의존성 주입 테스트")
    public void testInit() {
        assertNotNull(memberService);
        /* 1. service 폴더 하위 MemberService 인터페이스 생성
         * 2. 같은 폴더 하위에 실제 구현체인 MemberServiceImpl 클래스 생성
         */
    }

    @Test
    @DisplayName("신규 회원 등록용 서비스 성공 테스트")
    public void testSignUpMember() throws Exception {

        //given
        MemberDTO member = new MemberDTO();
        member.setMemberId("user01");
        member.setMemberPwd("pass01");
        member.setName("김회원");
        member.setPhone("01000010001");
        member.setEmail("test@reminder.com");
        member.setAddress("서울특별시");
        member.setAgreement('Y');

        RoleDTO role = new RoleDTO();
        role.setMemberId(member.getMemberId());
        role.setAuthorityCode(1);

        //when
        boolean result = memberService.signUpMember(member);

        //then
        assertTrue(result);
    }
}
