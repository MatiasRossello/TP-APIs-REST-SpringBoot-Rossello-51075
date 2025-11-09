package com.utn.productos_api.dto;

import com.utn.productos_api.model.Categoria;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(name = "ProductoDTO", description = "Payload para crear/actualizar productos")
public class ProductoDTO {

    @Schema(description = "Nombre del producto", example = "Teclado mecánico", minLength = 3, maxLength = 100, requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank
    @Size(min = 3, max = 100)
    private String nombre;

    @Schema(description = "Descripción del producto (máx. 500 caracteres)", example = "Switches rojos, formato TKL", maxLength = 500)
    @Size(max = 500)
    private String descripcion;

    @Schema(description = "Precio del producto (mínimo 0.01)", example = "19999.99", minimum = "0.01", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull
    @DecimalMin("0.01")
    private Double precio;

    @Schema(description = "Stock disponible (no negativo)", example = "50", minimum = "0", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull
    @Min(0)
    private Integer stock;

    @Schema(description = "Categoría del producto (enum)", example = "ELECTRONICA", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull
    private Categoria categoria;
}
