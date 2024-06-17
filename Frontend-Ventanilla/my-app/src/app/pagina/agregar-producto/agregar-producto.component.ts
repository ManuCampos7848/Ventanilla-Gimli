import { Component } from '@angular/core';
import { AgregarProductoDTO } from 'src/app/model/AgregarProductoDTO'; // Importación del modelo de datos para agregar productos
import { Alerta } from 'src/app/model/alerta'; // Importación del modelo de datos para alertas
import { AdministradorService } from 'src/app/servicios/administrador.service'; // Importación del servicio de administrador
import { EmpleadoService } from 'src/app/servicios/empleado.service'; // Importación del servicio de empleado
import { TokenService } from 'src/app/servicios/token.service'; // Importación del servicio de token
import { VentanillaService } from 'src/app/servicios/ventanilla.service'; // Importación del servicio de ventanilla

@Component({
  selector: 'app-agregar-producto',
  templateUrl: './agregar-producto.component.html',
  styleUrls: ['./agregar-producto.component.css']
})
export class AgregarProductoComponent {

  agregarProducto: AgregarProductoDTO; // Objeto para almacenar los datos del producto a agregar

  alerta!: Alerta; // Variable para manejar las alertas

  categorias: string[]; // Arreglo para almacenar las categorías disponibles

  nombresAlcoholes: string[]; // Arreglo para almacenar los nombres de alcoholes disponibles
  nombresDulces: string[]; // Arreglo para almacenar los nombres de dulces disponibles
  nombresGaseosas: string[]; // Arreglo para almacenar los nombres de gaseosas disponibles

  alcoholSeleccionado: string = ''; // Variable para almacenar el alcohol seleccionado
  dulceSeleccionado: string = ''; // Variable para almacenar el dulce seleccionado
  gaseosaSeleccionada: string = ''; // Variable para almacenar la gaseosa seleccionada

  constructor(private ventanillaService: VentanillaService, // Inyección del servicio de ventanilla
    private empleadoService: EmpleadoService, // Inyección del servicio de empleado
    private adminService: AdministradorService, // Inyección del servicio de administrador
    private tokenService: TokenService) { // Inyección del servicio de token

    this.agregarProducto = new AgregarProductoDTO(); // Inicialización del objeto agregarProducto como nuevo

    this.categorias = []; // Inicialización del arreglo de categorías
    this.cargarCategorias(); // Llamada al método para cargar las categorías disponibles

    this.nombresAlcoholes = []; // Inicialización del arreglo de nombres de alcoholes
    this.nombresDulces = []; // Inicialización del arreglo de nombres de dulces
    this.nombresGaseosas = []; // Inicialización del arreglo de nombres de gaseosas
  }

  private cargarCategorias() {
    // Método para cargar las categorías disponibles utilizando el servicio de ventanilla
    this.ventanillaService.listarCategorias().subscribe({
      next: data => {
        this.categorias = data.respuesta; // Asignación de las categorías obtenidas del servicio
      },
      error: error => {
        console.log(error); // Manejo de errores en caso de fallo al obtener las categorías
      }
    });
  }

  public cargarListaAlcoholes() {
    // Método para cargar los nombres de alcoholes disponibles según la categoría seleccionada
    this.ventanillaService.listarNombresAlcoholesMayor1(this.agregarProducto.categoria).subscribe({
      next: data => {
        this.nombresAlcoholes = data.respuesta; // Asignación de los nombres de alcoholes obtenidos del servicio
      },
      error: error => {
        console.log(error); // Manejo de errores en caso de fallo al obtener los nombres de alcoholes
      }
    });
  }

  public cargarListaDulces() {
    // Método para cargar los nombres de dulces disponibles según la categoría seleccionada
    this.ventanillaService.listarNombresDulcesMayor1(this.agregarProducto.categoria).subscribe({
      next: data => {
        this.nombresDulces = data.respuesta; // Asignación de los nombres de dulces obtenidos del servicio
      },
      error: error => {
        console.log(error); // Manejo de errores en caso de fallo al obtener los nombres de dulces
      }
    });
  }

  public cargarListaGaseosas() {
    // Método para cargar los nombres de gaseosas disponibles según la categoría seleccionada
    this.ventanillaService.listarNombresGaseosasMayor1(this.agregarProducto.categoria).subscribe({
      next: data => {
        this.nombresGaseosas = data.respuesta; // Asignación de los nombres de gaseosas obtenidos del servicio
      },
      error: error => {
        console.log(error); // Manejo de errores en caso de fallo al obtener los nombres de gaseosas
      }
    });
  }

  onCategoriaChange() {
    // Método llamado cuando cambia la categoría seleccionada
    switch (this.agregarProducto.categoria) {
      case 'ALCOHOL':
        this.cargarListaAlcoholes(); // Cargar los nombres de alcoholes si la categoría es ALCOHOL
        break;
      case 'DULCES':
        this.cargarListaDulces(); // Cargar los nombres de dulces si la categoría es DULCES
        break;
      case 'GASEOSA':
        this.cargarListaGaseosas(); // Cargar los nombres de gaseosas si la categoría es GASEOSA
        break;
    }
  }

  public registrar() {
    // Método para registrar el producto

    let codigo = this.tokenService.getCodigo(); // Obtener el código del empleado o administrador actual
    this.agregarProducto.codigoEmpleado = codigo; // Asignar el código del empleado o administrador al producto

    if (this.agregarProducto.cantidad <= 0) {
      // Validación: La cantidad del producto no puede ser menor o igual que cero
      this.alerta = { mensaje: "La cantidad del producto no puede ser menor o igual que cero", tipo: "danger" };
    } else if (this.agregarProducto.categoria == "") {
      // Validación: Seleccionar una categoría es obligatorio
      this.alerta = { mensaje: "Seleccione una categoría", tipo: "danger" }
    } else if (this.agregarProducto.cantidad >= 100) {
      // Validación: La cantidad del producto no puede ser mayor o igual que 100
      this.alerta = { mensaje: "La cantidad del producto no puede ser mayor o igual que 100", tipo: "danger" };
    } else {

      switch (this.agregarProducto.categoria) {
        case 'ALCOHOL':
          this.agregarProducto.nombre = this.alcoholSeleccionado; // Asignar el nombre del alcohol seleccionado
          break;
        case 'DULCES':
          this.agregarProducto.nombre = this.dulceSeleccionado; // Asignar el nombre del dulce seleccionado
          break;
        default:
          this.agregarProducto.nombre = this.gaseosaSeleccionada; // Asignar el nombre de la gaseosa seleccionada
          break;
      }

      if (this.tokenService.getRole() == 'empleado') {
        // Si el usuario es un empleado, llamar al servicio de empleado para agregar el producto
        this.empleadoService.agregarProducto(this.agregarProducto).subscribe({
          next: data => {
            this.alerta = { tipo: "success", mensaje: "Producto agregado con éxito" }; // Mostrar mensaje de éxito
            console.log(data); // Logging de la respuesta del servicio (opcional)
          },
          error: (error: { error: { respuesta: any; }; }) => {
            this.alerta = { mensaje: error.error.respuesta, tipo: "danger" }; // Mostrar mensaje de error
            console.log(error); // Logging del error (opcional)
          }
        });
      } else if (this.tokenService.getRole() == 'admin') {
        // Si el usuario es un administrador, llamar al servicio de administrador para agregar el producto
        this.adminService.agregarProducto(this.agregarProducto).subscribe({
          next: data => {
            this.alerta = { tipo: "success", mensaje: "Producto agregado con éxito" }; // Mostrar mensaje de éxito
            console.log(data); // Logging de la respuesta del servicio (opcional)
          },
          error: (error: { error: { respuesta: any; }; }) => {
            this.alerta = { mensaje: error.error.respuesta, tipo: "danger" }; // Mostrar mensaje de error
            console.log(error); // Logging del error (opcional)
          }
        });
      }
    }
  }

  camposVacios(): boolean {
    // Método para verificar si existen campos obligatorios vacíos
    if (!this.agregarProducto.cantidad || !this.agregarProducto.categoria) {
      return true; // Si la cantidad o la categoría están vacías, retornar true (indicando que hay campos vacíos)
    }
  
    switch (this.agregarProducto.categoria) {
      case 'ALCOHOL':
        return !this.alcoholSeleccionado; // Verificar si no se ha seleccionado un alcohol en caso de la categoría ALCOHOL
      case 'DULCES':
        return !this.dulceSeleccionado; // Verificar si no se ha seleccionado un dulce en caso de la categoría DULCES
      case 'GASEOSA':
        return !this.gaseosaSeleccionada; // Verificar si no se ha seleccionado una gaseosa en caso de la categoría GASEOSA
      default:
        return true; // Si la categoría no es ninguna de las anteriores, retornar true (indicando campos vacíos)
    }
  }
}  