package wolfbeacon.hackalist


import org.springframework.transaction.annotation.Transactional
import java.util.*

@Transactional
interface HackalistHackathonService {
    fun getHackathonsBetweenDatesByCoordinates(startDate: Date, endDate: Date, sortBy: String?, count: Int?, latitude: Double?, longitude: Double?): List<HackalistHackathon>
}