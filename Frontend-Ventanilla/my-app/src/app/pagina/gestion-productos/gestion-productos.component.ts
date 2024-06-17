import { Component } from '@angular/core';
import { ItemProductoDTO } from 'src/app/model/ItemProductoDTO';  // Importar DTOs necesarios
import { ItemRegistroProductoDTO } from 'src/app/model/ItemRegistroProductoDTO';
import { ItemVentaEmpleadoDTO } from 'src/app/model/ItemVentaEmpleadoDTO';
import { AdministradorService } from 'src/app/servicios/administrador.service';  // Importar servicios necesarios
import { EmpleadoService } from 'src/app/servicios/empleado.service';
import { TokenService } from 'src/app/servicios/token.service';
import { VentanillaService } from 'src/app/servicios/ventanilla.service';

@Component({
  selector: 'app-gestion-productos',
  templateUrl: './gestion-productos.component.html',
  styleUrls: ['./gestion-productos.component.css']
})
export class GestionProductosComponent {
  registroProductos: ItemRegistroProductoDTO[];  // Arreglo para almacenar registros de productos
  ventasEmpleados: ItemVentaEmpleadoDTO[];  // Arreglo para almacenar ventas de empleados
  itemProductoDTO: ItemProductoDTO[];  // Arreglo para almacenar productos

  // Paginación para productos
  pageProductos: number = 1;
  itemsPerPageProducto: number = 4;
  totalProductos: number = 0;

  // Paginación para ventas
  pageVentas: number = 1;
  itemsPerPageVentas: number = 4;
  totalVentas: number = 0;

  constructor(
    private empleadoService: EmpleadoService,
    private ventanilla: VentanillaService,
    private adminService: AdministradorService,
    private tokenService: TokenService
  ) {
    this.itemProductoDTO = [];  // Inicializar arreglo de productos vacío
    this.listarProductos();  // Llamar al método para listar productos al inicializar el componente

    this.registroProductos = [];  // Inicializar arreglo de registros de productos vacío
    this.obtenerListaRegistros();  // Llamar al método para obtener registros de productos al inicializar

    this.ventasEmpleados = [];  // Inicializar arreglo de ventas de empleados vacío
    this.obtenerListaVentasEmpleados();  // Llamar al método para obtener ventas de empleados al inicializar
  }

  // Método para calcular el total de páginas según la paginación y el número de productos por página
  totalPagesProductos(): number[] {
    const totalPages = Math.ceil(this.totalProductos / this.itemsPerPageProducto);
    return Array(totalPages).fill(0).map((x, i) => i);
  }

  // Método para calcular el total de páginas según la paginación y el número de ventas por página
  totalPagesVentas(): number[] {
    const totalPages = Math.ceil(this.totalVentas / this.itemsPerPageVentas);
    return Array(totalPages).fill(0).map((x, i) => i);
  }

  // Método para obtener la lista de registros de productos según el rol del usuario
  public obtenerListaRegistros() {
    if (this.tokenService.getRole() == 'empleado') {
      this.empleadoService.listarRegistrosAgreacionProductos().subscribe({
        next: data => {
          this.registroProductos = data.respuesta;  // Almacenar la lista de registros de productos obtenida del servicio
          console.log(this.registroProductos);
        },
        error: error => {
          console.log(error);  // Manejar errores en caso de fallo al obtener registros de productos
        }
      });
    } else if (this.tokenService.getRole() == 'admin') {
      this.adminService.listarRegistrosAgreacionProductos().subscribe({
        next: data => {
          this.registroProductos = data.respuesta;  // Almacenar la lista de registros de productos obtenida del servicio
          console.log(this.registroProductos);
        },
        error: error => {
          console.log(error);  // Manejar errores en caso de fallo al obtener registros de productos
        }
      });
    }
  }

  // Método para obtener la lista de ventas de empleados según el rol del usuario
  public obtenerListaVentasEmpleados() {
    const sortVentasDescendente = (ventas: any[]) => {
      return ventas.sort((a, b) => b.codigoVenta - a.codigoVenta);
    };
  
    if (this.tokenService.getRole() == 'empleado') {
      this.empleadoService.listaVentasEmpleados().subscribe({
        next: data => {
          this.ventasEmpleados = sortVentasDescendente(data.respuesta); // Ordenar las ventas en orden descendente
          this.totalVentas = this.ventasEmpleados.length;  // Actualizar el total de ventas
          console.log(this.ventasEmpleados);
        },
        error: error => {
          console.log(error);  // Manejar errores en caso de fallo al obtener ventas de empleados
        }
      });
    } else if (this.tokenService.getRole() == 'admin') {
      this.adminService.listaVentasEmpleados().subscribe({
        next: data => {
          this.ventasEmpleados = sortVentasDescendente(data.respuesta); // Ordenar las ventas en orden descendente
          this.totalVentas = this.ventasEmpleados.length;  // Actualizar el total de ventas
        },
        error: error => {
          console.log(error);  // Manejar errores en caso de fallo al obtener ventas de empleados
        }
      });
    }
  }

  // Método para listar todos los productos
  public listarProductos() {
    this.ventanilla.listarProductosConCantidad0().subscribe({
      next: data => {
        this.itemProductoDTO = data.respuesta;  // Almacenar la lista de productos obtenida del servicio
        this.totalProductos = this.itemProductoDTO.length;  // Actualizar el total de productos
        console.log(this.itemProductoDTO);
      },
      error: error => {
        console.log(error);  // Manejar errores en caso de fallo al obtener productos
      }
    });
  }
}
