def saque(valor, saldo, limite_credito):
    status = False

    if valor <= saldo:
        saldo = saldo - valor
        status = True
    elif valor <= limite_credito:
        limite_credito = limite_credito - valor
        status = True

    return status, saldo, limite_credito
