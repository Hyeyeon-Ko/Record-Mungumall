package com.example.mungumall.member.controller;

import com.example.mungumall.member.model.dto.MemberDTO;
import com.example.mungumall.member.model.dto.UserImpl;
import com.example.mungumall.member.model.service.MemberService;
import com.example.mungumall.validator.SignUpValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Locale;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;
    private final MessageSource messageSource;
    private final SignUpValidator signUpValidator;

    @Autowired
    public MemberController(MemberService memberService, MessageSource messageSource, SignUpValidator signUpValidator) {
        this.memberService = memberService;
        this.messageSource = messageSource;
        this.signUpValidator = signUpValidator;
        /* MessageSource
         * 1. ContextConfiguration 통해 Bean 등록
         * 2. classpath 하위에 messages 폴더 및 properties 파일 생성
         */
    }
    /**
     * 커스터마이징한 유효성 검증 추가
     */
    @InitBinder
    public void init(WebDataBinder binder) {
        log.info("init binder : {}", binder);
        binder.addValidators(signUpValidator);
    }

    /**
     * 아이디 중복 검사
     * @return 중복된 아이디 개수
     */
    @PostMapping(value="/checkid", produces="application/json; charset=UTF-8")
    @ResponseBody	//ajax 통신에서 JSON 응답을 치르기 위해서는 어노테이션 추가, spring-web의 경우 jackson-databind 등 기본적으로 추가돼 있는 의존성들을 고려하여야 함
    public int checkId(String memberId) {

        log.info("checkId 시작");
        log.info("전달 받은 ID : {}", memberId);
        int result = memberService.checkId(memberId);
        log.info("checkId 결과 : {}", result);

        return result;
    }

    /**
     * 이메일 중복 검사
     * @return 중복된 이메일 개수
     */
    @PostMapping(value="/checkEmail", produces="application/json; charset=UTF-8")
    @ResponseBody
    public int checkEmail(String email) {

        log.info("checkEmail 시작");
        log.info("전달 받은 Email : {}", email);
        int result = memberService.checkEmail(email);
        log.info("checkEmail 결과 : {}", result);

        return result;
    }

    @GetMapping("signup")
    public void signUpForm() {}

    @PostMapping("signup")
    public Object signUpMember(@Validated @ModelAttribute("member") MemberDTO member, BindingResult bindingResult,
                               @RequestParam Map<String, String> params, RedirectAttributes rttr, Model model, Locale locale) throws Exception { //BindingResult는 유효성 검사 대상 객체 바로 뒤에 선언

        /*
        signUpValidator.validate(member, bindingResult);

        if(bindingResult.hasErrors()) { //검증 오류 발생 시 입력 폼으로 리턴
            log.info("검증 오류 발생 : {}", bindingResult);
            return "signup";
        }
        */

        /* 회원가입 성공 로직 */
        String phone = params.get("phoneA") + params.get("phoneB") + params.get("phoneC");
        String address = params.get("postalCode") + "$" + params.get("address") + "$" + params.get("detailAddress");
        member.setPhone(phone);
        member.setAddress(address);
        memberService.signUpMember(member);

        /* 리다이렉트 시에는 요청이 새로 생겨나는 것이므로 RedirectAttributes 사용
         * 메시지 목록을 리터럴리하게 쓰지 않고 따로 목록화 해서 관리되도록 ContextConfiguration에 bean으로 등록
         * 상단에 MessageSource 타입에 대하여 의존성 주입
         *
         * 결과적으로 request scope 안에 successMessage가 담김
         * alert를 띄우기 위해서는 main html 안에 작성
         * */
        rttr.addFlashAttribute("successMessage", messageSource.getMessage("signUpMember", null, locale));

        log.info("성공 로직 실행 완료");

        return "redirect:/";
    }

    @GetMapping("signin")
    public void signInForm() {}

    @PostMapping("signin")
    public void signInMember(@RequestParam(required=false) String errorMessage, Model model) {
        model.addAttribute("errorMessage", errorMessage);
    }

    @GetMapping("mypage")
    public void mypage(@AuthenticationPrincipal UserImpl user) {
        //로그인 된 객체를 UserImpl 타입의 데이터로 관리하고 있으므로 매개변수에 어노테이션과 함께 불러옴
        log.info("로그인 된 유저 : {}", user);
    }
}