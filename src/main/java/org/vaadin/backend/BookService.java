package org.vaadin.backend;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import org.vaadin.backend.domain.Book;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaQuery;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

@Stateless
public class BookService {

    @PersistenceContext(unitName = "customer-pu")
    private EntityManager entityManager;

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
                .setParameter("filter", filter + "%").getResultList();
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

}
