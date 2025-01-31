package br.ufpr.appfin

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    private val codigo = 1 // Código para identificar a resposta

    val operacoes: MutableList<Operation> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }


    fun abrirTelaCadastro(view: View){
        val intent = Intent(this, TelaDeCadastro::class.java)
        //startActivity(intent)
        startActivityForResult(intent,codigo)
    }

    fun abrirTelaExtract(view: View){
        val intent = Intent(this, TelaDeExtrato::class.java)
        intent.putExtra("extrato",ArrayList(operacoes))
        startActivity(intent)
    }


    fun fecharApp(view: View){
        finishAffinity();
    }


    // Método chamado quando a tela de cadastro retorna um resultado
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        println("ok")

        val ope = if(Build.VERSION.SDK_INT >=Build.VERSION_CODES.TIRAMISU){
            data?.getParcelableExtra("operation", Operation::class.java)
        } else{
            @Suppress("DEPRECATION") // Remover alera de codido antigo
            data?.getParcelableExtra("operation")
        }

        if (resultCode == RESULT_OK) {

            if (ope != null) {
                operacoes.add(ope)
            }
        }

        // Para teste
        val detalhes = findViewById<TextView>(R.id.textView4)
        //detalhes.text = operacoes.toString()
    }

    
}