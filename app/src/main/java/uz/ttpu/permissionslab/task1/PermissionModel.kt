package uz.ttpu.permissionslab.task1

/**
 * Task 1: "Denied twice" permission state machine model.
 */
class PermissionModel {
    var granted: Boolean = false
        private set
    var rationale: Boolean = false
        private set
    var dialogShows: Boolean = true
        private set

    private var denyCount = 0

    fun request(userAllows: Boolean) {
        if (!dialogShows) {
            println("   -> dialog yo'q, darhol rad")
            return
        }

        if (granted) {
            println("   -> Ruxsat allaqachon berilgan")
            return
        }

        if (userAllows) {
            granted = true
            rationale = false
            dialogShows = false
            println("   -> Foydalanuvchi RUXSAT berdi")
        } else {
            denyCount++
            granted = false
            if (denyCount == 1) {
                rationale = true
                dialogShows = true
                println("   -> Foydalanuvchi 1-marta RAD etdi")
            } else {
                rationale = false
                dialogShows = false
                println("   -> Foydalanuvchi 2-marta RAD etdi")
            }
        }
    }

    fun printState(step: String) {
        println("[$step] -> granted=$granted, rationale=$rationale, dialogShows=$dialogShows")
    }
}

fun main() {
    val model = PermissionModel()
    println("=== Task 1: Permission Model ('Denied Twice' Simulation) ===")
    model.printState("Boshlang'ich holat")

    println("\n1. Birinchi so'rov (Foydalanuvchi rad etadi):")
    model.request(userAllows = false)
    model.printState("1-qadam")

    println("\n2. Ikkinchi so'rov (Foydalanuvchi yana rad etadi):")
    model.request(userAllows = false)
    model.printState("2-qadam")

    println("\n3. Uchinchi so'rov (Tizim muloqot oynasini ko'rsatishga urinadi):")
    model.request(userAllows = false)
    model.printState("3-qadam")

    println("\n4. To'rtinchi so'rov (Qayta urunish):")
    model.request(userAllows = true)
    model.printState("4-qadam")
}
