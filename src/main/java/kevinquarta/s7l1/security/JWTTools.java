package kevinquarta.s7l1.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import kevinquarta.s7l1.entities.Dipendente;
import kevinquarta.s7l1.excpetions.UnauthroizedException;
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
               .signWith(Keys.hmacShaKeyFor(secret.getBytes())) //firmo il token fornendogli un segreto che il server conosce e che usa per creare e verificare token/verifica l'integrità del token,in modo che controlli che non sia stato manipolato
               .compact();
    }

    public void verifyToken(String token){
        try {
            Jwts.parser().verifyWith(Keys.hmacShaKeyFor(secret.getBytes())).build().parse(token);
            // Questi metodi ci possono lanciare diverse eccezioni, a seconda della problematica
            // Ci può lanciare una certa eccezione se il token dovesse essere scaduto,
            // un'altra se il token è stato manipolato (firma non valida)
            // un'altra ancora se il token dovesse essere malformato (es. manca una parte)
        } catch (Exception ex) {
            throw new UnauthroizedException("Problemi col token!");
        }
    }

    public long extractIdFromToken (String token){
       return Long.parseLong(Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(secret.getBytes()))
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject());
    }
}
