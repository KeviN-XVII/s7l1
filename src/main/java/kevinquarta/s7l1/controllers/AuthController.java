package kevinquarta.s7l1.controllers;


import kevinquarta.s7l1.entities.Dipendente;
import kevinquarta.s7l1.excpetions.ValidationException;
import kevinquarta.s7l1.payloads.DipendenteDTO;
import kevinquarta.s7l1.payloads.LoginDTO;
import kevinquarta.s7l1.payloads.ResponseLoginDTO;
import kevinquarta.s7l1.services.AuthService;
import kevinquarta.s7l1.services.DipendentiService;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final DipendentiService  dipendentiService;

    public AuthController(AuthService authService, DipendentiService dipendentiService) {
        this.authService = authService;
        this.dipendentiService = dipendentiService;
    }

    @PostMapping("/login")
    public ResponseLoginDTO login (@RequestBody LoginDTO body) {
        return new ResponseLoginDTO(this.authService.CheckCredentialsAndGenerateToken(body));
    }

    //    POST /auth/register crea nuovo dipendente
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public Dipendente saveDipendente(@RequestBody @Validated DipendenteDTO payload, BindingResult validationResult){
        if(validationResult.hasErrors()){
            List<String> errorsList = validationResult.getFieldErrors()
                    .stream()
                    .map(fieldError -> fieldError.getDefaultMessage())
                    .toList();
            throw new ValidationException(errorsList);
        } else {
            return this.dipendentiService.saveDipendente(payload);
        }
    }



}
