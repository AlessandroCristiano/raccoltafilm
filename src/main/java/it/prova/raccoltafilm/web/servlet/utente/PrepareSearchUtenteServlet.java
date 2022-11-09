package it.prova.raccoltafilm.web.servlet.utente;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import it.prova.raccoltafilm.service.MyServiceFactory;
import it.prova.raccoltafilm.service.RegistaService;
import it.prova.raccoltafilm.service.UtenteService;

/**
 * Servlet implementation class PrepareSearchUtenteServlet
 */
@WebServlet("/admin/PrepareSearchUtenteServlet")
public class PrepareSearchUtenteServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	// injection del Service
	private UtenteService utenteService;

	public PrepareSearchUtenteServlet() {
		this.utenteService = MyServiceFactory.getUtenteServiceInstance();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getRequestDispatcher("/utente/search.jsp").forward(request, response);
	}

}
