package co.ventanilla_gimli.controladores;

import co.ventanilla_gimli.dto.*;
import co.ventanilla_gimli.dto.TokenDTO.MensajeDTO;
import co.ventanilla_gimli.model.Categoria;
import co.ventanilla_gimli.model.Subcategoria;
import co.ventanilla_gimli.servicios.interfaces.VentanillaServicio;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ventanilla")
@RequiredArgsConstructor
public class VentanillaController {

    private final VentanillaServicio ventanillaServicio;

    @GetMapping("/lista-categorias")
    public ResponseEntity<MensajeDTO<List<Categoria>>> listarCategorias(){
        return ResponseEntity.ok().body( new MensajeDTO<>(false,
                ventanillaServicio.listarCategorias()));
    }

    @GetMapping("/filtar-productos-nombre/{nombreProducto}")
    public ResponseEntity<MensajeDTO<FiltroBusquedaDTO>> filtrarProductoPorNombre(@PathVariable String nombreProducto) throws Exception{
        return ResponseEntity.ok().body( new MensajeDTO<>(false,
                ventanillaServicio.filtrarProductoPorNombre(nombreProducto)));
    }

    @GetMapping("/listar-productos")
    public ResponseEntity<MensajeDTO<List<ItemProductoDTO>>> listarProductos() throws Exception{
        return ResponseEntity.ok().body( new MensajeDTO<>(false,
                ventanillaServicio.listarProductos()));
    }

    @GetMapping("/listar-productos-cantidad-0")
    public ResponseEntity<MensajeDTO<List<ItemProductoDTO>>> listarProductosConCantidad0() throws Exception{
        return ResponseEntity.ok().body( new MensajeDTO<>(false,
                ventanillaServicio.listarProductosConCantidad0()));
    }

    @GetMapping("/detalle-producto/{codigoProducto}")
    public ResponseEntity<MensajeDTO<DetalleProductoDTO>> verDetalleRegistro(@PathVariable int codigoProducto) throws Exception{
        return ResponseEntity.ok().body( new MensajeDTO<>(false,
                ventanillaServicio.verDetalleProducto(codigoProducto)));
    }

    @GetMapping("/lista-subcategorias")
    public ResponseEntity<MensajeDTO<List<Subcategoria>>> listarSubcategorias(){
        return ResponseEntity.ok().body( new MensajeDTO<>(false,
                ventanillaServicio.listarSubcategorias()));
    }

    @PostMapping("/registrar-producto")
    public ResponseEntity<MensajeDTO<String>> registrarProducto(@Valid @RequestBody RegistroProductoDTO registroProductoDTO) throws Exception{
        ventanillaServicio.registrarProducto(registroProductoDTO);
        return ResponseEntity.ok().body(new MensajeDTO<>(false, "Producto registrado correctamente"));
    }

    @GetMapping("/lista-nombres-alcoholes/{categoria}")
    public ResponseEntity<MensajeDTO<List<String>>> listarNombresAlcoholes(@PathVariable Categoria categoria) throws Exception{
        return ResponseEntity.ok().body( new MensajeDTO<>(false,
                ventanillaServicio.listarNombresAlcoholes(categoria)));
    }

    @GetMapping("/lista-nombres-dulces/{categoria}")
    public ResponseEntity<MensajeDTO<List<String>>> listarNombresDulces(@PathVariable Categoria categoria) throws Exception{
        return ResponseEntity.ok().body( new MensajeDTO<>(false,
                ventanillaServicio.listarNombresDulces(categoria)));
    }

    @GetMapping("/lista-nombres-gaseosas/{categoria}")
    public ResponseEntity<MensajeDTO<List<String>>> listarNombresGaseosas(@PathVariable Categoria categoria) throws Exception{
        return ResponseEntity.ok().body( new MensajeDTO<>(false,
                ventanillaServicio.listarNombresGaseosas(categoria)));
    }

    /**
     * Cantidades mayores a 0
     * @param categoria
     * @return
     * @throws Exception
     */
    @GetMapping("/lista-nombres-alcoholes-cantidad-mayor-1/{categoria}")
    public ResponseEntity<MensajeDTO<List<String>>> listarNombresAlcoholesMayor1(@PathVariable Categoria categoria) throws Exception{
        return ResponseEntity.ok().body( new MensajeDTO<>(false,
                ventanillaServicio.listarNombresAlcoholesMayor1(categoria)));
    }

    @GetMapping("/lista-nombres-dulces-cantidad-mayor-1/{categoria}")
    public ResponseEntity<MensajeDTO<List<String>>> listarNombresDulcesMayor1(@PathVariable Categoria categoria) throws Exception{
        return ResponseEntity.ok().body( new MensajeDTO<>(false,
                ventanillaServicio.listarNombresDulcesMayor1(categoria)));
    }

    @GetMapping("/lista-nombres-gaseosas-cantidad-mayor-1/{categoria}")
    public ResponseEntity<MensajeDTO<List<String>>> listarNombresGaseosasMayor1(@PathVariable Categoria categoria) throws Exception{
        return ResponseEntity.ok().body( new MensajeDTO<>(false,
                ventanillaServicio.listarNombresGaseosasMayor1(categoria)));
    }

    @PostMapping("/registrar-venta")
    public ResponseEntity<MensajeDTO<String>> registrarVentaEmpleado(@Valid @RequestBody RegistroVentaEmpleadoDTO registroVentaEmpleadoDTO) throws Exception{
        ventanillaServicio.registrarVentaEmpleado(registroVentaEmpleadoDTO);
        return ResponseEntity.ok().body(new MensajeDTO<>(false, "VentaEmpleado registrada correctamente"));
    }

    @GetMapping("/obtener-codigo-cliente/{correo}")
    public ResponseEntity<MensajeDTO<Integer>> obtenerClientePorCorreo(@PathVariable String correo) throws Exception{
        return ResponseEntity.ok().body( new MensajeDTO<>(false,
                ventanillaServicio.encontrarClientePorCorreo(correo)));
    }

}
