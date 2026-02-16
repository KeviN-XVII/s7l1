package kevinquarta.s7l1.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kevinquarta.s7l1.excpetions.UnauthroizedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JWTCheckedFilter extends OncePerRequestFilter {

    private final JWTTools jwtTools;

    @Autowired
    public JWTCheckedFilter(JWTTools jwtTools) {
        this.jwtTools = jwtTools;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//        QUESTO E' IL METODO ESEGUITO AD OGNI RICHIESTA
        // Sarà questo metodo quindi che dovrà fare il controllo dei token

        // 1. Verifichiamo se la richiesta contiene l'header Authorization e che in caso sia nel formato "Bearer oi1j3oj21o3j213jo12j3"
        // Se l'header non c'è oppure se è malformato --> lanciamo eccezione
        String authHeader = request.getHeader("Authorization");
        if(authHeader == null|| !authHeader.startsWith("Bearer "))
            throw new UnauthroizedException("Inserire il token nell'Authorization header");

        // 2 estraiamo il token dall'header
        String accessToken = authHeader.replace("Bearer ", "");

        // 3 verifichiamo se il token è valido ( controllo firma e data scadenza)
        jwtTools.verifyToken(accessToken);
        // 4. Se tutto è OK --> andiamo avanti, trasmettiamo la richiesta al prossimo (può essere o un altro elemento della catena oppure il controller)
        filterChain.doFilter(request, response);
        // 5. Se c'è qualche problema con il token -> eccezione
    }
    // Tramite l'Override del metodo sottostante posso specificare quando il filtro non debba essere chiamato in causa
    // Ad esempio posso dirgli di non filtrare tutte le richieste dirette al controller "/auth"
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return new AntPathMatcher().match("/auth/**", request.getServletPath());
       // questo esclude direttamente tutti gli endpoint con /auth/..
    }
}
