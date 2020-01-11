package br.com.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import br.com.jpautil.JPAUtil;

public class DaoGeneric<E> {
	public void salvar(E entidade) {    //SALVA
		EntityManager entityManager = JPAUtil.getEntityManager();
		EntityTransaction entityTransaction = entityManager.getTransaction();
		entityTransaction.begin();
		entityManager.merge(entidade);
		entityTransaction.commit();
		entityManager.close();

	}

	public E merge(E entidade) {  // AUTUALIZA
		EntityManager entityManager = JPAUtil.getEntityManager();
		EntityTransaction entityTransaction = entityManager.getTransaction();
		entityTransaction.begin();
		E retorno = entityManager.merge(entidade);
		entityTransaction.commit();
		entityManager.close();

		return retorno;
	}

	public void delete(E entidade) {   // DELETA
		EntityManager entityManager = JPAUtil.getEntityManager();
		EntityTransaction entityTransaction = entityManager.getTransaction();
		entityTransaction.begin();
		entityManager.remove(entidade);
		entityTransaction.commit();
		entityManager.close();

	}
	public void deletePorId(E entidade) {   // DELETA POR ID
		EntityManager entityManager = JPAUtil.getEntityManager();
		EntityTransaction entityTransaction = entityManager.getTransaction();
		entityTransaction.begin();
		Object id = JPAUtil.getPrimarykey(entidade);
		entityManager.createQuery("delete from " + entidade.getClass().getCanonicalName() + " where id = " + id).executeUpdate();		
		entityTransaction.commit();
		entityManager.close();

	}
	@SuppressWarnings("unchecked")   // TRAS AS LISTA
	public List<E> getListEntity(Class<E> entidade){
		EntityManager entityManager = JPAUtil.getEntityManager();
		EntityTransaction entityTransaction = entityManager.getTransaction();
		entityTransaction.begin();
		
		List<E> retorno = entityManager.createQuery("from " + entidade.getName()).getResultList();
		
		entityTransaction.commit();
		entityManager.close();
		return retorno;
	}
	
	public E consultar(Class<E> entidade, String codigo) {
		EntityManager entityManager = JPAUtil.getEntityManager();
		EntityTransaction entityTransaction = entityManager.getTransaction();
		entityTransaction.begin();
		E Object = (E) entityManager.find(entidade, Long.parseLong(codigo));
		entityTransaction.commit();
		return Object;
	}
}
