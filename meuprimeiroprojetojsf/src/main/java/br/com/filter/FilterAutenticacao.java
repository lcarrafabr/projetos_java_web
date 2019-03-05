package br.com.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import br.com.jpautil.JPAUtil;

/**
 * Este filtro verifica todas as requisições e verifica se o usuário está logado
 * Se o usuário não estiver logado, será ancaminhado para a pagina de login*/

@WebFilter(urlPatterns={"/*"})
public class FilterAutenticacao implements Filter {

	@Override
	public void destroy() {		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		HttpServletRequest req = (HttpServletRequest) request;
		HttpSession session = req.getSession();
		
		String usuarioLogado = (String) session.getAttribute("usuarioLogado");
		
		String url = req.getServletPath();
		
		if(!url.equalsIgnoreCase("index.jsf") && usuarioLogado == null || 
			(usuarioLogado != null && usuarioLogado.trim().isEmpty())) {
			RequestDispatcher dispacher  = request.getRequestDispatcher("/index.jsf");
			dispacher.forward(request, response);
			return;
		}
		
		
		
		//Executa ações do request e do response
		chain.doFilter(request, response);
		
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		JPAUtil.getEntityManager();
		 
	}

}
