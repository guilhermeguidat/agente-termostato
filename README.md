# Relatório Prático: Agente Inteligente Reativo

**Projeto:** Controlador de Ar-Condicionado para Sala de Servidores
**Alunos:** Guilherme Guida Torres - Pedro Correa de Souza Quadros

---

## 1. Definição Formal do Ambiente (Modelo PEAS)
O agente desenvolvido tem como objetivo monitorar e controlar a temperatura de uma sala de servidores, garantindo que os equipamentos operem em uma faixa térmica segura.

* **P (Performance/Desempenho):** Manter a temperatura da sala estritamente entre 20°C e 24°C, minimizando o risco de superaquecimento dos servidores e evitando o desperdício de energia com refrigeração excessiva.
* **E (Environment/Ambiente):** Ambiente interno de uma sala de servidores fechada.
* **A (Actuators/Atuadores):** Sistema de Ar-Condicionado (com capacidade de ligar, desligar e ajustar potência).
* **S (Sensors/Sensores):** Termômetro digital ambiental.

## 2. Características do Ambiente e Justificativa
* **Totalmente Observável:** O agente tem acesso, a cada ciclo, à exata temperatura da sala através do seu sensor térmico, não havendo informações ocultas relevantes para a sua tomada de decisão no escopo deste problema.
* **Determinístico (sob o ponto de vista da simulação):** Acreditamos que a ação do ar-condicionado (ex: ligar o ar) resultará previsivelmente na queda da temperatura.
* **Sequencial:** As decisões presentes afetam as percepções futuras. Além disso, o agente consulta o banco de dados para saber a ação anterior, tornando a linha do tempo de ações relevante.
* **Dinâmico:** A temperatura do ambiente pode sofrer alterações enquanto o agente delibera, influenciada pelo calor dissipado pelos próprios servidores operando na sala.
* **Discreto:** As percepções (leituras de temperatura em intervalos) e as ações (comandos específicos para o ar-condicionado) são tratadas como eventos em passos de tempo distintos.
* **Agente Único:** Apenas o agente controlador do ar-condicionado atua para modificar a temperatura da sala.

## 3. Conjuntos de Percepções e Ações
* **Conjunto de Percepções:** { Temperatura atual em graus Celsius (T) }
* **Conjunto de Ações:** { Ligar Ar, Desligar Ar, Aumentar Potencia, Nenhuma Acao }

## 4. Tabela de Regras (Condição → Ação)
A tomada de decisão do agente cruza a percepção atual com o estado anterior (salvo no banco de dados) para definir a ação correta.

| Percepção Atual (T) | Consulta ao Histórico (Última Ação) | Ação Tomada |
| :--- | :--- | :--- |
| T > 24°C | Diferente de "Ligar Ar" | Ligar Ar |
| T > 24°C | Igual a "Ligar Ar" ou "Aumentar Potencia" | Aumentar Potencia |
| T < 20°C | Diferente de "Desligar Ar" | Desligar Ar |
| T < 20°C | Igual a "Desligar Ar" | Nenhuma Acao |
| 20°C <= T <= 24°C | Qualquer valor no histórico | Nenhuma Acao |

## 5. Modelo Conceitual do Banco de Dados (DER Simples)
O banco de dados foi modelado para manter a persistência das percepções e ações, permitindo que o agente possua uma "memória" de curto prazo.

* **Entidade Principal:** `Historico_Percepcao`
* **Relacionamento:** Um Agente (implícito no sistema) realiza N registros no Historico_Percepcao.

**Atributos da Entidade Historico_Percepcao:**
* `id` (Chave Primária, Inteiro, Auto-incremental)
* `temperatura` (Decimal) - Representa a percepção.
* `acao_tomada` (Texto/Varchar) - Representa a decisão do agente.
* `data_hora` (Timestamp) - Registra o momento do ciclo temporal.

## 6. Relatório de Simulações
Foram executadas 5 simulações sequenciais representando ciclos de leitura do termômetro. O sistema leu as variáveis, consultou o banco de dados e persistiu o novo estado.

* **Simulação 1 (Aquecimento inicial):**
    * **Dados inseridos (Percepção):** 25.5°C
    * **Consulta ao histórico:** Nenhuma Ação anterior (Banco inicialmente vazio)
    * **Ação tomada:** Ligar Ar
* **Simulação 2 (Calor persistente):**
    * **Dados inseridos (Percepção):** 26.0°C
    * **Consulta ao histórico:** Ligar Ar
    * **Ação tomada:** Aumentar Potencia (O agente percebeu que ligar não foi suficiente).
* **Simulação 3 (Temperatura estabilizada):**
    * **Dados inseridos (Percepção):** 22.0°C
    * **Consulta ao histórico:** Aumentar Potencia
    * **Ação tomada:** Nenhuma Acao (O ambiente atingiu a faixa ideal).
* **Simulação 4 (Resfriamento excessivo):**
    * **Dados inseridos (Percepção):** 19.0°C
    * **Consulta ao histórico:** Nenhuma Acao
    * **Ação tomada:** Desligar Ar
* **Simulação 5 (Frio residual):**
    * **Dados inseridos (Percepção):** 18.5°C
    * **Consulta ao histórico:** Desligar Ar
    * **Ação tomada:** Nenhuma Acao (O agente percebe que a temperatura está baixa, mas sabe que o comando de desligar já foi emitido, evitando redundância de comandos para o hardware).

## 7. Questão Conceitual
**Pergunta:** O agente ainda pode ser considerado puramente reativo? Ele passou a ser baseado em modelo? Justifique conceitualmente.

**Resposta:**
Não, o agente implementado não pode mais ser considerado puramente reativo. Ele evoluiu e passou a ser classificado conceitualmente como um Agente Reativo Baseado em Modelos.

**Justificativa:** Um agente puramente reativo (ou reativo simples) atua baseando-se exclusivamente na percepção do momento atual, sofrendo da limitação de não conhecer o passado e sendo incapaz de lidar com ambientes que não sejam totalmente observáveis ou que exijam contexto de estado.

A partir do momento em que o nosso agente utiliza o banco de dados MySQL para consultar a `acao_tomada` no ciclo anterior, ele passa a manter um estado interno. O banco de dados atua como o "modelo de mundo" desse agente. Ele agora entende como o ambiente evolui independentemente dele e como suas próprias ações afetam esse ambiente (por exemplo, ele sabe distinguir se o ambiente está a 26°C porque o ar ainda está desligado ou se está a 26°C mesmo com o ar já ligado, tomando ações diferentes para cada caso). Essa dependência do histórico de percepções/ações caracteriza perfeitamente a transição para um agente baseado em modelos.