#!/bin/bash
echo "===== Compilando proyecto Java ====="
mkdir -p bin
javac -d bin src/*.java
if [ $? -eq 0 ]; then
    echo "✔ Compilación exitosa."
else
    echo "✘ Hubo errores en la compilación."
fi
