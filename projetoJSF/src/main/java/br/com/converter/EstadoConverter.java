package br.com.converter;

import java.io.Serializable;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import br.com.entidades.Estados;
import br.com.jpautil.JPAUtil;

@FacesConverter(forClass = Estados.class, value = "estadoConverte")
public class EstadoConverter implements Converter, Serializable {

	private static final long serialVersionUID = 1L;

	@Override // RETORNA OBJETO INTEIRO
	public Object getAsObject(FacesContext context, UIComponent component, String codigoEstado) {

		EntityManager entityManager = JPAUtil.getEntityManager();
		EntityTransaction entityTransaction = entityManager.getTransaction();
		entityTransaction.begin();

		Estados estados = (Estados) entityManager.find(Estados.class, Long.parseLong(codigoEstado));
		return estados;
	}

	@Override // RETORNA EM STRING
	public String getAsString(FacesContext context, UIComponent component, Object estado) {

		if (estado == null) {
			return null;
		}
		if (estado instanceof Estados) {
			return ((Estados) estado).getId().toString();
		}else {
			return estado.toString();
		}

	}

}
