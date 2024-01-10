package com.pointage1.Model;

import java.io.Serializable;

public class Utilisateur implements Serializable{


    private String id;
    private String email;
    private String nom;
    private String prenom;
    private String cin;
    private String matricule;
    private String motdepasse;
    private String telephone;
    private String image;
    private String cover;
    private String date_naissance;
    private String date_inscription;
    private String role;
    //private String etas;


    /*public Utilisateur(String id, String email, String nom, String prenom, String cin, String matricule, String motdepasse, String telephone, String image, String cover, String date_naissance, String date_inscription) {
    }*/

    public Utilisateur() {
    }

    //public Utilisateur(String id, String email, String nom, String prenom, String cin, String matricule, String motdepasse, String telephone, String image, String cover, String date_naissance, String date_inscription, String etas) {
    public Utilisateur(String id, String email, String nom, String prenom, String cin, String matricule, String motdepasse, String telephone, String image, String cover, String date_naissance, String date_inscription) {
        this.id = id;
        this.email = email;
        this.nom = nom;
        this.prenom = prenom;
        this.cin = cin;
        this.matricule = matricule;
        this.motdepasse = motdepasse;
        this.telephone = telephone;
        this.image = image;
        this.cover = cover;
        this.date_naissance = date_naissance;
        this.date_inscription = date_inscription;
        //this.etas= etas;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }


    public String getCin() {
        return cin;
    }

    public void setCin(String cin) {
        this.cin = cin;
    }


    public String getMatricule() {
        return matricule;
    }

    public void setMatricule(String matricule) {
        this.matricule = matricule;
    }

    public String getMotdepasse() {
        return motdepasse;
    }

    public void setMotdepasse(String motdepasse) {
        this.motdepasse = motdepasse;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getDate_naissance() {
        return date_naissance;
    }

    public void setDate_naissance(String date_naissance) {
        this.date_naissance = date_naissance;
    }

    public String getDate_inscription() {
        return date_inscription;
    }

    public void setDate_inscription(String date_inscription) {
        this.date_inscription = date_inscription;
    }

    /*public String getEtas() {
        return etas;
    }

    public void setEtas(String etas) {
        this.etas = etas;
    }*/

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
