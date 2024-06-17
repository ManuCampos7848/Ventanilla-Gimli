import { Component, ViewChild } from '@angular/core';
import { NgForm } from '@angular/forms';
import { Router } from '@angular/router';
import { RegistroClienteDTO } from 'src/app/model/RegistroClienteDTO';
import { Alerta } from 'src/app/model/alerta';
import { RegistroEmpleadoDTO } from 'src/app/model/registro-empleado-dto';
import { AdministradorService } from 'src/app/servicios/administrador.service';
import { AuthService } from 'src/app/servicios/auth.service';

@Component({
  selector: 'app-registro-empleado',
  templateUrl: './registro-empleado.component.html',
  styleUrls: ['./registro-empleado.component.css']
})
export class RegistroEmpleadoComponent {

  registroEmpleadoDTO: RegistroEmpleadoDTO;  // Declara una propiedad para almacenar datos de empleado
  alerta!: Alerta;  // Declara una propiedad que puede contener mensajes de alerta

  @ViewChild('f', { static: false }) registroForm!: NgForm;  // Permite acceder al formulario de registro desde la plantilla HTML

  constructor(
    private authService: AuthService,  // Servicio de autenticación (no utilizado en este código)
    private router: Router,  // Servicio de enrutamiento (no utilizado en este código)
    private administradorService: AdministradorService  // Servicio para la gestión de empleados
  ) {
    this.registroEmpleadoDTO = new RegistroEmpleadoDTO();  // Inicializa el DTO (Data Transfer Object) para registrar empleados
  }

  // Comprueba si las contraseñas ingresadas son iguales
  public sonIguales(): boolean {
    return this.registroEmpleadoDTO.password == this.registroEmpleadoDTO.confirmaPassword;
  }

  // Método principal para registrar un empleado
  public registrar() {
    // Verifica el correo antes de continuar
    if (this.verificarCorreo(this.registroEmpleadoDTO.correo)) {
      // Si el correo no es válido, no hace nada más
    } else {
      // Si el correo es válido, llama al servicio para registrar el empleado
      this.administradorService.registrarEmpleado(this.registroEmpleadoDTO).subscribe({
        next: data => {
          // En caso de éxito, muestra un mensaje de éxito y reinicia el formulario
          this.alerta = { tipo: "success", mensaje: data.respuesta }
          this.registroForm.reset();  // Reinicia el formulario
          console.log(data);  // Muestra la respuesta en la consola
        },
        error: error => {
          // En caso de error, muestra un mensaje de error y loguea el error en la consola
          this.alerta = { mensaje: error.error.respuesta, tipo: "danger" };
          console.log(error);
        }
      });
    }
  }

  // Verifica si alguno de los campos requeridos está vacío o si hay algún error en el formulario
  camposVacios(): boolean {
    return !this.registroEmpleadoDTO.cedula ||
      !this.registroEmpleadoDTO.correo ||
      !this.registroEmpleadoDTO.nombre ||
      !this.registroEmpleadoDTO.telefono ||
      !this.registroEmpleadoDTO.password ||
      !this.registroEmpleadoDTO.confirmaPassword ||
      !this.sonIguales() ||  // Verifica que las contraseñas coincidan
      !this.registroForm.valid ||  // Verifica la validez del formulario según las validaciones de Angular
      !this.correoValido(this.registroEmpleadoDTO.correo);  // Verifica si el correo es válido
  }

  // Verifica si el correo electrónico no comienza con un número
  correoValido(correo: string): boolean {
    return !/^\d/.test(correo);  // Expresión regular que verifica que no comience con un dígito
  }

  // Verifica el dominio del correo electrónico
  public verificarCorreo(correo: string) {
    const dominioGmail = "@gmail.com";
    const dominioHotmail = "@hotmail.com";

    if (correo.endsWith(dominioGmail) || correo.endsWith(dominioHotmail)) {
      return false;  // Si el correo termina con un dominio válido, devuelve falso (correo válido)
    } else {
      // Si el correo no termina con un dominio válido, muestra un mensaje de alerta
      this.alerta = { mensaje: "El correo electrónico debe ser @gmail.com o @hotmail.com, por ejemplo", tipo: "danger" };
      return true;  // Devuelve verdadero (correo no válido)
    }
  }

}
