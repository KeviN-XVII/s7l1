package kevinquarta.s7l1.payloads;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record PrenotazioneDTO(
        @NotNull(message="L'id viaggio è obbligatorio")
        Long viaggioId,
        @NotNull(message="L'id dipendente è obbligatorio")
        Long dipendenteId,
        @NotBlank(message="Le preferenze sono obbligatorie")
        @Size(min=2, max=500)
        String preferenze
) {
}
