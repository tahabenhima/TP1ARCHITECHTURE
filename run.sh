#!/bin/bash
# Script pour compiler et exécuter le projet TP1

echo "=== Compilation du projet TP1ARCHITECHTURE ==="

# Créer le répertoire bin s'il n'existe pas
mkdir -p bin

# Compiler tous les fichiers Java
echo "Compilation des sources..."
javac -d bin -sourcepath src/main/java $(find src/main/java -name "*.java")

if [ $? -eq 0 ]; then
    echo "✓ Compilation réussie"
else
    echo "✗ Erreur de compilation"
    exit 1
fi

# Compiler les tests
echo "Compilation des tests..."
javac -d bin -cp bin -sourcepath src/test/java $(find src/test/java -name "*.java")

if [ $? -eq 0 ]; then
    echo "✓ Compilation des tests réussie"
else
    echo "✗ Erreur de compilation des tests"
    exit 1
fi

echo ""
echo "=== Exécution des tests ==="
java -cp bin com.tp1.reflection.IoCContainerTest

echo ""
echo "=== Exécution de l'application de démonstration ==="
java -cp bin com.tp1.reflection.examples.Application

echo ""
echo "=== Terminé ==="
