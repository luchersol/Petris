package org.springframework.samples.petris.configuration;

import java.util.Arrays;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.samples.petris.configuration.services.UserDetailsImpl;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@TestConfiguration
public class SpringSecurityWebAuxTestConfiguration {

    @Bean
    @Primary
    public UserDetailsService userDetailsService() {

        UserDetailsImpl adminActiveUser = new UserDetailsImpl(1, "admin1", "password",
        		Arrays.asList(
                        new SimpleGrantedAuthority("ADMIN"))
        );
        
        UserDetailsImpl playerActiveUser = new UserDetailsImpl(2, "player1", "password",
        		Arrays.asList(
                        new SimpleGrantedAuthority("PLAYER"))
        );

        return new InMemoryUserDetailsManager(Arrays.asList(
        		 adminActiveUser, playerActiveUser
        ));
    }
}
