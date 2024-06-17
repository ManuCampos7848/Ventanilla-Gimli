import { Component } from '@angular/core';
import { RegistroProductoDTO } from 'src/app/model/RegistroProductoDTO';
import { Alerta } from 'src/app/model/alerta';
import { VentanillaService } from 'src/app/servicios/ventanilla.service';

@Component({
  selector: 'app-crear-producto',
  templateUrl: './crear-producto.component.html',
  styleUrls: ['./crear-producto.component.css']
})
export class CrearProductoComponent {

  alerta!: Alerta; // Variable para mostrar alertas en caso de error

  categorias: string[]; // Arreglo para almacenar las categorías de productos
  subcategorias: string[]; // Arreglo para almacenar las subcategorías de productos
  subcategoriasAux: string[]; // Arreglo auxiliar para mostrar subcategorías según la categoría seleccionada

  nombresAlcoholes: string[]; // Arreglo para almacenar los nombres de los alcoholes disponibles

  registroProductoDTO: RegistroProductoDTO; // Objeto DTO para el registro de nuevos productos

  constructor(private ventanillaService: VentanillaService) {
    // Inicialización de variables y objetos en el constructor
    this.categorias = [];
    this.cargarCategorias();

    this.subcategorias = [];
    this.cargarSubcategorias();
    this.subcategoriasAux = [];

    this.nombresAlcoholes = [];

    this.registroProductoDTO = new RegistroProductoDTO();
  }

  public verificarPrecioCantidad(){
    // Verificar que la cantidad y el precio del producto sean mayores que 0
    if(this.registroProductoDTO.cantidad <= 0){
      this.alerta = {mensaje: "La cantidad de producto no puede ser menor o igual a 0", tipo: "danger"};
      return true;
    }else if(this.registroProductoDTO.precio <= 0){
      this.alerta = {mensaje: "El precio del producto no puede ser menor o igual a 0", tipo: "danger"};
      return true;
    }
    return false;
  }

  private cargarCategorias() {
    // Método para cargar las categorías de productos desde el servicio
    this.ventanillaService.listarCategorias().subscribe({
      next: data => {
        this.categorias = data.respuesta;
      },
      error: error => {
        console.log(error);
      }
    });
  }

  private cargarSubcategorias() {
    // Método para cargar las subcategorías de productos desde el servicio
    this.ventanillaService.listarSubcategorias().subscribe({
      next: data => {
        this.subcategorias = data.respuesta;
      },
      error: error => {
        console.log(error);
      }
    });
  }

  private cargarListaAlcoholes(categoria: string) {
    // Método para cargar los nombres de los alcoholes según la categoría seleccionada
    this.ventanillaService.listarNombresAlcoholes(categoria).subscribe({
      next: data => {
        this.nombresAlcoholes = data.respuesta;
      },
      error: error => {
        console.log(error);
      }
    });
  }

  onCategoriaChange() {
    // Método llamado al cambiar la categoría seleccionada
    switch (this.registroProductoDTO.categoria) {
      case 'ALCOHOL':
        // Si la categoría seleccionada es 'ALCOHOL', mostrar solo las subcategorías de alcohol y cargar los nombres de los alcoholes
        this.subcategoriasAux = this.subcategorias.slice(0, 6);
        this.cargarListaAlcoholes(this.registroProductoDTO.categoria);
        break;
      case 'DULCES':
        // Si la categoría seleccionada es 'DULCES', mostrar las subcategorías a partir de 'FRITURA'
        const indiceFritura = this.categorias.indexOf('DULCES');
        if (indiceFritura !== -1 && indiceFritura + 5 <= 11) {
          this.subcategoriasAux = this.subcategorias.slice(indiceFritura + 5, 17);
        } else {
          this.subcategoriasAux = this.subcategorias;
        }
        break;
      default:
        // Para otras categorías, mostrar todas las subcategorías disponibles
        const indiceGaseosa = this.categorias.indexOf('GASEOSA');
        this.subcategoriasAux = this.subcategorias.slice(indiceGaseosa + 15);
        break;
    }
  }

  public registrar() {
    // Método para registrar el nuevo producto
    if(!this.verificarPrecioCantidad()){ // Verificar que la cantidad y el precio sean válidos
      this.ventanillaService.registrarProducto(this.registroProductoDTO).subscribe({
        next: data => {
          alert("Registro Exitoso"); // Mostrar alerta de éxito
          location.reload(); // Recargar la página para reflejar los cambios
        },
        error: (error: { error: { respuesta: any; }; }) => {
          this.alerta = { mensaje: error.error.respuesta, tipo: "danger" }; // Mostrar mensaje de error en caso de falla
          console.log(error);
        }
      });
    }
  }

  camposVacios(): boolean {
    // Método para verificar si hay campos vacíos en el formulario de registro
    return !this.registroProductoDTO.nombre || !this.registroProductoDTO.cantidad || !this.registroProductoDTO.categoria || !this.registroProductoDTO.descripcion || !this.registroProductoDTO.precio ||
      !this.registroProductoDTO.proveedor || !this.registroProductoDTO.subcategoria;
  }

}

