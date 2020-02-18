package mx.edu.ittepic.ladm_u1_practica2_bryanromero

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
import java.io.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        permisos()

        guardar.setOnClickListener {
            if(memoriaInterna.isChecked){
            //GUARDARA EN MEMORIA INTERNA
              mensaje("Se guardo en la memoria interna")

              guardarArchivoInterno()
            }else if (memoriaSD.isChecked){
                //GUARDARA EN MEMORIA SD
                mensaje("Se guardo en la memoria externa")
                guardarArchivoSD()
            }

        }

        abrir.setOnClickListener {
            if(memoriaInterna.isChecked){
                //LEER EN MEMORIA INTERNA
                mensaje(nombreArchivo.text.toString())
                leerArchivoInterno()

            }else if (memoriaSD.isChecked){
                //LEER EN MEMORIA SD
                mensaje(nombreArchivo.text.toString())
                leerArchivoSD()
            }
        }

    }//onCreate

    fun permisos(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE),0)
        }else{
            mensaje("Permisos Ya Otorgados")
        }
    }//permisos

    fun mensaje(m:String){
        AlertDialog.Builder(this)
            .setTitle("Atencion")
            .setMessage(m)
            .setPositiveButton("OK"){d,i->}
            .show()
    }//mensaje

    fun guardarArchivoSD(){
        if(noSD()){
            mensaje("Sin memoria externa")
            return
        }
        try{
            var rutaSD= Environment.getExternalStorageDirectory()
            var datosArchivo = File(rutaSD.absolutePath,nombreArchivo.text.toString()+".txt")

            var flujoSalida = OutputStreamWriter(FileOutputStream(datosArchivo))
            var data =frase.text.toString()

            flujoSalida.write(data)
            flujoSalida.flush()
            flujoSalida.close()
            mensaje("EXITO! se guardo correctamente")
            frase.setText("")

        }catch (error : IOException){
            mensaje(error.message.toString())
        }
    }//guardarArchivoSD

    fun leerArchivoSD(){
        if(noSD()){
            mensaje("Sin memoria externa")
        }
        try {

            var rutaSD = Environment.getExternalStorageDirectory()
            var datosArchivo = File(rutaSD.absolutePath,nombreArchivo.text.toString()+".txt")

            var flujoEntrada= BufferedReader(InputStreamReader(FileInputStream(datosArchivo)))
            var data = flujoEntrada.readLine()

            frase.setText(data).toString()
            flujoEntrada.close()

        }catch (error: IOException){
            mensaje(error.message.toString())
        }
    }//leerArchivoSD

    fun noSD(): Boolean{
        var estado= Environment.getExternalStorageState()
        if(estado != Environment.MEDIA_MOUNTED){
            return true
        }
        return false
    }//noSD

    fun guardarArchivoInterno(){
        try{
            var flujoSalida = OutputStreamWriter(openFileOutput(nombreArchivo.text.toString()+".txt", Context.MODE_PRIVATE))
            var data =frase.text.toString()

            flujoSalida.write(data)
            flujoSalida.flush()
            flujoSalida.close()
            mensaje("EXITO! se guardo correctamente")
            frase.setText("")

        }catch (error : IOException){
            mensaje(error.message.toString())
        }
    }//guardarArchivoInterno

    fun leerArchivoInterno(){
        try {
            var flujoEntrada = BufferedReader(InputStreamReader(openFileInput(nombreArchivo.text.toString()+".txt")))
            var data = flujoEntrada.readLine()
            frase.setText(data).toString()
            flujoEntrada.close()

        }catch (error: IOException){
            mensaje(error.message.toString())
        }
    }//leerArchivoInterno

}
