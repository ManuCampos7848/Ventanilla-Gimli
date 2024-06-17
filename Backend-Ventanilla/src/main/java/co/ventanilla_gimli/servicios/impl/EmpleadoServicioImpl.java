package co.ventanilla_gimli.servicios.impl;

import co.ventanilla_gimli.dto.AgregarProductoDTO;
import co.ventanilla_gimli.dto.ClienteDTO.DetalleVentaEmpleadoDTO;
import co.ventanilla_gimli.dto.ItemRegistroProductoDTO;
import co.ventanilla_gimli.dto.DetalleRegistroProductoDTO;
import co.ventanilla_gimli.dto.ItemVentaEmpleadoDTO;
import co.ventanilla_gimli.model.*;
import co.ventanilla_gimli.repositorios.*;
import co.ventanilla_gimli.servicios.interfaces.EmpleadoServicio;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmpleadoServicioImpl implements EmpleadoServicio {

    /**
     * Acceso a los repositorios para hacer las consultas
     */
    private final ProductoRepo productoRepo;
    private final RegistroProductosRepo registroProductosRepo;
    private final VentaEmpleadoRepo ventaEmpleadoRepo;


    @Override
    public int agregarProducto(AgregarProductoDTO agregarProductoDTO) throws Exception {

        // Obtener la categoría del producto desde el DTO
        Categoria categoria = agregarProductoDTO.categoria();

        // Verificar que la categoría no sea nula
        if (categoria != null) {
            // Dependiendo de la categoría del producto, realizar las siguientes operaciones
            if (categoria.equals(Categoria.ALCOHOL)) {
                // Buscar el producto en la categoría Alcohol por su nombre
                Producto productoEncontrado = productoRepo.findByNombresAlcohol(agregarProductoDTO.nombre());

                // Incrementar la cantidad del producto encontrado con la cantidad especificada
                int nuevaCantidad = productoEncontrado.getCantidad() + agregarProductoDTO.cantidad();
                productoEncontrado.setCantidad(nuevaCantidad);

                // Guardar el producto actualizado en la base de datos
                productoRepo.save(productoEncontrado);

                // Realizar el registro de la agregación del producto
                hacerRegistroDeAgregacion(productoEncontrado, agregarProductoDTO.codigoEmpleado(), agregarProductoDTO.nombre(), agregarProductoDTO.cantidad());

            } else if (categoria.equals(Categoria.DULCES)) {
                // Buscar el producto en la categoría Dulces por su nombre
                Producto productoEncontrado = productoRepo.findByNombresDulces(agregarProductoDTO.nombre());

                // Incrementar la cantidad del producto encontrado con la cantidad especificada
                int nuevaCantidad = productoEncontrado.getCantidad() + agregarProductoDTO.cantidad();
                productoEncontrado.setCantidad(nuevaCantidad);

                // Guardar el producto actualizado en la base de datos
                productoRepo.save(productoEncontrado);

                // Realizar el registro de la agregación del producto
                hacerRegistroDeAgregacion(productoEncontrado, agregarProductoDTO.getCodigoEmpleado(), agregarProductoDTO.nombre(), agregarProductoDTO.cantidad());

            } else {
                // Buscar el producto en la categoría Gaseosas por su nombre
                Producto productoEncontrado = productoRepo.findByNombresGaseosas(agregarProductoDTO.nombre());

                // Incrementar la cantidad del producto encontrado con la cantidad especificada
                int nuevaCantidad = productoEncontrado.getCantidad() + agregarProductoDTO.cantidad();
                productoEncontrado.setCantidad(nuevaCantidad);

                // Guardar el producto actualizado en la base de datos
                productoRepo.save(productoEncontrado);

                // Realizar el registro de la agregación del producto
                hacerRegistroDeAgregacion(productoEncontrado, agregarProductoDTO.codigoEmpleado(), agregarProductoDTO.nombre(), agregarProductoDTO.cantidad());
            }
        }

        // Retornar un valor (0 en este caso, pero puede ser modificado según necesidades)
        return 0;
    }


    public void hacerRegistroDeAgregacion(Producto producto, int codigoEmpleado, String nombreProducto, int cantidad) {

        // Crear un nuevo objeto Administrador y establecer un código estático (ejemplo: 100)
        Administrador administrador = new Administrador();
        administrador.setCodigo(100);

        // Crear un nuevo objeto Empleado y establecer el código recibido como parámetro
        Empleado empleado = new Empleado();
        empleado.setCodigo(codigoEmpleado);

        // Crear un nuevo objeto RegistroProducto para registrar la acción de agregación
        RegistroProducto r = new RegistroProducto();

        // Establecer los detalles del registro
        r.setProducto(producto);                    // Producto agregado
        r.setEmpleado(empleado);                    // Empleado que realiza la acción
        r.setCategoria(producto.getCategoria());    // Categoría del producto
        r.setSubcategoria(producto.getSubcategoria()); // Subcategoría del producto
        r.setNombreProducto(nombreProducto);        // Nombre del producto agregado
        r.setCantidad(cantidad);                    // Cantidad del producto agregado

        // Establecer la fecha actual del registro
        LocalDate fechaActual = LocalDate.now();
        r.setFechaRegistro(fechaActual);

        // Establecer la hora actual del registro en formato de cadena (String)
        LocalTime horaActual = LocalTime.now();
        String horaActualString = horaActual.toString(); // Convertir a formato de cadena
        r.setHoraDeRegistro(horaActualString);

        // Guardar el registro de producto en el repositorio correspondiente
        registroProductosRepo.save(r);
    }



    @Override
    public List<ItemRegistroProductoDTO> listarRegistroProductos() {

        // Obtener todos los registros de productos del repositorio
        List<RegistroProducto> registrosEncontrados = registroProductosRepo.findAll();

        // Lista para almacenar los registros transformados en DTOs
        List<ItemRegistroProductoDTO> registros = new ArrayList<>();

        // Iterar sobre cada registro encontrado
        for (RegistroProducto r : registrosEncontrados) {
            // Verificar si el empleado asociado al registro no es nulo
            if (r.getEmpleado() != null) {
                // Agregar un nuevo DTO de registro con los datos obtenidos
                registros.add(new ItemRegistroProductoDTO(
                        r.getCodigo(),
                        r.getNombreProducto(),
                        r.getCategoria(),
                        r.getEmpleado().getCodigo(), // Código del empleado asociado al registro
                        r.getFechaRegistro()
                ));
            } else {
                // Manejar la situación donde el empleado asociado a este registro es null
                // Por ejemplo, asignar un valor predeterminado para el código de empleado
                registros.add(new ItemRegistroProductoDTO(
                        r.getCodigo(),
                        r.getNombreProducto(),
                        r.getCategoria(),
                        100, // Valor predeterminado para el código de empleado
                        r.getFechaRegistro()
                ));
            }
        }

        // Retornar la lista de registros en forma de DTOs
        return registros;
    }


    @Override
    public DetalleRegistroProductoDTO verDetalleRegistro(int codigoRegistro) throws Exception {

        // Buscar el registro de producto por su código
        Optional<RegistroProducto> registroEncontrado = registroProductosRepo.findById(codigoRegistro);

        // Verificar si el registro fue encontrado
        if (registroEncontrado.isEmpty()) {
            throw new Exception("No se pudo encontrar el registro con código: " + codigoRegistro);
        }

        // Obtener el registro encontrado
        RegistroProducto registro = registroEncontrado.get();

        // Obtener el empleado asociado al registro
        Empleado empleado = registro.getEmpleado();

        // Variables para almacenar el código y nombre del empleado
        int codigoEmpleado;
        String nombreEmpleado;

        // Verificar si el empleado es null
        if (empleado == null) {
            // Si el empleado es null, asignar valores por defecto
            codigoEmpleado = 100; // Código 100 como valor predeterminado
            nombreEmpleado = "Desconocido o empleado eliminado"; // Mensaje de nombre predeterminado
        } else {
            // Obtener el código y nombre del empleado
            codigoEmpleado = empleado.getCodigo();
            nombreEmpleado = empleado.getNombre();
        }

        // Crear y retornar un DTO con los detalles del registro encontrado
        return new DetalleRegistroProductoDTO(
                registro.getProducto().getCodigo(),      // Código del producto registrado
                registro.getNombreProducto(),            // Nombre del producto registrado
                registro.getCantidad(),                 // Cantidad del producto registrada
                registro.getCategoria(),                // Categoría del producto registrada
                registro.getSubcategoria(),             // Subcategoría del producto registrada
                registro.getFechaRegistro(),            // Fecha de registro del producto
                registro.getHoraDeRegistro(),           // Hora de registro del producto
                codigoEmpleado,                         // Código del empleado asociado
                nombreEmpleado                          // Nombre del empleado asociado
        );
    }


    @Override
    public List<ItemVentaEmpleadoDTO> listaVentasEmpleados() {

        // Obtener todas las ventas de empleados desde el repositorio
        List<VentaEmpleado> ventas = ventaEmpleadoRepo.findAll();

        // Lista para almacenar las ventas transformadas en DTOs
        List<ItemVentaEmpleadoDTO> ventasARetornar = new ArrayList<>();

        // Iterar sobre cada venta de empleado encontrada
        for (VentaEmpleado v : ventas) {
            // Verificar si el empleado asociado a la venta es null
            if (v.getEmpleado() == null) {
                // Si el empleado es null, asignar un nuevo objeto Empleado vacío
                v.setEmpleado(new Empleado());
            }

            // Agregar un nuevo DTO de venta con los datos obtenidos
            ventasARetornar.add(new ItemVentaEmpleadoDTO(
                    v.getCodigo(),                     // Código de la venta
                    v.getFechaVenta(),                 // Fecha de la venta
                    v.getHoraDeVenta(),                // Hora de la venta
                    v.getEmpleado().getNombre(),       // Nombre del empleado asociado
                    v.getEmpleado().getCodigo()        // Código del empleado asociado
            ));
        }

        // Retornar la lista de ventas en forma de DTOs
        return ventasARetornar;
    }


    @Override
    @Transactional
    public DetalleVentaEmpleadoDTO verDetalleVentaEmpleado(int codigoVenta) throws Exception {

        // Buscar la venta de empleado por su código
        Optional<VentaEmpleado> ventaEncontrada = ventaEmpleadoRepo.findById(codigoVenta);

        // Verificar si se encontró la venta
        if (ventaEncontrada.isEmpty()) {
            throw new Exception("No se encontró la venta con código: " + codigoVenta);
        }

        // Obtener el objeto VentaEmpleado desde el Optional
        VentaEmpleado venta = ventaEncontrada.get();

        // Determinar el nombre del cliente asociado a la venta, o asignar "Cliente casual" si es nulo
        String nombreCliente = (venta.getCliente() != null) ? venta.getCliente().getNombre() : "Cliente casual";

        // Obtener el nombre del producto asociado a la venta mediante su código
        String nombreProducto = encontrarProductoPorCodigo(venta.getProducto().getCodigo());

        // Crear y retornar un DTO con los detalles de la venta de empleado
        return new DetalleVentaEmpleadoDTO(
                venta.getCodigo(),                     // Código de la venta
                venta.getFechaVenta(),                 // Fecha de la venta
                venta.getHoraDeVenta(),                // Hora de la venta
                venta.getEmpleado().getNombre(),       // Nombre del empleado asociado
                venta.getEmpleado().getCodigo(),       // Código del empleado asociado
                nombreCliente,                         // Nombre del cliente asociado
                (venta.getCliente() != null) ? venta.getCliente().getCorreo() : null, // Correo del cliente, si no es nulo
                nombreProducto,                        // Nombre del producto vendido
                venta.getProducto().getCodigo(),    // Código del producto vendido
                venta.getDineroADevolver()
        );
    }


    public String encontrarProductoPorCodigo(int codigo) throws Exception {

        // Buscar el producto por su código
        Optional<Producto> productoEncontrado = productoRepo.findById(codigo);

        // Verificar si se encontró el producto
        if (productoEncontrado.isEmpty()) {
            throw new Exception("No se encontró el producto con código: " + codigo);
        }

        // Obtener el producto encontrado desde el Optional
        Producto producto = productoEncontrado.get();

        // Determinar el nombre del producto basado en su categoría
        if (producto.getCategoria().equals(Categoria.ALCOHOL)) {
            // Si la categoría del producto es ALCOHOL, retornar el primer nombre de la lista de nombres de alcohol
            for (String nombre : producto.getNombresAlcohol()) {
                return nombre;
            }
        } else if (producto.getCategoria().equals(Categoria.DULCES)) {
            // Si la categoría del producto es DULCES, retornar el primer nombre de la lista de nombres de dulces
            for (String nombre : producto.getNombresDulces()) {
                return nombre;
            }
        } else {
            // Para cualquier otra categoría, retornar el primer nombre de la lista de nombres de gaseosas
            for (String nombre : producto.getNombresGaseosas()) {
                return nombre;
            }
        }

        // Si no se encuentra ningún nombre (caso improbable), retornar null
        return null;
    }


}
