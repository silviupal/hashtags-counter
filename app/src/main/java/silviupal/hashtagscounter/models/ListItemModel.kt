package silviupal.hashtagscounter.models

import android.view.View
import androidx.core.content.ContextCompat
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.items.AbstractItem
import kotlinx.android.synthetic.main.list_item_layout.view.*
import silviupal.hashtagscounter.R
import silviupal.hashtagscounter.utils.ColorUtils
import silviupal.hashtagscounter.utils.StringUtils

/**
 * Created by Silviu Pal on 4/6/2019.
 */
data class ListItemModel(var id: Int = 0,
    var title: String = "",
    var text: String = "",
    var hashtagsCount: Int = 0,
    var charsCount: Int = 0) : AbstractItem<ListItemModel, ListItemModel.ViewHolder>() {
    override fun getType(): Int = R.id.list_item_id

    override fun getViewHolder(v: View): ViewHolder = ViewHolder(v)

    override fun getLayoutRes(): Int = R.layout.list_item_layout

    class ViewHolder(itemView: View) : FastAdapter.ViewHolder<ListItemModel>(itemView) {
        override fun bindView(item: ListItemModel, payloads: MutableList<Any>) {
            itemView.title.text = item.title
            itemView.text.text = item.text
            itemView.hashtagsCounterView.text = StringUtils.getHashtagsCounterText(item.hashtagsCount, itemView.context)
            itemView.hashtagsCounterView.setTextColor(ContextCompat.getColor(itemView.context,
                ColorUtils.getTextColor(item.hashtagsCount)))
            itemView.inputLengthView.text = StringUtils.getCharsCountText(item.charsCount, itemView.context)
        }

        override fun unbindView(item: ListItemModel) {

        }
    }
}