package co.ventanilla_gimli.servicios.impl;

import co.ventanilla_gimli.dto.RegistroProductoDTO;
import co.ventanilla_gimli.model.Categoria;
import co.ventanilla_gimli.model.Producto;
import co.ventanilla_gimli.repositorios.ProductoRepo;
import co.ventanilla_gimli.servicios.interfaces.ProductoServicio;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductoServicioImpl implements ProductoServicio {

    /**
     * Acceso a los repositorios para hacer las consultas
     */
    private final ProductoRepo productoRepo;

    @Override
    public int registrarProducto(RegistroProductoDTO registroProductoDTO) throws Exception {

        // Crear un nuevo objeto Producto
        Producto producto = new Producto();

        // Establecer los atributos del producto desde el DTO recibido
        producto.setCategoria(registroProductoDTO.categoria());
        producto.setSubcategoria(registroProductoDTO.subcategoria());
        producto.setProveedor(registroProductoDTO.proveedor());
        producto.setDescripcion(registroProductoDTO.descripcion());
        producto.setPrecio(registroProductoDTO.precio());
        producto.setCantidad(registroProductoDTO.cantidad());

        // Validar y agregar el nombre del producto según su categoría
        if (producto.getCategoria().equals(Categoria.ALCOHOL)) {
            // Si la categoría es ALCOHOL, verificar si el nombre ya existe en la lista de nombres de alcohol
            for (String nombre : producto.getNombresAlcohol()) {
                if (nombre.equals(registroProductoDTO.nombre())) {
                    throw new Exception("Esa bebida ya ha sido agregada con anterioridad");
                }
            }
            // Si no existe, agregar el nombre a la lista de nombres de alcohol
            producto.getNombresAlcohol().add(registroProductoDTO.nombre());

        } else if (producto.getCategoria().equals(Categoria.DULCES)) {
            // Si la categoría es DULCES, verificar si el nombre ya existe en la lista de nombres de dulces
            for (String nombre : producto.getNombresDulces()) {
                if (nombre.equals(registroProductoDTO.nombre())) {
                    throw new Exception("Ese dulce ya ha sido agregado con anterioridad");
                }
            }
            // Si no existe, agregar el nombre a la lista de nombres de dulces
            producto.getNombresDulces().add(registroProductoDTO.nombre());

        } else {
            // Si la categoría es GASEOSAS, verificar si el nombre ya existe en la lista de nombres de gaseosas
            for (String nombre : producto.getNombresGaseosas()) {
                if (nombre.equals(registroProductoDTO.nombre())) {
                    throw new Exception("Esa gaseosa ya ha sido agregada con anterioridad");
                }
            }
            // Si no existe, agregar el nombre a la lista de nombres de gaseosas
            producto.getNombresGaseosas().add(registroProductoDTO.nombre());
        }

        // Guardar el nuevo producto en el repositorio y obtener el producto actualizado con su código generado
        Producto productoNuevo = productoRepo.save(producto);

        // Retornar el código del producto registrado
        return productoNuevo.getCodigo();
    }



}
