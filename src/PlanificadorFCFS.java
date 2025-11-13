import java.util.List;

public class PlanificadorFCFS implements Planificador {

    @Override
    public String getNombre() {
        return "FCFS";
    }

    @Override
    public void ejecutar(List<TareaResolucion> tareas) {
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
