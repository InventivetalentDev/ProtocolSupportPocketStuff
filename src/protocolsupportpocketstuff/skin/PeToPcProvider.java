package protocolsupportpocketstuff.skin;

import java.util.Base64;
import java.util.UUID;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import protocolsupport.api.Connection;
import protocolsupport.api.events.PlayerPropertiesResolveEvent;
import protocolsupportpocketstuff.ProtocolSupportPocketStuff;
import protocolsupportpocketstuff.api.skins.SkinUtils;
import protocolsupportpocketstuff.api.util.PocketCon;
import protocolsupportpocketstuff.api.util.PocketPacketHandler;
import protocolsupportpocketstuff.api.util.PocketPacketListener;
import protocolsupportpocketstuff.packet.handshake.ClientLoginPacket;
import protocolsupportpocketstuff.packet.play.SkinPacket;
import protocolsupportpocketstuff.storage.Skins;
import protocolsupportpocketstuff.util.StuffUtils;

public class PeToPcProvider implements PocketPacketListener, Listener {

	private static final ProtocolSupportPocketStuff plugin = ProtocolSupportPocketStuff.getInstance();
	public static final String SKIN_PROPERTY_NAME = "textures";
	public static final String APPLY_SKIN_ON_JOIN_KEY = "applySkinOnJoin";

	@PocketPacketHandler
	public void onConnect(Connection connection, ClientLoginPacket packet) {
		String skinData = packet.getJsonPayload().get("SkinData").getAsString();
		String uniqueSkinId = UUID.nameUUIDFromBytes(skinData.getBytes()).toString();
		if (Skins.getInstance().hasPeSkin(uniqueSkinId)) {
			plugin.debug("Already cached skin, adding to the Connection's metadata...");
			connection.addMetadata(StuffUtils.APPLY_SKIN_ON_JOIN_KEY, Skins.getInstance().getPeSkin(uniqueSkinId));
			return;
		}
		byte[] skinByteArray = Base64.getDecoder().decode(skinData);
		boolean slim = packet.getJsonPayload().get("SkinGeometryName").getAsString().equals("geometry.humanoid.customSlim");
		new MineskinThread(connection, uniqueSkinId, skinByteArray, slim).start();
	}

	@PocketPacketHandler
	public void onSkinChange(Connection connection, SkinPacket packet) {
		boolean slim = packet.getSkinName().equals("skin.Standard.CustomSlim");
		new MineskinThread(connection, packet.getUUID().toString(), packet.getSkinData(), slim).start();
	}

	@EventHandler
	public void onPlayerPropertiesResolve(PlayerPropertiesResolveEvent event) {
		Connection con = event.getConnection();
		if (PocketCon.isPocketConnection(con)) {
			//In this event the server actually doesn't have the uuid registered yet.
			if (con.hasMetadata(StuffUtils.APPLY_SKIN_ON_JOIN_KEY)) {
				plugin.debug("Applying cached skin for " + event.getName() + "...");
				SkinUtils.SkinDataWrapper skinDataWrapper = (SkinUtils.SkinDataWrapper) con.getMetadata(APPLY_SKIN_ON_JOIN_KEY);
				event.addProperty(new PlayerPropertiesResolveEvent.ProfileProperty(SKIN_PROPERTY_NAME, skinDataWrapper.getValue(), skinDataWrapper.getSignature()));
				con.removeMetadata(APPLY_SKIN_ON_JOIN_KEY);
			}
		}
	}

}
