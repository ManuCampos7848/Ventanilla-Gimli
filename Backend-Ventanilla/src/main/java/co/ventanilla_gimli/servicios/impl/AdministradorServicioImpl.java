package co.ventanilla_gimli.servicios.impl;

import co.ventanilla_gimli.dto.*;
import co.ventanilla_gimli.dto.AdministradorDTO.ItemEmpleadoDTO;
import co.ventanilla_gimli.dto.AdministradorDTO.ModificarEmpleadoAdminDTO;
import co.ventanilla_gimli.dto.ClienteDTO.DetalleVentaEmpleadoDTO;
import co.ventanilla_gimli.model.*;
import co.ventanilla_gimli.repositorios.*;
import co.ventanilla_gimli.servicios.interfaces.AdministradorServicio;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdministradorServicioImpl implements AdministradorServicio {

    /**
     * Acceso a los repositorios para hacer las consultas
     */
    private  final ClienteRepo clienteRepo;
    private  final EmpleadoRepo empleadoRepo;
    private  final AdministradorRepo administradorRepo;
    private final ProductoRepo productoRepo;
    private final VentaEmpleadoRepo ventaEmpleadoRepo;
    private final RegistroProductosRepo registroProductosRepo;

    @Override
    public List<String> obtenerEmpleados() {

        // Creamos una lista para almacenar las cédulas de los empleados
        List<String> cedulasARetornar = new ArrayList<>();

        // Obtenemos todos los empleados del repositorio de empleados
        List<Empleado> empleados = empleadoRepo.findAll();

        // Recorremos la lista de empleados
        for (Empleado e : empleados) {
            // Para cada empleado, obtenemos su cédula y la agregamos a la lista de cédulas a retornar
            cedulasARetornar.add(e.getCedula());
        }

        // Retornamos la lista de cédulas de los empleados
        return cedulasARetornar;
    }


    @Override
    public int registrarEmpleado(RegistroEmpleadoDTO registroEmpleadoDTO) throws Exception {

        // Verificamos si el correo ya está en uso
        if (correoRepetido(registroEmpleadoDTO.correo())) {
            // Si el correo está repetido, lanzamos una excepción con un mensaje adecuado
            throw new Exception("El correo digitado ya se encuentra en uso");
        }

        // Creamos un nuevo objeto Empleado y configuramos sus atributos con los datos del DTO recibido
        Empleado empleado = new Empleado();
        empleado.setEstado(true); // Establecemos el estado del empleado como activo (true)
        empleado.setNombre(registroEmpleadoDTO.nombre()); // Asignamos el nombre del empleado
        empleado.setCedula(registroEmpleadoDTO.cedula()); // Asignamos la cédula del empleado
        empleado.setTelefono(registroEmpleadoDTO.telefono()); // Asignamos el teléfono del empleado
        empleado.setCorreo(registroEmpleadoDTO.correo()); // Asignamos el correo del empleado

        // Encriptamos la contraseña usando BCryptPasswordEncoder
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String passwordEncriptada = passwordEncoder.encode(registroEmpleadoDTO.password());
        empleado.setPassword(passwordEncriptada); // Asignamos la contraseña encriptada al empleado

        // Guardamos el objeto Empleado en el repositorio
        empleadoRepo.save(empleado);

        // Retornamos el código generado para el empleado registrado
        return empleado.getCodigo();
    }


    @Override
    public String modificarEmpleado(ModificarEmpleadoAdminDTO empleadoDTO) throws Exception {

        // Buscamos al empleado en el repositorio por su cédula previa
        Empleado empleadoEncontrado = empleadoRepo.findByCedula(empleadoDTO.cedulaPrevia());

        // Verificamos si se encontró al empleado
        if (empleadoEncontrado == null) {
            throw new Exception("No se encontró al empleado");
        }

        // Creamos una copia del empleado encontrado y actualizamos sus atributos con los datos del DTO recibido
        Empleado empleadoNuevo = empleadoEncontrado;
        empleadoNuevo.setCedula(empleadoDTO.cedulaNueva()); // Actualizamos la cédula del empleado
        empleadoNuevo.setNombre(empleadoDTO.nombre()); // Actualizamos el nombre del empleado
        empleadoNuevo.setTelefono(empleadoDTO.telefono()); // Actualizamos el teléfono del empleado
        empleadoNuevo.setCorreo(empleadoDTO.correo()); // Actualizamos el correo del empleado

        // Encriptamos la nueva contraseña usando BCryptPasswordEncoder
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String passwordEncriptada = passwordEncoder.encode(empleadoDTO.password());
        empleadoNuevo.setPassword(passwordEncriptada); // Actualizamos la contraseña encriptada del empleado

        // Guardamos los cambios en el repositorio
        empleadoRepo.save(empleadoNuevo);

        // Retornamos la nueva cédula del empleado modificado
        return empleadoNuevo.getCedula();
    }

    private boolean correoRepetido(String correo) {

        // Buscamos si el correo está siendo utilizado por algún cliente
        Cliente correoCliente = clienteRepo.findClienteByCorreo(correo);

        // Buscamos si el correo está siendo utilizado por algún empleado
        Empleado correoEmpleado = empleadoRepo.findByCorreo(correo);

        // Buscamos si el correo está siendo utilizado por algún administrador
        Administrador correoAdministrador = administradorRepo.findByCorreo(correo);

        // Si encontramos el correo en cualquiera de los repositorios, retornamos true
        if (correoCliente != null || correoEmpleado != null || correoAdministrador != null) {
            return true;
        }

        // Si no encontramos el correo en ninguno de los repositorios, retornamos false
        return false;
    }


    @Override
    public boolean eliminarCuentaEmpleado(String cedula) throws Exception {

        // Busca al cliente en base a cedula
        Empleado clienteEncontrado = empleadoRepo.findByCedula(cedula);

        // Desvincular todas las ventas asociadas al cliente
        ventaEmpleadoRepo.desvincularVentasDelEmpleado(clienteEncontrado);

        // Desvincular todos los registros de productos asociados al cliente
        ventaEmpleadoRepo.desvincularRegistrosDeEmpleado(clienteEncontrado);

        // Finalmente, eliminar el cliente
        empleadoRepo.delete(clienteEncontrado);

        return true;
    }

    @Override
    public List<ItemEmpleadoDTO> encontrarEmpleadosCedulaNombre() throws Exception {

        // Obtenemos todos los empleados del repositorio
        List<Empleado> empleados = empleadoRepo.findAll();

        // Creamos una lista para almacenar los resultados finales
        List<ItemEmpleadoDTO> empleadoARetornar = new ArrayList<>();

        // Iteramos sobre la lista de empleados obtenida
        for (Empleado e : empleados) {
            // Creamos un nuevo objeto ItemEmpleadoDTO y lo agregamos a la lista de resultados
            empleadoARetornar.add(new ItemEmpleadoDTO(
                    e.getNombre(),   // Nombre del empleado
                    e.getCorreo(),   // Correo del empleado
                    e.getCedula()    // Cédula del empleado
            ));
        }

        // Retornamos la lista de objetos ItemEmpleadoDTO
        return empleadoARetornar;
    }


    @Override
    public int agregarProducto(AgregarProductoDTO agregarProductoDTO) throws Exception {

        // Obtenemos la categoría del producto del DTO
        Categoria categoria = agregarProductoDTO.categoria();

        // Verificamos si la categoría no es nula
        if (categoria != null) {
            // Dependiendo de la categoría, realizamos las operaciones correspondientes
            if (categoria.equals(Categoria.ALCOHOL)) {
                // Buscamos el producto por su nombre en la categoría ALCOHOL
                Producto productoEncontrado = productoRepo.findByNombresAlcohol(agregarProductoDTO.nombre());

                // Calculamos la nueva cantidad sumando la cantidad existente y la cantidad a agregar
                int nuevaCantidad = productoEncontrado.getCantidad() + agregarProductoDTO.cantidad();
                productoEncontrado.setCantidad(nuevaCantidad); // Actualizamos la cantidad del producto

                // Guardamos el producto actualizado en la base de datos
                productoRepo.save(productoEncontrado);

                // Realizamos un registro de la agregación del producto
                hacerRegistroDeAgregacion(productoEncontrado, agregarProductoDTO.codigoEmpleado(), agregarProductoDTO.nombre(), agregarProductoDTO.cantidad());

            } else if (categoria.equals(Categoria.DULCES)) {
                // Buscamos el producto por su nombre en la categoría DULCES
                Producto productoEncontrado = productoRepo.findByNombresDulces(agregarProductoDTO.nombre());

                // Calculamos la nueva cantidad y actualizamos el producto
                int nuevaCantidad = productoEncontrado.getCantidad() + agregarProductoDTO.cantidad();
                productoEncontrado.setCantidad(nuevaCantidad);

                productoRepo.save(productoEncontrado);
                hacerRegistroDeAgregacion(productoEncontrado, agregarProductoDTO.codigoEmpleado(), agregarProductoDTO.nombre(), agregarProductoDTO.cantidad());

            } else {
                // Si la categoría no es ALCOHOL ni DULCES, asumimos que es GASEOSAS
                Producto productoEncontrado = productoRepo.findByNombresGaseosas(agregarProductoDTO.nombre());

                int nuevaCantidad = productoEncontrado.getCantidad() + agregarProductoDTO.cantidad();
                productoEncontrado.setCantidad(nuevaCantidad);

                productoRepo.save(productoEncontrado);
                hacerRegistroDeAgregacion(productoEncontrado, agregarProductoDTO.codigoEmpleado(), agregarProductoDTO.nombre(), agregarProductoDTO.cantidad());
            }
        }

        // Retornamos un valor que indica la finalización del método
        return 0;
    }


    public void hacerRegistroDeAgregacion(Producto producto, int codigoEmpleado, String nombreProducto, int cantidad) {

        // Creamos un objeto Administrador temporal para simular el administrador responsable del registro
        Administrador administrador = new Administrador();
        administrador.setCodigo(100); // Establecemos un código arbitrario para el administrador

        // Buscamos al empleado administrador por su ID
        Optional<Empleado> empleadoEncontrado = empleadoRepo.findById(codigoEmpleado);

        // Creamos un objeto Empleado para representar al administrador encontrado
        Empleado empleadoAdmin = empleadoEncontrado.orElse(null); // Usamos orElse para manejar el caso de que no se encuentre el empleado

        // Creamos un nuevo registro de producto
        RegistroProducto r = new RegistroProducto();

        // Establecemos los detalles del registro
        r.setProducto(producto); // Establecemos el producto involucrado en el registro
        r.setEmpleado(empleadoAdmin); // Establecemos el empleado administrador responsable del registro
        r.setCategoria(producto.getCategoria()); // Establecemos la categoría del producto
        r.setSubcategoria(producto.getSubcategoria()); // Establecemos la subcategoría del producto
        r.setNombreProducto(nombreProducto); // Establecemos el nombre del producto
        r.setCantidad(cantidad); // Establecemos la cantidad agregada del producto

        // Establecer la fecha actual del registro
        LocalDate fechaActual = LocalDate.now();
        r.setFechaRegistro(fechaActual);

        // Establecer la hora actual del registro en formato de cadena (String)
        LocalTime horaActual = LocalTime.now();
        String horaActualString = horaActual.toString(); // Convertimos la hora actual a formato de cadena
        r.setHoraDeRegistro(horaActualString);

        // Guardamos el registro de producto en el repositorio
        registroProductosRepo.save(r);
    }


    @Override
    public List<ItemRegistroProductoDTO> listarRegistroProductos() {

        // Obtener todos los registros de productos del repositorio
        List<RegistroProducto> registrosEncontrados = registroProductosRepo.findAll();

        // Lista para almacenar los resultados finales (DTOs de registros de productos)
        List<ItemRegistroProductoDTO> registros = new ArrayList<>();

        // Iterar sobre la lista de registros encontrados
        for (RegistroProducto r : registrosEncontrados) {
            if (r.getEmpleado() != null) {
                // Si el empleado asociado al registro no es nulo, crear un ItemRegistroProductoDTO con sus detalles
                registros.add(new ItemRegistroProductoDTO(
                        r.getCodigo(),                // Código del registro de producto
                        r.getNombreProducto(),        // Nombre del producto registrado
                        r.getCategoria(),             // Categoría del producto registrado
                        r.getEmpleado().getCodigo(),  // Código del empleado asociado al registro
                        r.getFechaRegistro()          // Fecha de registro del producto
                ));
            } else {
                // Si el empleado asociado al registro es nulo, manejar esta situación (puedes asignar un valor predeterminado)
                registros.add(new ItemRegistroProductoDTO(
                        r.getCodigo(),                // Código del registro de producto
                        r.getNombreProducto(),        // Nombre del producto registrado
                        r.getCategoria(),             // Categoría del producto registrado
                        100,                          // Valor predeterminado para el código de empleado
                        r.getFechaRegistro()          // Fecha de registro del producto
                ));
            }
        }

        // Retornar la lista de DTOs de registros de productos
        return registros;
    }


    @Override
    public List<ItemVentaEmpleadoDTO> listaVentasEmpleados() {

        // Obtener todas las ventas registradas por empleados desde el repositorio
        List<VentaEmpleado> ventas = ventaEmpleadoRepo.findAll();

        // Lista para almacenar los resultados finales (DTOs de ventas por empleado)
        List<ItemVentaEmpleadoDTO> ventasARetornar = new ArrayList<>();

        // Iterar sobre la lista de ventas encontradas
        for (VentaEmpleado v : ventas) {
            // Si el empleado asociado a la venta es nulo, inicializamos un objeto Empleado vacío
            if (v.getEmpleado() == null) {
                v.setEmpleado(new Empleado());
            }

            // Creamos un nuevo objeto ItemVentaEmpleadoDTO y lo agregamos a la lista de resultados
            ventasARetornar.add(new ItemVentaEmpleadoDTO(
                    v.getCodigo(),                // Código de la venta
                    v.getFechaVenta(),           // Fecha de la venta
                    v.getHoraDeVenta(),          // Hora de la venta
                    v.getEmpleado().getNombre(), // Nombre del empleado que realizó la venta
                    v.getEmpleado().getCodigo()  // Código del empleado que realizó la venta
            ));
        }

        // Retornamos la lista de DTOs de ventas por empleado
        return ventasARetornar;
    }


    @Override
    @Transactional
    public DetalleVentaEmpleadoDTO verDetalleVentaEmpleado(int codigoVenta) throws Exception {

        // Buscar la venta por su código en el repositorio
        Optional<VentaEmpleado> ventaEncontrada = ventaEmpleadoRepo.findById(codigoVenta);

        // Verificar si la venta fue encontrada
        if (!ventaEncontrada.isPresent()) {
            throw new Exception("Venta no encontrada");
        }

        // Obtener la venta encontrada
        VentaEmpleado venta = ventaEncontrada.get();

        // Determinar el nombre del cliente
        String nombreCliente = (venta.getCliente() != null) ? venta.getCliente().getNombre() : "Cliente casual";

        // Obtener el nombre del producto vendido
        String nombreProducto = encontrarProductoPorCodigo(venta.getProducto().getCodigo());

        // Obtener el empleado asociado a la venta
        Empleado empleado = venta.getEmpleado();
        int codigoEmpleado;
        String nombreEmpleado;

        if (empleado == null) {
            // Si el empleado es null, asignar un código y nombre genérico
            codigoEmpleado = 100; // Código arbitrario para empleado desconocido o eliminado
            nombreEmpleado = "Desconocido o empleado eliminado";
        } else {
            // Si el empleado no es null, obtener su código y nombre
            codigoEmpleado = empleado.getCodigo();
            nombreEmpleado = empleado.getNombre();
        }

        // Crear y retornar el objeto DetalleVentaEmpleadoDTO con los detalles de la venta
        return new DetalleVentaEmpleadoDTO(
                venta.getCodigo(),             // Código de la venta
                venta.getFechaVenta(),         // Fecha de la venta
                venta.getHoraDeVenta(),        // Hora de la venta
                nombreEmpleado,                // Nombre del empleado
                codigoEmpleado,                // Código del empleado
                nombreCliente,                 // Nombre del cliente
                (venta.getCliente() != null) ? venta.getCliente().getCorreo() : null, // Correo del cliente si está disponible
                nombreProducto,                // Nombre del producto vendido
                venta.getProducto().getCodigo(), // Código del producto vendido
                venta.getDineroADevolver() * (-1)
        );
    }

    public String encontrarProductoPorCodigo(int codigo) throws Exception {
        // Buscar el producto en el repositorio por su código
        Optional<Producto> productoEncontrado = productoRepo.findById(codigo);

        // Verificar si el producto fue encontrado
        if (!productoEncontrado.isPresent()) {
            throw new Exception("Producto no encontrado");
        }

        // Obtener el producto encontrado
        Producto producto = productoEncontrado.get();

        // Determinar el nombre del producto basado en su categoría
        if (producto.getCategoria().equals(Categoria.ALCOHOL)) {
            // Si la categoría del producto es ALCOHOL, retornar el primer nombre de alcohol encontrado
            for (String nombre : producto.getNombresAlcohol()) {
                return nombre;
            }
        } else if (producto.getCategoria().equals(Categoria.DULCES)) {
            // Si la categoría del producto es DULCES, retornar el primer nombre de dulces encontrado
            for (String nombre : producto.getNombresDulces()) {
                return nombre;
            }
        } else {
            // Si la categoría del producto es otra (por ejemplo, GASEOSAS), retornar el primer nombre de gaseosas encontrado
            for (String nombre : producto.getNombresGaseosas()) {
                return nombre;
            }
        }

        // Si no se encontró un nombre válido, retornar null
        return null;
    }


    @Override
    public DetalleRegistroProductoDTO verDetalleRegistro(int codigoRegistro) throws Exception {

        // Buscar el registro de producto en el repositorio por su código
        Optional<RegistroProducto> registroEncontrado = registroProductosRepo.findById(codigoRegistro);

        // Verificar si el registro fue encontrado
        if (registroEncontrado.isEmpty()) {
            throw new Exception("No se pudo encontrar el registro");
        }

        // Obtener el registro encontrado
        RegistroProducto registro = registroEncontrado.get();

        // Determinar el empleado asociado al registro
        Empleado empleado = registro.getEmpleado();
        int codigoEmpleado;
        String nombreEmpleado;

        if (empleado == null) {
            // Si el empleado es null, asignar un código y nombre genérico
            codigoEmpleado = 100; // Código arbitrario para empleado desconocido o eliminado
            nombreEmpleado = "Desconocido o empleado eliminado";
        } else {
            // Si el empleado no es null, obtener su código y nombre
            codigoEmpleado = empleado.getCodigo();
            nombreEmpleado = empleado.getNombre();
        }

        // Crear y retornar el objeto DetalleRegistroProductoDTO con los detalles del registro
        return new DetalleRegistroProductoDTO(
                registro.getProducto().getCodigo(),   // Código del producto registrado
                registro.getNombreProducto(),        // Nombre del producto registrado
                registro.getCantidad(),              // Cantidad registrada
                registro.getCategoria(),             // Categoría del producto registrado
                registro.getSubcategoria(),          // Subcategoría del producto registrado
                registro.getFechaRegistro(),         // Fecha de registro del producto
                registro.getHoraDeRegistro(),        // Hora de registro del producto
                codigoEmpleado,                      // Código del empleado que registró el producto
                nombreEmpleado                       // Nombre del empleado que registró el producto
        );
    }

}
