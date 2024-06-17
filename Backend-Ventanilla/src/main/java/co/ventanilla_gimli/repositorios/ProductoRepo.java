package co.ventanilla_gimli.repositorios;

import co.ventanilla_gimli.model.Categoria;
import co.ventanilla_gimli.model.Producto;
import co.ventanilla_gimli.servicios.interfaces.ProductoServicio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductoRepo extends JpaRepository<Producto, Integer> {

    Producto findByNombresAlcohol(String nombre);
    Producto findByNombresDulces(String nombre);
    Producto findByNombresGaseosas(String nombre);
    List<String> findAllByNombresAlcohol(Categoria categoria);

    @Query("SELECT p FROM Producto p")
    List<Producto> findAllIgnoreCantidad();

    @Query("SELECT p.nombresAlcohol FROM Producto p WHERE p.categoria = :categoria AND p.cantidad >= 1")
    List<String> findAllNombresAlcoholByCategoria(@Param("categoria") Categoria categoria);

    @Query("SELECT p.nombresDulces FROM Producto p WHERE p.categoria = :categoria AND p.cantidad >= 1")
    List<String> findAllNombresDulcesByCategoria(@Param("categoria") Categoria categoria);

    @Query("SELECT p.nombresGaseosas FROM Producto p WHERE p.categoria = :categoria AND p.cantidad >= 1")
    List<String> findAllNombresGaseosasByCategoria(@Param("categoria") Categoria categoria);


    @Query("SELECT p.nombresDulces FROM Producto p WHERE p.categoria = :categoria")
    List<String> findAllNombresDulcesByCategoriaIgnoreCantidad(@Param("categoria") Categoria categoria);

    @Query("SELECT p.nombresGaseosas FROM Producto p WHERE p.categoria = :categoria")
    List<String> findAllNombresGaseosasByCategoriaIgnoreCantidad(@Param("categoria") Categoria categoria);

    @Query("SELECT p.nombresAlcohol FROM Producto p WHERE p.categoria = :categoria")
    List<String> findAllNombresAlcoholByCategoriaIgnoreCantidad(@Param("categoria") Categoria categoria);


    List<Producto> findByCategoria(Categoria alcohol);
}
