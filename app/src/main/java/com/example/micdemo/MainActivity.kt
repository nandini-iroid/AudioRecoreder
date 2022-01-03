package com.example.micdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.widget.TextView
import android.os.Environment
import android.widget.Toast
import android.content.pm.PackageManager
import android.Manifest.permission.RECORD_AUDIO
import androidx.core.content.ContextCompat
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.util.Log
import androidx.core.app.ActivityCompat
import java.io.File
import java.io.IOException


class MainActivity : AppCompatActivity() {

    var record:AudioRecorder?=null
    private var mFileName: String? = null

    val REQUEST_AUDIO_PERMISSION_CODE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<TextView>(R.id.btnRecord).setOnClickListener { // start recording method will
            // start the recording of audio.
//            startRecording()
            mFileName = ""+Environment.getExternalStorageDirectory() + File.separator.toString() + Environment.DIRECTORY_DCIM + File.separator.toString() + "FILE_NAMEAudioRecording.3gp"
            Log.e("<>>>>>>", "onCreate: $mFileName")
            record  = AudioRecorder(mFileName)
                record?.start()
        }
        findViewById<TextView>(R.id.btnStop).setOnClickListener { // pause Recording method will
            record?.stop()
            // pause the recording of audio.
        }
        findViewById<TextView>(R.id.btnPlay).setOnClickListener { // play audio method will play
            // the audio which we have recorded
            record?.playarcoding()
        }
        findViewById<TextView>(R.id.btnStopPlay).setOnClickListener { // pause play method will
            // pause the play of audio
            record?.stopMediaPlayer()
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_AUDIO_PERMISSION_CODE -> if (grantResults.isNotEmpty()) {
                val permissionToRecord = grantResults[0] == PackageManager.PERMISSION_GRANTED
                val permissionToStore = grantResults[1] == PackageManager.PERMISSION_GRANTED
                if (permissionToRecord && permissionToStore) {
                    Toast.makeText(applicationContext, "Permission Granted", Toast.LENGTH_LONG)
                        .show()
                } else {
                    Toast.makeText(applicationContext, "Permission Denied", Toast.LENGTH_LONG)
                        .show()
                }
            }
        }
    }
}