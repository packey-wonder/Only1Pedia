package org.vaadin.backend;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaQuery;

import org.vaadin.backend.domain.Book;

@Stateless
public class BookService {

    @PersistenceContext(unitName = "customer-pu")
    private EntityManager entityManager;

	private List<Book> listBooks;

    public void saveOrPersist(Book entity) {
        if (entity.getId() > 0) {
            entityManager.merge(entity);
        } else {
            entityManager.persist(entity);
        }
    }

    public void deleteEntity(Book entity) {
//        if (entity.getId() > 0) {
//            // reattach to remove
//            entity = entityManager.merge(entity);
//            entityManager.remove(entity);
//        }
   }

    public List<Book> findAll() {
        CriteriaQuery<Book> cq = entityManager.getCriteriaBuilder().
                createQuery(Book.class);
        cq.select(cq.from(Book.class));
        return entityManager.createQuery(cq).getResultList();
    }

    public List<Book> findByName(String filter) {
        if (filter == null || filter.isEmpty()) {
            return findAll();
        }
        filter = filter.toLowerCase();
        return entityManager.createNamedQuery("Book.findByName",
                Book.class)
                .setParameter("filter",  "%" + filter + "%").getResultList();
    }
    public Book findByEmail(String filter) {
        if (filter == null || filter.isEmpty()) {
            return null;
        }
        filter = filter.toLowerCase();
        List<Book> list =  entityManager.createNamedQuery("Book.findByEmail",
                Book.class)
                .setParameter("filter", filter).getResultList();
        if (list.size() == 0) {
        	return null;
        } else {
        	return list.get(0);
        }
    }

    public List<Book> findByWord(String filter) {
        if (filter == null || filter.isEmpty()) {
            return findAll();
        }
        filter = filter.toLowerCase();
        return entityManager.createNamedQuery("Book.findByWord",
                Book.class)
                .setParameter("filter", filter + "%").getResultList();
    }

    /** Returns the number of books of the "DB" */
    public int countAllBooks() {
    	listBooks=findAll();
    	return listBooks.size();
    }


    /** Returns a "page" of the resultSet (if we had a DB) */
    public List<Book> getBooksFromRange(int indexStart, int indexEnd){
        return listBooks.subList(indexStart, indexEnd);
    }

}
