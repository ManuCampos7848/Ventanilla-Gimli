import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { DetalleDatosClienteDTO } from 'src/app/model/DetalleDatosClienteDTO';
import { ModificarClienteDTO } from 'src/app/model/ModificarClienteDTO';
import { Alerta } from 'src/app/model/alerta';
import { ClienteService } from 'src/app/servicios/cliente.service';
import { TokenService } from 'src/app/servicios/token.service';

@Component({
  selector: 'app-modificar-perfil-cliente',
  templateUrl: './modificar-perfil-cliente.component.html',
  styleUrls: ['./modificar-perfil-cliente.component.css']
})
export class ModificarPerfilClienteComponent {

  modificarClienteDTO: ModificarClienteDTO;  // DTO para modificar datos del cliente
  alerta!: Alerta;  // Objeto para mostrar alertas en la interfaz

  detalleCliente: DetalleDatosClienteDTO;  // DTO para almacenar detalles del cliente

  constructor(
    private tokenService: TokenService,  // Servicio para manejar tokens de autenticación
    private clienteService: ClienteService,  // Servicio para operaciones relacionadas con clientes
    private router: Router  // Router para navegación entre componentes
  ) {
    this.modificarClienteDTO = new ModificarClienteDTO();  // Inicializar el DTO para modificar cliente
    this.detalleCliente = new DetalleDatosClienteDTO();  // Inicializar el DTO para detalles del cliente

    this.verDetalleDatosCliente();  // Cargar los detalles del cliente al inicializar el componente
  }

  // Método para verificar si las contraseñas ingresadas coinciden
  public sonIguales(): boolean {
    return this.modificarClienteDTO.password == this.modificarClienteDTO.confirmaPassword;
  }

  // Método para modificar el perfil del cliente
  public modificarPerfilCliente() {
    this.modificarClienteDTO.codigoCliente = this.tokenService.getCodigo();  // Asignar el código del cliente desde el token

    this.clienteService.editarPerfil(this.modificarClienteDTO).subscribe({
      next: data => {
        alert("Edición de perfil exitosa");  // Mostrar mensaje de éxito
        this.tokenService.getNombre();  // Obtener y probablemente actualizar el nombre del cliente
        window.location.reload();  // Recargar la página para reflejar los cambios
      },
      error: error => {
        console.log(error);  // Manejar errores de la solicitud de edición de perfil
      }
    });
  }

  // Método para verificar si algún campo obligatorio está vacío
  camposVacios(): boolean {
    return !this.modificarClienteDTO.nombre || !this.modificarClienteDTO.telefono || !this.modificarClienteDTO.direccion || !this.modificarClienteDTO.correo || !this.modificarClienteDTO.password || !this.modificarClienteDTO.confirmaPassword;
  }

  // Método para eliminar la cuenta del cliente
  public eliminarCuenta() {
    const confirmacion = confirm('¿Estás seguro de que quieres eliminar tu cuenta?');  // Mostrar confirmación al usuario

    if (confirmacion) {
      let codigo = this.tokenService.getCodigo();  // Obtener el código del cliente desde el token

      this.clienteService.eliminarCuenta(codigo).subscribe({
        next: data => {
          alert('Cuenta eliminada con éxito');  // Mostrar mensaje de éxito
          this.tokenService.logout();  // Cerrar sesión del cliente
        },
        error: error => {
          console.log(error);  // Manejar errores al eliminar la cuenta del cliente
        }
      });
    } else {
      console.log('Operación de eliminación de cuenta cancelada');  // Informar si el usuario cancela la operación
    }
  }

  // Método para obtener y mostrar los detalles del cliente
  public verDetalleDatosCliente() {
    this.clienteService.verDetalleDatosCliente(this.tokenService.getCodigo()).subscribe({
      next: data => {
        console.log(data.respuesta);  // Mostrar detalles del cliente en la consola (para fines de depuración)
        this.detalleCliente = data.respuesta;  // Asignar los detalles del cliente obtenidos del servicio
      },
      error: error => {
        this.alerta = { mensaje: error.error.respuesta, tipo: "danger" };  // Mostrar mensaje de error en caso de fallo
        console.log(error);  // Manejar errores al obtener detalles del cliente
      }
    });
  }

}
