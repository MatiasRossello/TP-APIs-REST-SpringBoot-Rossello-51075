package com.utn.productos_api.dto;

import com.utn.productos_api.model.Categoria;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(name = "ProductoResponseDTO", description = "Respuesta con los datos completos del producto")
public class ProductoResponseDTO {

    @Schema(description = "Identificador del producto", example = "1")
    private Long id;

    @Schema(description = "Nombre del producto", example = "Teclado mecánico")
    private String nombre;

    @Schema(description = "Descripción del producto", example = "Switches rojos, formato TKL")
    private String descripcion;

    @Schema(description = "Precio del producto", example = "19999.99", minimum = "0.01")
    private Double precio;

    @Schema(description = "Stock disponible", example = "50", minimum = "0")
    private Integer stock;

    @Schema(description = "Categoría del producto (enum)", example = "ELECTRONICA")
    private Categoria categoria;
}
