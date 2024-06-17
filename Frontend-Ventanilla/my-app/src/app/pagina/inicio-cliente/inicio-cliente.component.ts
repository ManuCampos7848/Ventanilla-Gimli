import { Component } from '@angular/core';
import { FiltroBusquedaDTO } from 'src/app/model/FiltroBusquedaDTO';
import { ItemProductoDTO } from 'src/app/model/ItemProductoDTO';
import { Alerta } from 'src/app/model/alerta';
import { ClienteService } from 'src/app/servicios/cliente.service';
import { VentanillaService } from 'src/app/servicios/ventanilla.service';

@Component({
  selector: 'app-inicio-cliente',
  templateUrl: './inicio-cliente.component.html',
  styleUrls: ['./inicio-cliente.component.css']
})
export class InicioClienteComponent {

  itemProductoDTO: ItemProductoDTO[];  // Arreglo de productos obtenidos del servicio
  productosPorNombre: ItemProductoDTO;  // Producto filtrado por nombre

  filtroBusquedaDTO: FiltroBusquedaDTO;  // DTO para almacenar el nombre del producto a buscar

  alerta!: Alerta;  // Objeto para mostrar alertas en la interfaz

  totalProductos: number = 0;  // Total de productos obtenidos
  page: number = 1;  // Página actual de la paginación
  itemsPerPage: number = 4;  // Número de productos por página

  constructor(private ventanilla: VentanillaService) {
    this.itemProductoDTO = [];  // Inicializar arreglo de productos vacío
    this.listarProductos();  // Llamar al método para listar productos al inicializar el componente

    this.filtroBusquedaDTO = new FiltroBusquedaDTO();  // Inicializar DTO para filtros de búsqueda
    this.productosPorNombre = new ItemProductoDTO();  // Inicializar objeto para productos filtrados por nombre
  }

  // Método para calcular el total de páginas según la paginación y el número de productos por página
  totalPages(): number[] {
    const totalPages = Math.ceil(this.totalProductos / this.itemsPerPage);
    return Array(totalPages).fill(0).map((x, i) => i);
  }

  // Método para listar todos los productos
  public listarProductos() {
    this.ventanilla.listarProductos().subscribe({
      next: data => {
        this.itemProductoDTO = data.respuesta;  // Almacenar la lista de productos obtenida del servicio
        this.totalProductos = this.itemProductoDTO.length;  // Actualizar el total de productos
      },
      error: error => {
        console.log(error);  // Manejar errores en caso de fallo al obtener productos
      }
    });
  }

  // Método para filtrar productos por nombre
  public filtrarProductosPorNombre() {
    let nombreProducto = this.filtroBusquedaDTO.nombreProducto;  // Obtener el nombre de producto del DTO de filtro

    if (nombreProducto == "") {
      this.alerta = { tipo: "danger", mensaje: "Ingrese el nombre de un producto" };  // Validar nombre de producto vacío
    } else {
      this.ventanilla.filtrarProductosPorNombre(nombreProducto).subscribe({
        next: data => {
          this.productosPorNombre = data.respuesta;  // Almacenar el producto filtrado obtenido del servicio
        },
        error: error => {
          this.alerta = { tipo: "danger", mensaje: error.error.respuesta };  // Manejar errores al filtrar productos por nombre
          console.log(error);
        }
      });
    }
  }
}