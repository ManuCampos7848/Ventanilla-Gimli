import { Component } from '@angular/core';
import { ItemComprasClienteDTO } from 'src/app/model/ItemComprasClienteDTO'; // Importación del modelo de datos para las compras del cliente
import { ClienteService } from 'src/app/servicios/cliente.service'; // Importación del servicio de cliente
import { TokenService } from 'src/app/servicios/token.service'; // Importación del servicio de token

@Component({
  selector: 'app-compras-realizadas-cliente',
  templateUrl: './compras-realizadas-cliente.component.html',
  styleUrls: ['./compras-realizadas-cliente.component.css']
})
export class ComprasRealizadasClienteComponent {

  itemCompra: ItemComprasClienteDTO[]; // Arreglo para almacenar los items de compra del cliente

  totalCompras: number = 0; // Variable para almacenar el total de compras
  page: number = 1; // Página actual de la paginación
  itemsPerPage: number = 4; // Número de ítems por página

  constructor(private cliente: ClienteService, // Inyección del servicio ClienteService
    private token: TokenService // Inyección del servicio TokenService
  ) {
    // Inicialización del arreglo de items de compra y carga inicial de las compras realizadas
    this.itemCompra = [];
    this.listarComprasRealizadas();
  }

  public listarComprasRealizadas() {
    // Método para listar las compras realizadas por el cliente
    this.cliente.comprasRealizadas(this.token.getCodigo()).subscribe({
      next: data => {
        this.itemCompra = data.respuesta; // Asignación de los datos de compras obtenidos del servicio
        this.totalCompras = this.itemCompra.length; // Cálculo del total de compras
        console.log(this.itemCompra); // Logging de los datos de compras (opcional)
      },
      error: error => {
        console.log(error); // Manejo de errores en caso de fallo al obtener las compras
      }
    });
  }

  totalPages(): number[] {
    // Método para calcular el número total de páginas
    const totalPages = Math.ceil(this.totalCompras / this.itemsPerPage); // Cálculo del número total de páginas
    return Array(totalPages).fill(0).map((x, i) => i); // Creación de un arreglo con el número de páginas
  }
}
