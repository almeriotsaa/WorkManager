package com.example.workmanagerdemo1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.work.*
import com.example.workmanagerdemo1.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    companion object {
        const val KEY_COUNT_VALUE = "key_count"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.d("LIFECYCLE", "on create di panggil")

        binding.button.setOnClickListener {
            setOneTimeWorkRequest()
        }

    }

    override fun onStart() {
        super.onStart()
        Log.d("LIFECYCLE", "on start di panggil")
    }

    override fun onResume() {
        super.onResume()
        Log.d("LIFECYCLE", "on resume di panggil")
    }

    override fun onPause() {
        super.onPause()
        Log.d("LIFECYCLE", "on pause di panggil")
    }

    override fun onStop() {
        super.onStop()
        Log.d("LIFECYCLE", "on stop di panggil")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("LIFECYCLE", "on destroy di panggil")
    }

    private fun setOneTimeWorkRequest() {
        val workManager = WorkManager.getInstance(applicationContext)

        //constarint digunakan untuk syarat worker akan berjalan apabila
        //kondisi tersebut terpenuhi
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresCharging(true)
            .build()

        // membuat input ke dalam worker UploaderWorker
        val data = Data.Builder()
            .putInt(KEY_COUNT_VALUE, 125)
            .build()

        // membuat request worker
        val uploadRequest = OneTimeWorkRequest.Builder(UploadWorker::class.java)
            .setConstraints(constraints)
            .setInputData(data)
            .build()

        // mengirimkan request worker ke sistem
        workManager.enqueue(uploadRequest)

        // mendapatkan status workermanager
        workManager.getWorkInfoByIdLiveData(uploadRequest.id)
            .observe(this, {
                binding.status.text = it.state.name
                if (it.state.isFinished){
                    val date = it.outputData
                    val message = date.getString(UploadWorker.KEY_WORKER)
                    Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
                }
            })
    }
}

