package com.shalini.dbms.configurations;

import com.shalini.dbms.services.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    UserDetailsServiceImpl userDetailsService;

    @Autowired
    public WebSecurityConfiguration(UserDetailsServiceImpl userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    BCryptPasswordEncoder passwordEncoder(){
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        return bCryptPasswordEncoder;
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception{
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    public AuthenticationManager customAuthenticationManager() throws Exception{
        return authenticationManager();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.authorizeRequests().antMatchers("/","/login","/logout","/register").permitAll();
        http.authorizeRequests().antMatchers("/admin","/admin/**").access("hasAnyAuthority('admin')");
        http.authorizeRequests().antMatchers("/cart","/viewProfile","/viewOrderDetails/*","/editProfile","/orders","/feedback","/removeFromCart/*").access("hasAnyAuthority('Customer', 'Vendor', 'admin')");
        http.authorizeRequests().antMatchers("/shopAddress","/proposals","/addProposal","/editAddress").access("hasAnyAuthority('Vendor','admin')");
        http.authorizeRequests().and().exceptionHandling().accessDeniedPage("/403");
        http.authorizeRequests().and().formLogin().loginPage("/login")
                .defaultSuccessUrl("/welcome",true).failureUrl("/login?error=true").usernameParameter("email")
                .passwordParameter("password").and().logout().logoutUrl("/logout").logoutSuccessUrl("/");
    }
}
