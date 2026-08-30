package ru.alex.Config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import ru.alex.entity.User;
import ru.alex.entity.dto.UserRole;
import ru.alex.repository.UserRepository;

import java.util.Collections;
import java.util.Set;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final UserRepository userRepository;

    @Autowired
    public SecurityConfig(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    return http
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(authorize -> {
                // 1. Сначала специфичные, защищенные правила
                authorize.requestMatchers("/admin/**").hasAnyRole(UserRole.ADMIN.name(), UserRole.SUPER_ADMIN.name());
                authorize.requestMatchers("/account/**").hasAnyRole(UserRole.USER.name(), UserRole.ADMIN.name(), UserRole.SUPER_ADMIN.name());
                authorize.requestMatchers("/super-admin**").hasRole((UserRole.SUPER_ADMIN.name()));
                // 2. Затем публичные ресурсы, к которым разрешен доступ без аутентификации
                authorize.requestMatchers( "/css/**","/", "/login", "/registration", "/error").permitAll();

                // 3. ОБЯЗАТЕЛЬНО: Завершающее правило для всех остальных запросов
                // Все, что не было разрешено выше, требует аутентификации.
                authorize.anyRequest().authenticated();
            })
             //Если вы используете собственную страницу входа, важно ее указать
            .formLogin(formLogin -> formLogin
                    .loginPage("/login")
                    .permitAll()
                    .usernameParameter("email")
                    .passwordParameter("password")
                    .defaultSuccessUrl("/account")
            )
            .logout(logout -> logout
                    .logoutUrl("/logout").permitAll()// Разрешить доступ к самой странице входа
            )
            .build();

    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }
    @Bean
    public SecurityContextRepository securityContextRepository() {
        // Используем стандартную реализацию, которая хранит контекст в сессии
        return new HttpSessionSecurityContextRepository();
    }

    @Bean
    public UserDetailsService userDetailsService(){
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

                User user = userRepository.findByEmailIgnoreCase(username)
                        .orElseThrow(()->new UsernameNotFoundException("User name with email" + username + "not found"));
                Set<SimpleGrantedAuthority> roles = Collections.singleton(user.getRole().toAuthority());
                return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(),roles);
            }
        };
    }

}
