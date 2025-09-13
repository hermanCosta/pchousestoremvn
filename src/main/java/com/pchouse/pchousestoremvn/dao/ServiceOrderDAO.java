package com.pchouse.pchousestoremvn.dao;

import com.pchouse.pchousestoremvn.exception.BusinessException;
import com.pchouse.pchousestoremvn.models.Company;
import com.pchouse.pchousestoremvn.models.Customer;
import com.pchouse.pchousestoremvn.models.Deposit;
import com.pchouse.pchousestoremvn.models.Device;
import com.pchouse.pchousestoremvn.models.Employee;
import com.pchouse.pchousestoremvn.models.OrderNote;
import com.pchouse.pchousestoremvn.models.Person;
import com.pchouse.pchousestoremvn.models.ServiceOrder;
import com.pchouse.pchousestoremvn.models.ServiceOrderFault;
import com.pchouse.pchousestoremvn.models.ServiceOrderPayment;
import com.pchouse.pchousestoremvn.models.ServiceOrderProdServ;
import com.pchouse.pchousestoremvn.util.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class ServiceOrderDAO {
    
    public long getLastOrderIdDAO() {
        EntityManager em = JPAUtil.getEntityManager();
        long orderId = 0;
        try {
            TypedQuery<Long> query = em.createQuery("SELECT MAX(o.idServiceOrder) FROM ServiceOrder o", Long.class);
            Long result = query.getSingleResult();
            orderId = (result != null) ? result : 0;
        } catch (Exception e) {
            System.err.println("Error retrieving last order ID: " + e.getMessage());
            e.printStackTrace();
        } finally {
            em.close();
        }
        return orderId;
    }        
    
    public ServiceOrder getItemOrderDAO(long pIdOrder) {
        EntityManager em = JPAUtil.getEntityManager();
        ServiceOrder itemOrder = null;
        try {
            itemOrder = em.find(ServiceOrder.class, pIdOrder);
        } catch (Exception e) {
            System.err.println("Error retrieving order by ID: " + e.getMessage());
            e.printStackTrace();
        } finally {
            em.close();
        }
        return itemOrder;
    }
    
    public List<ServiceOrder> getAllOrderDAO(Company pCompany) {
        EntityManager em = JPAUtil.getEntityManager();
        List<ServiceOrder> listOrder = null;
        try {
            TypedQuery<ServiceOrder> query = em.createQuery(
                    "FROM ServiceOrder o WHERE o.company = :pCompany ORDER BY o.created DESC", ServiceOrder.class);
            query.setParameter("pCompany", pCompany);
            listOrder = query.getResultList();
        } catch (Exception e) {
            System.err.println("Error retrieving all orders: " + e.getMessage());
            e.printStackTrace();
        } finally {
            em.close();
        }
        return listOrder;
    }
    
    public List<ServiceOrder> searchOrderDAO(Company pCompany, String pSearch) {
        EntityManager em = JPAUtil.getEntityManager();
        List<ServiceOrder> listOrder = null;
        try {
            TypedQuery<ServiceOrder> query = em.createQuery(
                    "SELECT DISTINCT o FROM ServiceOrder o "
                    + "JOIN o.customer c JOIN c.person p "
                    + "LEFT JOIN o.device d "
                    + "WHERE o.company = :pCompany AND "
                    + "(p.firstName LIKE :pSearch OR p.lastName LIKE :pSearch OR p.contactNo LIKE :pSearch OR "
                    + "p.email LIKE :pSearch OR d.brand LIKE :pSearch OR d.model LIKE :pSearch OR "
                    + "d.serialNumber LIKE :pSearch OR CAST(o.idServiceOrder AS string) LIKE :pSearch)", ServiceOrder.class);
            query.setParameter("pCompany", pCompany);
            query.setParameter("pSearch", "%" + pSearch + "%");
            listOrder = query.getResultList();
        } catch (Exception e) {
            System.err.println("Error searching orders: " + e.getMessage());
            e.printStackTrace();
        } finally {
            em.close();
        }
        return listOrder;
    }
    
    public boolean updateServiceOrderStatusDAO(ServiceOrder pOrderModel) {
        EntityManager em = JPAUtil.getEntityManager();
        boolean success = false;
        
        try {
            em.getTransaction().begin();

            // Reattach Company
            if (pOrderModel.getCompany() != null) {
                Company managedCompany = em.getReference(Company.class, pOrderModel.getCompany().getIdCompany());
                pOrderModel.setCompany(managedCompany);
            }

            // Reattach Customer, Customer's Company, and Person
            if (pOrderModel.getCustomer() != null) {
                Customer detachedCustomer = pOrderModel.getCustomer();
                
                if (detachedCustomer.getCompany() != null) {
                    Company managedCustomerCompany = em.getReference(Company.class, detachedCustomer.getCompany().getIdCompany());
                    detachedCustomer.setCompany(managedCustomerCompany);
                }
                
                if (detachedCustomer.getPerson() != null) {
                    Person managedPerson = em.getReference(Person.class, detachedCustomer.getPerson().getIdPerson());
                    detachedCustomer.setPerson(managedPerson);
                }
                
                Customer managedCustomer = em.getReference(Customer.class, detachedCustomer.getIdCustomer());
                pOrderModel.setCustomer(managedCustomer);
            }

            // Reattach Device
            if (pOrderModel.getDevice() != null) {
                Device managedDevice = em.getReference(Device.class, pOrderModel.getDevice().getIdDevice());
                pOrderModel.setDevice(managedDevice);
            }

            // Reattach Employee
            if (pOrderModel.getEmployee() != null) {
                Employee managedEmployee = em.getReference(Employee.class, pOrderModel.getEmployee().getIdEmployee());
                pOrderModel.setEmployee(managedEmployee);
            }

            // Merge and commit
            em.merge(pOrderModel);
            em.getTransaction().commit();
            success = true;
            
        } catch (Exception e) {
            System.err.println("Error updating order: " + e.getMessage());
            e.printStackTrace();
            em.getTransaction().rollback();
        } finally {
            em.close();
        }
        
        return success;
    }
    
    public long addServiceOrderDAO(ServiceOrder order,
            List<ServiceOrderFault> faults,
            List<ServiceOrderProdServ> prodServs,
            List<ServiceOrderPayment> payments,
            Deposit deposit,
            OrderNote orderNote) throws BusinessException {
        EntityManager em = JPAUtil.getEntityManager();
        long idOrderAdded = 0;
        
        try {
            em.getTransaction().begin();

            // Persist Device, Person, Customer
            Customer customer = order.getCustomer();
            Person person = customer.getPerson();
            Device device = order.getDevice();

            // Reattach Employee
            if (order.getEmployee() != null) {
                Employee managedEmployee = em.getReference(Employee.class, order.getEmployee().getIdEmployee());
                order.setEmployee(managedEmployee);
            }
            
            if (device != null && device.getIdDevice() == 0) {
                em.persist(device);
                em.flush();
            }
            if (person.getIdPerson() == 0) {
                em.persist(person);
                em.flush();
            }
            if (customer.getIdCustomer() == 0) {
                em.persist(customer);
                em.flush();
            } else {
                customer = em.find(Customer.class, customer.getIdCustomer());
            }
            order.setCustomer(customer);
            
            if (order.getCompany() != null) {
                Company managedCompany = em.find(Company.class, order.getCompany().getIdCompany());
                order.setCompany(managedCompany);
            }

            // Persist the main order (merge returns managed entity)
            ServiceOrder managedOrder = em.merge(order);
            em.flush();  // force insert and get ID
            idOrderAdded = managedOrder.getIdServiceOrder();

            // Persist faults
            if (faults != null) {
                for (ServiceOrderFault fault : faults) {
                    fault.setServiceOrder(managedOrder);
                    em.persist(fault);
                }
            }

            // Persist prodServs
            if (prodServs != null) {
                for (ServiceOrderProdServ prodServ : prodServs) {
                    prodServ.setServiceOrder(managedOrder);
                    em.persist(prodServ);
                }
            }

            // Persist deposit
            if (deposit != null) {
                deposit.setServiceOrder(managedOrder);

                // Reattach Employee
                if (deposit.getEmployee() != null) {
                    Employee managedEmployee = em.getReference(Employee.class, order.getEmployee().getIdEmployee());
                    deposit.setEmployee(managedEmployee);
                }
                
                em.persist(deposit);
            }

            // Persist payment
            if (payments != null) {
                for (ServiceOrderPayment payment : payments) {
                    payment.setServiceOrder(managedOrder);
                    em.persist(payment);
                }
            }

            // Persist order note
            if (orderNote != null) {
                orderNote.setServiceOrder(managedOrder);

                // Reattach Employee
                if (orderNote.getEmployee() != null) {
                    Employee managedEmployee = em.getReference(Employee.class, order.getEmployee().getIdEmployee());
                    orderNote.setEmployee(managedEmployee);
                }
                
                em.persist(orderNote);
            }
            
            em.getTransaction().commit();
            
            return idOrderAdded;
            
        } catch (Exception e) {
            e.printStackTrace();
            em.getTransaction().rollback();
            throw new BusinessException("Failed to add order: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }
    
    public boolean updateServiceOrderDAO(ServiceOrder order,
            List<ServiceOrderFault> faults,
            List<ServiceOrderProdServ> prodServs,
            List<ServiceOrderPayment> payments,
            Deposit deposit,
            OrderNote orderNote) throws BusinessException {
        
        EntityManager em = JPAUtil.getEntityManager();
        boolean success = false;
        
        try {
            em.getTransaction().begin();

            // Reattach Employee
            if (order.getEmployee() != null) {
                Employee managedEmployee = em.getReference(Employee.class, order.getEmployee().getIdEmployee());
                order.setEmployee(managedEmployee);
            }

            // Reattach Company
            if (order.getCompany() != null) {
                Company managedCompany = em.find(Company.class, order.getCompany().getIdCompany());
                order.setCompany(managedCompany);
            }

            // Merge customer & device if necessary
            Customer customer = order.getCustomer();
            Person person = customer.getPerson();
            Device device = order.getDevice();
            
            if (device != null) {
                if (device.getIdDevice() == 0) {
                    em.persist(device);
                    em.flush();
                } else {
                    device = em.merge(device);
                }
                order.setDevice(device);
            }
            
            if (person.getIdPerson() == 0) {
                em.persist(person);
                em.flush();
            } else {
                person = em.merge(person);
            }
            
            if (customer.getIdCustomer() == 0) {
                em.persist(customer);
                em.flush();
            } else {
                customer = em.merge(customer);
            }
            order.setCustomer(customer);

            // Merge order itself
            ServiceOrder managedOrder = em.merge(order);
            em.flush();

            // Remove old faults & prodServs before adding new ones
            em.createQuery("DELETE FROM ServiceOrderFault f WHERE f.serviceOrder.idServiceOrder = :id")
                    .setParameter("id", managedOrder.getIdServiceOrder())
                    .executeUpdate();
            em.createQuery("DELETE FROM ServiceOrderProdServ p WHERE p.serviceOrder.idServiceOrder = :id")
                    .setParameter("id", managedOrder.getIdServiceOrder())
                    .executeUpdate();

            // Persist faults
            if (faults != null) {
                for (ServiceOrderFault fault : faults) {
                    fault.setServiceOrder(managedOrder);
                    fault.setIdServiceOrderFault(0);
                    em.persist(fault);
                }
            }

            // Persist prodServs
            if (prodServs != null) {
                for (ServiceOrderProdServ prodServ : prodServs) {
                    prodServ.setServiceOrder(managedOrder);
                    prodServ.setIdServiceOrderProdServ(0);
                    em.persist(prodServ);
                }
            }

            // Persist deposit if any 
            if (deposit != null) {
                deposit.setServiceOrder(managedOrder);
                if (deposit.getEmployee() != null) {
                    Employee managedEmployee = em.getReference(Employee.class, deposit.getEmployee().getIdEmployee());
                    deposit.setEmployee(managedEmployee);
                }
                em.persist(deposit);
            }

            // Persist payment if any
            if (payments != null) {
                for (ServiceOrderPayment payment : payments) {
                    payment.setServiceOrder(managedOrder);
                    
                    em.persist(payment);
                }
            }

            // Persist order note if any
            if (orderNote != null) {
                orderNote.setServiceOrder(managedOrder);
                if (orderNote.getEmployee() != null) {
                    Employee managedEmployee = em.getReference(Employee.class, orderNote.getEmployee().getIdEmployee());
                    orderNote.setEmployee(managedEmployee);
                }
                em.persist(orderNote);
            }
            
            em.getTransaction().commit();
            success = true;
            
        } catch (Exception e) {
            e.printStackTrace();
            em.getTransaction().rollback();
            throw new BusinessException("Failed to update order: " + e.getMessage(), e);
        } finally {
            em.close();
        }
        return success;
    }
}