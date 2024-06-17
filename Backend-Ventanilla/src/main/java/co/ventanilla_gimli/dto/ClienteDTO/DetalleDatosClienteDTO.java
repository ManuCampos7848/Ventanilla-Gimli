package co.ventanilla_gimli.dto.ClienteDTO;

public record DetalleDatosClienteDTO(
       int codigo,
       String nombreCliente,
       String correoCliente,
       String telefono,
       String direccion
) {
}
