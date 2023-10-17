package com.carlos.vscodejava.filter;

import java.io.IOException;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.carlos.vscodejava.user.IUserRepository;

import at.favre.lib.crypto.bcrypt.BCrypt;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class FilterTaskAuth extends OncePerRequestFilter {

    @Autowired
    private IUserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        var servletPath = request.getServletPath();
        if (servletPath.equals("/task/")) {
            // Pegar Autenticação(user, password)

            var authorization = request.getHeader("Authorization");

            var authEncoded = authorization.substring("Basic".length()).trim();

            byte[] authDecode = Base64.getDecoder().decode(authEncoded);
            var authString = new String(authDecode);
            String[] credentials = authString.split(":");
            String userName = credentials[0];
            String userPassword = credentials[1];

            /*
             * System.out.println("Authorization: " + authEncoded);
             * System.out.println("Decoder: "+authDecode);
             * System.out.println("usuario: "+userName);
             * System.out.println("Senha: "+userPassword);
             */

            // validar user
            var user = this.userRepository.findByUsernome(userName);
            if (user == null) {
                response.sendError(401, "Usuario não cadastrado!!!!");
            } else {
                // validar password
                var passwordVerigy = BCrypt.verifyer().verify(userPassword.toCharArray(), user.getPassword());
                if (passwordVerigy.verified) {
                    // segui viagem
                    request.setAttribute("idUser",user.getId());
                    filterChain.doFilter(request, response);
                    
                } else {
                    response.sendError(401);
                }
                

            }

        }else{
            filterChain.doFilter(request, response);
            
        }

    }

    // era co implements Filer
    /*
     * @Override
     * public void doFilter(ServletRequest request, ServletResponse response,
     * FilterChain chain)
     * throws IOException, ServletException {
     * 
     * // Executar alguma ação
     * System.out.println("Chegou no filtro!!  ");
     * chain.doFilter(request, response);
     * 
     * }
     */

}
