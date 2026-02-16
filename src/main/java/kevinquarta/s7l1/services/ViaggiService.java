package kevinquarta.s7l1.services;


import kevinquarta.progettos6l5.entities.Viaggio;
import kevinquarta.progettos6l5.excpetions.NotFoundException;
import kevinquarta.progettos6l5.payloads.ViaggioDTO;
import kevinquarta.progettos6l5.payloads.ViaggioStatoDTO;
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
public class ViaggiService {

    private final ViaggioRepository viaggioRepository;

    @Autowired
    public ViaggiService(ViaggioRepository viaggioRepository) {
        this.viaggioRepository = viaggioRepository;
    }

//    SALVA VIAGGIO
    public Viaggio saveViaggio(ViaggioDTO payload){
        //        NUOVO VIAGGIO
        Viaggio newViaggio = new Viaggio(payload.destinazione(), payload.dataViaggio());
//       SALVO VIAGGIO
        Viaggio savedViaggio = viaggioRepository.save(newViaggio);
//       LOG
        log.info("Il Viaggio con destinazione " + savedViaggio.getDestinazione() +" è stato salvato correttamente");

        return savedViaggio;
    }


    //    RICERCA VIAGGIO PER ID
    public Viaggio findById(Long viaggioId){
        return this.viaggioRepository.findById(viaggioId)
                .orElseThrow(()-> new NotFoundException(viaggioId));
    }

    //    RICERCA TUTTI I VIAGGI
    public Page<Viaggio> findAll(int page, int size, String orderBy, String sortCriteria) {
        if (size > 100 || size < 0) size = 10;
        if (page < 0) page = 0;
        Pageable pageable = PageRequest.of(page, size,
                sortCriteria.equals("desc") ? Sort.by(orderBy).descending() : Sort.by(orderBy));
        return this.viaggioRepository.findAll(pageable);
    }


//    MODIFICA STATO
    public Viaggio findByIdAndUpdateStato (Long viaggioId,ViaggioStatoDTO payload){
//        RICERCO VIAGGIO
        Viaggio found = this.findById(viaggioId);
//        MODIFICO STATO VIAGGIO
        found.setStatoViaggio(payload.statoViaggio());
//        SAVE STATO VIAGGIO
        Viaggio modifiedStato = viaggioRepository.save(found);
//        LOG
        log.info("Lo stato è stato modificato correttamente");
//        RETURN STATO VIAGGIO MODIFICATO
        return modifiedStato;
    }





//        MODIFICA VIAGGIO
    public Viaggio findByIdAndUpdate(Long viaggioId,ViaggioDTO payload){
//        RICERCO VIAGGIO
        Viaggio found = this.findById(viaggioId);
//        MODIFICO VIAGGIO
        found.setDestinazione(payload.destinazione());
        found.setDataViaggio(payload.dataViaggio());
//        SAVE VIAGGIO
        Viaggio modifiedViaggio = viaggioRepository.save(found);
//        LOG
        log.info("Il Viaggio è stato modificato correttamente!");
//        RETURN VIAGGIO MODIFICATO
        return modifiedViaggio;
    }


    //    ELIMINA VIAGGIO
    public void findByIdAndDelete(Long viaggioId){
        Viaggio found = this.findById(viaggioId);
        this.viaggioRepository.delete(found);
    }







}
