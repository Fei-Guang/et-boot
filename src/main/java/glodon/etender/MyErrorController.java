package glodon.etender;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.utils.StringUtil;


@Controller
public class MyErrorController implements ErrorController {

    private static final String ERROR_MAPPING = "/error";
    
    protected Logger logger=LoggerFactory.getLogger(getClass());

    @RequestMapping(value = ERROR_MAPPING)
    public String error(HttpServletRequest request) {
    	logger.info("trace into error controller...");
    	String type = request.getParameter("type");
		if (!StringUtil.isNull(type)) {
			request.setAttribute("type", type);
		}
		request.setAttribute("page", "error_index");
		return "etender/template";
    }

    @Override
    public String getErrorPath() {
        return ERROR_MAPPING;
    }

}
