package br.com.codejr.chat.model;

import totalcross.io.IOException;
import totalcross.io.IllegalArgumentIOException;
import totalcross.net.HttpStream;
import totalcross.net.HttpStream.Options;
import totalcross.net.URI;
import totalcross.sys.Convert;
import totalcross.sys.Vm;
import totalcross.util.Comparable;
import totalcross.xml.SyntaxException;
import br.com.codejr.chat.Chat;
import br.com.codejr.chat.model.xml.ListaDeMensagensDoXML;

public class Mensagem implements Comparable {

	public static Mensagem[] lista(Usuario de, int ultima) {
		Mensagem[] lista = null;
		if (!Usuario.logado()) {
			return null;
		}
		try {
			Options opcoes = new HttpStream.Options();
			opcoes.httpType = HttpStream.POST;
			opcoes.postData = "de.id=" + de.id;
			HttpStream http = new HttpStream(new URI(Chat.SERVIDOR + "/mensagem/lista/tc/" + ultima + ";jsessionid="
					+ Usuario.LOGADO.jsessionid), opcoes);
			boolean ok = http.isOk();
			if (ok) {
				lista = new ListaDeMensagensDoXML().lista(http);
			}
			http.close();
			return lista;
		} catch (IllegalArgumentIOException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SyntaxException e) {
			e.printStackTrace();
		}
		return null;

	}

	public static final Mensagem nova(Usuario de, Usuario para, String texto) {
		Mensagem nova = new Mensagem();
		nova.de = de;
		nova.para = para;
		nova.texto = texto;
		return nova;
	}

	public static int ultimaDa(Object[] lista) {
		if (lista == null) {
			return 0;
		}
		Mensagem[] novaLista = new Mensagem[lista.length];
		Vm.arrayCopy(lista, 0, novaLista, 0, novaLista.length);
		Convert.qsort(novaLista, 0, lista.length - 1);
		return novaLista[novaLista.length - 1].id;
	}

	public int id;
	public Usuario de;
	public Usuario para;
	public String texto;

	@Override
	public int compareTo(Object other) throws ClassCastException {
		Mensagem outra = (Mensagem) other;
		if (this.id == outra.id) {
			return 0;
		} else if (this.id > outra.id) {
			return 1;
		}
		return -1;
	}

	public boolean envia() {
		if (!Usuario.logado() || (id == 0 && (de == null || para == null))) {
			return false;
		}
		if (id != 0 || !de.equals(Usuario.LOGADO)) {
			return true;
		}
		try {
			Options opcoes = new HttpStream.Options();
			opcoes.httpType = HttpStream.POST;
			opcoes.postData = post();
			HttpStream http = new HttpStream(
					new URI(Chat.SERVIDOR + "/mensagem/nova/tc;jsessionid=" + Usuario.LOGADO.jsessionid), opcoes);
			boolean ok = http.isOk();
			http.close();
			return ok;
		} catch (IllegalArgumentIOException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	private String post() {
		return new StringBuffer().append("mensagem.de.id=").append(de.id).append("&mensagem.para.id=").append(para.id)
				.append("&mensagem.texto=").append(texto).toString();
	}

	@Override
	public String toString() {
		return ((de != null && de.equals(Usuario.LOGADO)) ? "Eu: " : "") + texto;
	}

}
