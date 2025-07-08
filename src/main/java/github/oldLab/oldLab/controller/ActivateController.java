package github.oldLab.oldLab.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import github.oldLab.oldLab.dto.request.ActivateRequest;
import github.oldLab.oldLab.dto.response.AuthResponse;
import github.oldLab.oldLab.service.ActivateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;

// [axgiri]: Контроллер для активации аккаунта и работы с OTP
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/activate")
public class ActivateController {
    
    public final ActivateService service;

    @Operation(summary = "Активация аккаунта по OTP", description = "Активировать аккаунт пользователя по OTP. Автор: Tais0ft")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Аккаунт активирован"),
        @ApiResponse(responseCode = "400", description = "Ошибка валидации или неверный OTP")
    })
    @PostMapping("/activate")
    public ResponseEntity<Void> activate(@Valid @RequestBody ActivateRequest request){
        log.debug("activating account with phone number: {}", request.getPhoneNumber());
        service.setActive(request);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Отправка OTP для активации", description = "Отправить OTP на номер для активации аккаунта. Автор: Tais0ft")
    @ApiResponses({
        @ApiResponse(responseCode = "202", description = "OTP отправлен"),
        @ApiResponse(responseCode = "400", description = "Ошибка отправки OTP")
    })
    @PostMapping("/send/activate/{phoneNumber}")
    public ResponseEntity<Void> sendOtp(@Parameter(description = "Номер телефона") @PathVariable String phoneNumber){
        log.debug("sending OTP to phone number: {}", phoneNumber);
        service.sendOtp(phoneNumber);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @Operation(summary = "Повторная отправка OTP для активации", description = "Повторно отправить OTP на номер для активации аккаунта. Автор: Tais0ft")
    @ApiResponses({
        @ApiResponse(responseCode = "202", description = "OTP повторно отправлен"),
        @ApiResponse(responseCode = "400", description = "Ошибка отправки OTP")
    })
    @PostMapping("/resend/activate/{phoneNumber}")
    public ResponseEntity<Void> resendOtp(@Parameter(description = "Номер телефона") @PathVariable String phoneNumber){
        log.debug("resending OTP to phone number: {}", phoneNumber);
        service.resendOtp(phoneNumber);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @Operation(summary = "Аутентификация по OTP", description = "Вход пользователя по номеру телефона и OTP. Автор: Tais0ft")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Успешная аутентификация"),
        @ApiResponse(responseCode = "401", description = "Неверный OTP или номер телефона")
    })
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody ActivateRequest request){
        log.debug("logging in user with phone number: {}", request.getPhoneNumber());
        AuthResponse response = service.login(request.getPhoneNumber(), request.getOtp());
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Отправка OTP для входа", description = "Отправить OTP на номер для входа. Автор: Tais0ft")
    @ApiResponses({
        @ApiResponse(responseCode = "202", description = "OTP отправлен"),
        @ApiResponse(responseCode = "400", description = "Ошибка отправки OTP")
    })
    @PostMapping("/send/login/{phoneNumber}")
    public ResponseEntity<Void> sendLoginOtp(@Parameter(description = "Номер телефона") @PathVariable String phoneNumber) {
        log.debug("sending login OTP to phone number: {}", phoneNumber);
        service.sendLoginOtp(phoneNumber);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }
}