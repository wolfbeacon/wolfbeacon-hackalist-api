package wolfbeacon.hackalist

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import wolfbeacon.hackalist.util.CoordinateComparator
import java.util.*


@Component
@Transactional
class HackalistHackathonServiceImpl : HackalistHackathonService {

    @Autowired
    lateinit var hackalistHackathonRepository: HackalistHackathonRepository

    override fun getHackathonsBetweenDatesByCoordinates(startDate: Date, endDate: Date, sortBy: String?, count: Int?, latitude: Double?, longitude: Double?): List<HackalistHackathon> {

        val fullHackalistHackathonList = hackalistHackathonRepository.findAll()

        var queriedHackathons: MutableList<HackalistHackathon> = ArrayList()

        // Get Hackathons between dates
        for (currHackathon in fullHackalistHackathonList) {
            if (currHackathon.startDate.compareTo(startDate) >= 0 && currHackathon.endDate.compareTo(endDate) <= 0) {
                queriedHackathons.add(currHackathon)
            }
        }

        // Sort by date or distance
        if (sortBy == null || sortBy == "date" || sortBy != "distance") {

            // Sort in ascending order of Start Date
            queriedHackathons.sortBy { it.startDate }
        } else {
            if (latitude != null && longitude != null) {
                Collections.sort(queriedHackathons, CoordinateComparator(latitude, longitude))
            }
        }

        // Adjust count
        if (count != null) {
            queriedHackathons = queriedHackathons.subList(0, count)
        }

        return queriedHackathons
    }

}