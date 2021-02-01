# FMS

Ce projet est à destination d'un machine fonctionnant sous Ubuntu 20.04.1 LTS, 64 bits avec un version GNOME 3.36.8 et un système de fenêtrage X11.

Il est necessaire d'avoir installe :

- python3 
- java 
- postgresql

En premier, il est necessaire d'installer la base de données 

Toute les commandes suivantes supposent que vous vous trouvez dans le repertoire /FMS

[Lien vers le document d'installation de la base de données](/NavDB/install.txt)/
<br/>
[Lien vers le document de remplissage de la base de données](NavDB/execution.txt)

Pour lancer le système de gestion du vol

$ chmod 777 ./run.sh
$ ./run.sh

Un terminal ivyprobe est connecté sur le bus et redirigé vers un fichier texte ivyprobe.txt à la racine 

Les fenêtres s'ouvrent, pour fermer l'integralité des fenêtres : 

$ q

dans le terminal initial.
Un nouveau terminal s'ouvre de manière à relancer le FMS.


Le système est constitué de 7 modules

- FPLN_ROUTE
- FPLN_LEGS
- GUID_TRAJ
- GUID_SEQ
- GUID_COMM [Lien vers le README de COMM](https://github.com/FlorianSan/GuidCommFms/blob/master/README)<br/>
- modele_fcu_ui
- SimParam [Lien vers le README de SimParam](https://github.com/FlorianSan/SimParam/blob/main/README.md)<br/>
