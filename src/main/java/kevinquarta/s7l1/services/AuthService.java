package kevinquarta.s7l1.services;

import kevinquarta.s7l1.entities.Dipendente;
import kevinquarta.s7l1.payloads.DipendenteDTO;
import kevinquarta.s7l1.payloads.LoginDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final DipendentiService dipendentiService;

    @Autowired
    public AuthService(DipendentiService dipendentiService) {
        this.dipendentiService = dipendentiService;
    }

    public String CheckCredentialsAndGenerateToken(LoginDTO body) {
        //1. controllo credenziali
        Dipendente found = this.dipendentiService.findByEmail(body.email());

        //2. genero il token

        //3. ritorno il token

        //4. se le credenziali non sono corrette ritorno 401 ERRORE

    }
}
