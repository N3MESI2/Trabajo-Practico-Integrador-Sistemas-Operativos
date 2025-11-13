import java.util.Comparator;
import java.util.List;

public class PlanificadorSJF implements Planificador {

    @Override
    public String getNombre() {
        return "SJF";
    }

    @Override
    public void ejecutar(List<TareaResolucion> tareas) {
        tareas.sort(Comparator.comparingLong(TareaResolucion::getTiempoServicioEstimadoMs));

        for (TareaResolucion t : tareas) {
            long inicio = System.currentTimeMillis();
            t.setTiempoInicio(inicio);
            t.setTiempoEspera(inicio - t.getTiempoLlegada());

            t.ejecutarTrabajoRealCompleto();

            long fin = System.currentTimeMillis();
            t.setTiempoFin(fin);
        }
    }
}
