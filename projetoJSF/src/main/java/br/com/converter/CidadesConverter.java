package br.com.converter;

import java.io.Serializable;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import br.com.entidades.Cidades;
import br.com.entidades.Estados;
import br.com.jpautil.JPAUtil;

@FacesConverter(forClass = Cidades.class, value = "cidadeConverte")
public class CidadesConverter implements Converter, Serializable {

	private static final long serialVersionUID = 1L;

	@Override // RETORNA OBJETO INTEIRO
	public Object getAsObject(FacesContext context, UIComponent component, String codigoCidade) {

		EntityManager entityManager = JPAUtil.getEntityManager();
		EntityTransaction entityTransaction = entityManager.getTransaction();
		entityTransaction.begin();

		Cidades cidades = (Cidades) entityManager.find(Cidades.class, Long.parseLong(codigoCidade));
		return cidades;
	}

	@Override // RETORNA EM STRING
	public String getAsString(FacesContext context, UIComponent component, Object cidade) {

		if (cidade == null) {
			return null;
		}
		if (cidade instanceof Cidades) {
			return ((Cidades) cidade).getId().toString();
		} else {
			return cidade.toString();
		}
	}

}
