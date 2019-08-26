/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.supero.core.dao;

import br.com.supero.core.bean.TaskBean;
import br.com.supero.core.dao.exceptions.NonexistentEntityException;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author tecbmmvf
 */
public class TaskBeanJpaController implements Serializable {

    public TaskBeanJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(TaskBean taskBean) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(taskBean);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(TaskBean taskBean) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            taskBean = em.merge(taskBean);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = taskBean.getIdTask();
                if (findTaskBean(id) == null) {
                    throw new NonexistentEntityException("The taskBean with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Long id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            TaskBean taskBean;
            try {
                taskBean = em.getReference(TaskBean.class, id);
                taskBean.getIdTask();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The taskBean with id " + id + " no longer exists.", enfe);
            }
            em.remove(taskBean);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<TaskBean> findTaskBeanEntities() {
        return findTaskBeanEntities(true, -1, -1);
    }

    public List<TaskBean> findTaskBeanEntities(int maxResults, int firstResult) {
        return findTaskBeanEntities(false, maxResults, firstResult);
    }

    private List<TaskBean> findTaskBeanEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(TaskBean.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public TaskBean findTaskBean(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(TaskBean.class, id);
        } finally {
            em.close();
        }
    }

    public int getTaskBeanCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TaskBean> rt = cq.from(TaskBean.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
