package com.example.appifome;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TelaPrincipal extends AppCompatActivity {
    Switch SwitchPizza, SwitchBebida;
    RadioGroup radioGroupTamanho, radioGroupEntrega;
    Spinner spinnerSabor, spinnerBebida;
    EditText editEndereco;
    Button btnRealizarPedido, btnRetornar;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_principal);

        userId = getIntent().getIntExtra("userId", -1);

        SwitchPizza = findViewById(R.id.switch_pizza);
        SwitchBebida = findViewById(R.id.switch_bebida);
        radioGroupTamanho = findViewById(R.id.radio_group_tamanho);
        radioGroupEntrega = findViewById(R.id.radio_group_entrega);
        spinnerSabor = findViewById(R.id.spinner_sabor);
        spinnerBebida = findViewById(R.id.spinner_bebida);
        editEndereco = findViewById(R.id.edit_endereco);
        btnRealizarPedido = findViewById(R.id.btn_realizar_pedido);
        btnRetornar = findViewById(R.id.btn_retornar);

        btnRealizarPedido.setOnClickListener(v -> enviarPedido());
        btnRetornar.setOnClickListener(v -> finish());
    }

    private void enviarPedido() {
        boolean pizza = SwitchPizza.isChecked();
        String tamanho = pizza ? getSelectedTamanho() : "NA";
        String sabor = pizza ? spinnerSabor.getSelectedItem().toString() : "NA";

        boolean bebida = SwitchBebida.isChecked();
        String descBebida = bebida ? spinnerBebida.getSelectedItem().toString() : "NA";

        String tele = isTeleEntrega() ? "Sim" : "Não";
        String endereco = tele.equals("Sim") ? editEndereco.getText().toString() : "NA";

        if (userId == -1) {
            Toast.makeText(this, "Erro: ID do usuário inválido.", Toast.LENGTH_SHORT).show();
            return;
        }

        JSONObject jsonPedido = new JSONObject();
        try {
            jsonPedido.put("idUsuario", userId);
            jsonPedido.put("pizza", pizza);
            jsonPedido.put("tamanho", tamanho);
            jsonPedido.put("sabor", sabor);
            jsonPedido.put("bebida", bebida);
            jsonPedido.put("desc_bebida", descBebida);
            jsonPedido.put("tele", tele);
            jsonPedido.put("endereco", endereco);

            enviarPedidoParaServidor(jsonPedido);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void enviarPedidoParaServidor(JSONObject jsonPedido) {
        executor.execute(() -> {
            try {
                String urlString = "http://200.132.172.204/ifome/enviar_pedido.php";
                URL url = new URL(urlString);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);
                connection.setRequestProperty("Content-Type", "application/json");

                OutputStream os = connection.getOutputStream();
                os.write(jsonPedido.toString().getBytes());
                os.flush();
                os.close();

                int responseCode = connection.getResponseCode();
                mainHandler.post(() -> {
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        Toast.makeText(TelaPrincipal.this, "Pedido realizado com sucesso!", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(TelaPrincipal.this, "Erro ao enviar o pedido.", Toast.LENGTH_LONG).show();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                mainHandler.post(() -> Toast.makeText(TelaPrincipal.this, "Erro na conexão com o servidor.", Toast.LENGTH_LONG).show());
            }
        });
    }

    private String getSelectedTamanho() {
        int selectedTamanhoId = radioGroupTamanho.getCheckedRadioButtonId();
        if (selectedTamanhoId == R.id.radio_pequena) return "Pequena";
        if (selectedTamanhoId == R.id.radio_media) return "Média";
        if (selectedTamanhoId == R.id.radio_grande) return "Grande";
        return "NA";
    }

    private boolean isTeleEntrega() {
        int selectedEntregaId = radioGroupEntrega.getCheckedRadioButtonId();
        return selectedEntregaId == R.id.radio_group_entrega;
    }
}