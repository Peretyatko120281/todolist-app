# 📝 ToDoList Application

Веб-приложение для управления задачами с системой ролей и авторизацией на Spring Security.

![Java](https://img.shields.io/badge/Java-17-ED8B00?style=flat&logo=java&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.0-6DB33F?style=flat&logo=spring-boot&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-316192?style=flat&logo=postgresql&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-2496ED?style=flat&logo=docker&logoColor=white)

## 📋 Оглавление

- [О проекте](#о-проекте)
- [Функциональность](#функциональность)
- [Технологии](#технологии)
- [Быстрый старт](#быстрый-старт)
    - [Способ 1: Docker Compose (рекомендуется)](#способ-1-docker-compose-рекомендуется)
    - [Способ 2: Ручной запуск](#способ-2-ручной-запуск)
- [Тестовые учетные записи](#тестовые-учетные-записи)
- [Структура проекта](#структура-проекта)
- [Безопасность](#безопасность)
- [Развитие проекта](#развитие-проекта)
- [Автор](#автор)

## 🎯 О проекте

**ToDoList Application** — это полнофункциональное веб-приложение для управления персональными задачами с разграничением прав доступа. Проект демонстрирует использование Spring Security для реализации ролевой модели безопасности.

### Ключевые особенности:
- 🔐 Безопасная аутентификация и авторизация
- 👥 Трехуровневая система ролей (USER, ADMIN, SUPER_ADMIN)
- 📝 Полный CRUD для задач
- 🎨 Адаптивный интерфейс с Bootstrap
- 💾 Хранение данных в PostgreSQL
- 🐳 Контейнеризация с Docker

## ⚡ Функциональность

### Для всех пользователей
- ✅ Регистрация нового аккаунта
- ✅ Вход в систему
- ✅ Просмотр личного кабинета
- ✅ Создание новых задач
- ✅ Изменение статуса задач (активная/выполнена)
- ✅ Удаление задач
- ✅ Фильтрация задач по статусу
- ✅ Счетчик посещений страницы

### Для администраторов (ADMIN, SUPER_ADMIN)
- 👥 Просмотр списка всех пользователей
- 🗑️ Удаление пользователей
- 📊 Управление задачами всех пользователей

### Для суперадминистраторов (SUPER_ADMIN)
- ⭐ Все функции администратора
- 🔄 Повышение пользователей до администраторов

## 🛠️ Технологии

### Бэкенд
| Технология | Версия | Назначение |
|------------|--------|------------|
| Java | 17 | Язык программирования |
| Spring Boot | 3.2.0 | Фреймворк приложения |
| Spring Security | 6.2.0 | Безопасность и аутентификация |
| Spring Data JPA | - | Работа с БД |
| PostgreSQL | 16 | База данных |
| Hibernate | 6.3.0 | ORM |
| Maven | 3.9.0 | Сборка проекта |

### Фронтенд
| Технология | Версия | Назначение |
|------------|--------|------------|
| Thymeleaf | 3.1.0 | Шаблонизатор |
| Bootstrap | 5.3.0 | CSS-фреймворк |
| HTML5 | - | Разметка |
| CSS3 | - | Стили |

## 🚀 Быстрый старт

### Способ 1: Docker Compose (рекомендуется)

#### Предварительные требования
- Docker
- Docker Compose

#### 1. Создайте файл `docker-compose.yml` в корне проекта:

```yaml
version: '3.8'

services:
  postgres:
    image: postgres:16-alpine
    container_name: todolist-postgres
    environment:
      POSTGRES_DB: alex
      POSTGRES_USER: alex
      POSTGRES_PASSWORD: 12345678
    ports:
      - "5432:5432"
    volumes:
      - postgres_data:/var/lib/postgresql/data
    networks:
      - todolist-network
    healthcheck:
      test: ["CMD-SHELL", "pg_isready -U alex -d alex"]
      interval: 10s
      timeout: 5s
      retries: 5

  app:
    build: .
    container_name: todolist-app
    depends_on:
      postgres:
        condition: service_healthy
    environment:
      SPRING_PROFILES_ACTIVE: docker
      SPRING_DATASOURCE_URL: jdbc:postgresql://postgres:5432/alex
      SPRING_DATASOURCE_USERNAME: alex
      SPRING_DATASOURCE_PASSWORD: 12345678
      SPRING_JPA_HIBERNATE_DDL_AUTO: update
    ports:
      - "8080:8080"
    networks:
      - todolist-network

volumes:
  postgres_data:

networks:
  todolist-network:
    driver: bridge

2. Создайте Dockerfile в корне проекта:

FROM openjdk:17-jdk-alpine

WORKDIR /app

COPY target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]

3. Соберите JAR и запустите:

# Сборка JAR файла
mvn clean package

# Запуск всех сервисов
docker-compose up --build

# Запуск в фоновом режиме
docker-compose up -d

4. Приложение будет доступно по адресу:

http://localhost:8080

Команды для управления:

# Остановка всех контейнеров
docker-compose down

# Остановка с удалением томов (удаляет данные БД)
docker-compose down -v

# Просмотр логов
docker-compose logs -f app

# Перезапуск
docker-compose restart

Способ 2: Ручной запуск
Предварительные требования
Java 17+

PostgreSQL 14+

Maven 3.8+

1. Запустите PostgreSQL в Docker:
bash
docker run -d \
  --name todolist-postgres \
  -e POSTGRES_DB=alex \
  -e POSTGRES_USER=alex \
  -e POSTGRES_PASSWORD=12345678 \
  -p 5432:5432 \
  -v postgres_data:/var/lib/postgresql/data \
  postgres:16-alpine
2. Настройте приложение:
Создайте файл application-local.properties в src/main/resources/:

properties
spring.datasource.url=jdbc:postgresql://localhost:5432/alex
spring.datasource.username=alex
spring.datasource.password=12345678
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
3. Запустите приложение:
Вариант A: Через IDEA

Откройте ToDoListApplication.java

Настройте Run Configuration с VM option:

text
-Dspring.profiles.active=local
Нажмите зеленую стрелку

Вариант B: Через терминал

bash
mvn spring-boot:run -Dspring-boot.run.profiles=local
4. Откройте в браузере:
text
http://localhost:8080
🔑 Тестовые учетные записи
Роль	Email	Пароль
👑 SUPER_ADMIN	superadmin@example.com	super123
👔 ADMIN	admin@example.com	admin123
👤 USER	user@example.com	user123
Доступные URL для тестирования
URL	Описание	Доступ
/	Главная страница	Все
/login	Вход в систему	Все
/registration	Регистрация	Все
/account	Личный кабинет	USER, ADMIN, SUPER_ADMIN
/admin	Админ панель	ADMIN, SUPER_ADMIN
/super-admin/**	Супер-админ функции	SUPER_ADMIN
⚠️ ВАЖНО: Безопасность
❗ НИКОГДА НЕ ИСПОЛЬЗУЙТЕ ЭТИ ДАННЫЕ В РЕАЛЬНЫХ ПРОЕКТАХ!

Для локальной разработки
✅ Тестовые пароли (super123, admin123, user123) предназначены ТОЛЬКО для локальной разработки

✅ Пароли в README и docker-compose.yml — это демонстрационные данные

✅ Для локальной разработки используйте application-local.properties (он в .gitignore)

Для продакшена
❌ ЗАПРЕЩЕНО использовать тестовые пароли

❌ ЗАПРЕЩЕНО хранить пароли в коде или Git

❌ ЗАПРЕЩЕНО использовать дефолтные учетные данные

Правила безопасности
Используйте переменные окружения:

properties
spring.datasource.password=${DB_PASSWORD}
Для продакшена используйте секреты:

Docker Secrets

Kubernetes Secrets

HashiCorp Vault

AWS Secrets Manager

Всегда меняйте пароли по умолчанию:

Администратор

База данных

Приложение

Никогда не коммитьте файлы с паролями:

application-local.properties — в .gitignore

.env — в .gitignore

Файлы с секретами — в .gitignore

Переменные окружения для продакшена
bash
# Обязательные переменные
export DB_PASSWORD=secure_production_password_123
export ADMIN_PASSWORD=super_secure_admin_password_456
export SPRING_PROFILES_ACTIVE=prod

# Опциональные
export DB_URL=jdbc:postgresql://production-db:5432/alex
export DB_USERNAME=prod_user
📁 Структура проекта
text
src/
├── main/
│   ├── java/
│   │   └── ru/alex/
│   │       ├── config/
│   │       │   ├── DatabaseInitializer.java    # Инициализация тестовых данных
│   │       │   └── SecurityConfig.java         # Настройки Spring Security
│   │       ├── controller/
│   │       │   ├── common/                     # Публичные контроллеры
│   │       │   └── secured/                    # Защищенные контроллеры
│   │       ├── entity/                         # JPA сущности
│   │       ├── repository/                     # Spring Data репозитории
│   │       ├── service/                        # Бизнес-логика
│   │       └── ToDoListApplication.java        # Точка входа
│   └── resources/
│       ├── static/
│       │   └── css/
│       │       └── styles.css
│       ├── templates/
│       │   ├── public/                         # Публичные страницы
│       │   └── private/                        # Защищенные страницы
│       ├── application.properties              # Основные настройки
│       └── application-example.properties      # Пример конфигурации
└── test/                                       # Тесты
🔮 Развитие проекта
□ Пагинация для списка задач
□ REST API
□ Unit-тесты
□ Система уведомлений
□ Экспорт задач в PDF
□ Поиск задач
□ Приоритеты задач
□ Swagger/OpenAPI документация
🤝 Внесение вклада
Fork репозитория

Создайте ветку для новой функции

Закоммитьте изменения

Откройте Pull Request

👤 Автор
Aleksandr Peretatko

GitHub: [@Peretyatko120281](https://github.com/Peretyatko120281)

Email: aap120281@gmail.com

📄 Лицензия
Этот проект распространяется под лицензией MIT.

<div align="center"> <sub>Built with ❤️ by Aleksandr Peretatko</sub> </div> ```


