import Route
import FlightPlan
import threading
import IhmManager
import DatabaseManager
import CommunicationManager

def ihm(FPLN_Route, FPLN_Leg, FPLN_temporary, communicationManager, databaseManager):
    ihmManager = IhmManager.IhmManager(FPLN_Route, FPLN_Leg, FPLN_temporary, communicationManager, databaseManager)
    ihmManager.run()
    pass

if __name__=="__main__":

    databaseManager = DatabaseManager.Database_Manager()
    
    FPLN_Route = Route.Route()
    FPLN_Leg = FlightPlan.FlightPlan(databaseManager)
    FPLN_temporary = FlightPlan.FlightPlan(databaseManager)

    communicationManager = CommunicationManager.CommunicationManager(FPLN_Route, FPLN_Leg, FPLN_temporary)

    ihmThread = threading.Thread(target=ihm, args=(FPLN_Route, FPLN_Leg, FPLN_temporary, communicationManager, databaseManager))
    ihmThread.start()

    communicationManager.run()
