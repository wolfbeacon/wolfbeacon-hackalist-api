package wolfbeacon.hackalist

import com.fasterxml.jackson.annotation.JsonFormat
import org.jetbrains.annotations.NotNull
import java.util.*
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

/***
 *
 * A data class automatically generates equals(), hashcode(), toString() and copy() methods
 *
 * All fields have been assigned a default varue because Hibernate requires an entity to have a no-arg constructor.
 * Hence, Assigning default varues to all the member fields will let hibernate instantiate an Article
 *
 * Using var(variable) instead of val(final) as data maybe updated
 */

@Entity
@Table(name = "hackalist_hackathon")
data class HackalistHackathon(
        @Id
        @NotNull
        var id: Long = 0,

        @NotNull
        var title: String = "",

        @NotNull
        var eventLink: String = "",

        @NotNull
        @JsonFormat(pattern = "yyyy-MM-dd")
        var startDate: Date = Date(),

        @NotNull
        @JsonFormat(pattern = "yyyy-MM-dd")
        var endDate: Date = Date(),

        @NotNull
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
        var lastUpdatedDate: Date = Date(),

        @NotNull
        var year: Int = 0,

        @NotNull
        var location: String = "",

        @NotNull
        var host: String = "",

        var length: Int? = 0,

        var size: String? = "",

        var travel: Boolean? = false,

        var prize: Boolean? = false,

        var highSchoolers: Boolean? = false,

        var cost: String? = "",

        var facebookLink: String? = "",

        var twitterLink: String? = "",

        var googlePlusLink: String? = "",

        var imageLink: String? = "",

        var latitude: Double? = 0.0,

        var longitude: Double? = 0.0,

        var notes: String? = ""

)