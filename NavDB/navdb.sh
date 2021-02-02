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

export PGPASSWORD=$psqlpass

#################################################
#                       #
#    PLEASE DO NOT CHANGE THE FOLLOWING CODES   #
#                       #
#################################################

#################
# Dependicies
#################
sudo apt-get update
sudo apt-get install postgresql-client postgresql pgadmin3 -y
sudo apt-get install  
sudo apt-get install 

#################
# Database
#################
sudo printf "CREATE USER $psqluser WITH PASSWORD '$psqlpass';\n ALTER ROLE user_nd WITH CREATEDB;\nCREATE DATABASE $psqldb WITH OWNER $psqluser;" > nd.sql

sudo -u postgres psql -f nd.sql

psql -h localhost -d navdb -U user_nd -f InitDatabase/src/parser/db.sql

echo "Remplissage de la bdd, cela peut-être très long"
java -jar InitDatabase/dist/InitDatabase.jar
