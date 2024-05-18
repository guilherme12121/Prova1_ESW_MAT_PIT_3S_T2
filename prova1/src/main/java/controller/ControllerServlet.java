package controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import db.Db;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.AulaDto;

@WebServlet(urlPatterns = { "/prova1", "/nova", "/edit" })
public class ControllerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public ControllerServlet() {
		super();
	}
	
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Db db = Db.getInstance();
		PrintWriter out = response.getWriter();
		String action = request.getServletPath();
		if (action.equals("/nova")) {
			RequestDispatcher rd = request.getRequestDispatcher("nova.jsp");
			rd.forward(request, response);
		} else if (action.equals("/edit")) {
			HttpSession session = request.getSession();
			String id = request.getParameter("id");
			AulaDto dto = db.findById(id);
			session.setAttribute("aulaDto", dto);
			RequestDispatcher rd = request.getRequestDispatcher("edit.jsp");
			rd.forward(request, response);
		}else {
		
			out.print(db.findAll());
		}
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        String op = request.getParameter("op");
        Db db = Db.getInstance();
        switch (op) {
            case "START_SESSION":
                this.poeDadosNaSessao(session);
                break;
            case "RESET":
                db.reset();
                break;
            case "CREATE":   	
                this.create(request, db);
                break;
            case "READ":
                this.getAula(request, response, db);
                break;
            case "UPDATE":
                this.update(request, db);
                break;
            case "DELETE":
                this.delete(request, db);
                break;
        }
    }
	
	
	private void poeDadosNaSessao(HttpSession session) throws ServletException {
        
        Db db = Db.getInstance();
        ArrayList<AulaDto> lista;
		lista = db.findAll();
		session.setAttribute("lista", lista);
    }

	
	private void create(HttpServletRequest request, Db db) {
        String codDisciplina = request.getParameter("codDisciplina");
        String assunto = request.getParameter("assunto");
        String duracao = request.getParameter("duracao");
        String data = request.getParameter("data");
        String horario = request.getParameter("horario");

        AulaDto dto = new AulaDto();
        dto.codDisciplina = codDisciplina;
        dto.assunto = assunto;
        dto.duracao = duracao;
        dto.data = data;
        dto.horario = horario;
        

        db.create(dto);
    }

	private void delete(HttpServletRequest request, Db db) {
        String id = request.getParameter("id");
        db.delete(id);
    }

	
	private void getAula(HttpServletRequest request, HttpServletResponse response, Db db) throws IOException {
	    String id = request.getParameter("id");
	    AulaDto dto = db.findById(id);
	    response.setContentType("application/json");
	    StringBuilder stb = new StringBuilder();
	    stb.append("{\"id\": \"").append(id).append("\",")
	       .append("\"disciplina\": \"").append(dto.disciplina).append("\",")
	       .append("\"codDisciplina\": \"").append(dto.codDisciplina).append("\",")
	       .append("\"assunto\": \"").append(dto.assunto).append("\",")
	       .append("\"duracao\": \"").append(dto.duracao).append("\",")
	       .append("\"data\": \"").append(dto.data).append("\",")
	       .append("\"horario\": \"").append(dto.horario).append("\"}");
	    String json = stb.toString();
	    try {
	        response.getWriter().write(json);
	    } catch (IOException e) {
	        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
	        response.getWriter().write("Ocorreu um erro ao processar sua solicitação: " + e.getMessage());
	    }
	}
	
	
	private void update(HttpServletRequest request, Db db) {
        String id = request.getParameter("id");
        String codDisciplina = request.getParameter("codDisciplina");
        String assunto = request.getParameter("assunto");
        String duracao = request.getParameter("duracao");
        String data = request.getParameter("data");
        String horario = request.getParameter("horario");

        AulaDto dto = new AulaDto();
        dto.id = id;
        dto.codDisciplina = codDisciplina;
        dto.assunto = assunto;
        dto.duracao = duracao;
        dto.data = data;
        dto.horario = horario;

        db.update(dto);
	}

}
