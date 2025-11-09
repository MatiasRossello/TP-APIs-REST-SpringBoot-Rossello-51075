package com.utn.productos_api.controller;

import com.utn.productos_api.dto.ActualizarStockDTO;
import com.utn.productos_api.dto.ProductoDTO;
import com.utn.productos_api.dto.ProductoResponseDTO;
import com.utn.productos_api.model.Categoria;
import com.utn.productos_api.model.Producto;
import com.utn.productos_api.service.ProductoService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

// === OpenAPI ===
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@RequestMapping("/api/productos")
@Tag(name = "Productos", description = "Controlador REST para gestión de productos")
public class ProductoController {

    private final ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @GetMapping
    @Operation(summary = "Listar productos", description = "Retorna todos los productos")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente"),
            @ApiResponse(responseCode = "400", description = "Parámetros inválidos")
    })
    public List<ProductoResponseDTO> listarProductos() {

        List<Producto> todos = productoService.obtenerTodos();

        List<ProductoResponseDTO> productoResponseDTOS = new ArrayList<>();

        todos.forEach(p -> {

            ProductoResponseDTO productoResponseDTO = setearDto(p);
            productoResponseDTOS.add(productoResponseDTO);

        });

        return productoResponseDTOS;
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener producto por ID", description = "Retorna un producto por su identificador")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Producto encontrado"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado"),
            @ApiResponse(responseCode = "400", description = "ID inválido")
    })
    public ProductoResponseDTO obtenerProductoPorId(@PathVariable @Min(1) Long id) {
        Producto producto = productoService.obtenerPorId(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no encontrado"));
        return setearDto(producto);
    }

    @GetMapping("/categoria/{categoria}")
    @Operation(summary = "Filtrar por categoría", description = "Retorna productos que pertenecen a la categoría indicada")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Listado filtrado correctamente"),
            @ApiResponse(responseCode = "400", description = "Categoría inválida")
    })
    public List<ProductoResponseDTO> obtenerProductoPorCategoria(@PathVariable Categoria categoria) {

        return productoService.obtenerPorCategoria(categoria).stream()
                .map(this::setearDto)
                .toList();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Crear producto", description = "Crea un nuevo producto")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Producto creado"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    public ProductoResponseDTO crearProducto(@Valid @RequestBody ProductoDTO productoDTO) {

        Producto producto = new Producto();

        producto.setCategoria(productoDTO.getCategoria());
        producto.setNombre(productoDTO.getNombre());
        producto.setPrecio(productoDTO.getPrecio());
        producto.setStock(productoDTO.getStock());
        producto.setDescripcion(productoDTO.getDescripcion());

        productoService.crearProducto(producto);

        return setearDto(producto);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar producto", description = "Actualiza un producto existente (reemplazo completo)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Producto actualizado"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    public void ActualizarProducto(@PathVariable Long id, @Valid @RequestBody ProductoDTO productoDTO) {

        Producto producto = new Producto();

        producto.setCategoria(productoDTO.getCategoria());
        producto.setNombre(productoDTO.getNombre());
        producto.setPrecio(productoDTO.getPrecio());
        producto.setStock(productoDTO.getStock());
        producto.setDescripcion(productoDTO.getDescripcion());

        productoService.actualizarProducto(id,producto);

    }

    @PatchMapping("/{id}/stock")
    @Operation(summary = "Actualizar stock", description = "Actualiza únicamente el stock del producto")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Stock actualizado"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    public void actualizarStockProducto(@PathVariable Long id, @Valid @RequestBody ActualizarStockDTO actualizarStockDTO) {

        productoService.actualizarStock(id, actualizarStockDTO.getStock());

    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Eliminar producto", description = "Elimina un producto por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Producto eliminado"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    public void eliminarProducto(@PathVariable Long id) {

        productoService.eliminarProducto(id);

    }

    public ProductoResponseDTO setearDto(Producto producto) {

        ProductoResponseDTO productoResponseDTO = new ProductoResponseDTO();

        productoResponseDTO.setId(producto.getId());
        productoResponseDTO.setNombre(producto.getNombre());
        productoResponseDTO.setDescripcion(producto.getDescripcion());
        productoResponseDTO.setPrecio(producto.getPrecio());
        productoResponseDTO.setStock(producto.getStock());
        productoResponseDTO.setCategoria(producto.getCategoria());

        return productoResponseDTO;

    }
}
