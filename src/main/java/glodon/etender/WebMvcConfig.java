package glodon.etender;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import org.codehaus.jackson.map.ObjectMapper;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.springframework.web.servlet.mvc.annotation.AnnotationMethodHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.web.filter.XssSqlFilter;
import com.web.interceptor.LoginInterceptor;
import com.web.job.DelegatingServletProxy;


@ComponentScan("com")
@MapperScan("com.web.dao")
@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {
	
  private static Logger logger = LoggerFactory.getLogger(WebMvcConfig.class);

  @Autowired 
  private LoginInterceptor li;
  
  @Autowired 
  private DelegatingServletProxy initialServlet;
  
  @Bean
  public AnnotationMethodHandlerAdapter annotationMethodHandlerAdapter()
  {
      logger.info("add MappingJackson2HttpMessageConverter to handleer@@@@@@@@@@@@@@@@@@@@@@@@@@");  
	  final AnnotationMethodHandlerAdapter annotationMethodHandlerAdapter = new AnnotationMethodHandlerAdapter();
        final MappingJackson2HttpMessageConverter mappingJacksonHttpMessageConverter = converter();
        HttpMessageConverter<?>[] httpMessageConverter = { mappingJacksonHttpMessageConverter };
        String[] supportedHttpMethods = { "POST", "GET", "HEAD" };
        annotationMethodHandlerAdapter.setMessageConverters(httpMessageConverter);
        //annotationMethodHandlerAdapter.setSupportedMethods(supportedHttpMethods);
        return annotationMethodHandlerAdapter;
  }
  
  @Bean
  MappingJackson2HttpMessageConverter converter() {
	  MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
	  //ObjectMapper objectMapper = new ObjectMapper();
	  //objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	  //converter.setObjectMapper(objectMapper);		  
      converter.setSupportedMediaTypes(Collections.singletonList(
    		    MediaType.valueOf("text/html;charset=UTF-8")));
      return converter;
  }
  

  
  
  @Override
  public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
    configurer.defaultContentType(MediaType.TEXT_HTML);
  }
  
  @Bean
  public MessageSource messageSource() {
  	  ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
      messageSource.setBasenames("messages");
      messageSource.setDefaultEncoding("UTF-8");
      messageSource.setUseCodeAsDefaultMessage(true);
      return messageSource;
  } 
  
  
  @Bean
  public ThreadPoolTaskExecutor taskExecutor(){
      ThreadPoolTaskExecutor pool = new ThreadPoolTaskExecutor();      
      pool.setCorePoolSize(2);
      pool.setMaxPoolSize(200);
      pool.setKeepAliveSeconds(500);
      pool.setQueueCapacity(5000);
      pool.setWaitForTasksToCompleteOnShutdown(true);
      return pool;
  }
  
  @Bean  
  public InternalResourceViewResolver setupViewResolver() {  
           InternalResourceViewResolver resolver = new InternalResourceViewResolver();  
      resolver.setPrefix("/WEB-INF/jsp/");  
      resolver.setSuffix(".jsp");  
      resolver.setViewClass(JstlView.class);  
      return resolver;  
  }
  
  
  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
	  logger.info("add resource location");
      registry.addResourceHandler("/res/**")
              .addResourceLocations("/,classpath:/META-INF/resources/");
      registry.addResourceHandler("/Avatars/**/**").addResourceLocations("/Avatars/**/");
      registry.addResourceHandler("/Download/**").addResourceLocations("/Download/");
      registry.addResourceHandler("/Flashs/**").addResourceLocations("/Flashs/");
      registry.addResourceHandler("/Images/**").addResourceLocations("/Images/");
      registry.addResourceHandler("/Reports/**").addResourceLocations("/Reports/");
      registry.addResourceHandler("/Scripts/**").addResourceLocations("/Scripts/");
      registry.addResourceHandler("/Styles/**").addResourceLocations("/Styles/");
      registry.addResourceHandler("/UpdateFiles/**").addResourceLocations("/UpdateFiles/");
      registry.addResourceHandler("/UploadImages/**").addResourceLocations("/UploadImages/");
      registry.addResourceHandler("/UploadVideos/**").addResourceLocations("/UploadVideos/");
      registry.addResourceHandler("/UploadImages/**").addResourceLocations("/UploadImages/");
      registry.addResourceHandler("/Videos/**").addResourceLocations("/Videos/");
      registry.addResourceHandler("/robots.txt").addResourceLocations("/robots.txt");
  }
  
  @Bean
  public LocaleResolver localeResolver() {
      SessionLocaleResolver slr = new SessionLocaleResolver();
      slr.setDefaultLocale(Locale.US);
      return slr;
  }

  @Bean
  public LocaleChangeInterceptor localeChangeInterceptor() {
      LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
      //lci.setParamName("lang");
      return lci;
  }
  
  
  @Bean 
  public ServletRegistrationBean servletRegistrationBean() {

      ServletRegistrationBean registration = new ServletRegistrationBean(
    		  initialServlet, "/InitialServlet");
      registration.setName("initialServlet");
      registration.addUrlMappings("/InitialServlet");
      return registration;
  }
  
  /**
  @Bean
  @Order(Ordered.HIGHEST_PRECEDENCE)
  public CharacterEncodingFilter characterEncodingFilter() {
      CharacterEncodingFilter filter = new CharacterEncodingFilter();
      filter.setEncoding("UTF-8");
      
      filter.setForceEncoding(true);
      return filter;
  }*/
  
  
  @Bean  
  public FilterRegistrationBean filterRegistrationBean() {
      FilterRegistrationBean registrationBean = new FilterRegistrationBean();
      ArrayList<String> match = new ArrayList<>();
      match.add("/*");
      CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
      characterEncodingFilter.setForceEncoding(true);
      characterEncodingFilter.setEncoding("UTF-8");
      characterEncodingFilter.setForceRequestEncoding(true);
      registrationBean.setFilter(characterEncodingFilter);
      registrationBean.setUrlPatterns(match);
      registrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE + 1);
      XssSqlFilter xsl = new XssSqlFilter();
      registrationBean.setFilter(xsl);
      
      //registrationBean.setUrlPatterns(match);
      //registrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE + 2);
      return registrationBean;
  }

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
	  registry.addInterceptor(localeChangeInterceptor());
	  logger.info("add interceptor:{}", li);
	  registry.addInterceptor(li)
	         //.addPathPatterns("/**");
	         .addPathPatterns("/project/*")
             .addPathPatterns("/supplier/**")
             .addPathPatterns("/trade/**")
             .addPathPatterns("/user/**")
             .addPathPatterns("/uploadFile") 
             .addPathPatterns("/uploadFile/**")
             .addPathPatterns("/auth/isUserInfoPerfect")
             .excludePathPatterns("/login/")
             .excludePathPatterns("/error/")
             .excludePathPatterns("/tbq/")
             .excludePathPatterns("/encrypt");    
  }
}

