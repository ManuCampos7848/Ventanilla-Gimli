package co.ventanilla_gimli.servicios.interfaces;

import co.ventanilla_gimli.dto.*;
import co.ventanilla_gimli.model.Categoria;
import co.ventanilla_gimli.model.Subcategoria;

import java.util.List;

public interface VentanillaServicio {

    List<Categoria> listarCategorias();
    List<Subcategoria> listarSubcategorias();
    List<String> listarNombresAlcoholes(Categoria categoria);
    List<String> listarNombresDulces(Categoria categoria);
    List<String> listarNombresGaseosas(Categoria categoria);
    List<String> listarNombresAlcoholesMayor1(Categoria categoria);
    List<String> listarNombresDulcesMayor1(Categoria categoria);
    List<String> listarNombresGaseosasMayor1(Categoria categoria);
     int registrarProducto(RegistroProductoDTO registroProductoDTO) throws Exception;
     int registrarVentaEmpleado(RegistroVentaEmpleadoDTO registroVentaEmpleado) throws Exception;
    int agregarProducto(AgregarProductoDTO agregarProductoDTO) throws Exception;
    List<ItemProductoDTO> listarProductos();
    List<ItemProductoDTO> listarProductosConCantidad0();
    DetalleProductoDTO verDetalleProducto(int codigoProducto) throws Exception;
    FiltroBusquedaDTO filtrarProductoPorNombre(String nombreProducto);
    int encontrarClientePorCorreo(String correo)throws Exception;
}
