package silviupal.hashtagscounter.presentation.adapterItems

import android.view.View
import androidx.core.content.ContextCompat
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.items.AbstractItem
import kotlinx.android.synthetic.main.post_item_layout.view.*
import silviupal.hashtagscounter.R
import silviupal.hashtagscounter.data.database.entities.PostEntity
import silviupal.hashtagscounter.common.utils.ColorUtils

/**
 * Created by Silviu Pal on 4/6/2019.
 */
class PostItem(var itemEntity: PostEntity) : AbstractItem<PostItem, PostItem.ViewHolder>() {
    override fun getType(): Int = R.id.post_item_id

    override fun getViewHolder(v: View): ViewHolder = ViewHolder(
        v)

    override fun getLayoutRes(): Int = R.layout.post_item_layout

    class ViewHolder(itemView: View) : FastAdapter.ViewHolder<PostItem>(itemView) {
        override fun bindView(item: PostItem, payloads: MutableList<Any>) {
            itemView.postTitle.text = item.itemEntity.title
            itemView.postText.text = item.itemEntity.text
            itemView.postHashtagsCounter.text = String.format(itemView.context.getString(R.string.hashtag_sign_format),
                item.itemEntity.hashtagsCount)
            itemView.postHashtagsCounter.setTextColor(ContextCompat.getColor(itemView.context,
                ColorUtils.getTextColor(item.itemEntity.hashtagsCount)))
        }

        override fun unbindView(item: PostItem) {

        }
    }
}