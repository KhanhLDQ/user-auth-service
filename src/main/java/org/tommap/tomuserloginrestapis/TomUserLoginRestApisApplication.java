package org.tommap.tomuserloginrestapis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
@EnableWebSecurity(debug = true)
public class TomUserLoginRestApisApplication {

    public static void main(String[] args) {
        SpringApplication.run(TomUserLoginRestApisApplication.class, args);
    }

}
