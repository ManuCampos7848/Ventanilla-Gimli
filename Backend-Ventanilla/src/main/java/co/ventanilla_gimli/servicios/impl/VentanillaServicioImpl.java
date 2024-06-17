package co.ventanilla_gimli.servicios.impl;

import co.ventanilla_gimli.dto.*;
import co.ventanilla_gimli.model.*;
import co.ventanilla_gimli.repositorios.*;
import co.ventanilla_gimli.servicios.interfaces.EmailServicio;
import co.ventanilla_gimli.servicios.interfaces.VentanillaServicio;
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
public class VentanillaServicioImpl implements VentanillaServicio {

    /**
     * Acceso a los repositorios para hacer las consultas
     */
    private final ProductoRepo productoRepo;
    private final EmpleadoRepo empleadoRepo;
    private final ClienteRepo clienteRepo;
    private final VentaEmpleadoRepo ventaRepo;
    private final VentaClienteRepo ventaRepoClienteRepo;
    private final EmailServicio emailServicio;
    private final DetalleVentaClienteRepo detalleVentaClienteRepo;

    @Override
    public List<Categoria> listarCategorias() {
        // Devuelve una lista de todas las categorías disponibles usando el método estático List.of()
        return List.of(Categoria.values());
    }

    @Override
    public List<Subcategoria> listarSubcategorias() {
        // Devuelve una lista de todas las subcategorías disponibles usando el método estático List.of()
        return List.of(Subcategoria.values());
    }

    @Override
    public List<String> listarNombresAlcoholes(Categoria categoria) {

        // Lista para almacenar los nombres de alcoholes a retornar
        List<String> nombresARetornar = new ArrayList<>();

        // Verificar si la categoría recibida es ALCOHOL
        if (categoria.equals(Categoria.ALCOHOL)) {
            // Si la categoría es ALCOHOL, obtener los nombres de alcohol de todos los productos
            List<String> nombresAlcohol = productoRepo.findAllNombresAlcoholByCategoria(Categoria.ALCOHOL);

            // Agregar los nombres de alcohol obtenidos a la lista a retornar
            nombresARetornar.addAll(nombresAlcohol);
        }

        // Retornar la lista de nombres de alcoholes (puede estar vacía si la categoría no es ALCOHOL)
        return nombresARetornar;
    }


    @Override
    public int encontrarClientePorCorreo(String correo) throws Exception {
        // Buscar el cliente por su correo electrónico en el repositorio
        Cliente clienteEncontrado = clienteRepo.findClienteByCorreo(correo);

        // Verificar si se encontró un cliente con el correo proporcionado
        if (clienteEncontrado != null) {
            // Si se encontró, retornar el código del cliente encontrado
            return clienteEncontrado.getCodigo();
        } else {
            // Si no se encontró ningún cliente con el correo proporcionado, lanzar una excepción
            throw new Exception("No se encontró ningún cliente con el correo proporcionado");
        }
    }


    @Override
    public List<String> listarNombresDulces(Categoria categoria) {

        // Lista para almacenar los nombres de dulces a retornar
        List<String> nombresARetornar = new ArrayList<>();

        // Verificar si la categoría recibida es DULCES
        if (categoria.equals(Categoria.DULCES)) {
            // Si la categoría es DULCES, obtener los nombres de dulces de todos los productos
            List<String> nombresDulces = productoRepo.findAllNombresDulcesByCategoria(categoria);

            // Agregar los nombres de dulces obtenidos a la lista a retornar
            nombresARetornar.addAll(nombresDulces);
        }

        // Retornar la lista de nombres de dulces (puede estar vacía si la categoría no es DULCES)
        return nombresARetornar;
    }


    @Override
    public List<String> listarNombresGaseosas(Categoria categoria) {

        // Lista para almacenar los nombres de gaseosas a retornar
        List<String> nombresARetornar = new ArrayList<>();

        // Verificar si la categoría recibida es GASEOSA
        if (categoria.equals(Categoria.GASEOSA)) {
            // Si la categoría es GASEOSA, obtener los nombres de gaseosas de todos los productos
            List<String> nombresGaseosas = productoRepo.findAllNombresGaseosasByCategoria(categoria);

            // Agregar los nombres de gaseosas obtenidos a la lista a retornar
            nombresARetornar.addAll(nombresGaseosas);
        }

        // Retornar la lista de nombres de gaseosas (puede estar vacía si la categoría no es GASEOSA)
        return nombresARetornar;
    }

    @Override
    public List<String> listarNombresAlcoholesMayor1(Categoria categoria) {
        // Lista para almacenar los nombres de alcoholes a retornar
        List<String> nombresARetornar = new ArrayList<>();

        // Verificar si la categoría recibida es ALCOHOL
        if (categoria.equals(Categoria.ALCOHOL)) {
            // Si la categoría es ALCOHOL, obtener los nombres de alcohol de todos los productos
            List<String> nombresAlcohol = productoRepo.findAllNombresAlcoholByCategoriaIgnoreCantidad(Categoria.ALCOHOL);

            // Agregar los nombres de alcohol obtenidos a la lista a retornar
            nombresARetornar.addAll(nombresAlcohol);
        }

        // Retornar la lista de nombres de alcoholes (puede estar vacía si la categoría no es ALCOHOL)
        return nombresARetornar;
    }

    @Override
    public List<String> listarNombresDulcesMayor1(Categoria categoria) {
        // Lista para almacenar los nombres de dulces a retornar
        List<String> nombresARetornar = new ArrayList<>();

        // Verificar si la categoría recibida es DULCES
        if (categoria.equals(Categoria.DULCES)) {
            // Si la categoría es DULCES, obtener los nombres de dulces de todos los productos
            List<String> nombresDulces = productoRepo.findAllNombresDulcesByCategoriaIgnoreCantidad(categoria);

            // Agregar los nombres de dulces obtenidos a la lista a retornar
            nombresARetornar.addAll(nombresDulces);
        }

        // Retornar la lista de nombres de dulces (puede estar vacía si la categoría no es DULCES)
        return nombresARetornar;
    }

    @Override
    public List<String> listarNombresGaseosasMayor1(Categoria categoria) {
        // Lista para almacenar los nombres de gaseosas a retornar
        List<String> nombresARetornar = new ArrayList<>();

        // Verificar si la categoría recibida es GASEOSA
        if (categoria.equals(Categoria.GASEOSA)) {
            // Si la categoría es GASEOSA, obtener los nombres de gaseosas de todos los productos
            List<String> nombresGaseosas = productoRepo.findAllNombresGaseosasByCategoriaIgnoreCantidad(categoria);

            // Agregar los nombres de gaseosas obtenidos a la lista a retornar
            nombresARetornar.addAll(nombresGaseosas);
        }

        // Retornar la lista de nombres de gaseosas (puede estar vacía si la categoría no es GASEOSA)
        return nombresARetornar;
    }

    /**
     * Metodo que permite registrar la venta del empleado teniendo en cuenta pautas como
     * si el tipo de cliente es casual o registrado, etc.
     * @param registroVentaEmpleado
     * @return
     * @throws Exception
     */
    @Override
    public int registrarVentaEmpleado(RegistroVentaEmpleadoDTO registroVentaEmpleado) throws Exception {

        VentaEmpleado venta = new VentaEmpleado();
        Optional<Empleado> empleado = empleadoRepo.findById(registroVentaEmpleado.codigoEmpleado());
        Optional<Cliente> cliente = clienteRepo.findById(registroVentaEmpleado.codigoCliente());
        Producto producto = encontrarProducto(registroVentaEmpleado.nombreProducto());

        double totalAPagar = producto.getPrecio() * registroVentaEmpleado.cantidad();


        if(cliente.isPresent()){ // Verifica si el Optional contiene un valor
            venta.setCliente(cliente.get()); // Si contiene un valor, asigna el cliente
        } else {
            venta.setCliente(null); // Si no contiene un valor, asigna null
        }



        if(registroVentaEmpleado.dinero() < totalAPagar){
            throw  new Exception("Dinero insuficiente");
        }
        double dineroADevolver = totalAPagar - registroVentaEmpleado.dinero();
        if (producto == null){
            throw new Exception("Error con el producto inexsistente");
        }

        venta.setEmpleado(empleado.get());

        venta.setFechaVenta(registroVentaEmpleado.fechaVenta());
        venta.setHoraDeVenta(registroVentaEmpleado.horaDeVenta());
        venta.setCantidad(registroVentaEmpleado.cantidad());
        venta.setDineroADevolver(dineroADevolver);
        venta.setProducto(producto);

        if(registroVentaEmpleado.cantidad() > producto.getCantidad()){
            int cantidadARestar = (producto.getCantidad() - registroVentaEmpleado.cantidad()) * -1;
            throw  new Exception("No hay tanta cantidad de producto, si desea puede restarle " + cantidadARestar + " a la cantidad de productos");
        }

        int nuevaCantidad = producto.getCantidad() - registroVentaEmpleado.cantidad();
        producto.setCantidad(nuevaCantidad);

        venta.setPrecioUnitario(registroVentaEmpleado.precioUnitario());

        VentaEmpleado ventaNueva = ventaRepo.save(venta);
        productoRepo.save(producto);

        if(cliente.isPresent()){
            emailServicio.enviarCorreo(new EmailDTO(
                    cliente.get().getCorreo(),
                    "Se ha registrado la compra con éxito",
                    "La compra del producto " + registroVentaEmpleado.nombreProducto() + " ha sido un" +
                            " exito, gracias por comprar en la Ventanilla Gimli. La cantidad de producto comprado fue de: " + registroVentaEmpleado.cantidad() +  "." +
                            " Sus devueltas son: " + (dineroADevolver)*(-1) + "$"
            ));
        }

        return ventaNueva.getCodigo();
    }

    public VentaCliente registroVentaCliente(Optional<Cliente> cliente, Producto producto,
                                             RegistroVentaEmpleadoDTO registroVentaEmpleado,
                                             LocalDate fechaActual, String horaActualString){

        VentaCliente venta = new VentaCliente();

        venta.setProducto(producto);
        venta.setCliente(cliente.get());
        venta.setDireccion(cliente.get().getDireccion());
        venta.setCantidad(registroVentaEmpleado.cantidad());
        venta.setPrecioUnitario(producto.getPrecio());
        venta.setFechaVenta(fechaActual);
        venta.setHoraDeVenta(horaActualString);

        ventaRepoClienteRepo.save(venta);

        return  venta;

    }

    private void hacerRegistroVenta(String nombreProducto, Producto productoEncontrado, int cantidad,
                                    LocalDate fechaActual, String horaActual, String direccion, double dineroADevolver, VentaCliente venta) {

        DetalleVentaCliente Detalleventa = new DetalleVentaCliente();
        Detalleventa.setNombreProducto(nombreProducto);
        Detalleventa.setDescripcion(productoEncontrado.getDescripcion());
        Detalleventa.setPrecio( productoEncontrado.getPrecio());
        Detalleventa.setCantidad(cantidad);
        Detalleventa.setFechaVenta(fechaActual);
        Detalleventa.setHoraDeVenta(horaActual);
        Detalleventa.setDireccion(direccion);
        Detalleventa.setDevueltas(dineroADevolver * (-1));
        Detalleventa.setNombreCliente(venta.getCliente().getNombre());
        Detalleventa.setCorreoCliente(venta.getCliente().getCorreo());
        Detalleventa.setTelefono(venta.getCliente().getTelefono());
        Detalleventa.setVenta(venta);

        detalleVentaClienteRepo.save(Detalleventa);

    }

    /**
     * Encuentra productos en base al nombre
     * @param nombre
     * @return
     * @throws Exception
     */
    public Producto encontrarProducto(String nombre) throws Exception {

        Producto productoEncontradoAlcohol = productoRepo.findByNombresAlcohol(nombre);

        if(productoEncontradoAlcohol == null){

            Producto productoEncontradoGaseosas = productoRepo.findByNombresGaseosas(nombre);

            if(productoEncontradoGaseosas == null){

                Producto productoEncontradoDulces = productoRepo.findByNombresDulces(nombre);

                if(productoEncontradoDulces == null){
                    throw new Exception("Error con el producto");
                }else{
                    return productoRepo.findByNombresDulces(nombre);
                }

            }else{
                return productoEncontradoGaseosas;
            }
        }else{
            return productoEncontradoAlcohol;
        }
    }

    @Override
    public int registrarProducto(RegistroProductoDTO registroProductoDTO) throws Exception {

        // Crear una nueva instancia de Producto
        Producto producto = new Producto();

        // Establecer la categoría y subcategoría del producto a partir del DTO
        producto.setCategoria(registroProductoDTO.categoria());
        producto.setSubcategoria(registroProductoDTO.subcategoria());

        // Establecer otros atributos del producto a partir del DTO
        // producto.setNombre(registroProductoDTO.nombre()); // Comentado, ya que el nombre se maneja más adelante
        producto.setProveedor(registroProductoDTO.proveedor());
        producto.setDescripcion(registroProductoDTO.descripcion());
        producto.setPrecio(registroProductoDTO.precio());
        producto.setCantidad(registroProductoDTO.cantidad());

        // Verificar y manejar el producto según su categoría
        if(producto.getCategoria().equals(Categoria.ALCOHOL)) {
            // Verificar si el nombre ya existe en la base de datos para productos de categoría ALCOHOL
            Producto productoExistente = productoRepo.findByNombresAlcohol(registroProductoDTO.nombre());
            if (productoExistente != null) {
                throw new Exception("Esa bebida ya ha sido agregada con anterioridad");
            }

            // Verificar si el nombre ya está en la lista actual de nombresAlcohol
            if (producto.getNombresAlcohol().contains(registroProductoDTO.nombre())) {
                throw new Exception("Esa bebida ya ha sido agregada con anterioridad");
            }

            // Agregar el nombre del producto a la lista de nombresAlcohol
            producto.getNombresAlcohol().add(registroProductoDTO.nombre());

        } else if (producto.getCategoria().equals(Categoria.DULCES)) {
            // Verificar si el nombre ya existe en la base de datos para productos de categoría DULCES
            Producto productoExistente = productoRepo.findByNombresDulces(registroProductoDTO.nombre());
            if(productoExistente != null){
                throw new Exception("Ese dulce ya ha sido agregado con anterioridad");
            }

            // Verificar si el nombre ya está en la lista actual de nombresDulces
            if(producto.getNombresDulces().contains(registroProductoDTO.nombre())){
                throw new Exception("Ese dulce ya ha sido agregado con anterioridad");
            }

            // Agregar el nombre del producto a la lista de nombresDulces
            producto.getNombresDulces().add(registroProductoDTO.nombre());

        } else {
            // Verificar si el nombre ya existe en la base de datos para productos de otras categorías (asumimos GASEOSAS aquí)
            Producto productoExistente = productoRepo.findByNombresGaseosas(registroProductoDTO.nombre());
            if(productoExistente != null){
                throw new Exception("Esa gaseosa ya ha sido agregada con anterioridad");
            }

            // Verificar si el nombre ya está en la lista actual de nombresGaseosas
            if(producto.getNombresDulces().contains(registroProductoDTO.nombre())){
                throw new Exception("Esa gaseosa ya ha sido agregada con anterioridad");
            }

            // Agregar el nombre del producto a la lista de nombresGaseosas
            producto.getNombresGaseosas().add(registroProductoDTO.nombre());
        }

        // Guardar el nuevo producto en el repositorio
        Producto productoNuevo = productoRepo.save(producto);

        // Retornar el código del nuevo producto
        return productoNuevo.getCodigo();
    }


    @Override
    public int agregarProducto(AgregarProductoDTO agregarProductoDTO) throws Exception {

        // Verificar si la cantidad es mayor o igual a 100 y lanzar una excepción si es el caso
        if(agregarProductoDTO.cantidad() >= 100){
            throw new Exception("Cantidad exagerada de producto");
        }

        // Verificar la categoría del producto y proceder en consecuencia
        if(agregarProductoDTO.categoria().equals(Categoria.ALCOHOL)){

            // Buscar el producto por nombre en la categoría ALCOHOL
            Producto productoEncontrado = productoRepo.findByNombresAlcohol(agregarProductoDTO.nombre());

            // Calcular la nueva cantidad sumando la cantidad actual con la cantidad a agregar
            int nuevaCantidad = productoEncontrado.getCantidad() + agregarProductoDTO.cantidad();
            productoEncontrado.setCantidad(nuevaCantidad);

            // Guardar el producto actualizado en la base de datos
            productoRepo.save(productoEncontrado);

        } else if (agregarProductoDTO.categoria().equals(Categoria.DULCES)) {

            // Buscar el producto por nombre en la categoría DULCES
            Producto productoEncontrado = productoRepo.findByNombresDulces(agregarProductoDTO.nombre());

            // Calcular la nueva cantidad sumando la cantidad actual con la cantidad a agregar
            int nuevaCantidad = productoEncontrado.getCantidad() + agregarProductoDTO.cantidad();
            productoEncontrado.setCantidad(nuevaCantidad);

            // Guardar el producto actualizado en la base de datos
            productoRepo.save(productoEncontrado);

        } else {

            // Buscar el producto por nombre en la categoría GASEOSAS (asumimos esta categoría)
            Producto productoEncontrado = productoRepo.findByNombresGaseosas(agregarProductoDTO.nombre());

            // Calcular la nueva cantidad sumando la cantidad actual con la cantidad a agregar
            int nuevaCantidad = productoEncontrado.getCantidad() + agregarProductoDTO.cantidad();
            productoEncontrado.setCantidad(nuevaCantidad);

            // Guardar el producto actualizado en la base de datos
            productoRepo.save(productoEncontrado);
        }

        // Retornar 0 para indicar que la operación se completó sin problemas
        return 0;
    }


    @Override
    public DetalleProductoDTO verDetalleProducto(int codigoProducto) throws Exception {

        // Buscar el producto por su código en la base de datos
        Optional<Producto> productoEncontrado = productoRepo.findById(codigoProducto);

        // Verificar si el producto fue encontrado
        if (!productoEncontrado.isPresent()) {
            throw new Exception("Producto no encontrado");
        }

        Producto producto = productoEncontrado.get();

        // Verificar la categoría del producto y proceder en consecuencia
        if (producto.getCategoria().equals(Categoria.ALCOHOL)) {
            // Si la categoría es ALCOHOL, iterar sobre los nombres de alcohol y retornar el primer detalle encontrado
            for (String nombre : producto.getNombresAlcohol()) {
                return new DetalleProductoDTO(
                        producto.getCodigo(),
                        nombre,
                        producto.getDescripcion(),
                        producto.getPrecio(),
                        producto.getCantidad(),
                        producto.getCategoria(),
                        producto.getSubcategoria(),
                        producto.getProveedor()
                );
            }
        } else if (producto.getCategoria().equals(Categoria.DULCES)) {
            // Si la categoría es DULCES, iterar sobre los nombres de dulces y retornar el primer detalle encontrado
            for (String nombre : producto.getNombresDulces()) {
                return new DetalleProductoDTO(
                        producto.getCodigo(),
                        nombre,
                        producto.getDescripcion(),
                        producto.getPrecio(),
                        producto.getCantidad(),
                        producto.getCategoria(),
                        producto.getSubcategoria(),
                        producto.getProveedor()
                );
            }
        } else {
            // Si la categoría es otra (asumimos GASEOSAS), iterar sobre los nombres de gaseosas y retornar el primer detalle encontrado
            for (String nombre : producto.getNombresGaseosas()) {
                return new DetalleProductoDTO(
                        producto.getCodigo(),
                        nombre,
                        producto.getDescripcion(),
                        producto.getPrecio(),
                        producto.getCantidad(),
                        producto.getCategoria(),
                        producto.getSubcategoria(),
                        producto.getProveedor()
                );
            }
        }

        // Retornar null si no se encuentra ningún detalle del producto (esto no debería ocurrir normalmente)
        return null;
    }


    @Override
    @Transactional
    public FiltroBusquedaDTO filtrarProductoPorNombre(String nombreProducto) {

        // Obtener todos los productos de la base de datos
        List<Producto> productos = productoRepo.findAll();

        // Buscar el producto por nombre en la categoría ALCOHOL
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

        // Buscar el producto por nombre en la categoría DULCES
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

        // Buscar el producto por nombre en la categoría GASEOSAS
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

        // Si no encontramos ninguna coincidencia, retornamos null
        return null;
    }


    @Override
    @Transactional
    public List<ItemProductoDTO> listarProductos() {

        // Obtener todos los productos de la base de datos
        List<Producto> productos = productoRepo.findAllIgnoreCantidad();
        List<ItemProductoDTO> productosARetornar = new ArrayList<>();

        // Iterar sobre todos los productos
        for (Producto producto : productos) {

            // Agregar productos de la categoría ALCOHOL si la cantidad es mayor o igual a 1
            for (String nombre : producto.getNombresAlcohol()) {
                if (producto.getCantidad() >= 1) {
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

            // Agregar productos de la categoría DULCES si la cantidad es mayor o igual a 1
            for (String nombre : producto.getNombresDulces()) {
                if (producto.getCantidad() >= 1) {
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

            // Agregar productos de la categoría GASEOSAS si la cantidad es mayor o igual a 1
            for (String nombre : producto.getNombresGaseosas()) {
                if (producto.getCantidad() >= 1) {
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

        // Retornar la lista de productos que cumplen con los criterios
        return productosARetornar;
    }

    @Override
    @Transactional
    public List<ItemProductoDTO> listarProductosConCantidad0() {

        // Obtener todos los productos de la base de datos
        List<Producto> productos = productoRepo.findAll();
        List<ItemProductoDTO> productosARetornar = new ArrayList<>();

        // Iterar sobre todos los productos
        for (Producto producto : productos) {

            // Agregar productos de la categoría ALCOHOL si la cantidad es mayor o igual a 1
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

            // Agregar productos de la categoría DULCES si la cantidad es mayor o igual a 1
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

            // Agregar productos de la categoría GASEOSAS si la cantidad es mayor o igual a 1
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

        // Retornar la lista de productos que cumplen con los criterios
        return productosARetornar;
    }

}
