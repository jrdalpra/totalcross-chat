package br.com.codejr.chat.ui;

import totalcross.ui.Button;
import totalcross.ui.Container;
import totalcross.ui.Control;
import totalcross.ui.ListBox;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import br.com.codejr.chat.Chat;
import br.com.codejr.chat.model.Usuario;

public class SalaFormulario extends Container {

	public ListBox usuarios;
	private final ChatFormulario chat;
	private Button conversar;

	public SalaFormulario(ChatFormulario chat) {
		this.chat = chat;
	}

	@Override
	public void initUI() {
		Button recarregar;
		add(recarregar = new Button("Recarregar"), RIGHT - 2, TOP + 2, PREFERRED, PREFERRED);
		add(conversar = new Button("Conversar"), RIGHT - 2, BOTTOM - 2, PREFERRED, PREFERRED);
		add(usuarios = new ListBox(), LEFT + 2, AFTER + 2, FILL - 2, FIT - 2, recarregar);

		recarregar.appId = Chat.BOTAO_RECARREGAR;
		conversar.appId = Chat.BOTAO_CONVERSAR;
		conversar.setEnabled(false);

		recarrega(Usuario.lista());

	}

	public void limpa() {
		recarrega(null);
	}

	@Override
	public void onEvent(Event event) {

		conversar.setEnabled(usuarios.getSelectedIndex() >= 0);

		switch (event.type) {
			case ControlEvent.PRESSED: {

				switch (((Control) event.target).appId) {
					case Chat.BOTAO_RECARREGAR: {
						recarrega(Usuario.lista());
						break;
					}
					case Chat.BOTAO_CONVERSAR: {
						chat.conversa((Usuario) usuarios.getSelectedItem());
						break;
					}
				}

				break;
			}
		}
	}

	public void recarrega(Usuario[] lista) {

		if (lista == null || lista.length == 0) {
			usuarios.removeAll();
			usuarios.repaintNow();
			return;
		}

		usuarios.removeAll();
		usuarios.add(lista);
		usuarios.repaintNow();

	}

}
