import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { LoginDTO } from '../model/login-dto';
import { MensajeDTO } from '../model/mensaje-dto';
import { RegistroClienteDTO } from '../model/RegistroClienteDTO';
import { RegistroCompraClienteDTO } from '../model/RegistroCompraClienteDTO';
import { ModificarClienteDTO } from '../model/ModificarClienteDTO';

@Injectable({
    providedIn: 'root'
})
export class ClienteService {

    /**
    * URL donde la API se comunica con el servidor
    */
    private userUrl = "http://localhost:8080/api/clientes";

    constructor(private http: HttpClient) { }

    /**
   * Metodos HTTP (GET, POST, PUT, DELETE)
   */
    public comprasRealizadas(codigoCliente: number): Observable<MensajeDTO> {
        return this.http.get<MensajeDTO>(`${this.userUrl}/lista-compras-cliente/${codigoCliente}`);
    }
    public registrarCompraCliente(compra: RegistroCompraClienteDTO): Observable<MensajeDTO> {
        return this.http.post<MensajeDTO>(`${this.userUrl}/registrar-compra`, compra);
    }
    public eliminarCuenta(codigo: number): Observable<MensajeDTO> {
        return this.http.delete<MensajeDTO>(`${this.userUrl}/eliminar-cuenta/${codigo}`);
    }
    public editarPerfil(clienteDTO: ModificarClienteDTO): Observable<MensajeDTO> {
        return this.http.put<MensajeDTO>(`${this.userUrl}/editar-perfil`, clienteDTO);
    }
    public verDetalleProducto(codigoProducto: number): Observable<MensajeDTO> {
        return this.http.get<MensajeDTO>(`${this.userUrl}/detalle-producto/${codigoProducto}`);
    }
    public verDetalleCompraCliente(codigoCompra: number): Observable<MensajeDTO> {
        return this.http.get<MensajeDTO>(`${this.userUrl}/detalle-compra-cliente/${codigoCompra}`);
    }
    public verDetalleDatosCliente(codigoCliente: number): Observable<MensajeDTO> {
        return this.http.get<MensajeDTO>(`${this.userUrl}/ver-detalles-cliente/${codigoCliente}`);
    }

}