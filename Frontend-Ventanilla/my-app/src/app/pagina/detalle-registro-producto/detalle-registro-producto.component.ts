import { Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { DetalleRegistroProductoDTO } from 'src/app/model/DetalleRegistroProductoDTO';
import { Alerta } from 'src/app/model/alerta';
import { AdministradorService } from 'src/app/servicios/administrador.service';
import { EmpleadoService } from 'src/app/servicios/empleado.service';
import { TokenService } from 'src/app/servicios/token.service';

@Component({
  selector: 'app-detalle-registro-producto',
  templateUrl: './detalle-registro-producto.component.html',
  styleUrls: ['./detalle-registro-producto.component.css']
})
export class DetalleRegistroProductoComponent {

  detalleProductoDTO: DetalleRegistroProductoDTO | undefined; // Objeto para almacenar los detalles del registro de producto
  codigoRegistro = 0; // Código del registro de producto a consultar

  alerta!: Alerta; // Variable para mostrar alertas

  constructor(
    private empleadoService: EmpleadoService,
    private route: ActivatedRoute,
    private adminService: AdministradorService,
    private tokenService: TokenService
  ) {
    // Suscripción a los parámetros de la ruta para obtener el código del registro de producto
    this.route.params.subscribe(params => {
      this.codigoRegistro = params['codigoRegistro'];
    });

    // Llamada al método para obtener y mostrar el detalle del registro de producto
    this.verDetalleRegistroProducto();
  }

  public verDetalleRegistroProducto() {
    // Verificar el rol del usuario (empleado o administrador) para mostrar el detalle del registro de producto correspondiente
    if (this.tokenService.getRole() == 'empleado') {
      this.empleadoService.verDetalleRegistro(this.codigoRegistro).subscribe({
        next: data => {
          this.detalleProductoDTO = data.respuesta; // Asignar el detalle del registro de producto
        },
        error: error => {
          this.alerta = { mensaje: error.error.respuesta, tipo: "danger" }; // Mostrar mensaje de error en caso de fallo
          console.log(error);
        }
      });
    } else if (this.tokenService.getRole() == 'admin') {
      this.adminService.verDetalleRegistro(this.codigoRegistro).subscribe({
        next: data => {
          this.detalleProductoDTO = data.respuesta; // Asignar el detalle del registro de producto
        },
        error: error => {
          this.alerta = { mensaje: error.error.respuesta, tipo: "danger" }; // Mostrar mensaje de error en caso de fallo
          console.log(error);
        }
      });
    }
  }
}
