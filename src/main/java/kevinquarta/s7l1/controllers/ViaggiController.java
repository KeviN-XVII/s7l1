package kevinquarta.s7l1.controllers;


import kevinquarta.progettos6l5.entities.Viaggio;
import kevinquarta.progettos6l5.excpetions.ValidationException;
import kevinquarta.progettos6l5.payloads.ViaggioDTO;
import kevinquarta.progettos6l5.payloads.ViaggioStatoDTO;
import kevinquarta.progettos6l5.services.ViaggiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/viaggi")
public class ViaggiController {


    private final ViaggiService viaggiService;

    @Autowired
    public ViaggiController(ViaggiService viaggiService) {
        this.viaggiService = viaggiService;
    }

    //    POST /viaggio crea nuovo viaggio
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Viaggio saveViaggio(@RequestBody @Validated ViaggioDTO payload, BindingResult validationResult){
        if(validationResult.hasErrors()){
            List<String> errorsList = validationResult.getFieldErrors()
                    .stream()
                    .map(fieldError -> fieldError.getDefaultMessage())
                    .toList();
            throw new ValidationException(errorsList);
        } else {
            return this.viaggiService.saveViaggio(payload);
        }
    }

    //  GET /viaggio ritorna lista di viaggi
    @GetMapping
    public Page<Viaggio> findAll(@RequestParam(defaultValue = "0")int page,
                                    @RequestParam(defaultValue = "10")int size,
                                    @RequestParam(defaultValue = "destinazione")String orderBy,
                                    @RequestParam(defaultValue = "asc")String sortCriteria) {
        return viaggiService.findAll(page, size, orderBy, sortCriteria);
    }

    //   GET /viaggio/123 ritorna un singolo viaggio
    @GetMapping("/{viaggioId}")
    public Viaggio getViaggioById(@PathVariable long viaggioId) {
        return viaggiService.findById(viaggioId);
    }

    //     PUT /viaggio/123 modifica lo specifico viaggio
    @PutMapping("/{viaggioId}")
    public Viaggio updateViaggio(@PathVariable long viaggioId, @RequestBody @Validated ViaggioDTO payload,BindingResult validationResult) {
        if(validationResult.hasErrors()){
            List<String> errorsList = validationResult.getFieldErrors()
                    .stream()
                    .map(fieldError -> fieldError.getDefaultMessage())
                    .toList();
            throw new ValidationException(errorsList);
        } else {
            return this.viaggiService.findByIdAndUpdate(viaggioId, payload);
        }
    }

    //     PUT /viaggio/123/stato modifica lo stato di un viaggio
    @PutMapping("/{viaggioId}/stato")
    public Viaggio updateStatoViaggio(@PathVariable long viaggioId, @RequestBody @Validated ViaggioStatoDTO payload, BindingResult validationResult) {
        if(validationResult.hasErrors()){
            List<String> errorsList = validationResult.getFieldErrors()
                    .stream()
                    .map(fieldError -> fieldError.getDefaultMessage())
                    .toList();
            throw new ValidationException(errorsList);
        } else {
            return this.viaggiService.findByIdAndUpdateStato(viaggioId,payload);
        }
    }

    //     DELETE /viaggio/123 elimina un viaggio specifico
    @DeleteMapping("/{viaggioId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteViaggio(@PathVariable long viaggioId) {
        this.viaggiService.findByIdAndDelete(viaggioId);
    }





}
