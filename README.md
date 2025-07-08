# OldLab API

## Описание
REST API для управления пользователями и активацией аккаунтов. Документация и разработка: **Tais0ft**

## Запуск проекта

1. Установите JDK 17+ и Maven 3.8+
2. Соберите проект:
   ```bash
   mvn clean install
   ```
3. Запустите приложение:
   ```bash
   mvn spring-boot:run
   ```

## Swagger UI

После запуска перейдите по адресу:
- [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
- или [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

Вся документация и примеры запросов доступны в Swagger UI.

## Основные эндпоинты

### Пользователь (PersonController)
- `POST /api/v1/persons/signup` — регистрация пользователя
- `POST /api/v1/persons/login` — аутентификация
- `GET /api/v1/persons/findById/{id}` — поиск по ID
- `GET /api/v1/persons/getMyColleagues` — получить коллег (пагинация)
- и др.

### Активация (ActivateController)
- `POST /api/v1/activate/activate` — активация аккаунта по OTP
- `POST /api/v1/activate/send/activate/{phoneNumber}` — отправка OTP
- `POST /api/v1/activate/login` — вход по OTP
- и др.

## Автор
Tais0ft 