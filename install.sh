#!/bin/bash

# ------------------------------------------------------------------
# Sansou Florian 
# fl.sansou@gmail.com

# install.sh
# Script d'installation
# ------------------------------------------------------------------


#################################################
#                       #
#    PLEASE DO NOT CHANGE THE FOLLOWING CODES   #
#                       #
#################################################

#################
# Dependicies
#################
sudo apt install -y openjdk-11-jre python3-pip python3-tk
sudo apt-get install --reinstall libpq-dev

python3 -m pip install ivy-python
python3 -m pip install PyQt5
python3 -m pip install matplotlib
python3 -m pip install psycopg2
python3 -m pip pip install pyproj

#################
# Database
#################

cd NavDB/
chmod +x navdb.sh
./navdb.sh


echo "Installation terminé"


