import { Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { DetalleProductoDTO } from 'src/app/model/DetalleProductoDTO';
import { Alerta } from 'src/app/model/alerta';
import { VentanillaService } from 'src/app/servicios/ventanilla.service';

@Component({
  selector: 'app-detalle-producto',
  templateUrl: './detalle-producto.component.html',
  styleUrls: ['./detalle-producto.component.css']
})
export class DetalleProductoComponent {

  detalleProductoDTO: DetalleProductoDTO | undefined; // Objeto para almacenar los detalles del producto
  alerta!: Alerta; // Variable para mostrar alertas en caso de error

  codigoProducto = 0; // Código del producto a consultar

  constructor(private route: ActivatedRoute, private ventanillaService: VentanillaService) {
    // Suscripción a los parámetros de la ruta para obtener el código del producto
    this.route.params.subscribe(params => {
      this.codigoProducto = params['codigoProducto'];
    });

    // Llamada inicial para obtener y mostrar los detalles del producto
    this.verDetalleRegistroProducto();
  }

  public verDetalleRegistroProducto() {
    // Llamada al servicio para obtener los detalles del producto según su código
    this.ventanillaService.verDetalleProducto(this.codigoProducto).subscribe({
      next: data => {
        this.detalleProductoDTO = data.respuesta; // Asignar los detalles del producto obtenidos
      },
      error: error => {
        this.alerta = { mensaje: error.error.respuesta, tipo: "danger" }; // Mostrar mensaje de error en caso de fallo
        console.log(error);
      }
    });
  }
}
