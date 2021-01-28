#!/bin/bash
echo 'Création de la Structure SQL'
psql -h localhost -d navdb -U user_nd -f InitDatabase/src/parser/db.sql
echo 'FIN'
