package punishers.thirst.client;

import punishers.thirst.shared.UploadedImage;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class ImageOverlay  extends Composite {
	private static ImageOverlayUiBinder uiBinder = GWT
			.create(ImageOverlayUiBinder.class);

	UserImageServiceAsync imageService = GWT.create(UserImageService.class);

	private HandlerManager handlerManager;

	interface ImageOverlayUiBinder extends UiBinder<Widget, ImageOverlay> {
		}

		@UiField
		Button deleteButton;

		@UiField
		Image image;

		@UiField
		Label timestamp;

//		@UiField
//		VerticalPanel tagPanel;

		protected UploadedImage uploadedImage;
		LoginInfo loginInfo;

		public ImageOverlay(UploadedImage uploadedImage, LoginInfo loginInfo) {
			handlerManager = new HandlerManager(this);

			this.uploadedImage = uploadedImage;
			this.loginInfo = loginInfo;

			initWidget(uiBinder.createAndBindUi(this));

			image.setUrl(uploadedImage.getServingUrl());
			timestamp.setText("Created at:" + uploadedImage.getCreatedAt());

			if (loginInfo != null
					&& (loginInfo.equals(uploadedImage.getOwnerId()))) {
				deleteButton.setText("Delete image");
				deleteButton.setVisible(true);
			} else {
				deleteButton.setVisible(false);
			}

		}


		@UiHandler("image")
		void onClickImage(MouseDownEvent e) {
			Element imageElement = e.getRelativeElement();
			int x = e.getRelativeX(imageElement);
			int y = e.getRelativeY(imageElement);
			// Window.alert("X: " + x + " Y: "+ y);

		}

		/**
		 * 
		 * Handles clicking of the delete button if owned.
		 * 
		 * @param {{@link ClickEvent} e
		 */
		@UiHandler("deleteButton")
		void onClick(ClickEvent e) {
			final ImageOverlay overlay = this;
			imageService.deleteImage(uploadedImage.getKey(),
					new AsyncCallback<Void>() {

						public void onSuccess(Void result) {
							GalleryUpdatedEvent event = new GalleryUpdatedEvent();
							fireEvent(event);
							overlay.removeFromParent();
						}

						public void onFailure(Throwable caught) {

						}
					});

		}

		@Override
		public void fireEvent(GwtEvent<?> event) {
			handlerManager.fireEvent(event);
		}

		public HandlerRegistration addGalleryUpdatedEventHandler(
				GalleryUpdatedEventHandler handler) {
			return handlerManager.addHandler(GalleryUpdatedEvent.TYPE, handler);
		}
}
