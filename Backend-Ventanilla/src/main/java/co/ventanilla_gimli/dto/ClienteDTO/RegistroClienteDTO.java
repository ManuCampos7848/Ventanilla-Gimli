package co.ventanilla_gimli.dto.ClienteDTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import org.aspectj.weaver.ast.Not;

public record RegistroClienteDTO (
        String nombre,
        String telefono,
        String direccion,
        @NotNull @Email String correo,
        String password

){
}
