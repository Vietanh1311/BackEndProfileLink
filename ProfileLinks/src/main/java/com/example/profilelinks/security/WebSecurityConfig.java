package com.example.profilelinks.security;


import com.example.profilelinks.constants.RoleEnum;
import com.example.profilelinks.security.jwt.JwtAuthenticationFilter;
import com.example.profilelinks.service.impl.CustomOAuth2UserService;
import com.example.profilelinks.service.impl.UserDetailsServiceImpl;
import com.example.profilelinks.service.impl.UserServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.zalando.problem.spring.web.advice.security.SecurityProblemSupport;

@EnableWebSecurity
@Import(SecurityProblemSupport.class)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private UserServiceImpl userService;

    private final SecurityProblemSupport problemSupport;

    private final CustomOAuth2UserService oAuth2UserService;

    private final PasswordEncoder passwordEncoder;

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public WebSecurityConfig(UserServiceImpl userService, SecurityProblemSupport problemSupport, CustomOAuth2UserService oAuth2UserService, PasswordEncoder passwordEncoder, JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.userService = userService;
        this.problemSupport = problemSupport;
        this.oAuth2UserService = oAuth2UserService;
        this.passwordEncoder = passwordEncoder;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }


    @Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailsServiceImpl();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder);
        return authProvider;
    }

    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        // Get AuthenticationManager bean
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth)
            throws Exception {
        auth.userDetailsService(userService) // Cung c??p userservice cho spring security
                .passwordEncoder(passwordEncoder); // cung c???p password encoder
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.antMatcher("/api/**")
                .cors().and()
                .csrf()
                .disable()
                .exceptionHandling()
                .authenticationEntryPoint(problemSupport)
                .accessDeniedHandler(problemSupport)
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/test/**",
                             "/login/oauth/**",
                             "/api/v1.0/design/get/**",
                             "/api/v1.0/link/list/**",
                             "/api/v1.0/profile/shortbio/**",
                             "/api/v1.0/social/get/**").permitAll()
                .antMatchers("/topic/**").permitAll()
                .antMatchers(HttpMethod.POST, "*/swagger-ui.html/*").permitAll()
                .antMatchers("/update-password-token", "/api/v1.0/user/username", "/api/v1.0/user/add", "/api/v1.0/charts", "/api/v1.0/link", "/api/v1.0/profile",
                        "/api/v1.0/social").hasAnyAuthority(RoleEnum.USER.name(), RoleEnum.USER_VIP.name(), RoleEnum.ADMIN.name())
                .antMatchers(HttpMethod.POST, "/api/v1.0/design").hasAnyAuthority(RoleEnum.USER_VIP.name(), RoleEnum.ADMIN.name())
                .antMatchers("/api/v1.0/design/{id}").hasAnyAuthority(RoleEnum.USER_VIP.name(), RoleEnum.ADMIN.name())
                .antMatchers().hasRole(RoleEnum.USER_VIP.name())
                .antMatchers().hasRole(RoleEnum.ADMIN.name())
                .anyRequest().authenticated()
                .and()
                .httpBasic()
                //        * Th??m m???t l???p Filter ki???m tra jwt
                .and()
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring()
                .antMatchers("/swagger-ui.html")
                .antMatchers("/test/**")
                .antMatchers("/login/oauth/**")
                .antMatchers("/topic/**");
    }
}
