package it.prova.raccoltafilm.web.servlet.film;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.math.NumberUtils;

import it.prova.raccoltafilm.service.FilmService;
import it.prova.raccoltafilm.service.MyServiceFactory;
import it.prova.raccoltafilm.service.RegistaService;

/**
 * Servlet implementation class PrepareUpdateFilmServlet
 */
@WebServlet("/PrepareUpdateFilmServlet")
public class PrepareUpdateFilmServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	// injection del Service
	private FilmService filmService;
	
	private RegistaService registaService;

	public PrepareUpdateFilmServlet() {
		this.filmService = MyServiceFactory.getFilmServiceInstance();
		this.registaService=MyServiceFactory.getRegistaServiceInstance();
	}
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String parametroFilmDaModificare=request.getParameter("idFilm");
		
		if (!NumberUtils.isCreatable(parametroFilmDaModificare)) {
			// qui ci andrebbe un messaggio nei file di log costruito ad hoc se fosse attivo
			request.setAttribute("errorMessage", "Attenzione si è verificato un errore.");
			request.getRequestDispatcher("/index.jsp").forward(request, response);
			return;
		}
		
		try {
			request.setAttribute("filmDaInviareAPaginaUpdate", filmService.caricaSingoloElementoEager(Long.parseLong(parametroFilmDaModificare)));
			request.setAttribute("registi_list_attribute", registaService.listAllElements());
		} catch (Exception e) {
			// qui ci andrebbe un messaggio nei file di log costruito ad hoc se fosse attivo
			e.printStackTrace();
			request.setAttribute("errorMessage", "Attenzione si è verificato un errore.");
			request.getRequestDispatcher("/index.jsp").forward(request, response);
			return;
		}
		request.getRequestDispatcher("/film/update.jsp").forward(request, response);
	}
}
