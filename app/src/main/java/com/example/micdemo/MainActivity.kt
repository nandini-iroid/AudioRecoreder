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
    private var mRecorder: MediaRecorder? = null

    private var mPlayer: MediaPlayer? = null

    private var mFileName: String? = null

    val REQUEST_AUDIO_PERMISSION_CODE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<TextView>(R.id.btnRecord).setOnClickListener { // start recording method will
            // start the recording of audio.
//            startRecording()
            Log.e("<>>>>>>", "onCreate: $mFileName")
            AudioRecorder("FILE_NAMEAudioRecording.3gp").start()
        }
        findViewById<TextView>(R.id.btnStop).setOnClickListener { // pause Recording method will
            // pause the recording of audio.
            pauseRecording()
        }
        findViewById<TextView>(R.id.btnPlay).setOnClickListener { // play audio method will play
            // the audio which we have recorded
            playAudio()
        }
        findViewById<TextView>(R.id.btnStopPlay).setOnClickListener { // pause play method will
            // pause the play of audio
            pausePlaying()
        }

    }

    private fun startRecording() {
        // check permission method is used to check
        // that the user has granted permission
        // to record nd store the audio.
        if (CheckPermissions()) {

            // setbackgroundcolor method will change
            // the background color of text view.
            findViewById<TextView>(R.id.btnStop).setBackgroundColor(resources.getColor(R.color.purple_200))
            findViewById<TextView>(R.id.btnRecord).setBackgroundColor(resources.getColor(R.color.gray))
            findViewById<TextView>(R.id.btnPlay).setBackgroundColor(resources.getColor(R.color.gray))
            findViewById<TextView>(R.id.btnStopPlay).setBackgroundColor(resources.getColor(R.color.gray))

            // we are here initializing our filename variable
            // with the path of the recorded audio file.

            // below method is used to initialize
            // the media recorder clss
            mRecorder = MediaRecorder()

            // below method is used to set the audio
            // source which we are using a mic.
            mRecorder?.setAudioSource(MediaRecorder.AudioSource.MIC)

            // below method is used to set
            // the output format of the audio.
            mRecorder?.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)

            // below method is used to set the
            // audio encoder for our recorded audio.
            mRecorder?.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)

            // below method is used to set the
            // output file location for our recorded audio
            mRecorder?.setOutputFile(mFileName)
            try {
                // below method will prepare
                // our audio recorder class
                mRecorder?.prepare()
            } catch (e: IOException) {
                Log.e("TAG", "prepare() failed")
            }
            // start method will start
            // the audio recording.
            mRecorder?.start()
            findViewById<TextView>(R.id.idTVstatus).text = "Recording Started"
        } else {
            // if audio recording permissions are
            // not granted by user below method will
            // ask for runtime permission for mic and storage.
            RequestPermissions()
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
    fun CheckPermissions(): Boolean {
        // this method is used to check permission
        val result = ContextCompat.checkSelfPermission(applicationContext, WRITE_EXTERNAL_STORAGE)
        val result1 = ContextCompat.checkSelfPermission(applicationContext, RECORD_AUDIO)
        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED
    }

    private fun RequestPermissions() {
        // this method is used to request the
        // permission for audio recording and storage.
        ActivityCompat.requestPermissions(
            this@MainActivity,
            arrayOf(RECORD_AUDIO, WRITE_EXTERNAL_STORAGE),
            REQUEST_AUDIO_PERMISSION_CODE
        )
    }
    fun playAudio() {
        findViewById<TextView>(R.id.btnStop).setBackgroundColor(resources.getColor(R.color.gray))
        findViewById<TextView>(R.id.btnRecord).setBackgroundColor(resources.getColor(R.color.purple_200))
        findViewById<TextView>(R.id.btnPlay).setBackgroundColor(resources.getColor(R.color.gray))
        findViewById<TextView>(R.id.btnStopPlay).setBackgroundColor(resources.getColor(R.color.purple_200))

        // for playing our recorded audio
        // we are using media player class.
        mPlayer = MediaPlayer()
        try {
            // below method is used to set the
            // data source which will be our file name
            mPlayer?.setDataSource(mFileName)

            // below method will prepare our media player
            mPlayer?.prepare()

            // below method will start our media player.
            mPlayer?.start()
            findViewById<TextView>(R.id.idTVstatus).text = "Recording Started Playing"
        } catch (e: IOException) {
            Log.e("TAG", "prepare() failed")
        }
    }

    fun pauseRecording() {
        findViewById<TextView>(R.id.btnStop).setBackgroundColor(resources.getColor(R.color.gray))
        findViewById<TextView>(R.id.btnRecord).setBackgroundColor(resources.getColor(R.color.purple_200))
        findViewById<TextView>(R.id.btnPlay).setBackgroundColor(resources.getColor(R.color.purple_200))
        findViewById<TextView>(R.id.btnStopPlay).setBackgroundColor(resources.getColor(R.color.purple_200))

        // below method will stop
        // the audio recording.
        mRecorder!!.stop()

        // below method will release
        // the media recorder class.
        mRecorder!!.release()
        mRecorder = null
        findViewById<TextView>(R.id.idTVstatus).text = "Recording Stopped"
    }
    fun pausePlaying() {
        // this method will release the media player
        // class and pause the playing of our recorded audio.
        mPlayer!!.release()
        mPlayer = null
        findViewById<TextView>(R.id.btnStop).setBackgroundColor(resources.getColor(R.color.gray))
        findViewById<TextView>(R.id.btnRecord).setBackgroundColor(resources.getColor(R.color.purple_200))
        findViewById<TextView>(R.id.btnPlay).setBackgroundColor(resources.getColor(R.color.purple_200))
        findViewById<TextView>(R.id.btnStopPlay).setBackgroundColor(resources.getColor(R.color.gray))
        findViewById<TextView>(R.id.idTVstatus).text = "Recording Play Stopped"
    }


}