<?php
$servidor = 'localhost';
$banco = 'bdifome';
$usuario = 'root';
$senha = '';

// Receber os dados enviados pelo aplicativo em JSON
$json = file_get_contents('php://input');
$obj = json_decode($json);

// Dados recebidos
$nomeUsuario = $obj->nome; // Nome do usuário para buscar o ID
$pizza = $obj->pizza ? 1 : 0; // Converte boolean para int (1 ou 0)
$tamanho = $obj->tamanho ?: "NA"; // Se vazio, definir como "NA"
$sabor = $obj->sabor ?: "NA"; // Se vazio, definir como "NA"
$bebida = $obj->bebida ? 1 : 0; // Converte boolean para int (1 ou 0)
$descBebida = $obj->desc_bebida ?: "NA"; // Se vazio, definir como "NA"
$tele = $obj->tele; // "Sim" ou "Não"
$endereco = ($tele === "Sim") ? $obj->endereco : "NA"; // Se "Tele" não, definir como "NA"

// Conexão com o banco de dados
$conexao = mysqli_connect($servidor, $usuario, $senha, $banco);

if (!$conexao) {
    echo json_encode(["status" => "erro", "mensagem" => "Erro ao conectar ao banco de dados."]);
    exit();
}

$queryUsuario = "SELECT id FROM usuario WHERE nome = ?";
$stmt = mysqli_prepare($conexao, $queryUsuario);
mysqli_stmt_bind_param($stmt, "s", $nomeUsuario);
mysqli_stmt_execute($stmt);
$result = mysqli_stmt_get_result($stmt);
$usuarioData = mysqli_fetch_assoc($result);

if (!$usuarioData) {
    echo json_encode(["status" => "erro", "mensagem" => "Usuário não encontrado."]);
    exit();
}

$idUsuario = $usuarioData['id'];

$queryPedido = "INSERT INTO pedidos (idusr, pizza, tamanho, sabor, bebida, desc_bebida, tele, endereco) 
                VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
$stmtPedido = mysqli_prepare($conexao, $queryPedido);
mysqli_stmt_bind_param(
    $stmtPedido,
    "iissssss",
    $idUsuario,
    $pizza,
    $tamanho,
    $sabor,
    $bebida,
    $descBebida,
    $tele,
    $endereco
);

if (mysqli_stmt_execute($stmtPedido)) {
    echo json_encode(["status" => "sucesso", "mensagem" => "Pedido registrado com sucesso!"]);
} else {
    echo json_encode(["status" => "erro", "mensagem" => "Erro ao registrar o pedido."]);
}

mysqli_stmt_close($stmt);
mysqli_stmt_close($stmtPedido);
mysqli_close($conexao);
?>