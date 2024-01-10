package com.pointage1.Model;

public class Admin extends Utilisateur {
    public Admin(String id, String email, String nom, String prenom, String cin, String matricule, String motdepasse, String telephone, String image, String cover, String date_naissance, String date_inscription) {
        super(id,  email,  nom, prenom, cin,matricule,  motdepasse, telephone, image,cover,date_naissance,date_inscription);
        setRole("Admin");
    }
}

