package it.prova.raccoltafilm.web.servlet.film;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.math.NumberUtils;

import it.prova.raccoltafilm.model.Film;
import it.prova.raccoltafilm.service.FilmService;
import it.prova.raccoltafilm.service.MyServiceFactory;
import it.prova.raccoltafilm.utility.UtilityForm;

/**
 * Servlet implementation class ExecuteUpdateFilmServlet
 */
@WebServlet("/ExecuteUpdateFilmServlet")
public class ExecuteUpdateFilmServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	// injection del Service
	private FilmService filmService;

	public ExecuteUpdateFilmServlet() {
		this.filmService = MyServiceFactory.getFilmServiceInstance();
	}
       
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String idFilm= request.getParameter("idFilm");
		// estraggo input
		String titoloParam = request.getParameter("titolo");
		String genereParam = request.getParameter("genere");
		String dataPubblicazioneParam = request.getParameter("dataPubblicazione");
		String minutiDurataParam = request.getParameter("minutiDurata");
		String registaIdParam = request.getParameter("regista.id");
		
		if (!NumberUtils.isCreatable(idFilm)) {
			// qui ci andrebbe un messaggio nei file di log costruito ad hoc se fosse attivo
			request.setAttribute("errorMessage", "Attenzione si è verificato un errore.");
			request.getRequestDispatcher("/index.jsp").forward(request, response);
			return;
		}
		
		// preparo un bean (che mi serve sia per tornare in pagina
		// che per inserire) e faccio il binding dei parametri
		Film filmInstance = UtilityForm.createFilmFromParams(titoloParam, genereParam, minutiDurataParam,
				dataPubblicazioneParam, registaIdParam);
		
		filmInstance.setId(Long.parseLong(idFilm));
		
		// se la validazione non risulta ok
		if (!UtilityForm.validateFilmBean(filmInstance)) {
			try {
				request.setAttribute("filmDaInviareAPaginaUpdate", filmService.caricaSingoloElementoEager(Long.parseLong(idFilm)));
			} catch (Exception e) {
				e.printStackTrace();
			}
			request.setAttribute("errorMessage", "Attenzione sono presenti errori di validazione");
			request.getRequestDispatcher("/film/update.jsp").forward(request, response);
			return;
		}
		
		try {
			MyServiceFactory.getFilmServiceInstance().aggiorna(filmInstance);
			request.setAttribute("film_list_attribute", filmService.listAllElements());
		}catch(Exception e) {
			//qui ci andrebbe un messaggio nei file di log costruito ad hoc se fosse attivo
			e.printStackTrace();
			request.setAttribute("errorMessage", "Attenzione si è verificato un errore.");
			request.getRequestDispatcher("/index.jsp").forward(request, response);
			return;
		}
		
		// andiamo ai risultati
		request.getRequestDispatcher("/film/list.jsp").forward(request, response);
		
	}

}
