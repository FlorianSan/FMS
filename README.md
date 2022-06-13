# FMS

Ce projet est à destination d'un machine fonctionnant sous Ubuntu 22.04.1 LTS, 64 bits.

Récuperez le dossier sur votre ordianteur:
```console
sudo apt install git-all

git clone --recursive -j8 git@github.com:FlorianSan/FMS.git

git checkout FMS_19
```
En premier, il est necessaire d'installer les modules et la base de données 
```console

./install.sh
```
Pour lancer le système de gestion du vol
```console

./run.sh
```
Un terminal ivyprobe est connecté sur le bus et redirigé vers un fichier texte ivyprobe.txt à la racine 

Les fenêtres s'ouvrent, pour fermer l'integralité des fenêtres : 
```console
q
```
dans le terminal initial.

Un nouveau terminal s'ouvre de manière à relancer le FMS.


Le système est constitué de 7 modules

- FPLN_ROUTE [Lien vers le README de ROUTE](/FPLN_ROUTE/README.md)<br/>
- FPLN_LEGS
- GUID_TRAJ [Lien vers le README de TRAJ](https://github.com/JulieMorvan33/Projet-AVI/blob/main/README.txt)<br/>
- GUID_SEQ [Lien vers le README de SEQ](https://github.com/NicolasABN/GUID_SEQ/blob/main/README.txt)<br/>
- GUID_COMM [Lien vers le README de COMM](https://github.com/FlorianSan/GuidCommFms/blob/master/README)<br/>
- modele_fcu_ui
- SimParam [Lien vers le README de SimParam](https://github.com/FlorianSan/SimParam/blob/main/README.md)<br/>

Comme le dépot est constituer de sous modules, il est necessaire lors de modification d'un/des sous modules de les récuperer
https://git-scm.com/book/fr/v2/Utilitaires-Git-Sous-modules

