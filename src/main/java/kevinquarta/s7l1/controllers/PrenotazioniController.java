package kevinquarta.s7l1.controllers;


import kevinquarta.s7l1.entities.Prenotazione;
import kevinquarta.s7l1.excpetions.ValidationException;
import kevinquarta.s7l1.payloads.PrenotazioneDTO;
import kevinquarta.s7l1.services.PrenotazioniService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/prenotazioni")
public class PrenotazioniController {

    private final PrenotazioniService prenotazioniService;

    @Autowired
    public PrenotazioniController(PrenotazioniService prenotazioniService) {
        this.prenotazioniService = prenotazioniService;
    }

    //    POST /prenotazioni crea nuova prenotazione
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Prenotazione savePrenotazione(@RequestBody @Validated PrenotazioneDTO payload, BindingResult validationResult){
        if(validationResult.hasErrors()){
            List<String> errorsList = validationResult.getFieldErrors()
                    .stream()
                    .map(fieldError -> fieldError.getDefaultMessage())
                    .toList();
            throw new ValidationException(errorsList);
        } else {
            return this.prenotazioniService.savePrenotazione(payload);
        }
    }

    //  GET /prenotazioni ritorna lista di prenotazioni
    @GetMapping
    public Page<Prenotazione> findAll(@RequestParam(defaultValue = "0")int page,
                                 @RequestParam(defaultValue = "10")int size,
                                 @RequestParam(defaultValue = "dipendente")String orderBy,
                                 @RequestParam(defaultValue = "asc")String sortCriteria) {
        return prenotazioniService.findAll(page, size, orderBy, sortCriteria);
    }

    //   GET /prenotazioni/123 ritorna un singola prenotazione
    @GetMapping("/{prenotazioniId}")
    public Prenotazione getPrenotazioneById(@PathVariable long prenotazioniId) {
        return prenotazioniService.findById(prenotazioniId);
    }

    //     DELETE /prenotazioni/123 elimina una prenotazione specifica
    @DeleteMapping("/{prenotazioniId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePrenotazione(@PathVariable long prenotazioneId) {
        this.prenotazioniService.deletePrenotazione(prenotazioneId);
    }






}
