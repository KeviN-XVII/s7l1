package kevinquarta.s7l1.services;


import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import kevinquarta.s7l1.entities.Dipendente;
import kevinquarta.s7l1.excpetions.BadRequestException;
import kevinquarta.s7l1.excpetions.NotFoundException;
import kevinquarta.s7l1.payloads.DipendenteDTO;
import kevinquarta.s7l1.repositories.DipendentiRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
@Slf4j
public class DipendentiService {
    private final DipendentiRepository dipendentiRepository;
    private final Cloudinary cloudinaryUploader;

    @Autowired
    public DipendentiService(DipendentiRepository dipendentiRepository, Cloudinary cloudinaryUploader) {
        this.dipendentiRepository = dipendentiRepository;
        this.cloudinaryUploader = cloudinaryUploader;

    }
//     RICERCA SE DIPENDENTE CON EMAIL E' GIA ESISTENTE
// DA USARE IN AUTHSERVICE
    public Dipendente findByEmail (String email) {
        return this.dipendentiRepository.findByEmail(email).orElseThrow(()-> new NotFoundException("L'utente con email " + email + " non è stato trovato!"));
    }

    // SALVA DIPENDENTE

    public Dipendente saveDipendente(DipendenteDTO payload){
//        CONTROLLO EMAIL
        this.dipendentiRepository.findByEmail(payload.email()).ifPresent(dipendente -> {
            throw new BadRequestException("L'email "+ dipendente.getEmail() + " è già registrata!");});
//        NUOVO USER
        Dipendente newDipendente = new Dipendente(payload.nome(), payload.cognome(), payload.email(),payload.password());
        newDipendente.setAvatar("https://ui-avatars.com/api/?name="+payload.nome()+"+"+payload.cognome());
//        SALVO
        Dipendente savedDipendente = dipendentiRepository.save(newDipendente);
//       LOG
        log.info("L'utente " + newDipendente.getNome() +" "+ newDipendente.getCognome() + " è stato salvato correttamente!");
        return savedDipendente;
    }

    //    RICERCA DIPENDENTE PER ID
    public Dipendente findById(Long dipendenteId){
        return this.dipendentiRepository.findById(dipendenteId)
                .orElseThrow(()-> new NotFoundException(dipendenteId));
    }

    //    RICERCA TUTTI DIPENDENTI
    public Page<Dipendente> findAll(int page, int size, String orderBy, String sortCriteria) {
        if (size > 100 || size < 0) size = 10;
        if (page < 0) page = 0;
        Pageable pageable = PageRequest.of(page, size,
                sortCriteria.equals("desc") ? Sort.by(orderBy).descending() : Sort.by(orderBy));
        return this.dipendentiRepository.findAll(pageable);

    }

    //    MODIFICA DIPENDENTE
    public Dipendente findByIdAndUpdate(Long userId, DipendenteDTO payload){
//       CERCO DIPENDENTE
        Dipendente found = this.findById(userId);
//        VALIDAZIONE DATI
        if(!found.getEmail().equals(payload.email()))this.dipendentiRepository.findByEmail(payload.email()).ifPresent(dipendente -> {
            throw new BadRequestException("L'email "+dipendente.getEmail()+" è già in uso!");
        });
//        MODIFICO DIPENDENTE
        found.setNome(payload.nome());
        found.setCognome(payload.cognome());
        found.setEmail(payload.email());
        found.setAvatar("https://ui-avatars.com/api?name=" + payload.nome() + "+" + payload.cognome());
//        SALVO
        Dipendente modifiedDipendente = dipendentiRepository.save(found);
//        LOG
        log.info("L'utente con id "+modifiedDipendente.getId()+" è stato modificato con successo!");
//  RETURN DIPENDENTE MODIFICATO
        return modifiedDipendente;
    }


    //    ELIMINA UTENTE
    public void findByIdAndDelete(Long userId){
        Dipendente found = this.findById(userId);
        this.dipendentiRepository.delete(found);
    }

    //    UPLOAD AVATAR DIPENDENTE
    public Dipendente uploadAvatar(Long dipendenteId, MultipartFile file){
//        find by id dipendente
        Dipendente found = this.findById(dipendenteId);
//        upload del file cloudinary
        try {
            Map result = cloudinaryUploader.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());

            String imageUrl = (String) result.get("secure_url");

            found.setAvatar(imageUrl);


            return dipendentiRepository.save(found);


        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }
}
