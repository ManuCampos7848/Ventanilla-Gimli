import { Component } from '@angular/core';
import { Router } from '@angular/router';  // Importación de Router para navegación
import { ItemEmpleadoDTO } from 'src/app/model/ItemEmpleadoDTO';  // Importación de DTOs y modelos necesarios
import { ModificarEmpleadoAdminDTO } from 'src/app/model/ModificarEmpleadoAdminDTO';
import { Alerta } from 'src/app/model/alerta';  // Importación del modelo de alerta
import { AdministradorService } from 'src/app/servicios/administrador.service';  // Importación del servicio de administrador

@Component({
  selector: 'app-editar-perfil-empleado-admin',
  templateUrl: './editar-perfil-empleado-admin.component.html',
  styleUrls: ['./editar-perfil-empleado-admin.component.css']
})
export class EditarPerfilEmpleadoAdminComponent {

  modificarEmpleadoDTO: ModificarEmpleadoAdminDTO;  // DTO para modificar empleado
  alerta!: Alerta;  // Variable para mostrar alertas
  listaCedulas: string[];  // Arreglo para almacenar las cédulas de los empleados
  empleados: ItemEmpleadoDTO[];  // Arreglo para almacenar los empleados

  constructor(private adminService: AdministradorService) {
    this.listaCedulas = [];  // Inicialización del arreglo de cédulas vacío
    this.modificarEmpleadoDTO = new ModificarEmpleadoAdminDTO();  // Inicialización del DTO para modificar empleado

    this.empleados = [];  // Inicialización del arreglo de empleados vacío

    // Métodos para obtener la lista de cédulas y empleados al inicializar el componente
    this.obtenerCedulas();
    this.listarEmpleadoCedulaNombre();
  }

  // Método para obtener la lista de cédulas de los empleados desde el servicio
  public obtenerCedulas() {
    this.adminService.listarCedulas().subscribe({
      next: data => {
        this.listaCedulas = data.respuesta;  // Almacenar las cédulas obtenidas en listaCedulas
      },
      error: error => {
        console.log(error);  // Manejar errores en caso de fallo en la obtención de cédulas
      }
    });
  }

  // Método para obtener la lista de empleados (cédula y nombre) desde el servicio
  public listarEmpleadoCedulaNombre() {
    this.adminService.listarEmpleadoCedulaNombre().subscribe({
      next: data => {
        this.empleados = data.respuesta;  // Almacenar los empleados obtenidos en empleados
      },
      error: error => {
        console.log(error);  // Manejar errores en caso de fallo en la obtención de empleados
      }
    });
  }

  // Método para verificar si las contraseñas ingresadas coinciden
  public sonIguales(): boolean {
    return this.modificarEmpleadoDTO.password == this.modificarEmpleadoDTO.confirmaPassword;
  }

  // Método para verificar si algún campo requerido está vacío
  camposVacios(): boolean {
    return !this.modificarEmpleadoDTO.nombre || !this.modificarEmpleadoDTO.telefono || !this.modificarEmpleadoDTO.correo || !this.modificarEmpleadoDTO.password || !this.modificarEmpleadoDTO.confirmaPassword;
  }

  // Método para modificar el perfil del empleado usando el servicio de administrador
  public modificarPerfilEmpleado() {
    this.adminService.editarPerfilEmpleado(this.modificarEmpleadoDTO).subscribe({
      next: data => {
        this.alerta = { tipo: "success", mensaje: "Perfil modificado exitosamente" };  // Mostrar alerta de éxito
        window.location.reload();  // Recargar la página después de modificar el perfil
      },
      error: error => {
        this.alerta = { mensaje: error.error.respuesta, tipo: "danger" };  // Mostrar alerta de error
        console.log(error);  // Manejar errores en la edición del perfil
      }
    });
  }

  // Método para eliminar la cuenta de un empleado usando el servicio de administrador
  public eliminarCuenta() {
    const confirmacion = confirm('¿Estás seguro de que quieres eliminar la cuenta? ' + this.modificarEmpleadoDTO.cedulaPrevia);

    if (confirmacion) {
      let codigo = this.modificarEmpleadoDTO.cedulaPrevia;

      if (codigo == "") {
        this.alerta = { tipo: "danger", mensaje: "Primero seleccione la cuenta que desea eliminar." };
      } else {
        this.adminService.eliminarCuentaEmpleado(codigo).subscribe({
          next: data => {
            alert('Cuenta eliminada con éxito');  // Mostrar alerta de éxito al eliminar la cuenta
            location.reload();  // Recargar la página después de eliminar la cuenta
          },
          error: error => {
            console.log(error);  // Manejar errores en la eliminación de la cuenta
          }
        });
      }
    } else {
      console.log('Operación de eliminación de cuenta cancelada');  // Mostrar mensaje en consola si se cancela la eliminación de la cuenta
    }
  }
}
