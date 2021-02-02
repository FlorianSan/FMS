DROP table IF EXISTS balises cascade;
DROP table IF EXISTS holdingpattern;
DROP table IF EXISTS aeroport cascade;
DROP table IF EXISTS piste;
DROP table IF EXISTS appr, sid, star, procedure, route;
DROP TYPE IF EXISTS typeAppr;
DROP FUNCTION IF EXISTS verif_fix_route ();

DROP INDEX IF EXISTS index_balises;
DROP INDEX IF EXISTS index_aeroport;
DROP INDEX IF EXISTS index_piste;

DROP SEQUENCE IF EXISTS compteur_balise CASCADE;
DROP SEQUENCE IF EXISTS compteur_aeroport CASCADE;
DROP SEQUENCE IF EXISTS compteur_piste CASCADE;


CREATE SEQUENCE compteur_balise START 1;
CREATE SEQUENCE compteur_aeroport START 1;
CREATE SEQUENCE compteur_piste START 1;


create table balises(
	id integer default nextval('compteur_balise'),
	icaoCode varchar(2) NOT NULL,
	identifiant varchar(5) NOT NULL,
	nom varchar(30) NOT NULL,
	latitude varchar(9) NOT NULL,
	longitude varchar(10) NOT NULL,
	aeroport varchar(4) NOT NULL,
	icaoAeroport varchar(2) NOT NULL,
	magneticVariation varchar(5) NOT NULL,
	datum varchar(3) NOT NULL,
	firIdentifier varchar(4) DEFAULT '',
	UirIdentifier varchar(4) DEFAULT '',
	sEIndicator varchar(1) DEFAULT '',
	sEdate varchar(11) DEFAULT '',
	elevation varchar(5) DEFAULT '',
	primary key(icaoCode,identifiant)
);

CREATE TABLE vor(
	frequence varchar(5) NOT NULL,
	facilityCharacteristics varchar(5) DEFAULT '',
	frequenceProtection varchar(3) NOT NULL,
	navaidMerit varchar(1) NOT NULL,
	primary key(icaoCode,identifiant)
) INHERITS (balises);

CREATE table dme(
	frequence varchar(5) NOT NULL,
	frequenceProtection varchar(3) NOT NULL,
	biais varchar(2) NOT NULL,
	navaidMerit varchar(1) NOT NULL,
	facilityCharacteristics varchar(5) DEFAULT '',
	primary key(icaoCode,identifiant)
) INHERITS (balises);

CREATE table tacan(
	frequence varchar(5) NOT NULL,
	frequenceProtection varchar(3) NOT NULL,
	biais varchar(2) NOT NULL,
	navaidMerit varchar(1) NOT NULL,
	facilityCharacteristics varchar(5) DEFAULT '',
	primary key(icaoCode,identifiant)
) INHERITS (balises);

CREATE table ilsdme(
	frequence varchar(5) NOT NULL,
	frequenceProtection varchar(3) NOT NULL,
	biais varchar(2) NOT NULL,
	navaidMerit varchar(1) NOT NULL,
	facilityCharacteristics varchar(5) DEFAULT '',
	primary key(icaoCode,identifiant)
) INHERITS (balises);

CREATE table vorDme(
	identifiantDme varchar(4) NOT NULL,
	latitudeDme varchar(9) NOT NULL,
	longitudeDme varchar(10) NOT NULL,
	frequence varchar(5) NOT NULL,
	frequenceProtection varchar(3) NOT NULL,
	biais varchar(2) NOT NULL,
	navaidMerit varchar(1) NOT NULL,
	facilityCharacteristics varchar(5) DEFAULT '',
        primary key(icaoCode, identifiant)
) INHERITS (balises);

CREATE TABLE ndb (
     	frequence varchar(5) NOT NULL,
    	classNdb varchar(5) NOT NULL,
    	facilityCharacteristics varchar(5) DEFAULT '',
    	primary key(icaoCode,identifiant)
) INHERITS (balises);

CREATE TABLE waypoint (
     	type varchar(3) NOT NULL,
    	usage varchar(2) NOT NULL,
    	nameIndicator varchar(3) DEFAULT '',
    	primary key(icaoCode,identifiant,aeroport)
) INHERITS (balises);

CREATE TABLE holdingpattern(

	regionCode varchar(4) NOT NULL,
	icaoCode  varchar(2) NOT NULL,
	dupIdentifier varchar(2) NOT NULL,
	fixIdentifier varchar(5) NOT NULL,
	icaoBalise varchar(2) NOT NULL,
	secCodeBalise varchar(1) NOT NULL,
	subCodeBalise varchar(1) NOT NULL,
	InboundHoldingCourse varchar(4) NOT NULL,
	turnDirection varchar(1) NOT NULL,
	legLength varchar(3) NOT NULL,
	legTime varchar(2) NOT NULL,
	minAltitude varchar(5) NOT NULL,
	maxAltitude varchar(5) NOT NULL,
	holdSpeed varchar(3) NOT NULL,
	rNP varchar(3) NOT NULL,
	arcRadius varchar(6) NOT NULL,
	name varchar(25) NOT NULL,
	typeBalise text,
	rattachBalise integer,
	primary key (dupIdentifier, fixIdentifier,icaoBalise, secCodeBalise, subCodeBalise));


CREATE TABLE aeroport(
        id integer default nextval('compteur_aeroport'),
	icaoCode varchar(2) NOT NULL,
	identifiant varchar(4) NOT NULL,
        ataIata  varchar(3) NOT NULL,
        speedLimitAltitude varchar(5) NOT NULL,
        longestRwy varchar(3) NOT NULL,
        ifr varchar(2) NOT NULL,
        longRwy varchar(1) NOT NULL,
        latitude varchar(9) NOT NULL,
        longitude varchar(10) NOT NULL,
        magneticVariation varchar(5) NOT NULL,
        elevation varchar(5) NOT NULL,
        speedLimit varchar(3) NOT NULL,
        recVhf varchar(4) NOT NULL,
        icaoCodeVhf varchar(2) NOT NULL,
        transAltitude varchar(5) NOT NULL,
        transLevel varchar(5) NOT NULL,
        publicMilitaire varchar(1) NOT NULL,
        timeZone varchar(3) NOT NULL,
        dayTime varchar(1) NOT NULL,
        MTInd varchar(1) NOT NULL,
        datum varchar(3) NOT NULL,
        airportName varchar(30) NOT NULL,
        firIdentifier varchar(4) DEFAULT '',
        uirIdentifier varchar(4) DEFAULT '',
        sEIndicator varchar(1) DEFAULT '',
	sEdate varchar(11) DEFAULT '',
        asInd varchar(1) DEFAULT '',
        asArptIdent varchar(4) DEFAULT '',
        asIcaoCode varchar(2) DEFAULT '',
        PRIMARY KEY(identifiant)
);

CREATE TABLE piste(
        id integer default nextval('compteur_piste'),
        icaoCode varchar(2) NOT NULL,
        identifiantRwy varchar(5) NOT NULL,
        identifiantAeroport varchar(4) NOT NULL,
        runwayLength varchar(5) NOT NULL,
        runwayBearing varchar(4) NOT NULL,
        latitude varchar(9) NOT NULL,
        longitude varchar(10) NOT NULL,
        runwayGrad varchar(5) NOT NULL,
        ellipsoidHeight varchar(6) NOT NULL,
        lndgThresElev varchar(5) NOT NULL,
        dsplcdThr varchar(4) NOT NULL,
        tch varchar(2) NOT NULL,
        width varchar(3) NOT NULL,
        locGlsIdent varchar(4) NOT NULL,
        categorieLoc varchar(4) NOT NULL,
        stopway varchar(4) NOT NULL,
        secLocGlsIdent varchar(4) NOT NULL,
        categorieSecLoc varchar(4) NOT NULL,
        PRIMARY KEY(identifiantRwy, identifiantAeroport),
        FOREIGN KEY (identifiantAeroport) REFERENCES aeroport (identifiant)
);

CREATE TABLE ils (
     	categorie varchar(1) NOT NULL,
        frequence varchar(5) NOT NULL,
        runwayIdentifiant varchar(5) NOT NULL,
        locBearing varchar(4) NOT NULL,
        gsLatitude varchar(9) NOT NULL,
        gsLongitude varchar(10) NOT NULL,
        locFr varchar(4) NOT NULL,
        localiserPositionReference varchar(1) NOT NULL,
        gsThres varchar(4) NOT NULL,
        locWidth varchar(4) NOT NULL,
        gsAngle varchar(3) NOT NULL,
        tch varchar(2) NOT NULL,
        gsElev varchar(5) NOT NULL,
        facility varchar(4) NOT NULL,
        facilityIcao varchar(2) NOT NULL,
        facilitySec varchar(1) NOT NULL,
        facilitySub varchar(1) NOT NULL,
        FOREIGN KEY (runwayIdentifiant,aeroport) REFERENCES piste(identifiantRwy, identifiantAeroport),
        PRIMARY KEY (identifiant, aeroport, runwayIdentifiant)
) INHERITS (balises);           

CREATE TABLE procedure(
    typeProcedure varchar(4) NOT NULL,
    aeroportIdentifiant varchar(4) NOT NULL,
    icaoCode varchar(2) NOT NULL,
    identifiant varchar(6) NOT NULL,
    routeType varchar(1) NOT NULL,
    transitionIdentifier varchar(5) NOT NULL,
    sequenceNumber varchar(3) NOT NULL,
    fixIdentifiant varchar(5) NOT NULL,
    icaoCodeFix varchar(2) NOT NULL,
    secCodeFix varchar(1) NOT NULL,
    subCodeFix varchar(1) NOT NULL,
    descriptionCode varchar(4) NOT NULL,
    turnDirection varchar(1) NOT NULL,
    requiredNavigationPerformance varchar(3) NOT NULL,
    pathAndTerminaison varchar(2) NOT NULL,
    turnDirectionValide varchar(1) NOT NULL,
    recommendedNavaid varchar(4) NOT NULL,
    icaoCodeNavaid varchar(2) NOT NULL,
    arcRadius varchar(6) NOT NULL,
    theta varchar(4) NOT NULL,
    rho varchar(4) NOT NULL,
    magneticCruise varchar(4) NOT NULL,
    routeDistance varchar(4) NOT NULL,
    secCodeRoute varchar(1) NOT NULL,
    subCodeRoute varchar(1) NOT NULL,
    altitudeDescription varchar(1) NOT NULL,
    atc varchar(1) NOT NULL,
    altitude varchar(5) NOT NULL,
    altitude2 varchar(5) NOT NULL,
    transAltitude varchar(5) NOT NULL,
    speedLimit varchar(3) NOT NULL,
    verticalAngle varchar(4) NOT NULL,
    centerFix varchar(5) NOT NULL,
    multiCd varchar(1) NOT NULL,
    icaoCodeCenter varchar(2) NOT NULL,
    secCodeCenter varchar(1) NOT NULL,
    subCodeCenter varchar(1) NOT NULL,
    gnssFmsIndicator varchar(1) NOT NULL,
    speedLmt varchar(1) NOT NULL,
    routeQual1 varchar(1) NOT NULL,
    routeQual2 varchar(1) NOT NULL,
    startEndIndicator varchar(1) DEFAULT '',
    startEndDate varchar(11) DEFAULT '',
    legDistance varchar(4) DEFAULT '', 
    PRIMARY KEY(typeProcedure, aeroportIdentifiant, icaoCode, identifiant, sequenceNumber, transitionIdentifier),
    FOREIGN KEY (aeroportIdentifiant) REFERENCES aeroport(identifiant)
);

CREATE TABLE sid(
    identifiant varchar(6) NOT NULL,
    icaoCode varchar(2) NOT NULL, 
    aeroportIdentifiant varchar(4) NOT NULL,
    runwayIdentifiant varchar(5),
    lastFix varchar(5),
    firstFix varchar(5),
    latLong text[][2] NOT NULL,
    PRIMARY KEY (identifiant, icaoCode, aeroportIdentifiant),
    FOREIGN KEY (aeroportIdentifiant) REFERENCES aeroport(identifiant)
);
           
CREATE TABLE star(
    identifiant varchar(6) NOT NULL,
    icaoCode varchar(2) NOT NULL,
    aeroportIdentifiant varchar(4) NOT NULL,
    runwayIdentifiant varchar(5),
    lastFix varchar(5),
    firstFix varchar(5),
    latLong text[][2] NOT NULL,
    PRIMARY KEY (identifiant, icaoCode, aeroportIdentifiant),
    FOREIGN KEY (aeroportIdentifiant) REFERENCES aeroport(identifiant)
);

CREATE TYPE typeAppr AS ENUM ('I', 'G', 'F', 'J','L','B','M','P','Q','R','S','U','D','V','N','W','X','Y','Z','A');

CREATE TABLE appr(
    runwayIdentifiant varchar(4) NOT NULL,
    typeApproche typeAppr NOT NULL,
    aeroportIdentifiant varchar(4) NOT NULL,
    icaoCode varchar(2) NOT NULL,
    premierPoint varchar(5) NOT NULL,
    balises text[][2] NOT NULL,
    FOREIGN KEY (aeroportIdentifiant) REFERENCES aeroport(identifiant)
);


CREATE table route(
    routeIdentifiant varchar(6) NOT NULL,
    sequenceNumber varchar(4) NOT NULL,
    fixIdentifiant varchar(5) NOT NULL,
    icaoCodeFix varchar(2) NOT NULL,
    secCodeFix varchar(1) NOT NULL,
    subCodeFix varchar(1) NOT NULL,
    descriptionCode varchar(4) NOT NULL,
    boundaryCode varchar(1) NOT NULL,
    routeType varchar(1) NOT NULL,
    levelRoute varchar(1) NOT NULL,
    direct varchar(1) NOT NULL,
    cruiseTableIdentifier varchar(2) NOT NULL,
    euIndicator varchar(1) NOT NULL,      
    receiverVhf varchar(4) NOT NULL,
    icaoCodeVhf varchar(2) NOT NULL,
    requiredNavigation varchar(3) NOT NULL,
    theta varchar(4) NOT NULL,
    rho varchar(4) NOT NULL,
    outboundMagneticCruise varchar(4) NOT NULL,
    routeFromDistance varchar(4) NOT NULL,
    inboundMagneticCruise varchar(4) NOT NULL,
    minAltitude varchar(5) NOT NULL,
    minAltitude2 varchar(5) NOT NULL,
    maxAltitude varchar(5) NOT NULL,
    fixRadius varchar(4) NOT NULL,
    PRIMARY KEY(routeIdentifiant, sequenceNumber, fixIdentifiant, icaoCodeFix)
);

CREATE UNIQUE INDEX index_balises ON balises (id);
CREATE UNIQUE INDEX index_aeroport ON aeroport (id);
CREATE UNIQUE INDEX index_piste ON piste (id);

CREATE FUNCTION verif_fix_route () RETURNS TRIGGER AS 
'
  DECLARE
    r RECORD;
  BEGIN   
    
        select * into r from balises* where identifiant like NEW.fixIdentifiant and icaoCode like NEW.icaoCodeFix;
        IF r.identifiant ISNULL THEN
          RAISE NOTICE ''Route : La Balise %, % n existe pas'', NEW.fixIdentifiant, NEW.icaoCodeFix;
        END IF;
        RETURN NEW;
  END; 
' 
LANGUAGE 'plpgsql'; 

CREATE TRIGGER trig_bef_ins_route BEFORE INSERT ON route 
FOR EACH ROW 
EXECUTE PROCEDURE verif_fix_route();


CREATE OR REPLACE FUNCTION latitude_degre (latitude text) RETURNS float AS $$
    DECLARE
	zone text;
	degre integer;
	minute integer;
	seconde integer;
	millisecondes integer;
	degreDecimal float;
    BEGIN
       	zone = substring(latitude from 1 for 1);
	degre = cast(substring(latitude from 2 for 2) as integer);
	minute = cast(substring(latitude from 4 for 2) as integer);
	seconde = cast(substring(latitude from 6 for 2) as integer);
	millisecondes = cast(substring(latitude from 8 for 2) as integer) * 10;
	degreDecimal = cast(degre as float) + cast(minute as float) * (1.0 / 60.0) + cast(seconde as float) * (1.0 / 3600.0) + cast(millisecondes as float) * (1.0 / 3600000);
    
	IF zone like 'N' THEN
    		return degreDecimal;
	ELSE
		return degreDecimal * -1.0;
	END IF;
    END;
  $$ LANGUAGE plpgsql;


CREATE OR REPLACE FUNCTION longitude_degre (longitude text) RETURNS float AS $$
    DECLARE
	zone text;
	degre integer;
	minute integer;
	seconde integer;
	millisecondes integer;
	degreDecimal float;
    BEGIN
       	zone = substring(longitude from 1 for 1);
	degre = cast(substring(longitude from 2 for 3) as integer);
	minute = cast(substring(longitude from 5 for 2) as integer);
	seconde = cast(substring(longitude from 7 for 2) as integer);
	millisecondes = cast(substring(longitude from 9 for 2) as integer) * 10;
	degreDecimal = cast(degre as float) + cast(minute as float) * (1.0 / 60.0) + cast(seconde as float) * (1.0 / 3600.0) + cast(millisecondes as float) * (1.0 / 3600000);
    
	IF zone like 'E' THEN
    		return degreDecimal;
	ELSE
		return degreDecimal * -1.0;
	END IF;
    END;
  $$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION distance (latitude1 float,longitude1 float, latitude2 text, longitude2 text) RETURNS float AS $$
    DECLARE
	lat2 float;
	lon2 float;
	a float;
	d float;
	dLat float;
	dLon float;
	lat1_rad float;
	lat2_rad float;
    BEGIN
	
	lat2 =latitude_degre(latitude2);
	lon2 =longitude_degre(longitude2);

       	dLat = (lat2-latitude1)*Pi()/360;
	dLon = (lon2-longitude1)*Pi()/360;

	lat1_rad = latitude1*Pi()/360;
	lat2_rad = lat2*Pi()/360;

	a = sin(dLat/2) * sin(dLat/2) + sin(dLon/2) * sin(dLon/2) * cos(lat1_rad) * cos(lat2_rad); 
	d = 6371 * 2 * atan2(sqrt(a), sqrt(1-a));

	return d;

    END;
  $$ LANGUAGE plpgsql;
