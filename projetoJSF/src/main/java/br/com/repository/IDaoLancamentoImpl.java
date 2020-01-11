package br.com.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import br.com.entidades.Lancamento;
import br.com.jpautil.JPAUtil;

public class IDaoLancamentoImpl implements IDaoLancamento {

	@SuppressWarnings("unchecked")
	@Override
	public List<Lancamento> consulta(Long user) {   // TRAZ A LISTA DE LANÇAMENTO POR USUARIO QUE LANÇOU
		
		List<Lancamento> lista = null;
		
		EntityManager entityManager = JPAUtil.getEntityManager();
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();
		
		lista = entityManager.createQuery("FROM Lancamento where usuario.id = " + user ).getResultList(); 
		
		transaction.commit();
		entityManager.close();
		return lista;
	}

}
