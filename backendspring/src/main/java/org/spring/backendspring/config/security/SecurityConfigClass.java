package org.spring.backendspring.config.security;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Arrays;

import lombok.RequiredArgsConstructor;
import org.spring.backendspring.config.security.filter.CustomLoginFilter;
import org.spring.backendspring.config.security.filter.CustomLogoutFilter;
import org.spring.backendspring.config.security.filter.JWTCheckFilter;
import org.spring.backendspring.config.security.handler.CustomAccessDeniedHandler;
import org.spring.backendspring.config.security.handler.CustomLoginFailureHandler;
import org.spring.backendspring.config.security.handler.OAuth2SuccessHandler;
import org.spring.backendspring.config.security.util.JWTUtil;
import org.spring.backendspring.member.service.MemberService;
import org.spring.backendspring.member.service.RefreshService;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties.Lettuce.Cluster.Refresh;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@EnableWebSecurity
@EnableMethodSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfigClass {

    private final JWTUtil jwtUtil;
    private final AuthenticationConfiguration configuration;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http,
                                           RefreshService refreshService,
                                           MemberService memberService) throws Exception {

        http.csrf(AbstractHttpConfigurer::disable);

        http.cors(cors -> cors.configurationSource(corsConfigurationSource()));

        http.authorizeHttpRequests(authorize -> {
            authorize
                    .requestMatchers("/api/payments/approval/**").permitAll() // ✅ 로그인 없이 허용
                    .requestMatchers("/api/admin/**").hasAnyRole("ADMIN", "MANAGER")
                    .requestMatchers(
                            "/api/crew/create/approved",
                            "/api/crew/create/rejected").hasRole("ADMIN")
                     .requestMatchers("/api/member/detail/**").authenticated()
                    .requestMatchers(
                            "/api/board/newPost",
                            "/api/board/write",
                            "/api/board/update/**",
                            "/api/board/updatePost").authenticated()
                    .requestMatchers("/api/mycrew/*/board/create",
                            "/api/mycrew/*/board/update",
                            "/api/mycrew/*/board/delete",
                            "/api/mycrew/*/board/*/comment/write").authenticated()
                    .requestMatchers("/api/member/**").permitAll()
                    .requestMatchers("/login", "/logout", "/api/**", "/index").permitAll()
                    .requestMatchers("/marathons").permitAll()

                    .requestMatchers("/swagger-ui/**").permitAll()
                    .requestMatchers("/v3/api-docs/**").permitAll()

                    .requestMatchers("/**").permitAll(); // css, js 파일 허용
        });

        http.exceptionHandling(ex -> ex
                .authenticationEntryPoint((req, res, excep) -> {
                    if (excep.getCause() instanceof ExpiredJwtException) {
                        res.sendError(HttpServletResponse.SC_UNAUTHORIZED, "ACCESS_TOKEN_EXPIRED");
                    } else {
                        res.sendError(HttpServletResponse.SC_UNAUTHORIZED, "로그인이 필요합니다.");
                    }
                }));


        http.sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.formLogin(AbstractHttpConfigurer::disable);

        CustomLoginFilter customLoginFilter =
                new CustomLoginFilter(authenticationManager(configuration), jwtUtil, refreshService);
        customLoginFilter.setFilterProcessesUrl("/api/member/login");
        customLoginFilter.setAuthenticationFailureHandler(customLoginFailureHandler());

        http.addFilterBefore(jwtCheckFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class);
        http.addFilterAt(customLoginFilter, UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(new CustomLogoutFilter(refreshService), LogoutFilter.class);


        http.exceptionHandling(config ->
                config.accessDeniedHandler(new CustomAccessDeniedHandler())
        );


        http.oauth2Login(oauth2 ->
                oauth2.userInfoEndpoint(info ->
                                info.userService(myDefaultOAuth2UserService()))
                        .successHandler(new OAuth2SuccessHandler(jwtUtil, memberService, refreshService))
        );

        http.logout(logout ->
                logout.logoutUrl("/api/member/logout")
                        .permitAll()
        );

        return http.build();
    }


    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOriginPatterns(Arrays.asList(
                "http://localhost:8088",
                "http://localhost:3000",
                "http://localhost:3001",
                "https://online-payment.kakaopay.com/**",
                "https://kapi.kakao.com/v1/payment/ready/**",
                "https://kapi.kakao.com/v1/payment/approve/**"

        ));

        configuration.setAllowedMethods(Arrays.asList("HEAD", "GET", "POST", "PUT", "DELETE"));

        configuration.setAllowedHeaders(Arrays.asList("*"));

        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration)
            throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JWTCheckFilter jwtCheckFilter(JWTUtil jwtUtil) {
        return new JWTCheckFilter(jwtUtil);
    }

    @Bean
    public MyDefaultOAuth2UserService myDefaultOAuth2UserService() {
        return new MyDefaultOAuth2UserService();
    }

    @Bean
    public CustomLoginFailureHandler customLoginFailureHandler() {
        return new CustomLoginFailureHandler();
    }

}