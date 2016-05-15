package org.vaadin.backend;

import java.util.List;
import java.util.Random;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaQuery;

import org.vaadin.backend.domain.Customer;
import org.vaadin.backend.domain.CustomerStatus;

@Stateless
public class CustomerService {

    @PersistenceContext(unitName = "customer-pu")
    private EntityManager entityManager;

    public void saveOrPersist(Customer entity) {
        if (entity.getId() > 0) {
            entityManager.merge(entity);
        } else {
            entityManager.persist(entity);
        }
    }

    public void deleteEntity(Customer entity) {
        if (entity.getId() > 0) {
            // reattach to remove
            entity = entityManager.merge(entity);
            entityManager.remove(entity);
        }
    }

    public List<Customer> findAll() {
        CriteriaQuery<Customer> cq = entityManager.getCriteriaBuilder().
                createQuery(Customer.class);
        cq.select(cq.from(Customer.class));
        return entityManager.createQuery(cq).getResultList();
    }

    public List<Customer> findByName(String filter) {
        if (filter == null || filter.isEmpty()) {
            return findAll();
        }
        filter = filter.toLowerCase();
        return entityManager.createNamedQuery("Customer.findByName",
                Customer.class)
                .setParameter("filter", filter + "%").getResultList();
    }
    public Customer findByEmail(String filter) {
        if (filter == null || filter.isEmpty()) {
            return null;
        }
        filter = filter.toLowerCase();
        List<Customer> list =  entityManager.createNamedQuery("Customer.findByEmail",
                Customer.class)
                .setParameter("filter", filter).getResultList();
        if (list.size() == 0) {
        	return null;
        } else {
        	return list.get(0);
        }
    }

    /**
     * Sample data generation
     */
    public void ensureTestData() {
        if (findAll().isEmpty()) {
            final String[] names = new String[]{"Gabrielle Patel", "Brian Robinson", "Eduardo Haugen", "Koen Johansen", "Alejandro Macdonald", "Angel Karlsson", "Yahir Gustavsson", "Haiden Svensson", "Emily Stewart", "Corinne Davis", "Ryann Davis", "Yurem Jackson", "Kelly Gustavsson", "Eileen Walker", "Katelyn Martin", "Israel Carlsson", "Quinn Hansson", "Makena Smith", "Danielle Watson", "Leland Harris", "Gunner Karlsen", "Jamar Olsson", "Lara Martin", "Ann Andersson", "Remington Andersson", "Rene Carlsson", "Elvis Olsen", "Solomon Olsen", "Jaydan Jackson", "Bernard Nilsen"};
            Random r = new Random(0);
            for (String name : names) {
                String[] split = name.split(" ");
                Customer c = new Customer();
                c.setFirstName(split[0]);
                c.setLastName(split[1]);
                // added to support login and access control
                c.setPassword("abc123");
                c.setStatus(CustomerStatus.values()[r.nextInt(CustomerStatus.
                        values().length)]);

                saveOrPersist(c);
            }
        }
    }

    public void resetTestData() {
        if(!findAll().isEmpty()) {
            entityManager.createQuery("DELETE FROM Customer c WHERE c.id > 0").
                    executeUpdate();
        }
        ensureTestData();
    }

}
