<?php include('header.php'); ?>

<div class="container mt-5">
    <?php
    // Recebe a data de nascimento do formulário
    $data_nascimento = $_POST['data_nascimento'];
    
    // Carrega o arquivo XML
    $signos = simplexml_load_file("signos.xml");
    
    // Converte a data enviada (YYYY-MM-DD) para extrair apenas Mês e Dia
    $data_input = new DateTime($data_nascimento);
    $mes_dia_input = $data_input->format('m-d'); // Formato: MM-DD
    
    $signo_encontrado = null;

    // Itera sobre cada signo no XML
    foreach ($signos->signo as $signo) {
        // Converte as datas do XML (DD/MM) para o mesmo formato MM-DD
        $inicio = DateTime::createFromFormat('d/m', (string)$signo->dataInicio)->format('m-d');
        $fim = DateTime::createFromFormat('d/m', (string)$signo->dataFim)->format('m-d');
        
        // Lógica de verificação
        // Se a data de início for maior que a do fim (caso de Capricórnio: 12-22 a 01-20)
        if ($inicio > $fim) {
            if ($mes_dia_input >= $inicio || $mes_dia_input <= $fim) {
                $signo_encontrado = $signo;
                break;
            }
        } else {
            // Para os demais signos onde Início é menor que o Fim no mesmo ano
            if ($mes_dia_input >= $inicio && $mes_dia_input <= $fim) {
                $signo_encontrado = $signo;
                break;
            }
        }
    }
    ?>

    <div class="row justify-content-center">
        <div class="col-md-8 text-center">
            <?php if ($signo_encontrado): ?>
                <div class="card text-white shadow-lg" style="background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); min-height: 250px; display: flex; flex-direction: column; justify-content: center;">
                    <div class="card-body">
                        <h1 class="display-3 fw-bold mb-4"><?php echo $signo_encontrado->signoNome; ?></h1>
                        <p class="lead" style="letter-spacing: 2px;">
                            <?php echo $signo_encontrado->descricao; ?>
                        </p>
                    </div>
                </div>
            <?php else: ?>
                <div class="alert alert-danger" role="alert">
                    Não foi possível identificar o signo para a data informada.
                </div>
            <?php endif; ?>
            
            <a href="index.php" class="btn btn-outline-primary mt-4 px-5">Voltar</a>
        </div>
    </div>
</div>

</body>
</html>