
# Separador Inteligente - SmartHopper

**O melhor e o mais compacto separador de itens.**

Já pensou na mesma coluna de baús poder colocar itens diferentes ou melhor colocar no mesmo baú itens da mesma família.
Além disso o ***SmartHopper*** **não usa redstone** diminuindo o lag do servidor.
O quê acha de poder criar o seu próprio grupo de itens.
Com separador inteligente isso é possível, agora você pode criar os seus próprios grupos, podendo remover ou adicionar novos itens ao grupo.  
Agora além de poder colocar os nomes dos itens vai poder colocar os **IDs** que é altamente ***recomendado***. Usando o ID vai poder colocar mais itens o grupo usando menos espaço sabendo que é limitado os caracteres da bigorna no máximo 32 caracteres.
**IDs** são números próprio que não se repetem, e existe tanto para os itens quanto para os grupos,  o ID do item 10 é diferente do ID do grupo 10, e para diferenciar é colocado uma letra na frente da numeração, usamos a letra **I** para o item e **G** para o grupo. Assim ficaria o **I10** para representar o item 10 e **G10** que presenta o grupo 10.
Uma da vantagem de usar o ID que vai caber mais itens por se tratar de apenas 5 caracteres no máximo.  
Para usar o separador inteligente basta ter uma **bigorna** e renome o funil com o nome ou **ID**, com um custo de um **XP**.

---

> Veja o tutorial completo [SmartHopper](https://youtu.be/fBIeZ57ka1M)
> Download SmartHopper [SmartHopper] (https://github.com/elderbr/SmartHopper/blob/main/build/libs/SmartHopper-3.5.1.jar)

---

## Como surgiu

O separador surgiu depois de ver que o meu armazém estava ficando muito grande, então resolvi fazer o ***SmartHopper*** para diminuir o armazém.

## Configurar o filtro

Para configurar o filtro vai precisar de uma bigorna e **XP**,  coloque um ou mais funil na bigorna e renome com o ***~~nome~~*** do item ou grupo em inglês, mas é recomendado usar o **ID**.
Pronto o funil está configurado.
Se você não sabe qual é o ID configurou para o funil, basta clicar no funil com graveto na mão será mostrando o nome do item ou grupo.

 #### -  Configurar mais de um item ou grupo
 Para configurar mais de um item ou grupo para o mesmo funil, separe usando o ponto e virgula ( **;** )
 O limite é padrão da bigorna de 32 caracteres.
 > Ex: I10;I34;G23 

## Item
Para saber o **ID** do item basta escrever o nome do item tanto no inglês ou português ou segurar o item na mão e escrever **/item**.
>Ex: **/item** escreva o nome do item desejado --> **/item stone** 

Vai receber uma mensagem com a confirmação do nome do item e seu **ID**.
>Ex: **item: stone ID: 340**


## Grupo
Grupo são vários itens da mesma família ou do mesmo seguimento. O plugin vem pré-configurado grupos.
Para saber o ID do grupo segure o item na mão ou escreva o nome. 

O mesmo item pode está representa em mais de um grupo, o tronco de pinheiro pode está no grupo ***Troncos*** e ***Pinheiro***, escolha qual grupo deseja.
Se não souber o nome do grupo pode obter uma lista de todos os nomes dos grupos usando o comando:
>**/book** ou **/livro** vai receber um livro com uma lista com todos os nomes dos grupos 

Pode ver todos os itens que estão no grupo digitando **/grupo** nome ou ID, vai aparecer um inventário com todos os itens do grupo.

#### - Adicionar novo grupo
Para adicionar novo grupo digite **/addgrupo** ***nome do grupo desejado*** será aberto um inventário para adicionar os itens, adicione todos os itens que deseja ao grupo, depois clique na lã verde para salvar.
Apenas os **adm** e ***operadores*** do ***SmartHopper*** podem adicionar novo grupo.

#### - Alterando grupo
Para alterar o grupo digite o **/grupo** ***nome ou ID*** será aberto o grupo como todos os itens adicione ou remova os itens do inventário depois clique na lã verde para salvar.
Apenas os **adm** e ***operadores*** do ***SmartHopper*** podem alterar grupo.

#### - Remover grupo
Para remover o grupo digite o **/removegrupo** ***nome ou ID***.
Apenas os **adm** e ***operadores*** do ***SmartHopper*** podem remover grupo.

___

## Adminstrador e operadores

### Administradores
- São responsáveis pelo os operadores e na criação, alterações ou remover os grupos.
Os administradores podem adicionar novos operadores ou remove-los. 

### Para adicionar novo operador:
> **/addoperador** ***nome do operador***

### Para remover operador:
> **/removeoperador** ***nome do operador***

### Operadores 
- São responsáveis: pela criação, alterações ou remover os grupos.

## Como usar

É importa ter um funil principal onde leva os itens para os funis que fazem a conexão formando uma encanação.
É importante ter um funil de resto, ou seja, aquele baú onde vão ficar os itens que não foram configurados no separador.

| Funil Principal |    Funil  |  Funil       |       Funil   |
| --------------|-----------|--------------|---------------|
| 				| Stone     | Stone Stairs |  Resto        |
|               | Stone Slab | Stone Wall   |               |


### AVISO
Na coluna ***não pode ter um funil sem está configurado***, quando funil não está configura ***recebe*** todos os tipos de item, ele se torna um funil global onde aceita qualquer item.
