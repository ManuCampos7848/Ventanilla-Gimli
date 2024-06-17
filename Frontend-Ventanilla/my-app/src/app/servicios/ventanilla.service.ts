import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { MensajeDTO } from '../model/mensaje-dto';
import { RegistroProductoDTO } from '../model/RegistroProductoDTO';
import { RegistrarVentaEmpleadoComponent } from '../pagina/registrar-venta-empleado/registrar-venta-empleado.component';
import { RegistroVentaEmpleadoDTO } from '../model/RegistroVentaEmpleadoDTO';

@Injectable({
  providedIn: 'root'
})
export class VentanillaService {

  /**
   * URL donde la API se comunica con el servidor
   */
  private ventanillaURL = "http://localhost:8080/api/ventanilla"

  constructor(private http: HttpClient) { }

  /**
   * Metodos HTTP (GET, POST)
   */
  public listarCategorias(): Observable<MensajeDTO> {
    return this.http.get<MensajeDTO>(`${this.ventanillaURL}/lista-categorias`);
  }
  public listarSubcategorias(): Observable<MensajeDTO> {
    return this.http.get<MensajeDTO>(`${this.ventanillaURL}/lista-subcategorias`);
  }
  public registrarProducto(producto: RegistroProductoDTO): Observable<MensajeDTO> {
    return this.http.post<MensajeDTO>(`${this.ventanillaURL}/registrar-producto`, producto);
  }
  public listarNombresAlcoholes(categoria: string): Observable<MensajeDTO> {
    return this.http.get<MensajeDTO>(`${this.ventanillaURL}/lista-nombres-alcoholes/${categoria}`);
  }
  public listarNombresDulces(categoria: string): Observable<MensajeDTO> {
    return this.http.get<MensajeDTO>(`${this.ventanillaURL}/lista-nombres-dulces/${categoria}`);
  }
  public listarNombresGaseosas(categoria: string): Observable<MensajeDTO> {
    return this.http.get<MensajeDTO>(`${this.ventanillaURL}/lista-nombres-gaseosas/${categoria}`);
  }
  /**
   * Cantidad mayor a 0
   * @param categoria 
   * @returns 
   */
  public listarNombresAlcoholesMayor1(categoria: string): Observable<MensajeDTO> {
    return this.http.get<MensajeDTO>(`${this.ventanillaURL}/lista-nombres-alcoholes-cantidad-mayor-1/${categoria}`);
  }
  public listarNombresDulcesMayor1(categoria: string): Observable<MensajeDTO> {
    return this.http.get<MensajeDTO>(`${this.ventanillaURL}/lista-nombres-dulces-cantidad-mayor-1/${categoria}`);
  }
  public listarNombresGaseosasMayor1(categoria: string): Observable<MensajeDTO> {
    return this.http.get<MensajeDTO>(`${this.ventanillaURL}/lista-nombres-gaseosas-cantidad-mayor-1/${categoria}`);
  }
  public registrarVentaEmpleado(venta: RegistroVentaEmpleadoDTO): Observable<MensajeDTO> {
    return this.http.post<MensajeDTO>(`${this.ventanillaURL}/registrar-venta`, venta);
  }
  public listarProductos(): Observable<MensajeDTO> {
    return this.http.get<MensajeDTO>(`${this.ventanillaURL}/listar-productos`);
  }
  public listarProductosConCantidad0(): Observable<MensajeDTO> {
    return this.http.get<MensajeDTO>(`${this.ventanillaURL}/listar-productos-cantidad-0`);
  }
  public verDetalleProducto(codigoProducto: number): Observable<MensajeDTO> {
    return this.http.get<MensajeDTO>(`${this.ventanillaURL}/detalle-producto/${codigoProducto}`);
  }
  public filtrarProductosPorNombre(nombreProducto: string): Observable<MensajeDTO> {
    return this.http.get<MensajeDTO>(`${this.ventanillaURL}/filtar-productos-nombre/${nombreProducto}`);
  }
  public obtenerClientePorCorreo(correo: string): Observable<MensajeDTO> {
    return this.http.get<MensajeDTO>(`${this.ventanillaURL}/obtener-codigo-cliente/${correo}`);
  }

}
