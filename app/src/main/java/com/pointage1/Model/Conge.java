package com.pointage1.Model;

import java.io.Serializable;

public class Conge implements Serializable {

    private String id_Conge;
    //private String nom;
    //private String prenom;
    private String id_Utilisateur;
    private String cause;
    //private String nbcredit;
    //private String nbdemande;
    private String dateDeDébut;
    private String dateDeFin;
    private String dateDeDemande;
    private String statusConge;

    /*public Conge(String id,String nom, String prenom, String cause, String nbcredit, String nbdemande, String statusConge) {
        this.id=id;
        this.nom = nom;
        this.prenom = prenom;
        this.cause = cause;
        this.nbcredit = nbcredit;
        this.nbdemande = nbdemande;
        this.statusConge = statusConge;
    }*/

    /*public Conge(String id, String nom, String prenom, String cause, String dateDeDébut, String dateDeFin, String statusConge) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.cause = cause;
        this.dateDeDébut = dateDeDébut;
        this.dateDeFin = dateDeFin;
        this.statusConge = statusConge;
    }*/

    public Conge() {
    }

    public Conge(String id_Conge, String id_Utilisateur, String cause, String dateDeDébut, String dateDeFin,String dateDeDemande, String statusConge) {
        this.id_Conge = id_Conge;
        this.id_Utilisateur = id_Utilisateur;
        this.cause = cause;
        this.dateDeDébut = dateDeDébut;
        this.dateDeFin = dateDeFin;
        this.dateDeDemande = dateDeDemande;
        this.statusConge = statusConge;
    }

    public String getId_Conge() {
        return id_Conge;
    }

    public void setId_Conge(String id_Conge) {
        this.id_Conge = id_Conge;
    }

    public String getId_Utilisateur() {
        return id_Utilisateur;
    }

    public void setId_Utilisateur(String id_Utilisateur) {
        this.id_Utilisateur = id_Utilisateur;
    }

    public String getCause() {
        return cause;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }

    public String getDateDeDébut() {
        return dateDeDébut;
    }

    public void setDateDeDébut(String dateDeDébut) {
        this.dateDeDébut = dateDeDébut;
    }

    public String getDateDeFin() {
        return dateDeFin;
    }

    public void setDateDeFin(String dateDeFin) {
        this.dateDeFin = dateDeFin;
    }


    public String getDateDeDemande() {
        return dateDeDemande;
    }

    public void setDateDeDemande(String dateDeDemande) {
        this.dateDeDemande = dateDeDemande;
    }

    public String getStatusConge() {
        return statusConge;
    }

    public void setStatusConge(String statusConge) {
        this.statusConge = statusConge;
    }
}
