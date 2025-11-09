package com.utn.productos_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(name = "ActualizarStockDTO", description = "Payload para actualizar únicamente el stock de un producto")
public class ActualizarStockDTO {

    @Schema(description = "Cantidad de unidades en stock (no negativo)", example = "25", minimum = "0", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull
    @Min(0)
    private Integer stock;
}
