package org.sid.authservice.web;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.sid.authservice.Utils.JWTUtil;
import org.sid.authservice.entities.AppRole;
import org.sid.authservice.entities.AppUser;
import org.sid.authservice.service.AccountService;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Principal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class AccountRestController {
    private AccountService accountService;

    public AccountRestController(AccountService accountService) {
        this.accountService = accountService;
    }
    @GetMapping(path="/users")
    @PostAuthorize("hasAuthority('USER')")
    public List<AppUser> appUsers(){
          return accountService.listUsers();
    }

    @PostMapping(path="/users")
    @PostAuthorize("hasAuthority('ADMIN')")
    public AppUser saveUser(@RequestBody AppUser appUser){
        return accountService.addNewUser(appUser);
    }
    @PostMapping(path="/roles")
    public AppRole saveRole(@RequestBody AppRole appRole){
        return accountService.addNewRole(appRole);
    }
    @PostMapping(path="/addRoleToUser")
    public void addRoleToUser(@RequestBody RoleUserForm roleUserForm){
         accountService.addRoleToUser(roleUserForm.getUsername(),roleUserForm.getRoleName());
    }

    //methode qui refrech le access token
    @GetMapping(path = "/refreshToken")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws Exception{
        String authToken=request.getHeader("Authorization");
        if(authToken !=null && authToken.startsWith("Bearer ")){
            try{
                String jwt=authToken.substring(7);
                Algorithm algo=Algorithm.HMAC256(JWTUtil.SECRET);
                JWTVerifier jwtVerifier= JWT.require(algo).build();
                DecodedJWT decodedJWT=jwtVerifier.verify(jwt);
                String username=decodedJWT.getSubject();
                AppUser appUser=accountService.loadUserByUsername(username);
                String jwtAccessToken= JWT.create()
                        .withSubject(appUser.getUsername())
                        .withExpiresAt(new Date(System.currentTimeMillis()+1*60*1000))
                        .withIssuer(request.getRequestURL().toString())
                        .withClaim("roles",appUser.getRoles().stream().map(r->r.getRoleName()).collect(Collectors.toList()))
                        .sign(algo);
                Map<String,String> Idtoken=new HashMap<>();
                Idtoken.put("access-token",jwtAccessToken);
                Idtoken.put("refresh-token",jwt);

                response.setContentType("application/json");

                new ObjectMapper().writeValue(response.getOutputStream(),Idtoken);
            }catch(Exception e){
               throw e;
                  }
        }else {
            throw new RuntimeException("refrsh token required");
        }
    }
    @GetMapping(path="/profile")
    public AppUser profile(Principal principal){
        return accountService.loadUserByUsername(principal.getName());

    }

}
@Data
class RoleUserForm{
    private String username;
    private String roleName;
}
