/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.supero.core.bean;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author tecbmmvf
 */
@Entity
@Table(name = "atividade")
public class TaskBean implements Serializable{
    
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idTask;
    
    public void setIdTask(Long idTask) {
        this.idTask = idTask;
    }
    
    public Long getIdTask() {
        return idTask;
    }
    
    @Column (name = "titulo")
    private String titulo;
    
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
    
    @Column (name = "descricao")
    private String descricao;
    
    @Column (name = "status")
    private Integer status;
    
    public TaskBean (){
        this.idTask = 0L;
    }

    

    

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    

    public String getTitulo() {
        return titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public Integer getStatus() {
        return status;
    }
    
    public String getDescStatus(){
        switch (this.getStatus()){
            case 0:
                return "Pendente";
            case 1:
                return "Concluído";
            default:
                return "Sem Estado";
        }
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 67 * hash + Objects.hashCode(this.idTask);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final TaskBean other = (TaskBean) obj;
        if (!Objects.equals(this.idTask, other.idTask)) {
            return false;
        }
        return true;
    }
           
}
