package br.ufpr.appfin

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.InputFilter
import android.text.method.Touch
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class TelaDeCadastro : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_tela_de_cadastro)

        // Mapeamento para fitro
        val editText = findViewById<EditText>(R.id.editTextNumberDecimal)

        // Filtro para entrada com no máximo 2 casas decimais
        val decimalFilter = InputFilter { source, start, end, dest, dstart, dend ->
            try {
                val newText = dest.toString().substring(0, dstart) +
                        source.toString() +
                        dest.toString().substring(dend)
                val value = newText.toDoubleOrNull()

                if (value != null && newText.matches(Regex("\\d*(\\.\\d{0,2})?"))) {
                    return@InputFilter null // Permite a entrada
                } else {
                    return@InputFilter "" // Bloqueia a entrada
                }
            } catch (e: NumberFormatException) {
                return@InputFilter ""
            }
        }

        // Adiciona o filtro ao EditText
        editText.filters = arrayOf(decimalFilter)

        // Validação quando o campo perde o foco
        editText.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) { // Quando o campo perde o foco
                val valor = editText.text.toString().toDoubleOrNull() ?: 0.0

                // Garante que o valor mínimo seja 0.01
                if (valor < 0.01) {
                    editText.setText("0.01")
                } else {
                    editText.setText(String.format("%.2f", valor))
                }
            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // fechar o app
        val voltar = findViewById<Button>(R.id.botaoVoltar)
        voltar.setOnClickListener(){
            finish()
        }
    }

    fun salvarDados(view: View) {

        val descricaoNaTela = findViewById<TextView>(R.id.editTextText)
        val valorNaTela = findViewById<EditText>(R.id.editTextNumberDecimal)
        val operacaoNaTela = findViewById<RadioGroup>(R.id.radioGroup)

        val idSelecionado = operacaoNaTela.checkedRadioButtonId

        if (idSelecionado == -1) {
            operacaoNaTela.requestFocus()
            toastView("Selecione uma operação Crédito ou Débito!")

        } else if (descricaoNaTela.length() == 0) {
            viwerForusKeyboard(descricaoNaTela)
            toastView("Prencha a descrição!")

        } else if (valorNaTela.length() == 0) {
            viwerForusKeyboard(valorNaTela)
            toastView("Insira um valor!")

        } else {
            // Pega o valor do botão selecionado
            val radioSelecionado = findViewById<RadioButton>(idSelecionado)

            // Pegar o valor
            val texto = valorNaTela.text.toString()
            var valorDouble : Double = 0.00

            // Converte o valor para Double
            try {
                valorDouble = texto.toDouble()
            } catch (e: NumberFormatException) {
                // Caso o texto não seja um número válido
                toastView("Insira um número válido!")
                return
            }

            if (valorDouble < 0.01){
                valorNaTela.requestFocus()
                toastView("Insira um número maior!")
                return
            }

            // Cria o objeto
            val ope = Operation(
                typoOpe = radioSelecionado.text.toString(),
                desc = descricaoNaTela.text.toString(), // Atribui o valor da descrição
                valor = valorDouble,
                data = LocalDateTime.now()
            )

            // devolve o objeto
            val intent = Intent()
            intent.putExtra("operation", ope)
            setResult(RESULT_OK, intent)
            finish()

        }
    }


    fun viwerForusKeyboard(objView:View){
        objView.requestFocus()
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(objView, InputMethodManager.SHOW_IMPLICIT)
    }


    fun toastView(text:String){
        Toast.makeText(this, text, Toast.LENGTH_LONG).show()
    }

    fun dataHoraAtual() {
        // Obtém a data e hora atual
        val dataHoraAtual = LocalDateTime.now()

        // Define um formato para exibir a data e hora
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")

        // Formata a data e hora
        val dataHoraFormatada = dataHoraAtual.format(formatter)

        // Exibe no console
        println("Data e Hora atual: $dataHoraFormatada")
    }


}