package kevinquarta.s7l1.services;


import kevinquarta.progettos6l5.entities.Dipendente;
import kevinquarta.progettos6l5.entities.Prenotazione;
import kevinquarta.progettos6l5.entities.Viaggio;
import kevinquarta.progettos6l5.excpetions.BadRequestException;
import kevinquarta.progettos6l5.excpetions.NotFoundException;
import kevinquarta.progettos6l5.payloads.PrenotazioneDTO;
import kevinquarta.progettos6l5.repositories.DipendentiRepository;
import kevinquarta.progettos6l5.repositories.PrenotazioniRepository;
import kevinquarta.progettos6l5.repositories.ViaggioRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PrenotazioniService {

    private final DipendentiRepository dipendentiRepository;
    private final ViaggioRepository  viaggioRepository;
    private final PrenotazioniRepository  prenotazioniRepository;

    @Autowired
    public PrenotazioniService(DipendentiRepository dipendentiRepository, ViaggioRepository viaggioRepository, PrenotazioniRepository prenotazioniRepository) {
        this.dipendentiRepository = dipendentiRepository;
        this.viaggioRepository = viaggioRepository;
        this.prenotazioniRepository = prenotazioniRepository;
    }

    //    SALVA PRENOTAZIONE
    public Prenotazione savePrenotazione (PrenotazioneDTO payload) {
//        RICERCO DIPENDENTE/VIAGGIO
        Dipendente dipendente = dipendentiRepository.findById(payload.dipendenteId())
                .orElseThrow(()->new NotFoundException(payload.dipendenteId()));
        Viaggio viaggio = viaggioRepository.findById(payload.viaggioId())
                .orElseThrow(()->new NotFoundException(payload.viaggioId()));

//        CONTROLLO
        if(this.prenotazioniRepository.existsByDipendenteIdAndViaggioDataViaggio(dipendente.getId(),viaggio.getDataViaggio()))throw new BadRequestException("Non è possibile completare la prenotazione,Data Viaggio già occupata");

//        NUOVA PRENOTAZIONE
        Prenotazione newPrenotazione = new Prenotazione(
                viaggio,
                dipendente,
                payload.preferenze()
             );
//       SALVA PRENOTAZIONE
        Prenotazione savedPrenotazione = prenotazioniRepository.save(newPrenotazione);
//        LOG
        log.info("La prenotazione per " + newPrenotazione.getViaggio().getDestinazione() +" in data "+ newPrenotazione.getDataDiRichiesta() + " è stata salvata correttamente!");
        return savedPrenotazione;
    }

    //    CERCA PRENOTAZIONE
    public Prenotazione findById(Long prenotazioneId){
        return this.prenotazioniRepository.findById(prenotazioneId).orElseThrow(()-> new NotFoundException(prenotazioneId));
    }

    //    CERCA TUTTE LE PRENOTAZIONI
    public Page<Prenotazione> findAll(int page, int size, String orderBy, String sortCriteria) {
    if (size > 100 || size < 0) size = 10;
    if (page < 0) page = 0;
    Pageable pageable = PageRequest.of(page, size,
            sortCriteria.equals("desc") ? Sort.by(orderBy).descending() : Sort.by(orderBy));
    return this.prenotazioniRepository.findAll(pageable);
}

    //    ELIMINA BLOG
    public void deletePrenotazione(Long prenotazioneId){
        Prenotazione found = findById(prenotazioneId);
        prenotazioniRepository.delete(found);
    }

}
