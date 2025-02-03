data class Activity(
    val type: String,
    val duration: Int, // in minutes
    val distance: Double, // in kilometers
    val caloriesBurned: Int
)

class FitnessTracker {
    private val activities = mutableListOf<Activity>()
    
    fun addActivity(type: String, duration: Int, distance: Double, caloriesBurned: Int) {
        activities.add(Activity(type, duration, distance, caloriesBurned))
    }
    
    fun totalDuration(): Int = activities.sumOf { it.duration }
    
    fun totalDistance(): Double = activities.sumOf { it.distance }
    
    fun totalCaloriesBurned(): Int = activities.sumOf { it.caloriesBurned }
    
    fun displaySummary() {
        println("\nFitness Activity Summary")
        println("-----------------------")
        
        activities.forEachIndexed { index, activity ->
            println("Activity #${index + 1}:")
            println("Type: ${activity.type}")
            println("Duration: ${activity.duration} mins")
            println("Distance: ${activity.distance} km")
            println("Calories: ${activity.caloriesBurned} kcal")
            println()
        }
        
        println("Totals:")
        println("Total Duration: ${totalDuration()} mins")
        println("Total Distance: ${"%.2f".format(totalDistance())} km")
        println("Total Calories Burned: ${totalCaloriesBurned()} kcal")
    }
}

fun main() {
    val tracker = FitnessTracker()
    
    // Add sample activities
    tracker.addActivity("Running", 30, 5.0, 300)
    tracker.addActivity("Walking", 45, 3.0, 150)
    tracker.addActivity("Cycling", 60, 15.0, 450)
    
    // Display summary
    tracker.displaySummary()
}
