package com.reservapp.juanb.juanm.dto;

import java.util.UUID;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record PagoRequestDTO(
    @NotNull(message = "El ID de la reserva no puede ser nulo")
    UUID idReserva,

    @NotNull(message = "El monto no puede ser nulo")
    @Min(value = 1, message = "El monto debe ser mayor a 0")
    Double monto,

    @NotNull(message = "El ID del método no puede ser nulo")
    UUID idMetodo,

    @NotBlank(message = "El nombre del titular es obligatorio")
    String nombreTitular,

    @NotBlank(message = "El número de tarjeta es obligatorio")
    @Pattern(regexp = "^[0-9]{13,19}$", message = "El número de tarjeta debe tener entre 13 y 19 dígitos")
    String numeroTarjeta,

    @NotBlank(message = "La fecha de expiración es obligatoria")
    @Pattern(regexp = "^(0[1-9]|1[0-2])/[0-9]{2}$", message = "La fecha debe tener el formato MM/YY")
    String fechaExpiracion,

    @NotBlank(message = "El CVV es obligatorio")
    @Pattern(regexp = "^[0-9]{3,4}$", message = "El CVV debe tener 3 o 4 dígitos")
    String cvv
) {}