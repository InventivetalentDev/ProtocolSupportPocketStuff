package protocolsupportpocketstuff.api.modals;

import protocolsupportpocketstuff.api.modals.elements.ModalUIElement;
import protocolsupportpocketstuff.util.GsonUtils;

import java.util.ArrayList;
import java.util.List;

public class SimpleForm implements Modal {
	
	private final transient ModalType modalType = ModalType.SIMPLE_FORM;
	private final String type = modalType.getPeName();
	
	private String title;
	private String content;
	private List<ModalUIElement> buttons = new ArrayList<>();

	public SimpleForm(String title, String content) {
		this.title = title;
		this.content = content;
	}

	public ModalType getType() {
		return modalType;
	}
	
	public String getPeType() {
		return type;
	}
	
	public SimpleForm setTitle(String title) {
		this.title = title;
		return this;
	}

	public String getTitle() {
		return title;
	}

	public SimpleForm setContent(String content) {
		this.content = content;
		return this;
	}

	public String getContent() {
		return content;
	}

	public SimpleForm addButton(ModalUIElement button) {
		buttons.add(button);
		return this;
	}

	public List<ModalUIElement> getButtons() {
		return buttons;
	}
	
	public String toJSON() {
		return GsonUtils.GSON.toJson(this);
	}
	
	public static SimpleForm fromJson(String JSON) {
		return GsonUtils.GSON.fromJson(JSON, SimpleForm.class);
	}
	
}
