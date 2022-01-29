package org.sid.compteservice.service;

import org.sid.compteservice.entities.Compte;
import org.sid.compteservice.repository.CompteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CompteServiceImpl implements CompteService {
    private CompteRepository compteRepository;

    public CompteServiceImpl(CompteRepository compteRepository) {
        this.compteRepository = compteRepository;
    }


    @Override
    public Compte getCompte(Long id) {
        return compteRepository.findById(id).get() ;
    }

    @Override
    public Compte addCompte(Compte compte) {

        return compteRepository.save(compte);
    }

    @Override
    public Compte virement(Long id, double montant) {
        Compte compte=compteRepository.findById(id).get();
        compte.setSolde(compte.getSolde()+montant);
        return compteRepository.save(compte);
    }

    @Override
    public Compte retrir(Long id, double montant) {
        Compte compte=compteRepository.findById(id).get();
        compte.setSolde(compte.getSolde()-montant);
        return compteRepository.save(compte);
    }

    @Override
    public List<Compte> allCompte() {

        return compteRepository.findAll();
    }

    @Override
    public void delete(Long id) {
        compteRepository.deleteById(id);
    }
}
