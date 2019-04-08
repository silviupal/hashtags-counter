package silviupal.hashtagscounter.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

/**
 * Created by Silviu Pal on 4/8/2019.
 */
@Entity
data class ListItemEntity(
    @PrimaryKey
    var id: Int = 0,
    var title: String = "",
    var text: String = "",
    var hashtagsCount: Int = 0,
    var charsCount: Int = 0,
    var lastUpdated: String)