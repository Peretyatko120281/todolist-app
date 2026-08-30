package ru.alex.Config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.alex.entity.User;
import ru.alex.entity.dto.UserRole;
import ru.alex.repository.UserRepository;

@Component
public class DatabaseInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${admin.email:admin@example.com}")
    private String adminEmail;

    @Value("${admin.password:admin123}")
    private String adminPassword;

    public DatabaseInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        createSuperAdmin();
        createAdmin();
        createDemoUser();
        printStartupInfo();
    }

    private void createSuperAdmin() {
        if (userRepository.findByEmailIgnoreCase("superadmin@example.com").isEmpty()) {
            User superAdmin = new User(
                    "Super Administrator",
                    "superadmin@example.com",  // Уникальный email для суперадмина
                    passwordEncoder.encode("super123"),
                    UserRole.SUPER_ADMIN
            );
            userRepository.save(superAdmin);
            System.out.println("✅ Суперадмин создан:");
            System.out.println("   Email: superadmin@example.com");
            System.out.println("   Пароль: super123");
            System.out.println("   Роль: SUPER_ADMIN");
        }
    }

    private void createAdmin() {
        if (userRepository.findByEmailIgnoreCase("admin@example.com").isEmpty()) {
            User admin = new User(
                    "Administrator",
                    "admin@example.com",
                    passwordEncoder.encode("admin123"),
                    UserRole.ADMIN
            );
            userRepository.save(admin);
            System.out.println("✅ Администратор создан:");
            System.out.println("   Email: admin@example.com");
            System.out.println("   Пароль: admin123");
            System.out.println("   Роль: ADMIN");
        }
    }

    private void createDemoUser() {
        if (userRepository.findByEmailIgnoreCase("user@example.com").isEmpty()) {
            User user = new User(
                    "Demo User",
                    "user@example.com",
                    passwordEncoder.encode("user123"),
                    UserRole.USER
            );
            userRepository.save(user);
            System.out.println("✅ Демо-пользователь создан:");
            System.out.println("   Email: user@example.com");
            System.out.println("   Пароль: user123");
            System.out.println("   Роль: USER");
        }
    }

    private void printStartupInfo() {
        System.out.println("\n========================================");
        System.out.println("🚀 ToDoList Application успешно запущена!");
        System.out.println("========================================");
        System.out.println("🔑 Тестовые учетные записи:");
        System.out.println("   👑 SUPER_ADMIN: superadmin@example.com / super123");
        System.out.println("   👔 ADMIN: admin@example.com / admin123");
        System.out.println("   👤 USER: user@example.com / user123");
        System.out.println("========================================");
        System.out.println("🌐 Перейдите на http://localhost:8080");
        System.out.println("========================================\n");
    }
}