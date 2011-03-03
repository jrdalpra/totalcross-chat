package br.com.codejr.chat.model.xml;

import totalcross.io.IOException;
import totalcross.io.Stream;
import totalcross.sys.Convert;
import totalcross.sys.InvalidNumberException;
import totalcross.xml.SyntaxException;
import totalcross.xml.XmlTokenizer;
import br.com.codejr.chat.model.Usuario;

public class ListaDeUsuariosDoXML extends XmlTokenizer {

	private Usuario[] lista;
	private String tag;
	private String atributo;
	private int posicao;
	private Usuario atual;

	@Override
	protected void foundAttributeName(byte[] input, int offset, int count) {
		atributo = new String(input, offset, count);
	}

	@Override
	protected void foundAttributeValue(byte[] input, int offset, int count, byte dlm) {
		String valor = new String(input, offset, count);

		if (atributo.equalsIgnoreCase("tamanho")) {
			lista = new Usuario[inteiro(valor)];
			posicao = -1;
		} else if (atributo.equalsIgnoreCase("login")) {
			atual.login = valor;
		} else if (atributo.equalsIgnoreCase("id")) {
			atual.id = inteiro(valor);
		}

	}

	@Override
	protected void foundStartTagName(byte[] input, int offset, int count) {
		tag = new String(input, offset, count);
		if (tag.equalsIgnoreCase("usuario")) {
			posicao++;
			atual = new Usuario();
			lista[posicao] = atual;
		}
	}

	private int inteiro(String valor) {
		try {
			return Convert.toInt(valor);
		} catch (InvalidNumberException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public Usuario[] lista(Stream stream) throws SyntaxException, IOException {
		tokenize(stream);
		return lista;
	}

}
