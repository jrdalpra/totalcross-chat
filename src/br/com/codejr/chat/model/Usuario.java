package br.com.codejr.chat.model;

import totalcross.io.IOException;
import totalcross.io.IllegalArgumentIOException;
import totalcross.net.HttpStream;
import totalcross.net.HttpStream.Options;
import totalcross.net.URI;
import totalcross.sys.Convert;
import totalcross.sys.InvalidNumberException;
import totalcross.xml.SyntaxException;
import totalcross.xml.XmlTokenizer;
import br.com.codejr.chat.Chat;
import br.com.codejr.chat.model.xml.ListaDeUsuariosDoXML;

public class Usuario extends XmlTokenizer {

	public static Usuario LOGADO = null;

	public static Usuario[] lista() {
		Usuario[] lista = null;
		if (!logado()) {
			return null;
		}
		try {
			Options opcoes = new HttpStream.Options();
			opcoes.httpType = HttpStream.POST;
			HttpStream http = new HttpStream(new URI(Chat.SERVIDOR + "/usuario/lista/tc;jsessionid=" + LOGADO.jsessionid), opcoes);
			boolean ok = http.isOk();
			if (ok) {
				lista = new ListaDeUsuariosDoXML().lista(http);
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

	public static boolean logaCom(String login, String senha) {
		return new Usuario().com(login, senha).consegueAutenticar();
	}

	public static boolean logado() {
		return LOGADO != null;
	}

	public int id;
	public String login;
	public String senha;
	public String jsessionid;

	private String atributo;

	public Usuario com(String login, String senha) {
		this.login = login;
		this.senha = senha;
		return this;
	}

	public boolean consegueAutenticar() {
		try {
			Options opcoes = new HttpStream.Options();
			opcoes.httpType = HttpStream.POST;
			opcoes.postData = post();
			HttpStream http = new HttpStream(new URI(Chat.SERVIDOR + "/login/autentica/tc"), opcoes);
			boolean ok = http.isOk();
			if (ok) {
				this.tokenize(http);
				LOGADO = this;
			}
			http.close();
			return ok;
		} catch (IllegalArgumentIOException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SyntaxException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || (!(obj instanceof Usuario))) {
			return false;
		}
		Usuario outro = (Usuario) obj;
		if (this.login == null || outro.login == null) {
			return false;
		}
		return this.login.equalsIgnoreCase(outro.login);
	}

	@Override
	protected void foundAttributeName(byte[] input, int offset, int count) {
		atributo = new String(input, offset, count);
	}

	@Override
	protected void foundAttributeValue(byte[] input, int offset, int count, byte dlm) {
		String valor = new String(input, offset, count);
		if (atributo.equalsIgnoreCase("id")) {
			id = id(valor);
		} else if (atributo.equalsIgnoreCase("jsessionid")) {
			jsessionid = valor;
		}
	}

	private int id(String valor) {
		try {
			return Convert.toInt(valor);
		} catch (InvalidNumberException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public String post() {
		return new StringBuffer().append("usuario.login=").append(login).append("&usuario.senha=").append(senha).toString();
	}

	@Override
	public String toString() {
		return login;
	}

}
