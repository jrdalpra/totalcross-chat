package br.com.codejr.chat;

import totalcross.ui.MainWindow;
import br.com.codejr.chat.ui.LoginFormulario;

public class Chat extends MainWindow {

	public static final String SERVIDOR = "http://localhost:8080/server";

	public static final int BOTAO_ENTRAR = 999;

	public static final int BOTAO_SAIR = 998;

	public static final int BOTAO_RECARREGAR = 997;

	public static final int BOTAO_CONVERSAR = 996;

	public static final int BOTAO_ENVIAR = 995;

	@Override
	public void initUI() {
		Chat.getMainWindow().swap(new LoginFormulario());
		// Chat.getMainWindow().swap(new ConversaFormulario(null));
	}

}
