import java.util.List;

public interface Planificador {
    String getNombre();
    void ejecutar(List<TareaResolucion> tareas);
}
