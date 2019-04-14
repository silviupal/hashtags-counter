package silviupal.hashtagscounter.items

import android.view.View
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.items.AbstractItem
import kotlinx.android.synthetic.main.hashtag_item_layout.view.*
import silviupal.hashtagscounter.R
import silviupal.hashtagscounter.database.entities.HashtagEntity

/**
 * Created by Silviu Pal on 4/12/2019.
 */
class HashtagItem(var itemEntity: HashtagEntity) : AbstractItem<HashtagItem, HashtagItem.ViewHolder>() {
    override fun getType(): Int = R.id.hashtag_item_id

    override fun getViewHolder(v: View): ViewHolder = ViewHolder(v)

    override fun getLayoutRes(): Int = R.layout.hashtag_item_layout

    class ViewHolder(itemView: View) : FastAdapter.ViewHolder<HashtagItem>(itemView) {
        override fun bindView(item: HashtagItem, payloads: MutableList<Any>) {
            itemView.hashtagName.text = item.itemEntity.name
        }

        override fun unbindView(item: HashtagItem) {
        }
    }
}