package com.nikita.development.rf;

import java.util.Locale;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

import com.nikita.development.rf.i18n.CustomLocaleResolver;

@SpringBootApplication
@EnableGlobalMethodSecurity(prePostEnabled = true)
@ComponentScan
public class App extends WebMvcConfigurerAdapter {

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
    
    @Bean
    public LocaleResolver localeResolver(){
    	 CustomLocaleResolver localeResolver = new CustomLocaleResolver();
         localeResolver.setDefaultLocale(Locale.US);
         return  localeResolver;
     }

     @Bean
     public LocaleChangeInterceptor localeChangeInterceptor() {
         LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
         localeChangeInterceptor.setParamName("lang");
         return localeChangeInterceptor;
     }

     @Override
     public void addInterceptors(InterceptorRegistry registry){
         registry.addInterceptor(localeChangeInterceptor());
     }

}
