#!/bin/bash
echo "===== Iniciando Monitoreo del Sistema ====="
mkdir -p logs
echo "→ Capturando CPU cada 2 segundos..."
top -b -d 2 > logs/cpu_usage.txt &
PID_TOP=$!
echo "→ Capturando memoria RAM..."
free -m > logs/memoria_inicial.txt
echo "→ Capturando procesos ordenados por CPU..."
ps aux --sort=-%cpu > logs/procesos_cpu.txt
echo "→ Capturando estadísticas de I/O de disco..."
iostat -dx 2 5 > logs/disk_io.txt
echo "→ Iniciando aplicación Java..."
java -cp bin Main
echo "→ Deteniendo monitoreo..."
kill $PID_TOP
echo "===== Monitoreo finalizado ====="
echo "Archivos generados en carpeta 'logs/'"
