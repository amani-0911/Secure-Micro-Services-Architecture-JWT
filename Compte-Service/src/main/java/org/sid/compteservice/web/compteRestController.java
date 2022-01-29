package org.sid.compteservice.web;

import org.sid.compteservice.entities.Compte;
import org.sid.compteservice.service.CompteService;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class compteRestController {
    private CompteService compteService;

    public compteRestController(CompteService compteService) {
        this.compteService = compteService;
    }
    @GetMapping(path = "/comptes/{id}")
    @PostAuthorize("hasAuthority('USER')")
    public Compte getCompte(@PathVariable Long id){
        return compteService.getCompte(id);
    }
    @PostMapping(path = "/comptes")
    @PostAuthorize("hasAuthority('ADMIN')")
    public Compte save(@RequestBody Compte cp){
        return compteService.addCompte(cp);
    }

    @GetMapping(path="/comptes")
    @PostAuthorize("hasAuthority('ADMIN')")
    public List<Compte> allComptes(){
        return compteService.allCompte();
    }
    @PutMapping(path = "/comptes/ver/{id}")
    @PostAuthorize("hasAuthority('ADMIN')")
    public Compte verm(@PathVariable Long id,@RequestParam double montant){

        return compteService.virement(id,montant);
    }
    @PutMapping(path = "/comptes/ret/{id}")
    @PostAuthorize("hasAuthority('ADMIN')")
    public Compte retir(@PathVariable Long id,@RequestParam double montant){

        return compteService.retrir(id,montant);
    }
    @DeleteMapping(path = "/comptes/{id}")
    @PostAuthorize("hasAuthority('ADMIN')")
    public void delete(@PathVariable Long id){
        compteService.delete(id);
    }
}
