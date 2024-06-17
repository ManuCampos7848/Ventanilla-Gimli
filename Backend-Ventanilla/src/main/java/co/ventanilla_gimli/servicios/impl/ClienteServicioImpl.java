package co.ventanilla_gimli.servicios.impl;

import co.ventanilla_gimli.dto.*;
import co.ventanilla_gimli.dto.ClienteDTO.*;
import co.ventanilla_gimli.model.*;
import co.ventanilla_gimli.repositorios.*;
import co.ventanilla_gimli.servicios.interfaces.ClienteServicio;
import co.ventanilla_gimli.servicios.interfaces.EmailServicio;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class ClienteServicioImpl implements ClienteServicio{

    /**
     * Acceso a los repositorios para hacer las consultas
     */
    private  final ClienteRepo clienteRepo;
    private  final EmpleadoRepo empleadoRepo;
    private  final AdministradorRepo administradorRepo;
    private final ProductoRepo productoRepo;
    private final VentaEmpleadoRepo ventaEmpleadoRepo;
    private final VentaClienteRepo ventaClienteRepo;
    private final EmailServicio emailServicio;
    private final DetalleVentaClienteRepo detalleVentaClienteRepo;

    @Override
    public int registrarCliente(RegistroClienteDTO registroClienteDTO) throws Exception {
        // Verificar si el correo electrónico ya está en uso
        if (correoRepetido(registroClienteDTO.correo())) {
            throw new Exception("El correo digitado ya se encuentra en uso");
        }

        // Crear un nuevo objeto Cliente y establecer sus atributos con los datos de registroClienteDTO
        Cliente cliente = new Cliente();
        cliente.setEstado(true); // Establecer estado activo por defecto
        cliente.setNombre(registroClienteDTO.nombre());
        cliente.setTelefono(registroClienteDTO.telefono());
        cliente.setDireccion(registroClienteDTO.direccion());
        cliente.setCorreo(registroClienteDTO.correo());

        // Encriptar la contraseña utilizando BCryptPasswordEncoder
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String passwordEncriptada = passwordEncoder.encode(registroClienteDTO.password());
        cliente.setPassword(passwordEncriptada);

        // Guardar el cliente en el repositorio
        clienteRepo.save(cliente);

        // Enviar un correo electrónico de confirmación al cliente registrado
        emailServicio.enviarCorreo(new EmailDTO(
                registroClienteDTO.correo(),
                "Registro Exitoso",
                "Felicidades, su registro en la ventanilla Gimli ha sido exitoso. ¡Bienvenido!"
        ));

        // Retornar el código del cliente generado por la base de datos
        return cliente.getCodigo();
    }


    @Override
    public int modificarCliente(ModificarClienteDTO modificarClienteDTO) throws Exception {
        // Buscar el cliente en el repositorio por su código
        Optional<Cliente> clienteEncontrado = clienteRepo.findById(modificarClienteDTO.codigoCliente());

        // Verificar si el cliente existe en la base de datos
        if (clienteEncontrado.isEmpty()) {
            throw new Exception("El cliente no existe");
        }

        // Obtener el cliente encontrado
        Cliente cliente = clienteEncontrado.get();

        // Actualizar los datos del cliente con la información proporcionada en modificarClienteDTO
        cliente.setNombre(modificarClienteDTO.nombre());
        cliente.setTelefono(modificarClienteDTO.telefono());
        cliente.setDireccion(modificarClienteDTO.direccion());
        cliente.setCorreo(modificarClienteDTO.correo());

        // Encriptar la nueva contraseña proporcionada en modificarClienteDTO.password()
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String passwordEncriptada = passwordEncoder.encode(modificarClienteDTO.password());
        cliente.setPassword(passwordEncriptada);

        // Guardar los cambios realizados en el cliente en el repositorio
        clienteRepo.save(cliente);

        // Retornar el código del cliente actualizado
        return cliente.getCodigo();
    }


    @Override
    public boolean eliminarCuenta(int codigoCliente) throws Exception {
        // Buscar el cliente en el repositorio por su código
        Optional<Cliente> clienteEncontrado = clienteRepo.findById(codigoCliente);

        // Verificar si se encontró el cliente
        Cliente cliente = clienteEncontrado.orElseThrow(() -> new Exception("Cliente no encontrado"));

        // Desvincular todas las ventas asociadas al cliente estableciendo cliente_codigo en NULL
        ventaClienteRepo.desvincularVentasDelCliente(cliente.getCodigo());
        ventaClienteRepo.desvincularVentasDelCliente2(cliente.getCodigo());

        // Eliminar el cliente de la base de datos
        clienteRepo.delete(cliente);

        return true; // Indicar que la eliminación fue exitosa
    }


    @Override
    public List<ItemCompraClienteDTO> comprasRealizadas(int codigoCliente) {
        // Obtener el correo electrónico del cliente utilizando el código
        String correoCliente = correoCliente(codigoCliente);

        // Buscar todos los detalles de ventas asociados al cliente utilizando su correo electrónico
        List<DetalleVentaCliente> compras = detalleVentaClienteRepo.findAllByCorreoCliente(correoCliente);
        List<ItemCompraClienteDTO> comprasARetornar = new ArrayList<>();

        // Iterar sobre cada detalle de venta encontrado
        for (DetalleVentaCliente d : compras) {
            // Crear un nuevo ItemCompraClienteDTO y agregarlo a la lista de compras a retornar
            comprasARetornar.add(new ItemCompraClienteDTO(
                    d.getCodigo(),
                    d.getNombreProducto(),
                    d.getCantidad(),
                    d.getFechaVenta()
            ));
        }

        return comprasARetornar;
    }


    @Override
    public DetalleDatosClienteDTO detalleDatosCliente(int codigoCliente) throws Exception {
        // Buscar el cliente en el repositorio por su código
        Optional<Cliente> clienteEncontrado = clienteRepo.findById(codigoCliente);

        // Verificar si se encontró el cliente
        if (clienteEncontrado.isEmpty()) {
            throw new Exception("Cliente no encontrado");
        }

        // Obtener el cliente encontrado
        Cliente cliente = clienteEncontrado.get();

        // Crear y retornar un objeto DetalleDatosClienteDTO con los datos del cliente encontrado
        return new DetalleDatosClienteDTO(
                cliente.getCodigo(),
                cliente.getNombre(),
                cliente.getCorreo(),
                cliente.getDireccion(),
                cliente.getTelefono()
        );
    }

    // Verificar si se encontró el cliente
    private String correoCliente(int codigoCliente) {
        Optional<Cliente> clienteEncontrado = clienteRepo.findById(codigoCliente);
        return clienteEncontrado.get().getCorreo();
    }

    @Override
    @Transactional
    public List<ItemProductoDTO> listarProductos() {

        List<Producto> productos = productoRepo.findAll();
        List<ItemProductoDTO> productosARetornar = new ArrayList<>();

        for (Producto producto : productos) {
            // Obtener la cantidad del producto
            int cantidad = producto.getCantidad();

            // Verificar si hay al menos una unidad disponible del producto
            if (cantidad >= 1) {
                // Obtener los nombres de alcohol y crear ItemProductoDTO para cada nombre
                for (String nombre : producto.getNombresAlcohol()) {
                    productosARetornar.add(new ItemProductoDTO(
                            producto.getCodigo(),
                            producto.getCategoria(),
                            producto.getSubcategoria(),
                            nombre,
                            producto.getPrecio(),
                            producto.getProveedor()
                    ));
                }

                // Obtener los nombres de dulces y crear ItemProductoDTO para cada nombre
                for (String nombre : producto.getNombresDulces()) {
                    productosARetornar.add(new ItemProductoDTO(
                            producto.getCodigo(),
                            producto.getCategoria(),
                            producto.getSubcategoria(),
                            nombre,
                            producto.getPrecio(),
                            producto.getProveedor()
                    ));
                }

                // Obtener los nombres de gaseosas y crear ItemProductoDTO para cada nombre
                for (String nombre : producto.getNombresGaseosas()) {
                    productosARetornar.add(new ItemProductoDTO(
                            producto.getCodigo(),
                            producto.getCategoria(),
                            producto.getSubcategoria(),
                            nombre,
                            producto.getPrecio(),
                            producto.getProveedor()
                    ));
                }
            }
        }

        return productosARetornar;
    }


    @Override
    public DetalleCompraClienteDTO verDetalleCompra(int codigoCompra) throws Exception {
        // Buscar la compra en el repositorio por su código
        Optional<DetalleVentaCliente> compra = detalleVentaClienteRepo.findById(codigoCompra);

        // Verificar si se encontró la compra
        if (compra.isEmpty()) {
            throw new Exception("Compra no encontrada con código: " + codigoCompra);
        }

        // Obtener la compra encontrada
        DetalleVentaCliente detalleVentaCliente = compra.get();

        // Obtener la venta asociada a la compra
        VentaCliente ventaCliente = detalleVentaCliente.getVenta();

        // Crear y retornar un objeto DetalleCompraClienteDTO con los datos de la compra encontrada
        return new DetalleCompraClienteDTO(
                detalleVentaCliente.getCodigo(),
                detalleVentaCliente.getNombreProducto(),
                detalleVentaCliente.getDescripcion(),
                detalleVentaCliente.getPrecio(),
                detalleVentaCliente.getCantidad(),
                detalleVentaCliente.getFechaVenta(),
                detalleVentaCliente.getHoraDeVenta(),
                detalleVentaCliente.getDireccion(),
                detalleVentaCliente.getDevueltas(),
                detalleVentaCliente.getNombreCliente(),
                detalleVentaCliente.getCorreoCliente(),
                detalleVentaCliente.getTelefono(),
                (ventaCliente != null) ? ventaCliente.getCodigo() : null
        );
    }


    /**
     * Método para obtener el detalle de un producto según su código.
     *
     * Busca un producto en el repositorio por su código y retorna un DTO
     * {@link DetalleProductoDTO} con los detalles correspondientes del producto
     * encontrado, incluyendo el nombre, descripción, precio, cantidad disponible,
     * categoría, subcategoría y proveedor.
     *
     * Si el producto pertenece a la categoría ALCOHOL, retorna el primer nombre de
     * alcohol encontrado.
     * Si el producto pertenece a la categoría DULCES, retorna el primer nombre de
     * dulce encontrado.
     * Si el producto pertenece a la categoría GASEOSAS, retorna el primer nombre de
     * gaseosa encontrado.
     *
     * Si no se encuentra ningún nombre correspondiente al producto o no se encuentra
     * el producto en el repositorio, retorna null.
     *
     * @param codigoProducto El código del producto a buscar.
     * @return DetalleProductoDTO con los detalles del producto encontrado, o null si
     *         no se encontró un producto válido.
     * @throws Exception Si ocurre algún error durante la búsqueda o el acceso a los datos.
     */
    @Override
    public DetalleProductoDTO verDetalleProducto(int codigoProducto) throws Exception {

        Optional<Producto> productoEncontrado = productoRepo.findById(codigoProducto);


        if(productoEncontrado.get().getCategoria().equals(Categoria.ALCOHOL)){
            for(String nombre : productoEncontrado.get().getNombresAlcohol()){
               return new DetalleProductoDTO(
                       productoEncontrado.get().getCodigo(),
                       nombre,
                       productoEncontrado.get().getDescripcion(),
                       productoEncontrado.get().getPrecio(),
                       productoEncontrado.get().getCantidad(),
                       productoEncontrado.get().getCategoria(),
                       productoEncontrado.get().getSubcategoria(),
                       productoEncontrado.get().getProveedor()
               );
            }
        }
        if(productoEncontrado.get().getCategoria().equals(Categoria.DULCES)){
            for(String nombre : productoEncontrado.get().getNombresDulces()){
                return new DetalleProductoDTO(
                        productoEncontrado.get().getCodigo(),
                        nombre,
                        productoEncontrado.get().getDescripcion(),
                        productoEncontrado.get().getPrecio(),
                        productoEncontrado.get().getCantidad(),
                        productoEncontrado.get().getCategoria(),
                        productoEncontrado.get().getSubcategoria(),
                        productoEncontrado.get().getProveedor()
                );
            }
        }else{
            for(String nombre : productoEncontrado.get().getNombresGaseosas()){
                return new DetalleProductoDTO(
                        productoEncontrado.get().getCodigo(),
                        nombre,
                        productoEncontrado.get().getDescripcion(),
                        productoEncontrado.get().getPrecio(),
                        productoEncontrado.get().getCantidad(),
                        productoEncontrado.get().getCategoria(),
                        productoEncontrado.get().getSubcategoria(),
                        productoEncontrado.get().getProveedor()
                );
            }

        }

        return null;
    }

    /**
     * Método para filtrar productos por nombre.
     *
     * Busca un producto en el repositorio por el nombre proporcionado. Compara el nombre
     * con los nombres de alcohol, dulces y gaseosas de cada producto y retorna un DTO
     * {@link FiltroBusquedaDTO} con los detalles del primer producto que coincide con el
     * nombre proporcionado.
     *
     * Si encuentra una coincidencia en los nombres de alcohol, dulces o gaseosas de algún
     * producto, retorna un DTO con el código del producto, su categoría, subcategoría,
     * nombre, precio y proveedor.
     *
     * Si no encuentra ninguna coincidencia, retorna null.
     *
     * @param nombreProducto El nombre del producto a buscar.
     * @return FiltroBusquedaDTO con los detalles del producto encontrado, o null si no se
     *         encontró un producto con el nombre proporcionado.
     */
    @Override
    @Transactional
    public FiltroBusquedaDTO filtrarProductoPorNombre(String nombreProducto) {

       List<Producto> productos = productoRepo.findAll();

        for (Producto producto : productos) {
            for (String nombre : producto.getNombresAlcohol()) {
                if (nombre.equals(nombreProducto)) {
                    // Si encontramos una coincidencia, retornamos el producto
                    return new FiltroBusquedaDTO(producto.getCodigo(),
                            producto.getCategoria(),
                            producto.getSubcategoria(),
                            nombre,
                            producto.getPrecio(),
                            producto.getProveedor());
                }
            }
        }
        for (Producto producto : productos) {
            for (String nombre : producto.getNombresDulces()) {
                if (nombre.equals(nombreProducto)) {
                    // Si encontramos una coincidencia, retornamos el producto
                    return new FiltroBusquedaDTO(producto.getCodigo(),
                            producto.getCategoria(),
                            producto.getSubcategoria(),
                            nombre,
                            producto.getPrecio(),
                            producto.getProveedor());
                }
            }
        }
        for (Producto producto : productos) {
            for (String nombre : producto.getNombresGaseosas()) {
                if (nombre.equals(nombreProducto)) {
                    // Si encontramos una coincidencia, retornamos el producto
                    return new FiltroBusquedaDTO(producto.getCodigo(),
                            producto.getCategoria(),
                            producto.getSubcategoria(),
                            nombre,
                            producto.getPrecio(),
                            producto.getProveedor());
                }
            }
        }

        return null;
    }

    /**
     * Método para comprobar si la cantidad y el precio de un producto cumplen ciertos criterios.
     *
     * Este método verifica si el precio de un producto es menor o igual a 1500 y si la cantidad
     * es menor o igual a 10. Si ambas condiciones se cumplen, retorna true; de lo contrario,
     * retorna false.
     *
     * @param cantidad La cantidad del producto a verificar.
     * @param precio El precio del producto a verificar.
     * @return true si el precio es menor o igual a 1500 y la cantidad es menor o igual a 10;
     *         false en caso contrario.
     */
    private boolean comprobacionPrecioCantidad(int cantidad, double precio) {
        if(precio <= 1500){
            if(cantidad <= 10){
                return  true;
            }
        }
        return  false;
    }

    /**
     * Método para realizar una compra de producto por parte de un cliente.
     *
     * Este método verifica varias condiciones antes de procesar la compra:
     * - Verifica si el producto está agotado.
     * - Verifica si el dinero proporcionado por el cliente es suficiente para pagar el total.
     * - Verifica si la cantidad de productos a comprar es mayor que 10.
     * - Verifica si la cantidad solicitada supera la cantidad disponible en inventario.
     * - Verifica que la cantidad solicitada sea mayor que cero.
     *
     * Si alguna de estas condiciones no se cumple, se lanza una excepción con un mensaje de error.
     *
     * Si todas las condiciones se cumplen, se realiza la venta, actualizando el inventario del producto
     * y guardando la información de la venta en la base de datos. Además, se envía un correo electrónico
     * de confirmación al cliente.
     *
     * @param registroCompraClienteDTO DTO que contiene los datos necesarios para registrar la compra.
     * @return El código de la venta realizada.
     * @throws Exception Si alguna de las condiciones de verificación no se cumple.
     */
    @Override
    public int realizarCompra(RegistroCompraClienteDTO registroCompraClienteDTO) throws Exception{

        Optional<Cliente> clienteEncontrado = clienteRepo.findById(registroCompraClienteDTO.codigoCliente());
        //Optional<Empleado> empleadoEncontado = empleadoRepo.findById(registroCompraClienteDTO.codigoEmpleado());
        Producto productoEncontrado = encontrarProductoPorNombre(registroCompraClienteDTO.nombreProducto());
        double totalAPagar = productoEncontrado.getPrecio() * registroCompraClienteDTO.cantidad();


        if(productoEncontrado.getCantidad() <= 0){
            throw  new Exception("Producto agotado, por favor intentelo mas tarde");
        }
        if(registroCompraClienteDTO.dinero() < totalAPagar){
            throw  new Exception("Dinero insuficiente");
        }
        if(comprobacionPrecioCantidad(registroCompraClienteDTO.cantidad(), productoEncontrado.getPrecio()) == true){
            throw  new Exception("Para realizar la compra, debe adquirir mas de 10 productos");
        }
        double dineroADevolver = totalAPagar - registroCompraClienteDTO.dinero();
        if(registroCompraClienteDTO.cantidad() > productoEncontrado.getCantidad()){
            int cantidadARestar = (productoEncontrado.getCantidad() - registroCompraClienteDTO.cantidad()) * -1;
            throw  new Exception("No hay tanta cantidad de producto, si desea puede restarle " + cantidadARestar + " a la cantidad de productos");
        }
        if(registroCompraClienteDTO.cantidad() <= 0){
            throw  new Exception("La cantidad a comprar no puede ser menor o igual a 0");
        }

        VentaCliente venta = new VentaCliente();

        venta.setProducto(productoEncontrado);
        venta.setCliente(clienteEncontrado.get());
        venta.setDireccion(registroCompraClienteDTO.direccion());
        venta.setCantidad(registroCompraClienteDTO.cantidad());
        venta.setPrecioUnitario(productoEncontrado.getPrecio());


        int nuevaCantidad = productoEncontrado.getCantidad() - registroCompraClienteDTO.cantidad();
        productoEncontrado.setCantidad(nuevaCantidad);

        // Establecer la fecha actual
        LocalDate fechaActual = LocalDate.now();
        venta.setFechaVenta(fechaActual);

        // Establecer la hora actual en formato de cadena (String)
        LocalTime horaActual = LocalTime.now();
        String horaActualString = horaActual.toString(); // Convertir a formato de cadena
        venta.setHoraDeVenta(horaActualString);

        VentaCliente ventaNueva = ventaClienteRepo.save(venta);
        productoRepo.save(productoEncontrado);

        hacerRegistroVenta(registroCompraClienteDTO.nombreProducto(), productoEncontrado,
                registroCompraClienteDTO.cantidad(), fechaActual, horaActualString, registroCompraClienteDTO.direccion(),
                dineroADevolver, venta);

        emailServicio.enviarCorreo(new EmailDTO(
                clienteEncontrado.get().getCorreo(),
                "Se ha registrado la compra con éxito",
                "La compra del producto " + registroCompraClienteDTO.nombreProducto()+ " ha sido un" +
                        " exito, gracias por comprar en la Ventanilla Gimli. La cantidad de producto comprado fue de: " + registroCompraClienteDTO.cantidad() +  "." +
                        " Sus devueltas son: " + (dineroADevolver)*(-1) + "$" + "\n" +
                        " - Fecha de la venta: " + fechaActual + "\n" +
                        " - Hora de la venta: " + horaActual + "\n" +
                        " - Correo del cliente: " + clienteEncontrado.get().getCorreo() + "\n" +
                        " - Direccion del cliente: " + registroCompraClienteDTO.direccion()

        ));

        return ventaNueva.getCodigo();
    }

    private void hacerRegistroVenta(String nombreProducto, Producto productoEncontrado, int cantidad,
                                    LocalDate fechaActual, String horaActual, String direccion, double dineroADevolver, VentaCliente venta) {

        // Creación de un nuevo detalle de venta para el cliente
        DetalleVentaCliente detalleVenta = new DetalleVentaCliente();

        // Establecer los detalles del producto vendido
        detalleVenta.setNombreProducto(nombreProducto);
        detalleVenta.setDescripcion(productoEncontrado.getDescripcion());
        detalleVenta.setPrecio(productoEncontrado.getPrecio());
        detalleVenta.setCantidad(cantidad);

        // Establecer la fecha y hora de la venta
        detalleVenta.setFechaVenta(fechaActual);
        detalleVenta.setHoraDeVenta(horaActual);

        // Establecer la dirección de entrega
        detalleVenta.setDireccion(direccion);

        // Establecer el monto de dinero a devolver (en caso necesario)
        detalleVenta.setDevueltas(dineroADevolver * (-1));

        // Establecer los datos del cliente asociado a la venta
        detalleVenta.setNombreCliente(venta.getCliente().getNombre());
        detalleVenta.setCorreoCliente(venta.getCliente().getCorreo());
        detalleVenta.setTelefono(venta.getCliente().getTelefono());

        // Establecer la relación con la venta principal
        detalleVenta.setVenta(venta);

        // Guardar el detalle de la venta en el repositorio
        detalleVentaClienteRepo.save(detalleVenta);
    }

    public Producto encontrarProductoPorNombre(String nombre) throws Exception {

        // Buscar el producto por nombre en la categoría de alcohol
        Producto productoEncontradoAlcohol = productoRepo.findByNombresAlcohol(nombre);

        // Si no se encuentra en la categoría de alcohol, buscar en la categoría de gaseosas
        if (productoEncontradoAlcohol == null) {
            Producto productoEncontradoGaseosas = productoRepo.findByNombresGaseosas(nombre);

            // Si no se encuentra en la categoría de gaseosas, buscar en la categoría de dulces
            if (productoEncontradoGaseosas == null) {
                Producto productoEncontradoDulces = productoRepo.findByNombresDulces(nombre);

                // Si tampoco se encuentra en la categoría de dulces, lanzar una excepción
                if (productoEncontradoDulces == null) {
                    throw new Exception("Error: Producto no encontrado");
                } else {
                    return productoEncontradoDulces; // Retornar el producto encontrado en la categoría de dulces
                }
            } else {
                return productoEncontradoGaseosas; // Retornar el producto encontrado en la categoría de gaseosas
            }
        } else {
            return productoEncontradoAlcohol; // Retornar el producto encontrado en la categoría de alcohol
        }
    }


    private boolean correoRepetido(String correo) {

        // Buscar si el correo está registrado en la entidad Cliente
        Cliente correoCliente = clienteRepo.findClienteByCorreo(correo);

        // Buscar si el correo está registrado en la entidad Empleado
        Empleado correoEmpleado = empleadoRepo.findByCorreo(correo);

        // Buscar si el correo está registrado en la entidad Administrador
        Administrador correoAdministrador = administradorRepo.findByCorreo(correo);

        // Si se encuentra el correo en alguna de las entidades, retornar true
        if (correoCliente != null || correoEmpleado != null || correoAdministrador != null) {
            return true;
        }

        // Si no se encuentra en ninguna entidad, retornar false
        return false;
    }

}
