package org.tommap.tomuserloginrestapis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
@EnableWebSecurity(debug = true)
/*
    - prePostEnabled is true by default
    - invocation authorization -> validate if someone can invoke a method or not based on their roles & authorities -> @PreAuthorize | @PostAuthorize
    - filtering authorization -> validates what a method can receive through its parameters and what the invoker can receive back from the method post business logic execution [filterObject should be of type Collection] -> @PreFilter | @PostFilter
 */
@EnableMethodSecurity
public class TomUserLoginRestApisApplication {

    public static void main(String[] args) {
        SpringApplication.run(TomUserLoginRestApisApplication.class, args);
    }

}
