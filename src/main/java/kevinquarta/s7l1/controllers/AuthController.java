package kevinquarta.s7l1.controllers;


import kevinquarta.s7l1.payloads.LoginDTO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @PostMapping("/login")
    public String login (@RequestBody LoginDTO body) {







        return "TOKEN";
    }
}
