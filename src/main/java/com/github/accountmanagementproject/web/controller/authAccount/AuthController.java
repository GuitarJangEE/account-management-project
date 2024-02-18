package com.github.accountmanagementproject.web.controller.authAccount;

import com.github.accountmanagementproject.repository.account.users.CustomUserDetails;
import com.github.accountmanagementproject.service.MakeResponseService;
import com.github.accountmanagementproject.service.authAccount.SignUpLoginService;
import com.github.accountmanagementproject.service.customExceptions.CustomBadRequestException;
import com.github.accountmanagementproject.web.dto.account.AccountDto;
import com.github.accountmanagementproject.web.dto.account.LoginRequest;
import com.github.accountmanagementproject.web.dto.response.CustomErrorResponse;
import com.github.accountmanagementproject.web.dto.response.CustomSuccessResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final MakeResponseService makeResponseService;
    private final SignUpLoginService signUpLoginService;

    @PostMapping("/sign-up")
    public ResponseEntity<CustomSuccessResponse> signUp(@RequestBody AccountDto accountDto){
        CustomSuccessResponse signUpResponse = signUpLoginService.signUp(accountDto);
        return new ResponseEntity<>(signUpResponse, signUpResponse.getSuccess().getHttpStatus());
    }
    @PostMapping("/sign-in")
    public CustomSuccessResponse login(@RequestBody LoginRequest loginRequest, HttpServletResponse httpServletResponse){
        List<Object> tokenAndResponse = signUpLoginService.loginResponseToken(loginRequest);
        httpServletResponse.setHeader("Authorization", tokenAndResponse.get(0).toString());
        return (CustomSuccessResponse) tokenAndResponse.get(1);
    }


    @GetMapping("/testerr")
    public CustomErrorResponse errorTest(){
        throw new CustomBadRequestException("메롱입니다.", "요청값입니다.");
    }
    @GetMapping("/testsuc")
    @ResponseStatus(HttpStatus.CREATED)
    public CustomSuccessResponse successTest(){
        //3번째 요소 데이터는 없어도됨
        return new CustomSuccessResponse(makeResponseService.makeSuccessDetail(
                HttpStatus.CREATED, "메세지", Arrays.asList(1,2,3,4,5))
        );
    }
    @GetMapping("/security-test")
    public String securityTest(@RequestParam(name = "user-id") Integer userId){
        return signUpLoginService.securityTest(userId);
    }
    @GetMapping("/token-test")
    public String tokenTest(HttpServletRequest httpServletRequest){
        return signUpLoginService.tokenTest(httpServletRequest.getHeader("Authorization"));
    }

    @GetMapping("/auth-test")
    public CustomUserDetails authTest(@AuthenticationPrincipal CustomUserDetails customUserDetails){
        return customUserDetails;
    }



}