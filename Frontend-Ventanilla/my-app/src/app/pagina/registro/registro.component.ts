import { Component, ViewChild } from '@angular/core';
import { NgForm } from '@angular/forms';
import { Router } from '@angular/router';
import { RegistroClienteDTO } from 'src/app/model/RegistroClienteDTO';
import { Alerta } from 'src/app/model/alerta';
import { AuthService } from 'src/app/servicios/auth.service';


@Component({
  selector: 'app-registro',
  templateUrl: './registro.component.html',
  styleUrls: ['./registro.component.css']
})
export class RegistroComponent {

  alerta!: Alerta;  // Propiedad para almacenar mensajes de alerta

  termsAccepted = false; // Variable para almacenar el estado de aceptación de términos (no se usa en el código proporcionado)

  @ViewChild('f', { static: false }) registroForm!: NgForm;  // Referencia al formulario de registro desde la plantilla HTML

  registroClienteDTO: RegistroClienteDTO;  // Objeto para almacenar los datos del cliente que se registrará

  constructor(
    private authService: AuthService,  // Servicio de autenticación (no utilizado en este código)
    private router: Router  // Servicio de enrutamiento (no utilizado en este código)
  ) {
    this.registroClienteDTO = new RegistroClienteDTO();  // Inicialización del DTO (Data Transfer Object) para el registro de clientes
  }

  // Método para realizar el registro del cliente
  public registrar() {
    // Verifica el correo antes de continuar
    if (this.verificarCorreo(this.registroClienteDTO.correo)) {
      // Si el correo no es válido, no hace nada más
    } else {
      // Si el correo es válido, llama al servicio para registrar al cliente
      this.authService.registrarCliente(this.registroClienteDTO).subscribe({
        next: data => {
          alert("Cuenta registrada con exito")       
          console.log(data);  // Muestra la respuesta en la consola
          this.router.navigate(['/login']);  // Redirige al usuario a la página de inicio de sesión (comentado en el código original)
        },
        error: error => {
          // En caso de error, muestra un mensaje de error y loguea el error en la consola
          this.alerta = { mensaje: error.error.respuesta, tipo: "danger" };
          console.log(error);
        }
      });
    }
  }

  // Método para obtener la fecha máxima permitida (no se utiliza en el código proporcionado)
  getMaxDate(): string {
    // Obtener la fecha actual en formato YYYY-MM-DD
    const today = new Date();
    const year = today.getFullYear();
    const month = (today.getMonth() + 1).toString().padStart(2, '0');
    const day = today.getDate().toString().padStart(2, '0');

    return `${year}-${month}-${day}`;
  }

  // Verifica si el correo electrónico es válido según los dominios permitidos
  public verificarCorreo(correo: string) {
    const dominioGmail = "@gmail.com";
    const dominioHotmail = "@hotmail.com";

    if (correo.endsWith(dominioGmail) || correo.endsWith(dominioHotmail)) {
      return false;  // Si el correo es válido, devuelve false (correo válido)
    } else {
      // Si el correo no es válido, muestra un mensaje de alerta y devuelve true (correo no válido)
      this.alerta = { mensaje: "El correo electrónico debe ser @gmail.com o @hotmail.com, por ejemplo", tipo: "danger" };
      return true;
    }
  }

  // Comprueba si las contraseñas ingresadas son iguales
  public sonIguales(): boolean {
    return this.registroClienteDTO.password == this.registroClienteDTO.confirmaPassword;
  }

  // Verifica si el correo electrónico no comienza con un número (no se utiliza en el código proporcionado)
  correoValido(correo: string): boolean {
    return !/^\d/.test(correo); // Verifica que no comience con un número
  }

  // Verifica si la dirección no comienza con un número (no se utiliza en el código proporcionado)
  direccionValida(direccion: string): boolean {
    return !/^\d/.test(direccion); // Verifica que no comience con un número
  }

}

