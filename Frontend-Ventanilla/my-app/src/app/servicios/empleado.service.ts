import { Injectable } from '@angular/core';
import { RegistroVentaEmpleadoDTO } from '../model/RegistroVentaEmpleadoDTO';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { MensajeDTO } from '../model/mensaje-dto';
import { AgregarProductoDTO } from '../model/AgregarProductoDTO';

@Injectable({
  providedIn: 'root'
})
export class EmpleadoService {

  /**
   * URL donde la API se comunica con el servidor
   */
  private userUrl = "http://localhost:8080/api/empleados";

  constructor(private http: HttpClient) { }

  /**
   * Metodos HTTP (GET, POST, PUT)
   */
  public agregarProducto(producto: AgregarProductoDTO): Observable<MensajeDTO> {
    return this.http.post<MensajeDTO>(`${this.userUrl}/agregar-producto`, producto);
  }
  public listarRegistrosAgreacionProductos(): Observable<MensajeDTO> {
    return this.http.get<MensajeDTO>(`${this.userUrl}/listar-registros-agregacion-productos`);
  }
  public verDetalleRegistro(codigoRegistro: number): Observable<MensajeDTO> {
    return this.http.get<MensajeDTO>(`${this.userUrl}/detalle-registro/${codigoRegistro}`);
  }
  public verDetalleVentaEmpleado(codigoVenta: number): Observable<MensajeDTO> {
    return this.http.get<MensajeDTO>(`${this.userUrl}/detalle-venta-empleado/${codigoVenta}`);
  }
  public listaVentasEmpleados(): Observable<MensajeDTO> {
    return this.http.get<MensajeDTO>(`${this.userUrl}/listar-ventas-empleados`);
  }

}
