package com.healthcare.modules.patient.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record FindPatientsByArguments(

        @Size(min = 3, max = 150, message = "El campo nombre completo solo se puede buscar entre 3 y 150 caracteres")
        String fullName,

        @Size(max = 30, message = "Los n�meros telefonicos no exceden los 30 caracteres")
        @Pattern(
                regexp = "^[0-9+()\\-\\s]+$",
                message = "El numero de teléfono a buscar contiene caracteres no válidos"
        )
        String phone,

        @Size(max = 50, message = "El número de documento a buscar no excede los 50 caracteres")
        String documentNumber
) {
}
