package api.person.absence.availability

enum class HourStatus(val order: Int) {
    Open(0),
    Requested(1),
    Approved(2)
}