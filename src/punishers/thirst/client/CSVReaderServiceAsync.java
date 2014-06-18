package punishers.thirst.client;

import java.util.Set;

import punishers.thirst.server.WaterFountain;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface CSVReaderServiceAsync {
  public void getWaterFountains(AsyncCallback<Set<String>> async);
  public void retrieveFromSet(int id, AsyncCallback<String> async);
  public void updateData(AsyncCallback<Void> async);
}