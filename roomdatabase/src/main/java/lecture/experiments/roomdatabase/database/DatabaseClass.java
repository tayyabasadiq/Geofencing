package lecture.experiments.roomdatabase.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import lecture.experiments.roomdatabase.ScreenTime;
import lecture.experiments.roomdatabase.dao.DaoAccess;
import lecture.experiments.roomdatabase.dao.location_interface;
import lecture.experiments.roomdatabase.dao.places;
import lecture.experiments.roomdatabase.repository.Location_Tracking;
import lecture.experiments.roomdatabase.repository.Places_Visited;

@Database(entities = {ScreenTime.class, Location_Tracking.class, Places_Visited.class}, version = 3, exportSchema = false)
public abstract class DatabaseClass extends RoomDatabase {

    public abstract DaoAccess daoAccess();
    public abstract location_interface location();
    public abstract places places();
}