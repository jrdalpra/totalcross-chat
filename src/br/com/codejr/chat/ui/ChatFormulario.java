package br.com.codejr.chat.ui;

import totalcross.sys.Vm;
import totalcross.ui.Button;
import totalcross.ui.Container;
import totalcross.ui.Control;
import totalcross.ui.TabbedContainer;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import br.com.codejr.chat.Chat;
import br.com.codejr.chat.model.Usuario;

public class ChatFormulario extends Container {

	private TabbedContainer tab;
	private SalaFormulario sala;
	private String[] tabs;
	private ConversaFormulario[] conversas;
	private Button sair;

	public void conversa(Usuario usuario) {

		int atual = 0, contador = tabs.length;

		for (int i = 0; i < contador; i++) {
			if (tabs[i].equalsIgnoreCase(usuario.login)) {
				tab.setActiveTab(i);
				return;
			}
		}

		String[] novasTabs = new String[contador + 1];
		ConversaFormulario[] novasConversas = new ConversaFormulario[contador];
		atual = novasTabs.length - 1;
		Vm.arrayCopy(tabs, 0, novasTabs, 0, contador);
		Vm.arrayCopy(conversas, 0, novasConversas, 0, contador - 1);
		novasTabs[atual] = usuario.login;
		novasConversas[atual - 1] = new ConversaFormulario(usuario);

		tabs = novasTabs;
		conversas = novasConversas;

		remove(tab);
		add(tab = new TabbedContainer(tabs), LEFT + 2, AFTER + 2, FILL - 2, FILL - 2, sair);

		tab.setContainer(0, sala);

		for (int i = 0; i < conversas.length; i++) {
			tab.setContainer(i + 1, conversas[i]);
		}

		tab.setActiveTab(atual);

		repaintNow();

	}

	@Override
	public void initUI() {
		sala = new SalaFormulario(this);
		tabs = new String[] { "Sala" };
		add(sair = new Button("Sair"), RIGHT - 2, TOP + 2, PREFERRED + 5, PREFERRED + 2);
		add(tab = new TabbedContainer(tabs), LEFT + 2, AFTER + 2, FILL - 2, FILL - 2);

		tab.setContainer(0, sala);

		sair.appId = Chat.BOTAO_SAIR;

	}

	@Override
	public void onEvent(Event event) {
		switch (event.type) {
			case ControlEvent.PRESSED: {
				switch (((Control) event.target).appId) {
					case Chat.BOTAO_SAIR: {
						Usuario.LOGADO = null;
						sala.limpa();
						Chat.getMainWindow().swap(new LoginFormulario());
						break;
					}
				}
				break;
			}
		}

	}

}
