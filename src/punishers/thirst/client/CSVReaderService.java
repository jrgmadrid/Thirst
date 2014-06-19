package punishers.thirst.client;

import java.util.Set;

import punishers.thirst.server.WaterFountain;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("csvreader")
public interface CSVReaderService extends RemoteService{
//public Set<WaterFountain> getWaterFountains();
//public WaterFountain retrieveFromSet(int id);
public void updateData();

}
