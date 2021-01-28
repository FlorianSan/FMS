#!/bin/bash
echo 'Création de la Structure SQL'
psql -h localhost -d navdb -U user_nd -f db.sql
echo 'FIN'
