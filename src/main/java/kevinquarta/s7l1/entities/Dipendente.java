package kevinquarta.s7l1.entities;


import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name="dipendenti")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Dipendente implements UserDetails{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private long id;

    private String nome;
    private String cognome;
    private String email;
    private String password;
    private String avatar;
    @Enumerated(EnumType.STRING)
    private Role role;

    public Dipendente(String nome, String cognome, String email,String password) {
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
        this.password = password;
        this.role = Role.USER;// IMPOSTIAMO OGNI REGISTRAZIONE COME USER,IN ALTERNATIVA SI FARà UN ENDPOINT PER CAMBIARE IN ADMIN O SUPERADMIN
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // restituisce una lista di ruoli
        // SimpleGrantedAuthority è una classe che ci permette di creare degli oggetti "ruolo" compatibili con questa collection
        // Quindi passiamo il valore dell'enum a quel costruttore
        return List.of(new SimpleGrantedAuthority(this.role.name()));
        // estrae l'enum e lo trasforma in stringa con .name()
    }

    @Override
    public String getUsername() {
        return this.email;
    }
}
