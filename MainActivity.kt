// MainActivity.kt
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import android.content.SharedPreferences

class MainActivity : AppCompatActivity(), SensorEventListener {
    private lateinit var sensorManager: SensorManager
    private lateinit var stepSensor: Sensor
    private lateinit var stepCountText: TextView
    private lateinit var statusText: TextView
    private lateinit var prefs: SharedPreferences
    
    private var initialSteps = 0
    private var currentSteps = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        stepCountText = findViewById(R.id.stepCountText)
        statusText = findViewById(R.id.statusText)
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        prefs = getPreferences(MODE_PRIVATE)

        setupResetButton()
        initializeStepCounter()
    }

    private fun setupResetButton() {
        findViewById<Button>(R.id.resetButton).setOnClickListener {
            resetStepCount()
        }
    }

    private fun initializeStepCounter() {
        stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
        if (stepSensor == null) {
            statusText.text = "Step sensor not available!"
            return
        }

        initialSteps = prefs.getInt("initial_steps", 0)
        sensorManager.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_UI)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        event?.let {
            if (it.sensor.type == Sensor.TYPE_STEP_COUNTER) {
                val totalSteps = it.values[0].toInt()
                
                if (initialSteps == 0) {
                    initialSteps = totalSteps
                    prefs.edit().putInt("initial_steps", initialSteps).apply()
                }
                
                currentSteps = totalSteps - initialSteps
                stepCountText.text = currentSteps.toString()
            }
        }
    }

    private fun resetStepCount() {
        val currentTotal = initialSteps + currentSteps
        initialSteps = currentTotal
        currentSteps = 0
        prefs.edit().putInt("initial_steps", initialSteps).apply()
        stepCountText.text = "0"
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onResume() {
        super.onResume()
        stepSensor?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_UI)
        }
    }
}
