package com.example.mungumall.handler;

import java.io.IOException;

import com.example.mungumall.member.model.service.MemberService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.example.mungumall.member.model.dto.MemberDTO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    private String loginId;
    private MemberService memberService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        MemberDTO loginUserInfo = (MemberDTO) authentication.getPrincipal();
        String username = request.getParameter(loginId);

        memberService.updateFailCountReset(username);
        log.info("loginUserInfo");

    }

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

}