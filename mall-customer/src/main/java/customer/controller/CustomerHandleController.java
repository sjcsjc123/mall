package customer.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class CustomerHandleController {

    @RequestMapping("/test1")
    public String test1(){
        return "hello";
    }
}
