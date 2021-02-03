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
sudo apt install -y openjdk-11-jre python3-pip python3-tk python3-pil python3-pil.imagetk
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

######################################
# installation relative to GUID_COMM #
######################################
cd ..
sudo apt update
sudo apt install build-essential
sudo apt-get install xorg openbox
sudo apt-get install libxt-dev
sudo apt-get install libpcre3-dev
sudo apt-get install tcl8.6-dev
sudo apt-get install libglib2.0-dev
cd GUID_COMM/ivy-c_3.15.1/src/ 
make
sudo make install

echo "Installation terminé"


