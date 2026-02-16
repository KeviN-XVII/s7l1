package kevinquarta.s7l1.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kevinquarta.s7l1.excpetions.UnauthroizedException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JWTCheckedFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//        QUESTO E' IL METODO ESEGUITO AD OGNI RICHIESTA
        // Sarà questo metodo quindi che dovrà fare il controllo dei token

        // PIANO DI BATTAGLIA

        // 1. Verifichiamo se la richiesta contiene l'header Authorization e che in caso sia nel formato "Bearer oi1j3oj21o3j213jo12j3"
        // Se l'header non c'è oppure se è malformato --> lanciamo eccezione
        String authHeader = request.getHeader("Authorization");
        if(authHeader == null) throw new UnauthroizedException("Inserire il token")

        // 2 estraiamo il token dall'header

        // 3 verifichiamo se il token è valido ( controllo firma e data scadenza)

        // 4. Se tutto è OK --> andiamo avanti, trasmettiamo la richiesta al prossimo (può essere o un altro elemento della catena oppure il controller)
        filterChain.doFilter(request, response);
        // 5. Se c'è qualche problema con il token -> eccezione
    }
}
