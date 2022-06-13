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


DB_USER="user_nd"   # Database username
DB_USER_PASS="nd"  # Database password
DB_NAME="navdb"   # Database name

export PGPASSWORD=$DB_USER_PASS

#################################################
#                       #
#    PLEASE DO NOT CHANGE THE FOLLOWING CODES   #
#                       #
#################################################

#################
# Dependicies
#################

# sudo apt-get install postgresql-client postgresql pgadmin3 -y
sudo apt install postgresql postgresql-client -y

#################
# Database
#################

sudo su - postgres <<EOF
createdb  $DB_NAME;
psql -c "CREATE USER $DB_USER WITH PASSWORD '$DB_USER_PASS';"
psql -c "grant all privileges on database $DB_NAME to $DB_USER;"
echo "Postgres User '$DB_USER' and database '$DB_NAME' created."
EOF


psql -h localhost -d navdb -U user_nd -f InitDatabase/src/parser/db.sql

echo "Remplissage de la bdd, cela peut-être très long"
java -jar InitDatabase/dist/InitDatabase.jar
