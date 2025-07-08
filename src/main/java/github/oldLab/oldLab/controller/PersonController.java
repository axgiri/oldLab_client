package github.oldLab.oldLab.controller;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import github.oldLab.oldLab.dto.request.LoginRequest;
import github.oldLab.oldLab.dto.request.PersonRequest;
import github.oldLab.oldLab.dto.response.AuthResponse;
import github.oldLab.oldLab.dto.response.PersonResponse;
import github.oldLab.oldLab.service.PersonService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;

// [axgiri]: Контроллер для управления пользователями
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/persons")
public class PersonController {

    private final PersonService service;
    
    // [Tais0ft & axgiri]: Пример для Swagger — показывает структуру успешного ответа
    @Operation(summary = "Регистрация пользователя", description = "Создать нового пользователя. Автор: Tais0ft & axgiri")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Пользователь успешно создан",
            content = @Content(mediaType = "application/json",
                examples = @ExampleObject(value = "{\"id\":1,\"first_name\":\"Иван\",\"last_name\":\"Иванов\",\"phone_number\":\"+79998887766\"}"))),
        @ApiResponse(responseCode = "400", description = "Ошибка валидации запроса")
    })
    @PostMapping("/signup")
    public ResponseEntity<PersonResponse> create(@Valid @RequestBody PersonRequest personRequest) {
        log.debug("creating person: {}", personRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(personRequest));
    }

    @Operation(summary = "Асинхронная регистрация пользователя", description = "Создать нового пользователя асинхронно. Автор: Tais0ft")
    @ApiResponses({
        @ApiResponse(responseCode = "202", description = "Запрос принят в обработку"),
        @ApiResponse(responseCode = "400", description = "Ошибка валидации запроса")
    })
    @PostMapping("/async/signup")
    public ResponseEntity<Void> createAsync(@Valid @RequestBody PersonRequest personRequest) {
        log.debug("creating person: {}", personRequest);
        service.createAsync(personRequest);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @Operation(summary = "Аутентификация пользователя", description = "Вход пользователя по номеру телефона и паролю. Автор: Tais0ft")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Успешная аутентификация",
            content = @Content(mediaType = "application/json",
                examples = @ExampleObject(value = "{\"token\":\"jwt-token\",\"person\":{\"id\":1,\"first_name\":\"Иван\",\"last_name\":\"Иванов\"}}"))),
        @ApiResponse(responseCode = "401", description = "Неверные учетные данные")
    })
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest loginRequest){
        AuthResponse authResponse = service.authenticate(loginRequest);
        return ResponseEntity.ok(authResponse);
    }

    @Operation(summary = "Поиск пользователя по ID", description = "Получить пользователя по его ID. Автор: Tais0ft")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Пользователь найден"),
        @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    })
    @GetMapping("/findById/{id}")
    public ResponseEntity<PersonResponse> findById(@Parameter(description = "ID пользователя") @PathVariable Long id) {
        log.debug("finding person with id: {}", id);
        return ResponseEntity.ok(service.findById(id));
    }

    @Operation(summary = "Поиск пользователя по номеру телефона", description = "Получить пользователя по номеру телефона. Автор: Tais0ft")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Пользователь найден"),
        @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    })
    @GetMapping("/findByPhoneNumber/{phoneNumber}")
    public ResponseEntity<PersonResponse> findByPhoneNumber(@Parameter(description = "Номер телефона пользователя") @PathVariable String phoneNumber) {
        log.debug("finding person with phone number: {}", phoneNumber);
        return ResponseEntity.ok(service.findByPhoneNumber(phoneNumber));
    }

    @Operation(summary = "Асинхронное обновление пользователя", description = "Обновить пользователя по ID асинхронно. Автор: Tais0ft")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Пользователь обновлен"),
        @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    })
    @PostMapping("/async/update/{id}")
    public ResponseEntity<CompletableFuture<PersonResponse>> update(
            @Parameter(description = "ID пользователя") @PathVariable Long id,
            @Valid @RequestBody PersonRequest personRequest) {
        log.debug("updating person with id: {}", id);
        return ResponseEntity.ok(service.update(id, personRequest));
    }

    @Operation(summary = "Удаление пользователя", description = "Удалить пользователя по ID. Автор: Tais0ft")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Пользователь удалён"),
        @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    })
    @PostMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@Parameter(description = "ID пользователя") @PathVariable Long id) {
        log.debug("deleting person with id: {}", id);
        service.delete(id);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Валидация токена", description = "Проверить валидность JWT токена. Автор: Tais0ft")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Токен валиден"),
        @ApiResponse(responseCode = "401", description = "Токен невалиден")
    })
    @GetMapping("/validate")
    public ResponseEntity<String> validate(@Parameter(description = "JWT токен") @RequestHeader("Authorization") String token){
        log.debug("validating token: {}", token);
        service.validateToken(token);
        return ResponseEntity.ok("validation successful");
    }

    @Operation(summary = "Получить роль пользователя", description = "Получить роль из JWT токена. Автор: Tais0ft")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Роль получена"),
        @ApiResponse(responseCode = "401", description = "Токен невалиден")
    })
    @GetMapping("/getRoleName")
    public ResponseEntity<String> getRoleName(@Parameter(description = "JWT токен") @RequestHeader("Authorization") String token){
        log.debug("getting role from token: {}", token);
        String role = service.getRole(token);
        return ResponseEntity.ok(role);
    }

    @Operation(summary = "Получить коллег пользователя", description = "Получить список коллег пользователя с пагинацией. Автор: Tais0ft")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Коллеги получены"),
        @ApiResponse(responseCode = "401", description = "Токен невалиден")
    })
    @GetMapping("/getMyColleagues")
    public ResponseEntity<Page<PersonResponse>> getColleagues(
            @Parameter(description = "JWT токен") @RequestHeader("Authorization") String token,
            @Parameter(description = "Номер страницы") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Размер страницы") @RequestParam(defaultValue = "10") int size) {
        log.debug("getting colleagues for token: {}", token);
        service.validateToken(token);
        List<PersonResponse> colleagues = service.getColleaguesAsync(token);
        Pageable pageable = PageRequest.of(page, size);
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), colleagues.size());
        Page<PersonResponse> pageResult = new PageImpl<>(colleagues.subList(start, end), pageable, colleagues.size());
        return ResponseEntity.ok(pageResult);
    }

    @Operation(summary = "Обновление пароля пользователя", description = "Обновить пароль пользователя. Автор: Tais0ft")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Пароль обновлён"),
        @ApiResponse(responseCode = "400", description = "Ошибка валидации запроса")
    })
    @PutMapping("/updatePassword")
    public ResponseEntity<Void> updatePassword(@Valid @RequestBody LoginRequest loginRequest, @Parameter(description = "Старый пароль") @RequestParam String oldPassword) {
        log.debug("updating password for phone number: {}", loginRequest.getPhoneNumber());
        service.updatePasswordAsync(loginRequest, oldPassword);
        return ResponseEntity.ok().build();
    }
}