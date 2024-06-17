import { Component } from '@angular/core';
import { ModificarEmpleadoDTO } from 'src/app/model/ModificarEmpleadoDTO';
import { Alerta } from 'src/app/model/alerta';

@Component({
  selector: 'app-editar-perfil-empleado',
  templateUrl: './editar-perfil-empleado.component.html',
  styleUrls: ['./editar-perfil-empleado.component.css']
})
export class EditarPerfilEmpleadoComponent {

  modificarEmpleadoDTO: ModificarEmpleadoDTO;  // Objeto para almacenar los datos a modificar del empleado
  alerta!: Alerta;  // Variable para mostrar alertas

  constructor() {
    this.modificarEmpleadoDTO = new ModificarEmpleadoDTO();  // Inicialización del objeto modificarEmpleadoDTO
  }

  // Método para verificar si las contraseñas ingresadas coinciden
  public sonIguales(): boolean {
    return this.modificarEmpleadoDTO.password == this.modificarEmpleadoDTO.confirmaPassword;
  }

  // Método para verificar si algún campo requerido está vacío
  camposVacios(): boolean {
    return !this.modificarEmpleadoDTO.nombre || !this.modificarEmpleadoDTO.telefono || !this.modificarEmpleadoDTO.correo || !this.modificarEmpleadoDTO.password || !this.modificarEmpleadoDTO.confirmaPassword;
  }

  // Método para modificar el perfil del empleado
  public modificarPerfilEmpleado() {
    // Aquí deberías implementar la lógica para enviar los datos a tu servicio y procesar la modificación del perfil del empleado
    // Por ejemplo:
    // this.miServicio.modificarPerfil(this.modificarEmpleadoDTO).subscribe({
    //   next: data => {
    //     this.alerta = { tipo: "success", mensaje: "Perfil modificado exitosamente" };
    //   },
    //   error: error => {
    //     this.alerta = { tipo: "danger", mensaje: error.error.respuesta };
    //     console.log(error);
    //   }
    // });
  }
}
