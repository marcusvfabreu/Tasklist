/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.supero.view.back;

import br.com.supero.core.bean.TaskBean;
import br.com.supero.core.dao.TaskBeanJpaController;
import br.com.supero.core.dao.exceptions.NonexistentEntityException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author tecbmmvf
 */
@ManagedBean(name = "inicio")
@ApplicationScoped
public class ProjetoSuperoBack {

    private TaskBean taskBean;
    
    @EJB
    private TaskBeanJpaController taskbeanjpacontroller;
    EntityManagerFactory emf;

    private List<TaskBean> listaAtividades;

    public List<TaskBean> getListaAtividades() {
        return listaAtividades;
    }

    @PostConstruct
    public void inicializar() {
        taskBean = new TaskBean();
        listaAtividades = new ArrayList<TaskBean>();
        emf = Persistence.createEntityManagerFactory("projeto");
        taskbeanjpacontroller = new TaskBeanJpaController(emf);
        limpar();
    }

    public void setListaAtividades(List<TaskBean> listaAtividades) {
        this.listaAtividades = listaAtividades;
    }

    public TaskBean getTaskBean() {
        return taskBean;
    }

    public void setTaskBean(TaskBean taskBean) {
        this.taskBean = taskBean;
    }

    public void salvar() {
       
        if (this.taskBean.getStatus() == -1){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Atenção!", "Selecione um Estado válido."));
        }
        if (this.taskBean.getTitulo() == null || this.taskBean.getTitulo().isEmpty()){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Atenção!", "Digite o título da Atividade."));
        }
        if (this.taskBean.getTitulo() == null || this.taskBean.getDescricao().isEmpty()){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Atenção!", "Digite a descrição da Atividade."));
        }
        
        if (this.taskBean.getIdTask() == 0) {
            taskbeanjpacontroller.create(taskBean);
        } else {
            try {
                taskbeanjpacontroller.edit(taskBean);
            } catch (Exception ex) {
                Logger.getLogger(ProjetoSuperoBack.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        limpar();
    }

    public void deletar(TaskBean ativ) {
        try {
            taskbeanjpacontroller.destroy(ativ.getIdTask());
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(ProjetoSuperoBack.class.getName()).log(Level.SEVERE, null, ex);
        }
        limpar();
    }

    public void editar(TaskBean ativ) {

        this.taskBean = ativ;
    }

    public void limpar() {
        this.taskBean = new TaskBean();
        listaAtividades = taskbeanjpacontroller.findTaskBeanEntities();
    }
}
