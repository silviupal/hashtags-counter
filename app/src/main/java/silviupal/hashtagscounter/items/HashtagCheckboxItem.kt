package silviupal.hashtagscounter.items

import android.view.View
import android.widget.CompoundButton
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.items.AbstractItem
import kotlinx.android.synthetic.main.hashtag_checkbox_item_layout.view.*
import silviupal.hashtagscounter.R
import silviupal.hashtagscounter.models.HashtagCheckboxModel

/**
 * Created by Silviu Pal on 4/13/2019.
 */

class HashtagCheckboxItem(var model: HashtagCheckboxModel) : AbstractItem<HashtagCheckboxItem, HashtagCheckboxItem.ViewHolder>() {
    override fun getType(): Int = R.id.hashtag_checkbox_item_id

    override fun getViewHolder(v: View): ViewHolder = ViewHolder(v)

    override fun getLayoutRes(): Int = R.layout.hashtag_checkbox_item_layout

    class ViewHolder(itemView: View) : FastAdapter.ViewHolder<HashtagCheckboxItem>(itemView) {
        override fun bindView(item: HashtagCheckboxItem, payloads: MutableList<Any>) {
            itemView.checkbox.text = item.model.name
            itemView.checkbox.isChecked = item.model.isChecked
            itemView.checkbox.setOnCheckedChangeListener { buttonView, isChecked ->
                item.model.isChecked = isChecked
            }
        }

        override fun unbindView(item: HashtagCheckboxItem) {

        }
    }
}