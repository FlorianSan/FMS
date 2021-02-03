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
sudo apt-get install -y --reinstall libpq-dev
sudo apt-get install -y --reinstall libxcb-xinerama0

python3 -m pip install ivy-python
python3 -m pip install PyQt5
python3 -m pip install matplotlib
python3 -m pip install psycopg2
python3 -m pip install pyproj
python3 -m pip install Pillow

#################
# Database
#################

cd NavDB/
chmod +x navdb.sh
./navdb.sh


echo "Installation terminé"


