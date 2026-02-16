package kevinquarta.s7l1.payloads;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record DipendenteDTO(
        @NotBlank(message="Il nome è un campo obbligatorio")
        @Size(min=3, max=30,message = "Il nome proprio deve essere tra i 3 e i 30 caratteri")
        String nome,
        @NotBlank(message="Il nome è un campo obbligatorio")
        @Size(min=3, max=30,message = "Il nome proprio deve essere tra i 3 e i 30 caratteri")
        String cognome,
        @NotBlank(message="Il nome è un campo obbligatorio")
        @Size(min=3, max=30,message = "Il nome proprio deve essere tra i 3 e i 30 caratteri")
        String email
) {
}