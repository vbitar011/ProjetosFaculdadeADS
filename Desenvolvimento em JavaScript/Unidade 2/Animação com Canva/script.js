//===== 1. CONFIGURAÇÃO INICIAL =====
const canvas = document.getElementById("meuCanvas"); // pega o elemento canvas
const ctx = canvas.getContext("2d");                  // contexto 2D: "pincel" que desenha

canvas.width = 800;   //largura em pixels
canvas.height = 500;  //altura em pixels

let pausado = false;  //controla se a animação está pausada

//===== 2. CLASSE QUE REPRESENTA CADA OBJETO (BOLA) =====
class Bola {
  constructor(x, y, raio, vx, vy, matiz) {
    this.x = x;         //posição horizontal
    this.y = y;         //posição vertical
    this.raio = raio;   //tamanho
    this.vx = vx;       //velocidade horizontal
    this.vy = vy;       //velocidade vertical
    this.matiz = matiz; //cor (0 a 360 no modelo HSL)
  }

  //Desenha a bola no canvas
  desenhar() {
    ctx.beginPath();                                   //inicia um novo desenho
    ctx.arc(this.x, this.y, this.raio, 0, Math.PI * 2); //círculo completo
    ctx.fillStyle = `hsl(${this.matiz}, 80%, 55%)`;     //define a cor
    ctx.fill();                                        //preenche
    ctx.closePath();
  }

  //Atualiza posição e cor a cada quadro
  atualizar() {
    this.x += this.vx; //move na horizontal
    this.y += this.vy; //move na vertical

    //Rebate nas paredes laterais
    if (this.x + this.raio > canvas.width || this.x - this.raio < 0) {
      this.vx = -this.vx; //inverte a direção
    }
    //Rebate no topo e no chão
    if (this.y + this.raio > canvas.height || this.y - this.raio < 0) {
      this.vy = -this.vy;
    }

    //Muda a cor ao longo do tempo
    this.matiz = (this.matiz + 1) % 360;
  }
}

//===== 3. CRIAÇÃO DOS OBJETOS =====
const bolas = [];

function numeroAleatorio(min, max) {
  return Math.random() * (max - min) + min;
}

function criarBola(x, y) {
  const raio = numeroAleatorio(15, 35);
  const vx = numeroAleatorio(-4, 4) || 2; //"|| 2" evita velocidade zero
  const vy = numeroAleatorio(-4, 4) || 2;
  const matiz = numeroAleatorio(0, 360);
  bolas.push(new Bola(x, y, raio, vx, vy, matiz));
}

//Usa um laço "for" para criar 8 bolas iniciais
for (let i = 0; i < 8; i++) {
  criarBola(
    numeroAleatorio(50, canvas.width - 50),
    numeroAleatorio(50, canvas.height - 50)
  );
}

//===== 4. LOOP DE ANIMAÇÃO =====
function animar() {
  if (!pausado) {
    //Limpa o quadro anterior com um leve rastro (efeito visual)
    ctx.fillStyle = "rgba(17, 17, 17, 0.25)";
    ctx.fillRect(0, 0, canvas.width, canvas.height);

    //Atualiza e desenha cada bola
    for (const bola of bolas) {
      bola.atualizar();
      bola.desenhar();
    }
  }
  requestAnimationFrame(animar); //pede o próximo quadro (~60 vezes por segundo)
}

animar(); //inicia a animação

//===== 5. INTERATIVIDADE =====
canvas.addEventListener("click", (evento) => {
  //Converte a posição do clique para coordenadas do canvas
  const area = canvas.getBoundingClientRect();
  const mouseX = evento.clientX - area.left;
  const mouseY = evento.clientY - area.top;

  let acertou = false;

  for (const bola of bolas) {
    //Distância entre o clique e o centro da bola
    const distancia = Math.hypot(mouseX - bola.x, mouseY - bola.y);
    if (distancia <= bola.raio) {
      bola.matiz = numeroAleatorio(0, 360); //muda a cor
      bola.vx = -bola.vx;                   //inverte o movimento
      acertou = true;
    }
  }

  //Se clicou no fundo, cria uma nova bola
  if (!acertou) {
    criarBola(mouseX, mouseY);
  }
});

//Teclado: ESPAÇO pausa e retoma
document.addEventListener("keydown", (evento) => {
  if (evento.code === "Space") {
    evento.preventDefault();
    pausado = !pausado;
  }
});