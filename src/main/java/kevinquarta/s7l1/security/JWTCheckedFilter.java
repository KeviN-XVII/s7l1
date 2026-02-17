package kevinquarta.s7l1.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kevinquarta.s7l1.entities.Dipendente;
import kevinquarta.s7l1.excpetions.UnauthroizedException;
import kevinquarta.s7l1.services.DipendentiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JWTCheckedFilter extends OncePerRequestFilter {

    private final JWTTools jwtTools;
    private final DipendentiService  dipendentiService;

    @Autowired
    public JWTCheckedFilter(JWTTools jwtTools, DipendentiService dipendentiService) {
        this.jwtTools = jwtTools;
        this.dipendentiService = dipendentiService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//        QUESTO E' IL METODO ESEGUITO AD OGNI RICHIESTA
        // Sarà questo metodo quindi che dovrà fare il controllo dei token
//----------------------------------------- AUTENTICAZIONE --------------------------------


        // 1. Verifichiamo se la richiesta contiene l'header Authorization e che in caso sia nel formato "Bearer oi1j3oj21o3j213jo12j3"
        // Se l'header non c'è oppure se è malformato --> lanciamo eccezione
        String authHeader = request.getHeader("Authorization");
        if(authHeader == null|| !authHeader.startsWith("Bearer "))
            throw new UnauthroizedException("Inserire il token nell'Authorization header");

        // 2 estraiamo il token dall'header
        String accessToken = authHeader.replace("Bearer ", "");

        // 3 verifichiamo se il token è valido ( controllo firma e data scadenza)
        jwtTools.verifyToken(accessToken);

//        ----------------------------------------- AUTORIZZAZIONE --------------------------------

        // 1. Cerchiamo l'utente nel DB tramite id (l'id sta nel token!)
        // 1.1 Leggiamo l'id dal token
        // ANDIAMO IN JWTTOOLS E FACCIAMO UN METODO PER TORNARE UN TOKEN
        long dipendenteId = jwtTools.extractIdFromToken(accessToken);
        // 1.2 Find by Id
        Dipendente authenticatedDipendente = dipendentiService.findById(dipendenteId);

        // 2. Associamo l'utente al Security Context
        // E' uno step fondamentale che serve per associare l'utente che sta effettuando la richiesta (il proprietario del token) alla richiesta
        // stessa per tutta la sua durata, cioè fino a che essa non ottiene una risposta
        // Così facendo chiunque arriverà dopo questo filtro, altri filtri, il controller, un endpoint... potrà risalire a chi sia l'utente che
        // ha effettuato la richiesta
        // Questo è molto utile per ad esempio controllare i ruoli dell'utente prima di arrivare ad uno specifico endpoint. Oppure ci può servire
        // per effettuare determinati controlli all'interno degli endpoint stessi per verificare che chi stia facendo una certa operazione di
        // lettura/modifica/cancellazione sia l'effettivo proprietario della risorsa, oppure per, in fase di creazione di una risorsa, associare
        // l'effettivo proprietario a tale risorsa.

        Authentication authentication = new UsernamePasswordAuthenticationToken(authenticatedDipendente,null,authenticatedDipendente.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authentication);
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
