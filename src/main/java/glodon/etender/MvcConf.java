package glodon.etender;

import java.util.Collections;
import java.util.List;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.mvc.annotation.AnnotationMethodHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;



//@Configuration
class MvcConf extends WebMvcConfigurationSupport {
	
	private static Logger logger = LoggerFactory.getLogger(MvcConf.class);	  
	  
	  @Override
	  public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
	      converters.add(converter());
	      addDefaultHttpMessageConverters(converters);
	      logger.info("###################json convert");
	  }

	  @Bean
	  public AnnotationMethodHandlerAdapter annotationMethodHandlerAdapter()
	  {
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
		  ObjectMapper objectMapper = new ObjectMapper();
		  objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		  converter.setObjectMapper(objectMapper);		  
	      // converter.setSupportedMediaTypes(Collections.singletonList(
	    		    //MediaType.valueOf("text/html;charset=UTF-8")));
	      return converter;
	  }
	  

	  @EventListener
	  public void on(ContextRefreshedEvent event) {
	      RequestMappingHandlerAdapter requestMappingHandlerAdapter =     this.getApplicationContext().getBean(RequestMappingHandlerAdapter.class);
	      List<?> messageConverters = requestMappingHandlerAdapter.getMessageConverters();
	      StringBuilder sb = new StringBuilder();
	      sb.append("Spring共加载了").append(messageConverters.size()).append("个消息转换器对象：").append(messageConverters.toString());
	      logger.info(sb.toString());
	  }
}
