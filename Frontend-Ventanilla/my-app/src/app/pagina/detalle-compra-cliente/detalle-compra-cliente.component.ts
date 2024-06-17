import { Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { DetalleCompraClienteDTO } from 'src/app/model/DetalleCompraClienteDTO';
import { Alerta } from 'src/app/model/alerta';
import { ClienteService } from 'src/app/servicios/cliente.service';

@Component({
  selector: 'app-detalle-compra-cliente',
  templateUrl: './detalle-compra-cliente.component.html',
  styleUrls: ['./detalle-compra-cliente.component.css']
})
export class DetalleCompraClienteComponent {

  detalle: DetalleCompraClienteDTO | undefined; // Objeto para almacenar los detalles de la compra
  codigoRegistro = 0; // Código de la compra a consultar

  alerta!: Alerta; // Variable para mostrar alertas en caso de error

  constructor(private route: ActivatedRoute,
              private clienteService: ClienteService
  ){
    // Suscripción a los parámetros de la ruta para obtener el código de la compra
    this.route.params.subscribe(params => {
      this.codigoRegistro = params['codigoCompra'];
    });

    // Llamada inicial para obtener y mostrar los detalles de la compra
    this.verDetalleCompraCliente();
  }

  public verDetalleCompraCliente(){
    // Llamada al servicio para obtener los detalles de la compra según su código
    this.clienteService.verDetalleCompraCliente(this.codigoRegistro).subscribe({
      next: data => {
        this.detalle = data.respuesta; // Asignar los detalles de la compra obtenidos
      },
      error: error => {
        this.alerta = { mensaje: error.error.respuesta, tipo: "danger" }; // Mostrar mensaje de error en caso de fallo
        console.log(error);
      }
    });
  }

}
