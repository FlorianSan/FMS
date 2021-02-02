#!/bin/bash

# ------------------------------------------------------------------
# Sansou Florian 
# fl.sansou@gmail.com

# navdb.sh
# Script de création de la navdb
# ------------------------------------------------------------------


#################
# Change this values
#################


psqluser="user_nd"   # Database username
psqlpass="nd"  # Database password
psqldb="navdb"   # Database name


#################################################
#                       #
#    PLEASE DO NOT CHANGE THE FOLLOWING CODES   #
#                       #
#################################################

#################
# Dependicies
#################
sudo apt-get update
sudo apt-get -y postgresql-client
sudo apt-get -ypostgresql 
sudo apt-get -y pgadmin3

#################
# Database
#################
sudo printf "CREATE USER $psqluser WITH PASSWORD '$psqlpass';\n ALTER ROLE user_nd WITH CREATEDB;\nCREATE DATABASE $psqldb WITH OWNER $psqluser;" > nd.sql

sudo -u postgres psql -f nd.sql

psql -h localhost -d navdb -U user_nd -f InitDatabase/src/parser/db.sql

java -jar InitDatabase/dist/InitDatabase.jar
