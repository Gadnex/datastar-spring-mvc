package io.github.gadnex.datastarspringmvc;

import gg.jte.springframework.boot.autoconfigure.JteViewResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

@Configuration
public class JteConfiguration {

  @Autowired
  public void configureJteViewResolver(JteViewResolver jteViewResolver) {
    jteViewResolver.setOrder(Ordered.HIGHEST_PRECEDENCE);
  }
}
