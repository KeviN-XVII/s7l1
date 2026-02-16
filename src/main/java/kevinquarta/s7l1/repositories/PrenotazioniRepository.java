package kevinquarta.s7l1.repositories;


import kevinquarta.progettos6l5.entities.Prenotazione;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface PrenotazioniRepository extends JpaRepository<Prenotazione, Long> {
    boolean existsByDipendenteIdAndViaggioDataViaggio(long dipendenteId, LocalDate dataViaggio);

}
