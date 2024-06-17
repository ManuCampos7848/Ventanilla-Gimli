import { Component } from '@angular/core';
import { RegistroCompraClienteDTO } from 'src/app/model/RegistroCompraClienteDTO';
import { RegistroProductoDTO } from 'src/app/model/RegistroProductoDTO';
import { RegistroVentaEmpleadoDTO } from 'src/app/model/RegistroVentaEmpleadoDTO';
import { Alerta } from 'src/app/model/alerta';
import { ClienteService } from 'src/app/servicios/cliente.service';
import { EmpleadoService } from 'src/app/servicios/empleado.service';
import { TokenService } from 'src/app/servicios/token.service';
import { VentanillaService } from 'src/app/servicios/ventanilla.service';

@Component({
  selector: 'app-registrar-compra-cliente',
  templateUrl: './registrar-compra-cliente.component.html',
  styleUrls: ['./registrar-compra-cliente.component.css']
})
export class RegistrarCompraClienteComponent {

  alerta!: Alerta;  // Propiedad para almacenar mensajes de alerta

  categorias: string[];  // Arreglo para almacenar las categorías de productos

  subcategorias: string[];  // Arreglo para almacenar las subcategorías de productos
  subcategoriasAux: string[];  // Arreglo auxiliar para subcategorías

  nombresAlcoholes: string[];  // Arreglo para almacenar nombres de alcoholes
  nombresDulces: string[];  // Arreglo para almacenar nombres de dulces
  nombresGaseosas: string[];  // Arreglo para almacenar nombres de gaseosas

  registroProductoDTO: RegistroProductoDTO;  // DTO para los datos del producto a registrar
  registroCompraClienteDTO: RegistroCompraClienteDTO;  // DTO para los datos de la compra del cliente

  alcoholSeleccionado: string = '';  // Variable para almacenar el alcohol seleccionado
  dulceSeleccionado: string = '';  // Variable para almacenar el dulce seleccionado
  gaseosaSeleccionada: string = '';  // Variable para almacenar la gaseosa seleccionada

  tipoCliente: string = '';  // Variable para almacenar el tipo de cliente
  correoCliente: string = '';  // Variable para almacenar el correo del cliente

  constructor(
    private ventanillaService: VentanillaService,  // Servicio para operaciones relacionadas con la ventanilla
    private clienteService: ClienteService,  // Servicio para operaciones relacionadas con clientes
    private tokenService: TokenService  // Servicio para manejar tokens de autenticación (no utilizado en este código)
  ) {
    // Inicialización de arreglos y objetos
    this.categorias = [];
    this.cargarCategorias();

    this.subcategorias = [];
    this.cargarSubcategorias();
    this.subcategoriasAux = [];

    this.nombresAlcoholes = []
    this.nombresDulces = []
    this.nombresGaseosas = []

    this.registroProductoDTO = new RegistroProductoDTO();
    this.registroCompraClienteDTO = new RegistroCompraClienteDTO();
  }

  // Método para cargar las categorías desde el servicio
  private cargarCategorias() {
    this.ventanillaService.listarCategorias().subscribe({
      next: data => {
        this.categorias = data.respuesta;  // Asigna las categorías obtenidas del servicio
      },
      error: error => {
        console.log(error);
      }
    });
  }

  // Método para cargar las subcategorías desde el servicio
  private cargarSubcategorias() {
    this.ventanillaService.listarSubcategorias().subscribe({
      next: data => {
        this.subcategorias = data.respuesta;  // Asigna las subcategorías obtenidas del servicio
      },
      error: error => {
        console.log(error);
      }
    });
  }

  // Método llamado cuando cambia la categoría seleccionada en el formulario
  onCategoriaChange() {
    switch (this.registroProductoDTO.categoria) {
      case 'ALCOHOL':
        this.cargarListaAlcoholes(this.registroProductoDTO.categoria);
        break;
      case 'DULCES':
        this.cargarListaDulces(this.registroProductoDTO.categoria);
        break;
      default:
        this.cargarListaGaseosas(this.registroProductoDTO.categoria);
        break;
    }
  }

  // Método para cargar la lista de alcoholes desde el servicio
  private cargarListaAlcoholes(categoria: string) {
    this.ventanillaService.listarNombresAlcoholes(categoria).subscribe({
      next: data => {
        this.nombresAlcoholes = data.respuesta;  // Asigna los nombres de alcoholes obtenidos del servicio
      },
      error: error => {
        console.log(error);
      }
    });
  }

  // Método para cargar la lista de dulces desde el servicio
  private cargarListaDulces(categoria: string) {
    this.ventanillaService.listarNombresDulces(categoria).subscribe({
      next: data => {
        this.nombresDulces = data.respuesta;  // Asigna los nombres de dulces obtenidos del servicio
      },
      error: error => {
        console.log(error);
      }
    });
  }

  // Método para cargar la lista de gaseosas desde el servicio
  private cargarListaGaseosas(categoria: string) {
    this.ventanillaService.listarNombresGaseosas(categoria).subscribe({
      next: data => {
        this.nombresGaseosas = data.respuesta;  // Asigna los nombres de gaseosas obtenidos del servicio
      },
      error: error => {
        console.log(error);
      }
    });
  }

  // Método principal para registrar la compra del cliente
  public registrar() {
    const fechaActual: string = new Date().toISOString().split('T')[0];
    const horaActual: string = new Date().toLocaleTimeString('en-US', { hour12: false });

    // Asigna el código del cliente actual obtenido del token
    this.registroCompraClienteDTO.codigoCliente = this.tokenService.getCodigo();

    // Asigna el nombre del producto según la categoría seleccionada
    switch (this.registroProductoDTO.categoria) {
      case 'ALCOHOL':
        this.registroCompraClienteDTO.nombreProducto = this.alcoholSeleccionado;
        break;
      case 'DULCES':
        this.registroCompraClienteDTO.nombreProducto = this.dulceSeleccionado;
        break;
      default:
        this.registroCompraClienteDTO.nombreProducto = this.gaseosaSeleccionada;
        break;
    }

    // Llama al servicio para registrar la compra del cliente
    this.clienteService.registrarCompraCliente(this.registroCompraClienteDTO).subscribe({
      next: data => {
        this.alerta = { tipo: "success", mensaje: "Compra registrada con éxito" };
        console.log(data);
        // Reinicia los datos después de una compra exitosa
        this.registroCompraClienteDTO = {
          cantidad: 0,
          nombreProducto: "",
          direccion: "",
          dinero: 0,
          codigoCliente: this.tokenService.getCodigo()
        };
        this.registroProductoDTO = {
          categoria: "",
          descripcion: "",
          cantidad: 0,
          subcategoria: "",
          nombre: "",
          precio: 0,
          proveedor: ""
        };
      },
      error: (error: { error: { respuesta: any; }; }) => {
        this.alerta = { mensaje: error.error.respuesta, tipo: "danger" };
        console.log(error);
      }
    });
  }

  // Método para verificar si hay campos vacíos antes de registrar la compra
  camposVacios(): boolean {
    if (!this.registroCompraClienteDTO.cantidad || !this.registroCompraClienteDTO.dinero) {
      return true;  // Devuelve true si algún campo necesario está vacío
    }

    switch (this.registroProductoDTO.categoria) {
      case 'ALCOHOL':
        return !this.alcoholSeleccionado;  // Devuelve true si no se ha seleccionado un alcohol
      case 'DULCES':
        return !this.dulceSeleccionado;  // Devuelve true si no se ha seleccionado un dulce
      case 'GASEOSA':
        return !this.gaseosaSeleccionada;  // Devuelve true si no se ha seleccionado una gaseosa
      default:
        return true;
    }
  }

  // Método para obtener el código del cliente por su correo electrónico
  public obtenerCodigoClientePorCorreo() {
    // Este método se encuentra actualmente comentado en el código proporcionado
    // y no está siendo utilizado en la lógica actual del componente.
  }

}
