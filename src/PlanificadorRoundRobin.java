import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public class PlanificadorRoundRobin implements Planificador {

    private final long quantumMs;

    public PlanificadorRoundRobin(long quantumMs) {
        this.quantumMs = quantumMs;
    }

    @Override
    public String getNombre() {
        return "Round Robin";
    }

    @Override
    public void ejecutar(List<TareaResolucion> tareas) {
        Queue<TareaResolucion> cola = new LinkedList<>(tareas);
        Map<TareaResolucion, Long> restante = new HashMap<>();

        for (TareaResolucion t : tareas) {
            restante.put(t, t.getTiempoServicioEstimadoMs());
        }

        while (!cola.isEmpty()) {
            TareaResolucion t = cola.poll();

            if (t.getTiempoInicio() == 0) {
                long inicio = System.currentTimeMillis();
                t.setTiempoInicio(inicio);
                t.setTiempoEspera(inicio - t.getTiempoLlegada());
            }

            long porEjecutar = Math.min(quantumMs, restante.get(t));

            try {
                Thread.sleep(porEjecutar);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            long nuevoRestante = restante.get(t) - porEjecutar;
            if (nuevoRestante <= 0) {
                long fin = System.currentTimeMillis();
                t.setTiempoFin(fin);
                restante.remove(t);
            } else {
                restante.put(t, nuevoRestante);
                cola.offer(t);
            }
        }
    }
}
