package br.com.jpb.repository;

import java.io.Serializable;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.querydsl.core.types.EntityPath;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

public abstract class BaseQueryDslRepository<T> implements Serializable {

	@PersistenceContext
	private EntityManager entityManager;

	protected JPAQuery<T> createJPAQuery(EntityPath<T> path) {
		JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(entityManager);
		return jpaQueryFactory.selectFrom(path);
	}

	protected <Q> JPAQuery<Q> createJPAQuery() {
		return new JPAQuery<>(entityManager);
	}

}
