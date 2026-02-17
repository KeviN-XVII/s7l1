package kevinquarta.s7l1.services;

import kevinquarta.s7l1.entities.Dipendente;
import kevinquarta.s7l1.excpetions.UnauthroizedException;
import kevinquarta.s7l1.payloads.DipendenteDTO;
import kevinquarta.s7l1.payloads.LoginDTO;
import kevinquarta.s7l1.security.JWTTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final DipendentiService dipendentiService;
//        porto JWTTOLS
    private final JWTTools jwtTools;
    private final PasswordEncoder bcrypt;

    @Autowired
    public AuthService(DipendentiService dipendentiService, JWTTools jwtTools,PasswordEncoder bcrypt) {
        this.dipendentiService = dipendentiService;
        this.jwtTools = jwtTools;
        this.bcrypt = bcrypt;
    }

    public String CheckCredentialsAndGenerateToken(LoginDTO body) {
        //1. controllo credenziali
        Dipendente found = this.dipendentiService.findByEmail(body.email());


        // TODO: bcrypt.matches(body.password(), found.getPassword()) IMPORTANTE FARE PRIMA BODY

        if(bcrypt.matches(body.password(), found.getPassword())) {
            //2.1 se credenziali ok
        //2. genero il token
            String accessToken= jwtTools.generateToken(found);

        //3. ritorno il token
            return accessToken;
        }
            else {
                throw new UnauthroizedException("Credenziali Errate");
        }

        //4. se le credenziali non sono corrette ritorno 401 ERRORE

    }
}
