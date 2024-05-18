<%@page import="java.time.LocalDate"%>
<%@page import="java.time.format.DateTimeFormatter"%>
<%@page import="model.AulaDto"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="pt-br">

<head>
  <meta charset="UTF-8">
  <title>Prova 1</title>
  <link rel="stylesheet" href="style.css">
</head>

<body>
<%

  AulaDto aulaDto = (AulaDto) session.getAttribute("aulaDto");
  if (aulaDto == null) {

      response.sendRedirect("paginaDeErro.jsp");
      return;
  }
  DateTimeFormatter inputDate = DateTimeFormatter.ofPattern("dd/MM/yyyy");
  DateTimeFormatter outputDate = DateTimeFormatter.ofPattern("yyyy-MM-dd");
  LocalDate date = LocalDate.parse(aulaDto.data, inputDate);
%>
<header class="container-cabecalho">
  <h3>Editando: aula de <span id="nome-disciplina"><%= aulaDto.disciplina %></span></h3>
</header>
<nav class="container-nav">
  <div class="btn-nav" onclick="enviarEdit()">ENVIAR</div>
  <div class="btn-nav" onclick="cancelarNovaAula()">CANCELAR</div> 
</nav>

<div class="container-geral">
  <div class="container-aula-edit" id="msg-id" hidden="hidden">
    <div class="texto">
      Erro ao tentar registrar dados
    </div>
  </div>
  <div class="container-aula-edit">
    <input hidden="hidden" id="register-id" value="<%= aulaDto.id %>" />
    <div class="container-linha1">
      <div class="info">Data: <input id="data-id" type="date" class="inp-data" value="<%= date.format(outputDate) %>"></div> 
      <div class="info">Horário: <input id="hora-id" type="text" class="inp-hora" value="<%= aulaDto.horario %>"></div> 
      <div class="info">Duração: <input id="dur-id" type="number" class="inp-dur" value="<%= aulaDto.duracao %>"></div> 
    </div>
    <div class="container-linha2">
      <div class="info">Disciplina:
        <select name="" id="disc-id" class="inp-disc">
          <option value="1" <%= aulaDto.codDisciplina.equals("1") ? "selected" : "" %>>CÁLCULO</option> 
          <option value="2" <%= aulaDto.codDisciplina.equals("2") ? "selected" : "" %>>LÓGICA</option> 
          <option value="3" <%= aulaDto.codDisciplina.equals("3") ? "selected" : "" %>>GEOMETRIA</option> 
          <option value="4" <%= aulaDto.codDisciplina.equals("4") ? "selected" : "" %>>FÍSICA</option> 
          <option value="5" <%= aulaDto.codDisciplina.equals("5") ? "selected" : "" %>>COMPILADORES</option> 
        </select>
      </div>
      <div class="info">Assunto: <input id="ass-id" type="text" class="inp-ass" value="<%= aulaDto.assunto %>"></div> 
    </div>
  </div>
</div>

<script src="script.js"></script>
<script type="text/javascript">
  function selecionar(cod) {
    let select = document.getElementById('disc-id');
    for (var i = 0; i < select.options.length; i++) {
      if (select.options[i].value == cod) {
        select.selectedIndex = i;
        break;
      }
    }
  }
  selecionar(<%= aulaDto.codDisciplina %>); 
</script>

</body>
</html>
