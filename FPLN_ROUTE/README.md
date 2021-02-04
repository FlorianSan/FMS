# FPLN ROUTE

////Menu principal:
```console
	1 - FPLN
	2 - READY
	3 - INIT
	4 - MODIF
	5 - ROUTE
	6 - Quit
	-------------------------
	Enter the number of this section:
```
* Taper 1 puis appuyer sur ENTER pour initialiser le plan de vol.
* Taper 2 puis appuyer sur ENTER pour envoyer un message "READY" aux LEGS :
	o "Initial Flight Plan Ready" après avoir initialisé le plan de vol
	o "Modified Flight Plan Ready" après avoir saisie une modification du plan de vol
* Taper 3 puis appuyer sur ENTER pour envoyer un message aux LEGS contenant :
	o Le plan de vol initial après initialisation de celui-ci (et de potentielles modifications pré-flight)
	o Les modifications du plan de vol après saisie de celle-ci (modifications in-flight)
* Taper 4 puis appuyer sur ENTER pour pouvoir saisir des modifications (pré-flight ou in-flight)
* Taper 5 puis appuyer sur ENTER pour afficher la route : 
	o En pré-flight : toute la route
	o In-flight : la section précédente (N-1), la section active (N) et les deux sections suivante (N+1, N+2)
* Taper 6 puis appuyer sur ENTER pour quitter l'application




////Initialisation du plan de vol :

	FPLN TAB
	-------------------------
	Type departure APT and press Enter :

* Taper l'identifiant de l'aéroport de départ (ex : LFBO) puis appuyer sur ENTER :
	o Message de confirmation : "==> FromAPT LFBO successfully added to FPLN !"
	o Message d'erreur (aéroport n'existe pas dans la NavDB ou n'est pas au bon format) : "==> INVALID ENTRY"

	Type arrival APT and press Enter :

* Taper l'identifiant de l'aéroport d'arrivée (ex : LFPO) puis appuyer sur ENTER :
	o Message de confirmation : "==> ToAPT LFPO successfully added to FPLN !"
	o Message d'erreur (aéroport n'existe pas dans la NavDB ou n'est pas au bon format) : "==> INVALID ENTRY"


	Type AWY WPT to enter a section
	Type DEL to delete the previous entry
	Type ACTIVATE when you are done

* Taper l'identifiant de l'airway, un espace, l'identifiant du waypoint puis appuyer sur ENTER pour entrer une section (ex : UY156 PERIG) :
	o Message de confirmation : "==> Section UY156-PERIG successfully added to route ! "
	o Message d'erreur :
		- "==> INCORRECT ENTRY: The route must begin by a section DIRECT-WPT !" (la première section saisie n'est pas au format "DIRECT WPT")
		- "==> INVALID ENTRY AWY" (l'airway saisie n'existe pas dans la NavDB ou n'est pas au bon format)
		- "==> INVALID ENTRY PREVIOUS WPT IN AWY" (l'airway saisie ne contient pas le waypoint final de la section précédent)
		- "==> INVALID ENTRY WPT" (le waypoint saisi n'existe pas dans la NavDB ou n'est pas au bon format)
		- "==> INVALID ENTRY WPT IN AWY" (l'airway saisie ne contient pas le waypoint saisi)
* Taper DEL puis appuyer sur ENTER pour supprimer la saisie précédente :
	o Message de confirmation : " ==> Last entry UY156-PERIG is deleted !" (si la dernière section saisie est UY156 PERIG)
	o Message d'erreur : "==> The route does not yet have a section ! "
* Taper ACTIVATE puis appuyer sur ENTER pour terminer la saisie de la route et activer le plan de vol :
	o Message de confirmation : " Route successfully added to FPLN !" "Fpln successfully filled ! "
	o Message d'erreur : " ==> IMPOSSIBLE ACTIVATION: The route must finish by a section DIRECT-STAR !" (la dernière section saisie avant activation n'est pas au format "DIRECT STAR")

Remarque : la route doit commencer par une section au format "DIRECT WPT" (ex : "DIRECT FISTO") et doit finir par la section "DIRECT STAR"




////Modifications in-flight :

	MODIF MENU (inflight)
	-------------------------
	1 - CHANGE OF THE LAST WPT IN SECTION
	2 - INSERT WPTs BETWEEN SECTIONS
	3 - Quit
	-------------------------
	Enter action to do:

* Taper 1 puis appuyer sur ENTER pour modifier le waypoint final d'une section :

	NON SEQUENCED ROUTE
	-------------------------
	1 - UY156 - PERIG
	2 - UT210 - TUDRA
	3 - UT158 - AMB
	4 - DIRECT - STAR
	-------------------------
	Which section do you want to modify ?
	Enter the number of this section:

	o Taper le numéro associé à la section à modifier puis appuyer sur ENTER (ex : 1 pour UY156 - PERIG)

	POSSIBLE WPT
	-------------------------
	1 - FOUCO
	2 - ADABI
	-------------------------
	Choose the new WPT.
	Enter its number: 

	o Taper le numéro associé au nouveau waypoint final souhaité puis appuyer sur ENTER (ex : 1 pour FOUCO)
		- Message de confirmation : "==> Section successfully modified ! section "

	CONNEXION MANAGEMENT
	-------------------------
	2 - UT210 - TUDRA
	3 - UT158 - AMB
	4 - DIRECT - STAR
	-------------------------
	From which section do you want to reach the route?
	Enter the number of this section:

	o Taper le numéro associé à la section à rejoindre pour récupérer la route puis appuyer sur ENTER (ex : 2 pour UT210 - TUDRA)

	POSSIBLE WPT
	-------------------------
	1 - DIBAG
	2 - TUDRA
	-------------------------
	Choose the WPT to reach the route.
	Enter its number:

	o Taper le numéro associé au waypoint par lequel rejoindre la section choisie puis appuyer sur ENTER (ex : 2 pour TUDRA)

	1- Join directly the chosen section
	2- Insert transitional WPT(s)
	-------------------------
	How to reach the selected section ?
	Choose an option:

	o Taper 2 puis appuyer sur ENTER pour insérer des waypoints intermédiaire avant de récupérer la route restante (cf. insertion de waypoints entre des sections de la route)
 
	o Taper 1 puis appuyer sur ENTER pour rejoindre directement la route restante (Si plusieurs airways existent entre le waypoint modifié et celui qu'on veut rejoindre le module demandera de choisir par laquelle passer)

	NON SEQUENCED MODIFIED ROUTE
	-------------------------
	1 - UY156 - FOUCO
	2 - UT209 - TUDRA
	3 - UT158 - AMB
	4 - DIRECT - STAR

	1- ACTIVATE MODIFICATION
	2- DELETE
	-------------------------
	Entry an option:

	o Taper 1 puis appuyer sur ENTER pour activer les modifications
		- Message de confirmation : "==> Successful modification ! "
	o Taper 2 puis appuyer sur ENTER pour annuler les modifications
		- Message de confirmation : "==> Modification canceled ! "



	MODIF MENU (inflight)
	-------------------------
	1 - CHANGE OF THE LAST WPT IN SECTION
	2 - INSERT WPTs BETWEEN SECTIONS
	3 - Quit
	-------------------------
	Enter action to do:

* Taper 2 puis appuyer sur ENTER pour insérer des waypoints entre des sections :

	NON SEQUENCED ROUTE
	-------------------------
	2 - UT210 - TUDRA
	3 - UT158 - AMB
	4 - DIRECT - STAR
	-------------------------
	After which section do you want to insert the waypoint?
	Enter the number of this section: 

	o Taper le numéro associé à la section après laquelle on veut insérer des waypoints puis appuyer sur ENTER (ex : 2 pour UT210 - TUDRA)

	Enter a waypoint
	Type DEL to delete the previous entry
	Type END when you are done

	o Taper l'identifiant du waypoint puis appuyer sur ENTER pour insérer le waypoint (ex : OLINO puis SOPIL)
	(Si plusieurs airways existent entre le waypoint précédent et celui qu'on veut insérer le module demandera de choisir par laquelle passer)
		- Message de confirmation : "==> Section DIRECT-OLINO successfully added to route ! "
		- Message d'erreur (waypoint n'existe pas dans la NavDB ou n'est pas au bon format) : "==> INVALID ENTRY WPT"
	o Taper DEL puis appuyer sur ENTER pour supprimer la saisie précédente :
		- Message de confirmation : " ==> Last entry DIRECT-OLINO is deleted !" (si la dernière section saisie est OLINO)
		- Message d'erreur : "==> You have not entered a WPT yet !"
	o Taper END puis appuyer sur ENTER pour terminer la saisie des waypoints à insérer.

	CONNEXION MANAGEMENT
	-------------------------
	5 - UT158 - AMB
	6 - DIRECT - STAR
	-------------------------
	From which section do you want to reach the route?
	Enter the number of this section: 

	o Taper le numéro associé à la section à rejoindre pour récupérer la route puis appuyer sur ENTER (ex : 5 pour UT158 - AMB)

	POSSIBLE WPT
	-------------------------
	1 - BEVOL
	2 - AMB
	-------------------------
	Choose the WPT to reach the route.
	Enter its number: Enter the number of this section: 

	o Taper le numéro associé au waypoint par lequel rejoindre la section choisie puis appuyer sur ENTER (ex : 2 pour AMB)

	(Plusieurs airways existent entre le dernier waypoint inséré et le waypoint que l'on souhaite rejoindre. Le module nous demande de choisir par laquelle passer)

	POSSIBLE AWY
	-------------------------
	1 - A34
	2 - R10
	-------------------------
	Choose the AWY to go through.
	Enter its number: 

	o Taper le numéro associé à l'airway par laquelle on souhaite passer rejoindre la route puis appuyer sur ENTER (ex : 1 pour A34)

	NON SEQUENCED MODIFIED ROUTE
	-------------------------
	2 UT210 - TUDRA
	3 DIRECT - OLINO
	4 DIRECT - SOPIL
	5 A34 - AMB
	6 DIRECT - STAR

	1- ACTIVATE MODIFICATION
	2- DELETE
	-------------------------
	Entry an option: 
	
	o Taper 1 puis appuyer sur ENTER pour activer les modifications
		- Message de confirmation : "==> Successful modification ! "
	o Taper 2 puis appuyer sur ENTER pour annuler les modifications
		- Message de confirmation : "==> Modification canceled ! "





////Modifications pré-flight :

	MODIF MENU (preflight)
	-------------------------
	1 - CHANGE OF THE LAST WPT IN SECTION
	2 - INSERT WPTs BETWEEN SECTIONS
	3 - CHANGE FPLN AIRPORTs
	4 - Quit
	-------------------------
	Enter action to do:

	(Options 1 et 2 comme pour les modifications in-flight)

* Taper 3 puis appuyer sur ENTER pour modifier les aéroports de départ et/ou d'arrivée:

	Do you want to change the departure APT ? (Y/N):

	o Taper Y puis appuyer sur ENTER pour pouvoir modifier l'aéroport de départ (sinon taper N) :

	Type departure APT and press Enter : 

	(Comme pour l'ajout de l'aéroport de départ lors de l'initialisation du plan de vol)

	Do you want to change the arrival APT ? (Y/N):

	o Taper Y puis appuyer sur ENTER pour pouvoir modifier l'aéroport d'arrivée (sinon taper N) :

	Type arrival APT and press Enter : 

	(Comme pour l'ajout de l'aéroport de départ lors de l'initialisation du plan de vol)

	o Si au moins un des deux aéroports a été modifié, le pilote devra ressaisir toute la route :

	Type AWY WPT to enter a section
	Type DEL to delete the previous entry
	Type ACTIVATE when you are done

	(Comme pour la saisie de la route lors de l'initialisation du plan de vol)
