package com.pchouse.pchousestoremvn.dao;

import com.pchouse.pchousestoremvn.models.Deposit;
import com.pchouse.pchousestoremvn.models.Employee;
import com.pchouse.pchousestoremvn.models.Sale;
import com.pchouse.pchousestoremvn.models.SalePayment;
import com.pchouse.pchousestoremvn.models.ServiceOrder;
import com.pchouse.pchousestoremvn.models.ServiceOrderPayment;
import com.pchouse.pchousestoremvn.util.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class DepositDAO {

    public long addDepositDAO(Deposit deposit) {
        EntityManager em = JPAUtil.getEntityManager();
        long idDepositAdded = 0;
        try {
            em.getTransaction().begin();

            // Attach associated ServiceOrder
            if (deposit.getServiceOrder() != null) {
                ServiceOrder managedOrder = em.find(
                        ServiceOrder.class,
                        deposit.getServiceOrder().getIdServiceOrder()
                );
                deposit.setServiceOrder(managedOrder);
            }
            
            // Attach associated Sale
            if (deposit.getSale()!= null) {
                Sale managedSale = em.find(
                        Sale.class,
                        deposit.getSale().getIdSale()
                );
                deposit.setSale(managedSale);
            }

            // Attach associated ServiceOrderPayment
            if (deposit.getServiceOrderPayment() != null) {
                ServiceOrderPayment managedOrderPayment = em.find(
                        ServiceOrderPayment.class,
                        deposit.getServiceOrderPayment().getIdOrderPayment()
                );
                deposit.setServiceOrderPayment(managedOrderPayment);
            }
            
            // Attach associated SalePayment
            if (deposit.getSalePayment() != null) {
                SalePayment managedSalePayment = em.find(
                        SalePayment.class,
                        deposit.getSalePayment().getIdSalePayment()
                );
                deposit.setSalePayment(managedSalePayment);
            }

            // Attach associated Employee
            if (deposit.getEmployee() != null) {
                Employee managedEmployee = em.find(
                        Employee.class,
                        deposit.getEmployee().getIdEmployee()
                );
                deposit.setEmployee(managedEmployee);
            }

            em.persist(deposit);
            em.getTransaction().commit();
            idDepositAdded = deposit.getIdDeposit();
        } catch (Exception e) {
            e.printStackTrace();
            em.getTransaction().rollback();
        } finally {
            em.close();
        }
        return idDepositAdded;
    }

    public List<Deposit> getOrderDepositDAO(ServiceOrder pOrder) {
        EntityManager em = JPAUtil.getEntityManager();
        List<Deposit> orderDeposits = null;
        try {
            TypedQuery<Deposit> query = em.createQuery(
                    "SELECT d FROM Deposit d WHERE d.serviceOrder = :pOrder", Deposit.class);
            query.setParameter("pOrder", pOrder);
            orderDeposits = query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.close();
        }
        return orderDeposits;
    }
    
     public List<Deposit> getSaleDepositDAO(Sale pSale) {
        EntityManager em = JPAUtil.getEntityManager();
        List<Deposit> orderDeposits = null;
        try {
            TypedQuery<Deposit> query = em.createQuery(
                    "SELECT d FROM Deposit d WHERE d.sale = :pSale", Deposit.class);
            query.setParameter("pSale", pSale);
            orderDeposits = query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.close();
        }
        return orderDeposits;
    }
}