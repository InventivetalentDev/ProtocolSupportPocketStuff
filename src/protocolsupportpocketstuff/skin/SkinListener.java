package protocolsupportpocketstuff.skin;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import protocolsupport.api.Connection;
import protocolsupportpocketstuff.ProtocolSupportPocketStuff;
import protocolsupportpocketstuff.api.event.ComplexFormResponseEvent;
import protocolsupportpocketstuff.api.event.ModalResponseEvent;
import protocolsupportpocketstuff.api.event.ModalWindowResponseEvent;
import protocolsupportpocketstuff.api.event.SimpleFormResponseEvent;
import protocolsupportpocketstuff.api.modals.SimpleForm;
import protocolsupportpocketstuff.api.modals.callback.SimpleFormCallback;
import protocolsupportpocketstuff.api.modals.elements.ModalImage;
import protocolsupportpocketstuff.api.modals.elements.ModalImage.ModalImageType;
import protocolsupportpocketstuff.api.modals.elements.simple.ModalButton;
import protocolsupportpocketstuff.api.util.PocketCon;
import protocolsupportpocketstuff.api.util.PocketPacketListener;

public class SkinListener implements Listener, PocketPacketListener {
	
	private ProtocolSupportPocketStuff plugin = ProtocolSupportPocketStuff.getInstance();
	
	
    //=====================================================\\
    //					From Pocket						   \\
    //=====================================================\\
	
	//Test to send packet.
	@EventHandler
	public void onChat(AsyncPlayerChatEvent e) {
		if(e.getMessage().contains(".meep")) {
			e.getPlayer().sendMessage("Meep!");
			for(Connection con : PocketCon.getPocketConnections()) {
				e.getPlayer().sendMessage("MEEEEEP!!");
				PocketCon.sendModal(con, 
						new SimpleForm("hoi", "hallo")
							.addButton(new ModalButton("Magbot").setImage(new ModalImage(ModalImageType.EXTERNAL_IMAGE, "http://magbot.nl/img/MagBot.png")))
							.addButton(new ModalButton("Awesome").setImage(new ModalImage(ModalImageType.EXTERNAL_IMAGE, "http://yumamom.com/wp-content/uploads/2015/05/LEGO.jpg"))),
						new SimpleFormCallback() {

							@Override
							public void onSimpleFormResponse(Player player, String modalJSON, boolean isClosedByClient, int clickedButton) {
								player.sendMessage("Thanks for clicking! :wave:");
							}
						});
			}
		}
	}

	//:F
	@EventHandler
	public void onClientResponse(ModalResponseEvent e) {
		plugin.debug("ClientResponseEvent received ~ " + e.getClass().getSimpleName() + " ~ JSON: " + e.getModalJSON());

		PocketCon.handleModalResponse(e.getConnection(), e);
	}

	@EventHandler
	public void onModalWindowResponse(ModalWindowResponseEvent e) {
		plugin.debug("ModalWindowResponseEvent received ~ " + e.getResult());
	}

	@EventHandler
	public void onSimpleFormResponse(SimpleFormResponseEvent e) {
		plugin.debug("SimpleFormResponseEvent received ~ " + e.getClickedButton());
	}

	@EventHandler
	public void onComplexFormResponse(ComplexFormResponseEvent e) {
		plugin.debug("ComplexFormResponseEvent received ~ " + e.getJsonArray());
	}
}
