package kevinquarta.s7l1.controllers;


import kevinquarta.s7l1.payloads.LoginDTO;
import kevinquarta.s7l1.payloads.ResponseLoginDTO;
import kevinquarta.s7l1.services.AuthService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseLoginDTO login (@RequestBody LoginDTO body) {
        return new ResponseLoginDTO(this.authService.CheckCredentialsAndGenerateToken(body));
    }
}
