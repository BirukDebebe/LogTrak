package logtrak_crm.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {
	 @GetMapping("/index")
	    public String index() {
	        return "index"; 
	    }

	    @GetMapping("/")
	    public String root() {
	        return "redirect:/index";
	    }

}
