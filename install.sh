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
sudo apt-get update
sudo apt-get update
sudo apt-get install -y sudo add-apt-repository ppa:openjdk-r/ppa
sudo apt-get update
sudo apt install openjdk-11-jre

python3 -m pip install ivy-python
python3 -m pip install PyQt5

#################
# Database
#################

cd NavDB/
chmod +x navdb.sh
./navdb.sh



