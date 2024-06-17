import { Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { DetalleVentaEmpleadoDTO } from 'src/app/model/DetalleVentaEmpleadoDTO';
import { Alerta } from 'src/app/model/alerta';
import { AdministradorService } from 'src/app/servicios/administrador.service';
import { EmpleadoService } from 'src/app/servicios/empleado.service';
import { TokenService } from 'src/app/servicios/token.service';

@Component({
  selector: 'app-detalle-venta-empleado',
  templateUrl: './detalle-venta-empleado.component.html',
  styleUrls: ['./detalle-venta-empleado.component.css']
})
export class DetalleVentaEmpleadoComponent {

  detalleVentaEmpleadoDTO: DetalleVentaEmpleadoDTO | undefined; // Objeto para almacenar los detalles de la venta del empleado
  codigoVenta = 0; // Código de la venta a consultar
  alerta!: Alerta; // Variable para mostrar alertas

  constructor(
    private route: ActivatedRoute,
    private empleadoService: EmpleadoService,
    private adminService: AdministradorService,
    private tokenService: TokenService
  ) {
    // Suscripción a los parámetros de la ruta para obtener el código de la venta
    this.route.params.subscribe(params => {
      this.codigoVenta = params['codigoVenta'];
    });

    // Llamada al método para obtener y mostrar el detalle de la venta del empleado
    this.verDetalleVentaEmpleado();
  }

  public verDetalleVentaEmpleado() {
    // Verificar el rol del usuario (empleado o administrador) para mostrar el detalle de la venta del empleado correspondiente
    if (this.tokenService.getRole() == 'empleado') {
      this.empleadoService.verDetalleVentaEmpleado(this.codigoVenta).subscribe({
        next: data => {
          this.detalleVentaEmpleadoDTO = data.respuesta; // Asignar el detalle de la venta del empleado
        },
        error: error => {
          this.alerta = { mensaje: error.error.respuesta, tipo: "danger" }; // Mostrar mensaje de error en caso de fallo
          console.log(error);
        }
      });
    } else if (this.tokenService.getRole() == 'admin') {
      this.adminService.verDetalleVentaEmpleado(this.codigoVenta).subscribe({
        next: data => {
          this.detalleVentaEmpleadoDTO = data.respuesta; // Asignar el detalle de la venta del empleado
        },
        error: error => {
          this.alerta = { mensaje: error.error.respuesta, tipo: "danger" }; // Mostrar mensaje de error en caso de fallo
          console.log(error);
        }
      });
    }
  }

}
