package com.example.mungumall.member.model.service;

import com.example.mungumall.member.model.dao.MemberMapper;
import com.example.mungumall.member.model.dto.AuthorityDTO;
import com.example.mungumall.member.model.dto.MemberDTO;
import com.example.mungumall.member.model.dto.RoleDTO;
import com.example.mungumall.member.model.dto.UserImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Transactional
@Service("memberService")
public class MemberServiceImpl implements MemberService {

    private MemberMapper memberMapper;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public MemberServiceImpl(MemberMapper memberMapper, PasswordEncoder passwordEncoder) {
        this.memberMapper = memberMapper;
        this.passwordEncoder = passwordEncoder;
        /* [PasswordEncoder]
         * 1. pom.xml 통해 spring-boot-starter-security 의존성 주입
         * 2. ContextConfiguration에서 BCryptPasswordEncoder에 대해 Bean 등록
         */
    }

    /**
     * 아이디 중복 검사
     * @return 중복된 아이디 개수
     */
    @Transactional(readOnly = true)
    @Override
    public int checkId(String memberId) {
        int count = memberMapper.checkId(memberId);
        return count;
    }

    /**
     * 이메일 중복 검사
     * @return 중복된 이메일 개수
     */
    @Transactional(readOnly = true)
    @Override
    public int checkEmail(String email) {
        int count = memberMapper.checkEmail(email);
        return count;
    }

    /**
     * 회원가입
     */
    @Override
    public boolean signUpMember(MemberDTO member) throws Exception {

        member.setMemberPwd(passwordEncoder.encode(member.getMemberPwd()));

        int resultA = memberMapper.insertMember(member);

        RoleDTO role = new RoleDTO();
        role.setMemberId(member.getMemberId());
        role.setAuthorityCode(1); //일반회원
        int resultB = memberMapper.insertRole(role);

        if(resultA <= 0 && resultB <= 0) {
            throw new Exception("신규 회원 등록에 실패");
        }

        return resultA > 0 && resultB > 0 ? true : false;

    }

    /**
     * 로그인
     * UserImpl 통해 id, pwd, authorities 이외에 추가 정보 호출
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        MemberDTO member = memberMapper.findMemberById(username); //회원 조회

        if(member == null) member = new MemberDTO();			  //null인 경우 빈 객체 반환하여 대체

        List<GrantedAuthority> authorities = new ArrayList<>();	  //권한 리스트

        if(member != null && member.getRoleList() != null) {

            for(RoleDTO role : member.getRoleList()) {
                AuthorityDTO authority = role.getAuthority();

                if(authority != null) {
                    authorities.add(new SimpleGrantedAuthority(authority.getAuthName())); //권한이름 전달
                }
            }
        }

        UserImpl user = new UserImpl(member.getMemberId(), member.getMemberPwd(), authorities);
        user.setDetails(member);

        return user;
    }

    @Override
    public void updateFailCountReset(String username) {
        memberMapper.updateFailCountReset(username);
    }

    @Override
    public void updateFailCount(String username) {
        memberMapper.updateFailCount(username);
    }

    @Override
    public int checkLoginFailureCount(String username) {
        int count = memberMapper.checkLoginFailureCount(username);
        return count;
    }

    @Override
    public void deactivateUsername(String username) {
        memberMapper.deactivateUsername(username);
    }
}