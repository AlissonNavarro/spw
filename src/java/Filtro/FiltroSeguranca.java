package Filtro;

import Usuario.UsuarioBean;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class FiltroSeguranca implements Filter {

    public void init(FilterConfig filterConfig) throws ServletException {
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException,
            ServletException {

        HttpSession session = ((HttpServletRequest) request).getSession();

        UsuarioBean usuario = (UsuarioBean) session.getAttribute("usuarioBean");
        String nomeAplicacao = session.getServletContext().getContextPath();
        boolean teste = true;
        if ((usuario == null)) {
            session.setAttribute("msg", "Você não está logado no sistema!");
            ((HttpServletResponse) response).sendRedirect(nomeAplicacao + "/faces/login.jsp");
            teste = false;
        } else if (!usuario.getEhAdministrador()) {
            session.setAttribute("msg", "Você não está logado no sistema!");
            ((HttpServletResponse) response).sendRedirect(nomeAplicacao + "/faces/login.jsp");
            teste = false;
        }

        if (teste) {
            filterChain.doFilter(request, response);
        }
    }

    public void destroy() {
        //     throw new UnsupportedOperationException("Not supported yet.");
    }
    /*
     * session shouldn’t be checked for some pages. For example: for timeout page..
     * Since we’re redirecting to timeout page from this filter,
     * if we don’t disable session control for it, filter will again redirect to it
     * and this will be result with an infinite loop…
     */
}
