import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SimuladorPlanificacion {

    public static List<TareaResolucion> generarLoteHeterogeneo() {
        List<TareaResolucion> lista = new ArrayList<>();
        long ahora = System.currentTimeMillis();

        // Circuitos simples
        lista.add(new TareaResolucion("T1", MetodoResolucion.CRAMER, Complejidad.SIMPLE, 200, ahora));
        lista.add(new TareaResolucion("T2", MetodoResolucion.GAUSS_JORDAN, Complejidad.SIMPLE, 250, ahora));

        // Circuitos medios
        lista.add(new TareaResolucion("T3", MetodoResolucion.LIBRERIA_NUMERICA, Complejidad.MEDIO, 500, ahora));
        lista.add(new TareaResolucion("T4", MetodoResolucion.CRAMER, Complejidad.MEDIO, 600, ahora));

        // Circuitos complejos
        lista.add(new TareaResolucion("T5", MetodoResolucion.GAUSS_JORDAN, Complejidad.COMPLEJO, 900, ahora));
        lista.add(new TareaResolucion("T6", MetodoResolucion.LIBRERIA_NUMERICA, Complejidad.COMPLEJO, 1000, ahora));

        return lista;
    }

    public static List<Planificador> crearPlanificadores() {
        return Arrays.asList(
                new PlanificadorFCFS(),
                new PlanificadorRoundRobin(100),
                new PlanificadorSJF()
        );
    }

    public static List<TareaResolucion> clonarTareas(List<TareaResolucion> original) {
        List<TareaResolucion> copia = new ArrayList<>();
        long ahora = System.currentTimeMillis();
        for (TareaResolucion t : original) {
            copia.add(new TareaResolucion(
                    t.getId(),
                    t.getMetodo(),
                    t.getComplejidad(),
                    t.getTiempoServicioEstimadoMs(),
                    ahora
            ));
        }
        return copia;
    }

    public static ResultadoPlanificador ejecutarPlanificador(Planificador p, List<TareaResolucion> base) {
        List<TareaResolucion> copia = clonarTareas(base);

        long inicioGlobal = System.currentTimeMillis();
        p.ejecutar(copia);
        long finGlobal = System.currentTimeMillis();
        long tiempoTotal = finGlobal - inicioGlobal;

        long sumaEspera = 0;
        long sumaTurnaround = 0;

        for (TareaResolucion t : copia) {
            long turnaround = t.getTiempoFin() - t.getTiempoLlegada();
            sumaEspera += t.getTiempoEspera();
            sumaTurnaround += turnaround;
        }

        double esperaPromedio = (double) sumaEspera / copia.size();
        double turnaroundPromedio = (double) sumaTurnaround / copia.size();
        double tiempoTotalSeg = tiempoTotal / 1000.0;
        double throughput = copia.size() / Math.max(tiempoTotalSeg, 0.001);

        return new ResultadoPlanificador(
                p.getNombre(),
                copia,
                tiempoTotal,
                esperaPromedio,
                turnaroundPromedio,
                throughput
        );
    }
}
