package kevinquarta.s7l1.entities;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name="viaggi")
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Viaggio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private long id;

    private String destinazione;
    private LocalDate dataViaggio;
    private String statoViaggio;

    public Viaggio(String destinazione, LocalDate dataViaggio) {
        this.destinazione = destinazione;
        this.dataViaggio = dataViaggio;
        this.statoViaggio = "IN PROGRAMMA";
    }
}
