package br.com.codejr.chat.ui;

import totalcross.ui.Button;
import totalcross.ui.Container;
import totalcross.ui.Control;
import totalcross.ui.Edit;
import totalcross.ui.Label;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import br.com.codejr.chat.Chat;
import br.com.codejr.chat.model.Usuario;

public class LoginFormulario extends Container {

	private Edit login;
	private Edit senha;
	private Button entrar;

	@Override
	public void initUI() {

		Button sair;

		add(new Label("Login"), LEFT + 1, TOP, PREFERRED, PREFERRED);
		add(login = new Edit(""), SAME, AFTER + 1, FILL, PREFERRED - 2);
		add(new Label("Senha"), SAME, AFTER + 2, PREFERRED, PREFERRED);
		add(senha = new Edit(""), SAME, AFTER + 1, FILL, PREFERRED - 2);
		add(entrar = new Button("Entrar"), CENTER, AFTER + 2, PREFERRED + 5, PREFERRED + 2);
		add(sair = new Button("Sair"), CENTER, AFTER + 2, SAME, SAME);

		senha.setMode(Edit.PASSWORD_ALL);
		entrar.setEnabled(false);

		entrar.appId = Chat.BOTAO_ENTRAR;
		sair.appId = Chat.BOTAO_SAIR;

	}

	@Override
	public void onEvent(Event event) {

		entrar.setEnabled(login.getLength() > 0 && senha.getLength() > 0);

		switch (event.type) {
			case ControlEvent.PRESSED: {
				switch (((Control) event.target).appId) {
					case Chat.BOTAO_ENTRAR: {
						boolean logou = Usuario.logaCom(login.getText(), senha.getText());
						if (logou) {
							Chat.getMainWindow().swap(new ChatFormulario());
						}
						break;
					}
					case Chat.BOTAO_SAIR: {
						Chat.exit(0);
						break;
					}
				}
				break;
			}
		}

	}
}
