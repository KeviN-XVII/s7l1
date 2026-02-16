package kevinquarta.s7l1.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;


@Entity
@Table(name="prenotazioni")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Prenotazione {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private long id;

    @ManyToOne
    @JoinColumn(name = "viaggio_id")
    private Viaggio viaggio;

    @ManyToOne
    @JoinColumn(name = "dipendente_id")
    private Dipendente dipendente;

    private LocalDate dataDiRichiesta;
    private String preferenze;

    public Prenotazione(Viaggio viaggio, Dipendente dipendente, String preferenze) {
        this.viaggio = viaggio;
        this.dipendente = dipendente;
        this.preferenze = preferenze;
        this.dataDiRichiesta = LocalDate.now();
    }
}
