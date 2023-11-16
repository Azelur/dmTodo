package ecole.ap.todolist;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Todo {
    @Id
    private Long id;
    private String contenu;
    private String datecreation;
    private Boolean statut;


    public Todo() {
    }

    public Todo(Long id, String contenu, String datecreation, Boolean statut) {
        this.id = id;
        this.contenu = contenu;
        this.datecreation = datecreation;
        this.statut = statut;
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContenu() {
        return contenu;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }

    public String getDatecreation() {
        return datecreation;
    }

    public void setDatecreation(String datecreation) {
        this.datecreation = datecreation;
    }

    public Boolean getStatut() {
        return statut;
    }

    public void setStatut(Boolean statut) {
        this.statut = statut;
    }


}
