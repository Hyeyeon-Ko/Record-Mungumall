package com.example.mungumall.admin.controller;

import com.example.mungumall.admin.model.service.AdminService;
import com.example.mungumall.member.model.dto.MemberDTO;
import com.example.mungumall.paging.model.dto.Criteria;
import com.example.mungumall.paging.model.dto.PageDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;

    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/dashboard")
    public String getDashboard() {
        return "admin/dashboard";
    }

    @GetMapping("/member/list")
    public void getMemberList(@Valid @ModelAttribute("criteria") Criteria criteria, BindingResult bindingResult, HttpServletRequest request, Model model) {
        log.info("회원 목록 요청");

        int total = adminService.getTotalNumber();
        int regular = adminService.getRegularNumber();
        int admin = adminService.getAdminNumber();
        int closed = adminService.getClosedNumber();
        log.info("전체 회원수 : {}", total);
        log.info("일반 회원수 : {}", regular);
        log.info("관리자 수 : {}", admin);

        List<MemberDTO> memberList = adminService.getMemberList(criteria);
        List<MemberDTO> memberOnly = adminService.getMemberOnly();
        List<MemberDTO> adminOnly = adminService.getAdminOnly();
        List<MemberDTO> closedOnly = adminService.getClosedOnly();
        log.info("회원 목록 조회 완료");

        model.addAttribute("total", total);
        model.addAttribute("regular", regular);
        model.addAttribute("admin", admin);
        model.addAttribute("closed", closed);
        model.addAttribute("memberList", memberList);

        model.addAttribute("memberOnly", memberOnly);
        model.addAttribute("adminOnly", adminOnly);
        model.addAttribute("closedOnly", closedOnly);
        model.addAttribute("pageMaker", new PageDTO(adminService.getTotalNumber(), 10, criteria));    }

    @GetMapping("/member/details")
    public String getMemberDetails(@RequestParam("id") String memberId, Model model) {
        MemberDTO member = adminService.getMemberDetails(memberId);
        model.addAttribute("member", member);
        return "admin/member/details";
    }

    @PostMapping(value="/member/manageAuth", produces="application/json; charset=UTF-8")
    @ResponseBody
    public String changeAuth(@RequestParam Map<String, Object> params, HttpServletRequest request) {
        log.info("권한 수정 시작");
        String optValue = params.get("optValue").toString();
        int selected = Integer.parseInt(optValue);
        String[] idList = request.getParameterValues("arr");
        int count = 0;
        String result = "";

        for(int i=0; i < idList.length; i++) {
            int current = adminService.searchAuthById(idList[i]);
            log.info("현재 권한 수(1 : 일반회원, 2 : 관리자) : {}", current);

            if(current == 2 && selected == 2) {
                result = idList[i] + "(은)는 이미 관리자입니다";
                log.info(result);
            } else if(current == 1 && selected == 1) {
                result = idList[i] + "(은)는 이미 일반회원입니다";
                log.info(result);
            } else if(current == 2 && selected == 1) { //현재 관리자에서 일반회원으로 변경
                count += adminService.deleteAuthAsAdmin(idList[i]);
            } else { //현재 일반회원에서 관리자로 변경
                count += adminService.insertAuthAsAdmin(idList[i]);
            }

            if(idList.length == count) {
                result = "성공";
                log.info(result);
            }
        }
        return result;
    }

    @PostMapping(value="/member/suspendAcc", produces="application/json; charset=UTF-8")
    @ResponseBody
    public String updateAccSuspension(@RequestParam Map<String, Object> params) {
        String memberId = params.get("memberId").toString();
        String accSuspDesc = params.get("accSuspDesc").toString();
        int resultA = adminService.updateAccSuspension(memberId);
        int resultB = adminService.insertAccSuspension(memberId, accSuspDesc);

        String result = "";
        if(resultA == 1 && resultB == 1) {
            result = "성공";
        }
        return result;
    }

    @PostMapping(value="/member/activateAcc", produces="application/json; charset=UTF-8")
    @ResponseBody
    public String updateAccActivation(HttpServletRequest request) {
        String[] idList = request.getParameterValues("arr");
        int count = 0;
        String result = "";

        for(int i=0; i < idList.length; i++) {
            adminService.updateAccActivation(idList[i]);
            count++;
        }

        if(idList.length == count) {
            result = "성공";
        }
        return result;
    }
}
