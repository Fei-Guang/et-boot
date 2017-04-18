package glodon.etender;


import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.utils.StringUtil;

@ControllerAdvice
class GlobalDefaultExceptionHandler {
	
	
	protected Logger logger;

	public GlobalDefaultExceptionHandler() {
		logger = LoggerFactory.getLogger(getClass());
	}
  public static final String DEFAULT_ERROR_VIEW = "error";

  @ExceptionHandler(value = Throwable.class)
  public ModelAndView  defaultErrorHandler(HttpServletRequest req, Exception e) throws Exception {
    // If the exception is annotated with @ResponseStatus rethrow it and let
    // the framework handle it - like the OrderNotFoundException example
    // at the start of this post.
    // AnnotationUtils is a Spring Framework utility class.
	  logger.error("!!!!!!!!!!!!!!!!!!!!!!!");
	  e.printStackTrace();
    if (AnnotationUtils.findAnnotation(e.getClass(), ResponseStatus.class) != null)
      throw e;

    // Otherwise setup and send the user to a default error-view.
    ModelAndView mav = new ModelAndView();
    mav.addObject("exception", e);
    //mav.addObject("url", req.getRequestURL());
    mav.addObject("url", "/error/index");
    String type = req.getParameter("type");
	if (!StringUtil.isNull(type)) {
		req.setAttribute("type", type);
	}
	req.setAttribute("page", "error_index");
	//return "etender/template";
    //mav.setViewName(DEFAULT_ERROR_VIEW);
	mav.setViewName("etender/template");
    
    return mav;
  }
}