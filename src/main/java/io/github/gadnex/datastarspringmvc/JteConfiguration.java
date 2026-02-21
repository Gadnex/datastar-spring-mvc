package io.github.gadnex.datastarspringmvc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import gg.jte.springframework.boot.autoconfigure.JteViewResolver;

@Configuration
public class JteConfiguration {

  @Autowired
  public void configureJteViewResolver(JteViewResolver jteViewResolver) {
    jteViewResolver.setOrder(0);
  }
}