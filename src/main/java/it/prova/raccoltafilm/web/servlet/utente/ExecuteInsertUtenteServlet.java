package it.prova.raccoltafilm.web.servlet.utente;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.math.NumberUtils;

import it.prova.raccoltafilm.model.Ruolo;
import it.prova.raccoltafilm.model.Utente;
import it.prova.raccoltafilm.service.MyServiceFactory;
import it.prova.raccoltafilm.service.RuoloService;
import it.prova.raccoltafilm.service.UtenteService;
import it.prova.raccoltafilm.utility.UtilityForm;

/**
 * Servlet implementation class ExecuteInsertUtenteServlet
 */
@WebServlet("/admin/ExecuteInsertUtenteServlet")
public class ExecuteInsertUtenteServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	// injection del Service
		private UtenteService utenteService;
		private RuoloService ruoloService;
	
    public ExecuteInsertUtenteServlet() {
    	this.utenteService = MyServiceFactory.getUtenteServiceInstance();
    	this.ruoloService = MyServiceFactory.getRuoloServiceInstance();
    }
    
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// estraggo input
		String usernameParam = request.getParameter("username");
		String passwordParam = request.getParameter("password");
		String passwordDueParam = request.getParameter("passwordDue");
		String nomeParam = request.getParameter("nome");
		String cognomeParam = request.getParameter("cognome");
		String[] ruoliParam = request.getParameterValues("ruoli");
		
		Utente utenteInstance = new Utente(usernameParam, passwordParam, nomeParam, cognomeParam, new Date());
		Set <Ruolo> ruoliUtente= new HashSet<>();
		
		try {
			if(ruoliParam!=null) {
				for(String elementi : ruoliParam) {
					
					Ruolo ruoloInstance=null;
					
					if(NumberUtils.isCreatable(elementi)) {
						ruoloInstance=ruoloService.caricaSingoloElemento(Long.parseLong(elementi));
					}
					if(ruoloInstance!=null) {
						ruoliUtente.add(ruoloInstance);
					}
				}
			}
			utenteInstance.setRuoli(ruoliUtente);
			
			if(!UtilityForm.validateUtenteBean(utenteInstance) || !passwordParam.equals(passwordDueParam)) {
				request.setAttribute("insert_utente_attr", utenteInstance);
				request.setAttribute("ruoli_list_attribute", ruoloService.listAll());
				List<Long> idRuoli = new ArrayList<>();
				for(Ruolo elementi : ruoliUtente) {
					idRuoli.add(elementi.getId());
				}
				request.setAttribute("ruoli_back", idRuoli);
				request.setAttribute("errorMessage", "Attenzione sono presenti errori di validazione.");
				request.getRequestDispatcher("/utente/insert.jsp").forward(request, response);
				return;
			}
			
			utenteService.inserisciNuovo(utenteInstance);
		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("errorMessage", "Attenzione si è verificato un errore.");
			request.getRequestDispatcher("/utente/insert.jsp").forward(request, response);
			return;
		}

		// andiamo ai risultati
		// uso il sendRedirect con parametro per evitare il problema del double save on
		// refresh
		response.sendRedirect("/raccoltafilm/admin/ExecuteListUtenteServlet?operationResult=SUCCESS");
		
	}
}
