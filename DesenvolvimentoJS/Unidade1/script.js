function gerarTabuada() {
            //Captura o valor digitado pelo usuário
            const numeroInput = document.getElementById('numero').value;
            const numero = parseInt(numeroInput);
            const divResultado = document.getElementById('resultado');

            //Limpa o resultado anterior
            divResultado.innerHTML = '';

            //Valida se o número está entre 1 e 10
            if (isNaN(numero) || numero < 1 || numero > 10) {
                divResultado.innerHTML = '<p class="erro">Por favor, insira um número válido entre 1 e 10.</p>';
                return;
            }

            //Inicia a manipulação do DOM e a criação da tabuada
            let htmlTabuada = '<h3>Tabuada do ' + numero + '</h3>';

            //Estrutura de repetição (Laço "for") de 1 a 10
            for (let i = 1; i <= 10; i++) {
                let multiplicacao = numero * i;
                htmlTabuada += '<p>' + numero + ' x ' + i + ' = <strong>' + multiplicacao + '</strong></p>';
            }

            //Exibe dinamicamente o resultado na página
            divResultado.innerHTML = htmlTabuada;
        }