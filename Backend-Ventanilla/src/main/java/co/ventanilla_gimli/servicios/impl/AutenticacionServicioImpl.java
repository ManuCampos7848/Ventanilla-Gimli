package co.ventanilla_gimli.servicios.impl;

import co.ventanilla_gimli.dto.LoginDTO;
import co.ventanilla_gimli.dto.TokenDTO.TokenDTO;
import co.ventanilla_gimli.model.Cuenta;
import co.ventanilla_gimli.model.Empleado;
import co.ventanilla_gimli.model.Cliente;
import co.ventanilla_gimli.repositorios.CuentaRepo;
import co.ventanilla_gimli.repositorios.EmpleadoRepo;
import co.ventanilla_gimli.repositorios.ClienteRepo;
import co.ventanilla_gimli.servicios.interfaces.AutenticacionServicio;
import co.ventanilla_gimli.utils.JWTUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AutenticacionServicioImpl implements AutenticacionServicio {

    /**
     * Acceso a los repositorios para hacer las consultas
     */
    private final CuentaRepo cuentaRepo;
    private final JWTUtils jwtUtils;
    @Override
    public TokenDTO login(LoginDTO loginDTO) throws Exception {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        // Buscar la cuenta por correo electrónico en el repositorio de cuentas
        Optional<Cuenta> cuentaOptional = cuentaRepo.findByCorreo(loginDTO.correo());

        // Verificar si la cuenta existe
        if (cuentaOptional.isEmpty()) {
            throw new Exception("Datos incorrectos, verifique nuevamente");
        }

        // Obtener la cuenta encontrada
        Cuenta cuenta = cuentaOptional.get();

        // Verificar el estado de la cuenta según el tipo
        if (cuenta instanceof Cliente) {
            Cliente cliente = (Cliente) cuenta;
            if (!cliente.isEstado()) {
                throw new Exception("La cuenta ha sido eliminada");
            }
        }
        // Aquí puedes agregar más lógica para otros tipos de cuenta, si es necesario

        // Verificar si la contraseña ingresada coincide con la contraseña almacenada
        if (!passwordEncoder.matches(loginDTO.password(), cuenta.getPassword())) {
            throw new Exception("La contraseña ingresada es incorrecta");
        }

        // Generar un token de autenticación y crear un objeto TokenDTO para retornarlo
        String token = crearToken(cuenta);
        return new TokenDTO(token);
    }


    private String crearToken(Cuenta cuenta) {
        String rol;
        String nombre;

        // Determinar el rol y nombre basado en el tipo de cuenta
        if (cuenta instanceof Cliente) {
            rol = "cliente";
            nombre = ((Cliente) cuenta).getNombre();
        } else if (cuenta instanceof Empleado) {
            rol = "empleado";
            nombre = ((Empleado) cuenta).getNombre();
        } else {
            rol = "admin";
            nombre = "Administrador";
        }

        // Crear un mapa de datos para incluir en el token JWT
        Map<String, Object> map = new HashMap<>();
        map.put("rol", rol);        // Rol del usuario (cliente, empleado, admin)
        map.put("nombre", nombre);  // Nombre del usuario
        map.put("id", cuenta.getCodigo());  // Código de identificación único del usuario

        // Generar el token JWT utilizando un servicio (jwtUtils) para la generación del token
        return jwtUtils.generarToken(cuenta.getCorreo(), map);
    }



}