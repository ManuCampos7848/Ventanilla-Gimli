import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { Alerta } from 'src/app/model/alerta';
import { LoginDTO } from 'src/app/model/login-dto';
//import { Alerta } from 'src/app/modelo/alerta';
import { AuthService } from 'src/app/servicios/auth.service';
import { TokenService } from 'src/app/servicios/token.service';
import { VentanillaService } from 'src/app/servicios/ventanilla.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {

  loginDTO: LoginDTO;  // DTO para almacenar datos de inicio de sesión
  alerta!: Alerta;  // Objeto para mostrar alertas en la interfaz
  correo: string = '';  // Variable para almacenar el correo (no se usa actualmente en el código proporcionado)

  constructor(
    private authService: AuthService,  // Servicio para autenticación de usuarios
    private tokenService: TokenService,  // Servicio para manejar tokens de autenticación
    private router: Router  // Router para navegación entre componentes
  ) {
    this.loginDTO = new LoginDTO();  // Inicializar el DTO para inicio de sesión
  }

  // Método para realizar el inicio de sesión
  public login() {
    if (this.loginDTO.correo == "" && this.loginDTO.password == "") {
      this.alerta = { mensaje: "Rellene los campos primero", tipo: "danger" };  // Validar campos vacíos
    } else {
      this.authService.login(this.loginDTO).subscribe({
        next: (data: { respuesta: { token: any; }; }) => {
          this.tokenService.login(data.respuesta.token);  // Almacenar el token de sesión en el servicio TokenService
        },
        error: (error: { error: { respuesta: any; }; }) => {
          this.alerta = { mensaje: error.error.respuesta, tipo: "danger" };  // Mostrar mensaje de error en caso de fallo de inicio de sesión
        }
      });
    }
  }

}
