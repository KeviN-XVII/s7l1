package kevinquarta.s7l1.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import kevinquarta.s7l1.entities.Dipendente;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JWTTools {
    @Value("${jwt.secret}")
    private String secret;
    // Jwts (proviene da jjwt-api) fornisce principalmente 2 metodi: builder() e parser(), il primo lo usiamo per creare i token, il secondo
    // per leggerli (ed estrarre info da essi) e validarli

    public String generateToken(Dipendente dipendente){
       return Jwts.builder().issuedAt(new Date(System.currentTimeMillis())) // Data di emissione (IaT - Issued At), va messa in millisecondi
               .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 7)) // Data di scadenza (Expiration Date) anche questa va messa in millisecondi
               .subject(String.valueOf(dipendente.getId())) // Subject cioè a chi appartiene il token. Ci inseriamo l'id dell'utente (MAI METTERE DATI SENSIBILI AL SUO INTERNO)
               .signWith(Keys.hmacShaKeyFor(secret.getBytes())) //firmo il token fornendogli un segreto che il server conosce e che usa per creare e verificare token
               .compact();
    }

    public void verifyToker(){
    }

}
