
reset = function(id) {
	
	let req = new XMLHttpRequest();
	req.open("POST", "ControllerServlet", true);
	req.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
	req.onreadystatechange = () => {
		if (req.readyState == 4) {
			if (req.status == 200) {  
			   atualizaSessao();
			   window.location.href = "/prova1";
			} else {
				console.error("Erro na requisição: ", req.status);
					}
			}
	}
	req.send("op=RESET");
}





novaAula = function() {
	window.location.href = "nova";
}


calcelarNovaAula = function() {
	window.location.href = "/prova1";
}


editarAula = function(id) {
	window.location.href = "edit?id=" + id;
}


enviarNovaAula = function() {
	
	let data = document.getElementById('data-id').value;
	let horario = document.getElementById('hora-id').value;
	let duracao = document.getElementById('dur-id').value;
	let codDisciplina = document.getElementById('disc-id').value;
	let assunto = document.getElementById('ass-id').value;
	
	if (!validaNovaAula(data, horario, duracao, codDisciplina, assunto)) {
        document.getElementById('msg-id').style.display = 'block';
        return;
    }
	let req = new XMLHttpRequest();
	req.open("POST", "ControllerServlet", true);
	req.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
	req.onreadystatechange = function() {
		if (req.readyState == 4) {
			if (req.status == 200) {  
			   atualizaSessao();
			   window.location.href = "/prova1";
			} else {
				console.error("Erro na requisição: ", req.status);
					}
			}
		}
	let parametros = "op=CREATE&data=" + encodeURIComponent(data) + "&horario=" + encodeURIComponent(horario) + "&duracao=" + encodeURIComponent(duracao) + "&codDisciplina=" + encodeURIComponent(codDisciplina) + "&assunto=" + encodeURIComponent(assunto);
   	req.send(parametros);
}
enviarEdit = function() {
    
    let id = document.getElementById('register-id').value;
    let data = document.getElementById('data-id').value;
    let horario = document.getElementById('hora-id').value;
    let duracao = document.getElementById('dur-id').value;
    let codDisciplina = document.getElementById('disc-id').value;
    let assunto = document.getElementById('ass-id').value;
    
    let xhr = new XMLHttpRequest();

    
    xhr.open("POST", "ControllerServlet", true);
    xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");

    
    xhr.onreadystatechange = function() {
        if (xhr.readyState == 4) {
            if (xhr.status == 200) {
                
                atualizaSessao();
                window.location.href = "/prova1";
            } else {
                
                console.error("Ocorreu um erro na requisição: " + xhr.status);
            }
        }
    };

    
    let params = "op=UPDATE&id=" + id + "&data=" + data + "&horario=" + horario + "&duracao=" + duracao + "&codDisciplina=" + codDisciplina + "&assunto=" + assunto;

    
    xhr.send(params);
}


deleta = function(id) {
    let req = new XMLHttpRequest();
    req.open("POST", "ControllerServlet", true);
    req.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");

    req.onreadystatechange = function() {
        if (req.readyState == 4 && req.status == 200) {
            
            atualizaSessao();
            
            window.location.href = "/prova1";
        } else{
			
		}
    };

   
    req.send("op=DELETE&id=" + id);
}



const atualizaSessao = function() {
	let req = new XMLHttpRequest();
	req.open("POST", "ControllerServlet", true);
	req.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
	req.onreadystatechange = () => {
		if (req.readyState == 4 && req.status == 200) {
			console.log("A sessão foi atualizada com sucesso.");
		} else {
			console.log("Ocorreu um erro ao tentar atualizar a sessão.");
		}
	}
	req.send("op=START_SESSION");
}





validaNovaAula = function(data, horario, duracao, codDisciplina, assunto) {
    
    if (!data || !horario || !duracao || !codDisciplina || !assunto) {
	document.getElementById('msg-vazio').style.display = 'block';
        return false; 
    }

   
    let dataRegex = /^\d{4}-\d{2}-\d{2}$/;
    if (!dataRegex.test(data)) {
	document.getElementById('msg-data').style.display = 'block';
        return false; 
    }

   
    let horarioRegex = /^\d{2}:\d{2}$/;
    if (!horarioRegex.test(horario)) {
	document.getElementById('msg-horario').style.display = 'block';
        return false; 
    }

    
    if (isNaN(parseFloat(duracao)) || parseFloat(duracao) <= 0) {
	document.getElementById('msg-duracao').style.display = 'block';
        return false; 
    }

    
    return true;
}


atualizaSessao();
