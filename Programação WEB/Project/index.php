<?php include('header.php'); ?>

<div class="container d-flex justify-content-center align-items-center vh-100">
    <div class="card shadow p-4" style="width: 100%; max-width: 400px;">
        <h3 class="text-center mb-4">Descubra seu signo:</h3>
        
        <form id="signo-form" method="POST" action="show_zodiac_sign.php">
            <div class="mb-3">
                <label for="data_nascimento" class="form-label">Data de nascimento</label>
                <!-- Usando o tipo date do HTML5 para validação nativa -->
                <input type="date" class="form-control" id="data_nascimento" name="data_nascimento" required>
            </div>
            <button type="submit" class="btn btn-primary w-100">Descobrir</button>
        </form>
    </div>
</div>

</body>
</html>