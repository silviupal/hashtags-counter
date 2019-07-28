package silviupal.hashtagscounter.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by Silviu Pal on 4/12/2019.
 */
@Entity
data class HashtagEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var name: String = "")