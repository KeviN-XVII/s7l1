package kevinquarta.s7l1.controllers;


import kevinquarta.s7l1.entities.Dipendente;
import kevinquarta.s7l1.excpetions.ValidationException;
import kevinquarta.s7l1.payloads.DipendenteDTO;
import kevinquarta.s7l1.services.DipendentiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/dipendenti")
public class DipendentiController {

    public DipendentiService  dipendentiService;

    @Autowired
    public DipendentiController(DipendentiService dipendentiService) {
        this.dipendentiService = dipendentiService;
    }



//  GET /dipendenti ritorna lista di dipendenti
     @GetMapping
     @PreAuthorize("hasAnyAuthority('SUPERADMIN', 'ADMIN')") // Solo admin o superadmin possono visualizzare la lista degli utenti
     public Page<Dipendente> findAll(@RequestParam(defaultValue = "0")int page,
                          @RequestParam(defaultValue = "10")int size,
                          @RequestParam(defaultValue = "nome")String orderBy,
                          @RequestParam(defaultValue = "asc")String sortCriteria) {
     return dipendentiService.findAll(page, size, orderBy, sortCriteria);
     }

//   GET /dipendenti/123 ritorna un singolo dipendente
     @GetMapping("/{dipendenteId}")
     public Dipendente getDipendenteById(@PathVariable long dipendenteId) {
        return dipendentiService.findById(dipendenteId);
     }

//     PUT /dipendenti/123 modifica lo specifico autore
    @PutMapping("/{dipendenteId}")
    @PreAuthorize("hasAnyAuthority('SUPERADMIN', 'ADMIN')")
    public Dipendente updateDipendente(@PathVariable long dipendenteId, @RequestBody @Validated DipendenteDTO payload,BindingResult validationResult) {
        if(validationResult.hasErrors()){
            List<String> errorsList = validationResult.getFieldErrors()
                    .stream()
                    .map(fieldError -> fieldError.getDefaultMessage())
                    .toList();
            throw new ValidationException(errorsList);
        } else {
            return this.dipendentiService.findByIdAndUpdate(dipendenteId, payload);
        }
    }
    // TODO: Utilizzare @AuthenticationPrincipal
    // PUT / dell'utente autenticato,utilizzando @AuthenticationPrincipal

    @PutMapping("/me")
    public Dipendente updateProfile(@AuthenticationPrincipal Dipendente currentAuthenticatedDipendente, @RequestBody @Validated DipendenteDTO payload, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            List<String> errorsList = validationResult.getFieldErrors()
                    .stream()
                    .map(fieldError -> fieldError.getDefaultMessage())
                    .toList();
            throw new ValidationException(errorsList);
        } else {
            return this.dipendentiService.findByIdAndUpdate(currentAuthenticatedDipendente.getId(), payload);
            // TODO : utilizziamo .getId()
        }
    }

//     DELETE /dipendenti/123 elimina un dipendente specifico
    @DeleteMapping("/{dipendenteId}")
    @PreAuthorize("hasAnyAuthority('SUPERADMIN', 'ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteDipendente(@PathVariable long dipendenteId) {
        this.dipendentiService.findByIdAndDelete(dipendenteId);
    }

//    UPLOAD IMAGE
    @PatchMapping("/{dipendenteId}/avatar")
    public Dipendente uploadImage(@RequestParam("profile_picture") MultipartFile file, @PathVariable long dipendenteId){

        Dipendente url=this.dipendentiService.uploadAvatar(dipendenteId,file);

        return url;
    }









}
