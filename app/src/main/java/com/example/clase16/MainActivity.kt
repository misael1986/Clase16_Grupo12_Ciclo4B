package com.example.clase16

import androidx.appcompat.app.AppCompatActivity// reemplaza a import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import android.os.Environment
import android.widget.TextView
import java.io.* // Funciona igual que el de la version de Java SE

class MainActivity : AppCompatActivity() {
    private val filepath = "MyFileStorage"
    internal var myExternalFile: File? = null
    private val isExternalStorageReadOnly: Boolean
        get() {
            val extStorageState = Environment.getExternalStorageState()
            return Environment.MEDIA_MOUNTED_READ_ONLY.equals(extStorageState)
        }
    private val isExternalStorageAvailable: Boolean
        get() {
            val extStorageState = Environment.getExternalStorageState()
            return Environment.MEDIA_MOUNTED.equals(extStorageState)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val fileName = findViewById<EditText>(R.id.editTextFile)
        val fileData = findViewById<EditText>(R.id.editTextData)
        val saveButton = findViewById<Button>(R.id.button_save)
        val viewButton = findViewById<Button>(R.id.button_view)
        val viewResult = findViewById<TextView>(R.id.textView3)

        saveButton.setOnClickListener(View.OnClickListener {
            myExternalFile = File(getExternalFilesDir(filepath), fileName.text.toString())
            try {
                val fileOutPutStream = FileOutputStream(myExternalFile)
                fileOutPutStream.write(fileData.text.toString().toByteArray())
                fileOutPutStream.close()// IMPORTANTE
            } catch (e: IOException) {
                e.printStackTrace()
            }
            Toast.makeText(applicationContext, "data save", Toast.LENGTH_SHORT).show()
        })
        viewButton.setOnClickListener(View.OnClickListener {
            myExternalFile = File(getExternalFilesDir(filepath), fileName.text.toString())

            val filename = fileName.text.toString()
            myExternalFile = File(getExternalFilesDir(filepath), filename)
            val name=myExternalFile.toString()
            if (!(filename.equals(null)) && (filename.trim() != "")
                && !name.equals(null) && !name.trim().equals("")) {
                var fileInputStream = FileInputStream(myExternalFile)
                var inputStreamReader = InputStreamReader(fileInputStream)
                val bufferedReader = BufferedReader(inputStreamReader)
                val stringBuilder= StringBuilder()
                var text: String? = null
                while ({ text = bufferedReader.readLine(); text }() != null) {
                    stringBuilder.append(text+"\n")
                }
                fileInputStream.close()
                //Displaying data on EditText
                //Toast.makeText(applicationContext, stringBuilder.toString(), Toast.LENGTH_LONG)
                //    .show()
                viewResult.text="Datos Encontrados: \n"+stringBuilder
            }

            else {
                var NotFoundMsg="Archivo '"+filename.toString()+"' No disponible o está en blanco"
                Toast.makeText(this,NotFoundMsg,Toast.LENGTH_LONG)
            }
        })

        if (!isExternalStorageAvailable || isExternalStorageReadOnly) {
            saveButton.isEnabled = false
        }
    }
}