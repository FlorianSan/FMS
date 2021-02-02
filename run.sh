#!/bin/bash

# ------------------------------------------------------------------
# Sansou Florian 
# fl.sansou@gmail.com

# Run.sh
# Application de lancement des modules du FMS 
# ------------------------------------------------------------------

#lauch apps




gnome-terminal --working-directory=`pwd`/modele_fcu_ui/ --title=FCU  -- bash -c "python3 simulator.py ; exec bash" &
sleep 1

gnome-terminal --working-directory=`pwd`/GUID_COMM/ --title=GUID_COMM  -- bash -c "./app > terminal.txt ; exec bash" &

gnome-terminal --working-directory=`pwd`/GUID_SEQ/ --title=GUID_SEQ -- bash -c "python3 main.py ; exec bash" &

gnome-terminal --working-directory=`pwd`/GUID_TRAJ/ --title=GUID_TRAJ -- bash -c "python3 main.py > terminal_traj.txt ; exec bash" & 

sleep 1

gnome-terminal --working-directory=`pwd`/FPLN_LEGS/ --title=FPLN_LEGS  -- bash -c "python3 main.py ; exec bash" &

gnome-terminal --working-directory=`pwd`/FPLN_ROUTE/dist --title=FPLN_ROUTE  -- bash -c "java -jar FPLN_ROUTE.jar  ; exec bash" &

gnome-terminal --working-directory=`pwd`/SimParam/ --title=SIM_PARAM  -- bash -c "python3 SimParam.py ; exec bash" &



gnome-terminal --working-directory=`pwd` --title=IvyProbe  -- bash -c "ivyprobe '(.*)' > ivyprobe.txt ; exec bash" &

echo "READY"


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
