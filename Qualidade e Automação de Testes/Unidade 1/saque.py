#===== FUNÇÃO ORIGINAL =====
def saque(valor, saldo, limite_credito):
    status = False

    if valor <= saldo:
        saldo = saldo - valor
        status = True
    elif valor <= limite_credito:
        limite_credito = limite_credito - valor
        status = True

    return status, saldo, limite_credito


#===== EXEMPLO DE USO DO ROTEIRO =====
saldo = 1000
limite_credito = 500

status, saldo, limite_credito = saque(1200, saldo, limite_credito)
print("Saque realizado?", status)
print("Saldo:", saldo)
print("Limite de crédito:", limite_credito)


#===== CASOS DE TESTE (CAIXA BRANCA) =====
def teste_caminho_1():
    """Valor <= saldo: saque feito com o saldo."""
    status, saldo, limite = saque(500, 1000, 500)
    assert status == True
    assert saldo == 500
    assert limite == 500
    print("Teste 1 (valor <= saldo): OK")


def teste_caminho_2():
    """Valor > saldo, mas <= limite de crédito: saque feito com o limite."""
    status, saldo, limite = saque(400, 300, 500)
    assert status == True
    assert saldo == 300
    assert limite == 100
    print("Teste 2 (valor > saldo e <= limite): OK")


def teste_caminho_3():
    """Valor > saldo e > limite: saque negado."""
    status, saldo, limite = saque(1200, 1000, 500)
    assert status == False
    assert saldo == 1000
    assert limite == 500
    print("Teste 3 (valor > saldo e > limite): OK")


#Testes extras de valores limite (opcionais)
def teste_limite_saldo():
    """Valor exatamente igual ao saldo."""
    status, saldo, limite = saque(1000, 1000, 500)
    assert status == True and saldo == 0 and limite == 500
    print("Teste extra (valor == saldo): OK")


def teste_limite_credito():
    """Valor exatamente igual ao limite, acima do saldo."""
    status, saldo, limite = saque(500, 300, 500)
    assert status == True and saldo == 300 and limite == 0
    print("Teste extra (valor == limite): OK")


print("\n--- Executando testes ---")
teste_caminho_1()
teste_caminho_2()
teste_caminho_3()
teste_limite_saldo()
teste_limite_credito()
print("Todos os testes passaram!")