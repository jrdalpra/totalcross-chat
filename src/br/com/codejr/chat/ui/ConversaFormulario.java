package br.com.codejr.chat.ui;

import totalcross.sys.Vm;
import totalcross.ui.Button;
import totalcross.ui.Container;
import totalcross.ui.Control;
import totalcross.ui.Edit;
import totalcross.ui.ListBox;
import totalcross.ui.dialog.MessageBox;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.util.concurrent.Lock;
import br.com.codejr.chat.Chat;
import br.com.codejr.chat.model.Mensagem;
import br.com.codejr.chat.model.Usuario;

public class ConversaFormulario extends Container implements Runnable {

	private final Lock lock = new Lock();

	private final Usuario destinatario;
	private Edit mensagem;
	private ListBox mensagens;
	private Button enviar;

	private Thread thread;

	public ConversaFormulario(Usuario destinatario) {
		this.destinatario = destinatario;
	}

	@Override
	public void initUI() {
		Button recarrega;
		add(recarrega = new Button("Recarregar"), RIGHT - 2, TOP + 2, PREFERRED, PREFERRED);
		add(enviar = new Button("Enviar"), RIGHT - 2, BOTTOM - 2, PREFERRED + 2, PREFERRED);
		add(mensagem = new Edit(""), LEFT + 2, SAME, FIT - 2, PREFERRED);
		add(mensagens = new ListBox(), LEFT + 2, AFTER + 2, FILL - 2, FIT - 2, recarrega);

		enviar.appId = Chat.BOTAO_ENVIAR;
		recarrega.appId = Chat.BOTAO_RECARREGAR;
		enviar.setEnabled(false);

		thread = new Thread(this);
		thread.start();
	}

	private void nova(Mensagem mensagem) {
		if (mensagem == null) {
			return;
		}
		if (mensagem.envia()) {
			mensagens.add(mensagem);
			mensagens.selectLast();
			repaintNow();
		} else {
			new MessageBox("Atenção!", "||Mensagem não foi enviada!").popupNonBlocking();
		}
	}

	private void novas() {
		synchronized (lock) {
			Object[] novas = Mensagem.lista(destinatario, Mensagem.ultimaDa(mensagens.getItems()));
			for (int i = 0, tamanho = novas == null ? 0 : novas.length; i < tamanho; i++) {
				nova((Mensagem) novas[i]);
			}
		}
	}

	@Override
	public void onEvent(Event event) {

		enviar.setEnabled(mensagem.getLength() > 0);

		switch (event.type) {
			case ControlEvent.PRESSED: {
				switch (((Control) event.target).appId) {
					case Chat.BOTAO_ENVIAR: {
						nova(Mensagem.nova(Usuario.LOGADO, destinatario, mensagem.getText()));
						novas();
						mensagem.clear();
						mensagem.requestFocus();
						break;
					}
					case Chat.BOTAO_RECARREGAR: {
						novas();
						break;
					}
				}
				break;
			}
		}
	}

	@Override
	protected void onRemove() {
		thread.stop();
	}

	@Override
	public void run() {
		while (thread.isAlive()) {
			novas();
			Thread.yield();
			Vm.sleep(10 * 1000);
		}
	}

}
