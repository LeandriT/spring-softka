@echo off
echo Deteniendo y eliminando contenedores, redes y volúmenes...
docker-compose down -v

echo Construyendo las imágenes de Docker...
docker-compose build

echo Iniciando los contenedores en segundo plano...
docker-compose up -d

echo Proceso completado.
pause
