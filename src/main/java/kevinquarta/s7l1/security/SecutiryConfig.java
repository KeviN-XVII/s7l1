package kevinquarta.s7l1.security;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity // sarà una classe apposita per Spring Security
@EnableMethodSecurity // Annotazione OBBLIGATORIA se vogliamo usare le varie @PreAuthorize sugli endpoint
public class SecutiryConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) {
        // disabilito il login
        // Non voglio l'autenticazione via form
        // proposta di default da Spring Security (avremo frontend React per quello)
        httpSecurity.formLogin(formLogin->formLogin.disable());
        // va tolta la protezione,non ci serve e va disabilitata
        httpSecurity.csrf(csrf->csrf.disable());
        // L'autenticazione JWT è l'esatto opposto del lavorare tramite sessioni, quindi lavoriamo in modalità STATELESS
        httpSecurity.sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));


        // Disabilitiamo il meccanismo di default di Spring Security che ritorna 401 ad ogni richiesta.
        // Siccome andremo ad implementare un meccanismo di autenticazione custom, sarà il nostro metodo di autenticazione e controllo a proteggere
        // i vari endpoint, non Security direttamente
        // PERSONALIZZIAMO IL COMPORTAMENTO DI FUNZIONALITA' PRE-ESISTENTI
        httpSecurity.authorizeHttpRequests(req->req.requestMatchers("/**").permitAll());
        return httpSecurity.build();
    }
}
