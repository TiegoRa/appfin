package br.ufpr.appfin

import android.os.Build
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.time.format.DateTimeFormatter

class TelaDeExtrato : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_extract)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val dados = intent.extras

        if (dados == null){
            println("Sem dados")
            return
        }

        val listaDeOperacoes: ArrayList<Operation>? = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            dados?.getParcelableArrayList("extrato", Operation::class.java)
        } else{
            @Suppress("DEPRECATION") // Remover alera de codido antigo
            dados?.getParcelableArrayList("extrato")
        }


        // Verificar se a lista é nula ou vazia
        if (listaDeOperacoes.isNullOrEmpty()) {
            Toast.makeText(this, "Nenhuma operação encontrada!", Toast.LENGTH_LONG).show()
            return
        }

        val listvew = findViewById<ListView>(R.id.listView)
//        val operacoes: MutableList<Operation> = mutableListOf()


//        val arrayAdapter: ArrayAdapter<Operation> = ArrayAdapter(this,
//            android.R.layout.simple_list_item_1, listaDeOperacoes)

        // Criar um ArrayAdapter com uma string representativa
        val arrayAdapter: ArrayAdapter<String> = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            listaDeOperacoes.map { operation ->
                // Retorna uma representação em String para exibição
                val format = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")
//                val dataFormatada = operation.data.format(format)
                """Tipo: ${operation.typoOpe}, Descrição: ${operation.desc},
                    | Valor: ${operation.valor}, Data:${operation.data.format(format)}""".trimMargin()
            }
        )

        listvew.adapter = arrayAdapter
    }
}