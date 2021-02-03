# FMS

Ce projet est à destination d'un machine fonctionnant sous Ubuntu 20.04.1 LTS, 64 bits avec un version GNOME 3.36.8 et un système de fenêtrage X11.

Récuperez le dossier sur votre ordianteur:

$ git clone --recursive -j8 https://github.com/FlorianSan/FMS

En premier, il est necessaire d'installer les modules et la base de données 

$ chmod 777 ./install.sh<br/>
$ ./install.sh

Pour lancer le système de gestion du vol

$ chmod 777 ./run.sh<br/>
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

Comme le dépot est constituer de sous modules, il est necessaire lors de modification d'un/des sous modules de les récuperer
https://git-scm.com/book/fr/v2/Utilitaires-Git-Sous-modules

