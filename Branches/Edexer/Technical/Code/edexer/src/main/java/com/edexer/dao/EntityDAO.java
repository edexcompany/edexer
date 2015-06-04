package com.edexer.dao;

import org.springframework.transaction.annotation.Transactional;

/**
 * @author Karim
 * This is the generic interface for CRUD operations on DB.
 */
@Transactional
public interface EntityDAO<E> {

	/**
	 * Add new entity
	 * @param entity the generic entity to be persisted into the db
	 * @return The id of object added
	 */
	public Object add(E entity);
	
	/**
	 * Delete an existing entity
	 * @param entity
	 * @return the deleted object
	 */
	public void delete(E entity);
	
	/**
	 * Update an existing entity in the DB
	 * @param entity
	 * @return the updated object
	 */
	public void update(E entity);
	
	/**
	 * Search for record in db.
	 * @param id, the identifier to search with in the db
	 * @return the object represents the record in the db
	 */
	public E find(Object id);
}
