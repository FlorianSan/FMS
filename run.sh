#!/bin/bash

# ------------------------------------------------------------------
# Sansou Florian 
# fl.sansou@gmail.com

# Run.sh
# Application de lancement des modules du FMS 
# ------------------------------------------------------------------

#lauch apps

gnome-terminal --working-directory=`pwd`/SimParam/ --title=SIM_PARAM --geometry=73x31+1000+100 -- bash -c "python3 SimParam.py ; exec bash" &
export APP_SIMPARAM=$!

gnome-terminal --working-directory=`pwd`/modele_fcu_ui/ --title=FCU --geometry=73x31+1000+100 -- bash -c "python3 simulator.py ; exec bash" &
export APP_FCU=$!

gnome-terminal --working-directory=`pwd`/GUID_COMM/ --title=GUID_COMM  -- bash -c "./app ; exec bash" &
export APP_COMM=$!

gnome-terminal --working-directory=`pwd`/GUID_SEQ/ --title=GUID_SEQ -- bash -c "python3 main.py ; exec bash" &
export APP_SEQ=$!

gnome-terminal --working-directory=`pwd`/GUID_TRAJ/ --title=GUID_TRAJ -- bash -c "python3 main.py ; exec bash" & # > terminal.txt
export APP_TRAJ=$!

gnome-terminal --working-directory=`pwd`/FPLN_LEGS/ --title=FPLN_LEGS -- bash -c "python3 main.py ; exec bash" &
export APP_LEGS=$!

gnome-terminal --working-directory=`pwd`/FPLN_ROUTE/dist --title=FPLN_ROUTE -- bash -c "java -jar FPLN_ROUTE.jar  ; exec bash" &
export APP_ROUTE=$!

echo "Press 'q' to exit"

while : ; do
	read -n 1 k <&1
	if [[ $k = q ]] ; then 
	    #Stop all app  
	    kill -9 $(pgrep gnome-terminal)
	    #Start new terminal
	    gnome-terminal --window --maximize --working-directory=`pwd` --title=Start  --  &
	    break
    fi
done
