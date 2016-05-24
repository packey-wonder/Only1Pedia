package org.vaadin.backend;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaQuery;

import org.vaadin.backend.domain.Customer;
import org.vaadin.backend.domain.ManagerClass;
import org.vaadin.backend.domain.MemberStatus;

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
        	final String[] names = new String[]{"K1 0 4", "E1 1 1", "G1 1 1"};
            for (String name : names) {
                String[] split = name.split(" ");
                Customer c = new Customer();
                c.setMembershipNo(split[0]);
                // added to support login and access control
                c.setPassword("abc123");
                c.setManagerClass(ManagerClass.values()[Integer.parseInt(split[1])]);
                c.setStatus(MemberStatus.values()[Integer.parseInt(split[2])]);

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

	public Customer findByMembershipNo(String filter) {
        if (filter == null || filter.isEmpty()) {
            return null;
        }
        filter = filter.toLowerCase();
        List<Customer> list =  entityManager.createNamedQuery("Customer.findByMembershipNo",
                Customer.class)
                .setParameter("filter", filter).getResultList();
        if (list.size() == 0) {
        	return null;
        } else {
        	return list.get(0);
        }
	}

}
