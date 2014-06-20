package punishers.thirst.client;

import java.util.Set;

import punishers.thirst.server.WaterFountain;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface CSVReaderServiceAsync {
  //public void getWaterFountains(AsyncCallback<Set<WaterFountain>> async);
  //public void retrieveFromSet(int id, AsyncCallback<WaterFountain> async);
  public void updateData(AsyncCallback<Void> async);
}