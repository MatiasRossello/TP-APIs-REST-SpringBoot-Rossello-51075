package com.utn.productos_api.service;

import com.utn.productos_api.exception.ProductoNotFoundException;
import com.utn.productos_api.model.Categoria;
import com.utn.productos_api.model.Producto;
import com.utn.productos_api.repository.ProductoRepository;
import org.springframework.stereotype.Service;

import java.time.Period;
import java.util.List;
import java.util.Optional;

@Service

public class ProductoService {

    private ProductoRepository productoRepository;

    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }


    public void crearProducto(Producto producto){
        productoRepository.save(producto);
    }

    public List<Producto> obtenerTodos(){
        return productoRepository.findAll();
    }

    public Optional<Producto> obtenerPorId(Long id) {
        return productoRepository.findById(id); // una sola consulta
    }


    public List<Producto> obtenerPorCategoria(Categoria categoria){
        return productoRepository.findByCategoria(categoria);
    }

    public void actualizarProducto(Long id, Producto productoActualizado){
        productoActualizado.setId(id);
        productoRepository.save(productoActualizado);
    }

    public void actualizarStock(Long id, Integer nuevoStock){

        Producto producto = productoRepository.findById(id).get();
        producto.setStock(nuevoStock);
        productoRepository.save(producto);
    }

    public void eliminarProducto(Long id){

        productoRepository.deleteById(id);

    }
}
