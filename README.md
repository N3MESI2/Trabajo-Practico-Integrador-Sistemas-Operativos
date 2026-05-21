Process Scheduling Algorithms Simulator & Performance Analyzer

## Overview
An engineered Java-based simulation environment designed to model, execute, and analyze CPU process scheduling algorithms. The system simulates heavy computational workloads (matrix resolutions using Cramer, Gauss-Jordan, and numerical libraries) to evaluate the efficiency and resource allocation of different scheduling policies. 

Coupled with a custom Bash-scripted monitoring suite, this project tracks real-time OS-level metrics (CPU usage, RAM allocation, Disk I/O) to provide a comprehensive analysis of system overhead, algorithmic speed-up, and scalability.

## Key Engineering Features

* Algorithmic Scheduling Engine: Full implementation of core OS scheduling algorithms including First-Come, First-Served (FCFS), Round Robin (RR), and Shortest Job First (SJF).
* System-Level Monitoring: Integrated Bash scripts (iostat, top) that run concurrently with the JVM to log real-time OS metrics and process behaviors into dedicated output streams.
* Advanced Performance Metrics: Automated generation of analytical reports calculating:
    - Waiting Time & Turnaround Time
    - System Throughput
    - Algorithmic Overhead & Speed-up ratios
    - Simulated Context Switch impact
* GUI & Data Visualization: Java-built dark-mode interface for real-time execution toggling and comparative performance graphing.

## Tech Stack & Requirements
* Language: Java (JDK 8+)
* Scripting & OS: Bash, Linux (Ubuntu / WSL)
* Architecture: Object-Oriented Programming (OOP), Concurrent Monitoring

## Project Architecture

    ├── src/                     # Java source code and scheduling logic
    ├── bin/                     # Compiled .class binaries
    ├── compilar.sh              # Build automation script
    ├── ejecutar.sh              # Runtime execution script
    ├── monitorear.sh            # OS-level resource monitoring script
    └── logs/                    # Automated system metrics output
        ├── cpu_usage.txt
        ├── memoria_inicial.txt
        ├── procesos_cpu.txt
        └── disk_io.txt

## Build & Execution Instructions

1. Environment Setup
Ensure you are running on a Linux environment (or WSL on Windows) with JDK installed. Grant execution permissions to the shell scripts:
chmod +x compilar.sh ejecutar.sh monitorear.sh

2. Compile the Project
./compilar.sh

3. Run OS Monitoring (Optional but Recommended)
Start the hardware resource tracking in the background before launching the simulator:
./monitorear.sh

4. Launch the Simulator
./ejecutar.sh

## Analytics & Reporting
Inside the graphical interface, triggering the "Save Metrics Report" generates a "metricas_planificacion.txt" file. This is the core deliverable of the engine, detailing the efficiency analysis, hardware footprint (CPU/RAM logs), and simulated I/O disk activity across varying workload complexities (Simple, Medium, Complex).

![050cef53-b940-4eb2-8fcc-a4f46279d077](https://github.com/user-attachments/assets/7d70ccc2-37e2-448b-abfe-8277c0343d0d)
