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
sudo apt update
sudo apt install -y openjdk-17-jre python3-pip python3-tk python3-pil python3-pil.imagetk
sudo apt-get install -y --reinstall libpq-dev
sudo apt-get install -y --reinstall libxcb-xinerama0

python3 -m pip install ivy-python==3.3
python3 -m pip install PyQt5==5.15.2
python3 -m pip install matplotlib==3.3.4
python3 -m pip install psycopg2==2.8.6
python3 -m pip install pyproj
python3 -m pip install Pillow

sudo apt install -y default-jre

#################
# Database
#################
cd ./NavDB/
chmod +x navdb.sh
./navdb.sh

######################################
# installation relative to GUID_COMM #
######################################
cd ..

sudo apt install -y build-essential xorg openbox libxt-dev libpcre3-dev tcl8.6-dev libglib2.0-dev
cd GUID_COMM/ivy-c_3.15.1/src/ 
make
sudo make install

echo "Installation complete"


