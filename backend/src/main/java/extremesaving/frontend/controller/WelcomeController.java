package extremesaving.frontend.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Date;

@Controller
public class WelcomeController {


    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index(Model model) {
        model.addAttribute("msg", getMessage());
        model.addAttribute("today", new Date());
        return "index";

    }

    private String getMessage() {
        return "Hello World";
    }
}