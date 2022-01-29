package org.sid.compteservice.service;

import org.sid.compteservice.entities.Compte;

import java.util.List;

public interface CompteService {
    Compte getCompte(Long id);
    Compte addCompte(Compte compte);
    Compte virement(Long id,double montant);
    Compte retrir(Long id,double montant);
    List<Compte> allCompte();
    void delete(Long id);
}
